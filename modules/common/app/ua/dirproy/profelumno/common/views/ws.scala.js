//this function can remove a array element.
Array.remove = function(array, from, to) {
    var rest = array.slice((to || from) + 1 || array.length);
    array.length = from < 0 ? array.length + from : from;
    return array.push.apply(array, rest);
};

//this variable represents the total number of popups can be displayed according to the viewport width
var total_popups = 0;

//arrays of popups ids
var popups = [];

//this variable is to avoid display_open_chats execute more than once when a page is load.
var timesChatControllerExecute = 0;

//this variable try to solved the problem of angular execute sequence, only for the display_open_chats
var currentUserChatIndex = 0;

//this is used to close a popup
function close_popup(chatData)
{
    //Code of Session Storage
    var chatIdAndUserSessionId = chatData.split("-");
    var chatID = chatIdAndUserSessionId[0];
    var userInSessionId = chatIdAndUserSessionId[1];
    var chatToId = chatIdAndUserSessionId[2];

    var savedChat = JSON.parse(sessionStorage.getItem(userInSessionId));

    var i = 0;
    var indexToDelete = 0;
    for(i; i < savedChat.length; i++){
        if(savedChat[i].savedChatToId == chatToId){
            indexToDelete = i;
        }
    }
    savedChat.splice(indexToDelete,1);

    sessionStorage.removeItem(userInSessionId);

    if(savedChat.length > 0){
        var chatToSave = JSON.stringify(savedChat);
        sessionStorage.setItem(userInSessionId, chatToSave);
    }
    //End of session storage code

    for(var iii = 0; iii < popups.length; iii++)
    {
        if(chatID == popups[iii])
        {
            Array.remove(popups, iii);

            document.getElementById(chatID).style.display = "none";

            calculate_popups();

            return;
        }
    }
}
//This function manage all the code for displaying the chats open when the user changes the windows.
function display_open_chats(userIdSession){
    
    if(timesChatControllerExecute == 0){
        var objLoaded = JSON.parse(sessionStorage.getItem(userIdSession));
        if(objLoaded != null){
            var i = 0;
            for(i ; i < objLoaded.length; i++){
                currentUserChatIndex = i;
                var loadChatToId = objLoaded[i].savedChatToId;
                var $http = angular.element($("#chatCtrl")).injector().get('$http');
                $http.get('../common/getChat?userId='+loadChatToId)
                    .success(function (data, status, headers, config) {
                        var loadedChat = data.chat;
                        loadChatToId = objLoaded[currentUserChatIndex].savedChatToId;
                        var loadName = objLoaded[currentUserChatIndex].savedName;
                        initialize_open_chats(loadedChat.id, userIdSession,loadChatToId,loadName);
                        var messages = loadedChat.messages;
                        var ii = 0;
                        for(ii ; ii < messages.length; ii++){
                            var message=messages[ii];
                            if (message.author.id==userIdSession){
                                angular.element('#socket-messages'+loadedChat.id).append('<div class="direct-chat-msg right">\
                            <div class="direct-chat-info clearfix">\
                                <span class="direct-chat-name pull-right">'+ message.author.name+" "+ message.author.surname+'</span>\
                                <span class="direct-chat-timestamp pull-left">' + message.date + '</span>\
                            </div>\
                            <div class="direct-chat-text">\
                                '+message.msg+'\
                            </div></div>');
                            }else{
                                angular.element('#socket-messages'+loadedChat.id).append('<div class="direct-chat-msg">\
                            <div class="direct-chat-info clearfix">\
                                <span class="direct-chat-name pull-left">'+ message.author.name+" "+ message.author.surname+'</span>\
                                <span class="direct-chat-timestamp pull-right">'+message.date+'</span>\
                            </div>\
                            <div class="direct-chat-text">\
                                '+message.msg+'\
                            </div></div>');
                            }
                            updateScroll();
                        }
                        currentUserChatIndex --;
                    }).
                    error(function () {
                    });
            }
        }
        timesChatControllerExecute ++;
    }
    else timesChatControllerExecute = 0;
}
//This function help the display_open_chats function.
function initialize_open_chats(chatID, userInSessionID,chatToID, userName){
    for(var iii = 0; iii < popups.length; iii++)
    {
        //already registered. Bring it to front.
        if(chatID == popups[iii])
        {
            Array.remove(popups, iii);

            popups.unshift(chatID);

            calculate_popups();


            return;
        }
    }
    var chatIdAndUserInSessionId = chatID + "-" + userInSessionID + "-" + chatToID;
    var element2='<div class="box box-danger direct-chat direct-chat-danger" >\
        <div class="box-header with-border">\
            <h3 class="box-title">'+userName+'</h3>\
            <div class="box-tools pull-right">\
                <button class="btn btn-box-tool"><a href="javascript:close_popup(\''+ chatIdAndUserInSessionId +'\');">&#10005;</a></button>\
            </div>\
        </div>\
        <div class="box-body">\
            <div class="direct-chat-messages" id="socket-messages'+chatID+'" name="chat-messages">\
            </div>\
        </div>\
        <div class="box-footer">\
            <div class="input-group">\
                <input type="text" name="message" placeholder="Escribir Mensaje ..." class="form-control" id="socket-input'+chatID+'">\
                <span class="input-group-btn">\
                    <button type="button" href="#" class="btn btn-danger btn-flat" onclick="prueba('+chatID+')">Enviar</button>\
                </span>\
            </div>\
        </div>\
    </div>';
    var element = '<div class="popup-box chat-popup" id="'+ chatID +'">';
    element+=element2;
    document.getElementById("idunico").innerHTML = document.getElementById("idunico").innerHTML + element;

    popups.unshift(chatID);
    calculate_popups();
}

//displays the popups. Displays based on the maximum number of popups that can be displayed on the current viewport width
function display_popups()
{
    var right = 220;
    var iii = 0;
    for(iii; iii < total_popups; iii++)
    {
        if(popups[iii] != undefined)
        {
            var element = document.getElementById(popups[iii]);
            element.style.right = right + "px";
            right = right + 320;
            element.style.display = "block";
        }
    }

    for(var jjj = iii; jjj < popups.length; jjj++)
    {
        var element = document.getElementById(popups[jjj]);
        element.style.display = "none";
    }
}
//creates markup for a new popup. Adds the id to popups array. Also saved an object for session storage
function register_popup(id,name,chatID,userInSessionID)
{
    //Code of Session Storage
    var chatToSave;
    //If there are no chats save.
    if(sessionStorage.getItem(userInSessionID) == null){
        chatToSave = [{
            savedName: name,
            savedChatToId: id
        }];
    }
    else{
        chatToSave = JSON.parse(sessionStorage.getItem(userInSessionID));
        chatToSave.push({
            savedName: name,
            savedChatToId: id
        });
    }
    var chatsToSaveJson = JSON.stringify(chatToSave);
    sessionStorage.setItem(userInSessionID, chatsToSaveJson);
    //End of Session Storage code.

    for(var iii = 0; iii < popups.length; iii++)
    {
        //already registered. Bring it to front.
        if(chatID == popups[iii])
        {
            Array.remove(popups, iii);

            popups.unshift(chatID);

            calculate_popups();


            return;
        }
    }
    var chatIdAndUserInSessionId = chatID + "-" + userInSessionID + "-" + id;
    var element2='<div class="box box-danger direct-chat direct-chat-danger" >\
        <div class="box-header with-border">\
            <h3 class="box-title">'+name+'</h3>\
            <div class="box-tools pull-right">\
                <button class="btn btn-box-tool"><a href="javascript:close_popup(\''+ chatIdAndUserInSessionId +'\');">&#10005;</a></button>\
            </div>\
        </div>\
        <div class="box-body">\
            <div class="direct-chat-messages" id="socket-messages'+chatID+'" name="chat-messages">\
            </div>\
        </div>\
        <div class="box-footer">\
            <div class="input-group">\
                <input type="text" name="message" placeholder="Escribir Mensaje ..." class="form-control" id="socket-input'+chatID+'">\
                <span class="input-group-btn">\
                    <button type="button" href="#" class="btn btn-danger btn-flat" onclick="prueba('+chatID+')">Enviar</button>\
                </span>\
            </div>\
        </div>\
    </div>';
    var element = '<div class="popup-box chat-popup" id="'+ chatID +'">';
    element+=element2;
    document.getElementById("idunico").innerHTML = document.getElementById("idunico").innerHTML + element;

    popups.unshift(chatID);

    calculate_popups();

}

//calculate the total number of popups suitable and then populate the toatal_popups variable.
function calculate_popups()
{
    //var width = window.innerWidth;
    //if(width < 540)
    //{
    //    total_popups = 0;
    //}
    //else
    //{
    //    width = width - 200;
    //    //320 is width of a single popup box
    //    total_popups = parseInt(width/320);
    //}
    total_popups=2;
    display_popups();

}

function updateScroll(){
    var element = document.getElementsByName("chat-messages");
    if (element!=null)
        element[0].scrollTop = element[0].scrollHeight;
}

//recalculate when window is loaded and also when window is resized.
window.addEventListener("resize", calculate_popups);
window.addEventListener("load", calculate_popups);
// get websocket class, firefox has a different way to get it
var WS = window['MozWebSocket'] ? window['MozWebSocket'] : WebSocket;
// open pewpew with websocket
var socket = new WS('@ua.dirproy.profelumno.common.controllers.routes.ChatController.wsInterface().webSocketURL()');


$(function() {
    angular.bootstrap(document.getElementById("chat-bar"),['chat']);
});

function prueba(cid){
    angular.element('#chatCtrl').scope().submitMessage(cid);

}

angular.module('chat', [])
    .controller('ChatController', ['$scope', '$http', function ($scope, $http) {
        var userInSession;
        $scope.connectedUser=[];
        $scope.disconnectedUser=[];

        $http.get('../common/userInSession')
            .success(function (data, status, headers, config) {
                userInSession=data;
                display_open_chats(userInSession.id);
            }).
            error(function (data, status, headers, config) {
                // log error
            });
        // var socket = new WebSocket("ws://localhost:9000/common/getSocket");

        function writeMessages(event){
            event = JSON.parse(event.data);
            if (event.type=="msg"){
                //agregar mensaje al chat
                //event.idChat
                //event.mesage
                var idChat=event.idChat;
                var message=event.message;
                console.log(idChat);
                console.log(message);
                var open=false;
                for(var iii = 0; iii < popups.length; iii++)
                {
                    if(idChat == popups[iii])
                    {
                        open=true;
                    }
                }if(!open){
                    $scope.getChat(message.author.id,message.author.name+" "+message.author.surname)
                }

                if (message.author.id==userInSession.id){
                    angular.element('#socket-messages'+idChat).append('<div class="direct-chat-msg right">\
                            <div class="direct-chat-info clearfix">\
                                <span class="direct-chat-name pull-right">'+ message.author.name+" "+ message.author.surname+'</span>\
                                <span class="direct-chat-timestamp pull-left">'+message.date+'</span>\
                            </div>\
                            <div class="direct-chat-text">\
                                '+message.msg+'\
                            </div></div>');
                }else{
                    angular.element('#socket-messages'+idChat).append('<div class="direct-chat-msg">\
                            <div class="direct-chat-info clearfix">\
                                <span class="direct-chat-name pull-left">'+ message.author.name+" "+ message.author.surname+'</span>\
                                <span class="direct-chat-timestamp pull-right">'+message.date+'</span>\
                            </div>\
                            <div class="direct-chat-text">\
                                '+message.msg+'\
                            </div></div>');
                }
                updateScroll();
                angular.element($('#socket-input'+idChat)).val('');
            }else if (event.type=="user"){
                //conecta o desconecta un usuario de mis contactos
                //event.user User
                //event.connected  boolean true connected false disconnected
                user=event.user;
                var aux=[];
                if (event.connected){
                    for(var j=0; j<$scope.disconnectedUser.length;j++){
                        if ($scope.disconnectedUser[j].id!=user.id){
                            aux.push($scope.disconnectedUser[j]);
                        }
                    }
                    $scope.$apply(function(){
                        $scope.disconnectedUser=aux;
                        var exists=false;
                        for(j=0; j<$scope.connectedUser.length;j++){
                            if ($scope.connectedUser[j].id==user.id){
                                exists=true;
                            }
                        }
                        if (!exists) {
                            $scope.connectedUser.push(user);
                        } else{
                            $scope.connectedUser=$scope.connectedUser;
                        }
                    });
                }else{
                    for(j=0; j<$scope.connectedUser.length;j++){
                        if ($scope.connectedUser[j].id!=user.id){
                            aux.push($scope.connectedUser[j]);
                        }
                    }
                    $scope.$apply(function(){
                        $scope.connectedUser=aux;
                        var exists=false;
                        for(var j=0; j<$scope.disconnectedUser.length;j++){
                            if ($scope.disconnectedUser[j].id==user.id){
                                exists=true;
                            }
                        }
                        if (!exists) {
                            $scope.disconnectedUser.push(user);
                        } else{
                            $scope.disconnectedUser=$scope.disconnectedUser;
                        }
                    });
                }
            } else if (event.type=="users"){
                //lista de personas conectadas y desconectadas
                //event.connectedUsers
                $scope.$apply(function(){
                    $scope.connectedUser=event.connectedUsers;
                    //event.disconnectedUsers
                    $scope.disconnectedUser=event.disconnectedUsers;
                });

            }

        }

        socket.onmessage = writeMessages;

        //Ask for connected and disconnected contacts
        socket.onopen = function () {
            console.log("onopen");
            socket.send(JSON.stringify({type: "connections"}));
        };


        $scope.registerPop = function(id, name){
            register_popup(id, name);
        };
        $scope.submitMessage=function(chatId){
            var message = angular.element($('#socket-input'+chatId)).val();
            if (message != '') {
                socket.send(JSON.stringify({idUserFrom: userInSession.id, message: message, idChat: chatId}));
            }
        };

        $scope.getChat = function (chatToId, name) {
            $http.get('../common/getChat?userId='+chatToId)
                .success(function (data, status, headers, config) {
                    //data.chat  Chat
                    var chat = data.chat;
                    register_popup(chatToId, name,chat.id, userInSession.id);
                    angular.element('#socket-messages'+chat.id).empty();

                    for (var i =0; i < chat.messages.length; i++){
                        var message=chat.messages[i];
                        if (message.author.id==userInSession.id){
                            angular.element('#socket-messages'+chat.id).append('<div class="direct-chat-msg right">\
                            <div class="direct-chat-info clearfix">\
                                <span class="direct-chat-name pull-right">'+ message.author.name+" "+ message.author.surname+'</span>\
                                <span class="direct-chat-timestamp pull-left">' + message.date + '</span>\
                            </div>\
                            <div class="direct-chat-text">\
                                '+message.msg+'\
                            </div></div>');
                        }else{
                            angular.element('#socket-messages'+chat.id).append('<div class="direct-chat-msg">\
                            <div class="direct-chat-info clearfix">\
                                <span class="direct-chat-name pull-left">'+ message.author.name+" "+ message.author.surname+'</span>\
                                <span class="direct-chat-timestamp pull-right">'+message.date+'</span>\
                            </div>\
                            <div class="direct-chat-text">\
                                '+message.msg+'\
                            </div></div>');
                        }
                        updateScroll();
                    }
                    //data.chat.messeges [Messeges] En indice 0 esta el mas viejo
                }).
                error(function (data, status, headers, config) {
                    // log error
                    alert("error");
                });
        };
    }]);





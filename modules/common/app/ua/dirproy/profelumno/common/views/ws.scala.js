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

//this is used to close a popup
function close_popup(id)
{
    for(var iii = 0; iii < popups.length; iii++)
    {
        if(id == popups[iii])
        {
            Array.remove(popups, iii);

            document.getElementById(id).style.display = "none";

            calculate_popups();

            return;
        }
    }
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

//creates markup for a new popup. Adds the id to popups array.
function register_popup(id, name)
{
    for(var iii = 0; iii < popups.length; iii++)
    {
        //already registered. Bring it to front.
        if(id == popups[iii])
        {
            Array.remove(popups, iii);

            popups.unshift(id);

            calculate_popups();


            return;
        }
    }
    var element2= '<div class="box box-danger direct-chat direct-chat-danger" ng-app="chatController" ng-controller="ChatController as controller">\
        <div class="box-header with-border">\
            <h3 class="box-title">'+name+'</h3>\
            <div class="box-tools pull-right">\
                <button class="btn btn-box-tool"><a href="javascript:close_popup(\''+ id +'\');">&#10005;</a></button>\
            </div>\
        </div>\
        <div class="box-body">\
            <div class="direct-chat-messages" id="socket-messages'+id+'" name="chat-messages">\
                <div class="direct-chat-msg right">\
                    <div class="direct-chat-info clearfix">\
                       <span class="direct-chat-name pull-right">nico rudolpg</span>\
                       <span class="direct-chat-timestamp pull-left"> ahashjbda </span>\
                    </div>\
                    <div class="direct-chat-text">\
                        hola\
                    </div>\
                </div>\
                <div class="direct-chat-msg right">\
                    <div class="direct-chat-info clearfix">\
                       <span class="direct-chat-name pull-right">nico rudolpg</span>\
                       <span class="direct-chat-timestamp pull-left"> ahashjbda </span>\
                    </div>\
                    <div class="direct-chat-text">\
                        hola\
                    </div>\
                </div>\
                <div class="direct-chat-msg right">\
                    <div class="direct-chat-info clearfix">\
                       <span class="direct-chat-name pull-right">nico rudolpg</span>\
                       <span class="direct-chat-timestamp pull-left"> ahashjbda </span>\
                    </div>\
                    <div class="direct-chat-text">\
                        hola\
                    </div>\
                </div>\
                <div class="direct-chat-msg right">\
                    <div class="direct-chat-info clearfix">\
                       <span class="direct-chat-name pull-right">nico rudolpg</span>\
                       <span class="direct-chat-timestamp pull-left"> ahashjbda </span>\
                    </div>\
                    <div class="direct-chat-text">\
                        hola\
                    </div>\
                </div>\
                <div class="direct-chat-msg right">\
                    <div class="direct-chat-info clearfix">\
                       <span class="direct-chat-name pull-right">nico rudolpg</span>\
                       <span class="direct-chat-timestamp pull-left"> ahashjbda </span>\
                    </div>\
                    <div class="direct-chat-text">\
                        hola\
                    </div>\
                </div>\
                <div class="direct-chat-msg right">\
                    <div class="direct-chat-info clearfix">\
                       <span class="direct-chat-name pull-right">nico rudolpg</span>\
                       <span class="direct-chat-timestamp pull-left"> ahashjbda </span>\
                    </div>\
                    <div class="direct-chat-text">\
                        hola\
                    </div>\
                </div>\
            </div>\
        </div>\
        <div class="box-footer">\
            <div class="input-group">\
                <input type="text" name="message" placeholder="Type Message ..." class="form-control" id="socket-input">\
                <span class="input-group-btn">\
                    <button type="button" class="btn btn-danger btn-flat" onclick="$scope.submitMessage('+id+')">Send</button>\
                </span>\
            </div>\
        </div>\
    </div>';
    var element = '<div class="popup-box chat-popup" id="'+ id +'">';
    element+=element2;
    document.getElementById("idunico").innerHTML = document.getElementById("idunico").innerHTML + element;

    popups.unshift(id);

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
angular.module('chat', [])
    .controller('ChatController', ['$scope', '$http', function ($scope, $http) {
        var userInSession;
        var person = {id:123, name:"John", surname:"Doe"};
        var person2 = {id:111, name:"Nico", surname:"Doe"};
        var person3 = {id:112, name:"Sebastian", surname:"Doe"};
        $scope.connectedUser=[person, person2];
        $scope.disconnectedUser=[person3];

        $http.get('common/userInSession')
            .success(function (data, status, headers, config) {
                userInSession=data;
            }).
            error(function (data, status, headers, config) {
                // log error
            });

        function writeMessages (event){
            if (event.type=="msg"){
                //agregar mensaje al chat
                //event.idChat
                //event.mesage
                var idChat=event.idChat;
                var message=event.message;
                if (message.author.id==userInSession.id){
                    angular.element('#socket-messages'+idChat).apend('<div class="direct-chat-msg right">\
                            <div class="direct-chat-info clearfix">\
                                <span class="direct-chat-name pull-right">'+ message.author.name+" "+ message.author.surname+'</span>\
                                <span class="direct-chat-timestamp pull-left">'+message.date+'</span>\
                            </div>\
                            <div class="direct-chat-text">\
                                '+message.msg+'\
                            </div></div>');
                }else{
                    angular.element('#socket-messages'+idChat).apend('<div class="direct-chat-msg">\
                            <div class="direct-chat-info clearfix">\
                                <span class="direct-chat-name pull-left">'+ message.author.name+" "+ message.author.surname+'</span>\
                                <span class="direct-chat-timestamp pull-right">'+message.date+'</span>\
                            </div>\
                            <div class="direct-chat-text">\
                                '+message.msg+'\
                            </div></div>');
                }
                updateScroll();
            }else if (event.type=="user"){
                //conecta o desconecta un usuario de mis contactos
                //event.user User
                //event.connected  boolean true connected false disconnected
                user=event.user;
                var aux=[];
                if (event.connected){
                    for(var j=0; j<disconnectedUser.length;j++){
                        if (disconnectedUser[j]!=user){
                            aux.push(disconnectedUser[j]);
                        }
                    }
                    disconnectedUser=aux;
                    connectedUser.push(user);
                }else{
                    for(j=0; j<connectedUser.length;j++){
                        if (connectedUser[j]!=user){
                            aux.push(connectedUser[j]);
                        }
                    }
                    connectedUser=aux;
                    disconnectedUser.push(user);
                }
            } else if (event.type=="users"){
                //lista de personas conectadas y desconectadas
                //event.connectedUsers
                connectedUser=event.connectedUsers;
                //event.disconnectedUsers
                disconnectedUser=event.disconnectedUsers;
            }

        };

        socket.onmessage = writeMessages;

        //Ask for connected and disconnected contacts
        socket.onopen = function () { socket.send({type: "connections"});};

        // if enter (charcode 13) is pushed, send message, then clear input field
        angular.element('#socket-input').keyup(function(event){
            var charCode = (event.which) ? event.which : event.keyCode ;

            if(charCode === 13){
                socket.send({idUserFrom: userInSession.id, message: $(this).val(), idChat: long});
                $(this).val('');
            }
        });
        $scope.registerPop = function(id, name){
            register_popup(id, name);
        };
        $scope.submitMessage= function(chatId){
            socket.send({idUserFrom: userInSession.id, message: $(this).val(), idChat: chatId});
        };

        $scope.getChat = function (chatToId, name) {
            $http.get('common/getChat?userId='+chatToId)
                .success(function (data, status, headers, config) {
                    //data.chat  Chat
                    register_popup(chatToId, name);
                    var chat = data.chat;
                    alert(chat);
                    for (var i =0; i < chat.messages.length; i++){
                        var message=chat.messages[i];
                        if (message.author.id==userInSession.id){
                            angular.element('#socket-messages'+idChat).apend('<div class="direct-chat-msg right">\
                            <div class="direct-chat-info clearfix">\
                                <span class="direct-chat-name pull-right">'+ message.author.name+" "+ message.author.surname+'</span>\
                                <span class="direct-chat-timestamp pull-left">message.date</span>\
                            </div>\
                            <div class="direct-chat-text">\
                                '+message.msg+'\
                            </div></div>');
                        }else{
                            angular.element('#socket-messages'+idChat).apend('<div class="direct-chat-msg">\
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





$(function() {
    angular.bootstrap(document.getElementById("chat-bar"),['app']);
});
angular.module('app', [])
    .controller('ChatController', ['$scope', '$http', function ($scope, $http) {
        var userInSession;
        $scope.connectedUsers=[];
        $scope.disconnectedUsers=[];

        $http.get('common/userInSession')
            .success(function (data, status, headers, config) {
               userInSession=data.id;
            }).
            error(function (data, status, headers, config) {
                // log error
            });

        // get websocket class, firefox has a different way to get it
        $scope.WS = window['MozWebSocket'] ? window['MozWebSocket'] : WebSocket;

        // open pewpew with websocket
        var socket = new WS('@ua.dirproy.profelumno.common.controllers.ChatController.wsInterface()');


        $scope.writeMessages = function(event){
            if (event.type=="msg"){
                //agregar mensaje al chat
                //event.idChat
                //event.mesage
                var idChat=event.idChat;
                var message=event.message;
                if (message.author.id==userInSession){
                    angular.element('#socket-messages'+event.idChat).apend('<p>'+event.message+'</p>');
                }else{
                    angular.element('#socket-messages'+idChat).apend('<p>'+event.message+'</p>');
                }
            }else if (event.type=="user"){
                //conecta o desconecta un usuario de mis contactos
                //event.user User
                //event.connected  boolean true connected false disconnected
                user=event.user;
                var aux=[];
                if (event.connected){
                    for(var j=0; j<disconnectedUsers.length;j++){
                        if (disconnectedUsers[j]!=user){
                            aux.push(disconnectedUsers[j]);
                        }
                    }
                    disconnectedUsers=aux;
                    connectedUsers.push(user);
                }else{
                    for(var j=0; j<connectedUsers.length;j++){
                        if (connectedUsers[j]!=user){
                            aux.push(connectedUsers[j]);
                        }
                    }
                    connectedUsers=aux;
                    disconnectedUsers.push(user);
                }
            } else if (event.type=="users"){
                //lista de personas conectadas y desconectadas
                //event.connectedUsers
                $scope.connectedUsers=event.connectedUsers;
                //event.disconnectedUsers
                $scope.disconnectedUsers=event.disconnectedUsers;
            }

        };

        $scope.socket.onmessage = writeMessages;

        // if enter (charcode 13) is pushed, send message, then clear input field
        angular.element('#socket-input').keyup(function(event){
            var charCode = (event.which) ? event.which : event.keyCode ;

            if(charCode === 13){
                socket.send($(this).val());
                //{idUserFrom: long, message: String, idChat: long}
                $(this).val('');
            }
        });

        $scope.getChat = function (chatToId) {
            $http.get('chat/getChat?userId='+chatToId)
                .success(function (data, status, headers, config) {
                    //data.chat  Chat
                    var chat = data.chat;
                    for (var i =0; i < chat.messeges.length; i++){
                        var message=chat.messeges[i];
                        if (messege.author.user.id==userInSession){

                        }else{

                        }
                    }
                    //data.chat.messeges [Messeges] En indice 0 esta el mas viejo
                }).
                error(function (data, status, headers, config) {
                    // log error
                });
        };
    }]);



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
    var element2= '<div class="box box-danger direct-chat direct-chat-danger" ng-controller="ChatController as controller">\
        <div class="box-header with-border">\
    <h3 class="box-title">name</h3>\
    <div class="box-tools pull-right">\
    <span data-toggle="tooltip" title="3 New Messages" class="badge bg-red">3</span>\
<button class="btn btn-box-tool" data-widget="collapse" data-toggle="tooltip" title="Collapse"><i class="fa fa-minus"></i></button>\
    <button class="btn btn-box-tool"><a href="javascript:close_popup(\''+ id +'\');">&#10005;</a></button>\
    </div>\
    </div>\
<div class="box-body">\
    <div class="direct-chat-messages">\
        <div class="direct-chat-msg">\
            <div class="direct-chat-info clearfix">\
                <span class="direct-chat-name pull-left">Alexander Pierce</span>\
                <span class="direct-chat-timestamp pull-right">23 Jan 2:00 pm</span>\
            </div>\
            <img class="direct-chat-img" src="../dist/img/user1-128x128.jpg" alt="message user image">\
                <div class="direct-chat-text">\
                    Is this template really for free? Thats unbelievable!\
                </div>\
            </div>\
            <div class="direct-chat-msg right">\
                <div class="direct-chat-info clearfix">\
                    <span class="direct-chat-name pull-right">Sarah Bullock</span>\
                    <span class="direct-chat-timestamp pull-left">23 Jan 2:05 pm</span>\
                </div>\
                <img class="direct-chat-img" src="../dist/img/user3-128x128.jpg" alt="message user image">\
                    <div class="direct-chat-text">\
                        You better believe it!\
                    </div>\
                </div>\
            </div>\
            <div class="direct-chat-contacts">\
                <ul class="contacts-list">\
                    <li>\
                        <a href="#">\
                            <img class="contacts-list-img" src="../dist/img/user1-128x128.jpg" alt="Contact Avatar">\
                                <div class="contacts-list-info">\
              <span class="contacts-list-name">\
                Count Dracula\
                <small class="contacts-list-date pull-right">2/28/2015</small>\
              </span>\
                                    <span class="contacts-list-msg">How have you been? I was...</span>\
                                </div>\
                            </a>\
                        </li>\
                    </ul>\
                </div>\
            </div>\
            <div class="box-footer">\
                <div class="input-group">\
                    <input type="text" name="message" placeholder="Type Message ..." class="form-control">\
      <span class="input-group-btn">\
        <button type="button" class="btn btn-danger btn-flat">Send</button>\
      </span>\
                    </div>\
                </div>\
            </div>';



    var element = '<div class="popup-box chat-popup" id="'+ id +'">';
    element+=element2;

    document.getElementsByTagName("body")[0].innerHTML = document.getElementsByTagName("body")[0].innerHTML + element;

    popups.unshift(id);

    calculate_popups();

}

//calculate the total number of popups suitable and then populate the toatal_popups variable.
function calculate_popups()
{
    var width = window.innerWidth;
    if(width < 540)
    {
        total_popups = 0;
    }
    else
    {
        width = width - 200;
        //320 is width of a single popup box
        total_popups = parseInt(width/320);
    }

    display_popups();

}

//recalculate when window is loaded and also when window is resized.
window.addEventListener("resize", calculate_popups);
window.addEventListener("load", calculate_popups);

//----------------------------------------SETUP VARS----------------------------------------
var audio = new Audio('assets/sound/message.mp3'); //notification Sound

var hostIP = document.location["hostname"]; //aktuelle HostAdresse
var port = 80; //port
var webSocket; //webSocket
var username, tmp_user, disconnected = true;

$(document).ready(function() {
    showLoginDialog("show", "alert", ""); //show login Dialog
    showLoginDialog("show", "alert_register", ""); //show login Dialog

    if(window.location.search.substring(1).length > 0){
        docIDs.in_loginUsername().val(window.location.search.substring(1));
        docIDs.in_loginPassword().focus();
    }
});
//---------------------------------------- Web Socket ----------------------------------------
function connect(firstMessage) {
    sendMessage(firstMessage);

    if (disconnected) {
        webSocket = new WebSocket("ws://" + hostIP + ":" + port + "/chat");
        //Websocket Events
        webSocket.onmessage = function(msg) {
            //dispatching
            disconnected = false;
            var data = JSON.parse(msg.data)
            var type = data["type"];
            if (type === "msg") {
                username = tmp_user;
                updateUserList(data);
                updateChat(data.msg, data.chatID);
            } else if (type.includes("server_msg")) {
                if (data.invite != undefined) {
                    notify();
                    tabManager.createTab(data.invite, data.name);
                } else if (type.includes("closeChat")) {
                    notify();
                    tabManager.closeTab(data.chatID);
                } else if (data.msg == "Registrieren erfolgreich") {
                    showLoginDialog("hide", "alert", "");
                    showLoginDialog("hide", "alert_register", "");
                    loginUser(docIDs.in_registerUsername().val(), docIDs.in_registerPassword().val());
                } else if (data.msg == "Gültige Zugangsdaten") {
                    showLoginDialog("hide", "alert", "");
                    showLoginDialog("hide", "alert_register", "");
                    docIDs.in_chatMessage().focus();
                    docIDs.div_tabContainer().html("<li><a href='javascript:void(0)' class='tablinks' onclick=\"tabManager.openTab(event.currentTarget, 'global')\" id='defaultOpen'>Global</a></li>");
                    docIDs.div_chatContainer().html("<div id='global' class='tabcontent'><div class='media-body'><article><b>Chat2U</b><p>Herzlich Willkommen! Vielen Dank, dass du Chat2U benutzt.</p><small></small></article><hr></div></div>");

                    var defaultChat = docIDs.a_defaultTab()[0];
                    defaultChat.onclick({currentTarget: defaultChat});
                }
            } else {
                if (data["exceptionType"] == "AccessDeniedException") {
                    showLoginDialog("show", "alert", data["msg"]);
                } else if (data["exceptionType"] == "UsernameExistException") {
                    docIDs.in_registerUsername().prop('disabled', false);
                    docIDs.in_registerPassword().prop('disabled', false);
                    docIDs.in_registerSecPassword().prop('disabled', false);

                    showLoginDialog("show", "alert_register", data["msg"]);
                } else if (data["exceptionType"] == "IllegalArgumentException") {
                    updateChat(data.msg, data.chatID);
                }
            }
        };
        webSocket.onclose = function() {
            updateChat("<article><b>Chat2U</b><p style='color:#F70505'>Client disconnected!</p></article>", "global");
            showLoginDialog("show", "alert", "<p style='color:#F70505'>Client disconnected!</p>"); //show login Dialog
            showLoginDialog("show", "alert_register", ""); //show login Dialog
            disconnected = true;
        };
    }
}

function registerUser(user, password, password2) {
    docIDs.in_registerUsername().prop('disabled', true);
    docIDs.in_registerPassword().prop('disabled', true);
    docIDs.in_registerSecPassword().prop('disabled', true);
    if (password == password2) {
        connect("{\"cmd\":\"register\",\"params\": {\"username\":\"" + user + "\",\"passwort\":\"" + password + "\"}}");
    } else {
        showLoginDialog("show", "alert_register", "<p style=\"color: #ff0000; margin-bottom: 0px;\">Passwörter nicht identisch</p>");
    }
}

function loginUser(username, password) {
    tmp_user = username;
    connect("{\"cmd\":\"login\",\"params\": {\"username\":\"" + username + "\",\"passwort\":\"" + password + "\"}}");
}


//----------------------------------------Helper Methods----------------------------------------
//Send a message if it's not empty, then clear the input field
function sendMessage(message) {
    wait(function() {
        if (message !== "") {
            webSocket.send(message);
        }
    });
}

//Send a message if it's not empty, then clear the input field
function sendMessageToChat(message) {
    var chatID = getCurrentChatID();
    wait(function() {
        if (message !== "") {
            var msg = "{\"cmd\":\"sendMessage\",\"params\":{\"message\":\"" + message + "\",\"chatID\":\"" + chatID + "\"}}"
            webSocket.send(msg);
            docIDs.in_chatMessage().val("");
        }
    });
}

//Update the chat-panel
function updateChat(msg, chatID) {
    var chat = $("#" + chatID).children()[0];
    chat.innerHTML += "\n" + msg;

    var scrollBar = docIDs.div_chatContainer().parent();
    scrollBar.scrollTop = scrollBar.scrollHeight;

    notify();
}

function notify() {
    if (window.blurred && docIDs.checkBox_notifications()[0].checked) {
        audio.play();
        document.title = "Chat2U ( ! )";
    }
}

//update UserList
function updateUserList(data) {
    docIDs.ul_userList().html("");
    data.userlist.forEach(function(user) {
        var userListItem = "<li id='user_" + user + "' class='media' style='display: block'><div class='media-body'><div class='media'><div class='pull-left'><img class='media-object img-circle' style='max-height:40px;' src='assets/img/newuser.png' /></div><div class='media-body' ><h5>" + user + "</h5><small class='text-muted'>DEIN STATUS</small></div></div></div></li>";
        docIDs.ul_userList().html(docIDs.ul_userList().html() + userListItem);
        if (username !== user) {
            var users = [username, user];
            $("#user_" + user).attr("onclick", 'inviteToChat("' + users + '")');
            console.log($("#user_" + user).attr("onclick"));
        }
    });
}

//show Login Dialog
function showLoginDialog(showhide, alert_type, alert) {
    if (showhide == "show") {
        docIDs.in_loginUsername().focus();
        docIDs.div_loginBox().css("visibility", "visible");
        if (alert != "") {
            $("#" + alert_type).css("visibility", "visible");
            $("#" + alert_type).html(alert);
        } else {
            $("#" + alert_type).css("visibility", "hidden");
        }
    } else if (showhide == "hide") {
        docIDs.div_loginBox().css("visibility", "hidden");
        $("#" + alert_type).css("visibility", "hidden");
    }
}

//wait for the socket to connect
function wait(callback) {
    setTimeout(
        function() {
            if (webSocket.readyState === 1) {

                if (callback != null) {
                    callback();
                }
                return;

            } else {
                wait(callback);
            }

        }, 5); // wait 5 milisecond for the connection...
}

function getCurrentChatID() {
    var chats = docIDs.div_chatContainer().children();
    for (var i = 0; i < chats.length; i++) {
        var child = chats[i];
        if (child.style.display === "block") {
            return child.id;
        }
    }
    return "global";
}

function inviteToChat(userList) {
    userList = userList.split(",");
    var users = (function(){
        var output = "";
        userList.forEach(function(user) {
            output += '{"name":"' + user + '"}, ';
        });
        return output.substring(0, output.length - 1);
    }());
    sendMessage('{"cmd":"openChat","params":{"users":[' + users + ']}}');
}
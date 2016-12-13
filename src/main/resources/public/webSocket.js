//----------------------------------------SETUP VARS----------------------------------------
var audio = new Audio('assets/sound/message.mp3'); //notification Sound

var hostIP = document.location["hostname"]; //aktuelle HostAdresse
var port = 80; //port
var webSocket; //webSocket
var applicationData = {username:""};

//----------------------------------------Events
$(document).ready(function() {
    showLoginDialog("show", "loginAlert", ""); //show login Dialog
    showLoginDialog("show", "registerAlert", ""); //show login Dialog

    if(window.location.search.substring(1).length > 0) {
        var params = (function(){
            var v = window.location.search.substring(1).split("&");
            var params = {};
            for(var i = 0; i < v.length; i++) {
                if(v[i].includes("=")) {
                    var param = v[i].split("=");
                    params[param[0]] = param[1];
                } else {
                    params[v[i]] = null;
                }
            }

            return params;
        }());

        doc.input.loginUsername().val(params.user);
        if(params.password !== undefined && params.password !== null) {
            doc.input.loginPassword().val(params.password);
            doc.btn.login().ready(function() {
                doc.btn.login().trigger("click");
            });
        } else {
            doc.input.loginPassword().focus();
        }
    } else if(Cookie.get("username").length > 0) {
        doc.input.loginUsername().val(Cookie.get("username"));
        doc.input.loginPassword().focus();
    }
});
//---------------------------------------- Web Socket ----------------------------------------
function connect(firstMessage) {
    sendMessage(firstMessage);

    if (webSocket === undefined || webSocket.readyState === 3) {
        var dispatcher = (function() {
            var dispatcher = new Dispatcher();
            dispatcher.createType("textMessage", function(msg){
                notify();

                var myUsername = applicationData.username;
                doc.ul_userList().html("");
                msg.secondData.forEach(function(user) {
                    var userListItem = "<li id='user_" + user.id + "' class='media' style='display: block'><div class='media-body'><div class='media'><div class='pull-left'><img class='media-object img-circle' style='max-height:40px;' src='assets/img/newuser.png' /></div><div class='media-body' ><h5>" + user.name + "</h5><small class='text-muted'>DEIN STATUS</small></div></div></div></li>";
                    doc.ul_userList().html(doc.ul_userList().html() + userListItem);
                    if (myUsername !== user.name) {
                        var users = [myUsername, user.name];
                        $("#user_" + user.id).attr("onclick", 'inviteToChat("' + users + '", prompt("Chat Name: "))');
                    }
                });
                updateChat(msg.primeData);
            });
            dispatcher.createType("tabControl", function(msg){
                if(msg.secondData === "open") {
                    notify();
                    tabManager.createTab(msg.primeData.chatID, msg.primeData.name, msg.primeData.type);
                }
                else {
                     notify();
                     tabManager.closeTab(msg.primeData);
                }
            });
            dispatcher.createType("statusRegister", function(msg){
                doc.div.loginBox().css("cursor", "auto");
                doc.btn.register().prop("disabled", false);
                doc.input.registerUsername().prop('disabled', false);
                doc.input.registerPassword().prop('disabled', false);
                doc.input.registerSecPassword().prop('disabled', false);

                if(msg.primeData === true) {
                    showLoginDialog("hide", "loginAlert", "");
                    showLoginDialog("hide", "registerAlert", "");

                    doc.input.loginUsername().val(doc.input.registerUsername().val());
                    doc.input.loginPassword().val(doc.input.registerPassword().val());
                    doc.input.registerUsername().val("");
                    doc.input.registerPassword().val("");
                    loginUser();
                } else {
                    showLoginDialog("show", "registerAlert", msg.secondData);
                }
            });
            dispatcher.createType("statusLogin", function(msg){
                doc.div.loginBox().css("cursor", "auto");
                doc.btn.login().prop("disabled", false);
                doc.input.loginUsername().prop('disabled', false);
                doc.input.loginPassword().prop('disabled', false);
                if(msg.primeData === true) {
                    showLoginDialog("hide", "loginAlert", "");
                    showLoginDialog("hide", "registerAlert", "");
                    doc.input.chatMessage().focus();
                    doc.div.tabContainer().html("");
                    doc.div.chatContainer().html("");
                } else if(msg.primeData === false || msg.primeData === "occupied") {
                    showLoginDialog("show", "loginAlert", msg.secondData);
                }
            });

            return dispatcher;
        })();

        webSocket = new WebSocket("ws://" + hostIP + ":" + port + "/chat");
        webSocket.onmessage = function(msg_from_server){
        	var message = JSON.parse(msg_from_server.data);
        	dispatcher.runType(message);
        }
        webSocket.onclose = function() {
            updateChat({message: "<article><b>Chat2U</b><p style='color:#F70505'>Client disconnected!</p></article>", chatID: "global"});
            showLoginDialog("show", "loginAlert", "<p style='color:#F70505'>Client disconnected!</p>"); //show login Dialog
            showLoginDialog("show", "registerAlert", ""); //show login Dialog
        };
        setInterval(function(){
            webSocket.send(".");
        }, 1000*((60*4) + 50));
    }
}

function registerUser() {
    var username = doc.input.registerUsername().val();
    var password = doc.input.registerPassword().val();
    var password2 = doc.input.registerSecPassword().val();

    if(username !== "" && password !== ""){
        if (password === password2) {
            doc.div.loginBox().css("cursor", "progress");
            doc.btn.register().prop("disabled", true);
            doc.input.registerUsername().prop('disabled', true);
            doc.input.registerPassword().prop('disabled', true);
            doc.input.registerSecPassword().prop('disabled', true);
            connect("{\"cmd\":\"register\",\"params\": {\"username\":\"" + username + "\",\"passwort\":\"" + password + "\"}}");
        } else {
            showLoginDialog("show", "registerAlert", "<p style=\"color: #ff0000; margin-bottom: 0px;\">Passwörter nicht identisch</p>");
        }
    }
}

function loginUser() {
    var username = doc.input.loginUsername().val();
    var password = doc.input.loginPassword().val();
    if(username.length > 0 && password.length > 0){
        Cookie.set("username", username);
        applicationData.username = username;
        doc.div.loginBox().css("cursor", "progress");
        doc.btn.login().prop("disabled", true);
        doc.input.loginUsername().prop('disabled', true);
        doc.input.loginPassword().prop('disabled', true);
        connect("{\"cmd\":\"login\",\"params\": {\"username\":\"" + username + "\",\"passwort\":\"" + password + "\"}}");
    }
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
    var chatID = tabManager.currentChatID;

    wait(function() {
        if (message !== "") {
            var msg = "{\"cmd\":\"sendMessage\",\"params\":{\"message\":\"" + message + "\",\"chatID\":\"" + chatID + "\"}}"
            webSocket.send(msg);
            doc.input.chatMessage().val("");
        }
    });
}

//Update the chat-panel
function updateChat(msg) {
    var chat = $("#" + msg.chatID).children()[0];
    chat.innerHTML += "\n" + msg.message;

    var scrollBar = doc.div.chatContainer().parent();
    scrollBar.scrollTop = scrollBar.scrollHeight;
}

function notify() {
    if (window.blurred && doc.checkBox_notifications()[0].checked) {
        audio.play();
        document.title = "Chat2U ( ! )";
    }
}

//show Login Dialog
function showLoginDialog(showHide, alert_type, alert) {
    if (showHide == "show") {
        doc.input.loginUsername().focus();
        doc.div.loginBox().css("visibility", "visible");
        if (alert != "") {
            $("#" + alert_type).css("visibility", "visible");
            $("#" + alert_type).html(alert);
        } else {
            $("#" + alert_type).css("visibility", "hidden");
        }
    } else if (showHide == "hide") {
        doc.div.loginBox().css("visibility", "hidden");
        $("#" + alert_type).css("visibility", "hidden");
    }
}

function wait(callback) {
    setTimeout(function() {
        if (webSocket.readyState === 1) {
            if (callback != null) {
                callback();
            }
            return;
        } else {
            wait(callback);
        }
    }, 5);
}

function inviteToChat(userList, chatName) {
    userList = userList.split(",");
    var users = (function(){
        var output = "";
        userList.forEach(function(user) {
            output += '{"name":"' + user + '"}, ';
        });
        return output.substring(0, output.length - 2);
    }());
    sendMessage('{"cmd":"openChat","params":{"users":[' + users + '], "chatName":"' + chatName + '"}}');
}
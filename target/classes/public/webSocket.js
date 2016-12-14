//----------------------------------------SETUP VARS----------------------------------------
var audio = new Audio('assets/sound/message.mp3'); //notification Sound
var webSocket; //webSocket
var applicationData = {username:""};

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
                doc.ul_groupUsers().html("");
                msg.secondData.forEach(function(user) {
                    var userListItem = '<li id="user_' + user.id + '" class="media" username="' + user.name + '" style="display: block"><div class="media-body"><div class="media"><div class="pull-left"><img class="media-object img-circle" style="max-height:40px;" src="assets/img/newuser.png" /></div><div class="media-body" ><h5>' + user.name + '</h5><small class="text-muted">DEIN STATUS</small></div></div></div></li>';
                    doc.ul_userList().html(doc.ul_userList().html() + userListItem);
                    if (myUsername !== user.name) {
                        $("#user_" + user.id).attr("onclick", 'popup.openCreateGroupBox(["' + user.name + '"])');

                        var groupUserItem = '<li><input type="checkbox" value="' + user.name + '"/> ' + user.name + ' </li>';
                        doc.ul_groupUsers().html(doc.ul_groupUsers().html() + groupUserItem);
                    }
                });

                //update chat window
                (function (msg) {
                    var chat = $("#" + msg.chatID).children()[0];
                    chat.innerHTML += "\n" + msg.message;

                    var scrollBar = doc.div.chatContainer().parent();
                    scrollBar.scrollTop = scrollBar.scrollHeight;
                })(msg.primeData);
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
                doc.div.popup().css("cursor", "auto");
                doc.btn.register().prop("disabled", false);
                doc.input.registerUsername().prop('disabled', false);
                doc.input.registerPassword().prop('disabled', false);
                doc.input.registerSecPassword().prop('disabled', false);

                if(msg.primeData === true) {
                    popup.closeLogin();

                    doc.input.loginUsername().val(doc.input.registerUsername().val());
                    doc.input.loginPassword().val(doc.input.registerPassword().val());
                    doc.input.registerUsername().val("");
                    doc.input.registerPassword().val("");
                    loginUser();
                } else {
                    popup.openLoginAlert("registerAlert", msg.secondData);
                }
            });
            dispatcher.createType("statusLogin", function(msg){
                doc.div.popup().css("cursor", "auto");
                doc.btn.login().prop("disabled", false);
                doc.input.loginUsername().prop('disabled', false);
                doc.input.loginPassword().prop('disabled', false);
                if(msg.primeData === true) {
                    popup.closeLogin();

                    doc.input.chatMessage().focus();
                    doc.div.tabContainer().html("<li><a href='javascript:void(0)' class='tablinks' onclick='popup.openCreateGroupBox()' id='addGroup'><strong>+</strong></a></li>");
                    doc.div.chatContainer().html("");
                } else if(msg.primeData === false || msg.primeData === "occupied") {
                    popup.openLoginAlert("loginAlert", msg.secondData);
                }
            });

            return dispatcher;
        })();

        webSocket = new WebSocket("ws://" + document.location.hostname + ":80/chat");
        webSocket.onmessage = function(msg_from_server){
        	var message = JSON.parse(msg_from_server.data);
        	dispatcher.runType(message);
        }
        webSocket.onclose = function() {
            popup.openLoginAlert("loginAlert", "<p style='color:#F70505'>Client disconnected!</p>"); //show login Dialog
        };
        setInterval(function(){
            webSocket.send(".");
        }, 1000*((60*4) + 50));
    }
}

function loginUser() {
    var username = doc.input.loginUsername().val();
    var password = doc.input.loginPassword().val();
    if(username.length > 0 && password.length > 0){
        Cookie.set("username", username);
        applicationData.username = username;
        doc.div.popup().css("cursor", "progress");
        doc.btn.login().prop("disabled", true);
        doc.input.loginUsername().prop('disabled', true);
        doc.input.loginPassword().prop('disabled', true);
        connect("{\"cmd\":\"login\",\"params\": {\"username\":\"" + username + "\",\"passwort\":\"" + password + "\"}}");
    }
}


//----------------------------------------Helper Methods----------------------------------------
//Send a message if it's not empty, then clear the input field
function sendMessage(message) {
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


    wait(function() {
        if (message !== "") {
            webSocket.send(message);
        }
    });
}

//Send a message if it's not empty, then clear the input field
function sendMessageToChat(message) {
    var chatID = tabManager.currentChatID;

    var msg = "{\"cmd\":\"sendMessage\",\"params\":{\"message\":\"" + message + "\",\"chatID\":\"" + chatID + "\"}}"
    sendMessage(msg);
    doc.input.chatMessage().val("");
}

function notify() {
    if (window.blurred && doc.checkBox_notifications()[0].checked) {
        audio.play();
        document.title = "Chat2U ( ! )";
    }
}

function inviteToChat(userList, chatName) {
    var users = (function(){
        var output = "";
        userList.forEach(function(user) {
            output += '{"name":"' + user + '"}, ';
        });
        return output.substring(0, output.length - 2);
    }());
    sendMessage('{"cmd":"openChat","params":{"users":[' + users + '], "chatName":"' + chatName + '"}}');
}
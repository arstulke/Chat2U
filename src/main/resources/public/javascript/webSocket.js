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
                notify(msg.primeData.chatID);

                var myUsername = applicationData.username;
                doc.ul_userList().html("");
                doc.ul_groupUsers().html("");
                if(msg.secondData !== undefined) {
                    msg.secondData.forEach(function(user) {
                        var userListItem = '<li id="user_' + user.id + '" class="media" style="display: block"> <i class="fa fa-user fa-4x pull-left"></i><div class="news-item-info"><div class="name" username="' + user.name + '"><a href="#">' + user.name + '</a></div><div class="position"> </div><div class="time">Last logged-in: Mar 12, 19:02</div></div></li>'
                        doc.ul_userList().html(doc.ul_userList().html() + userListItem);
                        if (myUsername !== user.name) {
                            $("#user_" + user.id).attr("onclick", 'popup.openCreateGroupBox(["' + user.name + '"])');

                            var groupUserItem = '<li><input type="checkbox" value="' + user.name + '"/> ' + user.name + ' </li>';
                            doc.ul_groupUsers().html(doc.ul_groupUsers().html() + groupUserItem);
                        }
                    });
                }

                //update chat window
                (function (msg) {
                    var chat = $("#" + msg.chatID).children()[0];
                    chat.innerHTML += "\n" + msg.message;

                    var scroll = doc.div.chatContainer().parent();
                    scroll.scrollTop(scroll.scrollHeight);
                })(msg.primeData);
            });
            dispatcher.createType("tabControl", function(msg){
                if(msg.secondData === "open") {
                    tabManager.createTab(msg.primeData.chatID, msg.primeData.name, msg.primeData.type);
                    notify(msg.primeData.chatID);
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
            if(webSocket.readyState === 1)
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

function notify(ChatId) {
    var tabElement = $("#t-"+ ChatId);
    var channelElement = $("#c-"+ ChatId);
    var groupElement = $("#g-"+ ChatId);
    var notificationElement = '<span class="badge">!</span> ';

    if(doc.checkBox_notifications()[0].checked) {
        if (window.blurred){ //wenn ChromeTab geschlossen
           document.title = "Sí ( ! )";
        }

        if(tabElement.attr("class")!= undefined){ //wenn tab existiert
            if(tabElement.attr("class")!= " active" && tabElement.children()[0].children.length === 0) {  //wenn tab geschlossen und kein notificon da ist
                tabElement.children().html(notificationElement + tabElement.children().text());
                audio.play();
            }
        }

        //wenn ein GruppenElement existiert und kein notificon da ist und tab=Gruppenelement geschlossen
        if(groupElement.children().children().text() != "" && !groupElement.children().children().html().includes(notificationElement) && tabElement.attr("class")!= " active"){
            groupElement.children().children().html(notificationElement + groupElement.children().children().html());
        }
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
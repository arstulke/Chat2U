//----------------------------------------SETUP VARS----------------------------------------
var audio = new Audio('assets/sound/message.mp3');	        	//notification Sound
showLoginDialog("show", "alert", "");							//show login Dialog
showLoginDialog("show", "alert_register", "");                  //show login Dialog

var hostIP = document.location["hostname"];                     //aktuelle HostAdresse
var port = 80;                                                  //port
var webSocket = null; //webSocket
var username, tmp_user;

//---------------------------------------- Web Socket ----------------------------------------
function connect(firstMessage) {
    if(webSocket != null)
    {
        webSocket.close();
        console.log("Closed Websocket");
    }
    webSocket = new WebSocket("ws://"+hostIP+":"+port+"/chat");
	sendMessage(firstMessage);

    //Websocket Events
    webSocket.onmessage = function(msg) {
        var data = JSON.parse(msg.data)
        var type = data["type"];
        if (type === "msg") {
            username = tmp_user;
            updateUserList(data);
            updateChat(data.msg, data.chatID);
        } else if (type.includes("server_msg")) {
            if(data.invite != undefined) {
                notify();
                tabManager.addTab(data.invite, data.name);
            } else if(type.includes("closeChat")){
                notify();
                tabManager.closeTab(data.chatID);
            } else if(data.msg == "Registrieren erfolgreich") {
                showLoginDialog("hide", "alert", "");
                showLoginDialog("hide", "alert_register", "");
                loginUser(id("user_register").value, id("password_register").value);
            } else if(data.msg == "Gültige Zugangsdaten") {
                showLoginDialog("hide", "alert", "");
                showLoginDialog("hide", "alert_register", "");
                id("message").focus();
                id("chats").innerHTML = "<li><a href='javascript:void(0)' class='tablinks' onclick=\"openTab(event.currentTarget, 'global')\" id='defaultOpen'>Global</a></li>";
                id("chat_contents").innerHTML = "<div id='global' class='tabcontent'><div class='media-body'><article><b>Chat2U</b><p>Herzlich Willkommen! Vielen Dank, dass du Chat2U benutzt.</p><small></small></article><hr></div></div>";
                document.getElementById("defaultOpen").click();
            }
        } else {
            if (data["exceptionType"] == "AccessDeniedException") {
                showLoginDialog("show","alert", data["msg"]);
            } else if (data["exceptionType"] == "UsernameExistException") {
                id("user_register").disabled = false;
                id("password_register").disabled = false;
                id("password2_register").disabled = false;

                showLoginDialog("show","alert_register", data["msg"]);
            } else if (data["exceptionType"] == "IllegalArgumentException") {
                 updateChat(data.msg, data.chatID);
            }
        }
    };
    webSocket.onclose = function() {
        updateChat("<article><b>Chat2U</b><p style='color:#F70505'>Client disconnected!</p></article>", "global");
        showLoginDialog("show", "alert", "<p style='color:#F70505'>Client disconnected!</p>");							//show login Dialog
        showLoginDialog("show", "alert_register", "");                  //show login Dialog
    };
}

function registerUser(user, password, password2) {
    id("user_register").disabled = true;
    id("password_register").disabled = true;
    id("password2_register").disabled = true;
    if(password == password2) {
        connect("{\"cmd\":\"register\",\"params\": {\"username\":\"" + user+"\",\"passwort\":\"" + password + "\"}}");
    } else {
        showLoginDialog("show", "alert_register", "<p style=\"color: #ff0000; margin-bottom: 0px;\">Passwörter nicht identisch</p>");
    }
}

function loginUser(username, password) {
    connect("{\"cmd\":\"login\",\"params\": {\"username\":\""+username+"\",\"passwort\":\""+password+"\"}}");
}


//----------------------------------------Helper Methods----------------------------------------
//Send a message if it's not empty, then clear the input field
function sendMessage(message) {
    wait(function(){
        if (message !== "") {
            webSocket.send(message);
            id("message").value = "";
        }
    });
}

//Send a message if it's not empty, then clear the input field
function sendMessageToChat(message, chatID) {
    wait(function() {
        if (message !== "") {
            var msg = "{\"cmd\":\"sendMessage\",\"params\":{\"message\":\"" + message + "\",\"chatID\":\"" + chatID  + "\"}}"
            webSocket.send(msg);
            id("message").value = "";
        }
    });
}

//Update the chat-panel
function updateChat(msg, chatID) {
    var parent = id(chatID);

    var i = 0;
    while(parent.childNodes[i].nodeType !== 1){
        i++;
    }
    var chat = parent.childNodes[i];

	var scrollBar = id("scroll");

    chat.innerHTML += "\n" + msg;
    scrollBar.scrollTop = scrollBar.scrollHeight;

    notify();
}

function notify(){
    if (window.blurred && id("checkbox").checked) {
        audio.play();
        document.title = "Chat2U ( ! )";
    }
}

//update UserList
function updateUserList(data) {
    id("userlist").innerHTML = "";
    data.userlist.forEach(function(user) {
        id("userlist").insertAdjacentHTML("afterbegin","<li id='user_" + user + "' class='media' style='display: block'><div class='media-body'><div class='media'><a class='pull-left' href='#'><img class='media-object img-circle' style='max-height:40px;' src='assets/img/newuser.png' /></a><div class='media-body' ><h5>" + user + "</h5><small class='text-muted'>DEIN STATUS</small></div></div></div></li>");
        if(username !== user) {
            $("#user_" + user).click(function() {
                sendMessage('{"cmd":"openChat","params":{"users":[{"name":"' + username + '"}, {"name":"' + user + '"}]}}');
            });
        }
    });
}

//show Login Dialog
function showLoginDialog(showhide, alert_type, alert) {
    if (showhide == "show") {
		id("user").focus();
        id('popupbox').style.visibility = "visible";
		if(alert != ""){
            id(alert_type).style.visibility = "visible";
            id(alert_type).innerHTML = ""+alert+"";
		}else{
		    id(alert_type).style.visibility = "hidden";
		}
    } else if (showhide == "hide") {
        id('popupbox').style.visibility = "hidden";
		id(alert_type).style.visibility = "hidden";
    }
}

//wait for the socket to connect
function wait(callback){
    setTimeout(
        function () {
            if (webSocket.readyState === 1) {

                if(callback != null){
                    callback();
                }
                return;

            } else {
                wait(callback);
            }

        }, 5); // wait 5 milisecond for the connection...
}

function getCurrentChatID() {
    var chats = id("chat_contents");
    for(var i = 1; i < chats.childNodes.length; i += 2) {
        var child = chats.childNodes[i];
        if(child.style.display === "block")
        {
            return child.id;
        }
    }
    return "global";
}
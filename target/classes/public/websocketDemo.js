//----------------------------------------SETUP VARS----------------------------------------
var audio = new Audio('assets/sound/message.mp3');		//notification Sound
var webSocket;											//webSocket
showLoginDialog("show","");								//show login Dialog

var hostIP = document.location["hostname"];             //aktuelle HostAdresse
var port = 80;                                          //port

//---------------------------------------- Web Socket ----------------------------------------
function login(user, password) {
    webSocket = new WebSocket("ws://"+hostIP+":"+port+"/chat");
	sendMessage("{\"cmd\":\"login\",\"params\": {\"username\":\""+user+"\",\"passwort\":\""+password+"\"}}");

    //Websocket Events
    webSocket.onmessage = function(msg) {
        var data = JSON.parse(msg.data)
        if (data["type"] == "msg") {
            updateUserList(data);
            updateChat(data.userMessage);

        } else if (data["type"] == "server_msg") {
            if(data.msg == "Gültige Zugangsdaten") {
                showLoginDialog("hide","");
                id("message").focus();
            }
        } else {
            if (data["exceptionType"] == "AccessDeniedException") {
                webSocket.close();
				showLoginDialog("show",data["msg"]);

            } else if (data["exceptionType"] == "UsernameExistsException") {
                updateChat(data["msg"]);

            } else if (data["exceptionType"] == "IllegalArgumentException") {
                updateChat(data["msg"]);

            }
        }
    };
    webSocket.onclose = function() {
        updateChat("<article><b>Chat2U</b><p style='color:#F70505'>Client disconnected!</p></article>");
        console.log("meh");
    };
}

//----------------------------------------Event Listener----------------------------------------
//Tab opend, Closed Events
window.onblur = function() {
    window.blurred = true;
};
window.onfocus = function() {
    document.title = "Chat2U";
    window.blurred = false;
};
//BUTTON EVENT LISTENER
id("send").addEventListener("click", function() {
    sendMessage(id("message").value);
});
id("message").addEventListener("keypress", function(e) {
    if (e.keyCode === 13) {
        sendMessage(e.target.value);
    }
});
id("user").addEventListener("keypress", function(e) {
    if (e.keyCode === 13) {
        login(id("user").value, id("password").value);
    }
});
id("password").addEventListener("keypress", function(e) {
    if (e.keyCode === 13) {
        login(id("user").value, id("password").value);
    }
});
id("login").addEventListener("click", function() {
    login(id("user").value, id("password").value);
});

//----------------------------------------Helper Methods----------------------------------------
//Send a message if it's not empty, then clear the input field
function sendMessage(message) {
    waitForSocketConnection(function(){
        if (message !== "") {
            webSocket.send(message);
            id("message").value = "";
        }
    });
}
//Update the chat-panel
function updateChat(msg) {
	var parentGuest = id("chat");
	var childGuest = id("li");
	var scrollBar = id("scroll");

    childGuest.innerHTML = childGuest.innerHTML + "<div class='media-body'><div class='media'><div class='media-body' >" + msg + "</div></div></div>";
    parentGuest.parentNode.insertBefore(childGuest, parentGuest.nextSibling);
    scrollBar.scrollTop = scrollBar.scrollHeight;

    if (window.blurred && id("checkbox").checked) {
        audio.play();
        document.title = "Chat2U ( ! )";
    }
}
//update UserList
function updateUserList(data) {
    id("userlist").innerHTML = "";
    data.userlist.forEach(function(user) {
        id("userlist").insertAdjacentHTML("afterbegin","<li class='media'><div class='media-body'><div class='media'><a class='pull-left' href='#'><img class='media-object img-circle' style='max-height:40px;' src='assets/img/newuser.png' /></a><div class='media-body' ><h5>" + user + "</h5><small class='text-muted'>DEIN STATUS</small></div></div></div></li>");
    });
}
//show Login Dialog
function showLoginDialog(showhide, alert) {
    if (showhide == "show") {
		id("user").focus();
        id('popupbox').style.visibility = "visible";
		if(alert != ""){
		id('alert').style.visibility = "visible";
		id('alert').innerHTML = ""+alert+"";
		}else{
		id('alert').style.visibility = "hidden";
		}
    } else if (showhide == "hide") {
        id('popupbox').style.visibility = "hidden";
		id('alert').style.visibility = "hidden";
    }
}
//selecting element by id
function id(id) {
    return document.getElementById(id);
}

function waitForSocketConnection(callback){
    setTimeout(
        function () {
            if (webSocket.readyState === 1) {
                console.log("Connection is made")
                if(callback != null){
                    callback();
                }
                return;

            } else {
                console.log("wait for connection...")
                waitForSocketConnection(callback);
            }

        }, 5); // wait 5 milisecond for the connection...
}
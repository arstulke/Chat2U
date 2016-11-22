//----------------------------------------SETUP VARS----------------------------------------
var audio = new Audio('assets/sound/message.mp3');	        	//notification Sound
showLoginDialog("show", "alert", "");							//show login Dialog
showLoginDialog("show", "alert_register", "");                  //show login Dialog

var hostIP = document.location["hostname"];                     //aktuelle HostAdresse
var port = 80;                                                  //port
var webSocket = new WebSocket("ws://"+hostIP+":"+port+"/chat"); //webSocket

//---------------------------------------- Web Socket ----------------------------------------
function connect(firstMessage) {
	sendMessage(firstMessage);

    //Websocket Events
    webSocket.onmessage = function(msg) {
        var data = JSON.parse(msg.data)
        if (data["type"] == "msg") {
            updateUserList(data);
            updateChat(data.userMessage);

        } else if (data["type"] == "server_msg") {
            if(data.msg == "Gültige Zugangsdaten") {
                showLoginDialog("hide", "alert", "");
                showLoginDialog("hide", "alert_register", "");
                id("message").focus();
            }
        } else {
            if (data["exceptionType"] == "AccessDeniedException") {
                showLoginDialog("show","alert", data["msg"]);
            } else if (data["exceptionType"] == "UsernameExistException") {
                showLoginDialog("show","alert_register", data["msg"]);
            } else if (data["exceptionType"] == "IllegalArgumentException") {
                 updateChat(data["msg"]);
            }
        }
    };
    webSocket.onclose = function() {
        updateChat("<article><b>Chat2U</b><p style='color:#F70505'>Client disconnected!</p></article>");
        console.log("meh");
        showLoginDialog("show", "alert", "<p style='color:#F70505'>Client disconnected!</p>");							//show login Dialog
        showLoginDialog("show", "alert_register", "");                  //show login Dialog
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
//-----------------LOGIN EVENTS
id("user").addEventListener("keypress", function(e) {
    if (e.keyCode === 13) {
       if(id("user").value !== "" && id("password").value !== ""){
           connect("{\"cmd\":\"login\",\"params\": {\"username\":\""+id("user").value+"\",\"passwort\":\""+id("password").value+"\"}}");
       }
    }
});
id("password").addEventListener("keypress", function(e) {
    if (e.keyCode === 13) {
        if(id("user").value !== "" && id("password").value !== ""){
            connect("{\"cmd\":\"login\",\"params\": {\"username\":\""+id("user").value+"\",\"passwort\":\""+id("password").value+"\"}}");
        }
    }
});
id("login").addEventListener("click", function() {
    if(id("user").value !== "" && id("password").value !== ""){
        connect("{\"cmd\":\"login\",\"params\": {\"username\":\""+id("user").value+"\",\"passwort\":\""+id("password").value+"\"}}");
    }
});
//------------------REGISTER EVENTS
id("register").addEventListener("click", function() {
    if(id("user_register").value !== "" && id("password_register").value !== ""){
        register(id("user_register").value,id("password_register").value,id("password2_register").value);
    }
});
id("user_register").addEventListener("keypress", function(e) {
    if (e.keyCode === 13) {
        if(id("user_register").value !== "" && id("password_register").value !== ""){
            register(id("user_register").value,id("password_register").value,id("password2_register").value);
        }
    }
});
id("password_register").addEventListener("keypress", function(e) {
    if (e.keyCode === 13) {
        if(id("user_register").value !== "" && id("password_register").value !== ""){
            register(id("user_register").value,id("password_register").value,id("password2_register").value);
        }
    }
});
id("password2_register").addEventListener("keypress", function(e) {
    if (e.keyCode === 13) {
        if(id("user_register").value !== "" && id("password_register").value !== ""){
            register(id("user_register").value,id("password_register").value,id("password2_register").value);
        }
    }
});
function register(user,password,password2){
  if(password == password2) {
        connect("{\"cmd\":\"register\",\"params\": {\"username\":\""+user+"\",\"passwort\":\""+password+"\"}}");
    } else {
        showLoginDialog("show", "alert_register", "<p style=\"color: #ff0000\">Passwörter nicht identisch</p>");
    }
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

//selecting element by id
function id(id) {
    return document.getElementById(id);
}

//wait for the socket to connect
function wait(callback){
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
                wait(callback);
            }

        }, 5); // wait 5 milisecond for the connection...
}
//SETUP VARS
var parentGuest = document.getElementById("chat");
parentGuest.className = 'media-list';
var childGuest = document.getElementById("li");
childGuest.className = 'media';
var audio = new Audio('assets/sound/message.mp3');
var scrollBar = document.getElementById("scroll");
var webSocket;



function login(user, password) {
	webSocket = new WebSocket("ws://10.250.25.78:8080/chat?username=" + user + "&password=" + password);
	//Websocket Events
webSocket.onmessage = function(msg) {
	var data = JSON.parse(msg.data)
	if(data["type"] == "msg"){
		updateUserList(data);
		updateChat(data.userMessage);
		
	} else {
		if(data["exceptionType"] == "AccessDeniedException"){
			webSocket.close();
			updateChat(data["msg"], "disconnect");
		
		} else if(data["exceptionType"] == "UsernameExistsException"){
			updateChat(data["msg"]);
		
		} else if(data["exceptionType"] == "IllegalArgumentException"){
			updateChat(data["msg"]);
		
		}
	}
};
webSocket.onclose = function() {
    updateChat("<article><b>Chat2U<\/b><p style='color:#F70505'>Client disconnected!<\/p><\/article>");
	console.log("meh");
};
}


id("login").addEventListener("click", function() {
    login(id("user").value,id("password").value);
});


//Tab opend, Closed Events
window.onblur = function() { window.blurred = true; };
window.onfocus = function() { 
document.title = "Chat2U";
window.blurred = false; };

//Send message if "Send" is clicked od Enter is pressed
id("send").addEventListener("click", function() {
    sendMessage(id("message").value);
});
id("message").addEventListener("keypress", function(e) {
    if (e.keyCode === 13) {
        sendMessage(e.target.value);
    }
});

//Send a message if it's not empty, then clear the input field
function sendMessage(message) {
    if (message !== "") {
        webSocket.send(message);
        id("message").value = "";
    }
}

//Update the chat-panel, and the list of connected users
function updateChat(msg, type) {
    childGuest.innerHTML = childGuest.innerHTML + "<div class='media-body'><div class='media'><div class='media-body' >" + msg + "</div></div></div>";
    parentGuest.parentNode.insertBefore(childGuest, parentGuest.nextSibling);

    scrollBar.scrollTop = scrollBar.scrollHeight;
    if (window.blurred) {
        audio.play();
		document.title = "Chat2U (!)";
    }
}

function updateUserList(data){
    id("userlist").innerHTML = "";
    data.userlist.forEach(function(user) {
        insert("userlist", "<li class='media'><div class='media-body'><div class='media'><a class='pull-left' href='#'><img class='media-object img-circle' style='max-height:40px;' src='assets/img/user.gif' /></a><div class='media-body' ><h5>" + user + "</h5><small class='text-muted'>DEIN STATUS</small></div></div></div></li>");
    });
}

//Helper function for inserting HTML as the first child of an element
function insert(targetId, message) {
    id(targetId).insertAdjacentHTML("afterbegin", message);
}

//Helper function for selecting element by id
function id(id) {
    return document.getElementById(id);
}
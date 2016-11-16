//SETUP VARS
var parentGuest = document.getElementById("chat");
parentGuest.className = 'media-list';
var childGuest = document.getElementById("li");
childGuest.className = 'media';
var webSocket = new WebSocket("ws://10.250.25.78:80/chat?username=" + prompt("Nickname:"));
var audio = new Audio('assets/sound/message.mp3');
var scrollBar = document.getElementById("scroll");

//Websocket Events
webSocket.onmessage = function(msg) {
    var data = JSON.parse(msg.data)
	updateUserList(data);
    updateChat(data.userMessage);
};
webSocket.onclose = function() {
    updateChat("<article><b>Chat2U<\/b><p style='color:#F70505'>THE SERVER WAS CLOSED!<\/p><small class=\"text-muted\">Server<\/small><\/article>");
	console.log("meh");
};

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
function updateChat(msg) {
    childGuest.innerHTML = childGuest.innerHTML + "<div class='media-body'><div class='media'><a class='pull-left' href='#'><img class='media-object img-circle ' src='assets/img/user.png' /></a><div class='media-body' >" + msg + "</div></div></div><hr>";
    parentGuest.parentNode.insertBefore(childGuest, parentGuest.nextSibling);

    scrollBar.scrollTop = scrollBar.scrollHeight;
    if (window.blurred) {
        audio.play();
		document.title = "Chat2U (new message)";
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
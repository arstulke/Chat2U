function openTab(evt, tab) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }

    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }

    document.getElementById(tab).style.display = "block";
    evt.currentTarget.className += " active";
}
document.getElementById("defaultOpen").click();

function addChat(chatID, name) {
    id("chats").innerHTML +="<li><a href=\"javascript:void(0)\" class=\"tablinks\" onclick=\"openTab(event, '" + chatID + "')\">" + name + "</a></li>";
    id("chat_contents").innerHTML += "<div id=\"" + chatID + "\" class=\"tabcontent\" style=\"display: none;\"><div class=\"media-body\">Du wurdest zum Chat <b>" + name + "</b> hinzugefügt.<hr></div></div>";
}

function closeChat(chatID) {
    //tab bar
    var parent = id("chats");
    for(var i = 0; i < parent.childNodes.length; i++) {
        if(parent.childNodes[i].nodeType === 1)
        {
            var chat = parent.childNodes[i].childNodes[0];
            if(chat.getAttribute("onclick").includes(chatID)) {
                chat.parentNode.remove();
                break;
            }
        }
    }

    //tab content
    parent = id("chat_contents");
    for(var i = 0; i < parent.childNodes.length; i++) {
        if(parent.childNodes[i].nodeType === 1)
        {
            var chat = parent.childNodes[i];
            if(chat.getAttribute("id") == chatID) {
                chat.remove();
                break;
            }
        }
    }
    //id("chat_contents").innerHTML += "<div id=\"" + chatID + "\" class=\"tabcontent\" style=\"display: none;\"><div class=\"media-body\">Du wurdest zum Chat <b>" + name + "</b> hinzugefügt.<hr></div></div>";
}
var tabManager = {
    currentChatID: null,
    openTab: function(eventTarget, tabID) {
        this.currentChatID = tabID;
        var i;

        //set all tabLinks inactive
        var chatLinks = $(".tablinks");
        for(i = 0; i < chatLinks.length; i++) {
            var chatLink = chatLinks[i].parentElement;
            var classAttr = chatLink.getAttribute("class");
            if(classAttr == null)
                classAttr = "";
            classAttr = classAttr.replace(" active", "");
            chatLink.setAttribute("class", classAttr);
        }

        //hide all chat contents
        var chatContents = doc.div.chatContainer().children();
        for(i = 0; i < chatContents.length; i++) {
            chatContents[i].style.display = "none";
        }

        //display chat box
        $("#" + tabID).css("display", "block");

        //set link active
        if(eventTarget != null && eventTarget != undefined) {
            var element = eventTarget.parentElement;
            var nAttr = element.getAttribute("class");
            if(nAttr === null)
                nAttr = "";
            nAttr += " active";
            element.setAttribute("class", nAttr);
        }
    },

    createTab: function(chatID, chatName){
        var tabLink = "<li><a href='javascript:void(0)' class='tablinks' onclick=\"tabManager.openTab(event.currentTarget, '" + chatID + "')\">" + chatName + "</a></li>";
        doc.div.tabContainer().html(doc.div.tabContainer().html() + tabLink);

        var chatContent = "<div id=\"" + chatID + "\" class=\"tabcontent\" style=\"display: none;\"><div class=\"media-body\">Du wurdest zum Chat <b>" + chatName + "</b> hinzugefügt.<hr></div></div>";
        doc.div.chatContainer().html(doc.div.chatContainer().html() + chatContent);
    },

    closeTab: function(chatID) {
        var chatLinks = $(".tablinks");
        for(i = 0; i < chatLinks.length; i++) {
            if(chatLinks[i].getAttribute("onclick").includes(chatID)) {
                chatLinks[i].parentElement.remove();
                break;
            }
        }

        $("#" + chatID).remove();
    }
};
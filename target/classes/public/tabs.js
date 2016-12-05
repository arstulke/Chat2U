var tabManager = {
    openTab: function(eventTarget, tabID) {
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
        var chatContents = docIDs.div_chatContainer().children();
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
        docIDs.div_tabContainer().html(docIDs.div_tabContainer().html() + tabLink);

        var chatContent = "<div id=\"" + chatID + "\" class=\"tabcontent\" style=\"display: none;\"><div class=\"media-body\">Du wurdest zum Chat <b>" + chatName + "</b> hinzugefügt.<hr></div></div>";
        docIDs.div_chatContainer().html(docIDs.div_chatContainer().html() + chatContent);
    },

    closeTab: function(chatID) {
        if(getCurrentChatID() === chatID)
            this.openTab(docIDs.a_defaultTab()[0], "global");

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
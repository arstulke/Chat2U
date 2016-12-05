var tabManager = {
    openTab: function(eventTarget, tabID) {
        var chatLinks = $(".tablinks");
        var i;

        //set all tabLinks inactive
        function setInactive(element) {
            var classAttr = element.getAttribute("class");
            if(classAttr == null)
                classAttr = "";
            classAttr = classAttr.replace(" active", "");
            element.setAttribute("class", classAttr);
        }
        for(i = 0; i < chatLinks.length; i++) {
            var chatLink = chatLinks[i].parentElement;
            setInactive(chatLink);
        }

        //hide all chat contents
        var chatContents = $("#chat_contents").children();
        for(i = 0; i < chatContents.length; i++) {
            chatContents[i].style.display = "none";
        }

        //display chat box
        $("#" + tabID).css("display", "block");

        //set link active
        if(eventTarget != null && eventTarget != undefined) {
            var activeChatLink = eventTarget.parentElement;
            activeChatLink.setAttribute("class", activeChatLink.getAttribute("class") + " active");
        }
    },

    createTab: function(chatID, chatName){
        var tabLink = "<li><a href='javascript:void(0)' class='tablinks' onclick=\"tabManager.openTab(event.currentTarget, '" + chatID + "')\" id='defaultOpen'>" + chatName + "</a></li>";
        $("#chats").html($("#chats").html() + tabLink);

        var chatContent = "<div id=\"" + chatID + "\" class=\"tabcontent\" style=\"display: none;\"><div class=\"media-body\">Du wurdest zum Chat <b>" + chatName + "</b> hinzugefügt.<hr></div></div>";
        $("#chat_contents").html($("#chat_contents").html() + chatContent);
    }
};
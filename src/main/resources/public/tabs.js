var tabManager = {
    openTab: function(eventTarget, tabID) {
        var chatLinks = $(".tablinks");
        var i;

        function setInactive(element){
            var classAttr = element.getAttribute("class");
            if(classAttr == null)
                classAttr = "";
            classAttr += " active";
            element.setAttribute("class", classAttr);
        }
        for(i = 0; i < chatLinks.length; i++) {
            var chatLink = chatLinks[i].parentElement;
            setInactive(chatLink);
        }

        var chatContents = $("#chat_contents")[0].children;
        for(i = 0; i < chatContents.length; i++) {
            chatContents[i].style.display = "none";
        }

        $("#" + tabID).style.display = "block";
        if(eventTarget !== null) {
            var activeChatLink = eventTarget.parentElement;
            activeChatLink.setAttribute("class", activeChatLink.getAttribute("class") + " active");
        }
    }
};

function openTab(eventTarget, tabID) {
    tabManager.openTab(eventTarget, tabID);
}
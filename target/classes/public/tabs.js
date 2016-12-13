var tabManager = (function(global){
    var localTabManager;

    var openTab = function (eventTarget, tabID) {
        localTabManager.currentChatID = tabID;
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
    };

    localTabManager = {
        currentChatID: null,
        global: global,
        openTab: openTab,

        createTab: function(chatID, chatName, type) {
            var add = "";
            if(type === this.global)
                add += 'id="a_defaultTab"';
            var tabLink = "<li><a href='javascript:void(0)' class='tablinks' onclick=\"tabManager.openTab(event.currentTarget, '" + chatID + "')\" " + add + ">" + chatName + "</a></li>";
            doc.div.tabContainer().html(doc.div.tabContainer().html() + tabLink);

            var chatContent = "<div id=\"" + chatID + "\" class=\"tabcontent\" style=\"display: none;\"><div class=\"media-body\">Du wurdest zum Chat <b>" + chatName + "</b> hinzugefügt.<hr></div></div>";
            doc.div.chatContainer().html(doc.div.chatContainer().html() + chatContent);

            if(this.currentChatID === null && type === this.global)
                doc.a_defaultTab().ready(function(){
                    openTab(doc.a_defaultTab()[0], chatID);
                });
        },

        closeTab: function(chatID) {
            if(this.currentChatID === chatID && doc.a_defaultTab()[0] !== undefined) {
                var id = doc.a_defaultTab().attr("onclick")
                this.openTab(doc.a_defaultTab()[0], id.substring(id.indexOf(", '") + 3, id.indexOf("')")));
            }

            var chatLinks = $(".tablinks");
            for(i = 0; i < chatLinks.length; i++) {
                if(chatLinks[i].getAttribute("onclick").includes(chatID)) {
                    chatLinks[i].parentElement.remove();
                    break;
                }
            }

            $("#" + chatID).remove();
        }
    }
    return localTabManager;
})("global");
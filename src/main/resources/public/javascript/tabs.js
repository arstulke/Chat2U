var tabManager = (function(global) {
    var localTabManager;

    var openTab = function(eventTarget, tabID) {
        localTabManager.currentChatID = tabID;
        var i;

        //set all tabLinks inactive
        var chatLinks = $(".tablinks");
        var exists = (function() {
            for (i = 0; i < chatLinks.length; i++) {
                var id = chatLinks[i].getAttribute("onclick");
                id = id.substring(id.indexOf("Target, '") + "Target, '".length, id.indexOf("')"));
                if (tabID === id)
                    return true;
            }
            return false;
        })();

        if (exists) {
            for (i = 0; i < chatLinks.length; i++) {
                var chatLink = chatLinks[i].parentElement;
                var classAttr = chatLink.getAttribute("class");
                if (classAttr == null)
                    classAttr = "";
                classAttr = classAttr.replace(" active", "");
                chatLink.setAttribute("class", classAttr);
            }

            //hide all chat contents
            var chatContents = doc.div.chatContainer().children();
            for (i = 0; i < chatContents.length; i++) {
                chatContents[i].style.display = "none";
            }

            //display chat box
            $("#" + tabID).css("display", "block");

            //set link active
            //tabManager.openTab(event.currentTarget, '73591734')
            var chatLinks = $(".tablinks");
            for (i = 0; i < chatLinks.length; i++) {
                var id = chatLinks[i].getAttribute("onclick");
                id = id.substring(id.indexOf("currentTarget, '") + "currentTarget, '".length, id.indexOf("')"));
                if (id === tabID) {
                    break;
                }
            }
            var chatLink = chatLinks[i].parentElement;
            var classAttr = chatLink.getAttribute("class");
            if (classAttr == null)
                classAttr = "";
            classAttr += " active";
            chatLink.setAttribute("class", classAttr);
            /*if (eventTarget != null && eventTarget != undefined) {
                var element = eventTarget.parentElement;
                var nAttr = element.getAttribute("class");
                if (nAttr === null)
                    nAttr = "";
                nAttr += " active";
                element.setAttribute("class", nAttr);
            }*/

        }
    };

    localTabManager = {
        currentChatID: null,
        global: global,
        openTab: openTab,

        createTab: function(chatID, chatName, type) {
            var add = "";
            if (type !== undefined) {
                if (type.includes(this.global))
                    add += 'id="a_defaultTab"';
                //var tabLink = '<li><div class="name"><span class="badge">2</span><a href="javascript:void(0)" class="tablinks" onclick="tabManager.openTab(event.currentTarget, "' + chatID + '")"' + add + '>' + chatName + '"</a></div></li>';
                var tabLink = "<li class='selected'><a href='javascript:void(0)' onclick=\"tabManager.addTab('" + chatID + "', '" + chatName + "','" + type + "')\" " + add + "><i class='fa fa-globe'></i> " + chatName + "</a></li>";
                doc.ul_channelList().html(doc.ul_channelList().html() + tabLink);

                if (type.includes(this.global)) {
                    var chatContent = "<div id=\"" + chatID + "\" class=\"tabcontent\" style=\"display: none;\"><div class=\"media-body\">Du wurdest zum Chat <b>" + chatName + "</b> hinzugefügt.<hr></div></div>";
                    doc.div.chatContainer().html(doc.div.chatContainer().html() + chatContent);
                }

                if (this.currentChatID === null && type === this.global) {
                    doc.a_defaultTab().ready(function() {
                        openTab(doc.a_defaultTab()[0], chatID);
                    });
                }

                if (type.includes(this.global))
                    doc.a_defaultTab().trigger("click", {
                        currentTarget: doc.a_defaultTab()[0]
                    });
            } else {
                var tabLink = "<li><div class='name'><a href='javascript:void(0)' onclick=\"tabManager.addTab('" + chatID + "', '" + chatName + "','')\"" + add + "><b>" + chatName + '</b></a></div></li>';
                //<i class="fa fa-globe"></i>
                doc.ul_groupList().html(doc.ul_groupList().html() + tabLink);

                var chatContent = "<div id=\"" + chatID + "\" class=\"tabcontent\" style=\"display: none;\"><div class=\"media-body\">Du wurdest zum Chat <b>" + chatName + "</b> hinzugefügt.<hr></div></div>";
                doc.div.chatContainer().html(doc.div.chatContainer().html() + chatContent);

                if (this.currentChatID === null && type === this.global)
                    doc.a_defaultTab().ready(function() {
                        openTab(doc.a_defaultTab()[0], chatID);
                    });
            }
        },

        addTab: function(chatID, chatName, type) {
            var created = (function() {
                var children = doc.div.tabContainer().children();
                for (var i = 0; i < children.length; i++) {
                    var tabLink = children[i].children[0];
                    var attr = tabLink.getAttribute("onclick");
                    if (attr !== undefined) {
                        var id = attr.substring(attr.indexOf("get, '") + "get, '".length, attr.indexOf("')"));
                        if (id === chatID)
                            return true;
                    }
                }
                return false;
            })();

            if (!created) {
                if (!type.includes(this.global) && type.includes("channel")) {
                    var msg = "{\"cmd\":\"channelOp\",\"params\":{\"chatID\":\"" + chatID + "\", \"op\":\"join\"}}";
                    sendMessage(msg);
                    var tabLink = "<li><a href='javascript:void(0)' class='tablinks' onclick=\"tabManager.openTab(event.currentTarget, '" + chatID + "')\" >" + chatName + "&nbsp;&nbsp;<i onclick='tabManager.closeTab(\"c_2056968094\")' class='fa fa-times'></i> </a></li>";
                    doc.div.tabContainer().html(doc.div.tabContainer().html() + tabLink);
                } else {
                    var tabLink = "<li><a href='javascript:void(0)' class='tablinks' onclick=\"tabManager.openTab(event.currentTarget, '" + chatID + "')\" >" + chatName + "</a></li>";
                    doc.div.tabContainer().html(doc.div.tabContainer().html() + tabLink);
                }
                var chatContent = "<div id=\"" + chatID + "\" class=\"tabcontent\" style=\"display: none;\"><div class=\"media-body\">Du wurdest zum Chat <b>" + chatName + "</b> hinzugefügt.<hr></div></div>";
                doc.div.chatContainer().html(doc.div.chatContainer().html() + chatContent);

                if (this.currentChatID === null && type === this.global)
                    doc.a_defaultTab().ready(function() {
                        openTab(doc.a_defaultTab()[0], chatID);
                    });
            }

            this.openTab((function() {
                var children = doc.div.tabContainer().children();

                for (var i = 0; i < children.length; i++) {
                    var tabLink = children[i].children[0];
                    var attr = tabLink.getAttribute("onclick");
                    if (attr !== undefined) {
                        var id = attr.substring(attr.indexOf("get, '") + "get, '".length, attr.indexOf("')"));
                        if (id === chatID)
                            return tabLink;
                    }
                }

                return null;
            })(), chatID);
        },

        /*createTab: function(chatID, chatName, type) {
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
                },*/

        closeTab: function(chatID) {
            if (this.currentChatID === chatID && doc.a_defaultTab()[0] !== undefined) {
                var id = doc.a_defaultTab().attr("onclick");
                id = id.substring(id.indexOf("('") + 2, id.indexOf("', '"));
                this.openTab(doc.a_defaultTab()[0], id);
            }

            var chatLinks = $(".tablinks");
            for (i = 0; i < chatLinks.length; i++) {
                if (chatLinks[i].getAttribute("onclick").includes(chatID)) {
                    chatLinks[i].parentElement.remove();
                    break;
                }
            }


            //$("#" + chatID).remove();
        }
    }
    return localTabManager;
})("global");
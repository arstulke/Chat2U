var popup = (function(){
    var popup;
    popup = {
            openLogin: function() {
                this.closeCreateGroupBox();
                $("#registerAlert").html("");
                $("#loginAlert").html("");

                doc.input.loginUsername().focus();
                doc.div.popup().css('visibility', 'visible');
                doc.div.loginBox().css('visibility', 'visible');
                doc.div.loginBox().css('display', 'block');
                doc.div.popup().css('display', 'block');
            },

            openLoginAlert: function(alertID, message) {
                this.openLogin();
                $("#" + alertID).html(message);
            },

            closeLogin: function() {
                doc.div.popup().css('visibility', 'hidden');
                doc.div.loginBox().css('visibility', 'hidden');
                doc.div.loginBox().css('display', 'none');
                doc.div.popup().css('display', 'none');
             },

            openCreateGroupBox: function(users) {
                doc.alert_createGroupBox().html("");

                doc.div.popup().css('visibility', 'visible');
                doc.div.createGroupBox().css('visibility', 'visible');

                if(users !== undefined && users.length > 0 && users[0] !== undefined) {
                    for(var i = 0; i < users.length; i++) {
                        var userItems = doc.ul_groupUsers().children();
                        for(var j = 0; j < userItems.length; j++) {
                            var checkbox = userItems[j].childNodes[0];
                            if(checkbox.value === users[i]) {
                                checkbox.checked = true;
                            }
                        }
                    }

                    doc.input.groupName().val((users.concat(applicationData.username) + "").replace(",", ", "));
                }

                doc.div.createGroupBox().css('display', 'block');
                doc.div.popup().css('display', 'block');
            },

            openCreateGroupBoxAlert: function(message) {
                this.openCreateGroupBox();
                doc.alert_createGroupBox().html(message);
            },

            closeCreateGroupBox: function() {
                doc.div.popup().css('visibility', 'hidden');
                doc.div.createGroupBox().css('visibility', 'hidden');
                doc.div.createGroupBox().css('display', 'none');
                doc.div.popup().css('display', 'none');
            }
    }
    return popup;
})();
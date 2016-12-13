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
            },

            openLoginAlert: function(alertID, message) {
                this.openLogin();
                $("#" + alertID).html(message);
            },

            closeLogin: function() {
                doc.div.popup().css('visibility', 'hidden');
                doc.div.loginBox().css('visibility', 'hidden');
                doc.div.loginBox().css('display', 'none');
             },

            openCreateGroupBox: function() {
                doc.alert_createGroupBox().html("");

                doc.div.popup().css('visibility', 'visible');
                doc.div.createGroupBox().css('visibility', 'visible');
                doc.div.createGroupBox().css('display', 'block');
            },

            openCreateGroupBoxAlert: function(message) {
                this.openCreateGroupBox();
                doc.alert_createGroupBox().html(message);
            },

            closeCreateGroupBox: function() {
                doc.div.popup().css('visibility', 'hidden');
                doc.div.createGroupBox().css('visibility', 'hidden');
                doc.div.createGroupBox().css('display', 'none');
            }
    }
    return popup;
})();
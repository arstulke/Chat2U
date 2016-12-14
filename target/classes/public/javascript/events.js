$(document).ready(function(){
    //----------------------------------------Event Listener----------------------------------------
    //-----------------BLUR EVENTS
    $(window).blur(function() {
        window.blurred = true;
    });
    $(window).focus(function() {
        document.title = "Sí - Dein Chatdienst";
        window.blurred = false;
    });


    //-----------------SEND EVENTS
    function sendMessageEvent(e) {
        if (e.type === "click" || e.keyCode === 13) {
            sendMessageToChat(doc.input.chatMessage().val());
            doc.input.chatMessage().focus();
        }
    }
    doc.btn.chatSendMessage().click(sendMessageEvent);
    doc.input.chatMessage().keypress(sendMessageEvent);


    //-----------------LOGIN EVENTS
    function login(e) {
        if (e.type === "click" || e.keyCode === 13) {
            loginUser();
        }
    }
    doc.input.loginUsername().keypress(login);
    doc.input.loginPassword().keypress(login);
    doc.btn.login().click(login);


    //------------------REGISTER EVENTS
    function register(e) {
        if (e.type === "click" || e.keyCode === 13) {
            var username = doc.input.registerUsername().val();
                var password = doc.input.registerPassword().val();
                var password2 = doc.input.registerSecPassword().val();

                if(username !== "" && password !== ""){
                    if (password === password2) {
                        doc.div.popup().css("cursor", "progress");
                        doc.btn.register().prop("disabled", true);
                        doc.input.registerUsername().prop('disabled', true);
                        doc.input.registerPassword().prop('disabled', true);
                        doc.input.registerSecPassword().prop('disabled', true);
                        connect("{\"cmd\":\"register\",\"params\": {\"username\":\"" + username + "\",\"passwort\":\"" + password + "\"}}");
                    } else {
                        popup.openLoginAlert("registerAlert", '<p style="color: #ff0000; margin-bottom: 0px;">Passwörter nicht identisch</p>');
                    }
                }
        }
    }
    doc.btn.register().click(register);
    doc.input.registerUsername().keypress(register);
    doc.input.registerPassword().keypress(register);
    doc.input.registerSecPassword().keypress(register);

    //------------------CREATE GROUP
    doc.btn.createGroup().click(function() {

        var users = (function(){
            var users = [applicationData.username];
            var items = doc.ul_groupUsers().children();
            for(var i = 0; i < items.length; i++){
                var checkbox = items[i].childNodes[0];
                if(checkbox.checked) {
                    users[users.length] = checkbox.value;
                }
            }

            return users;
        })();
        var chatName = doc.input.groupName().val();

        console.log(users);
        console.log(chatName);

        if(users.length < 2) {
            popup.openCreateGroupBoxAlert("Bitte wähle mehr Benutzer aus.");
        }
        else if(chatName.length < 3) {
            popup.openCreateGroupBoxAlert("Gruppenname zu kurz.");
        }
        else {
            (function(){
                var items = doc.ul_groupUsers().children();
                for(var i = 0; i < items.length; i++) {
                    items[i].childNodes[0].checked = false;
                }
            })();

            doc.input.searchGroupUser().val("");
            doc.input.groupName().val("");


            inviteToChat(users, chatName);
            popup.closeCreateGroupBox();
        }
    });
});
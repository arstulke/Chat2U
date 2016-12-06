$(document).ready(function(){
    //----------------------------------------Event Listener----------------------------------------
    //Tab opend, Closed Events
    $(window).blur(function() {
        window.blurred = true;
    });
    window.onfocus = function() {
        document.title = "Chat2U";
        window.blurred = false;
    };
    //BUTTON EVENT LISTENER
    doc.btn.chatSendMessage().click(function() {
        sendMessageToChat(doc.input.chatMessage().val());
    });
    doc.input.chatMessage().keypress(function(e) {
        if (e.keyCode === 13) {
            sendMessageToChat(e.target.value);
        }
    });
    //-----------------LOGIN EVENTS
    doc.input.loginUsername().keypress(function(e) {
        if (e.keyCode === 13) {
            if(doc.input.loginUsername().val() !== "" && doc.input.loginPassword().val() !== ""){
                loginUser(doc.input.loginUsername().val(), doc.input.loginPassword().val());
           }
        }
    });
    doc.input.loginPassword().keypress(function(e) {
        if (e.keyCode === 13) {
            if(doc.input.loginUsername().val() !== "" && doc.input.loginPassword().val() !== "") {
                loginUser(doc.input.loginUsername().val(), doc.input.loginPassword().val());
            }
        }
    });
    doc.btn.login().click(function() {
        if(doc.input.loginUsername().val() !== "" && doc.input.loginPassword().val() !== "") {
            loginUser(doc.input.loginUsername().val(), doc.input.loginPassword().val());
        }
    });
    //------------------REGISTER EVENTS
    doc.btn.register().click(function() {
        if(doc.input.registerUsername().val() !== "" && doc.input.registerPassword().val() !== ""){
            registerUser(doc.input.registerUsername().val(), doc.input.registerPassword().val(), doc.input.registerSecPassword().val());
        }
    });
    doc.input.registerUsername().keypress(function(e) {
        if (e.keyCode === 13) {
            if(doc.input.registerUsername().val() !== "" && doc.input.registerPassword().val() !== ""){
                registerUser(doc.input.registerUsername().val(), doc.input.registerPassword().val(), doc.input.registerSecPassword().val());
            }
        }
    });
    doc.input.registerPassword().keypress(function(e) {
        if (e.keyCode === 13) {
            if(doc.input.registerUsername().val() !== "" && doc.input.registerPassword().val() !== ""){
                registerUser(doc.input.registerUsername().val(), doc.input.registerPassword().val(), doc.input.registerSecPassword().val());
            }
        }
    });
    doc.input.registerSecPassword().keypress(function(e) {
        if (e.keyCode === 13) {
            if(doc.input.registerUsername().val() !== "" && doc.input.registerPassword().val() !== ""){
                registerUser(doc.input.registerUsername().val(), doc.input.registerPassword().val(), doc.input.registerSecPassword().val());
            }
        }
    });


    //Search User in Userlist
    doc.input.searchUser().on('input', function() {
        var searchEles = doc.ul_userList().children();
        for(var i = 0; i < searchEles.length; i++) {
            if(searchEles[i].id.indexOf('user_' + doc.input.searchUser().val()) !== 0) {
                searchEles[i].style.display  = "none";
            } else {
                searchEles[i].style.display = "block";
            }
        }
    });
});
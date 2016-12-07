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
    doc.btn.chatSendMessage().click(function(e) {
        sendMessageToChat(doc.input.chatMessage().val());
    });
    doc.input.chatMessage().keypress(function(e) {
        if (e === undefined || e === null || e.keyCode === 13) {
            sendMessageToChat(doc.input.chatMessage().val());
        }
    });
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
            registerUser();
        }
    }
    doc.btn.register().click(register);
    doc.input.registerUsername().keypress(register);
    doc.input.registerPassword().keypress(register);
    doc.input.registerSecPassword().keypress(register);


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
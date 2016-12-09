$(document).ready(function(){
    //----------------------------------------Event Listener----------------------------------------
    //-----------------BLUR EVENTS
    $(window).blur(function() {
        window.blurred = true;
    });
    $(window).focus(function() {
        document.title = "Chat2U";
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
            registerUser();
        }
    }
    doc.btn.register().click(register);
    doc.input.registerUsername().keypress(register);
    doc.input.registerPassword().keypress(register);
    doc.input.registerSecPassword().keypress(register);
});
$(document).ready(function(){
    //----------------------------------------Event Listener----------------------------------------
    //Tab opend, Closed Events
    window.onblur = function() {
        window.blurred = true;
    };
    window.onfocus = function() {
        document.title = "Chat2U";
        window.blurred = false;
    };
    //BUTTON EVENT LISTENER
    docIDs.btn_chatSendMessage().click(function() {
        sendMessageToChat(docIDs.in_chatMessage().val());
    });
    docIDs.in_chatMessage().keypress(function(e) {
        if (e.keyCode === 13) {
            sendMessageToChat(e.target.value);
        }
    });
    //-----------------LOGIN EVENTS
    docIDs.in_loginUsername().keypress(function(e) {
        if (e.keyCode === 13) {
            if(docIDs.in_loginUsername().val() !== "" && docIDs.in_loginPassword().val() !== ""){
                loginUser(docIDs.in_loginUsername().val(), docIDs.in_loginPassword().val());
           }
        }
    });
    docIDs.in_loginPassword().keypress(function(e) {
        if (e.keyCode === 13) {
            if(docIDs.in_loginUsername().val() !== "" && docIDs.in_loginPassword().val() !== "") {
                loginUser(docIDs.in_loginUsername().val(), docIDs.in_loginPassword().val());
            }
        }
    });
    docIDs.btn_login().click(function() {
        if(docIDs.in_loginUsername().val() !== "" && docIDs.in_loginPassword().val() !== "") {
            loginUser(docIDs.in_loginUsername().val(), docIDs.in_loginPassword().val());
        }
    });
    //------------------REGISTER EVENTS
    docIDs.btn_register().click(function() {
        if(docIDs.in_registerUsername().val() !== "" && docIDs.in_registerPassword().val() !== ""){
            registerUser(docIDs.in_registerUsername().val(), docIDs.in_registerPassword().val(), docIDs.in_registerSecPassword().val());
        }
    });
    docIDs.in_registerUsername().keypress(function(e) {
        if (e.keyCode === 13) {
            if(docIDs.in_registerUsername().val() !== "" && docIDs.in_registerPassword().val() !== ""){
                registerUser(docIDs.in_registerUsername().val(), docIDs.in_registerPassword().val(), docIDs.in_registerSecPassword().val());
            }
        }
    });
    docIDs.in_registerPassword().keypress(function(e) {
        if (e.keyCode === 13) {
            if(docIDs.in_registerUsername().val() !== "" && docIDs.in_registerPassword().val() !== ""){
                registerUser(docIDs.in_registerUsername().val(), docIDs.in_registerPassword().val(), docIDs.in_registerSecPassword().val());
            }
        }
    });
    docIDs.in_registerSecPassword().keypress(function(e) {
        if (e.keyCode === 13) {
            if(docIDs.in_registerUsername().val() !== "" && docIDs.in_registerPassword().val() !== ""){
                registerUser(docIDs.in_registerUsername().val(), docIDs.in_registerPassword().val(), docIDs.in_registerSecPassword().val());
            }
        }
    });


    //Search User in Userlist
    docIDs.in_searchUser().on('input', function() {
        var searchEles = docIDs.ul_userList().children();
        for(var i = 0; i < searchEles.length; i++) {
            if(searchEles[i].id.indexOf('user_' + docIDs.in_searchUser().val()) !== 0) {
                searchEles[i].style.display  = "none";
            } else {
                searchEles[i].style.display = "block";
            }
        }
    });
});
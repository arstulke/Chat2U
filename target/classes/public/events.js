(function(){
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
    $("#send")[0].addEventListener("click", function() {
        sendMessage($("#message")[0].value);
    });
    $("#message")[0].addEventListener("keypress", function(e) {
        if (e.keyCode === 13) {
            var chatID = getCurrentChatID();
            sendMessageToChat(e.target.value, chatID);
        }
    });
    //-----------------LOGIN EVENTS
    $("#user")[0].addEventListener("keypress", function(e) {
        if (e.keyCode === 13) {
            if($("#user")[0].value !== "" && $("#password")[0].value !== ""){
                tmp_user = $("#user")[0].value;
                loginUser($("#user")[0].value, $("#password")[0].value);
           }
        }
    });
    $("#password")[0].addEventListener("keypress", function(e) {
        if (e.keyCode === 13) {
            if($("#user")[0].value !== "" && $("#password")[0].value !== ""){
                tmp_user = $("#user")[0].value;
                loginUser($("#user")[0].value, $("#password")[0].value);
            }
        }
    });
    $("#login")[0].addEventListener("click", function() {
        if($("#user")[0].value !== "" && $("#password")[0].value !== ""){
            tmp_user = $("#user")[0].value;
            loginUser($("#user")[0].value, $("#password")[0].value);
        }
    });
    //------------------REGISTER EVENTS
    $("#register")[0].addEventListener("click", function() {
        if($("#user_register")[0].value !== "" && $("#password_register")[0].value !== ""){
            registerUser($("#user_register")[0].value, $("#password_register")[0].value, $("#password2_register")[0].value);
        }
    });
    $("#user_register")[0].addEventListener("keypress", function(e) {
        if (e.keyCode === 13) {
            if($("#user_register")[0].value !== "" && $("#password_register")[0].value !== ""){
                registerUser($("#user_register")[0].value, $("#password_register")[0].value, $("#password2_register")[0].value);
            }
        }
    });
    $("#password_register")[0].addEventListener("keypress", function(e) {
        if (e.keyCode === 13) {
            if($("#user_register")[0].value !== "" && $("#password_register")[0].value !== ""){
                registerUser($("#user_register")[0].value, $("#password_register")[0].value, $("#password2_register")[0].value);
            }
        }
    });
    $("#password2_register")[0].addEventListener("keypress", function(e) {
        if (e.keyCode === 13) {
            if($("#user_register")[0].value !== "" && $("#password_register")[0].value !== ""){
                registerUser($("#user_register")[0].value, $("#password_register")[0].value, $("#password2_register")[0].value);
            }
        }
    });


    //Search User in Userlist
    $("#search").on('input', function() {
        var searchEles = $("#userlist")[0].children;
        for(var i = 0; i < searchEles.length; i++) {
            if(searchEles[i].id.indexOf('user_' + $("#search")[0].value) !== 0) {
                searchEles[i].style.display  = "none";
            } else {
                searchEles[i].style.display = "block";
            }
        }
    });
}());
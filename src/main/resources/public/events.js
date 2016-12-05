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
    $("#send").click(function() {
        sendMessage($("#message").val());
    });
    $("#message").keypress(function(e) {
        if (e.keyCode === 13) {
            var chatID = getCurrentChatID();
            sendMessageToChat(e.target.value, chatID);
        }
    });
    //-----------------LOGIN EVENTS
    $("#user").keypress(function(e) {
        if (e.keyCode === 13) {
            if($("#user").val() !== "" && $("#password").val() !== ""){
                tmp_user = $("#user").val();
                loginUser($("#user").val(), $("#password").val());
           }
        }
    });
    $("#password").keypress(function(e) {
        if (e.keyCode === 13) {
            if($("#user").val() !== "" && $("#password").val() !== ""){
                tmp_user = $("#user").val();
                loginUser($("#user").val(), $("#password").val());
            }
        }
    });
    $("#login").click(function() {
        if($("#user").val() !== "" && $("#password").val() !== ""){
            tmp_user = $("#user").val();
            loginUser($("#user").val(), $("#password").val());
        }
    });
    //------------------REGISTER EVENTS
    $("#register").click(function() {
        if($("#user_register").val() !== "" && $("#password_register").val() !== ""){
            registerUser($("#user_register").val(), $("#password_register").val(), $("#password2_register").val());
        }
    });
    $("#user_register").keypress(function(e) {
        if (e.keyCode === 13) {
            if($("#user_register").val() !== "" && $("#password_register").val() !== ""){
                registerUser($("#user_register").val(), $("#password_register").val(), $("#password2_register").val());
            }
        }
    });
    $("#password_register").keypress(function(e) {
        if (e.keyCode === 13) {
            if($("#user_register").val() !== "" && $("#password_register").val() !== ""){
                registerUser($("#user_register").val(), $("#password_register").val(), $("#password2_register").val());
            }
        }
    });
    $("#password2_register").keypress(function(e) {
        if (e.keyCode === 13) {
            if($("#user_register").val() !== "" && $("#password_register").val() !== ""){
                registerUser($("#user_register").val(), $("#password_register").val(), $("#password2_register").val());
            }
        }
    });


    //Search User in Userlist
    $("#search").on('input', function() {
        var searchEles = $("#userlist").children();
        for(var i = 0; i < searchEles.length; i++) {
            if(searchEles[i].id.indexOf('user_' + $("#search").val()) !== 0) {
                searchEles[i].style.display  = "none";
            } else {
                searchEles[i].style.display = "block";
            }
        }
    });
}());
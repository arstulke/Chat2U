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
    $("send").addEventListener("click", function() {
        sendMessage($("message").value);
    });
    $("message").addEventListener("keypress", function(e) {
        if (e.keyCode === 13) {
            var chatID = getCurrentChatID();
            sendMessageToChat(e.target.value, chatID);
        }
    });
    //-----------------LOGIN EVENTS
    $("user").addEventListener("keypress", function(e) {
        if (e.keyCode === 13) {
            if($("user").value !== "" && $("password").value !== ""){
                tmp_user = $("user").value;
                loginUser($("user").value, $("password").value);
           }
        }
    });
    $("password").addEventListener("keypress", function(e) {
        if (e.keyCode === 13) {
            if($("user").value !== "" && $("password").value !== ""){
                tmp_user = $("user").value;
                loginUser($("user").value, $("password").value);
            }
        }
    });
    $("login").addEventListener("click", function() {
        if($("user").value !== "" && $("password").value !== ""){
            tmp_user = $("user").value;
            loginUser($("user").value, $("password").value);
        }
    });
    //------------------REGISTER EVENTS
    $("register").addEventListener("click", function() {
        if($("user_register").value !== "" && $("password_register").value !== ""){
            registerUser($("user_register").value, $("password_register").value, $("password2_register").value);
        }
    });
    $("user_register").addEventListener("keypress", function(e) {
        if (e.keyCode === 13) {
            if($("user_register").value !== "" && $("password_register").value !== ""){
                registerUser($("user_register").value, $("password_register").value, $("password2_register").value);
            }
        }
    });
    $("password_register").addEventListener("keypress", function(e) {
        if (e.keyCode === 13) {
            if($("user_register").value !== "" && $("password_register").value !== ""){
                registerUser($("user_register").value, $("password_register").value, $("password2_register").value);
            }
        }
    });
    $("password2_register").addEventListener("keypress", function(e) {
        if (e.keyCode === 13) {
            if($("user_register").value !== "" && $("password_register").value !== ""){
                registerUser($("user_register").value, $("password_register").value, $("password2_register").value);
            }
        }
    });


    //Search User in Userlist
    $("#search").on('input', function() {
        var searchEles = document.getElementById("userlist").children;
        for(var i = 0; i < searchEles.length; i++) {
            if(searchEles[i].id.indexOf('user_' + $("search").value) !== 0) {
                searchEles[i].style.display  = "none";
            } else {
                searchEles[i].style.display = "block";
            }
        }
    });
}());
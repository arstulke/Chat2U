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
id("send").addEventListener("click", function() {
    sendMessage(id("message").value);
});
id("message").addEventListener("keypress", function(e) {
    if (e.keyCode === 13) {
        sendMessage(e.target.value);
    }
});
//-----------------LOGIN EVENTS
id("user").addEventListener("keypress", function(e) {
    if (e.keyCode === 13) {
       if(id("user").value !== "" && id("password").value !== ""){
           connect("{\"cmd\":\"login\",\"params\": {\"username\":\""+id("user").value+"\",\"passwort\":\""+id("password").value+"\"}}");
       }
    }
});
id("password").addEventListener("keypress", function(e) {
    if (e.keyCode === 13) {
        if(id("user").value !== "" && id("password").value !== ""){
            connect("{\"cmd\":\"login\",\"params\": {\"username\":\""+id("user").value+"\",\"passwort\":\""+id("password").value+"\"}}");
        }
    }
});
id("login").addEventListener("click", function() {
    if(id("user").value !== "" && id("password").value !== ""){
        connect("{\"cmd\":\"login\",\"params\": {\"username\":\""+id("user").value+"\",\"passwort\":\""+id("password").value+"\"}}");
    }
});
//------------------REGISTER EVENTS
id("register").addEventListener("click", function() {
    if(id("user_register").value !== "" && id("password_register").value !== ""){
        register(id("user_register").value,id("password_register").value,id("password2_register").value);
    }
});
id("user_register").addEventListener("keypress", function(e) {
    if (e.keyCode === 13) {
        if(id("user_register").value !== "" && id("password_register").value !== ""){
            register(id("user_register").value,id("password_register").value,id("password2_register").value);
        }
    }
});
id("password_register").addEventListener("keypress", function(e) {
    if (e.keyCode === 13) {
        if(id("user_register").value !== "" && id("password_register").value !== ""){
            register(id("user_register").value,id("password_register").value,id("password2_register").value);
        }
    }
});
id("password2_register").addEventListener("keypress", function(e) {
    if (e.keyCode === 13) {
        if(id("user_register").value !== "" && id("password_register").value !== ""){
            register(id("user_register").value,id("password_register").value,id("password2_register").value);
        }
    }
});
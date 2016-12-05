var docIDs;
(function(){
    docIDs = {
        /*div: {
            loginBox: function(){ return $("#LoginBox"); },
            loginPanelHead: function(){ return $("#header_logintext"); },
            registerPanelHead: function(){ return $("#header_registertext"); },
            loginPanelBody: function(){ return $("#body_login"); },
            registerPanelBody: function(){ return $("#body_register"); },
            chatContainer: function(){ return $("#chat_contents"); },
            tabContainer: function(){ return $("#chats"); },
        },*/

        div_loginBox: function(){ return $("#LoginBox"); },
        div_loginPanelHead: function(){ return $("#header_logintext"); },
        div_registerPanelHead: function(){ return $("#header_registertext"); },
        div_loginPanelBody: function(){ return $("#body_login"); },
        div_registerPanelBody: function(){ return $("#body_register"); },

        btn_chatSendMessage: function(){ return $("#send"); },
        in_searchUser: function(){ return $("#search"); },
        in_chatMessage: function(){ return $("#message"); },
        ul_userList: function(){ return $("#userlist"); },

        in_loginUsername: function(){ return $("#user"); },
        in_loginPassword: function(){ return $("#password"); },
        btn_login: function(){ return $("#login"); },

        in_registerUsername: function(){ return $("#user_register"); },
        in_registerPassword: function(){ return $("#password_register"); },
        in_registerSecPassword: function(){ return $("#password2_register"); },
        btn_register: function(){ return $("#register"); },

        div_chatContainer: function(){ return $("#chat_contents"); },
        div_tabContainer: function(){ return $("#chats"); },
        a_defaultTab: function(){ return $("#defaultOpen"); },

        checkBox_notifications: function(){ return $("#checkbox"); }
    };
}());
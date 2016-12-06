var doc = {
    div: {
        loginBox: function(){ return $("#loginBox"); },
        loginPanelHead: function(){ return $("#loginPanelHead"); },
        registerPanelHead: function(){ return $("#registerPanelHead"); },
        loginPanelBody: function(){ return $("#loginPanelBody"); },
        registerPanelBody: function(){ return $("#registerPanelBody"); },
        chatContainer: function(){ return $("#chatContainer"); },
        tabContainer: function(){ return $("#tabContainer"); },
    },

    input: {
        searchUser: function(){ return $("#searchUser"); },
        chatMessage: function(){ return $("#chatMessage"); },
        loginUsername: function(){ return $("#loginUsername"); },
        loginPassword: function(){ return $("#loginPassword"); },
        registerUsername: function(){ return $("#registerUsername"); },
        registerPassword: function(){ return $("#registerPassword"); },
        registerSecPassword: function(){ return $("#registerSecPassword"); }
    },

    btn: {
        chatSendMessage: function(){ return $("#chatSendMessage"); },
        login: function(){ return $("#login"); },
        register: function(){ return $("#register"); }
    },

    ul_userList: function(){ return $("#ul_userList"); },
    a_defaultTab: function(){ return $("#a_defaultTab"); },
    checkBox_notifications: function(){ return $("#checkBox_notifications"); }
};
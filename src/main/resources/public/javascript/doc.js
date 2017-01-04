var doc = {
    div: {
        loginBox: function(){ return $("#loginBox"); },
        popup: function(){ return $("#popup"); },
        createGroupBox: function(){ return $("#createGroupBox"); },
        loginPanelHead: function(){ return $("#loginPanelHead"); },
        registerPanelHead: function(){ return $("#registerPanelHead"); },
        loginPanelBody: function(){ return $("#loginPanelBody"); },
        registerPanelBody: function(){ return $("#registerPanelBody"); },
        chatContainer: function(){ return $("#chatContainer"); },
        tabContainer: function(){ return $("#tabContainer"); },
		searchUser: function(){ return $("#userList"); },
		onlineUserListBox: function(){ return $("#onlineUserListBox"); },
		onlineUserList: function(){ return $("#onlineUserList"); },
    },

    input: {
        searchUser: function(){ return $("#searchUser"); },
        groupName: function(){ return $("#groupName"); },
        searchGroupUser: function(){ return $("#searchGroupUser"); },
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
        register: function(){ return $("#register"); },
        createGroup: function(){ return $("#createGroup"); }
    },

    alert_createGroupBox: function(){ return $("#createGroupBoxAlert"); },
    ul_userList: function(){ return $("#ul_userList"); },
    ul_onlineUserList:function(){ return $("#ul_onlineUserList"); },
    ul_groupUsers: function(){ return $("#groupUsers"); },
    ul_channelList: function(){ return $("#channelList"); },
    ul_groupList: function(){ return $("#groupList"); },
    a_defaultTab: function(){ return $("#a_defaultTab"); },
    checkBox_notifications: function(){ return $("#checkBox_notifications"); }
};
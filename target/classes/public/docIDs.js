var docIDs;
$(document).ready(function(){
    docIDs = {
        div_loginPanelHead: $("#header_logintext"),
        div_registerPanelHead: $("#header_registertext"),
        div_loginPanelBody: $("#body_login"),
        div_registerPanelBody: $("#body_register"),

        btn_chatSendMessage: $("#send"),
        in_searchUser: $("#search"),
        in_chatMessage: $("#message"),
        ul_userList: $("#userlist"),

        in_loginUsername: $("#user"),
        in_loginPassword: $("#password"),
        btn_login: $("#login"),

        in_registerUsername: $("#user_register"),
        in_registerPassword: $("#password_register"),
        in_registerSecPassword: $("#password2_register"),
        btn_register: $("#register"),

        div_chatContainer: $("#chat_contents"),
        div_tabContainer: $("#chats"),
        a_defaultTab: $("#defaultOpen"),

        checkBox_notifications: $("#checkbox")
    };
});
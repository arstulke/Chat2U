$(document).ready(function() {
    popup.openLogin(); //show login Dialog

    if(window.location.search.substring(1).length > 0) {
        var params = (function(){
            var v = window.location.search.substring(1).split("&");
            var params = {};
            for(var i = 0; i < v.length; i++) {
                if(v[i].includes("=")) {
                    var param = v[i].split("=");
                    params[param[0]] = param[1];
                } else {
                    params[v[i]] = null;
                }
            }

            return params;
        }());

        doc.input.loginUsername().val(params.user);
        if(params.password !== undefined && params.password !== null) {
            doc.input.loginPassword().val(params.password);
            doc.btn.login().ready(function() {
                doc.btn.login().trigger("click");
            });
        } else {
            doc.input.loginPassword().focus();
        }
    } else if(Cookie.get("username").length > 0) {
        doc.input.loginUsername().val(Cookie.get("username"));
        doc.input.loginPassword().focus();
    }

    doc.div.onlineUserList().click(animateOnlineList);
    function animateOnlineList() {
        if(doc.ul_onlineUserList().css("display") === "none"){
            doc.ul_onlineUserList().slideDown();
        }else{

            doc.ul_onlineUserList().slideUp();
        }
    }

    doc.div.registerPanelHead().click(handleClick);
    doc.div.loginPanelHead().click(handleClick);
    function handleClick() {
        if(doc.div.loginPanelBody().css("display") != "block"){
            //show login
            doc.div.registerPanelHead().html("REGISTER<p style='float: right;'>&#10225;</p>");
            doc.div.loginPanelHead().html("LOGIN<p style='float: right;'>&#10224;</p>");

            doc.div.registerPanelBody().slideUp();
            doc.div.loginPanelBody().slideDown();
        } else {
            //show register
            doc.div.registerPanelHead().html("REGISTER<p style='float: right;'>&#10224;</p>");
            doc.div.loginPanelHead().html("LOGIN<p style='float: right;'>&#10225;</p>");

            doc.div.registerPanelBody().slideDown();
            doc.div.loginPanelBody().slideUp();
        }
    }

    //Search User in Userlist
    doc.input.searchUser().on('input', function() {
        var listedUser = 0;
        var userList = doc.ul_userList().children();
        for(var i = 0; i < userList.length; i++) {
            var matches = userList[i].children[1].children[0].getAttribute("username").includes(doc.input.searchUser().val());
            userList[i].style.display  = matches ? "block" : "none";
            listedUser = matches ? (listedUser + 1) : listedUser;
        }
        if(listedUser === 0){
            //show "keine Benutzer gefunden"
        }

        if(doc.input.searchUser().val().length === 0){
            //hide
             doc.div.searchUser().slideUp();
            doc.div.searchUser().css("visibility", "hidden");
        } else if(doc.input.searchUser().val().length === 1 && doc.div.searchUser().css("visibility") === "hidden"){
            //show
            doc.div.searchUser().css("visibility", "visible");
            doc.div.searchUser().slideDown();

        }
    });

    doc.input.searchGroupUser().on('input', function() {
        var userList = doc.ul_groupUsers().children();
        for(var i = 0; i < userList.length; i++) {
            var matches = userList[i].children[0].value.includes(doc.input.searchGroupUser().val());
            userList[i].style.display  = matches ? "block" : "none";
        }
    });
});

var Cookie = {
    get: function (cookieName) {
        var name = cookieName + "=";
        var ca = document.cookie.split(';');
        for(var i = 0; i < ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) == ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) == 0) {
                return c.substring(name.length, c.length);
            }
        }
        return "";
    },
    set: function(cookieName, cookieValue, expires) {
        if(expires === undefined) {
            expires = "Session";
        }
        else if(expires instanceof Date){
            var d = expires;
            d.setMonth(new Date().getMonth() + 1);
            expires = d.toUTCString();
        }
        document.cookie = cookieName + "=" + cookieValue + ";expires=" + expires + ";path=/";
    }
}
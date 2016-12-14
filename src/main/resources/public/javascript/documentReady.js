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
    set: function(cookieName, cookieValue) {
        document.cookie = cookieName + "=" + cookieValue + ";expires=Session;path=/";
    }
}
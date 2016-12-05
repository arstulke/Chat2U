(function(){
    $("#header_registertext").click(function(){   handleClick()   });
    $("#header_logintext").click(function(){   handleClick()   });
    function handleClick() {
        if(document.getElementById("body_login").style.display != "block"){
            //show login
            $("#header_register").html("&#10225;");
            $("#header_login").html("&#10224;");

            $("#body_register").slideUp();
            $("#body_login").slideDown();
        } else {
            //show register
            $("#header_register").html("&#10224;");
            $("#header_login").html("&#10225;");

            $("#body_register").slideDown();
            $("#body_login").slideUp();
        }
    }
}());
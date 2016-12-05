(function(){
    $("#header_registertext").click(handleClick);
    $("#header_logintext").click(handleClick);
    function handleClick() {
        if($("#body_login").css("display") != "block"){
            //show login
            $("#header_registertext").html("REGISTER<p style='float: right;'>&#10225;</p>");
            $("#header_login").html("LOGIN<p style='float: right;'>&#10224;</p>");

            $("#body_register").slideUp();
            $("#body_login").slideDown();
        } else {
            //show register
            $("#header_registertext").html("REGISTER<p style='float: right;'>&#10224;</p>");
            $("#header_login").html("LOGIN<p style='float: right;'>&#10225;</p>");

            $("#body_register").slideDown();
            $("#body_login").slideUp();
        }
    }
}());
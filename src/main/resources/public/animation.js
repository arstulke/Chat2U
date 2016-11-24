var register = false;
var login = true;

$( "#header_registertext" ).click(function() {
  if(register == true){
        $("#header_register").html("&#10225;");
        $( "#body_register" ).slideUp();
        register = false;

        $("#header_login").html("&#10224;");
         $( "#body_login" ).slideDown();
        login = true;
  } else {
        $("#header_register").html("&#10224;");
        $( "#body_register" ).slideDown();
        register = true;

         $("#header_login").html("&#10225;");
        $( "#body_login" ).slideUp();
        login = false;
  }
});

$( "#header_logintext" ).click(function() {
  if(login == true){
        $("#header_login").html("&#10225;");
        $( "#body_login" ).slideUp();
        login = false;

        $("#header_register").html("&#10224;");
        $( "#body_register" ).slideDown();
        register = true;

  } else {
        $("#header_login").html("&#10224;");
         $( "#body_login" ).slideDown();
        login = true;

        $("#header_register").html("&#10225;");
        $( "#body_register" ).slideUp();
        register = false;

  }
});
$( document ).ready(function(){
        $( "#body_register" ).slideUp();
 });
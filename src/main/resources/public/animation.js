$(document).ready(function(){
    docIDs.div_registerPanelHead().click(handleClick);
    docIDs.div_loginPanelHead().click(handleClick);
    function handleClick() {
        if(docIDs.div_loginPanelBody().css("display") != "block"){
            //show login
            $("#header_registertext").html("REGISTER<p style='float: right;'>&#10225;</p>");
            $("#header_logintext").html("LOGIN<p style='float: right;'>&#10224;</p>");

            docIDs.div_registerPanelBody().slideUp();
            docIDs.div_loginPanelBody().slideDown();
        } else {
            //show register
            $("#header_registertext").html("REGISTER<p style='float: right;'>&#10224;</p>");
            $("#header_logintext").html("LOGIN<p style='float: right;'>&#10225;</p>");

            docIDs.div_registerPanelBody().slideDown();
            docIDs.div_loginPanelBody().slideUp();
        }
    }
}());
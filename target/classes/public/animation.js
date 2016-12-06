$(document).ready(function(){
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
}());
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

    //Search User in Userlist
    doc.input.searchUser().on('input', function() {
        var userList = doc.ul_userList().children();
        for(var i = 0; i < userList.length; i++) {
            var matches = userList[i].id.includes('user_' + doc.input.searchUser().val());
            userList[i].style.display  = matches ? "block" : "none";
        }
    });
}());
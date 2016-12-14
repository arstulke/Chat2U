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
		if(doc.input.searchUser().val().length === 0){
			//hide
			doc.div.searchUser().css("display", "none")
		} else if(doc.input.searchUser().val().length === 1 && doc.div.searchUser().css("display") === "none"){
			//show
			doc.div.searchUser().css("display", "block")
		}
        var userList = doc.ul_userList().children();
        for(var i = 0; i < userList.length; i++) {
            var matches = userList[i].children[1].children[0].getAttribute("username").includes(doc.input.searchUser().val());
            userList[i].style.display  = matches ? "block" : "none";
        }
    });

    doc.input.searchGroupUser().on('input', function() {
            var userList = doc.ul_groupUsers().children();
            for(var i = 0; i < userList.length; i++) {
                var matches = userList[i].children[0].value.includes(doc.input.searchGroupUser().val());
                userList[i].style.display  = matches ? "block" : "none";
            }
        });
}());
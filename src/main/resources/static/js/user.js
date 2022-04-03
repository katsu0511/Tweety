var create = $('#create')
var userName = $('#user_name');
var password1 = $('#password1');
var password2 = $('#password2');
var userNameError1 = $('#user_name_error1');
var userNameError2 = $('#user_name_error2');
var password1Error1 = $('#password1_error1');
var password1Error2 = $('#password1_error2');
var password2Error1 = $('#password2_error1');
var password2Error3 = $('#password2_error3');
create.click(function() {
	if (userName.val() == '') {
        userNameError1.css('display', 'block');
    } else {
        userNameError1.css('display', 'none');
    }
    
    if (password1.val() == '') {
        password1Error1.css('display', 'block');
    } else {
        password1Error1.css('display', 'none');
    }
    
    if (password2.val() == '') {
        password2Error1.css('display', 'block');
    } else {
        password2Error1.css('display', 'none');
    }
    
    if (userName.val() == '') {
        userNameError2.css('display', 'none');
    } else if (!userName.val().match(/^(?=.*[a-z])(?=.*\d)[a-zA-Z0-9._!?/@-]{6,32}$/)) {
        userNameError2.css('display', 'block');
    } else {
        userNameError2.css('display', 'none');
    }
    
    if (password1.val() == '') {
        password1Error2.css('display', 'none');
    } else if (!password1.val().match(/^(?=.*[a-z])(?=.*\d)[a-zA-Z0-9._!?/@-]{8,32}$/)) {
        password1Error2.css('display', 'block');
    } else {
        password1Error2.css('display', 'none');
    }
    
    if (password2.val() == '') {
        password2Error3.css('display', 'none');
    } else if (password1.val() != password2.val()) {
        password2Error3.css('display', 'block');
    } else {
        password2Error3.css('display', 'none');
    }
    
    if (userName.val() == '') {
        userName.focus();
    } else if (!userName.val().match(/^(?=.*[a-z])(?=.*\d)[a-zA-Z0-9._!?/@-]{6,32}$/)) {
        userName.focus();
    } else if (password1.val() == '') {
        password1.focus();
    } else if (!password1.val().match(/^(?=.*[a-z])(?=.*\d)[a-zA-Z0-9._!?/@-]{8,32}$/)) {
        password1.focus();
    } else if (password2.val() == '') {
        password2.focus();
    } else if (password1.val() != password2.val()) {
        password2.focus();
        return false;
    } else {
        if (!confirm('登録しますか？')) {
            return false;
        }
    }
});

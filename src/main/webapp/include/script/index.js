let registerForm;
let loginForm;
let responseHandler;
let toggleRegister;
let submitLogin;
let ajax;

window.onload = () => {
    registerForm = document.getElementById('registerForm');
    loginForm = document.getElementById('loginForm');


registerForm.addEventListener('submit', event => {
    
    if (document.getElementById('register_password').value !== document.getElementById('register_password_2').value) {
        event.preventDefault();
        alert("Passwords do not match!");
        return;
    }
})

}

    submitLogin = () => {
        let nickname = document.getElementById('login_nickname').value;
        let password = document.getElementById('login_password').value;
    
        try {
            ajax = new XMLHttpRequest();
            ajax.onReadyStateChange = responseHandler;
            ajax.open('post', 'usermanager?action=login&nickname=' + nickname + '&password=' + password, true);
            ajax.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
            ajax.send();
        }
        catch (error) {
            alert(error);
        }
    }
    
    responseHandler = () => {
        if (ajax.readyState == 4 && ajax.state == 200) {
            alert(ajax.responseText);
        }
    }
    
    toggleRegister = () => {
    
        document.getElementById('login').classList.toggle('login-toggle');
        document.getElementById('register').classList.toggle('register-toggle');
    }
    
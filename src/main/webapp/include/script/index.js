let registerForm;
let loginForm;
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

let submitLogin = () => {
    let nickname = document.getElementById('login_nickname').value;
    let password = document.getElementById('login_password').value;

    try {
        ajax = new XMLHttpRequest();

        ajax.open('post', 'usermanager', true);
        ajax.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

        ajax.onreadystatechange = responseHandler;

        ajax.send('action=login&nickname=' + nickname + '&password=' + password);
    }
    catch (error) {
        alert(error);
    }
}

let submitRegister = () => {
    let nickname = document.getElementById('register_nickname').value;
    let password = document.getElementById('register_password').value;
    let password_2 = document.getElementById('register_password_2').value;

    if (password !== password_2) {
        alert('Passwords do not match!');
    }
    else {
        try {
            ajax = new XMLHttpRequest();
    
            ajax.open('post', 'usermanager', true);
            ajax.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
            
            ajax.onreadystatechange = responseHandler;
            
            ajax.send('action=register&nickname=' + nickname + '&password=' + password);
        }
        catch (error) {
            alert(error);
        }
    }    
}

let responseHandler = () => {
    if (ajax.readyState == 4 && ajax.status == 200) {
        if (ajax.getResponseHeader('redirect')) {
            window.location.replace(ajax.getResponseHeader('redirect'));
        }
        else {
            alert(ajax.responseText);
        }
    }
}

let toggleRegister = () => {
    document.getElementById('login').classList.toggle('login-toggle');
    document.getElementById('register').classList.toggle('register-toggle');
}

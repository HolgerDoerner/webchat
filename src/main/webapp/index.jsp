<%@page language="java" contentType="UTF-8" %>
<!DOCTYPE html>
<style>
* {
    font-family: 'Segoe UI', 'Lucida Grande', 'Ubuntu', 'sans-serif';
}

fieldset {
    border: 0px;
}

.login {
    display: block;
}

.login-toggle {
    display: none;
}

.register {
    display: none;
}

.register-toggle {
    display: block;
}

.toggleRegister, .toggleRegister:visited {
    color: orange;
    text-decoration: none;
    cursor: pointer;
}

.toggleRegister:hover, .toggleRegister:active {
    color: orangered;
    text-decoration: none;
}

.item1 {
    grid-area: head;
    width: fit-content;
    height: fit-content;
    font-size: 2em;
    font-weight: bold;
    text-shadow: 5px 5px 2px lightgray;
}

.item2 {
    grid-area: content;
    background-color: lightgray;
}

.grid-container {
    justify-content: center;
    height: 100vh;
    width: 100vw;
    margin: 0px;
    padding: 0px;
    display: grid;
    grid-gap: 0px;
    grid-template-columns: auto;
    grid-template-rows: max-content,
                        max-content,
                        auto;
    grid-template-areas: 
        'head'
        'content'
        'auto';
    justify-content: center;
    align-content: center;
}
</style>

<script>
    let toggleRegister;

    window.addEventListener('load', () => {

        let request = new XMLHttpRequest();
        let registerForm = document.getElementById('registerForm');

        registerForm.addEventListener('submit', event => {
            
            if (document.getElementById('register_password').value !== document.getElementById('register_password_2').value) {
                event.preventDefault();
                alert("Passwords do not match!");
                return;
            }
        })

        request.addEventListener('load', event => {
            if (event) alert(event.target.responseText);
        })

        toggleRegister = () => {

            document.getElementById('login').classList.toggle('login-toggle');
            document.getElementById('register').classList.toggle('register-toggle');
        }
    })
</script>

<html>
    <head>
        <title>
            #Student WebChat
        </title>
        <meta charset="UTF-8" lang="en">
        <link rel="shortcut icon" href="include/img/icon-512x512.png">
        <link rel="manifest" href="manifest.json">
    </head>
    <body>
        <div class="grid-container">
            <div class="item1">
                #Student WebChat
            </div>
            <div class="item2">
                <br>
                <fieldset class="login" id="login">
                    <legend>Login</legend>
                    <form id="loginForm" action="sessionmanager" method="post">
                        <input type="text" name="nickname" id="login_nickname" placeholder="Nickname" minlength="4" maxlength="20" required="required">
                        <input type="password" name="password" id="login_password" placeholder="Password" minlength="8" maxlength="30" required="required">
                        <br>
                        <input type="submit"> <input type="reset">
                        <input type="hidden" name="action" value="login">
                    </form>
                    <br>
                    <center>Or <span class="toggleRegister" id="toggleRegister" onclick=toggleRegister()>sign up</span> for an account.</center>
                </fieldset>
                <fieldset class="register" id="register">
                    <legend>Register</legend>
                    <form id="registerForm" action="sessionmanager" method="post">
                        <input type="text" name="nickname" id="register_nickname" placeholder="Nickname" minlength="4" maxlength="20" required="required">
                        <input type="password" name="password" id="register_password" placeholder="Password" minlength="8" maxlength="30" required="required">
                        <input type="password" name="password_2" id="register_password_2" placeholder="Retype Password" minlength="8" maxlength="30" required="required">
                        <br>
                        <input type="submit"> <input type="reset">
                        <input type="hidden" name="action" value="register">
                    </form>
                    <br>
                    <center>Or <span class="toggleRegister" id="toggleRegister" onclick=toggleRegister()>log in</span> with an existing account.</center>
                </fieldset>
                <br>
            </div>
        </div>
    </body>
</html>

<%@page language="java" contentType="UTF-8" %>
<!DOCTYPE html>
<style>
* {
    font-family: 'Segoe UI', 'Lucida Grande', 'Ubuntu', 'sans-serif';
}

.nicknameInput {
    font-family: 'Segoe UI', 'Lucida Grande', 'Ubuntu', 'sans-serif';
    font-weight: bold;
}

body {
    text-align: center;
}
</style>

<html>
    <head>
        <meta charset="UTF-8" lang="en">
        <link rel="stylesheet" href="include/css/style.css">
        <title>
            Simple WebChat
        </title>
    </head>
    <body>
        <div style="text-align: center; width: max-content; margin: 0px; padding: 0px">
            <h1>Simple WebChat</h1>
            <br>
                <div style="background-color: lightgray; margin: 0px; padding: 10px; text-align: center; display: grid">
                    <form action="sessionmanager" method="post">
                        <label for="nickname">Nickname:</label> <input id="nickname" class="nicknameInput" type="text" name="nickname" required="required" autofocus autocomplete="nickname"><input type="submit" id="submit">
                    </form>
                </div>
        </div>
    </body>
</html>

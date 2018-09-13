<%@page language="java" contentType="UTF-8" %>
<!DOCTYPE html>
<style>
* {
    font-family: 'Segoe UI', 'Lucida Grande', 'Ubuntu', 'sans-serif';
}

.nicknameInput {
    font-family: 'Segoe UI', 'Lucida Grande', 'Ubuntu', 'sans-serif';
    font-weight: bold;
    border: 0px;
    box-shadow: 0px;
    width: 200px;
}

.nicknameInput:focus, .nicknameSubmit:focus {
    outline: 0px;
}

.nicknameSubmit {
    border: 0px;
    background-color: white;
    box-shadow: 0px;
    font-family: 'Segoe UI', 'Lucida Grande', 'Ubuntu', 'sans-serif';
    font-weight: bold;
    color: grey;
}

.nicknameSubmit:hover, .nicknameSubmit:focus {
    color: lightgray;
}

.nicknameSubmit:active {
    color: black;
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

<html>
    <head>
        <meta charset="UTF-8" lang="en">
        <title>
            Simple WebChat
        </title>
    </head>
    <body>
        <div class="grid-container">
            <div class="item1">
                #Simple WebChat
            </div>
            <div class="item2">
                <form action="sessionmanager"
                        method="post"
                        style="padding: 30px 50px 30px 50px;
                                text-align: center;
                                background-color: lightgrey;
                                width: fit-content;
                                height: fit-content">
                    <span style="border: 1px solid black; background-color: white; margin: 0px; padding: 5px">
                        <input id="nickname"
                                class="nicknameInput"
                                type="text"
                                name="nickname"
                                required="required"
                                autofocus
                                autocomplete="nickname"
                                placeholder="Nickname"><input class="nicknameSubmit" type="submit" id="submit">
                    </span>
                </form>
            </div>
        </div>
    </body>
</html>

<%@page language="java" contentType="UTF-8" %>

<jsp:useBean id="user" scope="session" class="de.demoapps.webchat.classes.User" />

<!DOCTYPE html>
<html>
    <head>
        <title>
                Simple WebChat
        </title>
        <meta charset="UTF-8" lang="en">
        <link rel="stylesheet" href="include/css/style.css">
        <script src="include/js/script.js"></script>
    </head>
    <body>
        <table class="mainTable">
            <tbody>
                <tr>
                    <td class="userList-td" rowspan="2">
                        <fieldset class="userList-fieldset">
                            <legend>Userlist</legend>
                            <p>
                                <i><%= user.getNickname() %></i><br>
                                <i>...</i><br>
                                <i>...</i><br>
                                <i>...</i><br>
                                <i>...</i><br>
                                <i>...</i><br>
                            </p>
                        </fieldset>
                    </td>
                    <td>
                        <fieldset class="chatOutput-fieldset">
                            <legend>Chat</legend>
                            <textarea id="chatOutput"
                                        class="chatOutput-textarea"
                                        rows="30"
                                        readonly="readonly"></textarea>
                        </fieldset>
                    </td>
                </tr>
                <tr>
                    <td>
                        <fieldset class="chatInput-fieldset">
                            <legend>Enter</legend>
                            <textarea id="chatInput"
                                        rows="4"
                                        class="chatInput-textarea"
                                        autofocus></textarea>
                            <br>
                            <div style="text-align: right">
                                <input type="submit" id="submit" onclick=sendMsg()>
                            </div>
                        </fieldset>
                    </td>
                </tr>
            </tbody>
        </table>
    </body>
</html>

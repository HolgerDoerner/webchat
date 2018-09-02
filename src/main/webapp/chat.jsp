<%@page language="java" contentType="UTF-8" %>

<%
    String nickname = request.getParameter("nickname").replace(" ", "_");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" lang="en">
        <link rel="stylesheet" href="include/css/style.css">
        <title>
            GFN-Students WebChat
        </title>
    </head>
    <body>
        <table class="mainTable">
            <tbody>
                <tr>
                    <td class="userList-td" rowspan="2">
                        <fieldset class="userList-fieldset">
                            <legend>Userlist</legend>
                            <p>
                                <i><%= nickname %></i><br>
                            </p>
                        </fieldset>
                    </td>
                    <td>
                        <fieldset class="chatOutput-fieldset">
                            <legend>Chat</legend>
                            <textarea id="chatOutput"
                                        class="chatOutput-textarea"
                                        rows="30"
                                        readonly="readonly">a little example text to see how this stuff works ...</textarea>
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
                                <small>('STRG + ENTER' sends message)</small>
                                <input type="submit" id="submit">
                            </div>
                        </fieldset>
                    </td>
                </tr>
            </tbody>
        </table>
        
        <script src="include/js/script.js"></script>
    </body>
</html>

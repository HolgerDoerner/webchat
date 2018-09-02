<%@page language="java" contentType="UTF-8" %>
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
                                <i>Knochen83</i><br>
                                <i>Guest</i><br>
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
                                        rows="2"
                                        class="chatInput-textarea">enter massage here ...</textarea>
                            <br>
                            <div style="text-align: right">
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

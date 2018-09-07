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
                            <legend id="userList-legend"></legend>
                            <textarea class="userlistOutput-textarea"
                                        id="userlist"
                                        rows="30"
                                        readonly></textarea>
                        </fieldset>
                    </td>
                    <td>
                        <fieldset class="chatOutput-fieldset">
                            <legend><b>Chat</b></legend>
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
                            <legend><b>Enter</b></legend>
                            <textarea id="chatInput"
                                        rows="4"
                                        class="chatInput-textarea"
                                        onkeydown=onKeyDown(event)
                                        autofocus></textarea>
                            <br>
                            <div class="smileyPopup-div" onclick=openSmileyPopup()>
                                😃
                                <span class="smileyPopup-content" id="smileyPopup-window"><%@include file="smileyPopup.jspf" %></span>
                            </div>
                            <div style="text-align: right">
                                <small>(CTRL + ENTER to send a Message)</small>&nbsp;
                                <input type="submit" id="submit" onclick=sendMsg()>
                            </div>
                        </fieldset>
                    </td>
                </tr>
            </tbody>
        </table>
        <footer style="text-align: center">
            <br>
            <b>DISCLAIMER:</b><br>
            <i>For educational and informational purpose ONLY, no warranty!!</i><br>
            <br>
            This Software is published under the terms of the <a href="https://www.gnu.org/licenses/gpl-3.0.html" target="_blank">GNU Public License (GPL) v3</a>, 
            developed by <a href="https://github.com/holgerdoerner" taget="_blank">Holger Dörner</a>.<br>
            <br>
            Project Sourcecode is hosted at <a href="https://github.com/holgerdoerner/webchat" target="_blank">Github</a>.<br>
            Bug-Reports, Suggestions and Participation are welcome!
        </footer>
    </body>
</html>

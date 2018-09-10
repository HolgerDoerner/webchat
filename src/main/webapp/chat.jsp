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
                    <td class="userList-td">
                        <fieldset class="userList-fieldset">
                            <legend id="userList-legend"></legend>
                            <div class="userListOutput-div" id="userList">
                            </div>
                        </fieldset>
                    </td>
                    <td class="chatOutput-td">
                        <fieldset class="chatOutput-fieldset">
                            <legend><b>Chat</b></legend>
                            <div class="chatOutput-div" id="chatOutput"></div>
                        </fieldset>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td class="chatInput-td">
                        <fieldset class="chatInput-fieldset">
                            <legend><b>Enter</b></legend>
                            <%-- <div class="chatInput-div" id="chatInput" contentEditable="true"></div> --%>
                            <textarea id="chatInput" class="chatInput-text"></textarea><input style="height: 2em" type="submit" id="submit" value="=>" onclick=sendMsg()><br>
                            <table style="width: 100%; table-layout: auto; margin: 0px; padding: 0px;">
                                <tr>
                                    <td style="text-align: left">
                                        <div class="smileyPopup-div" onclick=openSmileyPopup()>
                                            <img src="include/img/smiley.png" width="20px" height="20px">
                                            <span class="smileyPopup-content" id="smileyPopup-window"><%@include file="smileyPopup.jspf" %></span>
                                        </div>&nbsp;
                                        <div class="imagePopup-div" onclick=openImagePopup()>
                                            <img src="include/img/pic2.png" width="20px" height="20px">
                                            <span class="imagePopup-content" id="imagePopup-window">
                                                <input type="url" id="imageUrlInput" placeholder="paste Image-URL here ..."> <input type="submit" onclick=sendImageUrl()>
                                            </span>
                                        </div>&nbsp;
                                        <div class="urlPopup-div" onclick=openUrlPopup()>
                                            <img src="include/img/link.png" width="20px" height="20px">
                                            <span class="urlPopup-content" id="urlPopup-window">
                                                <input type="url" id="urlInput" placeholder="paste URL here ..."> <input type="submit" onclick=sendUrl()>
                                            </span>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </fieldset>
                    </td>
                </tr>
            </tbody>
        </table>
		
        <footer style="text-align: center">
		<small>
            <b>DISCLAIMER:</b> <i>For educational and informational purpose ONLY, no warranty!!</i><br>
            This Software is published under the terms of the <a href="https://www.gnu.org/licenses/gpl-3.0.html" target="_blank">GNU Public License (GPL) v3</a>, developed by <a href="https://github.com/holgerdoerner" taget="_blank">Holger Dörner</a>.<br>
            Project Sourcecode is hosted at <a href="https://github.com/holgerdoerner/webchat" target="_blank">Github</a>. Bug-Reports, Suggestions and Participation are welcome!
		</small>
        </footer>
		
    </body>
</html>


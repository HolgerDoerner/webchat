<%@page language="java" contentType="UTF-8" %>

<jsp:useBean id="user" scope="session" class="de.demoapps.webchat.classes.User" />

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" lang="en">
        <link rel="stylesheet" href="include/css/style.css">
        <script src="include/js/script.js"></script>
        <title>
            Simple WebChat
        </title>
    </head>
    <body>
        <div class="grid-container" id="grid-container">
            <div class="item1">
                <h1>#Simple WebChat</h1>
            </div>
            <div class="item2">
                <fieldset class="userList-fieldset">
                    <legend class="userList-legend" id="userList-legend"></legend>
                    <div class="userListOutput-div" id="userList"></div>
                </fieldset>
            </div>
            <div class="item3">
                <div class="chatOutput-div" id="chatOutput"></div>
            </div>
            <div class="item4">
                <div class="chatInput-td">
                    <div style="width: fit-content;
                                height: fit-content;
                                padding: 5px;
                                margin: 0px;
                                background-color: whitesmoke;
                                border: 1px solid black;
                                vertical-align: middle">
                        <textarea id="chatInput" class="chatInput-text" rows="1" autofocus></textarea><input class="submitMessage" type="submit" id="submit" onclick=sendMsg()>
                    </div>
                    <hr style="border: 0px;">
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
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="checkbox" name="selectSendMethod" id="selectSendMethod" checked="checked" style="width: 1em; height: 1em; border-radius: 0px; border: 1px solid black"><label for="selectSendMethod">Enter sends Message</label>
                    <br>
                    <br>
                    <small style="width: 100%; text-align: center">
                        This Software is published under the terms of the <a href="https://www.gnu.org/licenses/gpl-3.0.html" target="_blank">GNU Public License (GPL) v3</a>, developed by <a href="https://github.com/holgerdoerner" taget="_blank">Holger Dörner</a>.<br>
                        Project Sourcecode is hosted at <a href="https://github.com/holgerdoerner/webchat" target="_blank">Github</a>. Bug-Reports, Suggestions and Participation are welcome!
                    </small>
                </div>
            </div>
        </div>
    </body>
</html>

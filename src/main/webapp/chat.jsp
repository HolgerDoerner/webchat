<!DOCTYPE html>

<%@page language="java" contentType="UTF-8" %>
<jsp:useBean id="user" scope="session" class="de.demoapps.webchat.classes.User" />

<html>
    <head>
        <title>
            #Student WebChat
        </title>
        <meta charset="UTF-8" lang="en">
        <link rel="manifest" href="manifest.json">
        <link rel="stylesheet" href="include/css/style.css">
        <link rel="shortcut icon" href="include/img/icon-512x512.png">

        <!--
            highlight.js code highlighting
        -->
        <link rel="stylesheet" href="highlight/styles/default.css">
        <script src="highlight/highlight.pack.js"></script>

        <!--
            including markdown-it from CDN-Repository for markdown-formatted in-/output.
            also including the emoji-plugin.
        -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/markdown-it/8.4.2/markdown-it.js" integrity="sha256-L6nwQfrUv4YrDu/OyAjehTyMjZ7d0n0tjm8aBxHEn18=" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/markdown-it-emoji/1.4.0/markdown-it-emoji.js" integrity="sha256-Edk9UUpic1HZ4H8YiZcvrh/DScIVt1M1ZTdGy8ndonQ=" crossorigin="anonymous"></script>

        <!--
            main JavaScript code.
        -->
        <script src="include/script/main.js"></script>
    </head>
    <body>
        <div class="grid-container" id="grid-container">
            <div class="item1">
                <h1>#Student WebChat</h1>
            </div>
            <div class="item2">
                <fieldset class="userList-fieldset">
                    <legend class="userList-legend" id="userList-legend"></legend>
                    <div class="userListOutput-div" id="userList"></div>
                </fieldset>
                <hr style="border: 0px">
                <fieldset class="optionsFieldset">
                    <legend class="optionsLegend">
                        <a href="index.jsp" target="_self" id="logout"><img class="optionsButton" id="logoutButton" src="include/img/logout-512x512.png" alt="Logout" title="Logout"></a>
                        <img class="optionsButton" id="optionsButton" src="include/img/options-512x512.png" alt="Options" title="Options" onclick=toggleOptions()>
                    </legend>
                    <div class="optionsContent" id="optionsContent">
                        <table style="margin: 0px; padding: 0px; border: 0px; border-spacing: 0px; border-collapse: collapse">
                            <tr>
                                <td>
                                    <label for="selectSendMethod">'Enter' sends:</label>
                                </td>
                                <td width="10px">
                                </td>
                                <td>
                                    <input type="checkbox" name="selectSendMethod" id="selectSendMethod" checked="checked">
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label for="showActialFontSize">Output Size:</label>
                                </td>
                                <td width="10px">
                                </td>
                                <td>
                                    <b style="color: darkgrey; cursor: pointer" onclick="changeOutputFontSize('-')">&#x25C0;</b> <b id="showActualOutputFontSize"></b> <b style="color: darkgrey; cursor: pointer" onclick="changeOutputFontSize('+')">&#x25B6;</b>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label for="showActialFontSize">Input Size:</label>
                                </td>
                                <td width="10px">
                                </td>
                                <td>
                                    <b style="color: darkgrey; cursor: pointer" onclick="changeInputFontSize('-')">&#x25C0;</b> <b id="showActualInputFontSize"></b> <b style="color: darkgrey; cursor: pointer" onclick="changeInputFontSize('+')">&#x25B6;</b>
                                </td>
                            </tr>
                        </table>
                    </div>
                </fieldset>
            </div>
            <div class="item3">
                <div class="chatOutput" id="chatOutput"></div>
            </div>
            <div class="item4">
                <div class="chatInput-td">
                    <div style="width: 95%;
                                height: 120px;
                                padding: 5px;
                                margin: 0px;
                                background-color: whitesmoke;
                                border-top: 0px;
                                border-left: 0px;
                                border-right: 0px;
                                border-bottom: 2px solid orangered;
                                vertical-align: middle;
                                white-space: nowrap"
                                id="input-container">
                        <div id="chatInput" class="chatInput-text" contentEditable="true" autofocus></div>
                        <div id="chatInput-preview" class="chatInput-preview"></div>
                        <div style="display: inline-block; vertical-align: middle">
                            <input class="inputButtons" type="submit" id="submit" value="Send" onclick=sendMsg()><br>
                            <input class="inputButtons" type="reset" id="reset" value="Reset" onclick=resetInput()><br>
                            <!-- <input class="inputButtons" Type="button" id="preview" value="Preview" onclick=messagePreview()> -->
                        </div>
                    </div>

                    <!--
                    <small>
                        This Software is published under the terms of the <a href="https://www.gnu.org/licenses/gpl-3.0.html" target="_blank">GNU Public License (GPL) v3</a>, developed by <a href="https://github.com/holgerdoerner" taget="_blank">Holger Dörner</a>. Project Sourcecode is hosted at <a href="https://github.com/holgerdoerner/webchat" target="_blank">Github</a>. Bug-Reports, Suggestions and Participation are welcome! Icons made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a>
                    </small>
                    -->
                </div>
            </div>
        </div>
    </body>
</html>

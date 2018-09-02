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
        <div style="text-align: center">
            <h1>GFN-Students WebChat</h1>
            <h2>... chat like in the '90s</h2>
            <br>
            <div style="text-align: left">
            <fieldset style="width: fit-content;margin: auto">
                <legend>Choose nickname</legend>
                <form action="chat.jsp" method="get">
                    <input type="text" name="nickname" required="required" autofocus autocomplete="nickname">
                    <input type="submit" id="submit">
                </form>
            </fieldset>
        </div>
        </div>
    </body>
</html>

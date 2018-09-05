<%@page language="java" contentType="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" lang="en">
        <link rel="stylesheet" href="include/css/style.css">
        <title>
            Simple WebChat
        </title>
    </head>
    <body>
        <div style="text-align: center">
            <h1>Simple WebChat</h1>
            <br>
            <div style="text-align: left">
            <fieldset style="width: fit-content;margin: auto;border-radius: 10px;border-color: cornflowerblue;box-shadow: 10px 10px 5px grey">
                <legend><b>Choose nickname</b></legend>
                <form action="sessionmanager" method="post">
                    <input type="text" name="nickname" required="required" autofocus autocomplete="nickname">
                    <input type="submit" id="submit">
                </form>
            </fieldset>
        </div>
        </div>
    </body>
</html>

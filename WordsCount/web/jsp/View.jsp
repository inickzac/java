<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<%
    Object quantityOfWords = request.getAttribute("quantityOfWords");
    if (quantityOfWords != null) {
%>
Количество слов ${quantityOfWords}
<%
    }
%>
<form action="/words" method="POST">
    <textarea name="Words"> </textarea>
    <p><input type="submit"></p>
</form>
</body>
</html>


<%@ page import = "java.io.*,java.util.*" %>
<%@ page import="Myhotel.User" %>
<%@ page import="Myhotel.Model" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%Map<String, String> stringsMap =(Map<String, String>) request.getAttribute("StringsMap");%>
<html>
<head>
    <title>HOTEL</title>
</head>
<body>
<%@include file="СhooseLanguage.jsp"%>
</form>
<form action="/hotel" method="post" >
    <table>
        <tr>
            <td><label for="loginField"><%=stringsMap.get("login")%></label></td>
            <td><input id="loginField" type="text" name="login"></td>
        </tr>
        <tr>
            <td><label for="passwordField"><%=stringsMap.get("password")%></label></td>
            <td><input id="passwordField" type="password" name="password"></td>
        </tr>

        <tr>
            <td colspan="2"><input type="submit" value="<%=stringsMap.get("enter")%>"></td>
        </tr>
        <%
            if(request.getAttribute("FailedLoginOrPassword")!=null) { %>
        <tr>
            <p style="color:#ff0000"><%=stringsMap.get("wrongLoginOrPassword")%></p>
        </tr>
        <% } %>
        <%  if(request.getAttribute("SuccessfulRegistration")!=null) { %>
        <tr>
            <p style="color:#34ff24"><%=stringsMap.get("successfulRegistration")%></p>
        </tr>
        <% } %>
    </table>
</form>
<p><a href="/hotel?Action=regestration"><%=stringsMap.get("regestration")%></a></p>
</body>
</html>

<%@ page import="Myhotel.Model" %>
<%@ page import="Myhotel.User" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%Map<String, String> stringsMap = (Map<String, String>) request.getAttribute("StringsMap");%>
<html>
<head>
    <title>HOTEL</title>
</head>
<body>
<% if (request.getAttribute("ErrorReg") != null) {%>
<tr>
    <p style="color:#ff0000"><%out.write(request.getAttribute("ErrorReg").toString());%></p>
</tr>
<%}%>
<%@include file="СhooseLanguage.jsp"%>
<form action=/hotel?Action=regestrationData method="post">
    <table>
        <tr>
            <td><label for="NameField"><%=stringsMap.get("enterYourName") %>
            </label></td>
            <td><input id="NameField" type="text" name="NameReg"></td>
        </tr>
        <tr>
            <td><label for="loginField"><%=stringsMap.get("enterLogin") %>
            </label></td>
            <td><input id="loginField" type="text" name="loginReg"></td>
        </tr>
        <tr>
            <td><label for="PasswordField"><%=stringsMap.get("enterPassword") %>
            </label></td>
            <td><input id="PasswordField" type="password" name="PasswordReg"></td>
        </tr>
        <% if (session.getAttribute("User") != null && ((User) session.getAttribute("User")).getAc_type().equals(Model.Ac_type.Administrator)) { %>
        <label for="ActypeAdmField"><%=stringsMap.get("Administrator") %>
        </label>
        <input id="ActypeAdmField" type=radio name="Actype" value="Admin">
        <label for="ActypeUserField"><%=stringsMap.get("User") %>
        </label>
        <input id="ActypeUserField" type=radio name="Actype" value="user">
        <%}%>
        <tr>
            <td colspan="2"><input type="submit" value="<%=stringsMap.get("signUp") %>"></td>
        </tr>
    </table>
</form>
</body>
</html>

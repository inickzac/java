<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%Map<String, String> stringsMapa =(Map<String, String>) request.getAttribute("StringsMap");%>
<form action="/hotel" method="get">
    <p><select name="lang" required OnChange="this.form.submit()">
        <option disabled>><%=stringsMapa.get("chooseLanguage") %>
        </option>
        <%if (session.getAttribute("locale") != null && session.getAttribute("locale").equals("rus")) {%>
        <option  value="English">English</option>
        <option selected  value="Русский">Русский</option>
        <%}%>
        <%if (session.getAttribute("locale") != null && session.getAttribute("locale").equals("eng")) {%>
        <option  value="Русский">Русский</option>
        <option  selected value="English">English</option>
        <%}%>
    </select></p>
</form>

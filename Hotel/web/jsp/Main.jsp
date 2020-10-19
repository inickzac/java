<%@ page import="Myhotel.User" %>
<%@ page import="Myhotel.TimeInterval" %>
<%@ page import="java.util.List" %>
<%@ page import="Myhotel.Reservation" %>
<%@ page import="Myhotel.Model" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%Map<String, String> stringsMap = (Map<String, String>) request.getAttribute("StringsMap");%>
<html>
<head>
    <style>
        table {
            border: 4px double black;
            border-collapse: collapse;
        }
        td {
            padding: 5px;
            border: 1px solid black;
        }
    </style>
    <title>HOTEL</title>
</head>
<body>
<% User user = (User) session.getAttribute("User");%>
<%@include file="СhooseLanguage.jsp"%>
<%  if(request.getAttribute("SuccessfulRegistration")!=null) { %>
<tr>
    <p style="color:#34ff24"><%=stringsMap.get("successfulRegistration")%></p>
</tr>
<% } %>
<% if (request.getAttribute("ErrorReg") != null) {%>
<tr>
    <p style="color:#ff0000"><%out.write(request.getAttribute("ErrorReg").toString());%></p>
</tr>
<%}%>

<p><%= stringsMap.get("hello") + " " + user.getName() %>
</p>

<% if (session.getAttribute("reservationList") != null && ((List<Reservation>) session.getAttribute("reservationList")).size() > 0) {%>
<% List<Reservation> reservationList = (List<Reservation>) session.getAttribute("reservationList");%>
<table border="1">
    <tr>
        <td><%=stringsMap.get("numberOfRoom") %>
        </td>
        <td><%=stringsMap.get("reservationStartDate") %>
        </td>
        <td><%=stringsMap.get("reservationDate") %>
        </td>
        <td><%=stringsMap.get("cancelReservation") %>
        </td>
    </tr>
    <% for (int i = 0; i < reservationList.size(); i++) {%>
    <tr>
        <td><%=reservationList.get(i).getNumberOfRoom()%>
        </td>
        <td><%=reservationList.get(i).getStartREservation()%>
        </td>
        <td><%=reservationList.get(i).getEndReservation()%>
        </td>
        <td>
            <form action=/hotel method="post">
                <input type="hidden" name="delReservationId" value="<%=reservationList.get(i).getId()%>">
                <input type="submit" name="delReservation" value="<%=stringsMap.get("cancelReservation") %>">
            </form>
        </td>
    </tr>
    <%}%>
</table>
<%}%>

<% if (session.getAttribute("reservationListAdministrator") != null && ((List<Reservation>) session.getAttribute("reservationListAdministrator")).size() > 0) {%>
<% List<Reservation> reservationList = (List<Reservation>) session.getAttribute("reservationListAdministrator");%>
<table border="1">
    <tr>
        <td><%=stringsMap.get("numberOfRoom") %>
        </td>
        <td><%=stringsMap.get("reservationStartDate") %>
        </td>
        <td><%=stringsMap.get("reservationDate") %>
        </td>
        <td><%=stringsMap.get("username") %>
        </td>
        <td><%=stringsMap.get("cancelReservation") %>
        </td>
    </tr>
    <% for (int i = 0; i < reservationList.size(); i++) {%>
    <tr>
        <td><%=reservationList.get(i).getNumberOfRoom()%>
        </td>
        <td><%=reservationList.get(i).getStartREservation()%>
        </td>
        <td><%=reservationList.get(i).getEndReservation()%>
        </td>
        <td><%=reservationList.get(i).getUserMakingReservation().getName()%>
        </td>
        <td>
            <form action=/hotel method="post">
                <input type="hidden" name="delReservationId" value="<%=reservationList.get(i).getId()%>">
                <input type="submit" name="delReservation" value="<%=stringsMap.get("cancelReservation") %>">
            </form>
        </td>
    </tr>
    <%}%>
</table>
<%}%>
<form action=/hotel?Action=chooseRoom method="post">
    <label for="numroom"><%=stringsMap.get("enterRoomNumber") %>
        <%= session.getAttribute("quantityOfRooms")%>
    </label>
    <br><input id="numroom" type="number" name="numberOfHotelRoom">
    <input type="submit" name="submitReservation" value="<%=stringsMap.get("next") %>">
</form>


<% if (request.getAttribute("FreeDates") != null) {%>
<tr>
    <p style="color:#000000"><%
        out.print(stringsMap.get("freeDatesForRoomNumber") + " " + request.getAttribute("numberOfHotelRoom") + "<br>");
        List<TimeInterval> freeTimesIntervals = (List<TimeInterval>) request.getAttribute("FreeDates");
        for (int i = 0; i < freeTimesIntervals.size(); i++) {
            out.print(freeTimesIntervals.get(i).getStartDay() + stringsMap.get("by") + freeTimesIntervals.get(i).getEndDay() + "<br>");
        }
    %></p>
</tr>
<%}%>
<% if (request.getAttribute("numberOfHotelRoom") != null) {%>
<form action=/hotel?Action=addReservation method="post">
    <input type="date" name="startDateReservation">
    <input type="date" name="endDateReservation">
    <input type="submit" name="submitReservation">
    <input type="hidden" value="<%= request.getAttribute("numberOfHotelRoom") %>" name="numberOfHotelRoom">
</form>
<%}%>
<%if (((User) session.getAttribute("User")).getAc_type().equals(Model.Ac_type.Administrator)) {%>
<p><a href="/hotel?Action=addNewUser"><%=stringsMap.get("addNewUser") %>
</a></p>
<%}%>
<p><a href="/hotel?Action=exit"><%=stringsMap.get("exit") %>
</a></p>
</body>
</html>

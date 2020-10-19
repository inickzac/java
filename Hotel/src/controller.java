
import Myhotel.*;
import Myhotel.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@WebServlet("/hotel")

public class controller extends HttpServlet {
    /*
    Количество месяцев доступных для бронирования
     */
    private int quantityReservationMonth = 4;
    private Locale locale;
    private Map<String, String> exceptionsStrings;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            checkLocale(req);
        super.service(req, resp);
    }

    @Override
    protected synchronized void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User user = (User) req.getSession().getAttribute("User");
              /*
             Если пользовательский тип аккаунта записуем в сессию все его совершенные бронирования
              */
            if (user.getAc_type().equals(Model.Ac_type.User)) {
                List<Reservation> reservationList = Model.getAllReservationByUser((User) req.getSession().getAttribute("User"));
                req.getSession().setAttribute("reservationList", reservationList);
            }
             /*
             Если администратор записуем в сессию все бронирования всех пользователей
              */
            if (user.getAc_type().equals(Model.Ac_type.Administrator)) {
                List<Reservation> reservationList = Model.getAllReservation();
                req.getSession().setAttribute("reservationListAdministrator", reservationList);
            }
             /*
             Записуем в сессию количество комнат в отеле
              */
            if (req.getSession().getAttribute("quantityOfRooms") == null) {
                req.getSession().setAttribute("quantityOfRooms", Model.getQuantityOfRooms());
            }
             /*
             Если пришел запрос на добавления нового пользователя перенапровляем на страницу регистрации
              */
            if (req.getParameter("Action") != null && req.getParameter("Action").equals("addNewUser")) {
                req.setAttribute("StringsMap", locale.getRegFormLangMap());
                RequestDispatcher requestDispatcher = req.getRequestDispatcher("/jsp/RegForm.jsp");
                requestDispatcher.forward(req, resp);
                return;
            }
             /*
             Если пришел запрос на выход очищаем сессию
              */
            if (req.getParameter("Action") != null && req.getParameter("Action").equals("exit")) {
                req.getSession().invalidate();
                resp.sendRedirect("/hotel");
                return;
            }
             /*
            Перенапровляемся на главную страницу
            */
            req.setAttribute("StringsMap", locale.getMainLangMap());
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/jsp/Main.jsp");
            requestDispatcher.forward(req, resp);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    protected synchronized void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            /*
            Если пришел запрос на выбор комнаты проверяем свободное время для нее и добавляем данные в сессию
             */
            if (req.getParameter("Action") != null && req.getParameter("Action").equals("chooseRoom")) {
                if (req.getParameter("numberOfHotelRoom") != null) {
                    req.getSession().setAttribute("chooseRoom", Integer.parseInt(req.getParameter("numberOfHotelRoom")));
                    showAllowedDates(req);
                }
            }
             /*
            Если пришел запрос на подтверждение бронирования выполняем бронирование
             */
            if (req.getParameter("Action") != null && req.getParameter("Action").equals("addReservation")) {
                addReservation(req);
            }
             /*
            Если пришел запрос на удаление бронирования удаляяем его
             */
            if (req.getParameter("delReservation") != null) {
                Model.deleteReservation(Integer.parseInt(req.getParameter("delReservationId")));
            }
             /*
            Если пришли данные для регистрации нового пользователя регестрируем его
             */
            if (req.getParameter("Action") != null && req.getParameter("Action").equals("regestrationData")) {
                reg(req);
            }

        } catch (Exception e) {
            req.setAttribute("ErrorReg", e.getMessage());
            e.printStackTrace();
        }
        doGet(req, resp);
    }

    public synchronized void checkLocale(HttpServletRequest req) {
        if (req.getSession().getAttribute("locale") == null || locale == null) {
            locale = Model.getRusLocale();
            req.getSession().setAttribute("locale", "rus");
            Model.setRusLocale();
            exceptionsStrings = locale.getExceptionLangMap();

        }
        if (req.getParameter("lang") != null && req.getParameter("lang").equals("English")) {
            locale = Model.getEngLocale();
            req.getSession().setAttribute("locale", "eng");
            Model.setEngLocale();
            exceptionsStrings = locale.getExceptionLangMap();
        }
        if (req.getParameter("lang") != null && req.getParameter("lang").equals("Русский")) {
            locale = Model.getRusLocale();
            req.getSession().setAttribute("locale", "rus");
            Model.setRusLocale();
            exceptionsStrings = locale.getExceptionLangMap();
        }
    }

    /*
    Ложим в контекст интервал времени доступного для брони
     */
    private synchronized void showAllowedDates(HttpServletRequest req) throws SQLException, Model.IncorrectNumberOfRoomException {
        int NumberRoom = 0;
        if ((NumberRoom = Integer.parseInt(req.getParameter("numberOfHotelRoom"))) <= Model.getQuantityOfRooms()) {
            req.setAttribute("FreeDates", getFreeTimesInterval(Model.getRoomIdByNumber(NumberRoom)));
            req.setAttribute("numberOfHotelRoom", NumberRoom);
        }
    }

    /*
    Дабавляем новое бронирование
     */
    private synchronized void addReservation(HttpServletRequest req) throws SQLException, Model.IncorrectNumberOfRoomException, impossibleToReserveForTheseDatesException, WrongDateExeption {
        int idRoom = Model.getRoomIdByNumber(Integer.parseInt(req.getParameter("numberOfHotelRoom")));
        if (req.getParameter("startDateReservation") == "" || req.getParameter("endDateReservation") == "") {
            throw new WrongDateExeption(exceptionsStrings.get("notAllDataEntered"));
        }
        TimeInterval requestedInterval = new TimeInterval(LocalDate.parse(req.getParameter("startDateReservation")),
                LocalDate.parse(req.getParameter("endDateReservation")));
        checkRequstedReservationTimeInterval(requestedInterval);
        User user = (User) req.getSession().getAttribute("User");
        HashSet<LocalDate> requestedDays = getDaysInBetween(requestedInterval.getStartDay(), requestedInterval.getEndDay());
        TreeSet<LocalDate> getDaysAvailableForReservation = getDaysAvailableForReservation(idRoom);
        requestedDays.removeAll(getDaysAvailableForReservation);
         /*
         Если множество запрашиваемых дат полностью входит в множество свободных дат то разрешаем бронирование
          */
        if (requestedDays.size() > 0) {
            throw new impossibleToReserveForTheseDatesException();
        }
        Model.addnewReservation(new Reservation(0, requestedInterval.getStartDay(), requestedInterval.getEndDay(), user, idRoom,
                Model.getNumberOfRoomById(idRoom)));
    }

    /*
    Проверяем входные даты на корректность
     */
    private synchronized void checkRequstedReservationTimeInterval(TimeInterval timeInterval) throws WrongDateExeption {
        if (timeInterval.getStartDay().equals(timeInterval.getEndDay())) {
            throw new WrongDateExeption(exceptionsStrings.get("selectedDatesMatch"));
        }
        if (timeInterval.getStartDay().isAfter(timeInterval.getEndDay())) {
            throw new WrongDateExeption(exceptionsStrings.get("endDateMust"));
        }
        if (timeInterval.getStartDay().isBefore(LocalDate.now())) {
            throw new WrongDateExeption(exceptionsStrings.get("endDateMustBeAfterStartDate"));
        }
        if (timeInterval.getEndDay().isAfter(LocalDate.now().plusMonths(quantityReservationMonth))) {
            throw new WrongDateExeption(exceptionsStrings.get("reservationIsAvailableOnlyOn") + quantityReservationMonth +
                    exceptionsStrings.get("ofTheMonth"));
        }
    }

    /*
    Массив всех  забронированых дат
     */
    private synchronized HashSet<LocalDate> getAllReservedDays(List<TimeInterval> intervals) {
        HashSet<LocalDate> allReservedDays = new HashSet<LocalDate>();
        for (int i = 0; i < intervals.size(); i++) {
            allReservedDays.addAll(getDaysInBetween(intervals.get(i).getStartDay(), intervals.get(i).getEndDay()));
        }
        return allReservedDays;
    }

    /*
    Получить массив дат из интервала
     */
    private synchronized HashSet<LocalDate> getDaysInBetween(LocalDate startDay, LocalDate endDay) {
        HashSet<LocalDate> daysInBetweenSet = new HashSet<LocalDate>();
        LocalDate date = startDay;
        while (!date.equals(endDay)) {
            daysInBetweenSet.add(date);
            date = date.plusDays(1);
        }
        daysInBetweenSet.add(date);
        return daysInBetweenSet;
    }

    /*
    Получить массив всех свободных дней комнаты
     */
    private synchronized TreeSet<LocalDate> getDaysAvailableForReservation(int idHotelRoom) throws SQLException {
        TreeSet<LocalDate> daysAvailableForReservationSet = new TreeSet<LocalDate>();
        HashSet<LocalDate> AllDayForMounthsSet = spreadOutMounthsIntoDays(quantityReservationMonth);
        HashSet<LocalDate> allReservedDaysSet = getAllReservedDays(Model.getAllReservationByHotelRoom(idHotelRoom));
        daysAvailableForReservationSet.addAll(AllDayForMounthsSet);
        daysAvailableForReservationSet.removeAll(allReservedDaysSet);
        return daysAvailableForReservationSet;
    }

    /*
    Разложить месяцы на массив дней
     */
    private synchronized HashSet<LocalDate> spreadOutMounthsIntoDays(int CountOfMounths) {
        HashSet<LocalDate> AllDayForMounthsSet = new HashSet<LocalDate>();
        LocalDate date = LocalDate.now();
        LocalDate datePlusMounths = date.plusMonths(CountOfMounths);

        while (!date.equals(datePlusMounths)) {
            AllDayForMounthsSet.add(date);
            date = date.plusDays(1);
        }
        AllDayForMounthsSet.add(date);
        return AllDayForMounthsSet;
    }

    /*
    Регистрируем нового пользователя
     */
    private synchronized void reg(HttpServletRequest req) throws Exception {
        if (!req.getParameter("NameReg").equals("") && !req.getParameter("loginReg")
                .equals("") && !req.getParameter("PasswordReg").equals("")) {
            Model.Ac_type type;
            System.out.print(req.getParameter("Actype"));
            if (req.getParameter("Actype") != null && req.getParameter("Actype").equals("Admin")) {
                type = Model.Ac_type.Administrator;
            } else {
                type = Model.Ac_type.User;
            }
            Model.addNewUser(req.getParameter("loginReg"), req.getParameter("PasswordReg"),
                    type, req.getParameter("NameReg"));
            req.setAttribute("SuccessfulRegistration", "true");
        } else {
            throw new Exception(exceptionsStrings.get("notAllDataEntered"));
        }
    }

    /*
    Преобразуем массив дат в интервал дат
     */
    private synchronized List<TimeInterval> getFreeTimesInterval(int idHotelRoom) throws SQLException {
        List<TimeInterval> freeTimesIntervalList = new ArrayList<TimeInterval>();
        TreeSet<LocalDate> daysAvailableForReservationSet = getDaysAvailableForReservation(idHotelRoom);
        if (daysAvailableForReservationSet.size() > 0) {
            Iterator<LocalDate> it = daysAvailableForReservationSet.iterator();
            while (it.hasNext()) {
                LocalDate startDay = it.next();
                LocalDate predDay = startDay;
                LocalDate procDate = it.next();
                while (it.hasNext() && procDate.equals(predDay.plusDays(1))) {
                    predDay = procDate;
                    procDate = it.next();
                }
                LocalDate StartIntervalDay;
                if (freeTimesIntervalList.size() != 0) {
                    StartIntervalDay = startDay.minusDays(1);
                } else {
                    StartIntervalDay = startDay;
                }
                freeTimesIntervalList.add(new TimeInterval(StartIntervalDay, predDay));
            }
        }
        return freeTimesIntervalList;
    }


    public class impossibleToReserveForTheseDatesException extends Exception {

        public impossibleToReserveForTheseDatesException() {
            super(exceptionsStrings.get("bookingIsNotAvailableForTheseDates"));
        }
    }

    private class WrongDateExeption extends Exception {

        public WrongDateExeption(String message) {
            super(message);
        }
    }
}
package Myhotel;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Model {
    private static Connection connection = null;
    private static Statement statment = null;
    private static Map<String, String> stringsMap = null;

    /*
    В статическом конструкторе подключаемся к бд
     */
    static {
        try {
            connection = connectDB();
            statment = connection.createStatement();
            stringsMap = Model.getRusLocale().getExceptionLangMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static enum Ac_type {Administrator, User}

    private Model() {
    }
    /*
    Устанавливаем язык для исключений
     */
    public synchronized static void setRusLocale() {
        stringsMap = Model.getRusLocale().getExceptionLangMap();
    }

    public synchronized static void setEngLocale() {
        stringsMap = Model.getEngLocale().getExceptionLangMap();
    }

    private static Connection connectDB() throws SQLException, ClassNotFoundException {
        String user = "root";
        String password = "123456";
        String conectorURL = "jdbc:mysql://localhost:3306/hotel";
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection(conectorURL, user, password);
        return connection;
    }

    /*
    Список всех броней всех комнат
     */
    public static synchronized List<Reservation> getAllReservation() throws Exception {
        ArrayList<Reservation> userReservations = new ArrayList<>();
        PreparedStatement pstmt = connection.prepareStatement("select * from  reservation");
        ResultSet resultSet = pstmt.executeQuery();
        while (resultSet.next()) {
            userReservations.add(new Reservation(resultSet.getInt("id"), resultSet.getDate("startDay").toLocalDate(),
                    resultSet.getDate("endDay").toLocalDate(), getUserById(resultSet.getInt("personMakingAReservation")),
                    resultSet.getInt("numberOfRoom"),
                    getNumberOfRoomById(resultSet.getInt("numberOfRoom"))));
        }
        return userReservations;
    }

    /*
    Получаем обьект пользователя по его id
     */
    private synchronized static User getUserById(int idUser) throws Exception {
        PreparedStatement pstmt = connection.prepareStatement("select * from  user where id=?");
        pstmt.setInt(1, idUser);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()) {
            return new User(resultSet.getString("login"), resultSet.getString("password")
                    , getAcTypeByInt(resultSet.getInt("ac_type")), resultSet.getString("name"),
                    resultSet.getInt("id"));
        }
        throw new IncorrectUserIdException();
    }

    /*
    Список  всех броней пользователя
     */
    public synchronized static List<Reservation> getAllReservationByUser(User user) throws Exception {
        ArrayList<Reservation> userReservations = new ArrayList<>();
        PreparedStatement pstmt = connection.prepareStatement("select * from  reservation where personMakingAReservation=?");
        pstmt.setInt(1, user.getId());
        ResultSet resultSet = pstmt.executeQuery();
        while (resultSet.next()) {
            userReservations.add(new Reservation(resultSet.getInt("id"), resultSet.getDate("startDay").toLocalDate(),
                    resultSet.getDate("endDay").toLocalDate(), user, resultSet.getInt("numberOfRoom"),
                    getNumberOfRoomById(resultSet.getInt("numberOfRoom"))));
        }
        return userReservations;
    }

    /*
    Получаем номер комнаты по его id
     */
    public synchronized static int getNumberOfRoomById(int idRoom) throws SQLException, IncorrectNumberOfRoomException {
        PreparedStatement pstmt = connection.prepareStatement("select * from  hotelRoom where id=?");
        pstmt.setInt(1, idRoom);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("name");
        }
        throw new IncorrectNumberOfRoomException();
    }

    /*
    Добавляем новую резервацию в бд
     */
    public synchronized static void addnewReservation(Reservation reservation) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("insert into reservation values (0,?,?,?,?)");
        java.sql.Date sqlStartDate = java.sql.Date.valueOf(reservation.getStartREservation());
        java.sql.Date sqlEndDate = java.sql.Date.valueOf(reservation.getEndReservation());
        pstmt.setDate(1, sqlStartDate);
        pstmt.setDate(2, sqlEndDate);
        pstmt.setInt(3, reservation.getUserMakingReservation().getId());
        pstmt.setInt(4, reservation.getIdRoom());
        pstmt.executeUpdate();
    }

    /*
    Добавляем нового пользователя в бд
     */
    public synchronized static void addNewUser(String login, String password, Ac_type ac_type, String name) throws Exception {
        if (!checkExistLoginUser(login)) {
            PreparedStatement pstmt = connection.prepareStatement("insert into user values (0,?,?,?,?)");
            pstmt.setInt(1, getIdActype(ac_type));
            pstmt.setString(2, name);
            pstmt.setString(3, login);
            pstmt.setString(4, GetMD5Hash(password));
            pstmt.executeUpdate();
        } else {
            throw new UserAlreadyExistException(login);
        }
    }

    /*
    Поиск пользователя по логину и паролю
     */
    public synchronized static User getUserByLoginAndPassword(String login, String password) throws Exception {
        PreparedStatement pstmt = connection.prepareStatement("select * from  user where login=? and password=?");
        pstmt.setString(1, login);
        pstmt.setString(2, GetMD5Hash(password));
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()) {
            return new User(resultSet.getString("login"), resultSet.getString("password")
                    , getAcTypeByInt(resultSet.getInt("ac_type")), resultSet.getString("name"),
                    resultSet.getInt("id"));
        }
        throw new IncorrectLoginOrPasswordException();
    }

    /*
    Получить все бронирования для данной комнаты
     */
    public synchronized static List<TimeInterval> getAllReservationByHotelRoom(int roomId) throws SQLException {
        ArrayList<TimeInterval> roomReservations = new ArrayList<>();
        PreparedStatement pstmt = connection.prepareStatement("select * from  reservation where numberOfRoom=?");
        pstmt.setInt(1, roomId);
        ResultSet resultSet = pstmt.executeQuery();
        while (resultSet.next()) {
            roomReservations.add(new TimeInterval(resultSet.getDate("startDay").toLocalDate(),
                    resultSet.getDate("endDay").toLocalDate()));
        }
        return roomReservations;
    }

    private synchronized static int getIdActype(Ac_type ac_type) throws Exception {
        if (ac_type == Ac_type.Administrator) return 2;
        if (ac_type == Ac_type.User) return 1;
        throw new Exception(stringsMap.get("invalidaccounttype"));
    }


    private synchronized static Ac_type getAcTypeByInt(int intAcType) throws Exception {
        if (intAcType == 1) return Ac_type.User;
        if (intAcType == 2) return Ac_type.Administrator;
        throw new Exception(stringsMap.get("invalidaccounttype"));
    }

    /*
    Получить количество комнат в отеле
     */
    public synchronized static int getQuantityOfRooms() throws SQLException {
        int quantity = 0;
        PreparedStatement pstmt = connection.prepareStatement("select * from  hotelroom");
        ResultSet resultSet = pstmt.executeQuery();
        while (resultSet.next()) {
            quantity++;
        }
        return quantity;
    }

    /*
    Получить id комнаты по ее номеру
     */
    public synchronized static int getRoomIdByNumber(int number) throws SQLException, IncorrectNumberOfRoomException {
        PreparedStatement pstmt = connection.prepareStatement("select * from  hotelroom where name=?");
        pstmt.setInt(1, number);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()) return resultSet.getInt("id");
        throw new IncorrectNumberOfRoomException();
    }

    /*
    Добавить новые комнаты в бд
     */
    public synchronized static void createNewRooms(int quantity) throws SQLException {
        int acQuantity = getQuantityOfRooms() + quantity;
        for (int i = getQuantityOfRooms(); i <= quantity; i++) {
            PreparedStatement pstmt = connection.prepareStatement("insert into hotelroom values (0,?)");
            pstmt.setString(1, String.valueOf(i));
            pstmt.executeUpdate();
        }
    }

    /*
    Удалить бронирование
     */
    public synchronized static void deleteReservation(int idReservation) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("delete from reservation where id=?");
        pstmt.setInt(1, idReservation);
        pstmt.executeUpdate();
    }

    /*
    Проверить существует ли пользователь с таким логином
     */
    private synchronized static boolean checkExistLoginUser(String login) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("select * from  user where login=?");
        pstmt.setString(1, login);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()) return true;
        return false;
    }

    private synchronized static String GetMD5Hash(String plaintext) throws NoSuchAlgorithmException {
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.reset();
        m.update(plaintext.getBytes());
        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        String hashtext = bigInt.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }

    public static synchronized Locale getRusLocale() {
        return new LocaleRus();
    }

    public static synchronized Locale getEngLocale() {
        return new LocaleEng();
    }

    static class UserAlreadyExistException extends Exception {
        public UserAlreadyExistException(String login) {
            super(stringsMap.get("User") + " " + login + " " + stringsMap.get("alreadyExists"));
        }
    }

    static class IncorrectLoginOrPasswordException extends Exception {
        public IncorrectLoginOrPasswordException() {
            super(stringsMap.get("IncorrectLoginOrPassword"));
        }
    }

    public static class IncorrectNumberOfRoomException extends Exception {
        public IncorrectNumberOfRoomException() {
            super(stringsMap.get("invalidRoomNumber"));
        }
    }

    public static class IncorrectUserIdException extends Exception {

        public IncorrectUserIdException() {
            super(stringsMap.get("invalidUserId"));
        }
    }
}

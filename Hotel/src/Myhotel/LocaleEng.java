package Myhotel;

import java.util.HashMap;
import java.util.Map;

public class LocaleEng implements Locale {
    private Map<String, String> loginLangMap = new HashMap<String, String>();
    private Map<String, String> mainLangMap = new HashMap<String, String>();
    private Map<String, String> regFormLangMap = new HashMap<String, String>();
    private Map<String, String> exceptionLangMap = new HashMap<String, String>();

    public Map<String, String> getLoginLangMap() {
        return loginLangMap;
    }

    public Map<String, String> getMainLangMap() {
        return mainLangMap;
    }

    public Map<String, String> getRegFormLangMap() {
        return regFormLangMap;
    }

    public Map<String, String> getExceptionLangMap() {
        return exceptionLangMap;
    }

    LocaleEng() {
        loginLangMap.put("login", "Login");
        loginLangMap.put("password", "Password");
        loginLangMap.put("enter", "To come in");
        loginLangMap.put("wrongLoginOrPassword", "Incorrect login or password");
        loginLangMap.put("successfulRegistration", "Registration completed successfully");
        loginLangMap.put("regestration", "Sign up");
        loginLangMap.put("chooseLanguage", "Choose language");

        mainLangMap.put("numberOfRoom", "Room number");
        mainLangMap.put("reservationStartDate", "Reservation Start Date");
        mainLangMap.put("reservationDate", "Reservation Date");
        mainLangMap.put("cancelReservation", "Cancel Reservation");
        mainLangMap.put("hello", "Hello");
        mainLangMap.put("username", "Username");
        mainLangMap.put("enterRoomNumber", "Enter room number to continue 0-");
        mainLangMap.put("next", "Next");
        mainLangMap.put("addNewUser", "Add new user");
        mainLangMap.put("exit", "Exit");
        mainLangMap.put("freeDatesForRoomNumber", "Free dates for room number");
        mainLangMap.put("chooseLanguage", "Choose language");
        mainLangMap.put("successfulRegistration", "Registration completed successfully");
        mainLangMap.put("by", " by ");

        regFormLangMap.put("enterYourName", "Enter your name");
        regFormLangMap.put("enterLogin", "Enter login");
        regFormLangMap.put("enterPassword", "enter password");
        regFormLangMap.put("Administrator", "Administrator");
        regFormLangMap.put("User", "User");
        regFormLangMap.put("signUp", "Sign up");
        regFormLangMap.put("chooseLanguage", "Choose language");

        exceptionLangMap.put("invalidaccounttype", "Invalid account type type");
        exceptionLangMap.put("User", "User");
        exceptionLangMap.put("alreadyExists", "already exists");
        exceptionLangMap.put("IncorrectLoginOrPassword", "Incorrect login or password");
        exceptionLangMap.put("invalidRoomNumber", "Invalid room number");
        exceptionLangMap.put("invalidUserId", "Invalid user id");
        exceptionLangMap.put("notAllDataEntered", "Not all data entered");
        exceptionLangMap.put("selectedDatesMatch", "Selected dates match");
        exceptionLangMap.put("endDateMust", "End date must be after start date");
        exceptionLangMap.put("endDateMustBeAfterStartDate", "Start date must not be in the past");
        exceptionLangMap.put("reservationIsAvailableOnlyOn", "Reservation is available only on ");
        exceptionLangMap.put("ofTheMonth", " of the month");
        exceptionLangMap.put("bookingIsNotAvailableForTheseDates", "Booking is not available for these dates.");
        exceptionLangMap.put("SelectDatesToBook", "Select dates to book");
    }
}

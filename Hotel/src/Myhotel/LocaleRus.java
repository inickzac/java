package Myhotel;

import java.util.HashMap;
import java.util.Map;

public class LocaleRus implements Locale{
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


    public LocaleRus() {
        loginLangMap.put("login", "Логин");
        loginLangMap.put("password", "Пароль");
        loginLangMap.put("enter", "Войти");
        loginLangMap.put("wrongLoginOrPassword", "Неправильный логин или пароль");
        loginLangMap.put("successfulRegistration", "Регистрация проведена успешно");
        loginLangMap.put("regestration", "Зарегистрироваться");
        loginLangMap.put("chooseLanguage", "Выберите язык");

        mainLangMap.put("numberOfRoom", "Номер комнаты");
        mainLangMap.put("reservationStartDate", "Дата начала бронирования");
        mainLangMap.put("reservationDate", "Дата окончания бронирования");
        mainLangMap.put("cancelReservation", "Отмена бронирования");
        mainLangMap.put("hello", "Здравствуйте");
        mainLangMap.put("username", "Имя пользователя");
        mainLangMap.put("enterRoomNumber", "Введите номер комнаты для продолжения 0-");
        mainLangMap.put("next", "далее");
        mainLangMap.put("addNewUser", "Добавить нового пользователя");
        mainLangMap.put("exit", "Выход");
        mainLangMap.put("freeDatesForRoomNumber", "Свободные даты для комнаты номер");
        mainLangMap.put("chooseLanguage", "Выберите язык");
        mainLangMap.put("successfulRegistration", "Регистрация проведена успешно");
        mainLangMap.put("by", " по ");

        regFormLangMap.put("enterYourName", "Введите имя");
        regFormLangMap.put("enterLogin", "Введите логин");
        regFormLangMap.put("enterPassword", "Введите пароль");
        regFormLangMap.put("Administrator", "Адиминистратор");
        regFormLangMap.put("User", "Пользователь");
        regFormLangMap.put("signUp", "Зарегистрироваться");
        regFormLangMap.put("chooseLanguage", "Выберите язык");

        exceptionLangMap.put("invalidaccounttype", "Неправильный тип типа аккаунта");
        exceptionLangMap.put("User", "Пользователь");
        exceptionLangMap.put("alreadyExists", "уже существует");
        exceptionLangMap.put("IncorrectLoginOrPassword", "Неправильный логин или пароль");
        exceptionLangMap.put("invalidRoomNumber", "Неправильный номер комнаты");
        exceptionLangMap.put("invalidUserId", "Неправильный id пользователя");
        exceptionLangMap.put("notAllDataEntered", "Введены не все данные");
        exceptionLangMap.put("selectedDatesMatch", "Выбранные даты совпадают");
        exceptionLangMap.put("endDateMust", "Дата окончания должна быть после даты начала");
        exceptionLangMap.put("endDateMustBeAfterStartDate", "Дата начала не должна быть в прошлом");
        exceptionLangMap.put("reservationIsAvailableOnlyOn", "Резервация доступна толька на ");
        exceptionLangMap.put("ofTheMonth", " месяца");
        exceptionLangMap.put("bookingIsNotAvailableForTheseDates", "На данные даты резервация недоступна");
        exceptionLangMap.put("SelectDatesToBook", "Выберите даты для бронирования");
    }
}

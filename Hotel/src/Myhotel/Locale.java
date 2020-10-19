package Myhotel;

import java.util.Map;

public interface Locale {
    public Map<String, String> getLoginLangMap();

    public Map<String, String> getMainLangMap();

    public Map<String, String> getRegFormLangMap();

    public Map<String, String> getExceptionLangMap();
}

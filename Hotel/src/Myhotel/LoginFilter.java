package Myhotel;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@WebFilter("/hotel")
public class LoginFilter implements Filter {



    private Locale locale;
    ServletRequest servletRequest;
    ServletResponse servletResponse;
    HttpSession session;
    private Map<String, String> exceptionsStrings;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /* Проверяем запрос на атрибут в сессии logedIn если true входим иначе требуем ввести пароль или зарегестрироваться
     */
    @Override
    public synchronized void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            session = ((HttpServletRequest) servletRequest).getSession();
            this.servletRequest = servletRequest;
            this.servletResponse = servletResponse;
            checkLocale();
            if (session.getAttribute("logedIn") == "true") {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
            try {
                if (servletRequest.getParameter("login") != null && servletRequest.getParameter("password") != null) {
                    Enter();
                    return;
                }
            } catch (Model.IncorrectLoginOrPasswordException e) {
                servletRequest.setAttribute("FailedLoginOrPassword", "true");
            }
            if (servletRequest.getParameter("Action") != null && servletRequest.getParameter("Action").equals("regestration")) {
                servletRequest.setAttribute("StringsMap", locale.getRegFormLangMap());
                forwarding("/jsp/RegForm.jsp");
                return;
            }
            if (servletRequest.getParameter("Action") != null && servletRequest.getParameter("Action").equals("regestrationData")) {
                try {
                    registration();
                    servletRequest.setAttribute("SuccessfulRegistration", "true");
                    servletRequest.setAttribute("StringsMap", locale.getLoginLangMap());
                    forwarding("/jsp/login.jsp");
                } catch (Exception e) {
                    servletRequest.setAttribute("ErrorReg", e.getMessage());
                    servletRequest.setAttribute("StringsMap", locale.getRegFormLangMap());
                    forwarding("/jsp/RegForm.jsp");
                }
                return;
            }
            servletRequest.setAttribute("StringsMap", locale.getLoginLangMap());
            forwarding("/jsp/login.jsp");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {

    }

    private synchronized void Enter() throws Exception {
        HttpSession session = ((HttpServletRequest) servletRequest).getSession();
        User user = Model.getUserByLoginAndPassword(servletRequest.getParameter("login"), servletRequest.getParameter("password"));
        session.setAttribute("logedIn", "true");
        session.setAttribute("User", user);
        forwarding("/hotel");
    }

    private synchronized void forwarding(String page) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = servletRequest.getRequestDispatcher(page);
        requestDispatcher.forward(servletRequest, servletResponse);
    }

    private synchronized void registration() throws Exception {
        if (!servletRequest.getParameter("NameReg").equals("") && !servletRequest.getParameter("loginReg")
                .equals("") && !servletRequest.getParameter("PasswordReg").equals("")) {
            Model.addNewUser(servletRequest.getParameter("loginReg"), servletRequest.getParameter("PasswordReg"),
                    Model.Ac_type.User, servletRequest.getParameter("NameReg"));
        } else {
            throw new Exception("Введены не все данные");
        }
    }
    /*
    Переключение языков
     */
   public  synchronized void checkLocale() {
        if (session.getAttribute("locale") == null || locale == null) {
            locale = Model.getRusLocale();
            session.setAttribute("locale", "rus");
            Model.setRusLocale();
            exceptionsStrings = locale.getExceptionLangMap();

        }
        if (servletRequest.getParameter("lang") != null && servletRequest.getParameter("lang").equals("English")) {
            locale = Model.getEngLocale();
            session.setAttribute("locale", "eng");
            Model.setEngLocale();
            exceptionsStrings = locale.getExceptionLangMap();
        }
        if (servletRequest.getParameter("lang") != null && servletRequest.getParameter("lang").equals("Русский")) {
            locale = Model.getRusLocale();
            session.setAttribute("locale", "rus");
            Model.setRusLocale();
            exceptionsStrings = locale.getExceptionLangMap();
        }
    }

}

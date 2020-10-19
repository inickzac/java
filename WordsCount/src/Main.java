import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/words")
public class Main extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var param = req.getParameter("Words");
        if (param != null) {
            String[] words = param.split(" ");
            List<String> filtered = Arrays.stream(words).filter(w -> w != "")
                    .collect(Collectors.toList());
            req.setAttribute("quantityOfWords", filtered.size());
        }
        var requestDispatcher = req.getRequestDispatcher("jsp/View.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}

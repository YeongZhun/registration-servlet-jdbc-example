package registerservlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import registerbean.RegisterBean;
import registerdao.RegisterDao;


//@WebServlet("/register")
//public class RegisterServlet extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//	private RegisterDao loginDao;
//
//	@Override
//	public void init() {
//		loginDao = new RegisterDao();
//	}
//
//	@Override
//	protected void doPost(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//
//		String username = request.getParameter("username");
//		String password = request.getParameter("password");
//		RegisterBean loginBean = new RegisterBean();
//		loginBean.setUsername(username);
//		loginBean.setPassword(password);
//
//		try {
//			if (loginDao.validate(loginBean)) {
//			    HttpSession session = request.getSession();
//			    session.setAttribute("name", loginBean.getName());
//			    session.setAttribute("phoneNumber", loginBean.getPhoneNumber());
//
//			    response.sendRedirect("loginsuccess.jsp");
//			}
//			else {
//				 response.sendRedirect("login.jsp"); // Redirect back to login page
//			}
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//	}
//}

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private RegisterDao registerDao;

    @Override
    public void init() {
        registerDao = new RegisterDao();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve parameters from the registration form
        String name = request.getParameter("name");
        String phoneNumber = request.getParameter("phone_number");
        String address = request.getParameter("address");

        // Validate input parameters
        if (name.isEmpty() || phoneNumber.isEmpty() || address.isEmpty() ||
                name.length() > 50 || phoneNumber.length() > 50 || address.length() > 50) {
            // Invalid input, redirect back to registration form with error message
            request.setAttribute("errorMessage", "Validation failed. Please check your inputs.");
            request.getRequestDispatcher("registration.jsp").forward(request, response);
            return;
        }

        // Create RegisterBean object
        RegisterBean registerBean = new RegisterBean();
        registerBean.setName(name);
        registerBean.setPhoneNumber(phoneNumber);
        registerBean.setAddress(address);

        try {
            // Register the user and retrieve the generated user ID
            int userId = registerDao.registerUser(registerBean);

            // If a salary record exists for the user, increment the salary by 10000
            boolean updated = registerDao.incrementSalary(userId);

            // Redirect to registration success page with appropriate message
            if (userId != -1) {
                HttpSession session = request.getSession();
                session.setAttribute("userId", userId);
                session.setAttribute("name", name);
                session.setAttribute("phoneNumber", phoneNumber);
                session.setAttribute("address", address);
                if (updated) {
                    response.sendRedirect("registerSuccess.jsp");
                } else {
                    response.sendRedirect("registerSuccess.jsp?message=Salary update failed.");
                }
            } else {
                response.sendRedirect("registerSuccess.jsp?message=Registration succeeded, but user ID not generated.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package no.srib.sribapp.controller;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import sun.misc.BASE64Encoder;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.hibernate.BackendUserDAOImpl;
import no.srib.sribapp.dao.interfaces.BackendUserDAO;
import no.srib.sribapp.model.Backenduser;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        HttpSession ses = request.getSession(false);
        /*
         * if (request.getParameter("loggOut").equals("true")) { if (ses !=
         * null) { ses.setAttribute("loggedIn", "false"); }
         * response.sendRedirect("index.html"); }
         */
        if (ses != null) {
            if (ses.getAttribute("loggedIn").equals("true")) {
                RequestDispatcher reqD = request
                        .getRequestDispatcher("/WEB-INF/admin.html");
                reqD.forward(request, response);
            }
        } else {
            response.sendRedirect("index.html");

        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        BackendUserDAO dao = new BackendUserDAOImpl();
        Backenduser back = null;
        MessageDigest digester = null;
        try {
            back = dao.getByUserName("test");
        } catch (DAOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            digester = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        digester.update("test".getBytes("UTF-8"));
        byte[] raw = digester.digest();
        if (back != null) {
            System.out.println(back.getPassword());

            System.out.println(Hex.encodeHexString(raw));
        }
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        HttpSession ses = request.getSession(false);

        if (ses == null) {
            ses = request.getSession();

        } else {
            if (ses.getAttribute("loggedIn").equals("true")
                    && username.equals("test") && password.equals("test")) {
                RequestDispatcher reqD = request
                        .getRequestDispatcher("/WEB-INF/admin.html");
                reqD.forward(request, response);
                return;
            } else {
                ses.setAttribute("loggedIn", "false");
            }
        }

        if (username.equals("test") && password.equals("test")) {
            ses.setAttribute("loggedIn", "true");
            RequestDispatcher reqD = request
                    .getRequestDispatcher("/WEB-INF/admin.html");
            reqD.forward(request, response);
            return;
        } else {
            response.sendRedirect("index.html");
            return;
        }

    }

}

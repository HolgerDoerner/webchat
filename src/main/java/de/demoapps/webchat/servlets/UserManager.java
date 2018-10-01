package de.demoapps.webchat.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.enterprise.context.RequestScoped;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.common.hash.Hashing;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import de.demoapps.webchat.classes.HibernateUtils;
import de.demoapps.webchat.classes.User;

/**
 * 
 */
@WebServlet("/usermanager")
public class UserManager extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
    private Session session = sessionFactory.openSession();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        switch(request.getParameter("action")) {
            case "login":
                loginUser(request, response);
                break;

            case "register":
                registerUser(request, response);
                break;
        }
    }

    /**
     * redirect all GET-Requests to this servlet quietly to the index page
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void loginUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User user = null;

        System.out.println(request.getParameter("nickname") + " " + request.getParameter("password"));
        
        try {
            session.getTransaction().begin();

            Query<?> query = session.createQuery("from User where NICKNAME=:nick");
            query.setParameter("nick", request.getParameter("nickname"));

            user = (User) query.getSingleResult();

            session.getTransaction().commit();

            if(Hashing.sha256().hashString(request.getParameter("password"), StandardCharsets.UTF_8).toString()
                .equals(user.getPassword()) && request.getParameter("nickname").equals(user.getNickname())) {
                    
                final HttpSession session = request.getSession();
                session.setAttribute("user", user);
                Cookie nickname = new Cookie("nickname", user.getNickname());
                response.addCookie(nickname);
                
                response.sendRedirect("chat.jsp");
            }
            else {
                //response.sendRedirect("index.jsp?status=login_error");
                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("Wrong Nickname and/or Password !!");
            }
        }
        catch (Throwable e) {
            System.out.println("** ERROR: " + e.getMessage());
            //response.sendRedirect("index.jsp?status=login_error");
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("Wrong Nickname and/or Password !!");
        }
    }

    /**
     * 
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void registerUser(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String nickname = request.getParameter("nickname");
        String password = request.getParameter("password");
        
        session.getTransaction().begin();
        
        try {
            session.save(new User(nickname, Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString()));
            session.getTransaction().commit();
        }
        catch (PersistenceException e) {
            response.sendRedirect("index.jsp?status=nickname_duplicate");
        }
        response.sendRedirect("index.jsp?status=register_successfull");
    }
}
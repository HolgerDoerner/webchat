package de.demoapps.webchat.servlets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.enterprise.context.SessionScoped;
import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.common.hash.Hashing;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import de.demoapps.webchat.classes.HibernateUtils;
import de.demoapps.webchat.classes.User;

/**
 * servlet for handling user-managment tasks.
 * 
 * void doPost()
 * void doGet()
 * void loginUser()
 * void registerUser()
 * void logoutUser()
 */
@SessionScoped
@WebServlet("/usermanager")
public class UserManager extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
    private Session dbSession = sessionFactory.openSession();

    /**
     * mein method to handle requests and responses.
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // get value for parameter "action" from request and call the proper methods.
        switch(request.getParameter("action")) {
            case "login":
                loginUser(request, response);
                break;

            case "register":
                registerUser(request, response);
                break;

            case "logout":
                logoutUser(request, response);
                break;
        }
    }

    /**
     * redirect all GET-Requests to this servlet to doPost().
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * lets users log-in to the chat.
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void loginUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User user = null;
        Transaction transaction = dbSession.beginTransaction();
        
        try {
            Query<?> query = dbSession.createQuery("from User where NICKNAME=:nick");
            query.setParameter("nick", request.getParameter("nickname"));

            transaction.commit();

            user = (User) query.getSingleResult();

            if(Hashing.sha256().hashString(request.getParameter("password"), StandardCharsets.UTF_8).toString()
                .equals(user.getPassword()) && request.getParameter("nickname").equals(user.getNickname())) {
                
                // write the User-Bean into the session
                final HttpSession httpSession = request.getSession();
                httpSession.setAttribute("user", user);
                
                // redirect client to chat
                response.addHeader("redirect", "chat.jsp");
            }
            else {
                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("Wrong Nickname and/or Password !!");
            }
        }
        catch (Throwable e) {
            if (transaction.isActive()) transaction.rollback();
            
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("Wrong Nickname and/or Password !!");
        }
    }

    /**
     * registers a new user and makes entry in database.
     * 
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void registerUser(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        // get values for nickname and password from request-parameters.
        String nickname = request.getParameter("nickname");
        String password = request.getParameter("password");
        
        Transaction transaction = dbSession.beginTransaction();
        
        try {
            // make a new entry in database.
            // password is stored as SHA-256 hash.
            dbSession.save(new User(nickname, Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString()));
            transaction.commit();

            // notify user if registration was successfull
            response.getWriter().write("Nickname successfully registered!! You can now log in!");
        }
        catch (PersistenceException e) {
            // on error, rollback the transaction and notify user
            if (transaction.isActive()) transaction.rollback();

            response.getWriter().write("Nickname already taken, please try another one!");
        }

    }

    /**
     * log out the user and save state of the settings in database.
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void logoutUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // get user-id from session-bean
        Integer id = ((User) request.getSession().getAttribute("user")).getID();
        
        Transaction transaction = dbSession.beginTransaction();
        
        try {
            // load user from database and update settings
            User user = dbSession.get(User.class, id);
            user.setSingleSetting("enter", Integer.parseInt(request.getParameter("enter")));
            user.setSingleSetting("outputfontsize", Integer.parseInt(request.getParameter("outputfontsize")));
            user.setSingleSetting("inputfontsize", Integer.parseInt(request.getParameter("inputfontsize")));

            dbSession.update(user);
            transaction.commit();
        
            request.getSession().invalidate();
            response.addHeader("redirect", "index.jsp");
        }
        catch (PersistenceException e) {
            if (transaction.isActive()) transaction.rollback();

            response.getWriter().write(e.getMessage());
        }
        
    }
}
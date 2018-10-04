package de.demoapps.webchat.servlets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.enterprise.context.ApplicationScoped;
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
@ApplicationScoped
@WebServlet("/usermanager")
public class UserManager extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
    private Session dbSession = sessionFactory.openSession();
    private Transaction transaction = dbSession.beginTransaction();

    public UserManager() { super(); }

    /**
     * main method to handle requests and responses.
     * 
     * @param httpRequest
     * @param httpResponse
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doPost(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {
        
        // get value for parameter "action" from request and call the proper methods.
        switch(httpRequest.getParameter("action")) {
            case "login":
                loginUser(httpRequest, httpResponse);
                break;

            case "register":
                registerUser(httpRequest, httpResponse);
                break;

            case "logout":
                logoutUser(httpRequest, httpResponse);
                break;
        }
    }

    /**
     * redirect all GET-Requests to this servlet to doPost().
     * 
     * @param httpRequest
     * @param httpResponse
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {
        doPost(httpRequest, httpResponse);
    }

    /**
     * lets users log-in to the chat.
     * 
     * @param httpRequest
     * @param httpResponse
     * @throws ServletException
     * @throws IOException
     */
    public void loginUser(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {

        User user = null;

        // Transaction transaction = dbSession.beginTransaction();
        if (!transaction.isActive()) transaction.begin();
        
        try {
            Query<?> query = dbSession.createQuery("from User where NICKNAME=:nick");
            query.setParameter("nick", httpRequest.getParameter("nickname"));

            transaction.commit();

            user = (User) query.getSingleResult();

            if(Hashing.sha256().hashString(httpRequest.getParameter("password"), StandardCharsets.UTF_8).toString()
                .equals(user.getPassword()) && httpRequest.getParameter("nickname").equals(user.getNickname())) {
                
                // write the User-Bean into the session
                final HttpSession httpSession = httpRequest.getSession();
                httpSession.setAttribute("user", user);
                
                // redirect client to chat
                httpResponse.addHeader("redirect", "chat.jsp");
            }
            else {
                httpResponse.setContentType("text/plain");
                httpResponse.setCharacterEncoding("UTF-8");
                httpResponse.getWriter().write("Wrong Nickname and/or Password !!");
            }
        }
        catch (Throwable e) {
            if (transaction.isActive() || transaction.getRollbackOnly()) transaction.rollback();
            
            httpResponse.setContentType("text/plain");
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.getWriter().write("Wrong Nickname and/or Password !!");
        }
    }

    /**
     * registers a new user and makes entry in database.
     * 
     * @param httpRequest
     * @param httpResponse
     * @throws IOException
     * @throws ServletException
     */
    public void registerUser(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException, ServletException {

        // get values for nickname and password from request-parameters.
        String nickname = httpRequest.getParameter("nickname");
        String password = httpRequest.getParameter("password");
        
        // Transaction transaction = dbSession.beginTransaction();
        if (!transaction.isActive()) transaction.begin();
        
        try {
            // make a new entry in database.
            // password is stored as SHA-256 hash.
            dbSession.save(new User(nickname, Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString()));
            transaction.commit();

            // notify user if registration was successfull
            httpResponse.getWriter().write("Nickname successfully registered!! You can now log in!");
        }
        catch (PersistenceException e) {
            // on error, rollback the transaction and notify user
            if (transaction.isActive() || transaction.getRollbackOnly()) transaction.rollback();

            httpResponse.getWriter().write("Nickname already taken, please try another one!");
        }

    }

    /**
     * log out the user and save state of the settings in database.
     * 
     * @param httpRequest
     * @param httpResponse
     * @throws ServletException
     * @throws IOException
     */
    public void logoutUser(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {

        // get user-id from session-bean
        Integer id = ((User) httpRequest.getSession().getAttribute("user")).getID();
        
        // Transaction transaction = dbSession.beginTransaction();
        if (!transaction.isActive()) transaction.begin();
        
        try {
            // load user from database and update settings
            User user = dbSession.get(User.class, id);
            user.setSingleSetting("enter", Integer.parseInt(httpRequest.getParameter("enter")));
            user.setSingleSetting("outputfontsize", Integer.parseInt(httpRequest.getParameter("outputfontsize")));
            user.setSingleSetting("inputfontsize", Integer.parseInt(httpRequest.getParameter("inputfontsize")));

            dbSession.update(user);
            transaction.commit();
        
            httpRequest.getSession().invalidate();
            httpResponse.addHeader("redirect", "index.jsp");
        }
        catch (PersistenceException e) {
            if (transaction.isActive() || transaction.getRollbackOnly()) transaction.rollback();

            httpResponse.getWriter().write(e.getMessage());
        }
        
    }
}
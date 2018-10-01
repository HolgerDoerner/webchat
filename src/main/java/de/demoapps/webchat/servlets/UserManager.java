package de.demoapps.webchat.servlets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.common.hash.Hashing;
import com.google.gson.Gson;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

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
        Transaction transaction = session.beginTransaction();
        
        try {
            Query<?> query = session.createQuery("from User where NICKNAME=:nick");
            query.setParameter("nick", request.getParameter("nickname"));

            transaction.commit();

            user = (User) query.getSingleResult();

            if(Hashing.sha256().hashString(request.getParameter("password"), StandardCharsets.UTF_8).toString()
                .equals(user.getPassword()) && request.getParameter("nickname").equals(user.getNickname())) {
                    
                final HttpSession session = request.getSession();
                session.setAttribute("user", user);

                response.addCookie(new Cookie("nickname", user.getNickname()));
                response.addCookie(new Cookie("setting_enter", user.getSingleSetting("enter").toString()));
                response.addCookie(new Cookie("setting_outputfontsize", user.getSingleSetting("outputfontsize").toString()));
                response.addCookie(new Cookie("setting_inputfontsize", user.getSingleSetting("inputfontsize").toString()));
                
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
     * 
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void registerUser(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String nickname = request.getParameter("nickname");
        String password = request.getParameter("password");
        
        Transaction transaction = session.beginTransaction();
        
        try {
            session.save(new User(nickname, Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString()));
            transaction.commit();

            response.getWriter().write("Nickname successfully registered!! You can now log in!");
        }
        catch (PersistenceException e) {
            if (transaction.isActive()) transaction.rollback();

            response.getWriter().write("Nickname already taken, please try another one!");
        }

    }
}
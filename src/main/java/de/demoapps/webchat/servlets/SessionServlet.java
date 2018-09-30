package de.demoapps.webchat.servlets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.enterprise.context.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
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
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import de.demoapps.webchat.classes.User;

/**
 * 
 */
@SessionScoped
@WebServlet("/sessionmanager")
public class SessionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

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
        response.sendRedirect("index.jsp");
    }

    /**
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void loginUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("UserDB");
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        entityTransaction.begin();

        User user = null;
        
        try {
            Query query = entityManager.createQuery("from User where NICKNAME=:nick");
            query.setParameter("nick", request.getParameter("nickname"));
            entityTransaction.commit();
            user = (User) query.getSingleResult();

            entityManager.close();
            factory.close();

            if(Hashing.sha256().hashString(request.getParameter("password"), StandardCharsets.UTF_8).toString()
                .equals(user.getPassword()) && request.getParameter("nickname").equals(user.getNickname())) {
                    
                final HttpSession session = request.getSession();
                session.setAttribute("user", user);
                Cookie nickname = new Cookie("nickname", user.getNickname());
                response.addCookie(nickname);
                response.sendRedirect("chat.jsp");
            }
            else {
                response.sendRedirect("index.jsp?status=login_error");
            }
        }
        catch (NullPointerException | NoResultException e) {
            response.sendRedirect("index.jsp?status=login_error");
        }
        catch (Throwable e) {
            // TODO
        }
    }

    /**
     * 
     * @param request
     * @param response
     * @return boolean
     * @throws IOException
     * @throws ServletException
     */
    public void registerUser(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String nickname = request.getParameter("nickname");
        String password = request.getParameter("password");
        
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("UserDB");
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        try {
            entityTransaction.begin();            
            entityManager.persist(new User(nickname, Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString()));    
            entityTransaction.commit();
            entityManager.close();
            factory.close();
        }
        catch (PersistenceException e) {
            entityManager.close();
            factory.close();

            response.sendRedirect("index.jsp?status=nickname_duplicate");
        }
        
        response.sendRedirect("index.jsp?status=register_successfull");
    }
}
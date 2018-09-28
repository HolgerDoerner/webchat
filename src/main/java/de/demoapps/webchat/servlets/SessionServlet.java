package de.demoapps.webchat.servlets;

import java.io.IOException;
import java.io.PrintWriter;
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
        // final User user = new User();
        // user.setNickname(request.getParameter("nickname"));
        // final HttpSession session = request.getSession();
        // session.setAttribute("user", user);

        // Cookie nickname = new Cookie("nickname", user.getNickname());

        // response.addCookie(nickname);

        // response.sendRedirect("chat.jsp");

        switch(request.getParameter("action")) {
            case "login":
                loginUser(request, response);
                break;

            case "register":
                registerUser(request, response);
                break;
        }
    }

    public void loginUser(HttpServletRequest request, HttpServletResponse response) throws IOException {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("UserDB");
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        entityTransaction.begin();

        User user = null;
        
        try {
            Query query = entityManager.createQuery("from User where NICKNAME=:nick");
            query.setParameter("nick", request.getParameter("nickname"));
            user = (User) query.getSingleResult();
    
            entityTransaction.commit();
            entityManager.close();
            factory.close();
        }
        catch (NoResultException e) {
            PrintWriter out = response.getWriter();
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            out.write("<h2><b>Nickname</b> not found, please try again!</h2><br>");
            out.write("Back to <a href=\"index.jsp\" target=\"_self\">Login Page</a>.");
        }

        if(Hashing.sha256().hashString(request.getParameter("password"), StandardCharsets.UTF_8).toString()
            .equals(user.getPassword())) {
                
            final HttpSession session = request.getSession();
            session.setAttribute("user", user);
            Cookie nickname = new Cookie("nickname", user.getNickname());
            response.addCookie(nickname);
            response.sendRedirect("chat.jsp");
        }
        else {
            PrintWriter out = response.getWriter();
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            out.write("<h2>Wrong <b>Password</b>, please try again!</h2><br>");
            out.write("Back to <a href=\"index.jsp\" target=\"_self\">Login Page</a>.");
        }
    }

    public void registerUser(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String nickname = request.getParameter("nickname");
        String password = request.getParameter("password");
        
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("UserDB");
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        try {
            entityTransaction.begin();
            
            // try {
            //     Query query = entityManager.createQuery("from User where NICKNAME=:nick");
            //     query.setParameter("nick", nickname);
            //     query.getSingleResult();
            // }
            // catch (NoResultException e) {
            // }
            
            entityManager.persist(new User(nickname, Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString()));    
            entityTransaction.commit();
            entityManager.close();
            factory.close();
        }
        catch (PersistenceException e) {
            entityManager.close();
            factory.close();
            PrintWriter out = response.getWriter();
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            out.write("<h2>Username already taken, please try another one!</h2><br>");
            out.write("Back to <a href=\"index.jsp\" target=\"_self\">Login Page</a>.");
        }
        finally {
            PrintWriter out = response.getWriter();
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            out.write("<h2>Registration <b>successfull</b>, you can now log in!</h2><br>");
            out.write("Back to <a href=\"index.jsp\" target=\"_self\">Login Page</a>.");
        }
    }
}
package de.demoapps.webchat.servlets;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.demoapps.webchat.classes.User;

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
                loginUser(request.getParameter("nickname"), request.getParameter("password"));
                break;

            case "register":
                createUser(request.getParameter("nickname"), request.getParameter("password"));
                break;
        }

        response.sendRedirect("index.jsp");
    }

    public boolean loginUser(String nickname, String password) {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("UserDB");
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        entityTransaction.begin();
        
        Query query = entityManager.createQuery("from User where NICKNAME=:nick");
        query.setParameter("nick", nickname);
        User user = (User) query.getSingleResult();

        entityTransaction.commit();
        entityManager.close();
        factory.close();

        if(password.equals(user.getPassword())) {
            System.out.println("User " + user.getNickname() + " logged in successfull!!");
            return true;
        }
        else {
            System.out.println("Could not login user " + user.getNickname() + ", wrong Nickname or Password!");
            return false;
        }
    }

    public void createUser(String nickname, String password) {
        
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("UserDB");
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        entityTransaction.begin();
        
        entityManager.persist(new User(nickname, password));

        entityTransaction.commit();
        entityManager.close();
        factory.close();
    }
}
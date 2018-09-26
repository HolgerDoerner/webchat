package de.demoapps.webchat.servlets;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

        createUser(request.getParameter("nickname"), request.getParameter("password"));
        getUsers();
        response.sendRedirect("index.jsp");
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

    public void getUsers() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("UserDB");
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        entityTransaction.begin();
        
        Query query = entityManager.createQuery("SELECT * FROM Users;");
        List<User> userlist = (List<User>) query.getResultList();

        entityTransaction.commit();
        entityManager.close();
        factory.close();

        userlist.forEach(user -> {
            System.out.println(user);
        });
    }
}
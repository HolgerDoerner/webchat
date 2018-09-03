package de.demoapps.webchat.servlets;

import java.io.IOException;

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
        final User user = new User();
        user.setNickname(request.getParameter("nickname"));
        final HttpSession session = request.getSession();
        session.setAttribute("user", user);

        Cookie nickname = new Cookie("nickname", user.getNickname());

        response.addCookie(nickname);

        response.sendRedirect("chat.jsp");
    }
}
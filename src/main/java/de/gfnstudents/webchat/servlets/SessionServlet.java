package de.gfnstudents.webchat.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.gfnstudents.webchat.classes.User;

@WebServlet("/newsession")
public class SessionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        final User user = new User();
        user.setNickname(request.getParameter("nickname"));
        final HttpSession session = request.getSession();
        session.setAttribute("user", user);
    }
}
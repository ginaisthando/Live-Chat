package com.brightfuture.chat.servlets;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            response.sendRedirect("home");
            return;
        }
        
        // Serve registration page
        request.getRequestDispatcher("/pages/registration.html").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Validation
        if (username == null || password == null || confirmPassword == null ||
            username.trim().isEmpty() || password.trim().isEmpty() || confirmPassword.trim().isEmpty()) {
            request.setAttribute("error", "All fields are required.");
            request.getRequestDispatcher("/pages/registration.html").forward(request, response);
            return;
        }
        
        if (username.length() < 3) {
            request.setAttribute("error", "Username must be at least 3 characters long.");
            request.getRequestDispatcher("/pages/registration.html").forward(request, response);
            return;
        }
        
        if (password.length() < 6) {
            request.setAttribute("error", "Password must be at least 6 characters long.");
            request.getRequestDispatcher("/pages/registration.html").forward(request, response);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match.");
            request.getRequestDispatcher("/pages/registration.html").forward(request, response);
            return;
        }
        
        // Get users map from LoginServlet
        Map<String, String> users = LoginServlet.getUsers();
        
        if (users.containsKey(username)) {
            request.setAttribute("error", "Username already exists. Please choose a different one.");
            request.getRequestDispatcher("/pages/registration.html").forward(request, response);
            return;
        }
        
        // Register user
        users.put(username, password);
        
        // Auto-login after registration
        HttpSession session = request.getSession(true);
        session.setAttribute("username", username);
        session.setMaxInactiveInterval(30 * 60); // 30 minutes
        
        request.setAttribute("success", "Registration successful! Welcome to BrightFuture University Chat.");
        response.sendRedirect("home");
    }
}
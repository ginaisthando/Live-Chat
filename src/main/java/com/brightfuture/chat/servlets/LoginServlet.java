package com.brightfuture.chat.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    // Simple in-memory user storage (in production, use a database)
    private static Map<String, String> users = new HashMap<>();
    
    static {
        // Default users for testing
        users.put("admin", "admin123");
        users.put("student1", "password123");
        users.put("professor1", "prof123");
    }
    
    public static Map<String, String> getUsers() {
        return users;
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            response.sendRedirect("home");
            return;
        }
        
        // Serve login page
        request.getRequestDispatcher("/pages/login.html").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("error", "Please enter both username and password.");
            request.getRequestDispatcher("/pages/login.html").forward(request, response);
            return;
        }
        
        // Validate credentials
        if (users.containsKey(username) && users.get(username).equals(password)) {
            // Create session
            HttpSession session = request.getSession(true);
            session.setAttribute("username", username);
            session.setMaxInactiveInterval(30 * 60); // 30 minutes
            
            response.sendRedirect("home");
        } else {
            request.setAttribute("error", "Invalid username or password.");
            request.getRequestDispatcher("/pages/login.html").forward(request, response);
        }
    }
}
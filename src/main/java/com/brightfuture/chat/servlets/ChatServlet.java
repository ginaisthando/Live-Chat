package com.brightfuture.chat.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {
    
    // Simple in-memory message storage (in production, use a database)
    private static List<String> chatHistory = new ArrayList<>();
    
    static {
        // Add some initial messages
        chatHistory.add("[System] Welcome to BrightFuture University Chat!");
        chatHistory.add("[System] Please be respectful and follow university guidelines.");
    }
    
    public static List<String> getChatHistory() {
        return chatHistory;
    }
    
    public static void addMessage(String message) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String timestamp = sdf.format(new Date());
        chatHistory.add("[" + timestamp + "] " + message);
        
        // Keep only last 100 messages to prevent memory issues
        if (chatHistory.size() > 100) {
            chatHistory.remove(0);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not authenticated");
            return;
        }
        
        // Return chat history as JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        out.println("{");
        out.println("  \"messages\": [");
        
        for (int i = 0; i < chatHistory.size(); i++) {
            String message = chatHistory.get(i).replace("\"", "\\\"");
            out.print("    \"" + message + "\"");
            if (i < chatHistory.size() - 1) {
                out.println(",");
            } else {
                out.println();
            }
        }
        
        out.println("  ]");
        out.println("}");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not authenticated");
            return;
        }
        
        String username = (String) session.getAttribute("username");
        String message = request.getParameter("message");
        
        if (message != null && !message.trim().isEmpty()) {
            addMessage(username + ": " + message.trim());
        }
        
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
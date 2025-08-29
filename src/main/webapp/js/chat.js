// WebSocket connection variable
let socket = null;
let reconnectInterval = null;
let messageQueue = [];

// Initialize chat connection
function initializeChat() {
    connectWebSocket();
}

// Connect to WebSocket server
function connectWebSocket() {
    try {
        // Create WebSocket connection
        socket = new WebSocket("ws://localhost:8080/chat-app/chat");
        
        // Connection opened
        socket.onopen = function(event) {
            console.log("WebSocket connection established");
            updateConnectionStatus(true);
            
            // Clear reconnect interval if it exists
            if (reconnectInterval) {
                clearInterval(reconnectInterval);
                reconnectInterval = null;
            }
            
            // Send any queued messages
            while (messageQueue.length > 0) {
                const message = messageQueue.shift();
                socket.send(message);
            }
        };
        
        // Message received from server
        socket.onmessage = function(event) {
            let chatWindow = document.getElementById("chatWindow");
            let message = document.createElement("div");
            message.className = "chat-message";
            message.textContent = event.data;
            
            // Add message to chat window
            chatWindow.appendChild(message); // Fixed typo: was 'maessage'
            
            // Scroll to bottom
            chatWindow.scrollTop = chatWindow.scrollHeight;
            
            // Show notification if page is not visible
            if (typeof showNotification === 'function') {
                showNotification(event.data);
            }
            
            // Add animation effect
            message.style.opacity = '0';
            message.style.transform = 'translateY(10px)';
            setTimeout(() => {
                message.style.transition = 'opacity 0.3s ease, transform 0.3s ease';
                message.style.opacity = '1';
                message.style.transform = 'translateY(0)';
            }, 10);
        };
        
        // Connection closed
        socket.onclose = function(event) {
            console.log("WebSocket connection closed", event);
            updateConnectionStatus(false);
            
            // Attempt to reconnect after 3 seconds
            if (!reconnectInterval) {
                reconnectInterval = setTimeout(() => {
                    console.log("Attempting to reconnect...");
                    connectWebSocket();
                }, 3000);
            }
        };
        
        // Connection error
        socket.onerror = function(error) {
            console.error("WebSocket error:", error);
            updateConnectionStatus(false);
        };
        
    } catch (error) {
        console.error("Failed to create WebSocket connection:", error);
        updateConnectionStatus(false);
    }
}

// Send message function
function sendMessage() { // Fixed typo: was 'snedMessage'
    let messageInput = document.getElementById("messageInput"); // Fixed typo: was 'message'
    let message = messageInput.value.trim();
    
    if (!message) {
        return; // Don't send empty messages
    }
    
    if (message.length > 500) {
        alert("Message is too long. Please keep it under 500 characters.");
        return;
    }
    
    if (socket && socket.readyState === WebSocket.OPEN) {
        socket.send(message);
        messageInput.value = "";
        
        // Focus back to input
        messageInput.focus();
    } else {
        // Queue message if not connected
        messageQueue.push(message);
        messageInput.value = "";
        
        // Show user that message is queued
        let chatWindow = document.getElementById("chatWindow");
        let queuedMessage = document.createElement("div");
        queuedMessage.className = "chat-message queued-message";
        queuedMessage.textContent = "[Queued] Your message: " + message;
        queuedMessage.style.fontStyle = "italic";
        queuedMessage.style.color = "#888";
        chatWindow.appendChild(queuedMessage);
        chatWindow.scrollTop = chatWindow.scrollHeight;
        
        // Try to reconnect
        if (!reconnectInterval) {
            connectWebSocket();
        }
    }
}

// Update connection status (this function should be available globally)
function updateConnectionStatus(connected) {
    const statusElement = document.getElementById('connectionStatus');
    if (statusElement) {
        if (connected) {
            statusElement.className = 'status-connected';
            statusElement.innerHTML = '<i class="fas fa-circle"></i> Connected';
        } else {
            statusElement.className = 'status-disconnected';
            statusElement.innerHTML = '<i class="fas fa-circle"></i> Disconnected';
        }
    }
}

// Utility function to format timestamps
function formatTimestamp(date) {
    return date.toLocaleTimeString('en-US', { 
        hour12: false, 
        hour: '2-digit', 
        minute: '2-digit' 
    });
}

// Clean up WebSocket connection when page unloads
window.addEventListener('beforeunload', function() {
    if (socket) {
        socket.close();
    }
    if (reconnectInterval) {
        clearTimeout(reconnectInterval);
    }
});

// Handle visibility change for notifications
document.addEventListener('visibilitychange', function() {
    if (!document.hidden && typeof updateConnectionStatus === 'function') {
        // Page became visible, check connection status
        if (socket && socket.readyState === WebSocket.OPEN) {
            updateConnectionStatus(true);
        } else {
            updateConnectionStatus(false);
        }
    }
});

// Export functions for global access
window.initializeChat = initializeChat;
window.sendMessage = sendMessage;
window.updateConnectionStatus = updateConnectionStatus;

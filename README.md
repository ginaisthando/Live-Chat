# BrightFuture University Chat Application

![Java](https://img.shields.io/badge/Java-11+-red?logo=java&logoColor=white)
![WebSocket](https://img.shields.io/badge/WebSocket-JSR%20356-blue?logo=oracle&logoColor=white)
![Tomcat](https://img.shields.io/badge/Server-Tomcat%209+-orange?logo=apache&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build-yellow?logo=apachemaven&logoColor=white)
![Status](https://img.shields.io/badge/Status-Active-success)
![License](https://img.shields.io/badge/License-Educational-blue)

![Repo Size](https://img.shields.io/github/repo-size/ginaisthando/Live-Chat)
![Last Commit](https://img.shields.io/github/last-commit/ginaisthando/Live-Chat)
![Open Issues](https://img.shields.io/github/issues/ginaisthando/Live-Chat)
![Stars](https://img.shields.io/github/stars/ginaisthando/Live-Chat?style=social)

A **real-time messaging platform** built for BrightFuture University students and faculty using **Java WebSocket technology**, **Servlets**, and **modern web technologies**.  

---

## Features

- **User Authentication**: Secure registration and login system
- **Real-time Messaging**: WebSocket-based live chat functionality
- **Modern UI**: Responsive design with professional styling
- **Camera Integration**: Photo capture and video interaction capabilities
- **Session Management**: Secure user session handling
- **Cross-platform**: Works on desktop and mobile devices

## Technology Stack

- **Backend**: Java Servlets, WebSocket (JSR 356 API)
- **Frontend**: HTML5, CSS3, JavaScript
- **Server**: Apache Tomcat or Jetty
- **Build Tool**: Maven
- **Real-time Communication**: WebSocket API

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/brightfuture/chat/
│   │       ├── servlets/
│   │       │   ├── LoginServlet.java
│   │       │   ├── RegistrationServlet.java
│   │       │   ├── HomeServlet.java
│   │       │   ├── ConversationsServlet.java
│   │       │   └── ChatServlet.java
│   │       └── websocket/
│   │           ├── ChatEndpoint.java
│   │           └── HttpSessionConfigurator.java
│   └── webapp/
│       ├── WEB-INF/
│       │   └── web.xml
│       ├── css/
│       │   └── styles.css
│       ├── js/
│       │   └── chat.js
│       ├── pages/
│       │   ├── login.html
│       │   ├── registration.html
│       │   ├── home.html
│       │   ├── conversations.html
│       │   └── error.html
│       └── index.html
├── pom.xml
└── README.md
```

## Prerequisites

- Java 11 or higher
- Apache Maven 3.6+
- Apache Tomcat 9+ or Jetty 9+
- Modern web browser with WebSocket support

## Setup Instructions

### 1. Clone/Download the Project

```bash
# If using Git
git clone <repository-url>
cd Live\ Chat

# Or extract the downloaded ZIP file
```

### 2. Build the Project

```bash
# Compile and package the application
mvn clean compile package
```

### 3. Deploy to Tomcat

#### Option A: Using Maven Tomcat Plugin
```bash
# Start embedded Tomcat server
mvn tomcat7:run
```

#### Option B: Manual Deployment
1. Copy `target/chat-app.war` to Tomcat's `webapps` directory
2. Start Tomcat server
3. Access the application at `http://localhost:8080/chat-app`

### 4. Deploy to Jetty

```bash
# Start embedded Jetty server
mvn jetty:run
```

## Usage

### 1. Access the Application

Open your web browser and navigate to:
```
http://localhost:8080/chat-app
```

### 2. Demo Accounts

The application comes with pre-configured demo accounts:

- **Admin**: `admin` / `admin123`
- **Student**: `student1` / `password123`
- **Professor**: `professor1` / `prof123`

### 3. Create New Account

1. Click "Register here" on the login page
2. Fill in username (min 3 characters) and password (min 6 characters)
3. Confirm password and submit

### 4. Using the Chat

1. Login with your credentials
2. Navigate to "Live Conversations" from the home page
3. Start typing messages in the chat input
4. Messages appear in real-time for all connected users

### 5. Camera Features

1. From the home page, click "Open Camera"
2. Grant camera permissions when prompted
3. Use the controls to start camera, take photos, or stop

## Configuration

### WebSocket Endpoint

The WebSocket endpoint is configured at:
```
ws://localhost:8080/chat-app/chat
```

### Session Configuration

- Session timeout: 30 minutes
- HTTP-only cookies for security
- Automatic session cleanup

### Security Features

- Session-based authentication
- WebSocket connection validation
- XSS protection in message handling
- Secure cookie configuration

## API Endpoints

### Servlets

- `GET/POST /login` - User authentication
- `GET/POST /register` - User registration
- `GET /home` - Home dashboard
- `GET /conversations` - Chat interface
- `GET/POST /chat` - Chat history and message posting

### WebSocket

- `/chat` - Real-time messaging endpoint

## Customization

### Styling

Modify `src/main/webapp/css/styles.css` to customize the appearance:

- Colors and themes
- Layout and spacing
- Responsive breakpoints
- Animation effects

### Features

Extend functionality by:

- Adding database integration
- Implementing user roles
- Adding file upload capabilities
- Creating private messaging
- Adding emoji support

## Troubleshooting

### Common Issues

1. **Port Already in Use**
   ```bash
   # Change port in pom.xml or use different port
   mvn tomcat7:run -Dmaven.tomcat.port=8081
   ```

2. **WebSocket Connection Failed**
   - Ensure server is running
   - Check firewall settings
   - Verify WebSocket support in browser

3. **Session Issues**
   - Clear browser cookies
   - Restart the server
   - Check session timeout settings

### Development Mode

For development with auto-reload:

```bash
# Enable hot deployment
mvn jetty:run -Djetty.reload=automatic
```

## Browser Compatibility

- Chrome 60+
- Firefox 55+
- Safari 11+
- Edge 79+

## Security Considerations

### Production Deployment

1. **HTTPS**: Enable SSL/TLS encryption
2. **Database**: Replace in-memory storage with database
3. **Authentication**: Implement proper password hashing
4. **Session Security**: Configure secure cookies
5. **Input Validation**: Add server-side validation
6. **Rate Limiting**: Implement message rate limiting

## Support

For technical support or questions:

- Check the troubleshooting section
- Review server logs in `logs/` directory

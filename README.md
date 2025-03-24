# Howudoin - Messaging Application

Howudoin is a messaging application similar to WhatsApp, built with **Spring Boot** and **MongoDB** for the backend and **React Native with TypeScript** for the frontend. It features individual and group messaging capabilities, as well as user and friend management functionalities.

---

## ✨ Features

### Backend
- **User Management**: Register, log in.
- **Friend Management**: Add, accept, and list friends.
- **Messaging**: One-on-one and group messaging with history retrieval.
- **Group Management**: Create groups, add members, and send group messages.

### Frontend
- **Authentication**: Register and log in.
- **Real-time Messaging**: Instant updates for messages and notifications.
- **User Experience**: Intuitive, responsive UI for mobile.

---

## ⚙️ Tech Stack

### Backend
- Java + Spring Boot  
- MongoDB  
- Gradle  

### Frontend
- React Native  
- TypeScript (.tsx)  

---

## 📁 Backend Setup

### ✅ Prerequisites
- Java 17+  
- Gradle  
- MongoDB (running locally or via Docker)  
- Postman (for API testing, optional)  

### 🔧 Run MongoDB with Docker (if needed)
```bash
docker run -d -p 27017:27017 --name howudoin-mongo mongo
```

### 🔨 Run the Backend
```bash
cd backend  
./gradlew bootRun  
```

The backend will start at http://localhost:8080

### 📬 API Endpoints

#### User
- POST /user/add — Register  
- POST /user/login — Login  

#### Friends
- POST /friends/add — Add friend  
- POST /friends/accept — Accept friend request  
- GET /friends — Get friend list  

#### Messages
- POST /messages/send — Send a direct message  
- GET /messages/getmessages — Retrieve chat history  

#### Groups
- POST /groups/create — Create a new group  
- POST /groups/{groupId}/add-member — Add member to a group  
- POST /groups/{groupId}/send — Send a message to a group  
- GET /groups/messages — Retrieve group chat history  
- GET /groups/members — List group members  

---

## 📱 Frontend Setup

### ✅ Prerequisites
- Node.js & npm  
- Expo CLI (npm install -g expo-cli)  
- Android Studio / Xcode (emulator) or real device with Expo Go  

### ▶️ Run the Frontend
```bash
cd frontend  
npm install  
npx expo start  
```

- Scan the QR code with the **Expo Go app** (Android/iOS)  
- Or run in an emulator  

### 📌 Connect to Backend
Make sure your frontend points to http://localhost:8080 (or your local IP if using a real device). You may need to update the API base URL in a config file.

---

## 🔐 Environment Variables

You might need to configure:
- JWT secret (in the backend)  
- MongoDB URI (if not using the default)  

You can create .env files or set them in application.properties.

---

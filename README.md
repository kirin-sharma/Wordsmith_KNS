# Wordsmith

Wordsmith is a **multithreaded, client-server word game** implemented in Java.  
Players connect to a central server, receive random letters, and attempt to form valid words for points.  
The project demonstrates **Java networking, concurrency, object-oriented design, and game logic**.

---

## Features

- **Multithreaded Server**  
  Handles multiple clients concurrently using Java threads.

- **Client Application**  
  Allows players to connect, receive letters, submit words, and see their scores.

- **Letter Pool Management**  
  Dynamically generated letter distribution with associated point values.

- **Word Validation**  
  Checks submitted words against a dictionary for validity.

- **Session Isolation**  
  Each game session maintains its own independent `LetterPool`.

---

## Technologies Used

- **Java 17**
- **Java Sockets** for networking
- **Multithreading** (`Thread`, `Runnable`)

---

## Project Structure
- WordsmithClient.java # Client-side game and IO logic
- WordsmithServer.java # Server supporting multiple client connections
- GameManager.java # Manages matching players into game sessions and tracking active sessions
- GameSession.java # Handles gameplay between two players, and turn-based play
- ClientHandler.java # Class facilitating and maintaining communication to a client
- Player.java # Tracks player state and score
- LetterPool.java # Handles letter distributions and point values
- WordValidator.java # Validates words against an English dictionary

## How to Run

### **Note** Start the server first, then connect one or more clients.

### 1. **Compile the Project**
javac *.java

### 2. **Start the Server**
java WordsmithServer

### 3. **Start the Client(s)**
java WordsmithClient

## Gameplay Overview
- Server starts and waits for incoming connections.
- Clients connect to the server and matched into a game session.
- On each turn, players may form words with 7 letters randomly chosen from the letter pool.
- The server validates each word and awards points based on letter values.
- The game continues until no more letters are available or each player has taken a set amount of turns.
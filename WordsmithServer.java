/**
 * Project: Wordsmith_KNS
 * Class: WordsmithServer
 * 
 * This class contains server-side logic to initialize a server and allow for client connections.
 * It creates a client handler thread for each new connection, and then passes that to the game manager to pair them up and allow gameplay.
 * 
 * @author Kirin Sharma
 * @version 2.0
 *
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WordsmithServer {

    private static final int SERVER_PORT = 12345; // Default server port
    private ServerSocket serverSocket; // Socket for the server
    private final GameManager gameManager; // The GameManager that will run on the server

    /**
     * Constructor to create a gameManager to run on the server
     */
    public WordsmithServer() {
        gameManager = new GameManager();
    } // end constructor

    /**
     * Method to start the server, and continuously accept client connections
     * and pass these clients to the gamemanager to be paired into game sessions.
     */
    public void start() {
        try {
            // Start the server
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Wordsmith Server started on port " + SERVER_PORT);

            // Continuously listen for client connections
            while (true) {
                Socket clientSocket = serverSocket.accept(); // Wait for client connection
                System.out.println("Client connected: " + clientSocket.getRemoteSocketAddress());

                // Create a new clienthandler for the connected client, and start a thread for it
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();

                // Add clientHandler to GameManager so it can match clients into game sessions
                gameManager.addClient(clientHandler);
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        } finally {
            shutdown(); // shut the server down
        }
    } // end start

    /**
     * Helper method to shut down the server when necessary by closing open sockets
     */
    private void shutdown() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            System.out.println("Server shut down.");
        } catch (IOException e) {
            System.err.println("Error closing server socket: " + e.getMessage());
        }
    } // end shutdown

    /**
     * Main method to start the server
     * @param args
     */
    public static void main(String[] args) {
        WordsmithServer server = new WordsmithServer();
        server.start();
    } // end main

} // end class

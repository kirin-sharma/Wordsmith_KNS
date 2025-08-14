/**
 * Project: Wordsmith_KNS
 * Class: ClientHandler
 * 
 * This class contains functionality to handle a client connection on the server-side.
 * It keeps the connection alive and facilitates communication.
 * 
 * @author Kirin Sharma
 * @version 2.0
 *
 */

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    
    private final Socket clientSocket; // the socket associated with the client
    private BufferedReader in; // input reader
    private PrintWriter out; // output writer

    private Player player; // the player associated with the client
    private boolean playerReady = false; // flag for if the player has been created yet

    /**
     * Constructor to initialize the ClientHandler and associate it to a client given a socket
     * @param socket
     */
    public ClientHandler(Socket socket) {
        clientSocket = socket;
    } // end constructor

    @Override
    /**
     * Override of the run method. Gets the player's name and notify's the server of their readiness.
     * Keeps socket alive for communication until the socket closes.
     */
    public void run() {
        try {
            // Initialize input and output streams
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Get the client's name, if valid
            String name = in.readLine();
            if (name == null || name.trim().isEmpty()) {
                out.println("Invalid name. Connection closing.");
                cleanup();
                return;
            }

            // Initialize a player object associated with this client handler with the inputted name and notify threads of readiness
            synchronized(this) {
                player = new Player(name);
                playerReady = true;
                notifyAll();
            }
            out.println("Welcome! You have joined the game as: " + name + ".");
            out.println("Type '0' at any time to quit the game.");
            out.println("You will have 8 chances to input words that total more points than your opponent.");

            
            // Keep thread alive until socket closes
            while (!clientSocket.isClosed()) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.err.println("Connection error with client: " + e.getMessage());
        } finally {
            cleanup(); // close the clientSocket
        }
    } // end run

    /**
     * Method to send a message to the client
     * @param message the message to send
     */
    public void sendMessage(String message) {
        out.println(message);
    } // end sendMessage

    /**
     * Method to receive and read input from the client
     * @return a String representation of the client's input
     */
    public String receiveMessage() {
        try {
            String res = in.readLine();
            return res;
        } catch (Exception e) {
            System.out.println("Error");
            return null;
        }
    } // end receiveMessage

    /**
     * Synchronized method telling threads to wait until the player is ready (initialized with a name)
     */
    public synchronized void waitUntilPlayerReady() {
        while(!playerReady) {
            try {
                wait();
            } catch (Exception e) {}
        }
    } // end waitUntilPlayerReady

    /**
     * Method to close the open clientSocket when it is no longer needed
     */
    public void cleanup() {
        try {
            if(clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (Exception e) {
            System.err.println("Error in cleanup: " + e.getMessage());
        }
    } // end cleanup

    /**
     * Getter for player
     * @return the player associated with this client handler
     */
    public Player getPlayer() {
        return player;
    } // end getPlayer

} // end class

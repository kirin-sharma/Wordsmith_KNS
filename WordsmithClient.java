/**
 * This class contains methods to manage a single client, allowing for them to connect to the server
 * and play a game.
 * @author Kirin Sharma
 * @version 1.0
 * CS 310 Final Project
 *
 */

import java.io.*;
import java.net.*;

public class WordsmithClient {
    private static final String SERVER_ADDRESS = "localhost"; // Use "localhost" for the local server
    private static final int SERVER_PORT = 12345; // Default server port
    private Socket socket;
    private BufferedReader inputReader; // reader to read user input
    private PrintWriter outputWriter; // outputWriter to send messages to the server
    private BufferedReader serverInput; // reader to read server responses

    /**
     * Default constructor initializes all field variables.
     */
    public WordsmithClient() 
    {
        try {
            // Connect to the server
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            inputReader = new BufferedReader(new InputStreamReader(System.in));
            outputWriter = new PrintWriter(socket.getOutputStream(), true);
            serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connected to the server at " + SERVER_ADDRESS + ":" + SERVER_PORT);
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        }
    } // end default constructor

    /**
     * Method containing logic for the game on the client-side.
     */
    public void startGame() 
    {
        try {
            // Read server messages and respond accordingly
            while (true) {
                String serverMessage = serverInput.readLine();
                if (serverMessage == null) break;  // Server closed connection

                // Display the server's message
                System.out.println("Server: " + serverMessage);

                // Check if it's the player's turn
                if (serverMessage.contains("enter a word")) {
                    // Receive the player's word
                    String word = inputReader.readLine();

                    // Send the word to the server
                    outputWriter.println(word);

                    // Wait for server feedback
                    serverMessage = serverInput.readLine();
                    System.out.println("Server: " + serverMessage);
                }
            }
        } catch (IOException e) {
            System.err.println("Error during game interaction: " + e.getMessage());
        } finally {
            try {
                socket.close();
                System.out.println("Connection closed.");
            } catch (IOException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    } // end startGame

    /**
     * Main method to create a new client and begin the game.
     * @param args
     */
    public static void main(String[] args) 
    {
        // Create a new client instance and start the game
        WordsmithClient client = new WordsmithClient();
        client.startGame();
    } // end main
    
} // end class

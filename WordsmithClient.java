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
import java.util.Scanner;

public class WordsmithClient {
    private static final String SERVER_ADDRESS = "localhost"; // Use "localhost" for the local server
    private static final int SERVER_PORT = 12345; // Default server port

    private Socket socket;
    private BufferedReader inputReader; // reader to read user input
    private PrintWriter outputWriter; // outputWriter to send messages to the server
    private Scanner scanner;

    /**
     * Method containing logic for the game on the client-side.
     */
    public void start() 
    {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputWriter = new PrintWriter(socket.getOutputStream(), true);
            scanner = new Scanner(System.in);

            System.out.println("Connected to the Wordsmith server.");
            handleSession();

        } catch (IOException e) {
            System.err.println("Could not connect to server: " + e.getMessage());
        } finally {
            close();
        }
    } // end start

    private void handleSession() throws IOException {
            sendPlayerName();
     
        while(true) {
            String serverMessage = inputReader.readLine();
            if(serverMessage == null) break;

            if(serverMessage.contains("Enter a word")) {
                System.out.println(serverMessage);
                String word = scanner.nextLine().trim();

                if (word.equalsIgnoreCase("0")) {
                    outputWriter.println("QUIT");
                    break;
                }
                outputWriter.println(word);

            } else if(serverMessage.contains("Game Over!")) {
                System.out.println(serverMessage);

            }  else {
                System.out.println(serverMessage);
            }
        }
    }

    /**
     * Helper method to send the player's name to the server
     * @throws IOException
     */
    private void sendPlayerName() throws IOException {
        System.out.println("Please enter your name: ");
        String name = scanner.nextLine().trim();
        if (name == null || name.isEmpty()) {
            System.out.println("Invalid name. Exiting...");
            outputWriter.println("QUIT");
            return;
        }

        outputWriter.println(name);
    } // end sendPlayerName

    /**
     * Helper method to close the connection to the server
     */
    private void close() {
        try {
            if (socket != null) socket.close();
            if (scanner != null) scanner.close();
            System.out.println("Disconnected from server");
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    } // end close

    /**
     * Main method to create a new client and begin the game.
     * @param args
     */
    public static void main(String[] args) 
    {
        // Create a new client instance and start the game
        WordsmithClient client = new WordsmithClient();
        client.start();
    } // end main
    
} // end class

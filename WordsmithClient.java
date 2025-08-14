/**
 * Project: Wordsmith_KNS
 * Class: WordsmithClient
 * 
 * This class contains client-side logic to allow for a client to connect to and be part of a game.
 * It initializes a client and facilitates interaction with the server, allowing for the client to take
 * part in a game session.
 * 
 * @author Kirin Sharma
 * @version 2.0
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
     * Method connecting to the server and intitializing input and output streams,
     * then calling a helper method to handle a game session once connected.
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

    /**
     * Helper method to facilitate communication with the server and allow the client
     * to participate in a game session.
     * @throws IOException
     */
    private void handleSession() throws IOException {
        sendPlayerName(); // send the player's name to the server
     
        // Loop to keep communication with the server intact until the game is ended
        while(true) {
            String serverMessage = inputReader.readLine();
            if(serverMessage == null) break;

            if(serverMessage.contains("Enter a word")) {
                System.out.println(serverMessage);
                String word = scanner.nextLine().trim();
                outputWriter.println(word);

            } else if(serverMessage.contains("You win!") || serverMessage.contains("You lost.") || serverMessage.contains("You tied!")) {
                System.out.println(serverMessage);
                close();
                break;

            }  else {
                System.out.println(serverMessage);
            }
        }
    } // end handleSession

    /**
     * Helper method to send the player's name to the server
     * @throws IOException
     */
    private void sendPlayerName() throws IOException {
        System.out.println("Please enter your name: ");
        String name = scanner.nextLine().trim();

        // Check for valid name
        if (name == null || name.isEmpty()) {
            System.out.println("Invalid name. Exiting...");
            close();
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
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    } // end close

    /**
     * Main method to initialize a new client and begin the game.
     * @param args
     */
    public static void main(String[] args) 
    {
        // Create a new client instance and start the game
        WordsmithClient client = new WordsmithClient();
        client.start();
    } // end main
    
} // end class

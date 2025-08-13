import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    
    private final Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private Player player;

    public ClientHandler(Socket socket) {
        clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            out.println("WELCOME: Please enter your name:");
             String name = in.readLine();
            if (name == null || name.trim().isEmpty()) {
                out.println("Invalid name. Connection closing.");
                cleanup();
                return;
            }

            player = new Player(name);
            out.println("You have joined the game as " + name);

            // Keep thread alive until socket closes
            while (!clientSocket.isClosed()) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.err.println("Connection error with client: " + e.getMessage());
        } finally {
            cleanup();
        }
    } // end run

    /**
     * Method to send a message to the server
     * @param message
     */
    public void sendMessage(String message) {
        out.println(message);
    } // end sendMessage

    /**
     * Getter for player
     * @return the player associated with this client handler
     */
    public Player getPlayer() {
        return player;
    } // end getPlayer

    public String receiveMessage() {
        try {
            return in.readLine();
        } catch (Exception e) {
            System.out.println("Error");
            return null;
        }
    }

    private void cleanup() {
        try {
            if(clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (Exception e) {
            System.err.println("Error in cleanup: " + e.getMessage());
        }
    }

} // end class

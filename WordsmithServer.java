import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WordsmithServer {

    private static final int SERVER_PORT = 12345;
    private ServerSocket serverSocket;
    private final GameManager gameManager;

    public WordsmithServer() {
        gameManager = new GameManager();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Wordsmith Server started on port " + SERVER_PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept(); // Wait for client connection
                System.out.println("Client connected: " + clientSocket.getRemoteSocketAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();

                // Add clientHandler to GameManager so it can match clients into sessions
                gameManager.addClient(clientHandler);
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        } finally {
            shutdown();
        }
    }

    public void shutdown() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            System.out.println("Server shut down.");
        } catch (IOException e) {
            System.err.println("Error closing server socket: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        WordsmithServer server = new WordsmithServer();
        server.start();
    }
}

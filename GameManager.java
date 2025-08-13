import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameManager {
    private final List<ClientHandler> waitingClients = Collections.synchronizedList(new ArrayList<>());
    private final List<GameSession> activeSessions = Collections.synchronizedList(new ArrayList<>());

    public synchronized void addClient(ClientHandler client) {
        waitingClients.add(client);
        tryMatchClients();
    }

    private synchronized void tryMatchClients() {
        while (waitingClients.size() >= 2) {
            ClientHandler p1 = waitingClients.remove(0);
            ClientHandler p2 = waitingClients.remove(0);
               
            p1.waitUntilPlayerReady();
            p2.waitUntilPlayerReady();
            
            GameSession session = new GameSession(p1, p2);
            activeSessions.add(session);
            new Thread(session).start();  // run session in new thread
          
        }
    } // end tryMatchClients

    public synchronized void removeClient(ClientHandler client) {
        waitingClients.remove(client);
        // Remove from active sessions if needed, handle cleanup
    }

    public synchronized void removeSession(GameSession session) {
        activeSessions.remove(session);
    }

}

/**
 * Project: Wordsmith_KNS
 * Class: GameManager
 * 
 * Class containing logic to manage games on the server-side. Contains methods to match players who connect to the server
 * together into a game session, and tracks active sessions and clients waiting to be matched with an opponent.
 * 
 * @author Kirin Sharma
 * @version 2.0
 *
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameManager {
    private final List<ClientHandler> waitingClients = Collections.synchronizedList(new ArrayList<>()); // A list of waiting clients
    private final List<GameSession> activeSessions = Collections.synchronizedList(new ArrayList<>()); // A list of active game sessions

    /**
     * Synchronized method to add a client to the list of those waiting, and attempt to match them
     * @param client the client to add
     */
    public synchronized void addClient(ClientHandler client) {
        waitingClients.add(client);
        tryMatchClients();
    } // end addClient

    /**
     * Synchronized method to attempt to match waiting clients and place them into a game session
     */
    private synchronized void tryMatchClients() {
        while (waitingClients.size() >= 2) {
            ClientHandler p1 = waitingClients.remove(0);
            ClientHandler p2 = waitingClients.remove(0);
               
            // Wait until the players are completely ready to pair them into a game session
            p1.waitUntilPlayerReady();
            p2.waitUntilPlayerReady();
            
            // Initialize a new game session between the two players
            GameSession session = new GameSession(p1, p2, this);
            activeSessions.add(session);
            new Thread(session).start();  // run session in new thread
          
        }
    } // end tryMatchClients

    /**
     * Synchronized method to remove a gamesession from the list of active sessions once the session is finished
     * @param session the game session to remove
     */
    public synchronized void removeSession(GameSession session) {
        activeSessions.remove(session);
    } // end removeSession

} // end class

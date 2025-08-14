/**
 * Project: Wordsmith_KNS
 * Class: GameSession
 * 
 * Class intended to be the primary controller of game logic, containing methods to facilitate
 * gameplay and turn-management for a game session between two players.
 * 
 * @author Kirin Sharma
 * @version 2.0
 *
 */

public class GameSession implements Runnable {
    // ClientHandlers for the players in the game session
    private final ClientHandler player1Handler;
    private final ClientHandler player2Handler; 

    // References to the players in the game session
    private final Player player1;
    private final Player player2;

    private final LetterPool letterPool; // the letter pool that will be used in the game session
    private int turns; // tracks the number of total turns taken between the two players
    private boolean connected = false;

    private GameManager gm; // Pointer to the GameManager holding this gamesession

    /**
     * Constructor to initialize all instance variables necessary to run a game session
     * @param player1Handler the clienthandler for player 1
     * @param player2Handler the clienthandler for player 2
     */
    public GameSession(ClientHandler player1Handler, ClientHandler player2Handler, GameManager gm) {
        this.player1Handler = player1Handler;
        this.player2Handler = player2Handler;
        this.player1 = player1Handler.getPlayer();
        this.player2 = player2Handler.getPlayer();
        this.letterPool = new LetterPool(); // shared letter pool for both players
        turns = 0;
        connected = true;
        this.gm = gm;
    } // end constructor

    @Override
    /**
     * Override of the run method, contains logic for turn-based play between the two players
     */
    public void run() {
        // Start with player1
        ClientHandler currentHandler = player1Handler;
        Player currentPlayer = player1;

        // Play until the letter pool is empty, or 16 turns have been taken (8 by each player)
        while (connected && !letterPool.isEmpty() && turns < 16) {

            currentPlayer.drawLetters(letterPool); // draw letters
            playWord(currentPlayer, currentHandler); // call to helper method to play a word (execute a turn)
            if(!connected) break;

            turns++; // increment turns

            // Switch turns
            if (currentHandler == player1Handler) {
                currentHandler = player2Handler;
                currentPlayer = player2;
            } else {
                currentHandler = player1Handler;
                currentPlayer = player1;
            }
        }

        // Game ended - notify players of results based on scores
        int p1Score = player1.getScore();
        int p2Score = player2.getScore();

        if(p1Score > p2Score) {
            player1Handler.sendMessage("You win! You scored " + p1Score + " points, and your opponent only scored " + p2Score + " points.");
            player2Handler.sendMessage("You lost. Your opponent scored " + p1Score + " points, and you scored " + p2Score + " points.");
        } else if(p2Score > p1Score) {
            player2Handler.sendMessage("You win! You scored " + p2Score + " points, and your opponent only scored " + p1Score + " points.");
            player1Handler.sendMessage("You lost. Your opponent scored " + p2Score + " points, and you scored " + p1Score + " points.");
        } else {
            player1Handler.sendMessage("You tied! You and your opponent both scored " + p1Score + " points.");
            player2Handler.sendMessage("You tied! You and your opponent both scored " + p2Score + " points.");
        }

    } // end run

    /**
     * Helper method to allow a player to have their turn.
     * They may play a word, or pass and redraw their letters
     * If neither of those options are valid, they will be notified of the error and their turn will be skipped.
     * @param player the player whose turn it is
     * @param handler the clienthandler for the player whose turn it is
     * @return
     */
    private boolean playWord(Player player, ClientHandler handler) {
        handler.sendMessage("It's your turn! Your letters: " + player.getRack().toString() + ". Enter a word or '1' to pass and redraw letters: ");


        String word = handler.receiveMessage(); // receive input from the player

        // If null, pass turn
        if (word == null) {
            handler.sendMessage("No word entered. Turn skipped.");
            return false;
        }

        word = word.trim().toLowerCase(); // trim the word and format to lowercase

        // If the player has elected to quit, inform both players and quit the game session
        if(word.equals("0")) {
            player1Handler.sendMessage("You or your opponent has elected to quit the game. Closing the connection now.");
            player2Handler.sendMessage("You or your opponent has elected to quit the game. Closing the connection now.");
            player1Handler.cleanup();
            player2Handler.cleanup();
            connected = false;
            gm.removeSession(this);
        }

        // If the player has elected to redraw letters, allow them to do so
        if(word.equals("1")) {
            player.redrawLetters(letterPool);
            handler.sendMessage("You have chosen to pass this turn and redraw letters.");
            return false;
        }

        // If the player does not have the necessary letters for the word, inform and pass the turn
        if (!player.canFormWord(word)) {
            handler.sendMessage("You don't have the necessary letters. Turn passed.");
            return false;
        }

        // If the word is invalid, inform and pass the turn
        if (!WordValidator.isValidWord(word)) {
            handler.sendMessage("Invalid word. Turn passed.");
            return false;
        }

        player.playWord(word); // remove the used letters from the rack
        player.updateScore(letterPool.getWordPoints(word)); // update the player's score
        handler.sendMessage("Word accepted! Your score: " + player.getScore());

        return true;
    } // end playWord

} // end class

public class GameSession implements Runnable {
    private final ClientHandler player1Handler;
    private final ClientHandler player2Handler;
    private final Player player1;
    private final Player player2;
    private final LetterPool letterPool;
    private boolean isGameOver;
    private int turns;

    public GameSession(ClientHandler player1Handler, ClientHandler player2Handler) {
        this.player1Handler = player1Handler;
        this.player2Handler = player2Handler;
        this.player1 = player1Handler.getPlayer();
        this.player2 = player2Handler.getPlayer();
        this.letterPool = new LetterPool(); // shared letter pool for both players
        this.isGameOver = false;
        turns = 0;
    }

    @Override
    public void run() {
        ClientHandler currentHandler = player1Handler;
        Player currentPlayer = player1;

        while (!letterPool.isEmpty() && turns < 16) {
            currentPlayer.drawLetters(letterPool);
            playWord(currentPlayer, currentHandler);

            // Switch turns
            if (currentHandler == player1Handler) {
                currentHandler = player2Handler;
                currentPlayer = player2;
            } else {
                currentHandler = player1Handler;
                currentPlayer = player1;
            }
            turns++;
        }


        // FLAG FLAG

        // Game ended - notify players
        player1Handler.sendMessage("Game Over! Final Score: " + player1.getScore());
        player2Handler.sendMessage("Game Over! Final Score: " + player2.getScore());
    }

    private boolean playWord(Player player, ClientHandler handler) {
        handler.sendMessage("It's your turn! Your letters: " + player.getRack().toString() + ". Enter a word or '1' to redraw letters: ");

        String word = handler.receiveMessage(); // You may want to add a timeout or null check
        if (word == null) {
            handler.sendMessage("No word entered. Turn skipped.");
            return false;
        }

        word = word.trim().toLowerCase();

        if(word.equals("1")) {
            player.redrawLetters(letterPool);
            handler.sendMessage("You have chosen to pass this turn and redraw letters.");
            return false;
        }

        if (!player.canFormWord(word)) {
            handler.sendMessage("You don't have the necessary letters.");
            return false;
        }

        if (!WordValidator.isValidWord(word)) {
            handler.sendMessage("Invalid word.");
            return false;
        }

      

        player.playWord(word); // this should deduct letters and update score
        player.updateScore(letterPool.getWordPoints(word));
        handler.sendMessage("Word accepted! Your score: " + player.getScore());

        return true;
    }

}

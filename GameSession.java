public class GameSession implements Runnable {
    private final ClientHandler player1Handler;
    private final ClientHandler player2Handler;
    private final Player player1;
    private final Player player2;
    private final LetterPool letterPool;
    private int turns;

    public GameSession(ClientHandler player1Handler, ClientHandler player2Handler) {
        this.player1Handler = player1Handler;
        this.player2Handler = player2Handler;
        this.player1 = player1Handler.getPlayer();
        this.player2 = player2Handler.getPlayer();
        this.letterPool = new LetterPool(); // shared letter pool for both players
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

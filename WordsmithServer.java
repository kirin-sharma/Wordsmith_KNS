/**
 * This class contains methods and logic for the Wordsmith server, allowing it to handle multiple games
 * and ensure concurrency.
 * and play a game.
 * @author Kirin Sharma
 * @version 1.0
 * CS 310 Final Project
 *
 */

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.concurrent.*;

public class WordsmithServer 
{
	private static final int PORT = 12345; // default port
	private ServerSocket serverSocket; // socket to create for the server
	private WordValidator wordValidator; // to validate received words
	private ExecutorService threadPool;
	private LinkedList<Player> playerQueue; // a queue of players attempting to begin a game

	/**
	 * Default constructor initializes field variables
	 */
	public WordsmithServer() 
	{
		try 
		{
			serverSocket = new ServerSocket(PORT);
			wordValidator = new WordValidator(); // Shared across all games
			threadPool = Executors.newCachedThreadPool();  // To handle multiple client threads
			playerQueue = new LinkedList<Player>();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	} // end default constructor

	/**
	 * Method to start the server, and create a new thread for each client connection
	 */
	public void startServer() 
	{
		System.out.println("Server started, waiting for connections...");

		while (true) 
		{
			try 
			{
				Socket clientSocket = serverSocket.accept();
				System.out.println("New client connected: " + clientSocket.getInetAddress());

				// Create a Runnable task to handle the client connection
				Runnable clientTask = new Runnable() {
					@Override
					public void run() {
						handleClientConnection(clientSocket);  // Call method to handle the client connection
					}
				};

				threadPool.submit(clientTask);

			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	} // end startServer

	/**
	 * Method containing logic to handle each new client connection, by matching with an opponent
	 * and then controlling their game.
	 * @param clientSocket
	 */
	private void handleClientConnection(Socket clientSocket) 
	{
		try {
			// Create a new player for the connecting client
			Player player = createPlayer(clientSocket);

			// Add player to matchmaking queue
			synchronized (playerQueue) 
			{
				playerQueue.add(player);
				playerQueue.notify(); // Notify waiting threads if another player has joined
			}

			GameManager gameManager = null;

			// Wait for a second player to join and create a game
			synchronized (playerQueue) 
			{
				while (playerQueue.size() < 2) 
				{
					playerQueue.wait(); // Wait for another player to join
				}

				// Retrieve two players from the queue to start a new game
				Player player1 = playerQueue.poll();
				Player player2 = playerQueue.poll();

				// Initialize the GameManager for this game session
				gameManager = new GameManager(player1, player2, wordValidator);
			}

			// Handle the game loop, process player moves, etc.
			while (true) 
			{
				Player activePlayer = gameManager.getActivePlayer();

				// Assign letters to the player and inform the active player it's their turn
				gameManager.assignLettersToActivePlayer();
				activePlayer.sendMessage("Your turn! Your letters: " + activePlayer.getLetters());

				// Wait for the active player to submit a word
				String word = receiveWordFromPlayer(activePlayer, gameManager);
				
				// Logic to inform both players of action taken, either a played word or pass
				if(word.contains("1"))
				{
					// Inform the active player that they chose to redraw
					activePlayer.sendMessage("You chose to pass and redraw letters for next turn.");
					activePlayer.sendMessage("Your total score: " + activePlayer.getScore());

					// Switch to the other player's turn
					gameManager.switchTurn();
					activePlayer = gameManager.getActivePlayer();
					activePlayer.sendMessage("Your opponent chose to pass and redraw letters for their next turn.");
					activePlayer.sendMessage("Their total score is: " + gameManager.getInactivePlayer().getScore());
				}
				else {
					// Calculate the score and update the player's score
					int wordScore = gameManager.calculateWordScore(word);
					activePlayer.updateScore(wordScore);

					// Inform the active player of their score and total
					activePlayer.sendMessage("Word accepted! You scored: " + wordScore + " points.");
					activePlayer.sendMessage("Your total score: " + activePlayer.getScore());

					// Switch to the other player's turn
					gameManager.switchTurn();
					activePlayer = gameManager.getActivePlayer();
					activePlayer.sendMessage("Your opponent played the word: " + word);
					activePlayer.sendMessage("Their total score is: " + gameManager.getInactivePlayer().getScore());
				}
				// Check for game completion when the letter bag is empty
				if(isGameOver(gameManager))
				{
					endGame(gameManager);
					break;
				}
			} 
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	} // end handleClientConnection

	/**
	 * Method to end the game
	 * @param gameManager
	 */
	private void endGame(GameManager gameManager) {
		Player player1 = gameManager.getPlayer1();
		Player player2 = gameManager.getPlayer2();

		try {
			// Calculate final scores
			int score1 = player1.getScore();
			int score2 = player2.getScore();

			// Determine the outcome
			String outcome;
			if (score1 > score2) {
				outcome = "Player 1 wins with " + score1 + " points!";
			} else if (score2 > score1) {
				outcome = "Player 2 wins with " + score2 + " points!";
			} else {
				outcome = "It's a tie! Both players scored " + score1 + " points.";
			}

			// Notify players the game is over
			player1.sendMessage("Game Over! " + outcome);
			player2.sendMessage("Game Over! " + outcome);

			// Print the result
			System.out.println("Game ended: " + outcome);

			// Clean up resources
			player1.closeConnection();
			player2.closeConnection();

		} catch (Exception e) {
			System.err.println("Error ending game: " + e.getMessage());
		}
	} // end endGame

	/**
	 * Method containing logic to determine if the game is in a complete state
	 * @param gameManager
	 * @return
	 */
	private boolean isGameOver(GameManager gameManager) 
	{
		return (gameManager.getLetterPool().getLetterBag().size() == 0);
	} // end isGameOver

	/**
	 * Method to prompt a player for a word and ensure its validity.
	 * @param activePlayer
	 * @param gm
	 * @return
	 * @throws IOException
	 */
	private String receiveWordFromPlayer(Player activePlayer, GameManager gm) throws IOException 
	{
		BufferedReader input = new BufferedReader(new InputStreamReader(activePlayer.getClientSocket().getInputStream()));
		String word;

		while (true) {
			// Prompt the player for input
			activePlayer.sendMessage("Please enter a word (or enter 1 to pass and redraw new letters): ");

			// Read the word submitted by the player
			word = input.readLine();

			if (word == null) 
			{
				// If the client disconnects, return null
				System.out.println("Client disconnected.");
				return null; 
			}

			word = word.trim().toUpperCase();

			if (gm.validateWord(word)) 
			{
				System.out.println("Received valid word: " + word);
				return word;
			} else {
				// Inform the player of invalid word and continue prompting
				activePlayer.sendMessage("Invalid word. Please try again.");
			}
		}
	} // end receiveWordFromPlayer

	/**
	 * Method to create a new player from the incoming client connection
	 * @param clientSocket
	 * @return
	 * @throws IOException
	 */
	private Player createPlayer(Socket clientSocket) throws IOException 
	{
		Player player = new Player(clientSocket);

		return player;
	} // end createPlayer

} // end class

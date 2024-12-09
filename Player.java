/**
 * This class represents an individual player in the game.
 * @author Kirin Sharma
 * @version 1.0
 * CS 310 Final Project
 *
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Player 
{
	private static int playerCount = 0; // counts how many players have been created
	
	private int playerID; // unique identifier for the player
	private int score; // tracks the player's score in their game
	private boolean isTurn; // tracks whether it is the player's turn or not
	private ArrayList<Character> letters; // the letters a player has to play
	private Socket clientSocket; // socket for each player
	private PrintWriter output; // output writer allowing for communication with the player
	
	/**
	 * Default constructor initializes all instance variables
	 */
	public Player(Socket socket)
	{
		playerID = playerCount;
		playerCount++; // increment player count
		score = 0;
		isTurn = false;
		letters = new ArrayList<Character>();
		clientSocket = socket;
		try
		{
            output = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) 
		{
            e.printStackTrace();
        }
	} // end default constructor

	/**
	 * Getter for playerID
	 * @return the playerID
	 */
	public int getPlayerID() 
	{
		return playerID;
	} // end getPlayerID

	/**
	 * Getter for score
	 * @return the score
	 */
	public int getScore() 
	{
		return score;
	} // end getScore

	/**
	 * Function to update the player's score
	 * @param points the amount of points to add
	 */
	public void updateScore(int points) 
	{
		score += points;
	} // end updateScore

	/**
	 * Getter for isTurn
	 * @return the isTurn
	 */
	public boolean isTurn() 
	{
		return isTurn;
	} // end isTurn

	/**
	 * Setter for isTurn
	 * @param isTurn
	 */
	public void setTurn(boolean isTurn) 
	{
		this.isTurn = isTurn;
	} // end setTurn
	
	/**
	 * Getter for letters
	 * @return
	 */
	public ArrayList<Character> getLetters() 
	{
        return letters;
    } // end getLetters

	/**
	 * Setter for letters
	 * @param letters
	 */
    public void setLetters(ArrayList<Character> letters) 
    {
        this.letters = letters;
    } // end setLetters

	@Override
	/**
	 * Override of the toString method
	 */
	public String toString() 
	{
		return "Player [playerID=" + playerID + ", score=" + score + ", isTurn=" + isTurn + ", letters=" + letters
				+ "]";
	} // end toString
    
	/**
	 * Getter for clientSocket
	 * @return 
	 */
	public Socket getClientSocket()
	{
		return clientSocket;
	}// end getClientSocket
	
	/**
	 * Method to send a message to the player
	 * @param message
	 */
	public void sendMessage(String message)
	{
		if (output != null) 
		{
            output.println(message);
        }
	} // end sendMessage
	
	/**
	 * Method to close the player socket
	 */
	public void closeConnection()
	{
		try {
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} // end closeConnection
	
	/**
	 * Method to determine if the player's word attempt is valid,
	 * meaning if they have the necessary letters to make the word.
	 * @param word
	 * @return
	 */
	public boolean checkValidWordAttempt(String word)
	{
		if(word.length() > 7 || word.length() == 0)
			return false;
		
		@SuppressWarnings("unchecked")
		ArrayList<Character> lettersCopy = (ArrayList<Character>) letters.clone();
		
		char[] wordChars = word.toCharArray();
		
		// Remove letters from the available letters one by one and check if they are in there
		for(Character c : wordChars)
		{
			if(!lettersCopy.remove(c))
				return false;
		}
		
		// If it was a valid word, remove the characters from the real letter pool and return true
		for(Character c : wordChars)
		{
			letters.remove(c);
		}
		
		return true;
	} // end checkValidWordAttempt
	
} // end class

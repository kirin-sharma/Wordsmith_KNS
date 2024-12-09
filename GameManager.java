/**
 * This class contains methods to manage a single game between two players.
 * @author Kirin Sharma
 * @version 1.0
 * CS 310 Final Project
 *
 */

import java.util.*;

public class GameManager 
{
    private Player player1;
    private Player player2;
    private int currentTurn;
    private LetterPool letterPool;
    private WordValidator wordValidator;

    /**
     * Preferred constructor initializes field values
     * @param player1
     * @param player2
     */
    public GameManager(Player player1, Player player2, WordValidator wv) 
    {
        this.player1 = player1;
        this.player2 = player2;
        currentTurn = 1; // Player 1 starts
        letterPool = new LetterPool();
        wordValidator = wv;
    } // end default constructor

    /**
     * Method which returns the active player
     * @return
     */
    public Player getActivePlayer()
    {
    	if(currentTurn == 1)
    		return player1;
    	else
    		return player2;
    } // end getActivePlayer
    
    /**
     * Method which returns the inactive player
     * @return
     */
    public Player getInactivePlayer()
    {
    	if(currentTurn == 1)
    		return player2;
    	else
    		return player1;
    } // end getInactivePlayer
    
    /**
     * Method to switch whose turn it is
     */
    public void switchTurn()
    {
    	if(currentTurn == 1)
    		currentTurn = 2;
    	else
    		currentTurn = 1;
    } // end switchTurn
    
    /**
     * Method to assign 7 letters to the active player.
     * If they already have some letters, they will be given enough to add up to 7.
     */
    public void assignLettersToActivePlayer() 
    {
    	int numberCurrentLetters = getActivePlayer().getLetters().size();
    	int lettersInLetterPool = letterPool.getLetterBag().size();
    	int lettersNeeded = 7 - numberCurrentLetters;
    	
    	if(lettersNeeded > lettersInLetterPool)
    		lettersNeeded = lettersInLetterPool;
    	
        ArrayList<Character> lettersToAdd = letterPool.getRandomLetters(lettersNeeded);
        getActivePlayer().getLetters().addAll(lettersToAdd);
    } // end assignLettersToActivePlayer
    
    /**
     * Method to allow the player to return their current letters to the letter pool and redraw
     * a new random 7
     */
    public void redrawLetters()
    {
    	Player activePlayer = getActivePlayer();
    	// Return current letters to letter pool
    	while(!activePlayer.getLetters().isEmpty())
    	{
    		letterPool.getLetterBag().add(activePlayer.getLetters().remove(0));
    	}
    	// Draw new letters
    	assignLettersToActivePlayer();
    } // end redrawLetters
    
    /**
	 * Method to calculate the score of a given word based on the letter scoring system.
	 * @return the score of the word
	 */
	public int calculateWordScore(String word)
	{
		word = word.toUpperCase();
		int score = 0;
		for(char c : word.toCharArray())
		{
			score += letterPool.getLetterPoints(c);
		}
		
		return score;
	} // end calculateWordScore

	/**
	 * Validates a word using the WordValidator object
	 * @param word the word to validate
	 * @return
	 */
	public boolean validateWord(String word)
	{
		if(word.contains("1"))
		{
			redrawLetters();
			return true;
		}
		return wordValidator.isValidWord(word) && getActivePlayer().checkValidWordAttempt(word);
	} // end validateWord
	
	/**
	 * Getter for letterPool
	 * @return
	 */
	public LetterPool getLetterPool()
	{
		return letterPool;
	} // end getLetterPool
	
	@Override
	/**
	 * Override of the toString method
	 */
	public String toString() 
	{
		return "GameManager [player1=" + player1 + ", player2=" + player2 + ", currentTurn=" + currentTurn
				+ ", letterPool=" + letterPool + "]";
	} // end toString

	/**
	 * Getter for player1
	 * @return the player1
	 */
	public Player getPlayer1() 
	{
		return player1;
	} // end getPlayer1

	/**
	 * Getter for player2
	 * @return the player2
	 */
	public Player getPlayer2() 
	{
		return player2;
	} // end getPlayer2
	
} // end class

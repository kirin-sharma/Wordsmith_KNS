/**
 * This class represents an individual player in the game.
 * @author Kirin Sharma
 * @version 1.0
 * CS 310 Final Project
 *
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player 
{	 
	private final String name; // the name of the player
	private List<Character> rack; // the letters a player has to play
	private int score; // tracks the player's score in their game
	private boolean isTurn; // tracks whether it is the player's turn or not
	
	/**
	 * Constructor to initialize a new player with the given name.
	 * Initializes all instance variables
	 * @param playerName the name of the player
	 */
	public Player(String playerName) {
		name = playerName;
		score = 0;
		isTurn = false;
		rack = new ArrayList<>();
	} // end default constructor

	/**
	 * Method to add letters to the player's rack
	 * @param newLetters the letters to add to the player's rack
	 */
	public void addLetters(List<Character> newLetters) {
		if(newLetters != null) {
			rack.addAll(newLetters);
		}
	} // end addLetters

	/**
	 * Determines whether the player can form a given word with the letters in its rack 
	 * @param word the word to check
	 * @return true if it is possible to form the word with the current rack, false otherwise
	 */
	public boolean canFormWord(String word) {
		if(word == null) return false;

		Map<Character, Integer> letterCounts = new HashMap<>();
		for (char c : rack) {
			letterCounts.put(c, letterCounts.getOrDefault(c, 0) + 1);
		}

		for (char c : word.toUpperCase().toCharArray()) {
			if (!letterCounts.containsKey(c) || letterCounts.get(c) == 0) {
				return false;
			}
			letterCounts.put(c, letterCounts.get(c) - 1);
		}

		return true;
	} // end canFormWord

	/**
	 * Method to remove letters from the rack given the characters in a word
	 * @param word the word which letters should be removed from rack
	 */
	public void removeUsedLetters(String word) {
		for(char c : word.toCharArray()) {
			rack.remove((Character) c);
		}
	} // end removeUsedLetters
	
	/**
	 * Getter for name
	 * @return the name of the player
	 */
	public String getName() {
		return name;
	} // end getName

	/**
	 * Getter for score
	 * @return the score
	 */
	public int getScore() {
		return score;
	} // end getScore

	/**
	 * Function to update the player's score
	 * @param points the amount of points to add
	 */
	public void updateScore(int points) {
		score += points;
	} // end updateScore

	/**
	 * Getter for isTurn
	 * @return the isTurn
	 */
	public boolean isTurn() {
		return isTurn;
	} // end isTurn

	/**
	 * Setter for isTurn
	 * @param isTurn
	 */
	public void setTurn(boolean isTurn) {
		this.isTurn = isTurn;
	} // end setTurn

	/**
	 * Getter for rack
	 * @return an unmodifiable list view of rack
	 */
	public List<Character> getRack() {
		return Collections.unmodifiableList(rack);
	} // end getRack

} // end class

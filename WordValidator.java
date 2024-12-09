/**
 * This class contains methods to validate words placed by a player.
 * @author Kirin Sharma
 * @version 1.0
 * CS 310 Final Project
 *
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;

public class WordValidator 
{
	private HashSet<String> words; 	// HashSet containing all valid English words
	
	/**
	 * Default constructor reads the list of alphabetical English words from the words_alpha.txt
	 * file. Credit: https://github.com/dwyl/english-words.git
	 */
	public WordValidator()
	{
		words = new HashSet<String>();
		try 
        {
            words.addAll(Files.readAllLines(Paths.get("words_alpha.txt")));
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
	} // end default Constructor
		
	/**
	 * Determine if a word is valid by checking if it is in the list of valid English
	 * words loaded earlier
	 * @param word
	 * @return
	 */
    public boolean isValidWord(String word) 
    {
        return words.contains(word.toLowerCase());
    } // end isValidWord
	
} // end class

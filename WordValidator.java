/**
 * Project: Wordsmith_KNS
 * Class: WordValidator
 * 
 * This class contains methods to validate words placed by a player.
 * 
 * @author Kirin Sharma
 * @version 2.0
 *
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.Collections;
import java.util.HashSet;
import java.util.logging.Logger;
import java.util.logging.Level;

public class WordValidator 
{
	private static final String FILE_PATH = "./words_alpha.txt"; // relative file path for the word list
	private static final Logger logger = Logger.getLogger(WordValidator.class.getName());

	// Static set of all valid english words shared by all instances of this class
	private static final Set<String> WORDS;

	// Static block to read all words into the WORDS set
	static {
		Set<String> tempWords = new HashSet<>();
		try {
			tempWords.addAll(Files.readAllLines(Paths.get(FILE_PATH)));
		} catch(IOException e) {
			logger.log(Level.SEVERE, "Error reading word list", e);
		}
		WORDS = Collections.unmodifiableSet(tempWords);
	}
		
	/**
	 * Determine if a word is valid by checking if it is in the list of valid English
	 * words loaded earlier
	 * @param word the word to check
	 * @return true if the word is valid, false if not
	 */
    public static boolean isValidWord(String word) 
    {
        return WORDS.contains(word.toLowerCase());
    } // end isValidWord
	
} // end class

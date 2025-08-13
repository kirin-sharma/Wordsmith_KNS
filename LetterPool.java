/**
 * This class represents the pool of letters available and their
 * respective points for the game Wordsmith.
 * @author Kirin Sharma
 * @version 1.0
 * CS 310 Final Project
 *
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class LetterPool 
{

    // Static maps mapping to letter distributions and points
    private static final Map<Character, Integer> LETTER_DISTRIBUTION = new HashMap<>();
    private static final Map<Character, Integer> LETTER_POINTS = new HashMap<>();

    static {
        // Define point values for letters
        String onePoint = "AEIOULNRST";
        String twoPoints = "DG";
        String threePoints = "BCMP";
        String fourPoints = "FHVWY";
        String fivePoints = "K";
        String eightPoints = "JX";
        String tenPoints = "QZ";

        // Frequencies for each letter in the group
        // Add each letter to the letterBag and insert into the letterPoints HashMap
        for (char c : onePoint.toCharArray())    LETTER_DISTRIBUTION.put(c, 12); // more vowels than consonants
        for (char c : twoPoints.toCharArray())   LETTER_DISTRIBUTION.put(c, 9);
        for (char c : threePoints.toCharArray()) LETTER_DISTRIBUTION.put(c, 6);
        for (char c : fourPoints.toCharArray())  LETTER_DISTRIBUTION.put(c, 5);
        for (char c : fivePoints.toCharArray())  LETTER_DISTRIBUTION.put(c, 3);
        for (char c : eightPoints.toCharArray()) LETTER_DISTRIBUTION.put(c, 2);
        for (char c : tenPoints.toCharArray())   LETTER_DISTRIBUTION.put(c, 1);

        // Assign points based on your groups
        for (char c : onePoint.toCharArray()) LETTER_POINTS.put(c, 1);
        for (char c : twoPoints.toCharArray()) LETTER_POINTS.put(c, 2);
        for (char c : threePoints.toCharArray()) LETTER_POINTS.put(c, 3);
        for (char c : fourPoints.toCharArray()) LETTER_POINTS.put(c, 4);
        for (char c : fivePoints.toCharArray()) LETTER_POINTS.put(c, 5);
        for (char c : eightPoints.toCharArray()) LETTER_POINTS.put(c, 8);
        for (char c : tenPoints.toCharArray()) LETTER_POINTS.put(c, 10);
    }


	private List<Character> letterBag; // Bag of letters in the letter pool
	
	/**
	 * Default constructor to initialize instance variables
	 */
	public LetterPool()
	{
		letterBag = new ArrayList<>();
        for(Map.Entry<Character, Integer> entry : LETTER_DISTRIBUTION.entrySet()) {
            char letter  = entry.getKey();
            int freq = entry.getValue();
            for(int i = 0; i < freq; i++) {
                letterBag.add(letter);
            }
        }
	} // end default constructor

    /**
     * Method to return a list of letters to the letterBag.
     * This method is synchronized to ensure thread safety.
     * @param letters the list of letters to return to the letterBag
     */
    public synchronized void returnLetters(List<Character> letters) {
        letterBag.addAll(letters);
    } // end returnLetters

    /**
     * Helper method to get the point value of a given letter.
     * @param letter the letter to check the value of
     * @return the point value of the letter
     */
    private int getLetterPoints(char letter) 
    {
        return LETTER_POINTS.getOrDefault(letter, 0);
    } // end getLetterPoints


    public int getWordPoints(String word) {
        if(word == null) return 0;

        int points = 0;
        for(char c : word.toCharArray()) {
            points += getLetterPoints(c);
        }

        return points;
    }
    /**
     * Getter for letterBag
     * @return the letterBag as a read-only list
     */
    public List<Character> getLetterBag()
    {
    	return Collections.unmodifiableList(letterBag);
    } // end getLetterBag
    

    public boolean isEmpty() {
        return letterBag == null || letterBag.isEmpty();
    }
} // end LetterPool

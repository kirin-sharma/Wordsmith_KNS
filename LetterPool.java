/**
 * This class represents the pool of letters available and their
 * respective points for the game Wordsmith.
 * @author Kirin Sharma
 * @version 1.0
 * CS 310 Final Project
 *
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class LetterPool 
{

	public HashMap<Character, Integer> letterPoints; // HashMap mapping letters to their point value
	private ArrayList<Character> letterBag; // Bag of letters in the letter pool
	private Random random;
	
	/**
	 * Default constructor to initialize field variables
	 */
	public LetterPool()
	{
		letterPoints = new HashMap<Character, Integer>();
		letterBag = new ArrayList<Character>();
		random = new Random();
		
		// Define point values for letters
        String onePoint = "AEIOULNRST";
        String twoPoints = "DG";
        String threePoints = "BCMP";
        String fourPoints = "FHVWY";
        String fivePoints = "K";
        String eightPoints = "JX";
        String tenPoints = "QZ";

        // Add each letter to the letterBag and insert into the letterPoints HashMap
        for (char c : onePoint.toCharArray()) addLetter(c, 1, 12); // more vowels than consonants
        for (char c : twoPoints.toCharArray()) addLetter(c, 2, 9);
        for (char c : threePoints.toCharArray()) addLetter(c, 3, 6);
        for (char c : fourPoints.toCharArray()) addLetter(c, 4, 5);
        for (char c : fivePoints.toCharArray()) addLetter(c, 5, 3);
        for (char c : eightPoints.toCharArray()) addLetter(c, 8, 2);
        for (char c : tenPoints.toCharArray()) addLetter(c, 10, 1);
        
	} // end default constructor
	
	/**
	 * Helper method to add letters to the letter bag and map each letter to its
	 * point value in the letterPoints HashMap.
	 * @param letter the letter to add
	 * @param points the amount of points the letter is worth
	 * @param frequency how many times the letter should appear in the letter bag
	 */
	private  void addLetter(char letter, int points, int frequency) 
	{
        letterPoints.put(letter, points); // map letter to its point value
        
        for (int i = 0; i < frequency; i++) 
        {
            letterBag.add(letter);
        }
    } // end addLetter
	
	/**
	 * Method to generate a list of random letters from the letter bag.
	 * The length of the list is specified by the parameter count.
	 * @param count the number of letters to randomly select
	 * @return the list of letters retrieved
	 */
    public ArrayList<Character> getRandomLetters(int count) 
    {
        ArrayList<Character> letters = new ArrayList<Character>();
        for (int i = 0; i < count; i++) 
        {
            letters.add(letterBag.get(random.nextInt(letterBag.size())));
        }
        return letters;
    } // end getRandomLetters
	
    /**
     * Method to get the point value of a given letter.
     * @param letter the letter to check the value of
     * @return the point value of the letter
     */
    public int getLetterPoints(char letter) 
    {
        return letterPoints.getOrDefault(letter, 0);
    } // end getLetterPoints

    /**
     * Getter for letterBag
     * @return
     */
    public ArrayList<Character> getLetterBag()
    {
    	return letterBag;
    } // end getLetterBag
    
	@Override
	/**
	 * Override of the toString method
	 */
	public String toString() 
	{
		return "LetterPool [letterPoints=" + letterPoints + ", letterBag=" + letterBag + ", random=" + random + "]";
	} // end toString
    
} // end LetterPool

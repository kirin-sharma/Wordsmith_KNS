/**
 * This class contains the application initializing the Wordsmith server.
 * @author Kirin Sharma
 * @version 1.0
 * CS 310 Final Project
 *
 */

public class Application 
{

	public static void main(String[] args) 
	{
		System.out.println("Starting Wordsmith Server.");
		
		WordsmithServer server = new WordsmithServer();
		
		server.startServer();
	} // end main

} // end class

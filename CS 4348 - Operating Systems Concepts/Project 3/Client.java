import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The client for the ticket system.
 * Usage: java Client [host] [port]
 * @author Rahat
 *
 */
public class Client
{
	static Scanner inputScanner;
	static Socket socket;
	static BufferedReader in;
	static PrintWriter out;	
	public static void main(String[] args)
	{
		// Get host and port arguments
		if(args.length < 2)
		{
			System.err.println("Not enough arguments. Usage: \"java Client [host] [port]\"");
			System.exit(-1);
		}
		String host = args[0];
		short port = -1;
		try
		{
			port = Short.parseShort(args[1]);
		} catch (NumberFormatException e) {
			System.err.println("Unable to parse port.");
			System.exit(-1);
		}

		// Try to open a connection to the host:port
		try
		{
			socket = new Socket(host, port);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
		} catch (IOException e)
		{
			System.err.println("Unable to connect to host.");
			System.exit(-1);
		}
		
		try
		{
			inputScanner = new Scanner(System.in);
			boolean finished = false;
			while(!finished)
			{
				// Display menu
				System.out.println("A. Display the list of movies\n"
						+ "B. Purchase tickets\n"
						+ "C. Exit\n");
				
				// Get and do choice
				char choice = query("choice").toUpperCase().charAt(0);
				String line;
				switch(choice)
				{
					case 'A': // send LIST to server, read MOVIES and END-MOVIES
						sendMessage("LIST");
						ArrayList<String> movies = new ArrayList<>();
						line = "";
						while(!(line = in.readLine()).equals("END-MOVIES"))
						{
							String[] split = line.split(" ", 3);
							movies.add(String.format("\t%s.\t%s", split[1], split[2]));
						}
						System.out.println("Movie list:");
						for(String movieLine : movies)
							System.out.println(movieLine);
						System.out.println();
						break;
					case 'B': // query for movie and quantity, attempt to buy
						int movieID = queryInt("the number of the movie in the list");
						int quantity = queryInt("the number of tickets");
						sendMessage("RESERVE "+movieID+" "+quantity);
						line = in.readLine();
						// Server says not enough tickets available
						if(line.equals("SOLD-OUT"))
						{
							System.out.println("\nSorry, there are not enough seats left to purchase "+quantity+" tickets.\n");
							break;
						}
						// Attempt to confirm or cancel the purchase
						int confirm;
						do
						{
							confirm = queryInt("1 to confirm, 2 to cancel");
							if(confirm == 1)
								sendMessage("CONFIRM");
							else if(confirm == 2)
								sendMessage("CANCEL");
						} while (confirm != 1 && confirm != 2);
						line = in.readLine();
						if(line.equals("CONFIRMED"))
							System.out.println("\nPurchase successful! Proceed to theater to make payment and pickup tickets.\n");
						else if(line.equals("CANCELLED"))
							System.out.println("\nYour ticket purchase was cancelled.\n");
						break;
					case 'C': // Exit the client
						finished = true;
						socket.close();
						break;
				}
			}
		} catch (IOException e)
		{
			System.err.println("Encountered IO problem.");
			System.exit(-1);
		}
		
		
	}
	
	/**
	 * Convenience method to send message to Server.
	 * @param message the message to send
	 */
	public static void sendMessage(String message)
	{
		out.println(message);
		out.flush();
	}
	
	/**
	 * Convenience method to query for a string
	 * @param name What to tell the user to input
	 * @return What the user input
	 */
	public static String query(String name)
	{
		System.out.print("Enter " + name + ": ");
		return inputScanner.nextLine();
	}
	
	/**
	 * Convenience method to query for an integer.
	 * Retries if user doesn't input a proper integer.
	 * @param name What to tell the user to input
	 * @return What the user input as an integer
	 */
	public static int queryInt(String name)
	{
		int input = Integer.MIN_VALUE;
		do
		{
			System.out.print("Enter " + name + ": ");
			String line = inputScanner.nextLine();
			try
			{
				input = Integer.parseInt(line);
			} catch (NumberFormatException e) {
				System.out.println("That's not a number! Try again!");
			}
		} while (input == Integer.MIN_VALUE);
		return input;
	}

}

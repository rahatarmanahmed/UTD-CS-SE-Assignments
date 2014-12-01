import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The server for the ticket system.
 * Usage: java Server [port]
 * @author Rahat
 *
 */
public class Server
{
	static String movies[];
	static int seats[];
	
	static ServerSocket server;
	
	public static void main(String[] args)
	{
		// Get port from arguments
		String filename = "movies.txt";
		short port = -1;
		try
		{
			if(args.length >= 1)
				port = Short.parseShort(args[0]);
			else
			{
				System.err.println("No port given. Usage \"java Server [port]\"");
				System.exit(-1);
			}
		} catch (NumberFormatException e) {
			System.err.println("Unable to parse port.");
			System.exit(-1);
		}
		
		// Read movie file
		ArrayList<String> lines = new ArrayList<>();
		try(Scanner scan = new Scanner(new File(filename)))
		{
			while(scan.hasNextLine())
				lines.add(scan.nextLine());
		} catch (FileNotFoundException e)
		{
			System.err.println("ERROR: " + filename + " was not found.");
			System.exit(-1);
		}
		// Parse file contents into arrays
		movies = new String[lines.size()];
		seats = new int[lines.size()];
		for(int k=0; k<lines.size(); k++)
		{
			String split[] = lines.get(k).split("\t");
			movies[k] = split[0];
			seats[k] = Integer.parseInt(split[1]);
		}
		
		// Start the server and begin accepting clients into threads
		try
		{
			server = new ServerSocket(port);
			System.out.println("Server is running on port "+port+"...");
			while(true)
			{
				try
				{
					Thread t = new Thread(new ServerThread(server.accept()));
					t.start();
				} catch (IOException e) {
					System.out.println("Client accept failed.");
					System.exit(-1);
				}
			}
		} catch (IOException e)
		{
			System.err.println("Unable to start server.");
			System.exit(-1);
		}
		
	}
	
	/**
	 * Attempts to reserve the requested amount of seats for the
	 * requested movie. 
	 * @param index index of movie from movies
	 * @param num number of tickets to reserve
	 * @return true if enough seats were available to be reserved, false otherwise
	 */
	public synchronized static boolean reserveSeats(int index, int num)
	{
		if(seats[index] - num >=0)
		{
			seats[index] -= num;
			return true;
		}
		return false;
	}
	
	/**
	 * The server thread that handles a single client.
	 * @author Rahat
	 *
	 */
	public static class ServerThread implements Runnable
	{
		BufferedReader in;
		PrintWriter out;
		Socket socket;
		int reserved;
		int reservedMovie;
		
		/*
		 * Constructor sets up input and output
		 * and misc variables
		 */
		public ServerThread(Socket s) throws IOException
		{
			socket = s;
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintWriter(s.getOutputStream(), true);
			reserved = 0;
			reservedMovie = -1;
		}

		@Override
		public void run()
		{
			System.out.println("Received client "+socket.getInetAddress().getHostAddress());
			String msg;
			// Keep reading the client's input until socket closed
			while(!socket.isClosed())
			{
				try
				{
					msg = in.readLine();
					if(msg == null)
						break;
					String[] split = msg.split(" ");
					String cmd = split[0];
					// Handle specific client commands
					switch(cmd)
					{
						case "LIST": // Requests a list of movies
							for(int k=0; k<movies.length; k++)
								sendMessage("MOVIE "+(k+1)+" "+movies[k]);
							sendMessage("END-MOVIES");
							break;
						case "RESERVE": // Attempts to reserve tickets "RESERVE [movieID] [quantity]"
							int movieID = Integer.parseInt(split[1]) - 1;
							int quantity = Integer.parseInt(split[2]);
							if(reserveSeats(movieID, quantity))
							{
								reservedMovie = movieID;
								reserved = quantity;
								sendMessage("RESERVED");
							}
							else
								sendMessage("SOLD-OUT");
							break;
						case "CANCEL": // Cancels the reserved tickets
							reserveSeats(reservedMovie, -reserved);
							reservedMovie = -1;
							reserved = 0;
							sendMessage("CANCELLED");
							break;
						case "CONFIRM": // Confirms the reserved tickets
							reserved = 0;
							sendMessage("CONFIRMED");
							break;
					}
				} catch (IOException e)
				{
					break;
				}
			}
			System.out.println("Client "+socket.getInetAddress().getHostAddress()+" has left");
		}
		
		/**
		 * Convenience method to send a message to the client.
		 * @param message the message to send
		 */
		public void sendMessage(String message)
		{
			out.println(message);
			out.flush();
		}
	}
}

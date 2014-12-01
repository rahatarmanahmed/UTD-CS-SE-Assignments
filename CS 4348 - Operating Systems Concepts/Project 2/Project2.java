import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Project2
{
	// How many customers to simulate
	public static final int NUM_CUSTOMERS = 300;
	
	// Semaphores!
	static Semaphore boxOfficeAvailable = new Semaphore(0, true);
	static Semaphore boxOffice[] = {new Semaphore(0, true), new Semaphore(0, true)};
	static Semaphore customerTicketRequest[] = {new Semaphore(0, true), new Semaphore(0, true)};
	static Semaphore ticketSale[] = {new Semaphore(0, true), new Semaphore(0, true)};
	static Semaphore ticketTakerAvailable = new Semaphore(0, true);
	static Semaphore ticketTakerCustomer = new Semaphore(0, true);
	static Semaphore ticketTaken = new Semaphore(0, true);
	static Semaphore concessionWorkerAvailable = new Semaphore(0, true);
	static Semaphore customerOrdered = new Semaphore(0, true);
	static Semaphore orderFulfilled = new Semaphore(0, true);
	
	// Globals used among the threads
	static String movies[];
	static int seats[];
	static int customerTicketRequestID[] = new int[2];
	static int customerTicketRequestMovie[] = new int[2];
	static int ticketTakerCustomerID;
	static String purchases[] = {"Popcorn", "Soda", "Popcorn and Soda"};
	static int customerOrder, customerOrdering;
	
	static Random random = new Random();

	/**
	 * Reads file from given argument, or defaults to "movies.txt",
	 * then simulates theater.
	 */
	public static void main(String[] args)
	{
		// Read movies file
		String filename = "movies.txt";
		if(args.length >= 1)
			filename = args[0];
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
		// Initialize all threads
		Thread boxOfficeAgent[] = { new Thread(new BoxOfficeAgent(0)),
									new Thread(new BoxOfficeAgent(1)) };
		Thread ticketTaker = new Thread(new TicketTaker());
		Thread concessionStandWorker = new Thread(new ConcessionStandWorker());
		Thread customers[] = new Thread[NUM_CUSTOMERS];
		for(int k=0; k<NUM_CUSTOMERS; k++)
			customers[k] = new Thread(new Customer(k));
		
		// Start all threads
		boxOfficeAgent[0].start();
		boxOfficeAgent[1].start();
		ticketTaker.start();
		concessionStandWorker.start();
		for(int k=0; k<NUM_CUSTOMERS; k++)
			customers[k].start();
		
		// Wait for all customers to join
		for(int k=0; k<NUM_CUSTOMERS; k++)
		{
			try
			{
				customers[k].join();
				System.out.println("Joined customer " + k);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		// Lazy way to stop infinitely running threads.
		boxOfficeAgent[0].interrupt();
		boxOfficeAgent[1].interrupt();
		ticketTaker.interrupt();
		concessionStandWorker.interrupt();
	}	

	/**
	 *	Thread that simulates a customer
	 */
	public static class Customer implements Runnable
	{
		// ID of the customer
		int id;
		public Customer(int id)
		{
			this.id = id;
		}
		
		@Override
		public void run()
		{
			try
			{
				System.out.println("Customer " + id + " created");
				
				// Pick a random movie
				int movieID = random.nextInt(movies.length);
				System.out.println("Customer " + id + " buying ticket to "+movies[movieID]);
				
				// Getting a ticket at the box office
				
				// Wait for a box office to be available
				boxOfficeAvailable.acquire();
				
				// Figure out which box office it was
				int boxID;
				if(!boxOffice[0].tryAcquire())
				{
					boxOffice[1].acquire();
					boxID = 1;
				}
				else
					boxID = 0;
				
				// Make the request for the ticket
				customerTicketRequestID[boxID] = id;
				customerTicketRequestMovie[boxID] = movieID;
				customerTicketRequest[boxID].release();
				
				// Wait for the box office agent to finish selling the ticket
				ticketSale[boxID].acquire();
				
				// Check if the movie ID changed to -1, this means it was sold out
				if(customerTicketRequestMovie[boxID] == -1)
				{
					System.out.println("Customer " + id + " left");
					return;
				}
				
				// Going through the ticket taker
				System.out.println("Customer " + id + " in line to see ticket taker");
				
				// Wait for ticket taker to be available
				ticketTakerAvailable.acquire();
				
				// Identify self and give ticket to the taker
				ticketTakerCustomerID = id;
				ticketTakerCustomer.release();
				
				// Wait for ticket taker to take and tear ticket
				ticketTaken.acquire();
				
				// 50% chance to buy a snack
				if(random.nextBoolean())
				{
					// Pick random item ID to buy
					// ID corresponds to purchases array
					int purchaseID = random.nextInt(3);
					System.out.println("Customer " + id + " in line to buy " + purchases[purchaseID]);
					
					// Wait for concession worker to be available
					concessionWorkerAvailable.acquire();
					
					// Make order
					customerOrder = purchaseID;
					customerOrdering = id;
					customerOrdered.release();
					
					// Wait to receive order
					orderFulfilled.acquire();
					System.out.println("Customer " + id + " receives " + purchases[purchaseID]);
				}
				
				// Done, now enter theater.
				System.out.println("Customer " + id + " enters theater to see " + movies[movieID]);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/*
	 * Thread simulating BoxOfficeAgent
	 */
	public static class BoxOfficeAgent implements Runnable 
	{
		int id;
		public BoxOfficeAgent(int id)
		{
			this.id = id;
		}
		@Override
		public void run()
		{
			try
			{
				System.out.println("Box office agent " + id + " created");
				while(true)
				{
					// Accept next customer
					boxOffice[id].release();
					boxOfficeAvailable.release();
					
					// Wait for customer to approach
					customerTicketRequest[id].acquire();
					
					// Get customer ticket request
					int customerID = customerTicketRequestID[id];
					int movieID = customerTicketRequestMovie[id];
					System.out.println("Box office agent " + id + " serving customer " + customerID);
					
					// Check if seats available
					if(seats[movieID] > 0)
					{
						// Decrement seat count and sell ticket
						Thread.sleep(1500);
						seats[movieID]--;
						System.out.println("Box office agent " + id + " sold ticket for " + movies[movieID] + " to customer " + customerID);
					}
					else // Tickets sold out
					{
						// Tell customer tickets sold out by setting ID to -1
						System.out.println("Box office agent " + id + " told customer " + customerID + " that tickets for " + movies[movieID] + " are sold out");
						customerTicketRequestMovie[id] = -1;
					}
					
					// End transaction, hand over to the customer
					ticketSale[id].release();	
				}
			} catch (InterruptedException e) {
//				e.printStackTrace();
			}
			
		}
	}
	
	/*
	 * Thread simulating the ticket taker
	 */
	public static class TicketTaker implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				System.out.println("Ticket taker created");
				while(true)
				{
					// Accept next customer
					ticketTakerAvailable.release();
					
					// Wait for next customer
					ticketTakerCustomer.acquire();
					
					// Take ticket
					Thread.sleep(250);
					System.out.println("Ticket taken from customer " + ticketTakerCustomerID);
					
					// Let customer through
					ticketTaken.release();
				}
			} catch (InterruptedException e) {
//				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Thread simulating the concession stand worker
	 */
	public static class ConcessionStandWorker implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				System.out.println("Concession stand worker created");
				while(true)
				{
					// Accept next customer
					concessionWorkerAvailable.release();
					
					// Wait for customer order
					customerOrdered.acquire();
					
					// Make order
					System.out.println("Order for " + purchases[customerOrder] + " taken from customer " + customerOrdering);
					Thread.sleep(3000);
					
					// Give order to customer
					System.out.println(purchases[customerOrder] + " given to customer " + customerOrdering);
					orderFulfilled.release();
				}
			} catch (InterruptedException e) {
//				e.printStackTrace();
			}
		}
	}
}

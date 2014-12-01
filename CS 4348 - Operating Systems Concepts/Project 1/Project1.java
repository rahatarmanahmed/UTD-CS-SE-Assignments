import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class Project1
{
	public static void main(String[] args)
	{
		if(args.length < 2)
		{
			System.err.println("Not enough arguments (requires input program and timer length)");
			System.exit(1);
		}
		// Get the program arguments
		String inputProgram = args[0];
		int timeout = Integer.parseInt(args[1]);

		Runtime runtime = Runtime.getRuntime();
		// Call the Memory process with the input program argument
		try {
			Process memory = runtime.exec("java Memory "+inputProgram);
			final InputStream error = memory.getErrorStream();
			new Thread(new Runnable(){
			    public void run(){
			        byte[] buffer = new byte[8192];
			        int len = -1;
			        try {
						while((len = error.read(buffer)) > 0){
						    System.err.write(buffer, 0, len);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }
			}).start();
			
			Scanner memoryIn = new Scanner(memory.getInputStream());
			PrintWriter memoryOut = new PrintWriter(memory.getOutputStream());
			CPU cpu = new CPU(memoryIn, memoryOut, timeout);
			cpu.run();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Unable to create new process.");
			System.exit(1);
		}
//		while(true) {}
	}
	
	private static void log(Object... str)
	{
//		System.err.print("\t"+str[0]);
//		for(int k=1;k<str.length;k++)
//			System.err.print(" "+str[k]);
//		System.err.println();
	}
	
	/**
	 * Class representing the CPU
	 */
	private static class CPU
	{
		private int PC, SP, IR, AC, X, Y, timer, timeout;
		private boolean kernelMode;
		private Scanner memoryIn;
		private PrintWriter memoryOut;
		/**
		 * Constructor for CPU
		 * @param memoryIn Scanner to read output from memory
		 * @param memoryOut PrintWriter to send commands to memory
		 * @param timeout
		 */
		public CPU(Scanner memoryIn, PrintWriter memoryOut, int timeout) {
			this.memoryIn = memoryIn;
			this.memoryOut = memoryOut;
			this.timeout = timeout;
			kernelMode = false;
			PC = IR = AC = X = Y = timer = 0;
			SP = 1000;
		}

		/**
		 * Reads from memory
		 * @param address address to read from
		 * @return value at address
		 */
		private int readMemory(int address)
		{
			if(address >= 1000 && !kernelMode)
			{
				System.err.println("ERROR: Accessed system memory in user mode.");
				System.exit(-1);
			}
			memoryOut.println("r"+address);
			memoryOut.flush();
			return Integer.parseInt(memoryIn.nextLine());
		}
		
		/**
		 * Writes to memory
		 * @param address address to write to
		 * @param data value to write
		 */
		private void writeMemory(int address, int data)
		{
			memoryOut.printf("w%d,%d\n", address, data);
			memoryOut.flush();
		}
		
		/**
		 * Sends a command to end the memory process
		 */
		private void endMemoryProcess()
		{
			memoryOut.println("e");
			memoryOut.flush();
		}
		
		/**
		 * Runs the fetch-execution cycles until the end command is reached
		 */
		public void run()
		{
			boolean running = true;
			while(running)
			{
				fetch();
				running = execute();
				timer++;
				// Interrupt to 1000 every timeout instructions
				if(timer >= timeout)
				{
					if(!kernelMode) // Disable nested interrupts
					{
						timer = 0;
						kernelMode();
						PC = 1000;
					}
					
				}
			}
		}
		
		/**
		 * Fetches instruction from PC then increments PC
		 */
		private void fetch()
		{
			IR = readMemory(PC++);
		}
		
		/**
		 * Pushes data to stack
		 * @param data data to push
		 */
		private void push(int data)
		{
			writeMemory(--SP, data);
		}
		
		/**
		 * Pops from stack
		 * @return value popped from stack 
		 */
		private int pop()
		{
			return readMemory(SP++);
		}
		
		/**
		 * Enters kernel mode, switches stacks and saves user mode SP and PC. 
		 * NOTE: Does not jump. Change PC yourself
		 */
		private void kernelMode()
		{
			log("Entering kernel mode.");
			kernelMode = true;
			int tempSP = SP; 
			SP = 2000;
			push(tempSP);
			push(PC);
			push(IR);
			push(AC);
			push(X);
			push(Y);
		}
		
		/**
		 * Handles instructions
		 * @return true if still running, false to stop running
		 */
		private boolean execute()
		{
			// TODO: run instructions in IR
			switch(IR)
			{
				case 1: // Load value: Load value into AC
					fetch();
					log("Loading "+IR+" into AC");
					AC = IR;
					break;
				case 2: // Load addr: Load value at address into AC
					fetch();
					AC = readMemory(IR);
					log("Loading from address "+IR+" into AC: "+AC);
					break;
				case 3: // LoadInd addr: Load value from address at given address into AC
					fetch();
					AC = readMemory(readMemory(IR));
					log("Loading indirectly from address"+IR+" into AC: "+AC);
					break;
				case 4: // LoadInxX addr: Load value at (given address + X) into AC
					fetch();
					AC = readMemory(IR + X);
					log("LoadInxX", IR, X, "->", AC);
					
					break;
				case 5: // LoadInxY addr: Load value at (given address + Y) into AC
					fetch();
					AC = readMemory(IR + Y);
					log("LoadInxY", IR, Y, "->", AC);
					break;
				case 6: // LoadSpX: Load from (SP+X) into AC
					AC = readMemory(SP+X);
					log("LoadSpX", SP, X, "->", AC);
					break;
				case 7: // Store addr: Store AC to address
					fetch();
					writeMemory(IR, AC);
					log("Store",IR,AC);
					break;
				case 8: // Get: Get random int 1-100 into AC
					AC = (int) (Math.random()*100+1);
					log("Get",AC);
					break;
				case 9: // Put port: If port=1, write AC to screen as int, if port=2, write AC to screen as char
					fetch();
					if(IR == 1)
					{
						System.out.print(AC);
						log("Put","int",AC);
					}
						
					else if(IR == 2)
					{
						System.out.print((char)AC);
						log("Put","char",(char)AC);
					}
						
					break;
				case 10: // AddX: Add X to AC
					AC += X; break;
				case 11: // AddY: Add Y to AC
					AC += Y; break;
				case 12: // SubX: Sub X to AC
					AC -= X; break;
				case 13: // SubY: Sub Y to AC
					AC -= Y; break;
				case 14: // CopyToX: Copy value in AC to X
					X = AC; break;
				case 15: // CopyFromX: Copy value in X to AC
					AC = X; break;
				case 16: // CopyToY: Copy AC to Y
					Y = AC; break;
				case 17: // CopyFromY: Copy Y to AC
					AC = Y; break;
				case 18: // CopyToSp: Copy AC to SP
					SP = AC; break;
				case 19: // CopyFromSp: Copy SP to AC
					AC = SP; break;
				case 20: // Jump addr: Jump to address
					fetch();
					PC = IR;
					break;
				case 21: // JumpIfEqual addr: Jump only if AC is zero
					fetch();
					if(AC == 0)
						PC = IR;
					break;
				case 22: // JumpIfNotEqual addr: Jump only if AC is not zero
					fetch();
					if(AC != 0)
						PC = IR;
					break;
				case 23: // Call addr: Push return addr to stack, jump
					fetch();
					push(PC);
					PC = IR;
					break;
				case 24: // Ret: Pop return addr, jump back
					PC = pop();
					break;
				case 25: // IncX: Increment X
					X++; break;
				case 26: // DecX: Decrement X
					X--; break;
				case 27: // Push: Push AC onto stack
					push(AC);
					log("Pushing AC",AC);
					break;
				case 28: // Pop: Pop from stack onto AC
					AC = pop();
					log("Popping AC",AC);
					break;
				case 29: // Int: Set sys mode, switch stack, push SP/PC, set new SP/PC
					// Disable interrupts during interrupt processing
					if(!kernelMode)
					{
						kernelMode();
						PC = 1500;
					}
					break;
				case 30: // IRet: Restore registers, set user mode
					log("Exiting kernel mode.");
					Y = pop();
					X = pop();
					AC = pop();
					IR = pop();
					PC = pop();
					SP = pop();
					kernelMode = false;
					break;
				case 50: // End: End execution
					endMemoryProcess();
					return false;
				default: // Invalid instruction
					System.err.println("ERROR: Invalid instruction.");
					endMemoryProcess();
					return false;
			}
			return true;
		}
		
	}
}
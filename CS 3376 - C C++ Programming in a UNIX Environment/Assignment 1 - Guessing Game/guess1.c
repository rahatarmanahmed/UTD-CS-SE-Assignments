#include <stdio.h>

char readChar()
{
	char c;
	do
	{
		c = getchar();
	} while(c == '\n');
	return c;
}

int main()
{
	char replay;
	int numGuesses = 0, numGames = 0;
	int lowerBound = 1, upperBound = 100, guess = 0;
	do
	{
		numGames++;
		printf("Guess a number between 1 and 100 (both inclusive) and get ready to answer a few questions\n");
		int lowerBound = 1, upperBound = 100, guess = 0;
		while(lowerBound < upperBound)
		{
			numGuesses++;
			guess = (upperBound - lowerBound)/2 + lowerBound;
			printf("How about %d (<,=,>)? ",guess);
			char response = readChar();
			// char newLine; // for some reason scanf grabs a newline as a char
			// scanf("%c", &newLine); //this will get rid of it
			switch(response)
			{
				case '=':
					lowerBound = upperBound = guess;			
					break;
				case '<':
					upperBound = guess;
					break;
				case '>':
					lowerBound = guess;
					break;
				default:
					printf("That is not a valid response.\n");
			}

			//if the bounds go out of range just keep them in range.
			if(upperBound < 1)
				upperBound == 1;
			else if(upperBound > 100)
				upperBound == 100;
			if(lowerBound < 1)
				lowerBound == 1;
			else if(lowerBound > 100)
				lowerBound == 100;

		}
		printf("Your number is %d!\nThat was a good game.",guess);	
		printf("Do you want to play again (y/n) ? ");
		replay = readChar();
	} while(replay != 'n');
	printf("The average number of guesses was %f\n",numGuesses/(float)numGames);
	return 0;
}
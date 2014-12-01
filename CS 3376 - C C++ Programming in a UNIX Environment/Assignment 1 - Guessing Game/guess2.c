#include <stdio.h>
#include <stdlib.h>

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

	do
	{
		numGames++;
		printf("I have thought about a number between 1 and 100 (both inclusive).\nYou can guess my number now..\n");
		srand(time(NULL));
		int number = (rand() % 100) + 1;
		int guess = -1;
		while(guess != number)
		{
			numGuesses++;
			scanf("%d",&guess);
//		char newLine; // for some reason scanf grabs a newline as a char
//		scanf("%c", &newLine); //this will get rid of it
			if(guess < number)
				printf("Too low.\n");
			if(guess > number)
				printf("Too high.\n");
		}
	printf("Yes it was %d - Good job!\nDo you want to play again (y/n) ? ",guess);	
	replay = readChar();

	} while(replay != 'n');
	printf("The average number of guesses was %f\n",numGuesses/(float)numGames);

	return 0;
}
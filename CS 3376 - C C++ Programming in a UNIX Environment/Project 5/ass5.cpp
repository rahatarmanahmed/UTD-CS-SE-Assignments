#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <unistd.h>
#include "Tokenizer.h"

const int MAX = 21;
int pipes[MAX][2];

char operations[MAX];

using namespace std;

void printPipes()
{
	for(int k=0;k<MAX;k++)
		cout << pipes[k][0] << " " << pipes[k][1] << endl;
}

int main()
{
	int numOps = 0;
	ifstream inputFile("data1.txt");
	string line;		
	getline(inputFile, line);
	Tokenizer tokenizer(line);
	string token;
	while((token = tokenizer.next()) != "")
	{
		if(token == "+" || token == "-" || token == "*" || token == "/")
		{
			operations[numOps++] = token[0];
		}
	}

	for(int k=0;k<numOps+1;k++)
		pipe(pipes[k]);

	for(int k=0;k<numOps;k++)
	{
		if(fork() == 0)
		{
			close(0);
			dup(pipes[2*k][0]);
			close(3);
			dup(pipes[2*k+1][0]);
			close(1);
			dup(pipes[2*k+2][1]);
			switch(operations[k])
			{
				case '+':	execl("sum", "sum", "-v", NULL);
				case '-':	execl("subtract", "subtract", "-v", NULL);
				case '*':	execl("multiply", "multiply", "-v", NULL);
				case '/':	execl("divide", "divide", "-v", NULL);
			}
			cerr << "not good";
		}
	}

	getline(inputFile, line);
	istringstream convert(line);
	int a,b,c;
	convert >> a >> b >> c;
	write(pipes[0][1], &a, sizeof(int));
	write(pipes[1][1], &b, sizeof(int));
	int k = 3;
	while(convert.good())
	{
		int c;
		convert >> c;
		write(pipes[k+1][1], &c, sizeof(int));
		k += 2;
	}

	int result;
	while(read(pipes[2*numOps][0], (char *)&result, sizeof(int)))
		cout << result << endl;
	return 0;
}

#include <iostream>
#include <string>
#include <sstream>
#include <iomanip>

using namespace std;

// Display message then read in an int
// NOTE: reads an entire line of input, discards any extra data after first int
int promptInt(string message)
{
	cout << message;
	string line;
	getline(cin, line);
	stringstream stream(line);
	int response;
	stream >> response;
	return response;
}

// Display message then read in an double
// NOTE: reads an entire line of input, discards any extra data after first double
double promptDouble(string message)
{
	cout << message;
	string line;
	getline(cin, line);
	stringstream stream(line);
	double response;
	stream >> response;
	return response;
}

int main()
{
	double principal = promptDouble("Enter principal loan amount: ");
	double interestRate = promptDouble("Enter interest rate(as %): ") / 100 / 12;
	double monthlyPayment = promptDouble("Enter monthly payment: ");
	cout << endl;
	int month = 1;
	cout << left << fixed << setprecision(2) << setw(6) << "Month" << setw(20) << "Monthly Interest"
		 << setw(20) << "Payment" << setw(20) << "Remaining Principal" << endl;
	while(principal > 0)
	{
		double monthlyInterest = principal * interestRate;
		double payment = monthlyPayment - monthlyInterest;
		if(principal > payment)
			principal -= payment;
		else
		{
			payment = principal + monthlyInterest;
			principal = 0;
		}

		cout << setw(6) << month << '$' << setw(19) << monthlyInterest
			 << '$' << setw(19) << payment
			 << '$' << setw(19) << principal << endl;
		month++;
	}

	return 0;

}
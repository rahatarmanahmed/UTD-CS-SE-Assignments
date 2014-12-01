#include <iostream>

#include "SeatSelection.h"

using namespace std;

void prompt(bool& isPremium, int& numTickets)
{
	cout << "Premium(1) or Regular (2): ";
	int temp;
	cin >> temp;
	isPremium = (temp == 1) ? true : false;
	cout << "# of tickets: ";
	cin >> numTickets;
	return;
}

int main()
{
	SeatSelection premium(1, 5);
	SeatSelection regular(6,15);
	bool isPremium;
	int numTickets;

	while(true)
	{
		prompt(isPremium, numTickets);
		string result;
		if(isPremium)
			result = premium.tryToReserveSeats(numTickets);
		else
			result = regular.tryToReserveSeats(numTickets);
		if(result == "")
		{
			cout << "Sorry, not enough seats. Please come again!" << endl;
			break;
		}
		cout << "Your seats are: " << result << endl;
		// premium.printSeats();
		// cout << endl << premium.freeSeatsInRow(1) << endl;
		
	}
}
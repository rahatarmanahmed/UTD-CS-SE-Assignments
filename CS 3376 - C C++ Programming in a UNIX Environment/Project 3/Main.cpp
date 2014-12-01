#include "List.h"
#include "Menu.h"
#include "MenuItem.h"
#include "Order.h"
#include "Payment.h"
#include "Table.h"
#include "Waiter.h"
#include <fstream>
#include <sstream>
#include <iostream>
#include <iomanip>

using namespace std;

Table *tables;
int numTables;

Waiter **waiters;
int numWaiters;

Menu menu;

void loadConfig()
{
	ifstream config("config.txt");
	string line;
	getline(config, line); //skip the "Table: ..."" line
	getline(config, line);

	//Use a List to create the Table array
	List<Table> tableList;
	int tableNum, maxSeats;
	while(line != "")
	{
		istringstream convert(line);
		convert >> tableNum >> maxSeats;
		tableList << Table(tableNum, maxSeats);
		getline(config, line);
	}
	
	tables = tableList.getArray();
	numTables = tableList.getLength();
	

	// // Time for the waiters


	getline(config, line); // Skip the "Waiters: ..." line
	getline(config, line);

	// List<Waiter> waiterList;
	List<string> nameList;
	List<string> tableIdList;

	while(line != "")
	{
		istringstream convert(line);
		string name, tableIds;
		convert >> name >> tableIds;
		nameList << name;
		tableIdList << tableIds;
		getline(config, line);
	}

	numWaiters = nameList.getLength();

	waiters = new Waiter*[numWaiters];
	for(int k=0;k<numWaiters;k++)
	{
		waiters[k] = new Waiter(nameList[k], tableIdList[k], tables);
	}

	getline(config, line); // Skip the "Menu: ..." line
	getline(config, line);
	
	// Menu menu;

	while(line != "")
	{
		string code, name;
		double price;
		istringstream convert(line);
		convert >> code >> name >> price;
		menu.addItem(MenuItem(code, name, price));
		getline(config, line);
	}
}

Table* findTable(int tableId)
{
	for(int k = 0; k < numTables; k++)
	{
		if(tables[k].getTableId() == tableId)
			return tables+k;
	}
	return NULL;
}

void destroy()
{
	delete [] tables;
	for(int k=0;k<numWaiters;k++)
		delete waiters[k];
	delete [] waiters;
}

int main()
{
	loadConfig();

	ifstream activity("activity.txt");
	string line;
	getline(activity, line);
	while(line != "")
	{
		istringstream convert(line);
		char trash, action;
		int tableId;
		convert >> trash >> tableId >> action;
		Table* table = findTable(tableId);

		switch(action)
		{
			case 'P':
				int numPeople;
				convert >> numPeople;
				if(table->getStatus() != IDLE)
				{
					cout << "ERROR: Table " << tableId << " is not available.\n";
					break;
				}
				if(table->getWaiter() == NULL)
				{
					cout << "ERROR: Table " << tableId << " does not have a waiter assigned to it.\n";
					break;
				}
				table->partySeated(numPeople);
				cout << "Seating " << numPeople << " at Table " << tableId << endl;
				break;

			case 'O':
				{
					string orderStr;
					getline(convert, orderStr); // gets the rest of the line
					Order *order = new Order(orderStr, &menu);
					if(table->getStatus() != SEATED)
					{
						cout << "Error: Table" << tableId << " is not seated, so it cannot receive order " << orderStr << endl;
						break;
					}
					table->partyOrdered(order);
					cout << "Party at Table " << tableId << " ordered " << orderStr << endl;
				}
				break;
			case 'S':
					if(table->getStatus() != ORDERED)
					{
						cout << "Error: Table " << tableId << " cannot be served, because it does not have an order." << endl;
						break;
					}
					table->partyServed();
					cout << "Party at Table " << tableId << " has been served." << endl;
				break;
			case 'C':
					if(table->getStatus() != SERVED)
					{
						cout << "Error: Table " << tableId << " cannot be checkout, because it has not been served yet." << endl;
						break;
					}
					// Get order before checkout because it will be gone
					Order *order = table->getOrder();
					table->partyCheckout();
					cout << "Party at Table " << tableId << " has checked out." << endl;
					cout << "\tCHECKOUT RECEIPT" << endl;
					cout << "\t\tTable #: " << tableId << endl;
					cout << "\t\t# of customers in party: " << table->getNumPeople() << endl;
					double total = 0.0;
					for(int k = 0; k < order->getNumItems(); k++)
					{
						string name = order->getItem(k)->getName();
						double price = order->getItem(k)->getPrice();
						total += price;
						cout << "\t\t\t" << name << "\t$" << fixed << setprecision(2) << price << endl;
					}
					cout << "\t\tTotal: $" << fixed << setprecision(2) << total << endl;
					delete order; // We won't need this anymore.
				break;
		}


		getline(activity, line);
	}

	destroy();
	return 0;
}

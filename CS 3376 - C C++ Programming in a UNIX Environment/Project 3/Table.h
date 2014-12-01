#ifndef TABLE_H
#define TABLE_H
#include "Order.h"
#include "Waiter.h"

enum TableStatus { IDLE, SEATED, ORDERED, SERVED };

class Waiter; // to take care of circular reference.

class Table 
{
private:
	int tableId;		// table number
	// This used to be const but i can't use the assignment operator if it's const, 
	int maxSeats;	// table seat capacity
	TableStatus status;	// current status, you can use assign like
				// status = IDLE;
	int numPeople;		// number of people in current party
	Order *order;		// current party's order
	Waiter *waiter;		// pointer to waiter for this table

public:
	Table(int tblid =0, int mseats = 0);	// initialization, IDLE
	~Table();
	// Table(const Table &other);
	// Table& operator= (const Table &other);
	void assignWaiter(Waiter *person); 	// initially no waiter
	void partySeated(int npeople);		// process IDLE --> SEATED
	void partyOrdered(Order *order);	// process SEATED --> ORDERED
	void partyServed(void);			// process ORDERED --> SERVED
	void partyCheckout(void);		// process SERVED --> IDLE

	int getTableId();
	int getMaxSeats();
	int getNumPeople();
	TableStatus getStatus();
	Waiter* getWaiter();
	Order* getOrder();
};
#endif

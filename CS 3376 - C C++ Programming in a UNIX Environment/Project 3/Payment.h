#ifndef PAYMENT_H
#define PAYMENT_H
#include "Order.h"
#include "Menu.h"
#include "Waiter.h"

class Payment 
{
private:
	int tableId; 	// table number
	int numPeople;	// number of people in the party
	Order *orderp; 	// order information
	double total; 	// total bill
	Waiter *waiterp;	// pointer to waiter

public:
	// Payment();
	Payment(int tblId, int npersons, Order *order, double total, Waiter *waiter);
};
#endif

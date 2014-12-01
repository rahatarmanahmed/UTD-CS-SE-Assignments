#include "Payment.h"

// Payment::Payment()
// {

// }

Payment::Payment(int tblId, int npersons, Order *order, double total, Waiter *waiter) : tableId(tblId), numPeople(npersons), orderp(order), total(total), waiterp(waiter)
{
	
}
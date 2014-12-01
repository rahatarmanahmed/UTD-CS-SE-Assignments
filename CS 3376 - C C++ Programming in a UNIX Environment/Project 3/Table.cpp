#include "Table.h"

Table::Table(int tblid, int mseats) : tableId(tblid), maxSeats(mseats), numPeople(0), status(IDLE), waiter(NULL), order(NULL)
{

}

Table::~Table()
{
	if(order != NULL)
		delete order;
}

// Table::Table(const Table &other) : tableId(other.tableId), maxSeats(other.maxSeats), numPeople(other.numPeople), status(other.status), order(other.order), waiter(other.waiter)
// {

// }

// Table& Table::operator=(const Table &other)
// {
// 	tableId = other.tableId;
// 	maxSeats = other.maxSeats;
// 	status = other.status;
// 	numPeople = other.numPeople;
// 	order = other.order;
// 	waiter = other.waiter;

// 	return *this;
// }

void Table::assignWaiter(Waiter *person)
{
	waiter = person;
}

void Table::partySeated(int npeople)
{
	int numPeople = npeople;
	status = SEATED;
}

void Table::partyOrdered(Order *order)
{
	this->order = order;
	status = ORDERED;
}

void Table::partyServed()
{
	status = SERVED;
}

void Table::partyCheckout()
{
	status = IDLE;
	// If table checked out, main function will take care of delete
	// But if it never gets checked out, the destructor can compare
	// it to NULL to know it wasn't checked out, and order was
	// never deleted.
	order = NULL;
}

int Table::getTableId()
{
	return tableId;
}

int Table::getMaxSeats()
{
	return maxSeats;
}

int Table::getNumPeople()
{
	return numPeople;
}

TableStatus Table::getStatus()
{
	return status;
}

Waiter* Table::getWaiter()
{
	return waiter;
}

Order* Table::getOrder()
{
	return order;
}
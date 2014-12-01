#ifndef WAITER_H
#define WAITER_H
#include <string>
#include "Table.h"

class Table; // to take care of circular reference.

class Waiter
{
private:
	string name;	// waiter's name
	int numTables;	// number of tables waiter is in-charge for
	Table **tables;	// waiter's table list

public:
	~Waiter();
	Waiter(string mname = "", string tableList = "", Table *table = NULL); 
	// waiter's name, his table list as a string, table array pointer
	string getName();

};

#endif

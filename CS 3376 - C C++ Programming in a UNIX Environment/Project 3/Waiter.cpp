#include "List.h"
#include "Waiter.h"
#include "Tokenizer.h"
#include <string>
#include <sstream>
#include <iostream>

using namespace std;

Table* findTable(string tableCode, Table *table)
{
	istringstream stream(tableCode);
	int tableId;
	stream >> tableId;
	while(table->getTableId() != tableId)
	{
		table++;
	}
	return table;
}

Waiter::Waiter(string mname, string tableList, Table *table) : name(mname), numTables(0), tables(NULL)
{
	if(name == "" && tableList == "")
	{
		return; //the List of waiters will initialize empty waiters when expanding
	}
	Tokenizer tokenizedList(tableList,",");
	List<string> list;
	string token = tokenizedList.next();
	while(token != "")
	{
		list.add(token);
		token = tokenizedList.next();
	}
	numTables = list.getLength();
	tables = new Table*[numTables];

	for(int k = 0; k < numTables; k++)
	{
		tables[k] = findTable(list.get(k), table);
		tables[k]->assignWaiter(this);
	}
	// // Count how many tables are in the list
	// Tokenizer *list = new Tokenizer(tableList);
	// numTables = 0;
	// while(list->next() != "")
	// {
	// 	numTables++;
	// }
	// delete list;

	// // Create the list of tables for the waiter
	// tables = new Table*[numTables];

	// // Find and add all the tables
	// list = new Tokenizer(tableList);
	// string tableCode;
	// for(int k = 0; k < numTables; k++)
	// {
	// 	tableCode = list->next();
	// 	tables[k] = findTable(tableCode, table);
	// }
	// delete list;
}

Waiter::~Waiter()
{
	if(tables != NULL)
		delete [] tables;
}

string Waiter::getName()
{
	return name;
}
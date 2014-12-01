#include "List.h"
#include "Order.h"
#include <string>
#include "Tokenizer.h"
using namespace std;

Order::Order(int count)
{
	itemsp = new MenuItem*[count];
	maxItems = count;
	numItems = 0;
}

Order::~Order()
{
	delete [] itemsp;
}

int Order::addItem(MenuItem *itemp)
{
	itemsp[numItems++] = itemp;
}

MenuItem* Order::getItem(const int index)
{
	return itemsp[index];
}

int Order::getNumItems()
{
	return numItems;
}

Order::Order(string orderList, Menu *menu)
{
	Tokenizer tokenizedList(orderList);
	List<string> list;
	string token = tokenizedList.next();
	while(token != "")
	{
		list.add(token);
		token = tokenizedList.next();
	}
	itemsp = new MenuItem*[list.getLength()];
	maxItems = numItems = list.getLength();
	for(int k = 0; k < numItems; k++)
	{
		itemsp[k] = menu->findItem(list.get(k));
	}
	// // Go through the list once to figure out the size
	// Tokenizer *list = new Tokenizer(orderList);
	// int count = 0;
	// while(list->next() != "")
	// {
	// 	count++;
	// }
	// delete list;

	// // Create the right sized list
	// itemsp = new MenuItem*[count];
	// maxItems = numItems = count;

	
	// // Now go through the orderList again to do stuff
	// list = new Tokenizer(orderList);
	// string itemCode = list->next();
	// while(itemCode != "")
	// {
	// 	MenuItem* item = menu->findItem(itemCode);
	// }
	// delete list;
}
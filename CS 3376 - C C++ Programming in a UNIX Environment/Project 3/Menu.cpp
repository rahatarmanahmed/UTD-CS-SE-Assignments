#include "Menu.h"
#include <string>

using namespace std;

Menu::Menu(int items)
{
	menup = new MenuItem[items];
	maxItems = items;
	numItems = 0;
}

Menu::~Menu()
{
	delete [] menup;
	menup = NULL;
}

void Menu::addItem(MenuItem item)
{
	menup[numItems++] = item;
}

MenuItem* Menu::findItem(const string code)
{
	for(int k = 0; k < numItems; k++)
	{
		if(menup[k].getCode() == code)
			return menup+k;
	}
	return NULL;
}	
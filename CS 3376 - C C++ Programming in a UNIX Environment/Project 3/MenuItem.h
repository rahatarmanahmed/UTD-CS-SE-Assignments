#ifndef MENUITEM_H
#define MENUITEM_H
#include <iostream>
#include <string>

using namespace std;

class MenuItem
{
private:
	string code; // See sample codes in config.txt
	string name; // Full name of the entry
	double price; // price of the item

public:
	MenuItem(string mcode = "", string mname= "", double mprice = 0);
	// MenuItem& operator=(const MenuItem &other);

	string getCode();
	string getName();
	double getPrice();
};
#endif

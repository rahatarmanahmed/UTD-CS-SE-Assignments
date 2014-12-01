#include "MenuItem.h"
#include <string>

using namespace std;

MenuItem::MenuItem(string mcode, string mname, double mprice) : code(mcode), name(mname), price(mprice)
{

}

// MenuItem& MenuItem::operator=(const MenuItem &other)
// {
// 	code = other.code;
// 	name = other.name;
// 	price = other.price;
// }

string MenuItem::getCode()
{
	return code;
}
string MenuItem::getName()
{
	return name;
}
double MenuItem::getPrice()
{
	return price;
}
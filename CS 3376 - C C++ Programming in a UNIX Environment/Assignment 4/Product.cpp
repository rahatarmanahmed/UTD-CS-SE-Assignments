#include <iostream>
#include <string>

#include "Product.h"

using namespace std;

Product::Product(int p_code, string p_name, bool p_by_weight, double p_price, double p_inv)
{
	plu_code = p_code;
	name = p_name;
	by_weight = p_by_weight;
	price = p_price;
	inventory = p_inv;
}

bool Product::sold_by_weight(void)
{
	return by_weight;
}

double Product::compute_cost_by_units(int units)
{
	if (!by_weight) {
		inventory -= units;
		return units * price;
	} else
		return 0;
}

double Product::compute_cost_by_weight(double weight)
{
	if (by_weight) {
		inventory -= weight;
		return weight * price;
	} else
		return 0;
}

string Product::get_name(void) {
	return name;
}

int Product::get_plu_code(void) {
	return plu_code;
}

double Product::get_price(void) {
	return price;
}

double Product::get_inventory(void) {
	return inventory;
}


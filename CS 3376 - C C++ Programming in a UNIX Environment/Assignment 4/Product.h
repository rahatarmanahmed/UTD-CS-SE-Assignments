#ifndef GS_PRODUCT
#define GS_PRODUCT

#include <iostream>
#include <string>

using namespace std;

class Product
{
private:
		int plu_code;
		string name;
		double price;
		bool by_weight;
		double inventory;
public:
		Product(int p_code = 0, string p_name = "", bool p_by_weight = true, double p_price = 0.0, double p_inv = 0.0);
		bool sold_by_weight(void);
		double compute_cost_by_units(int units);
		double compute_cost_by_weight(double weight);
		string get_name(void);
		int get_plu_code(void);
		double get_price(void);
		double get_inventory(void);
};

#endif


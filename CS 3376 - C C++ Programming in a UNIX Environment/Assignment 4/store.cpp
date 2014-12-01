#include <iostream>
#include <fstream>
#include <sstream>
#include <iomanip>
#include <cstdlib>

#include "Product.h"
#include "LookupTable.h"
#include "Tokenizer.h"

using namespace std;

// #define MAX 100
// Product products[MAX];
// int numProducts = 0;
LookupTable<Product*> table;

#define DISCOUNT_RATE 0.05


void checkout(void)
{
	int units;
	double weight, total = 0;
	int plu, i;
	Product product;

	do {
		cout << "\nEnter PLU code or 0 to complete checkout: ";
		cin >> plu;

		if (!plu)
			break;

		if (table[plu] != NULL) {
			if (table[plu]->sold_by_weight())
			{
				cout << "\nEnter the weight for " << table[plu]->get_name() << ": ";
				cin >> weight;
				total += table[plu]->compute_cost_by_weight(weight);
			} else {
				cout << "\nEnter # of units for " << table[plu]->get_name() << ": ";
				cin >> units;
				total += table[plu]->compute_cost_by_units(units);
			}
			cout << fixed << setprecision(2) << "Total so far: $ " << total << endl;
		}
		else 
			cout << "Invalid selection. Try again.\n";

	} while (1); 
	
	if (total > 50) {
          cout << "   Total: $ " << total << endl;
          cout << "Discount: $ " << total * DISCOUNT_RATE << endl;
          total -= total * DISCOUNT_RATE;
     }
     cout << "Amount due: $ " << total << endl;    
}

int main()
{
	char user_response;
	int plu_code, sold_by_weight;
	string name;
	double price, inv;
	Product input;

	ifstream inputFile;

	table = LookupTable<Product*>();
	table.addRange(0, 9999);
	table.addRange(90000, 99999);
	
	string line;
	inputFile.open("inventory.csv");
	if (!inputFile)
		exit(1);

	getline(inputFile, line);
	while(line != "")
	{
		Tokenizer tokenizer(line,",");
		plu_code = atoi(tokenizer.next().c_str());
		name = tokenizer.next();
		sold_by_weight = atoi(tokenizer.next().c_str());
		price = atof(tokenizer.next().c_str());
		inv = atof(tokenizer.next().c_str());
		table[plu_code] = new Product(plu_code, name, (sold_by_weight == 1), price, inv);
		getline(inputFile, line);	
	}
/*	while(inputFile >> plu_code >> name >> sold_by_weight >> price >> inv) {
		table[plu_code] = new Product(plu_code, name, (sold_by_weight == 1), price, inv);
		cout << plu_code << " " << name << endl;
	}
*/

	inputFile.close();
	// cout << numProducts << " products read." << endl;

	do {
		checkout();
		cout << "Press y to checkout the next customer (n to quit): ";
		do 
			cin >> user_response;
		while ((user_response != 'y') && (user_response != 'n'));

	} while (user_response == 'y');

	cout << "Thanks for using grocery checkout software. Bye.\n";

	ofstream outputFile;

	outputFile.open("output.csv");
	for( int i = table.smallestIndex() ; i <= table.biggestIndex() ; i++ ) {
		Product *product = table[i];
		if(product != NULL)
			outputFile << product->get_plu_code() << "," << product->get_name() << ","
				<< product->sold_by_weight() << "," << product->get_price() << "," 
				<< product->get_inventory() << endl;
		delete product;
	}
	outputFile.close();
}


#include <iostream>
#include <fstream>
#include <iomanip>
#include <vector>
#include <algorithm>

#include "Product.h"

using namespace std;

vector<Product> products;

#define DISCOUNT_RATE 0.05

string searchStr;

// bool match_plu(Product p)
// {
// 	return p.get_plu_code() == selection;
// }

// void checkout(void)
// {
// 	int units;
// 	double weight, total = 0;
// 	vector<Product>::iterator ip;

// 	do {
// 		cout << "\nEnter PLU code or 0 to complete checkout: ";
// 		cin >> selection;

// 		if (!selection)
// 			break;

// 		ip = find_if(products.begin(), products.end(), match_plu);

// 		if (ip->get_plu_code() == selection) {
// 			if (ip->sold_by_weight())
// 			{
// 				cout << "\nEnter the weight for " << ip->get_name() << ": ";
// 				cin >> weight;
// 				total += ip->compute_cost_by_weight(weight);
// 			} else {
// 				cout << "\nEnter # of units for " << ip->get_name() << ": ";
// 				cin >> units;
// 				total += ip->compute_cost_by_units(units);
// 			}
// 			cout << fixed << setprecision(2) << "Total so far: $ " << total << endl;
// 		}
// 		else 
// 			cout << "Invalid selection. Try again.\n";

// 	} while (1); 
	
// 	if (total > 50) {
//           cout << "   Total: $ " << total << endl;
//           cout << "Discount: $ " << total * DISCOUNT_RATE << endl;
//           total -= total * DISCOUNT_RATE;
//      }
//      cout << "Amount due: $ " << total << endl;    
// }

bool searchProduct(Product p)
{
	string name = p.get_name();
	return p.get_name().find(searchStr) != string::npos;
}

void search()
{
		cout << "Enter search string: ";
		getline(cin, searchStr);
		if(searchStr == "")
			return;
		int count = count_if(products.begin(), products.end(), searchProduct);
		cout << "There are " << count << " products that match that string." << endl;
}

int main()
{
	char user_response;
	int plu_code, sold_by_weight;
	string name;
	double price, inv;

	ifstream inputFile;
	inputFile.exceptions ( ifstream::eofbit | ifstream::failbit | ifstream::badbit );

	try {
		inputFile.open("inventory.txt");

		while(1) {
			inputFile >> plu_code >> name >> sold_by_weight >> price >> inv;
			products.push_back(Product(plu_code, name, (sold_by_weight == 1), price, inv));
		}
	}
	catch (ifstream::failure e) {
		if (inputFile.eof())
			inputFile.close();
		else {
			cerr << "File open/read error.\n";
			exit(1);
		}
	}

	do {
		// checkout();
		search();
		cout << "Press y to make another search. (n to quit): ";
		do 
			cin >> user_response;
		while ((user_response != 'y') && (user_response != 'n'));

	} while (user_response == 'y');

	cout << "Thanks for using grocery search software. Bye.\n";

	ofstream outputFile;

	outputFile.open("output.txt");
	for( int i = 0 ; i < products.size() ; i++ ) {
		outputFile << products[i].get_plu_code() << " " << products[i].get_name() << " "
			<< products[i].sold_by_weight() << " " << products[i].get_price() << " " 
			<< products[i].get_inventory() << endl;
	}
	outputFile.close();
}
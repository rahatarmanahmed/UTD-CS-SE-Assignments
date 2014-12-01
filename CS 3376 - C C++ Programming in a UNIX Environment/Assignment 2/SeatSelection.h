#ifndef SEATSELECTION_H
#define SEATSELECTION_H

#include <string>

using namespace std;

class SeatSelection
{
private:
	int startingRow;
	int numRows;
	bool *seats; // true means seat is taken
	// Modifies seats array to reflect newly taken seats
	// returns string of seats
	string reserveSeats(int row, int count);
	void setSeatTaken(int row, int col);

public:
	SeatSelection(int startingRow, int numRows);
	~SeatSelection();

	int getStartingRow();
	int getNumRows();
	int freeSeatsInRow(int row);
	bool isSeatTaken(int row, int col);
	void printSeats();
	string getSeatName(int row, int column);
	// Figures out which seats to reserve, or returns null if no seats available.
	string tryToReserveSeats(int count);
};

#endif
#include <iostream>
#include <string>
#include <sstream>
#include "SeatSelection.h"

using namespace std; 

SeatSelection::SeatSelection(int startingRow, int numRows)
{
	this->startingRow = startingRow;
	this->numRows = numRows;
	seats = new bool[numRows*10];
}

SeatSelection::~SeatSelection()
{
	delete [] seats;
}

int SeatSelection::freeSeatsInRow(int row)
{
	if(row < startingRow || row >= startingRow+numRows)
		return -1;
	int freeSeats = 10;
	int col = 1;
	while(isSeatTaken(row,col) && freeSeats > 0)
	{
		freeSeats--;
		col++;
	}
	return freeSeats;
}

int SeatSelection::getStartingRow()
{
	return startingRow;
}

int SeatSelection::getNumRows()
{
	return numRows;
}

string SeatSelection::getSeatName(int row, int column)
{
	if(row < startingRow || row >= startingRow+numRows || column < 1 || column > 10)
	{
		ostringstream error;
		error << "INVALID_SEAT " << row << " " << column;
		return error.str();
	}
	ostringstream output;
	output << row;
	output << (char)('A'+column-1);
	return output.str();
}

string SeatSelection::reserveSeats(int row, int count)
{
	if(freeSeatsInRow(row) < count)
		return "BAD_RESERVATION";
	int col = 1;
	ostringstream output;
	while(count > 0)
	{
		if(!isSeatTaken(row, col))
		{
			setSeatTaken(row, col);
			count--;
			output << getSeatName(row, col) << ' ';
		}
		col++;
	}
	return output.str();
}

/*
	Return empty string if can't reserve.
*/
string SeatSelection::tryToReserveSeats(int count)
{
	// bool seatsReserved = false;
	for(int row = startingRow; row < startingRow + numRows; row++)
	{
		if(this->freeSeatsInRow(row) >= count)
		{
			return this->reserveSeats(row, count);
		}
	}
	ostringstream output;
	for(int row = startingRow; row < startingRow + numRows; row++)
	{
		int freeSeats = this->freeSeatsInRow(row);
		if(count > 0 && freeSeats > 0)
		{
			output << this->reserveSeats(row, freeSeats);
			count -= freeSeats;
		}
	}
	if(count > 0)
		return "";
	return output.str();
}

bool SeatSelection::isSeatTaken(int row, int col)
{
	return seats[(row-1)*10 + col-1];
}

void SeatSelection::setSeatTaken(int row, int col)
{
	seats[(row-1)*10 + col-1] = true;	
}
void SeatSelection::printSeats()
{
	cout << endl;
	for(int row = startingRow; row < startingRow + numRows; row++)
	{
		cout << row << "  ";
		for(int col = 1; col <= 10; col++)
		{
			cout << (isSeatTaken(row,col))?"T ":"F ";
		}
		cout << endl;
	}
}
#ifndef LOOKUPTABLE_H
#define LOOKUPTABLE_H

#include <iostream>
#include <limits>

using namespace std;

#define MAXRANGE 10

template <class T>
class LookupTable
{
private:
   T *aptr[MAXRANGE];
   int rangeStart[MAXRANGE]; // inclusive
   int rangeEnd[MAXRANGE]; // inclusive 
   int numRanges;
   T defaultValue; //needed to return when [] operator does not find valid product

public:
   LookupTable();      
   
   //for each range, following method needs to be called.
   void addRange(int start, int end);	  
   ~LookupTable(); 	      
   T &operator[](int value);
   int smallestIndex()
   {
      int smallest = numeric_limits<int>::max();
      for(int k=0;k<numRanges;k++)
         if(rangeStart[k] < smallest)
            smallest = rangeStart[k];
      return smallest;
   }
   int biggestIndex()
   {
      int biggest = -1;
      for(int k=0;k<numRanges;k++)
         if(rangeEnd[k] > biggest)
            biggest = rangeEnd[k];
      return biggest;
   }
   // find the appropriate range based on value, then
   // use index = value-start into corresponding array
};

template <class T>
LookupTable<T>::LookupTable()
{
   numRanges = 0;
   defaultValue = NULL;
}

template <class T>
void LookupTable<T>::addRange(int start, int end)
{
   rangeStart[numRanges] = start;
   rangeEnd[numRanges] = end;
   aptr[numRanges] = new T[end-start+1];
   for(int k=0;k<end-start+1;k++)
      aptr[numRanges][k] = NULL;
   numRanges++;
}


template <class T>
T &LookupTable<T>::operator[](int value)
{
   // cout << "Index is: " << value << endl;
   for(int k=0;k<numRanges;k++)
      if(rangeStart[k] <= value && value <= rangeEnd[k])
      {
         // cout << "Range index is: " << k << endl;
         return aptr[k][value-rangeStart[k]];
      }
   return defaultValue;
}


template <class T>
LookupTable<T>::~LookupTable()
{
   for(int k=0;k<numRanges;k++)
      delete [] aptr[k];
}

#endif

#include <cmath>

template <class T>
class List
{
	private:
	T *data;
	int length;
	int capacityExponent; // capacity is measured by 2^capacityExponent
	void increaseCapacity()
	{
		int newSize = pow(2,++capacityExponent);
		T *newdata = new T[newSize];
		for(int k = 0; k < getLength(); k++)
		{
			newdata[k] = data[k];
		}
		delete [] data;
		data = newdata;
	}

	public:
	List()
	{
		length = 0;
		capacityExponent = 0;
		int size = pow(2,capacityExponent);
		data = new T[size];
	}

	~List()
	{
		delete [] data;
	}

	int getLength()
	{
		return length;
	}

	int getCapacity()
	{
		return pow(2,capacityExponent);
	}

	void add(const T& value)
	{
		if(getLength()==getCapacity())
			increaseCapacity();
		data[length++] = value;
	}

	T* getArray()
	{
		T *copy = new T[length];
		for(int k = 0; k < getLength(); k++)
		{
			copy[k] = data[k];
		}
		return copy;
	}

	T** getPointerArray()
	{
		T **copy = new T*[length];
		for(int k = 0; k < getLength(); k++)
		{
			copy[k] = new T(data[k]);
		}
		return copy;
	}

	T get(const int index)
	{
		return data[index];
	}

	T operator[](const int index)
	{
		return data[index];
	}

	List<T>& operator<<(const T& value)
	{
		add(value);
		return *this;
	}

};

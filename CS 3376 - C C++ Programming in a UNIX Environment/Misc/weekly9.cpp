class Rectangle
{
private:
	int length, width;
public:
	Rectangle(double, double);
	double getLength();
	double getWidth();
	double getArea();
};

class Square : public Rectangle
{
public:
	Square(double);
	double getSide();
};

Rectangle::Rectangle(double length, double width)
{
	this->length = length;
	this->width = width;
}

double Rectangle::getLength()
{
	return length;
}

double Rectangle::getWidth()
{
	return width;
}

double Rectangle::getArea()
{
	return length*width;
}

Square::Square(double sideLength) : Rectangle(sideLength, sideLength)
{

}

double Square::getSide()
{
	return getLength();
}
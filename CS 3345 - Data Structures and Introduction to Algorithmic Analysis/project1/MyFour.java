public class MyFour<T>
{
	T item1, item2, item3, item4;

	public MyFour(T item1, T item2, T item3, T item4)
	{
		this.item1 = item1;
		this.item2 = item2;
		this.item3 = item3;
		this.item4 = item4;
	}

	public boolean allEqual()
	{
		return item1.equals(item2) && item2.equals(item3) && item3.equals(item4);
	}

	public void shiftLeft()
	{
		T temp = item4;
		item4 = item3;
		item3 = item2;
		item2 = item1;
		item1 = temp;
	}

	public String toString()
	{
		return String.format("(%s, %s, %s, %s)",
			item1.toString(),
			item2.toString(),
			item3.toString(),
			item4.toString());
	}

	public static void main(String[] args)
	{
		MyFour<String> myStringFour = new MyFour<String>("obey", "obey", "obey", "obey");
		System.out.println(myStringFour);
		System.out.println(myStringFour.allEqual());

		MyFour<Integer> myIntFour = new MyFour<Integer>(2, 3, 23, 5);
		System.out.println(myIntFour);
		System.out.println(myIntFour.allEqual());	
		myIntFour.shiftLeft();
		System.out.println(myIntFour);
	}
}
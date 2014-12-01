public class MyComparableFour<T extends Comparable<? super T>>
{
	T item1, item2, item3, item4;

	public MyComparableFour(T item1, T item2, T item3, T item4)
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

	public T max()
	{
		T max = (item1.compareTo(item2) >= 0) ? item1: item2;
		max = (max.compareTo(item3) >= 0) ? max : item3;
		max = (max.compareTo(item4) >= 0) ? max : item4;
		return max;
	}

	public static void main(String[] args)
	{
		MyComparableFour<String> myStringFour = new MyComparableFour<String>("obey", "obey", "obey", "obey");
		System.out.println(myStringFour);
		System.out.println(myStringFour.allEqual());

		MyComparableFour<Integer> myIntFour = new MyComparableFour<Integer>(2, 3, 23, 5);
		System.out.println(myIntFour);
		System.out.println(myIntFour.allEqual());	
		myIntFour.shiftLeft();
		System.out.println(myIntFour);
		System.out.println("Max: " + myIntFour.max());

		MyComparableFour<Employee> myEmployeeFour = new MyComparableFour<Employee>(
			new Employee(2),
			new Employee(3),
			new Employee(23),
			new Employee(5));
		System.out.println(myEmployeeFour);
		System.out.println("Max: " + myEmployeeFour.max());
	}
}
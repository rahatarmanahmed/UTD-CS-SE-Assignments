public class Employee extends Person
{
	public Employee(int age)
	{
		super(age);
	}

	public String toString()
	{
		return String.format("Employee(%d)", age);
	}
}
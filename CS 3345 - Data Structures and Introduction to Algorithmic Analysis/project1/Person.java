public class Person implements Comparable<Person>
{
	int age;

	public Person(int age)
	{
		this.age = age;
	}

	public int compareTo(Person other)
	{
		return this.age - other.age;
	}

	public String toString()
	{
		return String.format("Person(%d)", age);
	}
}
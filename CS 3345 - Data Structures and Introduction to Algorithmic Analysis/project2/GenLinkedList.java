import java.lang.IndexOutOfBoundsException;
import java.util.Random;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class GenLinkedList<T>
{
	private Node<T> head;

	private static class Node<T>
	{
		public T value;
		public Node<T> next;

		public Node(T value)
		{
			this(value, null);
		}
		
		public Node(T value, Node<T> next)
		{
			this.value = value;
			this.next = next;
		}
	}

	public void addFront(T item)
	{
		head = new Node<T>(item, head);
	}

	public void addEnd(T item)
	{
		if(head == null)
		{
			head = new Node<T>(item);
			return;
		}
		Node<T> end = head;
		while(end.next != null)
			end = end.next;
		end.next = new Node<T>(item);
	}
	/*
		If empty, returns null
	*/
	public T removeFront()
	{
		if(head == null)
			return null;
		Node<T> front = head;
		head = head.next;
		return front.value;
	}
	/*
		If empty, returns null
	*/
	public T removeEnd()
	{
		if(head == null)
			return null;
		Node<T> prev = null, end = head;
		while(end.next != null)
		{
			prev = end;
			end = end.next;
		}
		if(prev != null)
		{
			prev.next = null;
			return end.value;
		}
		else // head is end
		{
			head = null;
			return end.value;
		}
	}

	private Node<T> getSecondToEndNode()
	{
		Node<T> prev = head;
		while(prev != null && prev.next != null && prev.next.next != null)
			prev = prev.next;
		return prev;
	}

	private Node<T> getEndNode()
	{
		Node<T> end = head;
		while(end != null && end.next != null)
			end = end.next;
		return end;
	}

	private Node<T> getNode(int index) throws IndexOutOfBoundsException
	{
		return getNodeAfter(index, head);
	}

	private Node<T> getNodeAfter(int index, Node<T> start)
	{
		if(index < 0 || start == null)
			throw new IndexOutOfBoundsException("Given index: " + index);

		int pos = 0;

		while(index != pos)
		{
			if(start.next == null)
				throw new IndexOutOfBoundsException("Given index: " + index);
			start = start.next;
			pos++;
		}
		return start;
	}

	public void set(int index, T item) throws IndexOutOfBoundsException
	{
		getNode(index).value = item;
	}

	public T get(int index) throws IndexOutOfBoundsException
	{
		return getNode(index).value;
	}

	public void swap(int index1, int index2) throws IndexOutOfBoundsException
	{
		int firstIndex = Math.min(index1, index2), secondIndex = Math.max(index1, index2);
		Node<T> first = getNode(firstIndex);
		Node<T> second = getNodeAfter(secondIndex - firstIndex, first);
		T temp = first.value;
		first.value = second.value;
		second.value = temp;
	}

	// 6-1 2 3 4 5-?

	public void shift(int numShifts)
	{
		if(numShifts == 0 || head == null || head.next == null)
			return;
		else if(numShifts < 0)
		{
			Node<T> oldHead = head;
			head = head.next;
			getEndNode().next = oldHead;
			oldHead.next = null;
			shift(numShifts+1);
		}
		else
		{
			Node<T> oldHead = head, prev = getSecondToEndNode();
			head = getEndNode();
			head.next = oldHead;
			prev.next = null;
			shift(numShifts-1);
		}
	}

	public GenLinkedList<T> reverse()
	{
		if(head == null)
			return new GenLinkedList<T>();
		else if(head.next == null)
		{
			GenLinkedList<T> ret = new GenLinkedList<T>();
			ret.addFront(head.value);
			return ret;
		}

		GenLinkedList<T> ret = new GenLinkedList<T>();
		Node<T> node = head;
		while(node != null)
		{
			ret.addFront(node.value);
			node = node.next;
		}
		return ret;
	}

	public void erase(int index, int num)
	{
		if(num <= 0)
			return;
		if(index == 0)
			try
			{
				head = getNode(num);
			} catch (IndexOutOfBoundsException e) {
				head = null;
			}
		else
		{
			Node<T> justBefore = getNode(index-1);
			justBefore.next = getNodeAfter(num+1, justBefore);
		}
	}

	public void insertList(List<T> list, int index)
	{
		if(list.isEmpty())
			return;
		if(head == null)
		{
			Node<T> node = new Node<T>(list.get(0));
			head = node;
			for(int k=1;k<list.size();k++)
			{
				node.next = new Node<T>(list.get(k));
				node = node.next;
			}
		}
		else if(index == 0)
		{
			Node<T> node = new Node<T>(list.get(0));
			Node<T> oldHead = head;
			head = node;
			for(int k=1;k<list.size();k++)
			{
				node.next = new Node<T>(list.get(k));
				node = node.next;
			}
			node.next = oldHead;
		}
		else
		{
			Node<T> begin = getNode(index-1);
			Node<T> oldNext = begin.next;
			for(T item : list)
			{
				Node<T> node = new Node(item);
				begin.next = node;
				begin = node;
			}
			begin.next = oldNext;
		}
	}

	public String toString()
	{
		StringBuffer out = new StringBuffer("[");
		Node<T> temp = head;
		while(temp != null)
		{
			if(temp != head)
				out.append(',');
			out.append(temp.value.toString());
			temp = temp.next;
		}
		out.append(']');
		return out.toString();
	}


	public static void main(String[] args)
	{
		System.out.println("Testing on empty list: ");
		GenLinkedList<Integer> list = new GenLinkedList<Integer>();
		test(list);

		System.out.println("Testing on list with one element: ");
		list = new GenLinkedList<Integer>();
		list.addFront(1);
		test(list);

		System.out.println("Testing on a list with 10 elements: ");
		list = new GenLinkedList<Integer>();
		for(int k=0;k<10;k++)
			list.addEnd(k);
		test(list);
	}

	public static void test(GenLinkedList<Integer> list)
	{
		System.out.println("\tAdding to end:");
		for(int k=0;k<5;k++)
			list.addEnd(k);
		print(list);

		System.out.println("\tRemoving from end:");
		for(int k=0;k<5;k++)
			System.out.println("\t\t"+list.removeEnd());
		print(list);

		System.out.println("\tAdding to front:");
		for(int k=0;k<5;k++)
			list.addFront(k);
		print(list);

		System.out.println("\tRemoving from front:");
		for(int k=0;k<5;k++)
			System.out.println("\t\t"+list.removeFront());
		print(list);

		try
		{
			int get = list.get(0);
			System.out.println("\tGetting index 0: "+ get);
			System.out.println("\tSetting index 0 to 99:");
			list.set(0, 99);
			print(list);
			System.out.println("\tSetting index 0 back to old value:");
			list.set(0, get);
			print(list);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("\tCannot access index 0");
		}

		try
		{
			int get = list.get(5);
			System.out.println("\tGetting index 5: "+ get);
			System.out.println("\tSetting index 5 to 99:");
			list.set(5, 99);
			print(list);
			System.out.println("\tSetting index 5 back to old value:");
			list.set(5, get);
			print(list);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("\tCannot access index 5");
		}

		try
		{
			System.out.println("\tSwapping indexes 0 and 1");
			list.swap(0,1);
			print(list);
			System.out.println("\tUnswapping 0 and 1");
			list.swap(0,1);
			print(list);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("\tCan't swap, out of bounds.");
		}

		System.out.println("\tShifting list forward by 3");
		list.shift(3);
		print(list);
		System.out.println("\tShifting list backward by 3");
		list.shift(-3);
		print(list);

		System.out.println("\tReverse of list: "+list.reverse());

		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		for(int k=-5;k<0;k++)
			arrayList.add(k);
		System.out.println("\tAdding arrayList at index 0");
		list.insertList(arrayList, 0);
		print(list);

		System.out.println("\tErasing 5 elements starting frmo 0");
		list.erase(0, 5);
		print(list);
	}

	public static void print(GenLinkedList<Integer> list)
	{
		System.out.println("\tList: "+list);
	}
}
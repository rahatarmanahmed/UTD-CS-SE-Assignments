import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class SpellChecker
{
	/**
	 * First argument should be the file of the document to test
	 * for misspellings
	 * @param args
	 */

	private static class Rep
	{
		public String word; // The representative word
		public int pos; // What position the character is missing

		public Rep(String word, int pos)
		{
			this.word = word;
			this.pos = pos;
		}

		public boolean equals(Object o)
		{
			if(o instanceof Rep)
				if(((Rep)o).word.equals(this.word) && ((Rep)o).pos == this.pos)
					return true;
			return false;
		}

		public int hashCode()
		{
			return word.hashCode();
		}
	}
	
	private static QPHTMap<Integer, List<String>> wordsByLength;
	private static QPHTMap<Rep, List<String>> repToWord;
	public static void main(String[] args)
	{
		// Check input for errors before doing heavy lifting.
		if(args.length < 1)
		{
			System.err.println("No file to spell check provided.");
			System.exit(1);
		}
		File f = new File(args[0]);
		if(!f.exists() || !f.isFile())
		{
			System.err.println("Invalid file provided.");
			System.exit(1);
		}
		
		// Read dictionary from files
		wordsByLength = readDictionary();
		// Build table to find one letter off words
		repToWord = buildRepTable();
		
		
		// Now go through the file
		System.out.println("Checking input for misspellings: ");
		parseInput(f);
	}
	
	private static void parseInput(File f)
	{
		try(BufferedReader in = new BufferedReader(new FileReader(f)))
		{
			
			String line;
			while((line = in.readLine()) != null)
			{
				String[] words = line.split("([^A-Za-z]|'s)+");
				for(String word : words)
					checkWord(word);
			}
		} catch (IOException e)
		{
			System.err.println("Unable to read input file.");
			System.exit(1);
		}
	}
	
	private static void checkWord(String word)
	{
		if(word.length() < 2)
			return;
		word = word.toLowerCase();
		if(!wordsByLength.containsKey(word.length()))
		{
			System.out.println(word);
			System.out.println("Alternates: []");
			return;
		}
		else if(wordsByLength.get(word.length()).contains(word))
			return;
		Set<String> alternates = new HashSet<String>();
		for(int k=0; k<word.length(); k++)
		{
			Rep rep = new Rep(word.substring(0, k) + word.substring(k+1), k);
			List<String> list = repToWord.get(rep);
			if(list == null)
				continue;
			alternates.addAll(list);
		}
		System.out.println(word);
		System.out.println("Alternates: " + alternates.toString());
	}
	
	private static <K,V> void update(QPHTMap<K,List<V>> map, K key, V value)
	{
		List<V> list = map.get(key);
		if(list == null)
		{
			list = new ArrayList<V>();
			map.put(key, list);
		}
		list.add(value);
	}
	
	private static QPHTMap<Integer, List<String>> readDictionary()
	{
		File dict = new File("dictionary.txt");
		QPHTMap<Integer, List<String>> wordsByLength = new QPHTMap<>();
		try(BufferedReader in = new BufferedReader(new FileReader(dict)))
		{
			System.out.print("Reading dictionary.txt... ");
			String line;
			while((line = in.readLine()) != null)
			{
				if(wordsByLength.containsKey(line.length()))
					wordsByLength.get(line.length()).add(line);
				else
				{
					ArrayList<String> wordList = new ArrayList<>();
					wordList.add(line);
					wordsByLength.put(line.length(), wordList);
				}
			}
			System.out.println("Finished.");
		} catch (FileNotFoundException e)
		{
			System.err.println("Missing dictionary.txt.");
			System.exit(1);
		} catch (IOException e)
		{
			System.err.println("Unable to read dictionary.txt.");
			System.exit(1);
		}
		
		File custom = new File("custom.txt");
		try(BufferedReader in = new BufferedReader(new FileReader(custom)))
		{
			System.out.print("Reading custom.txt... ");
			String line;
			while((line = in.readLine()) != null)
			{
				if(wordsByLength.containsKey(line.length()))
					wordsByLength.get(line.length()).add(line);
				else
				{
					ArrayList<String> wordList = new ArrayList<>();
					wordList.add(line);
					wordsByLength.put(line.length(), wordList);
				}
			}
			System.out.println("Finished.");
		} catch (IOException e) {
			System.out.println("Couldn't find custom.txt. Continuing.");
		}
		return wordsByLength;
	}

	private static QPHTMap<Rep, List<String>> buildRepTable()
	{
		System.out.print("Building table... ");
		QPHTMap<Rep, List<String>> repToWord = new QPHTMap<>();
		
		for(TableEntry<Integer, List<String>> wordLength : wordsByLength.entrySet())
		{
			List<String> words = wordsByLength.get(wordLength.key);
			for(int k=0; k<wordLength.key; k++)
			{
				for(String word : words)
				{
					Rep rep = new Rep(word.substring(0, k) + word.substring(k+1), k);
					update(repToWord, rep, word);
				}
			}
		}
		System.out.println("Finished.");
		return repToWord;
	}
	
	private static boolean oneCharOff(String word1, String word2)
	{
		if(word1.length() != word2.length())
			return false;
		
		int diffs = 0;
		
		for(int k=0; k<word1.length(); k++)
			if(word1.charAt(k) != word2.charAt(k))
				if(++diffs > 1)
					return false;
		return diffs == 1;
	}
	
	private static class QPHTMap<K,V> extends QuadraticProbingHashTable<TableEntry<K,V>>
	{
		public boolean put(K key, V value)
		{
			return super.insert(new TableEntry<K,V>(key, value));
		}

		public V removeKey(K key)
		{
			V v = get(key);
			super.remove(new TableEntry<K,V>(key, null));
			return v;
		}

		public boolean containsKey(K key)
		{
			return super.contains(new TableEntry<K,V>(key, null));
		}

		public V get(K key)
		{
			TableEntry<K,V> e = super.getItem(new TableEntry<K,V>(key, null));
			return (e!=null) ? e.value : null;
		}

		public Set<TableEntry<K,V>> entrySet()
		{
			return super.toSet();
		}

	}

	private static class TableEntry<K,V>
	{
		public K key;
		public V value;
		
		public TableEntry(K key, V value)
		{
			this.key = key;
			this.value = value;
		}
		@Override
		public boolean equals(Object obj)
		{
			return key.equals(((TableEntry<K,V>)obj).key);
		}
		
		@Override
		public int hashCode()
		{
			return key.hashCode();
		}
		
		@Override
		public String toString()
		{
			return "(" + key + " => " + value + ")";
		}
		
	}

	
}

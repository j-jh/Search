package edu.usfca.cs272;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author Jadon Huang
 * 
 *         Inverted Index class containing data structures and nested data
 *         structure manipulation methods
 */
public class InvertedIndex {
	/**
	 * Map of String text file paths and Integer word count
	 */
	private final Map<String, Integer> countMap;

	/**
	 * TreeMap of String words, Nested TreeMap of file paths for String word, Nested
	 * TreeSet of Integer indexes for file path of each word
	 */
	private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> wordMap;

	/**
	 * Creates type InvertedIndex that takes in data structures for passing into
	 * builder
	 */
	public InvertedIndex() {
		this.wordMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		this.countMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	}

	/**
	 * Add method for building inverted index
	 * 
	 * @param word     the word to be added to data structure key
	 * @param filePath the filePath location for where word was located
	 * @param counter  the index at which word was located within filePath
	 */
	public void add(String word, String filePath, Integer counter) {

		wordMap.putIfAbsent(word, new TreeMap<>());
		wordMap.get(word).putIfAbsent(filePath, new TreeSet<Integer>());
		wordMap.get(word).get(filePath).add(counter);
		countMap.put(filePath, counter);
	}

	/**
	 * Add all method to loop through add method
	 * 
	 * @param index the Inverted Index class index to add to
	 */
	public void addAll(InvertedIndex index) {
		for (String key : index.wordMap.keySet()) {
			if (this.wordMap.containsKey(key)) {
				for (String locations : index.wordMap.get(key).keySet()) {
					if (this.wordMap.get(key).containsKey(locations) && this.wordMap.containsKey(key)) {
						this.wordMap.get(key).get(locations).addAll(index.wordMap.get(key).get(locations));
					} else {
						this.wordMap.get(key).put(locations, index.wordMap.get(key).get(locations));
					}
				}
			} else {
				this.wordMap.put(key, index.wordMap.get(key));
			}
		}

		for (String location : index.countMap.keySet()) {
			// System.out.println("inside for loop for count map");
			// if ((index.getCount(location) > this.getCount(location)) ||
			// !countMap.containsKey(location)) {
			if (this.wordMap.containsKey(location)) {
				this.countMap.put(location, this.countMap.get(location) + index.countMap.get(location));
			} else {

				// System.out.println("add all location: " + location);
				// System.out.println("add all countmap.get(location): " +
				// other.countMap.get(location));
				this.countMap.put(location, index.countMap.get(location));
			}
		}
	}

	/**
	 * general addAll method
	 *
	 * @param stems word in the file
	 * @param path  file path of the file
	 */
	public void addAll(List<String> stems, String path) {
		addAll(stems, path, 0);
	}

	/**
	 * adds the path of the file and all of the indexes that word appears at in the
	 * file
	 *
	 * @param stems stems word in the file
	 * @param path  path file path of the file
	 * @param start the integer to start at
	 */
	public void addAll(List<String> stems, String path, int start) {
		for (int i = 0; i < stems.size(); i++) {
			add(path, stems.get(i), start + i);
		}
	}

	/**
	 * get count to return word count
	 * 
	 * @param filePath name of file path
	 * @return the word count
	 */
	public Integer getCount(String filePath) {
		return countMap.get(filePath);
	}

	/**
	 * getCountMap for accessing contents of private final structure
	 * 
	 * @return unmodifiable wordMap data structure
	 */
	public Map<String, Integer> getCountMap() {
		return Collections.unmodifiableMap(countMap);
	}

	/**
	 * getIndex to get index of word at location
	 * 
	 * @param word     the word from wordMap
	 * @param filePath the file locations
	 * @return index of word location from file path
	 */
	public Set<Integer> getIndex(String word, String filePath) {
		return wordMap.get(word).get(filePath);
	}

	/**
	 * getPath to get path of word
	 * 
	 * @param word the word from wordMap
	 * @return set of file paths and indexes
	 */
	public Set<String> getPath(String word) {
		return wordMap.get(word).keySet();
	}

	/**
	 * getWord to get word key
	 * 
	 * @return set of words from wordMap
	 */
	public Set<String> getWord() {
		return wordMap.keySet();
	}

	/**
	 * getWordMap for accessing contents of private final structure
	 * 
	 * @return unmodifiable wordMap data structure
	 */
	public Map<String, TreeMap<String, TreeSet<Integer>>> getWordMap() {
		return Collections.unmodifiableMap(wordMap);
	}

	/**
	 * hasIndex for checking if index contained within data structure
	 * 
	 * @param word     the word from wordMap
	 * @param filePath the location of word
	 * @param index    the position of word
	 * @return true/false if word in position of file
	 */
	public boolean hasIndex(String word, String filePath, Integer index) {
		return hasWord(word) && wordMap.get(word).get(filePath).contains(index);
	}

	/**
	 * hasPath for checking if path contained within data structure
	 * 
	 * @param word     the word from wordMap
	 * @param filePath the location of word
	 * @return true/false if word in file path
	 */
	public boolean hasPath(String word, String filePath) {
		return hasWord(word) && wordMap.get(word).containsKey(filePath);
	}

	/**
	 * hasWord for checking if word contained within data structure
	 * 
	 * @param word the word from wordMap
	 * @return true/false if word contained within wordMap
	 */
	public boolean hasWord(String word) {
		return wordMap.containsKey(word);
	}

	/**
	 * sizeIndex for getting index size
	 * 
	 * @param word     the word nested filePaths
	 * @param filePath the filePath nested indexes
	 * @return size of the index structure
	 */
	public int sizeIndex(String word, String filePath) {
		return getIndex(word, filePath).size();
	}

	/**
	 * sizePath for size of path structure
	 * 
	 * @param word the word nested filePaths
	 * @return size of the filePath structure
	 */
	public int sizePath(String word) {
		return getPath(word).size();
	}

	/**
	 * sizeWord for wordMap size
	 * 
	 * @return size of the wordMap structure
	 */
	public int sizeWord() {
		return getWord().size();
	}

	/**
	 * search method to determine whether to use exact or partial search
	 * 
	 * @param query the treeset of queries to search through
	 * @param exact the boolean to determine exact or partial search
	 * @return the result search method
	 */
	public ArrayList<QuerySearchResults> search(TreeSet<String> query, boolean exact) {
		return exact ? exactSearch(query) : partialSearch(query);
	}

	/**
	 * exact seach to match exact word stem
	 * 
	 * @param queries treeset of queries to search through
	 * @return returns query search results
	 */
	public ArrayList<QuerySearchResults> exactSearch(TreeSet<String> queries) {
		ArrayList<QuerySearchResults> querySR = new ArrayList<>();
		TreeMap<String, QuerySearchResults> tempMap = new TreeMap<>();
		for (String query : queries) {
			buildMethod(query, querySR, tempMap);
		}
		Collections.sort(querySR);
		return querySR;
	}

	/**
	 * exact seach to search word that begins with stem
	 * 
	 * @param queries treeset of queries to search through
	 * @return returns query search results
	 */
	public ArrayList<QuerySearchResults> partialSearch(TreeSet<String> queries) {
		ArrayList<QuerySearchResults> querySR = new ArrayList<>();
		TreeMap<String, QuerySearchResults> tempMap = new TreeMap<>();
		for (String query : queries) {
			for (String partial : wordMap.tailMap(query).keySet()) {
				if (!partial.startsWith(query)) {
					break;
				}
				buildMethod(partial, querySR, tempMap);
			}
		}
		Collections.sort(querySR);
		return querySR;
	}

	/**
	 * Repeated build method code in both exact search and partial search
	 * 
	 * @param queryPart query to check in map
	 * @param querySR   query search result structure
	 * @param temprMap  temp map to check duplicates
	 */
	public void buildMethod(String queryPart, List<QuerySearchResults> querySR,
			Map<String, QuerySearchResults> temprMap) {

		if (wordMap.containsKey(queryPart)) {

			for (String filePath : wordMap.get(queryPart).keySet()) {
				QuerySearchResults result = new QuerySearchResults(filePath);

				if (!temprMap.containsKey(filePath)) {
					querySR.add(result);
					temprMap.put(filePath, result);
					temprMap.get(filePath).countUpdate(queryPart);
				}

				else if (temprMap.containsKey(filePath)) {
					temprMap.get(filePath).countUpdate(queryPart);
				}
			}
		}
	}

	@Override
	/*
	 * toString implementation to print out wordMap data structure
	 */
	public String toString() {
		return this.getWordMap().toString();
	}

	/**
	 * to write to JSON format for nested map
	 * 
	 * @param path the file path
	 * @throws IOException to throw
	 */
	public void mapJson(Path path) throws IOException {
		JsonWriter.writeNestedArray(this.wordMap, path);
	}

	/**
	 * to write to JSON format for count map
	 * 
	 * @param path the path to input
	 * @throws IOException to throw
	 */
	public void wordCountJson(Path path) throws IOException {
		// System.out.println("countmap: " + this.countMap);
		JsonWriter.writeObject(this.countMap, path);
	}

	/**
	 * QuerySearchResults interface implementing comparable
	 * 
	 * @author Jadon Huang
	 *
	 */
	public class QuerySearchResults implements Comparable<QuerySearchResults> {

		/**
		 * filePath file path of input
		 */
		public String filePath;

		/**
		 * total word count
		 */
		public int wordCount;

		/**
		 * word frequency score
		 */
		public double wordFreq;

		/**
		 * QuerySearchResults method call
		 * 
		 * @param filePath takes in file path to return word count, path, frequency
		 */
		public QuerySearchResults(String filePath) {
			new InvertedIndex();
			// this.wordCount = wordCount;
			this.filePath = filePath;
			// this.wordFreq = wordFreq;
		}

		/**
		 * call numbers to update count and frequency
		 * 
		 * @param count the word count to take in
		 * @param freq  the word frequency to take in
		 */
		public void callNumbers(int count, double freq) {
			this.setFreq(freq);
			this.setWord(count);
		}

		@Override
		/**
		 * override compare to method with priority word freq, count, file path
		 */
		public int compareTo(QuerySearchResults object) {
			if (this.wordFreq != object.wordFreq) {

				return Double.compare(object.wordFreq, this.wordFreq);
			} else if (this.wordCount != object.wordCount) {

				return Integer.compare(object.wordCount, this.wordCount);
			}
			return this.filePath.compareToIgnoreCase(object.filePath);
		}

		/**
		 * count update to calculate update word count and frequency score
		 * 
		 * @param query the word query
		 */
		private void countUpdate(String query) {
			this.wordCount += sizeIndex(query, filePath);
			this.wordFreq = (double) wordCount / Double.valueOf(getCount(filePath));
		}

		/**
		 * @return return file path
		 */
		public String fileGet() {
			return filePath;
		}

		/**
		 * @return return word frequency
		 */
		public double freqGet() {
			return wordFreq;
		}

		/**
		 * set file path name
		 * 
		 * @param filePath takes file path
		 */
		public void setFile(String filePath) {
			this.filePath = filePath;
		}

		/**
		 * set word frequency
		 * 
		 * @param wordFreq takes double
		 */
		public void setFreq(double wordFreq) {
			this.wordFreq = wordFreq;
		}

		/**
		 * set word count
		 * 
		 * @param wordCount takes int
		 */
		public void setWord(int wordCount) {
			this.wordCount = wordCount;
		}

		@Override
		// for testing fun!
		public String toString() {
			return "[" + wordCount + ", " + wordFreq + ", " + filePath + "]";

		}

		/**
		 * @return return word count
		 */
		public int wordGet() {
			return wordCount;
		}
	}
}

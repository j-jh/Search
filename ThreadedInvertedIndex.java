package edu.usfca.cs272;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * An index to store locations and the words found at those locations. Makes no
 * assumption about order or duplicates.
 *
 *
 * @author CS 272 Software Development (University of San Francisco)
 * @version Fall 2022
 */
public class ThreadedInvertedIndex extends InvertedIndex {
	/**
	 * lock for thread safe inverted index.
	 */
	private final ReadWriteLock lock;

	/**
	 * constructor to construct the data structure.
	 */
	public ThreadedInvertedIndex() {
		super();
		lock = new ReadWriteLock();
	}

	@Override
	/**
	 * Adds the location and word, and the position in file.
	 *
	 * @param word     the word found
	 * @param location the location the word was found
	 * @param count    the number/position where the word is located
	 */
	public void add(String word, String location, Integer count) {
		lock.write().lock();
		try {
			super.add(word, location, count);
		} finally {
			lock.write().unlock();
		}
	}

	@Override
	/**
	 * Adds the location and word, and the position in file.
	 *
	 * @param word     the word found
	 * @param location the location the word was found
	 * @param count    the number/position where the word is located
	 */
	public void addAll(InvertedIndex other) {
		lock.write().lock();
		try {
			super.addAll(other);
		} finally {
			lock.write().unlock();
		}
	}

	@Override
	/**
	 * get count to return word count
	 * 
	 * @param filePath name of file path
	 * @return the word count
	 */
	public Integer getCount(String filePath) {
		lock.read().lock();
		try {
			return super.getCount(filePath);
		} finally {
			lock.read().unlock();
		}
	}

	@Override
	/**
	 * getCountMap for accessing contents of private final structure
	 * 
	 * @return unmodifiable wordMap data structure
	 */
	public Map<String, Integer> getCountMap() {
		lock.read().lock();
		try {
			return super.getCountMap();
		} finally {
			lock.read().unlock();
		}
	}

	@Override
	/**
	 * getIndex to get index of word at location
	 * 
	 * @param word     the word from wordMap
	 * @param filePath the file locations
	 * @return index of word location from file path
	 */
	public Set<Integer> getIndex(String word, String filePath) {
		lock.read().lock();
		try {
			return super.getIndex(word, filePath);
		} finally {
			lock.read().unlock();
		}
	}

	@Override
	/**
	 * getPath to get path of word
	 * 
	 * @param word the word from wordMap
	 * @return set of file paths and indexes
	 */
	public Set<String> getPath(String word) {
		lock.read().lock();
		try {
			return super.getPath(word);
		} finally {
			lock.read().unlock();
		}
	}

	@Override
	/**
	 * getWord to get word key
	 * 
	 * @return set of words from wordMap
	 */
	public Set<String> getWord() {
		lock.read().lock();
		try {
			return super.getWord();
		} finally {
			lock.read().unlock();
		}
	}

	@Override
	/**
	 * getWordMap for accessing contents of private final structure
	 * 
	 * @return unmodifiable wordMap data structure
	 */
	public Map<String, TreeMap<String, TreeSet<Integer>>> getWordMap() {
		lock.read().lock();
		try {
			return super.getWordMap();
		} finally {
			lock.read().unlock();
		}
	}

	@Override
	/**
	 * hasIndex for checking if index contained within data structure
	 * 
	 * @param word     the word from wordMap
	 * @param filePath the location of word
	 * @param index    the position of word
	 * @return true/false if word in position of file
	 */
	public boolean hasIndex(String word, String filePath, Integer index) {
		lock.read().lock();
		try {
			return super.hasIndex(word, filePath, index);
		} finally {
			lock.read().unlock();
		}
	}

	@Override
	/**
	 * hasPath for checking if path contained within data structure
	 * 
	 * @param word     the word from wordMap
	 * @param filePath the location of word
	 * @return true/false if word in file path
	 */
	public boolean hasPath(String word, String filePath) {
		lock.read().lock();
		try {
			return super.hasPath(word, filePath);
		} finally {
			lock.read().unlock();
		}
	}

	@Override
	/**
	 * hasWord for checking if word contained within data structure
	 * 
	 * @param word the word from wordMap
	 * @return true/false if word contained within wordMap
	 */
	public boolean hasWord(String word) {
		lock.read().lock();
		try {
			return super.hasWord(word);
		} finally {
			lock.read().unlock();
		}
	}

	@Override
	public void mapJson(Path path) throws IOException {
		lock.read().lock();
		try {
			super.mapJson(path);
		} finally {
			lock.read().unlock();
		}
	}
	

	@Override
	/**
	 * sizeIndex for getting index size
	 * 
	 * @param word     the word nested filePaths
	 * @param filePath the filePath nested indexes
	 * @return size of the index structure
	 */
	public int sizeIndex(String word, String filePath) {
		lock.read().lock();
		try {
			return super.sizeIndex(word, filePath);
		} finally {
			lock.read().unlock();
		}
	}

	@Override
	/**
	 * sizePath for size of path structure
	 * 
	 * @param word the word nested filePaths
	 * @return size of the filePath structure
	 */
	public int sizePath(String word) {
		lock.read().lock();
		try {
			return super.sizePath(word);
		} finally {
			lock.read().unlock();
		}
	}

	@Override
	/**
	 * sizeWord for wordMap size
	 * 
	 * @return size of the wordMap structure
	 */
	public int sizeWord() {
		lock.read().lock();
		try {
			return super.sizeWord();
		} finally {
			lock.read().unlock();
		}
	}
	
	@Override
	/**
	 * Multi thread exact search
	 * 
	 * @param queries the set of quries to search
	 */
	public ArrayList<InvertedIndex.QuerySearchResults> exactSearch(TreeSet<String> queries) {
		lock.read().lock();
		try {
			return super.exactSearch(queries);
		} finally {
			lock.read().unlock();
		}
	}

	/**
	 * Multi thread partial search
	 * 
	 * @param queries the set of quries to search
	 */
	public ArrayList<InvertedIndex.QuerySearchResults> partialSearch(TreeSet<String> queries) {
		lock.read().lock();
		try {
			return super.partialSearch(queries);
		} finally {
			lock.read().unlock();
		}
	}

	@Override
	/**
	 * to string 
	 */
	public String toString() {
		lock.read().lock();
		try {
			return super.toString();
		} finally {
			lock.read().unlock();
		}
	}

	@Override
	/**
	 * output word count map to Json
	 */
	public void wordCountJson(Path path) throws IOException {
		lock.read().lock();
		try {
			super.wordCountJson(path);
		} finally {
			lock.read().unlock();
		}
	}
}

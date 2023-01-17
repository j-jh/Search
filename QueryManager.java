package edu.usfca.cs272;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * the Query manager class to handle query processing
 * 
 * @author Jadon Huang
 *
 */
public class QueryManager implements QueryInterface {

	/**
	 * Search results map consisting of string word, with nested structure query
	 * search result containing word count, frequency, location
	 */
	private final TreeMap<String, ArrayList<InvertedIndex.QuerySearchResults>> searchResult;

	/**
	 * Reference Inverted Index class methods
	 */
	private final InvertedIndex index;

	/**
	 * constructor
	 * 
	 * @param index invertedIndex to reference Inverted Index class methods
	 */
	public QueryManager(InvertedIndex index) {
		searchResult = new TreeMap<>();
		this.index = index;
	}

	@Override
	/**
	 * abstract query processor declaration
	 * 
	 * @param line  the line to process
	 * @param exact the boolean to determine search type
	 */
	public void queryProcessor(String line, boolean exact) {
		TreeSet<String> stemmedLine = WordCleaner.uniqueStems(line);
		String joined = String.join(" ", stemmedLine);

		if (!stemmedLine.isEmpty() && !searchResult.containsKey(joined)) {
			this.searchResult.put(joined, this.index.search(stemmedLine, exact));
		}
	}

	@Override
	/**
	 * to output to Json format
	 * 
	 * @param path the path to input
	 * @throws IOException to throw
	 */
	public void resultsJson(Path path) throws IOException {
		JsonWriter.writeSearch(searchResult, path);
	}
}

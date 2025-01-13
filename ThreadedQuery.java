package edu.usfca.cs272;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Threaded query processor class to handle queries
 * 
 * @author Jadon Huang
 *
 */
public class ThreadedQuery implements QueryInterface {
	/**
	 * for storing query search results + file path
	 */
	private TreeMap<String, ArrayList<InvertedIndex.QuerySearchResults>> querySR;

	/**
	 * InvertedIndex to use inverted index methods
	 */
	private final ThreadedInvertedIndex index;

	/**
	 * workqueue member is used for multithreading
	 */
	private final WorkQueue queuer;

	/**
	 * constructor for class
	 * 
	 * @param index  the threaded Inverted Index class
	 * @param queuer the workqueue to execute
	 */
	public ThreadedQuery(ThreadedInvertedIndex index, WorkQueue queuer) {
		querySR = new TreeMap<String, ArrayList<InvertedIndex.QuerySearchResults>>();
		this.index = index;
		this.queuer = queuer;
	}

	@Override
	/**
	 * the method to process each line in the query text file.
	 *
	 * @param path  the path to process the queries
	 * @param exact deterimie if search will be exact or not.
	 * @throws IOException the exception to throw if there is a error in processing
	 *                     the file
	 */
	public void queryProcessor(Path path, boolean exact) throws IOException {
		QueryInterface.super.queryProcessor(path, exact);
		this.queuer.finish();
	}

	@Override
	/**
	 * the query processor to execute run
	 */
	public void queryProcessor(String line, boolean exact) {
		queuer.execute(new Task(line, exact));
	}

	@Override
	/**
	 * to output map to Json format
	 */
	public void resultsJson(Path path) throws IOException {
		synchronized (querySR) {
			JsonWriter.writeSearch(this.querySR, path);
		}
	}

	/**
	 * task class to stem words and add to query map
	 *
	 * @author Jadon Huang
	 *
	 */
	public class Task implements Runnable {

		/**
		 * the string line to parse
		 */
		private final String line;

		/**
		 * boolean to determine search type
		 */
		private final boolean exact;

		/**
		 * constructor declaration
		 * 
		 * @param line  the line to parse from
		 * @param exact the boolean to see if we are doing exact search or not
		 *
		 */
		public Task(String line, boolean exact) {
			this.line = line;
			this.exact = exact;

		}

		@Override
		/**
		 * the run method to execute with queuer
		 */
		public void run() {
			TreeSet<String> stemmedLine = WordCleaner.uniqueStems(line);

			if (!stemmedLine.isEmpty()) {
				String joined = String.join(" ", stemmedLine);

				synchronized (querySR) {
					if (querySR.containsKey(joined)) {
						return;
					}
				}

				var local = index.search(stemmedLine, exact);

				// System.out.println("joined: " + joined);
				// System.out.println("local: " + local);

				synchronized (querySR) {
					querySR.put(joined, local);
				}
			}
		}

	}
}

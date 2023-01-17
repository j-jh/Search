package edu.usfca.cs272;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Class responsible for running this project based on the provided command-line
 * arguments. See the README for details.
 *
 * @author Jadon Huang
 * @author CS 272 Software Development (University of San Francisco)
 * @version Fall 2022
 */
public class Driver {
	/**
	 * Initializes the classes necessary based on the provided command-line
	 * arguments. This includes (but is not limited to) how to build or search an
	 * inverted index.
	 *
	 * @param args flag/value pairs used to start this program
	 */
	public static void main(String[] args) {

		ArgumentParser argParse = new ArgumentParser(args);
		InvertedIndex index = null;
		ThreadedInvertedIndex multiThread = null;
		QueryInterface query = null;
		WorkQueue queuer = null;
		Integer threads = 5;

		if (argParse.hasFlag("-threads")) {

			// System.out.println(argParse);
			Integer getThread = argParse.getInteger("-threads", threads);
			// System.out.println("num threads: " + getThread);

			if (getThread <= 0) {
				getThread = 5;
			}
			queuer = new WorkQueue(getThread);
			multiThread = new ThreadedInvertedIndex();

			query = new ThreadedQuery(multiThread, queuer);
			index = multiThread;
		} else {
			index = new InvertedIndex();
			query = new QueryManager(index);
			// System.out.println(query);
		}

		if (argParse.hasFlag("-text")) {
			Path argPath = argParse.getPath("-text");
			try {
				if (argPath == null) {
					System.out.println("Path is null: " + argPath);
				} else {

					if (queuer != null && multiThread != null) {
						// System.out.println("thread flag");
						ThreadedDirectoryStreamer.processDirectory(argPath, multiThread, queuer);

					} else {
						DirectoryStreamer.processDirectory(argPath, index);
					}
				}

			} catch (IOException e) {
				System.out.println("Bad text path: " + argParse.toString());
			}
		}
		if (argParse.hasFlag("-index")) {

			Path indexPath = argParse.getPath("-index", Path.of("index.json"));
			try {
				index.mapJson(indexPath);
			} catch (IOException e) {
				System.out.println("Bad text path: " + argParse.toString());
			}
		}
		if (argParse.hasFlag("-counts")) {
			Path countPath = argParse.getPath("-counts", Path.of("count.json"));
			try {
				// System.out.println("index after add all: " + index.getCountMap());
				index.wordCountJson(countPath);

			} catch (IOException e) {
				System.out.println("Bad text path: " + argParse.toString());
			}
		}

		if (argParse.hasFlag("-query")) {
			Path searchPath = argParse.getPath("-query");
			try {

				if (searchPath == null) {
					System.out.println("Path is null: " + searchPath);
				}

				else {
					// System.out.println(query);
					// System.out.println("test query");
					query.queryProcessor(searchPath, argParse.hasFlag("-exact"));

				}
			} catch (IOException e) {
				System.out.println("Bad query path");
			}

		}
		if (argParse.hasFlag("-results")) {
			Path resultPath = argParse.getPath("-results", Path.of("results.json"));

			// System.out.println(argParse.toString());

			try {
				query.resultsJson(resultPath);
			} catch (IOException e) {

				System.out.println("Bad results path");
			}
		}

		if (queuer != null) {
			queuer.shutdown();
		}
	}

}

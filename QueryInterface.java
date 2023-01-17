package edu.usfca.cs272;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Query interface class to implement in manager and threaded
 * 
 * @author Jadon Huang
 *
 */
public interface QueryInterface {

	/**
	 * query processor to process each query line
	 * 
	 * @param path  the file path
	 * @param exact the boolean to determine exact or partial search
	 * @throws IOException to throw
	 */
	public default void queryProcessor(Path path, boolean exact) throws IOException {
		try (BufferedReader reader = Files.newBufferedReader(path)) {
			String line;
			while ((line = reader.readLine()) != null) {
				queryProcessor(line, exact);
			}
		}
	}

	/**
	 * abstract method to query processor declaration
	 * 
	 * @param line  the line to process
	 * @param exact the boolean to determine search type
	 */
	public void queryProcessor(String line, boolean exact);

	/**
	 * abstract method to output to Json format
	 * 
	 * @param path the path to input
	 * @throws IOException to throw
	 */
	public void resultsJson(Path path) throws IOException;

}

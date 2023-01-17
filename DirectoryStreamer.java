package edu.usfca.cs272;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;
import java.nio.charset.StandardCharsets;
import static opennlp.tools.stemmer.snowball.SnowballStemmer.ALGORITHM.ENGLISH;

/**
 * DirectorySteamer recursively traverses all given paths and returns a list of
 * text files from the given directories
 */

public class DirectoryStreamer {
	/**
	 * Traverses through the directory and its sub-directories
	 * 
	 * @param directory the path to stream through
	 * @param index     the Inverted Index class
	 * @throws IOException to throw
	 */
	public static void processDirectory(Path directory, InvertedIndex index) throws IOException {
		if (Files.isDirectory(directory)) {
			traverseDirectory(directory, index);
		} else {
			stemmer(directory, index);
		}
	}

	/**
	 * Recursively traverses through directory
	 * 
	 * @param directory the current file directory
	 * @param index     the Inverted Index class
	 * @throws IOException thrown if traverseDirectory call invalid
	 */
	public static void traverseDirectory(Path directory, InvertedIndex index) throws IOException {
		try (DirectoryStream<Path> listing = Files.newDirectoryStream(directory)) {
			for (Path files : listing) {
				if (Files.isDirectory(files)) {
					traverseDirectory(files, index);
				} else if (isTextFile(files)) {
					stemmer(files, index);
				}
			}
		}
	}

	/**
	 * Checks if given path is a text file
	 * 
	 * @param path the current path location
	 * @return true/false if given path is a .txt .text file
	 */
	public static boolean isTextFile(Path path) {
		return path.toString().toLowerCase().endsWith(".txt") || path.toString().toLowerCase().endsWith(".text");
	}
	
	/**
	 * Stemmer to build the InvertedIndex data structure, calling a buffered reader
	 * to read through each line of word, parsing every line, and adding each
	 * default stemmed word, current path location, and current counter to the
	 * inverted index data structure
	 * 
	 * @param file the parsed file path
	 * @param index the Inverted Index class 
	 * @throws IOException to throw
	 */
	public static void stemmer(Path file, InvertedIndex index) throws IOException {
		Integer position = 1;
		Stemmer stemmer = new SnowballStemmer(ENGLISH);
		String location = file.toString();

		try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
			String line = reader.readLine();
			while (line != null) {
				for (String word : WordCleaner.parse(line)) {
					index.add(stemmer.stem(word).toString(), location, position++);
				}
				line = reader.readLine();
			}
		}
	}
}
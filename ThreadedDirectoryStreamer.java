package edu.usfca.cs272;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * DirectorySteamer recursively traverses all given paths and returns a list of
 * text files from the given directories
 */
public class ThreadedDirectoryStreamer {

	/**
	 * Multithreaded Process Directory method to call traverse directory if
	 * directory else execute threads
	 * 
	 * @param path   the file path
	 * @param index  the Threaded Inverted Index class
	 * @param queuer the work queue
	 * @throws IOException to throw
	 */
	public static void processDirectory(Path path, ThreadedInvertedIndex index, WorkQueue queuer) throws IOException {
		// System.out.println("inside process directory");
		// System.out.println("path: " + path);
		if (Files.isDirectory(path)) {
			traverseDirectory(path, index, queuer);
		} else {
			// System.out.println("process directory task");
			queuer.execute(new Task(path, index));
		}
		queuer.finish();
	}

	/**
	 * Recursively traverses through directory
	 * 
	 * @param directory the path directory to traverse
	 * @param index     the Threaded Inverted Index class reference
	 * @param queuer    the work queue to execute
	 * @throws IOException to throw
	 */
	private static void traverseDirectory(Path directory, ThreadedInvertedIndex index, WorkQueue queuer)
			throws IOException {

		try (DirectoryStream<Path> directoryList = Files.newDirectoryStream(directory)) {

			for (Path path : directoryList) {

				if (!Files.isDirectory(path) && (isTextFile(path))) {
					queuer.execute(new Task(path, index));
				}

				if (Files.isDirectory(path)) {
					traverseDirectory(path, index, queuer);
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
	 * Task class to stream directory and stem files
	 * 
	 * @author Jadon Huang
	 *
	 */
	public static class Task implements Runnable {

		/**
		 * Threaded inverted index
		 */
		private final ThreadedInvertedIndex index;

		/**
		 * the file path
		 */
		private final Path path;

		/**
		 * constructor declaration
		 * 
		 * @param path  the file path
		 * @param index to add to
		 */
		public Task(Path path, ThreadedInvertedIndex index) {
			this.path = path;
			this.index = index;
		}

		@Override
		/**
		 * to run with queuer
		 */
		public void run() throws UncheckedIOException {
			try {
				InvertedIndex local = new InvertedIndex();
				DirectoryStreamer.stemmer(path, local);
				// System.out.println(local.getCountMap());
				index.addAll(local);

			} catch (IOException e) {
				// System.out.println("directory");
				throw new UncheckedIOException(e);
			}
		}
	}
}
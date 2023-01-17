package edu.usfca.cs272;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import edu.usfca.cs272.InvertedIndex.QuerySearchResults;

/**
 * Outputs several simple data structures in "pretty" JSON format where newlines
 * are used to separate elements and nested elements are indented using tabs.
 *
 * Warning: This class is not thread-safe. If multiple threads access this class
 * concurrently, access must be synchronized externally.
 *
 * @author CS 272 Software Development (University of San Francisco)
 * @version Spring 2022
 */

public class JsonWriter {
	/**
	 * Returns the elements as a pretty JSON array.
	 *
	 * @param elements the elements to use
	 * @return a {@link String} containing the elements in pretty JSON format
	 *
	 * @see #writeBasicArray(Collection, Writer, int)
	 */
	public static String writeBasicArray(Collection<? extends Number> elements) {
		try {
			StringWriter writer = new StringWriter();
			writeBasicArray(elements, writer, 0);
			return writer.toString();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Writes the elements as a pretty JSON array to file.
	 *
	 * @param elements the elements to write
	 * @param path     the file path to use
	 * @throws IOException if an IO error occurs
	 *
	 * @see #writeBasicArray(Collection, Writer, int)
	 */
	public static void writeBasicArray(Collection<? extends Number> elements, Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path, UTF_8)) {
			writeBasicArray(elements, writer, 0);
		}
	}

	/**
	 * Writes the elements as a pretty JSON array.
	 * 
	 * @param elements the elements to write
	 * @param writer   the writer to use
	 * @param indent   the initial indent level; the first bracket is not indented,
	 *                 inner elements are indented by one, and the last bracket is
	 *                 indented at the initial indentation level
	 * @throws IOException if an IO error occurs
	 */
	public static void writeBasicArray(Collection<? extends Number> elements, Writer writer, int indent)
			throws IOException {

		writer.write("[");

		Iterator<? extends Number> iterate = elements.iterator();

		if (iterate.hasNext()) {
			writer.write("\n");
			writeIndent(writer, indent + 1);
			writer.write(iterate.next().toString());
		}

		while (iterate.hasNext()) {
			writer.write(",");
			writer.write("\n");
			writeIndent(writer, indent + 1);
			writer.write(iterate.next().toString());
		}

		writer.write("\n");
		writeIndent(writer, indent);
		writer.write("]");
	}

	/**
	 * Indents and then writes the String element.
	 *
	 * @param element the element to write
	 * @param writer  the writer to use
	 * @param indent  the number of times to indent
	 * @throws IOException if an IO error occurs
	 */
	public static void writeIndent(String element, Writer writer, int indent) throws IOException {
		writeIndent(writer, indent);
		writer.write(element);
	}

	/**
	 * Indents the writer by the specified number of times. Does nothing if the
	 * indentation level is 0 or less.
	 *
	 * @param writer the writer to use
	 * @param indent the number of times to indent
	 * @throws IOException if an IO error occurs
	 */
	public static void writeIndent(Writer writer, int indent) throws IOException {
		while (indent-- > 0) {
			writer.write("  ");
		}
	}

	/**
	 * @param elements the map containing a nested structure
	 * @return string containing JSON formatted element
	 */
	public static String writeNest(Map<String, ? extends Collection<? extends Number>> elements) {
		try {
			StringWriter writer = new StringWriter();
			writeNest(elements, writer, 0);
			return writer.toString();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Writes the elements as a pretty JSON array to file.
	 *
	 * @param elements the elements to write
	 * @param path     the file path to use
	 * @throws IOException if an IO error occurs
	 *
	 *                     #writeNest(Collection, Writer, int)
	 */
	public static void writeNest(Map<String, ? extends Collection<? extends Number>> elements, Path path)
			throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path, UTF_8)) {
			writeNest(elements, writer, 0);
		}
	}

	/**
	 * Writes the elements as a pretty JSON object with nested arrays. The generic
	 * notation used allows this method to be used for any type of map with any type
	 * of nested collection of integer objects.
	 * 
	 * @param element the elements to write
	 * @param writer  the writer to use
	 * @param indent  the initial indent level; the first bracket is not indented,
	 *                inner elements are indented by one, and the last bracket is
	 *                indented at the initial indentation level
	 * @throws IOException if an IO error occurs
	 */
	public static void writeNest(Map<String, ? extends Collection<? extends Number>> element, Writer writer, int indent)
			throws IOException {

		writer.write("{");

		Iterator<String> iterate = element.keySet().iterator();

		if (iterate.hasNext()) {
			var key = iterate.next();
			var value = element.get(key);

			writer.write("\n");
			writeIndent(writer, indent);
			writeQuote(key, writer, indent);
			writer.write(": ");
			writeBasicArray(value, writer, indent + 1);
		}

		while (iterate.hasNext()) {
			var key = iterate.next();
			var value = element.get(key);

			writer.write(",\n");
			writeIndent(writer, indent);
			writeQuote(key, writer, indent);
			writer.write(": ");
			writeBasicArray(value, writer, indent + 1);
		}
		writer.write("\n");
		writeIndent(writer, indent);
		writer.write("}");
	}

	/**
	 * Returns the elements as a pretty JSON object with nested arrays.
	 *
	 * @param elements the elements to use
	 * @return a {@link String} containing the elements in pretty JSON format
	 *
	 * @see #writeNestedArray(Map, Writer, int)
	 */
	public static String writeNestedArray(
			Map<String, ? extends Map<String, ? extends Collection<? extends Number>>> elements) {
		try {
			StringWriter writer = new StringWriter();
			writeNestedArray(elements, writer, 0);
			return writer.toString();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Writes the elements as a pretty JSON object with nested arrays to file.
	 *
	 * @param elements the elements to write
	 * @param path     the file path to use
	 * @throws IOException if an IO error occurs
	 *
	 * @see #writeNestedArray(Map, Writer, int)
	 */
	public static void writeNestedArray(
			Map<String, ? extends Map<String, ? extends Collection<? extends Number>>> elements, Path path)
			throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path, UTF_8)) {
			writeNestedArray(elements, writer, 0);
		}
	}

	/**
	 * Writes the elements as a pretty JSON object with triply nested arrays. The
	 * generic notation used allows this method to be used for any type of map with
	 * any type of nested collection of integer objects.
	 *
	 * @param elements the elements to write
	 * @param writer   the writer to use
	 * @param indent   the initial indent level; the first bracket is not indented,
	 *                 inner elements are indented by one, and the last bracket is
	 *                 indented at the initial indentation level
	 * @throws IOException if an IO error occurs
	 */

	public static void writeNestedArray(
			Map<String, ? extends Map<String, ? extends Collection<? extends Number>>> elements, Writer writer,
			int indent) throws IOException {

		writer.write("{");

		Iterator<String> iterate = elements.keySet().iterator();

		if (iterate.hasNext()) {
			var key = iterate.next();
			var value = elements.get(key);

			writer.write("\n");
			writeQuote(key, writer, indent + 1);
			writer.write(": ");
			writeNest(value, writer, indent + 1);
		}

		while (iterate.hasNext()) {
			var key = iterate.next();
			var value = elements.get(key); // to prevent skipping with next

			writer.write(",");
			writer.write("\n");
			writeQuote(key, writer, indent + 1);
			writer.write(": ");
			writeNest(value, writer, indent + 1);
		}
		writer.write("\n");
		writeIndent(writer, indent);
		writer.write("}");
	}

	/**
	 * Returns the elements as a pretty JSON object.
	 *
	 * @param elements the elements to use
	 * @return a {@link String} containing the elements in pretty JSON format
	 *
	 * @see #writeObject(Map, Writer, int)
	 */
	public static String writeObject(Map<String, ? extends Number> elements) {
		try {
			StringWriter writer = new StringWriter();
			writeObject(elements, writer, 0);
			return writer.toString();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Writes the elements as a pretty JSON object to file.
	 *
	 * @param elements the elements to write
	 * @param path     the file path to use
	 * @throws IOException if an IO error occurs
	 *
	 * @see #writeObject(Map, Writer, int)
	 */
	public static void writeObject(Map<String, ? extends Number> elements, Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path, UTF_8)) {
			writeObject(elements, writer, 0);
		}
	}

	/**
	 * Writes the elements as a pretty JSON object.
	 *
	 * @param elements the elements to write
	 * @param writer   the writer to use
	 * @param indent   the initial indent level; the first bracket is not indented,
	 *                 inner elements are indented by one, and the last bracket is
	 *                 indented at the initial indentation level
	 * @throws IOException if an IO error occurs
	 */
	public static void writeObject(Map<String, ? extends Number> elements, Writer writer, int indent)
			throws IOException {

		writer.write("{");

		Iterator<String> iterate = elements.keySet().iterator();

		if (iterate.hasNext()) {
			var key = iterate.next();
			var value = elements.get(key);

			writer.write("\n");
			writeIndent(writer, indent+1);
			writeQuote(key, writer, indent);
			writer.write(": ");
			writer.write(value.toString());
		}

		while (iterate.hasNext()) {
			var key = iterate.next();
			var value = elements.get(key);

			writer.write(",");
			writer.write("\n");
			writeIndent(writer, indent+1);
			writeQuote(key, writer, indent);
			writer.write(": ");
			writer.write(value.toString());

		}
		writer.write("\n");
		writeIndent(writer, indent);
		writer.write("}");
	}

	/**
	 * Indents and then writes the text element surrounded by {@code " "} quotation
	 * marks.
	 *
	 * @param element the element to write
	 * @param writer  the writer to use
	 * @param indent  the number of times to indent
	 * @throws IOException if an IO error occurs
	 */
	public static void writeQuote(String element, Writer writer, int indent) throws IOException {
		writeIndent(writer, indent);
		writer.write('"');
		writer.write(element);
		writer.write('"');
	}
	
	/**
	 * @param resultStore a
	 * @param writer a
	 * @param indent  a
	 * @throws IOException  a
	 */
	public static void writeSearch(TreeMap<String, ArrayList<QuerySearchResults>> resultStore, Writer writer, int indent) throws IOException{
		writer.write("{");
		writer.write("\n");

		for (String queryRes: resultStore.keySet())
		{

			writeQuote(queryRes.replace("[", "").replace("]", ""), writer, indent+1);
			
			
			writer.write(": [");
			for (QuerySearchResults querySingle: resultStore.get(queryRes))
			{
				writer.write("\n");
				writeIndent(writer, indent+2);
				writer.write("{");
				writer.write("\n");
				writeIndent(writer, indent+3);
				writer.write("\"count\": ");
				
				DecimalFormat CountFormat = new DecimalFormat("0");
				writer.write(CountFormat.format(querySingle.wordGet()));
				
				writer.write(", ");
				writer.write("\n");
				writeIndent(writer, indent+3);
				writer.write("\"score\": ");
				
				
				String formatted = String.format("%.8f", querySingle.freqGet());
						
				//DecimalFormat FORMATTER = new DecimalFormat("0.00000000");
				
				writer.write(formatted);
				
				
				writer.write(", ");
				writer.write("\n");
				writeIndent(writer, indent+3);
				writer.write("\"where\": \"");
				writer.write(querySingle.fileGet());
				writer.write("\"\n");
				writeIndent(writer, indent+2);
				writer.write("}");

				if (!querySingle.equals(resultStore.get(queryRes).get(resultStore.get(queryRes).size() - 1))) {
					writer.write(",");
				}
			}
			writer.write("\n");

			
			if (queryRes != resultStore.lastKey()) {
				//writeIndent(writer, indent-1);
				//writeIndent(writer, indent);
				writeIndent(writer, indent+1);
				writer.write("],");
				writer.write("\n");
			} else {
				//writeIndent(writer, indent-1);
				writeIndent(writer, indent+1);

				writer.write("]\n");
			}
		}
		writer.write("}");
		writer.write("\n");
	}
	
	/**
	 * @param resultStore a 
	 * @param path a
	 * @throws IOException a
	 */
	public static void writeSearch(
			TreeMap<String, ArrayList<QuerySearchResults>> resultStore, Path path)
			throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path, UTF_8)) {
			writeSearch(resultStore, writer, 0);
		}
	}
	
	/**
	 * @param resultStore a
	 * @return a 
	 */
	public static String writeSearch(TreeMap<String, ArrayList<QuerySearchResults>> resultStore)
	{
		try {
			StringWriter writer = new StringWriter();
			writeSearch(resultStore, writer, 0);
			return writer.toString();
		}
		catch (IOException e) {
			return null;
		}
	}
	
}

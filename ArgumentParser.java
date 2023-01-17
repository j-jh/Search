package edu.usfca.cs272;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Parses and stores command-line arguments into simple flag/value pairs.
 */
public class ArgumentParser {
	/**
	 * Stores command-line arguments in flag/value pairs.
	 */
	private final Map<String, String> map;

	/**
	 * Initializes argument map.
	 */
	public ArgumentParser() {
		this.map = new HashMap<>();
	}

	/**
	 * Initializes this argument map and then parsers the arguments into flag/value
	 * pairs where possible. Some flags may not have associated values. If a flag is
	 * repeated, its value is overwritten.
	 * 
	 * @param args the command line arguments to parse
	 */
	public ArgumentParser(String[] args) {
		this();
		parse(args);
	}

	/**
	 * Parses the arguments into flag/value pairs where possible. Some flags may not
	 * have associated values. If a flag is repeated, its value will be overwritten.
	 * 
	 * @param args the command line arguments to parse
	 */
	public void parse(String[] args) {

		for (int i = 0; i < args.length; i++) {
			if (isFlag(args[i])) {
				if (i < args.length - 1 && isValue(args[i + 1])) {
					map.put(args[i], args[i + 1]);
				} else {
					map.put(args[i], null);
				}
			}
		}
	}

	/**
	 * Determines whether the argument is a flag. The argument is considered a flag
	 * if it is a dash "-" character followed by any letter character.
	 * 
	 * @param arg the argument to test if its a flag
	 * @return true/false if the argument is a flag
	 */
	public static boolean isFlag(String arg) {
		return arg != null && arg.length() > 1 && arg.startsWith("-") && Character.isLetter(arg.codePointAt(1));
	}

	/**
	 * Determines whether the argument is a value. Anything that is not a flag is
	 * considered a value.
	 *
	 * @param arg the argument to test if its a value
	 * @return {@code true} if the argument is a value
	 */
	public static boolean isValue(String arg) {
		return !isFlag(arg);
	}

	/**
	 * Returns the number of unique flags.
	 *
	 * @return number of unique flags
	 */
	public int numFlags() {
		return map.size();
	}

	/**
	 * Determines whether the specified flag exists.
	 *
	 * @param flag the flag check
	 * @return {@code true} if the flag exists
	 */
	public boolean hasFlag(String flag) {
		return map.containsKey(flag);
	}

	/**
	 * Determines whether the specified flag is mapped to a non-null value.
	 *
	 * @param flag the flag to find
	 * @return {@code true} if the flag is mapped to a non-null value
	 */
	public boolean hasValue(String flag) {
		return map.get(flag) != null;
	}

	/**
	 * Returns the value to which the specified flag is mapped as a {@link String}
	 * or null if there is no mapping.
	 * 
	 * @param flag the flag whose associated value is to be returned
	 * @return the value to which the specified flag is mapped or {@code null} if
	 *         there is no mapping
	 */
	public String getString(String flag) {
		return map.get(flag);
	}

	/**
	 * Returns the value to which the specified flag is mapped as a {@link String}
	 * or the backup value if there is no mapping.
	 * 
	 * @param flag   the flag whose associated value is to be returned
	 * @param backup the backup value to return if there is no mapping
	 * @return the value to which the specified flag is mapped, or the backup value
	 *         if there is no mapping
	 */
	public String getString(String flag, String backup) {
		if (map.get(flag) == null) {
			return backup;
		}
		return map.get(flag);
	}

	/**
	 * Returns the value the specified flag is mapped as a {@link Path}, or the
	 * backup value if unable to retrieve this mapping (including being unable to
	 * convert the value to a {@link Path} or if no value exists).
	 *
	 * @param flag the flag whose associated value is to be returned
	 * @return the value to which the specified flag is mapped, or {@code null} if
	 *         unable to retrieve this mapping
	 */
	public Path getPath(String flag) {
		return getPath(flag, null);
	}

	/**
	 * Returns the value the specified flag is mapped as a {@link Path}, or the
	 * backup value if unable to retrieve this mapping (including being unable to
	 * convert the value to a {@link Path} or if no value exists).
	 * 
	 * @param flag   the flag whose associated value will be returned
	 * @param backup the backup value to return if there is no valid mapping
	 * @return the value the specified flag is mapped as a {@link Path}, or the
	 *         backup value if there is no valid mapping
	 */
	public Path getPath(String flag, Path backup) {
		try {
			return Path.of(map.get(flag));
		} catch (Exception e) {
			return backup;
		}
	}

	/**
	 * Returns the value the specified flag is mapped as an Integer value, or the
	 * backup value if unable to retrieve this mapping (including being unable to
	 * convert the value to an Integer or if no value exists).
	 *
	 * @param flag   the flag whose associated value will be returned
	 * @param backup the backup value to return if there is no valid mapping
	 * @return the value the specified flag is mapped as a Integer, or the backup
	 *         value if there is no valid mapping
	 */
	public Integer getInteger(String flag, Integer backup) {
		try {
			return Integer.valueOf(map.get(flag));
		} catch (Exception e) {
			return backup;
		}
	}

	/**
	 * Returns the value the specified flag is mapped as an int value, or null
	 * default value if unable to retrieve this mapping (including being unable to
	 * convert the value to an int or if no value exists).
	 * 
	 * @param flag the flag whose associated value will be returned
	 * @return the value the specified flag is mapped as a int, or null if there is
	 *         no valid mapping
	 */
	public Integer getInteger(String flag) {
		return getInteger(flag, null);
	}

	/**
	 * Returns boolean value if the value of the flag is Integer or not
	 * 
	 * @param value the value of the flag
	 * @return {@code true} if it is an Integer
	 */
	public boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	/*
	 * toString implementation to print out map structure
	 */
	public String toString() {
		return this.map.toString();
	}
}

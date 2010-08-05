/*
 * Copyright (c) 2003-2005 The BISON Project
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */

package peersim.util;

import java.util.*;

/**
 * This utility class can be used to parse range expressions. In particular,
 * it is used by {@link peersim.rangesim.RangeSimulator} to express ranges for
 * configuration properties.
 * <p>
 * The language for range expression is the following: 
 * <pre>
 *   [rangelist] := [range] | [range],[rangelist]
 *   [range] := value | min:max | min:max|step | 
 *      min:max*|step
 * </pre>
 * where <tt>value</tt>, <tt>min</tt>, <tt>max</tt> and <tt>step</tt>
 * are numeric atoms that defines ranges.
 * <p>
 * For example, the following range expression:
 * <pre>
 *   5,9:11,13:17|2,32:128*|2
 * </pre>
 * corresponds to 5 (single value), 9-10-11 (range between 9 and 11,
 * default increment 1), 13-15-17 (range between 13 and 17, specified
 * step 2, 32-64-128 (range between 32 and 128, multiplicative step 2).
 * 
 * @author Alberto Montresor
 * @version $Revision$
 */
public class StringListParser
{

/** Disable instance construction */
private StringListParser() { }

/**
 * Parse the specified string.
 * 
 * @param s the string to be parsed
 * @return an array of strings containing all the values defined by the
 *   range string
 */
public static String[] parseList(String s)
{
	ArrayList<String> list = new ArrayList<String>();
	String[] tokens = s.split(",");
	for (int i = 0; i < tokens.length; i++) {
		parseItem(list, tokens[i]);
	}
	return list.toArray(new String[list.size()]);
}

private static void parseItem(List<String> list, String item)
{
	String[] array = item.split(":");
	if (array.length == 1) {
		parseSingleItem(list, item);
	} else if (array.length == 2) {
		parseRangeItem(list, array[0], array[1]);
	} else {
		throw new IllegalArgumentException("Element " + item
				+ "should be formatted as <start>:<stop> or <value>");
	}
}

private static void parseSingleItem(List<String> list, String item)
{
	list.add(item);
}

private static void parseRangeItem(List<String> list, String start, String stop)
{
  try {
		long vstart;
		long vstop;
		long vinc;
		boolean sum;
		vstart = Long.parseLong(start);
		int pos = stop.indexOf("|*");
		if (pos >= 0) {
			// The string contains a multiplicative factor
			vstop = Long.parseLong(stop.substring(0, pos));
			vinc = Long.parseLong(stop.substring(pos + 2));
			sum = false;
		} else {
			pos = stop.indexOf("|");
			if (pos >= 0) {
				// The string contains an additive factor
				vstop = Long.parseLong(stop.substring(0, pos));
				vinc = Long.parseLong(stop.substring(pos + 1));
				sum = true;
			} else {
				// The string contains just the final value
				vstop = Long.parseLong(stop);
				vinc = 1;
				sum = true;
			}
		}
		if (sum) {
			for (long i = vstart; i <= vstop; i += vinc)
				list.add("" + i);
		} else {
			for (long i = vstart; i <= vstop; i *= vinc)
				list.add("" + i);
		}
  } catch (NumberFormatException e) {
		double vstart;
		double vstop;
		double vinc;
		boolean sum;
		vstart = Double.parseDouble(start);
		int pos = stop.indexOf("|*");
		if (pos >= 0) {
			// The string contains a multiplicative factor
			vstop = Double.parseDouble(stop.substring(0, pos));
			vinc = Double.parseDouble(stop.substring(pos + 2));
			sum = false;
		} else {
			pos = stop.indexOf("|");
			if (pos >= 0) {
				// The string contains an additive factor
				vstop = Double.parseDouble(stop.substring(0, pos));
				vinc = Double.parseDouble(stop.substring(pos + 1));
				sum = true;
			} else {
				// The string contains just the final value
				vstop = Double.parseDouble(stop);
				vinc = 1;
				sum = true;
			}
		}
		if (sum) {
			for (double i = vstart; i <= vstop; i += vinc)
				list.add("" + i);
		} else {
			for (double i = vstart; i <= vstop; i *= vinc)
				list.add("" + i);
		}
  }
}

/*
public static void main(String[] args)
{
	String[] ret = parseList(args[0]);
	for (int i = 0; i < ret.length; i++)
		System.out.print(ret[i] + " ");
	System.out.println("");
}
*/
}

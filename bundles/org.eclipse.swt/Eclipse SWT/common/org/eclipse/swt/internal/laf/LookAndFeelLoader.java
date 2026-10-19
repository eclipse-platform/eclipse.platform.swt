/*******************************************************************************
 * Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0, or (at your option) the Eclipse
 * Public License 2.0, which are available at
 * https://www.apache.org/licenses/LICENSE-2.0 and
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.laf;

import java.util.*;
import java.util.function.*;

/**
 * Loads a look and feel's defaults table from properties text and resolves it into packed ints
 * ({@link LookAndFeelDefaults}).
 * <p>
 * Format (one <code>key = value</code> per line; <code>#</code> or <code>!</code> starts a comment line):
 * </p>
 * <ul>
 * <li><code>@extends = name</code>: the table inherits every key of the named table and may override it;</li>
 * <li><code>@name = value</code>: a variable, usable as <code>@name</code> in other values and not a key itself;</li>
 * <li>colours <code>#rgb</code>, <code>#rgba</code>, <code>#rrggbb</code>, <code>#rrggbbaa</code>; integers;
 * <code>true</code> and <code>false</code>; insets <code>top,left,bottom,right</code>;</li>
 * <li><code>$Other.key</code>: the value of another key (resolved after inheritance, so a derived table that overrides
 * <code>Other.key</code> changes every key that refers to it);</li>
 * <li>colour functions: <code>darken(c, n%)</code> and <code>lighten(c, n%)</code> move the HSL lightness by n
 * points, <code>fade(c, n%)</code> sets the alpha to n percent, <code>mix(a, b, n%)</code> takes n percent of
 * <code>a</code> and the rest of <code>b</code>, <code>system(NAME)</code> is a platform colour by the name of an SWT
 * constant (for example <code>COLOR_LIST_SELECTION</code>).</li>
 * </ul>
 * <p>
 * Pattern (no code) from FlatLaf's <code>UIDefaultsLoader</code> and properties themes (Apache-2.0): inheritance,
 * variables, key references and derived colours. The functions follow the Less colour operations FlatLaf adopted.
 * </p>
 */
public final class LookAndFeelLoader {

	/** Platform colours for <code>system(NAME)</code>. */
	public interface SystemColors {
		/**
		 * @param name the name of an SWT colour constant, for example <code>COLOR_WIDGET_BACKGROUND</code>
		 * @return the opaque ARGB colour
		 * @throws IllegalArgumentException when the name is not a colour constant
		 */
		int argb (String name);
	}

	private static final String EXTENDS = "@extends";

	private final Function<String, String> sources;
	private final SystemColors system;

	/**
	 * @param sources the properties text of a look and feel by name, or <code>null</code> when there is none
	 * @param system the platform colours for <code>system(NAME)</code>
	 */
	public LookAndFeelLoader (Function<String, String> sources, SystemColors system) {
		this.sources = sources;
		this.system = system;
	}

	/**
	 * Loads and resolves a look and feel's table with everything it inherits.
	 *
	 * @throws IllegalArgumentException when the table or one it extends is missing, the inheritance or a reference
	 * is circular, or a value cannot be read
	 */
	public LookAndFeelDefaults load (String name) {
		Map<String, String> raw = merged (name, new ArrayList<> ());
		return new Resolver (name, raw).resolve ();
	}

	/* the raw values of a table over those of every table it extends */
	Map<String, String> merged (String name, List<String> chain) {
		if (chain.contains (name)) throw new IllegalArgumentException ("circular @extends: " + String.join (" -> ", chain) + " -> " + name);
		String text = sources.apply (name);
		if (text == null) throw new IllegalArgumentException ("no look and feel named '" + name + "'" + (chain.isEmpty () ? "" : " (extended by " + chain.get (chain.size () - 1) + ")"));
		chain.add (name);
		Map<String, String> own = parse (text, name);
		String parent = own.remove (EXTENDS);
		Map<String, String> result = parent == null ? new LinkedHashMap<> () : merged (parent.trim (), chain);
		result.putAll (own);
		return result;
	}

	/**
	 * LF6 (dependency tracking): the values of some keys of a raw table, resolving only what they reference.
	 *
	 * @param raw the raw values (keys and variables), for example {@link #merged} with some values replaced
	 */
	LookAndFeelDefaults resolve (String name, Map<String, String> raw, Collection<String> keys) {
		return new Resolver (name, raw).pack (keys);
	}

	/**
	 * LF6 (dependency tracking): the values of some keys of a raw table, resolving only what they reference.
	 *
	 * @param raw the raw values (keys and variables), for example {@link #merged} with some values replaced
	 */
	LookAndFeelDefaults resolve (String name, Map<String, String> raw, Collection<String> keys) {
		return new Resolver (name, raw).pack (keys);
	}

	/** The key-value lines of properties text, in order (a later line overrides an earlier one). */
	public static Map<String, String> parse (String text, String source) {
		Map<String, String> result = new LinkedHashMap<> ();
		int lineNumber = 0;
		for (String line : text.split ("\r?\n")) {
			lineNumber++;
			String trimmed = line.trim ();
			if (trimmed.isEmpty () || trimmed.startsWith ("#") || trimmed.startsWith ("!")) continue;
			int equals = trimmed.indexOf ('=');
			if (equals <= 0) throw new IllegalArgumentException (source + ":" + lineNumber + ": expected key = value: " + trimmed);
			result.put (trimmed.substring (0, equals).trim (), trimmed.substring (equals + 1).trim ());
		}
		return result;
	}

	/* one value during resolution */
	private static final class Value {
		final byte kind;
		final int value;
		final int [] insets;

		Value (byte kind, int value, int [] insets) {
			this.kind = kind;
			this.value = value;
			this.insets = insets;
		}
	}

	private final class Resolver {
		final String name;
		final Map<String, String> raw;
		final Map<String, Value> resolved = new HashMap<> ();
		final Deque<String> resolving = new ArrayDeque<> ();

		Resolver (String name, Map<String, String> raw) {
			this.name = name;
			this.raw = raw;
		}

		LookAndFeelDefaults resolve () {
			List<String> wanted = new ArrayList<> ();
			for (String key : raw.keySet ()) {
				if (!key.startsWith ("@")) wanted.add (key);
			}
			return pack (wanted);
		}

		LookAndFeelDefaults pack (Collection<String> wanted) {
			TreeMap<String, Value> keys = new TreeMap<> ();
			for (String key : wanted) keys.put (key, value (key));
			int size = keys.size (), insetCount = 0;
			for (Value value : keys.values ()) {
				if (value.kind == LookAndFeelDefaults.INSETS) insetCount++;
			}
			String [] names = new String [size];
			byte [] kinds = new byte [size];
			int [] values = new int [size], insets = new int [insetCount * 4];
			int i = 0, at = 0;
			for (Map.Entry<String, Value> entry : keys.entrySet ()) {
				Value value = entry.getValue ();
				names [i] = entry.getKey ();
				kinds [i] = value.kind;
				if (value.kind == LookAndFeelDefaults.INSETS) {
					System.arraycopy (value.insets, 0, insets, at, 4);
					values [i] = at;
					at += 4;
				} else {
					values [i] = value.value;
				}
				i++;
			}
			return new LookAndFeelDefaults (name, names, kinds, values, insets);
		}

		Value value (String key) {
			Value done = resolved.get (key);
			if (done != null) return done;
			if (resolving.contains (key)) {
				List<String> cycle = new ArrayList<> (resolving);
				Collections.reverse (cycle);
				throw new IllegalArgumentException (name + ": circular reference " + String.join (" -> ", cycle) + " -> " + key);
			}
			String text = raw.get (key);
			if (text == null) throw new IllegalArgumentException (name + ": " + (resolving.isEmpty () ? "" : resolving.peek () + ": ") + "unknown " + (key.startsWith ("@") ? "variable " : "key ") + key);
			resolving.push (key);
			try {
				Value value = expression (text.trim ());
				resolved.put (key, value);
				return value;
			} catch (IllegalArgumentException e) {
				if (e.getMessage () != null && e.getMessage ().startsWith (name + ": ")) throw e;
				throw new IllegalArgumentException (name + ": " + key + " = " + text + ": " + e.getMessage (), e);
			} finally {
				resolving.pop ();
			}
		}

		Value expression (String text) {
			if (text.isEmpty ()) throw new IllegalArgumentException ("empty value");
			if (text.startsWith ("$")) return value (text.substring (1).trim ());
			if (text.startsWith ("@")) return value (text);
			if (text.startsWith ("#")) return new Value (LookAndFeelDefaults.COLOR, color (text), null);
			if (text.equals ("true") || text.equals ("false")) return new Value (LookAndFeelDefaults.BOOLEAN, text.equals ("true") ? 1 : 0, null);
			int open = text.indexOf ('(');
			if (open > 0 && text.endsWith (")")) return function (text.substring (0, open).trim (), arguments (text.substring (open + 1, text.length () - 1)));
			List<String> parts = arguments (text);
			if (parts.size () == 4) {
				int [] insets = new int [4];
				for (int i = 0; i < 4; i++) insets [i] = integer (parts.get (i));
				return new Value (LookAndFeelDefaults.INSETS, 0, insets);
			}
			return new Value (LookAndFeelDefaults.INTEGER, integer (text), null);
		}

		Value function (String function, List<String> args) {
			switch (function) {
				case "darken":
				case "lighten": {
					expect (function, args, 2);
					float [] hsl = hsl (colorOf (args.get (0)));
					float amount = percent (args.get (1));
					hsl [2] = clamp (hsl [2] + (function.equals ("darken") ? -amount : amount));
					return new Value (LookAndFeelDefaults.COLOR, argb (hsl, alpha (colorOf (args.get (0)))), null);
				}
				case "fade": {
					expect (function, args, 2);
					int color = colorOf (args.get (0));
					int alpha = Math.round (clamp (percent (args.get (1))) * 255f / 100f);
					return new Value (LookAndFeelDefaults.COLOR, (alpha << 24) | (color & 0xFFFFFF), null);
				}
				case "mix": {
					expect (function, args, 3);
					int a = colorOf (args.get (0)), b = colorOf (args.get (1));
					float weight = clamp (percent (args.get (2))) / 100f;
					int result = 0;
					for (int shift = 0; shift < 32; shift += 8) {
						int channel = Math.round (((a >>> shift) & 0xFF) * weight + ((b >>> shift) & 0xFF) * (1 - weight));
						result |= (channel & 0xFF) << shift;
					}
					return new Value (LookAndFeelDefaults.COLOR, result, null);
				}
				case "system": {
					expect (function, args, 1);
					return new Value (LookAndFeelDefaults.COLOR, 0xFF000000 | system.argb (args.get (0).trim ()), null);
				}
				default:
					throw new IllegalArgumentException ("unknown function " + function);
			}
		}

		int colorOf (String text) {
			Value value = expression (text.trim ());
			if (value.kind != LookAndFeelDefaults.COLOR) throw new IllegalArgumentException ("not a colour: " + text);
			return value.value;
		}
	}

	/* comma-separated arguments at the top level of parentheses */
	static List<String> arguments (String text) {
		List<String> result = new ArrayList<> ();
		int depth = 0, start = 0;
		for (int i = 0; i < text.length (); i++) {
			char c = text.charAt (i);
			if (c == '(') depth++;
			else if (c == ')') depth--;
			else if (c == ',' && depth == 0) {
				result.add (text.substring (start, i).trim ());
				start = i + 1;
			}
		}
		result.add (text.substring (start).trim ());
		return result;
	}

	static void expect (String function, List<String> args, int count) {
		if (args.size () != count) throw new IllegalArgumentException (function + " takes " + count + " arguments, not " + args.size ());
	}

	/** An ARGB colour from <code>#rgb</code>, <code>#rgba</code>, <code>#rrggbb</code> or <code>#rrggbbaa</code>. */
	static int color (String text) {
		String hex = text.substring (1);
		if (hex.length () == 3 || hex.length () == 4) {
			StringBuilder wide = new StringBuilder ();
			for (char c : hex.toCharArray ()) wide.append (c).append (c);
			hex = wide.toString ();
		}
		if (hex.length () != 6 && hex.length () != 8) throw new IllegalArgumentException ("not a colour: " + text);
		long rgba;
		try {
			rgba = Long.parseLong (hex, 16);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException ("not a colour: " + text);
		}
		if (hex.length () == 6) return 0xFF000000 | (int) rgba;
		return (int) (((rgba & 0xFF) << 24) | (rgba >>> 8));
	}

	static int integer (String text) {
		try {
			return Integer.parseInt (text.trim ());
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException ("not a value: " + text);
		}
	}

	static float percent (String text) {
		String number = text.trim ();
		if (number.endsWith ("%")) number = number.substring (0, number.length () - 1).trim ();
		try {
			return Float.parseFloat (number);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException ("not a percentage: " + text);
		}
	}

	static float clamp (float percent) {
		return Math.max (0, Math.min (100, percent));
	}

	static int alpha (int argb) {
		return argb >>> 24;
	}

	/** Hue (0-360), saturation and lightness (0-100) of an ARGB colour. */
	static float [] hsl (int argb) {
		float r = ((argb >> 16) & 0xFF) / 255f, g = ((argb >> 8) & 0xFF) / 255f, b = (argb & 0xFF) / 255f;
		float max = Math.max (r, Math.max (g, b)), min = Math.min (r, Math.min (g, b));
		float h = 0, s = 0, l = (max + min) / 2;
		if (max != min) {
			float d = max - min;
			s = l > 0.5f ? d / (2 - max - min) : d / (max + min);
			if (max == r) h = (g - b) / d + (g < b ? 6 : 0);
			else if (max == g) h = (b - r) / d + 2;
			else h = (r - g) / d + 4;
			h *= 60;
		}
		return new float [] {h, s * 100, l * 100};
	}

	/** An ARGB colour from hue (0-360), saturation and lightness (0-100) and an alpha (0-255). */
	static int argb (float [] hsl, int alpha) {
		float h = hsl [0] / 360f, s = hsl [1] / 100f, l = hsl [2] / 100f;
		float r, g, b;
		if (s == 0) {
			r = g = b = l;
		} else {
			float q = l < 0.5f ? l * (1 + s) : l + s - l * s, p = 2 * l - q;
			r = hue (p, q, h + 1f / 3);
			g = hue (p, q, h);
			b = hue (p, q, h - 1f / 3);
		}
		return (alpha << 24) | (Math.round (r * 255) << 16) | (Math.round (g * 255) << 8) | Math.round (b * 255);
	}

	private static float hue (float p, float q, float t) {
		if (t < 0) t += 1;
		if (t > 1) t -= 1;
		if (t < 1f / 6) return p + (q - p) * 6 * t;
		if (t < 1f / 2) return q;
		if (t < 2f / 3) return p + (q - p) * (2f / 3 - t) * 6;
		return p;
	}
}

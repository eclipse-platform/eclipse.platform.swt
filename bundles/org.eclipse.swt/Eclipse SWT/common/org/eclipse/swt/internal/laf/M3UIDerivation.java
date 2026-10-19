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
import java.util.regex.*;

/**
 * The dependencies of a look and feel's derived values (docs/m3/LOOK_AND_FEEL.md, LF6, improvement 2), Swing's
 * <code>LazyValue</code> and <code>ActiveValue</code> resolved once and kept current: every key and variable of the
 * merged table is a node, a value that refers to another (<code>$Key</code>, <code>@variable</code>) is an edge, kept
 * reversed in CSR arrays (who depends on a node). When the developer layer sets a base (a key, or a variable such as
 * <code>@accent</code>), {@link #derive} resolves only the values that depend on it, transitively, against the
 * developer's bases; every other value keeps its resolution.
 */
final class M3UIDerivation {
	private static final Pattern REFERENCE = Pattern.compile ("[$@][A-Za-z0-9_.\\-]+");

	private final String name;
	private final LookAndFeelLoader loader;
	private final Map<String, String> raw;
	private final String [] nodes;
	private final int [] start, dependents;
	private int lastCone;

	private M3UIDerivation (String name, LookAndFeelLoader loader, Map<String, String> raw) {
		this.name = name;
		this.loader = loader;
		this.raw = raw;
		this.nodes = raw.keySet ().toArray (new String [0]);
		Map<String, Integer> index = new HashMap<> ();
		for (int i = 0; i < nodes.length; i++) index.put (nodes [i], i);
		/* edges node -> referenced node, then reversed into CSR: count, prefix sums, fill */
		int [] count = new int [nodes.length + 1];
		List<int []> edges = new ArrayList<> ();
		for (int i = 0; i < nodes.length; i++) {
			Matcher matcher = REFERENCE.matcher (raw.get (nodes [i]));
			while (matcher.find ()) {
				String reference = matcher.group ();
				Integer target = index.get (reference.charAt (0) == '$' ? reference.substring (1) : reference);
				if (target == null) continue;
				edges.add (new int [] {target, i});
				count [target + 1]++;
			}
		}
		for (int i = 0; i < nodes.length; i++) count [i + 1] += count [i];
		start = count.clone ();
		dependents = new int [edges.size ()];
		for (int [] edge : edges) dependents [count [edge [0]]++] = edge [1];
	}

	/** The dependencies of a look and feel's merged table. */
	static M3UIDerivation of (String name, LookAndFeelLoader loader) {
		return new M3UIDerivation (name, loader, loader.merged (name, new ArrayList<> ()));
	}

	/** The number of nodes (keys and variables). */
	int nodes () {
		return nodes.length;
	}

	/** The number of values the last {@link #derive} resolved. */
	int lastCone () {
		return lastCone;
	}

	/**
	 * The values that depend on the developer layer's bases, resolved against them, or <code>null</code> when no value
	 * depends on a base.
	 *
	 * @throws IllegalArgumentException when a developer variable cannot be read where it is used
	 */
	LookAndFeelDefaults derive (M3UIDefaults.Lanes developer) {
		boolean [] root = new boolean [nodes.length], cone = new boolean [nodes.length];
		int [] queue = new int [nodes.length];
		int tail = 0;
		for (int i = 0; i < nodes.length; i++) {
			if (base (developer, nodes [i]) != null) {
				root [i] = true;
				queue [tail++] = i;
			}
		}
		List<String> keys = new ArrayList<> ();
		for (int head = 0; head < tail; head++) {
			int node = queue [head];
			for (int e = start [node]; e < start [node + 1]; e++) {
				int dependent = dependents [e];
				if (cone [dependent] || root [dependent]) continue;
				cone [dependent] = true;
				queue [tail++] = dependent;
				if (!nodes [dependent].startsWith ("@")) keys.add (nodes [dependent]);
			}
		}
		lastCone = keys.size ();
		if (keys.isEmpty ()) return null;
		Map<String, String> bases = new HashMap<> (raw);
		for (int i = 0; i < nodes.length; i++) {
			if (root [i]) bases.put (nodes [i], base (developer, nodes [i]));
		}
		return loader.resolve (name, bases, keys);
	}

	/* the developer's value of a node as table text, or null when the developer layer has none */
	private static String base (M3UIDefaults.Lanes developer, String node) {
		if (node.startsWith ("@")) return developer.variable (node);
		int key = M3UIKeys.id (node);
		if (key < 0 || !developer.has (key)) return null;
		int value = developer.value (key);
		switch (M3UIKeys.kind (key)) {
			case M3UIKeys.COLOR: return String.format ("#%06x%02x", value & 0xFFFFFF, value >>> 24);
			case M3UIKeys.BOOLEAN: return value != 0 ? "true" : "false";
			case M3UIKeys.INSETS: {
				int [] quad = new int [4];
				developer.quad (key, quad);
				return quad [0] + "," + quad [1] + "," + quad [2] + "," + quad [3];
			}
			default: return Integer.toString (value);
		}
	}
}

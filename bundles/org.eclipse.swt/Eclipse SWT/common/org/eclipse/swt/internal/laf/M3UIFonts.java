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

/**
 * Font descriptions as ints (docs/m3/LOOK_AND_FEEL.md, LF6): a font of the defaults ({@link M3UIKeys#FONT}) is the id
 * of a name, a height in points and an SWT style, interned once into lanes. {@link M3UIResources} turns an id into an
 * SWT <code>Font</code> per display.
 */
public final class M3UIFonts {
	private static final Object LOCK = new Object ();
	private static final Map<String, Integer> IDS = new HashMap<> ();
	private static volatile String [] names = new String [16];
	private static volatile int [] heights = new int [16], styles = new int [16];
	private static volatile int count;

	private M3UIFonts () {
	}

	/**
	 * The id of a font description, interned on first use.
	 *
	 * @param name the face name, for example <code>Segoe UI</code>
	 * @param height the height in points
	 * @param style <code>SWT.NORMAL</code>, <code>SWT.BOLD</code>, <code>SWT.ITALIC</code> or both
	 */
	public static int intern (String name, int height, int style) {
		String key = name + '\u0000' + height + '\u0000' + style;
		synchronized (LOCK) {
			Integer id = IDS.get (key);
			if (id != null) return id;
			int next = count;
			if (next == names.length) {
				int length = next * 2;
				heights = Arrays.copyOf (heights, length);
				styles = Arrays.copyOf (styles, length);
				names = Arrays.copyOf (names, length);
			}
			names [next] = name;
			heights [next] = height;
			styles [next] = style;
			count = next + 1;
			IDS.put (key, next);
			return next;
		}
	}

	/** The number of interned fonts. */
	public static int count () {
		return count;
	}

	/** The face name of a font id. */
	public static String name (int id) {
		return names [id];
	}

	/** The height in points of a font id. */
	public static int height (int id) {
		return heights [id];
	}

	/** The SWT style of a font id. */
	public static int style (int id) {
		return styles [id];
	}
}

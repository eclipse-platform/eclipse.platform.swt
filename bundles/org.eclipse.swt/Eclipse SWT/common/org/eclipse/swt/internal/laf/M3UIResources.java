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

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * The SWT colours and fonts of the drawn widgets of one display (docs/m3/LOOK_AND_FEEL.md, LF6): the defaults hold
 * ARGB ints and font ids; a delegate asks here at paint time and gets one shared <code>Color</code> per ARGB value
 * (open addressing over the ints) and one <code>Font</code> per font id. A look and feel switch disposes them all
 * ({@link M3LookAndFeel#setLookAndFeel}), as Swing's look and feels drop their <code>UIResource</code>s, so a delegate
 * never keeps one across paints. Disposed with the display.
 */
public final class M3UIResources {
	private final Display display;
	private int [] keys = new int [64];
	private Color [] colours = new Color [64];
	private int colourCount;
	private Font [] fonts = new Font [16];
	private int fontCount, disposals;

	private M3UIResources (Display display) {
		this.display = display;
	}

	/** The resources of a display, created on first use (kept in the display's data, disposed with it). */
	public static M3UIResources of (Display display) {
		String key = M3UIResources.class.getName ();
		M3UIResources resources = (M3UIResources) display.getData (key);
		if (resources == null) {
			M3UIResources created = resources = new M3UIResources (display);
			display.setData (key, created);
			display.disposeExec (created::dispose);
		}
		return resources;
	}

	/** The colour of an ARGB value (alpha kept). */
	public Color color (int argb) {
		int mask = keys.length - 1;
		int at = mix (argb) & mask;
		while (colours [at] != null) {
			if (keys [at] == argb) return colours [at];
			at = (at + 1) & mask;
		}
		Color colour = new Color (display, (argb >>> 16) & 0xFF, (argb >>> 8) & 0xFF, argb & 0xFF, argb >>> 24);
		keys [at] = argb;
		colours [at] = colour;
		if (++colourCount * 2 > keys.length) grow ();
		return colour;
	}

	/** The font of a font id ({@link M3UIFonts}). */
	public Font font (int id) {
		if (id >= fonts.length) fonts = java.util.Arrays.copyOf (fonts, Math.max (fonts.length * 2, id + 1));
		Font font = fonts [id];
		if (font == null) {
			fonts [id] = font = new Font (display, M3UIFonts.name (id), M3UIFonts.height (id), M3UIFonts.style (id));
			fontCount++;
		}
		return font;
	}

	/** The number of live colours. */
	public int colours () {
		return colourCount;
	}

	/** The number of live fonts. */
	public int fonts () {
		return fontCount;
	}

	/** How many times the resources were disposed (by switches and the display). */
	public int disposals () {
		return disposals;
	}

	/** Disposes every colour and font; later requests create them again. */
	public void dispose () {
		for (int i = 0; i < colours.length; i++) {
			if (colours [i] != null) colours [i].dispose ();
		}
		for (Font font : fonts) {
			if (font != null) font.dispose ();
		}
		keys = new int [64];
		colours = new Color [64];
		fonts = new Font [16];
		colourCount = fontCount = 0;
		disposals++;
	}

	private void grow () {
		int [] oldKeys = keys;
		Color [] oldColours = colours;
		keys = new int [oldKeys.length * 2];
		colours = new Color [oldKeys.length * 2];
		int mask = keys.length - 1;
		for (int i = 0; i < oldKeys.length; i++) {
			if (oldColours [i] == null) continue;
			int at = mix (oldKeys [i]) & mask;
			while (colours [at] != null) at = (at + 1) & mask;
			keys [at] = oldKeys [i];
			colours [at] = oldColours [i];
		}
	}

	private static int mix (int value) {
		value ^= value >>> 16;
		value *= 0x85ebca6b;
		return value ^ value >>> 13;
	}
}

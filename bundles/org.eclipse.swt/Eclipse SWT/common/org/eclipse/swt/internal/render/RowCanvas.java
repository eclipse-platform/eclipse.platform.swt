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
package org.eclipse.swt.internal.render;

import org.eclipse.swt.graphics.*;

/**
 * The backend of a drawn plane: the few primitives a row renderer needs, in device pixels of the plane.
 * One implementation per drawn mode (GC, M3 raster, graphics hardware). A frame is
 * {@link #begin}, any number of primitives inside the frame's rectangle, {@link #end}.
 * <p>
 * Colours are <code>0xRRGGBB</code> ints. Fonts are native font handles of the widget's platform (on
 * win32 an <code>HFONT</code>), so a backend can cache derived fonts by handle.
 * </p>
 */
public interface RowCanvas {
	/** Theme part: row background of a selected, hot or drop-target row. */
	int PART_SELECTION = 1;
	/** Theme part: the expander of a row with children. */
	int PART_EXPANDER = 2;
	/** Theme part: a check box. */
	int PART_CHECKBOX = 3;

	/** Selection part states. */
	int SELECTION_SELECTED = 1, SELECTION_NO_FOCUS = 2, SELECTION_HOT = 3, SELECTION_HOT_SELECTED = 4;
	/** Expander part states; {@link #HOT} may be or-ed. */
	int EXPANDER_CLOSED = 1, EXPANDER_OPEN = 2;
	/** Check box part states; {@link #HOT} may be or-ed. */
	int CHECKBOX_UNCHECKED = 1, CHECKBOX_CHECKED = 2, CHECKBOX_MIXED = 3;
	/** Hot (pointer over) modifier of a part state. */
	int HOT = 0x100;

	/** Starts a frame that paints the rectangle; nothing outside it changes. */
	void begin (int x, int y, int width, int height);

	/** Ends the frame; the pixels are in the target when this returns. */
	void end ();

	/** Fills a rectangle with a colour. */
	void fill (int x, int y, int width, int height, int rgb);

	/**
	 * Draws one line of text inside a box: vertically centred, aligned horizontally (<code>SWT.LEFT</code>,
	 * <code>SWT.CENTER</code>, <code>SWT.RIGHT</code>), clipped to the box, without mnemonic processing.
	 */
	void text (String text, long font, int rgb, int x, int y, int width, int height, int align);

	/** Draws an image scaled into a box (the widget's image size at the image's place). */
	void image (Image image, int x, int y, int width, int height);

	/** Fills a polygon of <code>count</code> points (<code>x, y</code> pairs) with a colour. */
	void polygon (int [] points, int count, int rgb);

	/** Draws a theme part (one of the <code>PART_*</code> constants) in a box. */
	void part (int part, int state, int x, int y, int width, int height);

	/** Draws a focus rectangle. */
	void focus (int x, int y, int width, int height);

	/** Releases the backend's resources (caches, device objects). */
	void dispose ();
}

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

import java.util.*;

import org.eclipse.swt.graphics.*;

/**
 * The look and feel of the drawn planes (<code>custom</code>, <code>m3</code>, <code>gpu</code>): every visual of a drawn
 * row comes from a theme. A theme is an immutable value: colours, the look of selection, expanders and check boxes,
 * metrics, optional glyph images, and optional painters that replace the row or cell painting. Built-in themes are
 * {@link #NATIVE} (the default), {@link #SWING} and {@link #INITIATIVE31}; applications make their own with
 * {@link #builder(String)} and register them with {@link Themes}.
 * <p>
 * Selection: <code>-Dswt.widget.theme=&lt;name&gt;</code> (all drawn widgets) or
 * <code>-Dswt.widget.theme.&lt;Widget&gt;=&lt;name&gt;</code> at startup (<code>swt.lookAndFeel</code> is an alias), and
 * {@link Themes#select(String, Theme)} at run time; {@link #of(String)} answers the theme in effect for a widget class.
 * </p>
 * <p>
 * The built-in themes keep the platform's geometry (row height, indent, label positions), so a drawn widget answers
 * its API exactly as the stock widget in every built-in theme. The metric fields of a theme are -1 for "the
 * platform's".
 * </p>
 */
public final class Theme {
	/** How selection, expanders and check boxes are drawn. */
	public static final int LOOK_NATIVE = 0, LOOK_SWING = 1, LOOK_INITIATIVE31 = 2;

	/** The platform look: theme parts and the widget's colours. */
	public static final Theme NATIVE = builder ("native").look (LOOK_NATIVE).selection (0xCCE8FF, 0xD9D9D9).hot (0xE5F3FF)
		.lines (0xA0A0A0, 0xE3E3E3).border (0x000000).focus (0x808080).build ();
	/**
	 * Swing Metal (Ocean), values measured from the JDK's Swing (docs/m3/render-modes/evidence/swing-theme.output.txt):
	 * Tree.selectionBackground B8CFE5, Tree.foreground 333333, Tree.hash B8CFE5, Table.gridColor 7A8A99, borders 6382BF,
	 * focus A3B8CC.
	 */
	public static final Theme SWING = builder ("swing").look (LOOK_SWING).colors (0x333333, 0xFFFFFF).selection (0xB8CFE5, 0xDDE6F0)
		.hot (0xE6EEF6).lines (0xB8CFE5, 0x7A8A99).border (0x6382BF).focus (0xA3B8CC).check (0x333333, 0x333333).build ();
	/**
	 * Initiative 31 (swt-initiative31/prototype-skija renderers, EPL-2.0): Table.SELECTION_COLOR E0EEFE, hover COLOR_YELLOW,
	 * expander COLOR_DARK_GRAY triangles, check box border gray128 and fill hsb (208, 1, 0.72) = 0062B8 (grayed gray192),
	 * grid gray227.
	 */
	public static final Theme INITIATIVE31 = builder ("initiative31").look (LOOK_INITIATIVE31).colors (0x000000, 0xFFFFFF)
		.selection (0xE0EEFE, 0xE0EEFE).hot (0xFFFF00).lines (0x808080, 0xE3E3E3).border (0x808080).focus (0x000000)
		.check (0x0062B8, 0xC0C0C0).build ();

	/** Paints a whole row instead of the default order (background, selection, expander, check box, cells, lines, focus). */
	public interface RowPainter {
		void paint (RowCanvas canvas, RowPaint row, Theme theme, RowRenderer defaults);
	}

	/** Paints the content (image and text) of one cell instead of the default. */
	public interface CellPainter {
		void paint (RowCanvas canvas, RowPaint row, int cell, Theme theme);
	}

	/** Theme name (the property value that selects it). */
	public final String name;
	/** One of the <code>LOOK_*</code> constants. */
	public final int look;
	/** True when the platform's theme parts draw selection, expanders and check boxes. */
	public final boolean parts;
	/** Text and background colour, or -1 for the widget's own. */
	public final int foreground, background;
	/** Selected row background (focused and not focused), selected text (-1: the cell's), and hot row background. */
	public final int selectionBackground, selectionInactiveBackground, selectionForeground, hotBackground;
	/** Tree lines or expander colour, grid lines, handle and check box border, focus colour. */
	public final int hash, grid, border, focusColor;
	/** Check box fill when checked and when grayed. */
	public final int checkFill, grayFill;
	/** Metrics: the text inset in a cell; row height and indent, -1 for the platform's. */
	public final int textInset, rowHeight, indent;
	/** Optional glyph images (expander open and closed, check box checked, unchecked and mixed); null for the look's own. */
	public final ImageData expanderOpen, expanderClosed, checkOn, checkOff, checkMixed;
	/** Optional painters; null for the default painting. */
	public final RowPainter rowPainter;
	public final CellPainter cellPainter;

	private Theme (Builder b) {
		name = b.name;
		look = b.look;
		parts = b.look == LOOK_NATIVE;
		foreground = b.foreground;
		background = b.background;
		selectionBackground = b.selectionBackground;
		selectionInactiveBackground = b.selectionInactiveBackground;
		selectionForeground = b.selectionForeground;
		hotBackground = b.hotBackground;
		hash = b.hash;
		grid = b.grid;
		border = b.border;
		focusColor = b.focusColor;
		checkFill = b.checkFill;
		grayFill = b.grayFill;
		textInset = b.textInset;
		rowHeight = b.rowHeight;
		indent = b.indent;
		expanderOpen = b.expanderOpen;
		expanderClosed = b.expanderClosed;
		checkOn = b.checkOn;
		checkOff = b.checkOff;
		checkMixed = b.checkMixed;
		rowPainter = b.rowPainter;
		cellPainter = b.cellPainter;
	}

	/** The theme in effect for a widget class (see {@link Themes#current(String)}). */
	public static Theme of (String widget) {
		return Themes.current (widget);
	}

	/** A builder for a new theme; every value starts as the native theme's. */
	public static Builder builder (String name) {
		return new Builder (name);
	}

	/** A builder that starts from this theme's values, under another name. */
	public Builder derive (String newName) {
		Builder b = new Builder (newName).look (look).colors (foreground, background).selection (selectionBackground, selectionInactiveBackground)
			.selectionForeground (selectionForeground).hot (hotBackground).lines (hash, grid).border (border).focus (focusColor)
			.check (checkFill, grayFill).metrics (textInset, rowHeight, indent).expanderImages (expanderOpen, expanderClosed)
			.checkImages (checkOn, checkOff, checkMixed).rowPainter (rowPainter).cellPainter (cellPainter);
		return b;
	}

	@Override
	public String toString () {
		return "Theme " + name;
	}

	/** Collects the values of a theme; {@link #build()} makes the immutable theme. */
	public static final class Builder {
		final String name;
		int look = LOOK_NATIVE, foreground = -1, background = -1, selectionBackground = 0xCCE8FF, selectionInactiveBackground = 0xD9D9D9,
			selectionForeground = -1, hotBackground = 0xE5F3FF, hash = 0xA0A0A0, grid = 0xE3E3E3, border = 0x000000, focusColor = 0x808080,
			checkFill = -1, grayFill = -1, textInset = 2, rowHeight = -1, indent = -1;
		ImageData expanderOpen, expanderClosed, checkOn, checkOff, checkMixed;
		RowPainter rowPainter;
		CellPainter cellPainter;

		Builder (String name) {
			this.name = Objects.requireNonNull (name, "name").trim ().toLowerCase (Locale.ROOT);
			if (this.name.isEmpty ()) throw new IllegalArgumentException ("empty theme name");
		}

		/** One of the <code>LOOK_*</code> constants. */
		public Builder look (int value) {
			if (value < LOOK_NATIVE || value > LOOK_INITIATIVE31) throw new IllegalArgumentException ("look " + value);
			look = value;
			return this;
		}

		/** Text and background colours (<code>0xRRGGBB</code>, -1 for the widget's own). */
		public Builder colors (int textColor, int backgroundColor) {
			foreground = textColor;
			background = backgroundColor;
			return this;
		}

		/** Selected row background with and without focus. */
		public Builder selection (int focused, int unfocused) {
			selectionBackground = focused;
			selectionInactiveBackground = unfocused;
			return this;
		}

		/** Selected text colour, -1 for the cell's own. */
		public Builder selectionForeground (int value) {
			selectionForeground = value;
			return this;
		}

		/** Hot (pointer over) row background. */
		public Builder hot (int value) {
			hotBackground = value;
			return this;
		}

		/** Tree lines or expander colour, and grid line colour. */
		public Builder lines (int treeLines, int gridLines) {
			hash = treeLines;
			grid = gridLines;
			return this;
		}

		/** Handle and check box border colour. */
		public Builder border (int value) {
			border = value;
			return this;
		}

		/** Focus rectangle colour (looks that draw their own focus). */
		public Builder focus (int value) {
			focusColor = value;
			return this;
		}

		/** Check box fill when checked and when grayed. */
		public Builder check (int checked, int grayed) {
			checkFill = checked;
			grayFill = grayed;
			return this;
		}

		/** Text inset in a cell, row height and indent (-1 for the platform's). */
		public Builder metrics (int inset, int height, int indentation) {
			if (inset < 0) throw new IllegalArgumentException ("text inset " + inset);
			textInset = inset;
			rowHeight = height;
			indent = indentation;
			return this;
		}

		/** Images of the expander when open and closed (both or neither). */
		public Builder expanderImages (ImageData open, ImageData closed) {
			if ((open == null) != (closed == null)) throw new IllegalArgumentException ("both expander images or neither");
			expanderOpen = open;
			expanderClosed = closed;
			return this;
		}

		/** Images of the check box checked, unchecked and mixed (all or none). */
		public Builder checkImages (ImageData on, ImageData off, ImageData mixed) {
			if ((on == null) != (off == null) || (on == null) != (mixed == null)) throw new IllegalArgumentException ("all check images or none");
			checkOn = on;
			checkOff = off;
			checkMixed = mixed;
			return this;
		}

		/** A painter for whole rows, null for the default. */
		public Builder rowPainter (RowPainter painter) {
			rowPainter = painter;
			return this;
		}

		/** A painter for cell contents, null for the default. */
		public Builder cellPainter (CellPainter painter) {
			cellPainter = painter;
			return this;
		}

		/** The immutable theme. */
		public Theme build () {
			return new Theme (this);
		}
	}
}

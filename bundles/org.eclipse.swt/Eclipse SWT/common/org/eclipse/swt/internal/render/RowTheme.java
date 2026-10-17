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

import org.eclipse.swt.internal.*;

/**
 * The look and feel of the drawn planes: a defaults table resolved once into ints, chosen at startup like
 * Swing's <code>UIManager.setLookAndFeel</code>.
 * <ul>
 * <li><code>-Dswt.widget.theme=native|swing|initiative31</code> for every drawn widget,
 * <code>-Dswt.widget.theme.&lt;Widget&gt;</code> for one widget class; <code>swt.lookAndFeel</code> is read as an
 * alias.</li>
 * <li>{@link #NATIVE} (default) draws the platform's theme parts (selection, expander, check box) and the
 * widget's own colours.</li>
 * <li>{@link #SWING} is Swing's Metal (Ocean) look; its values were measured from the JDK's Swing
 * (docs/m3/render-modes/evidence/swing-theme.output.txt).</li>
 * <li>{@link #INITIATIVE31} is the look of the Initiative 31 custom-drawn widgets (swt-initiative31/prototype-skija
 * <code>TreeItemRenderer</code>, <code>DefaultCheckboxRenderer</code>, <code>DefaultColorProvider</code>,
 * EPL-2.0): pale blue selection, yellow hover, grey triangle expanders, rounded check boxes filled when checked.</li>
 * </ul>
 * A theme changes pixels only, never geometry, answers or events.
 */
public final class RowTheme {
	/** Name of the global property; a widget property appends <code>"." + simpleClassName</code>. */
	public static final String PROPERTY = "swt.widget.theme";
	private static final String ALIAS = "swt.lookAndFeel";
	private static final SwtLog LOG = SwtLog.getLog ("org.eclipse.swt.internal.render.RowTheme");

	/** How expanders, check boxes and selection are drawn. */
	public static final int LOOK_NATIVE = 0, LOOK_SWING = 1, LOOK_INITIATIVE31 = 2;

	/** The platform look: theme parts and the widget's colours. */
	public static final RowTheme NATIVE = new RowTheme ("native", LOOK_NATIVE, -1, -1, 0xCCE8FF, 0xD9D9D9, 0xE5F3FF, 0xA0A0A0, 0xE3E3E3, 0x000000, 0x808080, -1, -1);
	/** Swing Metal (Ocean): Tree.selectionBackground B8CFE5, Tree.foreground 333333, Tree.hash B8CFE5, Table.gridColor 7A8A99, borders 6382BF, focus A3B8CC. */
	public static final RowTheme SWING = new RowTheme ("swing", LOOK_SWING, 0x333333, 0xFFFFFF, 0xB8CFE5, 0xDDE6F0, 0xE6EEF6, 0xB8CFE5, 0x7A8A99, 0x6382BF, 0xA3B8CC, 0x333333, 0x333333);
	/**
	 * Initiative 31: Table.SELECTION_COLOR E0EEFE, hover COLOR_YELLOW, expander COLOR_DARK_GRAY triangles, check box
	 * border gray128 and fill hsb (208, 1, 0.72) = 0062B8 (grayed gray192), grid gray227.
	 */
	public static final RowTheme INITIATIVE31 = new RowTheme ("initiative31", LOOK_INITIATIVE31, 0x000000, 0xFFFFFF, 0xE0EEFE, 0xE0EEFE, 0xFFFF00, 0x808080, 0xE3E3E3, 0x808080, 0x000000, 0x0062B8, 0xC0C0C0);

	/** Theme name as given in the property. */
	public final String name;
	/** One of the <code>LOOK_*</code> constants. */
	public final int look;
	/** True when the platform's theme parts draw selection, expanders and check boxes. */
	public final boolean parts;
	/** Text and background colour, or -1 for the widget's own. */
	public final int foreground, background;
	/** Selected row background (focused and not focused) and hot row background. */
	public final int selectionBackground, selectionInactiveBackground, hotBackground;
	/** Tree lines or expander colour, grid lines, handle and check box border, focus colour. */
	public final int hash, grid, border, focusColor;
	/** Check box fill when checked and when grayed. */
	public final int checkFill, grayFill;

	private RowTheme (String name, int look, int foreground, int background, int selectionBackground, int selectionInactiveBackground,
			int hotBackground, int hash, int grid, int border, int focusColor, int checkFill, int grayFill) {
		this.name = name;
		this.look = look;
		this.parts = look == LOOK_NATIVE;
		this.foreground = foreground;
		this.background = background;
		this.selectionBackground = selectionBackground;
		this.selectionInactiveBackground = selectionInactiveBackground;
		this.hotBackground = hotBackground;
		this.hash = hash;
		this.grid = grid;
		this.border = border;
		this.focusColor = focusColor;
		this.checkFill = checkFill;
		this.grayFill = grayFill;
	}

	/**
	 * The theme of a widget class, from the startup properties.
	 *
	 * @param widget the simple class name, for example <code>"Tree"</code>
	 */
	public static RowTheme of (String widget) {
		String value = System.getProperty (PROPERTY + "." + widget);
		if (value == null) value = System.getProperty (PROPERTY);
		if (value == null) value = System.getProperty (ALIAS + "." + widget);
		if (value == null) value = System.getProperty (ALIAS);
		if (value == null) return NATIVE;
		String requested = value, name = value.trim ().toLowerCase (Locale.ROOT);
		switch (name) {
			case "native": return NATIVE;
			case "swing": case "metal": return SWING;
			case "initiative31": case "i31": return INITIATIVE31;
			default:
				LOG.info (() -> "ignoring " + PROPERTY + " value '" + requested + "' for " + widget + "; using native");
				return NATIVE;
		}
	}
}

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
 * The built-in look and feel tables (<code>native</code>, <code>flat</code>, <code>flatdark</code>) in the
 * {@link LookAndFeelLoader} format, and the key catalogue every built-in table defines. The tables are Java text so
 * that they ship inside the class files on every platform. Colour values are this project's own (the
 * <code>flat</code> themes follow FlatLaf's approach, not its values).
 */
public final class LookAndFeelThemes {

	/** The keys every built-in table defines; drawn widgets may rely on them. */
	public static final List<String> KEYS = List.of (
		"Component.background", "Component.foreground", "Component.disabledForeground", "Component.borderColor",
		"Component.focusColor", "Component.focusWidth", "Component.arc", "Component.hoverBackground",
		"Component.selectionBackground", "Component.selectionForeground", "Component.selectionInactiveBackground",
		"Component.selectionInactiveForeground",
		"Button.background", "Button.foreground", "Button.disabledForeground", "Button.borderColor", "Button.hoverBackground",
		"Button.pressedBackground", "Button.focusedBorderColor", "Button.default.background", "Button.default.foreground",
		"Button.arc", "Button.margin",
		"Label.foreground", "Label.disabledForeground",
		"Link.foreground", "Link.hoverForeground",
		"Text.background", "Text.foreground", "Text.borderColor", "Text.focusedBorderColor", "Text.margin",
		"List.background", "List.foreground", "List.selectionBackground", "List.selectionForeground",
		"List.selectionInactiveBackground", "List.rowHeight",
		"Table.background", "Table.foreground", "Table.selectionBackground", "Table.selectionForeground",
		"Table.selectionInactiveBackground", "Table.gridColor", "Table.stripeBackground", "Table.rowHeight", "Table.showGrid",
		"Table.headerBackground", "Table.headerForeground", "Table.headerSeparatorColor",
		"Tree.background", "Tree.foreground", "Tree.selectionBackground", "Tree.selectionForeground",
		"Tree.selectionInactiveBackground", "Tree.lineColor", "Tree.expandIconColor", "Tree.rowHeight", "Tree.indent",
		"Tree.showLines",
		"ScrollBar.track", "ScrollBar.thumb", "ScrollBar.hoverThumb", "ScrollBar.width", "ScrollBar.thumbArc",
		"ScrollBar.showButtons",
		"TabFolder.background", "TabFolder.selectedBackground", "TabFolder.hoverBackground", "TabFolder.underlineColor",
		"TabFolder.tabHeight",
		"ProgressBar.background", "ProgressBar.foreground", "ProgressBar.arc",
		"ToolTip.background", "ToolTip.foreground", "ToolTip.borderColor");

	/* the widget keys every table derives from the Component keys */
	private static final String COMMON = """
		Button.background = $Component.background
		Button.foreground = $Component.foreground
		Button.disabledForeground = $Component.disabledForeground
		Button.borderColor = $Component.borderColor
		Button.hoverBackground = $Component.hoverBackground
		Button.focusedBorderColor = $Component.focusColor
		Button.default.background = $Component.selectionBackground
		Button.default.foreground = $Component.selectionForeground
		Label.foreground = $Component.foreground
		Label.disabledForeground = $Component.disabledForeground
		Text.foreground = $Component.foreground
		Text.borderColor = $Component.borderColor
		Text.focusedBorderColor = $Component.focusColor
		Text.margin = 2,4,2,4
		List.background = $Text.background
		List.foreground = $Component.foreground
		List.selectionBackground = $Component.selectionBackground
		List.selectionForeground = $Component.selectionForeground
		List.selectionInactiveBackground = $Component.selectionInactiveBackground
		Table.background = $List.background
		Table.foreground = $List.foreground
		Table.selectionBackground = $List.selectionBackground
		Table.selectionForeground = $List.selectionForeground
		Table.selectionInactiveBackground = $List.selectionInactiveBackground
		Table.rowHeight = $List.rowHeight
		Table.headerForeground = $Component.foreground
		Tree.background = $List.background
		Tree.foreground = $List.foreground
		Tree.selectionBackground = $List.selectionBackground
		Tree.selectionForeground = $List.selectionForeground
		Tree.selectionInactiveBackground = $List.selectionInactiveBackground
		Tree.rowHeight = $List.rowHeight
		Tree.indent = 16
		ToolTip.borderColor = $Component.borderColor
		""";

	private static final String NATIVE = COMMON + """
		# The platform's colours (SWT system colours), so that a drawn widget looks like its native neighbours.
		Component.background = system(COLOR_WIDGET_BACKGROUND)
		Component.foreground = system(COLOR_WIDGET_FOREGROUND)
		Component.disabledForeground = system(COLOR_WIDGET_DISABLED_FOREGROUND)
		Component.borderColor = system(COLOR_WIDGET_NORMAL_SHADOW)
		Component.focusColor = system(COLOR_LIST_SELECTION)
		Component.focusWidth = 1
		Component.arc = 0
		Component.hoverBackground = mix(system(COLOR_LIST_SELECTION), system(COLOR_LIST_BACKGROUND), 15%)
		Component.selectionBackground = system(COLOR_LIST_SELECTION)
		Component.selectionForeground = system(COLOR_LIST_SELECTION_TEXT)
		Component.selectionInactiveBackground = system(COLOR_WIDGET_LIGHT_SHADOW)
		Component.selectionInactiveForeground = system(COLOR_WIDGET_FOREGROUND)
		Button.pressedBackground = system(COLOR_WIDGET_LIGHT_SHADOW)
		Button.arc = 0
		Button.margin = 3,10,3,10
		Link.foreground = system(COLOR_LINK_FOREGROUND)
		Link.hoverForeground = darken(system(COLOR_LINK_FOREGROUND), 10%)
		Text.background = system(COLOR_LIST_BACKGROUND)
		List.rowHeight = 18
		Table.gridColor = system(COLOR_WIDGET_LIGHT_SHADOW)
		Table.stripeBackground = $Table.background
		Table.showGrid = false
		Table.headerBackground = system(COLOR_WIDGET_BACKGROUND)
		Table.headerSeparatorColor = system(COLOR_WIDGET_NORMAL_SHADOW)
		Tree.lineColor = system(COLOR_WIDGET_NORMAL_SHADOW)
		Tree.expandIconColor = system(COLOR_WIDGET_DARK_SHADOW)
		Tree.showLines = false
		ScrollBar.track = system(COLOR_WIDGET_BACKGROUND)
		ScrollBar.thumb = system(COLOR_WIDGET_NORMAL_SHADOW)
		ScrollBar.hoverThumb = system(COLOR_WIDGET_DARK_SHADOW)
		ScrollBar.width = 17
		ScrollBar.thumbArc = 0
		ScrollBar.showButtons = true
		TabFolder.background = system(COLOR_WIDGET_BACKGROUND)
		TabFolder.selectedBackground = system(COLOR_LIST_BACKGROUND)
		TabFolder.hoverBackground = $Component.hoverBackground
		TabFolder.underlineColor = system(COLOR_LIST_SELECTION)
		TabFolder.tabHeight = 24
		ProgressBar.background = system(COLOR_WIDGET_LIGHT_SHADOW)
		ProgressBar.foreground = system(COLOR_LIST_SELECTION)
		ProgressBar.arc = 0
		ToolTip.background = system(COLOR_INFO_BACKGROUND)
		ToolTip.foreground = system(COLOR_INFO_FOREGROUND)
		""";

	private static final String FLAT = COMMON + """
		# A flat light theme: every colour derives from the variables below.
		@background = #f4f5f7
		@foreground = #1c1d1f
		@field = #ffffff
		@accent = #2f6fd0
		@selectionText = #ffffff
		@border = #c5c8cd
		@disabled = #8a8e95
		Component.background = @background
		Component.foreground = @foreground
		Component.disabledForeground = @disabled
		Component.borderColor = @border
		Component.focusColor = lighten(@accent, 15%)
		Component.focusWidth = 2
		Component.arc = 6
		Component.hoverBackground = darken(@background, 4%)
		Component.selectionBackground = @accent
		Component.selectionForeground = @selectionText
		Component.selectionInactiveBackground = darken(@background, 10%)
		Component.selectionInactiveForeground = @foreground
		Button.background = @field
		Button.pressedBackground = darken(@background, 8%)
		Button.arc = 6
		Button.margin = 3,12,3,12
		Link.foreground = @accent
		Link.hoverForeground = darken(@accent, 10%)
		Text.background = @field
		List.rowHeight = 22
		Table.gridColor = darken(@field, 8%)
		Table.stripeBackground = darken(@field, 2%)
		Table.showGrid = false
		Table.headerBackground = @background
		Table.headerSeparatorColor = @border
		Tree.lineColor = @border
		Tree.expandIconColor = mix(@foreground, @background, 60%)
		Tree.showLines = false
		ScrollBar.track = @background
		ScrollBar.thumb = darken(@background, 18%)
		ScrollBar.hoverThumb = darken(@background, 28%)
		ScrollBar.width = 10
		ScrollBar.thumbArc = 10
		ScrollBar.showButtons = false
		TabFolder.background = @background
		TabFolder.selectedBackground = @field
		TabFolder.hoverBackground = $Component.hoverBackground
		TabFolder.underlineColor = @accent
		TabFolder.tabHeight = 28
		ProgressBar.background = darken(@background, 8%)
		ProgressBar.foreground = @accent
		ProgressBar.arc = 4
		ToolTip.background = @field
		ToolTip.foreground = @foreground
		""";

	private static final String FLATDARK = """
		# The flat theme with dark variables; every derived colour follows.
		@extends = flat
		@background = #2b2d30
		@foreground = #dcdfe4
		@field = #1f2023
		@accent = #3b7be8
		@selectionText = #ffffff
		@border = #4c4f55
		@disabled = #737780
		Component.hoverBackground = lighten(@background, 6%)
		Component.selectionInactiveBackground = lighten(@background, 12%)
		Button.background = lighten(@background, 5%)
		Button.pressedBackground = lighten(@background, 10%)
		Table.gridColor = lighten(@field, 8%)
		Table.stripeBackground = lighten(@field, 3%)
		ScrollBar.thumb = lighten(@background, 18%)
		ScrollBar.hoverThumb = lighten(@background, 28%)
		ProgressBar.background = lighten(@background, 8%)
		""";

	private static final Map<String, String> TABLES = Map.of ("native", NATIVE, "flat", FLAT, "flatdark", FLATDARK);

	/* platform colours when no display is current: the Windows defaults of the light theme */
	private static final Map<String, Integer> FALLBACK_SYSTEM = Map.ofEntries (
		Map.entry ("COLOR_WIDGET_BACKGROUND", 0xF0F0F0), Map.entry ("COLOR_WIDGET_FOREGROUND", 0x000000),
		Map.entry ("COLOR_WIDGET_DISABLED_FOREGROUND", 0x6D6D6D), Map.entry ("COLOR_WIDGET_NORMAL_SHADOW", 0xA0A0A0),
		Map.entry ("COLOR_WIDGET_LIGHT_SHADOW", 0xE3E3E3), Map.entry ("COLOR_WIDGET_DARK_SHADOW", 0x696969),
		Map.entry ("COLOR_WIDGET_HIGHLIGHT_SHADOW", 0xFFFFFF), Map.entry ("COLOR_WIDGET_BORDER", 0x646464),
		Map.entry ("COLOR_LIST_BACKGROUND", 0xFFFFFF), Map.entry ("COLOR_LIST_FOREGROUND", 0x000000),
		Map.entry ("COLOR_LIST_SELECTION", 0x0078D7), Map.entry ("COLOR_LIST_SELECTION_TEXT", 0xFFFFFF),
		Map.entry ("COLOR_INFO_BACKGROUND", 0xFFFFE1), Map.entry ("COLOR_INFO_FOREGROUND", 0x000000),
		Map.entry ("COLOR_LINK_FOREGROUND", 0x0066CC), Map.entry ("COLOR_TITLE_BACKGROUND", 0x99B4D1),
		Map.entry ("COLOR_TITLE_FOREGROUND", 0x000000), Map.entry ("COLOR_TEXT_DISABLED_BACKGROUND", 0xF0F0F0));

	private LookAndFeelThemes () {
	}

	/** The properties text of a built-in look and feel, or <code>null</code>. */
	public static String text (String name) {
		return TABLES.get (name);
	}

	/** The names of the built-in look and feels. */
	public static Set<String> names () {
		return new TreeSet<> (TABLES.keySet ());
	}

	/** A platform colour (RGB) by SWT constant name when no display is current, or -1 when the name is unknown. */
	public static int fallbackSystemColor (String name) {
		Integer rgb = FALLBACK_SYSTEM.get (name);
		return rgb == null ? -1 : rgb;
	}
}

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

import org.eclipse.swt.internal.laf.*;

/**
 * The theme of a widget class's drawn rows read from a look and feel's defaults table (docs/m3/LOOK_AND_FEEL.md;
 * the keys in docs/m3/render-modes/LAF_KEYS.tsv): every colour by its Swing/FlatLaf key (<code>Table.selectionBackground</code>,
 * <code>Tree.lineColor</code>, <code>Component.focusColor</code>, ...), so a built-in look and feel's colours live in
 * its table, not in the drawn modes. The look (how selection, expanders and check boxes are drawn) is the built-in
 * theme's; a key the table leaves out answers that theme's value. The native look keeps the widget's own text and
 * background colours (an application's colours win, as on the platform). Row height and indent stay the platform's:
 * in stage 1 the native control is the behaviour plane and owns the geometry.
 */
public final class ThemeDefaults {
	private ThemeDefaults () {
	}

	/**
	 * The theme of a widget class under a look and feel.
	 *
	 * @param widget the widget's simple class name (<code>Tree</code>, <code>Table</code>, <code>List</code>)
	 * @param look the built-in theme whose look this is (its values answer the keys the table leaves out)
	 * @param table the look and feel's resolved defaults
	 */
	public static Theme of (String widget, Theme look, LookAndFeelDefaults table) {
		Theme.Builder b = look.derive (look.name);
		if (look.look != Theme.LOOK_NATIVE) {
			b.colors (rgb (table, widget + ".foreground", look.foreground), rgb (table, widget + ".background", look.background))
				.selectionForeground (rgb (table, widget + ".selectionForeground", look.selectionForeground));
		}
		return b.selection (rgb (table, widget + ".selectionBackground", look.selectionBackground),
				rgb (table, widget + ".selectionInactiveBackground", look.selectionInactiveBackground))
			.hot (rgb (table, "Component.hoverBackground", look.hotBackground))
			.lines (rgb (table, "Tree.lineColor", look.hash), rgb (table, "Table.gridColor", look.grid))
			.border (rgb (table, "Tree.expandIconColor", look.border)).focus (rgb (table, "Component.focusColor", look.focusColor))
			.check (rgb (table, "CheckBox.icon.checkmarkColor", look.checkFill), rgb (table, "CheckBox.icon.disabledCheckmarkColor", look.grayFill))
			.build ();
	}

	/* the 0xRRGGBB of a colour key (the table's ARGB without alpha), or the fallback when the key has no colour */
	static int rgb (LookAndFeelDefaults table, String key, int fallback) {
		int index = table.index (key);
		return index >= 0 && table.kind (index) == LookAndFeelDefaults.COLOR ? table.value (index) & 0xFFFFFF : fallback;
	}
}

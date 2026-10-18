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
package org.eclipse.swt.widgets;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;

/**
 * Query of the effective startup mode of a widget (enhanced M3 API).
 * <p>
 * Modes are chosen once per JVM by the properties <code>swt.widget.mode</code> (global) and
 * <code>swt.widget.mode.&lt;Widget&gt;</code> (per widget class); every mode keeps the public
 * SWT contract. A widget class that does not implement the requested mode runs in
 * {@link #VIEWPORT} when it has a viewport, otherwise in {@link #ORIGINAL}. This class answers
 * the mode a widget is effectively running in, as an <code>int</code> code.
 * </p>
 *
 * @since 3.138
 */
public final class M3Mode {

	/** Mediated viewport over the native control (the default). */
	public static final int VIEWPORT = 0;
	/** The original native implementation. */
	public static final int ORIGINAL = 1;
	/** Drawn by SWT with a GC. */
	public static final int CUSTOM = 2;
	/** Drawn by SWT through the M3 renderer. */
	public static final int M3 = 3;
	/** Drawn by SWT on the graphics hardware. */
	public static final int GPU = 4;

	private static final String [] NAMES = {"viewport", "original", "custom", "m3", "gpu"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

	/* Widget classes with a viewport; Tree also has the three drawn modes. */
	private static final Set<String> VIEWPORT_WIDGETS = Set.of ("Tree", "Table", "List", "Combo", "CoolBar", "ExpandBar", "Composite"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$

	private M3Mode () {
	}

	/**
	 * Returns the mode the widget runs in: one of {@link #VIEWPORT}, {@link #ORIGINAL},
	 * {@link #CUSTOM}, {@link #M3} and {@link #GPU}.
	 *
	 * @param widget the widget
	 * @return the mode code
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the widget is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the widget has been disposed</li>
	 * </ul>
	 */
	public static int of (Widget widget) {
		if (widget == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		widget.checkWidget ();
		String gate = gate (widget);
		return code (WidgetMode.of (gate, supported (widget, gate)));
	}

	/**
	 * Returns the property name of a mode code, for example <code>"viewport"</code>.
	 *
	 * @param mode the mode code
	 * @return the name
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if the code is not a mode</li>
	 * </ul>
	 */
	public static String name (int mode) {
		if (mode < 0 || mode >= NAMES.length) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		return NAMES [mode];
	}

	/* The SWT widget class whose gate decides: the nearest superclass in org.eclipse.swt.widgets. */
	private static String gate (Widget widget) {
		Class<?> type = widget.getClass ();
		while (!Widget.class.getPackageName ().equals (type.getPackageName ())) type = type.getSuperclass ();
		return type.getSimpleName ();
	}

	/* Modes a widget class implements besides ORIGINAL (the same table the widgets' gates use). */
	private static WidgetMode [] supported (Widget widget, String gate) {
		if (widget instanceof Tree) return new WidgetMode [] {WidgetMode.VIEWPORT, WidgetMode.CUSTOM, WidgetMode.M3, WidgetMode.GPU};
		if (VIEWPORT_WIDGETS.contains (gate)) return new WidgetMode [] {WidgetMode.VIEWPORT};
		return new WidgetMode [0];
	}

	private static int code (WidgetMode mode) {
		switch (mode) {
			case ORIGINAL: return ORIGINAL;
			case CUSTOM: return CUSTOM;
			case M3: return M3;
			case GPU: return GPU;
			default: return VIEWPORT;
		}
	}
}

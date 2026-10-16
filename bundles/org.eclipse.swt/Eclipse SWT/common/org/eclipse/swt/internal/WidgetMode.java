/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.internal;

import java.util.*;

/**
 * Startup strategy of SWT widgets. Every mode keeps the public API contract; they differ in how a
 * widget is realized.
 * <ul>
 * <li>{@link #VIEWPORT} (default): the native control holds only the rows (items, children, lines)
 * on screen; the widget mediates the rest and keeps the native look and behaviour.</li>
 * <li>{@link #ORIGINAL}: the native SWT implementation, unchanged.</li>
 * <li>{@link #CUSTOM}: the widget is drawn by SWT itself with {@link org.eclipse.swt.graphics.GC}.</li>
 * <li>{@link #SKIJA}: the widget is drawn by SWT itself through Skija (Skia).</li>
 * </ul>
 * The mode is chosen at startup with system properties, read once when a widget class initializes:
 * <ul>
 * <li><code>-Dswt.widget.mode=viewport|original|custom|skija</code> for every widget;</li>
 * <li><code>-Dswt.widget.mode.&lt;Widget&gt;=...</code> (for example <code>swt.widget.mode.Tree</code>)
 * for one widget class, overriding the global value.</li>
 * </ul>
 * A widget that does not implement the requested mode falls back to {@link #VIEWPORT} when it has a
 * viewport, otherwise to {@link #ORIGINAL}.
 */
public enum WidgetMode {
	/** Mediated viewport over the native control (the default). */
	VIEWPORT,
	/** The original native implementation. */
	ORIGINAL,
	/** Drawn by SWT with a GC. */
	CUSTOM,
	/** Drawn by SWT through Skija. */
	SKIJA;

	/** Name of the global property; a widget property appends <code>"." + simpleClassName</code>. */
	public static final String PROPERTY = "swt.widget.mode";

	private static final System.Logger LOG = System.getLogger ("org.eclipse.swt.internal.WidgetMode");

	/**
	 * Answers the mode of a widget class.
	 *
	 * @param widget the simple class name of the widget, for example <code>"Tree"</code>
	 * @param supported the modes the widget implements; {@link #ORIGINAL} is always implied
	 * @return the requested mode when supported, otherwise the fallback
	 */
	public static WidgetMode of (String widget, WidgetMode... supported) {
		WidgetMode requested = requested (widget);
		EnumSet<WidgetMode> modes = EnumSet.of (ORIGINAL);
		modes.addAll (Arrays.asList (supported));
		WidgetMode mode = modes.contains (requested) ? requested : modes.contains (VIEWPORT) ? VIEWPORT : ORIGINAL;
		LOG.log (System.Logger.Level.DEBUG, () -> widget + ": mode " + mode + (mode == requested ? "" : " (requested " + requested + ")"));
		return mode;
	}

	/** The mode the startup properties ask for, before any fallback. */
	public static WidgetMode requested (String widget) {
		String specific = System.getProperty (PROPERTY + "." + widget);
		String value = specific != null ? specific : System.getProperty (PROPERTY);
		if (value == null) return VIEWPORT;
		try {
			return valueOf (value.trim ().toUpperCase (Locale.ROOT));
		} catch (IllegalArgumentException e) {
			LOG.log (System.Logger.Level.INFO, () -> "ignoring " + PROPERTY + " value '" + value + "' for " + widget);
			return VIEWPORT;
		}
	}
}

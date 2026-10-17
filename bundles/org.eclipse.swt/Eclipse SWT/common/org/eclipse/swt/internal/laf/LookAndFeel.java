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
import java.util.concurrent.*;
import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.widgets.*;

/**
 * The look and feel of drawn widgets (docs/m3/LOOK_AND_FEEL.md), chosen at startup like Swing's
 * <code>UIManager.setLookAndFeel</code>: a name, a {@link RendererFactory} and a defaults table
 * ({@link LookAndFeelDefaults}).
 * <ul>
 * <li><code>-Dswt.lookAndFeel=native|flat|flatdark|...</code> for every widget class, and
 * <code>-Dswt.lookAndFeel.&lt;Widget&gt;=...</code> (for example <code>swt.lookAndFeel.Tree</code>) for one, read
 * when a widget asks; <code>native</code> is the default.</li>
 * <li>The look and feel applies only to a widget whose mode ({@link WidgetMode}) draws it: <code>custom</code>,
 * <code>m3</code> or <code>gpu</code>. In the <code>viewport</code> and <code>original</code> modes the pixels are
 * native and {@link #of(String, WidgetMode)} answers <code>native</code>.</li>
 * <li>An unknown or unreadable look and feel falls back to <code>native</code> (logged); <code>native</code> itself
 * never fails.</li>
 * </ul>
 * A table is loaded and resolved once per name and shared.
 */
public final class LookAndFeel {
	/** Name of the global property; a widget property appends <code>"." + simpleClassName</code>. */
	public static final String PROPERTY = "swt.lookAndFeel";
	/** The default look and feel: drawn widgets imitate the platform through its system colours. */
	public static final String NATIVE = "native";

	private static final SwtLog LOG = SwtLog.getLog ("org.eclipse.swt.internal.laf.LookAndFeel");
	private static final Map<String, LookAndFeel> LOADED = new ConcurrentHashMap<> ();
	private static final Map<String, String> TABLES = new ConcurrentHashMap<> ();
	private static final Map<String, RendererFactory> RENDERERS = new ConcurrentHashMap<> ();

	private final String name;
	private final LookAndFeelDefaults defaults;

	private LookAndFeel (String name, LookAndFeelDefaults defaults) {
		this.name = name;
		this.defaults = defaults;
	}

	/** The name, for example <code>flat</code>. */
	public String name () {
		return name;
	}

	/** The resolved defaults table. */
	public LookAndFeelDefaults defaults () {
		return defaults;
	}

	/** The renderer factory registered for this look and feel, or {@link RendererFactory#NONE}. */
	public RendererFactory renderers () {
		return RENDERERS.getOrDefault (name, RendererFactory.NONE);
	}

	/**
	 * The look and feel of a widget class in a mode.
	 *
	 * @param widget the simple class name, for example <code>"Button"</code>
	 * @param mode the widget's mode
	 * @return the requested look and feel when the mode draws the widget, otherwise <code>native</code>
	 */
	public static LookAndFeel of (String widget, WidgetMode mode) {
		return named (drawn (mode) ? requested (widget, System::getProperty) : NATIVE);
	}

	/** True for the modes in which SWT draws the widget itself: <code>custom</code>, <code>m3</code>, <code>gpu</code>. */
	public static boolean drawn (WidgetMode mode) {
		return mode == WidgetMode.CUSTOM || mode == WidgetMode.M3 || mode == WidgetMode.GPU;
	}

	/**
	 * The look and feel name the properties ask for a widget class: the widget property, else the global one, else
	 * <code>native</code>; lower case.
	 */
	public static String requested (String widget, Function<String, String> properties) {
		String specific = properties.apply (PROPERTY + "." + widget);
		String value = specific != null ? specific : properties.apply (PROPERTY);
		return value == null || value.isBlank () ? NATIVE : value.trim ().toLowerCase (Locale.ROOT);
	}

	/**
	 * The look and feel of a name, loaded and resolved once. An unknown name or a table that cannot be read falls back
	 * to <code>native</code>.
	 */
	public static LookAndFeel named (String name) {
		LookAndFeel loaded = LOADED.get (name);
		if (loaded != null) return loaded;
		try {
			loaded = new LookAndFeel (name, loader ().load (name));
		} catch (IllegalArgumentException e) {
			if (NATIVE.equals (name)) throw e;
			LOG.info (() -> "look and feel '" + name + "' falls back to native: " + e.getMessage ());
			return named (NATIVE);
		}
		if (Display.getCurrent () == null) {
			/* resolved with the fallback platform colours: not kept, so that a thread with a display resolves its own */
			return loaded;
		}
		LookAndFeel previous = LOADED.putIfAbsent (name, loaded);
		LookAndFeel result = previous != null ? previous : loaded;
		LOG.debug (() -> "look and feel " + name + ": " + result.defaults);
		return result;
	}

	/**
	 * Registers a look and feel: a defaults table (properties text in the {@link LookAndFeelLoader} format, which may
	 * extend a built-in one) and/or a renderer factory. A name registered again replaces the earlier registration for
	 * widgets that ask afterwards.
	 *
	 * @param name the name, lower case
	 * @param table the properties text, or <code>null</code> to keep the current table (a built-in one)
	 * @param renderers the renderers, or <code>null</code> for none
	 */
	public static void register (String name, String table, RendererFactory renderers) {
		if (table != null) TABLES.put (name, table);
		if (renderers != null) RENDERERS.put (name, renderers);
		else RENDERERS.remove (name);
		LOADED.remove (name);
	}

	/** The names that can be asked for: the built-in look and feels and the registered ones. */
	public static Set<String> names () {
		Set<String> names = new TreeSet<> (LookAndFeelThemes.names ());
		names.addAll (TABLES.keySet ());
		return names;
	}

	static LookAndFeelLoader loader () {
		return new LookAndFeelLoader (name -> TABLES.getOrDefault (name, LookAndFeelThemes.text (name)), LookAndFeel::systemColor);
	}

	/* a platform colour by SWT constant name: from the current display, or the built-in fallback without one */
	static int systemColor (String name) {
		int id;
		try {
			id = SWT.class.getField (name).getInt (null);
		} catch (ReflectiveOperationException e) {
			throw new IllegalArgumentException ("not an SWT colour constant: " + name);
		}
		Display display = Display.getCurrent ();
		if (display != null) {
			Color color = display.getSystemColor (id);
			RGB rgb = color.getRGB ();
			return (rgb.red << 16) | (rgb.green << 8) | rgb.blue;
		}
		int rgb = LookAndFeelThemes.fallbackSystemColor (name);
		if (rgb < 0) throw new IllegalArgumentException ("no fallback for the system colour " + name);
		return rgb;
	}

	@Override
	public String toString () {
		return "LookAndFeel {" + name + "}";
	}
}

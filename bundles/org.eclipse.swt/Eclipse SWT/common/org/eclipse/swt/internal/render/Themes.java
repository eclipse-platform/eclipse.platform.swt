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
import java.util.concurrent.*;

import org.eclipse.swt.internal.*;

/**
 * The theme registry and selection of the drawn planes, like Swing's <code>UIManager</code>.
 * <ul>
 * <li>{@link #register(Theme)} adds a user-defined theme; the built-in names (<code>native</code>, <code>swing</code>,
 * <code>initiative31</code>) cannot be replaced.</li>
 * <li>{@link #select(String, Theme)} selects a theme at run time for one widget class, or for all with
 * <code>null</code>; drawn planes pick it up on their next frame and the listeners ({@link #addListener}) are told, so
 * planes repaint.</li>
 * <li>Without a run-time selection the startup properties decide: <code>swt.widget.theme.&lt;Widget&gt;</code>, then
 * <code>swt.widget.theme</code>, then the aliases <code>swt.lookAndFeel[.&lt;Widget&gt;]</code>; an unknown name
 * logs once and falls back to {@link Theme#NATIVE}.</li>
 * </ul>
 */
public final class Themes {
	/** Name of the global property; a widget property appends <code>"." + simpleClassName</code>. */
	public static final String PROPERTY = "swt.widget.theme";
	static final String ALIAS = "swt.lookAndFeel";
	private static final SwtLog LOG = SwtLog.getLog ("org.eclipse.swt.internal.render.Themes");
	private static final Map<String, Theme> REGISTRY = new ConcurrentHashMap<> ();
	private static final Map<String, Theme> SELECTED = new ConcurrentHashMap<> ();
	private static final String ALL = "";
	private static final List<Runnable> LISTENERS = new CopyOnWriteArrayList<> ();
	private static volatile int generation;

	static {
		for (Theme theme : new Theme [] {Theme.NATIVE, Theme.SWING, Theme.INITIATIVE31}) REGISTRY.put (theme.name, theme);
		REGISTRY.put ("metal", Theme.SWING);
		REGISTRY.put ("i31", Theme.INITIATIVE31);
	}

	private Themes () {
	}

	/** Registers a user-defined theme under its name; answers false when the name is a built-in one. */
	public static boolean register (Theme theme) {
		Objects.requireNonNull (theme, "theme");
		if (builtIn (theme.name)) return false;
		REGISTRY.put (theme.name, theme);
		changed ();
		return true;
	}

	/** Removes a user-defined theme (and its run-time selections); answers false for built-in or unknown names. */
	public static boolean unregister (String name) {
		String key = key (name);
		if (builtIn (key)) return false;
		Theme removed = REGISTRY.remove (key);
		if (removed == null) return false;
		SELECTED.values ().removeIf (theme -> theme == removed);
		changed ();
		return true;
	}

	private static boolean builtIn (String name) {
		Theme theme = REGISTRY.get (name);
		return theme == Theme.NATIVE || theme == Theme.SWING || theme == Theme.INITIATIVE31;
	}

	/** The registered theme of a name (case-insensitive), or null. */
	public static Theme find (String name) {
		return name == null ? null : REGISTRY.get (key (name));
	}

	/** The registered theme names, sorted. */
	public static SortedSet<String> names () {
		return new TreeSet<> (REGISTRY.keySet ());
	}

	/**
	 * Selects a theme at run time.
	 *
	 * @param widget a widget class (<code>"Tree"</code>), or null for every drawn widget
	 * @param theme the theme, or null to go back to the startup properties
	 */
	public static void select (String widget, Theme theme) {
		String key = widget == null ? ALL : widget;
		if (theme == null) {
			SELECTED.remove (key);
		} else {
			SELECTED.put (key, theme);
		}
		changed ();
	}

	/** The theme in effect for a widget class: run-time selection, then startup properties, then {@link Theme#NATIVE}. */
	public static Theme current (String widget) {
		Theme theme = SELECTED.get (widget);
		if (theme == null) theme = SELECTED.get (ALL);
		if (theme != null) return theme;
		String value = System.getProperty (PROPERTY + "." + widget);
		if (value == null) value = System.getProperty (PROPERTY);
		if (value == null) value = System.getProperty (ALIAS + "." + widget);
		if (value == null) value = System.getProperty (ALIAS);
		if (value == null) return Theme.NATIVE;
		theme = find (value);
		if (theme != null) return theme;
		String requested = value;
		LOG.info (() -> "ignoring " + PROPERTY + " value '" + requested + "' for " + widget + "; using native");
		return Theme.NATIVE;
	}

	/** A counter that changes with every registration or selection; planes compare it per frame. */
	public static int generation () {
		return generation;
	}

	/** Tells a listener (any thread) when themes or selections change; planes use it to repaint. */
	public static void addListener (Runnable listener) {
		LISTENERS.add (Objects.requireNonNull (listener, "listener"));
	}

	/** Removes a listener. */
	public static void removeListener (Runnable listener) {
		LISTENERS.remove (listener);
	}

	private static void changed () {
		generation++;
		for (Runnable listener : LISTENERS) listener.run ();
	}

	private static String key (String name) {
		return name.trim ().toLowerCase (Locale.ROOT);
	}
}

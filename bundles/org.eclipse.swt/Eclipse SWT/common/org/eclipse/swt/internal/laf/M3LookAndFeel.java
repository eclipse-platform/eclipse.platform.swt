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

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.widgets.*;

/**
 * The look and feel manager of the drawn widgets (docs/m3/LOOK_AND_FEEL.md, LF6), Swing's <code>UIManager</code>:
 * the selected look and feel, switched at runtime ({@link #setLookAndFeel}), its {@link M3UIDefaults} over one
 * developer layer, the UI delegates of the widgets ({@link #installUI}) and their update on a switch.
 * <p>
 * A switch diffs the defaults of the two look and feels and touches only the widgets whose delegate reads a changed
 * key: the delegate is installed again (created again when the new look and feel's factory has one), the widget is
 * repainted, and relaid out when a measured key changed ({@link M3ComponentUI#metricKeys()}). The display's
 * {@link M3UIResources} are disposed. {@link #updateComponentTreeUI} is Swing's full update of a tree. Widgets without
 * a delegate (every native widget, every widget in the <code>viewport</code> and <code>original</code> modes) are
 * never visited.
 * </p>
 * <p>
 * Selection: a widget class pinned with <code>-Dswt.widget.theme.&lt;Widget&gt;</code> keeps its look and feel;
 * every other drawn widget follows {@link #setLookAndFeel}, or before any call the startup property
 * <code>swt.widget.theme</code> (see {@link LookAndFeel}).
 * </p>
 */
public final class M3LookAndFeel {
	/** The key of a widget's delegate in its data. */
	public static final String UI = M3ComponentUI.class.getName ();

	/** Told about every switch, after the selection changed and before the widgets are updated. */
	public interface Listener {
		/** The look and feel changed from one name to another. */
		void lookAndFeelChanged (String from, String to);
	}

	private static final M3UIDefaults.Lanes DEVELOPER = new M3UIDefaults.Lanes ();
	private static final Map<String, M3UIDefaults> DEFAULTS = new ConcurrentHashMap<> ();
	private static final java.util.List<Listener> LISTENERS = new CopyOnWriteArrayList<> ();
	private static final org.eclipse.swt.widgets.Listener DISPOSED = event -> remove ((Control) event.widget);
	private static volatile String selected;

	private M3LookAndFeel () {
	}

	/** The selected look and feel: the last {@link #setLookAndFeel}, else the startup property, else <code>native</code>. */
	public static String getLookAndFeel () {
		String name = selected;
		return name != null ? name : normalize (property (LookAndFeel.PROPERTY, LookAndFeel.ALIAS, ""));
	}

	/**
	 * Selects a look and feel and updates the widgets of the calling thread's display (other displays call
	 * {@link #update(Display, M3UIDefaults, M3UIDefaults)} on their thread).
	 *
	 * @param name a built-in, aliased or registered look and feel
	 * @throws IllegalArgumentException when no look and feel has the name
	 */
	public static void setLookAndFeel (String name) {
		String to = normalize (name);
		if (!LookAndFeel.names ().contains (to) && LookAndFeelThemes.text (to) == null) throw new IllegalArgumentException ("unknown look and feel '" + name + "'");
		String from = getLookAndFeel ();
		M3UIDefaults before = getDefaults ();
		selected = to;
		for (Listener listener : LISTENERS) listener.lookAndFeelChanged (from, to);
		Display display = Display.getCurrent ();
		if (display != null) update (display, before, getDefaults ());
	}

	/** Back to the startup property (tests, and applications that undo a selection). */
	public static void reset () {
		selected = null;
	}

	/** Adds a switch listener. */
	public static void addListener (Listener listener) {
		LISTENERS.add (listener);
	}

	/** Removes a switch listener. */
	public static void removeListener (Listener listener) {
		LISTENERS.remove (listener);
	}

	/** The defaults of the selected look and feel. */
	public static M3UIDefaults getDefaults () {
		return defaults (getLookAndFeel ());
	}

	/**
	 * The defaults of a look and feel, over the shared developer layer; resolved once per name on a display thread
	 * (as {@link LookAndFeel#named}).
	 */
	public static M3UIDefaults defaults (String name) {
		M3UIDefaults cached = DEFAULTS.get (name);
		if (cached != null) return cached;
		M3UIDefaults defaults = new M3UIDefaults (LookAndFeel.named (name).defaults (), DEVELOPER);
		if (Display.getCurrent () != null) DEFAULTS.putIfAbsent (name, defaults);
		return defaults;
	}

	/** {@link LookAndFeel#register}, dropping the cached defaults of the name. */
	public static void register (String name, String table, RendererFactory renderers) {
		LookAndFeel.register (name, table, renderers);
		DEFAULTS.clear ();
	}

	/**
	 * The look and feel of a widget class in a mode: <code>native</code> unless the mode draws the widget, else the
	 * widget's pin, else the selected look and feel.
	 */
	public static LookAndFeel of (String widget, WidgetMode mode) {
		return LookAndFeel.named (LookAndFeel.drawn (mode) ? lookAndFeel (widget) : LookAndFeel.NATIVE);
	}

	/**
	 * Installs the delegate of a drawn widget: the look and feel's factory is asked for <code>type</code>, and the
	 * widget's default delegate is used when it has none. Replaces an installed delegate.
	 *
	 * @return the installed delegate
	 */
	public static <U extends M3ComponentUI> U installUI (Control control, Class<U> type, U fallback) {
		uninstallUI (control);
		String name = lookAndFeel (control.getClass ().getSimpleName ());
		U created = LookAndFeel.named (name).renderers ().create (type, control);
		U ui = created != null ? created : fallback;
		ui.installUI (control, defaults (name));
		Registry registry = Registry.of (control.getDisplay ());
		control.setData (UI, new Slot (ui, name, registry.add (control)));
		control.addListener (SWT.Dispose, DISPOSED);
		return ui;
	}

	/** The delegate of a widget, or <code>null</code>. */
	public static M3ComponentUI getUI (Control control) {
		Slot slot = (Slot) control.getData (UI);
		return slot == null ? null : slot.ui;
	}

	/** Uninstalls the delegate of a widget, if any. */
	public static void uninstallUI (Control control) {
		Slot slot = (Slot) control.getData (UI);
		if (slot == null) return;
		slot.ui.uninstallUI (control);
		remove (control);
		control.removeListener (SWT.Dispose, DISPOSED);
	}

	/**
	 * Swing's <code>updateComponentTreeUI</code>: every delegate of a widget and its descendants is installed again with
	 * the defaults of its look and feel, and the widgets are repainted and relaid out.
	 *
	 * @return the number of delegates installed again
	 */
	public static int updateComponentTreeUI (Control control) {
		int touched = 0;
		Slot slot = (Slot) control.getData (UI);
		if (slot != null) {
			reinstall (control, slot, lookAndFeel (control.getClass ().getSimpleName ()));
			control.requestLayout ();
			control.redraw ();
			touched++;
		}
		if (control instanceof Composite composite) {
			for (Control child : composite.getChildren ()) touched += updateComponentTreeUI (child);
		}
		return touched;
	}

	/**
	 * Updates the widgets of a display after a switch between two defaults: the delegates that read a key the switch
	 * changes, nothing else (see the class comment). The display's resources are disposed.
	 *
	 * @return the number of delegates installed again
	 */
	public static int update (Display display, M3UIDefaults from, M3UIDefaults to) {
		M3UIResources.of (display).dispose ();
		Registry registry = Registry.of (display);
		long [] changed = M3UIDefaults.diff (from, to);
		int touched = 0;
		for (int i = 0; i < registry.count; i++) {
			Control control = registry.controls [i];
			Slot slot = (Slot) control.getData (UI);
			String target = lookAndFeel (control.getClass ().getSimpleName ());
			if (target.equals (slot.lookAndFeel)) continue;
			long [] keys = slot.lookAndFeel.equals (from.name ()) && target.equals (to.name ()) ? changed : M3UIDefaults.diff (defaults (slot.lookAndFeel), defaults (target));
			boolean relayout = reads (slot.ui.metricKeys (), keys);
			if (relayout || reads (slot.ui.paintKeys (), keys)) {
				reinstall (control, slot, target);
				if (relayout) control.requestLayout ();
				control.redraw ();
				touched++;
			} else {
				slot.lookAndFeel = target;
			}
		}
		return touched;
	}

	/**
	 * Sets a per-widget value (Swing's <code>putClientProperty</code>) in the display's {@link M3UIOverrides} and
	 * repaints the widget when its delegate reads the key (relays it out for a measured key).
	 */
	public static void override (Control control, int key, int value) {
		M3UIOverrides.of (control.getDisplay ()).put (control, key, value);
		M3ComponentUI ui = getUI (control);
		if (ui == null) return;
		long [] keys = new long [(key >>> 6) + 1];
		keys [key >>> 6] = 1L << key;
		if (reads (ui.metricKeys (), keys)) control.requestLayout ();
		if (reads (ui.paintKeys (), keys) || reads (ui.metricKeys (), keys)) control.redraw ();
	}

	/** The number of widgets of a display with a delegate. */
	public static int installed (Display display) {
		return Registry.of (display).count;
	}

	private static void reinstall (Control control, Slot slot, String target) {
		slot.ui.uninstallUI (control);
		M3ComponentUI created = LookAndFeel.named (target).renderers ().create (slot.ui.type (), control);
		if (created != null) slot.ui = created;
		slot.lookAndFeel = target;
		slot.ui.installUI (control, defaults (target));
	}

	private static boolean reads (int [] keys, long [] changed) {
		for (int key : keys) {
			int word = key >>> 6;
			if (word < changed.length && (changed [word] & (1L << key)) != 0) return true;
		}
		return false;
	}

	private static void remove (Control control) {
		Slot slot = (Slot) control.getData (UI);
		if (slot == null) return;
		Registry registry = Registry.of (control.getDisplay ());
		Control moved = registry.remove (slot.index);
		if (moved != null) ((Slot) moved.getData (UI)).index = slot.index;
		control.setData (UI, null);
	}

	/* the widget's pin (swt.widget.theme.<Widget> or its alias), else the selected look and feel */
	static String lookAndFeel (String widget) {
		String pin = property (LookAndFeel.PROPERTY + "." + widget, LookAndFeel.ALIAS + "." + widget, null);
		return pin != null ? normalize (pin) : getLookAndFeel ();
	}

	private static String property (String name, String alias, String absent) {
		String value = System.getProperty (name);
		if (value == null) value = System.getProperty (alias);
		return value == null || value.isBlank () ? absent : value;
	}

	private static String normalize (String name) {
		return name == null || name.isBlank () ? LookAndFeel.NATIVE : name.trim ().toLowerCase (Locale.ROOT);
	}

	/* a widget's delegate, the look and feel it was installed with, and its index in the display's registry */
	private static final class Slot {
		M3ComponentUI ui;
		String lookAndFeel;
		int index;

		Slot (M3ComponentUI ui, String lookAndFeel, int index) {
			this.ui = ui;
			this.lookAndFeel = lookAndFeel;
			this.index = index;
		}
	}

	/* the widgets of a display that have a delegate, dense (a removal moves the last one into the hole) */
	private static final class Registry {
		Control [] controls = new Control [16];
		int count;

		static Registry of (Display display) {
			String key = Registry.class.getName ();
			Registry registry = (Registry) display.getData (key);
			if (registry == null) display.setData (key, registry = new Registry ());
			return registry;
		}

		int add (Control control) {
			if (count == controls.length) controls = Arrays.copyOf (controls, count * 2);
			controls [count] = control;
			return count++;
		}

		/* removes an index; answers the control moved into it, or null */
		Control remove (int index) {
			Control last = controls [--count];
			controls [count] = null;
			if (index == count) return null;
			controls [index] = last;
			return last;
		}
	}
}

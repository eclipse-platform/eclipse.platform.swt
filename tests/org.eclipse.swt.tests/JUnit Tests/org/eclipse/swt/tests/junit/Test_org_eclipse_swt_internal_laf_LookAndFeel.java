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
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.laf.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Look and feel seam and defaults table (docs/m3/LOOK_AND_FEEL.md, leaves LF1 and LF2): selection by property and
 * mode, the loader's values, references, variables, functions, inheritance and errors, and the built-in tables.
 * Only {@link #displayResolution} needs a display (hidden desktop); everything else runs without one.
 */
public class Test_org_eclipse_swt_internal_laf_LookAndFeel {

	static final LookAndFeelLoader.SystemColors SYSTEM = name -> switch (name) {
		case "COLOR_LIST_SELECTION" -> 0x0078D7;
		case "COLOR_WIDGET_BACKGROUND" -> 0xF0F0F0;
		default -> throw new IllegalArgumentException ("unknown " + name);
	};

	static LookAndFeelDefaults load (String name, Map<String, String> tables) {
		return new LookAndFeelLoader (tables::get, SYSTEM).load (name);
	}

	static LookAndFeelDefaults one (String text) {
		return load ("t", Map.of ("t", text));
	}

	@Test
	public void colours () {
		LookAndFeelDefaults d = one ("""
			a = #abc
			b = #abcd
			c = #102030
			e = #11223344
			""");
		assertEquals (0xFFAABBCC, d.color ("a", 0));
		assertEquals (0xDDAABBCC, d.color ("b", 0));
		assertEquals (0xFF102030, d.color ("c", 0));
		assertEquals (0x44112233, d.color ("e", 0));
		assertEquals (LookAndFeelDefaults.COLOR, d.kind (d.index ("a")));
	}

	@Test
	public void integersBooleansInsets () {
		LookAndFeelDefaults d = one ("""
			# comment
			! another comment

			n = -12
			t = true
			f = false
			i = 1, 2,3 ,4
			""");
		assertEquals (-12, d.integer ("n", 0));
		assertTrue (d.bool ("t", false));
		assertFalse (d.bool ("f", true));
		assertArrayEquals (new int [] {1, 2, 3, 4}, d.insets ("i"));
		assertEquals ("1,2,3,4", d.format ("i"));
		assertEquals ("-12", d.format ("n"));
		assertEquals ("true", d.format ("t"));
		assertEquals ("false", d.format ("f"));
		assertNull (d.format ("missing"));
		assertEquals ("#ffaabbcc", one ("a = #abc\n").format ("a"));
		assertEquals (7, d.integer ("missing", 7));
		assertEquals (5, d.integer ("t", 5), "a boolean is not an integer");
		assertEquals (9, d.color ("n", 9), "an integer is not a colour");
		assertEquals (6, d.color ("missing", 6));
		assertTrue (d.bool ("n", true), "an integer is not a boolean");
		assertFalse (d.bool ("missing", false));
		assertEquals (-12, d.value (d.index ("n")), "the packed value painting reads");
		assertNull (d.insets ("n"));
		assertNull (d.insets ("missing"));
		assertEquals (4, d.size ());
		assertArrayEquals (new String [] {"f", "i", "n", "t"}, d.keys ());
		assertTrue (d.toString ().contains ("4 keys"), d.toString ());
	}

	@Test
	public void functions () {
		LookAndFeelDefaults d = one ("""
			lighter = lighten(#ff0000, 25%)
			darker = darken(#ffffff, 20%)
			faded = fade(#102030, 50%)
			mixed = mix(#ffffff, #000000, 25%)
			nested = darken(lighten(#000000, 40%), 20%)
			platform = system(COLOR_LIST_SELECTION)
			""");
		assertEquals (0xFFFF8080, d.color ("lighter", 0));
		assertEquals (0xFFCCCCCC, d.color ("darker", 0));
		assertEquals (0x80102030, d.color ("faded", 0));
		assertEquals (0xFF404040, d.color ("mixed", 0));
		assertEquals (0xFF333333, d.color ("nested", 0));
		assertEquals (0xFF0078D7, d.color ("platform", 0));
		LookAndFeelDefaults edges = one ("""
			greenBlack = darken(#00ff00, 100%)
			blueBlack = darken(#0000ff, 100%)
			roseBlack = darken(#ff0080, 100%)
			redWhite = lighten(#ff0000, 100%)
			fullFade = fade(#102030, 150%)
			noSign = mix(#ffffff, #000000, 50)
			""");
		assertEquals (0xFF000000, edges.color ("greenBlack", 1), "lightness clamps at 0");
		assertEquals (0xFF000000, edges.color ("blueBlack", 1));
		assertEquals (0xFF000000, edges.color ("roseBlack", 1));
		assertEquals (0xFFFFFFFF, edges.color ("redWhite", 0), "lightness clamps at 100");
		assertEquals (0xFF102030, edges.color ("fullFade", 0), "alpha clamps at 100 percent");
		assertEquals (0xFF808080, edges.color ("noSign", 0), "a bare number is a percentage too");
	}

	@Test
	public void referencesVariablesAndInheritance () {
		Map<String, String> tables = Map.of (
			"base", """
				@accent = #112233
				Button.background = @accent
				Button.border = darken($Button.background, 0%)
				Table.selection = $Button.background
				Table.rowHeight = 20
				""",
			"derived", """
				@extends = base
				@accent = #445566
				Table.rowHeight = 24
				""");
		LookAndFeelDefaults base = load ("base", tables), derived = load ("derived", tables);
		assertEquals (0xFF112233, base.color ("Table.selection", 0));
		assertEquals (0xFF445566, derived.color ("Button.background", 0), "a variable overridden by the derived table");
		assertEquals (0xFF445566, derived.color ("Table.selection", 0), "references resolve after inheritance");
		assertEquals (0xFF445566, derived.color ("Button.border", 0));
		assertEquals (24, derived.integer ("Table.rowHeight", 0));
		assertEquals (-1, derived.index ("@accent"), "variables are not keys");
		assertEquals ("derived", derived.name ());
	}

	@Test
	public void errors () {
		IllegalArgumentException cycle = assertThrows (IllegalArgumentException.class, () -> one ("a = $b\nb = $a\n"));
		assertTrue (cycle.getMessage ().contains ("circular"), cycle.getMessage ());
		IllegalArgumentException unknown = assertThrows (IllegalArgumentException.class, () -> one ("a = $missing\n"));
		assertTrue (unknown.getMessage ().contains ("unknown key missing"), unknown.getMessage ());
		IllegalArgumentException variable = assertThrows (IllegalArgumentException.class, () -> one ("a = @missing\n"));
		assertTrue (variable.getMessage ().contains ("unknown variable @missing"), variable.getMessage ());
		assertThrows (IllegalArgumentException.class, () -> one ("a = darken(#fff, 5%\n"), "an unclosed function is no function");
		/* a cause without a message (the system colours here) is still named by table and key */
		IllegalArgumentException blank = assertThrows (IllegalArgumentException.class,
			() -> new LookAndFeelLoader (name -> "a = system(COLOR_X)\n", name -> { throw new IllegalArgumentException (); }).load ("t"));
		assertTrue (blank.getMessage ().startsWith ("t: a = "), blank.getMessage ());
		assertThrows (IllegalArgumentException.class, () -> one ("a = #12\n"));
		assertThrows (IllegalArgumentException.class, () -> one ("a = #zzzzzz\n"));
		assertThrows (IllegalArgumentException.class, () -> one ("a = darken(#fff)\n"));
		assertThrows (IllegalArgumentException.class, () -> one ("a = darken(#fff, x%)\n"));
		assertThrows (IllegalArgumentException.class, () -> one ("a = shade(#fff, 2%)\n"));
		assertThrows (IllegalArgumentException.class, () -> one ("a = lighten(12, 2%)\n"));
		assertThrows (IllegalArgumentException.class, () -> one ("a = \n"));
		assertThrows (IllegalArgumentException.class, () -> one ("a = x,2\n"));
		assertThrows (IllegalArgumentException.class, () -> one ("no equals sign\n"));
		assertThrows (IllegalArgumentException.class, () -> one ("a = system(NOT_A_COLOR)\n"));
		IllegalArgumentException extendsCycle = assertThrows (IllegalArgumentException.class,
			() -> load ("x", Map.of ("x", "@extends = y\n", "y", "@extends = x\n")));
		assertTrue (extendsCycle.getMessage ().contains ("circular @extends"), extendsCycle.getMessage ());
		assertThrows (IllegalArgumentException.class, () -> load ("x", Map.of ("x", "@extends = none\n")));
	}

	@Test
	public void builtInTablesDefineEveryKey () {
		LookAndFeelLoader loader = new LookAndFeelLoader (LookAndFeelThemes::text, name -> {
			int rgb = LookAndFeelThemes.fallbackSystemColor (name);
			if (rgb < 0) throw new IllegalArgumentException (name);
			return rgb;
		});
		assertEquals (Set.of ("flat", "flatdark", "initiative31", "native", "swing"), LookAndFeelThemes.names ());
		Map<String, LookAndFeelDefaults> tables = new HashMap<> ();
		for (String name : LookAndFeelThemes.names ()) {
			LookAndFeelDefaults defaults = loader.load (name);
			tables.put (name, defaults);
			for (String key : LookAndFeelThemes.KEYS) {
				assertTrue (defaults.index (key) >= 0, name + " lacks " + key);
			}
		}
		assertEquals (0xFFF0F0F0, tables.get ("native").color ("Component.background", 0));
		assertEquals (0xFF0078D7, tables.get ("native").color ("Table.selectionBackground", 0));
		assertNotEquals (tables.get ("flat").color ("Tree.background", 0), tables.get ("flatdark").color ("Tree.background", 0));
		assertEquals (tables.get ("flat").integer ("Tree.rowHeight", 0), tables.get ("flatdark").integer ("Tree.rowHeight", -1));
		assertEquals (tables.get ("flatdark").color ("List.background", 0), tables.get ("flatdark").color ("Table.background", 1));
		/* swing: Metal (Ocean) values measured by docs/m3/look-and-feel/evidence/SwingDefaultsProbe.java.txt */
		LookAndFeelDefaults swing = tables.get ("swing");
		assertEquals (0xFFEEEEEE, swing.color ("Component.background", 0));
		assertEquals (0xFFB8CFE5, swing.color ("Tree.selectionBackground", 0));
		assertEquals (0xFFEAF1F7, swing.color ("Component.hoverBackground", 0), "mix(#b8cfe5, #ffffff, 30%)");
		assertEquals (16, swing.integer ("Table.rowHeight", 0));
		assertEquals (20, swing.integer ("Tree.indent", 0));
		assertTrue (swing.bool ("Tree.showLines", false));
		assertArrayEquals (new int [] {2, 14, 2, 14}, swing.insets ("Button.margin"));
		/* initiative31: prototype-skija DefaultColorProvider light instance, hsb colours resolved */
		LookAndFeelDefaults i31 = tables.get ("initiative31");
		assertEquals (0xFFE0EEFE, i31.color ("Tree.selectionBackground", 0), "Table.SELECTION_COLOR (224, 238, 254)");
		assertEquals (0xFFFFFF00, i31.color ("Component.hoverBackground", 0), "COLOR_YELLOW hover");
		assertEquals (0xFF0062B8, i31.color ("Button.default.background", 0), "hsb (208, 1, 0.72)");
		assertEquals (0xFF005299, i31.color ("Link.foreground", 0), "hsb (208, 1, 0.6)");
		assertEquals (0xFF808080, i31.color ("Tree.expandIconColor", 0), "COLOR_DARK_GRAY triangles");
		assertEquals (LookAndFeelThemes.text ("swing"), LookAndFeelThemes.text ("metal"), "alias");
		assertEquals (LookAndFeelThemes.text ("initiative31"), LookAndFeelThemes.text ("i31"), "alias");
	}

	@Test
	public void selection () {
		Map<String, String> properties = new HashMap<> ();
		assertEquals ("native", LookAndFeel.requested ("Tree", properties::get));
		properties.put ("swt.lookAndFeel", " ");
		assertEquals ("native", LookAndFeel.requested ("Tree", properties::get), "a blank value is no value");
		properties.put ("swt.lookAndFeel", " Flat ");
		assertEquals ("flat", LookAndFeel.requested ("Tree", properties::get));
		properties.put ("swt.lookAndFeel.Tree", "flatdark");
		assertEquals ("flatdark", LookAndFeel.requested ("Tree", properties::get));
		assertEquals ("flat", LookAndFeel.requested ("Table", properties::get));
		properties.put ("swt.widget.theme", "swing");
		assertEquals ("swing", LookAndFeel.requested ("Table", properties::get), "swt.widget.theme beats the alias");
		assertEquals ("flatdark", LookAndFeel.requested ("Tree", properties::get), "the widget alias beats the global property");
		properties.put ("swt.widget.theme.Tree", "initiative31");
		assertEquals ("initiative31", LookAndFeel.requested ("Tree", properties::get), "the widget property wins");
		assertTrue (LookAndFeel.drawn (WidgetMode.CUSTOM));
		assertTrue (LookAndFeel.drawn (WidgetMode.M3));
		assertTrue (LookAndFeel.drawn (WidgetMode.GPU));
		assertFalse (LookAndFeel.drawn (WidgetMode.VIEWPORT));
		assertFalse (LookAndFeel.drawn (WidgetMode.ORIGINAL));
		assertEquals ("native", LookAndFeel.of ("Tree", WidgetMode.VIEWPORT).name (), "native pixels in the viewport mode");
		assertEquals ("native", LookAndFeel.named ("no-such-look").name (), "an unknown look and feel falls back to native");
	}

	@Test
	public void drawnModeReadsTheProperties () {
		System.setProperty ("swt.widget.theme.CoverageWidget", "flat");
		try {
			assertEquals ("flat", LookAndFeel.of ("CoverageWidget", WidgetMode.CUSTOM).name ());
		} finally {
			System.clearProperty ("swt.widget.theme.CoverageWidget");
		}
		assertEquals ("native", LookAndFeel.of ("CoverageWidget", WidgetMode.GPU).name ());
	}

	@Test
	public void nativeItselfNeverFallsBack () {
		LookAndFeel.register ("native", "broken = $missing\n", null);
		try {
			assertThrows (IllegalArgumentException.class, () -> LookAndFeel.named ("native"));
		} finally {
			LookAndFeel.register ("native", LookAndFeelThemes.text ("native"), null);
		}
		assertEquals ("native", LookAndFeel.named ("native").name ());
		/* a registered table asking for a name that is no SWT colour constant falls back with the cause logged */
		LookAndFeel.register ("test-bad-colour", "a = system(NOT_A_COLOR)\n", null);
		/* COLOR_RED is a colour constant but has no built-in fallback, so it needs a display */
		LookAndFeel.register ("test-no-fallback", "a = system(COLOR_RED)\n", null);
		try {
			assertEquals ("native", LookAndFeel.named ("test-bad-colour").name ());
			if (Display.getCurrent () == null) {
				assertEquals ("native", LookAndFeel.named ("test-no-fallback").name ());
			}
		} finally {
			LookAndFeel.register ("test-bad-colour", "a = #fff\n", null);
			LookAndFeel.register ("test-no-fallback", "a = #fff\n", null);
		}
		assertEquals (-1, LookAndFeelThemes.fallbackSystemColor ("COLOR_RED"), "no fallback for colours the tables do not use");
	}

	@Test
	public void displayResolution () throws InterruptedException {
		Display display = new Display ();
		/* with FINE on, the resolution record is built on this thread and published by the SWT log thread */
		Logger log = Logger.getLogger ("org.eclipse.swt.internal.laf.LookAndFeel");
		BlockingQueue<String> records = new LinkedBlockingQueue<> ();
		Handler handler = new Handler () {
			@Override
			public void publish (LogRecord record) {
				records.add (record.getMessage ());
			}

			@Override
			public void flush () {
			}

			@Override
			public void close () {
			}
		};
		log.addHandler (handler);
		log.setLevel (Level.FINE);
		try {
			LookAndFeel.register ("test-display", "@extends = native\n", null);
			LookAndFeel first = LookAndFeel.named ("test-display");
			assertSame (first, LookAndFeel.named ("test-display"), "resolved once and cached on a display thread");
			int selection = 0xFF000000 | (display.getSystemColor (SWT.COLOR_LIST_SELECTION).getRGB ().red << 16)
				| (display.getSystemColor (SWT.COLOR_LIST_SELECTION).getRGB ().green << 8)
				| display.getSystemColor (SWT.COLOR_LIST_SELECTION).getRGB ().blue;
			assertEquals (selection, first.defaults ().color ("Component.selectionBackground", 0), "the display's platform colour");
			assertTrue (first.toString ().contains ("test-display"), first.toString ());
			String record = records.poll (10, TimeUnit.SECONDS);
			assertNotNull (record, "the debug record of the resolution");
			assertTrue (record.startsWith ("look and feel test-display: "), record);
		} finally {
			LookAndFeel.register ("test-display", "@extends = flat\n", null);
			display.dispose ();
			log.setLevel (null);
			log.removeHandler (handler);
		}
	}

	@Test
	public void registration () {
		Object renderer = new Object ();
		RendererFactory factory = new RendererFactory () {
			@Override
			public <R> R create (Class<R> type, Object widget) {
				return type.isInstance (renderer) ? type.cast (renderer) : null;
			}
		};
		LookAndFeel.register ("test-ocean", "@extends = flat\n@accent = #007799\n", factory);
		try {
			LookAndFeel ocean = LookAndFeel.named ("test-ocean");
			assertEquals ("test-ocean", ocean.name ());
			assertEquals (0xFF007799, ocean.defaults ().color ("Table.selectionBackground", 0));
			assertSame (renderer, ocean.renderers ().create (Object.class, null));
			assertTrue (LookAndFeel.names ().contains ("test-ocean"));
			assertNull (LookAndFeel.named ("flat").renderers ().create (Object.class, null));
			LookAndFeel.register ("test-renderers-only", null, factory);
			assertEquals ("native", LookAndFeel.named ("test-renderers-only").name (), "a factory without a table has no look and feel");
		} finally {
			LookAndFeel.register ("test-ocean", "@extends = flat\n", null);
			LookAndFeel.register ("test-renderers-only", null, null);
		}
	}
}

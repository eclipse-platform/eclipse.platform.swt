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

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.laf.*;
import org.junit.jupiter.api.*;

/**
 * Look and feel seam and defaults table (docs/m3/LOOK_AND_FEEL.md, leaves LF1 and LF2): selection by property and
 * mode, the loader's values, references, variables, functions, inheritance and errors, and the built-in tables.
 * No display is needed.
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
		assertEquals (7, d.integer ("missing", 7));
		assertEquals (5, d.integer ("t", 5), "a boolean is not an integer");
		assertNull (d.insets ("n"));
		assertEquals (4, d.size ());
		assertArrayEquals (new String [] {"f", "i", "n", "t"}, d.keys ());
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
		assertThrows (IllegalArgumentException.class, () -> one ("a = #12\n"));
		assertThrows (IllegalArgumentException.class, () -> one ("a = darken(#fff)\n"));
		assertThrows (IllegalArgumentException.class, () -> one ("a = shade(#fff, 2%)\n"));
		assertThrows (IllegalArgumentException.class, () -> one ("a = lighten(12, 2%)\n"));
		assertThrows (IllegalArgumentException.class, () -> one ("no equals sign\n"));
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
		assertEquals (Set.of ("flat", "flatdark", "native"), LookAndFeelThemes.names ());
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
	}

	@Test
	public void selection () {
		Map<String, String> properties = new HashMap<> ();
		assertEquals ("native", LookAndFeel.requested ("Tree", properties::get));
		properties.put ("swt.lookAndFeel", " Flat ");
		assertEquals ("flat", LookAndFeel.requested ("Tree", properties::get));
		properties.put ("swt.lookAndFeel.Tree", "flatdark");
		assertEquals ("flatdark", LookAndFeel.requested ("Tree", properties::get));
		assertEquals ("flat", LookAndFeel.requested ("Table", properties::get));
		assertTrue (LookAndFeel.drawn (WidgetMode.CUSTOM));
		assertTrue (LookAndFeel.drawn (WidgetMode.SKIJA));
		assertTrue (LookAndFeel.drawn (WidgetMode.GPU));
		assertFalse (LookAndFeel.drawn (WidgetMode.VIEWPORT));
		assertFalse (LookAndFeel.drawn (WidgetMode.ORIGINAL));
		assertEquals ("native", LookAndFeel.of ("Tree", WidgetMode.VIEWPORT).name (), "native pixels in the viewport mode");
		assertEquals ("native", LookAndFeel.named ("no-such-look").name (), "an unknown look and feel falls back to native");
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
		} finally {
			LookAndFeel.register ("test-ocean", "@extends = flat\n", null);
		}
	}
}

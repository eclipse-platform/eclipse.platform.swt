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
import java.util.concurrent.atomic.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.render.*;
import org.junit.jupiter.api.*;

/** The theme API of the drawn planes: built-in themes, builders, the registry, selection by property and at run time. */
public class Test_org_eclipse_swt_internal_render_Themes {
	static final String [] PROPERTIES = {"swt.widget.theme", "swt.widget.theme.Tree", "swt.lookAndFeel", "swt.lookAndFeel.Tree"};
	final Map<String, String> saved = new HashMap<> ();

	/* a built-in theme answers as its look, with the values of its look and feel's table for the widget class */
	static void assertLook (Theme expected, Theme actual) {
		assertLook (expected, actual, "");
	}

	static void assertLook (Theme expected, Theme actual, String message) {
		assertEquals (expected.name, actual.name, message);
		assertEquals (expected.look, actual.look, message);
	}

	/* the drawn rows read every colour of a built-in look and feel from its table (docs/m3/render-modes/LAF_KEYS.tsv) */
	@Test
	public void builtInThemesReadTheirLookAndFeelTables () {
		for (String name : new String [] {"swing", "initiative31", "native"}) {
			System.setProperty ("swt.widget.theme", name);
			for (String widget : new String [] {"Tree", "Table", "List"}) {
				Theme theme = Theme.of (widget);
				org.eclipse.swt.internal.laf.LookAndFeelDefaults table = org.eclipse.swt.internal.laf.LookAndFeel.named (name).defaults ();
				assertEquals (table.color (widget + ".selectionBackground", 0) & 0xFFFFFF, theme.selectionBackground, name + " " + widget);
				assertEquals (table.color (widget + ".selectionInactiveBackground", 0) & 0xFFFFFF, theme.selectionInactiveBackground, name + " " + widget);
				assertEquals (table.color ("Component.hoverBackground", 0) & 0xFFFFFF, theme.hotBackground);
				assertEquals (table.color ("Tree.lineColor", 0) & 0xFFFFFF, theme.hash);
				assertEquals (table.color ("Table.gridColor", 0) & 0xFFFFFF, theme.grid);
				assertEquals (table.color ("Component.focusColor", 0) & 0xFFFFFF, theme.focusColor);
				boolean nativeLook = theme.look == Theme.LOOK_NATIVE;
				assertEquals (nativeLook ? -1 : table.color (widget + ".foreground", 0) & 0xFFFFFF, theme.foreground, "the native look keeps the widget's colours");
				Theme look = "swing".equals (name) ? Theme.SWING : "initiative31".equals (name) ? Theme.INITIATIVE31 : Theme.NATIVE;
				assertLook (look, theme);
				assertEquals (look.checkFill, theme.checkFill, "a key the table leaves out answers the look's value");
			}
		}
		Theme user = Theme.builder ("user").look (Theme.LOOK_SWING).selection (0x123456, 0x654321).build ();
		Themes.select ("Tree", user);
		assertSame (user, Theme.of ("Tree"), "a user-defined theme answers as built");
	}

	@BeforeEach
	public void saveProperties () {
		for (String key : PROPERTIES) {
			saved.put (key, System.getProperty (key));
			System.clearProperty (key);
		}
		Themes.select (null, null);
		Themes.select ("Tree", null);
	}

	@AfterEach
	public void restoreProperties () {
		saved.forEach ((key, value) -> {
			if (value == null) System.clearProperty (key); else System.setProperty (key, value);
		});
		Themes.select (null, null);
		Themes.select ("Tree", null);
		Themes.unregister ("ocean");
	}

	@Test
	public void builtInThemesKeepThePlatformGeometry () {
		for (Theme theme : new Theme [] {Theme.NATIVE, Theme.SWING, Theme.INITIATIVE31}) {
			assertEquals (-1, theme.rowHeight, theme.name);
			assertEquals (-1, theme.indent, theme.name);
			assertEquals (2, theme.textInset, theme.name);
			assertNull (theme.rowPainter);
			assertNull (theme.cellPainter);
			assertNull (theme.expanderOpen);
			assertSame (theme, Themes.find (theme.name));
			assertEquals ("Theme " + theme.name, theme.toString ());
		}
		assertTrue (Theme.NATIVE.parts);
		assertFalse (Theme.SWING.parts);
		assertEquals (Theme.LOOK_INITIATIVE31, Theme.INITIATIVE31.look);
		assertEquals (0xB8CFE5, Theme.SWING.selectionBackground);
		assertEquals (0xE0EEFE, Theme.INITIATIVE31.selectionBackground);
		assertEquals (-1, Theme.NATIVE.foreground);
	}

	@Test
	public void builderValidatesAndDerives () {
		assertThrows (NullPointerException.class, () -> Theme.builder (null));
		assertThrows (IllegalArgumentException.class, () -> Theme.builder ("  "));
		assertThrows (IllegalArgumentException.class, () -> Theme.builder ("x").look (3));
		assertThrows (IllegalArgumentException.class, () -> Theme.builder ("x").look (-1));
		assertThrows (IllegalArgumentException.class, () -> Theme.builder ("x").metrics (-1, -1, -1));
		ImageData image = new ImageData (4, 4, 24, new PaletteData (0xFF0000, 0xFF00, 0xFF));
		assertThrows (IllegalArgumentException.class, () -> Theme.builder ("x").expanderImages (image, null));
		assertThrows (IllegalArgumentException.class, () -> Theme.builder ("x").expanderImages (null, image));
		assertThrows (IllegalArgumentException.class, () -> Theme.builder ("x").checkImages (image, null, image));
		assertThrows (IllegalArgumentException.class, () -> Theme.builder ("x").checkImages (image, image, null));
		Theme ocean = Theme.SWING.derive (" Ocean ").metrics (3, 20, 16).selectionForeground (0xFFFFFF).build ();
		assertEquals ("ocean", ocean.name);
		assertEquals (Theme.LOOK_SWING, ocean.look);
		assertEquals (0xB8CFE5, ocean.selectionBackground);
		assertEquals (0xFFFFFF, ocean.selectionForeground);
		assertEquals (3, ocean.textInset);
		assertEquals (20, ocean.rowHeight);
		assertEquals (16, ocean.indent);
		Theme again = ocean.derive ("ocean2").build ();
		assertEquals (ocean.hash, again.hash);
		assertEquals (ocean.checkFill, again.checkFill);
	}

	@Test
	public void registryKeepsBuiltInsAndAcceptsUserThemes () {
		AtomicInteger told = new AtomicInteger ();
		Runnable listener = told::incrementAndGet;
		Themes.addListener (listener);
		try {
			int generation = Themes.generation ();
			Theme ocean = Theme.builder ("ocean").look (Theme.LOOK_SWING).build ();
			assertTrue (Themes.register (ocean));
			assertFalse (Themes.register (Theme.builder ("native").build ()), "built-in names are reserved");
			assertFalse (Themes.register (Theme.builder ("metal").build ()), "aliases of built-ins are reserved");
			assertSame (ocean, Themes.find ("OCEAN"));
			assertNull (Themes.find ("nothing"));
			assertNull (Themes.find (null));
			assertTrue (Themes.names ().containsAll (List.of ("native", "swing", "initiative31", "metal", "i31", "ocean")));
			Themes.select ("Tree", ocean);
			assertSame (ocean, Theme.of ("Tree"));
			assertTrue (Themes.unregister ("ocean"));
			assertLook (Theme.NATIVE, Theme.of ("Tree"), "unregistering drops the selections of the theme");
			assertFalse (Themes.unregister ("ocean"));
			assertFalse (Themes.unregister ("swing"));
			assertTrue (Themes.generation () > generation);
			assertTrue (told.get () >= 3);
		} finally {
			Themes.removeListener (listener);
		}
		int before = told.get ();
		Themes.select (null, Theme.SWING);
		assertEquals (before, told.get (), "a removed listener is not told");
		assertThrows (NullPointerException.class, () -> Themes.register (null));
		assertThrows (NullPointerException.class, () -> Themes.addListener (null));
	}

	@Test
	public void runTimeSelectionWinsOverProperties () {
		System.setProperty ("swt.widget.theme", "swing");
		assertLook (Theme.SWING, Theme.of ("Tree"));
		Themes.select (null, Theme.INITIATIVE31);
		assertLook (Theme.INITIATIVE31, Theme.of ("Tree"));
		Themes.select ("Tree", Theme.NATIVE);
		assertLook (Theme.NATIVE, Theme.of ("Tree"));
		assertLook (Theme.INITIATIVE31, Theme.of ("List"));
		Themes.select ("Tree", null);
		Themes.select (null, null);
		assertLook (Theme.SWING, Theme.of ("Tree"));
	}

	@Test
	public void propertiesAreReadWidgetFirstThenGlobalThenAliases () {
		assertLook (Theme.NATIVE, Theme.of ("Tree"));
		System.setProperty ("swt.lookAndFeel", "i31");
		assertLook (Theme.INITIATIVE31, Theme.of ("Tree"));
		System.setProperty ("swt.lookAndFeel.Tree", "metal");
		assertLook (Theme.SWING, Theme.of ("Tree"));
		System.setProperty ("swt.widget.theme", "native");
		assertLook (Theme.NATIVE, Theme.of ("Tree"));
		System.setProperty ("swt.widget.theme.Tree", " Swing ");
		assertLook (Theme.SWING, Theme.of ("Tree"));
		System.setProperty ("swt.widget.theme.Tree", "no-such-theme");
		assertLook (Theme.NATIVE, Theme.of ("Tree"), "an unknown name falls back to native");
	}
}

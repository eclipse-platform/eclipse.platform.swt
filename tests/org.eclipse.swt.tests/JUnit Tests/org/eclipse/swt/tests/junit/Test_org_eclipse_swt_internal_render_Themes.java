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
			assertSame (Theme.NATIVE, Theme.of ("Tree"), "unregistering drops the selections of the theme");
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
		assertSame (Theme.SWING, Theme.of ("Tree"));
		Themes.select (null, Theme.INITIATIVE31);
		assertSame (Theme.INITIATIVE31, Theme.of ("Tree"));
		Themes.select ("Tree", Theme.NATIVE);
		assertSame (Theme.NATIVE, Theme.of ("Tree"));
		assertSame (Theme.INITIATIVE31, Theme.of ("List"));
		Themes.select ("Tree", null);
		Themes.select (null, null);
		assertSame (Theme.SWING, Theme.of ("Tree"));
	}

	@Test
	public void propertiesAreReadWidgetFirstThenGlobalThenAliases () {
		assertSame (Theme.NATIVE, Theme.of ("Tree"));
		System.setProperty ("swt.lookAndFeel", "i31");
		assertSame (Theme.INITIATIVE31, Theme.of ("Tree"));
		System.setProperty ("swt.lookAndFeel.Tree", "metal");
		assertSame (Theme.SWING, Theme.of ("Tree"));
		System.setProperty ("swt.widget.theme", "native");
		assertSame (Theme.NATIVE, Theme.of ("Tree"));
		System.setProperty ("swt.widget.theme.Tree", " Swing ");
		assertSame (Theme.SWING, Theme.of ("Tree"));
		System.setProperty ("swt.widget.theme.Tree", "no-such-theme");
		assertSame (Theme.NATIVE, Theme.of ("Tree"), "an unknown name falls back to native");
	}
}

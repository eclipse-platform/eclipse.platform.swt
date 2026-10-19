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

import org.eclipse.swt.internal.laf.*;
import org.junit.jupiter.api.*;

/**
 * Derived values that follow their bases (docs/m3/LOOK_AND_FEEL.md, LF6, improvement 2): a key or a variable set in
 * the developer layer recomputes the values derived from it, transitively, and nothing else. No display.
 */
public class Test_org_eclipse_swt_internal_laf_M3UIDerivation {
	static final String BASE = """
		@accent = #102030
		Derive.a = @accent
		Derive.b = darken (@accent, 10%)
		Derive.c = $Derive.a
		Derive.d = #ffffff
		Derive.size = 3
		Derive.flag = true
		Derive.margin = 1,2,3,4
		Derive.sizeCopy = $Derive.size
		Derive.flagCopy = $Derive.flag
		Derive.marginCopy = $Derive.margin
		""";
	static final LookAndFeelLoader LOADER = new LookAndFeelLoader (Map.of ("derive", BASE, "derive2", "@extends = derive\nDerive.d = $Derive.c\n")::get, name -> 0);

	static int key (String name, byte kind) {
		return M3UIKeys.intern (name, kind);
	}

	static final int A = key ("Derive.a", M3UIKeys.COLOR), B = key ("Derive.b", M3UIKeys.COLOR), C = key ("Derive.c", M3UIKeys.COLOR), D = key ("Derive.d", M3UIKeys.COLOR);
	static final int SIZE = key ("Derive.size", M3UIKeys.INTEGER), SIZE_COPY = key ("Derive.sizeCopy", M3UIKeys.INTEGER);
	static final int FLAG = key ("Derive.flag", M3UIKeys.BOOLEAN), FLAG_COPY = key ("Derive.flagCopy", M3UIKeys.BOOLEAN);
	static final int MARGIN = key ("Derive.margin", M3UIKeys.INSETS), MARGIN_COPY = key ("Derive.marginCopy", M3UIKeys.INSETS);

	M3UIDefaults.Lanes developer;
	M3UIDefaults defaults;

	@BeforeEach
	public void setUp () {
		developer = new M3UIDefaults.Lanes ();
		defaults = M3UIDefaults.of (LOADER, "derive", developer);
	}

	@Test
	public void aKeyBaseRecomputesOnlyItsDependents () {
		int b = defaults.color (B, 0);
		assertEquals (0, defaults.derivedCount (), "no base set");
		defaults.put (A, 0xFF445566);
		assertEquals (0xFF445566, defaults.color (A, 0), "the developer value");
		assertEquals (0xFF445566, defaults.color (C, 0), "derived from Derive.a");
		assertEquals (b, defaults.color (B, 0), "derived from @accent, not from Derive.a");
		assertEquals (1, defaults.derivedCount (), "only Derive.c was resolved again");
		defaults.remove (A);
		assertEquals (0xFF102030, defaults.color (C, 0), "back to the theme");
		defaults.put (D, 0xFF000000);
		assertEquals (0, defaults.derivedCount (), "nothing derives from Derive.d");
	}

	@Test
	public void aVariableRecomputesEveryValueDerivedFromIt () {
		int d = defaults.color (D, 0);
		defaults.putVariable ("accent", "#ff0000");
		assertEquals (0xFFFF0000, defaults.color (A, 0));
		assertEquals (0xFFFF0000, defaults.color (C, 0), "through Derive.a");
		assertEquals (0xFFCC0000, defaults.color (B, 0), "darken (#ff0000, 10%)");
		assertEquals (d, defaults.color (D, 0));
		assertEquals (3, defaults.derivedCount ());
		IllegalArgumentException bad = assertThrows (IllegalArgumentException.class, () -> defaults.putVariable ("accent", "nonsense"));
		assertTrue (bad.getMessage ().contains ("derive"), bad.getMessage ());
		assertEquals (0xFFFF0000, defaults.color (A, 0), "the earlier variable is kept after a failure");
		defaults.removeVariable ("accent");
		assertEquals (0xFF102030, defaults.color (A, 0));
		defaults.putVariable ("coverageUnused", "#000000");
		assertEquals (0, defaults.derivedCount (), "a variable the table does not use");
		IllegalArgumentException first = assertThrows (IllegalArgumentException.class, () -> defaults.putVariable ("accent", "nonsense"));
		assertNotNull (first.getMessage ());
		assertEquals (0xFF102030, defaults.color (A, 0), "no variable before, none after the failure");
	}

	@Test
	public void integersBooleansAndInsetsAreBasesToo () {
		defaults.put (SIZE, 7);
		defaults.put (FLAG, 0);
		defaults.putInsets (MARGIN, 9, 8, 7, 6);
		assertEquals (7, defaults.integer (SIZE_COPY, 0));
		assertFalse (defaults.bool (FLAG_COPY, true));
		int [] insets = new int [4];
		assertTrue (defaults.insets (MARGIN_COPY, insets));
		assertArrayEquals (new int [] {9, 8, 7, 6}, insets);
		assertEquals (3, defaults.derivedCount ());
		defaults.put (FLAG, 1);
		assertTrue (defaults.bool (FLAG_COPY, false));
	}

	@Test
	public void everyLookAndFeelOfTheLayerFollows () {
		M3UIDefaults other = M3UIDefaults.of (LOADER, "derive2", developer);
		M3UIDefaults fixed = new M3UIDefaults (LOADER.load ("derive"), developer);
		defaults.putVariable ("accent", "#00ff00");
		assertEquals (0xFF00FF00, other.color (D, 0), "derive2: Derive.d = $Derive.c = $Derive.a = @accent");
		assertEquals (0xFFFFFFFF, defaults.color (D, 0));
		assertEquals (0xFF102030, fixed.color (A, 0), "static defaults do not derive");
		assertEquals (0, fixed.derivedCount ());
		long [] changed = M3UIDefaults.diff (defaults, other);
		assertNotEquals (0, changed [D >>> 6] & (1L << D), "the diff sees the derived value");
	}
}

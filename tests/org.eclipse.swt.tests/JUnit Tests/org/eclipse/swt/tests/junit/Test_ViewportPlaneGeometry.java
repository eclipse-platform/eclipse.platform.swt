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
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.*;

import org.junit.jupiter.api.Test;

/** Arithmetic of plane geometry (package-private <code>ViewportPlaneGeometry</code>); no display needed. */
public class Test_ViewportPlaneGeometry {
	static final int BAND = constant ("BAND"), LIMIT = constant ("LIMIT"), VISIBLE_CAP = constant ("VISIBLE_CAP");

	static int constant (String name) {
		try {
			Field field = Class.forName ("org.eclipse.swt.widgets.ViewportPlaneGeometry").getDeclaredField (name);
			field.setAccessible (true);
			return field.getInt (null);
		} catch (ReflectiveOperationException e) {
			throw new AssertionError (e);
		}
	}

	static Object call (String name, Class<?> [] types, Object... args) {
		try {
			Method method = Class.forName ("org.eclipse.swt.widgets.ViewportPlaneGeometry").getDeclaredMethod (name, types);
			method.setAccessible (true);
			return method.invoke (null, args);
		} catch (ReflectiveOperationException e) {
			throw new AssertionError (e);
		}
	}

	static final Class<?> [] BASE = {int.class, int.class, int.class, int.class, int.class};

	static int base (int position, int size, int parentBase, int visible, int current) {
		return (Integer) call ("base", BASE, position, size, parentBase, visible, current);
	}

	static int nativePosition (int position, int base, int parentBase) {
		return (Integer) call ("nativePosition", new Class<?> [] {int.class, int.class, int.class}, position, base, parentBase);
	}

	@Test
	public void theNativeRangeIsTheMeasuredOne () {
		assertEquals (32767, LIMIT);
		assertTrue (BAND < LIMIT, "a clamped child of a plane must stay outside the band");
		assertTrue ((Boolean) call ("fits", new Class<?> [] {int.class, int.class}, -32768, BAND));
		assertFalse ((Boolean) call ("fits", new Class<?> [] {int.class, int.class}, -32769, 10));
		assertFalse ((Boolean) call ("fits", new Class<?> [] {int.class, int.class}, 32768, 10));
		assertFalse ((Boolean) call ("fits", new Class<?> [] {int.class, int.class}, 0, BAND + 1));
	}

	@Test
	public void aControlInsideTheBandHasNoBase () {
		assertEquals (0, base (-5000, BAND, 0, 300, 0));
		assertEquals (0, base (-5000, BAND, 0, 300, 777));
	}

	@Test
	public void theBandCoversTheVisiblePartAndKeepsItsBase () {
		int size = 1_000_000, visible = 300;
		for (int origin = 0; origin <= size - visible; origin += 997) {
			int b = base (-origin, size, 0, visible, 0);
			assertTrue (b <= origin && origin + visible <= b + BAND, "origin " + origin + " base " + b);
			assertTrue (b >= 0 && b <= size - BAND);
			int nativeY = nativePosition (-origin, b, 0);
			assertTrue (nativeY <= 0 && nativeY + BAND >= visible, "the native band covers the parent's client area");
			/* hysteresis: a step that stays inside the band keeps the base */
			int next = Math.min (size - visible, origin + 10);
			if (next + visible <= b + BAND && next >= b) assertEquals (b, base (-next, size, 0, visible, b));
		}
	}

	@Test
	public void rebasesAreRare () {
		int size = 10_000_000, visible = 600, b = 0, rebases = 0;
		for (int origin = 0; origin <= size - visible; origin += 10) {
			int next = base (-origin, size, 0, visible, b);
			if (next != b) rebases++;
			b = next;
		}
		assertTrue (rebases <= size / ((BAND - Math.max (visible, VISIBLE_CAP)) / 2) + 1, "rebases " + rebases);
	}

	@Test
	public void anInvisiblePlaneIsClampedOutOfSight () {
		int size = 200_000;
		int b = base (-300_000, size, 0, 300, 0);
		int y = nativePosition (-300_000, b, 0);
		assertEquals (-LIMIT - 1, y);
		assertTrue (y + BAND < 0);
		b = base (40_000, size, 0, 300, 0);
		assertEquals (LIMIT, nativePosition (40_000, b, 0));
	}

	@Test
	public void childrenOfAPlaneAreRelativeToItsBase () {
		assertEquals (970 - 500, nativePosition (970, 0, 500));
		assertEquals (LIMIT, nativePosition (999_970, 0, 500));
		assertEquals (-LIMIT - 1, nativePosition (0, 0, 900_000));
		assertEquals (0, nativePosition (Integer.MAX_VALUE - 1, 0, Integer.MAX_VALUE - 1));
		assertEquals (LIMIT, nativePosition (Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE), "no int overflow");
	}
}

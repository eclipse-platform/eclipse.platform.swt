/*******************************************************************************
 * Copyright (c) 2026 vogella GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - tests for viewport mode, memory pressure handling
 *******************************************************************************/
package org.eclipse.swt.tests.cocoa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.swt.SWT;
import org.junit.jupiter.api.Test;

public class Test_cocoa_Platform {

	@Test
	public void test_platformIsCocoa() {
		assertEquals("cocoa", SWT.getPlatform());
	}
	/**
	 * The original Cocoa Tree itself owns paint admission. Its pressure rule
	 * must remain identical to the existing, package-owned SWT viewport
	 * oracle; no extra widget, Viewport adapter or SWT public API is added.
	 */
	@Test
	public void test_cocoaVirtualTreePaintOverscanUsesExistingSwtPolicy() throws Exception {
		Method actual = Class.forName ("org.eclipse.swt.widgets.Tree")
				.getDeclaredMethod ("adaptiveVirtualPaintOverscan",
						int.class, long.class, long.class);
		actual.setAccessible (true);
		Method oracle = Class.forName ("org.eclipse.swt.widgets.VirtualTreeViewport")
				.getDeclaredMethod ("adaptiveOverscan",
						int.class, int.class, int.class, long.class, long.class);
		oracle.setAccessible (true);
		int [] visibleCounts = {0, 1, 25, 40, 200, 8_192, 16_384, 50_000};
		long [] heaps = {32L << 20, 256L << 20, 4L << 30};
		for (int visible : visibleCounts) {
			int admitted = Math.min (16_384, visible);
			for (long heap : heaps) {
				for (long used : new long [] {0L, heap / 2, heap - (1L << 20), heap}) {
					int expected = (int)oracle.invoke (null, 8, admitted,
							(16_384 - admitted) / 2, heap, used);
					int result = (int)actual.invoke (null, visible, heap, used);
					assertEquals (expected, result, "visible=" + visible + " heap=" + heap);
					assertTrue (result >= 0 && result <= (16_384 - admitted) / 2);
				}
			}
		}
		assertEquals (0, actual.invoke (null, 40, 32L << 20, 32L << 20));
		assertEquals (0, actual.invoke (null, 0, 32L << 20, 0L));
	}

	@Test
	public void test_cocoaVirtualTreeInvalidPressureRefused() throws Exception {
		Method actual = Class.forName ("org.eclipse.swt.widgets.Tree")
				.getDeclaredMethod ("adaptiveVirtualPaintOverscan",
						int.class, long.class, long.class);
		actual.setAccessible (true);
		InvocationTargetException failure = assertThrows (InvocationTargetException.class,
				() -> actual.invoke (null, -1, 256L << 20, 0L));
		assertTrue (failure.getCause () instanceof IllegalArgumentException);
		failure = assertThrows (InvocationTargetException.class,
				() -> actual.invoke (null, 40, 32L << 20, 33L << 20));
		assertTrue (failure.getCause () instanceof IllegalArgumentException);
	}

}

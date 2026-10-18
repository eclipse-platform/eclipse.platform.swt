// Copyright 2026 Hitesh Soliwal and Contributors to the Synexia Project.
// SPDX-License-Identifier: Apache-2.0
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Random;

import org.eclipse.swt.internal.ViewportRuntime;
import org.eclipse.swt.internal.ViewportRuntime.RowWindow;
import org.junit.jupiter.api.Test;

/** Headless contracts for visible and overscan row admission; no native widget fixtures. */
public class Test_ViewportRowWindow {

	@Test
	public void defaultViewportDoesNotPaintBeforeLayout() {
		RowWindow window = new RowWindow();
		assertEmpty(window);
		window.setLogicalCount(10_000_000L);
		assertEquals(0, window.visibleCount());
		assertEmpty(window);
	}

	@Test
	public void emptyViewportSuppressesOverscanAtStartMiddleAndTail() {
		for (int overscan : new int[] {0, 1, 8, Integer.MAX_VALUE}) {
			for (long first : new long[] {0, 1, 500, 999, 1000, Long.MAX_VALUE}) {
				RowWindow window = new RowWindow();
				window.setLogicalCount(1000);
				window.setViewport(first, 0);
				window.setOverscanRows(overscan);
				assertEquals(Math.min(first, 999), window.firstVisible());
				assertEmpty(window);
			}
		}
	}

	@Test
	public void collapseAndReopenRestoreAnchorAndOrdinaryOverscan() {
		RowWindow window = new RowWindow();
		window.setLogicalCount(10_000_000L);
		window.setViewport(5_000_000L, 40);
		assertEquals(4_999_992L, window.paintStart());
		assertEquals(5_000_048L, window.paintEndExclusive());
		assertEquals(56, window.paintCount());
		window.setViewport(5_000_000L, 0);
		assertEquals(5_000_000L, window.firstVisible());
		assertEmpty(window);
		window.setViewport(5_000_000L, 40);
		assertEquals(40, window.visibleCount());
		assertEquals(4_999_992L, window.paintStart());
		assertEquals(5_000_048L, window.paintEndExclusive());
		assertEquals(56, window.paintCount());
	}

	@Test
	public void navigationAndModelEditsStayEmptyWhileCollapsed() {
		RowWindow window = new RowWindow();
		window.setLogicalCount(1000);
		window.setViewport(500, 0);
		window.ensureVisible(900);
		assertEquals(900, window.firstVisible());
		assertEmpty(window);
		window.insert(0, 3);
		assertEquals(903, window.firstVisible());
		assertEmpty(window);
		window.remove(0, 2);
		assertEquals(901, window.firstVisible());
		assertEmpty(window);
		window.setLogicalCount(0);
		assertEmpty(window);
		window.setLogicalCount(1000);
		assertEmpty(window);
		window.setViewport(100, 40);
		assertEquals(56, window.paintCount());
	}

	@Test
	public void emptyModelRefillRetainsRequestedVisibleCapacity() {
		RowWindow window = new RowWindow();
		window.setLogicalCount(100);
		window.setViewport(95, 40);
		assertEquals(5, window.visibleCount());
		window.setLogicalCount(0);
		assertEmpty(window);
		window.setLogicalCount(100);
		window.ensureVisible(99);
		assertEquals(60, window.firstVisible());
		assertEquals(40, window.visibleCount());
		assertEquals(52, window.paintStart());
		assertEquals(100, window.paintEndExclusive());
	}

	@Test
	public void nonemptyWindowsMatchIndependentClippedIntervalOracle() {
		for (int count = 1; count <= 24; count++) {
			for (int first = 0; first <= count + 2; first++) {
				for (int visible = 1; visible <= 15; visible++) {
					for (int overscan = 0; overscan <= 8; overscan++) {
						verifyNonemptyWindow(count, first, visible, overscan);
					}
				}
			}
		}
		Random random = new Random(0x535754524f574cL);
		for (int sample = 0; sample < 4096; sample++) {
			long count = sample % 7 == 0 ? Long.MAX_VALUE : random.nextLong(1, Long.MAX_VALUE);
			long first = sample % 5 == 0 ? count - 1 : random.nextLong(count);
			verifyNonemptyWindow(count, first, random.nextInt(1, 513), random.nextInt(1025));
		}
	}

	@Test
	public void collapsedLongExtremaRemainEmptyWithoutOverflow() {
		RowWindow window = new RowWindow();
		window.setLogicalCount(Long.MAX_VALUE);
		window.setOverscanRows(Integer.MAX_VALUE);
		for (long first : new long[] {0, Integer.MAX_VALUE, Long.MAX_VALUE - 1, Long.MAX_VALUE}) {
			window.setViewport(first, 0);
			assertEmpty(window);
		}
	}

	@Test
	public void queriesAndRejectedUpdatesPreserveWindowState() {
		RowWindow window = new RowWindow();
		window.setLogicalCount(1000);
		window.setViewport(500, 0);
		long generation = window.generation();
		assertEmpty(window);
		assertEquals(generation, window.generation());
		assertThrows(IllegalArgumentException.class, () -> window.setViewport(0, -1));
		assertThrows(IllegalArgumentException.class, () -> window.setOverscanRows(-1));
		assertEquals(1000, window.logicalCount());
		assertEquals(500, window.firstVisible());
		assertEquals(0, window.visibleCount());
		assertEquals(8, window.overscanRows());
		assertEquals(generation, window.generation());
	}

	@Test
	public void flatPlannerUsesEmptySharedWindowForZeroHeightGeometry() throws Exception {
		Object planner = planner();
		invoke(planner, "setUniformGeometry", 1000, 20, 0, 400);
		assertEquals(400, invoke(planner, "firstVisible"));
		assertEquals(0, invoke(planner, "visibleCount"));
		assertEquals(400, invoke(planner, "paintStart"));
		assertEquals(400, invoke(planner, "paintEndExclusive"));
		assertEquals(false, invoke(planner, "isPaintCandidate", 399));
		assertEquals(false, invoke(planner, "isPaintCandidate", 400));
		assertEquals(false, invoke(planner, "isPaintCandidate", 407));
		invoke(planner, "setUniformGeometry", 1000, 20, 800, 400);
		assertEquals(40, invoke(planner, "visibleCount"));
		assertEquals(392, invoke(planner, "paintStart"));
		assertEquals(448, invoke(planner, "paintEndExclusive"));
	}

	@Test
	public void flatPlannerRetainsBoundedNonemptyWindow() throws Exception {
		Object planner = planner();
		invoke(planner, "setUniformGeometry", 10_000_000, 20, 800, 5_000_000);
		assertEquals(40, invoke(planner, "visibleCount"));
		assertEquals(4_999_992, invoke(planner, "paintStart"));
		assertEquals(5_000_048, invoke(planner, "paintEndExclusive"));
		assertEquals(true, invoke(planner, "isPaintCandidate", 4_999_992));
		assertEquals(false, invoke(planner, "isPaintCandidate", 4_999_991));
		assertEquals(false, invoke(planner, "isPaintCandidate", 5_000_048));
	}

	private static void verifyNonemptyWindow(long count, long requestedFirst, int requestedVisible, int overscan) {
		RowWindow window = new RowWindow();
		window.setLogicalCount(count);
		window.setViewport(requestedFirst, requestedVisible);
		window.setOverscanRows(overscan);
		long first = Math.min(requestedFirst, count - 1);
		int visible = (int)Math.min(requestedVisible, count - first);
		long lower = BigInteger.valueOf(first).subtract(BigInteger.valueOf(overscan))
				.max(BigInteger.ZERO).longValueExact();
		long upper = BigInteger.valueOf(first).add(BigInteger.valueOf(visible))
				.add(BigInteger.valueOf(overscan)).min(BigInteger.valueOf(count)).longValueExact();
		assertEquals(first, window.firstVisible());
		assertEquals(visible, window.visibleCount());
		assertEquals(lower, window.paintStart());
		assertEquals(upper, window.paintEndExclusive());
		assertEquals(Math.toIntExact(upper - lower), window.paintCount());
		assertTrue(window.isVisible(first));
		assertTrue(window.isPaintCandidate(lower));
		assertFalse(window.isPaintCandidate(upper));
		if (lower > 0) assertFalse(window.isPaintCandidate(lower - 1));
	}

	private static void assertEmpty(RowWindow window) {
		long first = window.firstVisible();
		assertEquals(0, window.visibleCount());
		assertEquals(first, window.paintStart(), "an empty paint range keeps its logical anchor");
		assertEquals(first, window.paintEndExclusive(), "overscan requires a nonempty visible interval");
		assertEquals(0, window.paintCount());
		for (long index : new long[] {-1, 0, first == 0 ? 0 : first - 1, first,
				first == Long.MAX_VALUE ? first : first + 1, Long.MAX_VALUE}) {
			assertFalse(window.isVisible(index));
			assertFalse(window.isPaintCandidate(index));
		}
	}

	private static Object planner() throws Exception {
		Constructor<?> constructor = Class.forName("org.eclipse.swt.widgets.VirtualViewportPlanner")
				.getDeclaredConstructor();
		constructor.setAccessible(true);
		return constructor.newInstance();
	}

	private static Object invoke(Object receiver, String method, int... arguments) throws Exception {
		Class<?>[] types = new Class<?>[arguments.length];
		java.util.Arrays.fill(types, int.class);
		Method target = receiver.getClass().getDeclaredMethod(method, types);
		target.setAccessible(true);
		Object[] values = new Object[arguments.length];
		for (int i = 0; i < arguments.length; i++) values[i] = arguments[i];
		return target.invoke(receiver, values);
	}
}

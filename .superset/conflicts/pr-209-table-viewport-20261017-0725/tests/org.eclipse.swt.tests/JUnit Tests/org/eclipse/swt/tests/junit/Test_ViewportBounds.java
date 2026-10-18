// Copyright 2026 Hitesh Soliwal and Contributors to the Synexia Project.
// SPDX-License-Identifier: Apache-2.0
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Random;
import java.util.function.Consumer;

import org.eclipse.swt.internal.ViewportRuntime;
import org.eclipse.swt.internal.ViewportRuntime.RowLayout;
import org.eclipse.swt.internal.ViewportRuntime.RowWindow;
import org.junit.jupiter.api.Test;

/** Independent two-dimensional viewport and unchanged-scrollbar contracts. */
public class Test_ViewportBounds {

	@Test
	public void zeroWidthWithPositiveHeightAdmitsNoRows() {
		for (int horizontal : new int[] {0, 1, 2}) {
			for (int vertical : new int[] {0, 1, 2}) {
				RowLayout layout = ViewportRuntime.solveRows(0, 800, 0,
						10_000_000L, 20, 400, 16, 16, horizontal, vertical);
				assertEquals(0, layout.bodyWidth());
				assertEquals(0, layout.visibleRows());
			}
		}
	}

	@Test
	public void scrollbarConsumingAllWidthAdmitsNoRows() {
		for (int width : new int[] {1, 8, 16}) {
			RowLayout layout = ViewportRuntime.solveRows(width, 800, 24,
					10_000_000L, 20, 400, 16, 16, 0, 0);
			assertTrue(layout.verticalVisible());
			assertEquals(0, layout.bodyWidth());
			assertTrue(layout.bodyHeight() > 0);
			assertEquals(0, layout.visibleRows());
		}
	}

	@Test
	public void solvedEmptyBodyFeedsAnEmptyRowWindow() {
		RowWindow window = new RowWindow();
		window.setLogicalCount(10_000_000L);
		for (int overscan : new int[] {0, 8, Integer.MAX_VALUE}) {
			window.setOverscanRows(overscan);
			for (int width : new int[] {0, 1, 16}) {
				RowLayout layout = ViewportRuntime.solveRows(width, 800, 24,
						window.logicalCount(), 20, 400, 16, 16, 0, 1);
				window.setViewport(5_000_000L, layout.visibleRows());
				assertEmpty(window);
			}
		}
	}

	@Test
	public void collapseMutationAndReopenRetainLogicalAnchor() {
		RowWindow window = new RowWindow();
		window.setLogicalCount(10_000_000L);
		window.setViewport(5_000_000L, layout(640, 800).visibleRows());
		assertEquals(56, window.paintCount());
		window.setViewport(window.firstVisible(), layout(0, 800).visibleRows());
		assertEmpty(window);
		window.insert(0, 3);
		window.remove(0, 1);
		assertEquals(5_000_002L, window.firstVisible());
		assertEmpty(window);
		window.setViewport(window.firstVisible(), layout(640, 800).visibleRows());
		assertEquals(40, window.visibleCount());
		assertEquals(56, window.paintCount());
		assertEquals(4_999_994L, window.paintStart());
	}

	@Test
	public void commonLayoutAdapterPropagatesEmptyWidth() throws Exception {
		Class<?> owner = Class.forName("org.eclipse.swt.widgets.ViewportScrollLayout");
		Method solve = owner.getDeclaredMethod("solve", int.class, int.class, int.class,
				long.class, int.class, long.class, int.class, int.class, int.class, int.class);
		solve.setAccessible(true);
		Object result = solve.invoke(null, 0, 800, 0, 10_000_000L, 20, 400L, 16, 16, 2, 2);
		Method visible = result.getClass().getDeclaredMethod("visibleRows");
		visible.setAccessible(true);
		RowWindow window = new RowWindow();
		window.setLogicalCount(10_000_000L);
		window.setViewport(5_000_000L, (int)visible.invoke(result));
		assertEmpty(window);
	}

	@Test
	public void exhaustiveSmallLayoutsMatchIndependentFixedPointFields() {
		long[] visited = {0};
		forEachSmall(input -> {
			assertFields(input, solve(input), oracle(input), false);
			visited[0]++;
		});
		assertEquals(291_600, visited[0]);
	}

	@Test
	public void exhaustiveSmallLayoutsEnforceTwoDimensionalVisibility() {
		forEachSmall(input -> assertEquals(oracle(input).visibleRows(), solve(input).visibleRows(),
				() -> input.toString()));
	}

	@Test
	public void seededIntegerAndLongExtremaMatchIndependentOracle() {
		Random random = new Random(0x535754424f554e44L);
		for (int sample = 0; sample < 4096; sample++) {
			int width = sample % 7 == 0 ? 0 : random.nextInt(Integer.MAX_VALUE);
			int height = sample % 11 == 0 ? Integer.MAX_VALUE : random.nextInt(Integer.MAX_VALUE);
			int header = sample % 13 == 0 || height == 0 ? height : random.nextInt(height + (height == Integer.MAX_VALUE ? 0 : 1));
			long rows = sample % 3 == 0 ? Long.MAX_VALUE : random.nextLong(Long.MAX_VALUE);
			Input input = new Input(width, height, header, rows, random.nextInt(1, Integer.MAX_VALUE),
					sample % 5 == 0 ? Long.MAX_VALUE : random.nextLong(Long.MAX_VALUE),
					random.nextInt(Integer.MAX_VALUE), random.nextInt(Integer.MAX_VALUE),
					random.nextInt(3), random.nextInt(3));
			assertFields(input, solve(input), oracle(input), true);
		}
	}

	@Test
	public void zeroHeightAndPriorEmptyWindowCorrectionRemainEffective() {
		RowWindow window = new RowWindow();
		window.setLogicalCount(Long.MAX_VALUE);
		window.setOverscanRows(Integer.MAX_VALUE);
		assertEmpty(window);
		for (int width : new int[] {0, 1, 16, Integer.MAX_VALUE}) {
			RowLayout layout = ViewportRuntime.solveRows(width, 24, 24,
					Long.MAX_VALUE, Integer.MAX_VALUE, Long.MAX_VALUE,
					16, 16, 0, 0);
			assertEquals(0, layout.visibleRows());
			window.setViewport(Long.MAX_VALUE - 1, layout.visibleRows());
			assertEmpty(window);
		}
	}

	@Test
	public void invalidGeometryRetainsExistingExceptionContract() {
		Input valid = new Input(400, 200, 24, 1000, 20, 600, 16, 16, 0, 0);
		long[][] bad = {{-1,200,24,1000,20,600,16,16,0,0}, {400,-1,0,1000,20,600,16,16,0,0},
				{400,200,-1,1000,20,600,16,16,0,0}, {400,200,201,1000,20,600,16,16,0,0},
				{400,200,24,-1,20,600,16,16,0,0}, {400,200,24,1000,0,600,16,16,0,0},
				{400,200,24,1000,20,-1,16,16,0,0}, {400,200,24,1000,20,600,-1,16,0,0},
				{400,200,24,1000,20,600,16,-1,0,0}, {400,200,24,1000,20,600,16,16,-1,0},
				{400,200,24,1000,20,600,16,16,0,3}};
		RowLayout before = solve(valid);
		for (long[] x : bad) {
			assertThrows(IllegalArgumentException.class, () -> ViewportRuntime.solveRows(
					(int)x[0], (int)x[1], (int)x[2], x[3], (int)x[4], x[5],
					(int)x[6], (int)x[7], (int)x[8], (int)x[9]));
			assertEquals(before, solve(valid));
		}
	}

	@Test
	public void largeNonemptyOverscanRetainsExplicitIntOverflowContract() {
		RowWindow window = new RowWindow();
		window.setLogicalCount(Long.MAX_VALUE);
		window.setViewport(5_000_000_000L, 1);
		window.setOverscanRows(Integer.MAX_VALUE);
		assertEquals(4_294_967_295L, window.paintEndExclusive() - window.paintStart());
		assertThrows(ArithmeticException.class, window::paintCount);
		long generation = window.generation();
		assertTrue(window.isPaintCandidate(5_000_000_000L));
		assertEquals(generation, window.generation());
	}

	private static RowLayout layout(int width, int height) {
		return ViewportRuntime.solveRows(width, height, 0, 10_000_000L, 20, 400, 16, 16, 2, 2);
	}

	private static void assertEmpty(RowWindow window) {
		assertEquals(0, window.visibleCount());
		assertEquals(window.firstVisible(), window.paintStart());
		assertEquals(window.firstVisible(), window.paintEndExclusive());
		assertEquals(0, window.paintCount());
		assertFalse(window.isPaintCandidate(window.firstVisible()));
	}

	private record Input(int width, int height, int header, long rows, int rowHeight,
			long contentWidth, int hbar, int vbar, int hp, int vp) {
	}

	private static RowLayout solve(Input x) {
		return ViewportRuntime.solveRows(x.width, x.height, x.header, x.rows, x.rowHeight,
				x.contentWidth, x.hbar, x.vbar, x.hp, x.vp);
	}

	/** Enumerates all four visibility states; chooses the least valid state, not iterative production code. */
	private static RowLayout oracle(Input x) {
		for (int state : new int[] {0, 1, 2, 3}) {
			boolean h = (state & 1) != 0, v = (state & 2) != 0;
			int width = BigInteger.valueOf(x.width).subtract(BigInteger.valueOf(v ? x.vbar : 0))
					.max(BigInteger.ZERO).intValueExact();
			int height = BigInteger.valueOf(x.height).subtract(BigInteger.valueOf(x.header))
					.subtract(BigInteger.valueOf(h ? x.hbar : 0)).max(BigInteger.ZERO).intValueExact();
			int rowCapacity = BigInteger.valueOf(height).add(BigInteger.valueOf(x.rowHeight - 1L))
					.divide(BigInteger.valueOf(x.rowHeight)).min(BigInteger.valueOf(x.rows)).intValueExact();
			if (h != policy(x.hp, x.contentWidth > width) || v != policy(x.vp, x.rows > rowCapacity)) continue;
			long extent = BigInteger.valueOf(x.rows).multiply(BigInteger.valueOf(x.rowHeight))
					.min(BigInteger.valueOf(Long.MAX_VALUE)).longValueExact();
			return new RowLayout(h, v, h && v, width, height, width, x.header,
					width == 0 ? 0 : rowCapacity, x.rows, extent, x.contentWidth);
		}
		throw new AssertionError("No fixed point for " + x);
	}

	private static boolean policy(int policy, boolean need) {
		return policy == 1 || policy == 0 && need;
	}

	private static void assertFields(Input input, RowLayout actual, RowLayout expected, boolean includeRows) {
		assertEquals(expected.horizontalVisible(), actual.horizontalVisible(), input::toString);
		assertEquals(expected.verticalVisible(), actual.verticalVisible(), input::toString);
		assertEquals(expected.cornerVisible(), actual.cornerVisible(), input::toString);
		assertEquals(expected.bodyWidth(), actual.bodyWidth(), input::toString);
		assertEquals(expected.bodyHeight(), actual.bodyHeight(), input::toString);
		assertEquals(expected.headerWidth(), actual.headerWidth(), input::toString);
		assertEquals(expected.headerHeight(), actual.headerHeight(), input::toString);
		assertEquals(expected.logicalRows(), actual.logicalRows(), input::toString);
		assertEquals(expected.estimatedContentHeight(), actual.estimatedContentHeight(), input::toString);
		assertEquals(expected.logicalContentWidth(), actual.logicalContentWidth(), input::toString);
		if (includeRows) assertEquals(expected.visibleRows(), actual.visibleRows(), input::toString);
	}

	private static void forEachSmall(Consumer<Input> action) {
		for (int width : new int[] {0, 1, 4, 17, 31})
		for (int height : new int[] {0, 1, 7, 20, 41})
		for (int header : java.util.Arrays.stream(new int[] {0, Math.min(1, height), height}).distinct().toArray())
		for (long rows : new long[] {0, 1, 2, 7, 100})
		for (int rowHeight : new int[] {1, 3, 8})
		for (long contentWidth : new long[] {0, 1, 17, 100})
		for (int hbar : new int[] {0, 1, 17})
		for (int vbar : new int[] {0, 1, 17})
		for (int hp = 0; hp < 3; hp++)
		for (int vp = 0; vp < 3; vp++)
			action.accept(new Input(width, height, header, rows, rowHeight, contentWidth, hbar, vbar, hp, vp));
	}
}

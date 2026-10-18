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

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.ViewportPaintGraph.Affine;
import org.eclipse.swt.widgets.Display;
import org.junit.jupiter.api.*;

@SuppressWarnings("restriction")
public class Test_org_eclipse_swt_internal_ViewportGcProxy {

	private Display display;
	private Image image;
	private GC gc;

	@BeforeEach
	public void setUp () {
		display = Display.getDefault ();
		image = new Image (display, 96, 96);
		gc = new GC (image);
	}

	@AfterEach
	public void tearDown () {
		if (gc != null && !gc.isDisposed ()) gc.dispose ();
		if (image != null && !image.isDisposed ()) image.dispose ();
	}

	@Test
	public void test_affineV2FactoriesInverseAndClassification () {
		assertSame (Affine.IDENTITY, Affine.translation (0, 0));
		assertSame (Affine.IDENTITY, Affine.scale (1, 1));
		assertSame (Affine.IDENTITY, Affine.rotation (0));
		assertSame (Affine.IDENTITY, Affine.shear (0, 0));

		Affine quarterTurn = Affine.rotation ((float)(Math.PI / 2d));
		assertEquals (0f, quarterTurn.m11 ());
		assertEquals (1f, quarterTurn.m12 ());
		assertEquals (-1f, quarterTurn.m21 ());
		assertEquals (0f, quarterTurn.m22 ());
		assertEquals (1f, quarterTurn.determinant (), 0.0001f);
		assertFalse (quarterTurn.isTranslationOnly ());
		float adjacent = Math.nextUp ((float)(Math.PI / 2d));
		assertNotEquals (0f, Affine.rotation (adjacent).m11 (),
				"only the canonical float quadrant angle may snap to exact integer geometry");
		assertTrue (Affine.translation (4, -7).isTranslationOnly ());
		assertFalse (Affine.shear (1, 0).isTranslationOnly ());

		Affine transform = Affine.translation (40, -10).compose (Affine.scale (2, 4));
		Affine restored = transform.compose (transform.inverse ());
		assertTrue (restored.isIdentity ());

		float [] mapped = new float [2];
		float [] inverse = new float [2];
		quarterTurn.map (1, 2, mapped);
		assertEquals (-2f, mapped [0], 0.0001f);
		assertEquals (1f, mapped [1], 0.0001f);
		assertTrue (quarterTurn.inverseMap (mapped [0], mapped [1], inverse));
		assertArrayEquals (new float [] {1, 2}, inverse, 0.0001f);

		assertThrows (IllegalStateException.class, () -> Affine.scale (0, 1).inverse ());
	}

	@Test
	public void test_gcScopeRestoresCallerStateAfterPlaneMutation () {
		gc.setAdvanced (true);
		if (!gc.getAdvanced ()) return; // Advanced graphics are optional on some native stacks.

		Rectangle originalClip = new Rectangle (3, 5, 70, 60);
		gc.setClipping (originalClip);
		gc.setAlpha (211);
		gc.setAntialias (SWT.OFF);
		gc.setTextAntialias (SWT.ON);
		gc.setInterpolation (SWT.HIGH);
		gc.setFillRule (SWT.FILL_EVEN_ODD);
		gc.setXORMode (true);
		gc.setForeground (display.getSystemColor (SWT.COLOR_DARK_BLUE));
		gc.setBackground (display.getSystemColor (SWT.COLOR_YELLOW));
		LineAttributes originalLine =
				new LineAttributes (3.5f, SWT.CAP_ROUND, SWT.JOIN_BEVEL, SWT.LINE_DASH, null, 0, 10);
		gc.setLineAttributes (originalLine);
		Transform originalTransform = new Transform (display, 1, 0.25f, 0.5f, 1, 7, 9);
		try {
			gc.setTransform (originalTransform);
			Rectangle expectedClip = gc.getClipping ();

			float [] expectedTransform = elements (originalTransform);
			LineAttributes expectedLine = gc.getLineAttributes ();
			Color expectedForeground = gc.getForeground ();
			Color expectedBackground = gc.getBackground ();

			try (ViewportGcProxy proxy = ViewportGcProxy.wrap (gc)) {
				proxy.clip (new Rectangle (20, 20, 30, 20))
						.transform (Affine.rotation (0.37f).compose (Affine.translation (13, -8)))
						.lineAttributes (new LineAttributes (
								9f, SWT.CAP_SQUARE, SWT.JOIN_ROUND, SWT.LINE_SOLID, null, 0, 10))
						.alpha (77);

				GC scoped = proxy.gc ();
				scoped.setAntialias (SWT.ON);
				scoped.setTextAntialias (SWT.OFF);
				scoped.setInterpolation (SWT.LOW);
				scoped.setFillRule (SWT.FILL_WINDING);
				scoped.setXORMode (false);
				scoped.setForeground (display.getSystemColor (SWT.COLOR_RED));
				scoped.setBackground (display.getSystemColor (SWT.COLOR_GREEN));
				assertNotEquals (expectedClip, scoped.getClipping ());
			}

			assertEquals (expectedClip, gc.getClipping ());
			assertArrayEquals (expectedTransform, elements (gc), 0.0001f);
			assertLineEquals (expectedLine, gc.getLineAttributes ());
			assertEquals (211, gc.getAlpha ());
			assertEquals (SWT.OFF, gc.getAntialias ());
			assertEquals (SWT.ON, gc.getTextAntialias ());
			assertEquals (SWT.HIGH, gc.getInterpolation ());
			assertEquals (SWT.FILL_EVEN_ODD, gc.getFillRule ());
			assertTrue (gc.getXORMode ());
			assertEquals (expectedForeground, gc.getForeground ());
			assertEquals (expectedBackground, gc.getBackground ());
		} finally {
			originalTransform.dispose ();
		}
	}

	@Test
	public void test_gcScopePreservesDeviceRegionAcrossAffineRoundTrips () {
		Region expected = new Region (display);
		Region actual = new Region (display);
		Region difference = new Region (display);
		Transform transform = new Transform (display);
		try {
			expected.add (new Rectangle (3, 5, 12, 14));
			expected.add (new Rectangle (45, 51, 17, 19));
			float [][] matrices = {
				{1, 0, 0, 1, 0, 0}, {1, 0, 0, 1, -3, 7},
				{1, 0.25f, 0.5f, 1, 7, 9}, {0.8f, 0.6f, -0.6f, 0.8f, 5, 3},
				{2, 0, 0, 0.5f, -2, 4}, {-1, 0, 0, 1, 80, 0}
			};
			for (float [] matrix : matrices) {
				gc.setTransform (null);
				gc.setClipping (expected);
				transform.setElements (matrix [0], matrix [1], matrix [2], matrix [3], matrix [4], matrix [5]);
				gc.setTransform (transform);
				Rectangle before = gc.getClipping ();
				try (ViewportGcProxy scope = ViewportGcProxy.wrap (gc)) {
					scope.translate (13, -8).clip (new Rectangle (5, 7, 9, 11));
				}
				assertEquals (before, gc.getClipping (), "user-space bounds drift");
				assertArrayEquals (matrix, elements (gc), 0.0001f);
				gc.setTransform (null);
				gc.getClipping (actual);
				difference.add (expected);
				difference.subtract (actual);
				assertTrue (difference.isEmpty (), "device clipping lost pixels");
				difference.add (actual);
				difference.subtract (expected);
				assertTrue (difference.isEmpty (), "device clipping leaked pixels or filled a hole");
			}
		} finally {
			gc.setTransform (null);
			transform.dispose (); difference.dispose (); actual.dispose (); expected.dispose ();
		}
	}

	@Test
	public void test_gcScopeRestoresPatternsFontAndExactNonRectangularClip () {
		gc.setAdvanced (true);
		if (!gc.getAdvanced ()) return; // Advanced graphics are optional.

		Region original = new Region (display);
		Region restored = new Region (display);
		Pattern initialForegroundPattern = null;
		Pattern initialBackgroundPattern = null;
		Pattern temporaryForegroundPattern = null;
		Pattern temporaryBackgroundPattern = null;
		Font changedFont = null;
		try {
			original.add (new Rectangle (2, 2, 8, 8));
			original.add (new Rectangle (30, 30, 8, 8));
			gc.setClipping (original);

			initialForegroundPattern = new Pattern (
					display, 0, 0, 16, 16,
					display.getSystemColor (SWT.COLOR_BLUE),
					display.getSystemColor (SWT.COLOR_CYAN));
			initialBackgroundPattern = new Pattern (
					display, 0, 0, 16, 16,
					display.getSystemColor (SWT.COLOR_WHITE),
					display.getSystemColor (SWT.COLOR_GRAY));
			gc.setForegroundPattern (initialForegroundPattern);
			gc.setBackgroundPattern (initialBackgroundPattern);
			Pattern expectedForegroundPattern = gc.getForegroundPattern ();
			Pattern expectedBackgroundPattern = gc.getBackgroundPattern ();
			Font expectedFont = gc.getFont ();

			try (ViewportGcProxy proxy = ViewportGcProxy.wrap (gc)) {
				GC scoped = proxy.gc ();
				scoped.setClipping (new Rectangle (0, 0, 96, 96));
				temporaryForegroundPattern = new Pattern (
						display, 0, 0, 16, 16,
						display.getSystemColor (SWT.COLOR_RED),
						display.getSystemColor (SWT.COLOR_GREEN));
				temporaryBackgroundPattern = new Pattern (
						display, 0, 0, 16, 16,
						display.getSystemColor (SWT.COLOR_BLACK),
						display.getSystemColor (SWT.COLOR_YELLOW));
				scoped.setForegroundPattern (temporaryForegroundPattern);
				scoped.setBackgroundPattern (temporaryBackgroundPattern);
				changedFont = new Font (
						display,
						expectedFont.getFontData () [0].getName (),
						Math.max (1, expectedFont.getFontData () [0].getHeight () + 1),
						SWT.BOLD);
				scoped.setFont (changedFont);
			}

			gc.getClipping (restored);
			assertTrue (restored.contains (4, 4));
			assertTrue (restored.contains (32, 32));
			assertFalse (restored.contains (20, 20));
			assertSame (expectedForegroundPattern, gc.getForegroundPattern ());
			assertSame (expectedBackgroundPattern, gc.getBackgroundPattern ());
			assertEquals (expectedFont, gc.getFont ());
		} finally {
			if (changedFont != null) changedFont.dispose ();
			if (temporaryForegroundPattern != null) temporaryForegroundPattern.dispose ();
			if (temporaryBackgroundPattern != null) temporaryBackgroundPattern.dispose ();
			if (initialForegroundPattern != null) initialForegroundPattern.dispose ();
			if (initialBackgroundPattern != null) initialBackgroundPattern.dispose ();
			restored.dispose ();
			original.dispose ();
		}
	}

	@Test
	public void test_gcScopeReleasesSnapshotsWhenCallerDisposesGc () {
		ViewportGcProxy proxy = ViewportGcProxy.wrap (gc);
		gc.dispose ();
		assertDoesNotThrow (proxy::close);
	}

	@Test
	public void test_gcScopeIsIdempotentAndRejectsUseAfterClose () {
		ViewportGcProxy proxy = ViewportGcProxy.wrap (gc);
		proxy.close ();
		proxy.close ();
		assertThrows (IllegalStateException.class, proxy::gc);
		assertThrows (IllegalStateException.class, () -> proxy.alpha (1));
	}

	private static float [] elements (Transform transform) {
		float [] result = new float [6];
		transform.getElements (result);
		return result;
	}

	private static float [] elements (GC gc) {
		Transform transform = new Transform (gc.getDevice ());
		try {
			gc.getTransform (transform);
			return elements (transform);
		} finally {
			transform.dispose ();
		}
	}

	private static void assertLineEquals (LineAttributes expected, LineAttributes actual) {
		assertEquals (expected.width, actual.width, 0.0001f);
		assertEquals (expected.cap, actual.cap);
		assertEquals (expected.join, actual.join);
		assertEquals (expected.style, actual.style);
		assertEquals (expected.dashOffset, actual.dashOffset, 0.0001f);
		assertEquals (expected.miterLimit, actual.miterLimit, 0.0001f);
		assertArrayEquals (expected.dash, actual.dash);
	}
}

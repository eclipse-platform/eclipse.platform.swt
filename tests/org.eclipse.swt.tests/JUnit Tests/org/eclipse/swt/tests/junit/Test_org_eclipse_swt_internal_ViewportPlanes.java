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

import static org.eclipse.swt.internal.VisibilityGate.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.DPIUtil;
import org.eclipse.swt.internal.ViewportDamageRecorder;
import org.eclipse.swt.internal.ViewportDamageRegion;
import org.eclipse.swt.internal.VisibilityGate;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.RECT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Leaf 2 (win32): box model, per-plane batch scope, damage test hook, burst debounce.
 * UI test: needs the desktop (hold C:/m3-work-c/ui-test.lock while running).
 */
public class Test_org_eclipse_swt_internal_ViewportPlanes {

	Display display;
	Shell shell;
	int paints, layouts;

	@BeforeEach
	public void setUp () {
		assumeTrue (SwtTestUtil.isWindows, "leaf 2 port is win32-only");
		display = Display.getDefault ();
		shell = new Shell (display, SWT.SHELL_TRIM | SWT.ON_TOP);
		shell.setLayout (new FillLayout ());
		shell.setBounds (40, 40, 420, 320);
	}

	@AfterEach
	public void tearDown () {
		if (shell != null) {
			shell.dispose ();
		}
	}

	static RECT windowRect (Control c) {
		RECT r = new RECT ();
		OS.GetWindowRect (c.handle, r);
		return r;
	}

	static RECT clientRect (Control c) {
		RECT r = new RECT ();
		OS.GetClientRect (c.handle, r);
		return r;
	}

	@Test
	public void test_boxEdgesAgreeWithWindowClientTrimAndBorder () {
		int [] styles = {SWT.NONE, SWT.BORDER, SWT.H_SCROLL, SWT.V_SCROLL, SWT.H_SCROLL | SWT.V_SCROLL, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL};
		shell.setLayout (null);
		shell.open ();
		for (int style : styles) {
			for (int kind = 0; kind < 2; kind++) {
				Scrollable control = kind == 0 ? new Canvas (shell, style) : new StyledText (shell, style);
				control.setBounds (10, 10, 200, 120);
				SwtTestUtil.processEvents ();
				int [] box = new int [BOX_LENGTH];
				assertTrue (VisibilityGate.boxEdges (control, box), "box available");
				RECT w = windowRect (control), c = clientRect (control);
				int horizontal = box [BORDER * 4 + LEFT] + box [BORDER * 4 + RIGHT] + box [TRACK * 4 + LEFT] + box [TRACK * 4 + RIGHT];
				int vertical = box [BORDER * 4 + TOP] + box [BORDER * 4 + BOTTOM] + box [TRACK * 4 + TOP] + box [TRACK * 4 + BOTTOM];
				String where = control.getClass ().getSimpleName () + " style 0x" + Integer.toHexString (style);
				assertEquals ((w.right - w.left) - (c.right - c.left), horizontal, where + " horizontal non-client");
				assertEquals ((w.bottom - w.top) - (c.bottom - c.top), vertical, where + " vertical non-client");
				if (DPIUtil.getDeviceZoom () == 100) {
					assertEquals (control.getBorderWidth (), box [BORDER * 4 + LEFT], where + " border width");
					Rectangle trim = control.computeTrim (0, 0, 0, 0);
					boolean barsShown = (style & (SWT.H_SCROLL | SWT.V_SCROLL)) == 0
						|| ((control.getVerticalBar () == null || control.getVerticalBar ().getVisible ())
						&& (control.getHorizontalBar () == null || control.getHorizontalBar ().getVisible ()));
					if (barsShown) {
						assertEquals (trim.width, horizontal, where + " computeTrim width");
						assertEquals (trim.height, vertical, where + " computeTrim height");
					}
				}
				control.dispose ();
			}
		}
	}

	@Test
	public void test_protectedRingsSurroundTheViewportExactly () {
		StyledText text = new StyledText (shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		shell.open ();
		SwtTestUtil.processEvents ();
		VisibilityGate.setBoxEdges (text, VisibilityGate.pack (3, 3, 3, 3), VisibilityGate.pack (4, 4, 4, 4));
		int [] content = new int [4];
		assertTrue (VisibilityGate.contentPixels (text, content));
		ViewportDamageRegion rings = new ViewportDamageRegion ();
		assertTrue (VisibilityGate.protectBox (text, rings, content [2], content [3]));
		ViewportDamageRegion viewport = new ViewportDamageRegion ();
		viewport.add (0, 0, content [2], content [3]);
		assertEquals (0, rings.intersectionArea (viewport), "rings never cover the content viewport");
		RECT w = windowRect (text);
		long outer = (long) (w.right - w.left) * (w.bottom - w.top);
		assertEquals (outer - (long) content [2] * content [3], rings.area (), "rings + viewport = outer bounds, no gap, no overlap");
	}

	@Test
	public void test_contentPixelsAreExactDevicePixels () {
		StyledText text = new StyledText (shell, SWT.BORDER | SWT.V_SCROLL);
		shell.open ();
		SwtTestUtil.processEvents ();
		int [] box = new int [BOX_LENGTH], content = new int [4];
		assertTrue (VisibilityGate.boxEdges (text, box));
		assertTrue (VisibilityGate.contentPixels (text, content));
		RECT w = windowRect (text);
		int width = 0, height = 0;
		for (int ring = BORDER; ring <= MARGIN; ring++) {
			width += box [ring * 4 + LEFT] + box [ring * 4 + RIGHT];
			height += box [ring * 4 + TOP] + box [ring * 4 + BOTTOM];
		}
		assertEquals (w.right - w.left, width + content [2], "edges + content = outer width in pixels at zoom " + DPIUtil.getDeviceZoom ());
		assertEquals (w.bottom - w.top, height + content [3], "edges + content = outer height in pixels at zoom " + DPIUtil.getDeviceZoom ());
	}

	@Test
	public void test_viewportBatchPaintsOnceAndOnlyTheDamage () {
		Canvas canvas = new Canvas (shell, SWT.V_SCROLL);
		canvas.addListener (SWT.Paint, e -> paints++);
		shell.open ();
		SwtTestUtil.processEvents ();
		ViewportDamageRecorder recorder = new ViewportDamageRecorder (1, 1);
		VisibilityGate.attachDamageRecorder (canvas, recorder);
		paints = 0;
		assertTrue (VisibilityGate.beginBatch (canvas, PLANE_VIEWPORT));
		for (int i = 0; i < 50; i++) {
			canvas.redraw (10 + i, 10, 20, 20, false);
			canvas.getVerticalBar ().setSelection (i);
			canvas.update ();
			SwtTestUtil.processEvents ();
		}
		assertEquals (0, paints, "content does not paint while the viewport plane is frozen");
		assertEquals (49, canvas.getVerticalBar ().getSelection (), "scrollbar stays live");
		assertTrue (VisibilityGate.endBatch (canvas, PLANE_VIEWPORT));
		canvas.update ();
		SwtTestUtil.processEvents ();
		assertEquals (1, paints, "one paint at the end");
		ViewportDamageRegion expected = new ViewportDamageRegion ();
		expected.add (10, 10, 69, 20);
		assertTrue (recorder.currentPainted ().isSubsetOf (expected), "no overpaint outside the accumulated damage");
		assertTrue (expected.isSubsetOf (recorder.currentPainted ()), "no underpaint");
		VisibilityGate.attachDamageRecorder (canvas, null);
	}

	@Test
	public void test_mainPlaneDefersLayoutWhileViewportStaysLive () {
		Composite composite = new Composite (shell, SWT.NONE);
		composite.setLayout (new Layout () {
			@Override
			protected Point computeSize (Composite c, int wHint, int hHint, boolean flushCache) {
				return new Point (100, 100);
			}

			@Override
			protected void layout (Composite c, boolean flushCache) {
				layouts++;
			}
		});
		Canvas canvas = new Canvas (composite, SWT.NONE);
		canvas.setBounds (0, 0, 100, 100);
		canvas.addListener (SWT.Paint, e -> paints++);
		shell.open ();
		SwtTestUtil.processEvents ();
		layouts = paints = 0;
		assertTrue (VisibilityGate.beginBatch (composite, PLANE_MAIN));
		for (int i = 0; i < 5; i++) {
			composite.layout (true, true);
		}
		canvas.redraw ();
		canvas.update ();
		assertEquals (0, layouts, "structure frozen");
		assertTrue (paints >= 1, "viewport (child) keeps painting during a main-plane freeze");
		assertTrue (VisibilityGate.endBatch (composite, PLANE_MAIN));
		assertEquals (1, layouts, "one layout at the end");
	}

	@Test
	public void test_nestedBatchesEndOnlyAtTheOutermostScope () {
		Canvas canvas = new Canvas (shell, SWT.NONE);
		canvas.addListener (SWT.Paint, e -> paints++);
		shell.open ();
		SwtTestUtil.processEvents ();
		paints = 0;
		VisibilityGate.beginBatch (canvas, PLANE_VIEWPORT);
		VisibilityGate.beginBatch (canvas, PLANE_VIEWPORT);
		canvas.redraw ();
		VisibilityGate.endBatch (canvas, PLANE_VIEWPORT);
		canvas.update ();
		assertEquals (0, paints, "inner end keeps the plane frozen");
		VisibilityGate.endBatch (canvas, PLANE_VIEWPORT);
		canvas.update ();
		assertEquals (1, paints);
	}

	@Test
	public void test_debounceCoalescesABurstAndCapsTheDelay () throws InterruptedException {
		Canvas canvas = new Canvas (shell, SWT.NONE);
		int [] fired = new int [2];
		long [] firedAt = new long [1];
		canvas.addListener (DEBOUNCED, e -> {
			fired [0]++;
			fired [1] |= e.detail;
			firedAt [0] = System.nanoTime ();
		});
		shell.open ();
		SwtTestUtil.processEvents ();
		long start = System.nanoTime ();
		long last = start;
		for (int i = 0; i < 200; i++) {
			assertTrue (VisibilityGate.debounce (canvas, 1 << (i & 3), DEBOUNCE_FRAME_MS));
			last = System.nanoTime ();
			Thread.sleep (1);
			if ((i & 15) == 0) {
				SwtTestUtil.processEvents ();
			}
		}
		SwtTestUtil.processEvents (400, null);
		long burstMs = (last - start) / 1_000_000L;
		assertTrue (fired [0] >= 1, "burst delivered");
		assertTrue (fired [0] <= burstMs / DEBOUNCE_MAX_MS + 2, "at most one delivery per cap window: " + fired [0] + " for a " + burstMs + " ms burst");
		assertEquals (0xF, fired [1] & 0xF, "kinds ORed");
		long settleMs = (firedAt [0] - last) / 1_000_000L;
		assertTrue (settleMs <= DEBOUNCE_MAX_MS + 100, "final delivery follows the last call within the cap, was " + settleMs + " ms");
	}

	/** Test-only check: a ScrolledComposite must not wrap a widget that scrolls itself. */
	static boolean scrollbarsOwnedByAncestor (Control content) {
		if (!(content.getParent () instanceof ScrolledComposite)) {
			return false;
		}
		return content instanceof StyledText || content instanceof org.eclipse.swt.widgets.Table
			|| content instanceof org.eclipse.swt.widgets.Tree || content instanceof org.eclipse.swt.widgets.List
			|| (content instanceof Scrollable s && (s.getStyle () & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0);
	}

	@Test
	public void test_scrollOwnershipAssertion () {
		ScrolledComposite wrong = new ScrolledComposite (shell, SWT.V_SCROLL);
		StyledText inner = new StyledText (wrong, SWT.NONE);
		wrong.setContent (inner);
		ScrolledComposite right = new ScrolledComposite (shell, SWT.V_SCROLL);
		Composite column = new Composite (right, SWT.NONE);
		right.setContent (column);
		assertTrue (scrollbarsOwnedByAncestor (inner), "StyledText wrapped by a ScrolledComposite is flagged");
		assertFalse (scrollbarsOwnedByAncestor (column), "plain composite content is fine");
	}
}

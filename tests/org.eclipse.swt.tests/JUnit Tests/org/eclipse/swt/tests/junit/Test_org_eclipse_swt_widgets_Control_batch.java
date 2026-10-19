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

import static org.eclipse.swt.internal.VisibilityGate.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
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
 * Control.beginBatch, endBatch and runBatch (win32): per-plane freeze, one repaint, busy while the viewport and scroll bars stay live.
 * UI test: needs the desktop (hold C:/m3-work-c/ui-test.lock while running).
 */
public class Test_org_eclipse_swt_widgets_Control_batch {


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
	public void test_viewportBatchPaintsOnceAndOnlyTheDamage () {
		Canvas canvas = new Canvas (shell, SWT.V_SCROLL);
		canvas.addListener (SWT.Paint, e -> paints++);
		shell.open ();
		SwtTestUtil.processEvents ();
		ViewportDamageRecorder recorder = new ViewportDamageRecorder (1, 1);
		VisibilityGate.attachDamageRecorder (canvas, recorder);
		paints = 0;
		canvas.beginBatch (SWT.PLANE_VIEWPORT);
		for (int i = 0; i < 50; i++) {
			canvas.redraw (10 + i, 10, 20, 20, false);
			canvas.getVerticalBar ().setSelection (i);
			canvas.update ();
			SwtTestUtil.processEvents ();
		}
		assertEquals (0, paints, "content does not paint while the viewport plane is frozen");
		assertEquals (49, canvas.getVerticalBar ().getSelection (), "scrollbar stays live");
		canvas.endBatch (SWT.PLANE_VIEWPORT);
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
		composite.beginBatch (SWT.PLANE_MAIN);
		for (int i = 0; i < 5; i++) {
			composite.layout (true, true);
		}
		canvas.redraw ();
		canvas.update ();
		assertEquals (0, layouts, "structure frozen");
		assertTrue (paints >= 1, "viewport (child) keeps painting during a main-plane freeze");
		composite.endBatch (SWT.PLANE_MAIN);
		assertEquals (1, layouts, "one layout at the end");
	}

	@Test
	public void test_nestedBatchesEndOnlyAtTheOutermostScope () {
		Canvas canvas = new Canvas (shell, SWT.NONE);
		canvas.addListener (SWT.Paint, e -> paints++);
		shell.open ();
		SwtTestUtil.processEvents ();
		paints = 0;
		canvas.beginBatch (SWT.PLANE_VIEWPORT);
		canvas.beginBatch (SWT.PLANE_VIEWPORT);
		canvas.redraw ();
		canvas.endBatch (SWT.PLANE_VIEWPORT);
		canvas.update ();
		assertEquals (0, paints, "inner end keeps the plane frozen");
		canvas.endBatch (SWT.PLANE_VIEWPORT);
		canvas.update ();
		assertEquals (1, paints);
	}

	@Test
	public void test_runBatchKeepsTheEventLoopLiveAndShowsBusy () {
		Canvas canvas = new Canvas (shell, SWT.V_SCROLL);
		canvas.addListener (SWT.Paint, e -> paints++);
		shell.open ();
		SwtTestUtil.processEvents ();
		int [] dispatched = new int [1], applied = new int [1];
		boolean [] busySeen = new boolean [1];
		paints = 0;
		display.timerExec (50, () -> dispatched [0]++);
		display.timerExec (250, () -> busySeen [0] = shell.getCursor () == display.getSystemCursor (SWT.CURSOR_WAIT));
		canvas.runBatch (SWT.PLANE_VIEWPORT, () -> {
			try {
				Thread.sleep (400);
			} catch (InterruptedException e) {
				Thread.currentThread ().interrupt ();
			}
		}, () -> {
			applied [0]++;
			canvas.redraw ();
		});
		assertEquals (1, dispatched [0], "event loop ran while the background work executed");
		assertTrue (busySeen [0], "busy cursor after the threshold");
		assertEquals (1, applied [0]);
		assertTrue (shell.getCursor () != display.getSystemCursor (SWT.CURSOR_WAIT), "cursor restored");
		canvas.update ();
		assertEquals (1, paints, "the frozen viewport paints once at the end");
		SWTException failure = assertThrows (SWTException.class, () -> canvas.runBatch (SWT.PLANE_VIEWPORT, () -> {
			throw new IllegalStateException ("boom");
		}, null));
		assertEquals (SWT.ERROR_FAILED_EXEC, failure.code);
		assertThrows (IllegalArgumentException.class, () -> canvas.runBatch (SWT.PLANE_VIEWPORT, null, null));
	}
}

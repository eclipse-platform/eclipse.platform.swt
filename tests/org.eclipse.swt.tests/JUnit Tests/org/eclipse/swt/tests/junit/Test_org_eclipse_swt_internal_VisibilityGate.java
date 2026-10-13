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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.VisibilityGate;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Effective-visibility gate (win32): hidden work is deferred and recorded,
 * public results are computed on demand, and visibility triggers one catch-up.
 */
public class Test_org_eclipse_swt_internal_VisibilityGate {

	Display display;
	Shell shell;
	int layouts;

	/** Places every child at a fixed inset; counts layout calls. */
	final class CountingLayout extends Layout {
		@Override
		protected Point computeSize (Composite composite, int wHint, int hHint, boolean flushCache) {
			return new Point (100, 100);
		}

		@Override
		protected void layout (Composite composite, boolean flushCache) {
			layouts++;
			Rectangle area = composite.getClientArea ();
			for (Control child : composite.getChildren ()) {
				child.setBounds (area.x + 7, area.y + 5, area.width / 2, area.height / 3);
			}
		}
	}

	@BeforeEach
	public void setUp () {
		assumeTrue (SwtTestUtil.isWindows, "visibility gate port is win32-only");
		display = Display.getDefault ();
		shell = new Shell (display);
		shell.setLayout (new FillLayout ());
		shell.setBounds (40, 40, 400, 300);
	}

	@AfterEach
	public void tearDown () {
		if (shell != null) {
			shell.dispose ();
		}
	}

	@Test
	public void test_hiddenLayoutIsDeferredButGeometryIsComputedOnDemand () {
		Composite composite = new Composite (shell, SWT.NONE);
		composite.setLayout (new CountingLayout ());
		Button button = new Button (composite, SWT.PUSH);
		shell.layout (true, true);
		layouts = 0;
		composite.setSize (300, 210);
		composite.layout (true, true);
		assertEquals (0, layouts, "layout of a never-shown shell must be deferred");
		Rectangle area = composite.getClientArea ();
		assertEquals (new Rectangle (area.x + 7, area.y + 5, area.width / 2, area.height / 3), button.getBounds ());
		assertEquals (1, layouts, "the geometry query runs the deferred layout exactly once");
		button.getBounds ();
		assertEquals (1, layouts);
	}

	@Test
	public void test_openRunsDeferredLayoutOnce () {
		Composite composite = new Composite (shell, SWT.NONE);
		composite.setLayout (new CountingLayout ());
		new Button (composite, SWT.PUSH);
		layouts = 0;
		for (int i = 0; i < 5; i++) {
			composite.layout (true, true);
		}
		assertEquals (0, layouts);
		shell.open ();
		SwtTestUtil.processEvents ();
		assertTrue (layouts >= 1 && layouts <= 2, "one catch-up layout (plus at most the open resize), was " + layouts);
	}

	@Test
	public void test_isOffscreenFollowsShellVisibilityAndMinimize () throws InterruptedException {
		Button button = new Button (shell, SWT.PUSH);
		assertTrue (VisibilityGate.isOffscreen (button, false), "never-shown shell");
		shell.open ();
		SwtTestUtil.processEvents ();
		assertFalse (VisibilityGate.isOffscreen (button, false), "open shell");
		shell.setMinimized (true);
		SwtTestUtil.processEvents (2000, shell::getMinimized);
		assertTrue (VisibilityGate.isOffscreen (button, false), "minimized shell");
		shell.setMinimized (false);
		SwtTestUtil.processEvents (2000, () -> !shell.getMinimized ());
		assertFalse (VisibilityGate.isOffscreen (button, false), "restored shell");
		button.setVisible (false);
		assertTrue (VisibilityGate.isOffscreen (button, false), "hidden control");
		assertFalse (button.isVisible (), "public API unchanged");
		button.setVisible (true);
		assertFalse (VisibilityGate.isOffscreen (button, false));
	}

	@Test
	public void test_controlScrolledOutOfScrolledCompositeIsOffscreen () {
		shell.setLayout (null);
		ScrolledComposite scrolled = new ScrolledComposite (shell, SWT.V_SCROLL);
		scrolled.setBounds (0, 0, 200, 100);
		Composite column = new Composite (scrolled, SWT.NONE);
		column.setSize (180, 2000);
		Button top = new Button (column, SWT.PUSH);
		top.setBounds (0, 0, 100, 20);
		Button far = new Button (column, SWT.PUSH);
		far.setBounds (0, 1500, 100, 20);
		scrolled.setContent (column);
		shell.open ();
		SwtTestUtil.processEvents ();
		assertFalse (VisibilityGate.isOffscreen (top, false));
		assertTrue (VisibilityGate.isOffscreen (far, false), "clipped out by the ScrolledComposite viewport");
		assertTrue (far.isVisible (), "public isVisible keeps its semantics");
		scrolled.setOrigin (0, 1490);
		SwtTestUtil.processEvents ();
		assertFalse (VisibilityGate.isOffscreen (far, false), "scrolled into view");
		assertTrue (VisibilityGate.isOffscreen (top, false));
	}

	@Test
	public void test_deferredConsumerWorkCatchesUpOnceWhenShown () {
		Composite composite = new Composite (shell, SWT.NONE);
		int [] caught = new int [2];
		composite.addListener (VisibilityGate.CATCH_UP, e -> {
			caught [0]++;
			caught [1] |= e.detail;
		});
		assertTrue (VisibilityGate.defer (composite, VisibilityGate.MEASURE));
		assertTrue (VisibilityGate.defer (composite, VisibilityGate.DATA));
		shell.open ();
		SwtTestUtil.processEvents ();
		assertEquals (1, caught [0], "one catch-up event");
		assertEquals (VisibilityGate.MEASURE | VisibilityGate.DATA, caught [1] & (VisibilityGate.MEASURE | VisibilityGate.DATA));
		assertFalse (VisibilityGate.defer (composite, VisibilityGate.MEASURE), "visible: work proceeds");
	}
}

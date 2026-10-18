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
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.VisibilityGate;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
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

	/**
	 * User report "inset problem": the native vertical scrollbar column must be painted
	 * with its track after the widget comes back from the gate. The column of a
	 * StyledText that was scrolled out of a ScrolledComposite and back is captured
	 * from the screen and compared with a twin that was never clipped.
	 */
	@Test
	public void test_scrollbarTrackPaintedAfterCatchUp () {
		/* ON_TOP so no other desktop window covers the captured pixels */
		shell.dispose ();
		shell = new Shell (display, SWT.SHELL_TRIM | SWT.ON_TOP);
		shell.setBounds (40, 40, 700, 360);
		ScrolledComposite scrolled = new ScrolledComposite (shell, SWT.V_SCROLL);
		scrolled.setBounds (0, 0, 300, 200);
		Composite column = new Composite (scrolled, SWT.NONE);
		column.setSize (280, 2000);
		StyledText gated = new StyledText (column, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		gated.setBounds (0, 1500, 260, 180);
		scrolled.setContent (column);
		StyledText twin = new StyledText (shell, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		twin.setBounds (360, 10, 260, 180);
		StringBuilder text = new StringBuilder ();
		for (int i = 0; i < 2000; i++) {
			text.append ("line ").append (i).append (System.lineSeparator ());
		}
		shell.open ();
		SwtTestUtil.processEvents ();
		gated.setText (text.toString ());
		twin.setText (text.toString ());
		assertTrue (VisibilityGate.isOffscreen (gated, true));
		scrolled.setOrigin (0, 1500);
		SwtTestUtil.processEvents ();
		assertFalse (VisibilityGate.isOffscreen (gated, true));
		ImageData a = captureRightColumn (gated), b = captureRightColumn (twin);
		assertEquals (b.width, a.width);
		assertEquals (b.height, a.height);
		/* Track colour = the twin column's most frequent pixel; the thumb may differ (Windows 11 animates it). */
		java.util.Map<Integer, Integer> histogram = new java.util.HashMap<> ();
		for (int y = 0; y < b.height; y++) {
			for (int x = 0; x < b.width; x++) {
				histogram.merge (b.getPixel (x, y), 1, Integer::sum);
			}
		}
		int track = histogram.entrySet ().stream ().max (java.util.Map.Entry.comparingByValue ()).get ().getKey ();
		int trackPixels = 0, whiteStripColumns = 0;
		for (int x = 0; x < a.width; x++) {
			boolean gatedWhite = true, twinWhite = true;
			for (int y = 0; y < a.height; y++) {
				if (a.getPixel (x, y) == track) {
					trackPixels++;
				}
				gatedWhite &= a.palette.getRGB (a.getPixel (x, y)).equals (new org.eclipse.swt.graphics.RGB (255, 255, 255));
				twinWhite &= b.palette.getRGB (b.getPixel (x, y)).equals (new org.eclipse.swt.graphics.RGB (255, 255, 255));
			}
			if (gatedWhite && !twinWhite) {
				whiteStripColumns++;
			}
		}
		if (trackPixels * 10 < a.width * a.height * 8 || whiteStripColumns != 0) {
			save (a, "gated-column");
			save (b, "twin-column");
		}
		assertEquals (0, whiteStripColumns, "white strip in the scrollbar column");
		assertTrue (trackPixels * 10 >= a.width * a.height * 8, "scrollbar track painted on at least 80% of the column, was " + trackPixels + " of " + a.width * a.height);
	}

	static void save (ImageData data, String name) {
		org.eclipse.swt.graphics.ImageLoader loader = new org.eclipse.swt.graphics.ImageLoader ();
		loader.data = new ImageData [] {data};
		loader.save (new java.io.File (System.getProperty ("java.io.tmpdir"), "swt-visibility-gate-" + name + ".png").getPath (), SWT.IMAGE_PNG);
	}

	/** Screen pixels of the control's vertical scrollbar column (non-client, right of the client area). */
	ImageData captureRightColumn (Scrollable control) {
		Rectangle client = control.getClientArea ();
		Point origin = control.toDisplay (client.x + client.width, client.y);
		int width = control.getVerticalBar ().getSize ().x;
		Image image = new Image (display, width, client.height);
		GC gc = new GC (display);
		try {
			gc.copyArea (image, origin.x, origin.y);
		} finally {
			gc.dispose ();
		}
		ImageData data = image.getImageData ();
		image.dispose ();
		return data;
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

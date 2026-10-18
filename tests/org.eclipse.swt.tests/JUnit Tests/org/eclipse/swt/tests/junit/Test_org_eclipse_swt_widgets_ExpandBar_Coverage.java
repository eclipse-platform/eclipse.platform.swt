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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * The cases of the ExpandBar viewport (docs/m3/EXPANDBAR_VIEWPORT.md) that the recorded differential does not reach
 * (brief rule 15, docs/m3/allwidgets-viewport/coverage): a band height cached for a font that is then disposed,
 * an item control disposed while a native scroll moves it, controls the application or the bar moves off the client
 * area (deferred, mapped from their logical position, logged at FINEST), a control taller than the plane band, a bar
 * inside a scrolled plane, and invalidations of item headers that lie off the client area. Every assertion is stock
 * behaviour, so the class runs in every mode (the coverage script runs it in the default mode, with
 * <code>-Dswt.widget.mode=original</code> and with <code>-Dswt.widget.mode.ExpandBar=original</code>); the two cases
 * past the native range need the plane mediation (upstream defects D1 and D2) and are skipped without it. Win32 only
 * (scrolling is driven with window messages).
 */
public class Test_org_eclipse_swt_widgets_ExpandBar_Coverage {
	static final int WM_VSCROLL = 0x115, SB_LINEDOWN = 1, SB_PAGEDOWN = 3, SB_TOP = 6, SB_BOTTOM = 7;
	Display display;
	Shell shell;

	@BeforeEach
	public void setUp () {
		Assumptions.assumeTrue ("win32".equals (SWT.getPlatform ()), "scrolling is driven with win32 messages");
		display = Display.getDefault ();
		ViewportDifferential.parkCursor (display);
		shell = new Shell (display, SWT.NO_TRIM | SWT.ON_TOP);
		shell.setBounds (40, 40, 300, 400);
		shell.setLayout (new FillLayout ());
	}

	@AfterEach
	public void tearDown () {
		if (shell != null) shell.dispose ();
	}

	ExpandBar bar (int items, int height) {
		ExpandBar bar = new ExpandBar (shell, SWT.V_SCROLL);
		for (int i = 0; i < items; i++) {
			ExpandItem item = new ExpandItem (bar, SWT.NONE);
			item.setText ("item " + i);
			item.setHeight (height);
			Label label = new Label (bar, SWT.NONE);
			label.setText ("control " + i);
			item.setControl (label);
			item.setExpanded (true);
		}
		shell.layout (true, true);
		shell.open ();
		ViewportDifferential.drain (display);
		return bar;
	}

	@Test
	public void disposedFontIsMeasuredAgain () {
		ExpandBar bar = bar (6, 40);
		Font big = new Font (display, "Segoe UI", 30, SWT.BOLD);
		bar.setFont (big);
		ViewportDifferential.drain (display);
		int header = bar.getItem (0).getHeaderHeight ();
		assertEquals (header, bar.getItem (3).getHeaderHeight (), "one band height for every header");
		Test_org_eclipse_swt_widgets_ExpandBar_Viewport.assertLogical (bar, "with the big font");
		big.dispose ();
		int measured = bar.getItem (0).getHeaderHeight ();
		assertEquals (header, measured, "the bar keeps measuring its native font handle after the Font is disposed (stock)");
		/* layouts from items after the first, with the band height measured again for the disposed font */
		bar.getItem (3).setHeight (60);
		ViewportDifferential.drain (display);
		Test_org_eclipse_swt_widgets_ExpandBar_Viewport.assertLogical (bar, "after a layout from item 4");
		bar.getItem (1).setExpanded (false);
		ViewportDifferential.drain (display);
		Test_org_eclipse_swt_widgets_ExpandBar_Viewport.assertLogical (bar, "after a layout from item 2");
		bar.getItem (4).setHeight (30);
		ViewportDifferential.drain (display);
		Test_org_eclipse_swt_widgets_ExpandBar_Viewport.assertLogical (bar, "after a layout from item 5");
	}

	@Test
	public void controlDisposedWhileScrolled () {
		ExpandBar bar = bar (12, 200);
		Control first = bar.getItem (0).getControl (), second = bar.getItem (1).getControl ();
		int [] moves = {0};
		first.addListener (SWT.Move, e -> {
			moves [0]++;
			if (!second.isDisposed ()) second.dispose ();
		});
		ViewportDifferential.send (bar, WM_VSCROLL, SB_LINEDOWN, 0);
		ViewportDifferential.drain (display);
		assertTrue (second.isDisposed (), "the Move listener of the first control ran during the scroll");
		assertTrue (moves [0] > 0);
		ViewportDifferential.send (bar, WM_VSCROLL, SB_PAGEDOWN, 0);
		ViewportDifferential.send (bar, WM_VSCROLL, SB_TOP, 0);
		ViewportDifferential.drain (display);
		ExpandItem item = bar.getItem (0);
		assertEquals (item.getHeaderHeight () + bar.getSpacing (), first.getBounds ().y, "the first control below its header at the top");
	}

	@Test
	public void headersOffTheClientAreaRepaint () {
		shell.setLayout (null);
		ExpandBar bar = new ExpandBar (shell, SWT.NONE);
		bar.setSpacing (0);
		bar.setBounds (0, 0, 0, 0);
		List<Rectangle> paints = new ArrayList<> ();
		bar.addListener (SWT.Paint, e -> paints.add (new Rectangle (e.x, e.y, e.width, e.height)));
		shell.open ();
		ExpandItem first = new ExpandItem (bar, SWT.NONE);
		first.setText ("first");
		ExpandItem second = new ExpandItem (bar, SWT.NONE);
		second.setText ("second");
		ViewportDifferential.drain (display);
		bar.setBounds (0, 0, 200, 200);
		bar.update ();
		ViewportDifferential.drain (display);
		assertTrue (covers (paints, 0, 0, 200, first.getHeaderHeight ()), "the first header is repainted after the bar got a size: " + paints);
		paints.clear ();
		bar.setSpacing (4);
		bar.setBounds (0, 0, 2, 200);
		bar.update ();
		ViewportDifferential.drain (display);
		second.setExpanded (true);
		bar.update ();
		ViewportDifferential.drain (display);
		assertEquals (2, bar.getClientArea ().width);
		bar.setBounds (0, 0, 200, 200);
		bar.update ();
		ViewportDifferential.drain (display);
		assertTrue (covers (paints, 4, 4, 192, first.getHeaderHeight ()), "the first header is repainted after the bar got wide again: " + paints);
	}

	@Test
	public void controlsMovedOffTheClientAreaMapLikeNativeOnes () {
		/* the plane mediation logs every deferred move at FINEST (its log is the Composite log) */
		Logger log = Logger.getLogger ("org.eclipse.swt.widgets.Composite");
		log.setLevel (Level.FINEST);
		try {
			ExpandBar bar = bar (12, 200);
			bar.getItem (0).setExpanded (false);
			ViewportDifferential.drain (display);
			bar.getItem (0).setExpanded (true);
			bar.getItem (1).setExpanded (false);
			ViewportDifferential.drain (display);
			Test_org_eclipse_swt_widgets_ExpandBar_Viewport.assertLogical (bar, "after collapsing and expanding");
			for (int i : new int [] {6, 10, 11}) {
				Control far = bar.getItem (i).getControl ();
				Point location = far.getLocation ();
				assertTrue (location.y > bar.getClientArea ().height, "control " + i + " below the client area: " + location);
				assertEquals (bar.toDisplay (location), far.toDisplay (0, 0), "control " + i + " maps from its logical position");
				assertEquals (bar.toDisplay (location), display.map (far, null, 0, 0), "control " + i + " maps from its logical position");
			}
			ViewportDifferential.send (bar, WM_VSCROLL, SB_BOTTOM, 0);
			ViewportDifferential.drain (display);
			Test_org_eclipse_swt_widgets_ExpandBar_Viewport.assertLogical (bar, "at the end");
			Control last = bar.getItem (11).getControl ();
			assertTrue (last.getBounds ().intersects (bar.getClientArea ()), "the last control is in view at the end: " + last.getBounds ());
			assertEquals (bar.toDisplay (last.getLocation ()), last.toDisplay (0, 0), "the last control maps from its logical position");
			shell.setSize (300, 900);
			ViewportDifferential.drain (display);
			Test_org_eclipse_swt_widgets_ExpandBar_Viewport.assertLogical (bar, "after the bar grew");
		} finally {
			log.setLevel (null);
		}
	}

	@Test
	public void bigAndNewControlsAreNotDeferred () {
		ExpandBar bar = bar (8, 200);
		/* a control higher than the plane band, and a control given to an item far below the client area */
		ExpandItem tall = bar.getItem (5);
		tall.setHeight (26000);
		ViewportDifferential.drain (display);
		Label late = new Label (bar, SWT.NONE);
		late.setText ("late");
		bar.getItem (7).setControl (late);
		ViewportDifferential.drain (display);
		Test_org_eclipse_swt_widgets_ExpandBar_Viewport.assertLogical (bar, "with a tall and a late control");
		bar.getItem (0).setExpanded (false);
		bar.getItem (2).setHeight (120);
		ViewportDifferential.drain (display);
		Test_org_eclipse_swt_widgets_ExpandBar_Viewport.assertLogical (bar, "after moving them");
		int height = tall.getControl ().getSize ().y;
		assertTrue (Math.abs (tall.getHeight () - height) <= 1, "the tall control keeps the item height (less an unthemed border): " + height);
	}

	@Test
	public void childrenTheApplicationMovesOffTheClientArea () {
		ExpandBar bar = bar (4, 100);
		Label free = new Label (bar, SWT.NONE);
		free.setText ("free");
		free.setBounds (10, 10, 60, 20);
		ViewportDifferential.drain (display);
		/* below, left of, right of the client area, and back */
		for (int [] to : new int [] [] {{10, 900}, {10, 1300}, {-500, 50}, {5000, 50}, {-500, 60}, {12, 14}}) {
			free.setLocation (to [0], to [1]);
			ViewportDifferential.drain (display);
			assertEquals (new Point (to [0], to [1]), free.getLocation ());
			assertEquals (bar.toDisplay (to [0], to [1]), free.toDisplay (0, 0), "maps from " + to [0] + "," + to [1]);
		}
		free.setLocation (-500, 60);
		/* moved by a Resize listener of the bar, while the bar batches its children's window positions */
		bar.addListener (SWT.Resize, e -> free.setLocation (20, bar.getClientArea ().height + 300));
		shell.setSize (300, 380);
		ViewportDifferential.drain (display);
		assertEquals (new Point (20, bar.getClientArea ().height + 300), free.getLocation ());
		assertEquals (bar.toDisplay (free.getLocation ()), free.toDisplay (0, 0));
		Test_org_eclipse_swt_widgets_ExpandBar_Viewport.assertLogical (bar, "after the resize");
	}

	@Test
	public void tallControlScrolledToItsMiddle () {
		Assumptions.assumeTrue (Test_org_eclipse_swt_widgets_ScrolledComposite_ViewportDifferential.planesActive (), "stock clamps controls past the native range (D1, D2)");
		ExpandBar bar = new ExpandBar (shell, SWT.V_SCROLL);
		ExpandItem top = new ExpandItem (bar, SWT.NONE);
		top.setText ("top");
		top.setHeight (100);
		top.setControl (new Label (bar, SWT.NONE));
		top.setExpanded (true);
		ExpandItem item = new ExpandItem (bar, SWT.NONE);
		item.setText ("tall");
		item.setHeight (60000);
		Composite tall = new Composite (bar, SWT.NONE);
		Label inner = new Label (tall, SWT.NONE);
		inner.setText ("inner");
		inner.setBounds (5, 40100, 80, 20);
		item.setControl (tall);
		item.setExpanded (true);
		/* a free child wider than the plane band */
		Label wide = new Label (bar, SWT.NONE);
		wide.setBounds (0, 3000, 30000, 20);
		shell.layout (true, true);
		shell.open ();
		ViewportDifferential.drain (display);
		top.setExpanded (false);
		ViewportDifferential.drain (display);
		top.setExpanded (true);
		ViewportDifferential.drain (display);
		Test_org_eclipse_swt_widgets_ExpandBar_Viewport.assertLogical (bar, "tall control at the top");
		/* scroll down about 40000 in pages shorter than the client area, until the middle of the tall control is in view */
		bar.getVerticalBar ().setPageIncrement (300);
		for (int i = 0; i < 400 && tall.getBounds ().y + 40100 >= bar.getClientArea ().height - 30; i++) {
			ViewportDifferential.send (bar, WM_VSCROLL, SB_PAGEDOWN, 0);
		}
		ViewportDifferential.drain (display);
		Test_org_eclipse_swt_widgets_ExpandBar_Viewport.assertLogical (bar, "in the middle of the tall control");
		Rectangle client = bar.getClientArea ();
		Point innerInBar = display.map (inner, bar, 0, 0);
		assertEquals (new Point (tall.getBounds ().x + 5, tall.getBounds ().y + 40100), innerInBar, "the inner label maps from its logical position");
		assertTrue (innerInBar.y > -20 && innerInBar.y < client.height, "the inner label is in view: " + innerInBar + " tall " + tall.getBounds () + " bar " + ViewportDifferential.bar (bar.getVerticalBar ()) + " item " + item.getHeight ());
		top.setExpanded (false);
		ViewportDifferential.drain (display);
		Test_org_eclipse_swt_widgets_ExpandBar_Viewport.assertLogical (bar, "after collapsing the top item");
		assertEquals (new Point (tall.getBounds ().x + 5, tall.getBounds ().y + 40100), display.map (inner, bar, 0, 0));
		ViewportDifferential.send (bar, WM_VSCROLL, SB_TOP, 0);
		ViewportDifferential.drain (display);
		Test_org_eclipse_swt_widgets_ExpandBar_Viewport.assertLogical (bar, "back at the top");
		assertEquals (3000, wide.getBounds ().y, "the wide child is back where it was");
	}

	@Test
	public void barInsideAScrolledPlane () {
		Assumptions.assumeTrue (Test_org_eclipse_swt_widgets_ScrolledComposite_ViewportDifferential.planesActive (), "stock clamps controls past the native range (D1, D2)");
		ScrolledComposite scrolled = new ScrolledComposite (shell, SWT.V_SCROLL);
		ExpandBar bar = new ExpandBar (scrolled, SWT.V_SCROLL);
		for (int i = 0; i < 150; i++) {
			ExpandItem item = new ExpandItem (bar, SWT.NONE);
			item.setText ("item " + i);
			item.setHeight (600);
			Label label = new Label (bar, SWT.NONE);
			label.setText ("control " + i);
			item.setControl (label);
			item.setExpanded (true);
		}
		bar.setSize (280, 60000);
		scrolled.setContent (bar);
		shell.layout (true, true);
		shell.open ();
		ViewportDifferential.drain (display);
		/* the bar is taller than the native range: a control past it is parked inside the bar's client area */
		ViewportDifferential.send (bar, WM_VSCROLL, SB_LINEDOWN, 0);
		ViewportDifferential.drain (display);
		Test_org_eclipse_swt_widgets_ExpandBar_Viewport.assertLogical (bar, "after a line down");
		/* scrolled far into the bar: the bar is a plane with a base */
		scrolled.setOrigin (0, 40000);
		ViewportDifferential.drain (display);
		Test_org_eclipse_swt_widgets_ExpandBar_Viewport.assertLogical (bar, "with the scrolled composite at 40000");
		bar.getItem (0).setExpanded (false);
		ViewportDifferential.drain (display);
		Test_org_eclipse_swt_widgets_ExpandBar_Viewport.assertLogical (bar, "after collapsing the first item");
		ViewportDifferential.send (bar, WM_VSCROLL, SB_PAGEDOWN, 0);
		ViewportDifferential.drain (display);
		Test_org_eclipse_swt_widgets_ExpandBar_Viewport.assertLogical (bar, "after a page down of the bar");
		Control seen = null;
		for (ExpandItem item : bar.getItems ()) {
			Point at = display.map (item.getControl (), scrolled, 0, 0);
			if (at.y >= 0 && at.y < scrolled.getClientArea ().height) seen = item.getControl ();
		}
		assertNotNull (seen, "an item control is in view of the scrolled composite");
		assertEquals (display.map (bar, scrolled, seen.getLocation ()), display.map (seen, scrolled, 0, 0), "it maps from its logical position");
	}

	/* whether the union of the Paint rectangles contains the rectangle (each row of it in one Paint rectangle) */
	static boolean covers (List<Rectangle> paints, int x, int y, int width, int height) {
		for (int row = y; row < y + height; row++) {
			boolean found = false;
			for (Rectangle paint : paints) {
				if (paint.contains (x, row) && paint.contains (x + width - 1, row)) found = true;
			}
			if (!found) return false;
		}
		return true;
	}
}

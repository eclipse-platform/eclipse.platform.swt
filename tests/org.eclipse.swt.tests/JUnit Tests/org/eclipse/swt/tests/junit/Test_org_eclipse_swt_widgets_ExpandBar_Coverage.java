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

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * The cases of the ExpandBar viewport (docs/m3/EXPANDBAR_VIEWPORT.md) that the recorded differential does not reach
 * (brief rule 15, docs/m3/allwidgets-viewport/coverage): a band height cached for a font that is then disposed, a
 * layout from an item after the band height changed, an item control disposed while a native scroll moves it, and
 * invalidations of item headers that lie off the client area. Every assertion is stock behaviour, so the class runs in
 * every mode (the coverage script runs it in the default mode, with <code>-Dswt.widget.mode=original</code> and with
 * <code>-Dswt.widget.mode.ExpandBar=original</code>). Win32 only (scrolling is driven with window messages).
 */
public class Test_org_eclipse_swt_widgets_ExpandBar_Coverage {
	static final int WM_VSCROLL = 0x115, SB_LINEDOWN = 1, SB_PAGEDOWN = 3, SB_TOP = 6;
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
		assertTrue (measured < header, "a disposed font is measured again (stock), not answered for the font it was: " + measured + " / " + header);
		/* a layout from an item after the band height changed starts from the top, then one from an item after that */
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

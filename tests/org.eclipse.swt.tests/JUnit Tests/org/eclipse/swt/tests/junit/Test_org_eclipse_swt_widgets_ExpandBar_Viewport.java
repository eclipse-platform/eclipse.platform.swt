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

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Acceptance of leaf L6 (docs/m3/EXPANDBAR_VIEWPORT.md) in the viewport mode: an ExpandBar whose items reach far
 * beyond the 16-bit native coordinate range keeps every item control at its logical position (stock clamps the child
 * windows at 32767) after the layout, after scrolling to the end and back with the bar, and after collapsing the first
 * item. Win32 only (scrolling is driven with window messages).
 */
public class Test_org_eclipse_swt_widgets_ExpandBar_Viewport {
	static final int WM_VSCROLL = 0x115, SB_PAGEDOWN = 3, SB_TOP = 6, SB_BOTTOM = 7;

	@Test
	public void farControlsStayLogical () {
		Assumptions.assumeTrue ("win32".equals (SWT.getPlatform ()), "scrolling is driven with win32 messages");
		Assumptions.assumeTrue ("viewport".equals (ViewportDifferential.reference ("ExpandBar")), "the viewport mode of ExpandBar is not active");
		Display display = Display.getDefault ();
		Shell shell = new Shell (display, SWT.NO_TRIM | SWT.ON_TOP);
		try {
			shell.setBounds (40, 40, 300, 400);
			shell.setLayout (new FillLayout ());
			ExpandBar bar = new ExpandBar (shell, SWT.V_SCROLL);
			for (int i = 0; i < 80; i++) {
				ExpandItem item = new ExpandItem (bar, SWT.NONE);
				item.setText ("item " + i);
				item.setHeight (600);
				Label label = new Label (bar, SWT.NONE);
				label.setText ("control " + i);
				item.setControl (label);
				item.setExpanded (true);
			}
			shell.open ();
			ViewportDifferential.drain (display);
			Control last = bar.getItem (79).getControl ();
			assertTrue (last.getBounds ().y > 40000, "the last control is beyond the native range: " + last.getBounds ());
			assertLogical (bar, "after the layout");
			ViewportDifferential.send (bar, WM_VSCROLL, SB_BOTTOM, 0);
			ViewportDifferential.drain (display);
			assertLogical (bar, "at the end");
			Rectangle end = last.getBounds ();
			assertTrue (end.y < bar.getClientArea ().height && end.y + end.height > 0, "the last control is in view at the end: " + end);
			for (int i = 0; i < 5; i++) ViewportDifferential.send (bar, WM_VSCROLL, SB_PAGEDOWN, 0);
			ViewportDifferential.send (bar, WM_VSCROLL, SB_TOP, 0);
			ViewportDifferential.drain (display);
			assertLogical (bar, "back at the top");
			bar.getItem (0).setExpanded (false);
			ViewportDifferential.drain (display);
			assertLogical (bar, "after collapsing the first item");
		} finally {
			shell.dispose ();
		}
	}

	/* every control at the layout computed from the public API, relative to the first control */
	static void assertLogical (ExpandBar bar, String when) {
		ExpandItem [] items = bar.getItems ();
		int spacing = bar.getSpacing (), y = 0, base = 0;
		for (int i = 0; i < items.length; i++) {
			ExpandItem item = items [i];
			int at = item.getControl ().getBounds ().y - (y + item.getHeaderHeight ());
			if (i == 0) base = at;
			assertEquals (base, at, "control " + i + " " + when + ": " + item.getControl ().getBounds ());
			y += item.getHeaderHeight () + spacing + (item.getExpanded () ? item.getHeight () : 0);
		}
	}
}

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
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * The case of the CoolBar viewport (docs/m3/TOOLBAR_VIEWPORT.md: a band child the rebar already placed is not placed
 * again) that the recorded differential does not reach (brief rule 15, docs/m3/allwidgets-viewport/coverage): band
 * children of a mirrored rebar, which the viewport always places again. The assertions are stock behaviour (mirroring
 * changes no size and keeps every child inside the bar), so the class runs in every mode.
 */
public class Test_org_eclipse_swt_widgets_CoolBar_Coverage {
	Display display;
	Shell shell;

	@BeforeEach
	public void setUp () {
		display = Display.getDefault ();
		ViewportDifferential.parkCursor (display);
		shell = new Shell (display, SWT.NO_TRIM | SWT.ON_TOP);
		shell.setBounds (40, 40, 520, 140);
		shell.open ();
	}

	@AfterEach
	public void tearDown () {
		shell.dispose ();
	}

	CoolBar bar (int orientation, int y) {
		CoolBar bar = new CoolBar (shell, SWT.FLAT | orientation);
		bar.setBounds (0, y, 500, 40);
		for (int i = 0; i < 3; i++) {
			CoolItem item = new CoolItem (bar, SWT.NONE);
			Label label = new Label (bar, SWT.NONE);
			label.setText ("band " + i);
			label.pack ();
			item.setControl (label);
			Point size = label.computeSize (SWT.DEFAULT, SWT.DEFAULT);
			Point preferred = item.computeSize (size.x, size.y);
			item.setPreferredSize (preferred);
			item.setSize (preferred);
		}
		ViewportDifferential.drain (display);
		bar.update ();
		ViewportDifferential.drain (display);
		return bar;
	}

	@Test
	public void mirroredBandChildren () {
		CoolBar leftToRight = bar (SWT.LEFT_TO_RIGHT, 0), rightToLeft = bar (SWT.RIGHT_TO_LEFT, 60);
		assertEquals (SWT.RIGHT_TO_LEFT, rightToLeft.getOrientation ());
		Rectangle client = rightToLeft.getClientArea ();
		for (int i = 0; i < 3; i++) {
			Control mirrored = rightToLeft.getItem (i).getControl (), plain = leftToRight.getItem (i).getControl ();
			Rectangle bounds = mirrored.getBounds ();
			assertEquals (plain.getSize (), mirrored.getSize (), "band " + i + " child: mirroring changes no size");
			assertTrue (bounds.x >= 0 && bounds.y >= 0 && bounds.x + bounds.width <= client.width && bounds.y + bounds.height <= client.height,
				"band " + i + " child inside the mirrored bar: " + bounds + " in " + client);
		}
		/* moving a band makes the rebar size its child again */
		rightToLeft.getItem (2).setSize (200, rightToLeft.getItem (2).getSize ().y);
		ViewportDifferential.drain (display);
		assertEquals (leftToRight.getItem (2).getControl ().getSize ().y, rightToLeft.getItem (2).getControl ().getSize ().y);
	}
}

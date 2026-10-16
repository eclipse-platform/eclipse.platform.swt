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

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Acceptance tests of scrollable-composite planes (docs/m3/SCROLL_MEDIATED_VIEWPORT.md): bounds, origin,
 * bars, mapping, input and paint stay logical past the 16-bit native coordinate range. Skipped unless the
 * planes are active (win32, default widget mode).
 */
public class Test_org_eclipse_swt_widgets_ScrolledComposite_Viewport {
	static final int HEIGHT = 1_000_000;
	Display display;
	Shell shell;
	ScrolledComposite sc;
	Canvas content;
	Composite bottom;
	final StringBuilder paints = new StringBuilder ();

	@BeforeEach
	public void setUp () {
		Assumptions.assumeTrue ("win32".equals (SWT.getPlatform ()), "the native coordinate limit is measured on win32");
		Assumptions.assumeTrue (Test_org_eclipse_swt_widgets_ScrolledComposite_ViewportDifferential.planesActive (), "planes are not active");
		display = Display.getDefault ();
		ViewportDifferential.parkCursor (display);
		shell = new Shell (display, SWT.NO_TRIM | SWT.ON_TOP);
		shell.setBounds (40, 40, 400, 300);
		shell.setLayout (new FillLayout ());
		sc = new ScrolledComposite (shell, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		content = new Canvas (sc, SWT.NONE);
		content.setBackground (display.getSystemColor (SWT.COLOR_WHITE));
		content.addListener (SWT.Paint, e -> paints.append (e.x).append (',').append (e.y).append (' ').append (e.width).append ('x').append (e.height).append (';'));
		bottom = new Composite (content, SWT.NONE);
		bottom.setBackground (display.getSystemColor (SWT.COLOR_BLUE));
		bottom.setBounds (10, HEIGHT - 30, 40, 30);
		sc.setContent (content);
		content.setSize (300, HEIGHT);
		shell.open ();
		flush ();
	}

	@AfterEach
	public void tearDown () {
		if (shell != null) shell.dispose ();
	}

	void flush () {
		while (display.readAndDispatch ()) {
			/* drain */
		}
		content.update ();
		while (display.readAndDispatch ()) {
			/* drain */
		}
	}

	int clientHeight () {
		return sc.getClientArea ().height;
	}

	@Test
	public void boundsAreLogicalPastTheNativeRange () {
		assertEquals (new Point (300, HEIGHT), content.getSize ());
		assertEquals (new Rectangle (10, HEIGHT - 30, 40, 30), bottom.getBounds ());
		assertEquals (new Rectangle (0, 0, 300, HEIGHT), content.getClientArea ());
		Composite parked = new Composite (shell, SWT.NONE);
		parked.setBounds (5, 60_000, 20, 20);
		assertEquals (new Rectangle (5, 60_000, 20, 20), parked.getBounds (), "a small control far away keeps its bounds");
		parked.dispose ();
	}

	@Test
	public void originAndBarAgreeAtTheEnd () {
		int end = HEIGHT - clientHeight ();
		sc.setOrigin (0, end);
		flush ();
		assertEquals (new Point (0, end), sc.getOrigin ());
		assertEquals (end, sc.getVerticalBar ().getSelection ());
		assertEquals (new Point (0, -end), content.getLocation ());
		ViewportDifferential.send (sc, Test_org_eclipse_swt_widgets_ScrolledComposite_ViewportDifferential.WM_VSCROLL, Test_org_eclipse_swt_widgets_ScrolledComposite_ViewportDifferential.SB_LINEUP, 0);
		flush ();
		assertEquals (sc.getVerticalBar ().getSelection (), sc.getOrigin ().y, "a native scroll message keeps bar and origin together");
	}

	@Test
	public void theEndOfTheContentIsShownWhereItBelongs () {
		sc.setOrigin (0, HEIGHT - clientHeight ());
		flush ();
		Point inSc = display.map (bottom, sc, 0, 0);
		assertEquals (new Point (10, clientHeight () - 30), inSc);
		assertEquals (bottom.toDisplay (0, 0), content.toDisplay (10, HEIGHT - 30));
		assertEquals (new Point (10, HEIGHT - 30), display.map (bottom, content, 0, 0));
		assertEquals (new Point (10 + 5, HEIGHT - 30 + 5), content.toControl (bottom.toDisplay (5, 5)));
		ImageData data = ViewportDifferential.render (sc);
		int border = sc.getBorderWidth ();
		RGB rgb = data.palette.getRGB (data.getPixel (border + 25, border + clientHeight () - 15));
		assertEquals (new RGB (0, 0, 255), rgb, "the last child is painted at the end of the content");
	}

	@Test
	public void scrollStepsPaintOnlyTheExposedStripAcrossRebases () throws ReflectiveOperationException {
		java.lang.reflect.Field planeField = Control.class.getDeclaredField ("plane");
		planeField.setAccessible (true);
		Object plane = planeField.get (content);
		assertNotNull (plane, "the content is a plane");
		java.lang.reflect.Field baseField = plane.getClass ().getDeclaredField ("baseY");
		baseField.setAccessible (true);
		Set<Integer> bases = new TreeSet<> ();
		int width = content.getSize ().x, start = 500_000;
		sc.setOrigin (0, start);
		flush ();
		for (int i = 1; i <= 2000; i++) {
			paints.setLength (0);
			sc.setOrigin (0, start + 10 * i);
			flush ();
			bases.add (baseField.getInt (planeField.get (content)));
			assertEquals ("0," + (start + 10 * i + clientHeight () - 10) + " " + width + "x10;", paints.toString (), "step " + i + " paints only the exposed strip");
		}
		assertTrue (bases.size () > 1, "the steps crossed at least one rebase: " + bases);
	}

	@Test
	public void mouseAndPaintEventsAreLogical () {
		int end = HEIGHT - clientHeight ();
		sc.setOrigin (0, end);
		flush ();
		int [] mouse = {-1, -1};
		content.addListener (SWT.MouseWheel, e -> {
			mouse [0] = e.x;
			mouse [1] = e.y;
			e.doit = false;
		});
		/* a wheel message carries screen coordinates and is dispatched synchronously (MouseDown is posted) */
		Point screen = content.toDisplay (50, HEIGHT - 50);
		ViewportDifferential.send (content, Test_org_eclipse_swt_widgets_ScrolledComposite_ViewportDifferential.WM_MOUSEWHEEL, (long) (-120) << 16, ViewportDifferential.makeLParam (screen.x, screen.y));
		assertArrayEquals (new int [] {50, HEIGHT - 50}, mouse, "mouse event in logical coordinates");
		paints.setLength (0);
		content.redraw (0, HEIGHT - 100, 100, 20, false);
		flush ();
		assertEquals ("0," + (HEIGHT - 100) + " 100x20;", paints.toString (), "redraw and paint rectangles are logical");
	}

	@Test
	public void moveAndResizeEventsFollowTheLogicalBounds () {
		StringBuilder events = new StringBuilder ();
		content.addListener (SWT.Move, e -> events.append ("M "));
		content.addListener (SWT.Resize, e -> events.append ("R "));
		sc.setOrigin (0, 700_000);
		flush ();
		assertEquals ("M ", events.toString ());
		events.setLength (0);
		content.setSize (300, HEIGHT + 5);
		flush ();
		assertEquals ("R ", events.toString (), "a logical resize with the same native band sends Resize");
		events.setLength (0);
		sc.setOrigin (0, 700_000);
		flush ();
		assertEquals ("", events.toString (), "no event when the logical bounds do not change");
	}
}

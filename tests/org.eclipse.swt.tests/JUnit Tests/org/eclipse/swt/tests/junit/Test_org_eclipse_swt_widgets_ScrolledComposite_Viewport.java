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

	/* the screen pixel at a point of a control (the shell is on top and the pointer parked) */
	RGB screenPixel (Control control, int x, int y) {
		Point p = control.toDisplay (x, y);
		Image image = new Image (display, 1, 1);
		GC gc = new GC (display);
		try {
			gc.copyArea (image, p.x, p.y);
		} finally {
			gc.dispose ();
		}
		ImageData data = image.getImageData ();
		image.dispose ();
		return data.palette.getRGB (data.getPixel (0, 0));
	}

	@Test
	public void aGcOutsidePaintDrawsInLogicalCoordinates () throws ReflectiveOperationException {
		sc.setOrigin (0, HEIGHT - clientHeight ());
		flush ();
		ViewportDifferential.settle (display, 100);
		/* the device point of a logical point: content.toDisplay is logical, the native client point is screen-relative */
		Point screen = content.toDisplay (75, HEIGHT - 90);
		Class<?> os = Class.forName ("org.eclipse.swt.internal.win32.OS"), pointType = Class.forName ("org.eclipse.swt.internal.win32.POINT");
		Object device = pointType.getConstructor ().newInstance ();
		pointType.getField ("x").setInt (device, screen.x);
		pointType.getField ("y").setInt (device, screen.y);
		os.getMethod ("ScreenToClient", long.class, pointType).invoke (null, content.handle, device);
		GC gc = new GC (content);
		try {
			/* the GC maps the logical point onto that device point: window origin = device origin + logical - device */
			Object origin = pointType.getConstructor ().newInstance ();
			os.getMethod ("GetWindowOrgEx", long.class, pointType).invoke (null, gc.handle, origin);
			assertEquals (75 - pointType.getField ("x").getInt (device), pointType.getField ("x").getInt (origin));
			assertEquals (HEIGHT - 90 - pointType.getField ("y").getInt (device), pointType.getField ("y").getInt (origin));
			gc.setBackground (display.getSystemColor (SWT.COLOR_RED));
			gc.fillRectangle (60, HEIGHT - 100, 30, 20);
		} finally {
			gc.dispose ();
		}
		/* screen pixels exist only on the input desktop (not on the invisible test desktop) */
		if (ViewportDifferential.onInputDesktop ()) {
			assertEquals (new RGB (255, 0, 0), screenPixel (content, 75, HEIGHT - 90), "drawn where the logical point is shown");
		}
	}

	@Test
	public void canvasScrollMovesLogicalRectangles () {
		sc.setOrigin (0, HEIGHT - clientHeight ());
		flush ();
		paints.setLength (0);
		int top = HEIGHT - clientHeight ();
		content.scroll (0, top + 20, 0, top + 30, 100, 50, false);
		flush ();
		/* the source [top + 30, top + 80) moves up by 10: its last 10 px are vacated */
		assertEquals ("0," + (top + 70) + " 100x10;", paints.toString (), "the vacated strip of the source is repainted, in logical coordinates");
	}

	@Test
	public void theCaretIsPlacedAtItsLogicalLocation () throws ReflectiveOperationException {
		sc.setOrigin (0, HEIGHT - clientHeight ());
		flush ();
		Caret caret = new Caret (content, SWT.NONE);
		caret.setBounds (40, HEIGHT - 80, 2, 16);
		Class<?> os = Class.forName ("org.eclipse.swt.internal.win32.OS"), pointType = Class.forName ("org.eclipse.swt.internal.win32.POINT");
		/* keyboard focus within this thread, without activating the shell (the desktop may refuse activation) */
		os.getMethod ("SetFocus", long.class).invoke (null, content.handle);
		ViewportDifferential.settle (display, 100);
		Assumptions.assumeTrue (content.isFocusControl (), "the content did not get the keyboard focus");
		assertEquals (new Rectangle (40, HEIGHT - 80, 2, 16), caret.getBounds ());
		Object point = pointType.getConstructor ().newInstance ();
		os.getMethod ("GetCaretPos", pointType).invoke (null, point);
		os.getMethod ("ClientToScreen", long.class, pointType).invoke (null, content.handle, point);
		Point screen = new Point (pointType.getField ("x").getInt (point), pointType.getField ("y").getInt (point));
		assertEquals (content.toDisplay (40, HEIGHT - 80), screen, "the native caret is where the logical caret location is shown");
	}

	@Test
	public void aFarComboReportsTheNativeHeightOfAComboInRange () {
		Combo near = new Combo (shell, SWT.DROP_DOWN);
		near.setBounds (0, 0, 120, 200);
		Combo far = new Combo (content, SWT.DROP_DOWN);
		far.setBounds (10, HEIGHT - 60, 120, 200);
		assertEquals (near.getSize (), far.getSize (), "a drop-down Combo sets the height of its text field");
		assertEquals (new Point (10, HEIGHT - 60), far.getLocation ());
		near.dispose ();
		far.dispose ();
	}

	@Test
	public void planesFollowAParentLargerThanTheVisibleCap () throws ReflectiveOperationException {
		Composite parent = new Composite (shell, SWT.NONE);
		parent.setBounds (0, 0, 300, 300);
		Composite plane = new Composite (parent, SWT.NONE);
		plane.setBounds (0, -500_000, 300, 1_000_000);
		java.lang.reflect.Field planeField = Control.class.getDeclaredField ("plane");
		planeField.setAccessible (true);
		parent.setSize (300, 20_000);
		Object record = planeField.get (plane);
		java.lang.reflect.Field baseField = record.getClass ().getDeclaredField ("baseY");
		baseField.setAccessible (true);
		int base = baseField.getInt (record);
		int band = Test_ViewportPlaneGeometry.BAND;
		assertTrue (base <= 500_000 && 520_000 <= base + band, "the band covers the parent's 20000 px client area: base " + base);
		assertEquals (new Rectangle (0, -500_000, 300, 1_000_000), plane.getBounds ());
		parent.dispose ();
	}
}

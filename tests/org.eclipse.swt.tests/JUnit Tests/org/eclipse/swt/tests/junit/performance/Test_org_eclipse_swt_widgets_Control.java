/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit.performance;

import junit.framework.*;
import junit.textui.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.test.performance.*;

/**
 * Automated Performance Test Suite for class org.eclipse.swt.widgets.Control
 *
 * @see org.eclipse.swt.widgets.Control
 */
public class Test_org_eclipse_swt_widgets_Control extends Test_org_eclipse_swt_widgets_Widget {

public Test_org_eclipse_swt_widgets_Control(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
}

protected void tearDown() {
	super.tearDown();
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	// abstract class
}

public void test_addControlListenerLorg_eclipse_swt_events_ControlListener() {
	final int COUNT = 7500;
	
	ControlListener[] listeners = new ControlListener[COUNT];
	for (int i = 0; i < COUNT; i++) {
		listeners[i] = new ControlAdapter() {}; 
	}
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.addControlListener(listeners[i]);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_addFocusListenerLorg_eclipse_swt_events_FocusListener() {
	final int COUNT = 7500;
	
	FocusListener[] listeners = new FocusListener[COUNT];
	for (int i = 0; i < COUNT; i++) {
		listeners[i] = new FocusAdapter() {}; 
	}
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.addFocusListener(listeners[i]);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_addHelpListenerLorg_eclipse_swt_events_HelpListener() {
	final int COUNT = 14000;
	
	HelpListener[] listeners = new HelpListener[COUNT];
	for (int i = 0; i < COUNT; i++) {
		listeners[i] = new HelpListener() {
			public void helpRequested(HelpEvent e) {} 
		};
	}
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.addHelpListener(listeners[i]);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_addKeyListenerLorg_eclipse_swt_events_KeyListener() {
	final int COUNT = 7500;
	
	KeyListener[] listeners = new KeyListener[COUNT];
	for (int i = 0; i < COUNT; i++) {
		listeners[i] = new KeyAdapter() {}; 
	}

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.addKeyListener(listeners[i]);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_addMouseListenerLorg_eclipse_swt_events_MouseListener() {
	final int COUNT = 5000;
	
	MouseListener[] listeners = new MouseListener[COUNT];
	for (int i = 0; i < COUNT; i++) {
		listeners[i] = new MouseAdapter() {}; 
	}

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.addMouseListener(listeners[i]);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_addMouseMoveListenerLorg_eclipse_swt_events_MouseMoveListener() {
	final int COUNT = 14000;
	
	MouseMoveListener[] listeners = new MouseMoveListener[COUNT];
	for (int i = 0; i < COUNT; i++) {
		listeners[i] = new MouseMoveListener() {
			public void mouseMove(MouseEvent e) {}
		};
	}

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.addMouseMoveListener(listeners[i]);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_addMouseTrackListenerLorg_eclipse_swt_events_MouseTrackListener() {
	final int COUNT = 5000;
	
	MouseTrackListener[] listeners = new MouseTrackListener[COUNT];
	for (int i = 0; i < COUNT; i++) {
		listeners[i] = new MouseTrackAdapter() {}; 
	}

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.addMouseTrackListener(listeners[i]);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_addPaintListenerLorg_eclipse_swt_events_PaintListener() {
	final int COUNT = 14000;
	
	PaintListener[] listeners = new PaintListener[COUNT];
	for (int i = 0; i < COUNT; i++) {
		listeners[i] = new PaintListener() {
			public void paintControl(PaintEvent e) {}
		}; 
	}

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.addPaintListener(listeners[i]);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_addTraverseListenerLorg_eclipse_swt_events_TraverseListener() {
	final int COUNT = 14000;
	
	TraverseListener[] listeners = new TraverseListener[COUNT];
	for (int i = 0; i < COUNT; i++) {
		listeners[i] = new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
			}
		}; 
	}

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.addTraverseListener(listeners[i]);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_computeSizeII() {
	final int COUNT = 90000;
	PerformanceMeter meter = createMeter("preferred size");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	}
	meter.stop();
	disposeMeter(meter);

	Point size = control.getSize();
	size.x *= 2;
	size.y *= 2;
	
	meter = createMeter("specify width");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.computeSize(size.x, SWT.DEFAULT);
	}
	meter.stop();
	disposeMeter(meter);

	meter = createMeter("specify height");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.computeSize(SWT.DEFAULT, size.y);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_computeSizeIIZ() {
	final int COUNT = 90000;
	
	PerformanceMeter meter = createMeter("preferred size");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
	}
	meter.stop();
	disposeMeter(meter);

	Point size = control.getSize();
	size.x *= 2;
	size.y *= 2;
	
	meter = createMeter("specify width");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.computeSize(size.x, SWT.DEFAULT, false);
	}
	meter.stop();
	disposeMeter(meter);

	meter = createMeter("specify height");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.computeSize(SWT.DEFAULT, size.y, false);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_forceFocus() {
	final int COUNT = 2000000;
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.forceFocus();
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_getAccessible() {
	final int COUNT = 50000000;
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.getAccessible();
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_getBackground() {
	final int COUNT = 20000000;
	
	control.setBackground(control.getDisplay().getSystemColor(SWT.COLOR_RED));
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.getBackground();
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_getBorderWidth() {
	final int COUNT = 4500000;
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.getBorderWidth();
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_getBounds() {
	final int COUNT = 1500000;
	
	control.setBounds(10,20,30,40);
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.getBounds();
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_getEnabled() {
	final int COUNT = 20000000;
	
	control.setEnabled(true);
	
	PerformanceMeter meter = createMeter("enabled");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.getEnabled();
	}
	meter.stop();
	disposeMeter(meter);
	
	control.setEnabled(false);

	meter = createMeter("disabled");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.getEnabled();
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_getFont() {
	final int COUNT = 800000;
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.getFont();
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_getForeground() {
	final int COUNT = 20000000;
	
	control.setForeground(control.getDisplay().getSystemColor(SWT.COLOR_RED));
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.getForeground();
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_getLocation() {
	final int COUNT = 1500000;
	
	control.setBounds(10,20,30,40);
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.getLocation();
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_getMenu() {
	final int COUNT = 50000000;
	
	PerformanceMeter meter = createMeter("no menu");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.getMenu();
	}
	meter.stop();
	disposeMeter(meter);

	Menu menu = new Menu(control.getShell());
	control.setMenu(menu);
	
	meter = createMeter("menu");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.getMenu();
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_getMonitor() {
	final int COUNT = 1200000;
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.getMonitor();
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_getParent() {
	final int COUNT = 50000000;
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.getParent();
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_getShell() {
	final int COUNT = 30000000;
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.getShell();
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_getSize() {
	final int COUNT = 2500000;
	
	control.setBounds(10,20,30,40);
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.getSize();
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_getToolTipText() {
	final int COUNT = 50000000;
	
	control.setToolTipText("Tool tip text");
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.getToolTipText();
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_getVisible() {
	final int COUNT = 9000000;
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.getVisible();
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_isEnabled() {
	final int COUNT = 8000000;
	
	control.setEnabled(true);
	
	PerformanceMeter meter = createMeter("enabled");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.isEnabled();
	}
	meter.stop();
	disposeMeter(meter);
	
	control.setEnabled(false);
	
	meter = createMeter("disabled");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.isEnabled();
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_isFocusControl() {
	final int COUNT = 2000000;
	
	control.getParent().setFocus();
	
	PerformanceMeter meter = createMeter("not focused");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.isFocusControl();
	}
	meter.stop();
	disposeMeter(meter);

	control.setFocus();
	
	meter = createMeter("focused");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.isFocusControl();
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_isReparentable() {
	final int COUNT = 45000000;
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.isReparentable();
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_isVisible() {
	final int COUNT = 9000000;
	
	control.getShell().setVisible(false);
	
	PerformanceMeter meter = createMeter("not visible");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.isVisible();
	}
	meter.stop();
	disposeMeter(meter);
	
	control.getShell().setVisible(true);
	control.setVisible(true);

	meter = createMeter("visible");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.isVisible();
	}
	meter.stop();
	disposeMeter(meter);
	
	control.getShell().setVisible(false);
}

public void test_moveAboveLorg_eclipse_swt_widgets_Control() {
	final int COUNT = 600000;
	
	Button button = new Button(shell, SWT.PUSH);
	
	Control[] controls = new Control[COUNT];
	for (int i = 0; i < COUNT; i += 4) {
		controls[i] = button;
		controls[i+1] = null;
		controls[i+2] = button;
		controls[i+3] = control;
	}
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.moveAbove(controls[i]);
	}
	meter.stop();
	disposeMeter(meter);
	
	button.dispose();
}

public void test_moveBelowLorg_eclipse_swt_widgets_Control() {
	final int COUNT = 250000;
	
	Button button = new Button(shell, SWT.PUSH);
	
	Control[] controls = new Control[COUNT];
	for (int i = 0; i < COUNT; i += 4) {
		controls[i] = button;
		controls[i+1] = null;
		controls[i+2] = button;
		controls[i+3] = control;
	}
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.moveBelow(controls[i]);
	}
	meter.stop();
	disposeMeter(meter);
	
	button.dispose();
}

public void test_pack() {
	final int COUNT = 12000;
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.pack();
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_packZ() {
	final int COUNT = 12000;
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.pack(false);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_redraw() {
	final int COUNT = 10000000;
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.redraw();
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_redrawIIIIZ() {
	final int COUNT = 40000000;
	
	Rectangle bounds = control.getBounds();
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.redraw(bounds.x, bounds.y, bounds.width, bounds.height, true);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_removeControlListenerLorg_eclipse_swt_events_ControlListener() {
	// tested in test_addControlListenerLorg_eclipse_swt_events_ControlListener
}

public void test_removeFocusListenerLorg_eclipse_swt_events_FocusListener() {
	// tested in test_addFocusListenerLorg_eclipse_swt_events_FocusListener
}

public void test_removeHelpListenerLorg_eclipse_swt_events_HelpListener() {
	// tested in test_addHelpListenerLorg_eclipse_swt_events_HelpListener
}

public void test_removeKeyListenerLorg_eclipse_swt_events_KeyListener() {
	// tested in test_addKeyListenerLorg_eclipse_swt_events_KeyListener
}

public void test_removeMouseListenerLorg_eclipse_swt_events_MouseListener() {
	// tested in test_addMouseListenerLorg_eclipse_swt_events_MouseListener
}

public void test_removeMouseMoveListenerLorg_eclipse_swt_events_MouseMoveListener() {
	// tested in test_addMouseMoveListenerLorg_eclipse_swt_events_MouseMoveListener
}

public void test_removeMouseTrackListenerLorg_eclipse_swt_events_MouseTrackListener() {
	// tested in test_addMouseTrackListenerLorg_eclipse_swt_events_MouseTrackListener
}

public void test_removePaintListenerLorg_eclipse_swt_events_PaintListener() {
	// tested in test_addPaintListenerLorg_eclipse_swt_events_PaintListener
}

public void test_removeTraverseListenerLorg_eclipse_swt_events_TraverseListener() {
	// tested in test_addTraverseListenerLorg_eclipse_swt_events_TraverseListener
}

public void test_setBackgroundLorg_eclipse_swt_graphics_Color() {
	final int COUNT = 2000000;
	
	Color red = control.getDisplay().getSystemColor(SWT.COLOR_RED);
	Color blue = control.getDisplay().getSystemColor(SWT.COLOR_BLUE);
	Color[] colors = new Color[COUNT];
	for (int i = 0; i < COUNT; i += 4) {
		colors[i] = red;
		colors[i+1] = blue;
		colors[i+2] = null;
		colors[i+3] = blue;
	}
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.setBackground(colors[i]);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_setBoundsIIII() {
	final int COUNT = 12000;
	
	int[] xs = new int[COUNT];
	int[] ys = new int[COUNT];
	int[] widths = new int[COUNT];
	int[] heights = new int[COUNT];
	for (int i = 0; i < COUNT; i += 2) {
		xs[i] = 10;			xs[i+1] = 40;
		ys[i] = 20;			ys[i+1] = 30;
		widths[i] = 30;		widths[i+1] = 20;
		heights[i] = 40;	heights[i+1] = 10;
	}

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.setBounds(xs[i], ys[i], widths[i], heights[i]);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_setBoundsLorg_eclipse_swt_graphics_Rectangle() {
	final int COUNT = 12000;
	
	Rectangle[] rectangles = new Rectangle[COUNT];
	Rectangle rectangle1 = new Rectangle(0,5,10,15);
	Rectangle rectangle2 = new Rectangle(10,15,110,115);
	for (int i = 0; i < COUNT; i += 2) {
		rectangles[i] = rectangle1;
		rectangles[i+1] = rectangle2;
	}

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.setBounds(rectangles[i]);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_setCaptureZ() {
	final int COUNT = 33000;
	
	boolean[] capture = new boolean [COUNT];
	for (int i = 0; i < COUNT; i += 2) {
		capture[i] = true;
		capture[i+1] = false;
	}
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.setCapture(capture[i]);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_setCursorLorg_eclipse_swt_graphics_Cursor() {
	final int COUNT = 250000;
	
	Cursor hand = control.getDisplay().getSystemCursor(SWT.CURSOR_HAND);
	Cursor size = control.getDisplay().getSystemCursor(SWT.CURSOR_SIZEALL);
	Cursor[] cursors = new Cursor[COUNT];
	for (int i = 0; i < COUNT; i += 4) {
		cursors[i] = hand;
		cursors[i+1] = size;
		cursors[i+2] = null;
		cursors[i+3] = size;
	}
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.setCursor(cursors[i]);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_setEnabledZ() {
	final int COUNT = 200000;
	
	boolean[] capture = new boolean [COUNT];
	for (int i = 0; i < COUNT; i += 2) {
		capture[i] = true;
		capture[i+1] = false;
	}
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.setEnabled(capture[i]);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_setFocus() {
	final int COUNT = 10000;
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.setFocus();
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_setFontLorg_eclipse_swt_graphics_Font() {
	final int COUNT = 800000;
	
	Font font1 = control.getFont();
	FontData[] data = font1.getFontData();
	data[0].height += 2;
	Font font2 = new Font(control.getDisplay(), data);
	Font[] fonts = new Font[COUNT];
	for (int i = 0; i < COUNT; i += 4) {
		fonts[i] = font1;
		fonts[i+1] = null;
		fonts[i+2] = font1;
		fonts[i+3] = font2;
	}

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.setFont(fonts[i]);
	}
	meter.stop();
	disposeMeter(meter);

	font2.dispose();
}

public void test_setForegroundLorg_eclipse_swt_graphics_Color() {
	final int COUNT = 2000000;
	
	Color red = control.getDisplay().getSystemColor(SWT.COLOR_RED);
	Color blue = control.getDisplay().getSystemColor(SWT.COLOR_BLUE);
	Color[] colors = new Color[COUNT];
	for (int i = 0; i < COUNT; i += 4) {
		colors[i] = red;
		colors[i+1] = blue;
		colors[i+2] = null;
		colors[i+3] = blue;
	}
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.setForeground(colors[i]);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_setLocationII() {
	final int COUNT = 13000;
	
	int[] xs = new int[COUNT];
	int[] ys = new int[COUNT];
	for (int i = 0; i < COUNT; i += 2) {
		xs[i] = 10;			xs[i+1] = 40;
		ys[i] = 20;			ys[i+1] = 30;
	}

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.setLocation(xs[i], ys[i]);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_setLocationLorg_eclipse_swt_graphics_Point() {
	final int COUNT = 13000;
	
	Point[] points = new Point[COUNT];
	Point point1 = new Point(0,5);
	Point point2 = new Point(10,15);
	for (int i = 0; i < COUNT; i += 2) {
		points[i] = point1;
		points[i + 1] = point2;
	}

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.setLocation(points[i]);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_setMenuLorg_eclipse_swt_widgets_Menu () {
	final int COUNT = 15000000;
	
	Menu menu1 = new Menu(control);
	new MenuItem(menu1, SWT.NONE).setText("item1");
	Menu menu2 = new Menu(control);
	new MenuItem(menu2, SWT.NONE).setText("item2");
	Menu[] menus = new Menu[COUNT];
	for (int i = 0; i < COUNT; i += 4) {
		menus[i] = menu1;
		menus[i+1] = null;
		menus[i+2] = menu1;
		menus[i+3] = menu2;
	}
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.setMenu(menus[i]);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_setRedrawZ() {
	final int COUNT = 12000000;
	
	boolean[] capture = new boolean [COUNT];
	for (int i = 0; i < COUNT; i += 2) {
		capture[i] = true;
		capture[i+1] = false;
	}
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.setRedraw(capture[i]);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_setSizeII() {
	final int COUNT = 13000;
	
	int[] widths = new int[COUNT];
	int[] heights = new int[COUNT];
	for (int i = 0; i < COUNT; i += 2) {
		widths[i] = 30;		widths[i+1] = 20;
		heights[i] = 40;	heights[i+1] = 10;
	}

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.setSize(widths[i], heights[i]);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_setSizeLorg_eclipse_swt_graphics_Point() {
	final int COUNT = 13000;
	
	Point[] points = new Point[COUNT];
	Point point1 = new Point(20,15);
	Point point2 = new Point(10,35);
	for (int i = 0; i < COUNT; i += 2) {
		points[i] = point1;
		points[i + 1] = point2;
	}

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.setSize(points[i]);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_setToolTipTextLjava_lang_String() {
	final int COUNT = 100000;
	
	String[] strings = new String[COUNT];
	for (int i = 0; i < COUNT; i += 4) {
		strings[0] = "tool tip 1";
		strings[1] = null;
		strings[2] = "";
		strings[3] = "the second tool tip";
	}

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.setToolTipText(strings[i]);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_setVisibleZ() {
	final int COUNT = 200000;
	
	boolean[] capture = new boolean [COUNT];
	for (int i = 0; i < COUNT; i += 2) {
		capture[i] = true;
		capture[i+1] = false;
	}
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.setVisible(capture[i]);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_toControlII() {
	final int COUNT = 4000000;
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.toControl(20, 30);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_toControlLorg_eclipse_swt_graphics_Point() {
	final int COUNT = 2500000;
	
	Point point = new Point(10,20);
	Point[] points = new Point[COUNT];
	for (int i = 0; i < COUNT; i++) {
		points[i] = point;
	}

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.toControl(points[i]);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_toDisplayII() {
	final int COUNT = 4000000;
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.toDisplay(20, 30);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_toDisplayLorg_eclipse_swt_graphics_Point() {
	final int COUNT = 4000000;
	
	Point point = new Point(10,20);
	Point[] points = new Point[COUNT];
	for (int i = 0; i < COUNT; i++) {
		points[i] = point;
	}

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.toDisplay(points[i]);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_traverseI() {
	final int COUNT = 3000000;
	
	PerformanceMeter meter = createMeter("TRAVERSE_ARROW_NEXT");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.traverse(SWT.TRAVERSE_ARROW_NEXT);
	}
	meter.stop();
	disposeMeter(meter);
	
	meter = createMeter("TRAVERSE_ARROW_PREVIOUS");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.traverse(SWT.TRAVERSE_ARROW_PREVIOUS);
	}
	meter.stop();
	disposeMeter(meter);
	
	meter = createMeter("TRAVERSE_ESCAPE");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.traverse(SWT.TRAVERSE_ESCAPE);
	}
	meter.stop();
	disposeMeter(meter);
	
	meter = createMeter("TRAVERSE_MNEMONIC");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.traverse(SWT.TRAVERSE_MNEMONIC);
	}
	meter.stop();
	disposeMeter(meter);
	
	meter = createMeter("TRAVERSE_NONE");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.traverse(SWT.TRAVERSE_NONE);
	}
	meter.stop();
	disposeMeter(meter);
	
	meter = createMeter("TRAVERSE_PAGE_NEXT");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.traverse(SWT.TRAVERSE_PAGE_NEXT);
	}
	meter.stop();
	disposeMeter(meter);
	
	meter = createMeter("TRAVERSE_PAGE_PREVIOUS");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.traverse(SWT.TRAVERSE_PAGE_PREVIOUS);
	}
	meter.stop();
	disposeMeter(meter);
	
	meter = createMeter("TRAVERSE_RETURN");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.traverse(SWT.TRAVERSE_RETURN);
	}
	meter.stop();
	disposeMeter(meter);

	meter = createMeter("TRAVERSE_TAB_NEXT");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.traverse(SWT.TRAVERSE_TAB_NEXT);
	}
	meter.stop();
	disposeMeter(meter);

	meter = createMeter("TRAVERSE_TAB_PREVIOUS");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.traverse(SWT.TRAVERSE_TAB_PREVIOUS);
	}
	meter.stop();
	disposeMeter(meter);
}

public void test_update() {
	final int COUNT = 2000000;
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		control.update();
	}
	meter.stop();
	disposeMeter(meter);
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_Control((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_addControlListenerLorg_eclipse_swt_events_ControlListener");
	methodNames.addElement("test_addFocusListenerLorg_eclipse_swt_events_FocusListener");
	methodNames.addElement("test_addHelpListenerLorg_eclipse_swt_events_HelpListener");
	methodNames.addElement("test_addKeyListenerLorg_eclipse_swt_events_KeyListener");
	methodNames.addElement("test_addMouseListenerLorg_eclipse_swt_events_MouseListener");
	methodNames.addElement("test_addMouseMoveListenerLorg_eclipse_swt_events_MouseMoveListener");
	methodNames.addElement("test_addMouseTrackListenerLorg_eclipse_swt_events_MouseTrackListener");
	methodNames.addElement("test_addPaintListenerLorg_eclipse_swt_events_PaintListener");
	methodNames.addElement("test_addTraverseListenerLorg_eclipse_swt_events_TraverseListener");
	methodNames.addElement("test_computeSizeII");
	methodNames.addElement("test_computeSizeIIZ");
	methodNames.addElement("test_forceFocus");
	methodNames.addElement("test_getAccessible");
	methodNames.addElement("test_getBackground");
	methodNames.addElement("test_getBorderWidth");
	methodNames.addElement("test_getBounds");
	methodNames.addElement("test_getEnabled");
	methodNames.addElement("test_getFont");
	methodNames.addElement("test_getForeground");
	methodNames.addElement("test_getLocation");
	methodNames.addElement("test_getMenu");
	methodNames.addElement("test_getMonitor");
	methodNames.addElement("test_getParent");
	methodNames.addElement("test_getShell");
	methodNames.addElement("test_getSize");
	methodNames.addElement("test_getToolTipText");
	methodNames.addElement("test_getVisible");
	methodNames.addElement("test_isEnabled");
	methodNames.addElement("test_isFocusControl");
	methodNames.addElement("test_isReparentable");
	methodNames.addElement("test_isVisible");
	methodNames.addElement("test_moveAboveLorg_eclipse_swt_widgets_Control");
	methodNames.addElement("test_moveBelowLorg_eclipse_swt_widgets_Control");
	methodNames.addElement("test_pack");
	methodNames.addElement("test_packZ");
	methodNames.addElement("test_redraw");
	methodNames.addElement("test_redrawIIIIZ");
	methodNames.addElement("test_removeControlListenerLorg_eclipse_swt_events_ControlListener");
	methodNames.addElement("test_removeFocusListenerLorg_eclipse_swt_events_FocusListener");
	methodNames.addElement("test_removeHelpListenerLorg_eclipse_swt_events_HelpListener");
	methodNames.addElement("test_removeKeyListenerLorg_eclipse_swt_events_KeyListener");
	methodNames.addElement("test_removeMouseListenerLorg_eclipse_swt_events_MouseListener");
	methodNames.addElement("test_removeMouseMoveListenerLorg_eclipse_swt_events_MouseMoveListener");
	methodNames.addElement("test_removeMouseTrackListenerLorg_eclipse_swt_events_MouseTrackListener");
	methodNames.addElement("test_removePaintListenerLorg_eclipse_swt_events_PaintListener");
	methodNames.addElement("test_removeTraverseListenerLorg_eclipse_swt_events_TraverseListener");
	methodNames.addElement("test_setBackgroundLorg_eclipse_swt_graphics_Color");
	methodNames.addElement("test_setBoundsIIII");
	methodNames.addElement("test_setBoundsLorg_eclipse_swt_graphics_Rectangle");
	methodNames.addElement("test_setCaptureZ");
	methodNames.addElement("test_setCursorLorg_eclipse_swt_graphics_Cursor");
	methodNames.addElement("test_setEnabledZ");
	methodNames.addElement("test_setFocus");
	methodNames.addElement("test_setFontLorg_eclipse_swt_graphics_Font");
	methodNames.addElement("test_setForegroundLorg_eclipse_swt_graphics_Color");
	methodNames.addElement("test_setLocationII");
	methodNames.addElement("test_setLocationLorg_eclipse_swt_graphics_Point");
	methodNames.addElement("test_setMenuLorg_eclipse_swt_widgets_Menu");
	methodNames.addElement("test_setRedrawZ");
	methodNames.addElement("test_setSizeII");
	methodNames.addElement("test_setSizeLorg_eclipse_swt_graphics_Point");
	methodNames.addElement("test_setToolTipTextLjava_lang_String");
	methodNames.addElement("test_setVisibleZ");
	methodNames.addElement("test_toControlII");
	methodNames.addElement("test_toControlLorg_eclipse_swt_graphics_Point");
	methodNames.addElement("test_toDisplayII");
	methodNames.addElement("test_toDisplayLorg_eclipse_swt_graphics_Point");
	methodNames.addElement("test_traverseI");
	methodNames.addElement("test_update");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Widget.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_addControlListenerLorg_eclipse_swt_events_ControlListener")) test_addControlListenerLorg_eclipse_swt_events_ControlListener();
	else if (getName().equals("test_addFocusListenerLorg_eclipse_swt_events_FocusListener")) test_addFocusListenerLorg_eclipse_swt_events_FocusListener();
	else if (getName().equals("test_addHelpListenerLorg_eclipse_swt_events_HelpListener")) test_addHelpListenerLorg_eclipse_swt_events_HelpListener();
	else if (getName().equals("test_addKeyListenerLorg_eclipse_swt_events_KeyListener")) test_addKeyListenerLorg_eclipse_swt_events_KeyListener();
	else if (getName().equals("test_addMouseListenerLorg_eclipse_swt_events_MouseListener")) test_addMouseListenerLorg_eclipse_swt_events_MouseListener();
	else if (getName().equals("test_addMouseMoveListenerLorg_eclipse_swt_events_MouseMoveListener")) test_addMouseMoveListenerLorg_eclipse_swt_events_MouseMoveListener();
	else if (getName().equals("test_addMouseTrackListenerLorg_eclipse_swt_events_MouseTrackListener")) test_addMouseTrackListenerLorg_eclipse_swt_events_MouseTrackListener();
	else if (getName().equals("test_addPaintListenerLorg_eclipse_swt_events_PaintListener")) test_addPaintListenerLorg_eclipse_swt_events_PaintListener();
	else if (getName().equals("test_addTraverseListenerLorg_eclipse_swt_events_TraverseListener")) test_addTraverseListenerLorg_eclipse_swt_events_TraverseListener();
	else if (getName().equals("test_computeSizeII")) test_computeSizeII();
	else if (getName().equals("test_computeSizeIIZ")) test_computeSizeIIZ();
	else if (getName().equals("test_forceFocus")) test_forceFocus();
	else if (getName().equals("test_getAccessible")) test_getAccessible();
	else if (getName().equals("test_getBackground")) test_getBackground();
	else if (getName().equals("test_getBorderWidth")) test_getBorderWidth();
	else if (getName().equals("test_getBounds")) test_getBounds();
	else if (getName().equals("test_getEnabled")) test_getEnabled();
	else if (getName().equals("test_getFont")) test_getFont();
	else if (getName().equals("test_getForeground")) test_getForeground();
	else if (getName().equals("test_getLocation")) test_getLocation();
	else if (getName().equals("test_getMenu")) test_getMenu();
	else if (getName().equals("test_getMonitor")) test_getMonitor();
	else if (getName().equals("test_getParent")) test_getParent();
	else if (getName().equals("test_getShell")) test_getShell();
	else if (getName().equals("test_getSize")) test_getSize();
	else if (getName().equals("test_getToolTipText")) test_getToolTipText();
	else if (getName().equals("test_getVisible")) test_getVisible();
	else if (getName().equals("test_isEnabled")) test_isEnabled();
	else if (getName().equals("test_isFocusControl")) test_isFocusControl();
	else if (getName().equals("test_isReparentable")) test_isReparentable();
	else if (getName().equals("test_isVisible")) test_isVisible();
	else if (getName().equals("test_moveAboveLorg_eclipse_swt_widgets_Control")) test_moveAboveLorg_eclipse_swt_widgets_Control();
	else if (getName().equals("test_moveBelowLorg_eclipse_swt_widgets_Control")) test_moveBelowLorg_eclipse_swt_widgets_Control();
	else if (getName().equals("test_pack")) test_pack();
	else if (getName().equals("test_packZ")) test_packZ();
	else if (getName().equals("test_redraw")) test_redraw();
	else if (getName().equals("test_redrawIIIIZ")) test_redrawIIIIZ();
	else if (getName().equals("test_removeControlListenerLorg_eclipse_swt_events_ControlListener")) test_removeControlListenerLorg_eclipse_swt_events_ControlListener();
	else if (getName().equals("test_removeFocusListenerLorg_eclipse_swt_events_FocusListener")) test_removeFocusListenerLorg_eclipse_swt_events_FocusListener();
	else if (getName().equals("test_removeHelpListenerLorg_eclipse_swt_events_HelpListener")) test_removeHelpListenerLorg_eclipse_swt_events_HelpListener();
	else if (getName().equals("test_removeKeyListenerLorg_eclipse_swt_events_KeyListener")) test_removeKeyListenerLorg_eclipse_swt_events_KeyListener();
	else if (getName().equals("test_removeMouseListenerLorg_eclipse_swt_events_MouseListener")) test_removeMouseListenerLorg_eclipse_swt_events_MouseListener();
	else if (getName().equals("test_removeMouseMoveListenerLorg_eclipse_swt_events_MouseMoveListener")) test_removeMouseMoveListenerLorg_eclipse_swt_events_MouseMoveListener();
	else if (getName().equals("test_removeMouseTrackListenerLorg_eclipse_swt_events_MouseTrackListener")) test_removeMouseTrackListenerLorg_eclipse_swt_events_MouseTrackListener();
	else if (getName().equals("test_removePaintListenerLorg_eclipse_swt_events_PaintListener")) test_removePaintListenerLorg_eclipse_swt_events_PaintListener();
	else if (getName().equals("test_removeTraverseListenerLorg_eclipse_swt_events_TraverseListener")) test_removeTraverseListenerLorg_eclipse_swt_events_TraverseListener();
	else if (getName().equals("test_setBackgroundLorg_eclipse_swt_graphics_Color")) test_setBackgroundLorg_eclipse_swt_graphics_Color();
	else if (getName().equals("test_setBoundsIIII")) test_setBoundsIIII();
	else if (getName().equals("test_setBoundsLorg_eclipse_swt_graphics_Rectangle")) test_setBoundsLorg_eclipse_swt_graphics_Rectangle();
	else if (getName().equals("test_setCaptureZ")) test_setCaptureZ();
	else if (getName().equals("test_setCursorLorg_eclipse_swt_graphics_Cursor")) test_setCursorLorg_eclipse_swt_graphics_Cursor();
	else if (getName().equals("test_setEnabledZ")) test_setEnabledZ();
	else if (getName().equals("test_setFocus")) test_setFocus();
	else if (getName().equals("test_setFontLorg_eclipse_swt_graphics_Font")) test_setFontLorg_eclipse_swt_graphics_Font();
	else if (getName().equals("test_setForegroundLorg_eclipse_swt_graphics_Color")) test_setForegroundLorg_eclipse_swt_graphics_Color();
	else if (getName().equals("test_setLocationII")) test_setLocationII();
	else if (getName().equals("test_setLocationLorg_eclipse_swt_graphics_Point")) test_setLocationLorg_eclipse_swt_graphics_Point();
	else if (getName().equals("test_setMenuLorg_eclipse_swt_widgets_Menu")) test_setMenuLorg_eclipse_swt_widgets_Menu();
	else if (getName().equals("test_setRedrawZ")) test_setRedrawZ();
	else if (getName().equals("test_setSizeII")) test_setSizeII();
	else if (getName().equals("test_setSizeLorg_eclipse_swt_graphics_Point")) test_setSizeLorg_eclipse_swt_graphics_Point();
	else if (getName().equals("test_setToolTipTextLjava_lang_String")) test_setToolTipTextLjava_lang_String();
	else if (getName().equals("test_setVisibleZ")) test_setVisibleZ();
	else if (getName().equals("test_toControlII")) test_toControlII();
	else if (getName().equals("test_toControlLorg_eclipse_swt_graphics_Point")) test_toControlLorg_eclipse_swt_graphics_Point();
	else if (getName().equals("test_toDisplayII")) test_toDisplayII();
	else if (getName().equals("test_toDisplayLorg_eclipse_swt_graphics_Point")) test_toDisplayLorg_eclipse_swt_graphics_Point();
	else if (getName().equals("test_traverseI")) test_traverseI();
	else if (getName().equals("test_update")) test_update();
	else super.runTest();
}

/* custom */
	Control control;
	boolean eventOccurred;

protected void setWidget(Widget w) {
	control = (Control)w;
	super.setWidget(w);
}
}

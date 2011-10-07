/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import java.util.Vector;

import junit.framework.*;
import junit.textui.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.Accessible;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Control
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

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	// abstract class
}

public void test_addControlListenerLorg_eclipse_swt_events_ControlListener() {
	ControlListener listener = new ControlListener() {
		public void controlMoved(ControlEvent e) {
			eventOccurred = true;
		}
		public void controlResized(ControlEvent e) {
			eventOccurred = true;
		}
	};
	control.addControlListener(listener);
	eventOccurred = false;
	control.notifyListeners(SWT.Move, new Event());
	assertTrue(eventOccurred);
	eventOccurred = false;
	control.notifyListeners(SWT.Resize, new Event());
	assertTrue(eventOccurred);
	control.removeControlListener(listener);
}

public void test_addFocusListenerLorg_eclipse_swt_events_FocusListener() {
	FocusListener listener = new FocusListener() {
		public void focusGained(FocusEvent e) {
			eventOccurred = true;
		}
		public void focusLost(FocusEvent e) {
			eventOccurred = true;
		}
	};
	control.addFocusListener(listener);
	eventOccurred = false;
	control.notifyListeners(SWT.FocusIn, new Event());
	assertTrue(eventOccurred);
	eventOccurred = false;
	control.notifyListeners(SWT.FocusOut, new Event());
	assertTrue(eventOccurred);
	control.removeFocusListener(listener);
}

public void test_addHelpListenerLorg_eclipse_swt_events_HelpListener() {
	HelpListener listener = new HelpListener() {
		public void helpRequested(HelpEvent e) {
			eventOccurred = true;
		}
	};
	control.addHelpListener(listener);
	eventOccurred = false;
	control.notifyListeners(SWT.Help, new Event());
	assertTrue(eventOccurred);
	control.removeHelpListener(listener);
}

public void test_addKeyListenerLorg_eclipse_swt_events_KeyListener() {
	KeyListener listener = new KeyListener() {
		public void keyPressed(KeyEvent e) {
			eventOccurred = true;
		}
		public void keyReleased(KeyEvent e) {
			eventOccurred = true;
		}
	};
	control.addKeyListener(listener);
	eventOccurred = false;
	control.notifyListeners(SWT.KeyDown, new Event());
	assertTrue(eventOccurred);
	eventOccurred = false;
	control.notifyListeners(SWT.KeyUp, new Event());
	assertTrue(eventOccurred);
	control.removeKeyListener(listener);
}

public void test_addMouseListenerLorg_eclipse_swt_events_MouseListener() {
	MouseListener listener = new MouseListener() {
		public void mouseDown(MouseEvent e) {
			eventOccurred = true;
		}
		public void mouseUp(MouseEvent e) {
			eventOccurred = true;
		}
		public void mouseDoubleClick(MouseEvent e) {
			eventOccurred = true;
		}
	};
	control.addMouseListener(listener);
	eventOccurred = false;
	control.notifyListeners(SWT.MouseDown, new Event());
	assertTrue(eventOccurred);
	eventOccurred = false;
	control.notifyListeners(SWT.MouseUp, new Event());
	assertTrue(eventOccurred);
	eventOccurred = false;
	control.notifyListeners(SWT.MouseDoubleClick, new Event());
	assertTrue(eventOccurred);
	control.removeMouseListener(listener);
}

public void test_addMouseMoveListenerLorg_eclipse_swt_events_MouseMoveListener() {
	MouseMoveListener listener = new MouseMoveListener() {
		public void mouseMove(MouseEvent e) {
			eventOccurred = true;
		}
	};
	control.addMouseMoveListener(listener);
	eventOccurred = false;
	control.notifyListeners(SWT.MouseMove, new Event());
	assertTrue(eventOccurred);
	control.removeMouseMoveListener(listener);
}

public void test_addMouseTrackListenerLorg_eclipse_swt_events_MouseTrackListener() {
	MouseTrackListener listener = new MouseTrackListener() {
		public void mouseEnter(MouseEvent e) {
			eventOccurred = true;
		}
		public void mouseExit(MouseEvent e) {
			eventOccurred = true;
		}
		public void mouseHover(MouseEvent e) {
			eventOccurred = true;
		}
	};
	control.addMouseTrackListener(listener);
	eventOccurred = false;
	control.notifyListeners(SWT.MouseEnter, new Event());
	assertTrue(eventOccurred);
	eventOccurred = false;
	control.notifyListeners(SWT.MouseExit, new Event());
	assertTrue(eventOccurred);
	eventOccurred = false;
	control.notifyListeners(SWT.MouseHover, new Event());
	assertTrue(eventOccurred);
	control.removeMouseTrackListener(listener);
}

public void test_addPaintListenerLorg_eclipse_swt_events_PaintListener() {
	PaintListener listener = new PaintListener() {
		public void paintControl(PaintEvent e) {
			eventOccurred = true;
		}
	};
	control.addPaintListener(listener);
	eventOccurred = false;
	Event event = new Event();
	GC gc = event.gc = new GC(control);	
	control.notifyListeners(SWT.Paint, event);
	gc.dispose();
	assertTrue(eventOccurred);
	control.removePaintListener(listener);
}

public void test_addTraverseListenerLorg_eclipse_swt_events_TraverseListener() {
	TraverseListener listener = new TraverseListener() {
		public void keyTraversed(TraverseEvent e) {
			eventOccurred = true;
		}
	};
	control.addTraverseListener(listener);
	eventOccurred = false;
	control.notifyListeners(SWT.Traverse, new Event());
	assertTrue(eventOccurred);
	control.removeTraverseListener(listener);
}

public void test_computeSizeII() {
	control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	Point size = control.getSize();
	control.computeSize(size.x, size.y);
	assertEquals(size.x, control.getSize().x);
	assertEquals(size.y, control.getSize().y);
}

public void test_computeSizeIIZ() {
	control.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
	Point size = control.getSize();
	control.computeSize(size.x, size.y, false);
	assertEquals(size.x, control.getSize().x);
	assertEquals(size.y, control.getSize().y);
}

public void test_forceFocus() {
	// this is difficult to test in Control.
	// subclasses that wish to test this should override.
}

public void test_getAccessible() {
	Accessible accessible = control.getAccessible();
	assertTrue(":a:", accessible != null);
}

public void test_getBackground() {
	// tested in test_setBackgroundLorg_eclipse_swt_graphics_Color
}

public void test_getBorderWidth() {
	control.getBorderWidth();
}

public void test_getBounds() {
	// tested in test_setBoundsIIII and test_setBoundsLorg_eclipse_swt_graphics_Rectangle
}

public void test_getEnabled() {
	// tested in test_setEnabledZ
}

public void test_getFont() {
	// tested in test_setFontLorg_eclipse_swt_graphics_Font
}

public void test_getForeground() {
	// tested in test_setForegroundLorg_eclipse_swt_graphics_Color
}

public void test_getLayoutData() {
	// tested in test_setLayoutDataLjava_lang_Object
}

public void test_getLocation() {
	control.setBounds(32, 43, 30, 40);
	assertTrue(control.getLocation().equals(new Point(32, 43)));
}

public void test_getMenu() {
	// tested in test_setMenuLorg_eclipse_swt_widgets_Menu
}

public void test_getMonitor() {
	Monitor monitor = control.getMonitor();
	assertNotNull(monitor);
	Display display = control.getDisplay();
	Monitor[] monitors = display.getMonitors();
	int i;
	/* monitor must be listed in Display.getMonitors */
	for (i = 0; i < monitors.length; i++) {
		if (monitor.equals(monitors[i])) break;
	}
	if (i == monitors.length) {
		fail("Control.getMonitor does not return a monitor listed in Display.getMonitors");
	}
}

public void test_getParent() {
	assertEquals(shell, control.getParent());
}

public void test_getShell() {
	assertEquals(shell, control.getShell());
}

public void test_getSize() {
	// tested in test_setSizeII
}

public void test_getToolTipText() {
	// tested in test_setToolTipTextLjava_lang_String
}

public void test_getVisible() {
	// tested in test_setVisibleZ
}

public void test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData() {
	// tested in test_internal_new_GCLorg_eclipse_swt_graphics_GCData
}

public void test_internal_new_GCLorg_eclipse_swt_graphics_GCData() {
	// intentionally not tested - not public API
}

public void test_isEnabled() {
	control.setEnabled(true);
	assertTrue(control.isEnabled());

	control.setEnabled(false);
	assertTrue(!control.isEnabled());
}

public void test_isFocusControl() {
	assertTrue(!control.isFocusControl());
}

public void test_isReparentable() {
	assertEquals ("isReparentable", control.isReparentable(), isReparentablePlatform());
}

public void test_isVisible() {
	control.setVisible(true);
	assertTrue(!control.isVisible());  //because the shell is not visible

	control.setVisible(false);
	assertTrue(!control.isVisible());

	if (!SwtJunit.isAIX) {
		control.setVisible(true);
		shell.setVisible(true);
		assertTrue("Window should be visible", control.isVisible());
		shell.setVisible(false);
		assertTrue("Window should not be visible", !control.isVisible());
	}
}

public void test_moveAboveLorg_eclipse_swt_widgets_Control() {
	control.moveAbove(null);

	control.moveAbove(control);

	Button b = new Button(shell, 0);
	control.moveAbove(b);
	b.dispose();
}

public void test_moveBelowLorg_eclipse_swt_widgets_Control() {
	control.moveBelow(null);

	control.moveBelow(control);

	Button b = new Button(shell, 0);
	control.moveBelow(b);
	b.dispose();
}

public void test_pack() {
	control.pack();
}

public void test_packZ() {
	control.pack(true);
	control.pack(false);
}

public void test_redraw() {
	control.redraw();
}

public void test_redrawIIIIZ() {
	control.redraw(0, 0, 0, 0, false);

	control.redraw(0, 0, 0, 0, true);

	control.redraw(-10, -10, -10, -10, true);

	control.redraw(10, 10, 10, 10, true);

	control.redraw(10, 10, 10, 10, false);

	control.redraw(10000, 10000, 10000, 10000, false);
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
	Color color = new Color(control.getDisplay(), 255, 0, 0);
	control.setBackground(color);
	assertEquals("getBackground not equal color after setBackground(color)", color, control.getBackground());
	control.setBackground(null);
	assertTrue("getBackground unchanged after setBackground(null)", !control.getBackground().equals(color));
	color.dispose();
}

public void test_setBoundsIIII() {
	control.setBounds(10, 20, 30, 40);
	assertEquals(new Rectangle(10, 20, 30, 40), control.getBounds());

	control.setBounds(20, 30, 40, 50);
	assertEquals(false, control.getBounds().equals(new Rectangle(10, 20, 30, 40)));

	control.setBounds(10, 20, 30, 40);
}

public void test_setBoundsLorg_eclipse_swt_graphics_Rectangle() {
	control.setBounds(new Rectangle(10, 20, 30, 40));
	assertEquals(new Rectangle(10, 20, 30, 40), control.getBounds());

	control.setBounds(new Rectangle(20, 30, 40, 50));
	assertEquals(false, control.getBounds().equals(new Rectangle(10, 20, 30, 40)));

	try {
		control.setBounds(null);
		fail("No exception thrown for rectangle == null");
	}
	catch (IllegalArgumentException e) {
	}

	control.setBounds(new Rectangle(10, 20, 30, 40));
}

public void test_setCaptureZ() {
	control.setCapture(true);

	control.setCapture(false);
}

public void test_setCursorLorg_eclipse_swt_graphics_Cursor() {
	control.setCursor(null);

	Cursor c = new Cursor(control.getDisplay(), 0);
	control.setCursor(c);
	c.dispose();
}

public void test_setEnabledZ() {
	control.setEnabled(true);
	assertTrue(control.getEnabled());

	control.setEnabled(false);
	assertTrue(!control.getEnabled());
}

public void test_setFocus() {
	control.setFocus();
}

public void test_setFontLorg_eclipse_swt_graphics_Font() {
	Font font = control.getFont();
	control.setFont(font);
	assertEquals(font, control.getFont());
	
	font = new Font(control.getDisplay(), SwtJunit.testFontName, 10, SWT.NORMAL);
	control.setFont(font);
	assertEquals(font, control.getFont());

	control.setFont(null);
	font.dispose();
	try {
		control.setFont(font);
		control.setFont(null);
		fail("No exception thrown for disposed font");
	} catch (IllegalArgumentException e) {
	}
}

public void test_setForegroundLorg_eclipse_swt_graphics_Color() {
	Color color = new Color(control.getDisplay(), 255, 0, 0);
	control.setForeground(color);
	assertEquals(color, control.getForeground());
	control.setForeground(null);
	assertTrue(!control.getForeground().equals(color));
	color.dispose();
}

public void test_setLayoutDataLjava_lang_Object() {
	control.setLayoutData(this);
	assertEquals(this, control.getLayoutData());

	control.setLayoutData("asldsahdahcaslcshdac");
	assertEquals("asldsahdahcaslcshdac", control.getLayoutData());

	control.setLayoutData(this.getClass());
	assertEquals(this.getClass(), control.getLayoutData());

	control.setLayoutData(null);
	assertNull(control.getLayoutData());
}

public void test_setLocationII() {
	control.setBounds(32, 43, 30, 40);
	control.setLocation(11, 22);
	control.setSize(32, 43);
	assertEquals(control.getLocation(), new Point(11, 22));

	control.setLocation(10, 10);
	assertEquals(control.getLocation(), new Point(10, 10));
}

public void test_setLocationLorg_eclipse_swt_graphics_Point() {
	try {
		control.setLocation(null);
		fail("No exception thrown for location == null");
	}
	catch (IllegalArgumentException e) {
	}

	Point loc = new Point(30, 40);
	control.setLocation(loc);
	assertEquals(control.getLocation(), loc);

	loc = new Point(10, 10);
	control.setLocation(loc);
	assertEquals(control.getLocation(), loc);

	loc = new Point(10000, 10000);
	control.setLocation(loc);
	assertEquals(control.getLocation(), loc);

	loc = new Point(-10, -10);
	control.setLocation(loc);
	assertEquals(control.getLocation(), loc);
}

public void test_setMenuLorg_eclipse_swt_widgets_Menu () {
	/* this is a valid thing to do, you can reset a menu to nothing */
	control.setMenu(null);

	Menu m = new Menu(control);
	control.setMenu(m);
	assertEquals(m, control.getMenu());
}

public void test_setParentLorg_eclipse_swt_widgets_Composite() {
	if (control.isReparentable()) {
		Shell originalParent = new Shell();
		Shell newParent = new Shell();
		Button b = new Button(originalParent, SWT.PUSH);
		b.setParent(newParent);
		originalParent.dispose();
		assertTrue(!b.isDisposed());
		newParent.dispose();
	}
}

public void test_setRedrawZ() {
	control.setRedraw(false);

	control.setRedraw(true);
}

public void test_setSizeII() {
	control.setBounds(32, 43, 30, 40);
	assertEquals(new Point(30, 40), control.getSize());

	control.setBounds(32, 43, 30, 40);
	control.setLocation(11, 22);
	control.setSize(32, 43);
	assertEquals(new Point(32, 43), control.getSize());

	control.setSize(0, 0);
	
	control.setSize(10, 10);

	control.setSize(10000, 10000);

	control.setSize(-10, -10);
}

public void test_setSizeLorg_eclipse_swt_graphics_Point() {
	control.setSize(new Point(30, 40));
	assertEquals(new Point(30, 40), control.getSize());

	try {
		control.setSize(null);
		fail("No exception thrown for size == null");
	}
	catch (IllegalArgumentException e) {
	}

	control.setSize(new Point(0, 0));
	
	control.setSize(new Point(10, 10));

	control.setSize(new Point(10000, 10000));

	control.setSize(new Point(-10, -10));
}

public void test_setToolTipTextLjava_lang_String() {
	control.setToolTipText("This is a tip");
	assertEquals("This is a tip", control.getToolTipText());

	control.setToolTipText(null);
	assertNull(control.getToolTipText());
}

public void test_setVisibleZ() {
	control.setVisible(true);
	assertTrue(control.getVisible());

	control.setVisible(false);
	assertTrue(!control.getVisible());
}

public void test_toControlII() {
	Point controlCoords = control.toControl(0, 0);
	assertEquals(new Point(0, 0), control.toDisplay(controlCoords.x, controlCoords.y));
}

public void test_toControlLorg_eclipse_swt_graphics_Point() {
	Point controlCoords = control.toControl(new Point(0, 0));
	assertEquals(new Point(0, 0), control.toDisplay(controlCoords));
	try {
		control.toControl(null);
		fail("No exception thrown for size == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_toDisplayII() {
	Point displayCoords = control.toDisplay(0, 0);
	assertEquals(new Point(0, 0), control.toControl(displayCoords.x, displayCoords.y));
}

public void test_toDisplayLorg_eclipse_swt_graphics_Point() {
	Point displayCoords = control.toDisplay(new Point(0, 0));
	assertEquals(new Point(0, 0), control.toControl(displayCoords));
	try {
		control.toDisplay(null);
		fail("No exception thrown for size == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_traverseI() {
	control.traverse(SWT.TRAVERSE_ESCAPE);
	control.traverse(SWT.TRAVERSE_RETURN);
	control.traverse(SWT.TRAVERSE_TAB_NEXT);
	control.traverse(SWT.TRAVERSE_TAB_PREVIOUS);
	control.traverse(SWT.TRAVERSE_ARROW_NEXT);
	control.traverse(SWT.TRAVERSE_ARROW_PREVIOUS);
}

public void test_update() {
	control.update();
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
	methodNames.addElement("test_getLayoutData");
	methodNames.addElement("test_getLocation");
	methodNames.addElement("test_getMenu");
	methodNames.addElement("test_getMonitor");
	methodNames.addElement("test_getParent");
	methodNames.addElement("test_getShell");
	methodNames.addElement("test_getSize");
	methodNames.addElement("test_getToolTipText");
	methodNames.addElement("test_getVisible");
	methodNames.addElement("test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData");
	methodNames.addElement("test_internal_new_GCLorg_eclipse_swt_graphics_GCData");
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
	methodNames.addElement("test_setLayoutDataLjava_lang_Object");
	methodNames.addElement("test_setLocationII");
	methodNames.addElement("test_setLocationLorg_eclipse_swt_graphics_Point");
	methodNames.addElement("test_setMenuLorg_eclipse_swt_widgets_Menu");
	methodNames.addElement("test_setParentLorg_eclipse_swt_widgets_Composite");
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
	else if (getName().equals("test_getLayoutData")) test_getLayoutData();
	else if (getName().equals("test_getLocation")) test_getLocation();
	else if (getName().equals("test_getMenu")) test_getMenu();
	else if (getName().equals("test_getMonitor")) test_getMonitor();
	else if (getName().equals("test_getParent")) test_getParent();
	else if (getName().equals("test_getShell")) test_getShell();
	else if (getName().equals("test_getSize")) test_getSize();
	else if (getName().equals("test_getToolTipText")) test_getToolTipText();
	else if (getName().equals("test_getVisible")) test_getVisible();
	else if (getName().equals("test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData")) test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData();
	else if (getName().equals("test_internal_new_GCLorg_eclipse_swt_graphics_GCData")) test_internal_new_GCLorg_eclipse_swt_graphics_GCData();
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
	else if (getName().equals("test_setLayoutDataLjava_lang_Object")) test_setLayoutDataLjava_lang_Object();
	else if (getName().equals("test_setLocationII")) test_setLocationII();
	else if (getName().equals("test_setLocationLorg_eclipse_swt_graphics_Point")) test_setLocationLorg_eclipse_swt_graphics_Point();
	else if (getName().equals("test_setMenuLorg_eclipse_swt_widgets_Menu")) test_setMenuLorg_eclipse_swt_widgets_Menu();
	else if (getName().equals("test_setParentLorg_eclipse_swt_widgets_Composite")) test_setParentLorg_eclipse_swt_widgets_Composite();
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

/* a different method in ConsistencyUtility is invoked depending on what method 
 * equals
 * ConsistencyUtility.MOUSE_CLICK:
 * 			paramA is the x coordinate offset from control 				  
 * 			paramB is the y coordinate offset from control
 * 			paramC is the mouse button to click.
 * 			paramD if it equals ConsistencyUtility.ESCAPE_MENU, then another click
 * 			will be made on the border of the shell, to escape the menu that came up
 * 			(ie right clicking in a text widget)
 * 			invokes ConsistencyUtility.postClick(Display, Point, int)
 * ConsistencyUtility.MOUSE_DOUBLECLICK
 * 			paramA is the x coordinate offset from control 				  
 * 			paramB is the y coordinate offset from control
 * 			paramC is the mouse button to click.
 * 			invokes ConsistencyUtility.postDoubleClick(Display, Point, int)
 * ConsistencyUtility.MOUSE_DRAG:
 * 			paramA is the x coordinate offset from control of the origin of drag
 * 			paramB is the y coordinate offset from control of the origin of drag
 * 			paramC is the x coordinate offset from control of the destination of drag
 * 			paramD is the y coordinate offset from control of the destination of drag
 * 			invokes ConsistencyUtility.postDrag(Display, Point, Point)
 * ConsistencyUtility.KEY_PRESS:  
 * 		 	paramA is the character to press
 * 			paramB is the keyCode 
 * 			invokes ConsistencyUtility.postKeyPress(Display, int, int)
 * ConsistencyUtility.DOUBLE_KEY_PRESS:  
 * 		 	paramA is the character to press and hold
 * 			paramB is the keyCode 
 * 			paramC is the second key to press while the first one is held (ie ctrl-a)
 * 			paramD is the second keycode
 * 			invokes ConsistencyUtility.postDoubleKeyPress(Display, int, int, int, int) 
 * ConsistencyUtility.SELECTION:
 * 			paramA is the x coordinate offset from control of the first click
 * 			paramB is the y coordinate offset from control of the first click
 * 			paramC is the x coordinate offset from control of the second click
 * 			paramD is the y coordinate offset from control of the second click
 * 			invokes ConsistencyUtility.postSelection(Display, Point, Point)
 * ConsistencyUtility.SHELL_ICONIFY:
 * 			paramA is the button to click with
 * 			invokes ConsistencyUtility.postShellIconify(Display, Point, int)
 */
protected void consistencyEvent(final int paramA, final int paramB, 
        						final int paramC, final int paramD,
        						final int method, Vector events, boolean focus) {
    if(fTestConsistency) {
        final Display display = shell.getDisplay();
        if(events == null) 
            events = new Vector();
        final String test = getTestName();
        
        shell.setLayout(new org.eclipse.swt.layout.FillLayout());
        shell.setText("Parent");

        shell.pack();
    	shell.open();
    	if(control instanceof Shell) {
    	    ((Shell)control).pack();
        	((Shell)control).open();
    	}
    	final Point[] pt = determineLocations(paramA, paramB, paramC, paramD, method);
        if(focus && !control.setFocus())
            control.forceFocus();
        String[] expectedEvents = hookExpectedEvents(test, events);
        new Thread() {
            public void run() {
                display.wake();
                switch(method) {
                	case ConsistencyUtility.MOUSE_CLICK:
                		Assert.assertTrue(test, 
                            ConsistencyUtility.postClick(display, pt[0], paramC));
                		if(paramD == ConsistencyUtility.ESCAPE_MENU) {
                		    Assert.assertTrue(test, 
	                            ConsistencyUtility.postClick(display, pt[1], 1));
                		}  
                		break;
                    case ConsistencyUtility.MOUSE_DOUBLECLICK:
                        Assert.assertTrue(test, 
                                ConsistencyUtility.postDoubleClick(display, pt[0], paramC));
                    	break;
                    case ConsistencyUtility.KEY_PRESS:
                        Assert.assertTrue(test, 
                            ConsistencyUtility.postKeyPress(display, paramA, paramB));
                    	break;
                    case ConsistencyUtility.DOUBLE_KEY_PRESS:
                        Assert.assertTrue(test,
                            ConsistencyUtility.postDoubleKeyPress(display, paramA, paramB, paramC, paramD));
                    	break;
                    case ConsistencyUtility.MOUSE_DRAG:
                        Assert.assertTrue(test, 
                            ConsistencyUtility.postDrag(display, 
                                    pt[0], pt[1]));
                        break;
                    case ConsistencyUtility.SELECTION:

                        Assert.assertTrue(test, 
                            ConsistencyUtility.postSelection(display, 
                                    pt[0], pt[1]));
                        break;
                    case ConsistencyUtility.SHELL_ICONIFY:
                        if(SwtJunit.isCarbon)
                            Assert.assertTrue(test,
                                ConsistencyUtility.postShellIconify(display, pt[0], paramA));
                        else
                            Assert.assertTrue(test,
                                ConsistencyUtility.postShellIconify(display, pt[1], paramA));
                    	if(control instanceof Shell) {
                    	    display.syncExec(new Thread() {
                    	        public void run() {
                    	            ((Shell)control).setMinimized(false);
                    	        }});
                    	} else
                    	    fail("Iconifying a non shell control");
                    	break;
                }
				display.asyncExec(new Thread() {
				    public void run() {
				        shell.dispose();
				    }
				});
             }
        }.start();

        while(!shell.isDisposed()) {
            if(!display.readAndDispatch()) display.sleep();
        }
        setUp();        
        String[] results = new String[events.size()];
        events.copyInto(results);
        assertEquals(test + " event ordering", expectedEvents, results);
    }
}

protected void consistencyEvent(int paramA, int paramB, 
								int paramC, int paramD,
								int method, Vector events) {
    consistencyEvent(paramA, paramB, paramC, paramD, method, events, true);
}

protected void consistencyEvent(int paramA, int paramB, 
								int paramC, int paramD,
								int method) {
    consistencyEvent(paramA, paramB, paramC, paramD, method, null, true);
}

protected void consistencyPrePackShell() {
    shell.setLayout(new org.eclipse.swt.layout.FillLayout());
    shell.pack();
}

protected void consistencyPrePackShell(Shell shell) {
    consistencyPrePackShell();
    shell.pack();
}


protected Point[] determineLocations(int paramA, int paramB,
        						     int paramC, int paramD, int method) {
    Point[] array = new Point[2];
    if(method >= ConsistencyUtility.MOUSE_CLICK)
        array[0] = control.toDisplay(paramA, paramB);
    if(method >= ConsistencyUtility.MOUSE_DRAG) 
        array[1] = control.toDisplay(paramC, paramD);
    if(method == ConsistencyUtility.MOUSE_CLICK && paramD == ConsistencyUtility.ESCAPE_MENU)
        array[1] = shell.toDisplay(25, -10);
    else if(method == ConsistencyUtility.SHELL_ICONIFY) {
        array[0] = control.toDisplay(0,0);
        array[1] = control.toDisplay(control.getSize().x -20, 0);
    }
    return array;
}

}

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

import static org.junit.Assert.assertArrayEquals;

import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.Accessible;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.junit.Assert;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Control
 *
 * @see org.eclipse.swt.widgets.Control
 */
public class Test_org_eclipse_swt_widgets_Control extends Test_org_eclipse_swt_widgets_Widget {

public Test_org_eclipse_swt_widgets_Control(String name) {
	super(name);
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	// abstract class
}

@Test
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

@Test
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

@Test
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

@Test
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

@Test
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

@Test
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

@Test
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
@Test
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
@Test
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

@Test
public void test_computeSizeII() {
	control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	Point size = control.getSize();
	control.computeSize(size.x, size.y);
	assertEquals(size.x, control.getSize().x);
	assertEquals(size.y, control.getSize().y);
}
@Test
public void test_computeSizeIIZ() {
	control.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
	Point size = control.getSize();
	control.computeSize(size.x, size.y, false);
	assertEquals(size.x, control.getSize().x);
	assertEquals(size.y, control.getSize().y);
}
@Test
public void test_getAccessible() {
	Accessible accessible = control.getAccessible();
	assertTrue(":a:", accessible != null);
}
@Test
public void test_getBorderWidth() {
	control.getBorderWidth();
}

@Test
public void test_getLocation() {
	control.setBounds(32, 43, 30, 40);
	assertTrue(control.getLocation().equals(new Point(32, 43)));
}
@Test
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
@Test
public void test_getParent() {
	assertEquals(shell, control.getParent());
}
@Test
public void test_getShell() {
	assertEquals(shell, control.getShell());
}
@Test
public void test_isEnabled() {
	control.setEnabled(true);
	assertTrue(control.isEnabled());

	control.setEnabled(false);
	assertTrue(!control.isEnabled());
}
@Test
public void test_isFocusControl() {
	assertTrue(!control.isFocusControl());
}
@Test
public void test_isReparentable() {
	assertEquals ("isReparentable", control.isReparentable(), SwtTestUtil.isReparentablePlatform());
}
@Test
public void test_isVisible() {
	control.setVisible(true);
	assertTrue(!control.isVisible());  //because the shell is not visible

	control.setVisible(false);
	assertTrue(!control.isVisible());

	if (!SwtTestUtil.isAIX) {
		control.setVisible(true);
		shell.setVisible(true);
		assertTrue("Window should be visible", control.isVisible());
		shell.setVisible(false);
		assertTrue("Window should not be visible", !control.isVisible());
	}
}
@Test
public void test_moveAboveLorg_eclipse_swt_widgets_Control() {
	control.moveAbove(null);

	control.moveAbove(control);

	Button b = new Button(shell, 0);
	control.moveAbove(b);
	b.dispose();
}
@Test
public void test_moveBelowLorg_eclipse_swt_widgets_Control() {
	control.moveBelow(null);

	control.moveBelow(control);

	Button b = new Button(shell, 0);
	control.moveBelow(b);
	b.dispose();
}
@Test
public void test_pack() {
	control.pack();
}
@Test
public void test_packZ() {
	control.pack(true);
	control.pack(false);
}
@Test
public void test_redraw() {
	control.redraw();
}
@Test
public void test_redrawIIIIZ() {
	control.redraw(0, 0, 0, 0, false);

	control.redraw(0, 0, 0, 0, true);

	control.redraw(-10, -10, -10, -10, true);

	control.redraw(10, 10, 10, 10, true);

	control.redraw(10, 10, 10, 10, false);

	control.redraw(10000, 10000, 10000, 10000, false);
}
@Test
public void test_setBackgroundLorg_eclipse_swt_graphics_Color() {
	Color color = new Color(control.getDisplay(), 255, 0, 0);
	control.setBackground(color);
	assertEquals("getBackground not equal color after setBackground(color)", color, control.getBackground());
	control.setBackground(null);
	assertTrue("getBackground unchanged after setBackground(null)", !control.getBackground().equals(color));
	color.dispose();
}
@Test
public void test_setBoundsIIII() {
	control.setBounds(10, 20, 30, 40);
	assertEquals(new Rectangle(10, 20, 30, 40), control.getBounds());

	control.setBounds(20, 30, 40, 50);
	assertEquals(false, control.getBounds().equals(new Rectangle(10, 20, 30, 40)));

	control.setBounds(10, 20, 30, 40);
}
@Test
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
@Test
public void test_setCaptureZ() {
	control.setCapture(true);

	control.setCapture(false);
}
@Test
public void test_setCursorLorg_eclipse_swt_graphics_Cursor() {
	control.setCursor(null);

	Cursor c = new Cursor(control.getDisplay(), 0);
	control.setCursor(c);
	c.dispose();
}
@Test
public void test_setEnabledZ() {
	control.setEnabled(true);
	assertTrue(control.getEnabled());

	control.setEnabled(false);
	assertTrue(!control.getEnabled());
}
@Test
public void test_setFocus() {
	control.setFocus();
}
@Test
public void test_setFontLorg_eclipse_swt_graphics_Font() {
	Font font = control.getFont();
	control.setFont(font);
	assertEquals(font, control.getFont());
	
	font = new Font(control.getDisplay(), SwtTestUtil.testFontName, 10, SWT.NORMAL);
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
@Test
public void test_setForegroundLorg_eclipse_swt_graphics_Color() {
	Color color = new Color(control.getDisplay(), 255, 0, 0);
	control.setForeground(color);
	assertEquals(color, control.getForeground());
	control.setForeground(null);
	assertTrue(!control.getForeground().equals(color));
	color.dispose();
}
@Test
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
@Test
public void test_setLocationII() {
	control.setBounds(32, 43, 30, 40);
	control.setLocation(11, 22);
	control.setSize(32, 43);
	assertEquals(control.getLocation(), new Point(11, 22));

	control.setLocation(10, 10);
	assertEquals(control.getLocation(), new Point(10, 10));
}
@Test
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
@Test
public void test_setMenuLorg_eclipse_swt_widgets_Menu () {
	/* this is a valid thing to do, you can reset a menu to nothing */
	control.setMenu(null);

	Menu m = new Menu(control);
	control.setMenu(m);
	assertEquals(m, control.getMenu());
}
@Test
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
@Test
public void test_setRedrawZ() {
	control.setRedraw(false);

	control.setRedraw(true);
}
@Test
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
@Test
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
@Test
public void test_setToolTipTextLjava_lang_String() {
	control.setToolTipText("This is a tip");
	assertEquals("This is a tip", control.getToolTipText());

	control.setToolTipText(null);
	assertNull(control.getToolTipText());
}
@Test
public void test_setVisibleZ() {
	control.setVisible(true);
	assertTrue(control.getVisible());

	control.setVisible(false);
	assertTrue(!control.getVisible());
}
@Test
public void test_toControlII() {
	Point controlCoords = control.toControl(0, 0);
	assertEquals(new Point(0, 0), control.toDisplay(controlCoords.x, controlCoords.y));
}
@Test
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
@Test
public void test_toDisplayII() {
	Point displayCoords = control.toDisplay(0, 0);
	assertEquals(new Point(0, 0), control.toControl(displayCoords.x, displayCoords.y));
}
@Test
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
@Test
public void test_traverseI() {
	control.traverse(SWT.TRAVERSE_ESCAPE);
	control.traverse(SWT.TRAVERSE_RETURN);
	control.traverse(SWT.TRAVERSE_TAB_NEXT);
	control.traverse(SWT.TRAVERSE_TAB_PREVIOUS);
	control.traverse(SWT.TRAVERSE_ARROW_NEXT);
	control.traverse(SWT.TRAVERSE_ARROW_PREVIOUS);
}
@Test
public void test_update() {
	control.update();
}

/* custom */
	Control control;
	boolean eventOccurred;

@Override
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
        						final int method, Vector<String> events, boolean focus) {
    if(SwtTestUtil.fTestConsistency) {
        final Display display = shell.getDisplay();
        if(events == null) 
            events = new Vector<String>();
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
            @Override
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
                        Assert.assertTrue(test,
                            ConsistencyUtility.postShellIconify(display, pt[1], paramA));
                    	if(control instanceof Shell) {
                    	    display.syncExec(new Thread() {
                    	        @Override
								public void run() {
                    	            ((Shell)control).setMinimized(false);
                    	        }});
                    	} else
                    	    fail("Iconifying a non shell control");
                    	break;
                }
				display.asyncExec(new Thread() {
				    @Override
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
        assertArrayEquals(test + " event ordering", expectedEvents, results);
    }
}

protected void consistencyEvent(int paramA, int paramB, 
								int paramC, int paramD,
								int method, Vector<String> events) {
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

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

import junit.framework.*;
import junit.textui.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Display
 *
 * @see org.eclipse.swt.widgets.Display
 */
public class Test_org_eclipse_swt_widgets_Display extends Test_org_eclipse_swt_graphics_Device {

public Test_org_eclipse_swt_widgets_Display(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	// There can only be one Display object per thread.
	// If a second Display is created on the same thread, an 
	// InvalidThreadAccessException is thrown.
	//
	// Each test will create its own Display and must dispose of it 
	// before completing.
}

protected void tearDown() {
}

public void test_Constructor() {
	Display disp = new Display();
	disp.dispose();
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceData() {
	Display disp;
	disp = new Display(null);
	disp.dispose();
	
	disp = new Display(new DeviceData());
	disp.dispose();
}

public void test_addFilterILorg_eclipse_swt_widgets_Listener() {
	final int CLOSE_CALLBACK = 0;
	final int DISPOSE_CALLBACK = 1;
	final boolean[] callbackReceived = new boolean[] {false, false};
	
	Listener listener = new Listener() {
		public void handleEvent(Event e) {
			if (e.type == SWT.Close)
				callbackReceived[CLOSE_CALLBACK] = true;
			else if (e.type == SWT.Dispose)
				callbackReceived[DISPOSE_CALLBACK] = true;
		}
	};
	
	Display display = new Display();
	try {
		try {
			display.addFilter(SWT.Dispose, null);
			fail("No exception thrown for addFilter with null argument");
		} catch (IllegalArgumentException e) {
			assertEquals("Incorrect exception thrown for addFilter with null argument", SWT.ERROR_NULL_ARGUMENT, e);
		}
		
		display.addFilter(SWT.Close, listener);
	} finally {
		display.close();
	}
	assertTrue(callbackReceived[CLOSE_CALLBACK]);
	assertFalse(callbackReceived[DISPOSE_CALLBACK]);
}

public void test_addListenerILorg_eclipse_swt_widgets_Listener() {
	final int CLOSE_CALLBACK = 0;
	final int DISPOSE_CALLBACK = 1;
	final boolean[] callbackReceived = new boolean[] {false, false};
	
	Listener listener = new Listener() {
		public void handleEvent(Event e) {
			if (e.type == SWT.Close)
				callbackReceived[CLOSE_CALLBACK] = true;
			else if (e.type == SWT.Dispose)
				callbackReceived[DISPOSE_CALLBACK] = true;
		}
	};
	
	Display display = new Display();
	try {
		try {
			display.addListener(SWT.Close, null);
			fail("No exception thrown for addListener with null argument");
		} catch (IllegalArgumentException e) {
			assertEquals("Incorrect exception thrown for addListener with null argument", SWT.ERROR_NULL_ARGUMENT, e);
		}
		
		display.addListener(SWT.Dispose, listener);
	} finally {
		display.close();
	}
	assertFalse(":a:", callbackReceived[CLOSE_CALLBACK]);
	assertTrue(":b:", callbackReceived[DISPOSE_CALLBACK]);

	display = new Display();
	try {
		display.addListener(SWT.Close, listener);
	} finally {
		display.close();
	}
	assertTrue(":c:", callbackReceived[CLOSE_CALLBACK]);
}

public void test_asyncExecLjava_lang_Runnable() {
	final Display display = new Display();
	try {
		display.asyncExec(new Runnable() {
			public void run() {
				display.beep();
			}
		});
	} finally {
		display.dispose();
	}
}

public void test_beep() {
	Display display = new Display();
	try {
		display.beep();
	} finally {
		display.dispose();
	}
}

public void test_close() {
	Display display = new Display();
	display.close();
	assertTrue(display.isDisposed());
}

public void test_disposeExecLjava_lang_Runnable() {
	// Also tests dispose and isDisposed
	Display testDisplay = new Display();
	disposeExecRan = false;
	testDisplay.disposeExec(new Runnable() {
		public void run() {
			disposeExecRan = true;
		}
	});
	assertEquals("Display should not be disposed", false, testDisplay.isDisposed());
	testDisplay.dispose();
	assertTrue("Display should be disposed", testDisplay.isDisposed());
	assertTrue("DisposeExec Runnable did not run", disposeExecRan);
}

public void test_findDisplayLjava_lang_Thread() {
	assertNull(Display.findDisplay(new Thread()));
	
	Display display = new Display();
	try {
		assertEquals(display, Display.findDisplay(Thread.currentThread()));
	} finally {
		display.dispose();
	}
}

public void test_findWidgetI() {
	warnUnimpl("Test test_findWidgetI not written");
}

public void test_getActiveShell() {
	Display display = new Display();
	try {
		Shell shell = new Shell(display);
		shell.open();
		assertTrue(display.getActiveShell() == shell);
		shell.dispose();
	} finally {
		display.dispose();
	}
}

public void test_getBounds() {
	Display display = new Display();
	try {
		Rectangle rect = display.getBounds();
		assertNotNull(rect);
	} finally {
		display.dispose();
	}
}

public void test_getClientArea() {
	Display display = new Display();
	try {
		Rectangle rect = display.getClientArea();
		assertNotNull(rect);
	} finally {
		display.dispose();
	}
}

public void test_getCurrent() {
	Display display = new Display();
	try {
		assertTrue(display.getThread() == Thread.currentThread());
	} finally {
		display.dispose();
	}
}

public void test_getCursorControl() {
	Display display = new Display();
	try {
		display.getCursorControl();
	} finally {
		display.dispose();
	}
}

public void test_getCursorLocation() {
	Display display = new Display();
	try {
		Point pt = display.getCursorLocation();
		assertNotNull(pt);
		assertTrue(pt.x >= 0);
		assertTrue(pt.y >= 0);
	} finally {
		display.dispose();
	}
}

public void test_getCursorSize() {
	warnUnimpl("Test test_getCursorSize not written");
}

public void test_getData() {
	// tested in setData(Object) method
}

public void test_getDataLjava_lang_String() {
	// tested in setData(String, Object) method
}

public void test_getDefault() {
	Display display = new Display();
	try {
		assertNotNull(Display.getDefault());
	} finally {
		display.dispose();
	}
}

public void test_getDismissalAlignment() {
	Display display = new Display();
	try {
		int alignment = display.getDismissalAlignment();
		assertTrue("getDismissalAlignment should return SWT.LEFT or SWT.RIGHT",
			alignment == SWT.LEFT || alignment == SWT.RIGHT);
	} finally {
		display.dispose();
	}
}

public void test_getDoubleClickTime() {
	Display display = new Display();
	try {
		int time = display.getDoubleClickTime();
		assertTrue(time > 0);
	} finally {
		display.dispose();
	}
}

public void test_getFocusControl() {
	Display display = new Display();
	try {
		display.getFocusControl();
	} finally {
		display.dispose();
	}
}

public void test_getIconDepth() {
	Display display = new Display();
	try {
		int depth = display.getIconDepth();
		assertTrue(depth > 0);
	} finally {
		display.dispose();
	}
}

public void test_getMonitors() {
	Display display = new Display();
	Monitor[] monitors = display.getMonitors();
	assertNotNull(monitors);
	assertTrue("at least one monitor should be returned", monitors.length >= 1);
	for (int i = 0; i < monitors.length; i++)
		assertTrue("monitor at index "+i+" should not be null", monitors[i] != null);
	display.dispose();
}

public void test_getPrimaryMonitor() {
	Display display = new Display();
	Monitor monitor = display.getPrimaryMonitor();
	assertNotNull(monitor);
	display.dispose();
}

public void test_getShells() {
	Display display = new Display();
	try {
		Shell shell1 = new Shell(display);
		Shell shell2 = new Shell(display);
		assertTrue(display.getShells().length == 2);
		shell1.dispose();
		shell2.dispose();
	} finally {
		display.dispose();
	}
}

public void test_getSyncThread() {
	final Display display = new Display();
	try {
		final boolean[] threadRan = new boolean[] {false};
		Thread nonUIThread = new Thread(new Runnable() {
			public void run() {
				// Assume no syncExec runnable is currently being invoked.
				assertNull(display.getSyncThread());
				
				// Create a runnable and invoke with syncExec to verify that
				// the invoking thread is the syncThread.
				final Thread invokingThread = Thread.currentThread();
				display.syncExec(new Runnable() {
					public void run() {
						assertEquals(invokingThread, display.getSyncThread());
					}
				});
				
				// Create a runnable and invoke with asyncExec to verify that
				// the syncThread is null while it's running.
				final boolean[] asyncExecRan = new boolean[] {false};
				display.asyncExec(new Runnable() {
					public void run() {
						assertNull(display.getSyncThread());
						asyncExecRan[0] = true;
					}
				});
				
				try {
					while (!asyncExecRan[0]) {
						Thread.sleep(100);
					}
				} catch (InterruptedException ex) {
				}
				threadRan[0] = true;
				display.wake();
			}
		});
		nonUIThread.start();
		
		while (!threadRan[0]) {
			if (!display.readAndDispatch()) display.sleep ();
		}
	} finally {
		display.dispose();
	}
}

public void test_getSystemColorI() {
	Display display = new Display();
	try {
		int [] colorIds = {
			SWT.COLOR_WIDGET_DARK_SHADOW, SWT.COLOR_WIDGET_NORMAL_SHADOW,
			SWT.COLOR_WIDGET_LIGHT_SHADOW, SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW,
			SWT.COLOR_WIDGET_BACKGROUND, SWT.COLOR_WIDGET_BORDER,
			SWT.COLOR_WIDGET_FOREGROUND, SWT.COLOR_LIST_FOREGROUND,
			SWT.COLOR_LIST_BACKGROUND, SWT.COLOR_LIST_SELECTION,
			SWT.COLOR_LIST_SELECTION_TEXT,
		};
		for (int i=0; i < colorIds.length; i++) {
			assertNotNull(display.getSystemColor(colorIds[i]));
		}
	} finally {
		display.dispose();
	}
}

public void test_getSystemFont() {
	Display display = new Display();
	try {
		Font font = display.getSystemFont();
		assertNotNull(font);
	} finally {
		display.dispose();
	}
}

public void test_getThread() {
	Display display = new Display();
	try {
		assertTrue(display.getThread() == Thread.currentThread());
	} finally {
		display.dispose();
	}
}

public void test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData() {
	// do not test internal API
}

public void test_internal_new_GCLorg_eclipse_swt_graphics_GCData() {
	// do not test internal API
}

public void test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlII() {
	Display display = new Display();
	try {
		Shell shell = new Shell(display, SWT.NO_TRIM);
		Button button1 = new Button(shell, SWT.PUSH);
		button1.setBounds(0,0,100,100);
		Button button2 = new Button(shell, SWT.PUSH);
		button2.setBounds(200,100,100,100);
		shell.setBounds(0,0,400,400);
		shell.open();
		
		Point shellOffset = shell.getLocation();
		Point result;
		
		result = display.map(button1, button2, 0, 0);
		assertEquals(new Point(-200,-100), result);
		result = display.map(button1, button2, -10, -20);
		assertEquals(new Point(-210,-120), result);
		result = display.map(button1, button2, 30, 40);
		assertEquals(new Point(-170,-60), result);
		
		result = display.map(button2, button1, 0, 0);
		assertEquals(new Point(200,100), result);
		result = display.map(button2, button1, -5, -15);
		assertEquals(new Point(195,85), result);
		result = display.map(button2, button1, 25, 35);
		assertEquals(new Point(225,135), result);
		
		result = display.map(null, button2, 0, 0);
		assertEquals(new Point(-200 - shellOffset.x,-100 - shellOffset.y), result);
		result = display.map(null, button2, -2, -4);
		assertEquals(new Point(-202 - shellOffset.x,-104 - shellOffset.y), result);
		result = display.map(null, button2, 6, 8);
		assertEquals(new Point(-194 - shellOffset.x,-92 - shellOffset.y), result);
		
		result = display.map(button2, null, 0, 0);
		assertEquals(new Point(shellOffset.x + 200,shellOffset.y + 100), result);
		result = display.map(button2, null, -3, -6);
		assertEquals(new Point(shellOffset.x + 197,shellOffset.y + 94), result);
		result = display.map(button2, null, 9, 12);
		assertEquals(new Point(shellOffset.x + 209,shellOffset.y + 112), result);
		
		button1.dispose();
		try {
			result = display.map(button1, button2, 0, 0);
			fail("No exception thrown for map from control being disposed");
		} catch (IllegalArgumentException e) {
			assertEquals("Incorrect exception thrown for map from control being disposed", SWT.ERROR_INVALID_ARGUMENT, e);
		}
		try {
			result = display.map(button2, button1, 0, 0);
			fail("No exception thrown for map to control being disposed");
		} catch (IllegalArgumentException e) {
			assertEquals("Incorrect exception thrown for map to control being disposed", SWT.ERROR_INVALID_ARGUMENT, e);
		}
		
		shell.dispose();
	} finally {
		display.dispose();
	}
}

public void test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlIIII() {
	Display display = new Display();
	try {
		Shell shell = new Shell(display, SWT.NO_TRIM);
		Button button1 = new Button(shell, SWT.PUSH);
		button1.setBounds(0,0,100,100);
		Button button2 = new Button(shell, SWT.PUSH);
		button2.setBounds(200,100,100,100);
		shell.setBounds(0,0,400,400);
		shell.open();
		
		Point shellOffset = shell.getLocation();
		Rectangle result;
		
		result = display.map(button1, button2, 0, 0, 100, 100);
		assertEquals(new Rectangle(-200,-100,100,100), result);
		result = display.map(button1, button2, -10, -20, 130, 140);
		assertEquals(new Rectangle(-210,-120,130,140), result);
		result = display.map(button1, button2, 50, 60, 170, 180);
		assertEquals(new Rectangle(-150,-40,170,180), result);
		
		result = display.map(button2, button1, 0, 0, 100, 100);
		assertEquals(new Rectangle(200,100,100,100), result);
		result = display.map(button2, button1, -5, -15, 125, 135);
		assertEquals(new Rectangle(195,85,125,135), result);
		result = display.map(button2, button1, 45, 55, 165, 175);
		assertEquals(new Rectangle(245,155,165,175), result);
		
		result = display.map(null, button2, 0, 0, 100, 100);
		assertEquals(new Rectangle(-200 - shellOffset.x,-100 - shellOffset.y,100,100), result);
		result = display.map(null, button2, -2, -4, 106, 108);
		assertEquals(new Rectangle(-202 - shellOffset.x,-104 - shellOffset.y,106,108), result);
		result = display.map(null, button2, 10, 12, 114, 116);
		assertEquals(new Rectangle(-190 - shellOffset.x,-88 - shellOffset.y,114,116), result);
		
		result = display.map(button2, null, 0, 0, 100, 100);
		assertEquals(new Rectangle(shellOffset.x + 200,shellOffset.y + 100,100,100), result);
		result = display.map(button2, null, -3, -6, 109, 112);
		assertEquals(new Rectangle(shellOffset.x + 197,shellOffset.y + 94,109,112), result);
		result = display.map(button2, null, 15, 18, 121, 124);
		assertEquals(new Rectangle(shellOffset.x + 215,shellOffset.y + 118,121,124), result);
		
		button1.dispose();
		try {
			result = display.map(button1, button2, 0, 0, 100, 100);
			fail("No exception thrown for map from control being disposed");
		} catch (IllegalArgumentException e) {
			assertEquals("Incorrect exception thrown for map from control being disposed", SWT.ERROR_INVALID_ARGUMENT, e);
		}
		try {
			result = display.map(button2, button1, 0, 0, 100, 100);
			fail("No exception thrown for map to control being disposed");
		} catch (IllegalArgumentException e) {
			assertEquals("Incorrect exception thrown for map to control being disposed", SWT.ERROR_INVALID_ARGUMENT, e);
		}
		
		shell.dispose();
	} finally {
		display.dispose();
	}
}

public void test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_graphics_Point() {
	Display display = new Display();
	try {
		Shell shell = new Shell(display, SWT.NO_TRIM);
		Button button1 = new Button(shell, SWT.PUSH);
		button1.setBounds(0,0,100,100);
		Button button2 = new Button(shell, SWT.PUSH);
		button2.setBounds(200,100,100,100);
		shell.setBounds(0,0,400,400);
		shell.open();
		
		Point result;
		Point point = new Point(0,0);
		Point shellOffset = shell.getLocation();

		
		result = display.map(button1, button2, point);
		assertEquals(new Point(-200,-100), result);
		result = display.map(button1, button2, new Point(-10,-20));
		assertEquals(new Point(-210,-120), result);
		result = display.map(button1, button2, new Point(30,40));
		assertEquals(new Point(-170,-60), result);
		
		result = display.map(button2, button1, point);
		assertEquals(new Point(200,100), result);
		result = display.map(button2, button1, new Point(-5,-15));
		assertEquals(new Point(195,85), result);
		result = display.map(button2, button1, new Point(25,35));
		assertEquals(new Point(225,135), result);
		
		result = display.map(null, button2, point);
		assertEquals(new Point(-200 - shellOffset.x,-100 - shellOffset.y), result);
		result = display.map(null, button2, new Point(-2,-4));
		assertEquals(new Point(-202 - shellOffset.x,-104 - shellOffset.y), result);
		result = display.map(null, button2, new Point(6,8));
		assertEquals(new Point(-194 - shellOffset.x,-92 - shellOffset.y), result);
		
		result = display.map(button2, null, point);
		assertEquals(new Point(shellOffset.x + 200,shellOffset.y + 100), result);
		result = display.map(button2, null, new Point(-3,-6));
		assertEquals(new Point(shellOffset.x + 197,shellOffset.y + 94), result);
		result = display.map(button2, null, new Point(9,12));
		assertEquals(new Point(shellOffset.x + 209,shellOffset.y + 112), result);
		
		button1.dispose();
		try {
			result = display.map(button1, button2, point);
			fail("No exception thrown for map from control being disposed");
		} catch (IllegalArgumentException e) {
			assertEquals("Incorrect exception thrown for map from control being disposed", SWT.ERROR_INVALID_ARGUMENT, e);
		}
		try {
			result = display.map(button2, button1, point);
			fail("No exception thrown for map to control being disposed");
		} catch (IllegalArgumentException e) {
			assertEquals("Incorrect exception thrown for map to control being disposed", SWT.ERROR_INVALID_ARGUMENT, e);
		}
		
		try {
			result = display.map(button2, button1, (Point) null);
			fail("No exception thrown for null point");
		} catch (IllegalArgumentException e) {
			assertEquals("Incorrect exception thrown for point being null", SWT.ERROR_NULL_ARGUMENT, e);
		}
		
		shell.dispose();
	} finally {
		display.dispose();
	}
}

public void test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_graphics_Rectangle() {
	Display display = new Display();
	try {
		Shell shell = new Shell(display, SWT.NO_TRIM);
		Button button1 = new Button(shell, SWT.PUSH);
		button1.setBounds(0,0,100,100);
		Button button2 = new Button(shell, SWT.PUSH);
		button2.setBounds(200,100,100,100);
		shell.setBounds(0,0,400,400);
		shell.open();
		
		Rectangle result;
		Rectangle rect = new Rectangle(0,0,100,100);
		Point shellOffset = shell.getLocation();
		
		result = display.map(button1, button2, rect);
		assertEquals(new Rectangle(-200,-100,100,100), result);
		result = display.map(button1, button2, new Rectangle(-10, -20, 130, 140));
		assertEquals(new Rectangle(-210,-120,130,140), result);
		result = display.map(button1, button2, new Rectangle(50, 60, 170, 180));
		assertEquals(new Rectangle(-150,-40,170,180), result);
		
		result = display.map(button2, button1, rect);
		assertEquals(new Rectangle(200,100,100,100), result);
		result = display.map(button2, button1, new Rectangle(-5, -15, 125, 135));
		assertEquals(new Rectangle(195,85,125,135), result);
		result = display.map(button2, button1, new Rectangle(45, 55, 165, 175));
		assertEquals(new Rectangle(245,155,165,175), result);
		
		result = display.map(null, button2, rect);
		assertEquals(new Rectangle(-200 - shellOffset.x,-100 - shellOffset.y,100,100), result);
		result = display.map(null, button2, new Rectangle(-2, -4, 106, 108));
		assertEquals(new Rectangle(-202 - shellOffset.x,-104 - shellOffset.y,106,108), result);
		result = display.map(null, button2, new Rectangle(10, 12, 114, 116));
		assertEquals(new Rectangle(-190 - shellOffset.x,-88 - shellOffset.y,114,116), result);
		
		result = display.map(button2, null, rect);
		assertEquals(new Rectangle(shellOffset.x + 200,shellOffset.y + 100,100,100), result);
		result = display.map(button2, null, new Rectangle(-3, -6, 109, 112));
		assertEquals(new Rectangle(shellOffset.x + 197,shellOffset.y + 94,109,112), result);
		result = display.map(button2, null, new Rectangle(15, 18, 121, 124));
		assertEquals(new Rectangle(shellOffset.x + 215,shellOffset.y + 118,121,124), result);
		
	
		button1.dispose();
		try {
			result = display.map(button1, button2, rect);
			fail("No exception thrown for map from control being disposed");
		} catch (IllegalArgumentException e) {
			assertEquals("Incorrect exception thrown for map from control being disposed", SWT.ERROR_INVALID_ARGUMENT, e);
		}
		try {
			result = display.map(button2, button1, rect);
			fail("No exception thrown for map to control being disposed");
		} catch (IllegalArgumentException e) {
			assertEquals("Incorrect exception thrown for map to control being disposed", SWT.ERROR_INVALID_ARGUMENT, e);
		}
		
		try {
			result = display.map(button2, button1, (Rectangle) null);
			fail("No exception thrown for null point");
		} catch (IllegalArgumentException e) {
			assertEquals("Incorrect exception thrown for rectangle being null", SWT.ERROR_NULL_ARGUMENT, e);
		}
		
		shell.dispose();
	} finally {
		display.dispose();
	}
}

public void test_postLorg_eclipse_swt_widgets_Event() {
	final int KEYCODE = SWT.SHIFT;
	
	Display display = new Display();
	try {
		try {
			display.post(null);
			fail("No exception thrown for post with null argument");
		} catch (IllegalArgumentException e) {
			assertEquals("Incorrect exception thrown for post with null argument", SWT.ERROR_NULL_ARGUMENT, e);
		}
		
		Shell shell = new Shell(display, SWT.NO_TRIM);
		shell.setBounds(display.getBounds());
		shell.open();

		Event event;
		
		// Test key events (down/up)
		event = new Event();
//		event.type = SWT.KeyDown;
//		event.keyCode = -1;  				// bogus key code
//		assertTrue(display.post(event));	// uses default 0 character
//		// don't test KeyDown/KeyUp with a character to avoid sending to 
//		// random window if test shell looses focus
//		
//		event = new Event();
//		event.type = SWT.KeyUp;
//		assertTrue(display.post(event));

		event.type = SWT.KeyDown;
		event.keyCode = KEYCODE;
		shell.setFocus();
		assertTrue(display.post(event));
		event.type = SWT.KeyUp;
		shell.setFocus();
		assertTrue(display.post(event));
		
		// Test mouse events (down/up/move)
		event = new Event();
		event.type = SWT.MouseMove;
		Rectangle bounds = shell.getBounds();
		event.x = bounds.x + bounds.width/2;
		event.y = bounds.y + bounds.height/2;
		shell.setFocus();
		assertTrue(display.post(event));
		
		event = new Event();
		event.type = SWT.MouseDown;
		assertFalse(display.post(event));  // missing button
		event.button = 1;
		shell.setFocus();
		assertTrue(display.post(event));
		
		event = new Event();
		event.type = SWT.MouseUp;
		assertFalse(display.post(event));  // missing button
		event.button = 1;
		shell.setFocus();
		assertTrue(display.post(event));
		
		// Test unsupported event
		event = new Event();
		event.type = SWT.MouseDoubleClick;
		assertFalse(display.post(event));
		
		shell.dispose();
		
		// can't verify that the events were actually sent to a listener.
		// the test shell won't receive any events if it has lost focus, 
		// e.g., due to user intervention or another process popping up 
		// a window. 
	} finally {
		display.dispose();
	}
}

public void test_readAndDispatch() {
	// The following tests rely on readAndDispatch in order to succeed,
	// thus no test is needed here.
	//    test_getSyncThread()
	//    test_postLorg_eclipse_swt_widgets_Event()
}

public void test_removeFilterILorg_eclipse_swt_widgets_Listener() {
	final int CLOSE_CALLBACK = 0;
	final int DISPOSE_CALLBACK = 1;
	final boolean[] callbackReceived = new boolean[] {false, false};
	
	Listener listener = new Listener() {
		public void handleEvent(Event e) {
			if (e.type == SWT.Close)
				callbackReceived[CLOSE_CALLBACK] = true;
			else if (e.type == SWT.Dispose)
				callbackReceived[DISPOSE_CALLBACK] = true;
		}
	};
	
	Display display = new Display();
	try {
		try {
			display.removeFilter(SWT.Dispose, null);
			fail("No exception thrown for removeFilter with null argument");
		} catch (IllegalArgumentException e) {
			assertEquals("Incorrect exception thrown for removeFilter with null argument", SWT.ERROR_NULL_ARGUMENT, e);
		}
		
		display.addFilter(SWT.Close, listener);
		display.removeFilter(SWT.Close, listener);
	} finally {
		display.close();
	}
	assertFalse(callbackReceived[CLOSE_CALLBACK]);
	assertFalse(callbackReceived[DISPOSE_CALLBACK]);
}

public void test_removeListenerILorg_eclipse_swt_widgets_Listener() {
	final int CLOSE_CALLBACK = 0;
	final int DISPOSE_CALLBACK = 1;
	final boolean[] callbackReceived = new boolean[] {false, false};
	
	Listener listener = new Listener() {
		public void handleEvent(Event e) {
			if (e.type == SWT.Close)
				callbackReceived[CLOSE_CALLBACK] = true;
			else if (e.type == SWT.Dispose)
				callbackReceived[DISPOSE_CALLBACK] = true;
		}
	};
	
	Display display = new Display();
	try {
		try {
			display.removeListener(SWT.Close, null);
			fail("No exception thrown for removeListener with null argument");
		} catch (IllegalArgumentException e) {
			assertEquals("Incorrect exception thrown for removeListener with null argument", SWT.ERROR_NULL_ARGUMENT, e);
		}
		
		display.addListener(SWT.Dispose, listener);
		display.removeListener(SWT.Dispose, listener);
	} finally {
		display.close();
	}
	assertFalse(callbackReceived[CLOSE_CALLBACK]);
	assertFalse(callbackReceived[DISPOSE_CALLBACK]);
}

public void test_release() {
	// Overriding test_release from Device.
}

public void test_setAppNameLjava_lang_String() {
	Display.setAppName("My Application Name");
}

public void test_setCursorLocationII() {
	Display display = new Display();
	try {
		display.setCursorLocation(0,0);
	} finally {
		display.dispose();
	}
}

public void test_setCursorLocationLorg_eclipse_swt_graphics_Point() {
	Display display = new Display();
	try {
		display.setCursorLocation(new Point(0,0));
		try {
			display.setCursorLocation(null);
			fail("No exception thrown for null argument");
		}
		catch (IllegalArgumentException e) {
			assertEquals("Incorrect exception thrown for setCursorLocation with null argument", SWT.ERROR_NULL_ARGUMENT, e);
		}
	} finally {
		display.dispose();
	}
}

public void test_setDataLjava_lang_Object() {
	Display display = new Display();
	try {
		display.setData(new Integer(10));
		Integer i = (Integer)display.getData();
		assertNotNull(i);
		assertTrue(i.equals(new Integer(10)));
	} finally {
		display.dispose();
	}
}

public void test_setDataLjava_lang_StringLjava_lang_Object() {
	Display display = new Display();
	try {
		display.setData("Integer", new Integer(10));
		display.setData("String", "xyz");
		Integer i = (Integer)display.getData("Integer");
		assertNotNull(i);
		assertTrue(i.equals(new Integer(10)));
		String s = (String)display.getData("String");
		assertNotNull(s);
		assertTrue(s.equals("xyz"));
	} finally {
		display.dispose();
	}
}

public void test_setSynchronizerLorg_eclipse_swt_widgets_Synchronizer() {
	final Display display = new Display();
	final boolean[] asyncExecRan = new boolean[] {false};
	
	try {
		try {
			display.setSynchronizer(null);
			fail("No exception thrown for post with null argument");
		} catch (IllegalArgumentException e) {
			assertEquals("Incorrect exception thrown for set synchronizer with null argument", SWT.ERROR_NULL_ARGUMENT, e);
		}
		
		class MySynchronizer extends Synchronizer {
			boolean invoked = false;
			MySynchronizer(Display d) {
				super(d);
			}
			protected void asyncExec (Runnable runnable) {
				invoked = true;
				super.asyncExec(runnable);
			}
		}
		
		MySynchronizer mySynchronizer = new MySynchronizer(display);
		display.setSynchronizer(mySynchronizer);
		display.asyncExec(new Runnable() {
			public void run() {
				asyncExecRan[0] = true;
			}
		});
		while (display.readAndDispatch()) {}
		assertTrue(mySynchronizer.invoked);
		assertTrue(asyncExecRan[0]);
	} finally {
		display.dispose();
	}
}

public void test_sleep() {
	final Display display = new Display();
	try {
		Thread thread;
		boolean eventQueued;
		
		// Ensure event queue is empty, otherwise sleep() will just return.
		while(display.readAndDispatch()) {}
		thread = new Thread() {
			public void run() {
				try {
					// Delay to ensure the UI thread has been put to sleep.
					sleep(3000);
				} catch (InterruptedException ex) {
				}
				// Use wake() to revive from sleep().
				display.wake();
			}
		};
		thread.start();
		// Note that sleep seems to always return true, at least
		// on Windows, since wake() uses a null event. 
		eventQueued = display.sleep();
		
		// Ensure event queue is empty, otherwise sleep() will just return.
		while(display.readAndDispatch()) {}
		thread = new Thread() {
			public void run() {
				try {
					// Delay to ensure the UI thread has been put to sleep.
					sleep(3000);
				} catch (InterruptedException ex) {
				}
				// Cause OS to generate an event to revive from sleep().
				display.syncExec(new Runnable() {
					public void run() {
						Shell s = new Shell(display);
						s.open();
						s.dispose();
					}
				});
			}
		};
		thread.start();
		eventQueued = display.sleep();
		assertTrue(eventQueued);
	} finally {
		display.dispose();
	}
}

public void test_syncExecLjava_lang_Runnable() {
	final Display display = new Display();
	try {
		display.syncExec(new Runnable() {
			public void run() {
				display.beep();
			}
		});
	} finally {
		display.dispose();
	}
}

public void test_timerExecILjava_lang_Runnable() {
	final Display display = new Display();
	try {
		final boolean[] timerExecRan = new boolean[] {false};
		final boolean[] threadRan = new boolean[] {false};
		
		try {
			display.timerExec(0, null);
			fail("No exception thrown for timerExec with null runnable");
		} catch (IllegalArgumentException e) {
			assertEquals("Incorrect exception thrown for timerExec with null runnable", SWT.ERROR_NULL_ARGUMENT, e);
		}
		
		display.timerExec(-100, new Runnable() {
			public void run() {
				timerExecRan[0] = true;
			}
		});
				
		final int delay = 3000;
		final long startTime = System.currentTimeMillis();
		display.timerExec(delay, new Runnable() {
			public void run() {
				long endTime = System.currentTimeMillis();
				// debug intermittent test failure
				if (endTime < (startTime + delay)) {
					System.out.println("Display.timerExec ran early " + ((startTime + delay) - endTime));
				}
				//assertTrue("Timer ran early! ms early: ", endTime >= (startTime + delay));
				threadRan[0] = true;
			}
		});
		while (!threadRan[0]) {
			// The read and dispatch loop must be running in order
			// for the runnable in the timer exec to be executed.
			if (!display.readAndDispatch ()) display.sleep();
		}
		
		// Verify the timerExec with less than zero milliseconds didn't execute.
		assertFalse("< 0 ms timer did execute", timerExecRan[0]);
	} finally {
		display.dispose();
	}
}

public void test_update() {
	Display display = new Display();
	try {
		display.update();
	} finally {
		display.dispose();
	}
}

public void test_wake() {
	// tested in sleep() method
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_Display((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_DeviceData");
	methodNames.addElement("test_addFilterILorg_eclipse_swt_widgets_Listener");
	methodNames.addElement("test_addListenerILorg_eclipse_swt_widgets_Listener");
	methodNames.addElement("test_asyncExecLjava_lang_Runnable");
	methodNames.addElement("test_beep");
	methodNames.addElement("test_close");
	methodNames.addElement("test_disposeExecLjava_lang_Runnable");
	methodNames.addElement("test_findDisplayLjava_lang_Thread");
	methodNames.addElement("test_findWidgetI");
	methodNames.addElement("test_getActiveShell");
	methodNames.addElement("test_getBounds");
	methodNames.addElement("test_getClientArea");
	methodNames.addElement("test_getCurrent");
	methodNames.addElement("test_getCursorControl");
	methodNames.addElement("test_getCursorLocation");
	methodNames.addElement("test_getCursorSize");
	methodNames.addElement("test_getData");
	methodNames.addElement("test_getDataLjava_lang_String");
	methodNames.addElement("test_getDefault");
	methodNames.addElement("test_getDismissalAlignment");
	methodNames.addElement("test_getDoubleClickTime");
	methodNames.addElement("test_getFocusControl");
	methodNames.addElement("test_getIconDepth");
	methodNames.addElement("test_getMonitors");
	methodNames.addElement("test_getPrimaryMonitor");
	methodNames.addElement("test_getShells");
	methodNames.addElement("test_getSyncThread");
	methodNames.addElement("test_getSystemColorI");
	methodNames.addElement("test_getSystemFont");
	methodNames.addElement("test_getThread");
	methodNames.addElement("test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData");
	methodNames.addElement("test_internal_new_GCLorg_eclipse_swt_graphics_GCData");
	methodNames.addElement("test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlII");
	methodNames.addElement("test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlIIII");
	methodNames.addElement("test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_graphics_Point");
	methodNames.addElement("test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_graphics_Rectangle");
	methodNames.addElement("test_postLorg_eclipse_swt_widgets_Event");
	methodNames.addElement("test_readAndDispatch");
	methodNames.addElement("test_removeFilterILorg_eclipse_swt_widgets_Listener");
	methodNames.addElement("test_removeListenerILorg_eclipse_swt_widgets_Listener");
	methodNames.addElement("test_setAppNameLjava_lang_String");
	methodNames.addElement("test_setCursorLocationII");
	methodNames.addElement("test_setCursorLocationLorg_eclipse_swt_graphics_Point");
	methodNames.addElement("test_setDataLjava_lang_Object");
	methodNames.addElement("test_setDataLjava_lang_StringLjava_lang_Object");
	methodNames.addElement("test_setSynchronizerLorg_eclipse_swt_widgets_Synchronizer");
	methodNames.addElement("test_sleep");
	methodNames.addElement("test_syncExecLjava_lang_Runnable");
	methodNames.addElement("test_timerExecILjava_lang_Runnable");
	methodNames.addElement("test_update");
	methodNames.addElement("test_wake");
	methodNames.addAll(Test_org_eclipse_swt_graphics_Device.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_DeviceData")) test_ConstructorLorg_eclipse_swt_graphics_DeviceData();
	else if (getName().equals("test_addFilterILorg_eclipse_swt_widgets_Listener")) test_addFilterILorg_eclipse_swt_widgets_Listener();
	else if (getName().equals("test_addListenerILorg_eclipse_swt_widgets_Listener")) test_addListenerILorg_eclipse_swt_widgets_Listener();
	else if (getName().equals("test_asyncExecLjava_lang_Runnable")) test_asyncExecLjava_lang_Runnable();
	else if (getName().equals("test_beep")) test_beep();
	else if (getName().equals("test_close")) test_close();
	else if (getName().equals("test_disposeExecLjava_lang_Runnable")) test_disposeExecLjava_lang_Runnable();
	else if (getName().equals("test_findDisplayLjava_lang_Thread")) test_findDisplayLjava_lang_Thread();
	else if (getName().equals("test_findWidgetI")) test_findWidgetI();
	else if (getName().equals("test_getActiveShell")) test_getActiveShell();
	else if (getName().equals("test_getBounds")) test_getBounds();
	else if (getName().equals("test_getClientArea")) test_getClientArea();
	else if (getName().equals("test_getCurrent")) test_getCurrent();
	else if (getName().equals("test_getCursorControl")) test_getCursorControl();
	else if (getName().equals("test_getCursorLocation")) test_getCursorLocation();
	else if (getName().equals("test_getCursorSize")) test_getCursorSize();
	else if (getName().equals("test_getData")) test_getData();
	else if (getName().equals("test_getDataLjava_lang_String")) test_getDataLjava_lang_String();
	else if (getName().equals("test_getDefault")) test_getDefault();
	else if (getName().equals("test_getDismissalAlignment")) test_getDismissalAlignment();
	else if (getName().equals("test_getDoubleClickTime")) test_getDoubleClickTime();
	else if (getName().equals("test_getFocusControl")) test_getFocusControl();
	else if (getName().equals("test_getIconDepth")) test_getIconDepth();
	else if (getName().equals("test_getMonitors")) test_getMonitors();
	else if (getName().equals("test_getPrimaryMonitor")) test_getPrimaryMonitor();
	else if (getName().equals("test_getShells")) test_getShells();
	else if (getName().equals("test_getSyncThread")) test_getSyncThread();
	else if (getName().equals("test_getSystemColorI")) test_getSystemColorI();
	else if (getName().equals("test_getSystemFont")) test_getSystemFont();
	else if (getName().equals("test_getThread")) test_getThread();
	else if (getName().equals("test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData")) test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData();
	else if (getName().equals("test_internal_new_GCLorg_eclipse_swt_graphics_GCData")) test_internal_new_GCLorg_eclipse_swt_graphics_GCData();
	else if (getName().equals("test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlII")) test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlII();
	else if (getName().equals("test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlIIII")) test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlIIII();
	else if (getName().equals("test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_graphics_Point")) test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_graphics_Point();
	else if (getName().equals("test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_graphics_Rectangle")) test_mapLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_widgets_ControlLorg_eclipse_swt_graphics_Rectangle();
	else if (getName().equals("test_postLorg_eclipse_swt_widgets_Event")) test_postLorg_eclipse_swt_widgets_Event();
	else if (getName().equals("test_readAndDispatch")) test_readAndDispatch();
	else if (getName().equals("test_removeFilterILorg_eclipse_swt_widgets_Listener")) test_removeFilterILorg_eclipse_swt_widgets_Listener();
	else if (getName().equals("test_removeListenerILorg_eclipse_swt_widgets_Listener")) test_removeListenerILorg_eclipse_swt_widgets_Listener();
	else if (getName().equals("test_setAppNameLjava_lang_String")) test_setAppNameLjava_lang_String();
	else if (getName().equals("test_setCursorLocationII")) test_setCursorLocationII();
	else if (getName().equals("test_setCursorLocationLorg_eclipse_swt_graphics_Point")) test_setCursorLocationLorg_eclipse_swt_graphics_Point();
	else if (getName().equals("test_setDataLjava_lang_Object")) test_setDataLjava_lang_Object();
	else if (getName().equals("test_setDataLjava_lang_StringLjava_lang_Object")) test_setDataLjava_lang_StringLjava_lang_Object();
	else if (getName().equals("test_setSynchronizerLorg_eclipse_swt_widgets_Synchronizer")) test_setSynchronizerLorg_eclipse_swt_widgets_Synchronizer();
	else if (getName().equals("test_sleep")) test_sleep();
	else if (getName().equals("test_syncExecLjava_lang_Runnable")) test_syncExecLjava_lang_Runnable();
	else if (getName().equals("test_timerExecILjava_lang_Runnable")) test_timerExecILjava_lang_Runnable();
	else if (getName().equals("test_update")) test_update();
	else if (getName().equals("test_wake")) test_wake();
	else super.runTest();
}

/* custom */
boolean disposeExecRan;

public void test_dispose() {
	// tested in virtually every method and in particular
	//   test_disposeExecLjava_lang_Runnable
}

/* Overloaded tests from Test_org_eclipse_swt_graphics_Device */
public void test_getDPI() {
	Display display = new Display();
	try {
		Point p = display.getDPI();
		assertTrue("horizontal DPI not greater than zero", p.x > 0);
		assertTrue("vertical DPI not greater than zero", p.y > 0);
	} finally {
		display.dispose();
	}
}

public void test_getDepth() {
	Display display = new Display();
	try {
		int d = display.getDepth();
		assertTrue("depth not greater than zero", d > 0);
	} finally {
		display.dispose();
	}
}

public void test_getFontListLjava_lang_StringZ() {
	Display display = new Display();
	try {
		FontData[] scalable = display.getFontList(null, true);
		FontData[] non_scalable = display.getFontList(null, false);
		assertTrue("no fonts detected", (scalable.length + non_scalable.length) > 0);
	} finally {
		display.dispose();
	}
}

public void test_getWarnings() {
	Display display = new Display();
	try {
		display.getWarnings();
		// Since the behavior is platform specific, there's
		// no good test for the result value.
	} finally {
		display.dispose();
	}
}

public void test_isDisposed() {
	Display disp = new Display();
	assertFalse(disp.isDisposed());
	disp.dispose();
	assertTrue(disp.isDisposed());
}

public void test_setWarningsZ() {
	Display display = new Display();
	try {
		display.setWarnings(true);
		display.setWarnings(false);
		// Since the behavior is platform specific, there's
		// no good test for invoking this method.
		// Note that on Windows, the implementation is a no-op.
	} finally {
		display.dispose();
	}
}
}

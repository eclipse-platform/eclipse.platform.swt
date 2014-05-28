/*******************************************************************************
 * Copyright (c) 2000, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.eclipse.swt.tests.junit.SwtTestUtil.assertSWTProblem;
import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.DeviceData;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Synchronizer;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Display
 *
 * @see org.eclipse.swt.widgets.Display
 */
public class Test_org_eclipse_swt_widgets_Display extends TestCase {

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
			assertSWTProblem("Incorrect exception thrown for addFilter with null argument", SWT.ERROR_NULL_ARGUMENT, e);
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
			assertSWTProblem("Incorrect exception thrown for addListener with null argument", SWT.ERROR_NULL_ARGUMENT, e);
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

public void test_asyncExecLjava_lang_Runnable_dispose() {
	final Display display = new Display();
	try {
		disposeExecRan = false;
		display.asyncExec(new Runnable() {
			public void run() {
				display.dispose();
				disposeExecRan = true;
			}
		});
	} finally {
		while (!disposeExecRan) {
			if (!display.readAndDispatch()) display.sleep ();
		}
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

public void test_getActiveShell() {
	if (SwtTestUtil.isGTK) {
		//TODO Fix GTK failure.
		if (SwtTestUtil.verbose) {
			System.out.println("Excluded test_getActiveShell(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Display)");
		}
		return;
	}
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

		Shell shellMapTest1 = new Shell(display,SWT.NO_TRIM);
		Button buttonTest1 = new Button(shellMapTest1, SWT.PUSH);
		buttonTest1.setBounds(0,0,100,100);
		shellMapTest1.setBounds(200, 200, 400, 400);
		shellMapTest1.open();
		Shell overlayShell1 = new Shell(shellMapTest1, SWT.NO_TRIM); //subshell
		overlayShell1.setBounds(shellMapTest1.getBounds());
		Rectangle r = buttonTest1.getBounds();
		result = display.map(buttonTest1, null, new Point(r.x, r.y));
		result = display.map(null, overlayShell1, result);
		assertEquals(new Point(r.x, r.y), result);

		Shell shellMapTest2 = new Shell(display,SWT.NO_TRIM);
		Button buttonTest2 = new Button(shellMapTest2, SWT.PUSH);
		buttonTest2.setBounds(0,0,100,100);
		shellMapTest2.setBounds(200, 200, 400, 400);
		shellMapTest2.open();
		Shell overlayShell2 = new Shell(display, SWT.NO_TRIM); //nativeshell
		overlayShell2.setBounds(shellMapTest2.getBounds());
		r = buttonTest2.getBounds();
		result = display.map(buttonTest2, null, new Point(r.x, r.y));
		result = display.map(null, overlayShell2, result.x, result.y);
		assertEquals(new Point(r.x, r.y), result);

		Shell shellMapTestRtoL1 = new Shell(display,SWT.NO_TRIM);
		shellMapTestRtoL1.setOrientation(SWT.RIGHT_TO_LEFT);
		Button buttonTestRtoL1 = new Button(shellMapTestRtoL1, SWT.PUSH);
		buttonTestRtoL1.setBounds(0,0,100,100);
		shellMapTestRtoL1.setBounds(200, 200, 400, 400);
		shellMapTestRtoL1.open();
		Shell overlayShellRtoL1 = new Shell(shellMapTestRtoL1, SWT.NO_TRIM); //subshell
		overlayShellRtoL1.setOrientation(SWT.RIGHT_TO_LEFT);
		overlayShellRtoL1.setBounds(shellMapTestRtoL1.getBounds());
		r = buttonTestRtoL1.getBounds();
		result = display.map(buttonTestRtoL1, null, new Point(r.x, r.y));
		result = display.map(null, overlayShellRtoL1, result);
		assertEquals(new Point(r.x, r.y), result);

		Shell shellMapTestRtoL2 = new Shell(display,SWT.NO_TRIM);
		shellMapTestRtoL2.setOrientation(SWT.RIGHT_TO_LEFT);
		Button buttonTestRtoL2 = new Button(shellMapTestRtoL2, SWT.PUSH);
		buttonTestRtoL2.setBounds(0,0,100,100);
		shellMapTestRtoL2.setBounds(200, 200, 400, 400);
		shellMapTestRtoL2.open();
		Shell overlayShellRtoL2 = new Shell(display, SWT.NO_TRIM); //nativeshell
		overlayShellRtoL2.setOrientation(SWT.RIGHT_TO_LEFT);
		overlayShellRtoL2.setBounds(shellMapTestRtoL2.getBounds());
		r = buttonTestRtoL2.getBounds();
		result = display.map(buttonTestRtoL2, null, new Point(r.x, r.y));
		result = display.map(null, overlayShellRtoL2, result.x, result.y);
		assertEquals(new Point(r.x, r.y), result);

		button1.dispose();
		buttonTest1.dispose();
		buttonTest2.dispose();
		shellMapTest1.dispose();
		shellMapTest2.dispose();
		overlayShell1.dispose();
		overlayShell2.dispose();

		buttonTestRtoL1.dispose();
		buttonTestRtoL2.dispose();
		shellMapTestRtoL1.dispose();
		shellMapTestRtoL2.dispose();
		overlayShellRtoL1.dispose();
		overlayShellRtoL2.dispose();

		try {
			result = display.map(button1, button2, 0, 0);
			fail("No exception thrown for map from control being disposed");
		} catch (IllegalArgumentException e) {
			assertSWTProblem("Incorrect exception thrown for map from control being disposed", SWT.ERROR_INVALID_ARGUMENT, e);
		}
		try {
			result = display.map(button2, button1, 0, 0);
			fail("No exception thrown for map to control being disposed");
		} catch (IllegalArgumentException e) {
			assertSWTProblem("Incorrect exception thrown for map to control being disposed", SWT.ERROR_INVALID_ARGUMENT, e);
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

		Shell shellMapTest1 = new Shell(display,SWT.NO_TRIM);
		Button buttonTest1 = new Button(shellMapTest1, SWT.PUSH);
		buttonTest1.setBounds(0,0,100,100);
		shellMapTest1.setBounds(200, 200, 400, 400);
		shellMapTest1.open();
		Shell overlayShell1 = new Shell(shellMapTest1, SWT.NO_TRIM); //subshell
		overlayShell1.setBounds(shellMapTest1.getBounds());
		Rectangle r = buttonTest1.getBounds();
		result = display.map(buttonTest1, null, r);
		result = display.map(null, overlayShell1, result);
		assertEquals(r, result);

		Shell shellMapTest2 = new Shell(display,SWT.NO_TRIM);
		Button buttonTest2 = new Button(shellMapTest2, SWT.PUSH);
		buttonTest2.setBounds(0,0,100,100);
		shellMapTest2.setBounds(200, 200, 400, 400);
		shellMapTest2.open();
		Shell overlayShell2 = new Shell(display, SWT.NO_TRIM); //root shell
		overlayShell2.setBounds(shellMapTest2.getBounds());
		r = buttonTest2.getBounds();
		result = display.map(buttonTest2, null, r);
		result = display.map(null, overlayShell2, result);
		assertEquals(r, result);

		Shell shellMapTestRtoL1 = new Shell(display,SWT.NO_TRIM);
		shellMapTestRtoL1.setOrientation(SWT.RIGHT_TO_LEFT);
		Button buttonTestRtoL1 = new Button(shellMapTestRtoL1, SWT.PUSH);
		buttonTestRtoL1.setBounds(0,0,100,100);
		shellMapTestRtoL1.setBounds(200, 200, 400, 400);
		shellMapTestRtoL1.open();
		Shell overlayShellRtoL1 = new Shell(shellMapTestRtoL1, SWT.NO_TRIM); //subshell
		overlayShellRtoL1.setOrientation(SWT.RIGHT_TO_LEFT);
		overlayShellRtoL1.setBounds(shellMapTestRtoL1.getBounds());
		r = buttonTest1.getBounds();
		result = display.map(buttonTestRtoL1, null, r);
		result = display.map(null, overlayShellRtoL1, result);
		assertEquals(r, result);

		Shell shellMapTestRtoL2 = new Shell(display,SWT.NO_TRIM);
		shellMapTestRtoL2.setOrientation(SWT.RIGHT_TO_LEFT);
		Button buttonTestRtoL2 = new Button(shellMapTestRtoL2, SWT.PUSH);
		buttonTestRtoL2.setBounds(0,0,100,100);
		shellMapTestRtoL2.setBounds(200, 200, 400, 400);
		shellMapTestRtoL2.open();
		Shell overlayShellRtoL2 = new Shell(display, SWT.NO_TRIM); //root shell
		overlayShellRtoL2.setOrientation(SWT.RIGHT_TO_LEFT);
		overlayShellRtoL2.setBounds(shellMapTestRtoL2.getBounds());
		r = buttonTestRtoL2.getBounds();
		result = display.map(buttonTestRtoL2, null, r);
		result = display.map(null, overlayShellRtoL2, result);
		assertEquals(r, result);

		button1.dispose();
		buttonTest1.dispose();
		buttonTest2.dispose();
		shellMapTest1.dispose();
		shellMapTest2.dispose();
		overlayShell1.dispose();
		overlayShell2.dispose();

		buttonTestRtoL1.dispose();
		buttonTestRtoL2.dispose();
		shellMapTestRtoL1.dispose();
		shellMapTestRtoL2.dispose();
		overlayShellRtoL1.dispose();
		overlayShellRtoL2.dispose();

		try {
			result = display.map(button1, button2, 0, 0, 100, 100);
			fail("No exception thrown for map from control being disposed");
		} catch (IllegalArgumentException e) {
			assertSWTProblem("Incorrect exception thrown for map from control being disposed", SWT.ERROR_INVALID_ARGUMENT, e);
		}
		try {
			result = display.map(button2, button1, 0, 0, 100, 100);
			fail("No exception thrown for map to control being disposed");
		} catch (IllegalArgumentException e) {
			assertSWTProblem("Incorrect exception thrown for map to control being disposed", SWT.ERROR_INVALID_ARGUMENT, e);
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

		Shell shellMapTest1 = new Shell(display,SWT.NO_TRIM);
		Button buttonTest1 = new Button(shellMapTest1, SWT.PUSH);
		buttonTest1.setBounds(0,0,100,100);
		shellMapTest1.setBounds(200, 200, 400, 400);
		shellMapTest1.open();
		Shell overlayShell1 = new Shell(shellMapTest1, SWT.NO_TRIM); //subshell
		overlayShell1.setBounds(shellMapTest1.getBounds());
		Rectangle r = buttonTest1.getBounds();
		result = display.map(buttonTest1, null, new Point(r.x, r.y));
		result = display.map(null, overlayShell1, result);
		assertEquals(new Point(r.x, r.y), result);

		Shell shellMapTest2 = new Shell(display,SWT.NO_TRIM);
		Button buttonTest2 = new Button(shellMapTest2, SWT.PUSH);
		buttonTest2.setBounds(0,0,100,100);
		shellMapTest2.setBounds(200, 200, 400, 400);
		shellMapTest2.open();
		Shell overlayShell2 = new Shell(display, SWT.NO_TRIM); //nativeshell
		overlayShell2.setBounds(shellMapTest2.getBounds());
		r = buttonTest2.getBounds();
		result = display.map(buttonTest2, null, new Point(r.x, r.y));
		result = display.map(null, overlayShell2, result.x, result.y);
		assertEquals(new Point(r.x, r.y), result);

		Shell shellMapTestRtoL1 = new Shell(display,SWT.NO_TRIM);
		shellMapTestRtoL1.setOrientation(SWT.RIGHT_TO_LEFT);
		Button buttonTestRtoL1 = new Button(shellMapTestRtoL1, SWT.PUSH);
		buttonTestRtoL1.setBounds(0,0,100,100);
		shellMapTestRtoL1.setBounds(200, 200, 400, 400);
		shellMapTestRtoL1.open();
		Shell overlayShellRtoL1 = new Shell(shellMapTestRtoL1, SWT.NO_TRIM); //subshell
		overlayShellRtoL1.setOrientation(SWT.RIGHT_TO_LEFT);
		overlayShellRtoL1.setBounds(shellMapTestRtoL1.getBounds());
		r = buttonTestRtoL1.getBounds();
		result = display.map(buttonTestRtoL1, null, new Point(r.x, r.y));
		result = display.map(null, overlayShellRtoL1, result);
		assertEquals(new Point(r.x, r.y), result);

		Shell shellMapTestRtoL2 = new Shell(display,SWT.NO_TRIM);
		shellMapTestRtoL2.setOrientation(SWT.RIGHT_TO_LEFT);
		Button buttonTestRtoL2 = new Button(shellMapTestRtoL2, SWT.PUSH);
		buttonTestRtoL2.setBounds(0,0,100,100);
		shellMapTestRtoL2.setBounds(200, 200, 400, 400);
		shellMapTestRtoL2.open();
		Shell overlayShellRtoL2 = new Shell(display, SWT.NO_TRIM); //nativeshell
		overlayShellRtoL2.setOrientation(SWT.RIGHT_TO_LEFT);
		overlayShellRtoL2.setBounds(shellMapTestRtoL2.getBounds());
		r = buttonTestRtoL2.getBounds();
		result = display.map(buttonTestRtoL2, null, new Point(r.x, r.y));
		result = display.map(null, overlayShellRtoL2, result.x, result.y);
		assertEquals(new Point(r.x, r.y), result);

		button1.dispose();
		buttonTest1.dispose();
		buttonTest2.dispose();
		shellMapTest1.dispose();
		shellMapTest2.dispose();
		overlayShell1.dispose();
		overlayShell2.dispose();

		buttonTestRtoL1.dispose();
		buttonTestRtoL2.dispose();
		shellMapTestRtoL1.dispose();
		shellMapTestRtoL2.dispose();
		overlayShellRtoL1.dispose();
		overlayShellRtoL2.dispose();

		try {
			result = display.map(button1, button2, point);
			fail("No exception thrown for map from control being disposed");
		} catch (IllegalArgumentException e) {
			assertSWTProblem("Incorrect exception thrown for map from control being disposed", SWT.ERROR_INVALID_ARGUMENT, e);
		}
		try {
			result = display.map(button2, button1, point);
			fail("No exception thrown for map to control being disposed");
		} catch (IllegalArgumentException e) {
			assertSWTProblem("Incorrect exception thrown for map to control being disposed", SWT.ERROR_INVALID_ARGUMENT, e);
		}
		
		try {
			result = display.map(button2, button1, (Point) null);
			fail("No exception thrown for null point");
		} catch (IllegalArgumentException e) {
			assertSWTProblem("Incorrect exception thrown for point being null", SWT.ERROR_NULL_ARGUMENT, e);
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

		Shell shellMapTest1 = new Shell(display,SWT.NO_TRIM);
		Button buttonTest1 = new Button(shellMapTest1, SWT.PUSH);
		buttonTest1.setBounds(0,0,100,100);
		shellMapTest1.setBounds(200, 200, 400, 400);
		shellMapTest1.open();
		Shell overlayShell1 = new Shell(shellMapTest1, SWT.NO_TRIM); //subshell
		overlayShell1.setBounds(shellMapTest1.getBounds());
		Rectangle r = buttonTest1.getBounds();
		result = display.map(buttonTest1, null, r);
		result = display.map(null, overlayShell1, result);
		assertEquals(r, result);

		Shell shellMapTest2 = new Shell(display,SWT.NO_TRIM);
		Button buttonTest2 = new Button(shellMapTest2, SWT.PUSH);
		buttonTest2.setBounds(0,0,100,100);
		shellMapTest2.setBounds(200, 200, 400, 400);
		shellMapTest2.open();
		Shell overlayShell2 = new Shell(display, SWT.NO_TRIM); //root shell
		overlayShell2.setBounds(shellMapTest2.getBounds());
		r = buttonTest2.getBounds();
		result = display.map(buttonTest2, null, r);
		result = display.map(null, overlayShell2, result);
		assertEquals(r, result);

		Shell shellMapTestRtoL1 = new Shell(display,SWT.NO_TRIM);
		shellMapTestRtoL1.setOrientation(SWT.RIGHT_TO_LEFT);
		Button buttonTestRtoL1 = new Button(shellMapTestRtoL1, SWT.PUSH);
		buttonTestRtoL1.setBounds(0,0,100,100);
		shellMapTestRtoL1.setBounds(200, 200, 400, 400);
		shellMapTestRtoL1.open();
		Shell overlayShellRtoL1 = new Shell(shellMapTestRtoL1, SWT.NO_TRIM); //subshell
		overlayShellRtoL1.setOrientation(SWT.RIGHT_TO_LEFT);
		overlayShellRtoL1.setBounds(shellMapTestRtoL1.getBounds());
		r = buttonTestRtoL1.getBounds();
		result = display.map(buttonTestRtoL1, null, r);
		result = display.map(null, overlayShellRtoL1, result);
		assertEquals(r, result);

		Shell shellMapTestRtoL2 = new Shell(display,SWT.NO_TRIM);
		shellMapTestRtoL2.setOrientation(SWT.RIGHT_TO_LEFT);
		Button buttonTestRtoL2 = new Button(shellMapTestRtoL2, SWT.PUSH);
		buttonTestRtoL2.setBounds(0,0,100,100);
		shellMapTestRtoL2.setBounds(200, 200, 400, 400);
		shellMapTestRtoL2.open();
		Shell overlayShellRtoL2 = new Shell(display, SWT.NO_TRIM); //root shell
		overlayShellRtoL2.setOrientation(SWT.RIGHT_TO_LEFT);
		overlayShellRtoL2.setBounds(shellMapTestRtoL2.getBounds());
		r = buttonTestRtoL2.getBounds();
		result = display.map(buttonTestRtoL2, null, r);
		result = display.map(null, overlayShellRtoL2, result);
		assertEquals(r, result);

		button1.dispose();
		buttonTest1.dispose();
		buttonTest2.dispose();
		shellMapTest1.dispose();
		shellMapTest2.dispose();
		overlayShell1.dispose();
		overlayShell2.dispose();

		buttonTestRtoL1.dispose();
		buttonTestRtoL2.dispose();
		shellMapTestRtoL1.dispose();
		shellMapTestRtoL2.dispose();
		overlayShellRtoL1.dispose();
		overlayShellRtoL2.dispose();

		try {
			result = display.map(button1, button2, rect);
			fail("No exception thrown for map from control being disposed");
		} catch (IllegalArgumentException e) {
			assertSWTProblem("Incorrect exception thrown for map from control being disposed", SWT.ERROR_INVALID_ARGUMENT, e);
		}
		try {
			result = display.map(button2, button1, rect);
			fail("No exception thrown for map to control being disposed");
		} catch (IllegalArgumentException e) {
			assertSWTProblem("Incorrect exception thrown for map to control being disposed", SWT.ERROR_INVALID_ARGUMENT, e);
		}
		
		try {
			result = display.map(button2, button1, (Rectangle) null);
			fail("No exception thrown for null point");
		} catch (IllegalArgumentException e) {
			assertSWTProblem("Incorrect exception thrown for rectangle being null", SWT.ERROR_NULL_ARGUMENT, e);
		}

		shell.dispose();
	} finally {
		display.dispose();
	}
}

public void test_postLorg_eclipse_swt_widgets_Event() {
	if (SwtTestUtil.isGTK || SwtTestUtil.isCocoa) {
		//TODO Fix GTK and Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out.println("Excluded test_postLorg_eclipse_swt_widgets_Event(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Display)");
		}
		return;
	}
	
	final int KEYCODE = SWT.SHIFT;
	
	Display display = new Display();
	try {
		try {
			display.post(null);
			fail("No exception thrown for post with null argument");
		} catch (IllegalArgumentException e) {
			assertSWTProblem("Incorrect exception thrown for post with null argument", SWT.ERROR_NULL_ARGUMENT, e);
		}
		
		Shell shell = new Shell(display, SWT.NO_TRIM);
		shell.setBounds(display.getBounds());
		shell.open();

		Event event;
		
		// Test key events (down/up)
		event = new Event();
		event.type = SWT.KeyDown;
		event.keyCode = -1;  // bogus key code; default 0 character
		assertTrue("Display#post failed, probably because screen is not rendered (bug 407862)", display.post(event));  //$NON-NLS-1$
		// don't test KeyDown/KeyUp with a character to avoid sending to 
		// random window if test shell looses focus
		
		event = new Event();
		event.type = SWT.KeyUp;
		assertTrue(display.post(event));

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
			assertSWTProblem("Incorrect exception thrown for removeFilter with null argument", SWT.ERROR_NULL_ARGUMENT, e);
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
			assertSWTProblem("Incorrect exception thrown for removeListener with null argument", SWT.ERROR_NULL_ARGUMENT, e);
		}
		
		display.addListener(SWT.Dispose, listener);
		display.removeListener(SWT.Dispose, listener);
	} finally {
		display.close();
	}
	assertFalse(callbackReceived[CLOSE_CALLBACK]);
	assertFalse(callbackReceived[DISPOSE_CALLBACK]);
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
			assertSWTProblem("Incorrect exception thrown for setCursorLocation with null argument", SWT.ERROR_NULL_ARGUMENT, e);
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
			assertSWTProblem("Incorrect exception thrown for set synchronizer with null argument", SWT.ERROR_NULL_ARGUMENT, e);
		}
		
		class MySynchronizer extends Synchronizer {
			boolean invoked = false;
			MySynchronizer(Display d) {
				super(d);
			}
			@Override
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
			@Override
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
			@Override
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

public void test_syncExecLjava_lang_Runnable_dispose() {
	final Display display = new Display();
	try {
		display.syncExec(new Runnable() {
			public void run() {
				display.dispose();
			}
		});
	} finally {
		assertTrue(display.isDisposed());
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
			assertSWTProblem("Incorrect exception thrown for timerExec with null runnable", SWT.ERROR_NULL_ARGUMENT, e);
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

/* custom */
boolean disposeExecRan;

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

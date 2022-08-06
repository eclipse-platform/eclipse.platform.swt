/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Red Hat Inc. - Bug 462631
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.eclipse.swt.tests.junit.SwtTestUtil.assertSWTProblem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.DeviceData;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.DPIUtil;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Synchronizer;
import org.eclipse.test.Screenshots;
import org.junit.Assume;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Display
 *
 * @see org.eclipse.swt.widgets.Display
 */
public class Test_org_eclipse_swt_widgets_Display {

private static boolean isRunningOnEclipseOrgHudson =
		"hudsonbuild".equalsIgnoreCase(System.getProperty("user.name"))
		|| "genie.platform".equalsIgnoreCase(System.getProperty("user.name"));

private static final boolean BUG_492569 = SwtTestUtil.isWindows && isRunningOnEclipseOrgHudson;

@Rule
public TestName testName = new TestName();

@Test
public void test_Constructor() {
	Display disp = new Display();
	System.out.println("org.eclipse.swt.internal.DPIUtil.getDeviceZoom(): " + DPIUtil.getDeviceZoom());
	disp.dispose();
	if (SwtTestUtil.isGTK) {
		System.out.println("org.eclipse.swt.internal.gtk.version=" + System.getProperty("org.eclipse.swt.internal.gtk.version"));
	}
}

@Test
public void test_ConstructorLorg_eclipse_swt_graphics_DeviceData() {
	Display disp;
	disp = new Display(null);
	disp.dispose();

	disp = new Display(new DeviceData());
	disp.dispose();
}

@Test
public void test_addFilterILorg_eclipse_swt_widgets_Listener() {
	final int CLOSE_CALLBACK = 0;
	final int DISPOSE_CALLBACK = 1;
	final boolean[] callbackReceived = new boolean[] {false, false};

	Listener listener = e -> {
		if (e.type == SWT.Close)
			callbackReceived[CLOSE_CALLBACK] = true;
		else if (e.type == SWT.Dispose)
			callbackReceived[DISPOSE_CALLBACK] = true;
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

@Test
public void test_addListenerILorg_eclipse_swt_widgets_Listener() {
	final int CLOSE_CALLBACK = 0;
	final int DISPOSE_CALLBACK = 1;
	final boolean[] callbackReceived = new boolean[] {false, false};

	Listener listener = e -> {
		if (e.type == SWT.Close)
			callbackReceived[CLOSE_CALLBACK] = true;
		else if (e.type == SWT.Dispose)
			callbackReceived[DISPOSE_CALLBACK] = true;
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

@Test
public void test_asyncExecLjava_lang_Runnable() {
	final Display display = new Display();
	try {
		display.asyncExec(() -> display.beep());
	} finally {
		display.dispose();
	}
}

@Test
public void test_Executor() throws InterruptedException {
	final Display display = new Display();
	try {
		AtomicInteger integer = new AtomicInteger();
		display.execute(() -> {
			assertEquals(display, Display.getCurrent());
			integer.set(1);
		});
		assertEquals(1, integer.get());
		CountDownLatch latch = new CountDownLatch(1);
		new Thread(() -> {
			display.execute(() -> {
				try {
					assertEquals(display, Display.getCurrent());
					integer.set(2);
				} finally {
					latch.countDown();
				}
			});
		}).start();
		while(!latch.await(10, TimeUnit.MILLISECONDS)) {
			while(display.readAndDispatch ()) {
				//dispatch
			}
		}
		assertEquals(2, integer.get());
		CompletableFuture<Void> future = CompletableFuture.supplyAsync(()->{
			assertNull(Display.getCurrent());
			return "Hello SWT from background thread";
		}).thenRunAsync(()->{
			assertEquals(display, Display.getCurrent());
		}, Display.getDefault());
		while(!future.isDone()) {
			while(display.readAndDispatch ()) {
				//dispatch
			}
		}
		assertFalse(future.isCancelled());
		assertFalse(future.isCompletedExceptionally());
	} finally {
		display.dispose();
	}
}

@Test
public void test_asyncExecLjava_lang_Runnable_dispose() {
	final Display display = new Display();
	try {
		disposeExecRan = false;
		display.asyncExec(() -> {
			display.dispose();
			disposeExecRan = true;
		});
	} finally {
		while (!disposeExecRan) {
			if (!display.readAndDispatch()) display.sleep ();
		}
	}
}

@Test
public void test_beep() {
	Display display = new Display();
	try {
		display.beep();
	} finally {
		display.dispose();
	}
}

@Test
public void test_close() {
	Display display = new Display();
	display.close();
	assertTrue(display.isDisposed());
}

@Test
public void test_disposeExecLjava_lang_Runnable() {
	// Also tests dispose and isDisposed
	Display testDisplay = new Display();
	disposeExecRan = false;
	testDisplay.disposeExec(() -> disposeExecRan = true);
	assertEquals("Display should not be disposed", false, testDisplay.isDisposed());
	testDisplay.dispose();
	assertTrue("Display should be disposed", testDisplay.isDisposed());
	assertTrue("DisposeExec Runnable did not run", disposeExecRan);
}

@Test
public void test_findDisplayLjava_lang_Thread() {
	assertNull(Display.findDisplay(new Thread()));

	Display display = new Display();
	try {
		assertEquals(display, Display.findDisplay(Thread.currentThread()));
	} finally {
		display.dispose();
	}
}

@Test
public void test_getActiveShell() {
	Assume.assumeFalse("Test fails on Mac: Bug 536564", SwtTestUtil.isCocoa);
	Display display = new Display();
	try {
		Shell shell = new Shell(display);
		shell.setText("test_getActiveShell");
		shell.open();
		drainEventQueue(display, 150); // workaround for https://bugs.eclipse.org/506680
		assertSame(shell, display.getActiveShell());
		shell.dispose();
	} finally {
		display.dispose();
	}
}

private static void drainEventQueue(Display display, int millis) {
	if (millis == 0) {
		while (!display.isDisposed() && display.readAndDispatch()) {
		}
		return;
	}
	long end = System.currentTimeMillis() + millis;
	while (!display.isDisposed() && System.currentTimeMillis() < end) {
		if (!display.readAndDispatch ()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

@Test
public void test_getBounds() {
	Display display = new Display();
	try {
		Rectangle rect = display.getBounds();
		assertNotNull(rect);
	} finally {
		display.dispose();
	}
}

@Test
public void test_getClientArea() {
	Display display = new Display();
	try {
		Rectangle rect = display.getClientArea();
		assertNotNull(rect);
	} finally {
		display.dispose();
	}
}

@Test
public void test_getCurrent() {
	Display display = new Display();
	try {
		assertSame(display.getThread(), Thread.currentThread());
	} finally {
		display.dispose();
	}
}

@Test
public void test_getCursorControl() {
	Display display = new Display();
	try {
		display.getCursorControl();
	} finally {
		display.dispose();
	}
}

@Test
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

@Test
public void test_getDefault() {
	Display display = new Display();
	try {
		assertNotNull(Display.getDefault());
	} finally {
		display.dispose();
	}
}

@Test
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

@Test
public void test_getDoubleClickTime() {
	Display display = new Display();
	try {
		int time = display.getDoubleClickTime();
		assertTrue(time > 0);
	} finally {
		display.dispose();
	}
}

@Test
public void test_getFocusControl() {
	Display display = new Display();
	try {
		display.getFocusControl();
	} finally {
		display.dispose();
	}
}

@Test
public void test_getIconDepth() {
	Display display = new Display();
	try {
		int depth = display.getIconDepth();
		assertTrue(depth > 0);
	} finally {
		display.dispose();
	}
}

@Test
public void test_getMonitors() {
	Display display = new Display();
	Monitor[] monitors = display.getMonitors();
	assertNotNull(monitors);
	assertTrue("at least one monitor should be returned", monitors.length >= 1);
	for (int i = 0; i < monitors.length; i++)
		assertNotNull("monitor at index "+i+" should not be null", monitors[i]);
	display.dispose();
}



/**
 * Note, this is hard to test via unit tests as you might not have multiple monitors.
 * When altering this function, it is recommended to perform
 * the following tests manually:
 * - Dual monitors with:
 *   - Primary being on the left
 *   - Primary being on the right
 *   - Stacked displays, primary on top
 *   - Stack displays, primary on bottom
 * - Single display
 */
@Test
public void test_getPrimaryMonitor() {
	Display display = new Display();
	Monitor monitor = display.getPrimaryMonitor();
	assertNotNull(monitor);
	display.dispose();
}

@Test
public void test_getShells() {
	Display display = new Display();
	try {
		Shell shell1 = new Shell(display);
		Shell shell2 = new Shell(display);
		assertEquals(2, display.getShells().length);
		shell1.dispose();
		shell2.dispose();
	} finally {
		display.dispose();
	}
}

@Test
public void test_getSyncThread() {
	final Display display = new Display();
	try {
		final boolean[] threadRan = new boolean[] {false};
		Thread nonUIThread = new Thread(() -> {
			// Assume no syncExec runnable is currently being invoked.
			assertNull(display.getSyncThread());

			// Create a runnable and invoke with syncExec to verify that
			// the invoking thread is the syncThread.
			final Thread invokingThread = Thread.currentThread();
			display.syncExec(() -> assertEquals(invokingThread, display.getSyncThread()));

			// Create a runnable and invoke with asyncExec to verify that
			// the syncThread is null while it's running.
			AtomicBoolean asyncExecRan = new AtomicBoolean(false);
			display.asyncExec(() -> {
				assertNull(display.getSyncThread());
				asyncExecRan.set(true);
			});

			try {
				while (!asyncExecRan.get()) {
					Thread.sleep(100);
				}
			} catch (InterruptedException ex) {
			}
			threadRan[0] = true;
			display.wake();
		});
		nonUIThread.start();

		while (!threadRan[0]) {
			if (!display.readAndDispatch()) display.sleep ();
		}
	} finally {
		display.dispose();
	}
}

@Test
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
		for (int colorId : colorIds) {
			assertNotNull(display.getSystemColor(colorId));
		}
	} finally {
		display.dispose();
	}
}

@Test
public void test_getSystemFont() {
	Display display = new Display();
	try {
		Font font = display.getSystemFont();
		assertNotNull(font);
	} finally {
		display.dispose();
	}
}

@Test
public void test_getThread() {
	Display display = new Display();
	try {
		assertSame(display.getThread(), Thread.currentThread());
	} finally {
		display.dispose();
	}
}

@Test
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
		assertEquals(new Point(shellOffset.x + 197, shellOffset.y + 94), result);
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

@Test
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

@Test
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

@Test
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

@Test
public void test_postLorg_eclipse_swt_widgets_Event() {
	if (SwtTestUtil.isGTK || SwtTestUtil.isCocoa || SwtTestUtil.isWindows ) {
		//TODO Fix/revisit GTK, Cocoa and Win10 failure test-case via bug 553754
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

		drainEventQueue(display, 500); // attempt to work around https://bugs.eclipse.org/492569

		shell.dispose();

		//TODO: Verify the events. The following comment is wrong:
		// can't verify that the events were actually sent to a listener.
		// the test shell won't receive any events if it has lost focus,
		// e.g., due to user intervention or another process popping up
		// a window.
	} finally {
		display.dispose();
	}
}

@Test
public void test_readAndDispatch() {
	// The following tests rely on readAndDispatch in order to succeed,
	// thus no test is needed here.
	//    test_getSyncThread()
	//    test_postLorg_eclipse_swt_widgets_Event()
}

@Test
public void test_removeFilterILorg_eclipse_swt_widgets_Listener() {
	final int CLOSE_CALLBACK = 0;
	final int DISPOSE_CALLBACK = 1;
	final boolean[] callbackReceived = new boolean[] {false, false};

	Listener listener = e -> {
		if (e.type == SWT.Close)
			callbackReceived[CLOSE_CALLBACK] = true;
		else if (e.type == SWT.Dispose)
			callbackReceived[DISPOSE_CALLBACK] = true;
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

@Test
public void test_removeListenerILorg_eclipse_swt_widgets_Listener() {
	final int CLOSE_CALLBACK = 0;
	final int DISPOSE_CALLBACK = 1;
	final boolean[] callbackReceived = new boolean[] {false, false};

	Listener listener = e -> {
		if (e.type == SWT.Close)
			callbackReceived[CLOSE_CALLBACK] = true;
		else if (e.type == SWT.Dispose)
			callbackReceived[DISPOSE_CALLBACK] = true;
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

@Test
public void test_setAppNameLjava_lang_String() {
	Display.setAppName("My Application Name");
}

@Test
public void test_setCursorLocationII() {
	Display display = new Display();
	try {
		// Move mouse back to original location, to prevent mouse being jerked around.
		Point originalLocation = display.getCursorLocation();
		display.addListener(SWT.Dispose, e -> display.setCursorLocation(originalLocation));

		Point location = new Point(100, 100);
		display.setCursorLocation(location.x, location.y); // don't put cursor into a corner, since that could trigger special platform events
		drainEventQueue(display, 150); // workaround for https://bugs.eclipse.org/492569
		Point actual = display.getCursorLocation();
		if (!BUG_492569 && SwtTestUtil.isX11) {
			if (!location.equals(actual)) {
				Screenshots.takeScreenshot(getClass(), testName.getMethodName()); // Bug 528968 This call causes crash on Wayland.
				fail("\nExpected:"+location.toString()+"  Actual:"+actual.toString());
			}
		} else {
			System.out.println(getClass().getName() + "#" + testName.getMethodName() + ": actual == " + actual);
		}

	} finally {
		display.dispose();
	}
}

@Test
public void test_setCursorLocationLorg_eclipse_swt_graphics_Point() {
	Display display = new Display();
	try {
		// Move mouse back to original location, to prevent mouse being jerked around.
		Point originalLocation = display.getCursorLocation();
		display.addListener(SWT.Dispose, e -> display.setCursorLocation(originalLocation));

		Point location = new Point(100, 50);
		display.setCursorLocation(location); // don't put cursor into a corner, since that could trigger special platform events
		try {
			display.setCursorLocation(null);
			fail("No exception thrown for null argument");
		} catch (IllegalArgumentException e) {
			assertSWTProblem("Incorrect exception thrown for setCursorLocation with null argument", SWT.ERROR_NULL_ARGUMENT, e);
		}
		drainEventQueue(display, 150); // workaround for https://bugs.eclipse.org/492569
		Point actual = display.getCursorLocation();
		if (!BUG_492569 && SwtTestUtil.isX11) {
			if (!location.equals(actual)) {
				Screenshots.takeScreenshot(getClass(), testName.getMethodName()); // Bug 528968 This call causes crash on Wayland.
				fail("\nExpected:"+location.toString()+"  Actual:"+actual.toString());
			}
		} else {
			System.out.println(getClass().getName() + "#" + testName.getMethodName() + ": actual == " + actual);
		}
	} finally {
		display.dispose();
	}
}

@Test
public void test_setDataLjava_lang_Object() {
	Display display = new Display();
	try {
		display.setData(Integer.valueOf(10));
		Integer i = (Integer)display.getData();
		assertNotNull(i);
		assertEquals(Integer.valueOf(10), i);
	} finally {
		display.dispose();
	}
}

@Test
public void test_setDataLjava_lang_StringLjava_lang_Object() {
	Display display = new Display();
	try {
		display.setData("Integer", Integer.valueOf(10));
		display.setData("String", "xyz");
		Integer i = (Integer)display.getData("Integer");
		assertNotNull(i);
		assertEquals(Integer.valueOf(10), i);
		String s = (String)display.getData("String");
		assertNotNull(s);
		assertEquals("xyz", s);
	} finally {
		display.dispose();
	}
}

@Test
public void test_setSynchronizerLorg_eclipse_swt_widgets_Synchronizer() {
	final Display display = new Display();
	final boolean[] asyncExec0Ran = new boolean[] {false};
	final boolean[] asyncExec1Ran = new boolean[] {false};
	final boolean[] asyncExec2Ran = new boolean[] {false};
	final boolean[] asyncExec3Ran = new boolean[] {false};

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
		mySynchronizer.asyncExec(() -> asyncExec3Ran[0] = asyncExec2Ran[0]); // assert it runs after 2
		display.asyncExec(() -> asyncExec0Ran[0] = true);
		display.asyncExec(() -> asyncExec2Ran[0] = asyncExec0Ran[0]); // assert it runs after 0
		display.setSynchronizer(mySynchronizer);
		display.asyncExec(() -> asyncExec1Ran[0] = true);
		assertFalse(asyncExec0Ran[0]);
		assertFalse(asyncExec1Ran[0]);
		assertFalse(asyncExec2Ran[0]);
		while (display.readAndDispatch()) {}
		assertTrue(mySynchronizer.invoked);
		assertTrue(asyncExec0Ran[0]);
		assertTrue(asyncExec1Ran[0]);
		assertTrue(asyncExec2Ran[0]);
		assertTrue(asyncExec3Ran[0]);
	} finally {
		display.dispose();
	}
}

@Test
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
				display.syncExec(() -> {
					Shell s = new Shell(display);
					s.open();
					s.dispose();
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

@Test
public void test_syncExecLjava_lang_Runnable() {
	final Display display = new Display();
	try {
		display.syncExec(() -> display.beep());
	} finally {
		display.dispose();
	}
}

@Test
public void test_syncExecLjava_lang_Runnable_dispose() {
	final Display display = new Display();
	try {
		display.syncExec(() -> display.dispose());
	} finally {
		assertTrue(display.isDisposed());
	}
}

@Test
public void test_syncCall() {
	final Display display = new Display();
	try {
		int depth=display.syncCall(() -> display.getDepth());
		assertEquals(display.getDepth(), depth);
	} finally {
		display.dispose();
	}
}

@Test
public void test_syncCall_dispose() {
	final Display display = new Display();
	try {
		int magic=display.syncCall(() -> {display.dispose(); return 42;});
		assertEquals(42, magic);
	} finally {
		assertTrue(display.isDisposed());
	}
}
@Test
public void test_syncCall_RuntimeException() {
	final Display display = new Display();
	try {
		int depth=display.syncCall(() -> {throw new IllegalArgumentException("42");});
		assertFalse("should not be reached "+depth, true);
	} catch (RuntimeException e) {
		assertEquals("42", e.getMessage());
	} finally {
		display.dispose();
	}
}
@Test
public void test_syncCall_Exception() {
	final Display display = new Display();
	try {
		int depth=display.syncCall(() -> {throw new IOException("42");});
		assertFalse("should not be reached "+depth, true);
	} catch (IOException e) {
		assertEquals("42", e.getMessage());
	} finally {
		display.dispose();
	}
}
@Test
public void test_syncCall_SWTException() {
	final Display display = new Display();
	display.dispose();
	try {
		int magic=display.syncCall(() -> {display.dispose(); return 42;});
		assertFalse("should not be reached "+magic, true);
	} catch (SWTException e) {
		assertEquals("Device is disposed", e.getMessage());
	}
}
@Test
public void test_syncCall_concurrentCallable() {
	final Display display = new Display();
	try {
		java.util.concurrent.Callable<Integer> c=() -> {return 42;};
		int magic=display.syncCall(c::call);
		assertEquals(42, magic);
	} catch (Exception e) {
		assertFalse("should not be reached ", true);
	} finally {
		display.dispose();
	}
}
@Test
public void test_syncCall_concurrentCallable_Exception() {
	final Display display = new Display();
	try {
		java.util.concurrent.Callable<Integer> c=() -> {throw new IOException("42");};
		int depth=display.syncCall(c::call);
		assertFalse("should not be reached "+depth, true);
	} catch (Exception e) {
		assertEquals("42", e.getMessage());
	} finally {
		display.dispose();
	}
}

@Test
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

		display.timerExec(-100, () -> timerExecRan[0] = true);

		final int delay = 3000;
		final long startTime = System.currentTimeMillis();
		display.timerExec(delay, () -> {
			long endTime = System.currentTimeMillis();
			// debug intermittent test failure
			if (endTime < (startTime + delay)) {
				System.out.println("Display.timerExec ran early " + ((startTime + delay) - endTime));
			}
			//assertTrue("Timer ran early! ms early: ", endTime >= (startTime + delay));
			threadRan[0] = true;
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

@Test
public void test_update() {
	Display display = new Display();
	try {
		display.update();
	} finally {
		display.dispose();
	}
}

@Test
public void test_wake() {
	// tested in sleep() method
}

/* custom */
boolean disposeExecRan;

@Test
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

@Test
public void test_getDepth() {
	Display display = new Display();
	try {
		int d = display.getDepth();
		assertTrue("depth not greater than zero", d > 0);
	} finally {
		display.dispose();
	}
}
@Test
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

@Test
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

@Test
public void test_isDisposed() {
	Display disp = new Display();
	assertFalse(disp.isDisposed());
	disp.dispose();
	assertTrue(disp.isDisposed());
}

@Test
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

@Test
public void test_isSystemDarkTheme() {
	System.out.println("org.eclipse.swt.widgets.Display.isSystemDarkTheme(): " + Display.isSystemDarkTheme());
}

}

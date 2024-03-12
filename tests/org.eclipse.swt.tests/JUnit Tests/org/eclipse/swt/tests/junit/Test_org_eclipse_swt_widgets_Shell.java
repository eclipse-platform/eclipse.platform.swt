/*******************************************************************************
 * Copyright (c) 2000, 2023 IBM Corporation and others.
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
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Shell
 *
 * @see org.eclipse.swt.widgets.Shell
 */
public class Test_org_eclipse_swt_widgets_Shell extends Test_org_eclipse_swt_widgets_Decorations {

private static final boolean IS_GTK_BUG_445900 = SwtTestUtil.isGTK;

@Override
@Before
public void setUp() {
	super.setUp();
	testShell = new Shell(shell, SWT.NULL);
	setWidget(shell);
	assertEquals(shell, testShell.getParent());
}

@Test
public void test_Constructor() {
	Shell newShell = new Shell();
	assertNotNull("a: ", newShell.getDisplay());
	newShell.dispose();
}

@Test
public void test_ConstructorI() {
	/* this should test various combinations of STYLE bits, for now just test individual bits */
	int[] cases = {SWT.NO_TRIM, SWT.RESIZE, SWT.TITLE, SWT.CLOSE, SWT.MENU, SWT.MIN, SWT.BORDER,
				   SWT.CLIP_CHILDREN, SWT.CLIP_SIBLINGS, SWT.ON_TOP, SWT.FLAT, SWT.SMOOTH};
	Shell newShell;
	for (int i = 0; i < cases.length; i++) {
		newShell = new Shell(cases[i]);
		assertEquals("a " + i, shell.getDisplay(), newShell.getDisplay());
		assertNotEquals(0, newShell.getStyle() & cases[i]);
		newShell.dispose();
	}
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_Display() {
	Display display = shell.getDisplay();
	Shell newShell = new Shell(display);
	assertEquals("a: ", display, newShell.getDisplay());
	newShell.dispose();
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_DisplayI() {
	int[] cases = {SWT.NO_TRIM, SWT.RESIZE, SWT.TITLE, SWT.CLOSE, SWT.MENU, SWT.MIN, SWT.BORDER,
				   SWT.CLIP_CHILDREN, SWT.CLIP_SIBLINGS, SWT.ON_TOP, SWT.FLAT, SWT.SMOOTH};
	Shell newShell;
	Display display = shell.getDisplay();
	for (int i = 0; i < cases.length; i++) {
		newShell = new Shell(display, cases[i]);
		assertEquals("a " + i, shell.getDisplay(), newShell.getDisplay());
		newShell.dispose();
	}
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_Shell() {
	Shell newShell = new Shell(shell);
	assertEquals("a: ", shell, newShell.getParent());
	newShell.dispose();
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_ShellI() {
	/* this should test various combinations of STYLE bits, for now just test individual bits */
	int[] cases = {SWT.NO_TRIM, SWT.RESIZE, SWT.TITLE, SWT.CLOSE, SWT.MENU, SWT.MIN, SWT.BORDER,
				   SWT.CLIP_CHILDREN, SWT.CLIP_SIBLINGS, SWT.ON_TOP, SWT.FLAT, SWT.SMOOTH};
	Shell newShell;
	for (int i = 0; i < cases.length; i++) {
		newShell = new Shell(shell, cases[i]);
		assertEquals("a: " + i, shell, newShell.getParent());
		newShell.dispose();
	}
}

@Test
public void test_addShellListenerLorg_eclipse_swt_events_ShellListener() {
	listenerCalled = false;
	boolean exceptionThrown = false;
	ShellListener listener = new ShellListener() {
		@Override
		public void shellActivated(ShellEvent e) {
			listenerCalled = true;
		}
		@Override
		public void shellClosed(ShellEvent e) {
		}
		@Override
		public void shellDeactivated(ShellEvent e) {
		}
		@Override
		public void shellDeiconified(ShellEvent e) {
		}
		@Override
		public void shellIconified(ShellEvent e) {
		}
	};
	try {
		shell.addShellListener(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown", exceptionThrown);
	exceptionThrown = false;
	shell.addShellListener(listener);
	shell.forceActive();
	/* can't assume listener is synchronously called when forceActive returned */
	/* assertTrue(":a:", listenerCalled == true); */

	listenerCalled = false;
	shell.removeShellListener(listener);
	shell.forceActive();
	/* can't assume listener is synchronously called when forceActive returned */
	/* assertTrue(":b:", listenerCalled == false); */
	try {
		shell.removeShellListener(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown", exceptionThrown);
}

@Test
public void test_addShellListenerShellActivatedAdapterLorg_eclipse_swt_events_ShellListener() {
	ShellListener listener = ShellListener.shellActivatedAdapter(e -> eventOccurred = true);
	shell.addShellListener(listener);
	eventOccurred = false;

	shell.notifyListeners(SWT.Activate, new Event());
	assertTrue(eventOccurred);

	eventOccurred = false;

	shell.notifyListeners(SWT.Deactivate, new Event());
	assertFalse(eventOccurred);

	eventOccurred = false;

	shell.notifyListeners(SWT.Deiconify, new Event());
	assertFalse(eventOccurred);

	eventOccurred = false;

	shell.notifyListeners(SWT.Iconify, new Event());
	assertFalse(eventOccurred);

	eventOccurred = false;

	shell.notifyListeners(SWT.Close, new Event());
	assertFalse(eventOccurred);

	shell.removeShellListener(listener);
	eventOccurred = false;

	shell.notifyListeners(SWT.Activate, new Event());
	assertFalse(eventOccurred);

	shell.notifyListeners(SWT.Deactivate, new Event());
	assertFalse(eventOccurred);

	shell.notifyListeners(SWT.Deiconify, new Event());
	assertFalse(eventOccurred);

	shell.notifyListeners(SWT.Iconify, new Event());
	assertFalse(eventOccurred);

	shell.notifyListeners(SWT.Close, new Event());
	assertFalse(eventOccurred);
}

@Test
public void test_addShellListenerShellDeactivatedAdapterLorg_eclipse_swt_events_ShellListener() {
	ShellListener listener = ShellListener.shellDeactivatedAdapter(e -> eventOccurred = true);
	shell.addShellListener(listener);
	eventOccurred = false;

	shell.notifyListeners(SWT.Deactivate, new Event());
	assertTrue(eventOccurred);

	eventOccurred = false;

	shell.notifyListeners(SWT.Activate, new Event());
	assertFalse(eventOccurred);

	eventOccurred = false;

	shell.notifyListeners(SWT.Deiconify, new Event());
	assertFalse(eventOccurred);

	eventOccurred = false;

	shell.notifyListeners(SWT.Iconify, new Event());
	assertFalse(eventOccurred);

	eventOccurred = false;

	shell.notifyListeners(SWT.Close, new Event());
	assertFalse(eventOccurred);

	shell.removeShellListener(listener);
	eventOccurred = false;

	shell.notifyListeners(SWT.Activate, new Event());
	assertFalse(eventOccurred);

	shell.notifyListeners(SWT.Deactivate, new Event());
	assertFalse(eventOccurred);

	shell.notifyListeners(SWT.Deiconify, new Event());
	assertFalse(eventOccurred);

	shell.notifyListeners(SWT.Iconify, new Event());
	assertFalse(eventOccurred);

	shell.notifyListeners(SWT.Close, new Event());
	assertFalse(eventOccurred);
}

@Test
public void test_addShellListenerShellDeiconifiedAdapterLorg_eclipse_swt_events_ShellListener() {
	ShellListener listener = ShellListener.shellDeiconifiedAdapter(e -> eventOccurred = true);
	shell.addShellListener(listener);
	eventOccurred = false;

	shell.notifyListeners(SWT.Deiconify, new Event());
	assertTrue(eventOccurred);

	eventOccurred = false;

	shell.notifyListeners(SWT.Deactivate, new Event());
	assertFalse(eventOccurred);

	eventOccurred = false;

	shell.notifyListeners(SWT.Activate, new Event());
	assertFalse(eventOccurred);

	eventOccurred = false;

	shell.notifyListeners(SWT.Iconify, new Event());
	assertFalse(eventOccurred);

	eventOccurred = false;

	shell.notifyListeners(SWT.Close, new Event());
	assertFalse(eventOccurred);

	shell.removeShellListener(listener);
	eventOccurred = false;

	shell.notifyListeners(SWT.Activate, new Event());
	assertFalse(eventOccurred);

	shell.notifyListeners(SWT.Deactivate, new Event());
	assertFalse(eventOccurred);

	shell.notifyListeners(SWT.Deiconify, new Event());
	assertFalse(eventOccurred);

	shell.notifyListeners(SWT.Iconify, new Event());
	assertFalse(eventOccurred);

	shell.notifyListeners(SWT.Close, new Event());
	assertFalse(eventOccurred);
}

@Test
public void test_addShellListenerShellIconifiedAdapterLorg_eclipse_swt_events_ShellListener() {
	ShellListener listener = ShellListener.shellIconifiedAdapter(e -> eventOccurred = true);
	shell.addShellListener(listener);
	eventOccurred = false;

	shell.notifyListeners(SWT.Iconify, new Event());
	assertTrue(eventOccurred);

	eventOccurred = false;

	shell.notifyListeners(SWT.Deactivate, new Event());
	assertFalse(eventOccurred);

	eventOccurred = false;

	shell.notifyListeners(SWT.Deiconify, new Event());
	assertFalse(eventOccurred);

	eventOccurred = false;

	shell.notifyListeners(SWT.Activate, new Event());
	assertFalse(eventOccurred);

	eventOccurred = false;

	shell.notifyListeners(SWT.Close, new Event());
	assertFalse(eventOccurred);

	shell.removeShellListener(listener);
	eventOccurred = false;

	shell.notifyListeners(SWT.Activate, new Event());
	assertFalse(eventOccurred);

	shell.notifyListeners(SWT.Deactivate, new Event());
	assertFalse(eventOccurred);

	shell.notifyListeners(SWT.Deiconify, new Event());
	assertFalse(eventOccurred);

	shell.notifyListeners(SWT.Iconify, new Event());
	assertFalse(eventOccurred);

	shell.notifyListeners(SWT.Close, new Event());
	assertFalse(eventOccurred);
}

@Test
public void test_addShellListenerShellClosedAdapterLorg_eclipse_swt_events_ShellListener() {
	ShellListener listener = ShellListener.shellClosedAdapter(e -> eventOccurred = true);
	shell.addShellListener(listener);
	eventOccurred = false;

	shell.notifyListeners(SWT.Close, new Event());
	assertTrue(eventOccurred);

	eventOccurred = false;

	shell.notifyListeners(SWT.Deactivate, new Event());
	assertFalse(eventOccurred);

	eventOccurred = false;

	shell.notifyListeners(SWT.Deiconify, new Event());
	assertFalse(eventOccurred);

	eventOccurred = false;

	shell.notifyListeners(SWT.Iconify, new Event());
	assertFalse(eventOccurred);

	eventOccurred = false;

	shell.notifyListeners(SWT.Activate, new Event());
	assertFalse(eventOccurred);

	shell.removeShellListener(listener);
	eventOccurred = false;

	shell.notifyListeners(SWT.Activate, new Event());
	assertFalse(eventOccurred);

	shell.notifyListeners(SWT.Deactivate, new Event());
	assertFalse(eventOccurred);

	shell.notifyListeners(SWT.Deiconify, new Event());
	assertFalse(eventOccurred);

	shell.notifyListeners(SWT.Iconify, new Event());
	assertFalse(eventOccurred);

	shell.notifyListeners(SWT.Close, new Event());
	assertFalse(eventOccurred);
}

@Test
public void test_close() {

	// bogus line that 'enabled' gpfs
	//	Shell newShell = new Shell();
	testShell.setBounds(20,30,200, 200);
	testShell.open();
	testShell.close();
	shell.setBounds(20,30,200, 200);
	shell.open();
}

@Test
public void test_dispose() {
	Shell newShell = new Shell();
	newShell.dispose();
}

@Test
public void test_forceActive() {
	shell.forceActive();
	/* can't assume listener is synchronously called when forceActive returned */
	/* assertTrue(":a:", shell.getDisplay().getActiveShell() == shell); */
}

@Test
public void test_getEnabled() {
	assertTrue(":a0:", shell.getEnabled());
	shell.setEnabled(false);
	assertTrue(":a:", !shell.getEnabled());
	shell.setEnabled(true);
	assertTrue(":b:", shell.getEnabled());
}

@Test
public void test_getImeInputMode() {
	int mode = shell.getImeInputMode();
	assertTrue(":a:", mode >= 0);
}

@Override
@Test
public void test_getLocation() {
	shell.setLocation(10,15);
	assertEquals(":a:", 10, shell.getLocation().x);
	assertEquals(":b:", 15, shell.getLocation().y);
}

@Override
@Test
public void test_getShell() {
	assertEquals(":a:", shell, shell.getShell());
	Shell shell_1 = new Shell(shell);
	assertEquals(":b:", shell_1, shell_1.getShell());
	shell_1.dispose();
}

@Test
public void test_getShells() {
	int num = shell.getShells().length;
	assertEquals(":a:", 1, num);
	Shell shell_1 = new Shell(shell);
	num = shell.getShells().length;
	assertEquals(":b:", 2, num);
	shell_1.dispose();
	num = shell.getShells().length;
	assertEquals(":c:", 1, num);
}

@Override
@Test
public void test_isEnabled() {
	assertTrue(":a:", shell.isEnabled());
	shell.setEnabled(false);
	assertTrue(":b:", !shell.isEnabled());
	if (SwtTestUtil.fCheckBogusTestCases)
		assertTrue(":b1:", !testShell.isEnabled());
	shell.setEnabled(true);
	assertTrue(":c:", shell.isEnabled());
	assertTrue(":a:", testShell.isEnabled());
	testShell.setEnabled(false);
	assertTrue(":b:", !testShell.isEnabled());
	testShell.setEnabled(true);
	assertTrue(":c:", testShell.isEnabled());
}

@Test
public void test_open() {
	shell.open();
}

@Test
public void test_setActive() {
	if (SwtTestUtil.isGTK || SwtTestUtil.isCocoa) {
		//TODO Fix GTK and Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out.println("Excluded test_setActive(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Shell))");
		}
		return;
	}
	/* Create shell2 and make it active. */
	Shell shell2 = new Shell();
	shell2.setText("Shell2");
	shell2.open();

	/* Test setActive for visible shell. */
	shell.setVisible(true);
	shell.setActive();
	assertTrue("visible shell was not made active", shell.getDisplay().getActiveShell() == shell);

	/* Test setActive for visible dialog shell. */
	shell2.setActive();
	testShell.setBounds(shell.getBounds());
	testShell.setVisible(true);
	testShell.setActive();
	assertTrue("visible dialog shell was not made active", testShell.getDisplay().getActiveShell() == testShell);

	/* Test setActive for non-visible shell. */
	shell2.setActive();
	shell.setVisible(false);
	shell.setActive();
	shell2.setText("Shell2: Not active");
	assertTrue("non-visible shell was made active", shell.getDisplay().getActiveShell() != shell);

	/* Test setActive for non-visible dialog shell. */
	shell2.setActive();
	testShell.setVisible(false);
	testShell.setActive();
	shell2.setText("Shell2: Not active");
	assertTrue("non-visible dialog shell was made active", testShell.getDisplay().getActiveShell() != testShell);

	shell2.dispose();
}

@Override
@Test
public void test_setEnabledZ() {
	// tested in getEnabled method
}

@Test
public void test_setImeInputModeI() {
	shell.setImeInputMode(SWT.NONE);
	assertEquals(":a:", SWT.NONE, shell.getImeInputMode());
}

@Override
@Test
public void test_setVisibleZ() {
	shell.setVisible(false);
	assertTrue(":a:", !shell.isVisible());
	shell.setVisible(true);
	assertTrue(":b:", shell.isVisible());
}


/* custom */
@Override
@Test
public void test_getParent () {
	// overriding Control.test_getParent
	assertNull(shell.getParent());
	assertEquals(shell, testShell.getParent());
}

@Test
public void test_getStyle() {
	// overriding Widget.test_getStyle
	assertTrue("testShell not modeless", (testShell.getStyle () & SWT.MODELESS) == SWT.MODELESS);
	int[] cases = {SWT.MODELESS, SWT.PRIMARY_MODAL, SWT.APPLICATION_MODAL, SWT.SYSTEM_MODAL};
	for (int i = 0; i < cases.length; i++) {
		Shell testShell2 = new Shell(shell, cases[i]);
		assertTrue("shell " + i, (testShell2.getStyle () & cases[i]) == cases[i]);
		testShell2.dispose();
	}
}

@Override
@Test
public void test_isVisible() {
	// overriding Control.test_isVisible
	testShell.setVisible(true);
	assertTrue(testShell.isVisible());
	shell.setVisible(true);
	assertTrue(shell.isVisible());

	testShell.setVisible(true);
	shell.setVisible(true);
	assertTrue("shell.isVisible() a:", shell.isVisible());
	shell.setVisible(false);
	assertTrue("shell.isVisible() b:", !shell.isVisible());
	if (SwtTestUtil.fCheckBogusTestCases)
		assertTrue("testShell.isVisible() c:", !testShell.isVisible());
}

@Override
@Test
public void test_setBoundsIIII() {
	// overridden from Control because Shells have a minimum size
}

@Override
@Test
public void test_setBoundsLorg_eclipse_swt_graphics_Rectangle() {
	// overridden from Control because Shells have a minimum size
//	/* windows */
//	/* note that there is a minimum size for a shell, this test will fail if p1.x < 112 or p1.y < 27 */
//	/* note that there is a maximum size for a shell, this test will fail if p1.x > 1292 or p1.y > 1036 */
//	if (SwtTestUtil.isWindows) {
//		Point p1 = new Point(112, 27);
//		Rectangle r1 = new Rectangle(20, 30, p1.x, p1.y);
//		Rectangle r2;
//		for (int i = 0; i < 11; i++) {
//			testShell.setBounds(r1);
//			r2 = testShell.getBounds();
//			assert("child shell iteration " + i + " set=" + r1 + " get=" + r2, r1.equals(r2));
//			r1.width += 100;
//			r1.height += 100;
//		}
//		r1 = new Rectangle(20, 30, p1.x, p1.y);
//		for (int i = 0; i < 11; i++) {
//			shell.setBounds(r1);
//			r2 = shell.getBounds();
//			assert("parent shell iteration " + i + " set=" + r1 + " get=" + r2, r1.equals(r2));
//			r1.width += 100;
//			r1.height += 100;
//		}
//	}
}

/**
 * Regression test for Bug 436841 - [GTK3] FocusOut/In and Activate/Deactivate
 * events when opening context menu. Only applicable on GTK x11.
 */
@Test
public void test_activateEventSend() {
	if (SwtTestUtil.isGTK && SwtTestUtil.isX11) {
		Shell testShell = new Shell(shell, SWT.SHELL_TRIM);
		testShell.addListener(SWT.Activate, e -> {
			listenerCalled = true;
			});
		testShell.open();
		int[] styles = {SWT.ON_TOP, SWT.APPLICATION_MODAL, SWT.PRIMARY_MODAL, SWT.SYSTEM_MODAL, SWT.NO_TRIM, SWT.BORDER, SWT.SHELL_TRIM};
		for (int style : styles) {
			Shell childShell = new Shell(testShell, style);
			listenerCalled = false;
			childShell.open();
			childShell.dispose();
			assertTrue(listenerCalled);
		}
	}
}

/**
 * Regression test for Bug 445900: [GTK] Shell#computeTrim(..) wrong for
 * first invisible shell (editor hovers jump when enriched).
 *
 * Disabled on Wayland as there is no absolute positioning.
 */
@Test
public void test_setBounds() throws Exception {
	if (SwtTestUtil.isX11) {
		Rectangle bounds = new Rectangle(100, 200, 200, 200);
		Rectangle bounds2 = new Rectangle(150, 250, 250, 250);

		StringBuilder log = new StringBuilder();
		int[] styles = { SWT.NO_TRIM, SWT.BORDER, SWT.RESIZE, SWT.TITLE | SWT.BORDER, SWT.TITLE | SWT.RESIZE, SWT.TITLE };
		for (int i = 0; i < styles.length; i++) {
			Shell testShell = new Shell(shell, styles[i]);
			try {
				testShell.setBounds(bounds);
				logUnlessEquals(log, i + ".1: style 0x" + Integer.toHexString(styles[i]), bounds, testShell.getBounds());

				testShell.open();
				logUnlessEquals(IS_GTK_BUG_445900 ? System.out : log, i + ".2: style 0x" + Integer.toHexString(styles[i]), bounds, testShell.getBounds());

				testShell.setBounds(bounds);
				logUnlessEquals(log, i + ".3: style 0x" + Integer.toHexString(styles[i]), bounds, testShell.getBounds());

				testShell.setBounds(bounds2);
				logUnlessEquals(log, i + ".4: style 0x" + Integer.toHexString(styles[i]), bounds2, testShell.getBounds());
			} finally {
				testShell.dispose();
			}
		}
		if (log.length() > 0) {
			Assert.fail(log.toString());
		}
	}
}

private void logUnlessEquals(Appendable log, String message, Rectangle expected, Rectangle actual) throws IOException {
	if (!expected.equals(actual)) {
			log.append(message).append("; expected: ").append(expected.toString())
					.append(", but was: ").append(actual.toString()).append("\n");
	}
}

//TODO This test was not hooked for running with the runTest override. It fails on GTK/Cocoa. Investigate.
public void a_test_setRegion() {
	Region region = new Region();
	region.add(new Rectangle(10, 20, 100, 200));
	// test shell without style SWT.NO_TRIM
	assertNull(":a:", shell.getRegion());
	shell.setRegion(region);
	assertNull(":b:", shell.getRegion());
	shell.setRegion(null);
	assertNull(":c:", shell.getRegion());
	// test shell with style SWT.NO_TRIM
	Display display = shell.getDisplay();
	Shell shell2 = new Shell(display, SWT.NO_TRIM);
	assertNull(":d:", shell2.getRegion());
	shell2.setRegion(region);
	assertEquals(":e:", region.handle, shell2.getRegion().handle);
	region.dispose();
	assertTrue(":f:", shell2.getRegion().isDisposed());
	shell2.setRegion(null);
	assertNull(":g:", shell2.getRegion());
}
@Override
@Test
public void test_setSizeII() {
	/* windows */
	/* note that there is a minimum size for a shell, this test will fail if p1.x < 112 or p1.y < 27 */
	/* note that there is a maximum size for a shell, this test will fail if p1.x > 1292 or p1.y > 1036 */
	if (SwtTestUtil.isWindows) {
		Point newSize = new Point(112, 27);
		for (int i = 0; i < 10; i++) {
			testShell.setSize(newSize.x, newSize.y);
			assertEquals(newSize, testShell.getSize());
			newSize.x += 100;
			newSize.y += 100;
		}
		newSize = new Point(1292, 1036);
		for (int i = 0; i < 10; i++) {
			testShell.setSize(newSize.x, newSize.y);
			assertEquals(newSize, testShell.getSize());
			newSize.x -= 100;
			newSize.y -= 100;
		}
	}

}

@Test
public void test_setSizeCustomResize() {
	/*
	 * Bug 319612: test the case where Shells with SWT.RESIZE and SWT.ON_TOP
	 * were shrinking in size. Only applicable to GTK.
	 */
	if (SwtTestUtil.isGTK) {
		Shell testShell = new Shell (shell, SWT.RESIZE | SWT.ON_TOP);
		Point size = new Point (200, 200);
		testShell.setSize(size);
		testShell.open();
		assertEquals(size, testShell.getSize());
		testShell.dispose();
	}
}

@Override
@Test
public void test_setSizeLorg_eclipse_swt_graphics_Point() {
	/* windows */
	/* note that there is a minimum size for a shell, this test will fail if p1.x < 112 or p1.y < 27 */
	/* note that there is a maximum size for a shell, this test will fail if p1.x > 1292 or p1.y > 1036 */
	if (SwtTestUtil.isWindows) {
		Point newSize = new Point(112, 27);
		for (int i = 0; i < 10; i++) {
			testShell.setSize(newSize);
			assertEquals(newSize, testShell.getSize());
			newSize.x += 100;
			newSize.y += 100;
		}
		newSize = new Point(1292, 1036);
		for (int i = 0; i < 10; i++) {
			testShell.setSize(newSize);
			assertEquals(newSize, testShell.getSize());
			newSize.x -= 100;
			newSize.y -= 100;
		}
	}
}

Shell testShell;

private void createShell() {
	tearDown();
	shell = new Shell();
	testShell = new Shell(shell, SWT.DIALOG_TRIM | SWT.MIN);
	testShell.setSize(100,300);
	testShell.setText("Shell");
	testShell.setLayout(new FillLayout());
	setWidget(testShell);

}

@Test
public void test_consistency_Open() {
	if (SwtTestUtil.fTestConsistency) {
		createShell();
		final Display display = shell.getDisplay();
		List<String> events = new ArrayList<>();
		String[] temp = hookExpectedEvents(testShell, getTestName(), events);
		shell.pack();
		shell.open();
		testShell.pack();
		testShell.open();
		new Thread() {
			@Override
			public void run() {
				display.asyncExec(new Thread() {
					@Override
					public void run() {
						shell.dispose();
					}
				});
		}}.start();

		while(!shell.isDisposed()) {
			if(!display.readAndDispatch()) display.sleep();
		}
		setUp();
		String[] results = events.toArray(new String[events.size()]);
		assertArrayEquals(getTestName() + " event ordering", temp, results);
	}
}

@Test
public void test_consistency_Iconify() {
	createShell();
	consistencyEvent(1, 0, 0, 0, ConsistencyUtility.SHELL_ICONIFY, null, false);
}

@Test
public void test_consistency_Close() {
	createShell();
	consistencyPrePackShell();
	consistencyEvent(0, SWT.ALT, 0, SWT.F4, ConsistencyUtility.DOUBLE_KEY_PRESS);
	createShell();
}

@Test
public void test_consistency_Dispose() {
	createShell();

	final Button button = new Button(testShell, SWT.PUSH);
	button.setText("dispose");
	button.addSelectionListener( new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent se) {
			button.dispose();
			testShell.dispose();
		}
	});
	List<String> events = new ArrayList<>();
	consistencyPrePackShell(testShell);
	Point pt = button.getLocation();
	consistencyEvent(pt.x, pt.y, 1, 0, ConsistencyUtility.MOUSE_CLICK, events);
	createShell();
}

@Test
public void test_setAlpha() {
	createShell();
	testShell.setAlpha(128);
	int alpha = testShell.getAlpha();
	if (SwtTestUtil.isGTK && alpha == 255) {
		System.out.println("Test_org_eclipse_swt_widgets_Shell.test_setAlpha(): expected 128, but was 255. "
				+ "Probably missing window manager functionality, see bug 498208.");
	} else {
		assertEquals(128, alpha);
	}
	testShell.setAlpha(255);
	assertEquals(255, testShell.getAlpha());
}

@Test // see bug#520488
public void test_childDisposesParent () {
	createShell();
	Shell root = new Shell(testShell, SWT.SHELL_TRIM);
	Shell child = new Shell(root, SWT.SHELL_TRIM);
	child.addListener(SWT.Dispose, e -> root.dispose());
	root.dispose();
}

@Test
public void test_bug558652_scrollBarNPE() {
	createShell();
	int[] styles = new int[] { SWT.NONE, SWT.DIALOG_TRIM, SWT.SHELL_TRIM };
	for (int style : styles) {
		Shell shell = new Shell(testShell, style);
		assertNull(shell.getVerticalBar());
		assertNull(shell.getHorizontalBar());
		shell.dispose();
		shell = new Shell(testShell, style | SWT.V_SCROLL);
		assertNotNull(shell.getVerticalBar());
		assertNull(shell.getHorizontalBar());
		shell.dispose();
		shell = new Shell(testShell, style | SWT.H_SCROLL);
		assertNull(shell.getVerticalBar());
		assertNotNull(shell.getHorizontalBar());
		shell.dispose();
		shell = new Shell(testShell, style | SWT.V_SCROLL | SWT.H_SCROLL);
		assertNotNull(shell.getVerticalBar());
		assertNotNull(shell.getHorizontalBar());
		shell.dispose();
	}
}

@Test
public void test_Issue450_NoShellActivateOnSetFocus() {
	final String key = "org.eclipse.swt.internal.activateShellOnForceFocus";
	final String prevKey = System.getProperty(key, null);
	System.setProperty(key, "false");
	try {
		Display display = shell.getDisplay();

		// First shell
		final Shell shell1 = new Shell(shell);
		shell1.setText("shell1");
		shell1.setLayout(new GridLayout()); // without layout the text controls will have zero size
		final Text text11 = new Text(shell1, SWT.BORDER | SWT.SINGLE);
		final Text text12 = new Text(shell1, SWT.BORDER | SWT.SINGLE);

		// Second shell
		final Shell shell2 = new Shell(shell);
		shell2.setText("shell2");
		shell2.setLayout(new GridLayout()); // without layout the text controls will have zero size
		final Text text21 = new Text(shell2, SWT.BORDER | SWT.SINGLE);
		final Text text22 = new Text(shell2, SWT.BORDER | SWT.SINGLE);

		// System.out.println("open 1st shell");
		SwtTestUtil.waitShellActivate(shell1::open, shell1);
		assertSame("expecting the 1st shell to be activated", display.getActiveShell(), shell1);
		assertTrue("expecting the 1st text field to be focused in the 1st shell", text11.isFocusControl());

		// System.out.println("open 2nd shell");
		SwtTestUtil.waitShellActivate(shell2::open, shell2);
		assertSame("expecting the 2nd shell to be activated", display.getActiveShell(), shell2);
		assertTrue("expecting the 1st text field in 2nd shell to be focused", text21.isFocusControl());

		// System.out.println("setting focus to 2nd text field in 1st (unactivated) shell");
		text12.setFocus();
		assertSame("expecting the 2nd shell to remain activated", display.getActiveShell(), shell2);

		// System.out.println("activating 1st shell");
		// Fails here for Linux (21.04+ or Manjaro starting ~July 2023)? Check Bug 575712
		// workaround: set env-var GDK_BACKEND=x11
		SwtTestUtil.waitShellActivate(shell1::setActive, shell1);
		assertSame("expecting the 1st shell to be activated", display.getActiveShell(), shell1);
		assertTrue("expecting the the 1st shell to have remembered the previous setFocus and with the shell activation setting it to the 2nd text field", text12.isFocusControl());

		// System.out.println("disposing 1st shell");
		SwtTestUtil.waitShellActivate(
			() -> {
				shell1.dispose();
				// on macOS, disposing a Shell sometimes doesn't activate the other Shell.
				// Not reproducible on my machine. Not reproducible on GitHub builder.
				// Only seems to occur on Eclipse builder, not sure what's different there.
				// Anyways, this is out of scope of this test, so just ask to activate.
				if (SwtTestUtil.isCocoa) shell2.forceActive();
			},
			shell2
		);
		assertSame("expecting the 2nd shell to be activated after the 1st, active has been disposed", display.getActiveShell(), shell2);
		assertTrue("expecting the 1st text field in the 2nd shell to still have the focus because it hasn't been changed", text21.isFocusControl());

		// System.out.println("setting the focus to the 2nd text field");
		text22.setFocus();
		assertSame("expecting the 2nd shell to remain activated", display.getActiveShell(), shell2);
		assertTrue("expecting the 2nd text field to have received the focus", text22.isFocusControl());

		// System.out.println("disposing the 2nd shell, too");
		shell2.dispose();
	}
	finally {
		if (prevKey != null) {
			System.setProperty(key, prevKey);
		}
		else {
			System.getProperties().remove(key);
		}
	}
}

@Test
public void test_Issue828_CheckShellBounds() {
	Composite composite = new Composite(shell, SWT.NONE);
	shell.setLayout(new FillLayout());
	shell.setSize(400, 500);
	shell.open();

	// Shell should match the expected size of (400, 500)
	Rectangle shellBounds = shell.getBounds();
	assertEquals("shell width doesn't match expectation", shellBounds.width, 400);
	assertEquals("shell height doesn't match expectation", shellBounds.height, 500);

	Point shellSize = shell.getSize();
	assertEquals("shell width doesn't match expectation", shellSize.x, 400);
	assertEquals("shell height doesn't match expectation", shellSize.y, 500);

	// Usable client area should be smaller (due to window decorations)
	Rectangle shellClientArea = shell.getClientArea();
	assertTrue("client area wider larger than expected", shellClientArea.width < 400);
	assertTrue("client area higher larger than expected", shellClientArea.height < 500);

	// The composite should take up the whole client area
	Rectangle compositeBounds = composite.getBounds();
	assertEquals("composite wider larger than expected", shellClientArea.width, compositeBounds.width);
	assertEquals("composite higher larger than expected", shellClientArea.height, compositeBounds.height);
}
}

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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Shell
 *
 * @see org.eclipse.swt.widgets.Shell
 */
public class Test_org_eclipse_swt_widgets_Shell extends Test_org_eclipse_swt_widgets_Decorations {

public Test_org_eclipse_swt_widgets_Shell(String name) {
	super(name);
}

@Override
protected void setUp() {
	super.setUp();
	testShell = new Shell(shell, SWT.NULL);
	setWidget(shell);
	assertTrue(testShell.getParent() == shell);
}

public void test_Constructor() {
	Shell newShell = new Shell();
	assertNotNull("a: ", newShell.getDisplay());
	newShell.dispose();
}

public void test_ConstructorI() {
	/* this should test various combinations of STYLE bits, for now just test individual bits */
	int[] cases = {SWT.NO_TRIM, SWT.RESIZE, SWT.TITLE, SWT.CLOSE, SWT.MENU, SWT.MIN, SWT.BORDER, 
				   SWT.CLIP_CHILDREN, SWT.CLIP_SIBLINGS, SWT.ON_TOP, SWT.FLAT, SWT.SMOOTH};
	Shell newShell;
	for (int i = 0; i < cases.length; i++) {
		newShell = new Shell(cases[i]);
		assertTrue("a " +i, newShell.getDisplay() == shell.getDisplay());
		newShell.dispose();
	}
}

public void test_ConstructorLorg_eclipse_swt_widgets_Display() {
	Display display = shell.getDisplay();
	Shell newShell = new Shell(display);
	assertTrue("a: ", newShell.getDisplay() == display);
	newShell.dispose();
}

public void test_ConstructorLorg_eclipse_swt_widgets_DisplayI() {
	int[] cases = {SWT.NO_TRIM, SWT.RESIZE, SWT.TITLE, SWT.CLOSE, SWT.MENU, SWT.MIN, SWT.BORDER, 
				   SWT.CLIP_CHILDREN, SWT.CLIP_SIBLINGS, SWT.ON_TOP, SWT.FLAT, SWT.SMOOTH};
	Shell newShell;
	Display display = shell.getDisplay();
	for (int i = 0; i < cases.length; i++) {
		newShell = new Shell(display, cases[i]);
		assertTrue("a " +i, newShell.getDisplay() == shell.getDisplay());
		newShell.dispose();
	}
}

public void test_ConstructorLorg_eclipse_swt_widgets_Shell() {
	Shell newShell = new Shell(shell);
	assertTrue("a: ", newShell.getParent() == shell);
	newShell.dispose();
}

public void test_ConstructorLorg_eclipse_swt_widgets_ShellI() {
	/* this should test various combinations of STYLE bits, for now just test individual bits */
	int[] cases = {SWT.NO_TRIM, SWT.RESIZE, SWT.TITLE, SWT.CLOSE, SWT.MENU, SWT.MIN, SWT.BORDER, 
				   SWT.CLIP_CHILDREN, SWT.CLIP_SIBLINGS, SWT.ON_TOP, SWT.FLAT, SWT.SMOOTH};
	Shell newShell;
	for (int i = 0; i < cases.length; i++) {
		newShell = new Shell(shell, cases[i]);
		assertTrue("a " +i, newShell.getParent() == shell);
		newShell.dispose();
	}
}

public void test_addShellListenerLorg_eclipse_swt_events_ShellListener() {
	listenerCalled = false;
	boolean exceptionThrown = false;
	ShellListener listener = new ShellListener() {
		public void shellActivated(ShellEvent e) {
			listenerCalled = true;
		}
		public void shellClosed(ShellEvent e) {
		}
		public void shellDeactivated(ShellEvent e) {
		}
		public void shellDeiconified(ShellEvent e) {
		}
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

public void test_close() {

	// bogus line that 'enabled' gpfs
	//	Shell newShell = new Shell();
	testShell.setBounds(20,30,200, 200);
	testShell.open();
	testShell.close();
	shell.setBounds(20,30,200, 200);
	shell.open();
}

public void test_dispose() {
	Shell newShell = new Shell();
	newShell.dispose();
}

public void test_forceActive() {
	shell.forceActive();
	/* can't assume listener is synchronously called when forceActive returned */
	/* assertTrue(":a:", shell.getDisplay().getActiveShell() == shell); */
}

public void test_getEnabled() {
	assertTrue(":a0:", shell.getEnabled());
	shell.setEnabled(false);
	assertTrue(":a:", !shell.getEnabled());
	shell.setEnabled(true);
	assertTrue(":b:", shell.getEnabled());
}

public void test_getImeInputMode() {
	int mode = shell.getImeInputMode();
	assertTrue(":a:", mode >= 0);
}

@Override
public void test_getLocation() {
	shell.setLocation(10,15);
	assertTrue(":a:", shell.getLocation().x == 10);
	assertTrue(":b:", shell.getLocation().y == 15);
}

@Override
public void test_getShell() {
	assertTrue(":a:", shell.getShell()==shell);
	Shell shell_1 = new Shell(shell);
	assertTrue(":b:", shell_1.getShell()== shell_1);
	shell_1.dispose();
}

public void test_getShells() {
	int num = shell.getShells().length;
	assertTrue(":a:", num == 1);
	Shell shell_1 = new Shell(shell);
	num = shell.getShells().length;
	assertTrue(":a:", num == 2);
	shell_1.dispose();
}

@Override
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

public void test_open() {
	shell.open();
}


public void test_setActive() {
	if (SwtTestUtil.isGTK) {
		//TODO Fix GTK failure.
		if (SwtTestUtil.verbose) {
			System.out.println("Excluded test_setActive(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Shell))");
		}
		return;
	}
	/* Create shell2 and make it active. */
	Shell shell2 = new Shell();
	shell2.open();
	
	/* Test setActive for visible shell. */
	shell.setVisible(true);
	shell.setActive();
	assertTrue("visible shell was not made active", shell.getDisplay().getActiveShell() == shell);
	
	/* Test setActive for visible dialog shell. */
	shell2.setActive();
	testShell.setVisible(true);
	testShell.setActive();
	assertTrue("visible dialog shell was not made active", testShell.getDisplay().getActiveShell() == testShell);
	
	/* Test setActive for non-visible shell. */
	shell2.setActive();
	shell.setVisible(false);
	shell.setActive();
	assertTrue("non-visible shell was made active", shell.getDisplay().getActiveShell() != shell);

	/* Test setActive for non-visible dialog shell. */
	shell2.setActive();
	testShell.setVisible(false);
	testShell.setActive();
	assertTrue("non-visible dialog shell was made active", testShell.getDisplay().getActiveShell() != testShell);
	
	shell2.dispose();
}

@Override
public void test_setEnabledZ() {
	// tested in getEnabled method
}

public void test_setImeInputModeI() {
	shell.setImeInputMode(SWT.NONE);
	assertTrue(":a:", shell.getImeInputMode() == SWT.NONE);
}

@Override
public void test_setVisibleZ() {
	shell.setVisible(false);
	assertTrue(":a:", !shell.isVisible());
	shell.setVisible(true);
	assertTrue(":b:", shell.isVisible());
}


/* custom */
@Override
public void test_getParent () {
	// overriding Control.test_getParent
	assertTrue(shell.getParent()==null);
	assertTrue(testShell.getParent() == shell);
}

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
public void test_setBoundsIIII() {
	// overridden from Control because Shells have a minimum size
}

@Override
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
//TODO This test was not hooked for running with the runTest override. It fails on GTK/Cocoa. Investigate.
public void a_test_setRegion() {
	Region region = new Region();
	region.add(new Rectangle(10, 20, 100, 200));
	// test shell without style SWT.NO_TRIM
	assertTrue(":a:", shell.getRegion() == null);
	shell.setRegion(region);
	assertTrue(":b:", shell.getRegion() == null);
	shell.setRegion(null);
	assertTrue(":c:", shell.getRegion() == null);
	// test shell with style SWT.NO_TRIM
	Display display = shell.getDisplay();
	Shell shell2 = new Shell(display, SWT.NO_TRIM);
	assertTrue(":d:", shell2.getRegion() == null);
	shell2.setRegion(region);
	assertTrue(":e:", shell2.getRegion().handle == region.handle);
	region.dispose();
	assertTrue(":f:", shell2.getRegion().isDisposed());
	shell2.setRegion(null);
	assertTrue(":g:", shell2.getRegion() == null);
}
@Override
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

@Override
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

public void test_consistency_Open() {
	if (SwtTestUtil.fTestConsistency) {
	    createShell();
	    final Display display = shell.getDisplay();
	    Vector<String> events = new Vector<String>();
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
	    String[] results = new String[events.size()];
	    events.copyInto(results);
	    assertArrayEquals(getTestName() + " event ordering", temp, results);
	}
}

public void test_consistency_Iconify() {
    createShell();
    consistencyEvent(1, 0, 0, 0, ConsistencyUtility.SHELL_ICONIFY, null, false);
}

public void test_consistency_Close() {
    createShell();
    consistencyPrePackShell();
    consistencyEvent(0, SWT.ALT, 0, SWT.F4, ConsistencyUtility.DOUBLE_KEY_PRESS);
    createShell();
}

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
    Vector<String> events = new Vector<String>();
    consistencyPrePackShell(testShell);
    Point pt = button.getLocation();
    consistencyEvent(pt.x, pt.y, 1, 0, ConsistencyUtility.MOUSE_CLICK, events);
    createShell();
}
}

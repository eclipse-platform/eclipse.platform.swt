/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 */


package org.eclipse.swt.tests.manualJUnit;

import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

/**
 * Some tests can't be automated and have to be tested manually by visual inspection.
 *
 * For example visual glitches in Tree/Table editing (aka cheese).
 *
 * MJ_root is designed to make this a fast(er) process by running multiple tests in sequence
 * and allowing human tester to quickly validate via keyboard input:
 * F1 - Pass
 * F2 - Fail
 * F3 - Skip
 * Esc - Quit the test sequence, no more tests are ran.
 *
 * Each test is independent. Notes:
 * - A new display is created for each one. (thus tests are a bit slow, but are more independent than creating/disposing shells).
 * - To pass a test programmatically, dispose the display.
 * - To fail a test programmatically, use fail()
 *
 * Shells are created by tests themselves, but 'mkShell(..)' is there for convenience.
 */
public class MJ_root {

	/** Default Shell values */
	final int SWIDTH = 1200;
	final int SHEIGHT = 800;

	Display display;
	@Before //each test.
	public void setUp() {
		display = new Display();
		display.addFilter(SWT.KeyDown, globalTestValidatorViaKeyboard);
	}

	@After
	public void tearDown() {
		if (!display.isDisposed()) { // pass = shell is disposed.
			display.dispose();
		}
	}

	/** Convenience method that sets the shell title to current test name + description */
	public Shell mkShell(String testInstructions) {
		Shell shell = new Shell(display);
		shell.setText(getCurrentTestName() + " -- " + testInstructions);
		return shell;
	}

	void mainLoop(Shell shell) {
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	Listener globalTestValidatorViaKeyboard = e -> {
		switch (e.keyCode) {
		case SWT.F1:
			System.out.println("TEST_Passed:" + getCurrentTestName());
			display.dispose();
			break;
		case SWT.F2:
			System.out.println("TEST_Failed:" + getCurrentTestName());
			fail();
			break;
		case SWT.F3:
			System.out.println("TEST_Skipped:" + getCurrentTestName());
			assumeTrue(false);
			break;
		case SWT.ESC:
			System.out.println("TEST_Quit on:" + getCurrentTestName());
			System.exit(0);
			break;
		}
	};

    @Rule
    public TestName testName = new TestName();
    private String getCurrentTestName() {
    	return testName.getMethodName ();
    }


    // Util functions.
    public void knownToBeBrokenGtk3(String msg) {
    	System.err.println(" info:" + getCurrentTestName() + " is known to be broken at the moment. Additional info: " + msg);
    }

    public void knownToBeBrokenGtk(String msg) {
    	System.err.println(" info:" + getCurrentTestName() + " is known to be broken at the moment. Additional info: " + msg);
    }

}

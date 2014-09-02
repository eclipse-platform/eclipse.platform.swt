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


import junit.framework.*;
import junit.textui.*;

/**
 * Suite for running most SWT test cases (all except for browser tests).
 */
public class AllNonBrowserTests extends TestSuite {

public static void main(String[] args) {
	TestRunner.run(suite());
}
public static Test suite() {
	return new AllNonBrowserTests();
}

public AllNonBrowserTests() {
	super(AllNonBrowserTests.class.getName());
	/* The logical order to run the tests in is:
	 * - SWT, SWTError, SWTException
	 * - Display
	 * - graphics classes
	 * - items and Caret, etc
	 * - widgets
	 * - dialogs
	 * - layout
	 * - custom widgets
	 * - printing and program
	 * - events
	 * - drag & drop
	 * - accessibility
	 * - OLE
	 * - browser
	 */
	addTestSuite(Test_org_eclipse_swt_SWT.class);
	addTestSuite(Test_org_eclipse_swt_SWTException.class);
	addTestSuite(Test_org_eclipse_swt_SWTError.class);

	/* NOTE: If the Display test suite is run, it must be run
	 * before any other tests that need a display (i.e. graphics
	 * or widget tests, etc). Otherwise, an InvalidThreadAccess
	 * exception will be thrown for each Display test.
	 */
	addTestSuite(Test_org_eclipse_swt_widgets_Display.class);

	addTest(AllGraphicsTests.suite());
	addTest(AllWidgetTests.suite());
	
	addTestSuite(Test_org_eclipse_swt_layout_GridData.class);

	addTestSuite(Test_org_eclipse_swt_events_ControlEvent.class);
	addTestSuite(Test_org_eclipse_swt_events_ModifyEvent.class);
	addTestSuite(Test_org_eclipse_swt_events_ArmEvent.class);
	addTestSuite(Test_org_eclipse_swt_events_ShellEvent.class);
	addTestSuite(Test_org_eclipse_swt_events_TypedEvent.class);
	addTestSuite(Test_org_eclipse_swt_events_PaintEvent.class);
	addTestSuite(Test_org_eclipse_swt_events_VerifyEvent.class);
	addTestSuite(Test_org_eclipse_swt_events_KeyEvent.class);
	addTestSuite(Test_org_eclipse_swt_events_TraverseEvent.class);
	addTestSuite(Test_org_eclipse_swt_events_DisposeEvent.class);
	addTestSuite(Test_org_eclipse_swt_events_SelectionEvent.class);
	addTestSuite(Test_org_eclipse_swt_events_HelpEvent.class);
	addTestSuite(Test_org_eclipse_swt_events_FocusEvent.class);
	addTestSuite(Test_org_eclipse_swt_events_MouseEvent.class);
	addTestSuite(Test_org_eclipse_swt_events_MenuEvent.class);
	addTestSuite(Test_org_eclipse_swt_events_TreeEvent.class);
	
	addTestSuite(Test_org_eclipse_swt_printing_PrintDialog.class);
	addTestSuite(Test_org_eclipse_swt_printing_PrinterData.class);
	addTestSuite(Test_org_eclipse_swt_printing_Printer.class);

	addTestSuite(Test_org_eclipse_swt_program_Program.class);

	addTestSuite(Test_org_eclipse_swt_accessibility_Accessible.class);
	addTestSuite(Test_org_eclipse_swt_accessibility_AccessibleControlEvent.class);
	addTestSuite(Test_org_eclipse_swt_accessibility_AccessibleEvent.class);

	// Don't run AllBrowserTests here; see AllTests.
}
}

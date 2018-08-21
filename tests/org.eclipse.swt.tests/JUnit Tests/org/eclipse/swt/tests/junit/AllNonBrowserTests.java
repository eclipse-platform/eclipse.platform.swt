/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
 *     Red Hat Inc. - Adapt to JUnit 4.
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Suite for running most SWT test cases (all except for browser tests).
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ Test_org_eclipse_swt_SWT.class, Test_org_eclipse_swt_SWTException.class,
		Test_org_eclipse_swt_SWTError.class, Test_org_eclipse_swt_widgets_Display.class, AllGraphicsTests.class,
		AllWidgetTests.class, Test_org_eclipse_swt_layout_GridData.class,
		Test_org_eclipse_swt_events_ControlEvent.class, Test_org_eclipse_swt_events_ModifyEvent.class,
		Test_org_eclipse_swt_events_ArmEvent.class, Test_org_eclipse_swt_events_ShellEvent.class,
		Test_org_eclipse_swt_events_TypedEvent.class, Test_org_eclipse_swt_events_PaintEvent.class,
		Test_org_eclipse_swt_events_VerifyEvent.class, Test_org_eclipse_swt_events_KeyEvent.class,
		Test_org_eclipse_swt_events_TraverseEvent.class, Test_org_eclipse_swt_events_DisposeEvent.class,
		Test_org_eclipse_swt_events_SelectionEvent.class, Test_org_eclipse_swt_events_HelpEvent.class,
		Test_org_eclipse_swt_events_FocusEvent.class, Test_org_eclipse_swt_events_MouseEvent.class,
		Test_org_eclipse_swt_events_MenuEvent.class, Test_org_eclipse_swt_events_TreeEvent.class,
		Test_org_eclipse_swt_layout_FormAttachment.class, Test_org_eclipse_swt_layout_GridData.class,
		Test_org_eclipse_swt_printing_PrintDialog.class, Test_org_eclipse_swt_printing_PrinterData.class,
		Test_org_eclipse_swt_printing_Printer.class, Test_org_eclipse_swt_program_Program.class,
		Test_org_eclipse_swt_accessibility_Accessible.class,
		Test_org_eclipse_swt_accessibility_AccessibleControlEvent.class,
		Test_org_eclipse_swt_accessibility_AccessibleEvent.class,
		Test_org_eclipse_swt_accessibility_AccessibleTextEvent.class })
public class AllNonBrowserTests {

	public static void main(String[] args) {
		JUnitCore.main(AllNonBrowserTests.class.getName());
	}

	/*
	 * The logical order to run the tests in is: - SWT, SWTError, SWTException -
	 * Display - graphics classes - items and Caret, etc - widgets - dialogs -
	 * layout - custom widgets - printing and program - events - drag & drop -
	 * accessibility - OLE - browser
	 */

	/*
	 * NOTE: If the Display test suite is run, it must be run before any other
	 * tests that need a display (i.e. graphics or widget tests, etc).
	 * Otherwise, an InvalidThreadAccess exception will be thrown for each
	 * Display test.
	 */

}

/*******************************************************************************
 * Copyright (c) 2000, 2025 IBM Corporation and others.
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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.tests.junit.AllNonBrowserTests.NonBrowserTestSuite;
import org.junit.platform.suite.api.AfterSuite;
import org.junit.platform.suite.api.BeforeSuite;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * Suite for running most SWT test cases (all except for browser tests).
 */
@NonBrowserTestSuite
public class AllNonBrowserTests {

	// Combine the test annotations with the test classes in a wrapper annotation
	// such that it can be reused for derived test suites
	@Suite(failIfNoTests = false)
	@SelectClasses({ //
			// Basic tests
			Test_org_eclipse_swt_SWT.class, //
			Test_org_eclipse_swt_SWTException.class, //
			Test_org_eclipse_swt_SWTError.class, //
			Test_org_eclipse_swt_widgets_Display.class, //
			// Groups of tests
			AllGraphicsTests.class, //
			AllWidgetTests.class, //
			// Rest of tests alphabetically
			DPIUtilTests.class, //
			JSVGRasterizerTest.class, //
			Test_org_eclipse_swt_accessibility_Accessible.class, //
			Test_org_eclipse_swt_accessibility_AccessibleControlEvent.class, //
			Test_org_eclipse_swt_accessibility_AccessibleEvent.class, //
			Test_org_eclipse_swt_accessibility_AccessibleTextEvent.class, //
			Test_org_eclipse_swt_dnd_ByteArrayTransfer.class, //
			Test_org_eclipse_swt_dnd_Clipboard.class, //
			Test_org_eclipse_swt_dnd_FileTransfer.class, //
			Test_org_eclipse_swt_dnd_HTMLTransfer.class, //
			Test_org_eclipse_swt_dnd_ImageTransfer.class, //
			Test_org_eclipse_swt_dnd_RTFTransfer.class, //
			Test_org_eclipse_swt_dnd_TextTransfer.class, //
			Test_org_eclipse_swt_dnd_URLTransfer.class, //
			Test_org_eclipse_swt_events_ArmEvent.class, //
			Test_org_eclipse_swt_events_ControlEvent.class, //
			Test_org_eclipse_swt_events_DisposeEvent.class, //
			Test_org_eclipse_swt_events_FocusEvent.class, //
			Test_org_eclipse_swt_events_HelpEvent.class, //
			Test_org_eclipse_swt_events_KeyEvent.class, //
			Test_org_eclipse_swt_events_MenuEvent.class, //
			Test_org_eclipse_swt_events_ModifyEvent.class, //
			Test_org_eclipse_swt_events_MouseEvent.class, //
			Test_org_eclipse_swt_events_PaintEvent.class, //
			Test_org_eclipse_swt_events_SelectionEvent.class, //
			Test_org_eclipse_swt_events_ShellEvent.class, //
			Test_org_eclipse_swt_events_TraverseEvent.class, //
			Test_org_eclipse_swt_events_TreeEvent.class, //
			Test_org_eclipse_swt_events_TypedEvent.class, //
			Test_org_eclipse_swt_events_VerifyEvent.class, //
			Test_org_eclipse_swt_internal_SVGRasterizer.class, //
			Test_org_eclipse_swt_layout_BorderLayout.class, //
			Test_org_eclipse_swt_layout_FormAttachment.class, //
			Test_org_eclipse_swt_layout_GridData.class, //
			Test_org_eclipse_swt_printing_PDFDocument.class, //
			Test_org_eclipse_swt_printing_PrintDialog.class, //
			Test_org_eclipse_swt_printing_Printer.class, //
			Test_org_eclipse_swt_printing_PrinterData.class, //
			Test_org_eclipse_swt_program_Program.class, //
	})
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@interface NonBrowserTestSuite  {
		//
	}

	private static List<Error> leakedResources;

	@BeforeSuite
	public static void beforeSuite() {
		// Set up ResourceTracked to detect any leaks
		leakedResources = new ArrayList<> ();
		Resource.setNonDisposeHandler (error -> {
			// Can't 'Assert.fail()' here, because Handler is called on
			// some other thread and Exception will simply be ignored.
			// Workaround: collect errors and report later.
			leakedResources.add (error);
		});
	}

	/*
	 * It would be easier to understand if errors here were reported
	 * through  a test and not through @AfterSuite, but this is a
	 * suite class and not a test class, so it can't have tests.
	 */
	@AfterSuite
	public static void afterSuite() {
		// Run GC in order do detect any outstanding leaks
		System.gc ();
		// And wait a bit to let Resource.ResourceTracker, that is
		// queued to another thread, do its job. It is unfortunate that
		// there is no simple better way to wait except waiting some
		// fixed timeout.
		try {
			Thread.sleep (100);
		} catch (InterruptedException e) {
			// Just ignore
		}

		for (Error leak : leakedResources) {
			leak.printStackTrace ();
		}

		if (0 != leakedResources.size ()) {
			// In order to help tools to filter fail reason out of entire log, throw the first leak.
			String hint = leakedResources.size () + " leaks found, the first is:";
			throw new AssertionError(hint, leakedResources.get (0));
		}
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

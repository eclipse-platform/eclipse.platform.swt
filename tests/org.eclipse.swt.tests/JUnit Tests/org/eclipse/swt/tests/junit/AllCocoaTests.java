/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
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
 * Suite for running all SWT test cases.
 */
public class AllCocoaTests extends TestSuite {

/**
 * Tests not run because they consistently fail
 */
static String[] excludeTests = {
	"test_getBoundsI(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_TableItem)",
	"test_getBoundsI(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_TreeItem)",
	"test_getBounds(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_TreeItem)",
	"test_getTopPixel(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Text)",
	"test_postLorg_eclipse_swt_widgets_Event(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Display)",
	
	//tests pass locally, but fail on the releng machine
	"test_copy(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Text)",
	"test_cut(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Text)",
	"test_paste(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Text)",
	"test_selectAll(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Text)",
	"test_copy(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Combo)",
	"test_cut(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Combo)",
	"test_paste(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Combo)",
	"test_copy(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_custom_CCombo)",
	"test_cut(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_custom_CCombo)",
	"test_paste(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_custom_CCombo)",
	"test_copy(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_custom_StyledText)",
	"test_cut(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_custom_StyledText)",
	"test_invokeActionI(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_custom_StyledText)",
	"test_paste(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_custom_StyledText)",
	
	// TextLayout bugs
	"test_getLineIndex(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_graphics_TextLayout)",
	"test_getLineBounds(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_graphics_TextLayout)",
	"test_getLocation(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_graphics_TextLayout)",
	"test_getNextOffset(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_graphics_TextLayout)",
	"test_getNextOffset2(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_graphics_TextLayout)",
	"test_getOffset(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_graphics_TextLayout)",
	"test_getSegmentsChars(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_graphics_TextLayout)",
};

static boolean isExcluded(String name) {
	for (int i = 0; i < excludeTests.length; i++) {
		if (name.equals(excludeTests[i])) return true;
	}
	return false;
}

public static void main(String[] args) {
	SwtTestCase.unimplementedMethods = 0;
	TestRunner.run(suite());
	if (SwtTestCase.unimplementedMethods > 0) {
		System.out.println("\nCalls to warnUnimpl: " + SwtTestCase.unimplementedMethods);
		System.out.println("\nExcluded Tests: " + excludeTests.length);
	}
}

public static Test suite() {
	TestSuite fullSuite = (TestSuite)AllTests.suite();
	TestSuite filteredSuite = new TestSuite();
	for (int i = 0; i < fullSuite.testCount(); i++) {
		Test candidateTest = fullSuite.testAt(i);
		if (candidateTest instanceof TestSuite) {
			TestSuite suite = (TestSuite)candidateTest;
			for (int j = 0; j < suite.testCount(); j++) {
				Test test = suite.testAt(j);
				if (!isExcluded(test.toString())) filteredSuite.addTest(test);				
			}
		}
	}
	return filteredSuite;
}
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit.performance;


import junit.framework.*;
import junit.textui.*;

/**
 * Suite for running SWT performance test cases.
 */
public class PerformanceTests extends TestSuite {

public static void main(String[] args) {
	TestRunner.run(suite());
}
public static Test suite() {
	return new PerformanceTests();
}

public PerformanceTests() {
	super();

	/* NOTE: If the Display test suite is run, it must be run
	 * before any other tests that need a display (i.e. graphics
	 * or widget tests, etc). Otherwise, an InvalidThreadAccess
	 * exception will be thrown for each Display test.
	 */

	addTest(Test_situational.suite());
	addTest(Test_org_eclipse_swt_graphics_Font.suite());
	addTest(Test_org_eclipse_swt_graphics_Region.suite());
	addTest(Test_org_eclipse_swt_graphics_Image.suite());
	addTest(Test_org_eclipse_swt_graphics_GC.suite());
	addTest(Test_org_eclipse_swt_graphics_ImageData.suite());
	addTest(Test_org_eclipse_swt_graphics_ImageLoader.suite());
	addTest(Test_Button.suite());
}
}

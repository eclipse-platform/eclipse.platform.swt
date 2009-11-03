/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
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
 * Suite for testing all of the graphics test cases.
 */
public class AllGraphicsTests {
public static void main(String[] args) {
	TestRunner.run (suite());
}
public static Test suite() {
	TestSuite suite = new TestSuite();

	suite.addTest(Test_org_eclipse_swt_graphics_Color.suite());
	suite.addTest(Test_org_eclipse_swt_graphics_Cursor.suite());
	suite.addTest(Test_org_eclipse_swt_graphics_DeviceData.suite());
	suite.addTest(Test_org_eclipse_swt_graphics_Font.suite());
	suite.addTest(Test_org_eclipse_swt_graphics_FontData.suite());
	suite.addTest(Test_org_eclipse_swt_graphics_FontMetrics.suite());
	suite.addTest(Test_org_eclipse_swt_graphics_GC.suite());
	suite.addTest(Test_org_eclipse_swt_graphics_GCData.suite());
	suite.addTest(Test_org_eclipse_swt_graphics_Image.suite());
	suite.addTest(Test_org_eclipse_swt_graphics_ImageData.suite());
	suite.addTest(Test_org_eclipse_swt_graphics_PaletteData.suite());
	suite.addTest(Test_org_eclipse_swt_graphics_Point.suite());
	suite.addTest(Test_org_eclipse_swt_graphics_Rectangle.suite());
	suite.addTest(Test_org_eclipse_swt_graphics_Region.suite());
	suite.addTest(Test_org_eclipse_swt_graphics_RGB.suite());
	suite.addTest(Test_org_eclipse_swt_graphics_TextLayout.suite());
	
	suite.addTest(Test_org_eclipse_swt_graphics_ImageLoader.suite());
	suite.addTest(Test_org_eclipse_swt_graphics_ImageLoaderEvent.suite());

	return suite;
}
}

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

	suite.addTestSuite(Test_org_eclipse_swt_graphics_Color.class);
	suite.addTestSuite(Test_org_eclipse_swt_graphics_Cursor.class);
	suite.addTestSuite(Test_org_eclipse_swt_graphics_DeviceData.class);
	suite.addTestSuite(Test_org_eclipse_swt_graphics_Font.class);
	suite.addTestSuite(Test_org_eclipse_swt_graphics_FontData.class);
	suite.addTestSuite(Test_org_eclipse_swt_graphics_FontMetrics.class);
	suite.addTestSuite(Test_org_eclipse_swt_graphics_GC.class);
	suite.addTestSuite(Test_org_eclipse_swt_graphics_Image.class);
	suite.addTestSuite(Test_org_eclipse_swt_graphics_ImageData.class);
	suite.addTestSuite(Test_org_eclipse_swt_graphics_PaletteData.class);
	suite.addTestSuite(Test_org_eclipse_swt_graphics_Point.class);
	suite.addTestSuite(Test_org_eclipse_swt_graphics_Rectangle.class);
	suite.addTestSuite(Test_org_eclipse_swt_graphics_Region.class);
	suite.addTestSuite(Test_org_eclipse_swt_graphics_RGB.class);
	suite.addTestSuite(Test_org_eclipse_swt_graphics_TextLayout.class);
	
	suite.addTestSuite(Test_org_eclipse_swt_graphics_ImageLoader.class);
	suite.addTestSuite(Test_org_eclipse_swt_graphics_ImageLoaderEvent.class);

	return suite;
}
}

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
 * Suite for testing widgets that have been emulated on Motif.
 */
public class EmulatedWidgetsTests {

public static void main(String[] args) {
	TestRunner.run (suite());
}
public static Test suite() {
	TestSuite suite = new TestSuite();

	suite.addTest(Test_org_eclipse_swt_widgets_ColorDialog.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_FontDialog.suite());	
	suite.addTest(Test_org_eclipse_swt_widgets_TabFolder.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_TabItem.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_Table.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_TableItem.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_TableColumn.suite());	
	suite.addTest(Test_org_eclipse_swt_widgets_Tree.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_TreeItem.suite());
		
	return suite;
}
}

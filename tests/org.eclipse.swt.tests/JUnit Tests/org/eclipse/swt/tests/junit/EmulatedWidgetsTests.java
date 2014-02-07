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

	suite.addTestSuite(Test_org_eclipse_swt_widgets_ColorDialog.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_FontDialog.class);	
	suite.addTestSuite(Test_org_eclipse_swt_widgets_TabFolder.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_TabItem.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_Table.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_TableItem.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_TableColumn.class);	
	suite.addTestSuite(Test_org_eclipse_swt_widgets_Tree.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_TreeItem.class);
		
	return suite;
}
}

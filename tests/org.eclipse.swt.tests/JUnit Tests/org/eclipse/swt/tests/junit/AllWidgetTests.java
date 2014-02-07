/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
 * Suite for testing all of the widget test cases.
 */
public class AllWidgetTests {
public static void main(String[] args) {
	TestRunner.run (suite());
}
public static Test suite() {
	TestSuite suite = new TestSuite();

	suite.addTestSuite(Test_org_eclipse_swt_widgets_ExpandItem.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_MenuItem.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_ToolItem.class);
	//suite.addTest(Test_org_eclipse_swt_widgets_CoolItem.suite());
	suite.addTestSuite(Test_org_eclipse_swt_widgets_TabItem.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_TableItem.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_TableColumn.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_TreeItem.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_Caret.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_Event.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_Menu.class);
	//suite.addTest(Test_org_eclipse_swt_widgets_Tracker.suite());
	
	suite.addTest(Test_org_eclipse_swt_widgets_Shell.suite());
	//suite.addTest(Test_org_eclipse_swt_widgets_Decorations.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_Label.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_Button.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_ExpandBar.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_List.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_Text.suite());
	suite.addTestSuite(Test_org_eclipse_swt_widgets_ScrollBar.class);
	suite.addTest(Test_org_eclipse_swt_widgets_Sash.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_Tree.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_TabFolder.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_Combo.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_Group.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_ToolBar.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_Table.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_Canvas.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_Scale.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_Slider.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_ProgressBar.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_Composite.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_Link.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_DateTime.suite());
	//suite.addTest(Test_org_eclipse_swt_widgets_CoolBar.suite());

	suite.addTest(Test_org_eclipse_swt_widgets_ColorDialog.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_FileDialog.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_DirectoryDialog.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_FontDialog.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_MessageBox.suite());
	suite.addTestSuite(Test_org_eclipse_swt_widgets_Monitor.class);

	suite.addTest(Test_org_eclipse_swt_custom_TableTree.suite());
	suite.addTestSuite(Test_org_eclipse_swt_custom_StyleRange.class);
	suite.addTest(Test_org_eclipse_swt_custom_CCombo.suite());
	suite.addTestSuite(Test_org_eclipse_swt_custom_TableTreeItem.class);
	suite.addTest(Test_org_eclipse_swt_custom_CLabel.suite());
	suite.addTestSuite(Test_org_eclipse_swt_custom_CTabItem.class);
	suite.addTest(Test_org_eclipse_swt_custom_StyledText.suite());
	suite.addTest(Test_org_eclipse_swt_custom_CTabFolder.suite());

	return suite;
}
}

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
 * Suite for testing all of the widget test cases.
 */
public class AllWidgetTests {
public static void main(String[] args) {
	TestRunner.run (suite());
}
public static Test suite() {
	TestSuite suite = new TestSuite(AllWidgetTests.class.getName());

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
	
	suite.addTestSuite(Test_org_eclipse_swt_widgets_Shell.class);
	//suite.addTest(Test_org_eclipse_swt_widgets_Decorations.suite());
	suite.addTestSuite(Test_org_eclipse_swt_widgets_Label.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_Button.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_ExpandBar.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_List.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_Text.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_ScrollBar.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_Sash.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_Tree.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_TabFolder.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_Combo.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_Group.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_ToolBar.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_Table.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_Canvas.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_Scale.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_Slider.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_ProgressBar.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_Composite.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_Link.class);
	suite.addTest(new JUnit4TestAdapter(Test_org_eclipse_swt_widgets_DateTime.class));
	//suite.addTest(Test_org_eclipse_swt_widgets_CoolBar.suite());

	suite.addTestSuite(Test_org_eclipse_swt_widgets_ColorDialog.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_FileDialog.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_DirectoryDialog.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_FontDialog.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_MessageBox.class);
	suite.addTestSuite(Test_org_eclipse_swt_widgets_Monitor.class);

	suite.addTestSuite(Test_org_eclipse_swt_custom_TableTree.class);
	suite.addTestSuite(Test_org_eclipse_swt_custom_StyleRange.class);
	suite.addTestSuite(Test_org_eclipse_swt_custom_CCombo.class);
	suite.addTestSuite(Test_org_eclipse_swt_custom_TableTreeItem.class);
	suite.addTestSuite(Test_org_eclipse_swt_custom_CLabel.class);
	suite.addTestSuite(Test_org_eclipse_swt_custom_CTabItem.class);
	suite.addTestSuite(Test_org_eclipse_swt_custom_StyledText.class);
	suite.addTestSuite(Test_org_eclipse_swt_custom_CTabFolder.class);

	return suite;
}
}

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

	suite.addTest(Test_org_eclipse_swt_widgets_ExpandItem.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_MenuItem.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_ToolItem.suite());
	//suite.addTest(Test_org_eclipse_swt_widgets_CoolItem.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_TabItem.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_TableItem.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_TableColumn.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_TreeItem.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_Caret.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_Event.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_TypedListener.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_Menu.suite());
	//suite.addTest(Test_org_eclipse_swt_widgets_Tracker.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_Synchronizer.suite());
	
	suite.addTest(Test_org_eclipse_swt_widgets_Shell.suite());
	//suite.addTest(Test_org_eclipse_swt_widgets_Decorations.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_Label.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_Button.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_ExpandBar.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_List.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_Text.suite());
	suite.addTest(Test_org_eclipse_swt_widgets_ScrollBar.suite());
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
	suite.addTest(Test_org_eclipse_swt_widgets_Monitor.suite());

	suite.addTest(Test_org_eclipse_swt_custom_TableTree.suite());
	suite.addTest(Test_org_eclipse_swt_custom_BidiSegmentEvent.suite());
	suite.addTest(Test_org_eclipse_swt_custom_LineBackgroundEvent.suite());
	suite.addTest(Test_org_eclipse_swt_custom_LineStyleEvent.suite());
	suite.addTest(Test_org_eclipse_swt_custom_SashForm.suite());
	suite.addTest(Test_org_eclipse_swt_custom_TableEditor.suite());
	suite.addTest(Test_org_eclipse_swt_custom_AnimatedProgress.suite());
	suite.addTest(Test_org_eclipse_swt_custom_StyleRange.suite());
	suite.addTest(Test_org_eclipse_swt_custom_TextChangedEvent.suite());
	suite.addTest(Test_org_eclipse_swt_custom_StackLayout.suite());
	suite.addTest(Test_org_eclipse_swt_custom_CTabFolderEvent.suite());
	suite.addTest(Test_org_eclipse_swt_custom_ExtendedModifyEvent.suite());
	suite.addTest(Test_org_eclipse_swt_custom_CCombo.suite());
	suite.addTest(Test_org_eclipse_swt_custom_TableTreeItem.suite());
	suite.addTest(Test_org_eclipse_swt_custom_ViewForm.suite());
	suite.addTest(Test_org_eclipse_swt_custom_CTabFolderAdapter.suite());
	suite.addTest(Test_org_eclipse_swt_custom_CLabel.suite());
	suite.addTest(Test_org_eclipse_swt_custom_TableTreeEditor.suite());
	suite.addTest(Test_org_eclipse_swt_custom_TextChangingEvent.suite());
	suite.addTest(Test_org_eclipse_swt_custom_ScrolledComposite.suite());
	suite.addTest(Test_org_eclipse_swt_custom_TreeEditor.suite());
	suite.addTest(Test_org_eclipse_swt_custom_PopupList.suite());
	suite.addTest(Test_org_eclipse_swt_custom_CTabItem.suite());
	suite.addTest(Test_org_eclipse_swt_custom_BusyIndicator.suite());
	suite.addTest(Test_org_eclipse_swt_custom_StyledText.suite());
	suite.addTest(Test_org_eclipse_swt_custom_ControlEditor.suite());
	suite.addTest(Test_org_eclipse_swt_custom_ST.suite());
	suite.addTest(Test_org_eclipse_swt_custom_CTabFolder.suite());

	return suite;
}
}

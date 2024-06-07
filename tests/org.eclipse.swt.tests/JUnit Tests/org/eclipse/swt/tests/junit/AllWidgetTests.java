/*******************************************************************************
 * Copyright (c) 2000 2015 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Red Hat Inc. - Bug 462631
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Suite for testing all of the widget test cases.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ Test_org_eclipse_swt_widgets_Shell.class,
		Test_org_eclipse_swt_widgets_ExpandItem.class, Test_org_eclipse_swt_widgets_MenuItem.class,
		Test_org_eclipse_swt_widgets_ToolItem.class, Test_org_eclipse_swt_widgets_TabItem.class,
		Test_org_eclipse_swt_widgets_TableItem.class, Test_org_eclipse_swt_widgets_TableColumn.class,
		Test_org_eclipse_swt_widgets_TreeItem.class, Test_org_eclipse_swt_widgets_Caret.class,
		Test_org_eclipse_swt_widgets_Event.class, Test_org_eclipse_swt_widgets_Menu.class,
		Test_org_eclipse_swt_widgets_Label.class,
		Test_org_eclipse_swt_widgets_Button.class, Test_org_eclipse_swt_widgets_ExpandBar.class,
		Test_org_eclipse_swt_widgets_List.class, Test_org_eclipse_swt_widgets_Text.class,
		Test_org_eclipse_swt_widgets_ScrollBar.class, Test_org_eclipse_swt_widgets_Sash.class,
		Test_org_eclipse_swt_widgets_Tree.class, Test_org_eclipse_swt_widgets_TabFolder.class,
		Test_org_eclipse_swt_widgets_Combo.class, Test_org_eclipse_swt_widgets_Group.class,
		Test_org_eclipse_swt_widgets_ToolBar.class, Test_org_eclipse_swt_widgets_Table.class,
		Test_org_eclipse_swt_widgets_Canvas.class, Test_org_eclipse_swt_widgets_Scale.class,
		Test_org_eclipse_swt_widgets_Slider.class, Test_org_eclipse_swt_widgets_ProgressBar.class,
		Test_org_eclipse_swt_widgets_Composite.class, Test_org_eclipse_swt_widgets_Link.class,
		Test_org_eclipse_swt_widgets_DateTime.class, Test_org_eclipse_swt_widgets_ColorDialog.class,
		Test_org_eclipse_swt_widgets_FileDialog.class, Test_org_eclipse_swt_widgets_DirectoryDialog.class,
		Test_org_eclipse_swt_widgets_FontDialog.class, Test_org_eclipse_swt_widgets_MessageBox.class,
		Test_org_eclipse_swt_widgets_Monitor.class,
		Test_org_eclipse_swt_custom_StyleRange.class, Test_org_eclipse_swt_custom_CCombo.class,
		Test_org_eclipse_swt_custom_CLabel.class,
		Test_org_eclipse_swt_custom_CTabItem.class,
		Test_org_eclipse_swt_custom_StyledText.class,
		Test_org_eclipse_swt_custom_StyledText_VariableLineHeight.class,
		Test_org_eclipse_swt_custom_StyledText_multiCaretsSelections.class,
		Test_org_eclipse_swt_custom_StyledTextLineSpacingProvider.class,
		Test_org_eclipse_swt_custom_CTabFolder.class, Test_org_eclipse_swt_widgets_Spinner.class,
		Test_org_eclipse_swt_widgets_ScrolledComposite.class,
		Test_org_eclipse_swt_custom_BusyIndicator.class})
public class AllWidgetTests {
	public static void main(String[] args) {
		JUnitCore.main(AllWidgetTests.class.getName());
	}
}

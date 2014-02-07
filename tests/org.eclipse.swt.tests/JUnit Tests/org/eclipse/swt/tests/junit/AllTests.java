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
 * Suite for running all SWT test cases.
 */
public class AllTests extends TestSuite {

public static void main(String[] args) {
	TestRunner.run(suite());
}
public static Test suite() {
	return new AllTests();
}

public AllTests() {
	super();
	/* The logical order to run the tests in is:
	 * - SWT, SWTError, SWTException
	 * - Display
	 * - graphics classes
	 * - items and Caret, etc
	 * - widgets
	 * - dialogs
	 * - layout
	 * - custom widgets
	 * - printing and program
	 * - events
	 * - drag & drop
	 * - accessibility
	 * - OLE
	 * - browser
	 */
	addTestSuite(Test_org_eclipse_swt_SWT.class);
	addTestSuite(Test_org_eclipse_swt_SWTException.class);
	addTestSuite(Test_org_eclipse_swt_SWTError.class);

	/* NOTE: If the Display test suite is run, it must be run
	 * before any other tests that need a display (i.e. graphics
	 * or widget tests, etc). Otherwise, an InvalidThreadAccess
	 * exception will be thrown for each Display test.
	 */
	addTestSuite(Test_org_eclipse_swt_widgets_Display.class);

	addTestSuite(Test_org_eclipse_swt_graphics_Image.class);
	addTestSuite(Test_org_eclipse_swt_graphics_Cursor.class);
	addTestSuite(Test_org_eclipse_swt_graphics_DeviceData.class);
	addTestSuite(Test_org_eclipse_swt_graphics_ImageLoaderEvent.class);
	addTestSuite(Test_org_eclipse_swt_graphics_RGB.class);
	addTestSuite(Test_org_eclipse_swt_graphics_Font.class);
	addTestSuite(Test_org_eclipse_swt_graphics_Rectangle.class);
	addTestSuite(Test_org_eclipse_swt_graphics_FontData.class);
	addTestSuite(Test_org_eclipse_swt_graphics_GC.class);
	addTestSuite(Test_org_eclipse_swt_graphics_ImageData.class);
	addTestSuite(Test_org_eclipse_swt_graphics_Region.class);
	addTestSuite(Test_org_eclipse_swt_graphics_FontMetrics.class);
	addTestSuite(Test_org_eclipse_swt_graphics_Color.class);
	addTestSuite(Test_org_eclipse_swt_graphics_Point.class);
	addTestSuite(Test_org_eclipse_swt_graphics_PaletteData.class);
	addTestSuite(Test_org_eclipse_swt_graphics_ImageLoader.class);
	addTestSuite(Test_org_eclipse_swt_graphics_TextLayout.class);

	addTestSuite(Test_org_eclipse_swt_widgets_ExpandItem.class);
	addTestSuite(Test_org_eclipse_swt_widgets_MenuItem.class);
	addTestSuite(Test_org_eclipse_swt_widgets_ToolItem.class);
	//addTest(Test_org_eclipse_swt_widgets_CoolItem.suite());
	addTestSuite(Test_org_eclipse_swt_widgets_TabItem.class);
	addTestSuite(Test_org_eclipse_swt_widgets_TableItem.class);
	addTestSuite(Test_org_eclipse_swt_widgets_TableColumn.class);
	addTestSuite(Test_org_eclipse_swt_widgets_TreeItem.class);
	addTestSuite(Test_org_eclipse_swt_widgets_Caret.class);
	addTestSuite(Test_org_eclipse_swt_widgets_Event.class);
	addTestSuite(Test_org_eclipse_swt_widgets_Menu.class);
	//addTest(Test_org_eclipse_swt_widgets_Tracker.suite());
	
	addTest(Test_org_eclipse_swt_widgets_Shell.suite());
	//addTest(Test_org_eclipse_swt_widgets_Decorations.suite());
	addTestSuite(Test_org_eclipse_swt_widgets_Label.class);
	addTestSuite(Test_org_eclipse_swt_widgets_Button.class);
	addTestSuite(Test_org_eclipse_swt_widgets_ExpandBar.class);
	addTestSuite(Test_org_eclipse_swt_widgets_List.class);
	addTestSuite(Test_org_eclipse_swt_widgets_Text.class);
	addTestSuite(Test_org_eclipse_swt_widgets_ScrollBar.class);
	addTestSuite(Test_org_eclipse_swt_widgets_Sash.class);
	addTestSuite(Test_org_eclipse_swt_widgets_Tree.class);
	addTestSuite(Test_org_eclipse_swt_widgets_TabFolder.class);
	addTestSuite(Test_org_eclipse_swt_widgets_Combo.class);
	addTestSuite(Test_org_eclipse_swt_widgets_Group.class);
	addTestSuite(Test_org_eclipse_swt_widgets_ToolBar.class);
	addTestSuite(Test_org_eclipse_swt_widgets_Table.class);
	addTest(Test_org_eclipse_swt_widgets_Canvas.suite());
	addTestSuite(Test_org_eclipse_swt_widgets_Scale.class);
	addTestSuite(Test_org_eclipse_swt_widgets_Slider.class);
	addTestSuite(Test_org_eclipse_swt_widgets_ProgressBar.class);
	addTest(Test_org_eclipse_swt_widgets_Composite.suite());
	addTestSuite(Test_org_eclipse_swt_widgets_Link.class);
	addTest(Test_org_eclipse_swt_widgets_DateTime.suite());
	//addTestSuite(Test_org_eclipse_swt_widgets_CoolBar.class);

	addTestSuite(Test_org_eclipse_swt_widgets_ColorDialog.class);
	addTestSuite(Test_org_eclipse_swt_widgets_FileDialog.class);
	addTestSuite(Test_org_eclipse_swt_widgets_DirectoryDialog.class);
	addTestSuite(Test_org_eclipse_swt_widgets_FontDialog.class);
	addTestSuite(Test_org_eclipse_swt_widgets_MessageBox.class);
	addTestSuite(Test_org_eclipse_swt_widgets_Monitor.class);
	addTestSuite(Test_org_eclipse_swt_layout_GridData.class);

	addTestSuite(Test_org_eclipse_swt_custom_TableTree.class);
	addTestSuite(Test_org_eclipse_swt_custom_StyleRange.class);
	addTestSuite(Test_org_eclipse_swt_custom_CCombo.class);
	addTestSuite(Test_org_eclipse_swt_custom_TableTreeItem.class);
	addTestSuite(Test_org_eclipse_swt_custom_CLabel.class);
	addTestSuite(Test_org_eclipse_swt_custom_CTabItem.class);
	addTestSuite(Test_org_eclipse_swt_custom_StyledText.class);
	addTestSuite(Test_org_eclipse_swt_custom_CTabFolder.class);

	addTestSuite(Test_org_eclipse_swt_events_ControlEvent.class);
	addTestSuite(Test_org_eclipse_swt_events_ModifyEvent.class);
	addTestSuite(Test_org_eclipse_swt_events_ArmEvent.class);
	addTestSuite(Test_org_eclipse_swt_events_ShellEvent.class);
	addTestSuite(Test_org_eclipse_swt_events_TypedEvent.class);
	addTestSuite(Test_org_eclipse_swt_events_PaintEvent.class);
	addTestSuite(Test_org_eclipse_swt_events_VerifyEvent.class);
	addTestSuite(Test_org_eclipse_swt_events_KeyEvent.class);
	addTestSuite(Test_org_eclipse_swt_events_TraverseEvent.class);
	addTestSuite(Test_org_eclipse_swt_events_DisposeEvent.class);
	addTestSuite(Test_org_eclipse_swt_events_SelectionEvent.class);
	addTestSuite(Test_org_eclipse_swt_events_HelpEvent.class);
	addTestSuite(Test_org_eclipse_swt_events_FocusEvent.class);
	addTestSuite(Test_org_eclipse_swt_events_MouseEvent.class);
	addTestSuite(Test_org_eclipse_swt_events_MenuEvent.class);
	addTestSuite(Test_org_eclipse_swt_events_TreeEvent.class);
	
	addTestSuite(Test_org_eclipse_swt_printing_PrintDialog.class);
	addTestSuite(Test_org_eclipse_swt_printing_PrinterData.class);
	addTestSuite(Test_org_eclipse_swt_printing_Printer.class);

	addTestSuite(Test_org_eclipse_swt_program_Program.class);

	addTestSuite(Test_org_eclipse_swt_accessibility_Accessible.class);
	addTestSuite(Test_org_eclipse_swt_accessibility_AccessibleControlEvent.class);
	addTestSuite(Test_org_eclipse_swt_accessibility_AccessibleEvent.class);

	addTest(Test_org_eclipse_swt_browser_Browser.suite());
	addTestSuite(Test_org_eclipse_swt_browser_CloseWindowListener.class);
	addTestSuite(Test_org_eclipse_swt_browser_LocationAdapter.class);
	addTestSuite(Test_org_eclipse_swt_browser_LocationListener.class);
	addTestSuite(Test_org_eclipse_swt_browser_OpenWindowListener.class);
	addTestSuite(Test_org_eclipse_swt_browser_ProgressAdapter.class);
	addTestSuite(Test_org_eclipse_swt_browser_ProgressListener.class);
	addTestSuite(Test_org_eclipse_swt_browser_StatusTextListener.class);
	addTestSuite(Test_org_eclipse_swt_browser_TitleListener.class);
	addTestSuite(Test_org_eclipse_swt_browser_VisibilityWindowAdapter.class);
	addTestSuite(Test_org_eclipse_swt_browser_VisibilityWindowListener.class);
	addTestSuite(org.eclipse.swt.tests.junit.browser.Test_BrowserSuite.class);

}
}

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
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
	SwtTestCase.unimplementedMethods = 0;
	TestRunner.run(suite());
	if (SwtTestCase.unimplementedMethods > 0) {
		System.out.println("\nCalls to warnUnimpl: " + SwtTestCase.unimplementedMethods);
	}
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
	 */
	addTest(Test_org_eclipse_swt_SWT.suite());
	addTest(Test_org_eclipse_swt_SWTException.suite());
	addTest(Test_org_eclipse_swt_SWTError.suite());

	addTest(Test_org_eclipse_swt_widgets_Display.suite());

	addTest(Test_org_eclipse_swt_graphics_Image.suite());
	addTest(Test_org_eclipse_swt_graphics_Cursor.suite());
	addTest(Test_org_eclipse_swt_graphics_DeviceData.suite());
	addTest(Test_org_eclipse_swt_graphics_ImageLoaderEvent.suite());
	addTest(Test_org_eclipse_swt_graphics_RGB.suite());
	addTest(Test_org_eclipse_swt_graphics_Font.suite());
	addTest(Test_org_eclipse_swt_graphics_Rectangle.suite());
	addTest(Test_org_eclipse_swt_graphics_FontData.suite());
	addTest(Test_org_eclipse_swt_graphics_GC.suite());
	addTest(Test_org_eclipse_swt_graphics_ImageData.suite());
	addTest(Test_org_eclipse_swt_graphics_Region.suite());
	addTest(Test_org_eclipse_swt_graphics_GCData.suite());
	addTest(Test_org_eclipse_swt_graphics_FontMetrics.suite());
	addTest(Test_org_eclipse_swt_graphics_Color.suite());
	addTest(Test_org_eclipse_swt_graphics_Point.suite());
	addTest(Test_org_eclipse_swt_graphics_PaletteData.suite());
	addTest(Test_org_eclipse_swt_graphics_ImageLoader.suite());

	addTest(Test_org_eclipse_swt_widgets_MenuItem.suite());
	addTest(Test_org_eclipse_swt_widgets_ToolItem.suite());
	//addTest(Test_org_eclipse_swt_widgets_CoolItem.suite());
	addTest(Test_org_eclipse_swt_widgets_TabItem.suite());
	addTest(Test_org_eclipse_swt_widgets_TableItem.suite());
	addTest(Test_org_eclipse_swt_widgets_TableColumn.suite());
	addTest(Test_org_eclipse_swt_widgets_TreeItem.suite());
	addTest(Test_org_eclipse_swt_widgets_Caret.suite());
	addTest(Test_org_eclipse_swt_widgets_Event.suite());
	addTest(Test_org_eclipse_swt_widgets_TypedListener.suite());
	addTest(Test_org_eclipse_swt_widgets_Menu.suite());
	//addTest(Test_org_eclipse_swt_widgets_Tracker.suite());
	addTest(Test_org_eclipse_swt_widgets_Synchronizer.suite());
	
	addTest(Test_org_eclipse_swt_widgets_Shell.suite());
	//addTest(Test_org_eclipse_swt_widgets_Decorations.suite());
	addTest(Test_org_eclipse_swt_widgets_Label.suite());
	addTest(Test_org_eclipse_swt_widgets_Button.suite());
	addTest(Test_org_eclipse_swt_widgets_List.suite());
	addTest(Test_org_eclipse_swt_widgets_Text.suite());
	addTest(Test_org_eclipse_swt_widgets_ScrollBar.suite());
	addTest(Test_org_eclipse_swt_widgets_Sash.suite());
	addTest(Test_org_eclipse_swt_widgets_Tree.suite());
	addTest(Test_org_eclipse_swt_widgets_TabFolder.suite());
	addTest(Test_org_eclipse_swt_widgets_Combo.suite());
	addTest(Test_org_eclipse_swt_widgets_Group.suite());
	addTest(Test_org_eclipse_swt_widgets_ToolBar.suite());
	addTest(Test_org_eclipse_swt_widgets_Table.suite());
	addTest(Test_org_eclipse_swt_widgets_Canvas.suite());
	addTest(Test_org_eclipse_swt_widgets_Scale.suite());
	addTest(Test_org_eclipse_swt_widgets_Slider.suite());
	addTest(Test_org_eclipse_swt_widgets_ProgressBar.suite());
	addTest(Test_org_eclipse_swt_widgets_Composite.suite());
	//addTest(Test_org_eclipse_swt_widgets_CoolBar.suite());

	addTest(Test_org_eclipse_swt_widgets_ColorDialog.suite());
	addTest(Test_org_eclipse_swt_widgets_FileDialog.suite());
	addTest(Test_org_eclipse_swt_widgets_DirectoryDialog.suite());
	addTest(Test_org_eclipse_swt_widgets_FontDialog.suite());
	addTest(Test_org_eclipse_swt_widgets_MessageBox.suite());
	addTest(Test_org_eclipse_swt_widgets_Monitor.suite());
	addTest(Test_org_eclipse_swt_layout_GridData.suite());
	addTest(Test_org_eclipse_swt_layout_RowData.suite());
	addTest(Test_org_eclipse_swt_layout_GridLayout.suite());
	addTest(Test_org_eclipse_swt_layout_FillLayout.suite());
	addTest(Test_org_eclipse_swt_layout_RowLayout.suite());

	addTest(Test_org_eclipse_swt_custom_TableTree.suite());
	addTest(Test_org_eclipse_swt_custom_BidiSegmentEvent.suite());
	addTest(Test_org_eclipse_swt_custom_LineBackgroundEvent.suite());
	addTest(Test_org_eclipse_swt_custom_LineStyleEvent.suite());
	addTest(Test_org_eclipse_swt_custom_SashForm.suite());
	addTest(Test_org_eclipse_swt_custom_TableEditor.suite());
	addTest(Test_org_eclipse_swt_custom_AnimatedProgress.suite());
	addTest(Test_org_eclipse_swt_custom_StyleRange.suite());
	addTest(Test_org_eclipse_swt_custom_TextChangedEvent.suite());
	addTest(Test_org_eclipse_swt_custom_StackLayout.suite());
	addTest(Test_org_eclipse_swt_custom_CTabFolderEvent.suite());
	addTest(Test_org_eclipse_swt_custom_ExtendedModifyEvent.suite());
	addTest(Test_org_eclipse_swt_custom_CCombo.suite());
	addTest(Test_org_eclipse_swt_custom_TableTreeItem.suite());
	addTest(Test_org_eclipse_swt_custom_ViewForm.suite());
	addTest(Test_org_eclipse_swt_custom_CTabFolderAdapter.suite());
	addTest(Test_org_eclipse_swt_custom_CLabel.suite());
	addTest(Test_org_eclipse_swt_custom_TableTreeEditor.suite());
	addTest(Test_org_eclipse_swt_custom_TextChangingEvent.suite());
	addTest(Test_org_eclipse_swt_custom_ScrolledComposite.suite());
	addTest(Test_org_eclipse_swt_custom_TreeEditor.suite());
	addTest(Test_org_eclipse_swt_custom_PopupList.suite());
	addTest(Test_org_eclipse_swt_custom_CTabItem.suite());
	addTest(Test_org_eclipse_swt_custom_BusyIndicator.suite());
	addTest(Test_org_eclipse_swt_custom_StyledText.suite());
	addTest(Test_org_eclipse_swt_custom_ControlEditor.suite());
	addTest(Test_org_eclipse_swt_custom_ST.suite());
	addTest(Test_org_eclipse_swt_custom_CTabFolder.suite());

	addTest(Test_org_eclipse_swt_events_ControlEvent.suite());
	addTest(Test_org_eclipse_swt_events_ModifyEvent.suite());
	addTest(Test_org_eclipse_swt_events_ArmEvent.suite());
	addTest(Test_org_eclipse_swt_events_ShellEvent.suite());
	addTest(Test_org_eclipse_swt_events_TypedEvent.suite());
	addTest(Test_org_eclipse_swt_events_PaintEvent.suite());
	addTest(Test_org_eclipse_swt_events_VerifyEvent.suite());
	addTest(Test_org_eclipse_swt_events_KeyEvent.suite());
	addTest(Test_org_eclipse_swt_events_TraverseEvent.suite());
	addTest(Test_org_eclipse_swt_events_MouseTrackAdapter.suite());
	addTest(Test_org_eclipse_swt_events_DisposeEvent.suite());
	addTest(Test_org_eclipse_swt_events_SelectionEvent.suite());
	addTest(Test_org_eclipse_swt_events_HelpEvent.suite());
	addTest(Test_org_eclipse_swt_events_FocusEvent.suite());
	addTest(Test_org_eclipse_swt_events_MouseEvent.suite());
	addTest(Test_org_eclipse_swt_events_MenuEvent.suite());
	addTest(Test_org_eclipse_swt_events_TreeEvent.suite());
	
	addTest(Test_org_eclipse_swt_printing_PrintDialog.suite());
	addTest(Test_org_eclipse_swt_printing_PrinterData.suite());
	addTest(Test_org_eclipse_swt_printing_Printer.suite());

	addTest(Test_org_eclipse_swt_program_Program.suite());

//	addTest(Test_org_eclipse_swt_dnd_FileTransfer.suite());
//	addTest(Test_org_eclipse_swt_dnd_DragSourceAdapter.suite());
//	addTest(Test_org_eclipse_swt_dnd_DropTargetAdapter.suite());
//	addTest(Test_org_eclipse_swt_dnd_TextTransfer.suite());
//	addTest(Test_org_eclipse_swt_dnd_DragSourceEvent.suite());
//	addTest(Test_org_eclipse_swt_dnd_DragSource.suite());
//	addTest(Test_org_eclipse_swt_dnd_DND.suite());
//	addTest(Test_org_eclipse_swt_dnd_TransferData.suite());
//	addTest(Test_org_eclipse_swt_dnd_RTFTransfer.suite());
//	addTest(Test_org_eclipse_swt_dnd_Clipboard.suite());
//	addTest(Test_org_eclipse_swt_dnd_DropTargetEvent.suite());
//	addTest(Test_org_eclipse_swt_dnd_DropTarget.suite());
}
}

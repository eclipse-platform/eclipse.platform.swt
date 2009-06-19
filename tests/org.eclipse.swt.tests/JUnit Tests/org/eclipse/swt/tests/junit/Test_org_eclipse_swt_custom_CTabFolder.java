/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Text;

import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.CTabFolder
 *
 * @see org.eclipse.swt.custom.CTabFolder
 */
public class Test_org_eclipse_swt_custom_CTabFolder extends Test_org_eclipse_swt_widgets_Composite {

public Test_org_eclipse_swt_custom_CTabFolder(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	makeCleanEnvironment();
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_CompositeI not written");
}

public void test_addCTabFolderListenerLorg_eclipse_swt_custom_CTabFolderListener() {
	warnUnimpl("Test test_addCTabFolderListenerLorg_eclipse_swt_custom_CTabFolderListener not written");
}

public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	warnUnimpl("Test test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener not written");
}

public void test_computeSizeIIZ() {
	warnUnimpl("Test test_computeSizeIIZ not written");
}

public void test_computeTrimIIII() {
	warnUnimpl("Test test_computeTrimIIII not written");
}

public void test_getClientArea() {
	warnUnimpl("Test test_getClientArea not written");
}

public void test_getItemCount() {
	warnUnimpl("Test test_getItemCount not written");
}

public void test_getItemI() {
	warnUnimpl("Test test_getItemI not written");
}

public void test_getItemLorg_eclipse_swt_graphics_Point() {
	warnUnimpl("Test test_getItemLorg_eclipse_swt_graphics_Point not written");
}

public void test_getItems() {
	warnUnimpl("Test test_getItems not written");
}

public void test_getSelection() {
	warnUnimpl("Test test_getSelection not written");
}

public void test_getSelectionIndex() {
	warnUnimpl("Test test_getSelectionIndex not written");
}

public void test_getTabHeight() {
	warnUnimpl("Test test_getTabHeight not written");
}

public void test_getTopRight() {
	warnUnimpl("Test test_getTopRight not written");
}

public void test_indexOfLorg_eclipse_swt_custom_CTabItem() {
	warnUnimpl("Test test_indexOfLorg_eclipse_swt_custom_CTabItem not written");
}

public void test_removeCTabFolderListenerLorg_eclipse_swt_custom_CTabFolderListener() {
	warnUnimpl("Test test_removeCTabFolderListenerLorg_eclipse_swt_custom_CTabFolderListener not written");
}

public void test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	warnUnimpl("Test test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener not written");
}

public void test_setBackgroundLorg_eclipse_swt_graphics_Color() {
	warnUnimpl("Test test_setBackgroundLorg_eclipse_swt_graphics_Color not written");
}

public void test_setBorderVisibleZ() {
	warnUnimpl("Test test_setBorderVisibleZ not written");
}

public void test_setFontLorg_eclipse_swt_graphics_Font() {
	warnUnimpl("Test test_setFontLorg_eclipse_swt_graphics_Font not written");
}

public void test_setInsertMarkIZ() {
	warnUnimpl("Test test_setInsertMarkIZ not written");
}

public void test_setInsertMarkLorg_eclipse_swt_custom_CTabItemZ() {
	warnUnimpl("Test test_setInsertMarkLorg_eclipse_swt_custom_CTabItemZ not written");
}

public void test_setSelectionBackground$Lorg_eclipse_swt_graphics_Color$I() {
	warnUnimpl("Test test_setSelectionBackground$Lorg_eclipse_swt_graphics_Color$I not written");
}

public void test_setSelectionBackgroundLorg_eclipse_swt_graphics_Image() {
	warnUnimpl("Test test_setSelectionBackgroundLorg_eclipse_swt_graphics_Image not written");
}

public void test_setSelectionForegroundLorg_eclipse_swt_graphics_Color() {
	warnUnimpl("Test test_setSelectionForegroundLorg_eclipse_swt_graphics_Color not written");
}

public void test_setSelectionI() {
	warnUnimpl("Test test_setSelectionI not written");
}

public void test_setSelectionLorg_eclipse_swt_custom_CTabItem() {
	warnUnimpl("Test test_setSelectionLorg_eclipse_swt_custom_CTabItem not written");
}

public void test_setTabHeightI() {
	warnUnimpl("Test test_setTabHeightI not written");
}

public void test_setTopRightLorg_eclipse_swt_widgets_Control() {
	warnUnimpl("Test test_setTopRightLorg_eclipse_swt_widgets_Control not written");
}

public void test_showItemLorg_eclipse_swt_custom_CTabItem() {
	warnUnimpl("Test test_showItemLorg_eclipse_swt_custom_CTabItem not written");
}

public void test_showSelection() {
	warnUnimpl("Test test_showSelection not written");
}


public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_custom_CTabFolder((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_addCTabFolderListenerLorg_eclipse_swt_custom_CTabFolderListener");
	methodNames.addElement("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_computeSizeIIZ");
	methodNames.addElement("test_computeTrimIIII");
	methodNames.addElement("test_getClientArea");
	methodNames.addElement("test_getItemCount");
	methodNames.addElement("test_getItemI");
	methodNames.addElement("test_getItemLorg_eclipse_swt_graphics_Point");
	methodNames.addElement("test_getItems");
	methodNames.addElement("test_getSelection");
	methodNames.addElement("test_getSelectionIndex");
	methodNames.addElement("test_getTabHeight");
	methodNames.addElement("test_getTopRight");
	methodNames.addElement("test_indexOfLorg_eclipse_swt_custom_CTabItem");
	methodNames.addElement("test_removeCTabFolderListenerLorg_eclipse_swt_custom_CTabFolderListener");
	methodNames.addElement("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_setBackgroundLorg_eclipse_swt_graphics_Color");
	methodNames.addElement("test_setBorderVisibleZ");
	methodNames.addElement("test_setFontLorg_eclipse_swt_graphics_Font");
	methodNames.addElement("test_setInsertMarkIZ");
	methodNames.addElement("test_setInsertMarkLorg_eclipse_swt_custom_CTabItemZ");
	methodNames.addElement("test_setSelectionBackground$Lorg_eclipse_swt_graphics_Color$I");
	methodNames.addElement("test_setSelectionBackgroundLorg_eclipse_swt_graphics_Image");
	methodNames.addElement("test_setSelectionForegroundLorg_eclipse_swt_graphics_Color");
	methodNames.addElement("test_setSelectionI");
	methodNames.addElement("test_setSelectionLorg_eclipse_swt_custom_CTabItem");
	methodNames.addElement("test_setTabHeightI");
	methodNames.addElement("test_setTopRightLorg_eclipse_swt_widgets_Control");
	methodNames.addElement("test_showItemLorg_eclipse_swt_custom_CTabItem");
	methodNames.addElement("test_showSelection");
	methodNames.addElement("test_consistency_KeySelection");
	methodNames.addElement("test_consistency_MouseSelection");
	methodNames.addElement("test_consistency_PgupSelection");
	methodNames.addElement("test_consistency_PgdwnSelection");
	methodNames.addElement("test_consistency_MenuDetect");
	methodNames.addElement("test_consistency_DragDetect");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Composite.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_addCTabFolderListenerLorg_eclipse_swt_custom_CTabFolderListener")) test_addCTabFolderListenerLorg_eclipse_swt_custom_CTabFolderListener();
	else if (getName().equals("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_computeSizeIIZ")) test_computeSizeIIZ();
	else if (getName().equals("test_computeTrimIIII")) test_computeTrimIIII();
	else if (getName().equals("test_getClientArea")) test_getClientArea();
	else if (getName().equals("test_getItemCount")) test_getItemCount();
	else if (getName().equals("test_getItemI")) test_getItemI();
	else if (getName().equals("test_getItemLorg_eclipse_swt_graphics_Point")) test_getItemLorg_eclipse_swt_graphics_Point();
	else if (getName().equals("test_getItems")) test_getItems();
	else if (getName().equals("test_getSelection")) test_getSelection();
	else if (getName().equals("test_getSelectionIndex")) test_getSelectionIndex();
	else if (getName().equals("test_getTabHeight")) test_getTabHeight();
	else if (getName().equals("test_getTopRight")) test_getTopRight();
	else if (getName().equals("test_indexOfLorg_eclipse_swt_custom_CTabItem")) test_indexOfLorg_eclipse_swt_custom_CTabItem();
	else if (getName().equals("test_removeCTabFolderListenerLorg_eclipse_swt_custom_CTabFolderListener")) test_removeCTabFolderListenerLorg_eclipse_swt_custom_CTabFolderListener();
	else if (getName().equals("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_setBackgroundLorg_eclipse_swt_graphics_Color")) test_setBackgroundLorg_eclipse_swt_graphics_Color();
	else if (getName().equals("test_setBorderVisibleZ")) test_setBorderVisibleZ();
	else if (getName().equals("test_setFontLorg_eclipse_swt_graphics_Font")) test_setFontLorg_eclipse_swt_graphics_Font();
	else if (getName().equals("test_setInsertMarkIZ")) test_setInsertMarkIZ();
	else if (getName().equals("test_setInsertMarkLorg_eclipse_swt_custom_CTabItemZ")) test_setInsertMarkLorg_eclipse_swt_custom_CTabItemZ();
	else if (getName().equals("test_setSelectionBackground$Lorg_eclipse_swt_graphics_Color$I")) test_setSelectionBackground$Lorg_eclipse_swt_graphics_Color$I();
	else if (getName().equals("test_setSelectionBackgroundLorg_eclipse_swt_graphics_Image")) test_setSelectionBackgroundLorg_eclipse_swt_graphics_Image();
	else if (getName().equals("test_setSelectionForegroundLorg_eclipse_swt_graphics_Color")) test_setSelectionForegroundLorg_eclipse_swt_graphics_Color();
	else if (getName().equals("test_setSelectionI")) test_setSelectionI();
	else if (getName().equals("test_setSelectionLorg_eclipse_swt_custom_CTabItem")) test_setSelectionLorg_eclipse_swt_custom_CTabItem();
	else if (getName().equals("test_setTabHeightI")) test_setTabHeightI();
	else if (getName().equals("test_setTopRightLorg_eclipse_swt_widgets_Control")) test_setTopRightLorg_eclipse_swt_widgets_Control();
	else if (getName().equals("test_showItemLorg_eclipse_swt_custom_CTabItem")) test_showItemLorg_eclipse_swt_custom_CTabItem();
	else if (getName().equals("test_showSelection")) test_showSelection();
	else if (getName().equals("test_consistency_KeySelection")) test_consistency_KeySelection();
	else if (getName().equals("test_consistency_MouseSelection")) test_consistency_MouseSelection();
	else if (getName().equals("test_consistency_PgdwnSelection")) test_consistency_PgdwnSelection();
	else if (getName().equals("test_consistency_PgupSelection")) test_consistency_PgupSelection();
	else if (getName().equals("test_consistency_MenuDetect")) test_consistency_MenuDetect();
	else if (getName().equals("test_consistency_DragDetect")) test_consistency_DragDetect();
	else super.runTest();
}

/* custom */
protected CTabFolder ctabFolder;

private void makeCleanEnvironment() {
// this method must be private or protected so the auto-gen tool keeps it
	ctabFolder = new CTabFolder(shell, 0);
	setWidget(ctabFolder);
}

private void createTabFolder(Vector events) {
	makeCleanEnvironment();
	for (int i = 0; i < 3; i++) {
		CTabItem item = new CTabItem(ctabFolder, SWT.NONE);
		item.setText("CTabItem &" + i);
		item.setToolTipText("CTabItem ToolTip" + i);
		Text itemText = new Text(ctabFolder, SWT.MULTI | SWT.BORDER);
		itemText.setText("\nText for CTabItem " + i + "\n\n\n");
		item.setControl(itemText);
		hookExpectedEvents(item, getTestName(), events);
		hookExpectedEvents(itemText, getTestName(), events);
	}
	ctabFolder.setSelection(ctabFolder.getItem(0));
}

public void test_consistency_KeySelection() {
    Vector events = new Vector();
    createTabFolder(events);
    consistencyEvent(0, SWT.ARROW_RIGHT, 0, 0, ConsistencyUtility.KEY_PRESS, events, false);
}

public void test_consistency_MouseSelection() {
    Vector events = new Vector();
    createTabFolder(events);
    consistencyPrePackShell();
    consistencyEvent(ctabFolder.getSize().x/2, 5, 1, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

public void test_consistency_PgdwnSelection () {
    Vector events = new Vector();
    createTabFolder(events);
    consistencyEvent(0, SWT.CTRL, 0, SWT.PAGE_DOWN, ConsistencyUtility.DOUBLE_KEY_PRESS, events, false);
}

public void test_consistency_PgupSelection () {
    Vector events = new Vector();
    createTabFolder(events);
    ctabFolder.setSelection(2);
    consistencyEvent(0, SWT.CTRL, 0, SWT.PAGE_UP, ConsistencyUtility.DOUBLE_KEY_PRESS, events, false);
}

public void test_consistency_MenuDetect () {
    Vector events = new Vector();
    createTabFolder(events);
    ctabFolder.setSelection(1);
    consistencyEvent(50, 5, 3, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

public void test_consistency_DragDetect () {
    Vector events = new Vector();
    createTabFolder(events);
    ctabFolder.setSelection(1);
    consistencyEvent(50, 5, 70, 10, ConsistencyUtility.MOUSE_DRAG, events);
}

}

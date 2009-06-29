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


import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Point;

import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.CCombo
 *
 * @see org.eclipse.swt.custom.CCombo
 */
public class Test_org_eclipse_swt_custom_CCombo extends Test_org_eclipse_swt_widgets_Composite {

public Test_org_eclipse_swt_custom_CCombo(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	ccombo = new CCombo(shell, 0);
	setWidget(ccombo);
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_CompositeI not written");
}

public void test_copy() {
	ccombo.setText("123456");
	ccombo.setSelection(new Point(1,3));
	ccombo.copy();
	ccombo.setSelection(new Point(0,0));
	ccombo.paste();
	assertTrue(":a:", ccombo.getText().equals("23123456"));
}

public void test_cut() {
	ccombo.setText("123456");
	ccombo.setSelection(new Point(1,3));
	ccombo.cut();
	assertTrue(":a:", ccombo.getText().equals("1456"));
}

public void test_addLjava_lang_String() {
	warnUnimpl("Test test_addLjava_lang_String not written");
}

public void test_addLjava_lang_StringI() {
	warnUnimpl("Test test_addLjava_lang_StringI not written");
}

public void test_addModifyListenerLorg_eclipse_swt_events_ModifyListener() {
	warnUnimpl("Test test_addModifyListenerLorg_eclipse_swt_events_ModifyListener not written");
}

public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	warnUnimpl("Test test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener not written");
}

public void test_clearSelection() {
	warnUnimpl("Test test_clearSelection not written");
}

public void test_computeSizeIIZ() {
	warnUnimpl("Test test_computeSizeIIZ not written");
}

public void test_deselectAll() {
	warnUnimpl("Test test_deselectAll not written");
}

public void test_deselectI() {
	warnUnimpl("Test test_deselectI not written");
}

public void test_getChildren() {
	warnUnimpl("Test test_getChildren not written");
}

public void test_getEditable() {
	warnUnimpl("Test test_getEditable not written");
}

public void test_getItemCount() {
	warnUnimpl("Test test_getItemCount not written");
}

public void test_getItemHeight() {
	warnUnimpl("Test test_getItemHeight not written");
}

public void test_getItemI() {
	warnUnimpl("Test test_getItemI not written");
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

public void test_getStyle() {
	warnUnimpl("Test test_getStyle not written");
}

public void test_getText() {
	warnUnimpl("Test test_getText not written");
}

public void test_getTextHeight() {
	warnUnimpl("Test test_getTextHeight not written");
}

public void test_getTextLimit() {
	warnUnimpl("Test test_getTextLimit not written");
}

public void test_indexOfLjava_lang_String() {
	warnUnimpl("Test test_indexOfLjava_lang_String not written");
}

public void test_indexOfLjava_lang_StringI() {
	warnUnimpl("Test test_indexOfLjava_lang_StringI not written");
}

public void test_isFocusControl() {
	warnUnimpl("Test test_isFocusControl not written");
}

public void test_paste() {
	ccombo.setText("123456");
	ccombo.setSelection(new Point(1,3));
	ccombo.cut();
	assertTrue(":a:", ccombo.getText().equals("1456"));
	ccombo.paste();
	assertTrue(":a:", ccombo.getText().equals("123456"));
}

public void test_redraw() {
	warnUnimpl("Test test_redraw not written");
}

public void test_redrawIIIIZ() {
	warnUnimpl("Test test_redrawIIIIZ not written");
}

public void test_removeAll() {
	warnUnimpl("Test test_removeAll not written");
}

public void test_removeI() {
	warnUnimpl("Test test_removeI not written");
}

public void test_removeII() {
	warnUnimpl("Test test_removeII not written");
}

public void test_removeLjava_lang_String() {
	warnUnimpl("Test test_removeLjava_lang_String not written");
}

public void test_removeModifyListenerLorg_eclipse_swt_events_ModifyListener() {
	warnUnimpl("Test test_removeModifyListenerLorg_eclipse_swt_events_ModifyListener not written");
}

public void test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	warnUnimpl("Test test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener not written");
}

public void test_selectI() {
	warnUnimpl("Test test_selectI not written");
}

public void test_setBackgroundLorg_eclipse_swt_graphics_Color() {
	warnUnimpl("Test test_setBackgroundLorg_eclipse_swt_graphics_Color not written");
}

public void test_setEditableZ() {
	warnUnimpl("Test test_setEditableZ not written");
}

public void test_setEnabledZ() {
	warnUnimpl("Test test_setEnabledZ not written");
}

public void test_setFocus() {
	warnUnimpl("Test test_setFocus not written");
}

public void test_setFontLorg_eclipse_swt_graphics_Font() {
	warnUnimpl("Test test_setFontLorg_eclipse_swt_graphics_Font not written");
}

public void test_setForegroundLorg_eclipse_swt_graphics_Color() {
	warnUnimpl("Test test_setForegroundLorg_eclipse_swt_graphics_Color not written");
}

public void test_setItemILjava_lang_String() {
	warnUnimpl("Test test_setItemILjava_lang_String not written");
}

public void test_setItems$Ljava_lang_String() {
	warnUnimpl("Test test_setItems$Ljava_lang_String not written");
}

public void test_setSelectionLorg_eclipse_swt_graphics_Point() {
	warnUnimpl("Test test_setSelectionLorg_eclipse_swt_graphics_Point not written");
}

public void test_setTextLimitI() {
	warnUnimpl("Test test_setTextLimitI not written");
}

public void test_setTextLjava_lang_String() {
	warnUnimpl("Test test_setTextLjava_lang_String not written");
}

public void test_setToolTipTextLjava_lang_String() {
	warnUnimpl("Test test_setToolTipTextLjava_lang_String not written");
}

public void test_setVisibleZ() {
	warnUnimpl("Test test_setVisibleZ not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_custom_CCombo((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_addLjava_lang_String");
	methodNames.addElement("test_addLjava_lang_StringI");
	methodNames.addElement("test_addModifyListenerLorg_eclipse_swt_events_ModifyListener");
	methodNames.addElement("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_clearSelection");
	methodNames.addElement("test_computeSizeIIZ");
	methodNames.addElement("test_copy");
	methodNames.addElement("test_cut");
	methodNames.addElement("test_deselectAll");
	methodNames.addElement("test_deselectI");
	methodNames.addElement("test_getChildren");
	methodNames.addElement("test_getEditable");
	methodNames.addElement("test_getItemCount");
	methodNames.addElement("test_getItemHeight");
	methodNames.addElement("test_getItemI");
	methodNames.addElement("test_getItems");
	methodNames.addElement("test_getSelection");
	methodNames.addElement("test_getSelectionIndex");
	methodNames.addElement("test_getStyle");
	methodNames.addElement("test_getText");
	methodNames.addElement("test_getTextHeight");
	methodNames.addElement("test_getTextLimit");
	methodNames.addElement("test_indexOfLjava_lang_String");
	methodNames.addElement("test_indexOfLjava_lang_StringI");
	methodNames.addElement("test_isFocusControl");
	methodNames.addElement("test_paste");
	methodNames.addElement("test_redraw");
	methodNames.addElement("test_redrawIIIIZ");
	methodNames.addElement("test_removeAll");
	methodNames.addElement("test_removeI");
	methodNames.addElement("test_removeII");
	methodNames.addElement("test_removeLjava_lang_String");
	methodNames.addElement("test_removeModifyListenerLorg_eclipse_swt_events_ModifyListener");
	methodNames.addElement("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_selectI");
	methodNames.addElement("test_setBackgroundLorg_eclipse_swt_graphics_Color");
	methodNames.addElement("test_setEditableZ");
	methodNames.addElement("test_setEnabledZ");
	methodNames.addElement("test_setFocus");
	methodNames.addElement("test_setFontLorg_eclipse_swt_graphics_Font");
	methodNames.addElement("test_setForegroundLorg_eclipse_swt_graphics_Color");
	methodNames.addElement("test_setItemILjava_lang_String");
	methodNames.addElement("test_setItems$Ljava_lang_String");
	methodNames.addElement("test_setSelectionLorg_eclipse_swt_graphics_Point");
	methodNames.addElement("test_setTextLimitI");
	methodNames.addElement("test_setTextLjava_lang_String");
	methodNames.addElement("test_setToolTipTextLjava_lang_String");
	methodNames.addElement("test_setVisibleZ");
	methodNames.addElement("test_consistency_MouseSelection");
	methodNames.addElement("test_consistency_KeySelection");
	methodNames.addElement("test_consistency_EnterSelection");
	methodNames.addElement("test_consistency_MenuDetect");
	methodNames.addElement("test_consistency_DragDetect");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Composite.methodNames()); // add superclass method names
	return methodNames;
}

protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_addLjava_lang_String")) test_addLjava_lang_String();
	else if (getName().equals("test_addLjava_lang_StringI")) test_addLjava_lang_StringI();
	else if (getName().equals("test_addModifyListenerLorg_eclipse_swt_events_ModifyListener")) test_addModifyListenerLorg_eclipse_swt_events_ModifyListener();
	else if (getName().equals("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_clearSelection")) test_clearSelection();
	else if (getName().equals("test_computeSizeIIZ")) test_computeSizeIIZ();
	else if (getName().equals("test_copy")) test_copy();
	else if (getName().equals("test_cut")) test_cut();
	else if (getName().equals("test_deselectAll")) test_deselectAll();
	else if (getName().equals("test_deselectI")) test_deselectI();
	else if (getName().equals("test_getChildren")) test_getChildren();
	else if (getName().equals("test_getEditable")) test_getEditable();
	else if (getName().equals("test_getItemCount")) test_getItemCount();
	else if (getName().equals("test_getItemHeight")) test_getItemHeight();
	else if (getName().equals("test_getItemI")) test_getItemI();
	else if (getName().equals("test_getItems")) test_getItems();
	else if (getName().equals("test_getSelection")) test_getSelection();
	else if (getName().equals("test_getSelectionIndex")) test_getSelectionIndex();
	else if (getName().equals("test_getStyle")) test_getStyle();
	else if (getName().equals("test_getText")) test_getText();
	else if (getName().equals("test_getTextHeight")) test_getTextHeight();
	else if (getName().equals("test_getTextLimit")) test_getTextLimit();
	else if (getName().equals("test_indexOfLjava_lang_String")) test_indexOfLjava_lang_String();
	else if (getName().equals("test_indexOfLjava_lang_StringI")) test_indexOfLjava_lang_StringI();
	else if (getName().equals("test_isFocusControl")) test_isFocusControl();
	else if (getName().equals("test_paste")) test_paste();
	else if (getName().equals("test_redraw")) test_redraw();
	else if (getName().equals("test_redrawIIIIZ")) test_redrawIIIIZ();
	else if (getName().equals("test_removeAll")) test_removeAll();
	else if (getName().equals("test_removeI")) test_removeI();
	else if (getName().equals("test_removeII")) test_removeII();
	else if (getName().equals("test_removeLjava_lang_String")) test_removeLjava_lang_String();
	else if (getName().equals("test_removeModifyListenerLorg_eclipse_swt_events_ModifyListener")) test_removeModifyListenerLorg_eclipse_swt_events_ModifyListener();
	else if (getName().equals("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_selectI")) test_selectI();
	else if (getName().equals("test_setBackgroundLorg_eclipse_swt_graphics_Color")) test_setBackgroundLorg_eclipse_swt_graphics_Color();
	else if (getName().equals("test_setEditableZ")) test_setEditableZ();
	else if (getName().equals("test_setEnabledZ")) test_setEnabledZ();
	else if (getName().equals("test_setFocus")) test_setFocus();
	else if (getName().equals("test_setFontLorg_eclipse_swt_graphics_Font")) test_setFontLorg_eclipse_swt_graphics_Font();
	else if (getName().equals("test_setForegroundLorg_eclipse_swt_graphics_Color")) test_setForegroundLorg_eclipse_swt_graphics_Color();
	else if (getName().equals("test_setItemILjava_lang_String")) test_setItemILjava_lang_String();
	else if (getName().equals("test_setItems$Ljava_lang_String")) test_setItems$Ljava_lang_String();
	else if (getName().equals("test_setSelectionLorg_eclipse_swt_graphics_Point")) test_setSelectionLorg_eclipse_swt_graphics_Point();
	else if (getName().equals("test_setTextLimitI")) test_setTextLimitI();
	else if (getName().equals("test_setTextLjava_lang_String")) test_setTextLjava_lang_String();
	else if (getName().equals("test_setToolTipTextLjava_lang_String")) test_setToolTipTextLjava_lang_String();
	else if (getName().equals("test_setVisibleZ")) test_setVisibleZ();
	else if (getName().equals("test_consistency_MouseSelection")) test_consistency_MouseSelection();
	else if (getName().equals("test_consistency_KeySelection")) test_consistency_KeySelection();
	else if (getName().equals("test_consistency_EnterSelection")) test_consistency_EnterSelection();
	else if (getName().equals("test_consistency_MenuDetect")) test_consistency_MenuDetect();
	else if (getName().equals("test_consistency_DragDetect")) test_consistency_DragDetect();
	else super.runTest();
}

/* Custom */
CCombo ccombo;

private void add() {
    ccombo.add("this");
    ccombo.add("is");
    ccombo.add("SWT");
}

public void test_consistency_MouseSelection () {
    add();
    consistencyPrePackShell();
    consistencyEvent(ccombo.getSize().x-10, 5, 30, ccombo.getItemHeight()*2, 
            		 ConsistencyUtility.SELECTION);
}

public void test_consistency_KeySelection () {
    add();
    consistencyEvent(0, SWT.ARROW_DOWN, 0, 0, ConsistencyUtility.KEY_PRESS);
}

public void test_consistency_EnterSelection () {
    add();
    consistencyEvent(10, 13, 0, 0, ConsistencyUtility.KEY_PRESS);
}

public void test_consistency_MenuDetect () {
    add();
    consistencyPrePackShell();
    //on arrow
    consistencyEvent(ccombo.getSize().x-10, 5, 3, 0, ConsistencyUtility.MOUSE_CLICK);
    //on text
    consistencyEvent(10, 5, 3, ConsistencyUtility.ESCAPE_MENU, ConsistencyUtility.MOUSE_CLICK);
}

public void test_consistency_DragDetect () {
    add();
    consistencyEvent(10, 5, 20, 10, ConsistencyUtility.MOUSE_DRAG);
}

}

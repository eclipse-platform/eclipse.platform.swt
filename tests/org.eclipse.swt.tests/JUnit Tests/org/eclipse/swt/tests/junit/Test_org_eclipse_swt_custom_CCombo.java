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
}

protected void tearDown() {
	super.tearDown();
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_CompositeI not written");
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

public void test_deselectI() {
	warnUnimpl("Test test_deselectI not written");
}

public void test_deselectAll() {
	warnUnimpl("Test test_deselectAll not written");
}

public void test_getChildren() {
	warnUnimpl("Test test_getChildren not written");
}

public void test_getItemI() {
	warnUnimpl("Test test_getItemI not written");
}

public void test_getItemCount() {
	warnUnimpl("Test test_getItemCount not written");
}

public void test_getItemHeight() {
	warnUnimpl("Test test_getItemHeight not written");
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

public void test_redrawIIIIZ() {
	warnUnimpl("Test test_redrawIIIIZ not written");
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

public void test_removeAll() {
	warnUnimpl("Test test_removeAll not written");
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

public void test_setTextLjava_lang_String() {
	warnUnimpl("Test test_setTextLjava_lang_String not written");
}

public void test_setTextLimitI() {
	warnUnimpl("Test test_setTextLimitI not written");
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
	methodNames.addElement("test_deselectI");
	methodNames.addElement("test_deselectAll");
	methodNames.addElement("test_getChildren");
	methodNames.addElement("test_getItemI");
	methodNames.addElement("test_getItemCount");
	methodNames.addElement("test_getItemHeight");
	methodNames.addElement("test_getItems");
	methodNames.addElement("test_getSelection");
	methodNames.addElement("test_getSelectionIndex");
	methodNames.addElement("test_getText");
	methodNames.addElement("test_getTextHeight");
	methodNames.addElement("test_getTextLimit");
	methodNames.addElement("test_indexOfLjava_lang_String");
	methodNames.addElement("test_indexOfLjava_lang_StringI");
	methodNames.addElement("test_isFocusControl");
	methodNames.addElement("test_redrawIIIIZ");
	methodNames.addElement("test_removeI");
	methodNames.addElement("test_removeII");
	methodNames.addElement("test_removeLjava_lang_String");
	methodNames.addElement("test_removeAll");
	methodNames.addElement("test_removeModifyListenerLorg_eclipse_swt_events_ModifyListener");
	methodNames.addElement("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_selectI");
	methodNames.addElement("test_setBackgroundLorg_eclipse_swt_graphics_Color");
	methodNames.addElement("test_setFocus");
	methodNames.addElement("test_setFontLorg_eclipse_swt_graphics_Font");
	methodNames.addElement("test_setForegroundLorg_eclipse_swt_graphics_Color");
	methodNames.addElement("test_setItemILjava_lang_String");
	methodNames.addElement("test_setItems$Ljava_lang_String");
	methodNames.addElement("test_setSelectionLorg_eclipse_swt_graphics_Point");
	methodNames.addElement("test_setTextLjava_lang_String");
	methodNames.addElement("test_setTextLimitI");
	methodNames.addElement("test_setVisibleZ");
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
	else if (getName().equals("test_deselectI")) test_deselectI();
	else if (getName().equals("test_deselectAll")) test_deselectAll();
	else if (getName().equals("test_getChildren")) test_getChildren();
	else if (getName().equals("test_getItemI")) test_getItemI();
	else if (getName().equals("test_getItemCount")) test_getItemCount();
	else if (getName().equals("test_getItemHeight")) test_getItemHeight();
	else if (getName().equals("test_getItems")) test_getItems();
	else if (getName().equals("test_getSelection")) test_getSelection();
	else if (getName().equals("test_getSelectionIndex")) test_getSelectionIndex();
	else if (getName().equals("test_getText")) test_getText();
	else if (getName().equals("test_getTextHeight")) test_getTextHeight();
	else if (getName().equals("test_getTextLimit")) test_getTextLimit();
	else if (getName().equals("test_indexOfLjava_lang_String")) test_indexOfLjava_lang_String();
	else if (getName().equals("test_indexOfLjava_lang_StringI")) test_indexOfLjava_lang_StringI();
	else if (getName().equals("test_isFocusControl")) test_isFocusControl();
	else if (getName().equals("test_redrawIIIIZ")) test_redrawIIIIZ();
	else if (getName().equals("test_removeI")) test_removeI();
	else if (getName().equals("test_removeII")) test_removeII();
	else if (getName().equals("test_removeLjava_lang_String")) test_removeLjava_lang_String();
	else if (getName().equals("test_removeAll")) test_removeAll();
	else if (getName().equals("test_removeModifyListenerLorg_eclipse_swt_events_ModifyListener")) test_removeModifyListenerLorg_eclipse_swt_events_ModifyListener();
	else if (getName().equals("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_selectI")) test_selectI();
	else if (getName().equals("test_setBackgroundLorg_eclipse_swt_graphics_Color")) test_setBackgroundLorg_eclipse_swt_graphics_Color();
	else if (getName().equals("test_setFocus")) test_setFocus();
	else if (getName().equals("test_setFontLorg_eclipse_swt_graphics_Font")) test_setFontLorg_eclipse_swt_graphics_Font();
	else if (getName().equals("test_setForegroundLorg_eclipse_swt_graphics_Color")) test_setForegroundLorg_eclipse_swt_graphics_Color();
	else if (getName().equals("test_setItemILjava_lang_String")) test_setItemILjava_lang_String();
	else if (getName().equals("test_setItems$Ljava_lang_String")) test_setItems$Ljava_lang_String();
	else if (getName().equals("test_setSelectionLorg_eclipse_swt_graphics_Point")) test_setSelectionLorg_eclipse_swt_graphics_Point();
	else if (getName().equals("test_setTextLjava_lang_String")) test_setTextLjava_lang_String();
	else if (getName().equals("test_setTextLimitI")) test_setTextLimitI();
	else if (getName().equals("test_setVisibleZ")) test_setVisibleZ();
	else super.runTest();
}
}

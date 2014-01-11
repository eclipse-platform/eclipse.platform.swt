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

@Override
protected void setUp() {
	super.setUp();
	ccombo = new CCombo(shell, 0);
	setWidget(ccombo);
}

@Override
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

@Override
public void test_computeSizeIIZ() {
	warnUnimpl("Test test_computeSizeIIZ not written");
}

@Override
public void test_getChildren() {
	warnUnimpl("Test test_getChildren not written");
}

@Override
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

@Override
public void test_redraw() {
	warnUnimpl("Test test_redraw not written");
}

@Override
public void test_redrawIIIIZ() {
	warnUnimpl("Test test_redrawIIIIZ not written");
}

@Override
public void test_setBackgroundLorg_eclipse_swt_graphics_Color() {
	warnUnimpl("Test test_setBackgroundLorg_eclipse_swt_graphics_Color not written");
}

@Override
public void test_setEnabledZ() {
	warnUnimpl("Test test_setEnabledZ not written");
}

@Override
public void test_setFocus() {
	warnUnimpl("Test test_setFocus not written");
}

@Override
public void test_setFontLorg_eclipse_swt_graphics_Font() {
	warnUnimpl("Test test_setFontLorg_eclipse_swt_graphics_Font not written");
}

@Override
public void test_setForegroundLorg_eclipse_swt_graphics_Color() {
	warnUnimpl("Test test_setForegroundLorg_eclipse_swt_graphics_Color not written");
}

@Override
public void test_setToolTipTextLjava_lang_String() {
	warnUnimpl("Test test_setToolTipTextLjava_lang_String not written");
}

@Override
public void test_setVisibleZ() {
	warnUnimpl("Test test_setVisibleZ not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector<String> methodNames = methodNames();
	java.util.Enumeration<String> e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_custom_CCombo(e.nextElement()));
	}
	return suite;
}

public static java.util.Vector<String> methodNames() {
	java.util.Vector<String> methodNames = new java.util.Vector<String>();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_computeSizeIIZ");
	methodNames.addElement("test_copy");
	methodNames.addElement("test_cut");
	methodNames.addElement("test_getChildren");
	methodNames.addElement("test_isFocusControl");
	methodNames.addElement("test_paste");
	methodNames.addElement("test_redraw");
	methodNames.addElement("test_redrawIIIIZ");
	methodNames.addElement("test_setBackgroundLorg_eclipse_swt_graphics_Color");
	methodNames.addElement("test_setEnabledZ");
	methodNames.addElement("test_setFocus");
	methodNames.addElement("test_setFontLorg_eclipse_swt_graphics_Font");
	methodNames.addElement("test_setForegroundLorg_eclipse_swt_graphics_Color");
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

@Override
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_computeSizeIIZ")) test_computeSizeIIZ();
	else if (getName().equals("test_copy")) test_copy();
	else if (getName().equals("test_cut")) test_cut();
	else if (getName().equals("test_getChildren")) test_getChildren();
	else if (getName().equals("test_isFocusControl")) test_isFocusControl();
	else if (getName().equals("test_paste")) test_paste();
	else if (getName().equals("test_redraw")) test_redraw();
	else if (getName().equals("test_redrawIIIIZ")) test_redrawIIIIZ();
	else if (getName().equals("test_setBackgroundLorg_eclipse_swt_graphics_Color")) test_setBackgroundLorg_eclipse_swt_graphics_Color();
	else if (getName().equals("test_setEnabledZ")) test_setEnabledZ();
	else if (getName().equals("test_setFocus")) test_setFocus();
	else if (getName().equals("test_setFontLorg_eclipse_swt_graphics_Font")) test_setFontLorg_eclipse_swt_graphics_Font();
	else if (getName().equals("test_setForegroundLorg_eclipse_swt_graphics_Color")) test_setForegroundLorg_eclipse_swt_graphics_Color();
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

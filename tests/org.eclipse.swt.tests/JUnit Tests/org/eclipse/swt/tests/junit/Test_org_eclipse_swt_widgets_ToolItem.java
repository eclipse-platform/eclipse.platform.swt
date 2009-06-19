/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
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
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.ToolItem
 *
 * @see org.eclipse.swt.widgets.ToolItem
 */
public class Test_org_eclipse_swt_widgets_ToolItem extends Test_org_eclipse_swt_widgets_Item {

public Test_org_eclipse_swt_widgets_ToolItem(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	toolBar = new ToolBar(shell, 0);
	toolItem = new ToolItem(toolBar, 0); 
	setWidget(toolItem);
}

public void test_ConstructorLorg_eclipse_swt_widgets_ToolBarI() {
	try {
		new ToolItem(null, SWT.NULL);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_ConstructorLorg_eclipse_swt_widgets_ToolBarII() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_ToolBarII not written");
}

public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	warnUnimpl("Test test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener not written");
}

public void test_getBounds() {
	warnUnimpl("Test test_getBounds not written");
}

public void test_getControl() {
	warnUnimpl("Test test_getControl not written");
}

public void test_getDisabledImage() {
	warnUnimpl("Test test_getDisabledImage not written");
}

public void test_getEnabled() {
	warnUnimpl("Test test_getEnabled not written");
}

public void test_getHotImage() {
	warnUnimpl("Test test_getHotImage not written");
}

public void test_getParent() {
	warnUnimpl("Test test_getParent not written");
}

public void test_getSelection() {
	// Test for method boolean org.eclipse.swt.widgets.ToolItem.getSelection()
	warnUnimpl( "Test Test_org_eclipse_swt_widgets_ToolItem.test_5_getSelection not written");
}

public void test_getToolTipText() {
	toolItem.setToolTipText("fred");
	assertTrue(":a: ", toolItem.getToolTipText().equals("fred"));
	toolItem.setToolTipText("fredttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt");
	assertTrue(":a: ", toolItem.getToolTipText().equals("fredttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt"));
}

public void test_getWidth() {
	warnUnimpl("Test test_getWidth not written");
}

public void test_isEnabled() {
	// Test for method boolean org.eclipse.swt.widgets.ToolItem.isEnabled()
	warnUnimpl( "Test Test_org_eclipse_swt_widgets_ToolItem.test_7_isEnabled not written");
}

public void test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	warnUnimpl("Test test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener not written");
}

public void test_setControlLorg_eclipse_swt_widgets_Control() {
	warnUnimpl("Test test_setControlLorg_eclipse_swt_widgets_Control not written");
}

public void test_setDisabledImageLorg_eclipse_swt_graphics_Image() {
	warnUnimpl("Test test_setDisabledImageLorg_eclipse_swt_graphics_Image not written");
}

public void test_setEnabledZ() {
	// Test for method void org.eclipse.swt.widgets.ToolItem.setEnabled(boolean)
	warnUnimpl( "Test Test_org_eclipse_swt_widgets_ToolItem.test_8_setEnabled not written");
}

public void test_setHotImageLorg_eclipse_swt_graphics_Image() {
	warnUnimpl("Test test_setHotImageLorg_eclipse_swt_graphics_Image not written");
}

public void test_setImageLorg_eclipse_swt_graphics_Image() {
	warnUnimpl("Test test_setImageLorg_eclipse_swt_graphics_Image not written");
}

public void test_setSelectionZ() {
	warnUnimpl("Test test_setSelectionZ not written");
}

public void test_setTextLjava_lang_String() {
	warnUnimpl("Test test_setTextLjava_lang_String not written");
}

public void test_setToolTipTextLjava_lang_String() {
	warnUnimpl("Test test_setToolTipTextLjava_lang_String not written");
}

public void test_setWidthI() {
	warnUnimpl("Test test_setWidthI not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_ToolItem((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_ToolBarI");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_ToolBarII");
	methodNames.addElement("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_getBounds");
	methodNames.addElement("test_getControl");
	methodNames.addElement("test_getDisabledImage");
	methodNames.addElement("test_getEnabled");
	methodNames.addElement("test_getHotImage");
	methodNames.addElement("test_getParent");
	methodNames.addElement("test_getSelection");
	methodNames.addElement("test_getToolTipText");
	methodNames.addElement("test_getWidth");
	methodNames.addElement("test_isEnabled");
	methodNames.addElement("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_setControlLorg_eclipse_swt_widgets_Control");
	methodNames.addElement("test_setDisabledImageLorg_eclipse_swt_graphics_Image");
	methodNames.addElement("test_setEnabledZ");
	methodNames.addElement("test_setHotImageLorg_eclipse_swt_graphics_Image");
	methodNames.addElement("test_setImageLorg_eclipse_swt_graphics_Image");
	methodNames.addElement("test_setSelectionZ");
	methodNames.addElement("test_setTextLjava_lang_String");
	methodNames.addElement("test_setToolTipTextLjava_lang_String");
	methodNames.addElement("test_setWidthI");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Item.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_ToolBarI")) test_ConstructorLorg_eclipse_swt_widgets_ToolBarI();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_ToolBarII")) test_ConstructorLorg_eclipse_swt_widgets_ToolBarII();
	else if (getName().equals("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_getBounds")) test_getBounds();
	else if (getName().equals("test_getControl")) test_getControl();
	else if (getName().equals("test_getDisabledImage")) test_getDisabledImage();
	else if (getName().equals("test_getEnabled")) test_getEnabled();
	else if (getName().equals("test_getHotImage")) test_getHotImage();
	else if (getName().equals("test_getParent")) test_getParent();
	else if (getName().equals("test_getSelection")) test_getSelection();
	else if (getName().equals("test_getToolTipText")) test_getToolTipText();
	else if (getName().equals("test_getWidth")) test_getWidth();
	else if (getName().equals("test_isEnabled")) test_isEnabled();
	else if (getName().equals("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_setControlLorg_eclipse_swt_widgets_Control")) test_setControlLorg_eclipse_swt_widgets_Control();
	else if (getName().equals("test_setDisabledImageLorg_eclipse_swt_graphics_Image")) test_setDisabledImageLorg_eclipse_swt_graphics_Image();
	else if (getName().equals("test_setEnabledZ")) test_setEnabledZ();
	else if (getName().equals("test_setHotImageLorg_eclipse_swt_graphics_Image")) test_setHotImageLorg_eclipse_swt_graphics_Image();
	else if (getName().equals("test_setImageLorg_eclipse_swt_graphics_Image")) test_setImageLorg_eclipse_swt_graphics_Image();
	else if (getName().equals("test_setSelectionZ")) test_setSelectionZ();
	else if (getName().equals("test_setTextLjava_lang_String")) test_setTextLjava_lang_String();
	else if (getName().equals("test_setToolTipTextLjava_lang_String")) test_setToolTipTextLjava_lang_String();
	else if (getName().equals("test_setWidthI")) test_setWidthI();
	else super.runTest();
}

/* custom */
ToolBar toolBar;
ToolItem toolItem;
}

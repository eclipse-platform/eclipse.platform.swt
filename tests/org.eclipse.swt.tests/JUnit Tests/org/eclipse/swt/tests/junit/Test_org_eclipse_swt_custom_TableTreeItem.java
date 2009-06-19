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


import org.eclipse.swt.custom.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.TableTreeItem
 *
 * @see org.eclipse.swt.custom.TableTreeItem
 */
public class Test_org_eclipse_swt_custom_TableTreeItem extends Test_org_eclipse_swt_widgets_Item {

	TableTree tableTree;
	TableTreeItem tableTreeItem;
	
public Test_org_eclipse_swt_custom_TableTreeItem(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	tableTree = new TableTree(shell, 0);
	tableTreeItem = new TableTreeItem(tableTree, 0);
	setWidget(tableTreeItem);
}

public void test_ConstructorLorg_eclipse_swt_custom_TableTreeI() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_custom_TableTreeI not written");
}

public void test_ConstructorLorg_eclipse_swt_custom_TableTreeII() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_custom_TableTreeII not written");
}

public void test_ConstructorLorg_eclipse_swt_custom_TableTreeItemI() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_custom_TableTreeItemI not written");
}

public void test_ConstructorLorg_eclipse_swt_custom_TableTreeItemII() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_custom_TableTreeItemII not written");
}

public void test_getBackground() {
	warnUnimpl("Test test_getBackground not written");
}

public void test_getBoundsI() {
	warnUnimpl("Test test_getBoundsI not written");
}

public void test_getChecked() {
	warnUnimpl("Test test_getChecked not written");
}

public void test_getExpanded() {
	warnUnimpl("Test test_getExpanded not written");
}

public void test_getForeground() {
	warnUnimpl("Test test_getForeground not written");
}

public void test_getGrayed() {
	warnUnimpl("Test test_getGrayed not written");
}

public void test_getImage() {
	warnUnimpl("Test test_getImage not written");
}

public void test_getImageI() {
	warnUnimpl("Test test_getImageI not written");
}

public void test_getItemCount() {
	warnUnimpl("Test test_getItemCount not written");
}

public void test_getItems() {
	warnUnimpl("Test test_getItems not written");
}

public void test_getParent() {
	warnUnimpl("Test test_getParent not written");
}

public void test_getParentItem() {
	warnUnimpl("Test test_getParentItem not written");
}

public void test_getText() {
	warnUnimpl("Test test_getText not written");
}

public void test_getTextI() {
	warnUnimpl("Test test_getTextI not written");
}

public void test_indexOfLorg_eclipse_swt_custom_TableTreeItem() {
	warnUnimpl("Test test_indexOfLorg_eclipse_swt_custom_TableTreeItem not written");
}

public void test_setBackgroundLorg_eclipse_swt_graphics_Color() {
	warnUnimpl("Test test_setBackgroundLorg_eclipse_swt_graphics_Color not written");
}

public void test_setCheckedZ() {
	warnUnimpl("Test test_setCheckedZ not written");
}

public void test_setExpandedZ() {
	warnUnimpl("Test test_setExpandedZ not written");
}

public void test_setForegroundLorg_eclipse_swt_graphics_Color() {
	warnUnimpl("Test test_setForegroundLorg_eclipse_swt_graphics_Color not written");
}

public void test_setGrayedZ() {
	warnUnimpl("Test test_setGrayedZ not written");
}

public void test_setImageILorg_eclipse_swt_graphics_Image() {
	warnUnimpl("Test test_setImageILorg_eclipse_swt_graphics_Image not written");
}

public void test_setImageLorg_eclipse_swt_graphics_Image() {
	warnUnimpl("Test test_setImageLorg_eclipse_swt_graphics_Image not written");
}

public void test_setTextILjava_lang_String() {
	warnUnimpl("Test test_setTextILjava_lang_String not written");
}

public void test_setTextLjava_lang_String() {
	warnUnimpl("Test test_setTextLjava_lang_String not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_custom_TableTreeItem((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_custom_TableTreeI");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_custom_TableTreeII");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_custom_TableTreeItemI");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_custom_TableTreeItemII");
	methodNames.addElement("test_getBackground");
	methodNames.addElement("test_getBoundsI");
	methodNames.addElement("test_getChecked");
	methodNames.addElement("test_getExpanded");
	methodNames.addElement("test_getForeground");
	methodNames.addElement("test_getGrayed");
	methodNames.addElement("test_getImage");
	methodNames.addElement("test_getImageI");
	methodNames.addElement("test_getItemCount");
	methodNames.addElement("test_getItems");
	methodNames.addElement("test_getParent");
	methodNames.addElement("test_getParentItem");
	methodNames.addElement("test_getText");
	methodNames.addElement("test_getTextI");
	methodNames.addElement("test_indexOfLorg_eclipse_swt_custom_TableTreeItem");
	methodNames.addElement("test_setBackgroundLorg_eclipse_swt_graphics_Color");
	methodNames.addElement("test_setCheckedZ");
	methodNames.addElement("test_setExpandedZ");
	methodNames.addElement("test_setForegroundLorg_eclipse_swt_graphics_Color");
	methodNames.addElement("test_setGrayedZ");
	methodNames.addElement("test_setImageILorg_eclipse_swt_graphics_Image");
	methodNames.addElement("test_setImageLorg_eclipse_swt_graphics_Image");
	methodNames.addElement("test_setTextILjava_lang_String");
	methodNames.addElement("test_setTextLjava_lang_String");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Item.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_custom_TableTreeI")) test_ConstructorLorg_eclipse_swt_custom_TableTreeI();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_custom_TableTreeII")) test_ConstructorLorg_eclipse_swt_custom_TableTreeII();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_custom_TableTreeItemI")) test_ConstructorLorg_eclipse_swt_custom_TableTreeItemI();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_custom_TableTreeItemII")) test_ConstructorLorg_eclipse_swt_custom_TableTreeItemII();
	else if (getName().equals("test_getBackground")) test_getBackground();
	else if (getName().equals("test_getBoundsI")) test_getBoundsI();
	else if (getName().equals("test_getChecked")) test_getChecked();
	else if (getName().equals("test_getExpanded")) test_getExpanded();
	else if (getName().equals("test_getForeground")) test_getForeground();
	else if (getName().equals("test_getGrayed")) test_getGrayed();
	else if (getName().equals("test_getImage")) test_getImage();
	else if (getName().equals("test_getImageI")) test_getImageI();
	else if (getName().equals("test_getItemCount")) test_getItemCount();
	else if (getName().equals("test_getItems")) test_getItems();
	else if (getName().equals("test_getParent")) test_getParent();
	else if (getName().equals("test_getParentItem")) test_getParentItem();
	else if (getName().equals("test_getText")) test_getText();
	else if (getName().equals("test_getTextI")) test_getTextI();
	else if (getName().equals("test_indexOfLorg_eclipse_swt_custom_TableTreeItem")) test_indexOfLorg_eclipse_swt_custom_TableTreeItem();
	else if (getName().equals("test_setBackgroundLorg_eclipse_swt_graphics_Color")) test_setBackgroundLorg_eclipse_swt_graphics_Color();
	else if (getName().equals("test_setCheckedZ")) test_setCheckedZ();
	else if (getName().equals("test_setExpandedZ")) test_setExpandedZ();
	else if (getName().equals("test_setForegroundLorg_eclipse_swt_graphics_Color")) test_setForegroundLorg_eclipse_swt_graphics_Color();
	else if (getName().equals("test_setGrayedZ")) test_setGrayedZ();
	else if (getName().equals("test_setImageILorg_eclipse_swt_graphics_Image")) test_setImageILorg_eclipse_swt_graphics_Image();
	else if (getName().equals("test_setImageLorg_eclipse_swt_graphics_Image")) test_setImageLorg_eclipse_swt_graphics_Image();
	else if (getName().equals("test_setTextILjava_lang_String")) test_setTextILjava_lang_String();
	else if (getName().equals("test_setTextLjava_lang_String")) test_setTextLjava_lang_String();
	else super.runTest();
}
}

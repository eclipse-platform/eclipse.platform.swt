package org.eclipse.swt.tests.junit;

/*
 * (c) Copyright IBM Corp. 2000, 2002. All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.custom.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.CLabel
 *
 * @see org.eclipse.swt.custom.CLabel
 */
public class Test_org_eclipse_swt_custom_CLabel extends Test_org_eclipse_swt_widgets_Canvas {

public Test_org_eclipse_swt_custom_CLabel(String name) {
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

public void test_computeSizeIIZ() {
	warnUnimpl("Test test_computeSizeIIZ not written");
}

public void test_getAlignment() {
	warnUnimpl("Test test_getAlignment not written");
}

public void test_getImage() {
	warnUnimpl("Test test_getImage not written");
}

public void test_setToolTipTextLjava_lang_String() {
	warnUnimpl("Test test_setToolTipTextLjava_lang_String not written");
}

public void test_getText() {
	warnUnimpl("Test test_getText not written");
}

public void test_getToolTipText() {
	warnUnimpl("Test test_getToolTipText not written");
}

public void test_setAlignmentI() {
	warnUnimpl("Test test_setAlignmentI not written");
}

public void test_setBackground$Lorg_eclipse_swt_graphics_Color$I() {
	warnUnimpl("Test test_setBackground$Lorg_eclipse_swt_graphics_Color$I not written");
}

public void test_setBackgroundLorg_eclipse_swt_graphics_Color() {
	warnUnimpl("Test test_setBackgroundLorg_eclipse_swt_graphics_Color not written");
}

public void test_setBackgroundLorg_eclipse_swt_graphics_Image() {
	warnUnimpl("Test test_setBackgroundLorg_eclipse_swt_graphics_Image not written");
}

public void test_setFontLorg_eclipse_swt_graphics_Font() {
	warnUnimpl("Test test_setFontLorg_eclipse_swt_graphics_Font not written");
}

public void test_setImageLorg_eclipse_swt_graphics_Image() {
	warnUnimpl("Test test_setImageLorg_eclipse_swt_graphics_Image not written");
}

public void test_setTextLjava_lang_String() {
	warnUnimpl("Test test_setTextLjava_lang_String not written");
}

public void test_shortenTextLorg_eclipse_swt_graphics_GCLjava_lang_StringI() {
	warnUnimpl("Test test_shortenTextLorg_eclipse_swt_graphics_GCLjava_lang_StringI not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_custom_CLabel((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_computeSizeIIZ");
	methodNames.addElement("test_getAlignment");
	methodNames.addElement("test_getImage");
	methodNames.addElement("test_setToolTipTextLjava_lang_String");
	methodNames.addElement("test_getText");
	methodNames.addElement("test_getToolTipText");
	methodNames.addElement("test_setAlignmentI");
	methodNames.addElement("test_setBackground$Lorg_eclipse_swt_graphics_Color$I");
	methodNames.addElement("test_setBackgroundLorg_eclipse_swt_graphics_Color");
	methodNames.addElement("test_setBackgroundLorg_eclipse_swt_graphics_Image");
	methodNames.addElement("test_setFontLorg_eclipse_swt_graphics_Font");
	methodNames.addElement("test_setImageLorg_eclipse_swt_graphics_Image");
	methodNames.addElement("test_setTextLjava_lang_String");
	methodNames.addElement("test_shortenTextLorg_eclipse_swt_graphics_GCLjava_lang_StringI");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Canvas.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_computeSizeIIZ")) test_computeSizeIIZ();
	else if (getName().equals("test_getAlignment")) test_getAlignment();
	else if (getName().equals("test_getImage")) test_getImage();
	else if (getName().equals("test_setToolTipTextLjava_lang_String")) test_setToolTipTextLjava_lang_String();
	else if (getName().equals("test_getText")) test_getText();
	else if (getName().equals("test_getToolTipText")) test_getToolTipText();
	else if (getName().equals("test_setAlignmentI")) test_setAlignmentI();
	else if (getName().equals("test_setBackground$Lorg_eclipse_swt_graphics_Color$I")) test_setBackground$Lorg_eclipse_swt_graphics_Color$I();
	else if (getName().equals("test_setBackgroundLorg_eclipse_swt_graphics_Color")) test_setBackgroundLorg_eclipse_swt_graphics_Color();
	else if (getName().equals("test_setBackgroundLorg_eclipse_swt_graphics_Image")) test_setBackgroundLorg_eclipse_swt_graphics_Image();
	else if (getName().equals("test_setFontLorg_eclipse_swt_graphics_Font")) test_setFontLorg_eclipse_swt_graphics_Font();
	else if (getName().equals("test_setImageLorg_eclipse_swt_graphics_Image")) test_setImageLorg_eclipse_swt_graphics_Image();
	else if (getName().equals("test_setTextLjava_lang_String")) test_setTextLjava_lang_String();
	else if (getName().equals("test_shortenTextLorg_eclipse_swt_graphics_GCLjava_lang_StringI")) test_shortenTextLorg_eclipse_swt_graphics_GCLjava_lang_StringI();
	else super.runTest();
}
}

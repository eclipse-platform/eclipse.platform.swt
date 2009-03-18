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

import junit.framework.*;
import junit.textui.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Label
 *
 * @see org.eclipse.swt.widgets.Label
 */
public class Test_org_eclipse_swt_widgets_Label extends Test_org_eclipse_swt_widgets_Control {

public Test_org_eclipse_swt_widgets_Label(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	label = new Label(shell, 0);
	setWidget(label);
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI(){
	try {
		label = new Label(null, 0);
		fail("No exception thrown"); //should never get here
	}
	catch (IllegalArgumentException e) {
	}

	label = new Label(shell, 0);

	int[] cases = {SWT.LEFT, SWT.RIGHT, SWT.CENTER, SWT.SEPARATOR, SWT.HORIZONTAL, SWT.VERTICAL, SWT.SHADOW_IN, SWT.SHADOW_OUT};
	for (int i = 0; i < cases.length; i++)
		label = new Label(shell, cases[i]);
}

public void test_computeSizeIIZ() {
	// super class test is sufficient
}

public void test_getAlignment(){
	int[] cases = {SWT.LEFT, SWT.RIGHT, SWT.CENTER};
	for (int i=0; i<cases.length; i++)
	{
 	  label = new Label(shell, cases[i]);
	  assertEquals(label.getAlignment(), cases[i]);
	} 
}

public void test_getImage(){
	Image[] cases = {null, new Image(null, 100, 100)};
	for(int i=0; i<cases.length; i++){
	 label.setImage(cases[i]);
	 assertEquals(label.getImage(), cases[i]);
	 if (cases[i]!=null)
	  cases[i].dispose();
	}
}

public void test_getText(){
	String[] cases = {"", "some name", "sdasdlkjshcdascecoewcwe"};
	for(int i=0; i<cases.length; i++){
	 label.setText(cases[i]);
	 assertEquals(label.getText(), cases[i]);
	}
}

public void test_setAlignmentI(){
	int[] cases = {SWT.LEFT, SWT.RIGHT, SWT.CENTER};
	for (int i=0; i<cases.length; i++)
	{
 	  label.setAlignment(cases[i]);
	  assertEquals(label.getAlignment(), cases[i]);
	} 
}

public void test_setFocus() {
	// super class test is sufficient
}

public void test_setImageLorg_eclipse_swt_graphics_Image() {
	// tested in getImage method
}

public void test_setTextLjava_lang_String(){
	try {
		label.setText(null);
		fail("No exception thrown for string == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_Label((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_computeSizeIIZ");
	methodNames.addElement("test_getAlignment");
	methodNames.addElement("test_getImage");
	methodNames.addElement("test_getText");
	methodNames.addElement("test_setAlignmentI");
	methodNames.addElement("test_setFocus");
	methodNames.addElement("test_setImageLorg_eclipse_swt_graphics_Image");
	methodNames.addElement("test_setTextLjava_lang_String");
	methodNames.addElement("test_consistency_MenuDetect");
	methodNames.addElement("test_consistency_DragDetect");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Control.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_computeSizeIIZ")) test_computeSizeIIZ();
	else if (getName().equals("test_getAlignment")) test_getAlignment();
	else if (getName().equals("test_getImage")) test_getImage();
	else if (getName().equals("test_getText")) test_getText();
	else if (getName().equals("test_setAlignmentI")) test_setAlignmentI();
	else if (getName().equals("test_setFocus")) test_setFocus();
	else if (getName().equals("test_setImageLorg_eclipse_swt_graphics_Image")) test_setImageLorg_eclipse_swt_graphics_Image();
	else if (getName().equals("test_setTextLjava_lang_String")) test_setTextLjava_lang_String();
	else if (getName().equals("test_consistency_MenuDetect")) test_consistency_MenuDetect();
	else if (getName().equals("test_consistency_DragDetect")) test_consistency_DragDetect();
	else super.runTest();
}

/* custom */
Label label;

public void test_consistency_MenuDetect () {
    consistencyEvent(10, 5, 3, 0, ConsistencyUtility.MOUSE_CLICK);
}

public void test_consistency_DragDetect () {
    consistencyEvent(10, 5, 20, 10, ConsistencyUtility.MOUSE_DRAG);
}
}

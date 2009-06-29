/* Copyright (c) 2000, 2006 IBM Corporation and others.
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
public class Test_org_eclipse_swt_widgets_ExpandItem extends Test_org_eclipse_swt_widgets_Item {

public Test_org_eclipse_swt_widgets_ExpandItem(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	expandBar = new ExpandBar(shell, 0);
	expandItem = new ExpandItem(expandBar, 0); 
	setWidget(expandItem);
}

public void test_ConstructorLorg_eclipse_swt_widgets_ExpandItemI() {
	try {
		new ExpandItem(null, SWT.NULL);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_ConstructorLorg_eclipse_swt_widgets_ExpandItemII() {
	ExpandItem item = new ExpandItem(expandBar, SWT.NULL, 0); //create an expand item at index 0
	assertNotNull(item);
	assertTrue(expandBar.getItem(0).equals(item));
	item = new ExpandItem(expandBar, SWT.NULL, 1);
	assertNotNull(item);
	assertTrue(expandBar.getItem(1).equals(item));
}

public void test_getControl() {
	assertNull(expandItem.getControl());

	Button button = new Button(expandBar, SWT.PUSH);
	expandItem.setControl(button);
	Control control = expandItem.getControl();
	assertEquals(button, control);

	button = new Button(expandBar, SWT.PUSH);
	expandItem.setControl(button);
	control = expandItem.getControl();
	assertEquals(button, control);
}

public void test_getExpanded() {
//tested in test_setExpandedZ()
}

public void test_getHeaderHeight() {
	warnUnimpl("Test test_getHeaderHeight not written");
}

public void test_getHeight() {
//tested in test_setHeightI()	
}

public void test_getParent() {	
	assertEquals(expandItem.getParent(), expandBar);
}

public void test_setControlLorg_eclipse_swt_widgets_Control() {
	expandItem.setControl(null);	
	Button button = new Button(expandBar, SWT.PUSH);
	expandItem.setControl(button);
	
	button = new Button(expandBar, SWT.PUSH);
	button.dispose();
	try {
		expandItem.setControl(button);
		fail("No exception when control.isDisposed()");
	}
	catch (IllegalArgumentException e) {
	}
	
	button = new Button(shell, SWT.PUSH);
	try {
		expandItem.setControl(button);
		fail("No exception thrown when control has wrong parent");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_setExpandedZ() {
	expandItem.setExpanded(true);
	assertTrue(expandItem.getExpanded());
	expandItem.setExpanded(false);
	assertEquals(expandItem.getExpanded(), false);
}

public void test_setHeightI() {
	expandItem.setHeight(30);
	assertEquals(expandItem.getHeight(), 30);
	expandItem.setHeight(-8);
	assertEquals(expandItem.getHeight(), 30);
}

public void test_setImageLorg_eclipse_swt_graphics_Image() {
	assertNull(expandItem.getImage());
	expandItem.setImage(images[0]);
	assertEquals(images[0], expandItem.getImage());
	assertTrue(expandItem.getImage() != images[1]);
	expandItem.setImage(null);
	assertNull(expandItem.getImage());
}

public void test_setTextLjava_lang_String() {
	expandItem.setText("ABCDEFG");
	assertTrue(expandItem.getText().equals("ABCDEFG"));
	try {
		expandItem.setText(null);
		fail("No exception thrown for addArmListener with null argument");
	} catch (IllegalArgumentException e) {
	}
	expandItem.setText("ABCDEFG");
	assertTrue(expandItem.getText().startsWith("ABCDEFG"));
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_ExpandItem((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_ExpandItemI");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_ExpandItemII");
	methodNames.addElement("test_getControl");
	methodNames.addElement("test_getExpanded");
	methodNames.addElement("test_getHeaderHeight");
	methodNames.addElement("test_getHeight");
	methodNames.addElement("test_getParent");
	methodNames.addElement("test_setControlLorg_eclipse_swt_widgets_Control");
	methodNames.addElement("test_setExpandedZ");
	methodNames.addElement("test_setHeightI");
	methodNames.addElement("test_setImageLorg_eclipse_swt_graphics_Image");
	methodNames.addElement("test_setTextLjava_lang_String");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Item.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_ExpandBarI")) test_ConstructorLorg_eclipse_swt_widgets_ExpandItemI();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_ExpandBarII")) test_ConstructorLorg_eclipse_swt_widgets_ExpandItemII();
	else if (getName().equals("test_getControl")) test_getControl();
	else if (getName().equals("test_getExpanded")) test_getExpanded();
	else if (getName().equals("test_getHeaderHeight")) test_getHeaderHeight();
	else if (getName().equals("test_getHeight")) test_getHeight();
	else if (getName().equals("test_getParent")) test_getParent();
	else if (getName().equals("test_setControlLorg_eclipse_swt_widgets_Control")) test_setControlLorg_eclipse_swt_widgets_Control();
	else if (getName().equals("test_setExpandedZ")) test_setExpandedZ();
	else if (getName().equals("test_setHeightI")) test_setHeightI();
	else if (getName().equals("test_setImageLorg_eclipse_swt_graphics_Image")) test_setImageLorg_eclipse_swt_graphics_Image();
	else if (getName().equals("test_setTextLjava_lang_String")) test_setTextLjava_lang_String();
	super.runTest();
}

/* custom */
ExpandBar expandBar;
ExpandItem expandItem;
}


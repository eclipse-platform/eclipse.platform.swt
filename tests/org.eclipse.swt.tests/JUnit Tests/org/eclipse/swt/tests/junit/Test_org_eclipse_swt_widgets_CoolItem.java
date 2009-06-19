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


import junit.framework.*;
import junit.textui.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.CoolItem
 *
 * @see org.eclipse.swt.widgets.CoolItem
 */
public class Test_org_eclipse_swt_widgets_CoolItem extends Test_org_eclipse_swt_widgets_Item {

public Test_org_eclipse_swt_widgets_CoolItem(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	CoolBar coolBar = new CoolBar(shell, 0);
	CoolItem coolItem = new CoolItem(coolBar, 0);
	setWidget(coolItem);
}

public void test_ConstructorLorg_eclipse_swt_widgets_CoolBarI() {
	CoolBar coolBar = new CoolBar(shell, 0);
	new CoolItem(coolBar, 0);
	
	try {
		new CoolItem(null, 0);
		fail("No exception thrown for parent == null");	
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_ConstructorLorg_eclipse_swt_widgets_CoolBarII() {
	CoolBar coolBar = new CoolBar(shell, 0);
	CoolItem coolItem = new CoolItem(coolBar, 0, 0);
	try {
		coolItem = new CoolItem(coolBar, 0, -1);
		fail("No exception thrown for index == -1");
	}
	catch (IllegalArgumentException e){
	}
	try {
		coolItem = new CoolItem(coolBar, 0, 2);
		fail("No exception thrown for index == 2");
	}
	catch (IllegalArgumentException e){
	}
	assertEquals(1, coolBar.getItemCount());
	coolItem = new CoolItem(coolBar, 0, 1);
	assertEquals(2, coolBar.getItemCount());
	coolItem = new CoolItem(coolBar, 0, 0);
	assertEquals(3, coolBar.getItemCount());
	assertEquals(coolItem, coolBar.getItem(0));	
}

public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	warnUnimpl("Test test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener not written");
}

public void test_computeSizeII() {
	CoolBar coolBar = new CoolBar(shell, 0);
	CoolItem coolItem = new CoolItem(coolBar, 0);
	Button button = new Button(coolBar, SWT.PUSH);
	button.setText("foo");

	Point size = coolItem.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	coolItem.setControl(button);
	Point size2 = coolItem.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	assertTrue(size2.x == size.x);

	size = coolItem.computeSize(50, 25);
	size2 = coolItem.computeSize(100, 25);
	assertEquals(size.x + 50, size2.x);
	assertEquals(size.y, size2.y);

	size = coolItem.computeSize(1,1);
	size2 = coolItem.computeSize(26, 26);
	assertEquals(25, size2.x - size.x);
}

public void test_getBounds() {
	CoolBar coolBar = new CoolBar(shell, 0);
	CoolItem coolItem = new CoolItem(coolBar, 0);
	Button button = new Button(coolBar, SWT.PUSH);
	button.setText("foo");
	coolItem.setControl(button);
	
	Rectangle rect = coolItem.getBounds();
	Point size = coolItem.getSize();
	assertEquals(size.x, rect.width);
	assertEquals(size.y, rect.height);
	
	coolItem.setSize(25, 25);
	rect = coolItem.getBounds();
	coolItem.setSize(100, 25);
	Rectangle newRect = coolItem.getBounds();
	assertEquals(rect.width + 75, newRect.width);
	assertEquals(rect.x, newRect.x);
	assertEquals(rect.y, newRect.y);
}

public void test_getControl() {
	CoolBar coolBar = new CoolBar(shell, 0);
	CoolItem coolItem = new CoolItem(coolBar, 0);
	assertNull(coolItem.getControl());

	Button button = new Button(coolBar, SWT.PUSH);
	coolItem.setControl(button);
	Control control = coolItem.getControl();
	assertEquals(button, control);

	button = new Button(coolBar, SWT.PUSH);
	coolItem.setControl(button);
	control = coolItem.getControl();
	assertEquals(button, control);
}

public void test_getMinimumSize() {
	warnUnimpl("Test test_getMinimumSize not written");
}

public void test_getParent() {
	CoolBar coolBar = new CoolBar(shell, 0);
	CoolItem coolItem = new CoolItem(coolBar, 0);
	assertEquals(coolBar, coolItem.getParent());
}

public void test_getPreferredSize() {
	CoolBar coolBar = new CoolBar(shell, 0);
	CoolItem coolItem = new CoolItem(coolBar, 0);
	Button button = new Button(coolBar, SWT.PUSH);
	button.setText("foobar");
	coolItem.setControl(button);
	
	Point pref = coolItem.getPreferredSize();
	coolItem.setPreferredSize(pref);
	assertEquals(pref, coolItem.getPreferredSize());
}

public void test_getSize() {
	CoolBar coolBar = new CoolBar(shell, 0);
	CoolItem coolItem = new CoolItem(coolBar, 0);
	Button button = new Button(coolBar, SWT.PUSH);
	button.setText("foo");
	coolItem.setControl(button);
	
	Point size = coolItem.getSize();
	Rectangle rect = coolItem.getBounds();
	assertEquals(rect.width, size.x);
	assertEquals(rect.height, size.y);
}

public void test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	warnUnimpl("Test test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener not written");
}

public void test_setControlLorg_eclipse_swt_widgets_Control() {
	CoolBar coolBar = new CoolBar(shell, 0);
	CoolItem coolItem = new CoolItem(coolBar, 0);
	coolItem.setControl(null);
	
	Button button = new Button(coolBar, SWT.PUSH);
	button.setText("foobar");
	
	Point size = coolItem.getSize();
	coolItem.setControl(button);
	Point size2 = coolItem.getSize();
	assertTrue(size2.x > size.x);
	
	if (!MINIMAL_CONFORMANCE) {
		size = button.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		size2 = coolItem.computeSize(size.x, size.y);
		assertEquals(size2, coolItem.getSize());
	}
	
	button = new Button(coolBar, SWT.PUSH);
	button.dispose();
	try {
		coolItem.setControl(button);
		fail("No exception when control.isDisposed()");
	}
	catch (IllegalArgumentException e) {
	}
	
	button = new Button(shell, SWT.PUSH);
	try {
		coolItem.setControl(button);
		fail("No exception thrown when control has wrong parent");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_setMinimumSizeII() {
	warnUnimpl("Test test_setMinimumSizeII not written");
}

public void test_setMinimumSizeLorg_eclipse_swt_graphics_Point() {
	warnUnimpl("Test test_setMinimumSizeLorg_eclipse_swt_graphics_Point not written");
}

public void test_setPreferredSizeII() {
	CoolBar coolBar = new CoolBar(shell, 0);
	CoolItem coolItem = new CoolItem(coolBar, 0);
	Button button = new Button(coolBar, SWT.PUSH);
	button.setText("foobar");
	coolItem.setControl(button);
	
	Point size = coolItem.getSize();
	coolItem.setPreferredSize(size);
	assertEquals(size.x, coolItem.getSize().x);
	coolItem.setSize(coolItem.getPreferredSize());
	assertEquals(size, coolItem.getSize());
}

public void test_setPreferredSizeLorg_eclipse_swt_graphics_Point() {
	CoolBar coolBar = new CoolBar(shell, 0);
	CoolItem coolItem = new CoolItem(coolBar, 0);
	Button button = new Button(coolBar, SWT.PUSH);
	button.setText("foobar");
	coolItem.setControl(button);
	
	Point size = new Point(50, 30);
	coolItem.setPreferredSize(size);
	Point size2 = coolItem.getPreferredSize();
	coolItem.setPreferredSize(50, 30);
	assertEquals(size2, coolItem.getPreferredSize());
}

public void test_setSizeII() {
	CoolBar coolBar = new CoolBar(shell, 0);
	CoolItem coolItem = new CoolItem(coolBar, 0);
	Button button = new Button(coolBar, SWT.PUSH);
	button.setText("foo");
	coolItem.setControl(button);

	coolItem.setSize(50, 50);
	assertEquals(new Point(50, 50), coolItem.getSize());

	coolItem.setSize(0, 0);
	Point smallest = coolItem.getSize();
	coolItem.setSize(1, 1);
	assertEquals(smallest, coolItem.getSize());
	
	Rectangle rect = coolItem.getBounds();
	Point size = coolItem.getSize();
	coolItem.setSize(rect.width, rect.height);
	assertEquals(size, coolItem.getSize());
}

public void test_setSizeLorg_eclipse_swt_graphics_Point() {
	CoolBar coolBar = new CoolBar(shell, 0);
	CoolItem coolItem = new CoolItem(coolBar, 0);
	Button button = new Button(coolBar, SWT.PUSH);
	button.setText("foo");
	coolItem.setControl(button);
	
	Point size = new Point(50, 50);
	coolItem.setSize(size);
	Point size2 = coolItem.getSize();
	coolItem.setSize(50, 50);
	assertEquals(size2, coolItem.getSize());
}


public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_CoolItem((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CoolBarI");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CoolBarII");
	methodNames.addElement("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_computeSizeII");
	methodNames.addElement("test_getBounds");
	methodNames.addElement("test_getControl");
	methodNames.addElement("test_getMinimumSize");
	methodNames.addElement("test_getParent");
	methodNames.addElement("test_getPreferredSize");
	methodNames.addElement("test_getSize");
	methodNames.addElement("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_setControlLorg_eclipse_swt_widgets_Control");
	methodNames.addElement("test_setMinimumSizeII");
	methodNames.addElement("test_setMinimumSizeLorg_eclipse_swt_graphics_Point");
	methodNames.addElement("test_setPreferredSizeII");
	methodNames.addElement("test_setPreferredSizeLorg_eclipse_swt_graphics_Point");
	methodNames.addElement("test_setSizeII");
	methodNames.addElement("test_setSizeLorg_eclipse_swt_graphics_Point");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Item.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CoolBarI")) test_ConstructorLorg_eclipse_swt_widgets_CoolBarI();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CoolBarII")) test_ConstructorLorg_eclipse_swt_widgets_CoolBarII();
	else if (getName().equals("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_computeSizeII")) test_computeSizeII();
	else if (getName().equals("test_getBounds")) test_getBounds();
	else if (getName().equals("test_getControl")) test_getControl();
	else if (getName().equals("test_getMinimumSize")) test_getMinimumSize();
	else if (getName().equals("test_getParent")) test_getParent();
	else if (getName().equals("test_getPreferredSize")) test_getPreferredSize();
	else if (getName().equals("test_getSize")) test_getSize();
	else if (getName().equals("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_setControlLorg_eclipse_swt_widgets_Control")) test_setControlLorg_eclipse_swt_widgets_Control();
	else if (getName().equals("test_setMinimumSizeII")) test_setMinimumSizeII();
	else if (getName().equals("test_setMinimumSizeLorg_eclipse_swt_graphics_Point")) test_setMinimumSizeLorg_eclipse_swt_graphics_Point();
	else if (getName().equals("test_setPreferredSizeII")) test_setPreferredSizeII();
	else if (getName().equals("test_setPreferredSizeLorg_eclipse_swt_graphics_Point")) test_setPreferredSizeLorg_eclipse_swt_graphics_Point();
	else if (getName().equals("test_setSizeII")) test_setSizeII();
	else if (getName().equals("test_setSizeLorg_eclipse_swt_graphics_Point")) test_setSizeLorg_eclipse_swt_graphics_Point();
	else super.runTest();
}

/* custom */
static final boolean MINIMAL_CONFORMANCE = false;

}

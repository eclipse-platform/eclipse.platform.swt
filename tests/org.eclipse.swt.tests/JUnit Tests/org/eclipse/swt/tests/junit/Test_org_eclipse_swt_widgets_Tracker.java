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
import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Tracker
 *
 * @see org.eclipse.swt.widgets.Tracker
 */
public class Test_org_eclipse_swt_widgets_Tracker extends Test_org_eclipse_swt_widgets_Widget {
	
public Test_org_eclipse_swt_widgets_Tracker(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	tracker = new Tracker(shell, 0);
	setWidget(tracker);
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_CompositeI not written");
}

public void test_ConstructorLorg_eclipse_swt_widgets_DisplayI() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_DisplayI not written");
}

public void test_addControlListenerLorg_eclipse_swt_events_ControlListener() {
	warnUnimpl("Test test_addControlListenerLorg_eclipse_swt_events_ControlListener not written");
}

public void test_close() {
	warnUnimpl("Test test_close not written");
}

public void test_getRectangles() {
	warnUnimpl("Test test_getRectangles not written");
}

public void test_getStippled() {
	warnUnimpl("Test test_getStippled not written");
}

public void test_open() {
	warnUnimpl("Test test_open not written");
}

public void test_removeControlListenerLorg_eclipse_swt_events_ControlListener() {
	warnUnimpl("Test test_removeControlListenerLorg_eclipse_swt_events_ControlListener not written");
}

public void test_setCursorLorg_eclipse_swt_graphics_Cursor() {
	warnUnimpl("Test test_setCursorLorg_eclipse_swt_graphics_Cursor not written");
}

public void test_setRectangles$Lorg_eclipse_swt_graphics_Rectangle() {
	warnUnimpl("Test test_setRectangles$Lorg_eclipse_swt_graphics_Rectangle not written");
}

public void test_setStippledZ() {
	warnUnimpl("Test test_setStippledZ not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_Tracker((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_DisplayI");
	methodNames.addElement("test_addControlListenerLorg_eclipse_swt_events_ControlListener");
	methodNames.addElement("test_close");
	methodNames.addElement("test_getRectangles");
	methodNames.addElement("test_getStippled");
	methodNames.addElement("test_open");
	methodNames.addElement("test_removeControlListenerLorg_eclipse_swt_events_ControlListener");
	methodNames.addElement("test_setCursorLorg_eclipse_swt_graphics_Cursor");
	methodNames.addElement("test_setRectangles$Lorg_eclipse_swt_graphics_Rectangle");
	methodNames.addElement("test_setStippledZ");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Widget.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_DisplayI")) test_ConstructorLorg_eclipse_swt_widgets_DisplayI();
	else if (getName().equals("test_addControlListenerLorg_eclipse_swt_events_ControlListener")) test_addControlListenerLorg_eclipse_swt_events_ControlListener();
	else if (getName().equals("test_close")) test_close();
	else if (getName().equals("test_getRectangles")) test_getRectangles();
	else if (getName().equals("test_getStippled")) test_getStippled();
	else if (getName().equals("test_open")) test_open();
	else if (getName().equals("test_removeControlListenerLorg_eclipse_swt_events_ControlListener")) test_removeControlListenerLorg_eclipse_swt_events_ControlListener();
	else if (getName().equals("test_setCursorLorg_eclipse_swt_graphics_Cursor")) test_setCursorLorg_eclipse_swt_graphics_Cursor();
	else if (getName().equals("test_setRectangles$Lorg_eclipse_swt_graphics_Rectangle")) test_setRectangles$Lorg_eclipse_swt_graphics_Rectangle();
	else if (getName().equals("test_setStippledZ")) test_setStippledZ();
	else super.runTest();
}

/* custom */
	Tracker tracker;
}

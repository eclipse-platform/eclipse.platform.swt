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
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Sash
 *
 * @see org.eclipse.swt.widgets.Sash
 */
public class Test_org_eclipse_swt_widgets_Sash extends Test_org_eclipse_swt_widgets_Control {

public Test_org_eclipse_swt_widgets_Sash(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	sash = new Sash(shell, 0);
	setWidget(sash);
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	try {
		sash = new Sash(null, 0);
		fail("No exception thrown"); //should never get here
	}
	catch (IllegalArgumentException e) {
	}

	int[] cases = {0, SWT.HORIZONTAL, SWT.VERTICAL};
	for (int i = 0; i < cases.length; i++)
		sash = new Sash(shell, cases[i]);
}

public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	warnUnimpl("Test test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener not written");
}

public void test_computeSizeIIZ() {
	warnUnimpl("Test test_computeSizeIIZ not written");
}

public void test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	warnUnimpl("Test test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener not written");
}

public void test_setFontLorg_eclipse_swt_graphics_Font() {
	// overridden from Control because it does not make sense
	// to set the font of a Sash.
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_Sash((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_computeSizeIIZ");
	methodNames.addElement("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_consistency_MenuDetect");
	methodNames.addElement("test_consistency_DragDetect");
	methodNames.addElement("test_consistency_MouseSelection");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Control.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_computeSizeIIZ")) test_computeSizeIIZ();
	else if (getName().equals("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_consistency_MenuDetect")) test_consistency_MenuDetect();
	else if (getName().equals("test_consistency_DragDetect")) test_consistency_DragDetect();
	else if (getName().equals("test_consistency_MouseSelection")) test_consistency_MouseSelection();
	else super.runTest();
}

/* custom */
Sash sash;

private void createSash() {
    tearDown();
    super.setUp();
    new Button(shell, SWT.PUSH);
    Sash sash = new Sash(shell, SWT.VERTICAL);
    new Button(shell, SWT.PUSH);
    setWidget(sash);
}

public void test_consistency_MenuDetect () {
    createSash();
    consistencyEvent(0, 5, 3, 0, ConsistencyUtility.MOUSE_CLICK);
}

public void test_consistency_DragDetect () {
    createSash();
    consistencyEvent(0, 5, 20, 10, ConsistencyUtility.MOUSE_DRAG);
}

public void test_consistency_MouseSelection() {
    createSash();
    consistencyEvent(0, 5, 1, 0, ConsistencyUtility.MOUSE_CLICK);
}

}

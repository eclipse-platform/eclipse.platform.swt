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


/**
 * Automated Test Suite for class org.eclipse.swt.events.ControlListener
 *
 * @see org.eclipse.swt.events.ControlListener
 */
public class Test_org_eclipse_swt_events_ControlListener extends SwtTestCase {

public Test_org_eclipse_swt_events_ControlListener(String name) {
	super(name);
}


protected void setUp() {
}

protected void tearDown() {
}

public void test_controlMovedLorg_eclipse_swt_events_ControlEvent() {
	warnUnimpl("Test test_controlMovedLorg_eclipse_swt_events_ControlEvent not written");
}

public void test_controlResizedLorg_eclipse_swt_events_ControlEvent() {
	warnUnimpl("Test test_controlResizedLorg_eclipse_swt_events_ControlEvent not written");
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_controlMovedLorg_eclipse_swt_events_ControlEvent");
	methodNames.addElement("test_controlResizedLorg_eclipse_swt_events_ControlEvent");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_controlMovedLorg_eclipse_swt_events_ControlEvent")) test_controlMovedLorg_eclipse_swt_events_ControlEvent();
	else if (getName().equals("test_controlResizedLorg_eclipse_swt_events_ControlEvent")) test_controlResizedLorg_eclipse_swt_events_ControlEvent();
}
}

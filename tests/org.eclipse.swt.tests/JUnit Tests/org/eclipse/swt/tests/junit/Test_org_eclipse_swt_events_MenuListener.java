package org.eclipse.swt.tests.junit;

/*
 * (c) Copyright IBM Corp. 2000, 2002. All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.events.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.events.MenuListener
 *
 * @see org.eclipse.swt.events.MenuListener
 */
public class Test_org_eclipse_swt_events_MenuListener extends SwtTestCase {

public Test_org_eclipse_swt_events_MenuListener(String name) {
	super(name);
}


protected void setUp() {
}

protected void tearDown() {
}

public void test_menuHiddenLorg_eclipse_swt_events_MenuEvent() {
	warnUnimpl("Test test_menuHiddenLorg_eclipse_swt_events_MenuEvent not written");
}

public void test_menuShownLorg_eclipse_swt_events_MenuEvent() {
	warnUnimpl("Test test_menuShownLorg_eclipse_swt_events_MenuEvent not written");
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_menuHiddenLorg_eclipse_swt_events_MenuEvent");
	methodNames.addElement("test_menuShownLorg_eclipse_swt_events_MenuEvent");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_menuHiddenLorg_eclipse_swt_events_MenuEvent")) test_menuHiddenLorg_eclipse_swt_events_MenuEvent();
	else if (getName().equals("test_menuShownLorg_eclipse_swt_events_MenuEvent")) test_menuShownLorg_eclipse_swt_events_MenuEvent();
}
}

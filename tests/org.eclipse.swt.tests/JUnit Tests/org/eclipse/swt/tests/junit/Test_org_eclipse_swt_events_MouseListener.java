package org.eclipse.swt.tests.junit;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.events.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.events.MouseListener
 *
 * @see org.eclipse.swt.events.MouseListener
 */
public class Test_org_eclipse_swt_events_MouseListener extends SwtTestCase {

public Test_org_eclipse_swt_events_MouseListener(String name) {
	super(name);
}


protected void setUp() {
}

protected void tearDown() {
}

public void test_mouseDoubleClickLorg_eclipse_swt_events_MouseEvent() {
	warnUnimpl("Test test_mouseDoubleClickLorg_eclipse_swt_events_MouseEvent not written");
}

public void test_mouseDownLorg_eclipse_swt_events_MouseEvent() {
	warnUnimpl("Test test_mouseDownLorg_eclipse_swt_events_MouseEvent not written");
}

public void test_mouseUpLorg_eclipse_swt_events_MouseEvent() {
	warnUnimpl("Test test_mouseUpLorg_eclipse_swt_events_MouseEvent not written");
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_mouseDoubleClickLorg_eclipse_swt_events_MouseEvent");
	methodNames.addElement("test_mouseDownLorg_eclipse_swt_events_MouseEvent");
	methodNames.addElement("test_mouseUpLorg_eclipse_swt_events_MouseEvent");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_mouseDoubleClickLorg_eclipse_swt_events_MouseEvent")) test_mouseDoubleClickLorg_eclipse_swt_events_MouseEvent();
	else if (getName().equals("test_mouseDownLorg_eclipse_swt_events_MouseEvent")) test_mouseDownLorg_eclipse_swt_events_MouseEvent();
	else if (getName().equals("test_mouseUpLorg_eclipse_swt_events_MouseEvent")) test_mouseUpLorg_eclipse_swt_events_MouseEvent();
}
}

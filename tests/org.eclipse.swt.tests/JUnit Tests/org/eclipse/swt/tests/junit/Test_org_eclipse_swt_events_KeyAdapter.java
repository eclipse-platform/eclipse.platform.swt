package org.eclipse.swt.tests.junit;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.events.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.events.KeyAdapter
 *
 * @see org.eclipse.swt.events.KeyAdapter
 */
public class Test_org_eclipse_swt_events_KeyAdapter extends SwtTestCase {

public Test_org_eclipse_swt_events_KeyAdapter(String name) {
	super(name);
}


protected void setUp() {
}

protected void tearDown() {
}

public void test_Constructor() {
	warnUnimpl("Test test_Constructor not written");
}

public void test_keyPressedLorg_eclipse_swt_events_KeyEvent() {
	warnUnimpl("Test test_keyPressedLorg_eclipse_swt_events_KeyEvent not written");
}

public void test_keyReleasedLorg_eclipse_swt_events_KeyEvent() {
	warnUnimpl("Test test_keyReleasedLorg_eclipse_swt_events_KeyEvent not written");
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor");
	methodNames.addElement("test_keyPressedLorg_eclipse_swt_events_KeyEvent");
	methodNames.addElement("test_keyReleasedLorg_eclipse_swt_events_KeyEvent");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_keyPressedLorg_eclipse_swt_events_KeyEvent")) test_keyPressedLorg_eclipse_swt_events_KeyEvent();
	else if (getName().equals("test_keyReleasedLorg_eclipse_swt_events_KeyEvent")) test_keyReleasedLorg_eclipse_swt_events_KeyEvent();
}
}

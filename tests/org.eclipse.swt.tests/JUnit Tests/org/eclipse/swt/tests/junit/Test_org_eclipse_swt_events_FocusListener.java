package org.eclipse.swt.tests.junit;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.events.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.events.FocusListener
 *
 * @see org.eclipse.swt.events.FocusListener
 */
public class Test_org_eclipse_swt_events_FocusListener extends SwtTestCase {

public Test_org_eclipse_swt_events_FocusListener(String name) {
	super(name);
}


protected void setUp() {
}

protected void tearDown() {
}

public void test_focusGainedLorg_eclipse_swt_events_FocusEvent() {
	warnUnimpl("Test test_focusGainedLorg_eclipse_swt_events_FocusEvent not written");
}

public void test_focusLostLorg_eclipse_swt_events_FocusEvent() {
	warnUnimpl("Test test_focusLostLorg_eclipse_swt_events_FocusEvent not written");
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_focusGainedLorg_eclipse_swt_events_FocusEvent");
	methodNames.addElement("test_focusLostLorg_eclipse_swt_events_FocusEvent");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_focusGainedLorg_eclipse_swt_events_FocusEvent")) test_focusGainedLorg_eclipse_swt_events_FocusEvent();
	else if (getName().equals("test_focusLostLorg_eclipse_swt_events_FocusEvent")) test_focusLostLorg_eclipse_swt_events_FocusEvent();
}
}

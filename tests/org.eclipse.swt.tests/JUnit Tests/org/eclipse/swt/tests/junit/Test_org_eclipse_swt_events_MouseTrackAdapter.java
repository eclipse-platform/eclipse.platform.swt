package org.eclipse.swt.tests.junit;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.events.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.events.MouseTrackAdapter
 *
 * @see org.eclipse.swt.events.MouseTrackAdapter
 */
public class Test_org_eclipse_swt_events_MouseTrackAdapter extends SwtTestCase {

public Test_org_eclipse_swt_events_MouseTrackAdapter(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
}

protected void tearDown() {
}

public void test_Constructor() {
	warnUnimpl("Test test_Constructor not written");
}

public void test_mouseEnterLorg_eclipse_swt_events_MouseEvent() {
	warnUnimpl("Test test_mouseEnterLorg_eclipse_swt_events_MouseEvent not written");
}

public void test_mouseExitLorg_eclipse_swt_events_MouseEvent() {
	warnUnimpl("Test test_mouseExitLorg_eclipse_swt_events_MouseEvent not written");
}

public void test_mouseHoverLorg_eclipse_swt_events_MouseEvent() {
	warnUnimpl("Test test_mouseHoverLorg_eclipse_swt_events_MouseEvent not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_events_MouseTrackAdapter((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor");
	methodNames.addElement("test_mouseEnterLorg_eclipse_swt_events_MouseEvent");
	methodNames.addElement("test_mouseExitLorg_eclipse_swt_events_MouseEvent");
	methodNames.addElement("test_mouseHoverLorg_eclipse_swt_events_MouseEvent");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_mouseEnterLorg_eclipse_swt_events_MouseEvent")) test_mouseEnterLorg_eclipse_swt_events_MouseEvent();
	else if (getName().equals("test_mouseExitLorg_eclipse_swt_events_MouseEvent")) test_mouseExitLorg_eclipse_swt_events_MouseEvent();
	else if (getName().equals("test_mouseHoverLorg_eclipse_swt_events_MouseEvent")) test_mouseHoverLorg_eclipse_swt_events_MouseEvent();
}
}

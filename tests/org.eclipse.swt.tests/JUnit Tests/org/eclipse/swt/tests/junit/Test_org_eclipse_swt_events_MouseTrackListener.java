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

/**
 * Automated Test Suite for class org.eclipse.swt.events.MouseTrackListener
 *
 * @see org.eclipse.swt.events.MouseTrackListener
 */
public class Test_org_eclipse_swt_events_MouseTrackListener extends SwtTestCase {

public Test_org_eclipse_swt_events_MouseTrackListener(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
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
		suite.addTest(new Test_org_eclipse_swt_events_MouseTrackListener((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_mouseEnterLorg_eclipse_swt_events_MouseEvent");
	methodNames.addElement("test_mouseExitLorg_eclipse_swt_events_MouseEvent");
	methodNames.addElement("test_mouseHoverLorg_eclipse_swt_events_MouseEvent");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_mouseEnterLorg_eclipse_swt_events_MouseEvent")) test_mouseEnterLorg_eclipse_swt_events_MouseEvent();
	else if (getName().equals("test_mouseExitLorg_eclipse_swt_events_MouseEvent")) test_mouseExitLorg_eclipse_swt_events_MouseEvent();
	else if (getName().equals("test_mouseHoverLorg_eclipse_swt_events_MouseEvent")) test_mouseHoverLorg_eclipse_swt_events_MouseEvent();
}
}

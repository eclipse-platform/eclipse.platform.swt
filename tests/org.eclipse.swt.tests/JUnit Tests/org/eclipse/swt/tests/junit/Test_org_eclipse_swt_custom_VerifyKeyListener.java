package org.eclipse.swt.tests.junit;

/*
 * (c) Copyright IBM Corp. 2000, 2002. All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.custom.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.VerifyKeyListener
 *
 * @see org.eclipse.swt.custom.VerifyKeyListener
 */
public class Test_org_eclipse_swt_custom_VerifyKeyListener extends SwtTestCase {
	Shell shell;
	StyledText styledText;
	int verify = -1;

public Test_org_eclipse_swt_custom_VerifyKeyListener(String name) {
	super(name);
}
public static void main(String[] args) {
	TestRunner.run(suite());
}
protected void setUp() {
	shell = new Shell();
	styledText = new StyledText(shell, SWT.NULL);
	shell.open();
}
public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_custom_VerifyKeyListener((String)e.nextElement()));
	}
	return suite;
}
protected void tearDown() {
}

public void test_verifyKeyLorg_eclipse_swt_events_VerifyEvent() {
	VerifyKeyListener listener = new VerifyKeyListener() {
		public void verifyKey(VerifyEvent event) {
			if (verify != 1) {event.doit = false;}
		}
	};
	styledText.addVerifyKeyListener(listener);
	verify = 1;
	Event e = new Event();
	e.character = 'a';
	styledText.notifyListeners(SWT.KeyDown, e);
	assertTrue(":1:", styledText.getText().equals("a"));

	verify = 2;
	styledText.setText("");
	e = new Event();
	e.character = 'a';
	styledText.notifyListeners(SWT.KeyDown, e);
	assertTrue(":2:", styledText.getText().equals(""));
	styledText.removeVerifyKeyListener(listener);
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_verifyKeyLorg_eclipse_swt_events_VerifyEvent");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_verifyKeyLorg_eclipse_swt_events_VerifyEvent")) test_verifyKeyLorg_eclipse_swt_events_VerifyEvent();
}
}

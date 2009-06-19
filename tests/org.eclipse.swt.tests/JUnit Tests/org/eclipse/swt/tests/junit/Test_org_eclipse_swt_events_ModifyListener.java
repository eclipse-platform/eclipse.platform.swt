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
 * Automated Test Suite for class org.eclipse.swt.events.ModifyListener
 *
 * @see org.eclipse.swt.events.ModifyListener
 */
public class Test_org_eclipse_swt_events_ModifyListener extends SwtTestCase {

public Test_org_eclipse_swt_events_ModifyListener(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void test_modifyTextLorg_eclipse_swt_events_ModifyEvent() {
	warnUnimpl("Test test_modifyTextLorg_eclipse_swt_events_ModifyEvent not written");
}


public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_events_ModifyListener((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_modifyTextLorg_eclipse_swt_events_ModifyEvent");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_modifyTextLorg_eclipse_swt_events_ModifyEvent")) test_modifyTextLorg_eclipse_swt_events_ModifyEvent();
}
}

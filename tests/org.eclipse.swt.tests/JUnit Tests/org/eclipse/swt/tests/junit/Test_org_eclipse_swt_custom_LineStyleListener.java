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


import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.custom.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.LineStyleListener
 *
 * @see org.eclipse.swt.custom.LineStyleListener
 */
public class Test_org_eclipse_swt_custom_LineStyleListener extends SwtTestCase {
	Shell shell;
	StyledText styledText;

public Test_org_eclipse_swt_custom_LineStyleListener(String name) {
	super(name);
}
public static void main(String[] args) {
	TestRunner.run(suite());
}
protected void setUp() {
	super.setUp();
	shell = new Shell();
	styledText = new StyledText(shell, SWT.NULL);
	shell.open();
}

public void test_lineGetStyleLorg_eclipse_swt_custom_LineStyleEvent() {
	LineStyleListener listener = new LineStyleListener() {
		public void lineGetStyle(LineStyleEvent event) {
			assertTrue(":1:", event.lineOffset==0);
			assertTrue(":2:",event.lineText.equals("0123456789"));
		}
	};
	styledText.addLineStyleListener(listener);
	styledText.setText("0123456789");
	// force get line styles callback
	styledText.getLocationAtOffset(5);
	styledText.removeLineStyleListener(listener);
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_custom_LineStyleListener((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_lineGetStyleLorg_eclipse_swt_custom_LineStyleEvent");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_lineGetStyleLorg_eclipse_swt_custom_LineStyleEvent")) test_lineGetStyleLorg_eclipse_swt_custom_LineStyleEvent();
}
}

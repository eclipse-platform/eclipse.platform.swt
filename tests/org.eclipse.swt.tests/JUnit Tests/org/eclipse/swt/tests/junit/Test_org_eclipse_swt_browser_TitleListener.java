/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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
import org.eclipse.swt.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.browser.TitleListener
 *
 * @see org.eclipse.swt.browser.TitleListener
 */
public class Test_org_eclipse_swt_browser_TitleListener extends SwtTestCase {

public Test_org_eclipse_swt_browser_TitleListener(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void test_changedLorg_eclipse_swt_browser_TitleEvent() {
	Display display = Display.getCurrent();
	Shell shell = new Shell(display);
	Browser browser = new Browser(shell, SWT.NONE);
	browser.addTitleListener(new TitleListener() {
		public void changed(TitleEvent event) {
		}
	});
	shell.close();
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_browser_TitleListener((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_changedLorg_eclipse_swt_browser_TitleEvent");
	return methodNames;
}

protected void runTest() throws Throwable {
	if (getName().equals("test_changedLorg_eclipse_swt_browser_TitleEvent")) test_changedLorg_eclipse_swt_browser_TitleEvent();
}
}

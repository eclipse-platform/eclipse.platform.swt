/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Synchronizer
 *
 * @see org.eclipse.swt.widgets.Synchronizer
 */
public class Test_org_eclipse_swt_widgets_Synchronizer extends SwtTestCase {

public Test_org_eclipse_swt_widgets_Synchronizer(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
}

protected void tearDown() {
}

public void test_ConstructorLorg_eclipse_swt_widgets_Display() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_Display not written");
}

public void test_asyncExecLjava_lang_Runnable() {
	warnUnimpl("Test test_asyncExecLjava_lang_Runnable not written");
}

public void test_syncExecLjava_lang_Runnable() {
	warnUnimpl("Test test_syncExecLjava_lang_Runnable not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_Synchronizer((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_Display");
	methodNames.addElement("test_asyncExecLjava_lang_Runnable");
	methodNames.addElement("test_syncExecLjava_lang_Runnable");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_Display")) test_ConstructorLorg_eclipse_swt_widgets_Display();
	else if (getName().equals("test_asyncExecLjava_lang_Runnable")) test_asyncExecLjava_lang_Runnable();
	else if (getName().equals("test_syncExecLjava_lang_Runnable")) test_syncExecLjava_lang_Runnable();
}
}

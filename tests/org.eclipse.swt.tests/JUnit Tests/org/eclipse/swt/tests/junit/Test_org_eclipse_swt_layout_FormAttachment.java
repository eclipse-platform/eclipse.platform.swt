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
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;

/**
 * Automated Test Suite for class org.eclipse.swt.layout.FormAttachment
 *
 * @see org.eclipse.swt.layout.FormAttachment
 */
public class Test_org_eclipse_swt_layout_FormAttachment extends SwtTestCase {

public Test_org_eclipse_swt_layout_FormAttachment(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	shell = new Shell();
}

protected void tearDown() {
	shell.dispose();
	super.tearDown();
}

public void test_ConstructorI() {
	FormAttachment attachment = new FormAttachment(50);
	assertNotNull(attachment);
}

public void test_ConstructorII() {
	FormAttachment attachment = new FormAttachment(50, 10);
	assertNotNull(attachment);
}

public void test_ConstructorIII() {
	FormAttachment attachment = new FormAttachment(50, 100, 10);
	assertNotNull(attachment);
}

public void test_ConstructorLorg_eclipse_swt_widgets_Control() {
	FormAttachment attachment = new FormAttachment(shell);
	assertNotNull(attachment);
}

public void test_ConstructorLorg_eclipse_swt_widgets_ControlI() {
	FormAttachment attachment = new FormAttachment(shell, 10);
	assertNotNull(attachment);
}

public void test_ConstructorLorg_eclipse_swt_widgets_ControlII() {
	FormAttachment attachment = new FormAttachment(shell, 10, SWT.LEFT);
	assertNotNull(attachment);
}

public void test_toString() {
	FormAttachment attachment = new FormAttachment(50);
	assertNotNull(attachment.toString());
	assertTrue(attachment.toString().length() > 0);
}


public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_layout_FormAttachment((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorI");
	methodNames.addElement("test_ConstructorII");
	methodNames.addElement("test_ConstructorIII");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_Control");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_ControlI");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_ControlII");
	methodNames.addElement("test_toString");
	return methodNames;
}

protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorI")) test_ConstructorI();
	else if (getName().equals("test_ConstructorII")) test_ConstructorII();
	else if (getName().equals("test_ConstructorIII")) test_ConstructorIII();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_Control")) test_ConstructorLorg_eclipse_swt_widgets_Control();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_ControlI")) test_ConstructorLorg_eclipse_swt_widgets_ControlI();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_ControlII")) test_ConstructorLorg_eclipse_swt_widgets_ControlII();
	else if (getName().equals("test_toString")) test_toString();
}

/* custom */
public Shell shell;
}

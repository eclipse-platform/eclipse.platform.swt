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
 * Automated Test Suite for class org.eclipse.swt.dnd.DropTargetAdapter
 *
 * @see org.eclipse.swt.dnd.DropTargetAdapter
 */
public class Test_org_eclipse_swt_dnd_DropTargetAdapter extends SwtTestCase {

public Test_org_eclipse_swt_dnd_DropTargetAdapter(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void test_Constructor() {
	warnUnimpl("Test test_Constructor not written");
}

public void test_dragEnterLorg_eclipse_swt_dnd_DropTargetEvent() {
	warnUnimpl("Test test_dragEnterLorg_eclipse_swt_dnd_DropTargetEvent not written");
}

public void test_dragLeaveLorg_eclipse_swt_dnd_DropTargetEvent() {
	warnUnimpl("Test test_dragLeaveLorg_eclipse_swt_dnd_DropTargetEvent not written");
}

public void test_dragOperationChangedLorg_eclipse_swt_dnd_DropTargetEvent() {
	warnUnimpl("Test test_dragOperationChangedLorg_eclipse_swt_dnd_DropTargetEvent not written");
}

public void test_dragOverLorg_eclipse_swt_dnd_DropTargetEvent() {
	warnUnimpl("Test test_dragOverLorg_eclipse_swt_dnd_DropTargetEvent not written");
}

public void test_dropAcceptLorg_eclipse_swt_dnd_DropTargetEvent() {
	warnUnimpl("Test test_dropAcceptLorg_eclipse_swt_dnd_DropTargetEvent not written");
}

public void test_dropLorg_eclipse_swt_dnd_DropTargetEvent() {
	warnUnimpl("Test test_dropLorg_eclipse_swt_dnd_DropTargetEvent not written");
}


public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_dnd_DropTargetAdapter((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor");
	methodNames.addElement("test_dragEnterLorg_eclipse_swt_dnd_DropTargetEvent");
	methodNames.addElement("test_dragLeaveLorg_eclipse_swt_dnd_DropTargetEvent");
	methodNames.addElement("test_dragOperationChangedLorg_eclipse_swt_dnd_DropTargetEvent");
	methodNames.addElement("test_dragOverLorg_eclipse_swt_dnd_DropTargetEvent");
	methodNames.addElement("test_dropAcceptLorg_eclipse_swt_dnd_DropTargetEvent");
	methodNames.addElement("test_dropLorg_eclipse_swt_dnd_DropTargetEvent");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_dragEnterLorg_eclipse_swt_dnd_DropTargetEvent")) test_dragEnterLorg_eclipse_swt_dnd_DropTargetEvent();
	else if (getName().equals("test_dragLeaveLorg_eclipse_swt_dnd_DropTargetEvent")) test_dragLeaveLorg_eclipse_swt_dnd_DropTargetEvent();
	else if (getName().equals("test_dragOperationChangedLorg_eclipse_swt_dnd_DropTargetEvent")) test_dragOperationChangedLorg_eclipse_swt_dnd_DropTargetEvent();
	else if (getName().equals("test_dragOverLorg_eclipse_swt_dnd_DropTargetEvent")) test_dragOverLorg_eclipse_swt_dnd_DropTargetEvent();
	else if (getName().equals("test_dropAcceptLorg_eclipse_swt_dnd_DropTargetEvent")) test_dropAcceptLorg_eclipse_swt_dnd_DropTargetEvent();
	else if (getName().equals("test_dropLorg_eclipse_swt_dnd_DropTargetEvent")) test_dropLorg_eclipse_swt_dnd_DropTargetEvent();
}
}

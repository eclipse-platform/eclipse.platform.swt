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


/**
 * Automated Test Suite for class org.eclipse.swt.graphics.Drawable
 *
 * @see org.eclipse.swt.graphics.Drawable
 */
public class Test_org_eclipse_swt_graphics_Drawable extends SwtTestCase {

public Test_org_eclipse_swt_graphics_Drawable(String name) {
	super(name);
}


protected void setUp() {
}

protected void tearDown() {
}

public void test_internal_new_GCLorg_eclipse_swt_graphics_GCData() {
	warnUnimpl("Test test_internal_new_GCLorg_eclipse_swt_graphics_GCData not written");
}

public void test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData() {
	warnUnimpl("Test test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData not written");
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_internal_new_GCLorg_eclipse_swt_graphics_GCData");
	methodNames.addElement("test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_internal_new_GCLorg_eclipse_swt_graphics_GCData")) test_internal_new_GCLorg_eclipse_swt_graphics_GCData();
	else if (getName().equals("test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData")) test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData();
}
}

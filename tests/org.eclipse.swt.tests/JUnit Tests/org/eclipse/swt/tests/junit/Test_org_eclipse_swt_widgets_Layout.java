package org.eclipse.swt.tests.junit;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.widgets.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Layout
 *
 * @see org.eclipse.swt.widgets.Layout
 */
public class Test_org_eclipse_swt_widgets_Layout extends SwtTestCase {

public Test_org_eclipse_swt_widgets_Layout(String name) {
	super(name);
}


protected void setUp() {
}

protected void tearDown() {
}

public void test_Constructor() {
	warnUnimpl("Test test_Constructor not written");
}

public void test_computeSizeLorg_eclipse_swt_widgets_CompositeIIZ() {
	warnUnimpl("Test test_computeSizeLorg_eclipse_swt_widgets_CompositeIIZ not written");
}

public void test_layoutLorg_eclipse_swt_widgets_CompositeZ() {
	warnUnimpl("Test test_layoutLorg_eclipse_swt_widgets_CompositeZ not written");
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor");
	methodNames.addElement("test_computeSizeLorg_eclipse_swt_widgets_CompositeIIZ");
	methodNames.addElement("test_layoutLorg_eclipse_swt_widgets_CompositeZ");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_computeSizeLorg_eclipse_swt_widgets_CompositeIIZ")) test_computeSizeLorg_eclipse_swt_widgets_CompositeIIZ();
	else if (getName().equals("test_layoutLorg_eclipse_swt_widgets_CompositeZ")) test_layoutLorg_eclipse_swt_widgets_CompositeZ();
}
}

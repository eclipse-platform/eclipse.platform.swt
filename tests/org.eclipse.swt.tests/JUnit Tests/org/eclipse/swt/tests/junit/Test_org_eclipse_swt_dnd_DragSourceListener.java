package org.eclipse.swt.tests.junit;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.dnd.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.dnd.DragSourceListener
 *
 * @see org.eclipse.swt.dnd.DragSourceListener
 */
public class Test_org_eclipse_swt_dnd_DragSourceListener extends SwtTestCase {

public Test_org_eclipse_swt_dnd_DragSourceListener(String name) {
	super(name);
}


protected void setUp() {
}

protected void tearDown() {
}

public void test_dragStartLorg_eclipse_swt_dnd_DragSourceEvent() {
	warnUnimpl("Test test_dragStartLorg_eclipse_swt_dnd_DragSourceEvent not written");
}

public void test_dragSetDataLorg_eclipse_swt_dnd_DragSourceEvent() {
	warnUnimpl("Test test_dragSetDataLorg_eclipse_swt_dnd_DragSourceEvent not written");
}

public void test_dragFinishedLorg_eclipse_swt_dnd_DragSourceEvent() {
	warnUnimpl("Test test_dragFinishedLorg_eclipse_swt_dnd_DragSourceEvent not written");
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_dragStartLorg_eclipse_swt_dnd_DragSourceEvent");
	methodNames.addElement("test_dragSetDataLorg_eclipse_swt_dnd_DragSourceEvent");
	methodNames.addElement("test_dragFinishedLorg_eclipse_swt_dnd_DragSourceEvent");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_dragStartLorg_eclipse_swt_dnd_DragSourceEvent")) test_dragStartLorg_eclipse_swt_dnd_DragSourceEvent();
	else if (getName().equals("test_dragSetDataLorg_eclipse_swt_dnd_DragSourceEvent")) test_dragSetDataLorg_eclipse_swt_dnd_DragSourceEvent();
	else if (getName().equals("test_dragFinishedLorg_eclipse_swt_dnd_DragSourceEvent")) test_dragFinishedLorg_eclipse_swt_dnd_DragSourceEvent();
}
}

package org.eclipse.swt.tests.junit;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.events.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.events.ShellAdapter
 *
 * @see org.eclipse.swt.events.ShellAdapter
 */
public class Test_org_eclipse_swt_events_ShellAdapter extends SwtTestCase {

public Test_org_eclipse_swt_events_ShellAdapter(String name) {
	super(name);
}


protected void setUp() {
}

protected void tearDown() {
}

public void test_Constructor() {
	warnUnimpl("Test test_Constructor not written");
}

public void test_shellActivatedLorg_eclipse_swt_events_ShellEvent() {
	warnUnimpl("Test test_shellActivatedLorg_eclipse_swt_events_ShellEvent not written");
}

public void test_shellClosedLorg_eclipse_swt_events_ShellEvent() {
	warnUnimpl("Test test_shellClosedLorg_eclipse_swt_events_ShellEvent not written");
}

public void test_shellDeactivatedLorg_eclipse_swt_events_ShellEvent() {
	warnUnimpl("Test test_shellDeactivatedLorg_eclipse_swt_events_ShellEvent not written");
}

public void test_shellDeiconifiedLorg_eclipse_swt_events_ShellEvent() {
	warnUnimpl("Test test_shellDeiconifiedLorg_eclipse_swt_events_ShellEvent not written");
}

public void test_shellIconifiedLorg_eclipse_swt_events_ShellEvent() {
	warnUnimpl("Test test_shellIconifiedLorg_eclipse_swt_events_ShellEvent not written");
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor");
	methodNames.addElement("test_shellActivatedLorg_eclipse_swt_events_ShellEvent");
	methodNames.addElement("test_shellClosedLorg_eclipse_swt_events_ShellEvent");
	methodNames.addElement("test_shellDeactivatedLorg_eclipse_swt_events_ShellEvent");
	methodNames.addElement("test_shellDeiconifiedLorg_eclipse_swt_events_ShellEvent");
	methodNames.addElement("test_shellIconifiedLorg_eclipse_swt_events_ShellEvent");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_shellActivatedLorg_eclipse_swt_events_ShellEvent")) test_shellActivatedLorg_eclipse_swt_events_ShellEvent();
	else if (getName().equals("test_shellClosedLorg_eclipse_swt_events_ShellEvent")) test_shellClosedLorg_eclipse_swt_events_ShellEvent();
	else if (getName().equals("test_shellDeactivatedLorg_eclipse_swt_events_ShellEvent")) test_shellDeactivatedLorg_eclipse_swt_events_ShellEvent();
	else if (getName().equals("test_shellDeiconifiedLorg_eclipse_swt_events_ShellEvent")) test_shellDeiconifiedLorg_eclipse_swt_events_ShellEvent();
	else if (getName().equals("test_shellIconifiedLorg_eclipse_swt_events_ShellEvent")) test_shellIconifiedLorg_eclipse_swt_events_ShellEvent();
}
}

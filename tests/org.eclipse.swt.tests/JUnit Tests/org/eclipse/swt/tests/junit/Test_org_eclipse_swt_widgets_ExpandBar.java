/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import java.util.Vector;

import junit.framework.*;
import junit.textui.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.ExpandEvent;
import org.eclipse.swt.events.ExpandListener;
import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.ExpandBar
 *
 * @see org.eclipse.swt.widgets.ExpandBar
 */
public class Test_org_eclipse_swt_widgets_ExpandBar extends Test_org_eclipse_swt_widgets_Composite {

public Test_org_eclipse_swt_widgets_ExpandBar(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	expandBar = new ExpandBar(shell, 0);
	setWidget(expandBar);
}

public void test_addExpandListenerLorg_eclipse_swt_events_ExpandListener() {
	final boolean[] listenerCalled = new boolean[] {false};
	ExpandListener expandListener = new ExpandListener() {		
		public void itemCollapsed(ExpandEvent e) {
			listenerCalled[0] = true;			
		}
		public void itemExpanded(ExpandEvent e) {
			listenerCalled[0] = true;			
		}
	};

	try {
		expandBar.addExpandListener(null);
		fail("No exception thrown for addExpandListener with null argument");
	} catch (IllegalArgumentException e) {
	}
	
	expandBar.addExpandListener(expandListener);
	expandBar.notifyListeners(SWT.Expand, new Event());
	assertTrue(":a:", listenerCalled[0]);

	listenerCalled[0] = false;
	expandBar.notifyListeners(SWT.Collapse, new Event());
	assertTrue(":b:", listenerCalled[0]);
	
	try {
		expandBar.removeExpandListener(null);
		fail("No exception thrown for removeExpandListener with null argument");
	} catch (IllegalArgumentException e) {
	}
	
	listenerCalled[0] = false;
	expandBar.removeExpandListener(expandListener);
	expandBar.notifyListeners(SWT.Expand, new Event());
	assertFalse(listenerCalled[0]);
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	try {
		new ExpandBar(null, 0);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_getItemCount() {
	int number = 10;
	for (int i = 0; i < number; i++) {
		assertTrue(":a:" + i, expandBar.getItemCount() == i);
	  	new ExpandItem(expandBar, 0);
	}
}

public void test_getItemI() {
	int number = 5;
	ExpandItem[] items = new ExpandItem[number];
	for (int i = 0; i<number ; i++){
	  	items[i] = new ExpandItem(expandBar, 0);
	}
	for (int i = 0; i<number ; i++){
		assertTrue(":a:", expandBar.getItem(i)==items[i]);
	}

	expandBar = new ExpandBar(shell, 0);
	number = 5;
	items = new ExpandItem[number];
	for (int i = 0; i<number ; i++){
	  	items[i] = new ExpandItem(expandBar, 0);
	}
	try {
		expandBar.getItem(number);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_getItemLorg_eclipse_swt_graphics_Point() {
	warnUnimpl("Test test_getItemLorg_eclipse_swt_graphics_Point not written");
}

public void test_getItems() {
	int number = 5;
	ExpandItem[] items = new ExpandItem[number];
	for (int i = 0; i<number ; i++) {
	  	items[i] = new ExpandItem(expandBar, 0);
	}
	assertEquals(items, expandBar.getItems());
	
	expandBar.getItems()[0].dispose();
	assertEquals(new ExpandItem[] {items[1], items[2], items[3], items[4]}, expandBar.getItems());

	expandBar.getItems()[3].dispose();
	assertEquals(new ExpandItem[] {items[1], items[2], items[3]}, expandBar.getItems());

	expandBar.getItems()[1].dispose();
	assertEquals(new ExpandItem[] {items[1], items[3]}, expandBar.getItems());
}

public void test_getSpacing() {
//tested in test_setSpacingI
}

public void test_indexOfLorg_eclipse_swt_widgets_ExpandItem() {
	int number = 10;
	ExpandItem[] items = new ExpandItem[number];
	for (int i = 0; i < number; i++) {
	  	items[i] = new ExpandItem(expandBar, 0);
	}
	for (int i = 0; i < number; i++) {
		assertTrue(":a:" + i, expandBar.indexOf(items[i] ) == i);
	}
	
	items = new ExpandItem[number];
	for (int i = 0; i < number; i++) {
	  	items[i] = new ExpandItem(expandBar, 0);
	}
	for (int i = 0; i < number; i++) {
		try {
			expandBar.indexOf(null);		
			fail("No exception thrown for expandItem == null");
		}
		catch (IllegalArgumentException e) {
		}
	}
}

public void test_removeExpandListenerLorg_eclipse_swt_events_ExpandListener() {
//tested in test_addExpandListenerLorg_eclipse_swt_events_ExpandListener 
}

public void test_setSpacingI() {
	expandBar.setSpacing(0);
	assertEquals(expandBar.getSpacing(), 0);
	
	expandBar.setSpacing(3);
	assertEquals(expandBar.getSpacing(), 3);
	
	expandBar.setSpacing(-4);
	assertEquals(expandBar.getSpacing(), 3);
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_ExpandBar((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_addExpandListenerLorg_eclipse_swt_events_ExpandListener");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_getItemCount");
	methodNames.addElement("test_getItemI");
	methodNames.addElement("test_getItemLorg_eclipse_swt_graphics_Point");
	methodNames.addElement("test_getItems");
	methodNames.addElement("test_getSpacing");
	methodNames.addElement("test_setSpacingI");
	methodNames.addElement("test_indexOfLorg_eclipse_swt_widgets_ExpandItem");
	methodNames.addElement("test_removeExpandListenerLorg_eclipse_swt_events_ExpandListener");
	methodNames.addElement("test_consistency_MouseSelection");
	methodNames.addElement("test_consistency_SpaceSelection");
	methodNames.addElement("test_consistency_EnterSelection");
	methodNames.addElement("test_consistency_MenuDetect");
	methodNames.addElement("test_consistency_DragDetect");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Composite.methodNames()); // add superclass method names
	return methodNames;
}

protected void runTest() throws Throwable {
	if (getName().equals("test_addExpandListenerLorg_eclipse_swt_events_ExpandListener")) test_addExpandListenerLorg_eclipse_swt_events_ExpandListener();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_getItemCount")) test_getItemCount();
	else if (getName().equals("test_getItemI")) test_getItemI();
	else if (getName().equals("test_getItemLorg_eclipse_swt_graphics_Point")) test_getItemLorg_eclipse_swt_graphics_Point();
	else if (getName().equals("test_getItems")) test_getItems();
	else if (getName().equals("test_getSpacing")) test_getSpacing();
	else if (getName().equals("test_setSpacingI")) test_setSpacingI();
	else if (getName().equals("test_indexOfLorg_eclipse_swt_widgets_ExpandItem")) test_indexOfLorg_eclipse_swt_widgets_ExpandItem();
	else if (getName().equals("test_removeExpandListenerLorg_eclipse_swt_events_ExpandListener")) test_removeExpandListenerLorg_eclipse_swt_events_ExpandListener();
	else if (getName().equals("test_consistency_MouseSelection")) test_consistency_MouseSelection();
	else if (getName().equals("test_consistency_EnterSelection")) test_consistency_EnterSelection();
	else if (getName().equals("test_consistency_SpaceSelection")) test_consistency_SpaceSelection();
	else if (getName().equals("test_consistency_MenuDetect")) test_consistency_MenuDetect();
	else if (getName().equals("test_consistency_DragDetect")) test_consistency_DragDetect();
	else super.runTest();
}

private void createExpandBar(Vector events) {
	expandBar = new ExpandBar(shell, SWT.V_SCROLL);
	for (int i = 0; i < 3; i++) {
		ExpandItem item = new ExpandItem(expandBar, SWT.NONE);
		item.setText("ExpandBar" + i);
		hookExpectedEvents(item, getTestName(), events);
	}
	setWidget(expandBar);
}

public void test_consistency_MouseSelection() {
    Vector events = new Vector();
    createExpandBar(events);
    consistencyEvent(30, 10, 1, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

public void test_consistency_EnterSelection() {
    Vector events = new Vector();
    createExpandBar(events);
    consistencyEvent(13, 10, 0, 0, ConsistencyUtility.KEY_PRESS, events);
}

public void test_consistency_SpaceSelection () {
    Vector events = new Vector();
    createExpandBar(events);
    consistencyEvent(' ', 32, 0, 0, ConsistencyUtility.KEY_PRESS, events);
}

public void test_consistency_MenuDetect () {
    Vector events = new Vector();
    createExpandBar(events);
    consistencyEvent(50, 15, 3, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

public void test_consistency_DragDetect () {
    Vector events = new Vector();
    createExpandBar(events);
    consistencyEvent(30, 20, 50, 20, ConsistencyUtility.MOUSE_DRAG, events);
}

/* custom */
protected ExpandBar expandBar;
}

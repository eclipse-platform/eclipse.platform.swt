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

import static org.junit.Assert.assertArrayEquals;

import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ExpandEvent;
import org.eclipse.swt.events.ExpandListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.ExpandBar
 *
 * @see org.eclipse.swt.widgets.ExpandBar
 */
public class Test_org_eclipse_swt_widgets_ExpandBar extends Test_org_eclipse_swt_widgets_Composite {

public Test_org_eclipse_swt_widgets_ExpandBar(String name) {
	super(name);
}

@Override
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

@Override
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

public void test_getItems() {
	int number = 5;
	ExpandItem[] items = new ExpandItem[number];
	for (int i = 0; i<number ; i++) {
	  	items[i] = new ExpandItem(expandBar, 0);
	}
	assertArrayEquals(items, expandBar.getItems());
	
	expandBar.getItems()[0].dispose();
	assertArrayEquals(new ExpandItem[] {items[1], items[2], items[3], items[4]}, expandBar.getItems());

	expandBar.getItems()[3].dispose();
	assertArrayEquals(new ExpandItem[] {items[1], items[2], items[3]}, expandBar.getItems());

	expandBar.getItems()[1].dispose();
	assertArrayEquals(new ExpandItem[] {items[1], items[3]}, expandBar.getItems());
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

public void test_setSpacingI() {
	expandBar.setSpacing(0);
	assertEquals(expandBar.getSpacing(), 0);
	
	expandBar.setSpacing(3);
	assertEquals(expandBar.getSpacing(), 3);
	
	expandBar.setSpacing(-4);
	assertEquals(expandBar.getSpacing(), 3);
}

private void createExpandBar(Vector<String> events) {
	expandBar = new ExpandBar(shell, SWT.V_SCROLL);
	for (int i = 0; i < 3; i++) {
		ExpandItem item = new ExpandItem(expandBar, SWT.NONE);
		item.setText("ExpandBar" + i);
		hookExpectedEvents(item, getTestName(), events);
	}
	setWidget(expandBar);
}

public void test_consistency_MouseSelection() {
    Vector<String> events = new Vector<String>();
    createExpandBar(events);
    consistencyEvent(30, 10, 1, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

public void test_consistency_EnterSelection() {
    Vector<String> events = new Vector<String>();
    createExpandBar(events);
    consistencyEvent(13, 10, 0, 0, ConsistencyUtility.KEY_PRESS, events);
}

public void test_consistency_SpaceSelection () {
    Vector<String> events = new Vector<String>();
    createExpandBar(events);
    consistencyEvent(' ', 32, 0, 0, ConsistencyUtility.KEY_PRESS, events);
}

public void test_consistency_MenuDetect () {
    Vector<String> events = new Vector<String>();
    createExpandBar(events);
    consistencyEvent(50, 15, 3, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

public void test_consistency_DragDetect () {
    Vector<String> events = new Vector<String>();
    createExpandBar(events);
    consistencyEvent(30, 20, 50, 20, ConsistencyUtility.MOUSE_DRAG, events);
}

/* custom */
protected ExpandBar expandBar;
}

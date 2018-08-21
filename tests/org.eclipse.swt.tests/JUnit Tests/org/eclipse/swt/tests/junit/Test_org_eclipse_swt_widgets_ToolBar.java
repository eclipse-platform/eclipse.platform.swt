/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.ToolBar
 *
 * @see org.eclipse.swt.widgets.ToolBar
 */
public class Test_org_eclipse_swt_widgets_ToolBar extends Test_org_eclipse_swt_widgets_Composite {

@Override
@Before
public void setUp() {
	super.setUp();
	toolBar = new ToolBar(shell, 0);
	setWidget(toolBar);
}

@Override
@Test
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	try {
		new ToolBar(null, 0);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

@Override
@Test
public void test_computeSizeIIZ() {
}

@Override
@Test
public void test_computeTrimIIII() {
}

@Test
public void test_getItemCount() {
	int number = 10;
	for (int i = 0; i<number ; i++){
		assertTrue(":a:" + i, toolBar.getItemCount()==i);
	  	new ToolItem(toolBar, 0);
	}
}

@Test
public void test_getItemI() {
	int number = 5;
	ToolItem[] items = new ToolItem[number];
	for (int i = 0; i<number ; i++){
	  	items[i] = new ToolItem(toolBar, 0);
	}
	for (int i = 0; i<number ; i++){
		assertTrue(":a:", toolBar.getItem(i)==items[i]);
	}

	toolBar = new ToolBar(shell, 0);
	number = 5;
	items = new ToolItem[number];
	for (int i = 0; i<number ; i++){
	  	items[i] = new ToolItem(toolBar, 0);
	}
	try {
		toolBar.getItem(number);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}
}

@Test
public void test_getItems() {
	int number = 5;
	ToolItem[] items = new ToolItem[number];
	for (int i = 0; i<number ; i++){
	  	items[i] = new ToolItem(toolBar, 0);
	}
	assertArrayEquals(items, toolBar.getItems());

	toolBar.getItems()[0].dispose();
	assertArrayEquals(new ToolItem[]{items[1], items[2], items[3], items[4]}, toolBar.getItems());

	toolBar.getItems()[3].dispose();
	assertArrayEquals(new ToolItem[]{items[1], items[2], items[3]}, toolBar.getItems());

	toolBar.getItems()[1].dispose();
	assertArrayEquals(new ToolItem[]{items[1], items[3]}, toolBar.getItems());
}

@Test
public void test_getRowCount() {
	if (SwtTestUtil.isGTK) {
		//TODO Fix GTK failure.
		if (SwtTestUtil.verbose) {
			System.out.println("Excluded test_getRowCount(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_ToolBar)");
		}
		return;
	}
	toolBar = new ToolBar(shell, SWT.WRAP);
	int number = 5;
	ToolItem[] items = new ToolItem[number];
	for (int i = 0; i<number ; i++){
	  	items[i] = new ToolItem(toolBar, 0);
	}
	assertTrue(":a:" + toolBar.getRowCount(), toolBar.getRowCount()==number);  //????  because of Size(0, 0)

	toolBar = new ToolBar(shell, 0);
	number = 5;
	items = new ToolItem[number];
	for (int i = 0; i<number ; i++){
	  	items[i] = new ToolItem(toolBar, 0);
	}
	assertTrue(":a:", toolBar.getRowCount()==1);
}

@Test
public void test_indexOfLorg_eclipse_swt_widgets_ToolItem() {
	int number = 10;
	ToolItem[] tis = new ToolItem[number];
	for (int i = 0; i<number ; i++){
	  	tis[i] = new ToolItem(toolBar, 0);
	}
	for (int i = 0; i<number ; i++){
		assertTrue(":a:" + i, toolBar.indexOf(tis[i])==i);
	}

	number = 10;
	tis = new ToolItem[number];
	for (int i = 0; i<number ; i++){
	  	tis[i] = new ToolItem(toolBar, 0);
	}
	for (int i = 0; i<number ; i++){
		try {
			toolBar.indexOf(null);
			fail("No exception thrown for toolItem == null");
		}
		catch (IllegalArgumentException e) {
		}
	}
}

/* custom */
protected ToolBar toolBar;

private void createToolBar(List<String> events) {
	toolBar = new ToolBar(shell, SWT.FLAT | SWT.HORIZONTAL);
	for (int i = 0; i < 3; i++) {
		ToolItem item = new ToolItem(toolBar, SWT.PUSH);
		item.setText("ToolBar" + i);
		item.setToolTipText("ToolItem ToolTip" + i);
		hookExpectedEvents(item, getTestName(), events);
	}
	setWidget(toolBar);
}

@Test
public void test_consistency_MouseSelection() {
    List<String> events = new ArrayList<>();
    createToolBar(events);
    consistencyEvent(30, 10, 1, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

@Test
public void test_consistency_EnterSelection () {
    List<String> events = new ArrayList<>();
    createToolBar(events);
    consistencyEvent(13, 10, 0, 0, ConsistencyUtility.KEY_PRESS, events);
}

@Test
public void test_consistency_SpaceSelection () {
    List<String> events = new ArrayList<>();
    createToolBar(events);
    consistencyEvent(' ', 32, 0, 0, ConsistencyUtility.KEY_PRESS, events);
}

@Test
public void test_consistency_MenuDetect () {
    List<String> events = new ArrayList<>();
    createToolBar(events);
    consistencyEvent(50, 15, 3, 0, ConsistencyUtility.MOUSE_CLICK, events);
}
@Test
public void test_consistency_DragDetect () {
    List<String> events = new ArrayList<>();
    createToolBar(events);
    consistencyEvent(30, 20, 50, 20, ConsistencyUtility.MOUSE_DRAG, events);
}

}

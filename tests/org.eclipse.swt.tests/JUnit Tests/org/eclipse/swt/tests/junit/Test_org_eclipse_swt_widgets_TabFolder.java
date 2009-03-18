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
import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.TabFolder
 *
 * @see org.eclipse.swt.widgets.TabFolder
 */
public class Test_org_eclipse_swt_widgets_TabFolder extends Test_org_eclipse_swt_widgets_Composite {

public Test_org_eclipse_swt_widgets_TabFolder(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	makeCleanEnvironment();
}

protected void tearDown() {
	super.tearDown();
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	try {
		new TabFolder(null, 0);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	warnUnimpl("Test test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener not written");
}

public void test_computeSizeIIZ() {
	warnUnimpl("Test test_computeSizeIIZ not written");
}

public void test_computeTrimIIII() {
	warnUnimpl("Test test_computeTrimIIII not written");
}

public void test_getClientArea() {
	warnUnimpl("Test test_getClientArea not written");
}

public void test_getItemCount() {
	int number = 10;
	for (int i = 0; i<number ; i++){
		assertTrue(":a:" + i, tabFolder.getItemCount()==i);
	  	new TabItem(tabFolder, 0);
	}
}

public void test_getItemI() {
	int number = 15;
	TabItem[] items = new TabItem[number];
	for (int i = 0; i < number; i++) {
		items[i] = new TabItem(tabFolder, 0);
	}

	for (int i = 0; i < number; i++) {
		assertTrue(":a:" +String.valueOf(i), tabFolder.getItem(i).equals(items[i]));
	}
	try {
		tabFolder.getItem(number);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		tabFolder.getItem(number+1);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		tabFolder.getItem(-1);
		fail("No exception thrown for index == -1");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_getItems() {
	int number = 5;
	TabItem[] items = new TabItem[number];

	assertEquals(0, tabFolder.getItems().length);
	
	for (int i = 0; i<number ; i++){
	  	items[i] = new TabItem(tabFolder, 0);
	}
	assertEquals(items, tabFolder.getItems());
	
	tabFolder.getItems()[0].dispose();
	assertEquals(new TabItem[]{items[1], items[2], items[3], items[4]}, tabFolder.getItems());

	tabFolder.getItems()[3].dispose();
	assertEquals(new TabItem[]{items[1], items[2], items[3]}, tabFolder.getItems());

	tabFolder.getItems()[1].dispose();
	assertEquals(new TabItem[]{items[1], items[3]}, tabFolder.getItems());
}

public void test_getSelection() {
	int number = 10;
	TabItem[] tis = new TabItem[number];
	for (int i = 0; i<number ; i++){
	  	tis[i] = new TabItem(tabFolder, 0);
	}
	assertTrue(":a:", tabFolder.getSelection()[0] == tis[0]);	
	for (int i = 0; i<number ; i++){
		tabFolder.setSelection(i);
		assertTrue(":b:" + i, tabFolder.getSelection()[0]==tis[i]);
	}
}

public void test_getSelectionIndex() {
	int number = 15;
	TabItem[] items = new TabItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TabItem(tabFolder, 0);
		
	assertTrue(":a:", tabFolder.getSelectionIndex()==0);

	tabFolder.setSelection(new TabItem[]{items[2], items[number-1], items[10]});
	assertTrue(":b:", tabFolder.getSelectionIndex()==2);
	
	tabFolder.setSelection(items);
	assertTrue(":c:", tabFolder.getSelectionIndex()==0);
	
	tabFolder.setSelection(items[2]);
	assertTrue(":d:", tabFolder.getSelectionIndex()==2);
}

public void test_indexOfLorg_eclipse_swt_widgets_TabItem() {
	int number = 10;
	TabItem[] tis = new TabItem[number];
	for (int i = 0; i<number ; i++){
	  	tis[i] = new TabItem(tabFolder, 0);
	}
	for (int i = 0; i<number ; i++){
		assertTrue(":a:" + i, tabFolder.indexOf(tis[i])==i);
	}

	//
	makeCleanEnvironment();
	
	for (int i = 0; i<number ; i++){
	  	tis[i] = new TabItem(tabFolder, 0);
	}
	for (int i = 0; i<number ; i++){
		try {
			tabFolder.indexOf(null);
			fail("No exception thrown for tabItem == null");
		}
		catch (IllegalArgumentException e) {
		}
	}

	//
	makeCleanEnvironment();	
	number = 20;
	TabItem[] items = new TabItem[number];

	for (int i = 0; i < number; i++) {
		items[i] = new TabItem(tabFolder, 0);
		items[i].setText(String.valueOf(i));
	}

	//another tabFolder
	TabFolder tabFolder_2 = new TabFolder(shell, 0);
	TabItem[] items_2 = new TabItem[number];
	for (int i = 0; i < number; i++) {
		items_2[i] = new TabItem(tabFolder_2, 0);
		items_2[i].setText(String.valueOf(i));
	}

	for (int i = 0; i < number; i++) {
		assertTrue(":a:" + String.valueOf(i), tabFolder.indexOf(items_2[i])==-1);
	}

	//
	TabFolder tabFolder2 = new TabFolder(shell, SWT.NULL);
	TabItem tabItem = new TabItem(tabFolder2, SWT.NULL);
	
	assertTrue(":a:", tabFolder.indexOf(tabItem) == -1);
}

public void test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	warnUnimpl("Test test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener not written");
}

public void test_setSelection$Lorg_eclipse_swt_widgets_TabItem() {
	warnUnimpl("Test test_setSelection$Lorg_eclipse_swt_widgets_TabItem not written");
}

public void test_setSelectionEmpty() {
	int number = 10;
	for (int i = 0; i<number ; i++){
	  	new TabItem(tabFolder, 0);
	}
	for (int i = 0; i<number ; i++){
		tabFolder.setSelection(i);
		assertEquals(i, tabFolder.getSelectionIndex());
	}

	tabFolder.setSelection(-1);
	assertEquals(0, tabFolder.getSelection().length);	
}

public void test_setSelectionI() {
	int number = 10;
	for (int i = 0; i<number ; i++){
	  	new TabItem(tabFolder, 0);
	}
	for (int i = 0; i<number ; i++){
		tabFolder.setSelection(i);
		assertEquals(i, tabFolder.getSelectionIndex());
	}

	//
	makeCleanEnvironment();
	
	for (int i = 0; i<number ; i++){
	  	new TabItem(tabFolder, 0);
	  	assertEquals("i=" + i, 0, tabFolder.getSelectionIndex());
	}

	//
	makeCleanEnvironment();
	
	number = 5;
	TabItem[] items = new TabItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TabItem(tabFolder, 0);
	try {
		tabFolder.setSelection((TabItem) null);
		fail("No exception thrown for selection == null");
	}
	catch (IllegalArgumentException e) {
	}
	finally {
		assertEquals(new TabItem[]{items[0]}, tabFolder.getSelection());
	}
	
	try {
		tabFolder.setSelection((TabItem[]) null);
		fail("No exception thrown for selection == null");
	}
	catch (IllegalArgumentException e) {
	}
	finally {
		assertEquals(new TabItem[]{items[0]}, tabFolder.getSelection());
	}

	//
	makeCleanEnvironment();
	
	items = new TabItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TabItem(tabFolder, 0);

	tabFolder.setSelection(0);
	assertEquals(new TabItem[]{items[0]}, tabFolder.getSelection());

	tabFolder.setSelection(4);
	assertEquals(new TabItem[]{items[4]}, tabFolder.getSelection());

	tabFolder.setSelection(2);
	assertEquals(new TabItem[]{items[2]}, tabFolder.getSelection());	

	tabFolder.setSelection(1);
	assertEquals(new TabItem[]{items[1]}, tabFolder.getSelection());

	tabFolder.setSelection(number + 1);
	assertEquals(new TabItem[]{items[1]}, tabFolder.getSelection());	

//	tabFolder.setSelection(-1);
//	assertEquals(0, tabFolder.getSelection().length);	
	
	tabFolder.setSelection(3);
	assertEquals(new TabItem[]{items[3]}, tabFolder.getSelection());

//	tabFolder.setSelection(-2);
//	assertEquals(0, tabFolder.getSelection().length);	

	//
	makeCleanEnvironment();
	
	for (int i = 0; i < number; i++)
		items[i] = new TabItem(tabFolder, 0);

	tabFolder.setSelection(items[0]);
	assertEquals(new TabItem[]{items[0]}, tabFolder.getSelection());
	
	tabFolder.setSelection(new TabItem[] {items[0]});
	assertEquals(new TabItem[] {items[0]}, tabFolder.getSelection());

	tabFolder.setSelection(new TabItem[] {items[3]});
	assertEquals(new TabItem[] {items[3]}, tabFolder.getSelection());	

	tabFolder.setSelection(new TabItem[] {items[4]});
	assertEquals(new TabItem[] {items[4]}, tabFolder.getSelection());

	tabFolder.setSelection(new TabItem[] {items[2]});
	assertEquals(new TabItem[] {items[2]}, tabFolder.getSelection());	

	tabFolder.setSelection(new TabItem[] {items[1]});
	assertEquals(new TabItem[] {items[1]}, tabFolder.getSelection());	

	//
	makeCleanEnvironment();
	
	for (int i = 0; i < number; i++)
		items[i] = new TabItem(tabFolder, 0);
	try {
		tabFolder.setSelection( new TabItem[]{items[0], null});
		tabFolder.setSelection( new TabItem[]{null});		
		fail("No exception thrown for selection == null");
	}
	catch (IllegalArgumentException e) {
	}
	finally {
		assertEquals(new TabItem[]{items[0]}, tabFolder.getSelection());
	}
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_TabFolder((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_computeSizeIIZ");
	methodNames.addElement("test_computeTrimIIII");
	methodNames.addElement("test_getClientArea");
	methodNames.addElement("test_getItemCount");
	methodNames.addElement("test_getItemI");
	methodNames.addElement("test_getItems");
	methodNames.addElement("test_getSelection");
	methodNames.addElement("test_getSelectionIndex");
	methodNames.addElement("test_indexOfLorg_eclipse_swt_widgets_TabItem");
	methodNames.addElement("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_setSelection$Lorg_eclipse_swt_widgets_TabItem");
	methodNames.addElement("test_setSelectionI");
	methodNames.addElement("test_setSelectionEmpty");
	methodNames.addElement("test_consistency_KeySelection");
	methodNames.addElement("test_consistency_MouseSelection");
	methodNames.addElement("test_consistency_PgupSelection");
	methodNames.addElement("test_consistency_PgdwnSelection");
	methodNames.addElement("test_consistency_MenuDetect");
	methodNames.addElement("test_consistency_DragDetect");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Composite.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_computeSizeIIZ")) test_computeSizeIIZ();
	else if (getName().equals("test_computeTrimIIII")) test_computeTrimIIII();
	else if (getName().equals("test_getClientArea")) test_getClientArea();
	else if (getName().equals("test_getItemCount")) test_getItemCount();
	else if (getName().equals("test_getItemI")) test_getItemI();
	else if (getName().equals("test_getItems")) test_getItems();
	else if (getName().equals("test_getSelection")) test_getSelection();
	else if (getName().equals("test_getSelectionIndex")) test_getSelectionIndex();
	else if (getName().equals("test_indexOfLorg_eclipse_swt_widgets_TabItem")) test_indexOfLorg_eclipse_swt_widgets_TabItem();
	else if (getName().equals("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_setSelection$Lorg_eclipse_swt_widgets_TabItem")) test_setSelection$Lorg_eclipse_swt_widgets_TabItem();
	else if (getName().equals("test_setSelectionI")) test_setSelectionI();
	else if (getName().equals("test_setSelectionEmpty")) test_setSelectionEmpty();
	else if (getName().equals("test_consistency_KeySelection")) test_consistency_KeySelection();
	else if (getName().equals("test_consistency_MouseSelection")) test_consistency_MouseSelection();
	else if (getName().equals("test_consistency_PgdwnSelection")) test_consistency_PgdwnSelection();
	else if (getName().equals("test_consistency_PgupSelection")) test_consistency_PgupSelection();
	else if (getName().equals("test_consistency_MenuDetect")) test_consistency_MenuDetect();
	else if (getName().equals("test_consistency_DragDetect")) test_consistency_DragDetect();
	else super.runTest();
}

/* custom */
protected TabFolder tabFolder;

private void makeCleanEnvironment() {
// this method must be private or protected so the auto-gen tool keeps it
	tabFolder = new TabFolder(shell, 0);
	setWidget(tabFolder);
}

private void createTabFolder(Vector events) {
	makeCleanEnvironment();
	for (int i = 0; i < 3; i++) {
		TabItem item = new TabItem(tabFolder, SWT.NONE);
		item.setText("TabItem &" + i);
		item.setToolTipText("TabItem ToolTip" + i);
		Text itemText = new Text(tabFolder, SWT.MULTI | SWT.BORDER);
		itemText.setText("\nText for TabItem " + i + "\n\n\n");
		item.setControl(itemText);
		hookExpectedEvents(item, getTestName(), events);
	}
	
//	tabFolder.setSelection(new TabItem[] {tabFolder.getItem(0)});
}

public void test_consistency_KeySelection() {
    Vector events = new Vector();
    createTabFolder(events);
    consistencyEvent(0, SWT.ARROW_RIGHT, 0, 0, ConsistencyUtility.KEY_PRESS, events, false);
}

public void test_consistency_MouseSelection() {
    Vector events = new Vector();
    createTabFolder(events);
    consistencyPrePackShell();
    consistencyEvent(tabFolder.getSize().x/2, 5, 1, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

public void test_consistency_PgdwnSelection () {
    Vector events = new Vector();
    createTabFolder(events);
    consistencyEvent(0, SWT.CTRL, 0, SWT.PAGE_DOWN, ConsistencyUtility.DOUBLE_KEY_PRESS, events, false);
}

public void test_consistency_PgupSelection () {
    Vector events = new Vector();
    createTabFolder(events);
    tabFolder.setSelection(2);
    consistencyEvent(0, SWT.CTRL, 0, SWT.PAGE_UP, ConsistencyUtility.DOUBLE_KEY_PRESS, events, false);
}

public void test_consistency_MenuDetect () {
    Vector events = new Vector();
    createTabFolder(events);
    tabFolder.setSelection(1);
    consistencyEvent(50, 5, 3, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

public void test_consistency_DragDetect () {
    Vector events = new Vector();
    createTabFolder(events);
    tabFolder.setSelection(1);
    consistencyEvent(50, 5, 70, 10, ConsistencyUtility.MOUSE_DRAG, events);
}
}

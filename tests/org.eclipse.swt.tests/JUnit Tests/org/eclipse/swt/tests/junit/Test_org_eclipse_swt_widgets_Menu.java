package org.eclipse.swt.tests.junit;

/*
 * (c) Copyright IBM Corp. 2000, 2002. All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Menu
 *
 * @see org.eclipse.swt.widgets.Menu
 */
public class Test_org_eclipse_swt_widgets_Menu extends Test_org_eclipse_swt_widgets_Widget {

Menu menu;

public Test_org_eclipse_swt_widgets_Menu(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	menu = new Menu(shell);
	setWidget(menu);
}

protected void tearDown() {
	super.tearDown();
}

/* tests Menu(Window) */
public void test_ConstructorLorg_eclipse_swt_widgets_Control(){
	Composite comp = new Composite(shell, SWT.NULL);
	Menu testMenu = new Menu(comp);
	comp.dispose();
}

public void test_ConstructorLorg_eclipse_swt_widgets_DecorationsI(){
	Menu newMenu;
	MenuItem mItem = new MenuItem(menu, SWT.NULL);
	newMenu = new Menu(mItem);
}

public void test_ConstructorLorg_eclipse_swt_widgets_Menu(){
	Menu newMenu;
	newMenu = new Menu(menu);
}

public void test_ConstructorLorg_eclipse_swt_widgets_MenuItem() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_MenuItem not written");
}

public void test_addHelpListenerLorg_eclipse_swt_events_HelpListener() {
	warnUnimpl("Test test_addHelpListenerLorg_eclipse_swt_events_HelpListener not written");
}

public void test_addMenuListenerLorg_eclipse_swt_events_MenuListener() {
	warnUnimpl("Test test_addMenuListenerLorg_eclipse_swt_events_MenuListener not written");
}

public void test_getDefaultItem() {
	warnUnimpl("Test test_getDefaultItem not written");
}

public void test_getDisplay() {
	warnUnimpl("Test test_getDisplay not written");
}

public void test_getEnabled() {
	warnUnimpl("Test test_getEnabled not written");
}

/**
 * Returns the item at the given, zero-relative index in the
 * receiver. Throws an exception if the index is out of range.
 *
 * @param index the index of the item to return
 * @return the item at the given index
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 */
public void test_getItemI(){
	MenuItem mItem0 = new MenuItem(menu, SWT.NULL);
	MenuItem mItem1 = new MenuItem(menu, SWT.NULL);
	assertEquals(menu.getItem(0), mItem0);
	assertEquals(menu.getItem(1), mItem1);
}

/**
 * Returns the number of items contained in the receiver.
 */
public void test_getItemCount(){
	int number = 10;
	MenuItem ti;
	for (int i = 0; i<number ; i++){
		assertEquals(menu.getItemCount(), i);
	  	ti = new MenuItem(menu, 0);
	}
}

/**
 * Returns an array of <code>MenuItem</code>s which are the items
 * in the receiver. 
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver. 
 * </p>
 */
public void test_getItems(){
	int number = 5;
	MenuItem[] items = new MenuItem[number];
	for (int i = 0; i<number ; i++){
	  	items[i] = new MenuItem(menu, 0);
	}
	assertEquals(":a:", items, menu.getItems());
	
	menu.getItems()[0].dispose();
	assertEquals(":b:", new MenuItem[]{items[1], items[2], items[3], items[4]}, menu.getItems());

	menu.getItems()[3].dispose();
	assertEquals(":c:", new MenuItem[]{items[1], items[2], items[3]}, menu.getItems());

	menu.getItems()[1].dispose();
	assertEquals(":d:", new MenuItem[]{items[1], items[3]}, menu.getItems());
}

/**
 * Returns the receiver's parent, which must be a <code>Decorations</code>.
 */
public void test_getParent(){
	assertEquals(menu.getParent(), shell);
}

/**
 * Returns the receiver's parent item, which must be a
 * <code>MenuItem</code> or null when the receiver is a
 * root.
 */
public void test_getParentItem(){
	MenuItem mItem = new MenuItem(menu, SWT.CASCADE);
	Menu newMenu = new Menu(shell, SWT.DROP_DOWN);
	assertNull(newMenu.getParentItem());
	mItem.setMenu(newMenu);
	assertEquals(newMenu.getParentItem(), mItem);
}

/**
 * Returns the receiver's parent item, which must be a
 * <code>Menu</code> or null when the receiver is a
 * root.
 */
public void test_getParentMenu(){
	MenuItem mItem = new MenuItem(menu, SWT.CASCADE);
	Menu newMenu = new Menu(shell, SWT.DROP_DOWN);
	assertNull(newMenu.getParentMenu());
	mItem.setMenu(newMenu);
	assertEquals(newMenu.getParentMenu(), menu);
}

/**
 * Returns the receiver's shell. For all controls other than
 * shells, this simply returns the control's nearest ancestor
 * shell. Shells return themselves, even if they are children
 * of other shells.
 */
public void test_getShell(){
	assertEquals(menu.getShell(), shell);
}

public void test_getVisible() {
	warnUnimpl("Test test_getVisible not written");
}

/**
 * Searches the receiver's list starting at the first item
 * (index 0) until an item is found that is equal to the 
 * argument, and returns the index of that item. If no item
 * is found, returns -1.
 *
 * @param item the search item
 * @return the index of the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 */
public void test_indexOfLorg_eclipse_swt_widgets_MenuItem(){
	int number = 10;
	MenuItem[] mis = new MenuItem[number];
	for (int i = 0; i<number ; i++){
	  	mis[i] = new MenuItem(menu, SWT.NULL);
	}
	for (int i = 0; i<number ; i++){
		assertEquals(menu.indexOf(mis[i]), i);
		if (i>1)
			assertTrue(menu.indexOf(mis[i-1]) != i);
	}
}

public void test_isEnabled() {
	warnUnimpl("Test test_isEnabled not written");
}

/**
 * Returns <code>true</code> if the receiver is visible, and
 * <code>false</code> otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, this method
 * may still indicate that it is considered visible even though
 * it may not actually be showing.
 * </p>
 */
public void test_isVisible(){
	// This test can not be run as it currently is written.  On Windows, if a 
	// menu has no menu items, it will not become visible.
	// If we add menu items to the menu then a second problem is encountered 
	// because menu.setVisible() enters into a modal loop and execution of 
	// the JUnit test case will not continue until the menu is selected and closed.
	if (true) return;
	menu.setVisible(true);
	assertTrue(menu.isVisible());

	// api not implemented yet
	if (fCheckVisibility) {
		menu.setVisible(false);
		assertEquals(menu.isVisible(), false);
	}
}

public void test_removeHelpListenerLorg_eclipse_swt_events_HelpListener() {
	warnUnimpl("Test test_removeHelpListenerLorg_eclipse_swt_events_HelpListener not written");
}

public void test_removeMenuListenerLorg_eclipse_swt_events_MenuListener() {
	warnUnimpl("Test test_removeMenuListenerLorg_eclipse_swt_events_MenuListener not written");
}

/**
 * Sets the default menu item to the argument or removes
 * the default emphasis when the argument is <code>null</code>.
 * 
 * @param item the default menu item or null
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the menu item has been disposed</li> 
 * </ul>
 */
public void test_setDefaultItemLorg_eclipse_swt_widgets_MenuItem(){
	MenuItem mItem0 = new MenuItem(menu, SWT.NULL);
	MenuItem mItem1 = new MenuItem(menu, SWT.NULL);
	menu.setDefaultItem(mItem0);
	assertEquals(menu.getDefaultItem(), mItem0);
	assertTrue("After setDefaultItem(mItem0):", menu.getDefaultItem() != mItem1);
	menu.setDefaultItem(mItem1);
	assertEquals(menu.getDefaultItem(), mItem1);
	assertTrue("After setDefaultItem(mItem1):", menu.getDefaultItem() != mItem0);
}

public void test_setEnabledZ() {
	warnUnimpl("Test test_setEnabledZ not written");
}

public void test_setLocationII() {
	menu.setLocation(0,0);
}

public void test_setLocationLorg_eclipse_swt_graphics_Point() {
	menu.setLocation(new Point(0,0));
	try {
		menu.setLocation(null);
		fail("No exception thrown for null argument");
	}
	catch (IllegalArgumentException e) {
	}	
}

/**
 * Marks the receiver as visible if the argument is <code>true</code>,
 * and marks it invisible otherwise. 
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, marking
 * it visible may not actually cause it to be displayed.
 * </p>
 */
/* tests both getVisible and setVisble */
public void test_setVisibleZ(){
	// This test can not be run as it currently is written.  On Windows, if a 
	// menu has no menu items, it will not become visible.
	// If we add menu items to the menu then a second problem is encountered 
	// because menu.setVisible() enters into a modal loop and execution of 
	// the JUnit test case will not continue until the menu is selected and closed.
	if (true) return;
	menu.setVisible(true);
	assertTrue(menu.getVisible());
	// API not implemented yet 
	if (fCheckVisibility) {
		menu.setVisible(false);
		assertEquals(menu.getVisible(), false);
	}
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_Menu((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_Control");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_DecorationsI");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_Menu");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_MenuItem");
	methodNames.addElement("test_addHelpListenerLorg_eclipse_swt_events_HelpListener");
	methodNames.addElement("test_addMenuListenerLorg_eclipse_swt_events_MenuListener");
	methodNames.addElement("test_getDefaultItem");
	methodNames.addElement("test_getDisplay");
	methodNames.addElement("test_getEnabled");
	methodNames.addElement("test_getItemI");
	methodNames.addElement("test_getItemCount");
	methodNames.addElement("test_getItems");
	methodNames.addElement("test_getParent");
	methodNames.addElement("test_getParentItem");
	methodNames.addElement("test_getParentMenu");
	methodNames.addElement("test_getShell");
	methodNames.addElement("test_getVisible");
	methodNames.addElement("test_indexOfLorg_eclipse_swt_widgets_MenuItem");
	methodNames.addElement("test_isEnabled");
	methodNames.addElement("test_isVisible");
	methodNames.addElement("test_removeHelpListenerLorg_eclipse_swt_events_HelpListener");
	methodNames.addElement("test_removeMenuListenerLorg_eclipse_swt_events_MenuListener");
	methodNames.addElement("test_setDefaultItemLorg_eclipse_swt_widgets_MenuItem");
	methodNames.addElement("test_setEnabledZ");
	methodNames.addElement("test_setLocationII");
	methodNames.addElement("test_setLocationLorg_eclipse_swt_graphics_Point");
	methodNames.addElement("test_setVisibleZ");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Widget.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_Control")) test_ConstructorLorg_eclipse_swt_widgets_Control();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_DecorationsI")) test_ConstructorLorg_eclipse_swt_widgets_DecorationsI();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_Menu")) test_ConstructorLorg_eclipse_swt_widgets_Menu();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_MenuItem")) test_ConstructorLorg_eclipse_swt_widgets_MenuItem();
	else if (getName().equals("test_addHelpListenerLorg_eclipse_swt_events_HelpListener")) test_addHelpListenerLorg_eclipse_swt_events_HelpListener();
	else if (getName().equals("test_addMenuListenerLorg_eclipse_swt_events_MenuListener")) test_addMenuListenerLorg_eclipse_swt_events_MenuListener();
	else if (getName().equals("test_getDefaultItem")) test_getDefaultItem();
	else if (getName().equals("test_getDisplay")) test_getDisplay();
	else if (getName().equals("test_getEnabled")) test_getEnabled();
	else if (getName().equals("test_getItemI")) test_getItemI();
	else if (getName().equals("test_getItemCount")) test_getItemCount();
	else if (getName().equals("test_getItems")) test_getItems();
	else if (getName().equals("test_getParent")) test_getParent();
	else if (getName().equals("test_getParentItem")) test_getParentItem();
	else if (getName().equals("test_getParentMenu")) test_getParentMenu();
	else if (getName().equals("test_getShell")) test_getShell();
	else if (getName().equals("test_getVisible")) test_getVisible();
	else if (getName().equals("test_indexOfLorg_eclipse_swt_widgets_MenuItem")) test_indexOfLorg_eclipse_swt_widgets_MenuItem();
	else if (getName().equals("test_isEnabled")) test_isEnabled();
	else if (getName().equals("test_isVisible")) test_isVisible();
	else if (getName().equals("test_removeHelpListenerLorg_eclipse_swt_events_HelpListener")) test_removeHelpListenerLorg_eclipse_swt_events_HelpListener();
	else if (getName().equals("test_removeMenuListenerLorg_eclipse_swt_events_MenuListener")) test_removeMenuListenerLorg_eclipse_swt_events_MenuListener();
	else if (getName().equals("test_setDefaultItemLorg_eclipse_swt_widgets_MenuItem")) test_setDefaultItemLorg_eclipse_swt_widgets_MenuItem();
	else if (getName().equals("test_setEnabledZ")) test_setEnabledZ();
	else if (getName().equals("test_setLocationII")) test_setLocationII();
	else if (getName().equals("test_setLocationLorg_eclipse_swt_graphics_Point")) test_setLocationLorg_eclipse_swt_graphics_Point();
	else if (getName().equals("test_setVisibleZ")) test_setVisibleZ();
	else super.runTest();
}
}

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


import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.MenuItem
 *
 * @see org.eclipse.swt.widgets.MenuItem
 */
public class Test_org_eclipse_swt_widgets_MenuItem extends Test_org_eclipse_swt_widgets_Item {

Menu menu;
MenuItem menuItem;

public Test_org_eclipse_swt_widgets_MenuItem(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	menu = new Menu(shell);
	menuItem = new MenuItem(menu, 0);
	setWidget(menuItem);
}

protected void tearDown() {
	super.tearDown();
}

/**
 * Possible exceptions:
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public void test_ConstructorLorg_eclipse_swt_widgets_MenuI(){
	MenuItem mItem = new MenuItem(menu, SWT.NULL);
	assertNotNull(mItem);
	
	if (fCheckSwtNullExceptions) {
		try {
			mItem = new MenuItem(null, SWT.NULL);
			fail("No exception thrown");
		} 
		catch (IllegalArgumentException e) {
		}
	}
}

public void test_ConstructorLorg_eclipse_swt_widgets_MenuII(){
	MenuItem mItem = new MenuItem(menu, SWT.NULL, 0); //create a menu item at index 0
	assertNotNull(mItem);
}

public void test_addArmListenerLorg_eclipse_swt_events_ArmListener() {
	warnUnimpl("Test test_addArmListenerLorg_eclipse_swt_events_ArmListener not written");
}

public void test_addHelpListenerLorg_eclipse_swt_events_HelpListener() {
	warnUnimpl("Test test_addHelpListenerLorg_eclipse_swt_events_HelpListener not written");
}

public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	warnUnimpl("Test test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener not written");
}

public void test_checkSubclass() {
	warnUnimpl("Test test_checkSubclass not written");
}

public void test_getAccelerator() {
	warnUnimpl("Test test_getAccelerator not written");
}

public void test_getDisplay() {
	warnUnimpl("Test test_getDisplay not written");
}

public void test_getEnabled() {
	warnUnimpl("Test test_getEnabled not written");
}

public void test_getMenu() {
	warnUnimpl("Test test_getMenu not written");
}

/**
 * Returns the receiver's parent, which must be a <code>Menu</code>.
 */
 public void test_getParent(){
	assertEquals(menuItem.getParent(), menu);
}

public void test_getSelection() {
	warnUnimpl("Test test_getSelection not written");
}

/**
 * Returns <code>true</code> if the receiver is enabled, and
 * <code>false</code> otherwise. A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 */
public void test_isEnabled(){
	menuItem.setEnabled(true);
	assertTrue(menuItem.isEnabled());
	menuItem.setEnabled(false);
	assertEquals(menuItem.isEnabled(), false);
}

public void test_removeArmListenerLorg_eclipse_swt_events_ArmListener() {
	warnUnimpl("Test test_removeArmListenerLorg_eclipse_swt_events_ArmListener not written");
}

public void test_removeHelpListenerLorg_eclipse_swt_events_HelpListener() {
	warnUnimpl("Test test_removeHelpListenerLorg_eclipse_swt_events_HelpListener not written");
}

public void test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	warnUnimpl("Test test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener not written");
}

/**
 * Sets the widget accelerator.  An accelerator is the bit-wise
 * OR of zero or more modifier masks and a key. Examples:
 * <code>SWT.CONTROL | SWT.SHIFT | 'T', SWT.ALT | SWT.F2</code>.
 */
public void test_setAcceleratorI(){
	menuItem.setAccelerator(SWT.CTRL + 'Z');
	assertEquals(menuItem.getAccelerator(), SWT.CTRL + 'Z');
	
}

/**
 * Enables the receiver if the argument is <code>true</code>,
 * and disables it otherwise. A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 */
public void test_setEnabledZ(){
	menuItem.setEnabled(true);
	assertTrue(menuItem.getEnabled());
	menuItem.setEnabled(false);
	assertEquals(menuItem.getEnabled(), false);
}

public void test_setImageLorg_eclipse_swt_graphics_Image() {
	warnUnimpl("Test test_setImageLorg_eclipse_swt_graphics_Image not written");
}

/**
 * Sets the receiver's pull down menu to the argument.
 * Only <code>CASCADE</code> menu items can have a
 * pull down menu. The sequence of key strokes, button presses
 * and/or button releases that are used to request a pull down
 * menu is platform specific.
 *
 * @param menu the new pull down menu
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_MENU_NOT_DROP_DOWN - if the menu is not a drop down menu</li>
 *    <li>ERROR_MENUITEM_NOT_CASCADE - if the menu item is not a <code>CASCADE</code></li>
 *    <li>ERROR_INVALID_ARGUMENT - if the menu has been disposed</li>
 *    <li>ERROR_INVALID_PARENT - if the menu is not in the same widget tree</li>
 * </ul>
 */
public void test_setMenuLorg_eclipse_swt_widgets_Menu(){
	assertNull(menuItem.getMenu());
	MenuItem mItem = new MenuItem(menu, SWT.CASCADE);
	Menu newMenu = new Menu(shell, SWT.DROP_DOWN);
	mItem.setMenu(newMenu);
	assertEquals(mItem.getMenu(), newMenu);
}

/**
 * Sets the selection state of the receiver.
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked.
 */
public void test_setSelectionZ(){

	int[] itemStyles = {SWT.CHECK, SWT.RADIO};
	for (int i=0; i<itemStyles.length; i++)
	{
		MenuItem mItem = new MenuItem(menu, itemStyles[i]);
		mItem.setSelection(false);
		assertEquals(mItem.getSelection(), false);
		mItem.setSelection(true);
		assertTrue(mItem.getSelection());
		mItem.dispose();
	} 
}

public void test_setTextLjava_lang_String() {
	warnUnimpl("Test test_setTextLjava_lang_String not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_MenuItem((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_MenuI");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_MenuII");
	methodNames.addElement("test_addArmListenerLorg_eclipse_swt_events_ArmListener");
	methodNames.addElement("test_addHelpListenerLorg_eclipse_swt_events_HelpListener");
	methodNames.addElement("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_checkSubclass");
	methodNames.addElement("test_getAccelerator");
	methodNames.addElement("test_getDisplay");
	methodNames.addElement("test_getEnabled");
	methodNames.addElement("test_getMenu");
	methodNames.addElement("test_getParent");
	methodNames.addElement("test_getSelection");
	methodNames.addElement("test_isEnabled");
	methodNames.addElement("test_removeArmListenerLorg_eclipse_swt_events_ArmListener");
	methodNames.addElement("test_removeHelpListenerLorg_eclipse_swt_events_HelpListener");
	methodNames.addElement("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_setAcceleratorI");
	methodNames.addElement("test_setEnabledZ");
	methodNames.addElement("test_setImageLorg_eclipse_swt_graphics_Image");
	methodNames.addElement("test_setMenuLorg_eclipse_swt_widgets_Menu");
	methodNames.addElement("test_setSelectionZ");
	methodNames.addElement("test_setTextLjava_lang_String");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Item.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_MenuI")) test_ConstructorLorg_eclipse_swt_widgets_MenuI();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_MenuII")) test_ConstructorLorg_eclipse_swt_widgets_MenuII();
	else if (getName().equals("test_addArmListenerLorg_eclipse_swt_events_ArmListener")) test_addArmListenerLorg_eclipse_swt_events_ArmListener();
	else if (getName().equals("test_addHelpListenerLorg_eclipse_swt_events_HelpListener")) test_addHelpListenerLorg_eclipse_swt_events_HelpListener();
	else if (getName().equals("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_checkSubclass")) test_checkSubclass();
	else if (getName().equals("test_getAccelerator")) test_getAccelerator();
	else if (getName().equals("test_getDisplay")) test_getDisplay();
	else if (getName().equals("test_getEnabled")) test_getEnabled();
	else if (getName().equals("test_getMenu")) test_getMenu();
	else if (getName().equals("test_getParent")) test_getParent();
	else if (getName().equals("test_getSelection")) test_getSelection();
	else if (getName().equals("test_isEnabled")) test_isEnabled();
	else if (getName().equals("test_removeArmListenerLorg_eclipse_swt_events_ArmListener")) test_removeArmListenerLorg_eclipse_swt_events_ArmListener();
	else if (getName().equals("test_removeHelpListenerLorg_eclipse_swt_events_HelpListener")) test_removeHelpListenerLorg_eclipse_swt_events_HelpListener();
	else if (getName().equals("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_setAcceleratorI")) test_setAcceleratorI();
	else if (getName().equals("test_setEnabledZ")) test_setEnabledZ();
	else if (getName().equals("test_setImageLorg_eclipse_swt_graphics_Image")) test_setImageLorg_eclipse_swt_graphics_Image();
	else if (getName().equals("test_setMenuLorg_eclipse_swt_widgets_Menu")) test_setMenuLorg_eclipse_swt_widgets_Menu();
	else if (getName().equals("test_setSelectionZ")) test_setSelectionZ();
	else if (getName().equals("test_setTextLjava_lang_String")) test_setTextLjava_lang_String();
	else super.runTest();
}
}

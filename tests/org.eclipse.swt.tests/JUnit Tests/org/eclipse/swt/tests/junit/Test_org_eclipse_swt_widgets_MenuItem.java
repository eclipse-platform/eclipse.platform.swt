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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ArmEvent;
import org.eclipse.swt.events.ArmListener;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.MenuItem
 *
 * @see org.eclipse.swt.widgets.MenuItem
 */
public class Test_org_eclipse_swt_widgets_MenuItem extends Test_org_eclipse_swt_widgets_Item {

public Test_org_eclipse_swt_widgets_MenuItem(String name) {
	super(name);
}

@Override
protected void setUp() {
	super.setUp();
	menu = new Menu(shell);
	menuItem = new MenuItem(menu, 0);
	setWidget(menuItem);
}

public void test_ConstructorLorg_eclipse_swt_widgets_MenuI() {
	MenuItem mItem = new MenuItem(menu, SWT.NULL);
	assertNotNull(mItem);
	
	try {
		new MenuItem(null, SWT.NULL);
		fail("No exception thrown");
	} 
	catch (IllegalArgumentException e) {
	}
	mItem = new MenuItem(menu, SWT.CHECK);
	assertTrue(mItem.getStyle()==SWT.CHECK);
	mItem.dispose();
	mItem = new MenuItem(menu, SWT.CASCADE);
	assertTrue(mItem.getStyle()==SWT.CASCADE);
	mItem.dispose();
	mItem = new MenuItem(menu, SWT.PUSH);
	assertTrue(mItem.getStyle()==SWT.PUSH);
	mItem.dispose();
	mItem = new MenuItem(menu, SWT.SEPARATOR);
	assertTrue(mItem.getStyle()==SWT.SEPARATOR);
	mItem.dispose();
	mItem = new MenuItem(menu, SWT.RADIO);
	assertTrue(mItem.getStyle()==SWT.RADIO);
	mItem.dispose();
}

public void test_ConstructorLorg_eclipse_swt_widgets_MenuII() {
	MenuItem mItem = new MenuItem(menu, SWT.NULL, 0); //create a menu item at index 0
	assertNotNull(mItem);
	assertTrue(menu.getItem(0).equals(mItem));
	mItem = new MenuItem(menu, SWT.NULL, 1);
	assertNotNull(mItem);
	assertTrue(menu.getItem(1).equals(mItem));
}

public void test_addArmListenerLorg_eclipse_swt_events_ArmListener() {
	listenerCalled = false;
	ArmListener listener = new ArmListener() {
		public void widgetArmed(ArmEvent e) {
			listenerCalled = true;
		}
	};
	
	try {
		menuItem.addArmListener(null);
		fail("No exception thrown for addArmListener with null argument");
	} catch (IllegalArgumentException e) {
	}
	
	menuItem.addArmListener(listener);
	menuItem.notifyListeners(SWT.Arm, new Event());
	assertTrue(listenerCalled);
	
	try {
		menuItem.removeArmListener(null);
		fail("No exception thrown for removeArmListener with null argument");
	} catch (IllegalArgumentException e) {
	}
	listenerCalled = false;
	menuItem.removeArmListener(listener);
	menuItem.notifyListeners(SWT.Arm, new Event());
	assertFalse(listenerCalled);
}

public void test_addHelpListenerLorg_eclipse_swt_events_HelpListener() {
	listenerCalled = false;
	HelpListener listener = new HelpListener() {
		public void helpRequested(HelpEvent e) {
			listenerCalled = true;
		}
	};
	
	try {
		menuItem.addHelpListener(null);
		fail("No exception thrown for addHelpListener with null argument");
	} catch (IllegalArgumentException e) {
	}
	
	menuItem.addHelpListener(listener);
	menuItem.notifyListeners(SWT.Help, new Event());
	assertTrue(listenerCalled);
	
	try {
		menuItem.removeHelpListener(null);
		fail("No exception thrown for removeHelpListener with null argument");
	} catch (IllegalArgumentException e) {
	}
	listenerCalled = false;
	menuItem.removeHelpListener(listener);
	menuItem.notifyListeners(SWT.Help, new Event());
	assertFalse(listenerCalled);
}

public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	listenerCalled = false;
	SelectionListener listener = new SelectionListener() {
		public void widgetSelected(SelectionEvent e) {
			listenerCalled = true;
		}
		public void widgetDefaultSelected(SelectionEvent e) {
		}
	};
	
	try {
		menuItem.addSelectionListener(null);
		fail("No exception thrown for addSelectionListener with null argument");
	} catch (IllegalArgumentException e) {
	}
	
	menuItem.addSelectionListener(listener);
	menuItem.notifyListeners(SWT.Selection, new Event());
	assertTrue(listenerCalled);
	
	try {
		menuItem.removeSelectionListener(null);
		fail("No exception thrown for removeSelectionListener with null argument");
	} catch (IllegalArgumentException e) {
	}
	listenerCalled = false;
	menuItem.removeSelectionListener(listener);
	menuItem.notifyListeners(SWT.Selection, new Event());
	assertFalse(listenerCalled);
}

public void test_getAccelerator() {
	menuItem.setAccelerator(SWT.MOD1 + 'X');
	assertEquals(menuItem.getAccelerator(), SWT.MOD1 + 'X');
	menuItem.setAccelerator(SWT.MOD2 + 'Y');
	assertEquals(menuItem.getAccelerator(), SWT.MOD2 + 'Y');
	menuItem.setAccelerator(SWT.MOD3 + 'Z');
	assertEquals(menuItem.getAccelerator(), SWT.MOD3 + 'Z');
}

public void test_getParent() {
	assertEquals(menuItem.getParent(), menu);
}

public void test_isEnabled() {
	menuItem.setEnabled(true);
	assertTrue(menuItem.isEnabled());
	menuItem.setEnabled(false);
	assertEquals(menuItem.isEnabled(), false);
}

public void test_setAcceleratorI() {
	menuItem.setAccelerator(SWT.CTRL + 'Z');
	assertEquals(menuItem.getAccelerator(), SWT.CTRL + 'Z');
}

public void test_setEnabledZ() {
	menuItem.setEnabled(true);
	assertTrue(menuItem.getEnabled());
	menuItem.setEnabled(false);
	assertEquals(menuItem.getEnabled(), false);
}

@Override
public void test_setImageLorg_eclipse_swt_graphics_Image() {
	assertNull(menuItem.getImage());
	menuItem.setImage(images[0]);
	assertEquals(images[0], menuItem.getImage());
	assertTrue(menuItem.getImage() != images[1]);
	menuItem.setImage(null);
	assertNull(menuItem.getImage());
}

public void test_setMenuLorg_eclipse_swt_widgets_Menu() {
	assertNull(menuItem.getMenu());
	MenuItem mItem = new MenuItem(menu, SWT.CASCADE);
	Menu newMenu = new Menu(shell, SWT.DROP_DOWN);
	mItem.setMenu(newMenu);
	assertEquals(mItem.getMenu(), newMenu);
}

public void test_setSelectionZ() {

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

@Override
public void test_setTextLjava_lang_String() {
	menuItem.setText("ABCDEFG");
	assertTrue(menuItem.getText().equals("ABCDEFG"));
	try {
		menuItem.setText(null);
		fail("No exception thrown for addArmListener with null argument");
	} catch (IllegalArgumentException e) {
	}
	menuItem.setText("ABCDEFG");
	menuItem.setAccelerator(SWT.MOD1 + 'A');
	assertTrue(menuItem.getText().startsWith("ABCDEFG"));
	menuItem.setAccelerator(0);
	menuItem.setText("AB&CDEFG");
	assertTrue(menuItem.getText().equals("AB&CDEFG"));
}

/* custom */
Menu menu;
MenuItem menuItem;
}

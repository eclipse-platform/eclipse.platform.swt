/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Menu
 *
 * @see org.eclipse.swt.widgets.Menu
 */
public class Test_org_eclipse_swt_widgets_Menu extends Test_org_eclipse_swt_widgets_Widget {

@Override
@Before
public void setUp() {
	super.setUp();
	menu = new Menu(shell);
	setWidget(menu);
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_Control() {
	Composite comp = new Composite(shell, SWT.NULL);
	new Menu(comp);
	comp.dispose();
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_DecorationsI() {
	Menu newMenu;
	Shell nullShell = null;
	try {
		newMenu = new Menu(nullShell, SWT.NULL);
		newMenu.dispose();
		fail("No exception thrown for parent == null");
	} catch (IllegalArgumentException e) {
	}

	newMenu = new Menu(shell, SWT.NULL);
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_Menu() {
	new Menu(menu);
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_MenuItem() {
	Menu newMenu;
	MenuItem mItem = null;
	try {
		newMenu = new Menu(mItem);
		newMenu.dispose();
		fail("No exception thrown for parent == null");
	} catch (IllegalArgumentException e) {
	}

	mItem = new MenuItem(menu, SWT.NULL);
	newMenu = new Menu(mItem);
}

@Test
public void test_addHelpListenerLorg_eclipse_swt_events_HelpListener() {
	listenerCalled = false;
	HelpListener listener = e -> listenerCalled = true;

	try {
		menu.addHelpListener(null);
		fail("No exception thrown for addHelpListener with null argument");
	} catch (IllegalArgumentException e) {
	}

	menu.addHelpListener(listener);
	menu.notifyListeners(SWT.Help, new Event());
	assertTrue(listenerCalled);

	try {
		menu.removeHelpListener(null);
		fail("No exception thrown for removeHelpListener with null argument");
	} catch (IllegalArgumentException e) {
	}

	listenerCalled = false;
	menu.removeHelpListener(listener);
	menu.notifyListeners(SWT.Help, new Event());
	assertFalse(listenerCalled);
}

@Test
public void test_addMenuListenerLorg_eclipse_swt_events_MenuListener() {
	listenerCalled = false;
	MenuListener menuListener = new MenuListener() {
		@Override
		public void menuShown(MenuEvent e) {
			listenerCalled = true;
		}
		@Override
		public void menuHidden(MenuEvent e) {
			listenerCalled = true;
		}
	};

	try {
		menu.addMenuListener(null);
		fail("No exception thrown for addMenuListener with null argument");
	} catch (IllegalArgumentException e) {
	}

	menu.addMenuListener(menuListener);
	menu.notifyListeners(SWT.Show, new Event());
	assertTrue(":a:", listenerCalled);

	listenerCalled = false;
	menu.notifyListeners(SWT.Hide, new Event());
	assertTrue(":b:", listenerCalled);

	try {
		menu.removeMenuListener(null);
		fail("No exception thrown for removeMenuListener with null argument");
	} catch (IllegalArgumentException e) {
	}

	listenerCalled = false;
	menu.removeMenuListener(menuListener);
	menu.notifyListeners(SWT.Show, new Event());
	assertFalse(listenerCalled);
}

@Test
public void test_addMenuListenerMenuShownAdapterLorg_eclipse_swt_events_MenuListener() {
	MenuListener listener = MenuListener.menuShownAdapter(e -> listenerCalled = true);
	menu.addMenuListener(listener);
	listenerCalled = false;

	menu.notifyListeners(SWT.Show, new Event());
	assertTrue(listenerCalled);

	listenerCalled = false;

	menu.notifyListeners(SWT.Hide, new Event());
	assertFalse(listenerCalled);

	menu.removeMenuListener(listener);
	listenerCalled = false;

	menu.notifyListeners(SWT.Show, new Event());
	assertFalse(listenerCalled);

	menu.notifyListeners(SWT.Hide, new Event());
	assertFalse(listenerCalled);
}

@Test
public void test_addMenuListenerMenuHiddenAdapterLorg_eclipse_swt_events_MenuListener() {
	MenuListener listener = MenuListener.menuHiddenAdapter(e -> listenerCalled = true);
	menu.addMenuListener(listener);
	listenerCalled = false;

	menu.notifyListeners(SWT.Hide, new Event());
	assertTrue(listenerCalled);

	listenerCalled = false;

	menu.notifyListeners(SWT.Show, new Event());
	assertFalse(listenerCalled);

	menu.removeMenuListener(listener);
	listenerCalled = false;

	menu.notifyListeners(SWT.Hide, new Event());
	assertFalse(listenerCalled);

	menu.notifyListeners(SWT.Show, new Event());
	assertFalse(listenerCalled);
}


@Test
public void test_getItemCount() {
	int number = 10;
	for (int i = 0; i<number ; i++){
		assertEquals(menu.getItemCount(), i);
	  	new MenuItem(menu, 0);
	}
}

@Test
public void test_getItemI() {
	MenuItem mItem0 = new MenuItem(menu, SWT.NULL);
	MenuItem mItem1 = new MenuItem(menu, SWT.NULL);
	assertEquals(menu.getItem(0), mItem0);
	assertEquals(menu.getItem(1), mItem1);
}

@Test
public void test_getItems() {
	int number = 5;
	MenuItem[] items = new MenuItem[number];
	for (int i = 0; i<number ; i++){
	  	items[i] = new MenuItem(menu, 0);
	}
	assertArrayEquals(":a:", items, menu.getItems());

	menu.getItems()[0].dispose();
	assertArrayEquals(":b:", new MenuItem[]{items[1], items[2], items[3], items[4]}, menu.getItems());

	menu.getItems()[3].dispose();
	assertArrayEquals(":c:", new MenuItem[]{items[1], items[2], items[3]}, menu.getItems());

	menu.getItems()[1].dispose();
	assertArrayEquals(":d:", new MenuItem[]{items[1], items[3]}, menu.getItems());
}

@Test
public void test_getParent() {
	assertEquals(menu.getParent(), shell);
}

@Test
public void test_getParentItem() {
	MenuItem mItem = new MenuItem(menu, SWT.CASCADE);
	Menu newMenu = new Menu(shell, SWT.DROP_DOWN);
	assertNull(newMenu.getParentItem());
	mItem.setMenu(newMenu);
	assertEquals(newMenu.getParentItem(), mItem);
}

@Test
public void test_getParentMenu() {
	MenuItem mItem = new MenuItem(menu, SWT.CASCADE);
	Menu newMenu = new Menu(shell, SWT.DROP_DOWN);
	assertNull(newMenu.getParentMenu());
	mItem.setMenu(newMenu);
	assertEquals(newMenu.getParentMenu(), menu);
}

@Test
public void test_getShell() {
	assertEquals(menu.getShell(), shell);
}

@Test
public void test_indexOfLorg_eclipse_swt_widgets_MenuItem() {
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

@Test
public void test_setDefaultItemLorg_eclipse_swt_widgets_MenuItem() {
	if (SwtTestUtil.isGTK) {
		//TODO Fix GTK failure.
		if (SwtTestUtil.verbose) {
			System.out.println("Excluded test_setDefaultItemLorg_eclipse_swt_widgets_MenuItem(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Menu)");
		}
		return;
	}
	MenuItem mItem0 = new MenuItem(menu, SWT.NULL);
	MenuItem mItem1 = new MenuItem(menu, SWT.NULL);
	menu.setDefaultItem(mItem0);
	assertEquals(menu.getDefaultItem(), mItem0);
	assertTrue("After setDefaultItem(mItem0):", menu.getDefaultItem() != mItem1);
	menu.setDefaultItem(mItem1);
	assertEquals(menu.getDefaultItem(), mItem1);
	assertTrue("After setDefaultItem(mItem1):", menu.getDefaultItem() != mItem0);
}

@Test
public void test_setEnabledZ() {
	menu.setEnabled(true);
	assertTrue(menu.getEnabled());
	menu.setEnabled(false);
	assertFalse(menu.getEnabled());
}

@Test
public void test_setLocationII() {
	menu.setLocation(0,0);
}

@Test
public void test_setLocationLorg_eclipse_swt_graphics_Point() {
	menu.setLocation(new Point(0,0));
	try {
		menu.setLocation(null);
		fail("No exception thrown for null argument");
	}
	catch (IllegalArgumentException e) {
	}
}

/* custom */
Menu menu;
}

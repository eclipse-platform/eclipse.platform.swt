/*******************************************************************************
 * Copyright (c) 2000, 2025 IBM Corporation and others.
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ArmListener;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.MenuItem
 *
 * @see org.eclipse.swt.widgets.MenuItem
 */
public class Test_org_eclipse_swt_widgets_MenuItem extends Test_org_eclipse_swt_widgets_Item {

@Override
@BeforeEach
public void setUp() {
	super.setUp();
	menu = new Menu(shell);
	menuItem = new MenuItem(menu, 0);
	setWidget(menuItem);
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_MenuI() {
	MenuItem mItem = new MenuItem(menu, SWT.NULL);
	assertNotNull(mItem);

	assertThrows(IllegalArgumentException.class, () -> new MenuItem(null, SWT.NULL));
	mItem = new MenuItem(menu, SWT.CHECK);
	assertEquals(SWT.CHECK, mItem.getStyle());
	mItem.dispose();
	mItem = new MenuItem(menu, SWT.CASCADE);
	assertEquals(SWT.CASCADE, mItem.getStyle());
	mItem.dispose();
	mItem = new MenuItem(menu, SWT.PUSH);
	assertEquals(SWT.PUSH, mItem.getStyle());
	mItem.dispose();
	mItem = new MenuItem(menu, SWT.SEPARATOR);
	assertEquals(SWT.SEPARATOR, mItem.getStyle());
	mItem.dispose();
	mItem = new MenuItem(menu, SWT.RADIO);
	assertEquals(SWT.RADIO, mItem.getStyle());
	mItem.dispose();
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_MenuII() {
	MenuItem mItem = new MenuItem(menu, SWT.NULL, 0); //create a menu item at index 0
	assertNotNull(mItem);
	assertEquals(mItem, menu.getItem(0));
	mItem = new MenuItem(menu, SWT.NULL, 1);
	assertNotNull(mItem);
	assertEquals(mItem, menu.getItem(1));
}

@Test
public void test_addArmListenerLorg_eclipse_swt_events_ArmListener() {
	listenerCalled = false;
	ArmListener listener = e -> listenerCalled = true;

	assertThrows(IllegalArgumentException.class, () -> menuItem.addArmListener(null));

	menuItem.addArmListener(listener);
	menuItem.notifyListeners(SWT.Arm, new Event());
	assertTrue(listenerCalled);

	assertThrows(IllegalArgumentException.class, () -> menuItem.removeArmListener(null));
	listenerCalled = false;
	menuItem.removeArmListener(listener);
	menuItem.notifyListeners(SWT.Arm, new Event());
	assertFalse(listenerCalled);
}

@Test
public void test_addHelpListenerLorg_eclipse_swt_events_HelpListener() {
	listenerCalled = false;
	HelpListener listener = e -> listenerCalled = true;

	assertThrows(IllegalArgumentException.class, () -> menuItem.addHelpListener(null));

	menuItem.addHelpListener(listener);
	menuItem.notifyListeners(SWT.Help, new Event());
	assertTrue(listenerCalled);

	assertThrows(IllegalArgumentException.class, () -> menuItem.removeHelpListener(null));
	listenerCalled = false;
	menuItem.removeHelpListener(listener);
	menuItem.notifyListeners(SWT.Help, new Event());
	assertFalse(listenerCalled);
}

@Test
public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	listenerCalled = false;
	SelectionListener listener = new SelectionListener() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			listenerCalled = true;
		}
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
		}
	};

	assertThrows(IllegalArgumentException.class, () -> menuItem.addSelectionListener(null));

	menuItem.addSelectionListener(listener);
	menuItem.notifyListeners(SWT.Selection, new Event());
	assertTrue(listenerCalled);

	assertThrows(IllegalArgumentException.class, () -> menuItem.removeSelectionListener(null));
	listenerCalled = false;
	menuItem.removeSelectionListener(listener);
	menuItem.notifyListeners(SWT.Selection, new Event());
	assertFalse(listenerCalled);
}

@Test
public void test_addSelectionListenerWidgetSelectedAdapterLorg_eclipse_swt_events_SelectionListener() {
	listenerCalled = false;
	SelectionListener listener = SelectionListener.widgetSelectedAdapter(e -> listenerCalled = true);

	menuItem.addSelectionListener(listener);
	menuItem.notifyListeners(SWT.Selection, new Event());
	assertTrue(listenerCalled);

	listenerCalled = false;
	menuItem.removeSelectionListener(listener);
	menuItem.notifyListeners(SWT.Selection, new Event());
	assertFalse(listenerCalled);
}

@Test
public void test_getAccelerator() {
	menuItem.setAccelerator(SWT.MOD1 + 'X');
	assertEquals(menuItem.getAccelerator(), SWT.MOD1 + 'X');
	menuItem.setAccelerator(SWT.MOD2 + 'Y');
	assertEquals(menuItem.getAccelerator(), SWT.MOD2 + 'Y');
	menuItem.setAccelerator(SWT.MOD3 + 'Z');
	assertEquals(menuItem.getAccelerator(), SWT.MOD3 + 'Z');
}

@Test
public void test_getParent() {
	assertEquals(menuItem.getParent(), menu);
}

@Test
public void test_isEnabled() {
	menuItem.setEnabled(true);
	assertTrue(menuItem.isEnabled());
	menuItem.setEnabled(false);
	assertFalse(menuItem.isEnabled());
}

@Test
public void test_setAcceleratorI() {
	menuItem.setAccelerator(SWT.CTRL + 'Z');
	assertEquals(menuItem.getAccelerator(), SWT.CTRL + 'Z');
}

@Test
public void test_setEnabledZ() {
	menuItem.setEnabled(true);
	assertTrue(menuItem.getEnabled());
	menuItem.setEnabled(false);
	assertFalse(menuItem.getEnabled());
}

@Tag("gtk4-todo")
@Override
@Test
public void test_setImageLorg_eclipse_swt_graphics_Image() {
	assertNull(menuItem.getImage());
	menuItem.setImage(images[0]);
	assertEquals(images[0], menuItem.getImage());
	assertTrue(menuItem.getImage() != images[1]);
	menuItem.setImage(null);
	assertNull(menuItem.getImage());
}

@Test
public void test_setMenuLorg_eclipse_swt_widgets_Menu() {
	assertNull(menuItem.getMenu());
	MenuItem mItem = new MenuItem(menu, SWT.CASCADE);
	Menu newMenu = new Menu(shell, SWT.DROP_DOWN);
	mItem.setMenu(newMenu);
	assertEquals(mItem.getMenu(), newMenu);
}

@Test
public void test_setSelectionZ() {

	int[] itemStyles = {SWT.CHECK, SWT.RADIO};
	for (int itemStyle : itemStyles) {
		MenuItem mItem = new MenuItem(menu, itemStyle);
		mItem.setSelection(false);
		assertFalse(mItem.getSelection());
		mItem.setSelection(true);
		assertTrue(mItem.getSelection());
		mItem.dispose();
	}
}

@Override
@Test
public void test_setTextLjava_lang_String() {
	menuItem.setText("ABCDEFG");
	assertEquals("ABCDEFG", menuItem.getText());
	assertThrows(IllegalArgumentException.class, () -> menuItem.setText(null));
	menuItem.setText("ABCDEFG");
	menuItem.setAccelerator(SWT.MOD1 + 'A');
	assertTrue(menuItem.getText().startsWith("ABCDEFG"));
	menuItem.setAccelerator(0);
	menuItem.setText("AB&CDEFG");
	assertEquals("AB&CDEFG", menuItem.getText());
}

/* custom */
Menu menu;
MenuItem menuItem;
}

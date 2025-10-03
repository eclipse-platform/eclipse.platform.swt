/*******************************************************************************
 * Copyright (c) 2000, 2020 IBM Corporation and others.
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.TabItem
 *
 * @see org.eclipse.swt.widgets.TabItem
 */
public class Test_org_eclipse_swt_widgets_TabItem extends Test_org_eclipse_swt_widgets_Item {

@Override
@BeforeEach
public void setUp() {
	super.setUp();
	tabFolder = new TabFolder(shell, 0);
	tabItem = new TabItem(tabFolder, 0);
	setWidget(tabItem);
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_TabFolderI() {
	assertThrows(IllegalArgumentException.class, () -> new TabItem(null, SWT.NULL));
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_TabFolderII() {
	TabItem tItem = new TabItem(tabFolder, SWT.NULL, 0);

	assertEquals(tItem, tabFolder.getItems()[0]);

	tItem = new TabItem(tabFolder, SWT.NULL, 1);
	assertEquals(tItem, tabFolder.getItems()[1]);

	tItem = new TabItem(tabFolder, SWT.NULL, 1);
	assertEquals(tItem, tabFolder.getItems()[1]);

	assertThrows(IllegalArgumentException.class, () -> new TabItem(tabFolder, SWT.NULL, -1));
	assertEquals(tItem, tabFolder.getItems()[1]);

	assertThrows(IllegalArgumentException.class, () -> new TabItem(tabFolder, SWT.NULL, tabFolder.getItemCount() + 1));
	assertEquals(tItem, tabFolder.getItems()[1]);

	assertThrows(IllegalArgumentException.class, () -> new TabItem(null, SWT.NULL, 0));
}

@Test
public void test_getParent() {
	assertEquals(tabFolder, tabItem.getParent());
}

@Test
public void test_setControlLorg_eclipse_swt_widgets_Control() {
	Control control = new Table(tabFolder, SWT.NULL);

	assertNull(tabItem.getControl());

	tabItem.setControl(control);
	assertEquals(control, tabItem.getControl());

	tabItem.setControl(null);
	assertNull(tabItem.getControl());
}

@Override
@Test
public void test_setImageLorg_eclipse_swt_graphics_Image() {
}

@Override
@Test
public void test_setTextLjava_lang_String() {
}

@Test
public void test_setToolTipTextLjava_lang_String() {
	tabItem.setToolTipText("fred");
	assertEquals("fred", tabItem.getToolTipText());
	tabItem.setToolTipText("fredttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt");
	assertEquals("fredttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt", tabItem.getToolTipText());
	tabItem.setToolTipText(null);
	assertNull(tabItem.getToolTipText());
}

/* custom */
TabFolder tabFolder;
TabItem tabItem;

}

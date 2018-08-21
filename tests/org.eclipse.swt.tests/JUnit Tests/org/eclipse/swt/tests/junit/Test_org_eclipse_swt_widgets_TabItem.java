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


import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.TabItem
 *
 * @see org.eclipse.swt.widgets.TabItem
 */
public class Test_org_eclipse_swt_widgets_TabItem extends Test_org_eclipse_swt_widgets_Item {

@Override
@Before
public void setUp() {
	super.setUp();
	tabFolder = new TabFolder(shell, 0);
	tabItem = new TabItem(tabFolder, 0);
	setWidget(tabItem);
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_TabFolderI() {
	try {
		new TabItem(null, SWT.NULL);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_TabFolderII() {
	TabItem tItem = new TabItem(tabFolder, SWT.NULL, 0);

	assertTrue(":a:", tabFolder.getItems()[0] == tItem);

	tItem = new TabItem(tabFolder, SWT.NULL, 1);
	assertTrue(":b:", tabFolder.getItems()[1] == tItem);

	tItem = new TabItem(tabFolder, SWT.NULL, 1);
	assertTrue(":c:", tabFolder.getItems()[1] == tItem);

	try {
		new TabItem(tabFolder, SWT.NULL, -1);
		fail("No exception thrown");
	}
	catch (IllegalArgumentException e) {
	}
	finally {
		assertTrue(":d:", tabFolder.getItems()[1] == tItem);
	}
	try {
		new TabItem(tabFolder, SWT.NULL, tabFolder.getItemCount() + 1);
		fail("No exception thrown");
	}
	catch (IllegalArgumentException e) {
	}
	finally {
		assertTrue(":e:", tabFolder.getItems()[1] == tItem);
	}
	try {
		new TabItem(null, SWT.NULL, 0);
		fail("No exception thrown");
	}
	catch (IllegalArgumentException e) {
	}
}

@Test
public void test_getParent() {
	assertTrue(":a: ", tabItem.getParent() == tabFolder);
}

@Test
public void test_setControlLorg_eclipse_swt_widgets_Control() {
	Control control = new Table(tabFolder, SWT.NULL);

	assertNull(":a: ", tabItem.getControl());

	tabItem.setControl(control);
	assertTrue(":b: ", tabItem.getControl() == control);

	tabItem.setControl(null);
	assertNull(":c: ", tabItem.getControl());
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
	assertTrue(":a: ", tabItem.getToolTipText().equals("fred"));
	tabItem.setToolTipText("fredttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt");
	assertTrue(":b: ", tabItem.getToolTipText().equals("fredttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt"));
	tabItem.setToolTipText(null);
	assertNull(":c: ", tabItem.getToolTipText());
}

/* custom */
TabFolder tabFolder;
TabItem tabItem;

}

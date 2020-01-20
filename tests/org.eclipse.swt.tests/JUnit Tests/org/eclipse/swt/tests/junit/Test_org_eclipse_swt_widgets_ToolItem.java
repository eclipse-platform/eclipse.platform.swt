/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.ToolItem
 *
 * @see org.eclipse.swt.widgets.ToolItem
 */
public class Test_org_eclipse_swt_widgets_ToolItem extends Test_org_eclipse_swt_widgets_Item {

@Override
@Before
public void setUp() {
	super.setUp();
	toolBar = new ToolBar(shell, 0);
	toolItem = new ToolItem(toolBar, 0);
	setWidget(toolItem);
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_ToolBarI() {
	try {
		new ToolItem(null, SWT.NULL);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

@Test
public void test_getToolTipText() {
	toolItem.setToolTipText("fred");
	assertTrue(":a: ", toolItem.getToolTipText().equals("fred"));
	toolItem.setToolTipText("fredttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt");
	assertTrue(":a: ", toolItem.getToolTipText().equals("fredttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt"));
}

@Override
@Test
public void test_setImageLorg_eclipse_swt_graphics_Image() {
}

@Test
public void test_setDisabledImage() {
		toolItem.setImage(images[0]);
		toolItem.setDisabledImage(images[1]);
		toolItem.setEnabled(false);
		assertEquals(images[0], toolItem.getImage());

		toolItem.setEnabled(true);
		assertEquals(images[0], toolItem.getImage());

		toolItem.setDisabledImage(images[0]);
		assertEquals(images[0], toolItem.getImage());

		toolItem.setEnabled(false);
		assertEquals(images[0], toolItem.getImage());

		toolItem.setImage(images[0]);
		toolItem.setEnabled(true);
		assertEquals(images[0], toolItem.getImage());

		toolItem.setDisabledImage(images[2]);
		toolItem.setEnabled(false);
		assertEquals(images[0], toolItem.getImage());

		toolItem.setEnabled(true);
		toolItem.setDisabledImage(null);
		toolItem.setEnabled(false);
		assertEquals(images[0], toolItem.getImage());

		toolItem.setEnabled(true);
		toolItem.setDisabledImage(null);
		assertEquals(images[0], toolItem.getImage());

		toolItem.setEnabled(false);
		toolItem.setDisabledImage(null);
		assertEquals(images[0], toolItem.getImage());

		toolItem.setImage(null);
		assertEquals(null, toolItem.getImage());
		toolItem.setEnabled(false);
		toolItem.setDisabledImage(images[1]);
		toolItem.setImage(images[1]);
		assertEquals(images[1], toolItem.getImage());
		toolItem.setImage(null);
		assertEquals(null, toolItem.getImage());
		assertEquals(images[1], toolItem.getDisabledImage());
}

@Override
@Test
public void test_setTextLjava_lang_String() {
}

/* custom */
ToolBar toolBar;
ToolItem toolItem;
}

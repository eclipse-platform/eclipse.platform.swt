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
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Automated Test Suite for class org.eclipse.swt.widgets.ToolItem
 *
 * @see org.eclipse.swt.widgets.ToolItem
 */
public class Test_org_eclipse_swt_widgets_ToolItem extends Test_org_eclipse_swt_widgets_Item {

@Override
@BeforeEach
public void setUp() {
	super.setUp();
	toolBar = new ToolBar(shell, 0);
	toolItem = new ToolItem(toolBar, 0);
	setWidget(toolItem);
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_ToolBarI() {
	assertThrows(IllegalArgumentException.class, () -> new ToolItem(null, SWT.NULL),
			"No exception thrown for parent == null");
}

@Test
public void test_getToolTipText() {
	toolItem.setToolTipText("fred");
	assertEquals("fred", toolItem.getToolTipText());
	toolItem.setToolTipText("fredttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt");
	assertEquals("fredttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt", toolItem.getToolTipText());
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

@Test
public void test_getBackground_inheritsWithBackgroundModeForce() {
	assertEquals(PARENT_COLOR, createItemOnColoredParent(SWT.INHERIT_FORCE).getBackground());
}

@Test
public void test_getBackground_inheritsWithBackgroundModeDefault() {
	assertEquals(PARENT_COLOR, createItemOnColoredParent(SWT.INHERIT_DEFAULT).getBackground());
}

@Test
public void test_getBackground_precedence() {
	ToolItem item = createItemOnColoredParent(SWT.INHERIT_FORCE);
	ToolBar bar = item.getParent();

	bar.setBackground(TOOLBAR_COLOR);
	assertEquals(TOOLBAR_COLOR, item.getBackground());
	item.setBackground(ITEM_COLOR);
	assertEquals(ITEM_COLOR, item.getBackground());
	item.setBackground(null);
	assertEquals(TOOLBAR_COLOR, item.getBackground());
	bar.setBackground(null);
	assertEquals(PARENT_COLOR, item.getBackground());
}

private ToolItem createItemOnColoredParent(int backgroundMode) {
	Composite parent = new Composite(shell, SWT.NONE);
	parent.setBackgroundMode(backgroundMode);
	parent.setBackground(PARENT_COLOR);
	return new ToolItem(new ToolBar(parent, SWT.FLAT), SWT.CHECK);
}

/* custom */
private static final Color PARENT_COLOR = new Color(10, 20, 30);
private static final Color TOOLBAR_COLOR = new Color(40, 50, 60);
private static final Color ITEM_COLOR = new Color(70, 80, 90);

ToolBar toolBar;
ToolItem toolItem;
}

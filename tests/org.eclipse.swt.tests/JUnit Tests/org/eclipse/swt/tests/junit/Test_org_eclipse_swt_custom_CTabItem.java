/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Color;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.CTabItem
 *
 * @see org.eclipse.swt.custom.CTabItem
 */
public class Test_org_eclipse_swt_custom_CTabItem extends Test_org_eclipse_swt_widgets_Item {

	CTabFolder cTabFolder;
	CTabItem cTabItem;

@Override
@Before
public void setUp() {
	super.setUp();
	cTabFolder = new CTabFolder(shell, SWT.NONE);
	cTabItem = new CTabItem(cTabFolder, SWT.NONE);
	setWidget(cTabItem);
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
public void test_setForegroundLorg_eclipse_swt_graphics_Color() {
	Color red = cTabFolder.getDisplay().getSystemColor(SWT.COLOR_RED);
	cTabFolder.setForeground(red);

	Color blue = cTabFolder.getDisplay().getSystemColor(SWT.COLOR_BLUE);
	cTabItem.setForeground(blue);
	assertEquals(blue, cTabItem.getForeground());

	cTabItem.setForeground(null);
	assertEquals(red, cTabItem.getForeground());
}

@Test
public void test_setSelectionForegroundLorg_eclipse_swt_graphics_Color() {
	Color red = cTabFolder.getDisplay().getSystemColor(SWT.COLOR_RED);
	cTabFolder.setSelectionForeground(red);

	Color blue = cTabFolder.getDisplay().getSystemColor(SWT.COLOR_BLUE);
	cTabItem.setSelectionForeground(blue);
	assertEquals(blue, cTabItem.getSelectionForeground());

	cTabItem.setSelectionForeground(null);
	assertEquals(red, cTabItem.getSelectionForeground());
}
}
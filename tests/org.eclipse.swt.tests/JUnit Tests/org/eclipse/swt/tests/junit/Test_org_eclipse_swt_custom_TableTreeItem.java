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

import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.custom.TableTreeItem;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.TableTreeItem
 *
 * @see org.eclipse.swt.custom.TableTreeItem
 */
@SuppressWarnings("deprecation")
public class Test_org_eclipse_swt_custom_TableTreeItem extends Test_org_eclipse_swt_widgets_Item {

	TableTree tableTree;
	TableTreeItem tableTreeItem;

@Override
@Before
public void setUp() {
	super.setUp();
	tableTree = new TableTree(shell, 0);
	tableTreeItem = new TableTreeItem(tableTree, 0);
	setWidget(tableTreeItem);
}

@Override
@Test
public void test_setImageLorg_eclipse_swt_graphics_Image() {
}

@Override
@Test
public void test_setTextLjava_lang_String() {
}
}

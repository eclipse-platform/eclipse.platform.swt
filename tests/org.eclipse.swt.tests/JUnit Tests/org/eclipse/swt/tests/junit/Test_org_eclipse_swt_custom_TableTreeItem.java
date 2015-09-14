/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.custom.TableTreeItem;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.TableTreeItem
 *
 * @see org.eclipse.swt.custom.TableTreeItem
 */
@SuppressWarnings("deprecation")
public class Test_org_eclipse_swt_custom_TableTreeItem extends Test_org_eclipse_swt_widgets_Item {

	TableTree tableTree;
	TableTreeItem tableTreeItem;
	
public Test_org_eclipse_swt_custom_TableTreeItem(String name) {
	super(name);
}

@Override
protected void setUp() {
	super.setUp();
	tableTree = new TableTree(shell, 0);
	tableTreeItem = new TableTreeItem(tableTree, 0);
	setWidget(tableTreeItem);
}

@Override
public void test_setImageLorg_eclipse_swt_graphics_Image() {
}

@Override
public void test_setTextLjava_lang_String() {
}
}

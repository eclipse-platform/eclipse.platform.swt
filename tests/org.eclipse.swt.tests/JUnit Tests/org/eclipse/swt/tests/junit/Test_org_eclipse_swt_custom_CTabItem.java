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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.CTabItem
 *
 * @see org.eclipse.swt.custom.CTabItem
 */
public class Test_org_eclipse_swt_custom_CTabItem extends Test_org_eclipse_swt_widgets_Item {

	CTabFolder cTabFolder;
	CTabItem cTabItem;

public Test_org_eclipse_swt_custom_CTabItem(String name) {
	super(name);
}

@Override
protected void setUp() {
	super.setUp();
	cTabFolder = new CTabFolder(shell, SWT.NONE);
	cTabItem = new CTabItem(cTabFolder, SWT.NONE);
	setWidget(cTabItem);
}

@Override
public void test_setImageLorg_eclipse_swt_graphics_Image() {
}

@Override
public void test_setTextLjava_lang_String() {
}
}

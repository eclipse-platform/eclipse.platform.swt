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
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.ToolItem
 *
 * @see org.eclipse.swt.widgets.ToolItem
 */
public class Test_org_eclipse_swt_widgets_ToolItem extends Test_org_eclipse_swt_widgets_Item {

public Test_org_eclipse_swt_widgets_ToolItem(String name) {
	super(name);
}

@Override
protected void setUp() {
	super.setUp();
	toolBar = new ToolBar(shell, 0);
	toolItem = new ToolItem(toolBar, 0); 
	setWidget(toolItem);
}

public void test_ConstructorLorg_eclipse_swt_widgets_ToolBarI() {
	try {
		new ToolItem(null, SWT.NULL);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_getToolTipText() {
	toolItem.setToolTipText("fred");
	assertTrue(":a: ", toolItem.getToolTipText().equals("fred"));
	toolItem.setToolTipText("fredttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt");
	assertTrue(":a: ", toolItem.getToolTipText().equals("fredttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt"));
}

@Override
public void test_setImageLorg_eclipse_swt_graphics_Image() {
}

@Override
public void test_setTextLjava_lang_String() {
}

/* custom */
ToolBar toolBar;
ToolItem toolItem;
}

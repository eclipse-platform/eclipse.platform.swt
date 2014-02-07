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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.TabItem
 *
 * @see org.eclipse.swt.widgets.TabItem
 */
public class Test_org_eclipse_swt_widgets_TabItem extends Test_org_eclipse_swt_widgets_Item {

public Test_org_eclipse_swt_widgets_TabItem(String name) {
	super(name);
}

@Override
protected void setUp() {
	super.setUp();
	tabFolder = new TabFolder(shell, 0);
	tabItem = new TabItem(tabFolder, 0);
	setWidget(tabItem);
}

public void test_ConstructorLorg_eclipse_swt_widgets_TabFolderI() {
	try {
		new TabItem(null, SWT.NULL);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

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

public void test_getParent() {
	assertTrue(":a: ", tabItem.getParent() == tabFolder);
}

public void test_setControlLorg_eclipse_swt_widgets_Control() {
	Control control = new Table(tabFolder, SWT.NULL);

	assertTrue(":a: ", tabItem.getControl() == null);	

	tabItem.setControl(control);
	assertTrue(":b: ", tabItem.getControl() == control);

	tabItem.setControl(null);
	assertTrue(":c: ", tabItem.getControl() == null);
}

@Override
public void test_setImageLorg_eclipse_swt_graphics_Image() {
}

@Override
public void test_setTextLjava_lang_String() {
}

public void test_setToolTipTextLjava_lang_String() {
	tabItem.setToolTipText("fred");
	assertTrue(":a: ", tabItem.getToolTipText().equals("fred"));
	tabItem.setToolTipText("fredttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt");
	assertTrue(":b: ", tabItem.getToolTipText().equals("fredttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt"));
	tabItem.setToolTipText(null);
	assertTrue(":c: ", tabItem.getToolTipText() == null);
}

/* custom */
TabFolder tabFolder;
TabItem tabItem;

}

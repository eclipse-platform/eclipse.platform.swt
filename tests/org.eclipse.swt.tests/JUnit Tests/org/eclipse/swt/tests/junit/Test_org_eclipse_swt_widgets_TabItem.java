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


import junit.framework.*;
import junit.textui.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.TabItem
 *
 * @see org.eclipse.swt.widgets.TabItem
 */
public class Test_org_eclipse_swt_widgets_TabItem extends Test_org_eclipse_swt_widgets_Item {

public Test_org_eclipse_swt_widgets_TabItem(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

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

public void test_getControl() {
	warnUnimpl("Test test_getControl not written");
}

public void test_getParent() {
	assertTrue(":a: ", tabItem.getParent() == tabFolder);
}

public void test_getToolTipText() {
	warnUnimpl("Test test_getToolTipText not written");
}

public void test_setControlLorg_eclipse_swt_widgets_Control() {
	Control control = new Table(tabFolder, SWT.NULL);

	assertTrue(":a: ", tabItem.getControl() == null);	

	tabItem.setControl(control);
	assertTrue(":b: ", tabItem.getControl() == control);

	tabItem.setControl(null);
	assertTrue(":c: ", tabItem.getControl() == null);
}

public void test_setImageLorg_eclipse_swt_graphics_Image() {
	warnUnimpl("Test test_setImageLorg_eclipse_swt_graphics_Image not written");
}

public void test_setTextLjava_lang_String() {
	warnUnimpl("Test test_setTextLjava_lang_String not written");
}

public void test_setToolTipTextLjava_lang_String() {
	tabItem.setToolTipText("fred");
	assertTrue(":a: ", tabItem.getToolTipText().equals("fred"));
	tabItem.setToolTipText("fredttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt");
	assertTrue(":b: ", tabItem.getToolTipText().equals("fredttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt"));
	tabItem.setToolTipText(null);
	assertTrue(":c: ", tabItem.getToolTipText() == null);
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_TabItem((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_TabFolderI");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_TabFolderII");
	methodNames.addElement("test_getControl");
	methodNames.addElement("test_getParent");
	methodNames.addElement("test_getToolTipText");
	methodNames.addElement("test_setControlLorg_eclipse_swt_widgets_Control");
	methodNames.addElement("test_setImageLorg_eclipse_swt_graphics_Image");
	methodNames.addElement("test_setTextLjava_lang_String");
	methodNames.addElement("test_setToolTipTextLjava_lang_String");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Item.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_TabFolderI")) test_ConstructorLorg_eclipse_swt_widgets_TabFolderI();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_TabFolderII")) test_ConstructorLorg_eclipse_swt_widgets_TabFolderII();
	else if (getName().equals("test_getControl")) test_getControl();
	else if (getName().equals("test_getParent")) test_getParent();
	else if (getName().equals("test_getToolTipText")) test_getToolTipText();
	else if (getName().equals("test_setControlLorg_eclipse_swt_widgets_Control")) test_setControlLorg_eclipse_swt_widgets_Control();
	else if (getName().equals("test_setImageLorg_eclipse_swt_graphics_Image")) test_setImageLorg_eclipse_swt_graphics_Image();
	else if (getName().equals("test_setTextLjava_lang_String")) test_setTextLjava_lang_String();
	else if (getName().equals("test_setToolTipTextLjava_lang_String")) test_setToolTipTextLjava_lang_String();
	else super.runTest();
}

/* custom */
TabFolder tabFolder;
TabItem tabItem;

}

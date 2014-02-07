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
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.ColorDialog
 *
 * @see org.eclipse.swt.widgets.ColorDialog
 */
public class Test_org_eclipse_swt_widgets_ColorDialog extends Test_org_eclipse_swt_widgets_Dialog {

ColorDialog colorDialog;

public Test_org_eclipse_swt_widgets_ColorDialog(String name) {
	super(name);
}

@Override
protected void setUp() {
	super.setUp();
	colorDialog = new ColorDialog(shell, SWT.NULL);
	setDialog(colorDialog);
}

public void test_ConstructorLorg_eclipse_swt_widgets_Shell() {
	new ColorDialog(shell);
	
	try {
		new ColorDialog(null);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_ConstructorLorg_eclipse_swt_widgets_ShellI() {
	new ColorDialog(shell, SWT.NULL);
	
	try {
		new ColorDialog(null, SWT.NULL);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_setRGBLorg_eclipse_swt_graphics_RGB() {
	RGB rgb = new RGB(0, 0, 0);

	assertTrue(":a:", colorDialog.getRGB() == null);	
		
	colorDialog.setRGB(rgb);
	assertTrue(":b:", colorDialog.getRGB() == rgb);

	colorDialog.setRGB(null);
	assertTrue(":c:", colorDialog.getRGB() == null);	
}
}

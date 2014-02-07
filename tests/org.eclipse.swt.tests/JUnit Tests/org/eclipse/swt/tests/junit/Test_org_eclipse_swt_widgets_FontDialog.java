/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.Assert.assertArrayEquals;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.FontDialog;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.FontDialog
 *
 * @see org.eclipse.swt.widgets.FontDialog
 */
public class Test_org_eclipse_swt_widgets_FontDialog extends Test_org_eclipse_swt_widgets_Dialog {

public Test_org_eclipse_swt_widgets_FontDialog(String name) {
	super(name);
}

@Override
protected void setUp() {
	super.setUp();
	fontDialog = new FontDialog(shell, SWT.NULL);
	setDialog(fontDialog);
}

public void test_ConstructorLorg_eclipse_swt_widgets_Shell() {
	new FontDialog(shell);
	try {
		new FontDialog(null);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_ConstructorLorg_eclipse_swt_widgets_ShellI() {
	try {
		new FontDialog(null, SWT.NULL);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_setFontDataLorg_eclipse_swt_graphics_FontData() {
	FontData [] fontData = new FontData [1];
	fontData [0] = new FontData ();

	assertNull(fontDialog.getFontList());	
		
	fontDialog.setFontList(fontData);
	assertArrayEquals(fontDialog.getFontList(), fontData);

	fontDialog.setFontList(null);
	assertNull(fontDialog.getFontList());	
}

public void test_setRGBLorg_eclipse_swt_graphics_RGB() {
	RGB rgb = new RGB(255, 0, 0);
	fontDialog.setRGB(rgb);
	RGB rgb2 = fontDialog.getRGB();
	assertEquals(rgb, rgb2);	
}

/* custom */
FontDialog fontDialog;
}

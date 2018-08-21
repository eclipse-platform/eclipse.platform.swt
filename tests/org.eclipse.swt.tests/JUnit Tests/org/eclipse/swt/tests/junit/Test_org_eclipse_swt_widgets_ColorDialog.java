/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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


import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.ColorDialog
 *
 * @see org.eclipse.swt.widgets.ColorDialog
 */
public class Test_org_eclipse_swt_widgets_ColorDialog extends Test_org_eclipse_swt_widgets_Dialog {

ColorDialog colorDialog;

@Override
@Before
public void setUp() {
	super.setUp();
	colorDialog = new ColorDialog(shell, SWT.NULL);
	setDialog(colorDialog);
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_Shell() {
	new ColorDialog(shell);

	try {
		new ColorDialog(null);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_ShellI() {
	new ColorDialog(shell, SWT.NULL);

	try {
		new ColorDialog(null, SWT.NULL);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

@Test
public void test_setRGBLorg_eclipse_swt_graphics_RGB() {
	RGB rgb = new RGB(0, 0, 0);

	assertNull(":a:", colorDialog.getRGB());

	colorDialog.setRGB(rgb);
	assertTrue(":b:", colorDialog.getRGB() == rgb);

	colorDialog.setRGB(null);
	assertNull(":c:", colorDialog.getRGB());
}
}

/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Red Hat Inc. - Bug 462631
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.printing.PrinterData;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.printing.PrinterData
 *
 * @see org.eclipse.swt.printing.PrinterData
 */
public class Test_org_eclipse_swt_printing_PrinterData {

@Test
public void test_Constructor() {
	new PrinterData();
}

@Test
public void test_ConstructorLjava_lang_StringLjava_lang_String() {
	new PrinterData("hello", "there");
}

@Test
public void test_toString() {
	PrinterData data = new PrinterData();
	assertNotNull(data.toString());
	assertTrue(data.toString().length() > 0);
}
}

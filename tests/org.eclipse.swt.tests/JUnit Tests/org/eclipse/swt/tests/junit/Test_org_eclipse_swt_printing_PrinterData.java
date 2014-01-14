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

import junit.framework.TestCase;

import org.eclipse.swt.printing.PrinterData;

/**
 * Automated Test Suite for class org.eclipse.swt.printing.PrinterData
 *
 * @see org.eclipse.swt.printing.PrinterData
 */
public class Test_org_eclipse_swt_printing_PrinterData extends TestCase {

public void test_Constructor() {
	new PrinterData();
}

public void test_ConstructorLjava_lang_StringLjava_lang_String() {
	new PrinterData("hello", "there");
}

public void test_toString() {
	PrinterData data = new PrinterData();
	assertNotNull(data.toString());
	assertTrue(data.toString().length() > 0);
}
}

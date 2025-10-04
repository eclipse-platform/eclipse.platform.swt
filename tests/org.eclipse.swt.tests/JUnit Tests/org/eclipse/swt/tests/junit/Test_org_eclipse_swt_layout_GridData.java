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
 *     Red Hat Inc. - Bug 462631
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.junit.jupiter.api.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.layout.GridData
 *
 * @see org.eclipse.swt.layout.GridData
 */
public class Test_org_eclipse_swt_layout_GridData {

@Test
public void test_Constructor() {
	GridData data = new GridData();
	assertNotNull(data);
	assertEquals(GridData.CENTER, data.verticalAlignment);
	assertEquals(GridData.BEGINNING, data.horizontalAlignment);
	assertEquals(SWT.DEFAULT, data.widthHint);
	assertEquals(SWT.DEFAULT, data.heightHint);
	assertEquals(0, data.horizontalIndent);
	assertEquals(1, data.horizontalSpan);
	assertEquals(1, data.verticalSpan);
	assertFalse(data.grabExcessHorizontalSpace);
	assertFalse(data.grabExcessVerticalSpace);
}

@Test
public void test_ConstructorI() {
	GridData data = new GridData(GridData.FILL_BOTH);
	assertNotNull(data);
	assertEquals(GridData.FILL, data.verticalAlignment);
	assertEquals(GridData.FILL, data.horizontalAlignment);
	assertTrue(data.grabExcessHorizontalSpace);
	assertTrue(data.grabExcessVerticalSpace);
}

@Test
public void test_ConstructorII() {
	GridData data = new GridData(100, 100);
	assertNotNull(data);
	assertEquals(100, data.widthHint);
	assertEquals(100, data.heightHint);
}
}

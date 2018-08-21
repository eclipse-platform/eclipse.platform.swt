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


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.junit.Test;

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
	assertTrue(data.verticalAlignment == GridData.CENTER);
	assertTrue(data.horizontalAlignment == GridData.BEGINNING);
	assertTrue(data.widthHint == SWT.DEFAULT);
	assertTrue(data.heightHint == SWT.DEFAULT);
	assertTrue(data.horizontalIndent == 0);
	assertTrue(data.horizontalSpan == 1);
	assertTrue(data.verticalSpan == 1);
	assertFalse(data.grabExcessHorizontalSpace);
	assertFalse(data.grabExcessVerticalSpace);
}

@Test
public void test_ConstructorI() {
	GridData data = new GridData(GridData.FILL_BOTH);
	assertNotNull(data);
	assertTrue(data.verticalAlignment == GridData.FILL);
	assertTrue(data.horizontalAlignment == GridData.FILL);
	assertTrue(data.grabExcessHorizontalSpace);
	assertTrue(data.grabExcessVerticalSpace);
}

@Test
public void test_ConstructorII() {
	GridData data = new GridData(100, 100);
	assertNotNull(data);
	assertTrue(data.widthHint == 100);
	assertTrue(data.heightHint == 100);
}
}

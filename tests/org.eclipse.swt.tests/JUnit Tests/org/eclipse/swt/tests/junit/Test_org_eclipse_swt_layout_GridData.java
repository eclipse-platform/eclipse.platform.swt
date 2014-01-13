/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

/**
 * Automated Test Suite for class org.eclipse.swt.layout.GridData
 *
 * @see org.eclipse.swt.layout.GridData
 */
public class Test_org_eclipse_swt_layout_GridData extends TestCase {

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
	assertTrue(data.grabExcessHorizontalSpace == false);
	assertTrue(data.grabExcessVerticalSpace == false);
}

public void test_ConstructorI() {
	GridData data = new GridData(GridData.FILL_BOTH);
	assertNotNull(data);
	assertTrue(data.verticalAlignment == GridData.FILL);
	assertTrue(data.horizontalAlignment == GridData.FILL);
	assertTrue(data.grabExcessHorizontalSpace == true);
	assertTrue(data.grabExcessVerticalSpace == true);
}

public void test_ConstructorII() {
	GridData data = new GridData(100, 100);
	assertNotNull(data);
	assertTrue(data.widthHint == 100);
	assertTrue(data.heightHint == 100);
}
}

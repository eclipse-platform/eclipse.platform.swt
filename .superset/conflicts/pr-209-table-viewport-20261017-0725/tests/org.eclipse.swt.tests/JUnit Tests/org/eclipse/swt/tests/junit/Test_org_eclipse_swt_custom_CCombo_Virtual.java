/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.junit.jupiter.api.BeforeEach;

/**
 * Runs every test of {@link Test_org_eclipse_swt_custom_CCombo} against a CCombo
 * created with SWT.VIRTUAL, whose drop-down List is the owner-data viewport;
 * the expected results are the plain CCombo's.
 */
public class Test_org_eclipse_swt_custom_CCombo_Virtual extends Test_org_eclipse_swt_custom_CCombo {

@Override
@BeforeEach
public void setUp() {
	super.setUp();
	ccombo.dispose();
	ccombo = new CCombo(shell, SWT.VIRTUAL);
	setWidget(ccombo);
}
}

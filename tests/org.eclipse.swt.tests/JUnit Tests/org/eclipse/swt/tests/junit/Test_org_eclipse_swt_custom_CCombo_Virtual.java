/*******************************************************************************
 * Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0, or (at your option) the Eclipse
 * Public License 2.0, which are available at
 * https://www.apache.org/licenses/LICENSE-2.0 and
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - initial API and implementation
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

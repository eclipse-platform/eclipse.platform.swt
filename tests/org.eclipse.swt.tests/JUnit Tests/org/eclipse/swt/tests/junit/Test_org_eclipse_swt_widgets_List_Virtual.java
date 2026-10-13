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
import org.eclipse.swt.widgets.List;
import org.junit.jupiter.api.BeforeEach;

/**
 * Runs every test of {@link Test_org_eclipse_swt_widgets_List} against a list
 * created with SWT.VIRTUAL (owner-data viewport); the expected results are the
 * plain list's.
 */
public class Test_org_eclipse_swt_widgets_List_Virtual extends Test_org_eclipse_swt_widgets_List {

@Override
@BeforeEach
public void setUp() {
	super.setUp();
	list.dispose();
	list = new List(shell, SWT.MULTI | SWT.VIRTUAL);
	setWidget(list);
}

@Override
protected List setSingleList() {
	list.dispose();
	list = new List(shell, SWT.SINGLE | SWT.VIRTUAL);
	setWidget(list);
	return list;
}
}

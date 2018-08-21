/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.layout.FormAttachment
 *
 * @see org.eclipse.swt.layout.FormAttachment
 */
public class Test_org_eclipse_swt_layout_FormAttachment {

@Before
public void setUp() {
	shell = new Shell();
}

@After
public void tearDown() {
	shell.dispose();
}

@Test
public void test_ConstructorI() {
	FormAttachment attachment = new FormAttachment(50);
	assertNotNull(attachment);
}

@Test
public void test_ConstructorII() {
	FormAttachment attachment = new FormAttachment(50, 10);
	assertNotNull(attachment);
}

@Test
public void test_ConstructorIII() {
	FormAttachment attachment = new FormAttachment(50, 100, 10);
	assertNotNull(attachment);
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_Control() {
	FormAttachment attachment = new FormAttachment(shell);
	assertNotNull(attachment);
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_ControlI() {
	FormAttachment attachment = new FormAttachment(shell, 10);
	assertNotNull(attachment);
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_ControlII() {
	FormAttachment attachment = new FormAttachment(shell, 10, SWT.LEFT);
	assertNotNull(attachment);
}

@Test
public void test_toString() {
	FormAttachment attachment = new FormAttachment(50);
	assertNotNull(attachment.toString());
	assertTrue(attachment.toString().length() > 0);
}

/* custom */
public Shell shell;
}

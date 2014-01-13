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
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Shell;

/**
 * Automated Test Suite for class org.eclipse.swt.layout.FormAttachment
 *
 * @see org.eclipse.swt.layout.FormAttachment
 */
public class Test_org_eclipse_swt_layout_FormAttachment extends TestCase {

@Override
protected void setUp() {
	shell = new Shell();
}

@Override
protected void tearDown() {
	shell.dispose();
}

public void test_ConstructorI() {
	FormAttachment attachment = new FormAttachment(50);
	assertNotNull(attachment);
}

public void test_ConstructorII() {
	FormAttachment attachment = new FormAttachment(50, 10);
	assertNotNull(attachment);
}

public void test_ConstructorIII() {
	FormAttachment attachment = new FormAttachment(50, 100, 10);
	assertNotNull(attachment);
}

public void test_ConstructorLorg_eclipse_swt_widgets_Control() {
	FormAttachment attachment = new FormAttachment(shell);
	assertNotNull(attachment);
}

public void test_ConstructorLorg_eclipse_swt_widgets_ControlI() {
	FormAttachment attachment = new FormAttachment(shell, 10);
	assertNotNull(attachment);
}

public void test_ConstructorLorg_eclipse_swt_widgets_ControlII() {
	FormAttachment attachment = new FormAttachment(shell, 10, SWT.LEFT);
	assertNotNull(attachment);
}

public void test_toString() {
	FormAttachment attachment = new FormAttachment(50);
	assertNotNull(attachment.toString());
	assertTrue(attachment.toString().length() > 0);
}

/* custom */
public Shell shell;
}

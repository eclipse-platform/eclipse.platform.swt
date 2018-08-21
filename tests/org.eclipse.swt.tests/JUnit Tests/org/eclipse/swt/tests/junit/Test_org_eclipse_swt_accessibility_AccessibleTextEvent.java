/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
 *     Red Hat Inc. - Adapt to JUnit 4.
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.accessibility.AccessibleTextEvent;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.accessibility.AccessibleTextEvent
 *
 * @see org.eclipse.swt.accessibility.AccessibleTextEvent
 */
public class Test_org_eclipse_swt_accessibility_AccessibleTextEvent {

@Before
public void setUp() {
	shell = new Shell();
}

@After
public void tearDown() {
	shell.dispose();
}

@Test
public void test_ConstructorLjava_lang_Object() {
	// The source object should be a widget's accessible.
	AccessibleTextEvent event = new AccessibleTextEvent(shell.getAccessible());
	assertNotNull(event);

	// Test with some other object also.
	event = new AccessibleTextEvent(Integer.valueOf(5));
	assertNotNull(event);
}

@Test
public void test_toString() {
	AccessibleTextEvent event = new AccessibleTextEvent(shell.getAccessible());
	assertNotNull(event.toString());
	assertTrue(event.toString().length() > 0);
}
/* custom */
public Shell shell;
}

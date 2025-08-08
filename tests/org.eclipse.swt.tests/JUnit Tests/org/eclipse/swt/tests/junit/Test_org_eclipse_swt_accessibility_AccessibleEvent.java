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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.accessibility.AccessibleEvent
 *
 * @see org.eclipse.swt.accessibility.AccessibleEvent
 */
public class Test_org_eclipse_swt_accessibility_AccessibleEvent {

@BeforeEach
public void setUp() {
	shell = new Shell();
}

@AfterEach
public void tearDown() {
	shell.dispose();
}

@Test
public void test_ConstructorLjava_lang_Object() {
	// The source object should be a widget's accessible.
	AccessibleEvent event = new AccessibleEvent(shell.getAccessible());
	assertNotNull(event);

	// Test with some other object also.
	event = new AccessibleEvent(Integer.valueOf(5));
	assertNotNull(event);
}

@Test
public void test_toString() {
	AccessibleEvent event = new AccessibleEvent(shell.getAccessible());
	assertNotNull(event.toString());
	assertFalse(event.toString().isEmpty());
}

/* custom */
public Shell shell;
}

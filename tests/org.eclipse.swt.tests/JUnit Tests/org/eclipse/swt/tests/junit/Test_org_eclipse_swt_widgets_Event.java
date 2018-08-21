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

import org.eclipse.swt.widgets.Event;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Event
 *
 * @see org.eclipse.swt.widgets.Event
 */
public class Test_org_eclipse_swt_widgets_Event {

@Test
public void test_Constructor() {
	Event event = new Event();
	assertNotNull(event);
}

@Test
public void test_toString() {
	Event event = new Event();
	assertNotNull(event.toString());
	assertTrue(event.toString().length() > 0);
}
}

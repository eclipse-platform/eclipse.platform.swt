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
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.events.TypedEvent
 *
 * @see org.eclipse.swt.events.TypedEvent
 */
public class Test_org_eclipse_swt_events_TypedEvent {

@Before
public void setUp() {
	shell = new Shell();
}

@After
public void tearDown() {
	shell.dispose();
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_Event() {
	Event event = new Event();
	event.widget = shell;
	TypedEvent typedEvent = newTypedEvent(event);
	assertNotNull(typedEvent);
}

@Test
public void test_toString() {
	Event event = new Event();
	event.widget = shell;
	TypedEvent typedEvent = newTypedEvent(event);
	assertNotNull(typedEvent.toString());
	assertTrue(typedEvent.toString().length() > 0);
}

/* custom */
public Shell shell;

protected TypedEvent newTypedEvent(Event event) {
	return new TypedEvent(event);
}
}

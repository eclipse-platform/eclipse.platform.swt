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

import org.eclipse.swt.widgets.Event;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Event
 *
 * @see org.eclipse.swt.widgets.Event
 */
public class Test_org_eclipse_swt_widgets_Event extends TestCase {

public void test_Constructor() {
	Event event = new Event();
	assertNotNull(event);
}

public void test_toString() {
	Event event = new Event();
	assertNotNull(event.toString());
	assertTrue(event.toString().length() > 0);
}
}

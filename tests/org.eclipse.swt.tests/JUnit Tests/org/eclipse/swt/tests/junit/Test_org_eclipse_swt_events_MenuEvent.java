/*******************************************************************************
 * Copyright (c) 2000, 2014 IBM Corporation and others.
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


import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.widgets.Event;

/**
 * Automated Test Suite for class org.eclipse.swt.events.MenuEvent
 *
 * @see org.eclipse.swt.events.MenuEvent
 */
public class Test_org_eclipse_swt_events_MenuEvent extends Test_org_eclipse_swt_events_TypedEvent {

/* custom */
@Override
protected TypedEvent newTypedEvent(Event event) {
	return new MenuEvent(event);
}
}

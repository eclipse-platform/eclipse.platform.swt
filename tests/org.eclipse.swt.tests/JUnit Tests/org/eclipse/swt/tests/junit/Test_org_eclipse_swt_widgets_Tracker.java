/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import org.eclipse.swt.widgets.Tracker;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Tracker
 *
 * @see org.eclipse.swt.widgets.Tracker
 */
public class Test_org_eclipse_swt_widgets_Tracker extends Test_org_eclipse_swt_widgets_Widget {
	
public Test_org_eclipse_swt_widgets_Tracker(String name) {
	super(name);
}

@Override
protected void setUp() {
	super.setUp();
	tracker = new Tracker(shell, 0);
	setWidget(tracker);
}

/* custom */
	Tracker tracker;
}

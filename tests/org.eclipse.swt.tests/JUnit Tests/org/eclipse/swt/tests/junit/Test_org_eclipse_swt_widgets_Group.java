/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Group;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Group
 *
 * @see org.eclipse.swt.widgets.Group
 */
public class Test_org_eclipse_swt_widgets_Group extends Test_org_eclipse_swt_widgets_Composite {

public Test_org_eclipse_swt_widgets_Group(String name) {
	super(name);
}

@Override
protected void setUp() {
	super.setUp();
	group = new Group(shell, 0);
	setWidget(group);
}

@Override
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	try {
		group = new Group(null, 0);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}

	int[] cases = {SWT.SHADOW_IN, SWT.SHADOW_OUT, SWT.SHADOW_ETCHED_IN, SWT.SHADOW_ETCHED_OUT};
	for (int i = 0; i < cases.length; i++)
		group = new Group(shell, cases[i]);
}

@Override
public void test_computeTrimIIII() {
}

@Override
public void test_getClientArea() {
}

public void test_setTextLjava_lang_String() {
	String[] cases = {"", "some text", "ldkashdoehufweovcnhslvhregojebckreavbkuhxbiufvcyhbifuyewvbiureyd.,cmnesljliewjfchvbwoifivbeworixuieurvbiuvbohflksjeahfcliureafgyciabelitvyrwtlicuyrtliureybcliuyreuceyvbliureybct"};
	for (int i = 0; i < cases.length; i++) {
		group.setText(cases[i]);
		assertTrue("case: " + String.valueOf(i), group.getText().equals(cases[i]));
	}
}

/* custom */
Group group;


public void test_consistency_MenuDetect () {
    consistencyEvent(10, 5, 3, 0, ConsistencyUtility.MOUSE_CLICK);
}

public void test_consistency_DragDetect () {
    consistencyEvent(10, 5, 20, 10, ConsistencyUtility.MOUSE_DRAG);
}

}

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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Group;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Group
 *
 * @see org.eclipse.swt.widgets.Group
 */
public class Test_org_eclipse_swt_widgets_Group extends Test_org_eclipse_swt_widgets_Composite {

@Override
@Before
public void setUp() {
	super.setUp();
	group = new Group(shell, 0);
	setWidget(group);
}

@Override
@Test
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
@Test
public void test_computeTrimIIII() {
}

@Override
@Test
public void test_getClientArea() {
}

@Test
public void test_setTextLjava_lang_String() {
	String[] cases = {"", "some text", "ldkashdoehufweovcnhslvhregojebckreavbkuhxbiufvcyhbifuyewvbiureyd.,cmnesljliewjfchvbwoifivbeworixuieurvbiuvbohflksjeahfcliureafgyciabelitvyrwtlicuyrtliureybcliuyreuceyvbliureybct"};
	for (int i = 0; i < cases.length; i++) {
		group.setText(cases[i]);
		assertTrue("case: " + String.valueOf(i), group.getText().equals(cases[i]));
	}
}

/* custom */
Group group;


@Test
public void test_consistency_MenuDetect () {
    consistencyEvent(10, 5, 3, 0, ConsistencyUtility.MOUSE_CLICK);
}

@Test
public void test_consistency_DragDetect () {
    consistencyEvent(10, 5, 20, 10, ConsistencyUtility.MOUSE_DRAG);
}

}

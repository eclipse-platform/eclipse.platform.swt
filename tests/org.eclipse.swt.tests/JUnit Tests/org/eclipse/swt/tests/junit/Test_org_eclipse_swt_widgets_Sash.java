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

import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Sash;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Sash
 *
 * @see org.eclipse.swt.widgets.Sash
 */
public class Test_org_eclipse_swt_widgets_Sash extends Test_org_eclipse_swt_widgets_Control {

@Override
@Before
public void setUp() {
	super.setUp();
	sash = new Sash(shell, 0);
	setWidget(sash);
}

@Override
@Test
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	try {
		sash = new Sash(null, 0);
		fail("No exception thrown"); //should never get here
	}
	catch (IllegalArgumentException e) {
	}

	int[] cases = {0, SWT.HORIZONTAL, SWT.VERTICAL};
	for (int i = 0; i < cases.length; i++)
		sash = new Sash(shell, cases[i]);
}

@Override
@Test
public void test_computeSizeIIZ() {
}

@Override
@Test
public void test_setFontLorg_eclipse_swt_graphics_Font() {
	// overridden from Control because it does not make sense
	// to set the font of a Sash.
}

/* custom */
Sash sash;

private void createSash() {
    tearDown();
    super.setUp();
    new Button(shell, SWT.PUSH);
    Sash sash = new Sash(shell, SWT.VERTICAL);
    new Button(shell, SWT.PUSH);
    setWidget(sash);
}

@Test
public void test_consistency_MenuDetect () {
    createSash();
    consistencyEvent(0, 5, 3, 0, ConsistencyUtility.MOUSE_CLICK);
}

@Test
public void test_consistency_DragDetect () {
    createSash();
    consistencyEvent(0, 5, 20, 10, ConsistencyUtility.MOUSE_DRAG);
}

@Test
public void test_consistency_MouseSelection() {
    createSash();
    consistencyEvent(0, 5, 1, 0, ConsistencyUtility.MOUSE_CLICK);
}

}

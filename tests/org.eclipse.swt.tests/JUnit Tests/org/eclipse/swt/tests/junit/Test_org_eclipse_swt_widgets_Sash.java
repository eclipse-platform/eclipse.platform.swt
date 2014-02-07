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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Sash;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Sash
 *
 * @see org.eclipse.swt.widgets.Sash
 */
public class Test_org_eclipse_swt_widgets_Sash extends Test_org_eclipse_swt_widgets_Control {

public Test_org_eclipse_swt_widgets_Sash(String name) {
	super(name);
}

@Override
protected void setUp() {
	super.setUp();
	sash = new Sash(shell, 0);
	setWidget(sash);
}

@Override
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
public void test_computeSizeIIZ() {
}

@Override
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

public void test_consistency_MenuDetect () {
    createSash();
    consistencyEvent(0, 5, 3, 0, ConsistencyUtility.MOUSE_CLICK);
}

public void test_consistency_DragDetect () {
    createSash();
    consistencyEvent(0, 5, 20, 10, ConsistencyUtility.MOUSE_DRAG);
}

public void test_consistency_MouseSelection() {
    createSash();
    consistencyEvent(0, 5, 1, 0, ConsistencyUtility.MOUSE_CLICK);
}

}

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


import org.eclipse.swt.custom.CLabel;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.CLabel
 *
 * @see org.eclipse.swt.custom.CLabel
 */
public class Test_org_eclipse_swt_custom_CLabel extends Test_org_eclipse_swt_widgets_Canvas {

public Test_org_eclipse_swt_custom_CLabel(String name) {
	super(name);
}

@Override
protected void setUp() {
	super.setUp();
	label = new CLabel(shell, 0);
	setWidget(label);
}

@Override
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
}

@Override
public void test_computeSizeIIZ() {
}

@Override
public void test_setBackgroundLorg_eclipse_swt_graphics_Color() {
}

@Override
public void test_setFontLorg_eclipse_swt_graphics_Font() {
}

@Override
public void test_setToolTipTextLjava_lang_String() {
}

/* custom */
CLabel label;

@Override
public void test_consistency_MenuDetect () {
    consistencyEvent(10, 5, 3, 0, ConsistencyUtility.MOUSE_CLICK);
}

@Override
public void test_consistency_DragDetect () {
    consistencyEvent(10, 5, 20, 10, ConsistencyUtility.MOUSE_DRAG);
}
}

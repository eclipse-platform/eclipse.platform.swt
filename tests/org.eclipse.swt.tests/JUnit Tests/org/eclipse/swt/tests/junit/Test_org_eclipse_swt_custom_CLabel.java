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


import org.eclipse.swt.custom.CLabel;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.CLabel
 *
 * @see org.eclipse.swt.custom.CLabel
 */
public class Test_org_eclipse_swt_custom_CLabel extends Test_org_eclipse_swt_widgets_Canvas {

@Override
@Before
public void setUp() {
	super.setUp();
	label = new CLabel(shell, 0);
	setWidget(label);
}

@Override
@Test
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
}

@Override
@Test
public void test_computeSizeIIZ() {
}

@Override
@Test
public void test_setBackgroundLorg_eclipse_swt_graphics_Color() {
}

@Override
@Test
public void test_setFontLorg_eclipse_swt_graphics_Font() {
}

@Override
@Test
public void test_setToolTipTextLjava_lang_String() {
}

/* custom */
CLabel label;

@Override
@Test
public void test_consistency_MenuDetect () {
    consistencyEvent(10, 5, 3, 0, ConsistencyUtility.MOUSE_CLICK);
}

@Override
@Test
public void test_consistency_DragDetect () {
    consistencyEvent(10, 5, 20, 10, ConsistencyUtility.MOUSE_DRAG);
}
}

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Label;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Label
 *
 * @see org.eclipse.swt.widgets.Label
 */
public class Test_org_eclipse_swt_widgets_Label extends Test_org_eclipse_swt_widgets_Control {

@Override
@Before
public void setUp() {
	super.setUp();
	label = new Label(shell, 0);
	setWidget(label);
}

@Override
@Test
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI(){
	try {
		label = new Label(null, 0);
		fail("No exception thrown"); //should never get here
	}
	catch (IllegalArgumentException e) {
	}

	label = new Label(shell, 0);

	int[] cases = {SWT.LEFT, SWT.RIGHT, SWT.CENTER, SWT.SEPARATOR, SWT.HORIZONTAL, SWT.VERTICAL, SWT.SHADOW_IN, SWT.SHADOW_OUT};
	for (int style : cases)
		label = new Label(shell, style);
}

@Override
@Test
public void test_computeSizeIIZ() {
	// super class test is sufficient
}

@Test
public void test_getAlignment(){
	int[] cases = {SWT.LEFT, SWT.RIGHT, SWT.CENTER};
	for (int style : cases) {
		label = new Label(shell, style);
		assertEquals(label.getAlignment(), style);
	}
}

@Test
public void test_getImage(){
	Image[] cases = {null, new Image(null, 100, 100)};
	for (Image image : cases) {
		label.setImage(image);
		assertEquals(label.getImage(), image);
		if (image!=null)
			image.dispose();
	}
}

@Test
public void test_getText(){
	String[] cases = {"", "some name", "sdasdlkjshcdascecoewcwe"};
	for (String text : cases) {
		label.setText(text);
		assertEquals(label.getText(), text);
	}
}

@Test
public void test_setAlignmentI(){
	int[] cases = {SWT.LEFT, SWT.RIGHT, SWT.CENTER};
	for (int style : cases) {
		label.setAlignment(style);
		assertEquals(label.getAlignment(), style);
	}
}

@Override
@Test
public void test_setFocus() {
	// super class test is sufficient
}

@Test
public void test_setTextLjava_lang_String(){
	try {
		label.setText(null);
		fail("No exception thrown for string == null");
	}
	catch (IllegalArgumentException e) {
	}
}

/* custom */
Label label;

@Test
public void test_consistency_MenuDetect () {
	consistencyEvent(10, 5, 3, 0, ConsistencyUtility.MOUSE_CLICK);
}

@Test
public void test_consistency_DragDetect () {
	consistencyEvent(10, 5, 20, 10, ConsistencyUtility.MOUSE_DRAG);
}
}

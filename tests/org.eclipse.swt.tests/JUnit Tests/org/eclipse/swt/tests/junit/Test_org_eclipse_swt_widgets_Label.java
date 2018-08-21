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
	for (int i = 0; i < cases.length; i++)
		label = new Label(shell, cases[i]);
}

@Override
@Test
public void test_computeSizeIIZ() {
	// super class test is sufficient
}

@Test
public void test_getAlignment(){
	int[] cases = {SWT.LEFT, SWT.RIGHT, SWT.CENTER};
	for (int i=0; i<cases.length; i++)
	{
 	  label = new Label(shell, cases[i]);
	  assertEquals(label.getAlignment(), cases[i]);
	}
}

@Test
public void test_getImage(){
	Image[] cases = {null, new Image(null, 100, 100)};
	for(int i=0; i<cases.length; i++){
	 label.setImage(cases[i]);
	 assertEquals(label.getImage(), cases[i]);
	 if (cases[i]!=null)
	  cases[i].dispose();
	}
}

@Test
public void test_getText(){
	String[] cases = {"", "some name", "sdasdlkjshcdascecoewcwe"};
	for(int i=0; i<cases.length; i++){
	 label.setText(cases[i]);
	 assertEquals(label.getText(), cases[i]);
	}
}

@Test
public void test_setAlignmentI(){
	int[] cases = {SWT.LEFT, SWT.RIGHT, SWT.CENTER};
	for (int i=0; i<cases.length; i++)
	{
 	  label.setAlignment(cases[i]);
	  assertEquals(label.getAlignment(), cases[i]);
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

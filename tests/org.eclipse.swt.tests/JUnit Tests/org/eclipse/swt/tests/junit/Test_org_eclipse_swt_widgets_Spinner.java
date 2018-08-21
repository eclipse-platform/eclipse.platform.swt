/*******************************************************************************
 * Copyright (c) 2000, 2016 Red Hat, Inc. and others.
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
 *     Ian Pun <ipun@redhat.com> - addition of Spinner test class
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Spinner;
import org.junit.Before;
import org.junit.Test;

public class Test_org_eclipse_swt_widgets_Spinner extends Test_org_eclipse_swt_widgets_Composite {

	Spinner spinner;

	@Override
	@Before
	public void setUp() {
		super.setUp();
		spinner = new Spinner(shell, 0);
		setWidget(spinner);
	}

	@Override
	@Test
	public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
		try {
			spinner = new Spinner(null, 0);
			fail("No exception thrown for parent == null");
		}
		catch (IllegalArgumentException e) {
		}
		int[] cases = {0, SWT.READ_ONLY, SWT.WRAP};
		for (int i = 0; i < cases.length; i++)
			spinner = new Spinner(shell, cases[i]);
	}

	@Override
	@Test
	public void test_computeSizeIIZ() {
		// super class test is sufficient
	}

	@Test
	public void test_getIncrement() {
		int [] cases = {5,100,1000,1};
		for (int i=0;i<cases.length;i++){
			spinner.setIncrement(cases[i]);
			assertEquals(spinner.getIncrement(), cases[i]);
		}
		spinner.setIncrement(-1);
		assertEquals(spinner.getIncrement(), cases[cases.length-1]);
	}

	@Test
	public void test_getDigits() {
		int [] cases = {1,10,0};
		for (int i=0;i<cases.length;i++){
			spinner.setDigits(cases[i]);
			assertEquals(spinner.getDigits(), cases[i]);
		}
		try{
			spinner.setDigits(-1);
			fail("setDigits should have failed with illegal Argument");
		}
		catch(IllegalArgumentException e){
			assertEquals(spinner.getDigits(), cases[cases.length-1]);
		}
	}

	@Test
	public void test_getMaximum() {
		spinner.setMaximum(1000);
		assertEquals(spinner.getMaximum(), 1000);
		spinner.setMinimum(100);
		spinner.setMaximum(99);
		assertEquals(spinner.getMaximum(), 1000);
	}

	@Test
	public void test_getMinimum() {
		spinner.setMinimum(2);
		assertEquals(spinner.getMinimum(), 2);
		spinner.setMaximum(99);
		spinner.setMinimum(100);
		assertEquals(spinner.getMinimum(), 2);
	}

	@Test
	public void test_getPageIncrement() {
		int [] cases = {5,1000,20,1};
		for (int i=0;i<cases.length;i++){
			spinner.setPageIncrement(cases[i]);
			assertEquals(spinner.getPageIncrement(), cases[i]);
		}
		spinner.setPageIncrement(-1);
		assertEquals(spinner.getPageIncrement(), cases[cases.length-1]);
	}

	@Test
	public void test_getSelection() {
		int [] cases = {5,1000,25,1};
		for (int i=0;i<cases.length;i++){
			if(cases[i]>=spinner.getMaximum())
				cases[i] = spinner.getMaximum()-1;
			spinner.setSelection(cases[i]);
			assertEquals(spinner.getSelection(), cases[i]);
		}
		spinner.setSelection(spinner.getMaximum()+1);
		assertEquals(spinner.getSelection(), spinner.getMaximum());
		spinner.setSelection(spinner.getMinimum()-1);
		assertEquals(spinner.getSelection(), spinner.getMinimum());
	}

	@Test
	public void test_getTextLimit() {
		int [] cases = {5,1000,1};
		for (int i=0;i<cases.length;i++){
			spinner.setTextLimit(cases[i]);
			assertEquals(spinner.getTextLimit(), cases[i]);
		}
		try {
			spinner.setTextLimit(0);
			fail("setTextLimit should have caused an expection with value 0");
		} catch (Exception e) {
		}
	}

	@Test
	public void test_setValues() {
		int cases = 4;
		int [] selection = {5,10,15,20};
		int [] minimum = {1,5,15,0};
		int [] maximum = {20,30,15,50};
		int [] digits = {0,1,2,3};
		int [] increment = {10,5,6,100};
		int [] pageIncrement = {50,5,6,100};
		for (int i=0;i<cases;i++){
			spinner.setValues(selection[i], minimum[i], maximum[i], digits[i], increment[i], pageIncrement[i]);
			assertEquals(spinner.getSelection(), selection[i]);
			assertEquals(spinner.getMinimum(), minimum[i]);
			assertEquals(spinner.getMaximum(), maximum[i]);
			assertEquals(spinner.getDigits(), digits[i]);
			assertEquals(spinner.getIncrement(), increment[i]);
			assertEquals(spinner.getPageIncrement(), pageIncrement[i]);
		}
		spinner.setValues(5, 10, 3, -1, 0, -1);
		assertEquals(spinner.getSelection(), selection[cases-1]);
		assertEquals(spinner.getMinimum(), minimum[cases-1]);
		assertEquals(spinner.getMaximum(), maximum[cases-1]);
		assertEquals(spinner.getDigits(), digits[cases-1]);
		assertEquals(spinner.getIncrement(), increment[cases-1]);
		assertEquals(spinner.getPageIncrement(), pageIncrement[cases-1]);
		spinner = new Spinner(shell,0);
	}

	@Test
	public void test_getText(){
		spinner.setSelection(5);
		assertEquals(spinner.getText(), "5");
		spinner.setSelection(-5);
		assertEquals(spinner.getText(), "0");
		spinner.setSelection(spinner.getMaximum()+1);
		assertEquals(spinner.getText(), String.valueOf(spinner.getMaximum()));
	}
}
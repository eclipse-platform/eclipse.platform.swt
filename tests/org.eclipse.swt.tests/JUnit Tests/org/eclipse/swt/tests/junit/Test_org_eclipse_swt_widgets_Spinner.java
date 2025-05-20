/*******************************************************************************
 * Copyright (c) 2000, 2025 Red Hat, Inc. and others.
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
import static org.junit.Assert.assertThrows;

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
		assertThrows("No exception thrown for parent == null", IllegalArgumentException.class,
				() -> new Spinner(null, 0));
		int[] cases = { 0, SWT.READ_ONLY, SWT.WRAP };
		for (int style : cases)
			spinner = new Spinner(shell, style);
	}

	@Test
	public void test_getIncrement() {
		int [] cases = {5,100,1000,1};
		for (int value : cases) {
			spinner.setIncrement(value);
			assertEquals(value, spinner.getIncrement());
		}
		spinner.setIncrement(-1);
		assertEquals(cases[cases.length-1], spinner.getIncrement());
	}

	@Test
	public void test_getDigits() {
		int [] cases = {1,10,0};
		for (int digits : cases) {
			spinner.setDigits(digits);
			assertEquals(digits, spinner.getDigits());
		}
		assertThrows("setDigits should have failed with illegal Argument", IllegalArgumentException.class,
				() -> spinner.setDigits(-1));
		assertEquals(cases[cases.length-1], spinner.getDigits());
	}

	@Test
	public void test_getMaximum() {
		spinner.setMaximum(1000);
		assertEquals(1000, spinner.getMaximum());
		spinner.setMinimum(100);
		spinner.setMaximum(99);
		assertEquals(1000, spinner.getMaximum());
	}

	@Test
	public void test_getMinimum() {
		spinner.setMinimum(2);
		assertEquals(2, spinner.getMinimum());
		spinner.setMaximum(99);
		spinner.setMinimum(100);
		assertEquals(2, spinner.getMinimum());
	}

	@Test
	public void test_getPageIncrement() {
		int [] cases = {5,1000,20,1};
		for (int value : cases) {
			spinner.setPageIncrement(value);
			assertEquals(value, spinner.getPageIncrement());
		}
		spinner.setPageIncrement(-1);
		assertEquals(cases[cases.length-1], spinner.getPageIncrement());
	}

	@Test
	public void test_getSelection() {
		int [] cases = {5,1000,25,1};
		for (int i=0;i<cases.length;i++){
			if(cases[i]>=spinner.getMaximum())
				cases[i] = spinner.getMaximum()-1;
			spinner.setSelection(cases[i]);
			assertEquals(cases[i], spinner.getSelection());
		}
		spinner.setSelection(spinner.getMaximum()+1);
		assertEquals(spinner.getMaximum(), spinner.getSelection());
		spinner.setSelection(spinner.getMinimum()-1);
		assertEquals(spinner.getMinimum(), spinner.getSelection());
	}

	@Test
	public void test_getTextLimit() {
		int [] cases = {5,1000,1};
		for (int value : cases) {
			spinner.setTextLimit(value);
			assertEquals(value, spinner.getTextLimit());
		}
		assertThrows("setTextLimit should have caused an expection with value 0", IllegalArgumentException.class,
				() -> spinner.setTextLimit(0));
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
			assertEquals("i=" + i, selection[i], spinner.getSelection());
			assertEquals("i=" + i, minimum[i], spinner.getMinimum());
			assertEquals("i=" + i, maximum[i], spinner.getMaximum());
			assertEquals("i=" + i, digits[i], spinner.getDigits());
			assertEquals("i=" + i, increment[i], spinner.getIncrement());
			assertEquals("i=" + i, pageIncrement[i], spinner.getPageIncrement());
		}

		// set invalid values. The last values should be preserved
		spinner.setValues(5, 10, 3, -1, 0, -1);
		assertEquals(selection[cases - 1], spinner.getSelection());
		assertEquals(minimum[cases - 1], spinner.getMinimum());
		assertEquals(maximum[cases - 1], spinner.getMaximum());
		assertEquals(digits[cases - 1], spinner.getDigits());
		assertEquals(increment[cases - 1], spinner.getIncrement());
		assertEquals(pageIncrement[cases - 1], spinner.getPageIncrement());
		spinner = new Spinner(shell, 0);
	}

	@Test
	public void test_getText(){
		spinner.setSelection(5);
		assertEquals("5", spinner.getText());
		spinner.setSelection(-5);
		assertEquals("0",spinner.getText());
		spinner.setSelection(spinner.getMaximum()+1);
		assertEquals(String.valueOf(spinner.getMaximum()), spinner.getText());
	}
}
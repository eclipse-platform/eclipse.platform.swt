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
 *     Ian Pun <ipun@redhat.com> - addition of ScrolledComposite test class
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.junit.Before;
import org.junit.Test;

public class Test_org_eclipse_swt_widgets_ScrolledComposite extends Test_org_eclipse_swt_widgets_Composite {

	ScrolledComposite scrolledComposite;

	@Override
	@Before
	public void setUp() {
		super.setUp();
		scrolledComposite = new ScrolledComposite(shell, 0);
		setWidget(scrolledComposite);
	}

	@Override
	@Test
	public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
		try {
			scrolledComposite = new ScrolledComposite(null, 0);
			fail("No exception thrown");
		}
		catch (IllegalArgumentException e) {
		}

		int[] cases = {SWT.H_SCROLL, SWT.V_SCROLL, SWT.BORDER, SWT.LEFT_TO_RIGHT, SWT.RIGHT_TO_LEFT};
		for (int i = 0; i < cases.length; i++){
			scrolledComposite = new ScrolledComposite(shell, cases[i]);
		}
	}

	@Override
	@Test
	public void test_computeSizeIIZ() {
		// super class test is sufficient
	}

	@Test
	public void test_getAlwaysShowScrollBars() {
		boolean defaultFlag = scrolledComposite.getAlwaysShowScrollBars();
		boolean setFlag = !defaultFlag;
		scrolledComposite.setAlwaysShowScrollBars(setFlag);
		if (setFlag){
			assertTrue(scrolledComposite.getAlwaysShowScrollBars());
		}
		else{
			assertFalse(scrolledComposite.getAlwaysShowScrollBars());
		}
		setFlag = !setFlag;
		scrolledComposite.setAlwaysShowScrollBars(setFlag);
		if (setFlag){
			assertTrue(scrolledComposite.getAlwaysShowScrollBars());
		}
		else{
			assertFalse(scrolledComposite.getAlwaysShowScrollBars());
		}
		scrolledComposite.setAlwaysShowScrollBars(defaultFlag);
	}

	@Test
	public void test_getShowFocusedControl() {
		boolean defaultFlag = scrolledComposite.getShowFocusedControl();
		boolean setFlag = !defaultFlag;
		scrolledComposite.setShowFocusedControl(setFlag);
		if (setFlag){
			assertTrue(scrolledComposite.getShowFocusedControl());
		}
		else{
			assertFalse(scrolledComposite.getShowFocusedControl());
		}
		setFlag = !setFlag;
		scrolledComposite.setShowFocusedControl(setFlag);
		if (setFlag){
			assertTrue(scrolledComposite.getShowFocusedControl());
		}
		else{
			assertFalse(scrolledComposite.getShowFocusedControl());
		}
		scrolledComposite.setShowFocusedControl(defaultFlag);
	}

	@Test
	public void test_getExpandHorizontal() {
		boolean defaultFlag = scrolledComposite.getExpandHorizontal();
		boolean setFlag = !defaultFlag;
		scrolledComposite.setExpandHorizontal(setFlag);
		if (setFlag){
			assertTrue(scrolledComposite.getExpandHorizontal());
		}
		else{
			assertFalse(scrolledComposite.getExpandHorizontal());
		}
		setFlag = !setFlag;
		scrolledComposite.setExpandHorizontal(setFlag);
		if (setFlag){
			assertTrue(scrolledComposite.getExpandHorizontal());
		}
		else{
			assertFalse(scrolledComposite.getExpandHorizontal());
		}
		scrolledComposite.setExpandHorizontal(defaultFlag);
	}

	@Test
	public void test_getExpandVertical() {
		boolean defaultFlag = scrolledComposite.getExpandVertical();
		boolean setFlag = !defaultFlag;
		scrolledComposite.setExpandVertical(setFlag);
		if (setFlag){
			assertTrue(scrolledComposite.getExpandVertical());
		}
		else{
			assertFalse(scrolledComposite.getExpandVertical());
		}
		setFlag = !setFlag;
		scrolledComposite.setExpandVertical(setFlag);
		if (setFlag){
			assertTrue(scrolledComposite.getExpandVertical());
		}
		else{
			assertFalse(scrolledComposite.getExpandVertical());
		}
		scrolledComposite.setExpandVertical(defaultFlag);
	}

	@Test
	public void test_getMinWidth() {
		int defaultWidth = 0;
		assertEquals(defaultWidth,scrolledComposite.getMinWidth());
		scrolledComposite.setMinWidth(10);
		assertEquals(10, scrolledComposite.getMinWidth());
		scrolledComposite.setMinWidth(-1);
		assertEquals(defaultWidth,scrolledComposite.getMinWidth());
		scrolledComposite.setMinWidth(defaultWidth);
		assertEquals(defaultWidth,scrolledComposite.getMinWidth());
	}

	@Test
	public void test_getMinHeight() {
		int defaultHeight = 0;
		assertEquals(defaultHeight,scrolledComposite.getMinHeight());
		scrolledComposite.setMinHeight(10);
		assertEquals(10, scrolledComposite.getMinHeight());
		scrolledComposite.setMinHeight(-1);
		assertEquals(defaultHeight, scrolledComposite.getMinHeight());
		scrolledComposite.setMinHeight(defaultHeight);
		assertEquals(defaultHeight,scrolledComposite.getMinHeight());
	}

	@Test
	public void test_setMinSize() {
		Point point = new Point(5,0);
		scrolledComposite.setMinSize(point);
		assertEquals(5,scrolledComposite.getMinWidth());
		assertEquals(0,scrolledComposite.getMinHeight());
		point.x = 0;
		point.y = 5;
		scrolledComposite.setMinSize(point);
		assertEquals(0,scrolledComposite.getMinWidth());
		assertEquals(5,scrolledComposite.getMinHeight());
		point.x = 1;
		point.y = -5;
		scrolledComposite.setMinSize(point);
		assertEquals(1,scrolledComposite.getMinWidth());
		assertEquals(0,scrolledComposite.getMinHeight());
		point.x = -2;
		point.y = 3;
		scrolledComposite.setMinSize(point);
		assertEquals(0,scrolledComposite.getMinWidth());
		assertEquals(3,scrolledComposite.getMinHeight());

		scrolledComposite.setMinSize(0, 10);
		assertEquals(0,scrolledComposite.getMinWidth());
		assertEquals(10,scrolledComposite.getMinHeight());
		scrolledComposite.setMinSize(10, 0);
		assertEquals(10,scrolledComposite.getMinWidth());
		assertEquals(0,scrolledComposite.getMinHeight());
		scrolledComposite.setMinSize(-5, 5);
		assertEquals(0,scrolledComposite.getMinWidth());
		assertEquals(5,scrolledComposite.getMinHeight());
		scrolledComposite.setMinSize(5, -5);
		assertEquals(5,scrolledComposite.getMinWidth());
		assertEquals(0,scrolledComposite.getMinHeight());
	}

	@Test
	public void test_getOrigin() {
		scrolledComposite = new ScrolledComposite(shell, SWT.H_SCROLL|SWT.V_SCROLL);
		Composite parent = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(parent);
		Point point = new Point(5,0);

		scrolledComposite.setOrigin(point);
		assertEquals(5,scrolledComposite.getOrigin().x);
		assertEquals(0,scrolledComposite.getOrigin().y);
		point.x = 0;
		point.y = 5;
		scrolledComposite.setOrigin(point);
		assertEquals(0,scrolledComposite.getOrigin().x);
		assertEquals(5,scrolledComposite.getOrigin().y);
		point.x = 1;
		point.y = -5;
		scrolledComposite.setOrigin(point);
		assertEquals(1,scrolledComposite.getOrigin().x);
		assertEquals(0,scrolledComposite.getOrigin().y);
		point.x = -2;
		point.y = 3;
		scrolledComposite.setOrigin(point);
		assertEquals(0,scrolledComposite.getOrigin().x);
		assertEquals(3,scrolledComposite.getOrigin().y);

		scrolledComposite.setOrigin(0, 10);
		assertEquals(0,scrolledComposite.getOrigin().x);
		assertEquals(10,scrolledComposite.getOrigin().y);
		scrolledComposite.setOrigin(10, 0);
		assertEquals(10,scrolledComposite.getOrigin().x);
		assertEquals(0,scrolledComposite.getOrigin().y);
		scrolledComposite.setOrigin(-5, 5);
		assertEquals(0,scrolledComposite.getOrigin().x);
		assertEquals(5,scrolledComposite.getOrigin().y);
		scrolledComposite.setOrigin(5, -5);
		assertEquals(5,scrolledComposite.getOrigin().x);
		assertEquals(0,scrolledComposite.getOrigin().y);
	}

	@Test
	public void test_setContent() {
		scrolledComposite = new ScrolledComposite(shell, SWT.H_SCROLL|SWT.V_SCROLL);
		Point point = new Point(5,5);
		Composite parent = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(null);
		scrolledComposite.setOrigin(point);
		assertEquals(0,scrolledComposite.getOrigin().x);
		assertEquals(0,scrolledComposite.getOrigin().y);
		scrolledComposite.setContent(parent);
		scrolledComposite.setOrigin(point);
		assertEquals(5,scrolledComposite.getOrigin().x);
		assertEquals(5,scrolledComposite.getOrigin().y);
	}
	@Test
	public void test_setLayout() {
		// setLayout() has been overwritten for scrollableComposite to not set a layout
	}
}

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
 *     Ian Pun <ipun@redhat.com> - addition of ScrolledComposite test class
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Test_org_eclipse_swt_widgets_ScrolledComposite extends Test_org_eclipse_swt_widgets_Composite {

	ScrolledComposite scrolledComposite;

	@Override
	@BeforeEach
	public void setUp() {
		super.setUp();
		scrolledComposite = new ScrolledComposite(shell, 0);
		setWidget(scrolledComposite);
	}

	@Override
	@Test
	public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
		assertThrows(IllegalArgumentException.class, () -> new ScrolledComposite(null, 0), "No exception thrown");

		int[] cases = {SWT.H_SCROLL, SWT.V_SCROLL, SWT.BORDER, SWT.LEFT_TO_RIGHT, SWT.RIGHT_TO_LEFT};
		for (int style : cases) {
			scrolledComposite = new ScrolledComposite(shell, style);
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
	@Test
	public void test_logicalContentSizeKeepsPhysicalContentBounded() {
		/* scroll bar visibility is the native state on win32 (GetScrollBarInfo), which is invisible while the shell is hidden */
		shell.open();
		scrolledComposite = new ScrolledComposite(shell, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setSize(160, 120);
		Canvas content = new Canvas(scrolledComposite, SWT.NONE);
		content.setSize(8, 8);
		scrolledComposite.setContent(content);

		assertNull(scrolledComposite.getLogicalContentSize());

		int logicalHeight = Math.multiplyExact(10_000_000, 22);
		scrolledComposite.setLogicalContentSize(1_600, logicalHeight);
		scrolledComposite.layout(false);

		assertEquals(new Point(1_600, logicalHeight), scrolledComposite.getLogicalContentSize());
		assertTrue(scrolledComposite.getHorizontalBar().getVisible());
		assertTrue(scrolledComposite.getVerticalBar().getVisible());
		assertEquals(new Point(0, 0), content.getLocation(),
				"logical content must remain pinned instead of moving a huge native child");

		Rectangle body = scrolledComposite.getClientArea();
		assertEquals(body.width, content.getSize().x,
				"logical paint surface should be bounded to the viewport width");
		assertEquals(body.height, content.getSize().y,
				"logical paint surface should be bounded to the viewport height");

		scrolledComposite.setOrigin(800, 100_000_000);
		assertEquals(new Point(800, 100_000_000), scrolledComposite.getOrigin());
		assertEquals(new Point(0, 0), content.getLocation(),
				"scrolling a logical extent must not translate the physical paint surface");

		scrolledComposite.clearLogicalContentSize();
		assertNull(scrolledComposite.getLogicalContentSize());
		assertEquals(new Point(0, 0), scrolledComposite.getOrigin(),
				"returning to legacy physical mode resets the logical origin");
	}

	@Test
	public void test_viewportOriginMirrorsLegacyContentMovementAndLayoutStabilizes() {
		/* scroll bar visibility is the native state on win32 (GetScrollBarInfo), which is invisible while the shell is hidden */
		shell.open();
		scrolledComposite = new ScrolledComposite(shell, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setSize(120, 100);
		Composite content = new Composite(scrolledComposite, SWT.NONE);
		content.setSize(600, 400);
		scrolledComposite.setContent(content);
		scrolledComposite.layout(false);

		assertTrue(scrolledComposite.getHorizontalBar().getVisible());
		assertTrue(scrolledComposite.getVerticalBar().getVisible());

		scrolledComposite.setOrigin(37, 41);
		assertEquals(new Point(37, 41), scrolledComposite.getOrigin());
		assertEquals(new Point(-37, -41), content.getLocation());

		/*
		 * Legacy callers are still allowed to move the physical content directly.
		 * The shared logical viewport mirrors that state instead of replacing it.
		 */
		content.setLocation(-19, -23);
		assertEquals(new Point(19, 23), scrolledComposite.getOrigin());
		content.setLocation(4, 6);
		assertEquals(new Point(-4, -6), scrolledComposite.getOrigin(),
				"direct legacy content movement must retain the historical physical-origin reflection");
		content.setLocation(-19, -23);
		assertEquals(new Point(19, 23), scrolledComposite.getOrigin());

		for (int pass = 0; pass < 8; pass++) {
			scrolledComposite.layout(false);
			assertTrue(scrolledComposite.getHorizontalBar().getVisible());
			assertTrue(scrolledComposite.getVerticalBar().getVisible());
		}
		assertEquals(new Point(19, 23), scrolledComposite.getOrigin());
	}


}

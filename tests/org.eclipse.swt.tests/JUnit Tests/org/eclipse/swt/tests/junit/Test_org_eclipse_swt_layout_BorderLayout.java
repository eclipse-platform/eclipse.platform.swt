/*******************************************************************************
 * Copyright (c) 2021 Christoph Läubrich and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Christoph Läubrich - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.BorderData;
import org.eclipse.swt.layout.BorderLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class {@link BorderLayout}
 */
public class Test_org_eclipse_swt_layout_BorderLayout {

	Display display;

	@Before
	public void setUp() {
		display = Display.getDefault();
	}

	public void tearDown() {
		SwtTestUtil.processEvents();
		display.dispose();
	}

	@Test
	public void testControlExcluded() {
		Shell shell = new Shell(display);
		shell.setLayout(new BorderLayout());
		shell.setSize(800, 600);
		MockControl control = new MockControl(shell);
		control.setLayoutData(new BorderData(SWT.NONE));
		control.setBounds(0, 0, 100, 100);
		shell.open();
		SwtTestUtil.processEvents();
		// the expectation is that the control is set to zero width/height
		// even we have set a different size before...
		Rectangle bounds = control.getBounds();
		assertEquals(0, bounds.width);
		assertEquals(0, bounds.height);
		shell.dispose();
	}

	@Test
	public void testHints() {
		Shell shell = new Shell(display);
		shell.setLayout(new BorderLayout());
		shell.setSize(800, 600);
		MockControl controlNorth = new MockControl(shell);
		MockControl controlSouth = new MockControl(shell);
		MockControl controlWest = new MockControl(shell);
		MockControl controlEast = new MockControl(shell);
		controlNorth.setLayoutData(new BorderData(SWT.TOP, 40, 50));
		controlWest.setLayoutData(new BorderData(SWT.LEFT, 20, 30));
		controlSouth.setLayoutData(new BorderData(SWT.BOTTOM));
		controlEast.setLayoutData(new BorderData(SWT.RIGHT));
		shell.open();
		SwtTestUtil.processEvents();
		// the expectation is that the hint is not passed but the (larger) x-space is
		// used to get optimal layout
		assertTrue(controlNorth.wHint > 100);
		// the expectation is that the hint is passed to the control
		assertEquals(50, controlNorth.hHint);
		//for the side areas, both hint should be passed
		assertEquals(30, controlWest.hHint);
		assertEquals(20, controlWest.wHint);
		//for the bottom area, we expect that SWT default is passed for the height but a certain number for the width
		assertTrue(controlSouth.wHint > 100);
		assertEquals(SWT.DEFAULT, controlSouth.hHint);
		//for the east, we want all the defaults
		assertEquals(SWT.DEFAULT, controlEast.hHint);
		assertEquals(SWT.DEFAULT, controlEast.wHint);
		shell.dispose();
	}

	@Test
	public void testControlLayout() {
		Shell shell = new Shell(display);
		shell.setLayout(new BorderLayout());
		shell.setSize(800, 600);
		MockControl topControl = new MockControl(shell);
		topControl.reportedHeight = 10;
		topControl.reportedWidth = 1000000;
		MockControl bottomControl = new MockControl(shell);
		bottomControl.reportedHeight = 20;
		bottomControl.reportedWidth = 123;
		MockControl leftControl = new MockControl(shell);
		leftControl.reportedHeight = 5;
		leftControl.reportedWidth = 50;
		MockControl rightControl = new MockControl(shell);
		rightControl.reportedHeight = 5;
		rightControl.reportedWidth = 40;
		MockControl centerControl = new MockControl(shell);
		topControl.setLayoutData(new BorderData(SWT.TOP));
		bottomControl.setLayoutData(new BorderData(SWT.BOTTOM));
		leftControl.setLayoutData(new BorderData(SWT.LEFT));
		rightControl.setLayoutData(new BorderData(SWT.RIGHT));
		shell.open();
		shell.layout();
		SwtTestUtil.processEvents();
		// the expectation is that the control at the north is at the top and has the
		// size returned by computeSize but a width that is the width of the shell
		Rectangle clientArea = shell.getClientArea();
		Rectangle topBounds = topControl.getBounds();
		assertEquals(0, topBounds.x);
		assertEquals(0, topBounds.y);
		assertEquals(clientArea.width, topBounds.width);
		assertEquals(topControl.reportedHeight, topBounds.height);
		// the expectation is that the control at the south is at the bottom and has
		// the size returned by computeSize but a width that is the width of the shell
		Rectangle bottomBounds = bottomControl.getBounds();
		assertEquals(0, bottomBounds.x);
		assertEquals(clientArea.height - bottomControl.reportedHeight, bottomBounds.y);
		assertEquals(clientArea.width, bottomBounds.width);
		assertEquals(bottomControl.reportedHeight, bottomBounds.height);
		// the expectation is that the control at the west is placed left and has
		// the size returned by computeSize but the height is that of the shell
		// subtracting the height of the north/south controls
		Rectangle leftBounds = leftControl.getBounds();
		assertEquals(0, leftBounds.x);
		assertEquals(topControl.reportedHeight, leftBounds.y);
		assertEquals(leftControl.reportedWidth, leftBounds.width);
		assertEquals(clientArea.height - topControl.reportedHeight - bottomControl.reportedHeight, leftBounds.height);
		// the expectation is that the control at the east is placed right and has
		// the size returned by computeSize but the height is that of the shell
		// subtracting the height of the north/south controls
		Rectangle rightBounds = rightControl.getBounds();
		assertEquals(clientArea.width - rightControl.reportedWidth, rightBounds.x);
		assertEquals(topControl.reportedHeight, rightBounds.y);
		assertEquals(rightControl.reportedWidth, rightBounds.width);
		assertEquals(clientArea.height - topControl.reportedHeight - bottomControl.reportedHeight, rightBounds.height);
		// the center control should take the remaining size...
		Rectangle centerBounds = centerControl.getBounds();
		assertEquals(leftControl.reportedWidth, centerBounds.x);
		assertEquals(topControl.reportedHeight, centerBounds.y);
		assertEquals(clientArea.width - leftControl.reportedWidth - rightControl.reportedWidth, centerBounds.width);
		assertEquals(clientArea.height - topControl.reportedHeight - bottomControl.reportedHeight, centerBounds.height);
		shell.dispose();
	}

	private static final class MockControl extends Canvas {

		private int wHint;
		private int hHint;

		private int reportedHeight = -1;
		private int reportedWidth = -1;

		public MockControl(Composite parent) {
			super(parent, SWT.NONE);
			setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_BLUE));
		}

		@Override
		public Point computeSize(int wHint, int hHint, boolean changed) {
			this.wHint = wHint;
			this.hHint = hHint;
			if (reportedHeight > 0 && reportedWidth > 0) {
				return new Point(reportedWidth, reportedHeight);
			}
			return super.computeSize(wHint, hHint, changed);
		}

	}

}

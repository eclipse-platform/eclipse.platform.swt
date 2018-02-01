/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Title: Bug 465280 – [GTK3] OS.gtk_widget_get_allocation returns (0,0) for invisible controls
 * How to run: These are jUnits. Select the class and run as jUnits.
 * Bug description: getBounds is not working properly in combination with setVisible.
 * Expected results: All tests should pass, but on Gtk3.8+ the "fails_*" tests fail.
 * GTK version(s): GTK3.8+
 *
 * This is a snippet to validate upcomming bug submission. These will be used to make new jUnits later.
 */
public class Bug497705_setBoundsAfterSetVisible {

	boolean debugShowWidget = false; // true = see shell & widget.  False = tests run without interaction.

	Display display;
	Shell shell ;
	private StringBuffer log;
	private int x;
	private int y;
	private int height;
	private int width;
	private boolean passed;
	private Rectangle bounds;
	Control testControl;

	@Before
	public void setUp() {
		display = Display.getDefault();
		shell = new Shell(display);
		shell.setSize(400, 400);
		log = new StringBuffer("");
		x = 5;
		y = 10;
		height = 100;
		width = 200;
		passed = true;
		testControl = new Button(shell, SWT.PUSH);
	}

	@Test
	public void fails_test2_setBoundsAfterVisibility() { // Works on Gtk2, Fails on Gtk3.
		testControl.setVisible(false);
		testControl.setVisible(true);

		testControl.setBounds(x, y, width, height);

		bounds = testControl.getBounds();
		verifyBounds();
	}
	@Test
	public void fails_test2b_setBoundsInvisibleWidgets() { // Works on Gtk2, Fails on Gtk3.
		testControl.setVisible(false);

		testControl.setBounds(x, y, width, height);

		bounds = testControl.getBounds();
		verifyBounds();
	}


	@Test
	public void fails_test3_setBoundsBetweenVisibility() { // Works on Gtk2, Fails on Gtk3.
		testControl.setVisible(false);
		testControl.setBounds(x, y, width, height);
		testControl.setVisible(true);

		bounds = testControl.getBounds();
		verifyBounds();
	}

	@Test
	public void fails_moveInnvisibleControl() {
		testControl.setBounds(4, 4, 6, 6);

		shell.open(); for (int i = 0; i < 500; i++) display.readAndDispatch();
		testControl.setVisible(false);
		shell.open(); for (int i = 0; i < 500; i++) display.readAndDispatch();
		testControl.setBounds(x, y, width, height);
		shell.open(); for (int i = 0; i < 500; i++) display.readAndDispatch();
		testControl.setVisible(true);
		shell.open(); for (int i = 0; i < 500; i++) display.readAndDispatch();

		bounds = testControl.getBounds(); // Visually looks ok. (width/height), but programatically incorrect getBounds().
		verifyBounds();
	}

	@Test
	public void fails_unecessaryEvents() { // Breaks on Gtk3.8 & onwards
		testControl.setVisible(false);

		AtomicInteger resizeCount = new AtomicInteger(0);
		AtomicInteger moveCount = new AtomicInteger(0);

		testControl.addControlListener(new ControlListener() {
			@Override
			public void controlResized(ControlEvent e) {
				resizeCount.incrementAndGet();
			}
			@Override
			public void controlMoved(ControlEvent e) {
				moveCount.incrementAndGet();
			}
		});

		for (int i = 0; i < 10; i++) {
			testControl.setBounds(x, y, width, height); // Once bounds set, calling same bounds shouldn't trigger SWT.MOVE events.
		}
		if (resizeCount.get() != 1 || moveCount.get() != 1) {
			passed = false;
			log.append("\nERROR:\nExpected only one Resize and one Move event.\nActually received R/M:" + resizeCount.get() + "/" + moveCount.get());
		}
	}

	@Test
	public void works_test1_setBoundsBeforeVisibility () {
		// Note, here you can see that getBounds() does work for widgets that are set to be invisible, but
		// only if setBounds was called before setVisible(false).
		// The problem is inside setBounds, if it's called after setVisible(false), then getBounds() returns wrong output.
		testControl.setBounds(x, y, width, height);

		testControl.setVisible(false);
//		testControl.setVisible(true);   // commenting or uncommenting this doesn't change anything.

		bounds = testControl.getBounds();
		verifyBounds();
	}

	@Test
	public void works_zeroSize() { // There has to be a mechanism by which we set with and height to 0.
		testControl.setBounds(x, y, width, height);
		x = y = width = height = 0;
		testControl.setBounds(x, y, width, height);
		bounds = testControl.getBounds();
		verifyBounds();
	}

	@Test
	public void works_drainingQueue() {
		testControl.setVisible(false);
		testControl.setVisible(true);

		// doing readAndDispatch up *before* 'setBounds()' doesn't make a difference.
		testControl.setBounds(x, y, width, height);

		// doing readAndDispatch *After* setBounds *many times* gives gtk time to update it's cache, and getBounds() returns correct coordinates.
		shell.open();
		for (int i = 0; i < 1000; i++)
			display.readAndDispatch();

		bounds = testControl.getBounds();
		verifyBounds();
	}

	@After
	public void tearDown() {
		if (debugShowWidget) {
			if (!passed) System.err.println(log.toString());

			shell.open();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
			display.dispose();
		}
		assertTrue(log.toString(), passed);
	}

	private void verifyBounds() {
		if (bounds.x != x | bounds.y != y) {
			passed = false;
			log.append("\nERROR: x,y do not match. Expected:" + x + "/" + y + " Actual:" + bounds.x + "/" + bounds.y);
		}
		if (bounds.height != height | bounds.width != width) {
			passed = false;
			log.append("\nERROR: width,height do not match. Expected:" + width + "/" + height + " Actual:"
					+ bounds.width + "/" + bounds.height);
		}
	}

}

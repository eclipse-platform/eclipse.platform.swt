/*******************************************************************************
 * Copyright (c) 2020, 2021 Syntevo and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Syntevo - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.manual;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Bug561444_JvmCrash_SetRedraw {
	static void paintRightSide(Control control) {
		GC gc = new GC(control);
		gc.setBackground(control.getDisplay().getSystemColor(SWT.COLOR_RED));

		Point size = control.getSize();
		int sizeX3 = size.x / 3;
		gc.fillRectangle(new Rectangle(size.x - sizeX3, 0, sizeX3, size.y));

		gc.dispose();
	}

	static void paintLeftSide(Control control, GC gc) {
		gc.setBackground(control.getDisplay().getSystemColor(SWT.COLOR_BLUE));

		Point size = control.getSize();
		int sizeX3 = size.x / 3;
		gc.fillRectangle(new Rectangle(0, 0, sizeX3, size.y));
	}

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, true));

		final Text hint = new Text(shell, SWT.READ_ONLY | SWT.MULTI);
		hint.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		hint.setText(
			"Common bugs\n" +
			"----\n" +
			"1) Disable (A) + Enable (A)\n" +
			"   Combo and Single-line Text will stop responding to mouse events\n" +
			"2) Click (C)\n" +
			"   Weird part of Combo will be painted.\n" +
			"   This is because wrong GdkWindow is used for constructing GC.\n" +
			"3) Click (C) then (D)\n" +
			"   Combo and Single-line Text will not be redrawn\n" +
			"\n" +
			"How to enable XIM input method\n" +
			"----\n" +
			"On GNOME, use env var:\n" +
			"   GTK_IM_MODULE=xim\n" +
			"On XFCE, configure:\n" +
			"   OS \"Applications\" menu | Settings | Input Method Selector\n" +
			"   'Use X compose table'\n" +
			"\n" +
			"Bugs with XIM\n" +
			"----\n" +
			"1) Disable (A)\n" +
			"   SWT will crash with 'received an X Window System error'\n" +
			"2) Click (C)\n" +
			"   SWT will crash with 'received an X Window System error'\n" +
			"\n" +
			"Bugs not fixed in this patch\n" +
			"----\n" +
			"1) Enable (B) + Disable (B)\n" +
			"   Combo will no longer have selection background.\n" +
			"\n" +
			"Tests that should work before and after patch\n" +
			"----\n" +
			"1) Enable (B) + Press (D)\n" +
			"   Left side shall be painted blue for tested controls\n" +
			"2) Click (E)\n" +
			"   Mouse cursor shall change for tested controls\n" +
			""
		);

		Composite composite = new Composite(shell, 0);
		composite.setLayout(new GridLayout(2, false));

		Group grpTestControls = new Group(composite, 0);
		grpTestControls.setText("Tested controls");
		grpTestControls.setLayout(new GridLayout(1, true));

		new Label(grpTestControls, 0).setText("Single-line text:");
		final Text textS = new Text(grpTestControls, SWT.BORDER);
		textS.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		new Label(grpTestControls, 0).setText("Multi-line text:");
		final Text textM = new Text(grpTestControls, SWT.BORDER | SWT.MULTI);
		textM.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		// Disable textview's own background so that it doesn't overpaint our SWT.Paint.
		// Note that child text's background covers entire control anyway.
		textM.setData("org.eclipse.swt.internal.gtk.css", "textview {background-color: transparent;}");

		new Label(grpTestControls, 0).setText("Combo:");
		final Combo combo = new Combo(grpTestControls, 0);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		combo.add("Combo item 1");
		combo.add("Combo item 2");

		final Control[] testControls = new Control[] {textS, textM, combo};

		Group grpTests = new Group(composite, 0);
		grpTests.setText("Available tests");
		grpTests.setLayout(new GridLayout(1, true));

		Button button = new Button(grpTests, SWT.CHECK);
		button.setText("(A) Enable redrawing for test controls");
		button.setSelection(true);
		button.addListener(SWT.Selection, e -> {
			final boolean isSelected = ((Button)e.widget).getSelection();
			for (Control control : testControls) {
				control.setRedraw(isSelected);
			}
		});

		final Listener paintLeftSideListener = e -> {
			paintLeftSide((Control)e.widget, e.gc);
		};

		button = new Button(grpTests, SWT.CHECK);
		button.setText("(B) Paint via SWT.Paint");
		button.addListener(SWT.Selection, e -> {
			final boolean isSelected = ((Button)e.widget).getSelection();
			if (isSelected) {
				for (Control control : testControls) {
					// Without disabling default background, anything we paint in SWT.Paint gets over-painted by default 'draw' in GTK
					control.setData(control.getBackground());
					control.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));

					control.addListener(SWT.Paint, paintLeftSideListener);
				}
			} else {
				for (Control control : testControls) {
					control.setBackground((Color)control.getData());
					control.removeListener(SWT.Paint, paintLeftSideListener);
				}
			}
		});

		button = new Button(grpTests, SWT.NONE);
		button.setText("(C) Paint via GC()");
		button.addListener(SWT.Selection, e -> {
			for (Control control : testControls) {
				paintRightSide(control);
			}
		});

		button = new Button(grpTests, SWT.NONE);
		button.setText("(D) Invoke Control.redraw()");
		button.addListener(SWT.Selection, e -> {
			for (Control control : testControls) {
				control.redraw();
			}
		});

		button = new Button(grpTests, SWT.NONE);
		button.setText("(E) Invoke Text.setCursor()");
		button.addListener(SWT.Selection, e -> {
			for (Control control : testControls) {
				control.setCursor(new Cursor(display, SWT.CURSOR_HAND));
			}
		});

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}

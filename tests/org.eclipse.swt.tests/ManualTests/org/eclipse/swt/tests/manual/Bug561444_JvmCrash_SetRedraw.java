/*******************************************************************************
 * Copyright (c) 2020 Syntevo and others.
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

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Bug561444_JvmCrash_SetRedraw {
	static Text textS;
	static Text textM;

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
		shell.setLayout(new RowLayout(SWT.VERTICAL));

		final Text hint = new Text(shell, SWT.READ_ONLY | SWT.MULTI);
		hint.setText(
			"How to enable XIM input method\n" +
			"----\n" +
			"   On GNOME, use env var:\n" +
			"       GTK_IM_MODULE=xim\n" +
			"   On XFCE, configure:\n" +
			"       OS \"Applications\" menu | Settings | Input Method Selector\n" +
			"       'Use X compose table'\n" +
			"\n" +
			"Bugs with XIM\n" +
			"----\n" +
			"1) Uncheck 'Control.setRedraw()'\n" +
			"   SWT will crash with 'received an X Window System error'\n" +
			"2) Click 'GC()' button\n" +
			"   SWT will crash with 'received an X Window System error'\n" +
			"\n" +
			"Bugs without XIM\n" +
			"----\n" +
			"1) Uncheck and check 'Control.setRedraw()'\n" +
			"   Single-line Text will stop responding to mouse events\n" +
			"2) Click 'GC()', then Control.setRedraw()\n" +
			"   Single-line Text will not be redrawn\n"
		);

		textS = new Text(shell, SWT.NONE);
		textS.setText("Single-line text");

		textM = new Text(shell, SWT.MULTI);
		textM.setText("Multi-line text");

		Button button;

		button = new Button(shell, SWT.NONE);
		button.setText("Control.getMonitor(): report to console");
		button.addListener(SWT.Selection, e -> {
			Monitor monitor1 = shell.getMonitor();
			Monitor monitor2 = textS.getMonitor();
			Monitor monitor3 = textM.getMonitor();

			if (monitor1.equals(monitor2) && monitor2.equals(monitor3))
				System.out.println("All good");
			else
				System.out.println("Some monitors are wrong");
		});

		button = new Button(shell, SWT.CHECK);
		button.setText("Control.setRedraw()");
		button.setSelection(true);
		button.addListener(SWT.Selection, e -> {
			final boolean isSelected = ((Button)e.widget).getSelection();
			textS.setRedraw(isSelected);
			textM.setRedraw(isSelected);
		});

		final Listener paintLeftSideListener = e -> {
			paintLeftSide((Control)e.widget, e.gc);
		};

		button = new Button(shell, SWT.CHECK);
		button.setText("Control.gtk_draw(): paint left side (also click button below and then 'Control.redrawWidget()')");
		button.addListener(SWT.Selection, e -> {
			final boolean isSelected = ((Button)e.widget).getSelection();
			if (isSelected) {
				textM.addListener(SWT.Paint, paintLeftSideListener);
				textS.addListener(SWT.Paint, paintLeftSideListener);
			} else {
				textM.removeListener(SWT.Paint, paintLeftSideListener);
				textS.removeListener(SWT.Paint, paintLeftSideListener);
			}
		});

		button = new Button(shell, SWT.NONE);
		button.setText("Remove default background of Text (required for painting left side)");
		button.addListener(SWT.Selection, e -> {
			// Without disabling default background, anything we paint in SWT.Paint gets over-painted by default 'draw' in GTK
			textS.setData("org.eclipse.swt.internal.gtk.css", "entry {background-color: transparent;}");
			textM.setData("org.eclipse.swt.internal.gtk.css", "textview {background-color: transparent;} textview text {background-color: transparent;}");
		});

		button = new Button(shell, SWT.NONE);
		button.setText("GC(): paint right side");
		button.addListener(SWT.Selection, e -> {
			paintRightSide(textS);
			paintRightSide(textM);
		});

		button = new Button(shell, SWT.NONE);
		button.setText("Control.redrawWidget()");
		button.addListener(SWT.Selection, e -> {
			textS.redraw();
			textM.redraw();
		});

		button = new Button(shell, SWT.NONE);
		button.setText("Text.setCursor()");
		button.addListener(SWT.Selection, e -> {
			textS.setCursor(new Cursor(display, SWT.CURSOR_HAND));
			textM.setCursor(new Cursor(display, SWT.CURSOR_HAND));
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

/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug379271_ComboCaretLocationOffset {

	static int width = 1;		// default width on Mac and GTK
	static int height = 0;

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		final Shell hint = new Shell(shell, SWT.NO_TRIM);

		shell.setLayout(new FillLayout());

		final Combo combo = new Combo(shell, SWT.DROP_DOWN);
		combo.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.CR) {
					combo.add(combo.getText());
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println("caret position: " + combo.getCaretPosition());
				System.out.println("caret location: " + combo.getCaretLocation());

				display.asyncExec(() -> {
					Point pt = combo.getCaretLocation();

					/* Mapping to combo box, wrong location */
					Rectangle comboRect = display.map(combo, null, pt.x, pt.y, width, height);

					hint.setLocation(comboRect.x, comboRect.y);

					hint.moveAbove(null);
					hint.redraw();
				});

			}
		});

		combo.addListener(SWT.MouseMove, e -> {
			System.out.println(e.x + " " + e.y);
		});

		height = combo.getFont().getFontData()[0].getHeight();
		System.out.println("caret width: " + width + ", combo font height: " + height);

		hint.setSize(width, height);
		hint.setBackground(display.getSystemColor(SWT.COLOR_RED));
		hint.open();

		shell.setBounds(800, 800, 200, 50);
		shell.pack();
		shell.open();

		System.err.println("Map (0, 0) to shell: " + display.map(shell, null, 0, 0, width, height));
		System.err.println("Map (0, 0) to combo: " + display.map(combo, null, 0, 0, width, height));

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();

	}
}

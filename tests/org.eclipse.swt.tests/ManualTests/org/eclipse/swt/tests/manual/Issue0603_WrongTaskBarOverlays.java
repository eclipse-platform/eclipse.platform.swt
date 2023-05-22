/*******************************************************************************
 * Copyright (c) 2023 Syntevo and others.
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
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public final class Issue0603_WrongTaskBarOverlays {
	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, true));

		final Label hint = new Label(shell, 0);
		hint.setText(
			"1. Use Win11; Win10 is not enough\n" +
			"2. Start the program and pin its icon on TaskBar\n" +
			"3. Press the button below to open a few Shells with overlays\n" +
			"4. Issue #603: when one of them is closed, it doesn't restore previous overlay correctly\n" +
			"   Note that Windows is documented to restore the overlay that was set last, contrary to\n" +
			"   overlay of currently active Shell\n" +
			"5. Issue #603: when all of them is closed, there should be no overlay\n" +
			"6. The checkboxes below are to test that patch didn't break setting text/image overlays"
		);

		Button button = new Button(shell, SWT.PUSH);
		button.setText("Test");
		button.addListener(SWT.Selection, e -> {
			for (int iShell = 0; iShell < 3; iShell++) {
				Shell shell2 = new Shell(display);
				shell2.setText("Shell #" + iShell);
				shell2.setSize(250, 100);
				shell2.open();

				final TaskItem taskItem2 = display.getSystemTaskBar().getItem(shell2);
				taskItem2.setOverlayText("#" + iShell);

				// Windows Explorer seems to get confused when multiple overlays are set quickly
				// It will then sometimes put them in wrong order.
				// The workaround is to wait and process display events after each shell
				{
					try {
						Thread.sleep(500);
					} catch (InterruptedException ex) {
					}

					while (display.readAndDispatch()) {
					}
				}
			}
		});

		final TaskItem taskItem = display.getSystemTaskBar().getItem(shell);

		Button chkText = new Button(shell, SWT.CHECK);
		chkText.setText("Set Text overlay for me");
		chkText.addListener(SWT.Selection, e -> {
			if (chkText.getSelection()) {
				taskItem.setOverlayText("Main");
			} else {
				taskItem.setOverlayText("");
			}
		});

		Image image = new Image(display, 16, 16);
		{
			GC gc = new GC(image);
			gc.setBackground(display.getSystemColor(SWT.COLOR_RED));
			gc.setForeground(display.getSystemColor(SWT.COLOR_RED));
			gc.fillRectangle(0, 0, 16, 16);
			gc.dispose();
		}

		Button chkImage = new Button(shell, SWT.CHECK);
		chkImage.setText("Set Image overlay for me");
		chkImage.addListener(SWT.Selection, e -> {
			if (chkImage.getSelection()) {
				taskItem.setOverlayImage(image);
			} else {
				taskItem.setOverlayImage(null);
			}
		});

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		image.dispose();
	}
}

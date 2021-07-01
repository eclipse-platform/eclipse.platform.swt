/*******************************************************************************
 * Copyright (c) 2021 Syntevo and others.
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
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tray;

public class Bug562233_DisposeExceptions {
	public static void main(String[] args) {
		GridLayout layout = new GridLayout(1, true);
		layout.marginHeight = 10;
		layout.marginWidth = 10;

		Display display = new Display();
		Shell mainShell = new Shell(display, SWT.SHELL_TRIM);
		mainShell.setLayout(layout);

		Button btnTest;

		btnTest = new Button(mainShell, SWT.PUSH);
		btnTest.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		btnTest.setText("1. [GTK] Becoming a zombie with SWT.Dispose for Shell");
		btnTest.addListener(SWT.Selection, eTest -> {
			Shell shell = new Shell(mainShell, SWT.SHELL_TRIM);
			shell.setLayout(layout);

			Label hint = new Label(shell, SWT.NONE);
			hint.setText(
				"1. Close this Shell\n" +
				"2. Close main Shell\n" +
				"3. There will be GTK-Critical on console or a JVM crash\n" +
				"4. Program will NOT exit and become a zombie without UI"
			);

			shell.addListener(SWT.Dispose, e -> {
				// This is the throw that breaks SWT
				throw new AssertionError("Test assertion");
			});

			shell.pack();
			shell.open();
		});

		btnTest = new Button(mainShell, SWT.PUSH);
		btnTest.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		btnTest.setText("2. [GTK] JVM crash with asyncExec() for Shell");
		btnTest.addListener(SWT.Selection, eTest -> {
			Shell shell = new Shell(mainShell, SWT.SHELL_TRIM);
			shell.setLayout(layout);

			Label hint = new Label(shell, SWT.NONE);
			hint.setText(
				"1. Close this Shell\n" +
				"2. JVM will crash"
			);

			shell.addListener(SWT.Dispose, e -> {
				display.asyncExec(() -> {
					// If SWT.Dispose throws, Shell will still be considered alive when it's not.
					// Trying to access it will access already-freed GTK pointers.
					if (!shell.isDisposed()) {
						shell.getBounds();
					}
				});

				// This is the throw that breaks SWT
				throw new AssertionError("Test assertion");
			});

			shell.pack();
			shell.open();
		});

		btnTest = new Button(mainShell, SWT.PUSH);
		btnTest.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		btnTest.setText("3. [GTK] Errors/Crashes with ProgressBar");
		btnTest.addListener(SWT.Selection, eTest -> {
			Shell shell = new Shell(mainShell, SWT.SHELL_TRIM);
			shell.setLayout(layout);

			Label hint = new Label(shell, SWT.NONE);
			hint.setText(
				"1. Close this Shell\n" +
				"2. There will be GTK-Critical on console or a JVM crash"
			);

			// ProgressBar(SWT.INDETERMINATE) has an internal timer that won't be removed
			// if dispose is interrupted by exception
			new ProgressBar(shell, SWT.INDETERMINATE);

			// There is a similar problem with tooltip.setVisible(), not shown by this snippet

			shell.addListener(SWT.Dispose, e -> {
				// This is the throw that breaks SWT
				throw new AssertionError("Test assertion");
			});

			shell.pack();
			shell.open();
		});

		btnTest = new Button(mainShell, SWT.PUSH);
		btnTest.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		btnTest.setText("4. [GTK] Errors/Crashes with StyledText");
		btnTest.addListener(SWT.Selection, eTest -> {
			Shell shell = new Shell(mainShell, SWT.SHELL_TRIM);
			shell.setLayout(layout);

			Label hint = new Label(shell, SWT.NONE);
			hint.setText(
				"1. Close this Shell\n" +
				"2. There will be GTK-Critical on console or a JVM crash"
			);

			// StyledText installs 'Display.asyncExec()' in many APIs
			StyledText styledText = new StyledText(shell, SWT.V_SCROLL);
			styledText.setWordWrap(true);
			styledText.setText("StyledText");

			shell.addListener(SWT.Dispose, e -> {
				// Trigger 'Display.asyncExec()' in StyledText
				styledText.setBackground(null);

				// This is the throw that breaks SWT
				throw new AssertionError("Test assertion");
			});

			shell.pack();
			shell.open();
		});

		btnTest = new Button(mainShell, SWT.PUSH);
		btnTest.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		btnTest.setText("5. [GTK][win32][macOS] Becoming a zombie with Display.dispose()");
		btnTest.addListener(SWT.Selection, eTest -> {
			Shell shell = new Shell(mainShell, SWT.SHELL_TRIM);
			shell.setLayout(layout);

			Label hint = new Label(shell, SWT.NONE);
			hint.setText(
				"1. Close this Shell\n" +
				"2. Snippet will attempt to exit and will close all Shells\n" +
				"3. Program will NOT exit and become a zombie without UI"
			);

			shell.addListener(SWT.Dispose, e -> {
				// Simulate application that counts open Shells and exits when the last one is closed.
				// Exit is scheduled in async way to avoid exiting while something is still on stack.
				System.out.println("Exiting from SWT.Dispose handler");
				display.asyncExec(() -> {
					mainShell.dispose();

					// Failure point 4: any pending Display.asyncExec()
					display.asyncExec(() -> {
						// This is the throw that breaks SWT
						System.out.println("Throwing from Display.asyncExec() handler");
						throw new AssertionError("Test assertion (Display.disposeExec)");
					});

					display.dispose ();
				});
			});

			// Failure point 1: 'SWT.Dispose' for 'Display'
			display.addListener (SWT.Dispose, e -> {
				// This is the throw that breaks SWT
				System.out.println("Throwing from SWT.Dispose handler for Display");
				throw new AssertionError("Test assertion (SWT.Dispose for Display)");
			});

			// Failure point 2: closing other Shells
			new Shell().addListener(SWT.Dispose, e -> {
				// This is the throw that breaks SWT
				System.out.println("Throwing from SWT.Dispose handler for Shell");
				throw new AssertionError("Test assertion (SWT.Dispose for Shell)");
			});

			// Failure point 3: closing Tray
			Tray tray = display.getSystemTray ();
			if (tray != null) {
				tray.addListener(SWT.Dispose, e -> {
					// This is the throw that breaks SWT
					System.out.println("Throwing from SWT.Dispose handler for Tray");
					throw new AssertionError("Test assertion (SWT.Dispose for Tray)");
				});
			}

			// Failure point 5: Display.disposeExec()
			display.disposeExec(() -> {
				// This is the throw that breaks SWT
				System.out.println("Throwing from Display.disposeExec() handler");
				throw new AssertionError("Test assertion (Display.disposeExec)");
			});

			shell.pack();
			shell.open();

			// A display loop without Shell based exit condition, to be broken by disposing Display
			while (!display.isDisposed()) {
				try {
					if (!display.readAndDispatch())
						display.sleep();
				} catch (Throwable ex) {
					System.out.println("Exception during readAndDispatch(): " + ex.getMessage());
				}
			}
		});

		btnTest = new Button(mainShell, SWT.PUSH);
		btnTest.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		btnTest.setText("6. [win32][macOS] Shell becomes broken after trying to close it");
		btnTest.addListener(SWT.Selection, eTest -> {
			Shell shell = new Shell(mainShell, SWT.SHELL_TRIM);
			shell.setLayout(layout);

			Label hint = new Label(shell, SWT.NONE);
			hint.setText(
				"1. Click [X] once to close this Shell\n" +
				"2. Note that the Shell will not be closed\n" +
				"3. Click TableItem\n" +
				"4. Note that images are gone\n" +
				"5. [win32] Note that exceptions are being thrown when hovering items\n" +
				"6. Click [X] again\n" +
				"7. Only now the Shell will be closed"
			);

			// Make a small image
			Image image = new Image (display, 16, 16);
			GC gc = new GC(image);
			gc.setBackground (display.getSystemColor (SWT.COLOR_BLUE));
			gc.fillRectangle (0, 0, 16, 16);
			gc.dispose ();

			// And a Table to use it
			Table table = new Table(shell, 0);
			for (int i = 0; i < 5; i++) {
				TableItem tableItem = new TableItem (table, 0);
				tableItem.setImage (image);
				tableItem.setText ("TableItem");
			}

			shell.addListener(SWT.Dispose, e -> {
				// Simulate disposing any resources used in Shell
				image.dispose ();

				// This is the throw that breaks SWT
				throw new AssertionError("Test assertion");
			});

			shell.pack();
			shell.open();
		});

		mainShell.pack();
		mainShell.open();
		while (!mainShell.isDisposed()) {
			try {
				if (!display.readAndDispatch())
					display.sleep();
			} catch (Throwable ex) {
				System.out.println("Exception during readAndDispatch(): " + ex.getMessage());
			}
		}
		display.dispose();
	}
}

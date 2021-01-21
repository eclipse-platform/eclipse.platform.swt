/*******************************************************************************
 * Copyright (c) 2021 Nikita Nemkin
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Nikita Nemkin - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Crash/memory corruption reproducer for unsynchronized GTK
 * access between SWT and Swing GTKLookAndFeel
 *
 * This snippet creates a SWT button and a Swing button and forces
 * endless repaint of both.
 *
 * How to use: launch and click or hover over the buttons randomly.
 * In a few seconds, some kind of error or crash occurs.
 */
public class Bug569468_GtkLafSync {

	static Shell createShell(Display display) {
		final Shell shell = new Shell(display);
		shell.setText("GtkLafSync");
		shell.setLayout(new RowLayout(SWT.VERTICAL));
		Button button = new Button(shell, SWT.PUSH);
		int[] counter = new int[1];
		button.setText("SWT Button #9999");
		button.addListener(SWT.Paint, e -> {
			button.setText("SWT Button #" + counter[0]++);
		});

		Composite composite = new Composite(shell, SWT.EMBEDDED);
		Frame frame = SWT_AWT.new_Frame(composite);

		EventQueue.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			}
			int[] counter1 = new int[1];
			@SuppressWarnings("serial")
			JButton button1 = new JButton("Swing Button #9999") {

				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					setText("Swing Button #" + counter1[0]++);
				}

			};
			frame.add(button1);
		});

		shell.open();
		return shell;
	}

	public static void main(String[] args) {
		final Display display = new Display();
		Shell shell = createShell(display);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}

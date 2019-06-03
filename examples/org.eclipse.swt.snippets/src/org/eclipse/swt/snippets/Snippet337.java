/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation and others.
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
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * SWT_AWT example snippet: launch SWT from AWT and keep both active
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import java.awt.*;
import java.awt.Canvas;
import java.awt.event.*;
import java.awt.event.WindowEvent;

import javax.swing.*;

import org.eclipse.swt.*;
import org.eclipse.swt.awt.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet337 {

static Display display;

public static void main(String args[]) {
	display = new Display();
	EventQueue.invokeLater(() -> {
		JFrame mainFrame = new JFrame("Main Window");
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainFrame.addWindowListener(new Snippet337.CloseListener());
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new FlowLayout());
		JButton launchBrowserButton = new JButton("Launch Browser");
		launchBrowserButton.addActionListener(e -> {
			JFrame childFrame = new JFrame();
			final Canvas canvas = new Canvas();
			childFrame.setSize(850, 650);
			childFrame.getContentPane().add(canvas);
			childFrame.setVisible(true);
			display.asyncExec(() -> {
				Shell shell = SWT_AWT.new_Shell(display, canvas);
				shell.setSize(800, 600);
				Browser browser = new Browser(shell, SWT.NONE);
				browser.setLayoutData(new GridData(GridData.FILL_BOTH));
				browser.setSize(800, 600);
				browser.setUrl("http://www.eclipse.org");
				shell.open();
			});
		});

		mainPanel.add(new JTextField("a JTextField"));
		mainPanel.add(launchBrowserButton);
		mainFrame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainFrame.pack();
		mainFrame.setVisible(true);
	});
	display.addListener(SWT.Close, event -> EventQueue.invokeLater(() -> {
		Frame[] frames = Frame.getFrames();
		for (int i = 0; i < frames.length; i++) {
			frames[i].dispose();
		}
	}));
	while (!display.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
}
private static class CloseListener implements WindowListener {
	@Override
	public void windowClosed(WindowEvent e) {
		display.asyncExec(() -> display.dispose());
	}
	@Override
	public void windowOpened(WindowEvent e) {
	}
	@Override
	public void windowClosing(WindowEvent e) {
	}
	@Override
	public void windowIconified(WindowEvent e) {
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
	}
	@Override
	public void windowActivated(WindowEvent e) {
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
}
}
}

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
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class Bug78888_ToolBarEventInconsistency {
	public static void main(String[] args) {
		Display display = new Display ();
		final Shell shell = new Shell(display);
		shell.setBounds(10,10,200,200);
		ToolBar bar = new ToolBar(shell, SWT.FLAT);
		bar.setBounds(10,10,150,30);
		ToolItem item = new ToolItem(bar, SWT.PUSH);
		item.setText("&item 1");
		bar.addTraverseListener(new TraverseListener() {
			@Override
			public void keyTraversed(TraverseEvent e) {
				System.out.println("traverse");
			}
		});
		bar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				System.out.println("mouse down");
			}
			@Override
			public void mouseUp(MouseEvent e) {
				System.out.println("mouse up");
			}
		});
		item.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				System.out.println("selection");
			}
		});
		bar.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println("key down");
			}
			@Override
			public void keyReleased(KeyEvent e) {
				System.out.println("key up");
			}
		});
		shell.open();
		bar.setFocus();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}

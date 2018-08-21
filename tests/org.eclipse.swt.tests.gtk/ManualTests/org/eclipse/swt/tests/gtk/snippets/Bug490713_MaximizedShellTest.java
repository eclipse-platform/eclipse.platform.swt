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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class Bug490713_MaximizedShellTest {

	public static void main(String[] args) {
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("Main");
		shell.setSize(400, 400);
		shell.setLayout(new GridLayout(2,false));
		
		final Shell shell2 = new Shell(shell,SWT.SHELL_TRIM);
		shell2.setText("Second");
		shell2.setSize(400, 400);
		shell2.setLayout(new GridLayout(2,false));
		
		Button bMaximize=new Button(shell, SWT.PUSH);
		bMaximize.setText("Maximize");
		bMaximize.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				shell2.setMaximized(true);
			}
			
		});
		Button bMinimize=new Button(shell, SWT.PUSH);
		bMinimize.setText("Unmaximize");
		bMinimize.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				shell2.setMaximized(false);
			}
			
		});
		Button bShow=new Button(shell, SWT.PUSH);
		bShow.setText("Show");
		bShow.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				shell2.setVisible(true);
				
			}
			
		});
		Button bHide=new Button(shell, SWT.PUSH);
		bHide.setText("Hide");
		bHide.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				shell2.setVisible(false);
				
			}
			
		});
		
		Button bIsMaxmized=new Button(shell, SWT.PUSH);
		bIsMaxmized.setText("Is 2 Shell Maximized");
		bIsMaxmized.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				MessageBox box = new MessageBox(shell);
				box.setText("Shell2 Maximized");
				box.setMessage("Shell2 Maximized:"+shell2.getMaximized());
				box.open();
			}
			
		});
		
		new Composite(shell, SWT.NONE);
		
		new Label(shell,SWT.BORDER).setText("TestCase1:\n maximize ->\n widget.shell 2 is maximized ->\n hide ->\n widget.shell should still be maximized");
		new Label(shell,SWT.NONE);
		new Label(shell,SWT.BORDER).setText("TestCase2a:\n move widget.shell to any position ->\n maximize ->\n hide ->\n show ->\n unmaximize ->\n position should be the initial one");
		new Label(shell,SWT.BORDER).setText("TestCase2b:\n move widget.shell to any position ->\n maximize ->\n hide ->\n unmaximize ->\n show ->\n position should be the initial one");
		
		
		shell.open();
		shell2.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}
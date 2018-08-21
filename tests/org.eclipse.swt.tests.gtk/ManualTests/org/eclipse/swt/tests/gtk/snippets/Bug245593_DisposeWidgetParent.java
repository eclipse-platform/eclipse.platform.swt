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
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * This Snippet is supposed to demonstrate a probable Bug in SWT for gtk. It causes a general protection fault when you do the following:
 * - launch as swt app
 * - focus the first textfield
 * - click into the second textfield
 * - click back into the first one --> should crash with a GPF with: Symbol=IA__gtk_container_set_focus_child . The symbol changes from time to time to g_object_lib but is the focus_child one most of the time
 * - it usually happens the first time this is done but you sometimes might have to do it 2-3 times
 * 
 * The Problem seems to arise because the widget that looses focus is disposed during its own focus event processing. Though this works without problems on windows plattforms.
 * @author psimon
 *
 */
public class Bug245593_DisposeWidgetParent {
	Shell shell;
	
	public Bug245593_DisposeWidgetParent() {
		final Display display = new Display();
		shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setSize(200,200);
		createWidgets();
		shell.open ();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
	public static void main(String [] args) {
		new Bug245593_DisposeWidgetParent();
	}
	
	public void disposeWidgets(){
		parent.dispose();
	}
	
	private Composite parent;
	
	public void createWidgets(){
		parent = new Composite(shell,SWT.NONE);
		parent.setLayout(new GridLayout(2,true));
		Text triggerText = new Text(parent,SWT.BORDER);
		triggerText.setText("widget.text 1");
		Text looseFocus = new Text(parent,SWT.BORDER);
		looseFocus.setText("widget.text 2");
		triggerText.addFocusListener(new FocusAdapter(){

			@Override
			public void focusLost(FocusEvent e) {
				triggerRefresh();
			}
			
		});

		shell.layout();
	}
	
	public void triggerRefresh(){
		disposeWidgets();
		createWidgets();
		shell.layout();
	}
	
	}
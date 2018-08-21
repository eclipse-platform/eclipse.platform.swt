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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class Bug500924_tabfolder_dispose_crash {
	private static TabItem tab1;
	private static TabItem tab2;
	private static Composite cmp1;
	private static Composite cmp2;
	private static TabFolder folder;

	public static void main(String[] args) {
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("SWT Tabitem dispose bug");
		shell.setLayout(new GridLayout(1,false));

		Button btn1 = new Button(shell, SWT.PUSH);
		btn1.setText("Show/Hide tab1");
		btn1.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false));
		btn1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TabItem[] arr = folder.getItems();
				for (int a = 0; a < arr.length; a++) {
					Control ctrl = arr[a].getControl();
					arr[a].setControl(null);
					ctrl.dispose();
					arr[a].dispose();
				}
			}
		});

		folder = new TabFolder(shell, SWT.NONE);
		folder.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));

		tab1 = new TabItem(folder, SWT.NONE);
		tab1.setText("Tab 1");

		cmp1 = new Composite(folder, SWT.NONE);
		cmp1.setLayout(new FillLayout());
		new Button(cmp1, SWT.NONE).setText("Button 1");
		new Button(cmp1, SWT.NONE).setText("Button 2");
		tab1.setControl(cmp1);


		tab2 = new TabItem(folder, SWT.NONE);
		tab2.setText("Tab 2");
		cmp2 = new Composite(folder, SWT.NONE);
		cmp2.setLayout(new FillLayout());
		new Button(cmp2, SWT.NONE).setText("Button 3");
		new Button(cmp2, SWT.NONE).setText("Button 4");
		tab2.setControl(cmp2);

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}

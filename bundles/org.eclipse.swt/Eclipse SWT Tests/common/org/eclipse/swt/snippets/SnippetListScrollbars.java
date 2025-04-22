package org.eclipse.swt.snippets;

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
/*
 * Composite Snippet: inherit a background color or image
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.2
 */

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class SnippetListScrollbars {

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("List: Composite.setBackgroundMode()");
		shell.setLayout(new RowLayout(SWT.VERTICAL));
		shell.setSize(800, 800);

		Color color = display.getSystemColor(SWT.COLOR_CYAN);

		Group group = new Group(shell, SWT.NONE);
		group.setText("SWT.INHERIT_NONE");
		group.setBackground(color);
		group.setBackgroundMode(SWT.INHERIT_NONE);
		group.setSize(300, 300);
		createChildren(group);

		group = new Group(shell, SWT.NONE);
		group.setBackground(color);
		group.setText("SWT.INHERIT_DEFAULT");
		group.setBackgroundMode(SWT.INHERIT_DEFAULT);
		group.setSize(300, 300);
		createChildren(group);

		group = new Group(shell, SWT.NONE);
		group.setBackground(color);
		group.setText("SWT.INHERIT_FORCE");
		group.setBackgroundMode(SWT.INHERIT_FORCE);
		group.setSize(300, 300);
		createChildren(group);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
	static void createChildren(Composite parent) {
		parent.setLayout(new RowLayout());
		List list = new List(parent,
				SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);

		list.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				System.out.println("widgetSelected: " + e);

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

				System.out.println("widgetDefaultSelected: " + e);

			}
		});

		for (int i = 0; i < 10; i++)
			list.add("List item " + i);

		list.setSize(300, 300);

	}
}

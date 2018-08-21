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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Bug496302_GroupComputeSizeGTK2 {

	private static Group group;
	private static Group groupSmall;
	private static Composite composite;
	private static Text textGroup;
	private static Text textGroupSmall;
	private static Text textComposite;

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(400, 400);
		final GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginWidth = 5;
		gridLayout.marginHeight = 5;
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		shell.setLayout(gridLayout);

		createComposite(shell);

		createGroup(shell);
		createGroupSmall(shell);

		createButton(shell);

		// widget.shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private static void createGroup(final Composite parent) {
		group = new Group(parent, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		group.setLayout(new GridLayout(3, false));
		group.setText("A Group"); //$NON-NLS-1$

		final Label label = new Label(group, SWT.RIGHT);
		label.setLayoutData(new GridData(SWT.END, SWT.CENTER, false, false));
		label.setText("A Label"); //$NON-NLS-1$

		textGroup = new Text(group, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
		textGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		final Combo combo = new Combo(group, SWT.DROP_DOWN | SWT.READ_ONLY);
		combo.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		combo.setItems(new String[] { "A", "B", "C" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		combo.select(0);
	}
	
	private static void createGroupSmall(final Composite parent) {
		groupSmall = new Group(parent, SWT.NONE);
		groupSmall.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		groupSmall.setLayout(new GridLayout(1, false));
		groupSmall.setText("A Group with a very very long title"); //$NON-NLS-1$

		textGroupSmall = new Text(groupSmall, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
		textGroupSmall.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	}

	private static void createComposite(final Composite parent) {
		composite = new Composite(parent, SWT.BORDER);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		composite.setLayout(new GridLayout(3, false));

		final Label label = new Label(composite, SWT.RIGHT);
		label.setLayoutData(new GridData(SWT.END, SWT.CENTER, false, false));
		label.setText("A Label"); //$NON-NLS-1$

		textComposite = new Text(composite, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
		textComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		final Combo combo = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
		combo.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		combo.setItems(new String[] { "A", "B", "C" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		combo.select(0);
	}

	private static void createButton(final Composite parent) {
		final Button button = new Button(parent, SWT.PUSH);
		button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		button.setText("Compute Size"); //$NON-NLS-1$
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				textComposite.setText("" + composite.computeSize(SWT.DEFAULT, SWT.DEFAULT)); //$NON-NLS-1$
				textGroup.setText("" + group.computeSize(SWT.DEFAULT, SWT.DEFAULT)); //$NON-NLS-1$
				textGroupSmall.setText("" + groupSmall.computeSize(SWT.DEFAULT, SWT.DEFAULT)); //$NON-NLS-1$
				composite.pack();
				group.pack();
				groupSmall.pack();
			}
		});

	}
}
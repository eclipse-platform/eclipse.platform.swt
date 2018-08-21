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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

public class Bug517633_SpinnerDouble {

	int originalValue;

	Button apply;
	
	Spinner spinner;
	
	public Control createContent(Composite aParent) {
		Composite container = new Composite(aParent, SWT.NONE);
		container.setLayout(new GridLayout());
		spinner = new Spinner(container, SWT.BORDER);
		spinner.setValues(0, Integer.MIN_VALUE,	Integer.MAX_VALUE, 0, 1, 2);
		GridData spinnerLD = new GridData(SWT.LEFT, SWT.TOP, false, false);
		spinnerLD.widthHint = 50;
		spinner.setLayoutData(spinnerLD);
		Composite buttonsContainer = new Composite(aParent, SWT.NONE);
		buttonsContainer.setLayout(new GridLayout());
		buttonsContainer.setLayoutData(new GridData(SWT.RIGHT,SWT.BOTTOM, true, true));
		apply = new Button(buttonsContainer, SWT.NONE);
		apply.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false));
		apply.setText("Apply Changes");
		setInput(originalValue);
		spinner.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent aEvent) {
				updateButtons();
			}
			
		});
		return container;
	}
	
	public void setInput(int aValue) {
		originalValue = aValue;
		if ((spinner != null) && !spinner.isDisposed()) {
			spinner.setSelection(aValue);
			updateButtons();
		}
	}
	
	protected void updateButtons() {
		apply.setEnabled(spinner.getSelection() != originalValue);
	}
	
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());
		Bug517633_SpinnerDouble page = new Bug517633_SpinnerDouble();
		Control pageControl =page.createContent(shell);
		page.setInput(10003);
		GridData pageLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
		pageLayoutData.widthHint = 200;
		pageControl.setLayoutData(pageLayoutData);
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			display.readAndDispatch();
		}
	}
	
}

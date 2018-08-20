/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
package org.eclipse.swt.examples.addressbook;


import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

/* Imports */
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * DataEntryDialog class uses <code>org.eclipse.swt</code>
 * libraries to implement a dialog that accepts basic personal information that
 * is added to a <code>Table</code> widget or edits a <code>TableItem</code> entry
 * to represent the entered data.
 */
public class DataEntryDialog {

	private static ResourceBundle resAddressBook = ResourceBundle.getBundle("examples_addressbook");

	Shell shell;
	String[] values;
	String[] labels;

public DataEntryDialog(Shell parent) {
	shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
	shell.setLayout(new GridLayout());
}

private void addTextListener(final Text text) {
	text.addModifyListener(e -> {
		Integer index = (Integer)(text.getData("index"));
		values[index.intValue()] = text.getText();
	});
}
private void createControlButtons() {
	Composite composite = new Composite(shell, SWT.NONE);
	composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
	GridLayout layout = new GridLayout();
	layout.numColumns = 2;
	composite.setLayout(layout);

	Button okButton = new Button(composite, SWT.PUSH);
	okButton.setText(resAddressBook.getString("OK"));
	okButton.addSelectionListener(widgetSelectedAdapter(e -> shell.close()));

	Button cancelButton = new Button(composite, SWT.PUSH);
	cancelButton.setText(resAddressBook.getString("Cancel"));
	cancelButton.addSelectionListener(widgetSelectedAdapter(e -> {
		values = null;
		shell.close();
	}));

	shell.setDefaultButton(okButton);
}

private void createTextWidgets() {
	if (labels == null) return;

	Composite composite = new Composite(shell, SWT.NONE);
	composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	GridLayout layout= new GridLayout();
	layout.numColumns = 2;
	composite.setLayout(layout);

	if (values == null)
		values = new String[labels.length];

	for (int i = 0; i < labels.length; i++) {
		Label label = new Label(composite, SWT.RIGHT);
		label.setText(labels[i]);
		Text text = new Text(composite, SWT.BORDER);
		GridData gridData = new GridData();
		gridData.widthHint = 400;
		text.setLayoutData(gridData);
		if (values[i] != null) {
			text.setText(values[i]);
		}
		text.setData("index", Integer.valueOf(i));
		addTextListener(text);
	}
}

public String[] getLabels() {
	return labels;
}
public String getTitle() {
	return shell.getText();
}
/**
 * Returns the contents of the <code>Text</code> widgets in the dialog in a
 * <code>String</code> array.
 *
 * @return	String[]
 *			The contents of the text widgets of the dialog.
 *			May return null if all text widgets are empty.
 */
public String[] getValues() {
	return values;
}
/**
 * Opens the dialog in the given state.  Sets <code>Text</code> widget contents
 * and dialog behaviour accordingly.
 *
 * @param 	dialogState	int
 *					The state the dialog should be opened in.
 */
public String[] open() {
	createTextWidgets();
	createControlButtons();
	shell.pack();
	shell.open();
	Display display = shell.getDisplay();
	while(!shell.isDisposed()){
		if(!display.readAndDispatch())
			display.sleep();
	}

	return getValues();
}
public void setLabels(String[] labels) {
	this.labels = labels;
}
public void setTitle(String title) {
	shell.setText(title);
}
/**
 * Sets the values of the <code>Text</code> widgets of the dialog to
 * the values supplied in the parameter array.
 *
 * @param	itemInfo	String[]
 * 						The values to which the dialog contents will be set.
 */
public void setValues(String[] itemInfo) {
	if (labels == null) return;

	if (values == null)
		values = new String[labels.length];

	int numItems = Math.min(values.length, itemInfo.length);
	for(int i = 0; i < numItems; i++) {
		values[i] = itemInfo[i];
	}
}
}

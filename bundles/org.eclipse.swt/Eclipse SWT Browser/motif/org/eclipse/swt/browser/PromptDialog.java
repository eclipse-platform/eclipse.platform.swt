/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

class PromptDialog extends Dialog {
	
	public PromptDialog(Shell parent, int style) {
		super(parent, style);
	}
	
	public PromptDialog(Shell parent) {
		this(parent, 0);
	}
	
	public void confirmEx(String title, String text, String check, String button1, String button2, String button3, final int[] checkValue, final int[] result) {
		Shell parent = getParent();
		final Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText(title);
		GridLayout gridLayout = new GridLayout();
		shell.setLayout(gridLayout);
		Label label = new Label(shell, SWT.WRAP);
		label.setText(text);
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		label.setLayoutData (data);
		
		final Button[] buttons = new Button[4];
		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				if (buttons[0] != null) checkValue[0] = buttons[0].getSelection() ? 1 : 0;
				Widget widget = event.widget;
				for (int i = 1; i < buttons.length; i++) {
					if (widget == buttons[i]) {
						result[0] = i - 1;
						break;
					}
				}
				shell.close();
			}	
		};
		if (check != null) {
			buttons[0] = new Button(shell, SWT.CHECK);
			buttons[0].setText(check);
			buttons[0].setSelection(checkValue[0] != 0);
			data = new GridData ();
			data.horizontalAlignment = GridData.END;
			buttons[0].setLayoutData (data);
		}
		Composite composite = new Composite(shell, SWT.NONE);
		data = new GridData();
		data.horizontalAlignment = GridData.END;
		composite.setLayoutData (data);
		composite.setLayout(new RowLayout());
		if (button1 != null) {
			buttons[1] = new Button(composite, SWT.PUSH);
			buttons[1].setText(button1);
			buttons[1].addListener(SWT.Selection, listener);
		}
		if (button2 != null) {
			buttons[2] = new Button(composite, SWT.PUSH);
			buttons[2].setText(button2);
			buttons[2].addListener(SWT.Selection, listener);
		}
		if (button3 != null) {
			buttons[3] = new Button(composite, SWT.PUSH);
			buttons[3].setText(button3);
			buttons[3].addListener(SWT.Selection, listener);
		}
		shell.pack();
		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
	}
	
	public void prompt(String title, String text, String check, final String[] value, final int[] checkValue, final int[] result) {
		Shell parent = getParent();
		final Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		if (title != null) shell.setText(title);
		GridLayout gridLayout = new GridLayout();
		shell.setLayout(gridLayout);
		Label label = new Label(shell, SWT.WRAP);
		label.setText(text);
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		label.setLayoutData (data);

		final Text valueText = new Text(shell, SWT.BORDER);
		if (value[0] != null) valueText.setText(value[0]);
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		valueText.setLayoutData(data);

		final Button[] buttons = new Button[3];
		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				if (buttons[0] != null) checkValue[0] = buttons[0].getSelection() ? 1 : 0;
				value[0] = valueText.getText();
				result[0] = event.widget == buttons[1] ? 1 : 0;
				shell.close();
			}	
		};
		if (check != null) {
			buttons[0] = new Button(shell, SWT.CHECK);
			buttons[0].setText(check);
			buttons[0].setSelection(checkValue[0] != 0);
			data = new GridData ();
			data.horizontalAlignment = GridData.END;
			buttons[0].setLayoutData (data);
		}
		Composite composite = new Composite(shell, SWT.NONE);
		data = new GridData();
		data.horizontalAlignment = GridData.END;
		composite.setLayoutData (data);
		composite.setLayout(new GridLayout(2, true));
		buttons[1] = new Button(composite, SWT.PUSH);
		buttons[1].setText(SWT.getMessage("SWT_OK")); //$NON-NLS-1$
		buttons[1].setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		buttons[1].addListener(SWT.Selection, listener);
		buttons[2] = new Button(composite, SWT.PUSH);
		buttons[2].setText(SWT.getMessage("SWT_Cancel")); //$NON-NLS-1$
		buttons[2].setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		buttons[2].addListener(SWT.Selection, listener);

		shell.pack();
		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}	
	}

	public void promptUsernameAndPassword(String title, String text, String check, final String[] user, final String[] pass, final int[] checkValue, final int[] result) {
		Shell parent = getParent();
		final Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText(title);
		GridLayout gridLayout = new GridLayout();
		shell.setLayout(gridLayout);
		Label label = new Label(shell, SWT.WRAP);
		label.setText(text);
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		label.setLayoutData (data);
		
		Label userLabel = new Label(shell, SWT.NONE);
		userLabel.setText(SWT.getMessage("SWT_Username")); //$NON-NLS-1$
		
		final Text userText = new Text(shell, SWT.BORDER);
		if (user[0] != null) userText.setText(user[0]);
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		userText.setLayoutData(data);
		
		Label passwordLabel = new Label(shell, SWT.NONE);
		passwordLabel.setText(SWT.getMessage("SWT_Password")); //$NON-NLS-1$
		
		final Text passwordText = new Text(shell, SWT.PASSWORD | SWT.BORDER);
		if (pass[0] != null) passwordText.setText(pass[0]);
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		passwordText.setLayoutData(data);

		final Button[] buttons = new Button[3];
		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				if (buttons[0] != null) checkValue[0] = buttons[0].getSelection() ? 1 : 0;
				user[0] = userText.getText();
				pass[0] = passwordText.getText();
				result[0] = event.widget == buttons[1] ? 1 : 0;
				shell.close();
			}	
		};
		if (check != null) {
			buttons[0] = new Button(shell, SWT.CHECK);
			buttons[0].setText(check);
			buttons[0].setSelection(checkValue[0] != 0);
			data = new GridData ();
			buttons[0].setLayoutData (data);
		}
		Composite composite = new Composite(shell, SWT.NONE);
		data = new GridData();
		data.horizontalAlignment = GridData.END;
		composite.setLayoutData (data);
		composite.setLayout(new GridLayout(2, true));
		buttons[1] = new Button(composite, SWT.PUSH);
		buttons[1].setText(SWT.getMessage("SWT_OK")); //$NON-NLS-1$
		buttons[1].setLayoutData(new GridData (GridData.FILL_HORIZONTAL));
		buttons[1].addListener(SWT.Selection, listener);
		buttons[2] = new Button(composite, SWT.PUSH);
		buttons[2].setText(SWT.getMessage("SWT_Cancel")); //$NON-NLS-1$
		buttons[2].setLayoutData(new GridData (GridData.FILL_HORIZONTAL));
		buttons[2].addListener(SWT.Selection, listener);

		shell.setDefaultButton(buttons[1]);
		shell.pack();
		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
	}
}

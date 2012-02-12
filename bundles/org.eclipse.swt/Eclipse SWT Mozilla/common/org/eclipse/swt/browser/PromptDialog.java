/*******************************************************************************
 * Copyright (c) 2003, 2009 IBM Corporation and others.
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
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.Compatibility;
import org.eclipse.swt.internal.mozilla.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

class PromptDialog extends Dialog {
	
	PromptDialog(Shell parent, int style) {
		super(parent, style);
	}
	
	PromptDialog(Shell parent) {
		this(parent, 0);
	}
	
	void alertCheck(String title, String text, String check, final boolean[] checkValue) {
		Shell parent = getParent();
		final Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		if (title != null) shell.setText(title);
		GridLayout gridLayout = new GridLayout();
		shell.setLayout(gridLayout);
		Label label = new Label(shell, SWT.WRAP);
		label.setText(text);
		GridData data = new GridData();
		Monitor monitor = parent.getMonitor();
		int maxWidth = monitor.getBounds().width * 2 / 3;
		int width = label.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		data.widthHint = Math.min(width, maxWidth);
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		label.setLayoutData (data);

		final Button checkButton = check != null ? new Button(shell, SWT.CHECK) : null;
		if (checkButton != null) {
			checkButton.setText(check);
			checkButton.setSelection(checkValue[0]);
			data = new GridData ();
			data.horizontalAlignment = GridData.BEGINNING;
			checkButton.setLayoutData (data);
		}
		Button okButton = new Button(shell, SWT.PUSH);
		okButton.setText(SWT.getMessage("SWT_OK")); //$NON-NLS-1$
		data = new GridData ();
		data.horizontalAlignment = GridData.CENTER;
		okButton.setLayoutData (data);
		okButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (checkButton != null) checkValue[0] = checkButton.getSelection();
				shell.close();
			}
		});

		shell.pack();
		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
	}

	boolean invalidCert(final Browser browser, String message, String[] problems, final nsIX509Cert cert) {
		Shell parent = getParent();
		Display display = parent.getDisplay();
		Monitor monitor = parent.getMonitor();
		int maxWidth = monitor.getBounds().width * 2 / 3;
		final Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText(Compatibility.getMessage("SWT_InvalidCert_Title")); //$NON-NLS-1$
		shell.setLayout(new GridLayout());

		Composite messageComposite = new Composite(shell, SWT.NONE);
		messageComposite.setLayout(new GridLayout(2, false));
		Image image = display.getSystemImage(SWT.ICON_WARNING);
		new Label(messageComposite, SWT.NONE).setImage(image);
		Text text = new Text(messageComposite, SWT.WRAP);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		text.setEditable(false);
		text.setBackground(shell.getBackground());
		text.setText(message);
		int width = messageComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		GridData data = new GridData();
		data.widthHint = Math.min(width, maxWidth);
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		messageComposite.setLayoutData(data);

		StyledText problemsText = new StyledText(shell, SWT.WRAP);
		problemsText.setMargins(30, 0, 30, 0);
		problemsText.setEditable(false);
		problemsText.setBackground(shell.getBackground());
		for (int i = 0; i < problems.length; i++) {
			problemsText.append(problems[i] + '\n');
		}
		StyleRange style = new StyleRange();
		style.metrics = new GlyphMetrics(0, 0, 30);
		Bullet bullet0 = new Bullet (style);
		problemsText.setLineBullet(0, problems.length, bullet0);
		width = problemsText.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		data = new GridData();
		data.widthHint = Math.min(width, maxWidth);
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		problemsText.setLayoutData(data);

		text = new Text(shell, SWT.SINGLE);
		text.setEditable(false);
		text.setBackground(shell.getBackground());
		text.setText(Compatibility.getMessage("SWT_InvalidCert_Connect")); //$NON-NLS-1$

		new Label(shell, SWT.NONE); /* vertical spacer */

		/*
		* Create a local invisible Browser to be passed to the ViewCert call,
		* so that this prompter can be the certificate view dialog's parent.
		*/
		final Browser localBrowser = new Browser(shell, browser.getStyle());
		data = new GridData();
		data.exclude = true;
		localBrowser.setLayoutData(data);

		Composite buttonsComposite = new Composite(shell, SWT.NONE);
		buttonsComposite.setLayout(new GridLayout(3, true));
		buttonsComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

		Button viewCertButton = new Button(buttonsComposite, SWT.PUSH);
		viewCertButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		viewCertButton.setText(Compatibility.getMessage("SWT_ViewCertificate")); //$NON-NLS-1$
		viewCertButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				int /*long*/[] result = new int /*long*/[1];
				int rc = XPCOM.NS_GetServiceManager (result);
				if (rc != XPCOM.NS_OK) Mozilla.error (rc);
				if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);

				nsIServiceManager serviceManager = new nsIServiceManager(result[0]);
				result[0] = 0;
				byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_CERTIFICATEDIALOGS_CONTRACTID, true);
				rc = serviceManager.GetServiceByContractID (aContractID, nsICertificateDialogs.NS_ICERTIFICATEDIALOGS_IID, result);
				if (rc != XPCOM.NS_OK) Mozilla.error (rc);
				if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);
				serviceManager.Release();
				
				nsICertificateDialogs dialogs = new nsICertificateDialogs(result[0]);
				result[0] = 0;
				
				/*
				* Bug in Mozilla.  The certificate viewer dialog does not show its content when
				* opened.  The workaround is to periodically wake up the UI thread.
				*/
				Runnable runnable = new Runnable() {
					public void run() {
						browser.getDisplay().timerExec(1000, this);
					}
				};
				runnable.run();

				rc = ((Mozilla)localBrowser.webBrowser).webBrowser.GetContentDOMWindow(result);
				if (rc != XPCOM.NS_OK) Mozilla.error (rc);
				if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);
				
				nsIDOMWindow window = new nsIDOMWindow(result[0]);
				rc = dialogs.ViewCert(window.getAddress(), cert.getAddress());
				browser.getDisplay().timerExec(-1, runnable);
				window.Release();
				result[0] = 0;
				dialogs.Release();
			}
		});

		final Button okButton = new Button(buttonsComposite, SWT.PUSH);
		okButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		okButton.setText(Compatibility.getMessage("SWT_OK")); //$NON-NLS-1$
		Button cancelButton = new Button(buttonsComposite, SWT.PUSH);
		cancelButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		cancelButton.setText(Compatibility.getMessage("SWT_Cancel")); //$NON-NLS-1$
		final boolean[] result = new boolean[1];
		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				shell.dispose();
				result[0] = event.widget == okButton;
			}
		};
		okButton.addListener(SWT.Selection, listener);
		cancelButton.addListener(SWT.Selection, listener);

		cancelButton.setFocus();
		shell.setDefaultButton(cancelButton);
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		return result[0];
	}

	void confirmEx(String title, String text, String check, String button0, String button1, String button2, int defaultIndex, final boolean[] checkValue, final int[] result) {
		Shell parent = getParent();
		final Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText(title);
		GridLayout gridLayout = new GridLayout();
		shell.setLayout(gridLayout);
		Label label = new Label(shell, SWT.WRAP);
		label.setText(text);
		GridData data = new GridData();
		Monitor monitor = parent.getMonitor();
		int maxWidth = monitor.getBounds().width * 2 / 3;
		int width = label.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		data.widthHint = Math.min(width, maxWidth);
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		label.setLayoutData (data);

		final Button[] buttons = new Button[4];
		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				if (buttons[0] != null) checkValue[0] = buttons[0].getSelection();
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
			buttons[0].setSelection(checkValue[0]);
			data = new GridData ();
			data.horizontalAlignment = GridData.BEGINNING;
			buttons[0].setLayoutData (data);
		}
		Composite composite = new Composite(shell, SWT.NONE);
		data = new GridData();
		data.horizontalAlignment = GridData.CENTER;
		composite.setLayoutData (data);
		GridLayout layout = new GridLayout();
		layout.makeColumnsEqualWidth = true;
		composite.setLayout(layout);
		int buttonCount = 0;
		if (button0 != null) {
			buttons[1] = new Button(composite, SWT.PUSH);
			buttons[1].setText(button0);
			buttons[1].addListener(SWT.Selection, listener);
			buttons[1].setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			buttonCount++;
		}
		if (button1 != null) {
			buttons[2] = new Button(composite, SWT.PUSH);
			buttons[2].setText(button1);
			buttons[2].addListener(SWT.Selection, listener);
			buttons[2].setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			buttonCount++;
		}
		if (button2 != null) {
			buttons[3] = new Button(composite, SWT.PUSH);
			buttons[3].setText(button2);
			buttons[3].addListener(SWT.Selection, listener);
			buttons[3].setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			buttonCount++;
		}
		layout.numColumns = buttonCount;
		Button defaultButton = buttons [defaultIndex + 1];
		if (defaultButton != null) shell.setDefaultButton (defaultButton);

		shell.pack();
		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
	}
	
	void prompt(String title, String text, String check, final String[] value, final boolean[] checkValue, final boolean[] result) {
		Shell parent = getParent();
		final Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		if (title != null) shell.setText(title);
		GridLayout gridLayout = new GridLayout();
		shell.setLayout(gridLayout);
		Label label = new Label(shell, SWT.WRAP);
		label.setText(text);
		GridData data = new GridData();
		Monitor monitor = parent.getMonitor();
		int maxWidth = monitor.getBounds().width * 2 / 3;
		int width = label.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		data.widthHint = Math.min(width, maxWidth);
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		label.setLayoutData (data);
				
		final Text valueText = new Text(shell, SWT.BORDER);
		if (value[0] != null) valueText.setText(value[0]);
		data = new GridData();
		width = valueText.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		if (width > maxWidth) data.widthHint = maxWidth;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		valueText.setLayoutData(data);

		final Button[] buttons = new Button[3];
		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				if (buttons[0] != null) checkValue[0] = buttons[0].getSelection();
				value[0] = valueText.getText();
				result[0] = event.widget == buttons[1];
				shell.close();
			}	
		};
		if (check != null) {
			buttons[0] = new Button(shell, SWT.CHECK);
			buttons[0].setText(check);
			buttons[0].setSelection(checkValue[0]);
			data = new GridData ();
			data.horizontalAlignment = GridData.BEGINNING;
			buttons[0].setLayoutData (data);
		}
		Composite composite = new Composite(shell, SWT.NONE);
		data = new GridData();
		data.horizontalAlignment = GridData.CENTER;
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

	void promptUsernameAndPassword(String title, String text, String check, final String[] user, final String[] pass, final boolean[] checkValue, final boolean[] result) {
		Shell parent = getParent();
		final Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText(title);
		GridLayout gridLayout = new GridLayout();
		shell.setLayout(gridLayout);
		Label label = new Label(shell, SWT.WRAP);
		label.setText(text);
		GridData data = new GridData();
		Monitor monitor = parent.getMonitor();
		int maxWidth = monitor.getBounds().width * 2 / 3;
		int width = label.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		data.widthHint = Math.min(width, maxWidth);
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
				if (buttons[0] != null) checkValue[0] = buttons[0].getSelection();
				user[0] = userText.getText();
				pass[0] = passwordText.getText();
				result[0] = event.widget == buttons[1];
				shell.close();
			}	
		};
		if (check != null) {
			buttons[0] = new Button(shell, SWT.CHECK);
			buttons[0].setText(check);
			buttons[0].setSelection(checkValue[0]);
			data = new GridData ();
			data.horizontalAlignment = GridData.BEGINNING;
			buttons[0].setLayoutData (data);
		}
		Composite composite = new Composite(shell, SWT.NONE);
		data = new GridData();
		data.horizontalAlignment = GridData.CENTER;
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

		shell.setDefaultButton(buttons[1]);
		shell.pack();
		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
	}
}

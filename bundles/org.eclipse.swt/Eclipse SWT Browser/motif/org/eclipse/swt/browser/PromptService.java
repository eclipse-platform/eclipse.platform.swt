/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.mozilla.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

class PromptService {
	XPCOMObject supports;
	XPCOMObject promptService;
	int refCount = 0;

public PromptService() {
	createCOMInterfaces();
}

int AddRef() {
	refCount++;
	return refCount;
}

void createCOMInterfaces() {
	/* Create each of the interfaces that this object implements */
	supports = new XPCOMObject(new int[]{2, 0, 0}){
		public int method0(int[] args) {return queryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
	};
	
	promptService = new XPCOMObject(new int[]{2, 0, 0, 3, 5, 4, 6, 10, 7, 8, 7, 7}){
		public int method0(int[] args) {return queryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
		public int method3(int[] args) {return Alert(args[0], args[1], args[2]);}
		public int method4(int[] args) {return AlertCheck(args[0], args[1], args[2], args[3], args[4]);}
		public int method5(int[] args) {return Confirm(args[0], args[1], args[2], args[3]);}
		public int method6(int[] args) {return ConfirmCheck(args[0], args[1], args[2], args[3], args[4], args[5]);}
		public int method7(int[] args) {return ConfirmEx(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9]);}
		public int method8(int[] args) {return Prompt(args[0], args[1], args[2], args[3], args[4], args[5], args[6]);}
		public int method9(int[] args) {return PromptUsernameAndPassword(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);}
		public int method10(int[] args) {return PromptPassword(args[0], args[1], args[2], args[3], args[4], args[5], args[6]);}
		public int method11(int[] args) {return Select(args[0], args[1], args[2], args[3], args[4], args[5], args[6]);}
	};		
}

void disposeCOMInterfaces() {
	if (supports != null) {
		supports.dispose();
		supports = null;
	}	
	if (promptService != null) {
		promptService.dispose();
		promptService = null;	
	}
}

int getAddress() {
	return promptService.getAddress();
}

int queryInterface(int riid, int ppvObject) {
	if (riid == 0 || ppvObject == 0) return XPCOM.NS_ERROR_NO_INTERFACE;
	nsID guid = new nsID();
	XPCOM.memmove(guid, riid, nsID.sizeof);
	
	if (guid.Equals(nsISupports.NS_ISUPPORTS_IID)) {
		XPCOM.memmove(ppvObject, new int[] {supports.getAddress()}, 4);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals(nsIPromptService.NS_IPROMPTSERVICE_IID)) {
		XPCOM.memmove(ppvObject, new int[] {promptService.getAddress()}, 4);
		AddRef();
		return XPCOM.NS_OK;
	}
	
	XPCOM.memmove(ppvObject, new int[] {0}, 4);
	return XPCOM.NS_ERROR_NO_INTERFACE;
}
        	
int Release() {
	refCount--;
	if (refCount == 0) disposeCOMInterfaces();
	return refCount;
}

Browser getBrowser(int aDOMWindow) {
	int[] result = new int[1];
	int rc = XPCOM.NS_GetServiceManager(result);
	if (rc != XPCOM.NS_OK) Browser.error(rc);
	if (result[0] == 0) Browser.error(XPCOM.NS_NOINTERFACE);
	
	nsIServiceManager serviceManager = new nsIServiceManager(result[0]);
	result[0] = 0;
	byte[] buffer = XPCOM.NS_WINDOWWATCHER_CONTRACTID.getBytes();
	byte[] aContractID = new byte[buffer.length + 1];
	System.arraycopy(buffer, 0, aContractID, 0, buffer.length);
	rc = serviceManager.GetServiceByContractID(aContractID, nsIWindowWatcher.NS_IWINDOWWATCHER_IID, result);
	if (rc != XPCOM.NS_OK) Browser.error(rc);
	if (result[0] == 0) Browser.error(XPCOM.NS_NOINTERFACE);		
	serviceManager.Release();
	
	nsIWindowWatcher windowWatcher = new nsIWindowWatcher(result[0]);
	result[0] = 0;
	rc = windowWatcher.GetChromeForWindow(aDOMWindow, result);
	if (rc != XPCOM.NS_OK) Browser.error(rc);
	if (result[0] == 0) Browser.error(XPCOM.NS_NOINTERFACE);		
	windowWatcher.Release();	
	
	nsIWebBrowserChrome webBrowserChrome = new nsIWebBrowserChrome(result[0]);
	result[0] = 0;
	rc = webBrowserChrome.QueryInterface(nsIEmbeddingSiteWindow.NS_IEMBEDDINGSITEWINDOW_IID, result);
	if (rc != XPCOM.NS_OK) Browser.error(rc);
	if (result[0] == 0) Browser.error(XPCOM.NS_NOINTERFACE);		
	webBrowserChrome.Release();
	
	nsIEmbeddingSiteWindow embeddingSiteWindow = new nsIEmbeddingSiteWindow(result[0]);
	result[0] = 0;
	rc = embeddingSiteWindow.GetSiteWindow(result);
	if (rc != XPCOM.NS_OK) Browser.error(rc);
	if (result[0] == 0) Browser.error(XPCOM.NS_NOINTERFACE);		
	embeddingSiteWindow.Release();
	
	Display display = Display.getCurrent();
	Shell[] shells = display.getShells();
	Browser browser = null;
	for (int i = 0; i < shells.length; i++) {
		browser = Browser.findBrowser(shells[i], result[0]);
		if (browser != null) break;
	}
	return browser;
}

String getLabel(int buttonFlag, int index, int buttonTitle) {
	String label = null;
	int flag = (buttonFlag & (0xff * index)) / index;
	switch (flag) {
		case nsIPromptService.BUTTON_TITLE_CANCEL : label = SWT.getMessage("SWT_Cancel"); break;
		case nsIPromptService.BUTTON_TITLE_NO : label = SWT.getMessage("SWT_No"); break;
		case nsIPromptService.BUTTON_TITLE_OK : label = SWT.getMessage("SWT_OK"); break;
		case nsIPromptService.BUTTON_TITLE_SAVE : label = SWT.getMessage("SWT_Save"); break;
		case nsIPromptService.BUTTON_TITLE_YES : label = SWT.getMessage("SWT_Yes"); break;
		case nsIPromptService.BUTTON_TITLE_IS_STRING : {
			int length = XPCOM.nsCRT_strlen_PRUnichar(buttonTitle);
			char[] dest = new char[length];
			XPCOM.memmove(dest, buttonTitle, length * 2);
			label = new String(dest);
		}
	}
	return label;
}

/* nsIPromptService */

public int Alert(int parent, int dialogTitle, int text) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int AlertCheck(int parent, int dialogTitle, int text, int checkMsg, int checkValue) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int Confirm(int parent, int dialogTitle, int text, int _retval) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int ConfirmCheck(int parent, int dialogTitle, int text, int checkMsg, int checkValue, int _retval) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int ConfirmEx(int parent, int dialogTitle, int text, int buttonFlags, int button0Title, int button1Title, int button2Title, int checkMsg, int checkValue, int _retval) {
	Browser browser = getBrowser(parent);
	
	int length = XPCOM.nsCRT_strlen_PRUnichar(dialogTitle);
	char[] dest = new char[length];
	XPCOM.memmove(dest, dialogTitle, length * 2);
	String titleLabel = new String(dest);
	
	length = XPCOM.nsCRT_strlen_PRUnichar(text);
	dest = new char[length];
	XPCOM.memmove(dest, text, length * 2);
	String textLabel = new String(dest);
	
	String checkLabel = null;
	if (checkMsg != 0) {
		length = XPCOM.nsCRT_strlen_PRUnichar(checkMsg);
		dest = new char[length];
		XPCOM.memmove(dest, checkMsg, length * 2);
		checkLabel = new String(dest);
	}
	
	String button1Label = getLabel(buttonFlags, nsIPromptService.BUTTON_POS_0, button0Title);
	String button2Label = getLabel(buttonFlags, nsIPromptService.BUTTON_POS_1, button0Title);
	String button3Label = getLabel(buttonFlags, nsIPromptService.BUTTON_POS_2, button0Title);
	
	PromptDialog dialog = new PromptDialog(browser.getShell());
	int[] check = new int[1], result = new int[1];
	if (checkValue != 0) XPCOM.memmove(check, checkValue, 4);
	dialog.confirmEx(titleLabel, textLabel, checkLabel, button1Label, button2Label, button3Label, check, result);
	if (checkValue != 0) XPCOM.memmove(checkValue, check, 4);
	XPCOM.memmove(_retval, result, 4);
	return XPCOM.NS_OK;
}

public int Prompt(int parent, int dialogTitle, int text, int value, int checkMsg, int checkValue, int _retval) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int PromptUsernameAndPassword(int parent, int dialogTitle, int text, int username, int password, int checkMsg, int checkValue, int _retval) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int PromptPassword(int parent, int dialogTitle, int text, int password, int checkMsg, int checkValue, int _retval) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int Select(int parent, int dialogTitle, int text, int count, int selectList, int outSelection, int _retval) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

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
}
}
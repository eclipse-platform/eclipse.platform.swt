/*******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
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
import org.eclipse.swt.internal.mozilla.*;
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
	
	return Browser.findBrowser(result[0]); 
}

String getLabel(int buttonFlag, int index, int buttonTitle) {
	String label = null;
	int flag = (buttonFlag & (0xff * index)) / index;
	switch (flag) {
		case nsIPromptService.BUTTON_TITLE_CANCEL : label = SWT.getMessage("SWT_Cancel"); break; //$NON-NLS-1$
		case nsIPromptService.BUTTON_TITLE_NO : label = SWT.getMessage("SWT_No"); break; //$NON-NLS-1$
		case nsIPromptService.BUTTON_TITLE_OK : label = SWT.getMessage("SWT_OK"); break; //$NON-NLS-1$
		case nsIPromptService.BUTTON_TITLE_SAVE : label = SWT.getMessage("SWT_Save"); break; //$NON-NLS-1$
		case nsIPromptService.BUTTON_TITLE_YES : label = SWT.getMessage("SWT_Yes"); break; //$NON-NLS-1$
		case nsIPromptService.BUTTON_TITLE_IS_STRING : {
			int length = XPCOM.strlen_PRUnichar(buttonTitle);
			char[] dest = new char[length];
			XPCOM.memmove(dest, buttonTitle, length * 2);
			label = new String(dest);
		}
	}
	return label;
}

/* nsIPromptService */

public int Alert(int parent, int dialogTitle, int text) {
	Browser browser = getBrowser(parent);
	
	int length = XPCOM.strlen_PRUnichar(dialogTitle);
	char[] dest = new char[length];
	XPCOM.memmove(dest, dialogTitle, length * 2);
	String titleLabel = new String(dest);

	length = XPCOM.strlen_PRUnichar(text);
	dest = new char[length];
	XPCOM.memmove(dest, text, length * 2);
	String textLabel = new String(dest);

	MessageBox messageBox = new MessageBox(browser.getShell(), SWT.OK | SWT.ICON_WARNING);
	messageBox.setText(titleLabel);
	messageBox.setMessage(textLabel);
	messageBox.open();
	return XPCOM.NS_OK;
}

public int AlertCheck(int parent, int dialogTitle, int text, int checkMsg, int checkValue) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int Confirm(int parent, int dialogTitle, int text, int _retval) {
	Browser browser = getBrowser(parent);
	
	int length = XPCOM.strlen_PRUnichar(dialogTitle);
	char[] dest = new char[length];
	XPCOM.memmove(dest, dialogTitle, length * 2);
	String titleLabel = new String(dest);

	length = XPCOM.strlen_PRUnichar(text);
	dest = new char[length];
	XPCOM.memmove(dest, text, length * 2);
	String textLabel = new String(dest);

	MessageBox messageBox = new MessageBox(browser.getShell(), SWT.OK | SWT.CANCEL | SWT.ICON_QUESTION);
	messageBox.setText(titleLabel);
	messageBox.setMessage(textLabel);
	int id = messageBox.open();
	int[] result = { id == SWT.OK ? 1 : 0};
	XPCOM.memmove(_retval, result, 4);
	return XPCOM.NS_OK;
}

public int ConfirmCheck(int parent, int dialogTitle, int text, int checkMsg, int checkValue, int _retval) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int ConfirmEx(int parent, int dialogTitle, int text, int buttonFlags, int button0Title, int button1Title, int button2Title, int checkMsg, int checkValue, int _retval) {
	Browser browser = getBrowser(parent);
	
	int length = XPCOM.strlen_PRUnichar(dialogTitle);
	char[] dest = new char[length];
	XPCOM.memmove(dest, dialogTitle, length * 2);
	String titleLabel = new String(dest);
	
	length = XPCOM.strlen_PRUnichar(text);
	dest = new char[length];
	XPCOM.memmove(dest, text, length * 2);
	String textLabel = new String(dest);
	
	String checkLabel = null;
	if (checkMsg != 0) {
		length = XPCOM.strlen_PRUnichar(checkMsg);
		dest = new char[length];
		XPCOM.memmove(dest, checkMsg, length * 2);
		checkLabel = new String(dest);
	}
	
	String button1Label = getLabel(buttonFlags, nsIPromptService.BUTTON_POS_0, button0Title);
	String button2Label = getLabel(buttonFlags, nsIPromptService.BUTTON_POS_1, button1Title);
	String button3Label = getLabel(buttonFlags, nsIPromptService.BUTTON_POS_2, button2Title);
	
	PromptDialog dialog = new PromptDialog(browser.getShell());
	int[] check = new int[1], result = new int[1];
	if (checkValue != 0) XPCOM.memmove(check, checkValue, 4);
	dialog.confirmEx(titleLabel, textLabel, checkLabel, button1Label, button2Label, button3Label, check, result);
	if (checkValue != 0) XPCOM.memmove(checkValue, check, 4);
	XPCOM.memmove(_retval, result, 4);
	return XPCOM.NS_OK;
}

public int Prompt(int parent, int dialogTitle, int text, int value, int checkMsg, int checkValue, int _retval) {
	Browser browser = getBrowser(parent);
	String titleLabel = null, textLabel, checkLabel = null;
	String[] valueLabel = new String[1];
	char[] dest;
	int length;
	if (dialogTitle != 0) {
		length = XPCOM.strlen_PRUnichar(dialogTitle);
		dest = new char[length];
		XPCOM.memmove(dest, dialogTitle, length * 2);
		titleLabel = new String(dest);
	}
	
	length = XPCOM.strlen_PRUnichar(text);
	dest = new char[length];
	XPCOM.memmove(dest, text, length * 2);
	textLabel = new String(dest);
	
	int [] valueAddr = new int [1];
	XPCOM.memmove(valueAddr, value, 4);
	if (valueAddr[0] != 0) {
		length = XPCOM.strlen_PRUnichar(valueAddr[0]);
		dest = new char[length];
		XPCOM.memmove(dest, valueAddr[0], length * 2);
		valueLabel[0] = new String(dest);		
	}
	
	if (checkMsg != 0) {
		length = XPCOM.strlen_PRUnichar(checkMsg);
		dest = new char[length];
		XPCOM.memmove(dest, checkMsg, length * 2);
		checkLabel = new String(dest);
	}
	
	PromptDialog dialog = new PromptDialog(browser.getShell());
	int[] check = new int[1], result = new int[1];
	if (checkValue != 0) XPCOM.memmove(check, checkValue, 4);
	dialog.prompt(titleLabel, textLabel, checkLabel, valueLabel, check, result);

	XPCOM.memmove(_retval, result, 4);
	if (result[0] == 1) {
		/* 
		* User selected OK. User name and password are returned as PRUnichar values. Any default
		* value that we override must be freed using the nsIMemory service.
		*/
		int cnt, size;
		int ptr;
		char[] buffer;
		int [] result2 = new int [1];
		if (valueLabel[0] != null) {
			cnt = valueLabel[0].length();
			buffer = new char[cnt + 1];
			valueLabel[0].getChars(0, cnt, buffer, 0);
			size = buffer.length * 2;
			ptr = XPCOM.PR_Malloc(size);
			XPCOM.memmove(ptr, buffer, size);
			XPCOM.memmove(value, new int [] {ptr}, 4);

			if (valueAddr[0] != 0) {
				int rc = XPCOM.NS_GetServiceManager(result2);
				if (rc != XPCOM.NS_OK) SWT.error(rc);
				if (result2[0] == 0) SWT.error(XPCOM.NS_NOINTERFACE);
			
				nsIServiceManager serviceManager = new nsIServiceManager(result2[0]);
				result2[0] = 0;
				byte[] tmp = XPCOM.NS_MEMORY_CONTRACTID.getBytes();
				byte[] aContractID = new byte[tmp.length + 1];
				System.arraycopy(tmp, 0, aContractID, 0, tmp.length);
				rc = serviceManager.GetServiceByContractID(aContractID, nsIMemory.NS_IMEMORY_IID, result2);
				if (rc != XPCOM.NS_OK) SWT.error(rc);
				if (result2[0] == 0) SWT.error(XPCOM.NS_NOINTERFACE);		
				serviceManager.Release();
				
				nsIMemory memory = new nsIMemory(result2[0]);
				result2[0] = 0;
				memory.Free(valueAddr[0]);
				memory.Release();
			}
		}
	}
	if (checkValue != 0) XPCOM.memmove(checkValue, check, 4);
	return XPCOM.NS_OK;
}

public int PromptUsernameAndPassword(int parent, int dialogTitle, int text, int username, int password, int checkMsg, int checkValue, int _retval) {
	Browser browser = getBrowser(parent);
	String titleLabel, textLabel, checkLabel = null;
	String[] userLabel = new String[1], passLabel = new String[1];
	char[] dest;
	int length;
	if (dialogTitle != 0) {
		length = XPCOM.strlen_PRUnichar(dialogTitle);
		dest = new char[length];
		XPCOM.memmove(dest, dialogTitle, length * 2);
		titleLabel = new String(dest);
	} else {
		titleLabel = "";	//$NON-NLS-1$
	}
	
	length = XPCOM.strlen_PRUnichar(text);
	dest = new char[length];
	XPCOM.memmove(dest, text, length * 2);
	textLabel = new String(dest);
	
	int[] userAddr = new int[1];
	XPCOM.memmove(userAddr, username, 4);
	if (userAddr[0] != 0) {
		length = XPCOM.strlen_PRUnichar(userAddr[0]);
		dest = new char[length];
		XPCOM.memmove(dest, userAddr[0], length * 2);
		userLabel[0] = new String(dest);		
	}
	
	int[] passAddr = new int[1];
	XPCOM.memmove(passAddr, password, 4);
	if (passAddr[0] != 0) {
		length = XPCOM.strlen_PRUnichar(passAddr[0]);
		dest = new char[length];
		XPCOM.memmove(dest, passAddr[0], length * 2);
		passLabel[0] = new String(dest);		
	}
	
	if (checkMsg != 0) {
		length = XPCOM.strlen_PRUnichar(checkMsg);
		dest = new char[length];
		XPCOM.memmove(dest, checkMsg, length * 2);
		checkLabel = new String(dest);
	}
	
	PromptDialog dialog = new PromptDialog(browser.getShell());
	int[] check = new int[1], result = new int[1];
	if (checkValue != 0) XPCOM.memmove(check, checkValue, 4);
	dialog.promptUsernameAndPassword(titleLabel, textLabel, checkLabel, userLabel, passLabel, check, result);

	XPCOM.memmove(_retval, result, 4);
	if (result[0] == 1) {
		/* 
		* User selected OK. User name and password are returned as PRUnichar values. Any default
		* value that we override must be freed using the nsIMemory service.
		*/
		int cnt, size, ptr;
		char[] buffer;
		if (userLabel[0] != null) {
			cnt = userLabel[0].length();
			buffer = new char[cnt + 1];
			userLabel[0].getChars(0, cnt, buffer, 0);
			size = buffer.length * 2;
			ptr = XPCOM.PR_Malloc(size);
			XPCOM.memmove(ptr, buffer, size);
			XPCOM.memmove(username, new int[] {ptr}, 4);

			if (userAddr[0] != 0) {
				result[0] = 0;
				int rc = XPCOM.NS_GetServiceManager(result);
				if (rc != XPCOM.NS_OK) SWT.error(rc);
				if (result[0] == 0) SWT.error(XPCOM.NS_NOINTERFACE);
			
				nsIServiceManager serviceManager = new nsIServiceManager(result[0]);
				result[0] = 0;
				byte[] tmp = XPCOM.NS_MEMORY_CONTRACTID.getBytes();
				byte[] aContractID = new byte[tmp.length + 1];
				System.arraycopy(tmp, 0, aContractID, 0, tmp.length);
				rc = serviceManager.GetServiceByContractID(aContractID, nsIMemory.NS_IMEMORY_IID, result);
				if (rc != XPCOM.NS_OK) SWT.error(rc);
				if (result[0] == 0) SWT.error(XPCOM.NS_NOINTERFACE);		
				serviceManager.Release();
				
				nsIMemory memory = new nsIMemory(result[0]);
				result[0] = 0;
				memory.Free(userAddr[0]);
				memory.Release();
			}
		}
		if (passLabel[0] != null) {
			cnt = passLabel[0].length();
			buffer = new char[cnt + 1];
			passLabel[0].getChars(0, cnt, buffer, 0);
			size = buffer.length * 2;
			ptr = XPCOM.PR_Malloc(size);
			XPCOM.memmove(ptr, buffer, size);
			XPCOM.memmove(password, new int[] {ptr}, 4);
			
			if (passAddr[0] != 0) {
				result[0] = 0;
				int rc = XPCOM.NS_GetServiceManager(result);
				if (rc != XPCOM.NS_OK) SWT.error(rc);
				if (result[0] == 0) SWT.error(XPCOM.NS_NOINTERFACE);
			
				nsIServiceManager serviceManager = new nsIServiceManager(result[0]);
				result[0] = 0;
				byte[] tmp = XPCOM.NS_MEMORY_CONTRACTID.getBytes();
				byte[] aContractID = new byte[tmp.length + 1];
				System.arraycopy(tmp, 0, aContractID, 0, tmp.length);
				rc = serviceManager.GetServiceByContractID(aContractID, nsIMemory.NS_IMEMORY_IID, result);
				if (rc != XPCOM.NS_OK) SWT.error(rc);
				if (result[0] == 0) SWT.error(XPCOM.NS_NOINTERFACE);		
				serviceManager.Release();
				
				nsIMemory memory = new nsIMemory(result[0]);
				result[0] = 0;
				memory.Free(passAddr[0]);
				memory.Release();
			}
		}
	}
	if (checkValue != 0) XPCOM.memmove(checkValue, check, 4);
	return XPCOM.NS_OK;
}

public int PromptPassword(int parent, int dialogTitle, int text, int password, int checkMsg, int checkValue, int _retval) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int Select(int parent, int dialogTitle, int text, int count, int selectList, int outSelection, int _retval) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
}

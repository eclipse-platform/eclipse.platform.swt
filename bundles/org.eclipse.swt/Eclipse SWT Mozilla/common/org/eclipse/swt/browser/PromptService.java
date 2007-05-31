/*******************************************************************************
 * Copyright (c) 2003, 2007 IBM Corporation and others.
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
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.mozilla.*;
import org.eclipse.swt.widgets.*;

class PromptService {
	XPCOMObject supports;
	XPCOMObject promptService;
	int refCount = 0;

public PromptService () {
	createCOMInterfaces ();
}

int AddRef () {
	refCount++;
	return refCount;
}

void createCOMInterfaces () {
	/* Create each of the interfaces that this object implements */
	supports = new XPCOMObject (new int[] {2, 0, 0}) {
		public int /*long*/ method0 (int /*long*/[] args) {return QueryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
	};
	
	promptService = new XPCOMObject (new int[] {2, 0, 0, 3, 5, 4, 6, 10, 7, 8, 7, 7}) {
		public int /*long*/ method0 (int /*long*/[] args) {return QueryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
		public int /*long*/ method3 (int /*long*/[] args) {return Alert (args[0], args[1], args[2]);}
		public int /*long*/ method4 (int /*long*/[] args) {return AlertCheck (args[0], args[1], args[2], args[3], args[4]);}
		public int /*long*/ method5 (int /*long*/[] args) {return Confirm (args[0], args[1], args[2], args[3]);}
		public int /*long*/ method6 (int /*long*/[] args) {return ConfirmCheck (args[0], args[1], args[2], args[3], args[4], args[5]);}
		public int /*long*/ method7 (int /*long*/[] args) {return ConfirmEx (args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9]);}
		public int /*long*/ method8 (int /*long*/[] args) {return Prompt (args[0], args[1], args[2], args[3], args[4], args[5], args[6]);}
		public int /*long*/ method9 (int /*long*/[] args) {return PromptUsernameAndPassword (args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);}
		public int /*long*/ method10 (int /*long*/[] args) {return PromptPassword (args[0], args[1], args[2], args[3], args[4], args[5], args[6]);}
		public int /*long*/ method11 (int /*long*/[] args) {return Select (args[0], args[1], args[2], args[3], args[4], args[5], args[6]);}
	};		
}

void disposeCOMInterfaces () {
	if (supports != null) {
		supports.dispose ();
		supports = null;
	}	
	if (promptService != null) {
		promptService.dispose ();
		promptService = null;	
	}
}

int /*long*/ getAddress () {
	return promptService.getAddress ();
}

int /*long*/ QueryInterface (int /*long*/ riid, int /*long*/ ppvObject) {
	if (riid == 0 || ppvObject == 0) return XPCOM.NS_ERROR_NO_INTERFACE;
	nsID guid = new nsID ();
	XPCOM.memmove (guid, riid, nsID.sizeof);
	
	if (guid.Equals (nsISupports.NS_ISUPPORTS_IID)) {
		XPCOM.memmove (ppvObject, new int /*long*/[] {supports.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (nsIPromptService.NS_IPROMPTSERVICE_IID)) {
		XPCOM.memmove (ppvObject, new int /*long*/[] {promptService.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	
	XPCOM.memmove (ppvObject, new int /*long*/[] {0}, C.PTR_SIZEOF);
	return XPCOM.NS_ERROR_NO_INTERFACE;
}
        	
int Release () {
	refCount--;
	if (refCount == 0) disposeCOMInterfaces ();
	return refCount;
}

Browser getBrowser (int /*long*/ aDOMWindow) {
	if (aDOMWindow == 0) return null;

	int /*long*/[] result = new int /*long*/[1];
	int rc = XPCOM.NS_GetServiceManager (result);
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);
	
	nsIServiceManager serviceManager = new nsIServiceManager (result[0]);
	result[0] = 0;
	byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_WINDOWWATCHER_CONTRACTID, true);
	rc = serviceManager.GetServiceByContractID (aContractID, nsIWindowWatcher.NS_IWINDOWWATCHER_IID, result);
	if (rc != XPCOM.NS_OK) Mozilla.error(rc);
	if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);		
	serviceManager.Release ();
	
	nsIWindowWatcher windowWatcher = new nsIWindowWatcher (result[0]);
	result[0] = 0;
	/* the chrome will only be answered for the top-level nsIDOMWindow */
	nsIDOMWindow window = new nsIDOMWindow (aDOMWindow);
	rc = window.GetTop (result);
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);
	aDOMWindow = result[0];
	result[0] = 0;
	rc = windowWatcher.GetChromeForWindow (aDOMWindow, result);
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);		
	windowWatcher.Release ();	
	
	nsIWebBrowserChrome webBrowserChrome = new nsIWebBrowserChrome (result[0]);
	result[0] = 0;
	rc = webBrowserChrome.QueryInterface (nsIEmbeddingSiteWindow.NS_IEMBEDDINGSITEWINDOW_IID, result);
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);		
	webBrowserChrome.Release ();
	
	nsIEmbeddingSiteWindow embeddingSiteWindow = new nsIEmbeddingSiteWindow (result[0]);
	result[0] = 0;
	rc = embeddingSiteWindow.GetSiteWindow (result);
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);		
	embeddingSiteWindow.Release ();
	
	return Mozilla.findBrowser (result[0]); 
}

String getLabel (int buttonFlag, int index, int /*long*/ buttonTitle) {
	String label = null;
	int flag = (buttonFlag & (0xff * index)) / index;
	switch (flag) {
		case nsIPromptService.BUTTON_TITLE_CANCEL : label = SWT.getMessage ("SWT_Cancel"); break; //$NON-NLS-1$
		case nsIPromptService.BUTTON_TITLE_NO : label = SWT.getMessage ("SWT_No"); break; //$NON-NLS-1$
		case nsIPromptService.BUTTON_TITLE_OK : label = SWT.getMessage ("SWT_OK"); break; //$NON-NLS-1$
		case nsIPromptService.BUTTON_TITLE_SAVE : label = SWT.getMessage ("SWT_Save"); break; //$NON-NLS-1$
		case nsIPromptService.BUTTON_TITLE_YES : label = SWT.getMessage ("SWT_Yes"); break; //$NON-NLS-1$
		case nsIPromptService.BUTTON_TITLE_IS_STRING : {
			int length = XPCOM.strlen_PRUnichar (buttonTitle);
			char[] dest = new char[length];
			XPCOM.memmove (dest, buttonTitle, length * 2);
			label = new String (dest);
		}
	}
	return label;
}

/* nsIPromptService */

public int /*long*/ Alert (int /*long*/ aParent, int /*long*/ aDialogTitle, int /*long*/ aText) {
	Browser browser = getBrowser (aParent);
	
	int length = XPCOM.strlen_PRUnichar (aDialogTitle);
	char[] dest = new char[length];
	XPCOM.memmove (dest, aDialogTitle, length * 2);
	String titleLabel = new String (dest);

	length = XPCOM.strlen_PRUnichar (aText);
	dest = new char[length];
	XPCOM.memmove (dest, aText, length * 2);
	String textLabel = new String (dest);

	Shell shell = browser == null ? new Shell () : browser.getShell (); 
	MessageBox messageBox = new MessageBox (shell, SWT.OK | SWT.ICON_WARNING);
	messageBox.setText (titleLabel);
	messageBox.setMessage (textLabel);
	messageBox.open ();
	return XPCOM.NS_OK;
}

public int /*long*/ AlertCheck (int /*long*/ aParent, int /*long*/ aDialogTitle, int /*long*/ aText, int /*long*/ aCheckMsg, int /*long*/ aCheckState) {
	Browser browser = getBrowser (aParent);
	
	int length = XPCOM.strlen_PRUnichar (aDialogTitle);
	char[] dest = new char[length];
	XPCOM.memmove (dest, aDialogTitle, length * 2);
	String titleLabel = new String (dest);

	length = XPCOM.strlen_PRUnichar (aText);
	dest = new char[length];
	XPCOM.memmove (dest, aText, length * 2);
	String textLabel = new String (dest);

	length = XPCOM.strlen_PRUnichar (aCheckMsg);
	dest = new char[length];
	XPCOM.memmove (dest, aCheckMsg, length * 2);
	String checkLabel = new String (dest);

	Shell shell = browser == null ? new Shell () : browser.getShell ();
	PromptDialog dialog = new PromptDialog (shell);
	int[] check = new int[1];
	if (aCheckState != 0) XPCOM.memmove (check, aCheckState, 4); /* PRBool */
	dialog.alertCheck (titleLabel, textLabel, checkLabel, check);
	if (aCheckState != 0) XPCOM.memmove (aCheckState, check, 4); /* PRBool */
	return XPCOM.NS_OK;
}

public int /*long*/ Confirm (int /*long*/ aParent, int /*long*/ aDialogTitle, int /*long*/ aText, int /*long*/ _retval) {
	Browser browser = getBrowser (aParent);
	
	int length = XPCOM.strlen_PRUnichar (aDialogTitle);
	char[] dest = new char[length];
	XPCOM.memmove (dest, aDialogTitle, length * 2);
	String titleLabel = new String (dest);

	length = XPCOM.strlen_PRUnichar (aText);
	dest = new char[length];
	XPCOM.memmove (dest, aText, length * 2);
	String textLabel = new String (dest);

	Shell shell = browser == null ? new Shell () : browser.getShell ();
	MessageBox messageBox = new MessageBox (shell, SWT.OK | SWT.CANCEL | SWT.ICON_QUESTION);
	messageBox.setText (titleLabel);
	messageBox.setMessage (textLabel);
	int id = messageBox.open ();
	int[] result = {id == SWT.OK ? 1 : 0};
	XPCOM.memmove (_retval, result, 4);
	return XPCOM.NS_OK;
}

public int /*long*/ ConfirmCheck (int /*long*/ aParent, int /*long*/ aDialogTitle, int /*long*/ aText, int /*long*/ aCheckMsg, int /*long*/ aCheckState, int /*long*/ _retval) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int /*long*/ ConfirmEx (int /*long*/ aParent, int /*long*/ aDialogTitle, int /*long*/ aText, int /*long*/ aButtonFlags, int /*long*/ aButton0Title, int /*long*/ aButton1Title, int /*long*/ aButton2Title, int /*long*/ aCheckMsg, int /*long*/ aCheckState, int /*long*/ _retval) {
	Browser browser = getBrowser (aParent);
	
	int length = XPCOM.strlen_PRUnichar (aDialogTitle);
	char[] dest = new char[length];
	XPCOM.memmove (dest, aDialogTitle, length * 2);
	String titleLabel = new String (dest);

	length = XPCOM.strlen_PRUnichar (aText);
	dest = new char[length];
	XPCOM.memmove (dest, aText, length * 2);
	String textLabel = new String (dest);
	
	String checkLabel = null;
	if (aCheckMsg != 0) {
		length = XPCOM.strlen_PRUnichar (aCheckMsg);
		dest = new char[length];
		XPCOM.memmove (dest, aCheckMsg, length * 2);
		checkLabel = new String (dest);
	}
	
	String button0Label = getLabel ((int)/*64*/aButtonFlags, nsIPromptService.BUTTON_POS_0, aButton0Title);
	String button1Label = getLabel ((int)/*64*/aButtonFlags, nsIPromptService.BUTTON_POS_1, aButton1Title);
	String button2Label = getLabel ((int)/*64*/aButtonFlags, nsIPromptService.BUTTON_POS_2, aButton2Title);
	
	int defaultIndex = 0;
	if ((aButtonFlags & nsIPromptService.BUTTON_POS_1_DEFAULT) != 0) {
		defaultIndex = 1;
	} else if ((aButtonFlags & nsIPromptService.BUTTON_POS_2_DEFAULT) != 0) {
		defaultIndex = 2;
	}
	
	Shell shell = browser == null ? new Shell () : browser.getShell ();
	PromptDialog dialog = new PromptDialog (shell);
	int[] check = new int[1], result = new int[1];
	if (aCheckState != 0) XPCOM.memmove (check, aCheckState, 4);
	dialog.confirmEx (titleLabel, textLabel, checkLabel, button0Label, button1Label, button2Label, defaultIndex, check, result);
	if (aCheckState != 0) XPCOM.memmove (aCheckState, check, 4);
	XPCOM.memmove (_retval, result, 4);
	return XPCOM.NS_OK;
}

public int /*long*/ Prompt (int /*long*/ aParent, int /*long*/ aDialogTitle, int /*long*/ aText, int /*long*/ aValue, int /*long*/ aCheckMsg, int /*long*/ aCheckState, int /*long*/ _retval) {
	Browser browser = getBrowser (aParent);
	String titleLabel = null, textLabel, checkLabel = null;
	String[] valueLabel = new String[1];
	char[] dest;
	int length;
	if (aDialogTitle != 0) {
		length = XPCOM.strlen_PRUnichar (aDialogTitle);
		dest = new char[length];
		XPCOM.memmove (dest, aDialogTitle, length * 2);
		titleLabel = new String (dest);
	}
	
	length = XPCOM.strlen_PRUnichar (aText);
	dest = new char[length];
	XPCOM.memmove (dest, aText, length * 2);
	textLabel = new String (dest);
	
	int /*long*/[] valueAddr = new int /*long*/[1];
	XPCOM.memmove (valueAddr, aValue, C.PTR_SIZEOF);
	if (valueAddr[0] != 0) {
		length = XPCOM.strlen_PRUnichar (valueAddr[0]);
		dest = new char[length];
		XPCOM.memmove (dest, valueAddr[0], length * 2);
		valueLabel[0] = new String (dest);		
	}
	
	if (aCheckMsg != 0) {
		length = XPCOM.strlen_PRUnichar (aCheckMsg);
		if (length > 0) {
			dest = new char[length];
			XPCOM.memmove (dest, aCheckMsg, length * 2);
			checkLabel = new String (dest);
		}
	}

	Shell shell = browser == null ? new Shell () : browser.getShell ();
	PromptDialog dialog = new PromptDialog (shell);
	int[] check = new int[1], result = new int[1];
	if (aCheckState != 0) XPCOM.memmove (check, aCheckState, 4);
	dialog.prompt (titleLabel, textLabel, checkLabel, valueLabel, check, result);

	XPCOM.memmove (_retval, result, 4);
	if (result[0] == 1) {
		/* 
		* User selected OK. User name and password are returned as PRUnichar values. Any default
		* value that we override must be freed using the nsIMemory service.
		*/
		int cnt, size;
		int /*long*/ ptr;
		char[] buffer;
		int /*long*/[] result2 = new int /*long*/[1];
		if (valueLabel[0] != null) {
			cnt = valueLabel[0].length ();
			buffer = new char[cnt + 1];
			valueLabel[0].getChars (0, cnt, buffer, 0);
			size = buffer.length * 2;
			ptr = C.malloc (size);
			XPCOM.memmove (ptr, buffer, size);
			XPCOM.memmove (aValue, new int /*long*/[] {ptr}, C.PTR_SIZEOF);

			if (valueAddr[0] != 0) {
				int rc = XPCOM.NS_GetServiceManager (result2);
				if (rc != XPCOM.NS_OK) SWT.error (rc);
				if (result2[0] == 0) SWT.error (XPCOM.NS_NOINTERFACE);
			
				nsIServiceManager serviceManager = new nsIServiceManager (result2[0]);
				result2[0] = 0;
				byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_MEMORY_CONTRACTID, true);
				rc = serviceManager.GetServiceByContractID (aContractID, nsIMemory.NS_IMEMORY_IID, result2);
				if (rc != XPCOM.NS_OK) SWT.error (rc);
				if (result2[0] == 0) SWT.error (XPCOM.NS_NOINTERFACE);		
				serviceManager.Release ();
				
				nsIMemory memory = new nsIMemory (result2[0]);
				result2[0] = 0;
				memory.Free (valueAddr[0]);
				memory.Release ();
			}
		}
	}
	if (aCheckState != 0) XPCOM.memmove (aCheckState, check, 4);
	return XPCOM.NS_OK;
}

public int /*long*/ PromptUsernameAndPassword (int /*long*/ aParent, int /*long*/ aDialogTitle, int /*long*/ aText, int /*long*/ aUsername, int /*long*/ aPassword, int /*long*/ aCheckMsg, int /*long*/ aCheckState, int /*long*/ _retval) {
	Browser browser = getBrowser (aParent);
	String titleLabel, textLabel, checkLabel = null;
	String[] userLabel = new String[1], passLabel = new String[1];
	char[] dest;
	int length;
	if (aDialogTitle != 0) {
		length = XPCOM.strlen_PRUnichar (aDialogTitle);
		dest = new char[length];
		XPCOM.memmove (dest, aDialogTitle, length * 2);
		titleLabel = new String (dest);
	} else {
		titleLabel = "";	//$NON-NLS-1$
	}
	
	length = XPCOM.strlen_PRUnichar (aText);
	dest = new char[length];
	XPCOM.memmove (dest, aText, length * 2);
	textLabel = new String (dest);
	
	int /*long*/[] userAddr = new int /*long*/[1];
	XPCOM.memmove (userAddr, aUsername, C.PTR_SIZEOF);
	if (userAddr[0] != 0) {
		length = XPCOM.strlen_PRUnichar (userAddr[0]);
		dest = new char[length];
		XPCOM.memmove (dest, userAddr[0], length * 2);
		userLabel[0] = new String (dest);		
	}
	
	int /*long*/[] passAddr = new int /*long*/[1];
	XPCOM.memmove (passAddr, aPassword, C.PTR_SIZEOF);
	if (passAddr[0] != 0) {
		length = XPCOM.strlen_PRUnichar (passAddr[0]);
		dest = new char[length];
		XPCOM.memmove (dest, passAddr[0], length * 2);
		passLabel[0] = new String (dest);		
	}
	
	if (aCheckMsg != 0) {
		length = XPCOM.strlen_PRUnichar (aCheckMsg);
		if (length > 0) {
			dest = new char[length];
			XPCOM.memmove (dest, aCheckMsg, length * 2);
			checkLabel = new String (dest);
		}
	}

	Shell shell = browser == null ? new Shell () : browser.getShell ();
	PromptDialog dialog = new PromptDialog (shell);
	int[] check = new int[1], result = new int[1];
	if (aCheckState != 0) XPCOM.memmove (check, aCheckState, 4);
	dialog.promptUsernameAndPassword (titleLabel, textLabel, checkLabel, userLabel, passLabel, check, result);

	XPCOM.memmove (_retval, result, 4);
	if (result[0] == 1) {
		/* 
		* User selected OK. User name and password are returned as PRUnichar values. Any default
		* value that we override must be freed using the nsIMemory service.
		*/
		int cnt, size;
		int /*long*/ ptr;
		char[] buffer;
		int /*long*/[] result2 = new int /*long*/[1];
		if (userLabel[0] != null) {
			cnt = userLabel[0].length ();
			buffer = new char[cnt + 1];
			userLabel[0].getChars (0, cnt, buffer, 0);
			size = buffer.length * 2;
			ptr = C.malloc (size);
			XPCOM.memmove (ptr, buffer, size);
			XPCOM.memmove (aUsername, new int /*long*/[] {ptr}, C.PTR_SIZEOF);

			if (userAddr[0] != 0) {
				int rc = XPCOM.NS_GetServiceManager (result2);
				if (rc != XPCOM.NS_OK) SWT.error (rc);
				if (result2[0] == 0) SWT.error (XPCOM.NS_NOINTERFACE);
			
				nsIServiceManager serviceManager = new nsIServiceManager (result2[0]);
				result2[0] = 0;
				byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_MEMORY_CONTRACTID, true);
				rc = serviceManager.GetServiceByContractID (aContractID, nsIMemory.NS_IMEMORY_IID, result2);
				if (rc != XPCOM.NS_OK) SWT.error (rc);
				if (result[0] == 0) SWT.error (XPCOM.NS_NOINTERFACE);		
				serviceManager.Release ();
				
				nsIMemory memory = new nsIMemory (result2[0]);
				result2[0] = 0;
				memory.Free (userAddr[0]);
				memory.Release ();
			}
		}
		if (passLabel[0] != null) {
			cnt = passLabel[0].length ();
			buffer = new char[cnt + 1];
			passLabel[0].getChars (0, cnt, buffer, 0);
			size = buffer.length * 2;
			ptr = C.malloc (size);
			XPCOM.memmove (ptr, buffer, size);
			XPCOM.memmove (aPassword, new int /*long*/[] {ptr}, C.PTR_SIZEOF);
			
			if (passAddr[0] != 0) {
				int rc = XPCOM.NS_GetServiceManager (result2);
				if (rc != XPCOM.NS_OK) SWT.error (rc);
				if (result2[0] == 0) SWT.error (XPCOM.NS_NOINTERFACE);

				nsIServiceManager serviceManager = new nsIServiceManager (result2[0]);
				result2[0] = 0;
				byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_MEMORY_CONTRACTID, true);
				rc = serviceManager.GetServiceByContractID (aContractID, nsIMemory.NS_IMEMORY_IID, result2);
				if (rc != XPCOM.NS_OK) SWT.error (rc);
				if (result2[0] == 0) SWT.error (XPCOM.NS_NOINTERFACE);		
				serviceManager.Release ();

				nsIMemory memory = new nsIMemory (result2[0]);
				result2[0] = 0;
				memory.Free (passAddr[0]);
				memory.Release ();
			}
		}
	}
	if (aCheckState != 0) XPCOM.memmove (aCheckState, check, 4);
	return XPCOM.NS_OK;
}

public int /*long*/ PromptPassword (int /*long*/ aParent, int /*long*/ aDialogTitle, int /*long*/ aText, int /*long*/ aPassword, int /*long*/ aCheckMsg, int /*long*/ aCheckState, int /*long*/ _retval) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int /*long*/ Select (int /*long*/ aParent, int /*long*/ aDialogTitle, int /*long*/ aText, int /*long*/ aCount, int /*long*/ aSelectList, int /*long*/ aOutSelection, int /*long*/ _retval) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
}

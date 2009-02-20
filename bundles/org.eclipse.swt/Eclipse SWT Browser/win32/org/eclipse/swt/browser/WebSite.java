/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
import org.eclipse.swt.internal.C;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.ole.win32.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.win32.*;

class WebSite extends OleControlSite {
	COMObject iDocHostUIHandler;
	COMObject iDocHostShowUI;
	COMObject iServiceProvider;
	COMObject iInternetSecurityManager;
	COMObject iOleCommandTarget;
	COMObject iDispatch;
	boolean ignoreNextMessage;
	Boolean canExecuteApplets;

	static final int OLECMDID_SHOWSCRIPTERROR = 40;
	static final short [] ACCENTS = new short [] {'~', '`', '\'', '^', '"'};
	static final String CONSUME_KEY = "org.eclipse.swt.OleFrame.ConsumeKey"; //$NON-NLS-1$

public WebSite(Composite parent, int style, String progId) {
	super(parent, style, progId);		
}

protected void createCOMInterfaces () {
	super.createCOMInterfaces();
	iDocHostUIHandler = new COMObject(new int[]{2, 0, 0, 4, 1, 5, 0, 0, 1, 1, 1, 3, 3, 2, 2, 1, 3, 2}){
		public int /*long*/ method0(int /*long*/[] args) {return QueryInterface(args[0], args[1]);}
		public int /*long*/ method1(int /*long*/[] args) {return AddRef();}
		public int /*long*/ method2(int /*long*/[] args) {return Release();}
		public int /*long*/ method3(int /*long*/[] args) {return ShowContextMenu((int)/*64*/args[0], args[1], args[2], args[3]);}
		public int /*long*/ method4(int /*long*/[] args) {return GetHostInfo(args[0]);}
		public int /*long*/ method5(int /*long*/[] args) {return ShowUI((int)/*64*/args[0], args[1], args[2], args[3], args[4]);}
		public int /*long*/ method6(int /*long*/[] args) {return HideUI();}
		public int /*long*/ method7(int /*long*/[] args) {return UpdateUI();}
		public int /*long*/ method8(int /*long*/[] args) {return EnableModeless((int)/*64*/args[0]);}
		public int /*long*/ method9(int /*long*/[] args) {return OnDocWindowActivate((int)/*64*/args[0]);}
		public int /*long*/ method10(int /*long*/[] args) {return OnFrameWindowActivate((int)/*64*/args[0]);}
		public int /*long*/ method11(int /*long*/[] args) {return ResizeBorder(args[0], args[1], (int)/*64*/args[2]);}
		public int /*long*/ method12(int /*long*/[] args) {return TranslateAccelerator(args[0], args[1], (int)/*64*/args[2]);}
		public int /*long*/ method13(int /*long*/[] args) {return GetOptionKeyPath(args[0], (int)/*64*/args[1]);}
		public int /*long*/ method14(int /*long*/[] args) {return GetDropTarget(args[0], args[1]);}
		public int /*long*/ method15(int /*long*/[] args) {return GetExternal(args[0]);}
		public int /*long*/ method16(int /*long*/[] args) {return TranslateUrl((int)/*64*/args[0], args[1], args[2]);}		
		public int /*long*/ method17(int /*long*/[] args) {return FilterDataObject(args[0], args[1]);}
	};
	iDocHostShowUI = new COMObject(new int[]{2, 0, 0, 7, C.PTR_SIZEOF == 4 ? 7 : 6}){
		public int /*long*/ method0(int /*long*/[] args) {return QueryInterface(args[0], args[1]);}
		public int /*long*/ method1(int /*long*/[] args) {return AddRef();}
		public int /*long*/ method2(int /*long*/[] args) {return Release();}
		public int /*long*/ method3(int /*long*/[] args) {return ShowMessage(args[0], args[1], args[2], (int)/*64*/args[3], args[4], (int)/*64*/args[5], args[6]);}
		public int /*long*/ method4(int /*long*/[] args) {
			if (args.length == 7) {
				return ShowHelp(args[0], args[1], (int)/*64*/args[2], (int)/*64*/args[3], (int)/*64*/args[4], (int)/*64*/args[5], args[6]);
			} else {
				return ShowHelp_64(args[0], args[1], (int)/*64*/args[2], (int)/*64*/args[3], args[4], args[5]);
			}
		}
	};
	iServiceProvider = new COMObject(new int[]{2, 0, 0, 3}){
		public int /*long*/ method0(int /*long*/[] args) {return QueryInterface(args[0], args[1]);}
		public int /*long*/ method1(int /*long*/[] args) {return AddRef();}
		public int /*long*/ method2(int /*long*/[] args) {return Release();}
		public int /*long*/ method3(int /*long*/[] args) {return QueryService(args[0], args[1], args[2]);}
	};
	iInternetSecurityManager = new COMObject(new int[]{2, 0, 0, 1, 1, 3, 4, 8, 7, 3, 3}){
		public int /*long*/ method0(int /*long*/[] args) {return QueryInterface(args[0], args[1]);}
		public int /*long*/ method1(int /*long*/[] args) {return AddRef();}
		public int /*long*/ method2(int /*long*/[] args) {return Release();}
		public int /*long*/ method3(int /*long*/[] args) {return SetSecuritySite(args[0]);}
		public int /*long*/ method4(int /*long*/[] args) {return GetSecuritySite(args[0]);}
		public int /*long*/ method5(int /*long*/[] args) {return MapUrlToZone(args[0], args[1], (int)/*64*/args[2]);}
		public int /*long*/ method6(int /*long*/[] args) {return GetSecurityId(args[0], args[1], args[2], args[3]);}
		public int /*long*/ method7(int /*long*/[] args) {return ProcessUrlAction(args[0], (int)/*64*/args[1], args[2], (int)/*64*/args[3], args[4], (int)/*64*/args[5], (int)/*64*/args[6], (int)/*64*/args[7]);}
		public int /*long*/ method8(int /*long*/[] args) {return QueryCustomPolicy(args[0], args[1], args[2], args[3], args[4], (int)/*64*/args[5], (int)/*64*/args[6]);}
		public int /*long*/ method9(int /*long*/[] args) {return SetZoneMapping((int)/*64*/args[0], args[1], (int)/*64*/args[2]);}
		public int /*long*/ method10(int /*long*/[] args) {return GetZoneMappings((int)/*64*/args[0], args[1], (int)/*64*/args[2]);}
	};
	iOleCommandTarget = new COMObject(new int[]{2, 0, 0, 4, 5}) {
		public int /*long*/ method0(int /*long*/[] args) {return QueryInterface(args[0], args[1]);}
		public int /*long*/ method1(int /*long*/[] args) {return AddRef();}
		public int /*long*/ method2(int /*long*/[] args) {return Release();}		
		public int /*long*/ method3(int /*long*/[] args) {return QueryStatus(args[0], (int)/*64*/args[1], args[2], args[3]);}		
		public int /*long*/ method4(int /*long*/[] args) {return Exec(args[0], (int)/*64*/args[1], (int)/*64*/args[2], args[3], args[4]);}
	};
	iDispatch = new COMObject (new int[] {2, 0, 0, 1, 3, 5, 8}) {
		public int /*long*/ method0 (int /*long*/[] args) {
			/* 
			 * IDispatch check must be done here instead of in the shared QueryInterface
			 * implementation, to avoid answering the superclass's IDispatch implementation
			 * instead of this one.
			 */
			GUID guid = new GUID ();
			COM.MoveMemory (guid, args[0], GUID.sizeof);
			if (COM.IsEqualGUID (guid, COM.IIDIDispatch)) {
				COM.MoveMemory (args[1], new int /*long*/[] {iDispatch.getAddress ()}, OS.PTR_SIZEOF);
				AddRef ();
				return COM.S_OK;
			}
			return QueryInterface (args[0], args[1]);
		}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
		public int /*long*/ method3 (int /*long*/[] args) {return GetTypeInfoCount (args[0]);}
		public int /*long*/ method4 (int /*long*/[] args) {return GetTypeInfo ((int)/*64*/args[0], (int)/*64*/args[1], args[2]);}
		public int /*long*/ method5 (int /*long*/[] args) {return GetIDsOfNames ((int)/*64*/args[0], args[1], (int)/*64*/args[2], (int)/*64*/args[3], args[4]);}
		public int /*long*/ method6 (int /*long*/[] args) {return Invoke ((int)/*64*/args[0], (int)/*64*/args[1], (int)/*64*/args[2], (int)/*64*/args[3], args[4], args[5], args[6], args[7]);}
	};
}

protected void disposeCOMInterfaces() {
	super.disposeCOMInterfaces();
	if (iDocHostUIHandler != null) {
		iDocHostUIHandler.dispose();
		iDocHostUIHandler = null;
	}
	if (iDocHostShowUI != null) {
		iDocHostShowUI.dispose();
		iDocHostShowUI = null;
	}
	if (iServiceProvider != null) {
		iServiceProvider.dispose();
		iServiceProvider = null;
	}
	if (iInternetSecurityManager != null) {
		iInternetSecurityManager.dispose();
		iInternetSecurityManager = null;
	}
	if (iOleCommandTarget != null) {
		iOleCommandTarget.dispose();
		iOleCommandTarget = null;
	}
	if (iDispatch != null) {
		iDispatch.dispose ();
		iDispatch = null;
	}
}

protected int AddRef() {
	/* Workaround for javac 1.1.8 bug */
	return super.AddRef();
}

protected int QueryInterface(int /*long*/ riid, int /*long*/ ppvObject) {
	int result = super.QueryInterface(riid, ppvObject);
	if (result == COM.S_OK) return result;
	if (riid == 0 || ppvObject == 0) return COM.E_INVALIDARG;
	GUID guid = new GUID();
	COM.MoveMemory(guid, riid, GUID.sizeof);
	if (COM.IsEqualGUID(guid, COM.IIDIDocHostUIHandler)) {
		COM.MoveMemory(ppvObject, new int /*long*/[] {iDocHostUIHandler.getAddress()}, OS.PTR_SIZEOF);
		AddRef();
		return COM.S_OK;
	}
	if (COM.IsEqualGUID(guid, COM.IIDIDocHostShowUI)) {
		COM.MoveMemory(ppvObject, new int /*long*/[] {iDocHostShowUI.getAddress()}, OS.PTR_SIZEOF);
		AddRef();
		return COM.S_OK;
	}
	if (COM.IsEqualGUID(guid, COM.IIDIServiceProvider)) {
		COM.MoveMemory(ppvObject, new int /*long*/[] {iServiceProvider.getAddress()}, OS.PTR_SIZEOF);
		AddRef();
		return COM.S_OK;
	}
    if (COM.IsEqualGUID(guid, COM.IIDIInternetSecurityManager)) {
        COM.MoveMemory(ppvObject, new int /*long*/[] {iInternetSecurityManager.getAddress()}, OS.PTR_SIZEOF);
        AddRef();
        return COM.S_OK;
    }
	if (COM.IsEqualGUID(guid, COM.IIDIOleCommandTarget)) {
		COM.MoveMemory(ppvObject, new int /*long*/[] {iOleCommandTarget.getAddress()}, OS.PTR_SIZEOF);
		AddRef();
		return COM.S_OK;
	}
	COM.MoveMemory(ppvObject, new int /*long*/[] {0}, OS.PTR_SIZEOF);
	return COM.E_NOINTERFACE;
}

/* IDocHostUIHandler */

int EnableModeless(int EnableModeless) {
	return COM.E_NOTIMPL;
}

int FilterDataObject(int /*long*/ pDO, int /*long*/ ppDORet) {
	return COM.E_NOTIMPL;
}

int GetDropTarget(int /*long*/ pDropTarget, int /*long*/ ppDropTarget) {
	return COM.E_NOTIMPL;
}

int GetExternal(int /*long*/ ppDispatch) {
	OS.MoveMemory (ppDispatch, new int /*long*/[] {iDispatch.getAddress()}, C.PTR_SIZEOF);
	AddRef ();
	return COM.S_OK;
}

int GetHostInfo(int /*long*/ pInfo) {
	int info = IE.DOCHOSTUIFLAG_THEME;
	IE browser = (IE)((Browser)getParent().getParent()).webBrowser;
	if ((browser.style & SWT.BORDER) == 0) info |= IE.DOCHOSTUIFLAG_NO3DOUTERBORDER;
	OS.MoveMemory(pInfo + 4, new int[] {info}, 4);

	/*
	* TODO replace the implementation above with the one commented below
	* when 32-bit swt starts compiling with a newer mssdk whose definition
	* of DOCHOSTUIINFO includes the last two OLECHAR* fields.
	*/ 
//	int info = IE.DOCHOSTUIFLAG_THEME;
//	Browser browser = (Browser)getParent().getParent();
//	if ((browser.getStyle() & SWT.BORDER) == 0) info |= IE.DOCHOSTUIFLAG_NO3DOUTERBORDER;
//
//	DOCHOSTUIINFO uiInfo = new DOCHOSTUIINFO ();
//	OS.MoveMemory(uiInfo, pInfo, DOCHOSTUIINFO.sizeof);
//	uiInfo.dwFlags = info;
//	OS.MoveMemory(pInfo, uiInfo, DOCHOSTUIINFO.sizeof);
	return COM.S_OK;
}

int GetOptionKeyPath(int /*long*/ pchKey, int dw) {
	return COM.E_NOTIMPL;
}

int HideUI() {
	return COM.E_NOTIMPL;
}

int OnDocWindowActivate(int fActivate) {
	return COM.E_NOTIMPL;
}

int OnFrameWindowActivate(int fActivate) {
	return COM.E_NOTIMPL;
}

protected int Release() {
	/* Workaround for javac 1.1.8 bug */
	return super.Release();
}

int ResizeBorder(int /*long*/ prcBorder, int /*long*/ pUIWindow, int fFrameWindow) {
	return COM.E_NOTIMPL;
}

int ShowContextMenu(int dwID, int /*long*/ ppt, int /*long*/ pcmdtReserved, int /*long*/ pdispReserved) {
	Browser browser = (Browser)getParent().getParent();
	Event event = new Event();
	POINT pt = new POINT();
	OS.MoveMemory(pt, ppt, POINT.sizeof);
	event.x = pt.x;
	event.y = pt.y;
	browser.notifyListeners(SWT.MenuDetect, event);
	if (!event.doit) return COM.S_OK;
	Menu menu = browser.getMenu();
	if (menu != null && !menu.isDisposed ()) {
		if (pt.x != event.x || pt.y != event.y) {
			menu.setLocation (event.x, event.y);
		}
		menu.setVisible (true);
		return COM.S_OK;
	}
	/* Show default IE popup menu */
	return COM.S_FALSE;
}

int ShowUI(int dwID, int /*long*/ pActiveObject, int /*long*/ pCommandTarget, int /*long*/ pFrame, int /*long*/ pDoc) {
	return COM.S_FALSE;
}

int TranslateAccelerator(int /*long*/ lpMsg, int /*long*/ pguidCmdGroup, int nCmdID) {
	/*
	* Feature on Internet Explorer.  By default the embedded Internet Explorer control runs
	* the Internet Explorer shortcuts (e.g. Ctrl+F for Find).  This overrides the shortcuts
	* defined by SWT.  The workaround is to forward the accelerator keys to the parent window
	* and have Internet Explorer ignore the ones handled by the parent window.
	*/
	Menu menubar = getShell().getMenuBar();
	if (menubar != null && !menubar.isDisposed() && menubar.isEnabled()) {
		Shell shell = menubar.getShell();
		int /*long*/ hwnd = shell.handle;
		int /*long*/ hAccel = OS.SendMessage(hwnd, OS.WM_APP+1, 0, 0);
		if (hAccel != 0) {
			MSG msg = new MSG();
			OS.MoveMemory(msg, lpMsg, MSG.sizeof);
			if (OS.TranslateAccelerator(hwnd, hAccel, msg) != 0) return COM.S_OK;
		}
	}
	/*
	* By default the IE shortcuts are run.  However, F5 causes a refresh, which is not
	* appropriate when rendering HTML from memory, and CTRL-N opens a standalone IE,
	* which is undesirable and can cause a crash in some contexts.  The workaround is
	* to block IE from handling these shortcuts by answering COM.S_OK.
	*/
	int result = COM.S_FALSE;
	MSG msg = new MSG();
	OS.MoveMemory(msg, lpMsg, MSG.sizeof);
	if (msg.message == OS.WM_KEYDOWN) {
		switch ((int)/*64*/msg.wParam) {
			case OS.VK_F5:
				OleAutomation auto = new OleAutomation(this);
				int[] rgdispid = auto.getIDsOfNames(new String[] { "LocationURL" }); //$NON-NLS-1$
				Variant pVarResult = auto.getProperty(rgdispid[0]);
				auto.dispose();
				if (pVarResult != null) {
					if (pVarResult.getType() == OLE.VT_BSTR) {
						String url = pVarResult.getString();
						if (url.equals(IE.ABOUT_BLANK)) result = COM.S_OK;
					}
					pVarResult.dispose();
				}
				break;
			case OS.VK_TAB:
				/* 
				 * Do not interfere with tab traversal since it's not known
				 * if it will be within IE or out to another Control.
				 */
				break;
			case OS.VK_RETURN:
				/*
				* Translating OS.VK_RETURN results in the native control handling it
				* twice (eg.- inserting two lines instead of one).  So this key is not
				* translated here, and instead is explicitly handled in the keypress
				* handler.
				*/
				break;
			case OS.VK_N:
				if (OS.GetKeyState (OS.VK_CONTROL) < 0 && OS.GetKeyState (OS.VK_MENU) >= 0 && OS.GetKeyState (OS.VK_SHIFT) >= 0) {
					frame.setData(CONSUME_KEY, "false"); //$NON-NLS-1$
					result = COM.S_OK;
					break;
				}
				// FALL THROUGH
			default:
				OS.TranslateMessage(msg);
				frame.setData(CONSUME_KEY, "true"); //$NON-NLS-1$
				break;
		}
	}

	switch (msg.message) {
		case OS.WM_KEYDOWN:
		case OS.WM_KEYUP: {
			if (!OS.IsWinCE) {
				boolean isAccent = false;
				switch ((int)/*64*/msg.wParam) {
					case OS.VK_SHIFT:
					case OS.VK_MENU:
					case OS.VK_CONTROL:
					case OS.VK_CAPITAL:
					case OS.VK_NUMLOCK:
					case OS.VK_SCROLL:
						break;
					default: {
						/*
						* Bug in Windows. The high bit in the result of MapVirtualKey() on
						* Windows NT is bit 32 while the high bit on Windows 95 is bit 16.
						* They should both be bit 32.  The fix is to test the right bit.
						*/
						int mapKey = OS.MapVirtualKey ((int)/*64*/msg.wParam, 2);
						if (mapKey != 0) {
							isAccent = (mapKey & (OS.IsWinNT ? 0x80000000 : 0x8000)) != 0;
							if (!isAccent) {
								for (int i=0; i<ACCENTS.length; i++) {
									int value = OS.VkKeyScan (ACCENTS [i]);
									if (value != -1 && (value & 0xFF) == msg.wParam) {
										int state = value >> 8;
										if ((OS.GetKeyState (OS.VK_SHIFT) < 0) == ((state & 0x1) != 0) &&
											(OS.GetKeyState (OS.VK_CONTROL) < 0) == ((state & 0x2) != 0) &&
											(OS.GetKeyState (OS.VK_MENU) < 0) == ((state & 0x4) != 0)) {
												if ((state & 0x7) != 0) isAccent = true;
												break;
										}
									}
								}
							}
						}
						break;
					}
				};
				if (isAccent) result = COM.S_OK;
			}
		}
	}
	return result;
}

int TranslateUrl(int dwTranslate, int /*long*/ pchURLIn, int /*long*/ ppchURLOut) {
	return COM.E_NOTIMPL;
}

int UpdateUI() {
	return COM.E_NOTIMPL;
}

/* IDocHostShowUI */

int ShowMessage(int /*long*/ hwnd, int /*long*/ lpstrText, int /*long*/ lpstrCaption, int dwType, int /*long*/ lpstrHelpFile, int dwHelpContext, int /*long*/ plResult) {
	boolean ignore = ignoreNextMessage;
	ignoreNextMessage = false;
	return ignore ? COM.S_OK : COM.S_FALSE;
}

int ShowHelp_64(int /*long*/ hwnd, int /*long*/ pszHelpFile, int uCommand, int dwData, long pt, int /*long*/ pDispatchObjectHit) {
	POINT point = new POINT();
	OS.MoveMemory(point, new long[]{pt}, 8);
	return ShowHelp(hwnd, pszHelpFile, uCommand, dwData, point.x, point.y, pDispatchObjectHit);
}

/* Note.  One of the arguments of ShowHelp is a POINT struct and not a pointer to a POINT struct. Because
 * of the way Callback gets int parameters from a va_list of C arguments 2 integer arguments must be declared,
 * ptMouse_x and ptMouse_y. Otherwise the Browser crashes when the user presses F1 to invoke
 * the help.
 */
int ShowHelp(int /*long*/ hwnd, int /*long*/ pszHelpFile, int uCommand, int dwData, int ptMouse_x, int ptMouse_y, int /*long*/ pDispatchObjectHit) {
	Browser browser = (Browser)getParent().getParent();
	Event event = new Event();
	event.type = SWT.Help;
	event.display = getDisplay();
	event.widget = browser;
	Shell shell = browser.getShell();
	Control control = browser;
	do {
		if (control.isListening(SWT.Help)) {
			control.notifyListeners(SWT.Help, event);
			break;
		}
		if (control == shell) break;
		control = control.getParent();
	} while (true);
	return COM.S_OK;
}

/* IServiceProvider */

int QueryService(int /*long*/ guidService, int /*long*/ riid, int /*long*/ ppvObject) {
	if (riid == 0 || ppvObject == 0) return COM.E_INVALIDARG;
	GUID guid = new GUID();
	COM.MoveMemory(guid, riid, GUID.sizeof);
	if (COM.IsEqualGUID(guid, COM.IIDIInternetSecurityManager)) {
		COM.MoveMemory(ppvObject, new int /*long*/[] {iInternetSecurityManager.getAddress()}, OS.PTR_SIZEOF);
		AddRef();
		return COM.S_OK;
	}
	COM.MoveMemory(ppvObject, new int /*long*/[] {0}, OS.PTR_SIZEOF);
	return COM.E_NOINTERFACE;
}

/* IInternetSecurityManager */

int SetSecuritySite(int /*long*/ pSite) {
	return IE.INET_E_DEFAULT_ACTION;
}

int GetSecuritySite(int /*long*/ ppSite) {
	return IE.INET_E_DEFAULT_ACTION;
}

int MapUrlToZone(int /*long*/ pwszUrl, int /*long*/ pdwZone, int dwFlags) {	
	/*
	* Feature in IE 6 sp1.  HTML rendered in memory
	* does not enable local links but the exact same
	* HTML document loaded through a local file is
	* permitted to follow local links.  The workaround is
	* to return URLZONE_INTRANET instead of the default
	* value URLZONE_LOCAL_MACHINE.
	*/	
	COM.MoveMemory(pdwZone, new int[] {IE.URLZONE_INTRANET}, 4);
	return COM.S_OK;
}

int GetSecurityId(int /*long*/ pwszUrl, int /*long*/ pbSecurityId, int /*long*/ pcbSecurityId, int /*long*/ dwReserved) {
	return IE.INET_E_DEFAULT_ACTION;
}

int ProcessUrlAction(int /*long*/ pwszUrl, int dwAction, int /*long*/ pPolicy, int cbPolicy, int /*long*/ pContext, int cbContext, int dwFlags, int dwReserved) {
	ignoreNextMessage = false;

	/*
	* Feature in IE 6 sp1.  HTML rendered in memory
	* containing an OBJECT tag referring to a local file
	* brings up a warning dialog asking the user whether
	* it should proceed or not.  The workaround is to
	* set the policy to URLPOLICY_ALLOW in this case (dwAction
	* value of 0x1406).
	* 
	* Feature in IE. Security Patches and user settings
	* affect the way the embedded web control behaves.  The current
	* approach is to consider the content trusted and allow
	* all URLs by default.
	*/
	int policy = IE.URLPOLICY_ALLOW;

	if (dwAction >= IE.URLACTION_JAVA_MIN && dwAction <= IE.URLACTION_JAVA_MAX) {
		if (canExecuteApplets ()) {
			policy = IE.URLPOLICY_JAVA_LOW;
		} else {
			policy = IE.URLPOLICY_JAVA_PROHIBIT;
			ignoreNextMessage = true;
		}
	}
	if (dwAction == IE.URLACTION_ACTIVEX_RUN) {
		GUID guid = new GUID();
		COM.MoveMemory(guid, pContext, GUID.sizeof);
		if (COM.IsEqualGUID(guid, COM.IIDJavaBeansBridge) && !canExecuteApplets ()) {
			policy = IE.URLPOLICY_DISALLOW;
			ignoreNextMessage = true;
		}
		if (COM.IsEqualGUID(guid, COM.IIDShockwaveActiveXControl)) {
			policy = IE.URLPOLICY_DISALLOW;
			ignoreNextMessage = true;
		}
	}
	if (dwAction == IE.URLACTION_SCRIPT_RUN) {
		IE browser = (IE)((Browser)getParent ().getParent ()).webBrowser;
		if (!browser.jsEnabled) {
			policy = IE.URLPOLICY_DISALLOW;
		}
	}

	if (cbPolicy >= 4) COM.MoveMemory(pPolicy, new int[] {policy}, 4);
	return policy == IE.URLPOLICY_ALLOW ? COM.S_OK : COM.S_FALSE;
}

boolean canExecuteApplets () {
	/*
	* Executing an applet in embedded IE will crash if IE's Java plug-in
	* launches its jre in IE's process, because this new jre conflicts
	* with the one running eclipse.  These cases need to be avoided by
	* vetoing the running of applets.
	* 
	* However as of Sun jre 1.6u10, applets can be launched in a separate
	* process, which avoids the conflict with the jre running eclipse.
	* Therefore if this condition is detected, and if the required jar 
	* libraries are available, then applets can be executed. 
	*/

	/* 
	* executing applets with IE6 embedded can crash, so do not
	* attempt this if the version is less than IE7
	*/
	if (!IE.IsIE7) return false;

	if (canExecuteApplets == null) {
		WebBrowser webBrowser = ((Browser)getParent ().getParent ()).webBrowser;
		String script = "try {var element = document.createElement('object');element.classid='clsid:CAFEEFAC-DEC7-0000-0000-ABCDEFFEDCBA';return element.object.isPlugin2();} catch (err) {};return false;"; //$NON-NLS-1$
		canExecuteApplets = ((Boolean)webBrowser.evaluate (script)); 
		if (canExecuteApplets.booleanValue ()) {
			try {
				Class.forName ("sun.plugin2.main.server.IExplorerPlugin"); /* plugin.jar */	//$NON-NLS-1$
				Class.forName ("com.sun.deploy.services.Service"); /* deploy.jar */	//$NON-NLS-1$
				Class.forName ("com.sun.javaws.Globals"); /* javaws.jar */	//$NON-NLS-1$
			} catch (ClassNotFoundException e) {
				/* one or more of the required jar libraries are not available */
				canExecuteApplets = Boolean.FALSE;
			}
		}
	}
	return canExecuteApplets.booleanValue ();
}

int QueryCustomPolicy(int /*long*/ pwszUrl, int /*long*/ guidKey, int /*long*/ ppPolicy, int /*long*/ pcbPolicy, int /*long*/ pContext, int cbContext, int dwReserved) {
	return IE.INET_E_DEFAULT_ACTION;
}

int SetZoneMapping(int dwZone, int /*long*/ lpszPattern, int dwFlags) {
	return IE.INET_E_DEFAULT_ACTION;
}

int GetZoneMappings(int dwZone, int /*long*/ ppenumString, int dwFlags) {
	return COM.E_NOTIMPL;
}

/* IOleCommandTarget */
int QueryStatus(int /*long*/ pguidCmdGroup, int cCmds, int /*long*/ prgCmds, int /*long*/ pCmdText) {
	return COM.E_NOTSUPPORTED;
}

int Exec(int /*long*/ pguidCmdGroup, int nCmdID, int nCmdExecOpt, int /*long*/ pvaIn, int /*long*/ pvaOut) {
	if (pguidCmdGroup != 0) {
		GUID guid = new GUID();
		COM.MoveMemory(guid, pguidCmdGroup, GUID.sizeof);

		/*
		* If a javascript error occurred then suppress IE's default script error dialog.
		*/
		if (COM.IsEqualGUID(guid, COM.CGID_DocHostCommandHandler)) {
			if (nCmdID == OLECMDID_SHOWSCRIPTERROR) return COM.S_OK;
		}

		/*
		* Bug in Internet Explorer.  OnToolBar TRUE is also fired when any of the 
		* address bar or menu bar are requested but not the tool bar.  A workaround
		* has been posted by a Microsoft developer on the public webbrowser_ctl
		* newsgroup. The workaround is to implement the IOleCommandTarget interface
		* to test the argument of an undocumented command.
		*/
		if (nCmdID == 1 && COM.IsEqualGUID(guid, COM.CGID_Explorer) && ((nCmdExecOpt & 0xFFFF) == 0xA)) {
			IE browser = (IE)((Browser)getParent().getParent()).webBrowser;
			browser.toolBar = (nCmdExecOpt & 0xFFFF0000) != 0;
		}
	}
	return COM.E_NOTSUPPORTED;
}

/* IDispatch */

int GetTypeInfoCount (int /*long*/ pctinfo) {
	C.memmove (pctinfo, new int[] {0}, 4);
	return COM.S_OK;
}

int GetTypeInfo (int iTInfo, int lcid, int /*long*/ ppTInfo) {
	return COM.S_OK;
}

int GetIDsOfNames (int riid, int /*long*/ rgszNames, int cNames, int lcid, int /*long*/ rgDispId) {
    int /*long*/[] ptr = new int /*long*/[1];
    OS.MoveMemory (ptr, rgszNames, C.PTR_SIZEOF);
    int length = OS.wcslen (ptr[0]);
    char[] buffer = new char[length];
    OS.MoveMemory (buffer, ptr[0], length * 2);
    String functionName = String.valueOf (buffer);
    int result = COM.S_OK; 
    int[] ids = new int[cNames];	/* DISPIDs */
    if (functionName.equals ("callJava")) { //$NON-NLS-1$
	    for (int i = 0; i < cNames; i++) {
	        ids[i] = i + 1;
	    }
    } else {
    	result = COM.DISP_E_UNKNOWNNAME;
	    for (int i = 0; i < cNames; i++) {
	        ids[i] = COM.DISPID_UNKNOWN;
	    }
    }
    OS.MoveMemory (rgDispId, ids, cNames * 4);
	return result;
}

int Invoke (int dispIdMember, int /*long*/ riid, int lcid, int dwFlags, int /*long*/ pDispParams, int /*long*/ pVarResult, int /*long*/ pExcepInfo, int /*long*/ pArgErr) {
	DISPPARAMS dispParams = new DISPPARAMS ();
	COM.MoveMemory (dispParams, pDispParams, DISPPARAMS.sizeof);
	if (dispParams.cArgs != 2) {
		if (pVarResult != 0) {
			COM.MoveMemory (pVarResult, new int /*long*/[] {0}, C.PTR_SIZEOF);
		}
		return COM.S_OK;
	}

	int /*long*/ ptr = dispParams.rgvarg + Variant.sizeof;
	Variant variant = Variant.win32_new (ptr);
	int index = variant.getInt ();
	variant.dispose ();
	if (index <= 0) {
		if (pVarResult != 0) {
			COM.MoveMemory (pVarResult, new int /*long*/[] {0}, C.PTR_SIZEOF);
		}
		return COM.S_OK;
	}

	variant = Variant.win32_new (dispParams.rgvarg);
	IE ie = (IE)((Browser)getParent ().getParent ()).webBrowser;
	Object key = new Integer (index);
	BrowserFunction function = (BrowserFunction)ie.functions.get (key);
	Object returnValue = null;
	if (function != null) {
		try {
			Object temp = convertToJava (variant);
			if (temp instanceof Object[]) {
				Object[] args = (Object[])temp;
				try {
					returnValue = function.function (args);
				} catch (Exception e) {
					/* exception during function invocation */
					returnValue = WebBrowser.CreateErrorString (e.getLocalizedMessage ());
				}
			}
		} catch (IllegalArgumentException e) {
			/* invalid argument value type */
			if (function.isEvaluate) {
				/* notify the function so that a java exception can be thrown */
				function.function (new String[] {WebBrowser.CreateErrorString (new SWTException (SWT.ERROR_INVALID_RETURN_VALUE).getLocalizedMessage ())});
			}
			returnValue = WebBrowser.CreateErrorString (e.getLocalizedMessage ());
		}
	}
	variant.dispose ();

	if (pVarResult != 0) {
		if (returnValue == null) {
			COM.MoveMemory (pVarResult, new int /*long*/[] {0}, C.PTR_SIZEOF);
		} else {
			try {
				variant = convertToJS (returnValue);
			} catch (SWTException e) {
				/* invalid return value type */
				variant = convertToJS (WebBrowser.CreateErrorString (e.getLocalizedMessage ()));
			}
			Variant.win32_copy (pVarResult, variant);
			variant.dispose ();
		}
	}
	return COM.S_OK;
}

Object convertToJava (Variant variant) {
	switch (variant.getType ()) {
		case OLE.VT_NULL: return null;
		case OLE.VT_BSTR: return variant.getString ();
		case OLE.VT_BOOL: return new Boolean (variant.getBoolean ());
		case OLE.VT_I2:
		case OLE.VT_I4:
		case OLE.VT_I8:
		case OLE.VT_R4:
		case OLE.VT_R8:
			return new Double (variant.getDouble ());
		case OLE.VT_DISPATCH: {
			Object[] args = null;
			OleAutomation auto = variant.getAutomation ();
			TYPEATTR typeattr = auto.getTypeInfoAttributes ();
			if (typeattr != null) {
				GUID guid = new GUID ();
				guid.Data1 = typeattr.guid_Data1;
				guid.Data2 = typeattr.guid_Data2;
				guid.Data3 = typeattr.guid_Data3;
				guid.Data4 = typeattr.guid_Data4;
				if (COM.IsEqualGUID (guid, COM.IIDIJScriptTypeInfo)) {
					int[] rgdispid = auto.getIDsOfNames (new String[] {"length"}); //$NON-NLS-1$
					if (rgdispid != null) {
						Variant varLength = auto.getProperty (rgdispid[0]);
						int length = varLength.getInt ();
						varLength.dispose ();
						args = new Object[length];
						for (int i = 0; i < length; i++) {
							rgdispid = auto.getIDsOfNames (new String[] {String.valueOf (i)});
							if (rgdispid != null) {
								Variant current = auto.getProperty (rgdispid[0]);
								try {
									args[i] = convertToJava (current);
									current.dispose ();
								} catch (IllegalArgumentException e) {
									/* invalid argument value type */
									current.dispose ();
									auto.dispose ();
									throw e;
								}
							}
						}
					}
				} else {
					auto.dispose ();
					SWT.error (SWT.ERROR_INVALID_ARGUMENT);
				}
			}
			auto.dispose ();
			return args;
		}
	}
	SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	return null;
}

Variant convertToJS (Object value) {
	if (value == null) {
		return new Variant ();
	}
	if (value instanceof String) {
		return new Variant ((String)value);
	}
	if (value instanceof Boolean) {
		return new Variant (((Boolean)value).booleanValue ());
	}
	if (value instanceof Number) {
		return new Variant (((Number)value).doubleValue ());
	}
	if (value instanceof Object[]) {
		Object[] arrayValue = (Object[])value;
		int length = arrayValue.length;
		if (length > 0) {
			/* get IHTMLDocument2 */
			IE browser = (IE)((Browser)getParent ().getParent ()).webBrowser;
			OleAutomation auto = browser.auto;
			int[] rgdispid = auto.getIDsOfNames (new String[] {"Document"}); //$NON-NLS-1$
			if (rgdispid == null) return new Variant ();
			Variant pVarResult = auto.getProperty (rgdispid[0]);
			if (pVarResult == null) return new Variant ();
			if (pVarResult.getType () == COM.VT_EMPTY) {
				pVarResult.dispose ();
				return new Variant ();
			}
			OleAutomation document = pVarResult.getAutomation ();
			pVarResult.dispose ();

			/* get IHTMLWindow2 */
			rgdispid = document.getIDsOfNames (new String[] {"parentWindow"}); //$NON-NLS-1$
			if (rgdispid == null) {
				document.dispose ();
				return new Variant ();
			}
			pVarResult = document.getProperty (rgdispid[0]);
			if (pVarResult == null || pVarResult.getType () == COM.VT_EMPTY) {
				if (pVarResult != null) pVarResult.dispose ();
				document.dispose ();
				return new Variant ();	
			}
			OleAutomation ihtmlWindow2 = pVarResult.getAutomation ();
			pVarResult.dispose ();
			document.dispose ();

			/* create a new JS array to be returned */
			rgdispid = ihtmlWindow2.getIDsOfNames (new String[] {"Array"}); //$NON-NLS-1$
			if (rgdispid == null) {
				ihtmlWindow2.dispose ();
				return new Variant ();
			}
			Variant arrayType = ihtmlWindow2.getProperty (rgdispid[0]);
			ihtmlWindow2.dispose ();
			IDispatch arrayTypeDispatch = arrayType.getDispatch ();
			arrayType.dispose ();

			int /*long*/[] result = new int /*long*/[1];
			int rc = arrayTypeDispatch.QueryInterface (COM.IIDIDispatchEx, result);
			if (rc != COM.S_OK) return new Variant ();
			IDispatchEx arrayTypeDispatchEx = new IDispatchEx (result[0]);
			result[0] = 0;
			int /*long*/ resultPtr = OS.GlobalAlloc (OS.GMEM_FIXED | OS.GMEM_ZEROINIT, VARIANT.sizeof);
			DISPPARAMS params = new DISPPARAMS ();
			rc = arrayTypeDispatchEx.InvokeEx (COM.DISPID_VALUE, COM.LOCALE_USER_DEFAULT, COM.DISPATCH_CONSTRUCT, params, resultPtr, null, 0);
			if (rc != COM.S_OK) {
				OS.GlobalFree (resultPtr);
				return new Variant ();	
			}
			Variant array = Variant.win32_new (resultPtr);
			OS.GlobalFree (resultPtr);

			/* populate the array */
			auto = array.getAutomation ();
			int[] rgdispids = auto.getIDsOfNames (new String[] {"push"}); //$NON-NLS-1$
			if (rgdispids != null) {
				for (int i = 0; i < length; i++) {
					Object currentObject = arrayValue[i];
					try {
						Variant variant = convertToJS (currentObject);
						auto.invoke (rgdispids[0], new Variant[] {variant});
						variant.dispose ();
					} catch (SWTException e) {
						/* invalid return value type */
						auto.dispose ();
						array.dispose ();
						throw e;
					}
				}
			}
			auto.dispose ();
			return array;
		}
	}
	SWT.error (SWT.ERROR_INVALID_RETURN_VALUE);
	return null;
}

}

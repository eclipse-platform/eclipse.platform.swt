/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
package org.eclipse.swt.browser;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.ole.win32.*;
import org.eclipse.swt.widgets.*;

class WebSite extends OleControlSite {
	COMObject iDocHostUIHandler;
	COMObject iDocHostShowUI;
	COMObject iServiceProvider;
	COMObject iInternetSecurityManager;
	COMObject iOleCommandTarget;
	COMObject iAuthenticate;
	COMObject iDispatch;
	boolean ignoreNextMessage, ignoreAllMessages;
	boolean isForceTrusted;
	Boolean canExecuteApplets;

	static final int OLECMDID_SHOWSCRIPTERROR = 40;
	static final short [] ACCENTS = new short [] {'~', '`', '\'', '^', '"'};
	static final String CONSUME_KEY = "org.eclipse.swt.OleFrame.ConsumeKey"; //$NON-NLS-1$

public WebSite(Composite parent, int style, String progId) {
	super(parent, style, progId);
}

@Override
protected void createCOMInterfaces () {
	super.createCOMInterfaces();
	iDocHostUIHandler = new COMObject(new int[]{2, 0, 0, 4, 1, 5, 0, 0, 1, 1, 1, 3, 3, 2, 2, 1, 3, 2}){
		@Override
		public long method0(long[] args) {return QueryInterface(args[0], args[1]);}
		@Override
		public long method1(long[] args) {return AddRef();}
		@Override
		public long method2(long[] args) {return Release();}
		@Override
		public long method3(long[] args) {return ShowContextMenu((int)args[0], args[1], args[2], args[3]);}
		@Override
		public long method4(long[] args) {return GetHostInfo(args[0]);}
		@Override
		public long method5(long[] args) {return ShowUI((int)args[0], args[1], args[2], args[3], args[4]);}
		@Override
		public long method6(long[] args) {return HideUI();}
		@Override
		public long method7(long[] args) {return UpdateUI();}
		@Override
		public long method8(long[] args) {return EnableModeless((int)args[0]);}
		@Override
		public long method9(long[] args) {return OnDocWindowActivate((int)args[0]);}
		@Override
		public long method10(long[] args) {return OnFrameWindowActivate((int)args[0]);}
		@Override
		public long method11(long[] args) {return ResizeBorder(args[0], args[1], (int)args[2]);}
		@Override
		public long method12(long[] args) {return TranslateAccelerator(args[0], args[1], (int)args[2]);}
		@Override
		public long method13(long[] args) {return GetOptionKeyPath(args[0], (int)args[1]);}
		@Override
		public long method14(long[] args) {return GetDropTarget(args[0], args[1]);}
		@Override
		public long method15(long[] args) {return GetExternal(args[0]);}
		@Override
		public long method16(long[] args) {return TranslateUrl((int)args[0], args[1], args[2]);}
		@Override
		public long method17(long[] args) {return FilterDataObject(args[0], args[1]);}
	};
	iDocHostShowUI = new COMObject(new int[]{2, 0, 0, 7, 6}){
		@Override
		public long method0(long[] args) {return QueryInterface(args[0], args[1]);}
		@Override
		public long method1(long[] args) {return AddRef();}
		@Override
		public long method2(long[] args) {return Release();}
		@Override
		public long method3(long[] args) {return ShowMessage(args[0], args[1], args[2], (int)args[3], args[4], (int)args[5], args[6]);}
		@Override
		public long method4(long[] args) {return ShowHelp(args[0], args[1], (int)args[2], (int)args[3], args[4], args[5]);}
	};
	iServiceProvider = new COMObject(new int[]{2, 0, 0, 3}){
		@Override
		public long method0(long[] args) {return QueryInterface(args[0], args[1]);}
		@Override
		public long method1(long[] args) {return AddRef();}
		@Override
		public long method2(long[] args) {return Release();}
		@Override
		public long method3(long[] args) {return QueryService(args[0], args[1], args[2]);}
	};
	iInternetSecurityManager = new COMObject(new int[]{2, 0, 0, 1, 1, 3, 4, 8, 7, 3, 3}){
		@Override
		public long method0(long[] args) {return QueryInterface(args[0], args[1]);}
		@Override
		public long method1(long[] args) {return AddRef();}
		@Override
		public long method2(long[] args) {return Release();}
		@Override
		public long method3(long[] args) {return SetSecuritySite(args[0]);}
		@Override
		public long method4(long[] args) {return GetSecuritySite(args[0]);}
		@Override
		public long method5(long[] args) {return MapUrlToZone(args[0], args[1], (int)args[2]);}
		@Override
		public long method6(long[] args) {return GetSecurityId(args[0], args[1], args[2], args[3]);}
		@Override
		public long method7(long[] args) {return ProcessUrlAction(args[0], (int)args[1], args[2], (int)args[3], args[4], (int)args[5], (int)args[6], (int)args[7]);}
		@Override
		public long method8(long[] args) {return QueryCustomPolicy(args[0], args[1], args[2], args[3], args[4], (int)args[5], (int)args[6]);}
		@Override
		public long method9(long[] args) {return SetZoneMapping((int)args[0], args[1], (int)args[2]);}
		@Override
		public long method10(long[] args) {return GetZoneMappings((int)args[0], args[1], (int)args[2]);}
	};
	iOleCommandTarget = new COMObject(new int[]{2, 0, 0, 4, 5}) {
		@Override
		public long method0(long[] args) {return QueryInterface(args[0], args[1]);}
		@Override
		public long method1(long[] args) {return AddRef();}
		@Override
		public long method2(long[] args) {return Release();}
		@Override
		public long method3(long[] args) {return QueryStatus(args[0], (int)args[1], args[2], args[3]);}
		@Override
		public long method4(long[] args) {return Exec(args[0], (int)args[1], (int)args[2], args[3], args[4]);}
	};
	iAuthenticate = new COMObject(new int[]{2, 0, 0, 3}){
		@Override
		public long method0(long[] args) {return QueryInterface(args[0], args[1]);}
		@Override
		public long method1(long[] args) {return AddRef();}
		@Override
		public long method2(long[] args) {return Release();}
		@Override
		public long method3(long[] args) {return Authenticate(args[0], args[1], args[2]);}
	};
	iDispatch = new COMObject (new int[] {2, 0, 0, 1, 3, 5, 8}) {
		@Override
		public long method0 (long[] args) {
			/*
			 * IDispatch check must be done here instead of in the shared QueryInterface
			 * implementation, to avoid answering the superclass's IDispatch implementation
			 * instead of this one.
			 */
			GUID guid = new GUID ();
			COM.MoveMemory (guid, args[0], GUID.sizeof);
			if (COM.IsEqualGUID (guid, COM.IIDIDispatch)) {
				OS.MoveMemory (args[1], new long[] {iDispatch.getAddress ()}, C.PTR_SIZEOF);
				AddRef ();
				return COM.S_OK;
			}
			return QueryInterface (args[0], args[1]);
		}
		@Override
		public long method1 (long[] args) {return AddRef ();}
		@Override
		public long method2 (long[] args) {return Release ();}
		@Override
		public long method3 (long[] args) {return GetTypeInfoCount (args[0]);}
		@Override
		public long method4 (long[] args) {return GetTypeInfo ((int)args[0], (int)args[1], args[2]);}
		@Override
		public long method5 (long[] args) {return GetIDsOfNames ((int)args[0], args[1], (int)args[2], (int)args[3], args[4]);}
		@Override
		public long method6 (long[] args) {return Invoke ((int)args[0], (int)args[1], (int)args[2], (int)args[3], args[4], args[5], args[6], args[7]);}
	};
}

@Override
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
	if (iAuthenticate != null) {
		iAuthenticate.dispose();
		iAuthenticate = null;
	}
	if (iDispatch != null) {
		iDispatch.dispose ();
		iDispatch = null;
	}
}

@Override
protected int AddRef() {
	/* Workaround for javac 1.1.8 bug */
	return super.AddRef();
}

@Override
protected int QueryInterface(long riid, long ppvObject) {
	int result = super.QueryInterface(riid, ppvObject);
	if (result == COM.S_OK) return result;
	if (riid == 0 || ppvObject == 0) return COM.E_INVALIDARG;
	GUID guid = new GUID();
	COM.MoveMemory(guid, riid, GUID.sizeof);
	if (COM.IsEqualGUID(guid, COM.IIDIDocHostUIHandler)) {
		OS.MoveMemory(ppvObject, new long[] {iDocHostUIHandler.getAddress()}, C.PTR_SIZEOF);
		AddRef();
		return COM.S_OK;
	}
	if (COM.IsEqualGUID(guid, COM.IIDIDocHostShowUI)) {
		OS.MoveMemory(ppvObject, new long[] {iDocHostShowUI.getAddress()}, C.PTR_SIZEOF);
		AddRef();
		return COM.S_OK;
	}
	if (COM.IsEqualGUID(guid, COM.IIDIServiceProvider)) {
		OS.MoveMemory(ppvObject, new long[] {iServiceProvider.getAddress()}, C.PTR_SIZEOF);
		AddRef();
		return COM.S_OK;
	}
	if (COM.IsEqualGUID(guid, COM.IIDIInternetSecurityManager)) {
		OS.MoveMemory(ppvObject, new long[] {iInternetSecurityManager.getAddress()}, C.PTR_SIZEOF);
		AddRef();
		return COM.S_OK;
	}
	if (COM.IsEqualGUID(guid, COM.IIDIOleCommandTarget)) {
		OS.MoveMemory(ppvObject, new long[] {iOleCommandTarget.getAddress()}, C.PTR_SIZEOF);
		AddRef();
		return COM.S_OK;
	}
	OS.MoveMemory(ppvObject, new long[] {0}, C.PTR_SIZEOF);
	return COM.E_NOINTERFACE;
}

/* IDocHostUIHandler */

int EnableModeless(int EnableModeless) {
	return COM.E_NOTIMPL;
}

int FilterDataObject(long pDO, long ppDORet) {
	return COM.E_NOTIMPL;
}

int GetDropTarget(long pDropTarget, long ppDropTarget) {
	return COM.E_NOTIMPL;
}

int GetExternal(long ppDispatch) {
	OS.MoveMemory (ppDispatch, new long[] {iDispatch.getAddress()}, C.PTR_SIZEOF);
	AddRef ();
	return COM.S_OK;
}

int GetHostInfo(long pInfo) {
	int info = IE.DOCHOSTUIFLAG_THEME | IE.DOCHOSTUIFLAG_ENABLE_REDIRECT_NOTIFICATION | IE.DOCHOSTUIFLAG_DPI_AWARE;
	IE browser = (IE)((Browser)getParent().getParent()).webBrowser;
	if ((browser.style & SWT.BORDER) == 0) info |= IE.DOCHOSTUIFLAG_NO3DOUTERBORDER;
	DOCHOSTUIINFO uiInfo = new DOCHOSTUIINFO ();
	OS.MoveMemory(uiInfo, pInfo, DOCHOSTUIINFO.sizeof);
	uiInfo.dwFlags = info;
	OS.MoveMemory(pInfo, uiInfo, DOCHOSTUIINFO.sizeof);
	return COM.S_OK;
}

int GetOptionKeyPath(long pchKey, int dw) {
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

@Override
protected int Release() {
	/* Workaround for javac 1.1.8 bug */
	return super.Release();
}

int ResizeBorder(long prcBorder, long pUIWindow, int fFrameWindow) {
	return COM.E_NOTIMPL;
}

int ShowContextMenu(int dwID, long ppt, long pcmdtReserved, long pdispReserved) {
	Browser browser = (Browser)getParent().getParent();
	Event event = new Event();
	POINT pt = new POINT();
	OS.MoveMemory(pt, ppt, POINT.sizeof);
	pt.x = DPIUtil.scaleDown(pt.x, DPIUtil.getDeviceZoom()); // To Points
	pt.y = DPIUtil.scaleDown(pt.y, DPIUtil.getDeviceZoom()); // To Points
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

int ShowUI(int dwID, long pActiveObject, long pCommandTarget, long pFrame, long pDoc) {
	return COM.S_FALSE;
}

int TranslateAccelerator(long lpMsg, long pguidCmdGroup, int nCmdID) {
	/*
	* Feature in Internet Explorer.  By default the embedded Internet Explorer control runs
	* the Internet Explorer shortcuts (e.g. Ctrl+F for Find).  This overrides the shortcuts
	* defined by SWT.  The workaround is to forward the accelerator keys to the parent window
	* and have Internet Explorer ignore the ones handled by the parent window.
	*/
	Menu menubar = getShell().getMenuBar();
	if (menubar != null && !menubar.isDisposed() && menubar.isEnabled()) {
		Shell shell = menubar.getShell();
		long hwnd = shell.handle;
		long hAccel = OS.SendMessage(hwnd, OS.WM_APP+1, 0, 0);
		if (hAccel != 0) {
			MSG msg = new MSG();
			OS.MoveMemory(msg, lpMsg, MSG.sizeof);
			if (OS.TranslateAccelerator(hwnd, hAccel, msg) != 0) return COM.S_OK;
		}
	}
	/*
	* By default the IE shortcuts are run.  However, the shortcuts below should not run
	* in this context.  The workaround is to block IE from handling these shortcuts by
	* answering COM.S_OK.
	*
	* - F5 causes a refresh, which is not appropriate when rendering HTML from memory
	* - CTRL+L and CTRL+O show an Open Location dialog in IE8, which is undesirable and
	* can crash in some contexts
	* - CTRL+N opens a standalone IE, which is undesirable and can crash in some contexts
	*/
	int result = COM.S_FALSE;
	MSG msg = new MSG();
	OS.MoveMemory(msg, lpMsg, MSG.sizeof);
	if (msg.message == OS.WM_KEYDOWN) {
		switch ((int)msg.wParam) {
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
			case OS.VK_UP:
			case OS.VK_DOWN:
			case OS.VK_LEFT:
			case OS.VK_RIGHT:
			case OS.VK_HOME:
			case OS.VK_END:
			case OS.VK_PRIOR:
			case OS.VK_NEXT:
				/* Do not translate/consume IE's keys for scrolling content. */
				break;
			case OS.VK_SPACE:
			case OS.VK_BACK:
			case OS.VK_RETURN:
				/*
				 * Translating OS.VK_BACK, OS.VK_RETURN or OS.VK_SPACE results in the native
				 * control handling them twice (eg.- inserting two lines instead of one). So
				 * these keys are not translated here, and instead are explicitly handled
				 * in the keypress handler.
				 */
				break;
			case OS.VK_L:
			case OS.VK_N:
			case OS.VK_O:
				if (OS.GetKeyState (OS.VK_CONTROL) < 0 && OS.GetKeyState (OS.VK_MENU) >= 0 && OS.GetKeyState (OS.VK_SHIFT) >= 0) {
					if (msg.wParam == OS.VK_N || IE.IEVersion >= 8) {
						frame.setData(CONSUME_KEY, "false"); //$NON-NLS-1$
						result = COM.S_OK;
						break;
					}
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
			boolean isAccent = false;
			switch ((int)msg.wParam) {
				case OS.VK_SHIFT:
				case OS.VK_MENU:
				case OS.VK_CONTROL:
				case OS.VK_CAPITAL:
				case OS.VK_NUMLOCK:
				case OS.VK_SCROLL:
					break;
				default: {
					int mapKey = OS.MapVirtualKey ((int)msg.wParam, 2);
					if (mapKey != 0) {
						isAccent = (mapKey & 0x80000000) != 0;
						if (!isAccent) {
							for (short element : ACCENTS) {
								int value = OS.VkKeyScan (element);
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
			}
			if (isAccent) result = COM.S_OK;
		}
	}
	return result;
}

int TranslateUrl(int dwTranslate, long pchURLIn, long ppchURLOut) {
	return COM.E_NOTIMPL;
}

int UpdateUI() {
	return COM.E_NOTIMPL;
}

/* IDocHostShowUI */

int ShowMessage(long hwnd, long lpstrText, long lpstrCaption, int dwType, long lpstrHelpFile, int dwHelpContext, long plResult) {
	boolean ignore = ignoreNextMessage || ignoreAllMessages;
	ignoreNextMessage = false;
	return ignore ? COM.S_OK : COM.S_FALSE;
}

int ShowHelp(long hwnd, long pszHelpFile, int uCommand, int dwData, long pt, long pDispatchObjectHit) {
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

int QueryService(long guidService, long riid, long ppvObject) {
	if (riid == 0 || ppvObject == 0) return COM.E_INVALIDARG;
	GUID guid = new GUID();
	COM.MoveMemory(guid, riid, GUID.sizeof);
	if (COM.IsEqualGUID(guid, COM.IIDIInternetSecurityManager)) {
		OS.MoveMemory(ppvObject, new long[] {iInternetSecurityManager.getAddress()}, C.PTR_SIZEOF);
		AddRef();
		return COM.S_OK;
	}
	if (COM.IsEqualGUID(guid, COM.IIDIAuthenticate)) {
		OS.MoveMemory(ppvObject, new long[] {iAuthenticate.getAddress()}, C.PTR_SIZEOF);
		AddRef();
		return COM.S_OK;
	}
	OS.MoveMemory(ppvObject, new long[] {0}, C.PTR_SIZEOF);
	return COM.E_NOINTERFACE;
}

/* IInternetSecurityManager */

int SetSecuritySite(long pSite) {
	return IE.INET_E_DEFAULT_ACTION;
}

int GetSecuritySite(long ppSite) {
	return IE.INET_E_DEFAULT_ACTION;
}

int MapUrlToZone(long pwszUrl, long pdwZone, int dwFlags) {
	/*
	* Feature in IE.  HTML rendered in memory does not enable local links
	* but the same HTML document loaded through a local file is permitted
	* to follow local links.  The workaround is to return URLZONE_INTRANET
	* instead of the default value URLZONE_LOCAL_MACHINE.
	*/
	if (isForceTrusted) {
		OS.MoveMemory(pdwZone, new int[] {IE.URLZONE_INTRANET}, 4);
		return COM.S_OK;
	}
	return IE.INET_E_DEFAULT_ACTION;
}

int GetSecurityId(long pwszUrl, long pbSecurityId, long pcbSecurityId, long dwReserved) {
	return IE.INET_E_DEFAULT_ACTION;
}

int ProcessUrlAction(long pwszUrl, int dwAction, long pPolicy, int cbPolicy, long pContext, int cbContext, int dwFlags, int dwReserved) {
	ignoreNextMessage = false;

	/*
	* If the current page is about:blank and is trusted then
	* override default zone elevation settings to allow the action.
	*/
	if (dwAction == IE.URLACTION_FEATURE_ZONE_ELEVATION) {
		IE ie = (IE)((Browser)getParent().getParent()).webBrowser;
		if (ie.auto != null && ie._getUrl().startsWith(IE.ABOUT_BLANK) && !ie.untrustedText) {
			if (cbPolicy >= 4) OS.MoveMemory(pPolicy, new int[] {IE.URLPOLICY_ALLOW}, 4);
			return COM.S_OK;
		}
	}

	int policy = IE.INET_E_DEFAULT_ACTION;

	if (dwAction >= IE.URLACTION_JAVA_MIN && dwAction <= IE.URLACTION_JAVA_MAX) {
		if (canExecuteApplets ()) {
			policy = IE.URLPOLICY_JAVA_LOW;
		} else {
			policy = IE.URLPOLICY_JAVA_PROHIBIT;
			ignoreNextMessage = true;
		}
	}
	if (dwAction == IE.URLACTION_ACTIVEX_RUN && pContext != 0) {
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
		policy = browser.jsEnabled ? IE.URLPOLICY_ALLOW : IE.URLPOLICY_DISALLOW;
	}

	if (policy == IE.INET_E_DEFAULT_ACTION) return IE.INET_E_DEFAULT_ACTION;
	if (cbPolicy >= 4) OS.MoveMemory(pPolicy, new int[] {policy}, 4);
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
	if (IE.IEVersion < 7) return false;

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

int QueryCustomPolicy(long pwszUrl, long guidKey, long ppPolicy, long pcbPolicy, long pContext, int cbContext, int dwReserved) {
	return IE.INET_E_DEFAULT_ACTION;
}

int SetZoneMapping(int dwZone, long lpszPattern, int dwFlags) {
	return IE.INET_E_DEFAULT_ACTION;
}

int GetZoneMappings(int dwZone, long ppenumString, int dwFlags) {
	return COM.E_NOTIMPL;
}

/* IOleCommandTarget */
int QueryStatus(long pguidCmdGroup, int cCmds, long prgCmds, long pCmdText) {
	return COM.E_NOTSUPPORTED;
}

int Exec(long pguidCmdGroup, int nCmdID, int nCmdExecOpt, long pvaIn, long pvaOut) {
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

/* IAuthenticate */

int Authenticate (long hwnd, long szUsername, long szPassword) {
	IE browser = (IE)((Browser)getParent ().getParent ()).webBrowser;
	for (AuthenticationListener authenticationListener : browser.authenticationListeners) {
		AuthenticationEvent event = new AuthenticationEvent (browser.browser);
		event.location = browser.lastNavigateURL;
		authenticationListener.authenticate (event);
		if (!event.doit) return COM.E_ACCESSDENIED;
		if (event.user != null && event.password != null) {
			TCHAR user = new TCHAR (0, event.user, true);
			int size = user.length () * TCHAR.sizeof;
			long userPtr = OS.CoTaskMemAlloc (size);
			OS.MoveMemory (userPtr, user, size);
			TCHAR password = new TCHAR (0, event.password, true);
			size = password.length () * TCHAR.sizeof;
			long passwordPtr = OS.CoTaskMemAlloc (size);
			OS.MoveMemory (passwordPtr, password, size);
			C.memmove (hwnd, new long[] {0}, C.PTR_SIZEOF);
			C.memmove (szUsername, new long[] {userPtr}, C.PTR_SIZEOF);
			C.memmove (szPassword, new long[] {passwordPtr}, C.PTR_SIZEOF);
			return COM.S_OK;
		}
	}

	/* no listener handled the challenge, so defer to the native dialog */
	C.memmove (hwnd, new long[] {getShell().handle}, C.PTR_SIZEOF);
	return COM.S_OK;
}

/* IDispatch */

int GetTypeInfoCount (long pctinfo) {
	C.memmove (pctinfo, new int[] {0}, 4);
	return COM.S_OK;
}

int GetTypeInfo (int iTInfo, int lcid, long ppTInfo) {
	return COM.S_OK;
}

int GetIDsOfNames (int riid, long rgszNames, int cNames, int lcid, long rgDispId) {
	long[] ptr = new long[1];
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

int Invoke (int dispIdMember, long riid, int lcid, int dwFlags, long pDispParams, long pVarResult, long pExcepInfo, long pArgErr) {
	IE ie = (IE)((Browser)getParent ().getParent ()).webBrowser;
	Map<Integer, BrowserFunction> functions = ie.functions;
	if (functions == null) {
		if (pVarResult != 0) {
			OS.MoveMemory (pVarResult, new long[] {0}, C.PTR_SIZEOF);
		}
		return COM.S_OK;
	}

	DISPPARAMS dispParams = new DISPPARAMS ();
	COM.MoveMemory (dispParams, pDispParams, DISPPARAMS.sizeof);
	if (dispParams.cArgs != 3) {
		if (pVarResult != 0) {
			OS.MoveMemory (pVarResult, new long[] {0}, C.PTR_SIZEOF);
		}
		return COM.S_OK;
	}

	long ptr = dispParams.rgvarg + 2 * Variant.sizeof;
	Variant variant = Variant.win32_new (ptr);
	if (variant.getType () != COM.VT_I4) {
		variant.dispose ();
		if (pVarResult != 0) {
			OS.MoveMemory (pVarResult, new long[] {0}, C.PTR_SIZEOF);
		}
		return COM.S_OK;
	}
	int index = variant.getInt ();
	variant.dispose ();
	if (index <= 0) {
		if (pVarResult != 0) {
			OS.MoveMemory (pVarResult, new long[] {0}, C.PTR_SIZEOF);
		}
		return COM.S_OK;
	}

	ptr = dispParams.rgvarg + Variant.sizeof;
	variant = Variant.win32_new (ptr);
	int type = variant.getType ();
	if (type != COM.VT_BSTR) {
		variant.dispose ();
		if (pVarResult != 0) {
			OS.MoveMemory (pVarResult, new long[] {0}, C.PTR_SIZEOF);
		}
		return COM.S_OK;
	}
	String token = variant.getString ();
	variant.dispose ();

	variant = Variant.win32_new (dispParams.rgvarg);
	BrowserFunction function = functions.get (index);
	Object returnValue = null;
	if (function != null && token.equals (function.token)) {
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
		try {
			variant = convertToJS (returnValue);
		} catch (SWTException e) {
			/* invalid return value type */
			variant = convertToJS (WebBrowser.CreateErrorString (e.getLocalizedMessage ()));
		}
		Variant.win32_copy (pVarResult, variant);
		variant.dispose ();
	}
	return COM.S_OK;
}

Object convertToJava (Variant variant) {
	switch (variant.getType ()) {
		case OLE.VT_EMPTY:
		case OLE.VT_NULL: return null;
		case OLE.VT_BSTR: return variant.getString ();
		case OLE.VT_BOOL: return variant.getBoolean ();
		case OLE.VT_I2:
		case OLE.VT_I4:
		case OLE.VT_I8:
		case OLE.VT_R4:
		case OLE.VT_R8:
			return variant.getDouble ();
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
		return Variant.NULL;
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
		long[] result = new long[1];
		int rc = arrayTypeDispatch.QueryInterface (COM.IIDIDispatchEx, result);
		arrayType.dispose ();
		if (rc != COM.S_OK) return new Variant ();

		IDispatchEx arrayTypeDispatchEx = new IDispatchEx (result[0]);
		result[0] = 0;
		long resultPtr = OS.GlobalAlloc (OS.GMEM_FIXED | OS.GMEM_ZEROINIT, VARIANT.sizeof);
		DISPPARAMS params = new DISPPARAMS ();
		rc = arrayTypeDispatchEx.InvokeEx (COM.DISPID_VALUE, COM.LOCALE_USER_DEFAULT, COM.DISPATCH_CONSTRUCT, params, resultPtr, null, 0);
		if (rc != COM.S_OK) {
			OS.GlobalFree (resultPtr);
			return new Variant ();
		}
		Variant array = Variant.win32_new (resultPtr);
		OS.GlobalFree (resultPtr);

		/* populate the array */
		Object[] arrayValue = (Object[])value;
		int length = arrayValue.length;
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
	SWT.error (SWT.ERROR_INVALID_RETURN_VALUE);
	return null;
}

}

/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
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
package org.eclipse.swt.ole.win32;

import java.util.*;
import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.widgets.*;

/**
 *
 * OleFrame is an OLE Container's top level frame.
 *
 * <p>This object implements the OLE Interfaces IUnknown and IOleInPlaceFrame
 *
 * <p>OleFrame allows the container to do the following: <ul>
 *	<li>position and size the ActiveX Control or OLE Document within the application
 *	<li>insert menu items from the application into the OLE Document's menu
 *	<li>activate and deactivate the OLE Document's menus
 *	<li>position the OLE Document's menu in the application
 *	<li>translate accelerator keystrokes intended for the container's frame</ul>
 *
 * <dl>
 *	<dt><b>Styles</b> <dd>BORDER
 *	<dt><b>Events</b> <dd>Dispose, Move, Resize
 * </dl>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#ole">OLE and ActiveX snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Examples: OLEExample, OleWebBrowser</a>
 */
final public class OleFrame extends Composite
{
	// Interfaces for this Ole Client Container
	private COMObject iOleInPlaceFrame;

	// Access to the embedded/linked Ole Object
	private IOleInPlaceActiveObject objIOleInPlaceActiveObject;

	private OleClientSite currentdoc;

	private int refCount = 0;

	private MenuItem[] fileMenuItems;
	private MenuItem[] containerMenuItems;
	private MenuItem[] windowMenuItems;

	private Listener listener;

	private long shellHandle;
	private long oldMenuHandle;
	private long newMenuHandle;
	private static long lastActivatedMenuHandle;

	private static String CHECK_FOCUS = "OLE_CHECK_FOCUS"; //$NON-NLS-1$
	private static String HHOOK = "OLE_HHOOK"; //$NON-NLS-1$
	private static String HHOOKMSG = "OLE_HHOOK_MSG"; //$NON-NLS-1$

	private static boolean ignoreNextKey;
	private static final short [] ACCENTS = new short [] {'~', '`', '\'', '^', '"'};

	private static final String CONSUME_KEY = "org.eclipse.swt.OleFrame.ConsumeKey"; //$NON-NLS-1$
	private static final String ACCEL_KEY_HIT = "org.eclipse.swt.internal.win32.accelKeyHit"; //$NON-NLS-1$

/**
 * Create an OleFrame child widget using style bits
 * to select a particular look or set of properties.
 *
 * @param parent a composite widget (cannot be null)
 * @param style the bitwise OR'ing of widget styles
 *
 * @exception IllegalArgumentException <ul>
 *     <li>ERROR_NULL_ARGUMENT when the parent is null
 * </ul>
 * @exception SWTException <ul>
 *     <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread
 * </ul>
 *
 */
public OleFrame(Composite parent, int style) {
	super(parent, style);

	createCOMInterfaces();

	// setup cleanup proc
	listener = e -> {
		switch (e.type) {
		case SWT.Activate :    onActivate(e); break;
		case SWT.Deactivate :  onDeactivate(e); break;
		case SWT.Dispose :  onDispose(e); break;
		case SWT.Resize :
		case SWT.Move :     onResize(e); break;
		default :
			OLE.error(SWT.ERROR_NOT_IMPLEMENTED);
		}
	};


	addListener(SWT.Activate, listener);
	addListener(SWT.Deactivate, listener);
	addListener(SWT.Dispose, listener);

	// inform inplaceactiveobject whenever frame resizes
	addListener(SWT.Resize, listener);

	// inform inplaceactiveobject whenever frame moves
	addListener(SWT.Move, listener);

	// Maintain a reference to yourself so that when
	// ClientSites close, they don't take the frame away
	// with them.
	this.AddRef();

	// Check for focus change
	Display display = getDisplay();
	initCheckFocus(display);
	initMsgHook(display);
}
private static void initCheckFocus (final Display display) {
	if (display.getData(CHECK_FOCUS) != null) return;
	display.setData(CHECK_FOCUS, CHECK_FOCUS);
	final int time = 50;
	final Runnable[] timer = new Runnable[1];
	final Control[] lastFocus = new Control[1];
	timer[0] = () -> {
		if (lastFocus[0] instanceof OleClientSite && !lastFocus[0].isDisposed()) {
			// ignore popup menus and dialogs
			long hwnd = OS.GetFocus();
			while (hwnd != 0) {
				long ownerHwnd = OS.GetWindow(hwnd, OS.GW_OWNER);
				if (ownerHwnd != 0) {
					display.timerExec(time, timer[0]);
					return;
				}
				hwnd = OS.GetParent(hwnd);
			}
		}
		if (lastFocus[0] == null || lastFocus[0].isDisposed() || !lastFocus[0].isFocusControl()) {
			Control currentFocus = display.getFocusControl();
			if (currentFocus instanceof OleFrame) {
				OleFrame frame = (OleFrame) currentFocus;
				currentFocus = frame.getCurrentDocument();
			}
			if (lastFocus[0] != currentFocus) {
				Event event = new Event();
				if (lastFocus[0] instanceof OleClientSite && !lastFocus[0].isDisposed()) {
					lastFocus[0].notifyListeners (SWT.FocusOut, event);
				}
				if (currentFocus instanceof OleClientSite && !currentFocus.isDisposed()) {
					currentFocus.notifyListeners(SWT.FocusIn, event);
				}
			}
			lastFocus[0] = currentFocus;
		}
		display.timerExec(time, timer[0]);
	};
	display.timerExec(time, timer[0]);
}
private static void initMsgHook(Display display) {
	if (display.getData(HHOOK) != null) return;
	final Callback callback = new Callback(OleFrame.class, "getMsgProc", 3); //$NON-NLS-1$
	long address = callback.getAddress();
	int threadId = OS.GetCurrentThreadId();
	final long hHook = OS.SetWindowsHookEx(OS.WH_GETMESSAGE, address, 0, threadId);
	if (hHook == 0) {
		callback.dispose();
		return;
	}
	display.setData(HHOOK, new LONG(hHook));
	display.setData(HHOOKMSG, new MSG());
	display.disposeExec(() -> {
		if (hHook != 0) OS.UnhookWindowsHookEx(hHook);
		if (callback != null) callback.dispose();
	});
}
static long getMsgProc(long code, long wParam, long lParam) {
	Display display = Display.getCurrent();
	if (display == null) return 0;
	LONG hHook = (LONG)display.getData(HHOOK);
	if (hHook == null) return 0;
	if (code < 0 || (wParam & OS.PM_REMOVE) == 0) {
		return OS.CallNextHookEx(hHook.value, (int)code, wParam, lParam);
	}
	MSG msg = (MSG)display.getData(HHOOKMSG);
	OS.MoveMemory(msg, lParam, MSG.sizeof);
	int message = msg.message;
	if (OS.WM_KEYFIRST <= message && message <= OS.WM_KEYLAST) {
		if (display != null) {
			Widget widget = null;
			long hwnd = msg.hwnd;
			while (hwnd != 0) {
				widget = display.findWidget (hwnd);
				if (widget != null) break;
				hwnd = OS.GetParent (hwnd);
			}
			if (widget instanceof OleClientSite) {
				OleClientSite site = (OleClientSite)widget;
				if (site.handle == hwnd) {
					boolean consumed = false;
					/* Allow activeX control to translate accelerators except when a menu is active. */
					int thread = OS.GetWindowThreadProcessId(msg.hwnd, null);
					GUITHREADINFO  lpgui = new GUITHREADINFO();
					lpgui.cbSize = GUITHREADINFO.sizeof;
					boolean rc = OS.GetGUIThreadInfo(thread, lpgui);
					int mask = OS.GUI_INMENUMODE | OS.GUI_INMOVESIZE | OS.GUI_POPUPMENUMODE | OS.GUI_SYSTEMMENUMODE;
					if (!rc || (lpgui.flags & mask) == 0) {
						OleFrame frame = site.frame;
						frame.setData(CONSUME_KEY, null);
						display.setData(ACCEL_KEY_HIT, Boolean.TRUE);
						consumed = frame.translateOleAccelerator(msg);
						/* translateOleAccelerator() may send client events, ensure that the frame and display are still valid */
						if (display.isDisposed()) return 0;
						display.setData(ACCEL_KEY_HIT, Boolean.FALSE);
						if (frame.isDisposed()) return 0;
						String value = (String)frame.getData(CONSUME_KEY);
						if (value != null) consumed = value.equals("true"); //$NON-NLS-1$
						frame.setData(CONSUME_KEY, null);
					}
					boolean accentKey = false;
					switch (msg.message) {
						case OS.WM_KEYDOWN:
						case OS.WM_SYSKEYDOWN: {
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
										accentKey = (mapKey & 0x80000000) != 0;
										if (!accentKey) {
											for (short element : ACCENTS) {
												int value = OS.VkKeyScan (element);
												if (value != -1 && (value & 0xFF) == msg.wParam) {
													int state = value >> 8;
													if ((OS.GetKeyState (OS.VK_SHIFT) < 0) == ((state & 0x1) != 0) &&
														(OS.GetKeyState (OS.VK_CONTROL) < 0) == ((state & 0x2) != 0) &&
														(OS.GetKeyState (OS.VK_MENU) < 0) == ((state & 0x4) != 0)) {
															if ((state & 0x7) != 0) accentKey = true;
															break;
													}
												}
											}
										}
									}
									break;
								}
							}
							break;
						}
					}
					/* Allow OleClientSite to process key events before activeX control */
					if (!consumed && !accentKey && !ignoreNextKey) {
						long hwndOld = msg.hwnd;
						msg.hwnd = site.handle;
						consumed = OS.DispatchMessage (msg) == 1;
						msg.hwnd = hwndOld;
					}
					switch (msg.message) {
						case OS.WM_KEYDOWN:
						case OS.WM_SYSKEYDOWN: {
							switch ((int)msg.wParam) {
								case OS.VK_SHIFT:
								case OS.VK_MENU:
								case OS.VK_CONTROL:
								case OS.VK_CAPITAL:
								case OS.VK_NUMLOCK:
								case OS.VK_SCROLL:
									break;
								default: {
									ignoreNextKey = accentKey;
									break;
								}
							}
						}
					}

					if (consumed) {
						// In order to prevent this message from also being processed
						// by the application, zero out message, wParam and lParam
						msg.message = OS.WM_NULL;
						msg.wParam = msg.lParam = 0;
						OS.MoveMemory(lParam, msg, MSG.sizeof);
						return 0;
					}
				}
			}
		}
	}
	return OS.CallNextHookEx(hHook.value, (int)code, wParam, lParam);
}
/**
 * Increment the count of references to this instance
 *
 * @return the current reference count
 */
int AddRef() {
	refCount++;
	return refCount;
}
private int ContextSensitiveHelp(int fEnterMode) {
	return COM.S_OK;
}
private void createCOMInterfaces() {
	iOleInPlaceFrame = new COMObject(new int[]{2, 0, 0, 1, 1, 1, 1, 1, 2, 2, 3, 1, 1, 1, 2}){
		@Override
		public long method0(long[] args) {return QueryInterface(args[0], args[1]);}
		@Override
		public long method1(long[] args) {return AddRef();}
		@Override
		public long method2(long[] args) {return Release();}
		@Override
		public long method3(long[] args) {return GetWindow(args[0]);}
		@Override
		public long method4(long[] args) {return ContextSensitiveHelp((int)args[0]);}
		@Override
		public long method5(long[] args) {return GetBorder(args[0]);}
		@Override
		public long method6(long[] args) {return RequestBorderSpace(args[0]);}
		@Override
		public long method7(long[] args) {return SetBorderSpace(args[0]);}
		@Override
		public long method8(long[] args) {return SetActiveObject(args[0], args[1]);}
		@Override
		public long method9(long[] args) {return InsertMenus(args[0], args[1]);}
		@Override
		public long method10(long[] args) {return SetMenu(args[0], args[1], args[2]);}
		@Override
		public long method11(long[] args) {return RemoveMenus(args[0]);}
		// method12 SetStatusText - not implemented
		// method13 EnableModeless - not implemented
		@Override
		public long method14(long[] args) {return TranslateAccelerator(args[0], (int)args[1]);}
	};
}
private void disposeCOMInterfaces () {
	if (iOleInPlaceFrame != null)
		iOleInPlaceFrame.dispose();
	iOleInPlaceFrame = null;
}
private int GetBorder(long lprectBorder) {
	/*
	The IOleInPlaceUIWindow::GetBorder function, when called on a document or frame window
	object, returns the outer rectangle (relative to the window) where the object can put
	toolbars or similar controls.
	*/
	if (lprectBorder == 0) return COM.E_INVALIDARG;
	RECT rectBorder = new RECT();
	// Coordinates must be relative to the window
	OS.GetClientRect(handle, rectBorder);
	OS.MoveMemory(lprectBorder, rectBorder, RECT.sizeof);
	return COM.S_OK;
}
/**
 *
 * Returns the application menu items that will appear in the Container location when an OLE Document
 * is in-place activated.
 *
 * <p>When an OLE Document is in-place active, the Document provides its own menus but the application
 * is given the opportunity to merge some of its menus into the menubar.  The application
 * is allowed to insert its menus in three locations: File (far left), Container(middle) and Window
 * (far right just before Help).  The OLE Document retains control of the Edit, Object and Help
 * menu locations.  Note that an application can insert more than one menu into a single location.
 *
 * @return the application menu items that will appear in the Container location when an OLE Document
 *         is in-place activated.
 *
 */
public MenuItem[] getContainerMenus(){
	return containerMenuItems;
}
/**
 *
 * Returns the application menu items that will appear in the File location when an OLE Document
 * is in-place activated.
 *
 * <p>When an OLE Document is in-place active, the Document provides its own menus but the application
 * is given the opportunity to merge some of its menus into the menubar.  The application
 * is allowed to insert its menus in three locations: File (far left), Container(middle) and Window
 * (far right just before Help).  The OLE Document retains control of the Edit, Object and Help
 * menu locations.  Note that an application can insert more than one menu into a single location.
 *
 * @return the application menu items that will appear in the File location when an OLE Document
 *         is in-place activated.
 *
 */
public MenuItem[] getFileMenus(){
	return fileMenuItems;
}
long getIOleInPlaceFrame() {
	return iOleInPlaceFrame.getAddress();
}
private long getMenuItemID(long hMenu, int index) {
	long id = 0;
	MENUITEMINFO lpmii = new MENUITEMINFO();
	lpmii.cbSize = MENUITEMINFO.sizeof;
	lpmii.fMask = OS.MIIM_STATE | OS.MIIM_SUBMENU | OS.MIIM_ID;
	OS.GetMenuItemInfo(hMenu, index, true, lpmii);
	if ((lpmii.fState & OS.MF_POPUP) == OS.MF_POPUP) {
		id = lpmii.hSubMenu;
	} else {
		id = lpmii.wID;
	}
	return id;
}
private int GetWindow(long phwnd) {
	if (phwnd != 0) {
		OS.MoveMemory(phwnd, new long[] {handle}, C.PTR_SIZEOF);
	}
	return COM.S_OK;
}
/**
 *
 * Returns the application menu items that will appear in the Window location when an OLE Document
 * is in-place activated.
 *
 * <p>When an OLE Document is in-place active, the Document provides its own menus but the application
 * is given the opportunity to merge some of its menus into the menubar.  The application
 * is allowed to insert its menus in three locations: File (far left), Container(middle) and Window
 * (far right just before Help).  The OLE Document retains control of the Edit, Object and Help
 * menu locations.  Note that an application can insert more than one menu into a single location.
 *
 * @return the application menu items that will appear in the Window location when an OLE Document
 *         is in-place activated.
 *
 */
public MenuItem[] getWindowMenus(){
	return windowMenuItems;
}
private int InsertMenus(long hmenuShared, long lpMenuWidths) {
	// locate menu bar
	Menu menubar = getShell().getMenuBar();
	if (menubar == null || menubar.isDisposed()) {
		OS.MoveMemory(lpMenuWidths, new int[] {0}, 4);
		return COM.S_OK;
	}
	long hMenu = menubar.handle;

	// Create a holder for menu information.  This will be passed down to
	// the OS and the OS will fill in the requested information for each menu.
	MENUITEMINFO lpmii = new MENUITEMINFO();
	long hHeap = OS.GetProcessHeap();
	int cch = 128;
	int byteCount = cch * TCHAR.sizeof;
	long pszText = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
	lpmii.cbSize = MENUITEMINFO.sizeof;
	lpmii.fMask = OS.MIIM_STATE | OS.MIIM_ID | OS.MIIM_TYPE | OS.MIIM_SUBMENU | OS.MIIM_DATA;
	lpmii.dwTypeData = pszText;
	lpmii.cch = cch;

	// Loop over all "File-like" menus in the menubar and get information about the
	// item from the OS.
	int fileMenuCount = 0;
	int newindex = 0;
	if (this.fileMenuItems != null) {
		for (MenuItem item : this.fileMenuItems) {
			if (item != null) {
				int index = item.getParent().indexOf(item);
				lpmii.cch = cch;  // lpmii.cch gets updated by GetMenuItemInfo to indicate the
				                  // exact number of characters in name.  Reset it to our max size
				                  // before each call.
				if (OS.GetMenuItemInfo(hMenu, index, true, lpmii)) {
					if (OS.InsertMenuItem(hmenuShared, newindex, true, lpmii)) {
						// keep track of the number of items
						fileMenuCount++;
						newindex++;
					}
				}
			}
		}
	}

	// copy the menu item count information to the pointer
	OS.MoveMemory(lpMenuWidths, new int[] {fileMenuCount}, 4);

	// Loop over all "Container-like" menus in the menubar and get information about the
	// item from the OS.
	int containerMenuCount = 0;
	if (this.containerMenuItems != null) {
		for (MenuItem item : this.containerMenuItems) {
			if (item != null) {
				int index = item.getParent().indexOf(item);
				lpmii.cch = cch; // lpmii.cch gets updated by GetMenuItemInfo to indicate the
				                           // exact number of characters in name.  Reset it to a large number
				                           // before each call.
				if (OS.GetMenuItemInfo(hMenu, index, true, lpmii)) {
					if (OS.InsertMenuItem(hmenuShared, newindex, true, lpmii)) {
						// keep track of the number of items
						containerMenuCount++;
						newindex++;
					}
				}
			}
		}
	}

	// copy the menu item count information to the pointer
	OS.MoveMemory(lpMenuWidths + 8, new int[] {containerMenuCount}, 4);

	// Loop over all "Window-like" menus in the menubar and get information about the
	// item from the OS.
	int windowMenuCount = 0;
	if (this.windowMenuItems != null) {
		for (MenuItem item : this.windowMenuItems) {
			if (item != null) {
				int index = item.getParent().indexOf(item);
				lpmii.cch = cch; // lpmii.cch gets updated by GetMenuItemInfo to indicate the
				                           // exact number of characters in name.  Reset it to a large number
				                           // before each call.
				if (OS.GetMenuItemInfo(hMenu, index, true, lpmii)) {
					if (OS.InsertMenuItem(hmenuShared, newindex, true, lpmii)) {
						// keep track of the number of items
						windowMenuCount++;
						newindex++;
					}
				}
			}
		}
	}

	// copy the menu item count information to the pointer
	OS.MoveMemory(lpMenuWidths + 16, new int[] {windowMenuCount}, 4);

	// free resources used in querying the OS
	if (pszText != 0)
		OS.HeapFree(hHeap, 0, pszText);
	return COM.S_OK;
}
void onActivate(Event e) {
	if (objIOleInPlaceActiveObject != null) {
		objIOleInPlaceActiveObject.OnFrameWindowActivate(true);
	}
}
void onDeactivate(Event e) {
	if (objIOleInPlaceActiveObject != null) {
		objIOleInPlaceActiveObject.OnFrameWindowActivate(false);
	}
}
private void onDispose(Event e) {

	releaseObjectInterfaces();
	currentdoc = null;

	this.Release();
	removeListener(SWT.Activate, listener);
	removeListener(SWT.Deactivate, listener);
	removeListener(SWT.Dispose, listener);
	removeListener(SWT.Resize, listener);
	removeListener(SWT.Move, listener);
}
void onFocusIn(Event e) {
	if (lastActivatedMenuHandle != newMenuHandle && currentdoc != null) {
		currentdoc.doVerb(OLE.OLEIVERB_SHOW);
	}
	if (OS.GetMenu(shellHandle) != newMenuHandle) {
		OS.SetMenu(shellHandle, newMenuHandle);
	}
}
void onFocusOut(Event e) {
	Control control = getDisplay().getFocusControl();
	if (OS.GetMenu(shellHandle) != oldMenuHandle && control != null && control.handle != shellHandle)
		OS.SetMenu(shellHandle, oldMenuHandle);
}
private void onResize(Event e) {
	if (objIOleInPlaceActiveObject != null) {
		RECT lpRect = new RECT();
		OS.GetClientRect(handle, lpRect);
		objIOleInPlaceActiveObject.ResizeBorder(lpRect, iOleInPlaceFrame.getAddress(), true);
	}
}
private int QueryInterface(long riid, long ppvObject) {
//	implements IUnknown, IOleInPlaceFrame, IOleContainer, IOleInPlaceUIWindow
	if (riid == 0 || ppvObject == 0)
		return COM.E_INVALIDARG;
	GUID guid = new GUID();
	COM.MoveMemory(guid, riid, GUID.sizeof);
	if (COM.IsEqualGUID(guid, COM.IIDIUnknown) || COM.IsEqualGUID(guid, COM.IIDIOleInPlaceFrame) ) {
		OS.MoveMemory(ppvObject, new long [] {iOleInPlaceFrame.getAddress()}, C.PTR_SIZEOF);
		AddRef();
		return COM.S_OK;
	}

	OS.MoveMemory(ppvObject, new long [] {0}, C.PTR_SIZEOF);
	return COM.E_NOINTERFACE;
}
/**
 * Decrement the count of references to this instance
 *
 * @return the current reference count
 */
int Release() {
	refCount--;
	if (refCount == 0){
		disposeCOMInterfaces();
		if (COM.FreeUnusedLibraries) {
			COM.CoFreeUnusedLibraries();
		}
	}
	return refCount;
}
private void releaseObjectInterfaces() {
	if (objIOleInPlaceActiveObject != null) {
		objIOleInPlaceActiveObject.Release();
	}
	objIOleInPlaceActiveObject = null;
}
private int RemoveMenus(long hmenuShared) {

	Menu menubar = getShell().getMenuBar();
	if (menubar == null || menubar.isDisposed()) return COM.S_FALSE;

	long hMenu = menubar.handle;

	List<LONG> ids = new ArrayList<>();
	if (this.fileMenuItems != null) {
		for (MenuItem item : this.fileMenuItems) {
			if (item != null && !item.isDisposed()) {
				int index = item.getParent().indexOf(item);
				// get Id from original menubar
				long id = getMenuItemID(hMenu, index);
				ids.add(new LONG(id));
			}
		}
	}
	if (this.containerMenuItems != null) {
		for (MenuItem item : this.containerMenuItems) {
			if (item != null && !item.isDisposed()) {
				int index = item.getParent().indexOf(item);
				long id = getMenuItemID(hMenu, index);
				ids.add(new LONG(id));
			}
		}
	}
	if (this.windowMenuItems != null) {
		for (MenuItem item : this.windowMenuItems) {
			if (item != null && !item.isDisposed()) {
				int index = item.getParent().indexOf(item);
				long id = getMenuItemID(hMenu, index);
				ids.add(new LONG(id));
			}
		}
	}
	int index = OS.GetMenuItemCount(hmenuShared) - 1;
	for (int i = index; i >= 0; i--) {
		long id = getMenuItemID(hmenuShared, i);
		if (ids.contains(new LONG(id))){
			OS.RemoveMenu(hmenuShared, i, OS.MF_BYPOSITION);
		}
	}
	return COM.S_OK;
}
private int RequestBorderSpace(long pborderwidths) {
	return COM.S_OK;
}
int SetActiveObject(long pActiveObject, long pszObjName) {
	if (objIOleInPlaceActiveObject != null) {
		objIOleInPlaceActiveObject.Release();
		objIOleInPlaceActiveObject = null;
	}
	if (pActiveObject != 0) {
		objIOleInPlaceActiveObject = new IOleInPlaceActiveObject(pActiveObject);
		objIOleInPlaceActiveObject.AddRef();
	}

	return COM.S_OK;
}
private int SetBorderSpace(long pborderwidths) {
	// A Control/Document can :
	// Use its own toolbars, requesting border space of a specific size, or,
	// Use no toolbars, but force the container to remove its toolbars by passing a
	//   valid BORDERWIDTHS structure containing nothing but zeros in the pborderwidths parameter, or,
	// Use no toolbars but allow the in-place container to leave its toolbars up by
	//   passing NULL as the pborderwidths parameter.
	if (objIOleInPlaceActiveObject == null) return COM.S_OK;
	RECT borderwidth = new RECT();
	if (pborderwidths == 0 || currentdoc == null ) return COM.S_OK;

	COM.MoveMemory(borderwidth, pborderwidths, RECT.sizeof);
	currentdoc.setBorderSpace(borderwidth);

	return COM.S_OK;
}
/**
 *
 * Specify the menu items that should appear in the Container location when an OLE Document
 * is in-place activated.
 *
 * <p>When an OLE Document is in-place active, the Document provides its own menus but the application
 * is given the opportunity to merge some of its menus into the menubar.  The application
 * is allowed to insert its menus in three locations: File (far left), Container(middle) and Window
 * (far right just before Help).  The OLE Document retains control of the Edit, Object and Help
 * menu locations.  Note that an application can insert more than one menu into a single location.
 *
 * <p>This method must be called before in place activation of the OLE Document.  After the Document
 * is activated, the menu bar will not be modified until a subsequent activation.
 *
 * @param containerMenus an array of top level MenuItems to be inserted into the Container location of
 *        the menubar
 */
public void setContainerMenus(MenuItem[] containerMenus){
	containerMenuItems = containerMenus;
}
OleClientSite getCurrentDocument() {
	return currentdoc;
}
void setCurrentDocument(OleClientSite doc) {
	currentdoc = doc;

	if (currentdoc != null && objIOleInPlaceActiveObject != null) {
		RECT lpRect = new RECT();
		OS.GetClientRect(handle, lpRect);
		objIOleInPlaceActiveObject.ResizeBorder(lpRect, iOleInPlaceFrame.getAddress(), true);
	}
}
/**
 *
 * Specify the menu items that should appear in the File location when an OLE Document
 * is in-place activated.
 *
 * <p>When an OLE Document is in-place active, the Document provides its own menus but the application
 * is given the opportunity to merge some of its menus into the menubar.  The application
 * is allowed to insert its menus in three locations: File (far left), Container(middle) and Window
 * (far right just before Help).  The OLE Document retains control of the Edit, Object and Help
 * menu locations.  Note that an application can insert more than one menu into a single location.
 *
 * <p>This method must be called before in place activation of the OLE Document.  After the Document
 * is activated, the menu bar will not be modified until a subsequent activation.
 *
 * @param fileMenus an array of top level MenuItems to be inserted into the File location of
 *        the menubar
 */
public void setFileMenus(MenuItem[] fileMenus){
	fileMenuItems = fileMenus;
}
private int SetMenu(long hmenuShared, long holemenu, long hwndActiveObject) {
	long inPlaceActiveObject = 0;
	if (objIOleInPlaceActiveObject != null)
		inPlaceActiveObject = objIOleInPlaceActiveObject.getAddress();

	Menu menubar = getShell().getMenuBar();
	if (menubar == null || menubar.isDisposed()){
		return COM.OleSetMenuDescriptor(0, getShell().handle, hwndActiveObject, iOleInPlaceFrame.getAddress(), inPlaceActiveObject);
	}

	long handle = menubar.getShell().handle;

	if (hmenuShared == 0 && holemenu == 0) {
		// re-instate the original menu - this occurs on deactivation
		hmenuShared = menubar.handle;
	}
	if (hmenuShared == 0) return COM.E_FAIL;

	shellHandle = handle;
	oldMenuHandle = menubar.handle;
	newMenuHandle = hmenuShared;
	lastActivatedMenuHandle = newMenuHandle;

	return COM.OleSetMenuDescriptor(holemenu, handle, hwndActiveObject, iOleInPlaceFrame.getAddress(), inPlaceActiveObject);
}
/**
 *
 * Set the menu items that should appear in the Window location when an OLE Document
 * is in-place activated.
 *
 * <p>When an OLE Document is in-place active, the Document provides its own menus but the application
 * is given the opportunity to merge some of its menus into the menubar.  The application
 * is allowed to insert its menus in three locations: File (far left), Container(middle) and Window
 * (far right just before Help).  The OLE Document retains control of the Edit, Object and Help
 * menu locations.  Note that an application can insert more than one menu into a single location.
 *
 * <p>This method must be called before in place activation of the OLE Document.  After the Document
 * is activated, the menu bar will not be modified until a subsequent activation.
 *
 * @param windowMenus an array of top level MenuItems to be inserted into the Window location of
 *        the menubar
 */
public void setWindowMenus(MenuItem[] windowMenus){
	windowMenuItems = windowMenus;
}
private boolean translateOleAccelerator(MSG msg) {
	if (objIOleInPlaceActiveObject == null) return false;
	int result = objIOleInPlaceActiveObject.TranslateAccelerator(msg);
	return (result != COM.S_FALSE && result != COM.E_NOTIMPL);
}
private int TranslateAccelerator(long lpmsg, int wID){
	Menu menubar = getShell().getMenuBar();
	if (menubar == null || menubar.isDisposed() || !menubar.isEnabled()) return COM.S_FALSE;
	if (wID < 0) return COM.S_FALSE;

	Shell shell = menubar.getShell();
	long hwnd = shell.handle;
	long hAccel = OS.SendMessage(hwnd, OS.WM_APP+1, 0, 0);
	if (hAccel == 0) return COM.S_FALSE;

	MSG msg = new MSG();
	OS.MoveMemory(msg, lpmsg, MSG.sizeof);
	int result = OS.TranslateAccelerator(hwnd, hAccel, msg);
	return result == 0 ? COM.S_FALSE : COM.S_OK;
}
}

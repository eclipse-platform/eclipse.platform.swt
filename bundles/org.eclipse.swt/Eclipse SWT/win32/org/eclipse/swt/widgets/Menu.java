/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

 
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class are user interface objects that contain
 * menu items.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>BAR, DROP_DOWN, POP_UP, NO_RADIO_GROUP</dd>
 * <dd>LEFT_TO_RIGHT, RIGHT_TO_LEFT</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Help, Hide, Show </dd>
 * </dl>
 * <p>
 * Note: Only one of BAR, DROP_DOWN and POP_UP may be specified.
 * Only one of LEFT_TO_RIGHT or RIGHT_TO_LEFT may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#menu">Menu snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public class Menu extends Widget {
	/**
	 * the handle to the OS resource 
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 * 
	 * @noreference This field is not intended to be referenced by clients.
	 */
	public int /*long*/ handle;
	
	int x, y; 
	int /*long*/ hBrush, hwndCB;
	int id0, id1;
	int foreground = -1, background = -1;
	Image backgroundImage;	
	boolean hasLocation;
	MenuItem cascade;
	Decorations parent;
	ImageList imageList;
	
	/* Resource ID for SHMENUBARINFO */
	static final int ID_PPC = 100;
	
	/* SmartPhone SoftKeyBar resource ids */
	static final int ID_SPMM = 102;
	static final int ID_SPBM = 103;
	static final int ID_SPMB = 104;
	static final int ID_SPBB = 105;
	static final int ID_SPSOFTKEY0 = 106; 
	static final int ID_SPSOFTKEY1 = 107;

/**
 * Constructs a new instance of this class given its parent,
 * and sets the style for the instance so that the instance
 * will be a popup menu on the given parent's shell.
 * <p>
 * After constructing a menu, it can be set into its parent
 * using <code>parent.setMenu(menu)</code>.  In this case, the parent may
 * be any control in the same widget tree as the parent.
 * </p>
 *
 * @param parent a control which will be the parent of the new instance (cannot be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#POP_UP
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Menu (Control parent) {
	this (checkNull (parent).menuShell (), SWT.POP_UP);
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Decorations</code>) and a style value
 * describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p><p>
 * After constructing a menu or menuBar, it can be set into its parent
 * using <code>parent.setMenu(menu)</code> or <code>parent.setMenuBar(menuBar)</code>.
 * </p>
 *
 * @param parent a decorations control which will be the parent of the new instance (cannot be null)
 * @param style the style of menu to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#BAR
 * @see SWT#DROP_DOWN
 * @see SWT#POP_UP
 * @see SWT#NO_RADIO_GROUP
 * @see SWT#LEFT_TO_RIGHT
 * @see SWT#RIGHT_TO_LEFT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Menu (Decorations parent, int style) {
	this (parent, checkStyle (style), 0);
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Menu</code>) and sets the style
 * for the instance so that the instance will be a drop-down
 * menu on the given parent's parent.
 * <p>
 * After constructing a drop-down menu, it can be set into its parentMenu
 * using <code>parentMenu.setMenu(menu)</code>.
 * </p>
 *
 * @param parentMenu a menu which will be the parent of the new instance (cannot be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#DROP_DOWN
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Menu (Menu parentMenu) {
	this (checkNull (parentMenu).parent, SWT.DROP_DOWN);
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>MenuItem</code>) and sets the style
 * for the instance so that the instance will be a drop-down
 * menu on the given parent's parent menu.
 * <p>
 * After constructing a drop-down menu, it can be set into its parentItem
 * using <code>parentItem.setMenu(menu)</code>.
 * </p>
 *
 * @param parentItem a menu item which will be the parent of the new instance (cannot be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#DROP_DOWN
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Menu (MenuItem parentItem) {
	this (checkNull (parentItem).parent);
}

Menu (Decorations parent, int style, int /*long*/ handle) {
	super (parent, checkStyle (style));
	this.parent = parent;
	this.handle = handle;
	/*
	* Bug in IBM JVM 1.3.1.  For some reason, when the checkOrientation() is
	* called from createWidget(), the JVM issues this error:
	*
	* JVM Exception 0x2 (subcode 0x0) occurred in thread "main" (TID:0x9F19D8)
	* 
	* In addition, on Windows XP, a dialog appears with following error message,
	* indicating that the problem may be in the JIT:
	* 
	* AppName: java.exe	 AppVer: 0.0.0.0	 ModName: jitc.dll
	* ModVer: 0.0.0.0	 Offset: 000b6912
	* 
	* The fix is to call checkOrientation() from here.
	*/
	checkOrientation (parent);
	createWidget ();
}

void _setVisible (boolean visible) {
	if ((style & (SWT.BAR | SWT.DROP_DOWN)) != 0) return;
	int /*long*/ hwndParent = parent.handle;
	if (visible) {
		int flags = OS.TPM_LEFTBUTTON;
		if (OS.GetKeyState (OS.VK_LBUTTON) >= 0) flags |= OS.TPM_RIGHTBUTTON;
		if ((style & SWT.RIGHT_TO_LEFT) != 0) flags |= OS.TPM_RIGHTALIGN;
		if ((parent.style & SWT.MIRRORED) != 0) {
			flags &= ~OS.TPM_RIGHTALIGN;
			if ((style & SWT.LEFT_TO_RIGHT) != 0) flags |= OS.TPM_RIGHTALIGN;
		}
		int nX = x, nY = y;
		if (!hasLocation) {
			int pos = OS.GetMessagePos ();
			nX = OS.GET_X_LPARAM (pos);
			nY = OS.GET_Y_LPARAM (pos);
		}
		hasLocation = false;
		/*
		* Feature in Windows.  It is legal use TrackPopupMenu()
		* to display an empty menu as long as menu items are added
		* inside of WM_INITPOPUPMENU.  If no items are added, then
		* TrackPopupMenu() fails and does not send an indication
		* that the menu has been closed.  This is not strictly a
		* bug but leads to unwanted behavior when application code
		* assumes that every WM_INITPOPUPMENU will eventually result
		* in a WM_MENUSELECT, wParam=MAKEWPARAM (0, 0xFFFF), lParam=0 to
		* indicate that the menu has been closed.  The fix is to detect
		* the case when TrackPopupMenu() fails and the number of items in
		* the menu is zero and issue a fake WM_MENUSELECT.
		*/
		boolean success = OS.TrackPopupMenu (handle, flags, nX, nY, 0, hwndParent, null);
		if (!success && GetMenuItemCount (handle) == 0) {
			OS.SendMessage (hwndParent, OS.WM_MENUSELECT, OS.MAKEWPARAM (0, 0xFFFF), 0);
		}
	} else {
		OS.SendMessage (hwndParent, OS.WM_CANCELMODE, 0, 0);
	}
	/*
	* Bug in Windows.  After closing a popup menu, the accessibility focus
	* is not returned to the focus control.  This causes confusion for AT users.
	* The fix is to explicitly set the accessibility focus back to the focus control.
	*/
	int /*long*/ hFocus = OS.GetFocus();
	if (hFocus != 0) {
		OS.NotifyWinEvent (OS.EVENT_OBJECT_FOCUS, hFocus, OS.OBJID_CLIENT, 0);
	}
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when help events are generated for the control,
 * by sending it one of the messages defined in the
 * <code>HelpListener</code> interface.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see HelpListener
 * @see #removeHelpListener
 */
public void addHelpListener (HelpListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Help, typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when menus are hidden or shown, by sending it
 * one of the messages defined in the <code>MenuListener</code>
 * interface.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see MenuListener
 * @see #removeMenuListener
 */
public void addMenuListener (MenuListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Hide,typedListener);
	addListener (SWT.Show,typedListener);
}

static Control checkNull (Control control) {
	if (control == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return control;
}

static Menu checkNull (Menu menu) {
	if (menu == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return menu;
}

static MenuItem checkNull (MenuItem item) {
	if (item == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return item;
}

static int checkStyle (int style) {
	return checkBits (style, SWT.POP_UP, SWT.BAR, SWT.DROP_DOWN, 0, 0, 0);
}

void createHandle () {
	if (handle != 0) return;
	if ((style & SWT.BAR) != 0) {
		if (OS.IsPPC) {
			int /*long*/ hwndShell = parent.handle;
			SHMENUBARINFO mbi = new SHMENUBARINFO ();
			mbi.cbSize = SHMENUBARINFO.sizeof;
			mbi.hwndParent = hwndShell;
			mbi.dwFlags = OS.SHCMBF_HIDDEN;
			mbi.nToolBarId = ID_PPC;
			mbi.hInstRes = OS.GetLibraryHandle ();
			boolean success = OS.SHCreateMenuBar (mbi);
			hwndCB = mbi.hwndMB;
			if (!success) error (SWT.ERROR_NO_HANDLES);
			/* Remove the item from the resource file */
			OS.SendMessage (hwndCB, OS.TB_DELETEBUTTON, 0, 0);
			return;
		}
		/*
		* Note in WinCE SmartPhone.  The SoftBar contains only 2 items.
		* An item can either be a menu or a button. 
		* SWT.BAR: creates a SoftBar with 2 menus
		* SWT.BAR | SWT.BUTTON1: creates a SoftBar with 1 button
		*    for button1, and a menu for button2
		* SWT.BAR | SWT.BUTTON1 | SWT.BUTTON2: creates a SoftBar with
		*    2 buttons
		*/
		if (OS.IsSP) {
			/* Determine type of menubar */
			int nToolBarId;
			if ((style & SWT.BUTTON1) != 0) {
				nToolBarId = ((style & SWT.BUTTON2) != 0) ? ID_SPBB : ID_SPBM;
			} else {
				nToolBarId = ((style & SWT.BUTTON2) != 0) ? ID_SPMB : ID_SPMM;
			}
			
			/* Create SHMENUBAR */
			SHMENUBARINFO mbi = new SHMENUBARINFO ();
			mbi.cbSize = SHMENUBARINFO.sizeof;
			mbi.hwndParent = parent.handle;
			mbi.dwFlags = OS.SHCMBF_HIDDEN;
			mbi.nToolBarId = nToolBarId; /* as defined in .rc file */
			mbi.hInstRes = OS.GetLibraryHandle ();
			if (!OS.SHCreateMenuBar (mbi)) error (SWT.ERROR_NO_HANDLES);
			hwndCB = mbi.hwndMB;
			
			/*
			* Feature on WinCE SmartPhone.  The SHCMBF_HIDDEN flag causes the
			* SHMENUBAR to not be drawn. However the keyboard events still go
			* through it.  The workaround is to also hide the SHMENUBAR with
			* ShowWindow ().
			*/
			OS.ShowWindow (hwndCB, OS.SW_HIDE);
			
			TBBUTTONINFO info = new TBBUTTONINFO ();
			info.cbSize = TBBUTTONINFO.sizeof;
			info.dwMask = OS.TBIF_COMMAND;
			MenuItem item;
			
			/* Set first item */
			if (nToolBarId == ID_SPMM || nToolBarId == ID_SPMB) {
				int /*long*/ hMenu = OS.SendMessage (hwndCB, OS.SHCMBM_GETSUBMENU, 0, ID_SPSOFTKEY0);
				/* Remove the item from the resource file */
				OS.RemoveMenu (hMenu, 0, OS.MF_BYPOSITION);
				Menu menu = new Menu (parent, SWT.DROP_DOWN, hMenu);
				item = new MenuItem (this, menu, SWT.CASCADE, 0);
			} else {
				item = new MenuItem (this, null, SWT.PUSH, 0);
			}
			info.idCommand = id0 = item.id;
			OS.SendMessage (hwndCB, OS.TB_SETBUTTONINFO, ID_SPSOFTKEY0, info);	

			/* Set second item */
			if (nToolBarId == ID_SPMM || nToolBarId == ID_SPBM) {
				int /*long*/ hMenu = OS.SendMessage (hwndCB, OS.SHCMBM_GETSUBMENU, 0, ID_SPSOFTKEY1);
				OS.RemoveMenu (hMenu, 0, OS.MF_BYPOSITION);
				Menu menu = new Menu (parent, SWT.DROP_DOWN, hMenu);
				item = new MenuItem (this, menu, SWT.CASCADE, 1);
			} else {
				item = new MenuItem (this, null, SWT.PUSH, 1);
			}
			info.idCommand = id1 = item.id;
			OS.SendMessage (hwndCB, OS.TB_SETBUTTONINFO, ID_SPSOFTKEY1, info);

			/*
			* Override the Back key.  For some reason, the owner of the menubar
			* must be a Dialog or it won't receive the WM_HOTKEY message.  As
			* a result, Shell on WinCE SP must use the class Dialog.
			*/
			int dwMask = OS.SHMBOF_NODEFAULT | OS.SHMBOF_NOTIFY;
			int /*long*/ lParam = OS.MAKELPARAM (dwMask, dwMask);
			OS.SendMessage (hwndCB, OS.SHCMBM_OVERRIDEKEY, OS.VK_ESCAPE, lParam);
			return;
		}
		handle = OS.CreateMenu ();
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		if (OS.IsHPC) {
			int /*long*/ hwndShell = parent.handle;
			hwndCB = OS.CommandBar_Create (OS.GetModuleHandle (null), hwndShell, 1);
			if (hwndCB == 0) error (SWT.ERROR_NO_HANDLES);
			OS.CommandBar_Show (hwndCB, false);
			OS.CommandBar_InsertMenubarEx (hwndCB, 0, handle, 0);
			/*
			* The command bar hosts the 'close' button when the window does not
			* have a caption.
			*/
			if ((parent.style & SWT.CLOSE) != 0 && (parent.style & SWT.TITLE) == 0) {
				OS.CommandBar_AddAdornments (hwndCB, 0, 0);
			}
		}
	} else {
		handle = OS.CreatePopupMenu ();
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	}
}

void createItem (MenuItem item, int index) {
	int count = GetMenuItemCount (handle);
	if (!(0 <= index && index <= count)) error (SWT.ERROR_INVALID_RANGE);
	display.addMenuItem (item);
	boolean success = false;
	if ((OS.IsPPC || OS.IsSP) && hwndCB != 0) {
		if (OS.IsSP) return;
		TBBUTTON lpButton = new TBBUTTON ();
		lpButton.idCommand = item.id;
		lpButton.fsStyle = (byte) OS.TBSTYLE_AUTOSIZE;
		if ((item.style & SWT.CASCADE) != 0) lpButton.fsStyle |= OS.TBSTYLE_DROPDOWN | 0x80;
		if ((item.style & SWT.SEPARATOR) != 0) lpButton.fsStyle = (byte) OS.BTNS_SEP;
		lpButton.fsState = (byte) OS.TBSTATE_ENABLED;
		lpButton.iBitmap = OS.I_IMAGENONE;
		success = OS.SendMessage (hwndCB, OS.TB_INSERTBUTTON, index, lpButton) != 0;
	} else {
		if (OS.IsWinCE) {
			int uFlags = OS.MF_BYPOSITION;
			TCHAR lpNewItem = null;
			if ((item.style & SWT.SEPARATOR) != 0) {
				uFlags |= OS.MF_SEPARATOR;
			} else {
				lpNewItem = new TCHAR (0, " ", true);
			}
			success = OS.InsertMenu (handle, index, uFlags, item.id, lpNewItem);
			if (success) {
				MENUITEMINFO info = new MENUITEMINFO ();
				info.cbSize = MENUITEMINFO.sizeof;
				info.fMask = OS.MIIM_DATA;
				info.dwItemData = item.id;
				success = OS.SetMenuItemInfo (handle, index, true, info);
			}
		} else {
			/*
			* Bug in Windows.  For some reason, when InsertMenuItem()
			* is used to insert an item without text, it is not possible
			* to use SetMenuItemInfo() to set the text at a later time.
			* The fix is to insert the item with some text.
			* 
			* Feature in Windows.  When an empty string is used instead
			* of a space and InsertMenuItem() is used to set a submenu
			* before setting text to a non-empty string, the menu item
			* becomes unexpectedly disabled.  The fix is to insert a
			* space.
			*/
			int /*long*/ hHeap = OS.GetProcessHeap ();
			TCHAR buffer = new TCHAR (0, " ", true);
			int byteCount = buffer.length () * TCHAR.sizeof;
			int /*long*/ pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
			OS.MoveMemory (pszText, buffer, byteCount);	
			MENUITEMINFO info = new MENUITEMINFO ();
			info.cbSize = MENUITEMINFO.sizeof;
			info.fMask = OS.MIIM_ID | OS.MIIM_TYPE | OS.MIIM_DATA;
			info.wID = item.id;
			info.dwItemData = item.id;
			info.fType = item.widgetStyle ();
			info.dwTypeData = pszText;
			success = OS.InsertMenuItem (handle, index, true, info);
			if (pszText != 0) OS.HeapFree (hHeap, 0, pszText);
		}
	}
	if (!success) {
		display.removeMenuItem (item);
		error (SWT.ERROR_ITEM_NOT_ADDED);
	}
	redraw ();
}
	
void createWidget () {
	/*
	* Bug in IBM JVM 1.3.1.  For some reason, when the following code is called
	* from this method, the JVM issues this error:
	*
	* JVM Exception 0x2 (subcode 0x0) occurred in thread "main" (TID:0x9F19D8)
	* 
	* In addition, on Windows XP, a dialog appears with following error message,
	* indicating that the problem may be in the JIT:
	* 
	* AppName: java.exe	 AppVer: 0.0.0.0	 ModName: jitc.dll
	* ModVer: 0.0.0.0	 Offset: 000b6912
	* 
	* The fix is to move the code to the caller of this method.
	*/
//	checkOrientation (parent);
	createHandle ();
	parent.addMenu (this);
}

int defaultBackground () {
	return OS.GetSysColor (OS.COLOR_MENU);
}

int defaultForeground () {
	return OS.GetSysColor (OS.COLOR_MENUTEXT);
}

void destroyAccelerators () {
	parent.destroyAccelerators ();
}

void destroyItem (MenuItem item) {
	if (OS.IsWinCE) {
		if ((OS.IsPPC || OS.IsSP) && hwndCB != 0) {
			if (OS.IsSP) {
				redraw();
				return;
			}
			int index = (int)/*64*/OS.SendMessage (hwndCB, OS.TB_COMMANDTOINDEX, item.id, 0);
			if (OS.SendMessage (hwndCB, OS.TB_DELETEBUTTON, index, 0) == 0) {
				error (SWT.ERROR_ITEM_NOT_REMOVED);
			}
			int count = (int)/*64*/OS.SendMessage (hwndCB, OS.TB_BUTTONCOUNT, 0, 0);
			if (count == 0) {
				if (imageList != null) {
					OS.SendMessage (handle, OS.TB_SETIMAGELIST, 0, 0);
					display.releaseImageList (imageList);
					imageList = null;
				}
			}
		} else {
			int index = 0;
			MENUITEMINFO info = new MENUITEMINFO ();
			info.cbSize = MENUITEMINFO.sizeof;
			info.fMask = OS.MIIM_DATA;
			while (OS.GetMenuItemInfo (handle, index, true, info)) {
				if (info.dwItemData == item.id) break;
				index++;
			}
			if (info.dwItemData != item.id) {
				error (SWT.ERROR_ITEM_NOT_REMOVED);
			}	
			if (!OS.DeleteMenu (handle, index, OS.MF_BYPOSITION)) {
				error (SWT.ERROR_ITEM_NOT_REMOVED);
			}
		}
	} else {
		if (!OS.DeleteMenu (handle, item.id, OS.MF_BYCOMMAND)) {
			error (SWT.ERROR_ITEM_NOT_REMOVED);
		}
	}
	redraw ();
}

void destroyWidget () {
	MenuItem cascade = this.cascade;
	int /*long*/ hMenu = handle, hCB = hwndCB;
	releaseHandle ();
	if (OS.IsWinCE && hCB != 0) {
		OS.CommandBar_Destroy (hCB);
	} else {
		if (cascade != null) {
			if (!OS.IsSP) cascade.setMenu (null, true);
		} else {
			if (hMenu != 0) OS.DestroyMenu (hMenu);
		}
	}
}

void fixMenus (Decorations newParent) {
	MenuItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		items [i].fixMenus (newParent);
	}
	parent.removeMenu (this);
	newParent.addMenu (this);
	this.parent = newParent;
}

/**
 * Returns the receiver's background color.
 *
 * @return the background color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.3
 */
/*public*/ Color getBackground () {
	checkWidget ();
	return Color.win32_new (display, background != -1 ? background : defaultBackground ());
}

/**
 * Returns the receiver's background image.
 *
 * @return the background image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.3
 */
/*public*/ Image getBackgroundImage () {
	checkWidget ();
	return backgroundImage;
}

/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent (or its display if its parent is null),
 * unless the receiver is a menu or a shell. In this case, the
 * location is relative to the display.
 * <p>
 * Note that the bounds of a menu or menu item are undefined when
 * the menu is not visible.  This is because most platforms compute
 * the bounds of a menu dynamically just before it is displayed.
 * </p>
 *
 * @return the receiver's bounding rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
/*public*/ Rectangle getBounds () {
	checkWidget ();
	if (OS.IsWinCE) return new Rectangle (0, 0, 0, 0);
	if ((style & SWT.BAR) != 0) {
		if (parent.menuBar != this) {
			return new Rectangle (0, 0, 0, 0);
		}
		int /*long*/ hwndShell = parent.handle;
		MENUBARINFO info = new MENUBARINFO ();
		info.cbSize = MENUBARINFO.sizeof;
		if (OS.GetMenuBarInfo (hwndShell, OS.OBJID_MENU, 0, info)) {
			int width = info.right - info.left;
			int height = info.bottom - info.top;
			return new Rectangle (info.left, info.top, width, height);
		}
	} else {
		int count = GetMenuItemCount (handle);
		if (count != 0) {
			RECT rect1 = new RECT ();
			if (OS.GetMenuItemRect (0, handle, 0, rect1)) {
				RECT rect2 = new RECT ();
				if (OS.GetMenuItemRect (0, handle, count - 1, rect2)) {
					int x = rect1.left - 2, y = rect1.top - 2;
					int width = (rect2.right - rect2.left) + 4;
					int height = (rect2.bottom - rect1.top) + 4;
					return new Rectangle (x, y, width, height);
				}
			}
		}
	}
	return new Rectangle (0, 0, 0, 0);
}

/**
 * Returns the default menu item or null if none has
 * been previously set.
 *
 * @return the default menu item.
 *
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public MenuItem getDefaultItem () {
	checkWidget ();
	if (OS.IsWinCE) return null;
	int id = OS.GetMenuDefaultItem (handle, OS.MF_BYCOMMAND, OS.GMDI_USEDISABLED);
	if (id == -1) return null;
	MENUITEMINFO info = new MENUITEMINFO ();
	info.cbSize = MENUITEMINFO.sizeof;
	info.fMask = OS.MIIM_ID;
	if (OS.GetMenuItemInfo (handle, id, false, info)) {
		return display.getMenuItem (info.wID);
	}
	return null;
}

/**
 * Returns <code>true</code> if the receiver is enabled, and
 * <code>false</code> otherwise. A disabled menu is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #isEnabled
 */
public boolean getEnabled () {
	checkWidget ();
	return (state & DISABLED) == 0;
}

/**
 * Returns the foreground color that the receiver will use to draw.
 *
 * @return the receiver's foreground color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
/*public*/ Color getForeground () {
	checkWidget ();
	return Color.win32_new (display, foreground != -1 ? foreground : defaultForeground ());
}

/**
 * Returns the item at the given, zero-relative index in the
 * receiver. Throws an exception if the index is out of range.
 *
 * @param index the index of the item to return
 * @return the item at the given index
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public MenuItem getItem (int index) {
	checkWidget ();
	int id = 0;
	if ((OS.IsPPC || OS.IsSP) && hwndCB != 0) {
		if (OS.IsPPC) {
			TBBUTTON lpButton = new TBBUTTON ();
			int /*long*/ result = OS.SendMessage (hwndCB, OS.TB_GETBUTTON, index, lpButton);
			if (result == 0) error (SWT.ERROR_CANNOT_GET_ITEM);
			id = lpButton.idCommand;
		}
		if (OS.IsSP) {
			if (!(0 <= index && index <= 1)) error (SWT.ERROR_CANNOT_GET_ITEM);
			id = index == 0 ? id0 : id1;
		}
	} else {
		MENUITEMINFO info = new MENUITEMINFO ();
		info.cbSize = MENUITEMINFO.sizeof;
		info.fMask = OS.MIIM_DATA;
		if (!OS.GetMenuItemInfo (handle, index, true, info)) {
			error (SWT.ERROR_INVALID_RANGE);
		}
		id = (int)/*64*/info.dwItemData;
	}
	return display.getMenuItem (id);
}

/**
 * Returns the number of items contained in the receiver.
 *
 * @return the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getItemCount () {
	checkWidget ();
	return GetMenuItemCount (handle);
}

/**
 * Returns a (possibly empty) array of <code>MenuItem</code>s which
 * are the items in the receiver. 
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return the items in the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public MenuItem [] getItems () {
	checkWidget ();
	if ((OS.IsPPC || OS.IsSP) && hwndCB != 0) {
		if (OS.IsSP) {
			MenuItem [] result = new MenuItem [2];
			result[0] = display.getMenuItem (id0);
			result[1] = display.getMenuItem (id1);
			return result;
		}
		int count = (int)/*64*/OS.SendMessage (hwndCB, OS.TB_BUTTONCOUNT, 0, 0);
		TBBUTTON lpButton = new TBBUTTON ();
		MenuItem [] result = new MenuItem [count];
		for (int i=0; i<count; i++) {
			OS.SendMessage (hwndCB, OS.TB_GETBUTTON, i, lpButton);
			result [i] = display.getMenuItem (lpButton.idCommand);
		}
		return result;
	}
	int index = 0, count = 0;
	int length = OS.IsWinCE ? 4 : OS.GetMenuItemCount (handle);
	MenuItem [] items = new MenuItem [length];
	MENUITEMINFO info = new MENUITEMINFO ();
	info.cbSize = MENUITEMINFO.sizeof;
	info.fMask = OS.MIIM_DATA;
	while (OS.GetMenuItemInfo (handle, index, true, info)) {
		if (count == items.length) {
			MenuItem [] newItems = new MenuItem [count + 4];
			System.arraycopy (items, 0, newItems, 0, count);
			items = newItems;
		}
		MenuItem item = display.getMenuItem ((int)/*64*/info.dwItemData);
		if (item != null) items [count++] = item;
		index++;
	}
	if (count == items.length) return items;
	MenuItem [] result = new MenuItem [count];
	System.arraycopy (items, 0, result, 0, count);
	return result;
}

int GetMenuItemCount (int /*long*/ handle) {
	if (OS.IsWinCE) {
		if ((OS.IsPPC || OS.IsSP) && hwndCB != 0) {
			return OS.IsSP ? 2 : (int)/*64*/OS.SendMessage (hwndCB, OS.TB_BUTTONCOUNT, 0, 0);
		}
		int count = 0;
		MENUITEMINFO info = new MENUITEMINFO ();
		info.cbSize = MENUITEMINFO.sizeof;
		while (OS.GetMenuItemInfo (handle, count, true, info)) count++;
		return count;
	}
	return OS.GetMenuItemCount (handle);
}

String getNameText () {
	String result = "";
	MenuItem [] items = getItems ();
	int length = items.length;
	if (length > 0) {
		for (int i=0; i<length-1; i++) {
			result = result + items [i].getNameText() + ", ";
		}
		result = result + items [length-1].getNameText ();
	}
	return result;
}

/**
 * Returns the orientation of the receiver, which will be one of the
 * constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
 *
 * @return the orientation style
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.7
 */
public int getOrientation () {
	checkWidget ();
	return style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
}

/**
 * Returns the receiver's parent, which must be a <code>Decorations</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Decorations getParent () {
	checkWidget ();
	return parent;
}

/**
 * Returns the receiver's parent item, which must be a
 * <code>MenuItem</code> or null when the receiver is a
 * root.
 *
 * @return the receiver's parent item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public MenuItem getParentItem () {
	checkWidget ();
	return cascade;
}

/**
 * Returns the receiver's parent item, which must be a
 * <code>Menu</code> or null when the receiver is a
 * root.
 *
 * @return the receiver's parent item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Menu getParentMenu () {
	checkWidget ();
	if (cascade != null) return cascade.parent;
	return null;
}

/**
 * Returns the receiver's shell. For all controls other than
 * shells, this simply returns the control's nearest ancestor
 * shell. Shells return themselves, even if they are children
 * of other shells.
 *
 * @return the receiver's shell
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getParent
 */
public Shell getShell () {
	checkWidget ();
	return parent.getShell ();
}

/**
 * Returns <code>true</code> if the receiver is visible, and
 * <code>false</code> otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, this method
 * may still indicate that it is considered visible even though
 * it may not actually be showing.
 * </p>
 *
 * @return the receiver's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getVisible () {
	checkWidget ();
	if ((style & SWT.BAR) != 0) {
		return this == parent.menuShell ().menuBar;
	}
	if ((style & SWT.POP_UP) != 0) {
		Menu [] popups = display.popups;
		if (popups == null) return false;
		for (int i=0; i<popups.length; i++) {
			if (popups [i] == this) return true;
		}
	}
	Shell shell = getShell ();
	Menu menu = shell.activeMenu;
	while (menu != null && menu != this) {
		menu = menu.getParentMenu ();
	}
	return this == menu;
}

int imageIndex (Image image) {
	if (hwndCB == 0 || image == null) return OS.I_IMAGENONE;
	if (imageList == null) {
		Rectangle bounds = image.getBounds ();
		imageList = display.getImageList (style & SWT.RIGHT_TO_LEFT, bounds.width, bounds.height);
		int index = imageList.add (image);
		int /*long*/ hImageList = imageList.getHandle ();
		OS.SendMessage (hwndCB, OS.TB_SETIMAGELIST, 0, hImageList);
		return index;
	}
	int index = imageList.indexOf (image);
	if (index == -1) {
		index = imageList.add (image);
	} else {
		imageList.put (index, image);
	}
	return index;
}

/**
 * Searches the receiver's list starting at the first item
 * (index 0) until an item is found that is equal to the 
 * argument, and returns the index of that item. If no item
 * is found, returns -1.
 *
 * @param item the search item
 * @return the index of the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (MenuItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if (item.parent != this) return -1;
	if ((OS.IsPPC || OS.IsSP) && hwndCB != 0) {
		if (OS.IsPPC) {
			return (int)/*64*/OS.SendMessage (hwndCB, OS.TB_COMMANDTOINDEX, item.id, 0);
		}
		if (OS.IsSP) {
			if (item.id == id0) return 0;
			if (item.id == id1) return 1;
			return -1;
		}
	}
	int index = 0;
	MENUITEMINFO info = new MENUITEMINFO ();
	info.cbSize = MENUITEMINFO.sizeof;
	info.fMask = OS.MIIM_DATA;
	while (OS.GetMenuItemInfo (handle, index, true, info)) {
		if (info.dwItemData == item.id) return index;
		index++;
	}
	return -1;
}

/**
 * Returns <code>true</code> if the receiver is enabled and all
 * of the receiver's ancestors are enabled, and <code>false</code>
 * otherwise. A disabled menu is typically not selectable from the
 * user interface and draws with an inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #getEnabled
 */
public boolean isEnabled () {
	checkWidget ();
	Menu parentMenu = getParentMenu ();
	if (parentMenu == null) {
		return getEnabled () && parent.isEnabled ();
	}
	return getEnabled () && parentMenu.isEnabled ();
}

/**
 * Returns <code>true</code> if the receiver is visible and all
 * of the receiver's ancestors are visible and <code>false</code>
 * otherwise.
 *
 * @return the receiver's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getVisible
 */
public boolean isVisible () {
	checkWidget ();
	return getVisible ();
}

void redraw () {
	if (!isVisible ()) return;
	if ((style & SWT.BAR) != 0) {
		display.addBar (this);
	} else {
		update ();
	}
}

void releaseHandle () {
	super.releaseHandle ();
	handle = hwndCB = 0;
	cascade = null;
}

void releaseChildren (boolean destroy) {
	MenuItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		MenuItem item = items [i];
		if (item != null && !item.isDisposed ()) {
			if (OS.IsPPC && hwndCB != 0) {
				item.dispose ();
			} else {
				item.release (false);
			}
		}
	}
	super.releaseChildren (destroy);
}

void releaseParent () {
	super.releaseParent ();
	if ((style & SWT.BAR) != 0) {
		display.removeBar (this);
		if (this == parent.menuBar) {
			parent.setMenuBar (null);
		}
	} else {
		if ((style & SWT.POP_UP) != 0) {
			display.removePopup (this);
		}
	}
}

void releaseWidget () {
	super.releaseWidget ();
	backgroundImage = null;
	if (hBrush == 0) OS.DeleteObject (hBrush);
	hBrush = 0;
	if (OS.IsPPC && hwndCB != 0) {
		if (imageList != null) {
			OS.SendMessage (hwndCB, OS.TB_SETIMAGELIST, 0, 0);
			display.releaseToolImageList (imageList);
			imageList = null;
		}
	}
	if (parent != null) parent.removeMenu (this);
	parent = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the help events are generated for the control.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see HelpListener
 * @see #addHelpListener
 */
public void removeHelpListener (HelpListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Help, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the menu events are generated for the control.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see MenuListener
 * @see #addMenuListener
 */
public void removeMenuListener (MenuListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Hide, listener);
	eventTable.unhook (SWT.Show, listener);
}

void reskinChildren (int flags) {
	MenuItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		MenuItem item = items [i];
		item.reskin (flags);
	}
	super.reskinChildren (flags);
}

/**
 * Sets the receiver's background color to the color specified
 * by the argument, or to the default system color for the control
 * if the argument is null.
 *
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.3
 */
/*public*/ void setBackground (Color color) {
	checkWidget ();
	int pixel = -1;
	if (color != null) {
		if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		pixel = color.handle;
	}
	if (pixel == background) return;
	background = pixel;
	updateBackground ();
}

/**
 * Sets the receiver's background image to the image specified
 * by the argument, or to the default system color for the control
 * if the argument is null.  The background image is tiled to fill
 * the available space.
 *
 * @param image the new image (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 *    <li>ERROR_INVALID_ARGUMENT - if the argument is not a bitmap</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.3
 */
/*public*/ void setBackgroundImage (Image image) {
	checkWidget ();
	if (image != null) {
		if (image.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (image.type != SWT.BITMAP) error (SWT.ERROR_INVALID_ARGUMENT);
	}
	if (backgroundImage == image) return;
	backgroundImage = image;
	updateBackground ();
}

/**
 * Sets the receiver's foreground color to the color specified
 * by the argument, or to the default system color for the control
 * if the argument is null.
 *
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.3
 */
/*public*/ void setForeground (Color color) {
	checkWidget ();
	int pixel = -1;
	if (color != null) {
		if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		pixel = color.handle;
	}
	if (pixel == foreground) return;
	foreground = pixel;
	updateForeground ();
}

/**
 * Sets the default menu item to the argument or removes
 * the default emphasis when the argument is <code>null</code>.
 * 
 * @param item the default menu item or null
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the menu item has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setDefaultItem (MenuItem item) {
	checkWidget ();
	int newID = -1;
	if (item != null) {
		if (item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		if (item.parent != this) return;
		newID = item.id;
	}
	if (OS.IsWinCE) return;
	int oldID = OS.GetMenuDefaultItem (handle, OS.MF_BYCOMMAND, OS.GMDI_USEDISABLED);
	if (newID == oldID) return;
	OS.SetMenuDefaultItem (handle, newID, OS.MF_BYCOMMAND);
	redraw ();
}

/**
 * Enables the receiver if the argument is <code>true</code>,
 * and disables it otherwise. A disabled menu is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 *
 * @param enabled the new enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setEnabled (boolean enabled) {
	checkWidget ();
	state &= ~DISABLED;
	if (!enabled) state |= DISABLED;
}

/**
 * Sets the location of the receiver, which must be a popup,
 * to the point specified by the arguments which are relative
 * to the display.
 * <p>
 * Note that this is different from most widgets where the
 * location of the widget is relative to the parent.
 * </p><p>
 * Note that the platform window manager ultimately has control
 * over the location of popup menus.
 * </p>
 *
 * @param x the new x coordinate for the receiver
 * @param y the new y coordinate for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLocation (int x, int y) {
	checkWidget ();
	if ((style & (SWT.BAR | SWT.DROP_DOWN)) != 0) return;
	this.x = x;
	this.y = y;
	hasLocation = true;
}

/**
 * Sets the location of the receiver, which must be a popup,
 * to the point specified by the argument which is relative
 * to the display.
 * <p>
 * Note that this is different from most widgets where the
 * location of the widget is relative to the parent.
 * </p><p>
 * Note that the platform window manager ultimately has control
 * over the location of popup menus.
 * </p>
 *
 * @param location the new location for the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1
 */
public void setLocation (Point location) {
	checkWidget ();
	if (location == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	setLocation (location.x, location.y);
}

/**
 * Sets the orientation of the receiver, which must be one
 * of the constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
 * <p>
 *
 * @param orientation new orientation style
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.7  
 */
public void setOrientation (int orientation) { 
    checkWidget ();
    if ((style & (SWT.BAR | SWT.DROP_DOWN)) != 0) return;
    _setOrientation (orientation);
}

void _setOrientation (int orientation) {
   if (OS.IsWinCE) return;
   if (OS.WIN32_VERSION < OS.VERSION (4, 10)) return;
   int flags = SWT.RIGHT_TO_LEFT | SWT.LEFT_TO_RIGHT;
   if ((orientation & flags) == 0 || (orientation & flags) == flags) return;
   style &= ~flags;
   style |= orientation & flags;
   MenuItem [] itms = getItems ();
   for (int i=0; i<itms.length; i++) {
       itms [i].setOrientation (orientation);
   }
}

/**
 * Marks the receiver as visible if the argument is <code>true</code>,
 * and marks it invisible otherwise. 
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, marking
 * it visible may not actually cause it to be displayed.
 * </p>
 *
 * @param visible the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setVisible (boolean visible) {
	checkWidget ();
	if ((style & (SWT.BAR | SWT.DROP_DOWN)) != 0) return;
	if (visible) {
		display.addPopup (this);
	} else {
		display.removePopup (this);
		_setVisible (false);
	}
}

void update () {
	if (OS.IsPPC || OS.IsSP) return;
	if (OS.IsHPC) {
		/*
		* Each time a menu has been modified, the command menu bar
		* must be redrawn or it won't update properly.  For example,
		* a submenu will not drop down.
		*/
		Menu menuBar = parent.menuBar;
		if (menuBar != null) {
			Menu menu = this;
			while (menu != null && menu != menuBar) {
				menu = menu.getParentMenu ();
			}
			if (menu == menuBar) {
				OS.CommandBar_DrawMenuBar (menuBar.hwndCB, 0);
				OS.CommandBar_Show (menuBar.hwndCB, true);
			}
		}
		return;
	}
	if (OS.IsWinCE) return;
	if ((style & SWT.BAR) != 0) {
		if (this == parent.menuBar) OS.DrawMenuBar (parent.handle);
		return;
	}
	if (OS.WIN32_VERSION < OS.VERSION (4, 10)) {
		return;
	}
	boolean hasCheck = false, hasImage = false;
	MenuItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		MenuItem item = items [i];
		if (item.image != null) {
			if ((hasImage = true) && hasCheck) break;
		}
		if ((item.style & (SWT.CHECK | SWT.RADIO)) != 0) {
			if ((hasCheck = true) && hasImage) break;
		}
	}
	
	/*
	* Bug in Windows.  If a menu contains items that have
	* images and can be checked, Windows does not include
	* the width of the image and the width of the check when
	* computing the width of the menu.  When the longest item
	* does not have an image, the label and the accelerator
	* text can overlap.  The fix is to use SetMenuItemInfo()
	* to indicate that all items have a bitmap and then include
	* the width of the widest bitmap in WM_MEASURECHILD.
	* 
	* NOTE:  This work around causes problems on Windows 98.
	* Under certain circumstances that have yet to be isolated,
	* some menus can become huge and blank.  For now, do not
	* run the code on Windows 98.
	* 
	* NOTE:  This work around doesn't run on Vista because
	* WM_MEASURECHILD and WM_DRAWITEM cause Vista to lose
	* the menu theme.
	*/	
	if (!OS.IsWin95) {
		if (OS.WIN32_VERSION < OS.VERSION (6, 0)) {
			MENUITEMINFO info = new MENUITEMINFO ();
			info.cbSize = MENUITEMINFO.sizeof;
			info.fMask = OS.MIIM_BITMAP;
			for (int i=0; i<items.length; i++) {
				MenuItem item = items [i];
				if ((style & SWT.SEPARATOR) == 0) {
					if (item.image == null || foreground != -1) {
						info.hbmpItem = hasImage || foreground != -1 ? OS.HBMMENU_CALLBACK : 0;
						OS.SetMenuItemInfo (handle, item.id, false, info);
					}
				}
			}
		}
	}

	/* Update the menu to hide or show the space for bitmaps */
	MENUINFO lpcmi = new MENUINFO ();
	lpcmi.cbSize = MENUINFO.sizeof;
	lpcmi.fMask = OS.MIM_STYLE;
	OS.GetMenuInfo (handle, lpcmi);
	if (hasImage && !hasCheck) {
		lpcmi.dwStyle |= OS.MNS_CHECKORBMP;
	} else {
		lpcmi.dwStyle &= ~OS.MNS_CHECKORBMP;
	}
	OS.SetMenuInfo (handle, lpcmi);
}

void updateBackground () {
	if (hBrush == 0) OS.DeleteObject (hBrush);
	hBrush = 0;
	if (backgroundImage != null) {
		hBrush = OS.CreatePatternBrush (backgroundImage.handle);
	} else {
		if (background != -1) hBrush = OS.CreateSolidBrush (background);
	}
	MENUINFO lpcmi = new MENUINFO ();
	lpcmi.cbSize = MENUINFO.sizeof;
	lpcmi.fMask = OS.MIM_BACKGROUND;
	lpcmi.hbrBack = hBrush;
	OS.SetMenuInfo (handle, lpcmi);
}

void updateForeground () {
	if (OS.WIN32_VERSION < OS.VERSION (4, 10)) return;
	MENUITEMINFO info = new MENUITEMINFO ();
	info.cbSize = MENUITEMINFO.sizeof;
	int index = 0;
	while (OS.GetMenuItemInfo (handle, index, true, info)) {
		info.fMask = OS.MIIM_BITMAP;
		info.hbmpItem = OS.HBMMENU_CALLBACK;
		OS.SetMenuItemInfo (handle, index, true, info);
		index++;
	}
	redraw ();
}
}

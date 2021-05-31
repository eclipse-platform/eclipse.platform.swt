/*******************************************************************************
 * Copyright (c) 2010, 2021 IBM Corporation and others.
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
 *     Tasktop Technologies - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Instances of this class represent the system task bar.
 *
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 *
 * @see Display#getSystemTaskBar
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 *
 * @since 3.6
 *
 * @noextend This class is not intended to be subclassed by clients.
 */
public class TaskBar extends Widget {
	int itemCount;
	TaskItem [] items = new TaskItem [4];
	ITaskbarList3 mTaskbarList3;
	String iconsDir;

	static final char [] EXE_PATH;
	static final PROPERTYKEY PKEY_Title = new PROPERTYKEY ();
	static final PROPERTYKEY PKEY_AppUserModel_IsDestListSeparator = new PROPERTYKEY ();
	static final String EXE_PATH_KEY = "org.eclipse.swt.win32.taskbar.executable";  //$NON-NLS-1$
	static final String EXE_ARGS_KEY = "org.eclipse.swt.win32.taskbar.arguments";  //$NON-NLS-1$
	static final String ICON_KEY = "org.eclipse.swt.win32.taskbar.icon";  //$NON-NLS-1$
	static final String ICON_INDEX_KEY = "org.eclipse.swt.win32.taskbar.icon.index";  //$NON-NLS-1$
	static {
		OS.PSPropertyKeyFromString ("{F29F85E0-4FF9-1068-AB91-08002B27B3D9} 2\0".toCharArray (), PKEY_Title); //$NON-NLS-1$
		OS.PSPropertyKeyFromString ("{9F4C2855-9F79-4B39-A8D0-E1D42DE1D5F3}, 6\0".toCharArray (), PKEY_AppUserModel_IsDestListSeparator); //$NON-NLS-1$
		char [] buffer = new char [OS.MAX_PATH];
		while (OS.GetModuleFileName (0, buffer, buffer.length) == buffer.length) {
			buffer = new char [buffer.length + OS.MAX_PATH];
		}
		EXE_PATH = buffer;
	}

TaskBar (Display display, int style) {
	this.display = display;
	createHandle ();
	reskinWidget ();
}

void createHandle () {
	long[] ppv = new long [1];
	int hr = COM.CoCreateInstance (COM.CLSID_TaskbarList, 0, COM.CLSCTX_INPROC_SERVER, COM.IID_ITaskbarList3, ppv);
	if (hr == COM.REGDB_E_CLASSNOTREG) error (SWT.ERROR_NOT_IMPLEMENTED);
	if (hr != OS.S_OK) error (SWT.ERROR_NO_HANDLES);
	mTaskbarList3 = new ITaskbarList3 (ppv [0]);
}

void createItem (TaskItem item, int index) {
	if (index == -1) index = itemCount;
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_INVALID_RANGE);
	if (itemCount == items.length) {
		TaskItem [] newItems = new TaskItem [items.length + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	System.arraycopy (items, index, items, index + 1, itemCount++ - index);
	items [index] = item;
}

void createItems () {
	for (Shell shell : display.getShells ()) {
		getItem (shell);
	}
	getItem (null);
}

IShellLink createShellLink (MenuItem item) {
	int style = item.getStyle ();
	if ((style & SWT.CASCADE) != 0) return null;
	long [] ppv = new long [1];
	int hr = COM.CoCreateInstance (COM.CLSID_ShellLink, 0, COM.CLSCTX_INPROC_SERVER, COM.IID_IShellLinkW, ppv);
	if (hr != OS.S_OK) error (SWT.ERROR_NO_HANDLES);
	IShellLink pLink = new IShellLink (ppv [0]);

	long hHeap = OS.GetProcessHeap ();
	long pv = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, OS.PROPVARIANT_sizeof());
	long titlePtr = 0;
	PROPERTYKEY key;
	if ((style & SWT.SEPARATOR) != 0) {
		OS.MoveMemory (pv, new short [] {OS.VT_BOOL}, 2);
		OS.MoveMemory (pv + 8, new short [] {OS.VARIANT_TRUE}, 2);
		key = PKEY_AppUserModel_IsDestListSeparator;
	} else {
		String text = item.getText ();
		int length = text.length ();
		char [] buffer = new char [length + 1];
		text.getChars (0, length, buffer, 0);
		titlePtr = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, buffer.length * 2);
		OS.MoveMemory (titlePtr, buffer, buffer.length * 2);
		OS.MoveMemory (pv, new short [] {OS.VT_LPWSTR}, 2);
		OS.MoveMemory (pv + 8, new long [] {titlePtr}, C.PTR_SIZEOF);
		key = PKEY_Title;

		String exePath = (String)item.getData (EXE_PATH_KEY);
		if (exePath != null) {
			length = exePath.length ();
			buffer = new char [length + 1];
			exePath.getChars (0, length, buffer, 0);
		} else {
			buffer = EXE_PATH;
		}
		hr = pLink.SetPath(buffer);
		if (hr != OS.S_OK) error (SWT.ERROR_INVALID_ARGUMENT);

		text = (String)item.getData (EXE_ARGS_KEY);
		if (text == null) text = Display.LAUNCHER_PREFIX + Display.TASKBAR_EVENT + item.id;
		length = text.length ();
		buffer = new char [length + 1];
		text.getChars (0, length, buffer, 0);
		hr = pLink.SetArguments(buffer);
		if (hr != OS.S_OK) error (SWT.ERROR_INVALID_ARGUMENT);

		/* This code is intentionally commented */
//		String tooltip = item.tooltip;
//		if (tooltip != null) {
//			length = tooltip.length ();
//			buffer = new char [length + 1];
//			tooltip.getChars (0, length, buffer, 0);
//			hr = pLink.SetDescription (buffer);
//			if (hr != OS.S_OK) error (SWT.ERROR_INVALID_ARGUMENT);
//		}

		String icon = (String)item.getData (ICON_KEY);
		int index = 0;
		if (icon != null) {
			text = (String)item.getData (ICON_INDEX_KEY);
			if (text != null) index = Integer.parseInt (text);
		} else {
			String directory = null;
			Image image = item.getImage ();
			if (image != null) directory = getIconsDir ();
			if (directory != null) {
				icon = directory + "\\" + "menu" + item.id + ".ico";
				ImageData data;
				if (item.hBitmap != 0) {
					Image image2 = Image.win32_new (display, SWT.BITMAP, item.hBitmap);
					data = image2.getImageData (DPIUtil.getDeviceZoom ());
					/*
					 * image2 instance doesn't own the handle and shall not be disposed. Make it
					 * appear disposed to cause leak trackers to ignore it.
					 */
					image2.handle = 0;
				} else {
					data = image.getImageData (DPIUtil.getDeviceZoom ());
				}
				ImageLoader loader = new ImageLoader ();
				loader.data = new ImageData [] {data};
				loader.save (icon, SWT.IMAGE_ICO);
			}
		}
		if (icon != null) {
			length = icon.length ();
			buffer = new char [length + 1];
			icon.getChars (0, length, buffer, 0);
			hr = pLink.SetIconLocation(buffer, index);
			if (hr != OS.S_OK) error (SWT.ERROR_INVALID_ARGUMENT);
		}
	}

	hr = pLink.QueryInterface(COM.IID_IPropertyStore, ppv);
	if (hr != OS.S_OK) error (SWT.ERROR_NO_HANDLES);
	IPropertyStore pPropStore = new IPropertyStore (ppv [0]);
	hr = pPropStore.SetValue(key, pv);
	if (hr != OS.S_OK) error (SWT.ERROR_INVALID_ARGUMENT);
	pPropStore.Commit();
	pPropStore.Release();

	OS.HeapFree (hHeap, 0, pv);
	if (titlePtr != 0) OS.HeapFree (hHeap, 0, titlePtr);
	return pLink;
}

IObjectArray createShellLinkArray (MenuItem [] items) {
	if (items == null) return null;
	if (items.length == 0) return null;
	long [] ppv = new long [1];
	int hr = COM.CoCreateInstance (COM.CLSID_EnumerableObjectCollection, 0, COM.CLSCTX_INPROC_SERVER, COM.IID_IObjectCollection, ppv);
	if (hr != OS.S_OK) error (SWT.ERROR_NO_HANDLES);
	IObjectCollection pObjColl = new IObjectCollection (ppv [0]);
	for (MenuItem item : items) {
		IShellLink pLink = createShellLink (item);
		if (pLink != null) {
			pObjColl.AddObject (pLink);
			if (hr != OS.S_OK) error (SWT.ERROR_INVALID_ARGUMENT);
			pLink.Release ();
		}
	}
	hr = pObjColl.QueryInterface(COM.IID_IObjectArray, ppv);
	if (hr != OS.S_OK) error (SWT.ERROR_NO_HANDLES);
	IObjectArray poa = new IObjectArray (ppv [0]);
	pObjColl.Release ();
	return poa;
}

void destroyItem (TaskItem item) {
	int index = 0;
	while (index < itemCount) {
		if (items [index] == item) break;
		index++;
	}
	if (index == itemCount) return;
	System.arraycopy (items, index + 1, items, index, --itemCount - index);
	items [itemCount] = null;
}

String getIconsDir() {
	if (iconsDir != null) return iconsDir;
	File dir = new File(display.appLocalDir + "\\ico_dir");
	if (dir.exists()) {
		// remove old icons
		for (File file : dir.listFiles()) file.delete();
	} else if (!dir.mkdirs()) {
		return null;
	}
	return iconsDir = dir.getPath();
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
public TaskItem getItem (int index) {
	checkWidget ();
	createItems ();
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_INVALID_RANGE);
	return items [index];
}

/**
 * Returns the <code>TaskItem</code> for the given <code>Shell</code> or the <code>TaskItem</code>
 * for the application if the <code>Shell</code> parameter is <code>null</code>.
 * If the requested item is not supported by the platform it returns <code>null</code>.
 *
 * @param shell the shell for which the task item is requested, or null to request the application item
 * @return the task item for the given shell or the application
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TaskItem getItem (Shell shell) {
	checkWidget ();
	for (TaskItem item : items) {
		if (item != null && item.shell == shell) {
			return item;
		}
	}
	TaskItem item = new TaskItem (this, SWT.NONE);
	if (shell != null) item.setShell (shell);
	return item;
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
	createItems ();
	return itemCount;
}

/**
 * Returns an array of <code>TaskItem</code>s which are the items
 * in the receiver.
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
public TaskItem [] getItems () {
	checkWidget ();
	createItems ();
	TaskItem [] result = new TaskItem [itemCount];
	System.arraycopy (items, 0, result, 0, result.length);
	return result;
}

@Override
void releaseChildren (boolean destroy) {
	if (items != null) {
		for (TaskItem item : items) {
			if (item != null && !item.isDisposed ()) {
				item.release (false);
			}
		}
		items = null;
	}
	super.releaseChildren (destroy);
}

@Override
void releaseParent () {
	super.releaseParent ();
	if (display.taskBar == this) display.taskBar = null;
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	mTaskbarList3.Release();
	mTaskbarList3 = null;
}

@Override
void reskinChildren (int flags) {
	if (items != null) {
		for (TaskItem item : items) {
			if (item != null) item.reskin (flags);
		}
	}
	super.reskinChildren (flags);
}

void setMenu (Menu menu) {
	long [] ppv = new long [1];
	int hr = COM.CoCreateInstance (COM.CLSID_DestinationList, 0, COM.CLSCTX_INPROC_SERVER, COM.IID_ICustomDestinationList, ppv);
	if (hr != OS.S_OK) error (SWT.ERROR_NO_HANDLES);
	ICustomDestinationList pDestList = new ICustomDestinationList (ppv [0]);
	String appName = Display.APP_NAME;
	char [] buffer = {'S', 'W', 'T', '\0'};
	if (appName != null && appName.length () > 0) {
		int length = appName.length ();
		buffer = new char [length + 1];
		appName.getChars (0, length, buffer, 0);
	}
	MenuItem [] items = null;
	if (menu != null && (items = menu.getItems ()).length != 0) {
		IObjectArray poa = createShellLinkArray (items);
		if (poa != null) {
			hr = pDestList.SetAppID (buffer);
			if (hr != OS.S_OK) error (SWT.ERROR_INVALID_ARGUMENT);

			int [] cMaxSlots = new int [1];
			pDestList.BeginList(cMaxSlots, COM.IID_IObjectArray, ppv);
			if (hr != OS.S_OK) error (SWT.ERROR_INVALID_ARGUMENT);
			IObjectArray pRemovedItems = new IObjectArray (ppv [0]);

			int [] count = new int [1];
			poa.GetCount (count);
			if (count [0] != 0) {
				hr = pDestList.AddUserTasks (poa);
				if (hr != OS.S_OK) error (SWT.ERROR_INVALID_ARGUMENT);
			}

			for (MenuItem item : items) {
				if ((item.getStyle () & SWT.CASCADE) != 0) {
					Menu subMenu = item.getMenu ();
					if (subMenu != null) {
						MenuItem [] subItems = subMenu.getItems ();
						IObjectArray poa2 = createShellLinkArray (subItems);
						if (poa2 != null) {
							poa2.GetCount (count);
							if (count [0] != 0) {
								String text = item.getText ();
								int length = text.length ();
								char [] buffer2 = new char [length + 1];
								text.getChars (0, length, buffer2, 0);
								hr = pDestList.AppendCategory (buffer2, poa2);
								if (hr != OS.S_OK) error (SWT.ERROR_INVALID_ARGUMENT);
							}
							poa2.Release ();
						}
					}
				}
			}
			poa.Release();
			hr = pDestList.CommitList ();
			if (hr != OS.S_OK) error (SWT.ERROR_INVALID_ARGUMENT);
			pRemovedItems.Release ();
		}
	} else {
		hr = pDestList.DeleteList (buffer);
		if (hr != OS.S_OK) error (SWT.ERROR_INVALID_ARGUMENT);
	}
	pDestList.Release ();
}

}

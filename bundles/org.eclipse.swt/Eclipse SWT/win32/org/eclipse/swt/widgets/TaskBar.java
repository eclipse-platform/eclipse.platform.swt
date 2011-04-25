/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Tasktop Technologies - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;

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
	int /*long*/ mTaskbarList3;
	
	static final char [] EXE_PATH;
	static final char [] ICO_DIR = {'i','c','o','_','d','i','r','\0'};
	static final PROPERTYKEY PKEY_Title = new PROPERTYKEY ();
	static final PROPERTYKEY PKEY_AppUserModel_IsDestListSeparator = new PROPERTYKEY ();
	static final String EXE_PATH_KEY = "org.eclipse.swt.win32.taskbar.executable";  //$NON-NLS-1$
	static final String EXE_ARGS_KEY = "org.eclipse.swt.win32.taskbar.arguments";  //$NON-NLS-1$
	static final String ICON_KEY = "org.eclipse.swt.win32.taskbar.icon";  //$NON-NLS-1$
	static final String ICON_INDEX_KEY = "org.eclipse.swt.win32.taskbar.icon.index";  //$NON-NLS-1$
	static final byte [] CLSID_TaskbarList = new byte [16]; 
	static final byte [] CLSID_DestinationList = new byte[16]; 
	static final byte [] CLSID_EnumerableObjectCollection = new byte[16]; 
	static final byte [] CLSID_ShellLink = new byte[16]; 
	static final byte [] CLSID_FileOperation = new byte [16];
	static final byte [] IID_ITaskbarList3 = new byte [16];
	static final byte [] IID_ICustomDestinationList = new byte[16]; 
	static final byte [] IID_IObjectArray = new byte[16]; 
	static final byte [] IID_IObjectCollection = new byte[16]; 
	static final byte [] IID_IShellLinkW = new byte[16]; 
	static final byte [] IID_IPropertyStore = new byte[16]; 
	static final byte [] IID_IShellItem = new byte [16]; 
	static final byte [] IID_IFileOperation = new byte [16];
	static final byte [] FOLDERID_LocalAppData = new byte [16];
	static {
		OS.IIDFromString ("{56FDF344-FD6D-11d0-958A-006097C9A090}\0".toCharArray (), CLSID_TaskbarList); //$NON-NLS-1$
		OS.IIDFromString ("{77f10cf0-3db5-4966-b520-b7c54fd35ed6}\0".toCharArray (), CLSID_DestinationList); //$NON-NLS-1$
		OS.IIDFromString ("{2d3468c1-36a7-43b6-ac24-d3f02fd9607a}\0".toCharArray (), CLSID_EnumerableObjectCollection); //$NON-NLS-1$
		OS.IIDFromString ("{00021401-0000-0000-C000-000000000046}\0".toCharArray (), CLSID_ShellLink); //$NON-NLS-1$
		OS.IIDFromString ("{3ad05575-8857-4850-9277-11b85bdb8e09}\0".toCharArray (), CLSID_FileOperation);
		OS.IIDFromString ("{EA1AFB91-9E28-4B86-90E9-9E9F8A5EEFAF}\0".toCharArray (), IID_ITaskbarList3); //$NON-NLS-1$	
		OS.IIDFromString ("{6332debf-87b5-4670-90c0-5e57b408a49e}\0".toCharArray (), IID_ICustomDestinationList); //$NON-NLS-1$
		OS.IIDFromString ("{92CA9DCD-5622-4bba-A805-5E9F541BD8C9}\0".toCharArray (), IID_IObjectArray); //$NON-NLS-1$
		OS.IIDFromString ("{5632b1a4-e38a-400a-928a-d4cd63230295}\0".toCharArray (), IID_IObjectCollection); //$NON-NLS-1$
		OS.IIDFromString ("{000214F9-0000-0000-C000-000000000046}\0".toCharArray (), IID_IShellLinkW); //$NON-NLS-1$
		OS.IIDFromString ("{886d8eeb-8cf2-4446-8d02-cdba1dbdcf99}\0".toCharArray (), IID_IPropertyStore); //$NON-NLS-1$
		OS.IIDFromString ("{43826d1e-e718-42ee-bc55-a1e261c37bfe}\0".toCharArray (), IID_IShellItem); //$NON-NLS-1$
		OS.IIDFromString ("{947aab5f-0a5c-4c13-b4d6-4bf7836fc9f8}\0".toCharArray (), IID_IFileOperation); //$NON-NLS-1$
		OS.IIDFromString ("{F1B32785-6FBA-4FCF-9D55-7B8E7F157091}\0".toCharArray (), FOLDERID_LocalAppData); //$NON-NLS-1$
		OS.PSPropertyKeyFromString ("{F29F85E0-4FF9-1068-AB91-08002B27B3D9} 2\0".toCharArray (), PKEY_Title); //$NON-NLS-1$
		OS.PSPropertyKeyFromString ("{9F4C2855-9F79-4B39-A8D0-E1D42DE1D5F3}, 6\0".toCharArray (), PKEY_AppUserModel_IsDestListSeparator); //$NON-NLS-1$
		TCHAR buffer = new TCHAR (0, OS.MAX_PATH);
		while (OS.GetModuleFileName (0, buffer, buffer.length ()) == buffer.length ()) {
			buffer = new TCHAR (0, buffer.length () + OS.MAX_PATH);
		}
		int length = buffer.strlen ();
		EXE_PATH = new char [length + 1];
		System.arraycopy (buffer.chars, 0, EXE_PATH, 0, length);
	}

TaskBar (Display display, int style) {
	if (display == null) display = Display.getCurrent ();
	if (display == null) display = Display.getDefault ();
	if (!display.isValidThread ()) {
		error (SWT.ERROR_THREAD_INVALID_ACCESS);
	}
	this.display = display;
	createHandle ();
	reskinWidget ();
}

void createHandle () {
	int /*long*/[] ppv = new int /*long*/ [1];
	int hr = OS.CoCreateInstance (CLSID_TaskbarList, 0, OS.CLSCTX_INPROC_SERVER, IID_ITaskbarList3, ppv);
	if (hr != OS.S_OK) error (SWT.ERROR_NO_HANDLES);
	mTaskbarList3 = ppv [0];
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
	Shell [] shells = display.getShells ();
	for (int i = 0; i < shells.length; i++) {
		getItem (shells[i]);
	}
	getItem (null);
}

int /*long*/ createShellLink (MenuItem item, String directory) {
	int style = item.getStyle ();
	if ((style & SWT.CASCADE) != 0) return 0;
	int /*long*/ [] ppv = new int /*long*/ [1];
	int hr = OS.CoCreateInstance (CLSID_ShellLink, 0, OS.CLSCTX_INPROC_SERVER, IID_IShellLinkW, ppv);
	if (hr != OS.S_OK) error (SWT.ERROR_NO_HANDLES);
	int /*long*/ pLink = ppv [0];

	int /*long*/ hHeap = OS.GetProcessHeap ();
	int /*long*/ pv = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, OS.PROPVARIANT_sizeof());
	int /*long*/ titlePtr = 0;
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
		OS.MoveMemory (pv + 8, new int /*long*/ [] {titlePtr}, OS.PTR_SIZEOF);
		key = PKEY_Title;
		
		/*IShellLink::SetPath*/
		String exePath = (String)item.getData (EXE_PATH_KEY);
		if (exePath != null) {
			length = exePath.length ();
			buffer = new char [length + 1];
			exePath.getChars (0, length, buffer, 0);
		} else {
			buffer = EXE_PATH;
		}
		hr = OS.VtblCall (20, pLink, buffer);
		if (hr != OS.S_OK) error (SWT.ERROR_INVALID_ARGUMENT);
		
		text =  (String)item.getData (EXE_ARGS_KEY);
		if (text == null) text = Display.LAUNCHER_PREFIX + Display.TASKBAR_EVENT + item.id;
		length = text.length ();
		buffer = new char [length + 1];
		text.getChars (0, length, buffer, 0);
		/*IShellLink::SetArguments*/
		hr = OS.VtblCall (11, pLink, buffer);
		if (hr != OS.S_OK) error (SWT.ERROR_INVALID_ARGUMENT);
		
		/* This code is intentionally commented */
//		String tooltip = item.tooltip;
//		if (tooltip != null) {
//			length = tooltip.length ();
//			buffer = new char [length + 1];
//			tooltip.getChars (0, length, buffer, 0);
//			/*IShellLink::SetDescription*/
//			hr = OS.VtblCall (7, pLink, buffer);
//			if (hr != OS.S_OK) error (SWT.ERROR_INVALID_ARGUMENT);
//		}
		
		String icon = (String)item.getData (ICON_KEY);
		int index = 0;
		if (icon != null) {
			text = (String)item.getData (ICON_INDEX_KEY);
			if (text != null) index = Integer.parseInt (text);
		} else {
			Image image = item.getImage ();
			if (image != null && directory != null) {
				icon = directory + "\\menu" + item.id + ".ico" ;
				ImageData data;
				if (item.hBitmap != 0) {
					Image image2 = Image.win32_new (display, SWT.BITMAP, item.hBitmap);
					data = image2.getImageData ();
				} else {
					data = image.getImageData ();
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
			/*IShellLink::SetIconLocation*/
			hr = OS.VtblCall (17, pLink, buffer, index);
			if (hr != OS.S_OK) error (SWT.ERROR_INVALID_ARGUMENT);
		}
	}
	
	/*IUnknown::QueryInterface*/
	hr = OS.VtblCall (0, pLink, IID_IPropertyStore, ppv);
	if (hr != OS.S_OK) error (SWT.ERROR_NO_HANDLES);
	int /*long*/ pPropStore = ppv [0];
	/*IPropertyStore::SetValue*/
	hr = OS.VtblCall (6, pPropStore, key, pv);
	if (hr != OS.S_OK) error (SWT.ERROR_INVALID_ARGUMENT);
	/*IPropertyStore::Commit*/
	OS.VtblCall (7, pPropStore);
	/*IUnknown::Release*/
	OS.VtblCall (2, pPropStore);
	
	OS.HeapFree (hHeap, 0, pv);
	if (titlePtr != 0) OS.HeapFree (hHeap, 0, titlePtr);
	return pLink;
}

int /*long*/ createShellLinkArray (MenuItem [] items, String directory) {
	if (items == null) return 0;
	if (items.length == 0) return 0;
	int /*long*/ [] ppv = new int /*long*/ [1];
	int hr = OS.CoCreateInstance (CLSID_EnumerableObjectCollection, 0, OS.CLSCTX_INPROC_SERVER, IID_IObjectCollection, ppv);
	if (hr != OS.S_OK) error (SWT.ERROR_NO_HANDLES);
	int /*long*/ pObjColl = ppv [0];
	for (int i = 0; i < items.length; i++) {
		int /*long*/ pLink = createShellLink (items[i], directory);
		if (pLink != 0) {
			/*IObjectCollection::AddObject*/
			hr = OS.VtblCall (5, pObjColl, pLink);
			if (hr != OS.S_OK) error (SWT.ERROR_INVALID_ARGUMENT);
			/*IUnknown::Release*/
			OS.VtblCall (2, pLink);
		}
	}
	/*IUnknown::QueryInterface*/
	hr = OS.VtblCall (0, pObjColl, IID_IObjectArray, ppv);
	if (hr != OS.S_OK) error (SWT.ERROR_NO_HANDLES);
	int /*long*/ poa = ppv [0];
	/*IUnknown::Release*/
	OS.VtblCall (2, pObjColl);
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

String getDirectory (char[] appName) {
	char [] appDir = new char [appName.length];
	for (int i = 0; i < appName.length; i++) {
		char c = appName [i];
		switch (c) {
			case '\\':
			case '/':
			case ':':
			case '*':
			case '?':
			case '\"':
			case '<':
			case '>':
			case '|':
				appDir [i] = '_';
				break;
			default:
				appDir [i] = c;
		}
	}
	String result = null;
	int /*long*/ [] ppv = new int /*long*/ [1];
	int hr = OS.SHCreateItemInKnownFolder (FOLDERID_LocalAppData, 0, null, IID_IShellItem, ppv);
	if (hr == OS.S_OK) {
		int /*long*/ psiRoot = ppv [0];
		hr = OS.CoCreateInstance (CLSID_FileOperation, 0, OS.CLSCTX_INPROC_SERVER, IID_IFileOperation, ppv);
		if (hr == OS.S_OK) {
			int /*long*/ pfo = ppv [0];
			/*IFileOperation.SetOperationFlags*/
			hr = OS.VtblCall (5, pfo, OS.FOF_NO_UI);
			if (hr == OS.S_OK) {
				int /*long*/ psiAppDir = getDirectory (psiRoot, pfo, appDir, false);
				if (psiAppDir != 0) {
					int /*long*/ psiIcoDir = getDirectory (psiAppDir, pfo, ICO_DIR, true);
					if (psiIcoDir != 0) {
						/*IShellItem::GetDisplayName*/
						hr = OS.VtblCall (5, psiIcoDir, OS.SIGDN_FILESYSPATH, ppv);
						if (hr == OS.S_OK) {
							int /*long*/ wstr = ppv [0]; 
							int length = OS.wcslen (wstr);
							char [] buffer = new char [length];
							OS.MoveMemory (buffer, wstr, length * 2);
							result = new String (buffer);
							OS.CoTaskMemFree (wstr);
						}
						/*IUnknown::Release*/
						OS.VtblCall (2, psiIcoDir);
					}
					/*IUnknown::Release*/
					OS.VtblCall (2, psiAppDir);
				}
			}
			/*IUnknown::Release*/
			OS.VtblCall(2, pfo);
		}
		/*IUnknown::Release*/
		OS.VtblCall (2, psiRoot);
	}
	return result;
}

int /*long*/ getDirectory (int /*long*/ parent, int /*long*/ pfo, char [] name, boolean delete) {
	int /*long*/ [] ppv = new int /*long*/ [1];
	int hr = OS.SHCreateItemFromRelativeName (parent, name, 0, IID_IShellItem, ppv);
	if (hr == OS.S_OK) {
		if (delete) {
			/*IFileOperation.Delete*/
			hr = OS.VtblCall (18, pfo, ppv [0], 0);
			/*IUnknown::Release*/
			OS.VtblCall (2, ppv [0]);
			if (hr == OS.S_OK) {
				/*IFileOperation.NewItem */
				hr = OS.VtblCall (20, pfo, parent, OS.FILE_ATTRIBUTE_DIRECTORY, name, null, 0);
				if (hr == OS.S_OK) {
					/*IFileOperation.PerformOperations */
					hr = OS.VtblCall (21, pfo);
					if (hr == OS.S_OK) {
						hr = OS.SHCreateItemFromRelativeName (parent, name, 0, IID_IShellItem, ppv);
						if (hr == OS.S_OK) {
							return ppv [0];
						}
					}
				}
			}
		} else {
			return ppv [0];
		}
	} else {
		/*IFileOperation.NewItem */
		hr = OS.VtblCall (20, pfo, parent, OS.FILE_ATTRIBUTE_DIRECTORY, name, null, 0);
		if (hr == OS.S_OK) {
			/*IFileOperation.PerformOperations */
			hr = OS.VtblCall (21, pfo);
			if (hr == OS.S_OK) {
				hr = OS.SHCreateItemFromRelativeName (parent, name, 0, IID_IShellItem, ppv);
				if (hr == OS.S_OK) {
					return ppv [0];
				}
			}
		}
	}
	return 0;
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
	for (int i = 0; i < items.length; i++) {
		if (items [i] != null && items [i].shell == shell) {
			return items [i];
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

void releaseChildren (boolean destroy) {
	if (items != null) {
		for (int i=0; i<items.length; i++) {
			TaskItem item = items [i];
			if (item != null && !item.isDisposed ()) {
				item.release (false);
			}
		}
		items = null;
	}
	super.releaseChildren (destroy);
}

void releaseParent () {
	super.releaseParent ();
	if (display.taskBar == this) display.taskBar = null;
}

void releaseWidget () {
	super.releaseWidget ();
	if (mTaskbarList3 != 0) {
		/* Release() */
		OS.VtblCall (2, mTaskbarList3);
		mTaskbarList3 = 0;
	}
}

void reskinChildren (int flags) {	
	if (items != null) {
		for (int i=0; i<items.length; i++) {
			TaskItem item = items [i];
			if (item != null) item.reskin (flags);
		}
	}
	super.reskinChildren (flags);
}

void setMenu (Menu menu) {
	int /*long*/ [] ppv = new int /*long*/ [1];
	int hr = OS.CoCreateInstance (CLSID_DestinationList, 0, OS.CLSCTX_INPROC_SERVER, IID_ICustomDestinationList, ppv);
	if (hr != OS.S_OK) error (SWT.ERROR_NO_HANDLES);
	int /*long*/ pDestList = ppv[0];
	String appName = Display.APP_NAME;
	char [] buffer = {'S', 'W', 'T', '\0'};
	if (appName != null && appName.length () > 0) {
		int length = appName.length ();
		buffer = new char [length + 1];
		appName.getChars (0, length, buffer, 0);
	}
	
	MenuItem [] items = null; 
	if (menu != null && (items = menu.getItems ()).length != 0) {
		String directory = null;
		for (int i = 0; i < items.length; i++) {
			MenuItem item = items [i];
			if (item.getImage () != null && item.getData (ICON_KEY) == null) {
				directory = getDirectory (buffer);
				break;
			}
		}
		int /*long*/ poa = createShellLinkArray (items, directory);
		if (poa != 0) {
			
			/*ICustomDestinationList::SetAppID*/
			hr = OS.VtblCall (3, pDestList, buffer);
			if (hr != OS.S_OK) error (SWT.ERROR_INVALID_ARGUMENT);
			
			/*ICustomDestinationList::BeginList*/
			int [] cMaxSlots = new int [1];
			OS.VtblCall (4, pDestList, cMaxSlots, IID_IObjectArray, ppv);
			if (hr != OS.S_OK) error (SWT.ERROR_INVALID_ARGUMENT);
			int /*long*/ pRemovedItems = ppv [0];
			
			int [] count = new int [1];
			/*IObjectArray::GetCount*/
			OS.VtblCall (3, poa, count);
			if (count [0] != 0) {
				/*ICustomDestinationList::AddUserTasks*/
				hr = OS.VtblCall (7, pDestList, poa);
				if (hr != OS.S_OK) error (SWT.ERROR_INVALID_ARGUMENT);
			}
			
			for (int i = 0; i < items.length; i++) {
				MenuItem item = items [i];
				if ((item.getStyle () & SWT.CASCADE) != 0) {
					Menu subMenu = item.getMenu ();
					if (subMenu != null) {
						MenuItem [] subItems = subMenu.getItems ();
						if (directory == null) {
							for (int j = 0; j < subItems.length; j++) {
								MenuItem subItem = subItems [j];
								if (subItem.getImage () != null && subItem.getData (ICON_KEY) == null) {
									directory = getDirectory (buffer);
									break;
								}
							}
						}
						int /*long*/ poa2 = createShellLinkArray (subItems, directory);
						if (poa2 != 0) {
							/*IObjectArray::GetCount*/
							OS.VtblCall (3, poa2, count);
							if (count [0] != 0) {
								String text = item.getText ();
								int length = text.length ();
								char [] buffer2 = new char [length + 1];
								text.getChars (0, length, buffer2, 0);
								/*ICustomDestinationList::AppendCategory*/
								hr = OS.VtblCall (5, pDestList, buffer2, poa2);
								if (hr != OS.S_OK) error (SWT.ERROR_INVALID_ARGUMENT);
							}
							/*IUnknown::Release*/
							OS.VtblCall (2, poa2);
						}
					}
				}
			}
			
			/*ICustomDestinationList::CommitList*/
			hr = OS.VtblCall (8, pDestList);
			if (hr != OS.S_OK) error (SWT.ERROR_INVALID_ARGUMENT);
			
			/*IUnknown::Release*/
			if (pRemovedItems != 0) OS.VtblCall (2, pRemovedItems);
			/*IUnknown::Release*/
			OS.VtblCall (2, poa);
		}
	} else {
		/*ICustomDestinationList::DeleteList*/
		hr = OS.VtblCall (10, pDestList, buffer);
		if (hr != OS.S_OK) error (SWT.ERROR_INVALID_ARGUMENT);
	}
	/*IUnknown::Release*/
	OS.VtblCall (2, pDestList);
}

}

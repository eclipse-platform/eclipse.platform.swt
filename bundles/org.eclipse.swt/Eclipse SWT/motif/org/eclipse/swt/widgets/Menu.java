package org.eclipse.swt.widgets;

/*
* Licensed Materials - Property of IBM,
* SWT - The Simple Widget Toolkit,
* (c) Copyright IBM Corp 1998, 1999.
*/

/* Imports */
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;

/**
*	A menu is a user interface object contains selectable
* menu items.
* <p>
* <b>Styles</b><br>
* <dd>BAR, DROP_DOWN, POP_UP<br>
*/

/* Class Definition */
public /*final*/ class Menu extends Widget {
	boolean hasLocation;
	MenuItem cascade, defaultItem;
	Decorations parent;
/**
* Creates a new instance of the widget.
*/
public Menu (Control parent) {
	this (checkNull(parent).getShell (), SWT.POP_UP);
}
/**
* Creates a new instance of the widget.
*/
public Menu (Decorations parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	createWidget (0);
}
/**
* Creates a new instance of the widget.
*/
public Menu (Menu parentMenu) {
	this (checkNull(parentMenu).parent, SWT.DROP_DOWN);
}
/**
* Creates a new instance of the widget.
*/
public Menu (MenuItem parentItem) {
	this (checkNull(parentItem).parent);
}
/**	 
* Adds the listener to receive events.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void addHelpListener (HelpListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Help, typedListener);
}
/**	 
* Adds the listener to receive events.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void addMenuListener(MenuListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.Hide,typedListener);
	addListener(SWT.Show,typedListener);
}
static int checkStyle (int style) {
	return checkBits (style, SWT.POP_UP, SWT.BAR, SWT.DROP_DOWN, 0, 0, 0);
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
void createHandle (int index) {
	state |= HANDLE;
	
	/*
	* Bug in Motif. For some reason, creating a menu after any application context
	* and shell have been destroyed will segment fault unless a new application
	* context and shell have been created in the current thread.  The fix is to
	* detect this case and create and destroy a temporary application context and
	* shell.
	*/
	int xDisplay = 0, shellHandle = 0;
	if (Display.DisplayDisposed) {
		int [] argc = new int [] {0};
		int xtContext = OS.XtCreateApplicationContext ();
		xDisplay = OS.XtOpenDisplay (xtContext, null, null, null, 0, 0, argc, 0);
		shellHandle = OS.XtAppCreateShell (null, null, OS.TopLevelShellWidgetClass (), xDisplay, null, 0);
	}
	
	/* BAR menu */
	if ((style & SWT.BAR) != 0) {
		int parentHandle = parent.scrolledHandle;
		handle = OS.XmCreateMenuBar (parentHandle, null, null, 0);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		return;
	}
	
	/* POPUP and PULLDOWN menus */
	
	/*
	* Bug in Motif.  When an existing popup menu is destroyed just
	* before creating a new popup menu and the new menu is managed,
	* the cursor changes to the menu cursor but the new menu is not
	* displayed.  Also, Motif fails to show a popup menu when the
	* mouse is released.  Both problems stem from the fact that the
	* popup menu is in the widget tree of a visible shell.  The fix
	* is to create all popup menus as children of a hidden dialog
	* shell.  Menus created this way are automatically destroyed
	* when the shell is destroyed.
	*/
	if ((style & SWT.POP_UP) != 0) {
		int parentHandle = parent.dialogHandle ();
		handle = OS.XmCreatePopupMenu (parentHandle, new byte [1], null, 0);
	} else {
		/*
		* Bug in Linux.  For some reason, when the parent of the pulldown
		* menu is not the main window handle, XtDestroyWidget() occasionally
		* segment faults when the shell is destroyed.  The fix is to ensure
		* that the parent is the main window.
		*/
		int parentHandle = parent.scrolledHandle;
		handle = OS.XmCreatePulldownMenu (parentHandle, new byte [1], null, 0);
	}
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);

	/* Workaround for bug in Motif */
	if (Display.DisplayDisposed) {
		if (shellHandle != 0) OS.XtDestroyWidget (shellHandle);
		if (xDisplay != 0) {
			int xtContext = OS.XtDisplayToApplicationContext (xDisplay);
			OS.XtDestroyApplicationContext (xtContext);
		}
	}
}
void createWidget (int index) {
	super.createWidget (index);
	parent.add (this);
}
/**
* Gets the default item.
* <p>
*
* @return the default menu item.
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public MenuItem getDefaultItem () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return defaultItem;
}
/**
* Gets the Display.
*/
public Display getDisplay () {
	Decorations parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}
/**
* Gets the enabled state.
* <p>
* @return the enabled flag
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public boolean getEnabled () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNsensitive, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1] != 0;
}
/**
* Gets an item at an index.
* <p>
* Indexing is zero based.
*
* This operation will fail when the index is out
* of range or an item could not be queried from
* the OS.
*
* @param index the index of the item
* @return the item
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_CANNOT_GET_ITEM)
*	when the operation fails
*/
public MenuItem getItem (int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNchildren, 0, OS.XmNnumChildren, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (argList [1] == 0) error (SWT.ERROR_CANNOT_GET_ITEM);
	int [] handles = new int [argList [3]];
	OS.memmove (handles, argList [1], argList[3] * 4);
	int i = 0, count = 0;
	while (i < argList [3]) {
		if (OS.XtIsManaged (handles [i])) {
			if (index == count) break;
			count++;
		}
		i++;
	}
	if (index != count) error (SWT.ERROR_INVALID_RANGE);
	Widget widget = WidgetTable.get (handles [i]);
	if (!(widget instanceof MenuItem)) error (SWT.ERROR_CANNOT_GET_ITEM);
	return (MenuItem) widget;
}
/**
* Gets the number of items.
* <p>
* @return the number of items
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public int getItemCount () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNchildren, 0, OS.XmNnumChildren, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (argList [1] == 0 || argList [3] == 0) return 0;
	int [] handles = new int [argList [3]];
	OS.memmove (handles, argList [1], argList [3] * 4);
	int count = 0;
	for (int i=0; i<argList [3]; i++) {
		if (OS.XtIsManaged (handles [i])) count++;
	}
	return count;	
}
/**
* Gets the items.
* <p>
* @return items
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public MenuItem [] getItems () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNchildren, 0, OS.XmNnumChildren, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int ptr = argList [1], count = argList [3];
	if (count == 0 || ptr == 0) return new MenuItem [0];
	int [] handles = new int [count];
	OS.memmove (handles, ptr, count * 4);
	MenuItem [] items = new MenuItem [count];
	int i = 0, j = 0;
	while (i < count) {
		Widget item = WidgetTable.get (handles [i]);
		if (item != null) items [j++] = (MenuItem) item;
		i++;
	}
	if (i == j) return items;
	MenuItem [] newItems = new MenuItem [j];
	System.arraycopy (items, 0, newItems, 0, j);
	return newItems;
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
* Gets the parent.
* <p>
* @return the parent
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public Decorations getParent () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent;
}
/**
* Gets the parent item.
* <p>
* @return the parent item
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public MenuItem getParentItem () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return cascade;
}
/**
* Gets the parent menu.
* <p>
* @return the parent menu
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public Menu getParentMenu () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (cascade != null) return cascade.parent;
	return null;
}
/**
* Gets the shell.
* <p>
* @return the shell
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public Shell getShell () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getShell ();
}
/**
* Gets the visibility state.
* <p>
*	If the parent is not visible or some other condition
* makes the widget not visible, the widget can still be
* considered visible even though it may not actually be
* showing.
*
* @return visible the visibility state
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public boolean getVisible () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return OS.XtIsManaged (handle);
}
void hookEvents () {
	int windowProc = parent.getShell ().getDisplay ().windowProc;
	OS.XtAddCallback (handle, OS.XmNhelpCallback, windowProc, SWT.Help);
	OS.XtAddCallback (handle, OS.XmNmapCallback, windowProc, SWT.Show);
	OS.XtAddCallback (handle, OS.XmNunmapCallback, windowProc, SWT.Hide);
}
/**
* Gets the index of an item.
* <p>
* The list is searched starting at 0 until an
* item is found that is equal to the search item.
* If no item is found, -1 is returned.  Indexing
* is zero based.
*
* @param item the search item
* @return the index of the item
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when string is null
*/
public int indexOf (MenuItem item) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNchildren, 0, OS.XmNnumChildren, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int [] handles = new int [argList [3]];
	OS.memmove (handles, argList [1], handles.length * 4);
	int index = 0;
	for (int i=0; i<handles.length; i++) {
		if (OS.XtIsManaged (handles [i])) {
			if (handles [i] == item.handle) return index;
			index++;
		}
	}
	return -1;
}
public boolean isEnabled () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	Menu parentMenu = getParentMenu ();
	if (parentMenu == null) return getEnabled ();
	return getEnabled () && parentMenu.isEnabled ();
}
/**
* Gets the visibility status.
* <p>
*	This method returns the visibility status of the
* widget in the widget hierarchy.  If the parent is not
* visible or some other condition makes the widget not
* visible, this method will return false.
*
* @return the actual visibility state
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public boolean isVisible () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return getVisible ();
}
int processHelp (int callData) {
	sendHelpEvent (callData);
	return 0;
}
int processHide (int callData) {
	sendEvent (SWT.Hide);
	return 0;
}
int processShow (int callData) {
	/*
	* SWT.Selection events are posted to allow stepping
	* in the VA/Java debugger.  SWT.Show events are
	* sent to ensure that application event handler
	* code runs before the menu is displayed.  This
	* means that SWT.Show events would normally occur
	* before SWT.Selection events.  While this is not 
	* strictly incorrect, applications often use the 
	* SWT.Selection event to update the state of menu
	* items and would like the ordering of events to 
	* be the other way around.
	*
	* The fix is to run the deferred events before
	* the menu is shown.  This means that stepping
	* through a selection event that was caused by
	* a popup menu will fail in VA/Java.
	*/
	Display display = getDisplay ();
	display.runDeferredEvents ();
	sendEvent (SWT.Show);
	return 0;
}
void releaseChild () {
	super.releaseChild ();
	if (cascade != null) cascade.setMenu (null);
	if (((style & SWT.BAR) != 0) && (this == parent.menuBar)) parent.setMenuBar (null);
}
void releaseWidget () {
	MenuItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		MenuItem item = items [i];
		if (!item.isDisposed ()) {
			item.releaseWidget ();
			item.releaseHandle ();
		}
	}
	super.releaseWidget ();
	if (parent != null) parent.remove (this);
	parent = null;
	cascade = defaultItem = null;
}
/**	 
* Removes the listener.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void removeHelpListener (HelpListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Help, listener);
}
/**	 
* Removes the listener.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void removeMenuListener(MenuListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Hide, listener);
	eventTable.unhook(SWT.Show, listener);
}
void sendHelpEvent (int callData) {
	if (hooks (SWT.Help)) {
		postEvent (SWT.Help);
		return;
	}
	parent.sendHelpEvent (callData);
}
/**
* Sets the default item.
* <p>
* @param item the default menu item (or null)
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setDefaultItem (MenuItem item) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	defaultItem = item;
}
/**
* Sets the enabled state.
* <p>
* @param enabled the new value of the enabled flag
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setEnabled (boolean enabled) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNsensitive, enabled ? 1 : 0};
	OS.XtSetValues (handle, argList, argList.length / 2);
}
public void setLocation (int x, int y) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & (SWT.BAR | SWT.DROP_DOWN)) != 0) return;
	int [] argList = {OS.XmNx, x, OS.XmNy, y};
	OS.XtSetValues (handle, argList, argList.length / 2);
	hasLocation = true;
}
/**
* Sets the visibility state.
* <p>
*	If the parent is not visible or some other condition
* makes the widget not visible, the widget can still be
* considered visible even though it may not actually be
* showing.
*
* @param visible the new visibility state
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setVisible (boolean visible) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & (SWT.BAR | SWT.DROP_DOWN)) != 0) return;
	if (visible) {
		if (!hasLocation) {
			int xDisplay = OS.XtDisplay (handle);
			if (xDisplay == 0) return;
			int xWindow = OS.XDefaultRootWindow (xDisplay);
			if (xWindow == 0) return;
			int [] unused = new int [1];
			int [] rootX = new int [1], rootY = new int [1];
			if (OS.XQueryPointer (
				xDisplay, xWindow, unused, unused,
				rootX, rootY,
				unused, unused, unused) == 0) return;
			int [] argList = {OS.XmNx, rootX [0], OS.XmNy, rootY [0]};
			OS.XtSetValues (handle, argList, argList.length / 2);
		}
		OS.XtManageChild (handle);
	} else {
		OS.XtUnmanageChild (handle);
	}
}
}

package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class are user interface objects that contain
 * menu items.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>BAR, DROP_DOWN, POP_UP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Help, Hide, Show </dd>
 * </dl>
 * <p>
 * Note: Only one of BAR, DROP_DOWN and POP_UP may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class Menu extends Widget {
	int x, y;
	boolean hasLocation;
	MenuItem cascade, selectedItem;
	Decorations parent;
	int barHandle;
	Callback barHandleCallback;
	
/**
 * Constructs a new instance of this class given its parent,
 * and sets the style for the instance so that the instance
 * will be a popup menu on the given parent's shell.
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
	this (parent.getShell (), SWT.POP_UP);
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
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Menu (Decorations parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	createWidget (0);
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Menu</code>) and sets the style
 * for the instance so that the instance will be a drop-down
 * menu on the given parent's parent.
 *
 * @param parent a menu which will be the parent of the new instance (cannot be null)
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
	this (parentMenu.parent, SWT.DROP_DOWN);
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>MenuItem</code>) and sets the style
 * for the instance so that the instance will be a drop-down
 * menu on the given parent's parent menu.
 *
 * @param parent a menu item which will be the parent of the new instance (cannot be null)
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
	this (parentItem.parent);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.POP_UP, SWT.BAR, SWT.DROP_DOWN, 0, 0, 0);
}

void addAccelerators (int accelGroup) {
	MenuItem [] items = getItems ();
	for (int i = 0; i < items.length; i++) {
		MenuItem item = items[i];
		item.addAccelerator (accelGroup);
	}
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Hide,typedListener);
	addListener (SWT.Show,typedListener);
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Help, typedListener);
}

void createHandle (int index) {
	state |= HANDLE;
	if ((style & SWT.BAR) != 0) {
		handle = OS.gtk_menu_bar_new ();
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		int parentHandle = parent.fixedHandle;
		OS.gtk_container_add (parentHandle, handle);
	} else {
		handle = OS.gtk_menu_new ();
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	}
}

void createWidget (int index) {
	super.createWidget (index);
	parent.add (this);
}

void destroyAccelGroup () {
	parent.destroyAccelGroup ();
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
	checkWidget();
	return null;
}

public Display getDisplay () {
	Decorations parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}

/**
 * Returns <code>true</code> if the receiver is enabled, and
 * <code>false</code> otherwise. A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getEnabled () {
	checkWidget();
	return OS.GTK_WIDGET_SENSITIVE (handle);     
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
	checkWidget();
	int list = OS.gtk_container_get_children (handle);
	if (list == 0) error (SWT.ERROR_CANNOT_GET_ITEM);
	int data = OS.g_list_nth_data (list, index);
	OS.g_list_free (list);
	if (data == 0) error (SWT.ERROR_CANNOT_GET_ITEM);
	return (MenuItem) WidgetTable.get (data);
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
	checkWidget();
	int list = OS.gtk_container_get_children (handle);
	if (list == 0) return 0;
	int itemCount = OS.g_list_length (list);
	OS.g_list_free (list);
	return itemCount;
}

/**
 * Returns an array of <code>MenuItem</code>s which are the items
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
public MenuItem [] getItems () {
	checkWidget();
	int list = OS.gtk_container_get_children (handle);
	if (list == 0) return new MenuItem [0];
	int count = OS.g_list_length (list);
	MenuItem [] items = new MenuItem [count];
	for (int i=0; i<count; i++) {
		int data = OS.g_list_nth_data (list, i);
		items [i] = (MenuItem) WidgetTable.get (data);
	}
	OS.g_list_free (list);
	return items;
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
	checkWidget();
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
	checkWidget();
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
	checkWidget();
	if (cascade == null) return null;
	return cascade.getParent ();
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
	checkWidget();
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
	checkWidget();
	return OS.GTK_WIDGET_MAPPED (handle);    
}

int GtkMenuPositionFunc (int menu, int x, int y, int push_in, int user_data) {
	if (x != 0) OS.memmove (x, new int [] {this.x}, 4);
	if (y != 0) OS.memmove (y, new int [] {this.y}, 4);
	return 0;
}

int GtkMenuBarEventFunc (int widget, int event, int user_data) {
	if (user_data == SWT.MouseDown) return 1;
	if (user_data == 0) {
		GdkEvent gdkEvent = new GdkEvent ();
		OS.memmove(gdkEvent, event, GdkEvent.sizeof);
		if (gdkEvent.type == OS.GDK_BUTTON_RELEASE) {
			OS.gtk_menu_popdown (handle);
		}
	}
	return 0;
}

void hookEvents () {
	super.hookEvents ();
	Display display = getDisplay ();
	int windowProc2 = display.windowProc2;
	int windowProc3 = display.windowProc3;
	OS.gtk_signal_connect (handle, OS.show, windowProc2, SWT.Show);
	OS.gtk_signal_connect (handle, OS.hide, windowProc2, SWT.Hide);
	OS.gtk_signal_connect (handle, OS.show_help, windowProc3, SWT.Help);
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
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (MenuItem item) {
	checkWidget();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	MenuItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		if (items [i] == item) return i;
	}
	return -1;
}

/**
 * Returns <code>true</code> if the receiver is enabled and all
 * of the receiver's ancestors are enabled, and <code>false</code>
 * otherwise. A disabled control is typically not selectable from the
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
	checkWidget();
	return getEnabled () && getParent ().getEnabled ();
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
	checkWidget();
	return getVisible ();
}

int processHelp (int int0, int int1, int int2) {
	if (sendHelpEvent (int0)) OS.gtk_menu_shell_deactivate (handle);
	return 0;
}

int processHide (int int0, int int1, int int2) {
	if ((style & SWT.POP_UP) != 0) {
		/* Release resources and unwanted grabs */
		int grabHandle = OS.gtk_grab_get_current ();
		if (grabHandle != 0) OS.gtk_grab_remove (grabHandle);
		if (OS.gdk_pointer_is_grabbed ()) {
			OS.gdk_pointer_ungrab (OS.GDK_CURRENT_TIME);
			OS.gdk_keyboard_ungrab (OS.GDK_CURRENT_TIME);
		}
		if (barHandle != 0) {
			OS.gtk_signal_disconnect_by_data (barHandle, SWT.MouseDown);
			OS.gtk_signal_disconnect_by_data (barHandle, 0);
			OS.gtk_widget_destroy (barHandle);
			barHandle = 0;
		}
		if (barHandleCallback != null) {
			barHandleCallback.dispose ();
			barHandleCallback = null;
		}
	}
	sendEvent (SWT.Hide);
	return 0;
}

int processShow (int int0, int int1, int int2) {
	if ((style & SWT.POP_UP) != 0) return 0;
	sendEvent (SWT.Show);
	if ((style & SWT.BAR) != 0) return 0;
	parent.createAccelGroup ();
	return 0;
}

void releaseChild () {
	super.releaseChild ();
	if (cascade != null) cascade.setMenu (null);
	if ((style & SWT.BAR) != 0 && this == parent.menuBar) {
		parent.setMenuBar (null);
	}
}

void releaseWidget () {
	MenuItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		MenuItem item = items [i];
		if (!item.isDisposed ()) item.releaseResources ();
	}
	if (parent != null) parent.remove (this);
	super.releaseWidget ();
	parent = null;
	cascade = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the menu events are generated for the control.
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
 * @see #addMenuListener
 */
public void removeMenuListener (MenuListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Hide, listener);
	eventTable.unhook (SWT.Show, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the help events are generated for the control.
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
 * @see #addHelpListener
 */
public void removeHelpListener (HelpListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Help, listener);
}

boolean sendHelpEvent (int helpType) {
	if (selectedItem != null && !selectedItem.isDisposed()) {
		if (selectedItem.hooks (SWT.Help)) {
			selectedItem.postEvent (SWT.Help);
			return true;
		}
	}
	if (hooks (SWT.Help)) {
		postEvent (SWT.Help);
		return true;
	}
	return parent.sendHelpEvent (helpType);
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
	checkWidget();
}

/**
 * Enables the receiver if the argument is <code>true</code>,
 * and disables it otherwise. A disabled control is typically
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
	checkWidget();
	OS.gtk_widget_set_sensitive (handle, enabled);
}

/**
 * Sets the receiver's location to the point specified by
 * the arguments which are relative to the display.
 * <p>
 * Note:  This is different from most widgets where the
 * location of the widget is relative to the parent.
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
	checkWidget();
	if ((style & (SWT.BAR | SWT.DROP_DOWN)) != 0) return;
	this.x = x;
	this.y = y;
	hasLocation = true;	
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
	checkWidget();
	if ((style & (SWT.BAR | SWT.DROP_DOWN)) != 0) return;
	if (visible == OS.GTK_WIDGET_MAPPED (handle)) return;
	if (visible) {
		sendEvent (SWT.Show);
		if (getItemCount () != 0) {
			/*
			* Feature in GTK.  When gtk_menu_popup() is called with no parent menu shell
			* and parent menu item, the menu temporarily takes focus from the top level shell
			* and then restores the focus when the menu pops down.  This behavior is not
			* incorrect but is unwanted.  The fix is to create a temporary menu bar and item
			* to pass to gtk_menu_popup().  This requires special code to pop down the
			* menu when the user clicks outside of the menu and special code to ensure
			* that an item is selected when it is pressed.
			*/
			int parentHandle = parent.fixedHandle;
			int width = OS.GTK_WIDGET_WIDTH (parentHandle);
			int height = OS.GTK_WIDGET_HEIGHT (parentHandle);
			barHandle = OS.gtk_menu_bar_new ();
			if (barHandle == 0) error (SWT.ERROR_NO_HANDLES);
			OS.gtk_container_add (parentHandle, barHandle);
			OS.gtk_fixed_move (parentHandle, barHandle, width, height);
			OS.gtk_widget_show (barHandle);
			int itemHandle = OS.gtk_image_menu_item_new_with_label (new byte[1]);
			if (itemHandle == 0) error (SWT.ERROR_NO_HANDLES);
			OS.gtk_menu_shell_insert (barHandle, itemHandle, 0);
			OS.gtk_widget_show (itemHandle);
			OS.gtk_menu_shell_select_item (barHandle, itemHandle);
			barHandleCallback = new Callback (this, "GtkMenuBarEventFunc", 3);
			int barHandleProc = barHandleCallback.getAddress ();
			if (barHandleProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
			OS.gtk_signal_connect (barHandle, OS.event_after, barHandleProc, 0);
			OS.gtk_signal_connect (barHandle, OS.button_press_event, barHandleProc, SWT.MouseDown);

			/* Pop up the menu */
			int address = 0;
			Callback GtkMenuPositionFunc = null;
			if (hasLocation) {
				GtkMenuPositionFunc = new Callback (this, "GtkMenuPositionFunc", 5);
				address = GtkMenuPositionFunc.getAddress ();
			}
			OS.gtk_menu_popup (handle, barHandle, itemHandle, address, 0, 0, OS.gtk_get_current_event_time());
			if (GtkMenuPositionFunc != null) GtkMenuPositionFunc.dispose ();
		} else {
			sendEvent (SWT.Hide);
		}
	} else {
		OS.gtk_menu_popdown (handle);
	}
}
}

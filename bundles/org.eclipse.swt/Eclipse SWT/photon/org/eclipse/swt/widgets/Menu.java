package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

public class Menu extends Widget {
	int x, y;
	boolean hasLocation;
	Decorations parent;
	MenuItem cascade, defaultItem;

public Menu (Control parent) {
	this (checkNull (parent).getShell (), SWT.POP_UP);
}

public Menu (Decorations parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	createWidget (0);
}

public Menu (Menu parentMenu) {
	this (checkNull (parentMenu).parent, SWT.DROP_DOWN);
}

public Menu (MenuItem parentItem) {
	this (checkNull (parentItem).parent);
}

static Control checkNull (Control control) {
	if (control == null) error (SWT.ERROR_NULL_ARGUMENT);
	return control;
}

static Menu checkNull (Menu menu) {
	if (menu == null) error (SWT.ERROR_NULL_ARGUMENT);
	return menu;
}

static MenuItem checkNull (MenuItem item) {
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	return item;
}

static int checkStyle (int style) {
	return checkBits (style, SWT.POP_UP, SWT.BAR, SWT.DROP_DOWN, 0, 0, 0);
}

public void addHelpListener (HelpListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Help, typedListener);
}

public void addMenuListener (MenuListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Hide,typedListener);
	addListener (SWT.Show,typedListener);
}

void createHandle (int index) {
	state |= HANDLE;
	int parentHandle = parent.topHandle ();
	if ((style & SWT.BAR) != 0) {
		int [] args = {
			OS.Pt_ARG_FLAGS, 0, OS.Pt_GETS_FOCUS,
			OS.Pt_ARG_FLAGS, OS.Pt_DELAY_REALIZE, OS.Pt_DELAY_REALIZE,
		};
		handle = OS.PtCreateWidget (OS.PtMenuBar (), parentHandle, args.length / 3, args);
	} else {
		handle = OS.PtCreateWidget (OS.PtMenu (), parentHandle, 0, null);
	}
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
}

void createWidget (int index) {
	super.createWidget (index);
	parent.add (this);
}

public MenuItem getDefaultItem () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return defaultItem;
}

public Display getDisplay () {
	Decorations parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}

public boolean getEnabled () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {OS.Pt_ARG_FLAGS, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return (args [1] & OS.Pt_BLOCKED) == 0;
}

public int getItemCount () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int count = 0;
	int child = OS.PtWidgetChildBack (handle);
	if (child != 0 && (style & SWT.BAR) != 0) child = OS.PtWidgetChildBack (child);
	while (child != 0) {
		child = OS.PtWidgetBrotherInFront (child);
		count++;
	}
	return count;
}

public MenuItem getItem (int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (index < 0) error (SWT.ERROR_INVALID_RANGE);	
	int i = 0;
	int child = OS.PtWidgetChildBack (handle);
	if (child != 0 && (style & SWT.BAR) != 0) child = OS.PtWidgetChildBack (child);
	while (child != 0) {
		Widget widget = WidgetTable.get (child);
		if (widget != null && widget instanceof MenuItem) {
			if (i++ == index) return (MenuItem) widget;
		}
		child = OS.PtWidgetBrotherInFront (child);
	}
	error (SWT.ERROR_INVALID_RANGE);
	return null;
}

public MenuItem [] getItems () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int count = 0;
	int child = OS.PtWidgetChildBack (handle);
	if (child != 0  && (style & SWT.BAR) != 0) child = OS.PtWidgetChildBack (child);
	while (child != 0) {
		child = OS.PtWidgetBrotherInFront (child);
		count++;
	}
	MenuItem [] items = new MenuItem [count];
	int i = 0, j = 0;
	child = OS.PtWidgetChildBack (handle);
	if (child != 0 && (style & SWT.BAR) != 0) child = OS.PtWidgetChildBack (child);
	while (i < count) {
		Widget widget = WidgetTable.get (child);
		if (widget != null && widget instanceof MenuItem) {
			items [j++] = (MenuItem) widget;
		}
		i++;
		child = OS.PtWidgetBrotherInFront (child);
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

public Decorations getParent () {
	return parent;
}

public MenuItem getParentItem () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return cascade;
}

public Menu getParentMenu () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (cascade != null) return cascade.parent;
	return null;
}

public Shell getShell () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getShell ();
}

public boolean getVisible () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return true;
}

void hookEvents () {
	int windowProc = getDisplay ().windowProc;
	OS.PtAddCallback (handle, OS.Pt_CB_UNREALIZED, windowProc, SWT.Hide);
}

public int indexOf (MenuItem item) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	int i = 0;
	int child = OS.PtWidgetChildBack (handle);
	if (child != 0 && (style & SWT.BAR) != 0) child = OS.PtWidgetChildBack (child);
	while (child != 0) {
		Widget widget = WidgetTable.get (child);
		if (item == widget) return i;
		if (widget != null && widget instanceof MenuItem) i++;
		child = OS.PtWidgetBrotherInFront (child);
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

public boolean isVisible () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return getVisible ();
}

int processHide (int info) {
	if (cascade != null) {
		int [] args = {OS.Pt_ARG_MENU_FLAGS, 0, OS.Pt_MENU_CHILD};
		OS.PtSetResources (handle, args.length / 3, args);
		int shellHandle = parent.topHandle ();
		OS.PtReParentWidget (handle, shellHandle);
	}
	sendEvent (SWT.Hide);
	return OS.Pt_CONTINUE;
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
		if (!item.isDisposed ()) {
			item.releaseWidget ();
			item.releaseHandle ();
		}
	}
	super.releaseWidget ();
	if (parent != null) parent.remove (this);
	parent = null;
	cascade = null;
}

public void removeHelpListener (HelpListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Help, listener);
}

public void removeMenuListener (MenuListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Hide, listener);
	eventTable.unhook (SWT.Show, listener);
}

public void setDefaultItem (MenuItem item) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	defaultItem = item;
}

public void setEnabled (boolean enabled) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {
		OS.Pt_ARG_FLAGS, enabled ? 0 : OS.Pt_BLOCKED, OS.Pt_BLOCKED,
		OS.Pt_ARG_FLAGS, enabled ? 0 : OS.Pt_GHOST, OS.Pt_GHOST,
	};
	OS.PtSetResources (handle, args.length / 3, args);
}

public void setLocation (int x, int y) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	this.x = x;  this.y = y;
	hasLocation = true;
}

public void setVisible (boolean visible) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & SWT.POP_UP) == 0) return;
	if (visible == OS.PtWidgetIsRealized (handle)) return;
	if (visible) {
		PhPoint_t pt = new PhPoint_t ();
		pt.x = (short) x;
		pt.y = (short) y;
		if (!hasLocation) {		
			int ig = OS.PhInputGroup (0);
			PhCursorInfo_t info = new PhCursorInfo_t ();
			OS.PhQueryCursor ((short) ig, info);
			pt.x = info.last_press_x;
			pt.y = info.last_press_y;
		}
		int ptr = OS.malloc (PhPoint_t.sizeof);
		OS.memmove (ptr, pt, PhPoint_t.sizeof);
		int [] args = {OS.Pt_ARG_POS, ptr, 0};
		OS.PtSetResources (handle, args.length / 3, args);
		OS.free (ptr);
		sendEvent (SWT.Show);
		OS.PtRealizeWidget (handle);
	} else {
		OS.PtUnrealizeWidget(handle);
	}
}

}

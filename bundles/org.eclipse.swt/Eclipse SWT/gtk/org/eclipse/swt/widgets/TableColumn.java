/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class represent a column in a table widget.
 * <p><dl>
 * <dt><b>Styles:</b></dt>
 * <dd>LEFT, RIGHT, CENTER</dd>
 * <dt><b>Events:</b></dt>
 * <dd> Move, Resize, Selection</dd>
 * </dl>
 * </p><p>
 * Note: Only one of the styles LEFT, RIGHT and CENTER may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#table">Table, TableItem, TableColumn snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class TableColumn extends Item {
	int /*long*/ labelHandle, imageHandle, buttonHandle;
	Table parent;
	int modelIndex, lastButton, lastTime, lastX, lastWidth;
	boolean customDraw, useFixedWidth;
	String toolTipText;

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Table</code>) and a style value
 * describing its behavior and appearance. The item is added
 * to the end of the items maintained by its parent.
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
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#CENTER
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TableColumn (Table parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	createWidget (parent.getColumnCount ());
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Table</code>), a style value
 * describing its behavior and appearance, and the index
 * at which to place it in the items maintained by its parent.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 * <p>
 * Note that due to a restriction on some platforms, the first column
 * is always left aligned.
 * </p>
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 * @param index the zero-relative index to store the receiver in its parent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the parent (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#CENTER
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TableColumn (Table parent, int style, int index) {
	super (parent, checkStyle (style));
	this.parent = parent;
	createWidget (index);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is moved or resized, by sending
 * it one of the messages defined in the <code>ControlListener</code>
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
 * @see ControlListener
 * @see #removeControlListener
 */
public void addControlListener(ControlListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Resize,typedListener);
	addListener (SWT.Move,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the column header is selected.
 * <code>widgetDefaultSelected</code> is not called.
 * </p>
 *
 * @param listener the listener which should be notified when the control is selected by the user
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #removeSelectionListener
 * @see SelectionEvent
 */
public void addSelectionListener (SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

void createWidget (int index) {
	parent.createItem (this, index);
	setOrientation (true);
	hookEvents ();
	register ();
	text = "";
}

void deregister() {
	super.deregister ();
	display.removeWidget (handle);
	if (buttonHandle != 0) display.removeWidget (buttonHandle);
	if (labelHandle != 0) display.removeWidget (labelHandle);
}

void destroyWidget () {
	parent.destroyItem (this);
	releaseHandle ();
}

/**
 * Returns a value which describes the position of the
 * text or image in the receiver. The value will be one of
 * <code>LEFT</code>, <code>RIGHT</code> or <code>CENTER</code>.
 *
 * @return the alignment 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getAlignment () {
	checkWidget();
	if ((style & SWT.LEFT) != 0) return SWT.LEFT;
	if ((style & SWT.CENTER) != 0) return SWT.CENTER;
	if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
	return SWT.LEFT;
}

/**
 * Gets the moveable attribute. A column that is
 * not moveable cannot be reordered by the user 
 * by dragging the header but may be reordered 
 * by the programmer.
 *
 * @return the moveable attribute
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see Table#getColumnOrder()
 * @see Table#setColumnOrder(int[])
 * @see TableColumn#setMoveable(boolean)
 * @see SWT#Move
 * 
 * @since 3.1
 */
public boolean getMoveable() {
	checkWidget();
	return OS.gtk_tree_view_column_get_reorderable (handle);
}

/**
 * Returns the receiver's parent, which must be a <code>Table</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Table getParent () {
	checkWidget();
	return parent;
}

/**
 * Gets the resizable attribute. A column that is
 * not resizable cannot be dragged by the user but
 * may be resized by the programmer.
 *
 * @return the resizable attribute
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getResizable () {
	checkWidget();
	return OS.gtk_tree_view_column_get_resizable (handle);
}

/**
 * Returns the receiver's tool tip text, or null if it has
 * not been set.
 *
 * @return the receiver's tool tip text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public String getToolTipText () {
	checkWidget();
	return toolTipText;
}

/**
 * Gets the width of the receiver.
 *
 * @return the width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getWidth () {
	checkWidget();
	if (!OS.gtk_tree_view_column_get_visible (handle)) {
		return 0;
	}
	if (useFixedWidth) return OS.gtk_tree_view_column_get_fixed_width (handle);
	return OS.gtk_tree_view_column_get_width (handle);
}

int /*long*/ gtk_clicked (int /*long*/ widget) {
	/*
	* There is no API to get a double click on a table column.  Normally, when
	* the mouse is double clicked, this is indicated by GDK_2BUTTON_PRESS
	* but the table column sends the click signal on button release.  The fix is to
	* test for double click by remembering the last click time and mouse button
	* and testing for the double click interval.
	*/
	boolean doubleClick = false;
	boolean postEvent = true;
	int /*long*/ eventPtr = OS.gtk_get_current_event ();
	if (eventPtr != 0) {
		GdkEventButton gdkEvent = new GdkEventButton ();
		OS.memmove (gdkEvent, eventPtr, GdkEventButton.sizeof);
		OS.gdk_event_free (eventPtr);
		switch (gdkEvent.type) {
			case OS.GDK_BUTTON_RELEASE: {
				int clickTime = display.getDoubleClickTime ();
				int eventTime = gdkEvent.time, eventButton = gdkEvent.button;
				if (lastButton == eventButton && lastTime != 0 && Math.abs (lastTime - eventTime) <= clickTime) {
					doubleClick = true;
				}
				lastTime = eventTime == 0 ? 1: eventTime;
				lastButton = eventButton;
				break;
			}
			case OS.GDK_MOTION_NOTIFY: {
				/*
				* Bug in GTK.  Dragging a column in a GtkTreeView causes a clicked 
				* signal to be emitted even though the mouse button was never released.
				* The fix to ignore the signal if the current GDK event is a motion notify.
				* The GTK bug was fixed in version 2.6
				*/
				if (OS.GTK_VERSION < OS.VERSION (2, 6, 0)) postEvent = false;
				break;
			}
		}
	}
	if (postEvent) sendSelectionEvent (doubleClick ? SWT.DefaultSelection : SWT.Selection);
	return 0;
}

int /*long*/ gtk_event_after (int /*long*/ widget, int /*long*/ gdkEvent) {
	GdkEvent event = new GdkEvent ();
	OS.memmove (event, gdkEvent, GdkEvent.sizeof);
	switch (event.type) {
		case OS.GDK_BUTTON_PRESS: {
			GdkEventButton gdkEventButton = new GdkEventButton ();
			OS.memmove (gdkEventButton, gdkEvent, GdkEventButton.sizeof);
			if (gdkEventButton.button == 3) {
				parent.showMenu ((int) gdkEventButton.x_root, (int) gdkEventButton.y_root);
			}
			break;
		}
	}
	return 0;
}

int /*long*/ gtk_mnemonic_activate (int /*long*/ widget, int /*long*/ arg1) {
	return parent.gtk_mnemonic_activate (widget, arg1);
}

int /*long*/ gtk_size_allocate (int /*long*/ widget, int /*long*/ allocation) {
	useFixedWidth = false;
	int x = OS.GTK_WIDGET_X (widget);
	int width = OS.GTK_WIDGET_WIDTH (widget);
	if (x != lastX) {
		lastX = x;
		sendEvent (SWT.Move);
	}
	if (width != lastWidth) {
		lastWidth = width;
		sendEvent (SWT.Resize);
	}
	return 0;
}

void hookEvents () {
	super.hookEvents ();
	OS.g_signal_connect_closure (handle, OS.clicked, display.closures [CLICKED], false);
	if (buttonHandle != 0) {
		OS.g_signal_connect_closure_by_id (buttonHandle, display.signalIds [SIZE_ALLOCATE], 0, display.closures [SIZE_ALLOCATE], false);
		OS.g_signal_connect_closure_by_id (buttonHandle, display.signalIds [EVENT_AFTER], 0, display.closures [EVENT_AFTER], false);
	}
	if (labelHandle != 0) OS.g_signal_connect_closure_by_id (labelHandle, display.signalIds [MNEMONIC_ACTIVATE], 0, display.closures [MNEMONIC_ACTIVATE], false);
}

/**
 * Causes the receiver to be resized to its preferred size.
 * For a composite, this involves computing the preferred size
 * from its layout, if there is one.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 */
public void pack () {
	checkWidget();
	int width = 0;
	if (buttonHandle != 0) {
		GtkRequisition requisition = new GtkRequisition ();
		OS.gtk_widget_size_request (buttonHandle, requisition);
		width = requisition.width;
	}
	if ((parent.style & SWT.VIRTUAL) != 0) {
		for (int i=0; i<parent.items.length; i++) {
			TableItem item = parent.items [i];
			if (item != null && item.cached) {
				width = Math.max (width, parent.calculateWidth (handle, item.handle));
			}
		}
	} else {
		int /*long*/ iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
		if (OS.gtk_tree_model_get_iter_first (parent.modelHandle, iter)) {
			do {
				width = Math.max (width, parent.calculateWidth (handle, iter));
			} while (OS.gtk_tree_model_iter_next(parent.modelHandle, iter));
		}
		OS.g_free (iter);
	}
	setWidth(width);
}

void register () {
	super.register ();
	display.addWidget (handle, this);
	if (buttonHandle != 0) display.addWidget (buttonHandle, this);
	if (labelHandle != 0) display.addWidget (labelHandle, this);
}

void releaseHandle () {
	super.releaseHandle ();
	handle = buttonHandle = labelHandle = imageHandle = 0;
	modelIndex = -1; 
	parent = null;
}

void releaseParent () {
	super.releaseParent ();
	if (parent.sortColumn == this) {
		parent.sortColumn = null;
	}
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is moved or resized.
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
 * @see ControlListener
 * @see #addControlListener
 */
public void removeControlListener (ControlListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Move, listener);
	eventTable.unhook (SWT.Resize, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is selected by the user.
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
 * @see SelectionListener
 * @see #addSelectionListener
 */
public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

/**
 * Controls how text and images will be displayed in the receiver.
 * The argument should be one of <code>LEFT</code>, <code>RIGHT</code>
 * or <code>CENTER</code>.
 * <p>
 * Note that due to a restriction on some platforms, the first column
 * is always left aligned.
 * </p>
 * @param alignment the new alignment 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setAlignment (int alignment) {
	checkWidget();
	if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) return;
	int index = parent.indexOf (this);
	if (index == -1 || index == 0) return;
	style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	style |= alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	parent.createRenderers (handle, modelIndex, index == 0, style);
}

void setFontDescription (int /*long*/ font) {
	OS.gtk_widget_modify_font (labelHandle, font);
	OS.gtk_widget_modify_font (imageHandle, font);
}

public void setImage (Image image) {
	checkWidget ();
	super.setImage (image);
	if (image != null) {
		ImageList headerImageList = parent.headerImageList;
		if (headerImageList == null) {
			headerImageList = parent.headerImageList = new ImageList ();
		}
		int imageIndex = headerImageList.indexOf (image);
		if (imageIndex == -1) imageIndex = headerImageList.add (image);
		int /*long*/ pixbuf = headerImageList.getPixbuf (imageIndex);
		OS.gtk_image_set_from_pixbuf (imageHandle, pixbuf);
		OS.gtk_widget_show (imageHandle);
	} else {
		OS.gtk_image_set_from_pixbuf (imageHandle, 0);
		OS.gtk_widget_hide (imageHandle);
	}
}

/**
 * Sets the resizable attribute.  A column that is
 * resizable can be resized by the user dragging the
 * edge of the header.  A column that is not resizable 
 * cannot be dragged by the user but may be resized 
 * by the programmer.
 *
 * @param resizable the resize attribute
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setResizable (boolean resizable) {
	checkWidget();
	OS.gtk_tree_view_column_set_resizable (handle, resizable);
}

/**
 * Sets the moveable attribute.  A column that is
 * moveable can be reordered by the user by dragging
 * the header. A column that is not moveable cannot be 
 * dragged by the user but may be reordered 
 * by the programmer.
 *
 * @param moveable the moveable attribute
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see Table#setColumnOrder(int[])
 * @see Table#getColumnOrder()
 * @see TableColumn#getMoveable()
 * @see SWT#Move
 * 
 * @since 3.1
 */
public void setMoveable (boolean moveable) {
	checkWidget();
	OS.gtk_tree_view_column_set_reorderable (handle, moveable);
}

void setOrientation (boolean create) {
	if ((parent.style & SWT.RIGHT_TO_LEFT) != 0 || !create) {
		if (buttonHandle != 0) {
			int dir = (parent.style & SWT.RIGHT_TO_LEFT) != 0 ? OS.GTK_TEXT_DIR_RTL : OS.GTK_TEXT_DIR_LTR;
			OS.gtk_widget_set_direction (buttonHandle, dir);
			OS.gtk_container_forall (buttonHandle, display.setDirectionProc, dir);
		}
	}
}

public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	super.setText (string);
	char [] chars = fixMnemonic (string);
	byte [] buffer = Converter.wcsToMbcs (null, chars, true);
	OS.gtk_label_set_text_with_mnemonic (labelHandle, buffer);
	if (string.length () != 0) {
		OS.gtk_widget_show (labelHandle);
	} else {
		OS.gtk_widget_hide (labelHandle);
	}
}

/**
 * Sets the receiver's tool tip text to the argument, which
 * may be null indicating that the default tool tip for the 
 * control will be shown. For a control that has a default
 * tool tip, such as the Tree control on Windows, setting
 * the tool tip text to an empty string replaces the default,
 * causing no tool tip text to be shown.
 * <p>
 * The mnemonic indicator (character '&amp;') is not displayed in a tool tip.
 * To display a single '&amp;' in the tool tip, the character '&amp;' can be 
 * escaped by doubling it in the string.
 * </p>
 * 
 * @param string the new tool tip text (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public void setToolTipText (String string) {
	checkWidget();
	Shell shell = parent._getShell ();
	setToolTipText (shell, string);
	toolTipText = string;
}

void setToolTipText (Shell shell, String newString) {
	shell.setToolTipText (buttonHandle, newString);
}

/**
 * Sets the width of the receiver.
 *
 * @param width the new width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setWidth (int width) {
	checkWidget();
	if (width < 0) return;
	if (width == lastWidth) return;
	if (width > 0) {
		useFixedWidth = true;
		OS.gtk_tree_view_column_set_fixed_width (handle, width);
	}
	/*
	 * Bug in GTK.  For some reason, calling gtk_tree_view_column_set_visible()
	 * when the parent is not realized fails to show the column. The fix is to
	 * ensure that the table has been realized.
	 */
	if (width != 0) OS.gtk_widget_realize (parent.handle);
	OS.gtk_tree_view_column_set_visible (handle, width != 0);
	lastWidth = width;
	/*
	 * Bug in GTK. When the column is made visible the event window of column
	 * header is raised above the gripper window of the previous column. In 
	 * some cases, this can cause the previous column to be not resizable by
	 * the mouse. The fix is to find the event window and lower it to bottom to
	 * the z-order stack. 
	 */
	if (width != 0) {
		if (buttonHandle != 0) {
			int /*long*/ window = OS.gtk_widget_get_parent_window (buttonHandle);
			if (window != 0) {
				int /*long*/ windowList = OS.gdk_window_get_children (window);
				if (windowList != 0) {
					int /*long*/ windows = windowList;
					int /*long*/ [] userData = new int /*long*/ [1];
					while (windows != 0) {
						int /*long*/ child = OS.g_list_data (windows);
						OS.gdk_window_get_user_data (child, userData);
						if (userData[0] == buttonHandle) {
							OS.gdk_window_lower (child);
							break;
						}
						windows = OS.g_list_next (windows);
					}
					OS.g_list_free (windowList);
				}
			}
		}
	}
	sendEvent (SWT.Resize);
}

}

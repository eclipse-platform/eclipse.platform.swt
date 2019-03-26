/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;

/**
 * Instances of this class represent a selectable user interface object
 * that represents a button in a tool bar.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>PUSH, CHECK, RADIO, SEPARATOR, DROP_DOWN</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles CHECK, PUSH, RADIO, SEPARATOR and DROP_DOWN
 * may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#toolbar">ToolBar, ToolItem snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ToolItem extends Item {
	long arrowHandle, labelHandle, imageHandle;
	long eventHandle, proxyMenuItem, provider;
	ToolBar parent;
	Control control;
	Image hotImage, disabledImage;
	String toolTipText;
	boolean drawHotImage;

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>ToolBar</code>) and a style value
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
 * @see SWT#PUSH
 * @see SWT#CHECK
 * @see SWT#RADIO
 * @see SWT#SEPARATOR
 * @see SWT#DROP_DOWN
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ToolItem (ToolBar parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	createWidget (parent.getItemCount ());
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>ToolBar</code>), a style value
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
 *
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
 * @see SWT#PUSH
 * @see SWT#CHECK
 * @see SWT#RADIO
 * @see SWT#SEPARATOR
 * @see SWT#DROP_DOWN
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ToolItem (ToolBar parent, int style, int index) {
	super (parent, checkStyle (style));
	this.parent = parent;
	int count = parent.getItemCount ();
	if (!(0 <= index && index <= count)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	createWidget (index);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called when the mouse is over the arrow portion of a drop-down tool,
 * the event object detail field contains the value <code>SWT.ARROW</code>.
 * <code>widgetDefaultSelected</code> is not called.
 * </p>
 * <p>
 * When the <code>SWT.RADIO</code> style bit is set, the <code>widgetSelected</code> method is
 * also called when the receiver loses selection because another item in the same radio group
 * was selected by the user. During <code>widgetSelected</code> the application can use
 * <code>getSelection()</code> to determine the current selected state of the receiver.
 * </p>
 *
 * @param listener the listener which should be notified when the control is selected by the user,
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
public void addSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.PUSH, SWT.CHECK, SWT.RADIO, SWT.SEPARATOR, SWT.DROP_DOWN, 0);
}

@Override
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

@Override
void createHandle (int index) {
	state |= HANDLE;
	int bits = SWT.SEPARATOR | SWT.RADIO | SWT.CHECK | SWT.PUSH | SWT.DROP_DOWN;
	if ((style & SWT.SEPARATOR) == 0) {
		labelHandle = GTK.gtk_label_new_with_mnemonic (null);
		if (labelHandle == 0) error (SWT.ERROR_NO_HANDLES);
		imageHandle = GTK.gtk_image_new_from_pixbuf (0);
		if (imageHandle == 0) error (SWT.ERROR_NO_HANDLES);
	}
	switch (style & bits) {
		case SWT.SEPARATOR:
			handle = GTK.gtk_separator_tool_item_new ();
			if (handle == 0) error (SWT.ERROR_NO_HANDLES);
			GTK.gtk_separator_tool_item_set_draw (handle, true);
			break;
		case SWT.DROP_DOWN:
			handle = GTK.gtk_menu_tool_button_new (0, null);
			if (handle == 0) error (SWT.ERROR_NO_HANDLES);
			/*
			 * Feature in GTK. The arrow button of DropDown tool-item is
			 * disabled when it does not contain menu. The fix is to
			 * find the arrow button handle and enable it.
			 */
			long child = GTK.gtk_bin_get_child (handle);
			long list = GTK.gtk_container_get_children (child);
			arrowHandle = OS.g_list_nth_data (list, 1);
			if (arrowHandle != 0) {
				GTK.gtk_widget_set_sensitive (arrowHandle, true);
			}
			break;
		case SWT.RADIO:
			/*
			* Because GTK enforces radio behavior in a button group
			* a radio group is not created for each set of contiguous
			* buttons, each radio button will not draw unpressed.
			* The fix is to use toggle buttons instead.
			*/
		case SWT.CHECK:
			handle = GTK.gtk_toggle_tool_button_new ();
			if (handle == 0) error (SWT.ERROR_NO_HANDLES);
			break;
		case SWT.PUSH:
		default:
			handle = GTK.gtk_tool_button_new (0, null);
			if (handle == 0) error (SWT.ERROR_NO_HANDLES);
			break;
	}
	if (labelHandle != 0) {
		GTK.gtk_tool_button_set_label_widget(handle, labelHandle);
	}
	if (imageHandle != 0) {
		GTK.gtk_tool_button_set_icon_widget(handle, imageHandle);
	}
	if ((parent.state & FONT) != 0) {
		setFontDescription (parent.getFontDescription());
	}
	/*
	 * Feature in GTK. GtkToolButton class uses this property to
	 * determine whether to show or hide its label when the toolbar
	 * style is GTK_TOOLBAR_BOTH_HORIZ (or SWT.RIGHT).
	 */
	if ((parent.style & SWT.RIGHT) != 0) GTK.gtk_tool_item_set_is_important (handle, true);
	if ((style & SWT.SEPARATOR) == 0) GTK.gtk_tool_button_set_use_underline (handle, true);
}

@Override
void createWidget (int index) {
	super.createWidget (index);
	showWidget (index);
	parent.relayout ();
}

Widget [] computeTabList () {
	if (isTabGroup ()) {
		if (getEnabled ()) {
			if ((style & SWT.SEPARATOR) != 0) {
				if (control != null) return control.computeTabList();
			} else {
				return new Widget [] {this};
			}
		}
	}
	return new Widget [0];
}

@Override
void deregister() {
	super.deregister ();
	if (eventHandle != 0) display.removeWidget (eventHandle);
	if (arrowHandle != 0) display.removeWidget (arrowHandle);
}

@Override
public void dispose () {
	if (isDisposed ()) return;
	ToolBar parent = this.parent;
	super.dispose ();
	parent.relayout ();
}

/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent.
 *
 * @return the receiver's bounding rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle getBounds () {
	checkWidget ();
	return DPIUtil.autoScaleDown (getBoundsInPixels ());
}

Rectangle getBoundsInPixels () {
	checkWidget();
	parent.forceResize ();
	long topHandle = topHandle ();
	GtkAllocation allocation = new GtkAllocation ();
	GTK.gtk_widget_get_allocation (topHandle, allocation);
	int x = allocation.x;
	int y = allocation.y;
	int width = allocation.width;
	int height = allocation.height;
	if ((parent.style & SWT.MIRRORED) != 0) x = parent.getClientWidth () - width - x;
	if ((style & SWT.SEPARATOR) != 0 && control != null) height = Math.max (height, 23);
	return new Rectangle (x, y, width, height);
}

/**
 * Returns the control that is used to fill the bounds of
 * the item when the item is a <code>SEPARATOR</code>.
 *
 * @return the control
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Control getControl () {
	checkWidget();
	return control;
}

/**
 * Returns the receiver's disabled image if it has one, or null
 * if it does not.
 * <p>
 * The disabled image is displayed when the receiver is disabled.
 * </p>
 *
 * @return the receiver's disabled image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getDisabledImage () {
	checkWidget();
	return disabledImage;
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
 *
 * @see #isEnabled
 */
public boolean getEnabled () {
	checkWidget();
	long topHandle = topHandle ();
	return GTK.gtk_widget_get_sensitive (topHandle);
}

/**
 * Returns the receiver's hot image if it has one, or null
 * if it does not.
 * <p>
 * The hot image is displayed when the mouse enters the receiver.
 * </p>
 *
 * @return the receiver's hot image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getHotImage () {
	checkWidget();
	return hotImage;
}

/**
 * Returns the receiver's parent, which must be a <code>ToolBar</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public ToolBar getParent () {
	checkWidget();
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent;
}

/**
 * Returns <code>true</code> if the receiver is selected,
 * and false otherwise.
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked (which some platforms draw as a
 * pushed in button). If the receiver is of any other type, this method
 * returns false.
 * </p>
 *
 * @return the selection state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getSelection () {
	checkWidget();
	if ((style & (SWT.CHECK | SWT.RADIO)) == 0) return false;
	return GTK.gtk_toggle_tool_button_get_active (handle);
}

/**
 * Returns the receiver's tool tip text, or null if it has not been set.
 *
 * @return the receiver's tool tip text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
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
	checkWidget ();
	return DPIUtil.autoScaleDown (getWidthInPixels ());
}

int getWidthInPixels () {
	checkWidget();
	parent.forceResize ();
	long topHandle = topHandle ();
	GtkAllocation allocation = new GtkAllocation();
	GTK.gtk_widget_get_allocation (topHandle, allocation);
	return allocation.width;
}

@Override
long gtk_event (long widget, long event) {
	if (!GTK.GTK4) return 0;
	int eventType = GDK.gdk_event_get_event_type(event);
	switch (eventType) {
		case GDK.GDK4_BUTTON_PRESS: {
			return gtk_button_press_event(widget, event);
		}
		case GDK.GDK4_BUTTON_RELEASE: {
			return gtk_button_release_event(widget, event);
		}
	}
	return 0;
}

@Override
long gtk_button_press_event (long widget, long event) {
	return parent.gtk_button_press_event (widget, event);
}

@Override
long gtk_button_release_event (long widget, long event) {
	return parent.gtk_button_release_event (widget, event);
}

@Override
long gtk_clicked (long widget) {
	Event event = new Event ();
	if ((style & SWT.DROP_DOWN) != 0) {
		long eventPtr = GTK.gtk_get_current_event ();
		if (eventPtr != 0) {
			int eventType = GDK.gdk_event_get_event_type(eventPtr);
			eventType = Control.fixGdkEventTypeValues(eventType);
			long topHandle = topHandle();
			switch (eventType) {
				case GDK.GDK_KEY_RELEASE: //Fall Through..
				case GDK.GDK_BUTTON_PRESS:
				case GDK.GDK_2BUTTON_PRESS:
				case GDK.GDK_BUTTON_RELEASE: {
					boolean isArrow = false;
					if (widget == arrowHandle) {
						isArrow = true;
						topHandle = widget;
						/*
						 * Feature in GTK. ArrowButton stays in toggled state if there is no popup menu.
						 * It is required to set back the state of arrow to normal state after it is clicked.
						 */
						OS.g_signal_handlers_block_matched (widget, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CLICKED);
						GTK.gtk_toggle_button_set_active(widget, false);
						OS.g_signal_handlers_unblock_matched (widget, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CLICKED);
					}
					if (isArrow) {
						event.detail = SWT.ARROW;
						GtkAllocation allocation = new GtkAllocation ();
						GTK.gtk_widget_get_allocation (topHandle, allocation);
						event.x = DPIUtil.autoScaleDown(allocation.x);
						if ((style & SWT.MIRRORED) != 0) event.x = DPIUtil.autoScaleDown (parent.getClientWidth ()- allocation.width) - event.x;
						event.y = DPIUtil.autoScaleDown(allocation.y + allocation.height);
					}
					break;
				}
			}
			gdk_event_free (eventPtr);
		}
	}
	if ((style & SWT.RADIO) != 0) {
		if ((parent.getStyle () & SWT.NO_RADIO_GROUP) == 0) {
			selectRadio ();
		}
	}
	sendSelectionEvent (SWT.Selection, event, false);
	return 0;
}

@Override
long gtk_create_menu_proxy (long widget) {
	/*
	 * Feature in GTK. If the item is a radio/check button
	 * with only image, then that image does not appear in
	 * the overflow menu.
	 * The fix is to create and use the proxy menu for the
	 * items appearing in the overflow menu.
	 */
	byte [] buffer = Converter.wcsToMbcs ("menu-id", true); //$NON-NLS-1$
	if (proxyMenuItem != 0) {
		/*
		 * The menuItem to appear in the overflow menu is cached
		 * for the tool-item. If the text/image of the item changes,
		 * then the proxyMenu is reset.
		 */
		GTK.gtk_tool_item_set_proxy_menu_item (widget, buffer, proxyMenuItem);
		return 1;
	}

	if (image != null) {
		ImageList imageList = parent.imageList;
		if (imageList != null) {
			int index = imageList.indexOf (image);
			if (index != -1) {
				long pixbuf = imageList.getPixbuf (index);
				byte[] label = null;
				int [] showImages = new int []{1};
				/*
				 * GTK tool items with only image appear as blank items
				 * in overflow menu when the system property "gtk-menu-images"
				 * is set to false. To avoid that, display the tooltip text
				 * if available, in the overflow menu.
				 * Feature in GTK. When the menuItem is initialised only
				 * with the image, the overflow menu appears very sloppy.
				 * The fix is to initialise menu item with empty string.
				 */
				if (text == null || text.length() == 0) {
					if ((showImages [0] == 0) && (toolTipText != null))
						label = Converter.wcsToMbcs(toolTipText, true);
					else
						label = new byte[]{0};
				}
				else {
					label = Converter.wcsToMbcs(text, true);
				}
				long menuItem = GTK.gtk_menu_item_new ();
				if (menuItem == 0) error (SWT.ERROR_NO_HANDLES);

				long boxHandle = gtk_box_new (GTK.GTK_ORIENTATION_HORIZONTAL, false, 6);
				if (boxHandle == 0) error (SWT.ERROR_NO_HANDLES);

				long menuLabel = GTK.gtk_accel_label_new (label);
				if (menuLabel == 0) error (SWT.ERROR_NO_HANDLES);
				if (GTK.GTK_VERSION >= OS.VERSION (3, 16, 0)) {
					GTK.gtk_label_set_xalign (labelHandle, 0);
					GTK.gtk_widget_set_halign (labelHandle, GTK.GTK_ALIGN_FILL);
				} else {
					GTK.gtk_misc_set_alignment(labelHandle, 0, 0);
				}

				long menuImage = GTK.gtk_image_new_from_pixbuf (pixbuf);
				if (menuImage == 0) error (SWT.ERROR_NO_HANDLES);

				GTK.gtk_container_add (boxHandle, menuImage);
				gtk_box_pack_end (boxHandle, menuLabel, true, true, 0);
				GTK.gtk_container_add (menuItem, boxHandle);
				GTK.gtk_tool_item_set_proxy_menu_item (widget, buffer, menuItem);

				/*
				 * Since the arrow button does not appear in the drop_down
				 * item, we request the menu-item and then, hook the
				 * activate signal to send the Arrow selection signal.
				 */
				proxyMenuItem = GTK.gtk_tool_item_get_proxy_menu_item (widget, buffer);
				OS.g_signal_connect(menuItem, OS.activate, ToolBar.menuItemSelectedFunc.getAddress(), handle);
				return 1;
			}
		}
	}
	return 0;
}

void gtk_css_provider_load_from_css (long context, String css) {
	/* Utility function. */
	//@param css : a 'css java' string like "{\nbackground: red;\n}".
	if (provider == 0) {
		provider = GTK.gtk_css_provider_new ();
		GTK.gtk_style_context_add_provider (context, provider, GTK.GTK_STYLE_PROVIDER_PRIORITY_APPLICATION);
		OS.g_object_unref (provider);
	}
	if (GTK.GTK4) {
		GTK.gtk_css_provider_load_from_data (provider, Converter.wcsToMbcs (css, true), -1);
	} else {
		GTK.gtk_css_provider_load_from_data (provider, Converter.wcsToMbcs (css, true), -1, null);
	}
}

@Override
long gtk_enter_notify_event (long widget, long event) {
	parent.gtk_enter_notify_event (widget, event);
	drawHotImage = (parent.style & SWT.FLAT) != 0 && hotImage != null;
	if (drawHotImage) {
		ImageList imageList = parent.imageList;
		if (imageList != null) {
			int index = imageList.indexOf (hotImage);
			if (index != -1 && imageHandle != 0) {
				long pixbuf = imageList.getPixbuf (index);
				gtk_image_set_from_gicon(imageHandle, pixbuf);
			}
		}
	}
	return 0;
}

@Override
long gtk_event_after (long widget, long gdkEvent) {
	int eventType = GDK.gdk_event_get_event_type(gdkEvent);
	eventType = Control.fixGdkEventTypeValues(eventType);
	switch (eventType) {
		case GDK.GDK_BUTTON_PRESS: {
			int [] eventButton = new int [1];
			GDK.gdk_event_get_button(gdkEvent, eventButton);
			if (eventButton[0] == 3) {
				double [] eventRX = new double [1];
				double [] eventRY = new double [1];
				GDK.gdk_event_get_root_coords(gdkEvent, eventRX, eventRY);
				parent.showMenu ((int) eventRX[0], (int) eventRY[0]);
			}
			break;
		}
	}
	return 0;
}

@Override
long gtk_focus_in_event (long widget, long event) {
	parent.hasChildFocus = true;
	parent.currentFocusItem = this;
	return 0;
}

@Override
long gtk_focus_out_event (long widget, long event) {
	parent.hasChildFocus = false;
	return 0;
}

@Override
long gtk_leave_notify_event (long widget, long event) {
	parent.gtk_leave_notify_event (widget, event);
	if (drawHotImage) {
		drawHotImage = false;
		if (image != null) {
			ImageList imageList = parent.imageList;
			if (imageList != null) {
				int index = imageList.indexOf (image);
				if (index != -1 && imageHandle != 0) {
					long pixbuf = imageList.getPixbuf (index);
					gtk_image_set_from_gicon(imageHandle, pixbuf);
				}
			}
		}
	}
	return 0;
}

@Override
long gtk_map (long widget) {
	parent.fixZOrder ();
	return 0;
}

@Override
long gtk_mnemonic_activate (long widget, long arg1) {
	return parent.gtk_mnemonic_activate (widget, arg1);
}

@Override
void hookEvents () {
	super.hookEvents ();
	if ((style & SWT.SEPARATOR) != 0) return;
	OS.g_signal_connect_closure (handle, OS.clicked, display.getClosure (CLICKED), false);
	/*
	 * Feature in GTK. GtkToolItem does not respond to basic listeners
	 * such as button-press, enter-notify to it. The fix is to assign
	 * the listener to child (GtkButton) of the tool-item.
	 */
	eventHandle = GTK.gtk_bin_get_child(handle);
	if ((style & SWT.DROP_DOWN) != 0) {
		long list = GTK.gtk_container_get_children(eventHandle);
		eventHandle = OS.g_list_nth_data(list, 0);
		if (arrowHandle != 0) OS.g_signal_connect_closure (arrowHandle, OS.clicked, display.getClosure (CLICKED), false);
	}
	OS.g_signal_connect_closure (handle, OS.create_menu_proxy, display.getClosure (CREATE_MENU_PROXY), false);
	if (GTK.GTK4) {
		long keyController = GTK.gtk_event_controller_key_new();
		GTK.gtk_widget_add_controller(eventHandle, keyController);
		GTK.gtk_event_controller_set_propagation_phase(keyController, GTK.GTK_PHASE_TARGET);

		long focusAddress = display.focusCallback.getAddress();
		OS.g_signal_connect (keyController, OS.focus_in, focusAddress, FOCUS_IN);
		OS.g_signal_connect (keyController, OS.focus_out, focusAddress, FOCUS_OUT);

		long motionController = GTK.gtk_event_controller_motion_new();
		GTK.gtk_widget_add_controller(eventHandle, motionController);
		GTK.gtk_event_controller_set_propagation_phase(motionController, GTK.GTK_PHASE_TARGET);

		long enterAddress = display.enterMotionScrollCallback.getAddress();
		long leaveAddress = display.leaveCallback.getAddress();
		OS.g_signal_connect (motionController, OS.enter, enterAddress, ENTER);
		OS.g_signal_connect (motionController, OS.leave, leaveAddress, LEAVE);
	} else {
		OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [FOCUS_IN_EVENT], 0, display.getClosure (FOCUS_IN_EVENT), false);
		OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [FOCUS_OUT_EVENT], 0, display.getClosure (FOCUS_OUT_EVENT), false);

		OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [ENTER_NOTIFY_EVENT], 0, display.getClosure (ENTER_NOTIFY_EVENT), false);
		OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [LEAVE_NOTIFY_EVENT], 0, display.getClosure (LEAVE_NOTIFY_EVENT), false);
	}
	/*
	* Feature in GTK.  Usually, GTK widgets propagate all events to their
	* parent when they are done their own processing.  However, in contrast
	* to other widgets, the buttons that make up the tool items, do not propagate
	* the mouse up/down events. It is interesting to note that they DO propagate
	* mouse motion events.  The fix is to explicitly forward mouse up/down events
	* to the parent.
	*/
	int mask =
		GDK.GDK_EXPOSURE_MASK | GDK.GDK_POINTER_MOTION_MASK |
		GDK.GDK_BUTTON_PRESS_MASK | GDK.GDK_BUTTON_RELEASE_MASK |
		GDK.GDK_ENTER_NOTIFY_MASK | GDK.GDK_LEAVE_NOTIFY_MASK |
		GDK.GDK_KEY_PRESS_MASK | GDK.GDK_KEY_RELEASE_MASK |
		GDK.GDK_FOCUS_CHANGE_MASK;
	GTK.gtk_widget_add_events (eventHandle, mask);
	if (GTK.GTK4) {
		OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [EVENT], 0, display.getClosure (EVENT), false);
	} else {
		OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [BUTTON_PRESS_EVENT], 0, display.getClosure (BUTTON_PRESS_EVENT), false);
		OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [BUTTON_RELEASE_EVENT], 0, display.getClosure (BUTTON_RELEASE_EVENT), false);
	}
	if (GTK.GTK4) {
		OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [EVENT], 0, display.getClosure (EVENT), false);
	} else {
		OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [EVENT_AFTER], 0, display.getClosure (EVENT_AFTER), false);
	}

	long topHandle = topHandle ();
	OS.g_signal_connect_closure_by_id (topHandle, display.signalIds [MAP], 0, display.getClosure (MAP), true);
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
	return getEnabled () && parent.isEnabled ();
}

boolean isTabGroup () {
	ToolItem [] tabList = parent._getTabItemList ();
	if (tabList != null) {
		for (int i=0; i<tabList.length; i++) {
			if (tabList [i] == this) return true;
		}
	}
	if ((style & SWT.SEPARATOR) != 0) return true;
	int index = parent.indexOf (this);
	if (index == 0) return true;
	ToolItem previous = parent.getItem (index - 1);
	return (previous.getStyle () & SWT.SEPARATOR) != 0;
}

@Override
void register () {
	super.register ();
	if (eventHandle != 0) display.addWidget (eventHandle, this);
	if (arrowHandle != 0) display.addWidget (arrowHandle, this);
}

@Override
void releaseHandle () {
	super.releaseHandle ();
	arrowHandle = labelHandle = imageHandle = eventHandle = 0;
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	if (parent.currentFocusItem == this) parent.currentFocusItem = null;
	parent = null;
	control = null;
	hotImage = disabledImage = null;
	toolTipText = null;
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

void resizeControl () {
	if (control != null && !control.isDisposed ()) {
		/*
		* Set the size and location of the control
		* separately to minimize flashing in the
		* case where the control does not resize
		* to the size that was requested.  This
		* case can occur when the control is a
		* combo box.
		*/
		Rectangle itemRect = getBounds ();
		control.setSize (itemRect.width, itemRect.height);
		resizeHandle(itemRect.width, itemRect.height);
		Rectangle rect = control.getBounds ();
		rect.x = itemRect.x + (itemRect.width - rect.width) / 2;
		rect.y = itemRect.y + (itemRect.height - rect.height) / 2;
		control.setLocation (rect.x, rect.y);
	}
}

void resizeHandle(int width, int height) {
	GTK.gtk_widget_set_size_request (handle, width, height);
	/*
	* Cause a size allocation this widget's topHandle.  Note that
	* all calls to gtk_widget_size_allocate() must be preceded by
	* a call to gtk_widget_size_request().
	*/
	GtkRequisition requisition = new GtkRequisition ();
	parent.gtk_widget_get_preferred_size (handle, requisition);
	GtkAllocation allocation = new GtkAllocation ();
	GTK.gtk_widget_get_allocation (handle, allocation);
	allocation.width = width;
	allocation.height = height;
	GTK.gtk_widget_size_allocate (handle, allocation);
}

void selectRadio () {
	int index = 0;
	ToolItem [] items = parent.getItems ();
	while (index < items.length && items [index] != this) index++;
	int i = index - 1;
	while (i >= 0 && items [i].setRadioSelection (false)) --i;
	int j = index + 1;
	while (j < items.length && items [j].setRadioSelection (false)) j++;
	setSelection (true);
}

/**
 * Sets the control that is used to fill the bounds of
 * the item when the item is a <code>SEPARATOR</code>.
 *
 * @param control the new control
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li>
 *    <li>ERROR_INVALID_PARENT - if the control is not in the same widget tree</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setControl (Control control) {
	checkWidget ();
	if (control != null) {
		if (control.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (control.parent != parent) error (SWT.ERROR_INVALID_PARENT);
	}
	if ((style & SWT.SEPARATOR) == 0) return;
	if (this.control == control) return;
	this.control = control;
	parent.relayout ();
}

/**
 * Sets the receiver's disabled image to the argument, which may be
 * null indicating that no disabled image should be displayed.
 * <p>
 * The disabled image is displayed when the receiver is disabled.
 * </p>
 *
 * @param image the disabled image to display on the receiver (may be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setDisabledImage (Image image) {
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) return;
	disabledImage = image;
}

/**
 * Enables the receiver if the argument is <code>true</code>,
 * and disables it otherwise.
 * <p>
 * A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 * </p>
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
	long topHandle = topHandle ();
	if (GTK.gtk_widget_get_sensitive (topHandle) == enabled) return;
	GTK.gtk_widget_set_sensitive (topHandle, enabled);
}

boolean setFocus () {
	return GTK.gtk_widget_child_focus (handle, GTK.GTK_DIR_TAB_FORWARD);
}

void setFontDescription (long font) {
	if (labelHandle != 0) setFontDescription (labelHandle, font);
}

void setForegroundRGBA (GdkRGBA rgba) {
	if (labelHandle != 0) setForegroundRGBA (labelHandle, rgba);
}

void setBackgroundRGBA (GdkRGBA rgba) {
	if (handle != 0) setBackgroundRGBA (handle, rgba);
}

void setBackgroundRGBA (long handle, GdkRGBA rgba) {
	if (GTK.GTK_VERSION >= OS.VERSION(3, 14, 0)) {
		// Form background string
		long context = GTK.gtk_widget_get_style_context(handle);
		String name = GTK.GTK_VERSION >= OS.VERSION(3,	 20, 0) ? display.gtk_widget_class_get_css_name(handle)
        		: display.gtk_widget_get_name(handle);
		String css = name + " {background-color: " + display.gtk_rgba_to_css_string(rgba) + "}";


		// Apply background color and any foreground color
		gtk_css_provider_load_from_css(context, css);
	}
}

void setForegroundRGBA (long handle, GdkRGBA rgba) {
	if (GTK.GTK_VERSION < OS.VERSION(3, 14, 0)) {
		GdkRGBA selectedForeground = display.COLOR_LIST_SELECTION_TEXT_RGBA;
		GTK.gtk_widget_override_color (handle, GTK.GTK_STATE_FLAG_NORMAL, rgba);
		GTK.gtk_widget_override_color (handle, GTK.GTK_STATE_FLAG_SELECTED, selectedForeground);
		long context = GTK.gtk_widget_get_style_context (handle);
		GTK.gtk_style_context_invalidate (context);
		return;
	}
	GdkRGBA toSet = new GdkRGBA();
	if (rgba != null) {
		toSet = rgba;
	} else {
		toSet = display.COLOR_WIDGET_FOREGROUND_RGBA;
	}
	long context = GTK.gtk_widget_get_style_context (handle);
	// Form foreground string
	String color = display.gtk_rgba_to_css_string(toSet);
	String css = "* {color: " + color + ";}";

	// Cache and apply foreground color
	parent.cssForeground = css;
	gtk_css_provider_load_from_css(context, css);

	// We need to set the parent ToolBar's background
	// otherwise setting the foreground will wipe it out.
	parent.restoreBackground();
}

/**
 * Sets the receiver's hot image to the argument, which may be
 * null indicating that no hot image should be displayed.
 * <p>
 * The hot image is displayed when the mouse enters the receiver.
 * </p>
 *
 * @param image the hot image to display on the receiver (may be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setHotImage (Image image) {
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) return;
	hotImage = image;
	if (image != null) {
		ImageList imageList = parent.imageList;
		if (imageList == null) imageList = parent.imageList = new ImageList ();
		int imageIndex = imageList.indexOf (image);
		if (imageIndex == -1) {
			imageIndex = imageList.add (image);
		} else {
			imageList.put (imageIndex, image);
		}
	}
}

@Override
public void setImage (Image image) {
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) return;
	super.setImage (image);
	if (image != null) {
		ImageList imageList = parent.imageList;
		if (imageList == null) imageList = parent.imageList = new ImageList ();
		int imageIndex = imageList.indexOf (image);
		if (imageIndex == -1) {
			imageIndex = imageList.add (image);
		} else {
			imageList.put (imageIndex, image);
		}
		long pixbuf = imageList.getPixbuf (imageIndex);
		gtk_image_set_from_gicon(imageHandle, pixbuf);
	} else {
		gtk_image_set_from_gicon(imageHandle, 0);
	}
	/*
	* If Text/Image of a tool-item changes, then it is
	* required to reset the proxy menu. Otherwise, the
	* old menuItem appears in the overflow menu.
	*/
	if ((style & SWT.DROP_DOWN) != 0) {
		proxyMenuItem = 0;
		proxyMenuItem = GTK.gtk_tool_item_retrieve_proxy_menu_item (handle);
		OS.g_signal_connect(proxyMenuItem, OS.activate, ToolBar.menuItemSelectedFunc.getAddress(), handle);
	}
	parent.relayout ();
}

@Override
void setOrientation (boolean create) {
	if ((parent.style & SWT.RIGHT_TO_LEFT) != 0 || !create) {
		int dir = (parent.style & SWT.RIGHT_TO_LEFT) != 0 ? GTK.GTK_TEXT_DIR_RTL : GTK.GTK_TEXT_DIR_LTR;
		if (handle != 0) GTK.gtk_widget_set_direction (handle, dir);
	}
}

boolean setRadioSelection (boolean value) {
	if ((style & SWT.RADIO) == 0) return false;
	if (getSelection () != value) {
		setSelection (value);
		sendSelectionEvent (SWT.Selection);
	}
	return true;
}

/**
 * Sets the selection state of the receiver.
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked (which some platforms draw as a
 * pushed in button).
 * </p>
 *
 * @param selected the new selection state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (boolean selected) {
	checkWidget ();
	if ((style & (SWT.CHECK | SWT.RADIO)) == 0) return;
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CLICKED);
	GTK.gtk_toggle_tool_button_set_active (handle, selected);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CLICKED);
}

@Override
boolean setTabItemFocus (boolean next) {
	return setFocus ();
}

/**
 * Sets the receiver's text. The string may include
 * the mnemonic character.
 * <p>
 * Mnemonics are indicated by an '&amp;' that causes the next
 * character to be the mnemonic.  When the user presses a
 * key sequence that matches the mnemonic, a selection
 * event occurs. On most platforms, the mnemonic appears
 * underlined but may be emphasised in a platform specific
 * manner.  The mnemonic indicator character '&amp;' can be
 * escaped by doubling it in the string, causing a single
 * '&amp;' to be displayed.
 * </p><p>
 * Note: If control characters like '\n', '\t' etc. are used
 * in the string, then the behavior is platform dependent.
 * </p>
 *
 * @param string the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the text is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	if (string.equals(this.text)) return;
	super.setText (string);
	if (labelHandle == 0) return;
	char [] chars = fixMnemonic (string);
	byte [] buffer = Converter.wcsToMbcs (chars, true);
	GTK.gtk_label_set_text_with_mnemonic (labelHandle, buffer);
	/*
	* If Text/Image of a tool-item changes, then it is
	* required to reset the proxy menu. Otherwise, the
	* old menuItem appears in the overflow menu.
	*/
	if ((style & SWT.DROP_DOWN) != 0) {
		proxyMenuItem = 0;
		proxyMenuItem = GTK.gtk_tool_item_retrieve_proxy_menu_item (handle);
		OS.g_signal_connect(proxyMenuItem, OS.activate, ToolBar.menuItemSelectedFunc.getAddress(), handle);
	}
	parent.relayout ();
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
 * <p>
 * NOTE: This operation is a hint and behavior is platform specific, on Windows
 * for CJK-style mnemonics of the form " (&amp;C)" at the end of the tooltip text
 * are not shown in tooltip.
 * </p>
 *
 * @param string the new tool tip text (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setToolTipText (String string) {
	checkWidget();
	if (toolTipText == string || (toolTipText != null && toolTipText.equals(string))) return;
	if (parent.toolTipText == null) {
		Shell shell = parent._getShell ();
		setToolTipText (shell, string);
	}
	toolTipText = string;
	/*
	* Since tooltip text of a tool-item is used in overflow
	* menu when images are not shown, it is required to
	* reset the proxy menu when the tooltip text changes.
	* Otherwise, the old menuItem appears in the overflow
	* menu as a blank item.
	*/
	if ((style & SWT.DROP_DOWN) != 0) {
		proxyMenuItem = 0;
		proxyMenuItem = GTK.gtk_tool_item_retrieve_proxy_menu_item (handle);
		OS.g_signal_connect(proxyMenuItem, OS.activate, ToolBar.menuItemSelectedFunc.getAddress(), handle);
	}
}

void setToolTipText (Shell shell, String newString) {
	long child = GTK.gtk_bin_get_child (handle);
	if ((style & SWT.DROP_DOWN) != 0) {
		long list = GTK.gtk_container_get_children (child);
		child = OS.g_list_nth_data (list, 0);
		if (arrowHandle != 0) shell.setToolTipText (arrowHandle, newString);
	}
	shell.setToolTipText (child != 0 ? child : handle, newString);
}

/**
 * Sets the width of the receiver, for <code>SEPARATOR</code> ToolItems.
 *
 * @param width the new width. If the new value is <code>SWT.DEFAULT</code>,
 * the width is a fixed-width area whose amount is determined by the platform.
 * If the new value is 0 a vertical or horizontal line will be drawn, depending
 * on the setting of the corresponding style bit (<code>SWT.VERTICAL</code> or
 * <code>SWT.HORIZONTAL</code>). If the new value is <code>SWT.SEPARATOR_FILL</code>
 * a variable-width space is inserted that acts as a spring between the two adjoining
 * items which will push them out to the extent of the containing ToolBar.
 *
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setWidth (int width) {
	checkWidget ();
	setWidthInPixels(DPIUtil.autoScaleUp(width));
}

void setWidthInPixels (int width) {
	checkWidget();
	if ((style & SWT.SEPARATOR) == 0) return;
	if (width < 0) return;
	resizeHandle(width, (parent.style & SWT.VERTICAL) != 0 ? 6 : 15);
	parent.relayout ();
}

void showWidget (int index) {
	if (handle != 0) GTK.gtk_widget_show (handle);
	if (labelHandle != 0) GTK.gtk_widget_show (labelHandle);
	if (imageHandle != 0) GTK.gtk_widget_show (imageHandle);
	GTK.gtk_toolbar_insert(parent.handle, handle, index);
}
}

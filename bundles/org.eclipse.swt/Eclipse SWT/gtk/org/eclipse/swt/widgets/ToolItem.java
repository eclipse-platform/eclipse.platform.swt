/*******************************************************************************
 * Copyright (c) 2000, 2020 IBM Corporation and others.
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
 *     Christoph Läubrich - Bug 569750 - Terminate button doesn't change state for process that is terminated at breakpoint
 *******************************************************************************/
package org.eclipse.swt.widgets;


import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;

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

	/** GTK4 only field, used to keep track of the containing box of the image & label */
	long boxHandle, groupHandle;

	ToolBar parent;
	Control control;
	Image hotImage, disabledImage, defaultDisableImage;
	Color background, foreground;
	String toolTipText;
	boolean drawHotImage;
	/** True iff map has been hooked for this ToolItem. See bug 546914. */
	boolean mapHooked;
	boolean enabled = true;

	/**
	 * The image that is currently used by the tool item.
	 * Either the image set by client code via {@link #setImage(Image)}
	 * or {@link #setDisabledImage(Image)}, depending on button state.
	 * Or if the button is disabled but no disabled image is specified,
	 * a grayed out version of the "normal" image.
	 */
	Image currentImage;

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

	switch (style & bits) {
		case SWT.SEPARATOR:
			if (GTK.GTK4) {
				handle = GTK.gtk_separator_new(GTK.GTK_ORIENTATION_VERTICAL);
				if (handle == 0) error(SWT.ERROR_NO_HANDLES);
			} else {
				handle = GTK3.gtk_separator_tool_item_new ();
				if (handle == 0) error(SWT.ERROR_NO_HANDLES);
				GTK3.gtk_separator_tool_item_set_draw(handle, true);
			}
			break;
		case SWT.DROP_DOWN:
			if (GTK.GTK4) {
				handle = GTK.gtk_box_new(GTK.GTK_ORIENTATION_HORIZONTAL, 0);
				if (handle == 0) error(SWT.ERROR_NO_HANDLES);

				boxHandle = GTK.gtk_box_new(GTK.GTK_ORIENTATION_VERTICAL, 0);
				if (boxHandle == 0) error(SWT.ERROR_NO_HANDLES);

				long button = GTK.gtk_button_new();
				if (button == 0) error(SWT.ERROR_NO_HANDLES);
				GTK4.gtk_button_set_child(button, boxHandle);

				long menuButton = GTK.gtk_menu_button_new();
				if (menuButton == 0) error(SWT.ERROR_NO_HANDLES);

				GTK4.gtk_box_append(handle, button);
				GTK4.gtk_box_append(handle, menuButton);

				arrowHandle = GTK4.gtk_widget_get_first_child(menuButton);
				GTK4.gtk_menu_button_set_use_underline(menuButton, true);
			} else {
				handle = GTK3.gtk_menu_tool_button_new(0, null);
				if (handle == 0) error(SWT.ERROR_NO_HANDLES);

				long child = GTK3.gtk_bin_get_child(handle);
				long list = GTK3.gtk_container_get_children(child);
				arrowHandle = OS.g_list_nth_data(list, 1);
				OS.g_list_free(list);
			}

			/*
			 * Feature in GTK. The arrow button of DropDown tool-item is
			 * disabled when it does not contain menu. The fix is to
			 * find the arrow button handle and enable it.
			 */
			if (arrowHandle != 0) GTK.gtk_widget_set_sensitive (arrowHandle, true);
			break;
		case SWT.RADIO:
			/*
			* Because GTK enforces radio behavior in a button group
			* a radio group is not created for each set of contiguous
			* buttons, each radio button will not draw unpressed.
			* The fix is to use toggle buttons instead.
			*/
		case SWT.CHECK:
			if (GTK.GTK4) {
				handle = GTK.gtk_toggle_button_new();
				if (handle == 0) error(SWT.ERROR_NO_HANDLES);
				boxHandle = GTK.gtk_box_new(GTK.GTK_ORIENTATION_VERTICAL, 0);
				if (boxHandle == 0) error(SWT.ERROR_NO_HANDLES);

				GTK4.gtk_button_set_child(handle, boxHandle);
				GTK.gtk_button_set_use_underline(handle, true);
			} else {
				handle = GTK3.gtk_toggle_tool_button_new();
				if (handle == 0) error(SWT.ERROR_NO_HANDLES);
			}
			break;
		case SWT.PUSH:
		default:
			if (GTK.GTK4) {
				handle = GTK.gtk_button_new();
				if (handle == 0) error(SWT.ERROR_NO_HANDLES);
				boxHandle = GTK.gtk_box_new(GTK.GTK_ORIENTATION_VERTICAL, 0);
				if (boxHandle == 0) error(SWT.ERROR_NO_HANDLES);

				GTK4.gtk_button_set_child(handle, boxHandle);

				GTK.gtk_button_set_use_underline(handle, true);
			} else {
				handle = GTK3.gtk_tool_button_new (0, null);
				if (handle == 0) error (SWT.ERROR_NO_HANDLES);
			}

			break;
	}

	if ((style & SWT.SEPARATOR) == 0) {
		if (GTK.GTK4) {
			labelHandle = GTK.gtk_label_new_with_mnemonic(null);
			if (labelHandle == 0) error(SWT.ERROR_NO_HANDLES);
			imageHandle = GTK.gtk_image_new();
			if (imageHandle == 0) error(SWT.ERROR_NO_HANDLES);

			GTK.gtk_widget_set_valign(boxHandle, GTK.GTK_ALIGN_CENTER);

			GTK4.gtk_box_append(boxHandle, imageHandle);
			GTK4.gtk_box_append(boxHandle, labelHandle);

			GTK.gtk_widget_hide(imageHandle);
			GTK.gtk_widget_hide(labelHandle);
		} else {
			labelHandle = GTK.gtk_label_new_with_mnemonic(null);
			if (labelHandle == 0) error(SWT.ERROR_NO_HANDLES);
			imageHandle = GTK3.gtk_image_new_from_surface(0);
			if (imageHandle == 0) error(SWT.ERROR_NO_HANDLES);

			GTK3.gtk_tool_button_set_icon_widget(handle, imageHandle);
			GTK3.gtk_tool_button_set_label_widget(handle, labelHandle);

			GTK3.gtk_tool_button_set_use_underline (handle, true);
		}
	}

	if ((parent.state & FONT) != 0) {
		long fontDesc = parent.getFontDescription ();
		setFontDescription (fontDesc);
		OS.pango_font_description_free (fontDesc);
	}

	/*
	 * Set the "homogeneous" property to false, otherwise all ToolItems will be as large as
	 * the largest one in the ToolBar. See bug 548331, 395296 for more information.
	 */
	if (GTK.GTK4) {
		GTK.gtk_box_set_homogeneous(parent.handle, false);
	} else {
		GTK3.gtk_tool_item_set_homogeneous(handle, false);
	}
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
 * Returns the receiver's background color.
 *
 * @return the background color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.120
 */
public Color getBackground () {
	checkWidget ();
	return (background != null) ? background : parent.getBackground ();
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
 * Returns the foreground color that the receiver will use to draw.
 *
 * @return the receiver's foreground color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.120
 */
public Color getForeground () {
	checkWidget ();
	return (foreground != null) ? foreground : parent.getForeground ();
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
 * Returns the receiver's enabled image if it has one, or null
 * if it does not.
 *
 * @return the receiver's enabled image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public Image getImage () {
	return this.image;
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

	boolean selection;
	if (GTK.GTK4) {
		selection = GTK.gtk_toggle_button_get_active(handle);
	} else {
		selection = GTK3.gtk_toggle_tool_button_get_active(handle);
	}

	return selection;
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
		long eventPtr = GTK3.gtk_get_current_event ();
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
	if (GTK.GTK4) {
		/* TODO: GTK4 have to implement our own overflow menu */
		return 0;
	}

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
		GTK3.gtk_tool_item_set_proxy_menu_item (widget, buffer, proxyMenuItem);
		return 1;
	}

	Image image = currentImage;
	if (image != null) {
		ImageList imageList = parent.imageList;
		if (imageList != null) {
			int index = imageList.indexOf (image);
			if (index != -1) {
				long surface = imageList.getSurface (index);
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
				long menuItem = GTK3.gtk_menu_item_new ();
				if (menuItem == 0) error (SWT.ERROR_NO_HANDLES);

				long boxHandle = gtk_box_new (GTK.GTK_ORIENTATION_HORIZONTAL, false, 6);
				if (boxHandle == 0) error (SWT.ERROR_NO_HANDLES);

				long menuLabel = GTK3.gtk_accel_label_new (label);
				if (menuLabel == 0) error (SWT.ERROR_NO_HANDLES);
				GTK.gtk_label_set_xalign (labelHandle, 0);
				GTK.gtk_widget_set_halign (labelHandle, GTK.GTK_ALIGN_FILL);

				long menuImage = GTK3.gtk_image_new_from_surface(surface);
				if (menuImage == 0) error (SWT.ERROR_NO_HANDLES);

				GTK3.gtk_container_add (boxHandle, menuImage);
				gtk_box_pack_end (boxHandle, menuLabel, true, true, 0);
				GTK3.gtk_container_add (menuItem, boxHandle);
				GTK3.gtk_tool_item_set_proxy_menu_item (widget, buffer, menuItem);

				/*
				 * Since the arrow button does not appear in the drop_down
				 * item, we request the menu-item and then, hook the
				 * activate signal to send the Arrow selection signal.
				 */
				proxyMenuItem = GTK3.gtk_tool_item_get_proxy_menu_item (widget, buffer);
				OS.g_signal_connect(menuItem, OS.activate, ToolBar.menuItemSelectedFunc.getAddress(), handle);
				return 1;
			}
		}
	}
	return 0;
}

@Override
long gtk_enter_notify_event (long widget, long event) {
	parent.gtk_enter_notify_event (widget, event);
	drawHotImage = (parent.style & SWT.FLAT) != 0 && hotImage != null;
	if (drawHotImage) {
		ImageList imageList = parent.imageList;
		if (imageList != null) {
			int index = imageList.indexOf(hotImage);
			if (index != -1 && imageHandle != 0) {
				if (GTK.GTK4) {
					long pixbuf = ImageList.createPixbuf(hotImage);
					long texture = GDK.gdk_texture_new_for_pixbuf(pixbuf);
					OS.g_object_unref(pixbuf);
					GTK4.gtk_image_set_from_paintable(imageHandle, texture);
				} else {
					GTK3.gtk_image_set_from_surface(imageHandle, imageList.getSurface(index));
				}
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
			if (GTK.GTK4) {
				eventButton[0] = GDK.gdk_button_event_get_button(gdkEvent);
			} else {
				GDK.gdk_event_get_button(gdkEvent, eventButton);
			}

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
	parent.sendFocusEvent(SWT.FocusIn);
	return 0;
}

@Override
long gtk_focus_out_event (long widget, long event) {
	parent.hasChildFocus = false;
	parent.sendFocusEvent(SWT.FocusOut);
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
				int index = imageList.indexOf(image);
				if (index != -1 && imageHandle != 0) {
					if (GTK.GTK4) {
						long pixbuf = ImageList.createPixbuf(image);
						long texture = GDK.gdk_texture_new_for_pixbuf(pixbuf);
						OS.g_object_unref(pixbuf);
						GTK4.gtk_image_set_from_paintable(imageHandle, texture);
					} else {
						GTK3.gtk_image_set_from_surface(imageHandle, imageList.getSurface(index));
					}
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

	if (GTK.GTK4) {
		if ((style & SWT.DROP_DOWN) != 0) {
			if (arrowHandle != 0) {
				long clickGesture = GTK4.gtk_gesture_click_new();
				OS.g_signal_connect(clickGesture, OS.pressed, display.gesturePressReleaseProc, GESTURE_PRESSED);
				GTK4.gtk_widget_add_controller(arrowHandle, clickGesture);
			}

			/*
			 * SWT.DROP_DOWN's handle is of the top level GtkBox which holds the main GtkButton & arrow GtkMenuButton.
			 * Therefore, we connect to the first child of the GtkBox, which is the main GtkButton
			 */
			OS.g_signal_connect_closure(GTK4.gtk_widget_get_first_child(handle), OS.clicked, display.getClosure(CLICKED), false);
		} else {
			OS.g_signal_connect_closure(handle, OS.clicked, display.getClosure (CLICKED), false);
		}

		long focusController = GTK4.gtk_event_controller_focus_new();
		GTK4.gtk_widget_add_controller(handle, focusController);
		GTK.gtk_event_controller_set_propagation_phase(focusController, GTK.GTK_PHASE_TARGET);

		OS.g_signal_connect(focusController, OS.enter, display.focusProc, FOCUS_IN);
		OS.g_signal_connect(focusController, OS.leave, display.focusProc, FOCUS_OUT);

		long motionController = GTK4.gtk_event_controller_motion_new();
		GTK4.gtk_widget_add_controller(handle, motionController);
		GTK.gtk_event_controller_set_propagation_phase(motionController, GTK.GTK_PHASE_TARGET);

		OS.g_signal_connect(motionController, OS.enter, display.enterMotionProc, ENTER);
		OS.g_signal_connect(motionController, OS.leave, display.leaveProc, LEAVE);

		//TODO: event-after
		long clickController = GTK4.gtk_gesture_click_new();
		GTK4.gtk_widget_add_controller(handle, clickController);
		OS.g_signal_connect(clickController, OS.pressed, display.gesturePressReleaseProc, GESTURE_PRESSED);
	} else {
		/*
		 * Feature in GTK. GtkToolItem does not respond to basic listeners
		 * such as button-press, enter-notify to it. The fix is to assign
		 * the listener to child (GtkButton) of the tool-item.
		 */
		eventHandle = GTK3.gtk_bin_get_child(handle);
		if ((style & SWT.DROP_DOWN) != 0) {
			long list = GTK3.gtk_container_get_children(eventHandle);
			eventHandle = OS.g_list_nth_data(list, 0);
			OS.g_list_free(list);
			if (arrowHandle != 0) OS.g_signal_connect_closure (arrowHandle, OS.clicked, display.getClosure (CLICKED), false);
		}

		OS.g_signal_connect_closure (handle, OS.clicked, display.getClosure (CLICKED), false);
		OS.g_signal_connect_closure (handle, OS.create_menu_proxy, display.getClosure (CREATE_MENU_PROXY), false);

		OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [FOCUS_IN_EVENT], 0, display.getClosure (FOCUS_IN_EVENT), false);
		OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [FOCUS_OUT_EVENT], 0, display.getClosure (FOCUS_OUT_EVENT), false);

		OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [ENTER_NOTIFY_EVENT], 0, display.getClosure (ENTER_NOTIFY_EVENT), false);
		OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [LEAVE_NOTIFY_EVENT], 0, display.getClosure (LEAVE_NOTIFY_EVENT), false);

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
		GTK3.gtk_widget_add_events (eventHandle, mask);

		OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [BUTTON_PRESS_EVENT], 0, display.getClosure (BUTTON_PRESS_EVENT), false);
		OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [BUTTON_RELEASE_EVENT], 0, display.getClosure (BUTTON_RELEASE_EVENT), false);
		OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [EVENT_AFTER], 0, display.getClosure (EVENT_AFTER), false);
	}

	if (!mapHooked) {
		long topHandle = topHandle ();
		OS.g_signal_connect_closure_by_id (topHandle, display.signalIds [MAP], 0, display.getClosure (MAP), true);
		mapHooked = true;
	}
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

	disposeDefault();
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
	gtk_widget_size_allocate(handle, allocation, -1);
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
 * Sets the receiver's background color to the color specified
 * by the argument, or to the default system color for the item
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
 * @since 3.120
 */
public void setBackground (Color color) {
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		error (SWT.ERROR_INVALID_ARGUMENT);
	}
	Color oldColor = background;
	background = color;
	if (Objects.equals (oldColor, background)) return;
	updateStyle ();
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
	// Fix the Z-order in order to ensure proper event traversal. See bug 546914.
	if (control != null) {
		parent.fixZOrder();
		if (!mapHooked) {
			OS.g_signal_connect_closure_by_id (topHandle(), display.signalIds [MAP], 0, display.getClosure (MAP), true);
			mapHooked = true;
		}
	}
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
	if (this.disabledImage == image) return;
	if ((style & SWT.SEPARATOR) != 0) return;
	disabledImage = image;
	if (image != null) {
		if (!enabled) {
			_setImage(image);
		}
		disposeDefault();
	}
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
	long topHandle = topHandle();
	if (this.enabled == enabled) return;
	this.enabled = enabled;

	GTK.gtk_widget_set_sensitive(topHandle, enabled);
	_setEnabledOrDisabledImage();

}

private void _setEnabledOrDisabledImage() {
	if (!enabled) {
		if (disabledImage == null) {
			if (defaultDisableImage == null && image != null) {
				defaultDisableImage = new Image(getDisplay(), image, SWT.IMAGE_DISABLE);
			}
			_setImage(defaultDisableImage);
		} else {
			_setImage(disabledImage);
		}
	}
	if (enabled && image != null) _setImage(image);
}

boolean setFocus () {
	return GTK.gtk_widget_child_focus (handle, GTK.GTK_DIR_TAB_FORWARD);
}

void setFontDescription (long font) {
	if (labelHandle != 0) setFontDescription (labelHandle, font);
}

/**
 * Sets the receiver's foreground color to the color specified
 * by the argument, or to the default system color for the item
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
 * @since 3.120
 */
public void setForeground (Color color) {
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		error (SWT.ERROR_INVALID_ARGUMENT);
	}
	Color oldColor = foreground;
	foreground = color;
	if (Objects.equals (oldColor, foreground)) return;
	updateStyle ();
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
	if (this.hotImage == image) return;
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
	if (this.image == image) return;
	if ((style & SWT.SEPARATOR) != 0) return;
	super.setImage (image);
	disposeDefault();
	if (!enabled && disabledImage != image && disabledImage != null) {
		return;
	}
	_setEnabledOrDisabledImage();
}

private void disposeDefault() {
	if (defaultDisableImage != null) {
		defaultDisableImage.dispose();
		defaultDisableImage = null;
	}
}

void _setImage (Image image) {
	if ((style & SWT.SEPARATOR) != 0) return;
	currentImage = image;
	if (image != null) {
		ImageList imageList = parent.imageList;
		if (imageList == null) imageList = parent.imageList = new ImageList ();
		int imageIndex = imageList.indexOf (image);
		if (imageIndex == -1) {
			imageIndex = imageList.add (image);
		} else {
			imageList.put (imageIndex, image);
		}

		if (GTK.GTK4) {
			GTK.gtk_widget_show(imageHandle);
			long pixbuf = ImageList.createPixbuf(image);
			long texture = GDK.gdk_texture_new_for_pixbuf(pixbuf);
			OS.g_object_unref(pixbuf);
			GTK4.gtk_image_set_from_paintable(imageHandle, texture);
		} else {
			GTK3.gtk_image_set_from_surface(imageHandle, imageList.getSurface(imageIndex));
		}
	} else {
		if(GTK.GTK4) {
			GTK4.gtk_image_clear(imageHandle);
			GTK.gtk_widget_hide(imageHandle);
		} else {
			GTK3.gtk_image_set_from_surface(imageHandle, 0);
		}
	}
	/*
	* If Text/Image of a tool-item changes, then it is
	* required to reset the proxy menu. Otherwise, the
	* old menuItem appears in the overflow menu.
	*/
	recreateMenuProxy();
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
	if (GTK.GTK4) {
		GTK.gtk_toggle_button_set_active (handle, selected);
	} else {
		GTK3.gtk_toggle_tool_button_set_active (handle, selected);
	}
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

	if (GTK.GTK4) {
		GTK.gtk_widget_show(labelHandle);
		GTK.gtk_label_set_text_with_mnemonic(labelHandle, buffer);
	} else {
		GTK.gtk_label_set_text_with_mnemonic(labelHandle, buffer);
	}

	/*
	 * Only set important if this ToolItem actually has text.
	 * See bug 543895.
	 */
	if ((parent.style & SWT.RIGHT) != 0) {
		if (!GTK.GTK4) GTK3.gtk_tool_item_set_is_important (handle, !string.isEmpty());
	}
	/*
	* If Text/Image of a tool-item changes, then it is
	* required to reset the proxy menu. Otherwise, the
	* old menuItem appears in the overflow menu.
	*/
	recreateMenuProxy();
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
public void setToolTipText(String string) {
	checkWidget();
	if (toolTipText == string || (toolTipText != null && toolTipText.equals(string))) return;

	if (parent.toolTipText == null) {
		if (GTK.GTK4) {
			setToolTipText(handle, string);
		} else {
			long child = GTK3.gtk_bin_get_child(handle);
			if ((style & SWT.DROP_DOWN) != 0) {
				long list = GTK3.gtk_container_get_children(child);
				child = OS.g_list_nth_data(list, 0);
				OS.g_list_free(list);
				if (arrowHandle != 0) setToolTipText(arrowHandle, string);
			}
			setToolTipText(child != 0 ? child : handle, string);
		}
	}
	toolTipText = string;
	/*
	* Since tooltip text of a tool-item is used in overflow
	* menu when images are not shown, it is required to
	* reset the proxy menu when the tooltip text changes.
	* Otherwise, the old menuItem appears in the overflow
	* menu as a blank item.
	*/
	recreateMenuProxy();
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
	if (GTK.GTK4) {
		if (index == 0) {
			GTK4.gtk_box_prepend(parent.handle, handle);
		} else if (index < 0) {
			GTK4.gtk_box_append(parent.handle, handle);
		} else {
			for (long sibling = GTK4.gtk_widget_get_first_child(parent.handle); sibling != 0; sibling = GTK4.gtk_widget_get_next_sibling(sibling)) {
				if (index == 1) {
					GTK4.gtk_box_insert_child_after(parent.handle, handle, sibling);
					break;
				}

				index--;
			}
		}
	} else {
		if (handle != 0) GTK.gtk_widget_show (handle);
		if (labelHandle != 0) GTK.gtk_widget_show (labelHandle);
		if (imageHandle != 0) GTK.gtk_widget_show (imageHandle);
		GTK3.gtk_toolbar_insert(parent.handle, handle, index);
	}
}

void updateStyle () {
	if ((style & SWT.SEPARATOR) != 0) return;

	if (provider == 0) {
		provider = GTK.gtk_css_provider_new ();
		if ((style & SWT.DROP_DOWN) != 0) {
			long box = GTK3.gtk_bin_get_child (handle);
			long list = GTK3.gtk_container_get_children (box);
			for (int i = 0; i < 2; i++) {
				long child = OS.g_list_nth_data(list, i);
				long context = GTK.gtk_widget_get_style_context (child);
				GTK.gtk_style_context_add_provider (context, provider, GTK.GTK_STYLE_PROVIDER_PRIORITY_APPLICATION);
			}
			OS.g_list_free(list);
		} else {
			long child = GTK3.gtk_bin_get_child (handle);
			long context = GTK.gtk_widget_get_style_context (child);
			GTK.gtk_style_context_add_provider (context, provider, GTK.GTK_STYLE_PROVIDER_PRIORITY_APPLICATION);
		}
		OS.g_object_unref (provider);
	}

	String css = "";
	if (foreground != null) {
		css += "button { color: " + display.gtk_rgba_to_css_string (foreground.handle) + "; }";
	} else {
		css += parent.cssForeground;
	}
	if (background != null) {
		css += "button { background-image: none; background-color: " + display.gtk_rgba_to_css_string (background.handle) + "; }";
	}

	if (GTK.GTK4) {
		GTK4.gtk_css_provider_load_from_data (provider, Converter.wcsToMbcs (css, true), -1);
	} else {
		GTK3.gtk_css_provider_load_from_data (provider, Converter.wcsToMbcs (css, true), -1, null);
	}
}

@Override
String getNameText() {
	String nameText = super.getNameText();
	Object data = getData();
	if(data != null) {
		return "text: '" + nameText + "', data: " + data;
	}
	return nameText;
}

@Override
long dpiChanged(long object, long arg0) {
	super.dpiChanged(object, arg0);

	if (image != null) {
		image.internal_gtk_refreshImageForZoom();
		setImage(image);
	}

	if (hotImage != null) {
		hotImage.internal_gtk_refreshImageForZoom();
		setHotImage(hotImage);
	}

	if (disabledImage != null) {
		disabledImage.internal_gtk_refreshImageForZoom();
		setDisabledImage(disabledImage);
	}

	return 0;
}

private void recreateMenuProxy() {
	if ((style & SWT.DROP_DOWN) != 0 || proxyMenuItem != 0) {
		if (GTK.GTK4) {
			/* TODO: GTK4 have to implement our own overflow menu */
		} else {
			proxyMenuItem = 0;
			proxyMenuItem = GTK3.gtk_tool_item_retrieve_proxy_menu_item (handle);
		}
	}
}
}
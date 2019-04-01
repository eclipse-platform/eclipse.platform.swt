/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
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
 * Instances of this class represent a selectable user interface object that
 * issues notification when pressed and released.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>ARROW, CHECK, PUSH, RADIO, TOGGLE, FLAT, WRAP</dd>
 * <dd>UP, DOWN, LEFT, RIGHT, CENTER</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles ARROW, CHECK, PUSH, RADIO, and TOGGLE
 * may be specified.
 * </p><p>
 * Note: Only one of the styles LEFT, RIGHT, and CENTER may be specified.
 * </p><p>
 * Note: Only one of the styles UP, DOWN, LEFT, and RIGHT may be specified
 * when the ARROW style is specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#button">Button snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Button extends Control {
	long boxHandle, labelHandle, imageHandle, arrowHandle, groupHandle;
	boolean selected, grayed;
	ImageList imageList;
	Image image;
	String text;
	GdkRGBA background;

	static final int INNER_BORDER = 1;
	static final int DEFAULT_BORDER = 1;

/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
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
 * @see SWT#ARROW
 * @see SWT#CHECK
 * @see SWT#PUSH
 * @see SWT#RADIO
 * @see SWT#TOGGLE
 * @see SWT#FLAT
 * @see SWT#UP
 * @see SWT#DOWN
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#CENTER
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Button (Composite parent, int style) {
	super (parent, checkStyle (style));
}

static int checkStyle (int style) {
	style = checkBits (style, SWT.PUSH, SWT.ARROW, SWT.CHECK, SWT.RADIO, SWT.TOGGLE, 0);
	if ((style & (SWT.PUSH | SWT.TOGGLE)) != 0) {
		return checkBits (style, SWT.CENTER, SWT.LEFT, SWT.RIGHT, 0, 0, 0);
	}
	if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
		return checkBits (style, SWT.LEFT, SWT.RIGHT, SWT.CENTER, 0, 0, 0);
	}
	if ((style & SWT.ARROW) != 0) {
		style |= SWT.NO_FOCUS;
		return checkBits (style, SWT.UP, SWT.DOWN, SWT.LEFT, SWT.RIGHT, 0, 0);
	}
	return style;
}

GtkBorder getBorder (byte[] border, long handle, int defaultBorder) {
    GtkBorder gtkBorder = new GtkBorder();
    long []  borderPtr = new long [1];
    if (GTK.GTK4) {
		long context = GTK.gtk_widget_get_style_context (handle);
		int stateFlag = GTK.gtk_widget_get_state_flags(handle);
		gtk_style_context_get_border(context, stateFlag, gtkBorder);
		return gtkBorder;
    } else {
    	GTK.gtk_widget_style_get (handle, border, borderPtr,0);
    	if (borderPtr[0] != 0) {
            OS.memmove (gtkBorder, borderPtr[0], GtkBorder.sizeof);
            GTK.gtk_border_free(borderPtr[0]);
            return gtkBorder;
        }
    }
    gtkBorder.left = defaultBorder;
    gtkBorder.top = defaultBorder;
    gtkBorder.right = defaultBorder;
    gtkBorder.bottom = defaultBorder;
    return gtkBorder;
}

@Override
GdkRGBA getContextBackgroundGdkRGBA () {
	if (background != null && (state & BACKGROUND) != 0) {
		return background;
	}
	return defaultBackground();
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the control is selected by the user.
 * <code>widgetDefaultSelected</code> is not called.
 * </p>
 * <p>
 * When the <code>SWT.RADIO</code> style bit is set, the <code>widgetSelected</code> method is
 * also called when the receiver loses selection because another item in the same radio group
 * was selected by the user. During <code>widgetSelected</code> the application can use
 * <code>getSelection()</code> to determine the current selected state of the receiver.
 * </p>
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
 * @see SelectionListener
 * @see #removeSelectionListener
 * @see SelectionEvent
 */
public void addSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

@Override
Point computeSizeInPixels (int wHint, int hHint, boolean changed) {
	checkWidget ();
	if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
	if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
	/*
	* Feature in GTK, GtkCheckButton and GtkRadioButton allocate
	* only the minimum size necessary for its child. This causes the child
	* alignment to fail. The fix is to set the child size to the size
	* of the button.
	*/
	forceResize ();
	int [] reqWidth = null, reqHeight = null;
	if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
		reqWidth = new int [1];
		reqHeight = new int [1];
		GTK.gtk_widget_get_size_request (boxHandle, reqWidth, reqHeight);
		GTK.gtk_widget_set_size_request (boxHandle, -1, -1);
	}
	Point size;
	boolean wrap = labelHandle != 0 && (style & SWT.WRAP) != 0 && GTK.gtk_widget_get_visible (labelHandle);
	if (wrap) {
		int borderWidth = gtk_container_get_border_width_or_margin (handle);
		int trimWidth, trimHeight;
		if (!GTK.GTK4) {
			int[] focusWidth = new int[1];
			GTK.gtk_widget_style_get (handle, OS.focus_line_width, focusWidth, 0);
			int[] focusPadding = new int[1];
			GTK.gtk_widget_style_get (handle, OS.focus_padding, focusPadding, 0);
			trimWidth = 2 * (borderWidth + focusWidth [0] + focusPadding [0]);
		} else {
			trimWidth = 2 * borderWidth;
		}
		trimHeight = trimWidth;
		int indicatorHeight = 0;
		if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
			if (GTK.GTK4) {
				long icon = GTK.gtk_widget_get_first_child(handle);
				GtkRequisition minimum = new GtkRequisition ();
				GTK.gtk_widget_get_preferred_size(icon, minimum, null);
				long context = GTK.gtk_widget_get_style_context(icon);
				GtkBorder margin = new GtkBorder ();
				GTK.gtk_style_context_get_margin(context, margin);
				trimWidth += minimum.width + margin.right;
			} else {
				int[] indicatorSize = new int[1];
				int[] indicatorSpacing = new int[1];
				GTK.gtk_widget_style_get (handle, OS.indicator_size, indicatorSize, 0);
				GTK.gtk_widget_style_get (handle, OS.indicator_spacing, indicatorSpacing, 0);
				indicatorHeight = indicatorSize [0] + 2 * indicatorSpacing [0];
				trimWidth += indicatorHeight + indicatorSpacing [0];
			}
		} else {
			Point thickness = getThickness (handle);
			trimWidth += thickness.x * 2;
			trimHeight += thickness.y * 2;
			GtkBorder innerBorder = getBorder (OS.inner_border, handle, INNER_BORDER);
			trimWidth += innerBorder.left + innerBorder.right;
			trimHeight += innerBorder.top + innerBorder.bottom;
			if (GTK.gtk_widget_get_can_default (handle)) {
				GtkBorder defaultBorder = getBorder (OS.default_border, handle, DEFAULT_BORDER);
				trimWidth += defaultBorder.left + defaultBorder.right;
				trimHeight += defaultBorder.top + defaultBorder.bottom;
			}
		}
		int imageWidth = 0, imageHeight = 0;
		if (GTK.gtk_widget_get_visible (imageHandle)) {
			GtkRequisition requisition = new GtkRequisition ();
			gtk_widget_get_preferred_size (imageHandle, requisition);
			imageWidth = requisition.width;
			imageHeight = requisition.height;
			int [] spacing = new int [1];
			OS.g_object_get (boxHandle, OS.spacing, spacing, 0);
			imageWidth += spacing [0];
		}
		long labelLayout = GTK.gtk_label_get_layout (labelHandle);
		int pangoWidth = OS.pango_layout_get_width (labelLayout);
		if (wHint != SWT.DEFAULT) {
			OS.pango_layout_set_width (labelLayout, Math.max (1, (wHint - imageWidth - trimWidth)) * OS.PANGO_SCALE);
		} else {
			OS.pango_layout_set_width (labelLayout, -1);
		}
		int [] w = new int [1], h = new int [1];
		OS.pango_layout_get_pixel_size (labelLayout, w, h);
		OS.pango_layout_set_width (labelLayout, pangoWidth);
		size = new Point(0, 0);
		size.x += wHint == SWT.DEFAULT ? w [0] + imageWidth + trimWidth : wHint;
		size.y += hHint == SWT.DEFAULT ? Math.max(Math.max(imageHeight, indicatorHeight), h [0]) + trimHeight : hHint;
	} else {
		size = computeNativeSize (handle, wHint, hHint, changed);
	}
	if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
		GTK.gtk_widget_set_size_request (boxHandle, reqWidth [0], reqHeight [0]);
	}
	if (wHint != SWT.DEFAULT || hHint != SWT.DEFAULT) {
		if (GTK.gtk_widget_get_can_default (handle)) {
			GtkBorder border = getBorder (OS.default_border, handle, DEFAULT_BORDER);
			if (wHint != SWT.DEFAULT) size.x += border.left + border.right;
			if (hHint != SWT.DEFAULT) size.y += border.top + border.bottom;
		}
	}
	return size;
}

@Override
void createHandle (int index) {
	state |= HANDLE;
	if ((style & (SWT.PUSH | SWT.TOGGLE)) == 0) state |= THEME_BACKGROUND;
	int bits = SWT.ARROW | SWT.TOGGLE | SWT.CHECK | SWT.RADIO | SWT.PUSH;
	fixedHandle = OS.g_object_new (display.gtk_fixed_get_type (), 0);
	if (fixedHandle == 0) error (SWT.ERROR_NO_HANDLES);
	gtk_widget_set_has_surface_or_window (fixedHandle, true);
	switch (style & bits) {
		case SWT.ARROW:
			byte arrowType [] = GTK.GTK_NAMED_ICON_GO_UP;
			if ((style & SWT.UP) != 0) arrowType = GTK.GTK_NAMED_ICON_GO_UP;
			if ((style & SWT.DOWN) != 0) arrowType = GTK.GTK_NAMED_ICON_GO_DOWN;
			if ((style & SWT.LEFT) != 0) arrowType = GTK.GTK_NAMED_ICON_GO_PREVIOUS;
			if ((style & SWT.RIGHT) != 0) arrowType = GTK.GTK_NAMED_ICON_GO_NEXT;
			arrowHandle = GTK.gtk_image_new_from_icon_name (arrowType, GTK.GTK_ICON_SIZE_MENU);
			if (arrowHandle == 0) error (SWT.ERROR_NO_HANDLES);

			handle = GTK.gtk_button_new ();
			if (handle == 0) error (SWT.ERROR_NO_HANDLES);
			break;
		case SWT.TOGGLE:
			handle = GTK.gtk_toggle_button_new ();
			if (handle == 0) error (SWT.ERROR_NO_HANDLES);
			break;
		case SWT.CHECK:
			handle = GTK.gtk_check_button_new ();
			if (handle == 0) error (SWT.ERROR_NO_HANDLES);
			break;
		case SWT.RADIO:
			/*
			* Feature in GTK.  In GTK, radio button must always be part of
			* a radio button group.  In a GTK radio group, one button is always
			* selected.  This means that it is not possible to have a single
			* radio button that is unselected.  This is necessary to allow
			* applications to implement their own radio behavior or use radio
			* buttons outside of radio groups.  The fix is to create a hidden
			* radio button for each radio button we create and add them
			* to the same group.  This allows the visible button to be
			* unselected.
			*/
			groupHandle = GTK.gtk_radio_button_new (0);
			if (groupHandle == 0) error (SWT.ERROR_NO_HANDLES);
			OS.g_object_ref (groupHandle);
			OS.g_object_ref_sink (groupHandle);
			handle = GTK.gtk_radio_button_new (GTK.gtk_radio_button_get_group (groupHandle));
			if (handle == 0) error (SWT.ERROR_NO_HANDLES);
			break;
		case SWT.PUSH:
		default:
			handle = GTK.gtk_button_new ();
			if (handle == 0) error (SWT.ERROR_NO_HANDLES);
			GTK.gtk_widget_set_can_default (handle, true);
			break;
	}
	if ((style & SWT.ARROW) != 0) {
		// Use gtk_button_set_image() on GTK3 to prevent icons from being
		// trimmed with smaller sized buttons; see bug 528284.
		GTK.gtk_button_set_image(handle, arrowHandle);
	} else {
		boxHandle = gtk_box_new (GTK.GTK_ORIENTATION_HORIZONTAL, false, 4);
		if (boxHandle == 0) error (SWT.ERROR_NO_HANDLES);
		labelHandle = GTK.gtk_label_new_with_mnemonic (null);
		if (labelHandle == 0) error (SWT.ERROR_NO_HANDLES);
		imageHandle = GTK.gtk_image_new ();
		if (imageHandle == 0) error (SWT.ERROR_NO_HANDLES);
		GTK.gtk_container_add (handle, boxHandle);
		GTK.gtk_container_add (boxHandle, imageHandle);
		GTK.gtk_container_add (boxHandle, labelHandle);
		if ((style & SWT.WRAP) != 0) {
			GTK.gtk_label_set_line_wrap (labelHandle, true);
			GTK.gtk_label_set_line_wrap_mode (labelHandle, OS.PANGO_WRAP_WORD_CHAR);
		}
	}
	GTK.gtk_container_add (fixedHandle, handle);

	if ((style & SWT.ARROW) != 0) return;
	// In GTK 3 font description is inherited from parent widget which is not how SWT has always worked,
	// reset to default font to get the usual behavior
	setFontDescription(defaultFont().handle);
	_setAlignment (style & (SWT.LEFT | SWT.CENTER | SWT.RIGHT));
}

@Override
void createWidget (int index) {
	super.createWidget (index);
	text = "";
}

@Override
void deregister () {
	super.deregister ();
	if (boxHandle != 0) display.removeWidget (boxHandle);
	if (labelHandle != 0) display.removeWidget (labelHandle);
	if (imageHandle != 0) display.removeWidget (imageHandle);
	if (arrowHandle != 0) display.removeWidget (arrowHandle);
}

@Override
long fontHandle () {
	if (labelHandle != 0) return labelHandle;
	return super.fontHandle ();
}

/**
 * Returns a value which describes the position of the
 * text or image in the receiver. The value will be one of
 * <code>LEFT</code>, <code>RIGHT</code> or <code>CENTER</code>
 * unless the receiver is an <code>ARROW</code> button, in
 * which case, the alignment will indicate the direction of
 * the arrow (one of <code>LEFT</code>, <code>RIGHT</code>,
 * <code>UP</code> or <code>DOWN</code>).
 *
 * @return the alignment
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getAlignment () {
	checkWidget ();
	if ((style & SWT.ARROW) != 0) {
		if ((style & SWT.UP) != 0) return SWT.UP;
		if ((style & SWT.DOWN) != 0) return SWT.DOWN;
		if ((style & SWT.LEFT) != 0) return SWT.LEFT;
		if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
		return SWT.UP;
	}
	if ((style & SWT.LEFT) != 0) return SWT.LEFT;
	if ((style & SWT.CENTER) != 0) return SWT.CENTER;
	if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
	return SWT.LEFT;
}

/**
 * Returns <code>true</code> if the receiver is grayed,
 * and false otherwise. When the widget does not have
 * the <code>CHECK</code> style, return false.
 *
 * @return the grayed state of the checkbox
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public boolean getGrayed () {
	checkWidget();
	if ((style & SWT.CHECK) == 0) return false;
	return grayed;
}

/**
 * Returns the receiver's image if it has one, or null
 * if it does not.
 *
 * @return the receiver's image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getImage () {
	checkWidget ();
	return image;
}

@Override
String getNameText () {
	return getText ();
}

/**
 * Returns <code>true</code> if the receiver is selected,
 * and false otherwise.
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked. When it is of type <code>TOGGLE</code>,
 * it is selected when it is pushed in. If the receiver is of any other type,
 * this method returns false.
 *
 * @return the selection state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getSelection () {
	checkWidget ();
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return false;
	return GTK.gtk_toggle_button_get_active (handle);
}

/**
 * Returns the receiver's text, which will be an empty
 * string if it has never been set or if the receiver is
 * an <code>ARROW</code> button.
 *
 * @return the receiver's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText () {
	checkWidget();
	if ((style & SWT.ARROW) != 0) return "";
	return text;
}

@Override
long gtk_button_press_event (long widget, long event) {
	long result = super.gtk_button_press_event (widget, event);
	if (result != 0) return result;
	if ((style & SWT.RADIO) != 0) selected  = getSelection ();
	return result;
}

@Override
long gtk_clicked (long widget) {
	if (containedInRegion(lastInput.x, lastInput.y)) return 0;
	if ((style & SWT.RADIO) != 0) {
		if ((parent.getStyle () & SWT.NO_RADIO_GROUP) != 0) {
			setSelection (!selected);
		} else {
			selectRadio ();
		}
	} else {
		if ((style & SWT.CHECK) != 0) {
			if (grayed) {
				if (GTK.gtk_toggle_button_get_active (handle)) {
					GTK.gtk_toggle_button_set_inconsistent (handle, true);
				} else {
					GTK.gtk_toggle_button_set_inconsistent (handle, false);
				}
			}
		}
	}
	sendSelectionEvent (SWT.Selection);
	return 0;
}

@Override
long gtk_draw (long widget, long cairo) {
	/*
	 * On GTK3.19+, widget are are shown with the default minimum size regardless of the
	 * size of the fixed container. This causes 0x0 widgets to be visible but cannot be used.
	 * The fix is to make the widget invisible to the user. Resizing widget later on to a larger size
	 * makes the widget visible again in setBounds. See Bug 533469, Bug 531120.
	 */
	if (GTK.GTK_VERSION >= OS.VERSION (3, 20, 0) && (state & ZERO_WIDTH) != 0 && (state & ZERO_HEIGHT) != 0) {
		if (GTK.gtk_widget_get_visible(widget)) GTK.gtk_widget_set_visible(widget, false);
		// Button and display should not be disposed after hiding widget
		if (isDisposed() || display == null || display.isDisposed()) error (SWT.ERROR_DEVICE_DISPOSED);
	}
	return super.gtk_draw(widget, cairo);
}

@Override
boolean mustBeVisibleOnInitBounds() {
	// Bug 540298: Button needs to be visible in order to gain focus.
	return true;
}

@Override
long gtk_focus_in_event (long widget, long event) {
	long result = super.gtk_focus_in_event (widget, event);
	// widget could be disposed at this point
	if (handle == 0) return 0;
	if ((style & SWT.PUSH) != 0 && GTK.gtk_widget_has_default (handle)) {
		Decorations menuShell = menuShell ();
		menuShell.defaultButton = this;
	}
	return result;
}

@Override
long gtk_focus_out_event (long widget, long event) {
	long result = super.gtk_focus_out_event (widget, event);
	// widget could be disposed at this point
	if (handle == 0) return 0;
	if ((style & SWT.PUSH) != 0) {
		Decorations menuShell = menuShell ();
		if (menuShell.defaultButton == this) {
			menuShell.defaultButton = null;
		}
	}
	return result;
}

@Override
long gtk_key_press_event (long widget, long event) {
	long result = super.gtk_key_press_event (widget, event);
	if (result != 0) return result;
	if ((style & SWT.RADIO) != 0) selected  = getSelection ();
	return result;
}

@Override
void hookEvents () {
	super.hookEvents();
	OS.g_signal_connect_closure (handle, OS.clicked, display.getClosure (CLICKED), false);
	if (labelHandle != 0) {
		OS.g_signal_connect_closure_by_id (labelHandle, display.signalIds [MNEMONIC_ACTIVATE], 0, display.getClosure (MNEMONIC_ACTIVATE), false);
	}
}

@Override
boolean isDescribedByLabel () {
	return false;
}

@Override
boolean mnemonicHit (char key) {
	if (labelHandle == 0) return false;
	boolean result = super.mnemonicHit (labelHandle, key);
	if (result) setFocus ();
	return result;
}

@Override
boolean mnemonicMatch (char key) {
	if (labelHandle == 0) return false;
	return mnemonicMatch (labelHandle, key);
}

@Override
void register () {
	super.register ();
	if (boxHandle != 0) display.addWidget (boxHandle, this);
	if (labelHandle != 0) display.addWidget (labelHandle, this);
	if (imageHandle != 0) display.addWidget (imageHandle, this);
	if (arrowHandle != 0) display.addWidget (arrowHandle, this);
}

@Override
void releaseHandle () {
	super.releaseHandle ();
	boxHandle = imageHandle = labelHandle = arrowHandle = 0;
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	if (groupHandle != 0) OS.g_object_unref (groupHandle);
	groupHandle = 0;
	if (imageList != null) imageList.dispose ();
	imageList = null;
	image = null;
	text = null;
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
public void removeSelectionListener (SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);
}

@Override
void resizeHandle (int width, int height) {
	if ((style & (SWT.CHECK | SWT.RADIO)) != 0 && (style & SWT.WRAP) == 0) {
		OS.swt_fixed_resize (GTK.gtk_widget_get_parent (topHandle()), topHandle(), width, height);
	} else {
		super.resizeHandle(width, height);
	}
}

void selectRadio () {
	/*
	* This code is intentionally commented.  When two groups
	* of radio buttons with the same parent are separated by
	* another control, the correct behavior should be that
	* the two groups act independently.  This is consistent
	* with radio tool and menu items.  The commented code
	* implements this behavior.
	*/
//	int index = 0;
//	Control [] children = parent._getChildren ();
//	while (index < children.length && children [index] != this) index++;
//	int i = index - 1;
//	while (i >= 0 && children [i].setRadioSelection (false)) --i;
//	int j = index + 1;
//	while (j < children.length && children [j].setRadioSelection (false)) j++;
//	setSelection (true);
	Control [] children = parent._getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (this != child) child.setRadioSelection (false);
	}
	setSelection (true);
}

/**
 * Controls how text, images and arrows will be displayed
 * in the receiver. The argument should be one of
 * <code>LEFT</code>, <code>RIGHT</code> or <code>CENTER</code>
 * unless the receiver is an <code>ARROW</code> button, in
 * which case, the argument indicates the direction of
 * the arrow (one of <code>LEFT</code>, <code>RIGHT</code>,
 * <code>UP</code> or <code>DOWN</code>).
 *
 * @param alignment the new alignment
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setAlignment (int alignment) {
	checkWidget ();
	_setAlignment (alignment);
}

void _setAlignment (int alignment) {
	if ((style & SWT.ARROW) != 0) {
		if ((style & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT)) == 0) return;
		style &= ~(SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT);
		style |= alignment & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT);
		boolean isRTL = (style & SWT.RIGHT_TO_LEFT) != 0;
		byte arrowType [] = GTK.GTK_NAMED_ICON_GO_UP;
		switch (alignment) {
			case SWT.UP: arrowType = GTK.GTK_NAMED_ICON_GO_UP; break;
			case SWT.DOWN: arrowType = GTK.GTK_NAMED_ICON_GO_DOWN; break;
			case SWT.LEFT: arrowType = isRTL ? GTK.GTK_NAMED_ICON_GO_NEXT : GTK.GTK_NAMED_ICON_GO_PREVIOUS; break;
			case SWT.RIGHT: arrowType = isRTL ? GTK.GTK_NAMED_ICON_GO_PREVIOUS : GTK.GTK_NAMED_ICON_GO_NEXT; break;
		}
		GTK.gtk_image_set_from_icon_name (arrowHandle, arrowType, GTK.GTK_ICON_SIZE_MENU);
		return;
	}
	if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) return;
	style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	style |= alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	/* Alignment not honoured when image and text are visible */
	boolean bothVisible = GTK.gtk_widget_get_visible (labelHandle) && GTK.gtk_widget_get_visible (imageHandle);
	if (bothVisible) {
		if ((style & (SWT.RADIO | SWT.CHECK)) != 0) alignment = SWT.LEFT;
		if ((style & (SWT.PUSH | SWT.TOGGLE)) != 0) alignment = SWT.CENTER;
	}
	if ((alignment & SWT.LEFT) != 0) {
		if (bothVisible) {
			gtk_box_set_child_packing (boxHandle, labelHandle, false, false, 0, GTK.GTK_PACK_START);
			gtk_box_set_child_packing (boxHandle, imageHandle, false, false, 0, GTK.GTK_PACK_START);
		} else {
			gtk_box_set_child_packing (boxHandle, labelHandle, true, true, 0, GTK.GTK_PACK_END);
			gtk_box_set_child_packing (boxHandle, imageHandle, true, true, 0, GTK.GTK_PACK_START);
		}

		if (GTK.GTK_VERSION >= OS.VERSION(3, 16, 0)) {
			gtk_label_set_align(labelHandle,0.0f,0.5f);
			gtk_widget_set_align(imageHandle,GTK.GTK_ALIGN_START, GTK.GTK_ALIGN_CENTER);
		} else {
			GTK.gtk_misc_set_alignment (labelHandle, 0.0f, 0.5f);
			GTK.gtk_misc_set_alignment (imageHandle, 0.0f, 0.5f);
		}

		GTK.gtk_label_set_justify (labelHandle, GTK.GTK_JUSTIFY_LEFT);
		return;
	}
	if ((alignment & SWT.CENTER) != 0) {
		if (bothVisible) {
			gtk_box_set_child_packing (boxHandle, labelHandle, true, true, 0, GTK.GTK_PACK_END);
			gtk_box_set_child_packing (boxHandle, imageHandle, true, true, 0, GTK.GTK_PACK_START);

			if (GTK.GTK_VERSION >= OS.VERSION(3, 16, 0))  {
				gtk_label_set_align(labelHandle,0.0f,0.5f);
				gtk_widget_set_align(imageHandle,GTK.GTK_ALIGN_END, GTK.GTK_ALIGN_CENTER);
			} else  {
				GTK.gtk_misc_set_alignment (labelHandle, 0f, 0.5f);
				GTK.gtk_misc_set_alignment (imageHandle, 1f, 0.5f);
			}

		} else {
			gtk_box_set_child_packing (boxHandle, labelHandle, true, true, 0, GTK.GTK_PACK_END);
			gtk_box_set_child_packing (boxHandle, imageHandle, true, true, 0, GTK.GTK_PACK_START);

			if (GTK.GTK_VERSION >= OS.VERSION(3, 16, 0))  {
				gtk_label_set_align(labelHandle,0.5f,0.5f);
				gtk_widget_set_align(imageHandle,GTK.GTK_ALIGN_CENTER, GTK.GTK_ALIGN_CENTER);
			} else {
				GTK.gtk_misc_set_alignment (labelHandle, 0.5f, 0.5f);
				GTK.gtk_misc_set_alignment (imageHandle, 0.5f, 0.5f);
			}
			GTK.gtk_label_set_justify (labelHandle, GTK.GTK_JUSTIFY_CENTER);
		}
		return;
	}
	if ((alignment & SWT.RIGHT) != 0) {
		if (bothVisible) {
			gtk_box_set_child_packing (boxHandle, labelHandle, false, false, 0, GTK.GTK_PACK_END);
			gtk_box_set_child_packing (boxHandle, imageHandle, false, false, 0, GTK.GTK_PACK_END);
		} else {
			gtk_box_set_child_packing (boxHandle, labelHandle, true, true, 0, GTK.GTK_PACK_END);
			gtk_box_set_child_packing (boxHandle, imageHandle, true, true, 0, GTK.GTK_PACK_START);
		}

		if (GTK.GTK_VERSION >= OS.VERSION(3, 16, 0))  {
			gtk_label_set_align(labelHandle,1.0f,0.5f);
			gtk_widget_set_align(imageHandle,GTK.GTK_ALIGN_END, GTK.GTK_ALIGN_CENTER);
		} else {
			GTK.gtk_misc_set_alignment (labelHandle, 1.0f, 0.5f);
			GTK.gtk_misc_set_alignment (imageHandle, 1.0f, 0.5f);
		}
		GTK.gtk_label_set_justify (labelHandle, GTK.GTK_JUSTIFY_RIGHT);
		return;
	}
}

@Override
void setBackgroundGdkRGBA (long context, long handle, GdkRGBA rgba) {
	background = rgba;
	// Form background CSS string
	String css ="* {background : ";
	String color = display.gtk_rgba_to_css_string (rgba);
	css += color + ";}";

	// Cache background color
	cssBackground = css;

	// Apply background color and any cached foreground color
	String finalCss = display.gtk_css_create_css_color_string (cssBackground, cssForeground, SWT.BACKGROUND);
	gtk_css_provider_load_from_css (context, finalCss);
}

@Override
int setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	/*
	* Bug in GTK.  For some reason, when the label is
	* wrappable and its container is resized, it does not
	* cause the label to be wrapped.  The fix is to
	* determine the size that will wrap the label
	* and expilictly set that size to force the label
	* to wrap.
	*
	* This part of the fix causes the label to be
	* resized to the preferred size but it still
	* won't draw properly.
	*/
	boolean wrap = labelHandle != 0 && (style & SWT.WRAP) != 0 && GTK.gtk_widget_get_visible (labelHandle);
	if (wrap) GTK.gtk_widget_set_size_request (boxHandle, -1, -1);
	int result = super.setBounds (x, y, width, height, move, resize);
	/*
	* Bug in GTK.  For some reason, when the label is
	* wrappable and its container is resized, it does not
	* cause the label to be wrapped.  The fix is to
	* determine the size that will wrap the label
	* and expilictly set that size to force the label
	* to wrap.
	*
	* This part of the fix forces the label to be
	* resized so that it will draw wrapped.
	*/
	if (wrap) {
		GtkAllocation allocation = new GtkAllocation();
		GTK.gtk_widget_get_allocation (boxHandle, allocation);
		int boxWidth = allocation.width;
		int boxHeight = allocation.height;
		long labelLayout = GTK.gtk_label_get_layout (labelHandle);
		int pangoWidth = OS.pango_layout_get_width (labelLayout);
		OS.pango_layout_set_width (labelLayout, -1);
		int [] w = new int [1], h = new int [1];
		OS.pango_layout_get_pixel_size (labelLayout, w, h);
		OS.pango_layout_set_width (labelLayout, pangoWidth);
		int imageWidth = 0;
		if (GTK.gtk_widget_get_visible (imageHandle)) {
			GtkRequisition requisition = new GtkRequisition ();
			gtk_widget_get_preferred_size (imageHandle, requisition);
			imageWidth = requisition.width;
			int [] spacing = new int [1];
			OS.g_object_get (boxHandle, OS.spacing, spacing, 0);
			imageWidth += spacing [0];
		}
		GTK.gtk_widget_set_size_request (labelHandle, Math.min(w [0], boxWidth - imageWidth), -1);
		/*
		* Bug in GTK.  Setting the size request should invalidate the label's
		* layout, but it does not.  The fix is to resize the label directly.
		*/
		GtkRequisition requisition = new GtkRequisition ();
		gtk_widget_get_preferred_size (boxHandle, requisition);
		allocation.width = boxWidth;
		allocation.height = boxHeight;
		Point sizes = resizeCalculationsGTK3(boxHandle, boxWidth, boxHeight);
		allocation.width = sizes.x;
		allocation.height = sizes.y;
		GTK.gtk_widget_size_allocate (boxHandle, allocation);
	}
	return result;
}

@Override
void setFontDescription (long fontDesc) {
	// Don't set the font if we have no text set
	if (GTK.GTK_VERSION >= OS.VERSION(3, 22, 0) && ((text != null && text.isEmpty()) || text == null)) {
		return;
	} else {
		super.setFontDescription (fontDesc);
		if (labelHandle != 0) setFontDescription (labelHandle, fontDesc);
		if (imageHandle != 0) setFontDescription (imageHandle, fontDesc);
	}
}

@Override
boolean setRadioSelection (boolean value) {
	if ((style & SWT.RADIO) == 0) return false;
	if (getSelection () != value) {
		setSelection (value);
		sendSelectionEvent (SWT.Selection);
	}
	return true;
}

@Override
void setForegroundGdkRGBA (GdkRGBA rgba) {
	super.setForegroundGdkRGBA(rgba);
	setForegroundGdkRGBA (fixedHandle, rgba);
	if (labelHandle != 0) setForegroundGdkRGBA (labelHandle, rgba);
	if (imageHandle != 0) setForegroundGdkRGBA (imageHandle, rgba);

	/*
	 * Feature in GTK3: GtkCheckButton & its descendant GtkRadioButton are often invisible or
	 * very hard to see with certain themes that don't define an icon for Check/Radio buttons.
	 * Giving them a border color that matches the text color ensures consistent visibility
	 * across most themes. See bug 463733.
	 */
	if (GTK.GTK_VERSION < OS.VERSION (3, 16, 0) && (style & (SWT.CHECK | SWT.RADIO)) != 0) {
		gtk_swt_set_border_color (rgba);
	}
}

@Override
void setForegroundGdkRGBA (long handle, GdkRGBA rgba) {
	if (GTK.GTK_VERSION < OS.VERSION(3, 14, 0)) {
		super.setForegroundGdkRGBA(handle, rgba);
		return;
	}
	GdkRGBA toSet = rgba == null ? display.COLOR_WIDGET_FOREGROUND_RGBA : rgba;
	long context = GTK.gtk_widget_get_style_context (handle);

	// Form foreground string
	String color = display.gtk_rgba_to_css_string(toSet);
	String name = GTK.GTK_VERSION >= OS.VERSION(3, 20, 0) ? "button" : "GtkButton";
	String css = name + " {color: " + color + ";}";

	// Cache foreground color
	cssForeground = css;

	// Apply foreground color and any cached background color
	String finalCss = display.gtk_css_create_css_color_string (cssBackground, cssForeground, SWT.FOREGROUND);
	gtk_css_provider_load_from_css(context, finalCss);
}

private void gtk_swt_set_border_color (GdkRGBA rgba) {
	String css_string = "* {\n";
	if (rgba != null) {
		String css_color = display.gtk_rgba_to_css_string (rgba);
		css_string += "border-color: " + css_color + ";\n";
	}
	css_string += "}\n";

	String finalCss;
	if (GTK.GTK_VERSION >= OS.VERSION(3, 14, 0)) {
		// Cache foreground color
		cssForeground += "\n" + css_string;
		finalCss = display.gtk_css_create_css_color_string (cssBackground, cssForeground, SWT.FOREGROUND);
	} else {
		finalCss = css_string;
	}

	// Apply the CSS
	long context = GTK.gtk_widget_get_style_context (handle);
	gtk_css_provider_load_from_css (context, finalCss);
}

/**
 * Sets the grayed state of the receiver.  This state change
 * only applies if the control was created with the SWT.CHECK
 * style.
 *
 * @param grayed the new grayed state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public void setGrayed (boolean grayed) {
	checkWidget();
	if ((style & SWT.CHECK) == 0) return;
	this.grayed = grayed;
	if (grayed && GTK.gtk_toggle_button_get_active (handle)) {
		GTK.gtk_toggle_button_set_inconsistent (handle, true);
	} else {
		GTK.gtk_toggle_button_set_inconsistent (handle, false);
	}
}

/**
 * Sets the receiver's image to the argument, which may be
 * <code>null</code> indicating that no image should be displayed.
 * <p>
 * Note that a Button can display an image and text simultaneously
 * on Windows (starting with XP), GTK+ and OSX.  On other platforms,
 * a Button that has an image and text set into it will display the
 * image or text that was set most recently.
 * </p>
 * @param image the image to display on the receiver (may be <code>null</code>)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setImage (Image image) {
	checkWidget ();
	if ((style & SWT.ARROW) != 0) return;
	if (imageList != null) imageList.dispose ();
	imageList = null;
	if (image != null) {
		if (image.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
		imageList = new ImageList ();
		int imageIndex = imageList.add (image);
		long pixbuf = imageList.getPixbuf (imageIndex);
		gtk_image_set_from_gicon(imageHandle, pixbuf);
	} else {
		gtk_image_set_from_gicon (imageHandle, 0);
	}
	this.image = image;
	updateWidgetsVisibility();
	_setAlignment (style);
}

@Override
void setOrientation (boolean create) {
	super.setOrientation (create);
	if ((style & SWT.RIGHT_TO_LEFT) != 0 || !create) {
		int dir = (style & SWT.RIGHT_TO_LEFT) != 0 ? GTK.GTK_TEXT_DIR_RTL : GTK.GTK_TEXT_DIR_LTR;
		if (boxHandle != 0) GTK.gtk_widget_set_direction (boxHandle, dir);
		if (labelHandle != 0) GTK.gtk_widget_set_direction (labelHandle, dir);
		if (imageHandle != 0) GTK.gtk_widget_set_direction (imageHandle, dir);
		if (arrowHandle != 0) {
			byte arrowType [] = (style & SWT.RIGHT_TO_LEFT) != 0 ? GTK.GTK_NAMED_ICON_GO_NEXT : GTK.GTK_NAMED_ICON_GO_PREVIOUS;
			switch (style & (SWT.LEFT | SWT.RIGHT)) {
				case SWT.LEFT: GTK.gtk_image_set_from_icon_name (arrowHandle, arrowType, GTK.GTK_ICON_SIZE_MENU); break;
				case SWT.RIGHT: GTK.gtk_image_set_from_icon_name (arrowHandle, arrowType, GTK.GTK_ICON_SIZE_MENU); break;
			}
		}
	}
}

/**
 * Sets the selection state of the receiver, if it is of type <code>CHECK</code>,
 * <code>RADIO</code>, or <code>TOGGLE</code>.
 *
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked. When it is of type <code>TOGGLE</code>,
 * it is selected when it is pushed in.
 *
 * @param selected the new selection state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (boolean selected) {
	checkWidget();
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return;
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CLICKED);
	GTK.gtk_toggle_button_set_active (handle, selected);
	if ((style & SWT.CHECK) != 0) {
		if (selected && grayed) {
			GTK.gtk_toggle_button_set_inconsistent (handle, true);
		} else {
			GTK.gtk_toggle_button_set_inconsistent (handle, false);
		}
	}
	if ((style & SWT.RADIO) != 0) GTK.gtk_toggle_button_set_active (groupHandle, !selected);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CLICKED);
}

/**
 * Sets the receiver's text.
 * <p>
 * This method sets the button label.  The label may include
 * the mnemonic character but must not contain line delimiters.
 * </p>
 * <p>
 * Mnemonics are indicated by an '&amp;' that causes the next
 * character to be the mnemonic.  When the user presses a
 * key sequence that matches the mnemonic, a selection
 * event occurs. On most platforms, the mnemonic appears
 * underlined but may be emphasized in a platform specific
 * manner.  The mnemonic indicator character '&amp;' can be
 * escaped by doubling it in the string, causing a single
 * '&amp;' to be displayed.
 * </p><p>
 * Note that a Button can display an image and text simultaneously
 * on Windows (starting with XP), GTK+ and OSX.  On other platforms,
 * a Button that has an image and text set into it will display the
 * image or text that was set most recently.
 * </p><p>
 * Also note, if control characters like '\n', '\t' etc. are used
 * in the string, then the behavior is platform dependent.
 * </p>
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
public void setText (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.ARROW) != 0) return;
	text = string;
	char [] chars = fixMnemonic (string);
	byte [] buffer = Converter.wcsToMbcs (chars, true);
	GTK.gtk_label_set_text_with_mnemonic (labelHandle, buffer);
	updateWidgetsVisibility();
	_setAlignment (style);
	// Now that text has been added, set the font. This ensures
	// the Button's size is correctly calculated/updated.
	if (GTK.GTK_VERSION >= OS.VERSION(3, 22, 0)) {
		setFontDescription(font == null ? defaultFont().handle : font.handle);
	}
}

private void updateWidgetsVisibility() {
	if (text.length() == 0 && image == null) {
		GTK.gtk_widget_hide (boxHandle);
		GTK.gtk_widget_hide (labelHandle);
		GTK.gtk_widget_hide (imageHandle);
	} else {
		GTK.gtk_widget_show (boxHandle);
		if (text.length() == 0)
			GTK.gtk_widget_hide (labelHandle);
		else
			GTK.gtk_widget_show (labelHandle);
		if (image == null)
			GTK.gtk_widget_hide (imageHandle);
		else
			GTK.gtk_widget_show (imageHandle);
	}
}

@Override
void showWidget () {
	super.showWidget ();
	if (boxHandle != 0 && ((text != null && text.length() != 0) || image != null)) GTK.gtk_widget_show (boxHandle);
	if (labelHandle != 0  && text != null && text.length() != 0) GTK.gtk_widget_show (labelHandle);
	if (arrowHandle != 0) GTK.gtk_widget_show (arrowHandle);
}

@Override
int traversalCode(int key, long event) {
	int code = super.traversalCode (key, event);
	if ((style & SWT.ARROW) != 0) code &= ~(SWT.TRAVERSE_TAB_NEXT | SWT.TRAVERSE_TAB_PREVIOUS);
	if ((style & SWT.RADIO) != 0) code |= SWT.TRAVERSE_ARROW_NEXT | SWT.TRAVERSE_ARROW_PREVIOUS;
	return code;
}

@Override
long windowProc (long handle, long arg0, long user_data) {
	/*
	 * For Labels/Buttons, the first widget in the tree with a GdkWindow is SwtFixed.
	 * Unfortunately this fails the check in !GTK_IS_CONTAINER check Widget.windowProc().
	 * Instead lets override windowProc() here and check for paintHandle() compatibility.
	 * Fixes bug 481485 without re-introducing bug 483791.
	 */
	switch ((int)user_data) {
		case DRAW: {
			if (paintHandle() == handle) {
				return gtk_draw(handle, arg0);
			}
		}
	}
	return super.windowProc(handle, arg0, user_data);
}

}

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class provide an etched border
 * with an optional title.
 * <p>
 * Shadow styles are hints and may not be honoured
 * by the platform.  To create a group with the
 * default shadow style for the platform, do not
 * specify a shadow style.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SHADOW_ETCHED_IN, SHADOW_ETCHED_OUT, SHADOW_IN, SHADOW_OUT, SHADOW_NONE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of the above styles may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class Group extends Composite {
	int clientHandle, labelHandle;
	String text = "";

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
 * @see SWT#SHADOW_ETCHED_IN
 * @see SWT#SHADOW_ETCHED_OUT
 * @see SWT#SHADOW_IN
 * @see SWT#SHADOW_OUT
 * @see SWT#SHADOW_NONE
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Group (Composite parent, int style) {
	super (parent, checkStyle (style));
}

static int checkStyle (int style) {
	/*
	* Even though it is legal to create this widget
	* with scroll bars, they serve no useful purpose
	* because they do not automatically scroll the
	* widget's client area.  The fix is to clear
	* the SWT style.
	*/
	return style & ~(SWT.H_SCROLL | SWT.V_SCROLL);
}

int clientHandle () {
	return clientHandle;
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
	if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
	Point defaultSize = computeNativeSize (handle, wHint, hHint, changed);
	Point size;
	if (layout != null) {
		size = layout.computeSize (this, wHint, hHint, changed);
	} else {
		size = minimumSize ();
	}
	int width = size.x,  height = size.y;
	if (width == 0) width = DEFAULT_WIDTH;
	if (height == 0) height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	Rectangle trim = computeTrim (0, 0, width, height);
	width = Math.max (trim.width, defaultSize.x);
	height = trim.height;
	return new Point (width, height);
}

/**
 * Given a desired <em>client area</em> for the receiver
 * (as described by the arguments), returns the bounding
 * rectangle which would be required to produce that client
 * area.
 * <p>
 * In other words, it returns a rectangle such that, if the
 * receiver's bounds were set to that rectangle, the area
 * of the receiver which is capable of displaying data
 * (that is, not covered by the "trimmings") would be the
 * rectangle described by the arguments (relative to the
 * receiver's parent).
 * </p>
 * 
 * @return the required bounds to produce the given client area
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getClientArea
 */
public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget();
	int clientX = OS.GTK_WIDGET_X (clientHandle);
	int clientY = OS.GTK_WIDGET_Y (clientHandle);
	x -= clientX;
	y -= clientY;
	width += clientX + clientX;
	height += clientX + clientY;
	return new Rectangle (x, y, width, height);
}

void createHandle(int index) {
	state |= HANDLE;
	fixedHandle = OS.gtk_fixed_new ();
	if (fixedHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_fixed_set_has_window (fixedHandle, true);
	handle = OS.gtk_frame_new (null);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	labelHandle = OS.gtk_label_new (null);
	if (labelHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.g_object_ref (labelHandle);
	OS.gtk_object_sink (labelHandle);
	clientHandle = OS.gtk_fixed_new();
	if (clientHandle == 0) error (SWT.ERROR_NO_HANDLES);
	int parentHandle = parent.parentingHandle ();
	OS.gtk_container_add (parentHandle, fixedHandle);
	OS.gtk_container_add (fixedHandle, handle);
	OS.gtk_container_add (handle, clientHandle);
	OS.gtk_widget_show (handle);
	OS.gtk_widget_show (clientHandle);
	OS.gtk_widget_show (labelHandle);
	OS.gtk_widget_show (fixedHandle);	
	if ((style & SWT.SHADOW_IN) != 0) {
		OS.gtk_frame_set_shadow_type (handle, OS.GTK_SHADOW_IN);
	}
	if ((style & SWT.SHADOW_OUT) != 0) {
		OS.gtk_frame_set_shadow_type (handle, OS.GTK_SHADOW_OUT);
	}
	if ((style & SWT.SHADOW_ETCHED_IN) != 0) {
		OS.gtk_frame_set_shadow_type (handle, OS.GTK_SHADOW_ETCHED_IN);
	}
	if ((style & SWT.SHADOW_ETCHED_OUT) != 0) {
		OS.gtk_frame_set_shadow_type (handle, OS.GTK_SHADOW_ETCHED_OUT);
	}
}

void deregister () {
	super.deregister ();
	display.removeWidget (clientHandle);
	display.removeWidget (labelHandle);
}

void enableWidget (boolean enabled) {
	OS.gtk_widget_set_sensitive (labelHandle, enabled);
}

int eventHandle () {
	return fixedHandle;
}

void fixGroup () {
	/*
	* Force the container to allocate the size of its children.
	*/
	int flags = OS.GTK_WIDGET_FLAGS (handle);
	OS.GTK_WIDGET_SET_FLAGS (handle, OS.GTK_VISIBLE);
	GtkRequisition requisition = new GtkRequisition ();
	OS.gtk_widget_size_request (handle, requisition);
	OS.gtk_container_resize_children (handle);
	if ((flags & OS.GTK_VISIBLE) == 0) {
		OS.GTK_WIDGET_UNSET_FLAGS (handle, OS.GTK_VISIBLE);	
	}
}

public Rectangle getClientArea () {
	checkWidget();
	int width = OS.GTK_WIDGET_WIDTH (clientHandle);
	int height = OS.GTK_WIDGET_HEIGHT (clientHandle);
	return new Rectangle (0, 0, width, height);
}

String getNameText () {
	return getText ();
}

/**
 * Returns the receiver's text, which is the string that the
 * is used as the <em>title</em>. If the text has not previously
 * been set, returns an empty string.
 *
 * @return the text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText () {
	checkWidget();
	return text;
}

void hookEvents () {
	super.hookEvents();
	if (labelHandle != 0) {
		OS.g_signal_connect (labelHandle, OS.mnemonic_activate, display.windowProc3, MNEMONIC_ACTIVATE);
	}
}

boolean mnemonicHit (char key) {
	if (labelHandle == 0) return false;
	boolean result = super.mnemonicHit (labelHandle, key);
	if (result) setFocus ();
	return result;
}

boolean mnemonicMatch (char key) {
	if (labelHandle == 0) return false;
	return mnemonicMatch (labelHandle, key);
}

int parentingHandle() {
	return clientHandle;
}

void register () {
	super.register ();
	display.addWidget (clientHandle, this);
	display.addWidget (labelHandle, this);
}

void releaseHandle () {
	super.releaseHandle ();
	clientHandle = labelHandle = 0;
}

void releaseWidget () {
	super.releaseWidget ();
	if (labelHandle != 0) OS.g_object_unref (labelHandle);
	text = null;
}

void setBackgroundColor (GdkColor color) {
	super.setBackgroundColor (color);
	setBackgroundColor(fixedHandle, color);
}

void setFontDescription (int font) {
	super.setFontDescription (font);
	OS.gtk_widget_modify_font (labelHandle, font);
}

void setForegroundColor (GdkColor color) {
	super.setForegroundColor (color);
	OS.gtk_widget_modify_fg (labelHandle,  OS.GTK_STATE_NORMAL, color);
}

void setOrientation () {
	super.setOrientation ();
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		OS.gtk_widget_set_direction (handle, OS.GTK_TEXT_DIR_RTL);
		OS.gtk_widget_set_direction (labelHandle, OS.GTK_TEXT_DIR_RTL);
	}
}

/**
 * Sets the receiver's text, which is the string that will
 * be displayed as the receiver's <em>title</em>, to the argument,
 * which may not be null. The string may include the mnemonic character.
 * </p>
 * Mnemonics are indicated by an '&amp' that causes the next
 * character to be the mnemonic.  When the user presses a
 * key sequence that matches the mnemonic, focus is assgned
 * to the first child of the group. On most platforms, the
 * mnemonic appears underlined but may be emphasised in a
 * platform specific manner.  The mnemonic indicator character
 *'&amp' can be escaped by doubling it in the string, causing
 * a single '&amp' to be displayed.
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
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	text = string;
	char [] chars = fixMnemonic (string);
	byte [] buffer = Converter.wcsToMbcs (null, chars, true);
	OS.gtk_label_set_text_with_mnemonic (labelHandle, buffer);
	if (string.length () != 0) {
		if (OS.gtk_frame_get_label_widget (handle) == 0) {
			OS.gtk_frame_set_label_widget (handle, labelHandle);
		}	
	} else {
		OS.gtk_frame_set_label_widget (handle, 0);
	}
	fixGroup ();
}

}

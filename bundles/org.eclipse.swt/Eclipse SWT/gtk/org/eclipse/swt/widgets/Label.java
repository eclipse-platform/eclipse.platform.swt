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
 * Instances of this class represent a non-selectable
 * user interface object that displays a string or image.
 * When SEPARATOR is specified, displays a single
 * vertical or horizontal line.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SEPARATOR, HORIZONTAL, VERTICAL</dd>
 * <dd>SHADOW_IN, SHADOW_OUT, SHADOW_NONE</dd>
 * <dd>CENTER, LEFT, RIGHT, WRAP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of SHADOW_IN, SHADOW_OUT and SHADOW_NONE may be specified.
 * SHADOW_NONE is a HINT. Only one of HORIZONTAL and VERTICAL may be specified.
 * Only one of CENTER, LEFT and RIGHT may be specified.
 * </p><p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public class Label extends Control {
	int frameHandle, labelHandle, imageHandle;
	Image image;
	String text;

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
 * @see SWT#SEPARATOR
 * @see SWT#HORIZONTAL
 * @see SWT#VERTICAL
 * @see SWT#SHADOW_IN
 * @see SWT#SHADOW_OUT
 * @see SWT#SHADOW_NONE
 * @see SWT#CENTER
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#WRAP
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Label (Composite parent, int style) {
	super (parent, checkStyle (style));
}

static int checkStyle (int style) {
	style |= SWT.NO_FOCUS;
	if ((style & SWT.SEPARATOR) != 0) {
		style = checkBits (style, SWT.VERTICAL, SWT.HORIZONTAL, 0, 0, 0, 0);
		return checkBits (style, SWT.SHADOW_OUT, SWT.SHADOW_IN, SWT.SHADOW_NONE, 0, 0, 0);
	} 
	return checkBits (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
	if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
	if ((style & SWT.SEPARATOR) != 0) {
		if ((style & SWT.HORIZONTAL) != 0) {
			if (wHint == SWT.DEFAULT) wHint = DEFAULT_WIDTH;
		} else {
			if (hHint == SWT.DEFAULT) hHint = DEFAULT_HEIGHT;
		}
	}
	int width = OS.GTK_WIDGET_WIDTH (handle);
	int height = OS.GTK_WIDGET_HEIGHT (handle);
	int labelWidth = 0, labelHeight = 0;
	if (labelHandle != 0) {
		labelWidth = OS.GTK_WIDGET_WIDTH (labelHandle);
		labelHeight = OS.GTK_WIDGET_HEIGHT (labelHandle);
		OS.gtk_widget_set_size_request (labelHandle, wHint, hHint);
	}
	GtkRequisition requisition = new GtkRequisition ();
	if (frameHandle != 0) {
		int frameWidth = OS.GTK_WIDGET_WIDTH (frameHandle);
		int frameHeight = OS.GTK_WIDGET_HEIGHT (frameHandle);
		OS.gtk_widget_set_size_request (frameHandle, -1, -1);
		OS.gtk_widget_set_size_request (handle, wHint, hHint);
		OS.gtk_widget_size_request (frameHandle, requisition);
		OS.gtk_widget_set_size_request (frameHandle, frameWidth, frameHeight);
	} else {
		OS.gtk_widget_set_size_request (handle, wHint, hHint);
		OS.gtk_widget_size_request (handle, requisition);
	}
	if (labelHandle != 0) {
		OS.gtk_widget_set_size_request (labelHandle, labelWidth, labelHeight);
	}
	OS.gtk_widget_set_size_request (handle, width, height);
	return new Point (requisition.width, requisition.height);	
}

void createHandle (int index) {
	state |= HANDLE;
	fixedHandle = OS.gtk_fixed_new ();
	if (fixedHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_fixed_set_has_window (fixedHandle, true);
	if ((style & SWT.SEPARATOR) != 0) {
		if ((style & SWT.HORIZONTAL)!= 0) {
			handle = OS.gtk_hseparator_new ();
		} else {
			handle = OS.gtk_vseparator_new ();
		}
		OS.gtk_widget_show (handle);
	} else {
		handle = OS.gtk_hbox_new (false, 0);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		labelHandle = OS.gtk_label_new_with_mnemonic (null);
		if (labelHandle == 0) error (SWT.ERROR_NO_HANDLES);
		imageHandle = OS.gtk_image_new ();
		if (imageHandle == 0) error (SWT.ERROR_NO_HANDLES);
		OS.gtk_container_add (handle, labelHandle);
		OS.gtk_container_add (handle, imageHandle);
		OS.gtk_widget_show (handle);
		OS.gtk_widget_show (labelHandle);		
	}
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	if ((style & SWT.BORDER) != 0) {
		frameHandle = OS.gtk_frame_new (null);
		if (frameHandle == 0) error (SWT.ERROR_NO_HANDLES);
		OS.gtk_container_add (fixedHandle, frameHandle);
		OS.gtk_container_add (frameHandle, handle);
		OS.gtk_widget_show (frameHandle);
		// CHECK THEME
		OS.gtk_frame_set_shadow_type (frameHandle, OS.GTK_SHADOW_ETCHED_IN);
	} else {
		OS.gtk_container_add (fixedHandle, handle);
	}
	int parentHandle = parent.parentingHandle ();
	OS.gtk_container_add (parentHandle, fixedHandle);
	OS.gtk_widget_show (fixedHandle);
	if ((style & SWT.SEPARATOR) != 0) return;
	if ((style & SWT.WRAP) != 0) OS.gtk_label_set_line_wrap (labelHandle, true);
	if ((style & SWT.LEFT) != 0) {
		OS.gtk_misc_set_alignment (labelHandle, 0.0f, 0.0f);
		OS.gtk_label_set_justify (labelHandle, OS.GTK_JUSTIFY_LEFT);
		OS.gtk_misc_set_alignment (imageHandle, 0.0f, 0.5f);
		return;
	}
	if ((style & SWT.CENTER) != 0) {
		OS.gtk_misc_set_alignment (labelHandle, 0.5f, 0.0f);
		OS.gtk_label_set_justify (labelHandle, OS.GTK_JUSTIFY_CENTER);
		OS.gtk_misc_set_alignment (imageHandle, 0.5f, 0.5f);
		return;
	}
	if ((style & SWT.RIGHT) != 0) {
		OS.gtk_misc_set_alignment (labelHandle, 1.0f, 0.0f);
		OS.gtk_label_set_justify (labelHandle, OS.GTK_JUSTIFY_RIGHT);
		OS.gtk_misc_set_alignment (imageHandle, 1.0f, 0.5f);
		return;
	}
}

void createWidget (int index) {
	super.createWidget (index);
	text = "";
}

void deregister () {
	super.deregister ();
	if (frameHandle != 0) display.removeWidget (frameHandle);
	if (labelHandle != 0) display.removeWidget (labelHandle);
	if (imageHandle != 0) display.removeWidget (imageHandle);
}

int eventHandle () {
	return fixedHandle;
}

/**
 * Returns a value which describes the position of the
 * text or image in the receiver. The value will be one of
 * <code>LEFT</code>, <code>RIGHT</code> or <code>CENTER</code>
 * unless the receiver is a <code>SEPARATOR</code> label, in 
 * which case, <code>NONE</code> is returned.
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
	if ((style & SWT.SEPARATOR) != 0) return 0;
	if ((style & SWT.LEFT) != 0) return SWT.LEFT;
	if ((style & SWT.CENTER) != 0) return SWT.CENTER;
	if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
	return SWT.LEFT;
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

String getNameText () {
	return getText ();
}

/**
 * Returns the receiver's text, which will be an empty
 * string if it has never been set or if the receiver is
 * a <code>SEPARATOR</code> label.
 *
 * @return the receiver's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText () {
	checkWidget ();
	if ((style & SWT.SEPARATOR) != 0) return "";
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
	if (result) {
		Composite control = this.parent;
		while (control != null) {
			Control [] children = control._getChildren ();
			int index = 0;
			while (index < children.length) {
				if (children [index] == this) break;
				index++;
			}
			index++;
			if (index < children.length) {
				if (children [index].setFocus ()) return result;
			}
			control = control.parent;
		}
	}
	return result;
}

boolean mnemonicMatch (char key) {
	if (labelHandle == 0) return false;
	return mnemonicMatch (labelHandle, key);
}

void register () {
	super.register ();
	if (frameHandle != 0) display.addWidget (frameHandle, this);
	if (labelHandle != 0) display.addWidget (labelHandle, this);
	if (imageHandle != 0) display.addWidget (imageHandle, this);
}

void releaseHandle () {
	super.releaseHandle ();
	frameHandle = imageHandle = labelHandle = 0;
}

void releaseWidget () {
	super.releaseWidget ();
	image = null;
	text = null;
}

void resizeHandle (int width, int height) {
	OS.gtk_widget_set_size_request (fixedHandle, width, height);
	int widgetHandle = frameHandle != 0 ? frameHandle : handle;
	OS.gtk_widget_set_size_request (widgetHandle, width, height);

	/*
	* Feature in GTK.  Some widgets do not allocate the size
	* of their internal children in gtk_widget_size_allocate().
	* Instead this is done in gtk_widget_size_request().  This
	* means that the client area of the widget is not correct.
	* The fix is to call gtk_widget_size_request() (and throw
	* the results away).
	*
	* Note: The following widgets rely on this feature:
	* 	GtkScrolledWindow
	* 	GtkNotebook
	* 	GtkFrame
	* 	GtkCombo
	*/
	GtkRequisition requisition = new GtkRequisition ();
	OS.gtk_widget_size_request (widgetHandle, requisition);
}

/**
 * Controls how text and images will be displayed in the receiver.
 * The argument should be one of <code>LEFT</code>, <code>RIGHT</code>
 * or <code>CENTER</code>.  If the receiver is a <code>SEPARATOR</code>
 * label, the argument is ignored and the alignment is not changed.
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
	if ((style & SWT.SEPARATOR) != 0) return;
	if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) return;
	style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	style |= alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	boolean isRTL = (style & SWT.RIGHT_TO_LEFT) != 0;
	if ((style & SWT.LEFT) != 0) {
		OS.gtk_misc_set_alignment (labelHandle, 0.0f, 0.0f);
		OS.gtk_label_set_justify (labelHandle, isRTL ? OS.GTK_JUSTIFY_RIGHT : OS.GTK_JUSTIFY_LEFT);
		OS.gtk_misc_set_alignment (imageHandle, 0.0f, 0.5f);
		return;
	}
	if ((style & SWT.CENTER) != 0) {
		OS.gtk_misc_set_alignment (labelHandle, 0.5f, 0.0f);
		OS.gtk_label_set_justify (labelHandle, OS.GTK_JUSTIFY_CENTER);
		OS.gtk_misc_set_alignment (imageHandle, 0.5f, 0.5f);
		return;
	}
	if ((style & SWT.RIGHT) != 0) {
		OS.gtk_misc_set_alignment (labelHandle, 1.0f, 0.0f);
		OS.gtk_label_set_justify (labelHandle, isRTL ? OS.GTK_JUSTIFY_LEFT : OS.GTK_JUSTIFY_RIGHT);
		OS.gtk_misc_set_alignment (imageHandle, 1.0f, 0.5f);
		return;
	}
}

void setBackgroundColor (GdkColor color) {
	super.setBackgroundColor (color);
	setBackgroundColor(fixedHandle, color);
	if (labelHandle != 0) setBackgroundColor(labelHandle, color);
	if (imageHandle != 0) setBackgroundColor(imageHandle, color);
}

boolean setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
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
	boolean fixWrap = resize && labelHandle != 0 && (style & SWT.WRAP) != 0;
	if (fixWrap) OS.gtk_widget_set_size_request (labelHandle, -1, -1);
	boolean changed = super.setBounds (x, y, width, height, move, resize);
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
	if (fixWrap) {
		int labelWidth = OS.GTK_WIDGET_WIDTH (handle);
		int labelHeight = OS.GTK_WIDGET_HEIGHT (handle);
		OS.gtk_widget_set_size_request (labelHandle, labelWidth, labelHeight);
		int widgetHandle = frameHandle != 0 ? frameHandle : handle;
		GtkRequisition requisition = new GtkRequisition ();
		OS.gtk_widget_size_request (widgetHandle, requisition);
	}
	return changed;
}

void setFontDescription (int font) {
	super.setFontDescription (font);
	if (labelHandle != 0) OS.gtk_widget_modify_font (labelHandle, font);
	if (imageHandle != 0) OS.gtk_widget_modify_font (imageHandle, font);
}

void setForegroundColor (GdkColor color) {
	super.setForegroundColor (color);
	OS.gtk_widget_modify_fg (fixedHandle,  OS.GTK_STATE_NORMAL, color);
	if (labelHandle != 0) OS.gtk_widget_modify_fg (labelHandle,  OS.GTK_STATE_NORMAL, color);
	if (imageHandle != 0) OS.gtk_widget_modify_fg (imageHandle,  OS.GTK_STATE_NORMAL, color);
}

void setOrientation () {
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		OS.gtk_widget_set_direction (handle, OS.GTK_TEXT_DIR_RTL);
		if (labelHandle != 0) OS.gtk_widget_set_direction (labelHandle, OS.GTK_TEXT_DIR_RTL);
		if (imageHandle != 0) OS.gtk_widget_set_direction (imageHandle, OS.GTK_TEXT_DIR_RTL);
		if ((style & SWT.LEAD) != 0) {
			if (labelHandle != 0) OS.gtk_label_set_justify (labelHandle, OS.GTK_JUSTIFY_RIGHT);
		}
		if ((style & SWT.TRAIL) != 0) {
			if (labelHandle != 0) OS.gtk_label_set_justify (labelHandle, OS.GTK_JUSTIFY_LEFT);
		}
	}
}

/**
 * Sets the receiver's image to the argument, which may be
 * null indicating that no image should be displayed.
 *
 * @param image the image to display on the receiver (may be null)
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
	if ((style & SWT.SEPARATOR) != 0) return;
	this.image = image;
	if (image != null) {
		OS.gtk_image_set_from_pixmap (imageHandle, image.pixmap, image.mask);
		OS.gtk_widget_hide (labelHandle);
		OS.gtk_widget_show (imageHandle);
	} else {
		OS.gtk_image_set_from_pixmap (imageHandle, 0, 0);
		OS.gtk_widget_show (labelHandle);
		OS.gtk_widget_hide (imageHandle);
	}
}

/**
 * Sets the receiver's text.
 * <p>
 * This method sets the widget label.  The label may include
 * the mnemonic character and line delimiters.
 * </p>
 * <p>
 * Mnemonics are indicated by an '&amp' that causes the next
 * character to be the mnemonic.  When the user presses a
 * key sequence that matches the mnemonic, focus is assigned
 * to the control that follows the label. On most platforms,
 * the mnemonic appears underlined but may be emphasised in a
 * platform specific manner.  The mnemonic indicator character
 *'&amp' can be escaped by doubling it in the string, causing
 * a single '&amp' to be displayed.
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
public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	text = string;
	if ((style & SWT.SEPARATOR) != 0) return;
	char [] chars = fixMnemonic (string);
	byte [] buffer = Converter.wcsToMbcs (null, chars, true);
	OS.gtk_label_set_text_with_mnemonic (labelHandle, buffer);
	OS.gtk_widget_hide (imageHandle);
	OS.gtk_widget_show (labelHandle);
}

}

package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

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
 * <dd>SEPARATOR, HORIZONTAL, SHADOW_IN, SHADOW_OUT, VERTICAL</dd>
 * <dd>CENTER, LEFT, RIGHT, WRAP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of SHADOW_IN and SHADOW_OUT may be specified. Only
 * one of HORIZONTAL and VERTICAL may be specified. Only one of CENTER,
 * LEFT and RIGHT may be specified.
 * </p><p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */

public class Label extends Control {
	int frameHandle;
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
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
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
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Label (Composite parent, int style) {
	super (parent, checkStyle (style));
}

static int checkStyle (int style) {
	if ((style & SWT.SEPARATOR) != 0) return style;
	return checkBits (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	if ((style & SWT.SEPARATOR) != 0) {
		if ((style & SWT.HORIZONTAL) != 0) {
			if (wHint == SWT.DEFAULT) wHint = DEFAULT_WIDTH;
		} else {
			if (hHint == SWT.DEFAULT) hHint = DEFAULT_HEIGHT;
		}
	}
	// FIXME
	// If the wHint is set, and the label has a border, then
	// the GtkLabel will believe it has more width at its disposal
	// than it actually does (by a few pixels).
	// It is possible (but unlikely) that this will cause it to
	// answer a smaller height.
	int width = OS.GTK_WIDGET_WIDTH (fixedHandle);
	int height = OS.GTK_WIDGET_HEIGHT (fixedHandle);
	OS.gtk_widget_set_size_request (frameHandle, -1, -1);
	OS.gtk_widget_set_size_request (handle, wHint, hHint);
	GtkRequisition requisition = new GtkRequisition ();
	OS.gtk_widget_size_request (frameHandle, requisition);
	OS.gtk_widget_set_size_request (frameHandle, width, height);
	OS.gtk_widget_set_size_request (handle, width, height);
	width = wHint == SWT.DEFAULT ? requisition.width : wHint;
	height = hHint == SWT.DEFAULT ? requisition.height : hHint;
	return new Point (width, height);	
}

void createHandle (int index) {
	state |= HANDLE;
	fixedHandle = OS.gtk_fixed_new ();
	if (fixedHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_fixed_set_has_window (fixedHandle, true);
	frameHandle = OS.gtk_frame_new(null);
	if (frameHandle == 0) error (SWT.ERROR_NO_HANDLES);
	if ((style & SWT.SEPARATOR) != 0) {
		if ((style & SWT.HORIZONTAL)!= 0) {
			handle = OS.gtk_hseparator_new ();
		} else {
			handle = OS.gtk_vseparator_new ();
		}
	} else {
		handle = OS.gtk_label_new (null);
	}
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	int parentHandle = parent.parentingHandle ();
	OS.gtk_container_add(parentHandle, fixedHandle);
	OS.gtk_container_add(fixedHandle, frameHandle);
	OS.gtk_container_add(frameHandle, handle);
	OS.gtk_widget_show (fixedHandle);
	OS.gtk_widget_show (frameHandle);
	OS.gtk_widget_show (handle);

	// CHECK THEME
	int type = (style & SWT.BORDER) != 0 ? OS.GTK_SHADOW_ETCHED_IN : OS.GTK_SHADOW_NONE;	
	OS.gtk_frame_set_shadow_type (frameHandle, type);
	if ((style & SWT.SEPARATOR) != 0) return;
	if ((style & SWT.WRAP) != 0) OS.gtk_label_set_line_wrap (handle, true);
	if ((style & SWT.LEFT) != 0) {
		OS.gtk_misc_set_alignment (handle, 0.0f, 0.0f);
		OS.gtk_label_set_justify (handle, OS.GTK_JUSTIFY_LEFT);
		return;
	}
	if ((style & SWT.CENTER) != 0) {
		OS.gtk_misc_set_alignment (handle, 0.5f, 0.0f);
		OS.gtk_label_set_justify (handle, OS.GTK_JUSTIFY_CENTER);
		return;
	}
	if ((style & SWT.RIGHT) != 0) {
		OS.gtk_misc_set_alignment (handle, 1.0f, 0.0f);
		OS.gtk_label_set_justify (handle, OS.GTK_JUSTIFY_RIGHT);
		return;
	}
}

void createWidget (int index) {
	super.createWidget (index);
	text = "";
}

void register () {
	super.register ();
	WidgetTable.put (frameHandle, this);
}

void deregister () {
	super.deregister ();
	WidgetTable.remove (frameHandle);
}

int eventHandle () {
	return fixedHandle;
}

void releaseWidget () {
	super.releaseWidget ();
	image = null;
	text = null;
}

void releaseHandle () {
	super.releaseHandle ();
	frameHandle = 0;
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
	boolean isText = OS.GTK_WIDGET_TYPE (handle) == OS.gtk_label_get_type ();
	if ((style & SWT.LEFT) != 0) {
		OS.gtk_misc_set_alignment (handle, 0.0f, 0.0f);
		if (isText) OS.gtk_label_set_justify (handle, OS.GTK_JUSTIFY_LEFT);
		return;
	}
	if ((style & SWT.CENTER) != 0) {
		OS.gtk_misc_set_alignment (handle, 0.5f, 0.0f);
		if (isText) OS.gtk_label_set_justify (handle, OS.GTK_JUSTIFY_CENTER);
		return;
	}
	if ((style & SWT.RIGHT) != 0) {
		OS.gtk_misc_set_alignment (handle, 1.0f, 0.0f);
		if (isText) OS.gtk_label_set_justify (handle, OS.GTK_JUSTIFY_RIGHT);
		return;
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
	this.image = image;
	if ((style & SWT.SEPARATOR) != 0) return;
	//NOT IMPLEMENTED - events and state of handle lost
	WidgetTable.remove (handle);
	OS.gtk_widget_destroy (handle);
	if (image == null) {
		handle = OS.gtk_label_new (null);
	} else {
		handle = OS.gtk_pixmap_new (image.pixmap, image.mask);
	}
	OS.gtk_container_add (frameHandle, handle);
	WidgetTable.put (handle, this);
	int alignment = style & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	switch (alignment) {
		case SWT.LEFT: OS.gtk_misc_set_alignment (handle, 0.0f, 0.0f); break;
		case SWT.CENTER: OS.gtk_misc_set_alignment (handle, 0.5f, 0.0f); break;
		case SWT.RIGHT: OS.gtk_misc_set_alignment (handle, 1.0f, 0.0f); break;
	}
	OS.gtk_widget_show (handle);
}

/**
 * Sets the receiver's text.
 * <p>
 * This method sets the widget label.  The label may include
 * the mnemonic characters and line delimiters.
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
	if ((style & SWT.SEPARATOR) != 0) return;
	text = string;
	boolean isText = OS.GTK_WIDGET_TYPE (handle) == OS.gtk_label_get_type ();
	if (!isText) {
		//NOT IMPLEMENTED - events and state of handle lost
		WidgetTable.remove (handle);
		OS.gtk_widget_destroy (handle);
		handle = OS.gtk_label_new (null);
		OS.gtk_container_add (frameHandle, handle);
		WidgetTable.put (handle, this);
		int alignment = style & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
		switch (alignment) {
		case SWT.LEFT:
			OS.gtk_misc_set_alignment (handle, 0.0f, 0.0f);
			OS.gtk_label_set_justify (handle, OS.GTK_JUSTIFY_LEFT);
			break;
		case SWT.CENTER:
			OS.gtk_misc_set_alignment (handle, 0.5f, 0.0f);
			OS.gtk_label_set_justify (handle, OS.GTK_JUSTIFY_CENTER);
			break;
		case SWT.RIGHT:
			OS.gtk_misc_set_alignment (handle, 1.0f, 0.0f);
			OS.gtk_label_set_justify (handle, OS.GTK_JUSTIFY_RIGHT);
			break;
		}
	}
	int length = string.length ();
	char [] text = new char [length + 1];
	string.getChars (0, length, text, 0);
	for (int i=0; i<length; i++) {
		if (text [i] == '&') text [i] = '_';
	}
	byte [] buffer = Converter.wcsToMbcs (null, text);
	OS.gtk_label_set_text_with_mnemonic (handle, buffer);
}

void resizeHandle (int width, int height) {
	int topHandle = topHandle ();
	int flags = OS.GTK_WIDGET_FLAGS (topHandle);
	OS.GTK_WIDGET_SET_FLAGS(topHandle, OS.GTK_VISIBLE);
	OS.gtk_widget_set_size_request(handle, -1,-1);
	GtkAllocation alloc = new GtkAllocation();
	alloc.width = width;
	alloc.height = height;
	OS.gtk_widget_size_allocate(frameHandle, alloc);
	int w = OS.GTK_WIDGET_WIDTH(handle), h = OS.GTK_WIDGET_HEIGHT(handle);
	OS.gtk_widget_set_size_request (fixedHandle, width, height);
	OS.gtk_widget_set_size_request (frameHandle, width, height);
	OS.gtk_widget_set_size_request (handle, w, h);
	//FIXME - causes scrollbar problems when button child of table
	int parentHandle = parent.parentingHandle ();
	Display display = getDisplay ();
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.gtk_container_resize_children (parentHandle);
	display.setWarnings (warnings);
	if ((flags & OS.GTK_VISIBLE) == 0) {
		OS.GTK_WIDGET_UNSET_FLAGS(topHandle, OS.GTK_VISIBLE);	
	}
}

}

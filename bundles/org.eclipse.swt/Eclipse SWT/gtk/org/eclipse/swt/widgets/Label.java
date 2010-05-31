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
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class represent a non-selectable
 * user interface object that displays a string or image.
 * When SEPARATOR is specified, displays a single
 * vertical or horizontal line.
 * <p>
 * Shadow styles are hints and may not be honored
 * by the platform.  To create a separator label
 * with the default shadow style for the platform,
 * do not specify a shadow style.
 * </p>
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
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#label">Label snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Label extends Control {
	int /*long*/ frameHandle, labelHandle, imageHandle;
	ImageList imageList;
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

void addRelation (Control control) {
	if (!control.isDescribedByLabel ()) return;
	if (labelHandle == 0) return;
	control._getAccessible().addRelation(ACC.RELATION_LABELLED_BY, _getAccessible());
	control.labelRelation = this;
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
	Point size; 
	/* 
	* Feature in GTK. GTK has a predetermined maximum width for wrapping text. 
	* The fix is to use pango layout directly instead of the label size request 
	* to calculate its preferred size.
	*/
	boolean fixWrap = labelHandle != 0 && (style & SWT.WRAP) != 0 && (OS.GTK_WIDGET_FLAGS (labelHandle) & OS.GTK_VISIBLE) != 0;
	if (fixWrap || frameHandle != 0) forceResize ();
	if (fixWrap) {
		int /*long*/ labelLayout = OS.gtk_label_get_layout (labelHandle);
		int pangoWidth = OS.pango_layout_get_width (labelLayout);
		if (wHint != SWT.DEFAULT) {
			OS.pango_layout_set_width (labelLayout, wHint * OS.PANGO_SCALE);
		} else {
			OS.pango_layout_set_width (labelLayout, -1);
		}
		int [] w = new int [1], h = new int [1];
		OS.pango_layout_get_size (labelLayout, w, h);
		OS.pango_layout_set_width (labelLayout, pangoWidth);
		if (frameHandle != 0) {
			int [] labelWidth = new int [1], labelHeight = new int [1];
			OS.gtk_widget_get_size_request (labelHandle, labelWidth, labelHeight);
			OS.gtk_widget_set_size_request (labelHandle, 1, 1);
			size = computeNativeSize (frameHandle, -1, -1, changed);
			OS.gtk_widget_set_size_request (labelHandle, labelWidth [0], labelHeight [0]);
			size.x = size.x - 1;
			size.y = size.y - 1;
		} else { 
			size = new Point (0,0);
		}
		size.x += wHint == SWT.DEFAULT ? OS.PANGO_PIXELS(w [0]) : wHint;
		size.y += hHint == SWT.DEFAULT ? OS.PANGO_PIXELS(h [0]) : hHint;
	} else {
		if (frameHandle != 0) {
			int [] reqWidth = new int [1], reqHeight = new int [1];
			OS.gtk_widget_get_size_request (handle, reqWidth, reqHeight);
			OS.gtk_widget_set_size_request (handle, wHint, hHint);
			size = computeNativeSize (frameHandle, -1, -1, changed);
			OS.gtk_widget_set_size_request (handle, reqWidth [0], reqHeight [0]);
		} else {
			size = computeNativeSize (handle, wHint, hHint, changed);
		}
	}
	/*
	* Feature in GTK.  Instead of using the font height to determine
	* the preferred height of the widget, GTK uses the text metrics.
	* The fix is to ensure that the preferred height is at least as
	* tall as the font height.
	* 
	* NOTE: This work around does not fix the case when there are
	* muliple lines of text.
	*/
	if (hHint == SWT.DEFAULT && labelHandle != 0) {
		int /*long*/ layout = OS.gtk_label_get_layout (labelHandle);
		int /*long*/ context = OS.pango_layout_get_context (layout);
		int /*long*/ lang = OS.pango_context_get_language (context);
		int /*long*/ font = getFontDescription ();
		int /*long*/ metrics = OS.pango_context_get_metrics (context, font, lang);
		int ascent = OS.PANGO_PIXELS (OS.pango_font_metrics_get_ascent (metrics));
		int descent = OS.PANGO_PIXELS (OS.pango_font_metrics_get_descent (metrics));
		OS.pango_font_metrics_unref (metrics);
		int fontHeight = ascent + descent;
		int [] buffer = new int [1];
		OS.g_object_get (labelHandle, OS.ypad, buffer, 0);
		fontHeight += 2 * buffer [0];
		if (frameHandle != 0) {
			int /*long*/ style = OS.gtk_widget_get_style (frameHandle);
			fontHeight += 2 * OS.gtk_style_get_ythickness (style);
			fontHeight += 2 * OS.gtk_container_get_border_width (frameHandle);
		}
		size.y = Math.max (size.y, fontHeight);
	}
	return size;
}

void createHandle (int index) {
	state |= HANDLE | THEME_BACKGROUND;
	fixedHandle = OS.g_object_new (display.gtk_fixed_get_type (), 0);
	if (fixedHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_fixed_set_has_window (fixedHandle, true);
	if ((style & SWT.SEPARATOR) != 0) {
		if ((style & SWT.HORIZONTAL)!= 0) {
			handle = OS.gtk_hseparator_new ();
		} else {
			handle = OS.gtk_vseparator_new ();
		}
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	} else {
		handle = OS.gtk_hbox_new (false, 0);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		labelHandle = OS.gtk_label_new_with_mnemonic (null);
		if (labelHandle == 0) error (SWT.ERROR_NO_HANDLES);
		imageHandle = OS.gtk_image_new ();
		if (imageHandle == 0) error (SWT.ERROR_NO_HANDLES);
		OS.gtk_container_add (handle, labelHandle);
		OS.gtk_container_add (handle, imageHandle);
	}
	if ((style & SWT.BORDER) != 0) {
		frameHandle = OS.gtk_frame_new (null);
		if (frameHandle == 0) error (SWT.ERROR_NO_HANDLES);
		OS.gtk_container_add (fixedHandle, frameHandle);
		OS.gtk_container_add (frameHandle, handle);
		OS.gtk_frame_set_shadow_type (frameHandle, OS.GTK_SHADOW_ETCHED_IN);
	} else {
		OS.gtk_container_add (fixedHandle, handle);
	}
	if ((style & SWT.SEPARATOR) != 0) return;
	if ((style & SWT.WRAP) != 0) {
		OS.gtk_label_set_line_wrap (labelHandle, true);
		if (OS.GTK_VERSION >= OS.VERSION (2, 10, 0)) {
			OS.gtk_label_set_line_wrap_mode (labelHandle, OS.PANGO_WRAP_WORD_CHAR);
		}
	}
	setAlignment ();
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

int /*long*/ eventHandle () {
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

public int getBorderWidth () {
	checkWidget();
	if (frameHandle != 0) {
		return OS.gtk_style_get_xthickness (OS.gtk_widget_get_style (frameHandle));
	}
	return 0;
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
		OS.g_signal_connect_closure_by_id (labelHandle, display.signalIds [MNEMONIC_ACTIVATE], 0, display.closures [MNEMONIC_ACTIVATE], false);
	}
}

boolean isDescribedByLabel () {
	return false;
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
	if (imageList != null) imageList.dispose ();
	imageList = null;
	image = null;
	text = null;
}

void resizeHandle (int width, int height) {
	OS.gtk_widget_set_size_request (fixedHandle, width, height);
	OS.gtk_widget_set_size_request (frameHandle != 0 ? frameHandle : handle, width, height);
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
	setAlignment ();
}

void setAlignment () {
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

void setBackgroundColor (GdkColor color) {
	super.setBackgroundColor (color);
	setBackgroundColor(fixedHandle, color);
	if (labelHandle != 0) setBackgroundColor(labelHandle, color);
	if (imageHandle != 0) setBackgroundColor(imageHandle, color);
}

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
	boolean fixWrap = resize && labelHandle != 0 && (style & SWT.WRAP) != 0;
	if (fixWrap) OS.gtk_widget_set_size_request (labelHandle, -1, -1);
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
	if (fixWrap) {
		int labelWidth = OS.GTK_WIDGET_WIDTH (handle);
		int labelHeight = OS.GTK_WIDGET_HEIGHT (handle);
		OS.gtk_widget_set_size_request (labelHandle, labelWidth, labelHeight);
		/*
		* Bug in GTK.  Setting the size request should invalidate the label's
		* layout, but it does not.  The fix is to resize the label directly. 
		*/
		GtkRequisition requisition = new GtkRequisition ();
		OS.gtk_widget_size_request (labelHandle, requisition);
		GtkAllocation allocation = new GtkAllocation ();
		allocation.x = OS.GTK_WIDGET_X (labelHandle);
		allocation.y = OS.GTK_WIDGET_Y (labelHandle);
		allocation.width = labelWidth;
		allocation.height = labelHeight;
		OS.gtk_widget_size_allocate (labelHandle, allocation);
	}
	return result;
}

void setFontDescription (int /*long*/ font) {
	super.setFontDescription (font);
	if (labelHandle != 0) OS.gtk_widget_modify_font (labelHandle, font);
	if (imageHandle != 0) OS.gtk_widget_modify_font (imageHandle, font);
}

void setForegroundColor (GdkColor color) {
	super.setForegroundColor (color);
	setForegroundColor (fixedHandle, color);
	if (labelHandle != 0) setForegroundColor (labelHandle, color);
	if (imageHandle != 0) setForegroundColor (imageHandle, color);
}

void setOrientation () {
	super.setOrientation ();
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		if (labelHandle != 0) OS.gtk_widget_set_direction (labelHandle, OS.GTK_TEXT_DIR_RTL);
		if (imageHandle != 0) OS.gtk_widget_set_direction (imageHandle, OS.GTK_TEXT_DIR_RTL);
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
	if (imageList != null) imageList.dispose ();
	imageList = null;
	if (image != null) {
		imageList = new ImageList ();
		int imageIndex = imageList.add (image);
		int /*long*/ pixbuf = imageList.getPixbuf (imageIndex);
		OS.gtk_image_set_from_pixbuf (imageHandle, pixbuf);
		OS.gtk_widget_hide (labelHandle);
		OS.gtk_widget_show (imageHandle);
	} else {
		OS.gtk_image_set_from_pixbuf (imageHandle, 0);
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
 * Mnemonics are indicated by an '&amp;' that causes the next
 * character to be the mnemonic.  When the user presses a
 * key sequence that matches the mnemonic, focus is assigned
 * to the control that follows the label. On most platforms,
 * the mnemonic appears underlined but may be emphasised in a
 * platform specific manner.  The mnemonic indicator character
 * '&amp;' can be escaped by doubling it in the string, causing
 * a single '&amp;' to be displayed.
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
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	text = string;
	char [] chars = fixMnemonic (string);
	byte [] buffer = Converter.wcsToMbcs (null, chars, true);
	OS.gtk_label_set_text_with_mnemonic (labelHandle, buffer);
	OS.gtk_widget_hide (imageHandle);
	OS.gtk_widget_show (labelHandle);
}

void showWidget () {
	super.showWidget ();
	if (frameHandle != 0) OS.gtk_widget_show (frameHandle);
	if (labelHandle != 0) OS.gtk_widget_show (labelHandle);
}
}

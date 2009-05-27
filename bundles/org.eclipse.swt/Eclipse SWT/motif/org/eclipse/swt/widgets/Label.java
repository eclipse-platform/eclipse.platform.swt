/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;

import org.eclipse.swt.*;
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
	int formHandle;
	String text = "";
	Image image, bitmap, disabled;

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
void _setText (String string) {
	/*
	* Bug in Motif.  The widget will not receive mouse events, if the
	* label string is empty.  The fix is to detect that and set a single
	* space instead. 
	*/
	if (string.length () == 0) string = " ";

	/* Strip out mnemonic marker symbols, and remember the mnemonic. */
	char [] unicode = new char [string.length ()];
	string.getChars (0, unicode.length, unicode, 0);
	int mnemonic = fixMnemonic (unicode);
	
	/* Wrap the text if necessary, and convert to mbcs. */
	byte [] buffer = null;
	if ((style & SWT.WRAP) != 0) {
		int [] argList = {
			OS.XmNwidth, 0,        /* 1 */
			OS.XmNmarginWidth, 0,  /* 3 */
		};
		OS.XtGetValues (handle, argList, argList.length / 2);
		int width = argList [1] - argList [3] * 2;
		if (mnemonic != 0) string = new String (unicode);
		string = display.wrapText (string, font, width);
		buffer = Converter.wcsToMbcs (getCodePage (), string, true);
	} else {
		buffer = Converter.wcsToMbcs (getCodePage (), unicode, true);
	}
	
	int xmString = OS.XmStringGenerate(buffer, null, OS.XmCHARSET_TEXT, null);
	if (xmString == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
		
	/*
	* Bug in Solaris.  If a mnemonic is defined to be a character
	* that appears in a string in a position that follows a '\n',
	* Solaris segment faults.  For example, a label with text
	* "Hello\nthe&re" would GP since "r" appears after '\n'.
	*
	* The fix is to remove mnemonics from labels that contain
	* '\n', which is fine since such labels generally just act
	* as descriptive texts anyways.
	*/ 
	if (mnemonic == 0 || string.indexOf ('\n') != -1) {
		mnemonic = OS.XK_VoidSymbol;
	}
	int [] argList = {
		OS.XmNlabelType, OS.XmSTRING,
		OS.XmNlabelString, xmString,
		OS.XmNmnemonic, mnemonic,
	};
	OS.XtSetValues (handle, argList, argList.length / 2);
	if (xmString != 0) OS.XmStringFree (xmString);
}
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int border = getBorderWidth ();
	int width = border * 2, height = border * 2;
	if ((style & SWT.SEPARATOR) != 0) {
		if ((style & SWT.HORIZONTAL) != 0) {
			width += DEFAULT_WIDTH;  height += 3;
		} else {
			width += 3; height += DEFAULT_HEIGHT;
		}
		if (wHint != SWT.DEFAULT) width = wHint + border * 2;
		if (hHint != SWT.DEFAULT) height = hHint + border * 2;
		return new Point (width, height);
	}
	int [] argList1 = {OS.XmNlabelType, 0};
	OS.XtGetValues (handle, argList1, argList1.length / 2);
	int labelType = argList1 [1];
	if (labelType == OS.XmSTRING && (style & SWT.WRAP) != 0) {
		/* If we are wrapping text, calculate the height based on wHint. */
		int [] argList2 = {
			OS.XmNmarginTop, 0,     /* 1 */
			OS.XmNmarginBottom, 0,  /* 3 */
			OS.XmNmarginHeight, 0,  /* 5 */
			OS.XmNmarginWidth, 0,   /* 7 */
			OS.XmNmarginLeft, 0,    /* 9 */
			OS.XmNmarginRight, 0,   /* 11 */
		};
		OS.XtGetValues (handle, argList2, argList2.length / 2);
		String string = text;
		if (wHint != SWT.DEFAULT) {
			string = display.wrapText (string, font, wHint - (argList2 [7] * 2) - argList2 [9] - argList2 [11]);
		}
		GC gc = new GC (this);
		Point extent = gc.textExtent (string);
		gc.dispose ();
		height = extent.y + argList2 [1] + argList2 [3] + (argList2 [5] * 2) + (border * 2);
		if (wHint == SWT.DEFAULT) {
			width += extent.x + (argList2 [7] * 2) + argList2 [9] + argList2 [11];
		}
	} else {
		/* If we are not wrapping, ask the widget for its geometry. */
		XtWidgetGeometry result = new XtWidgetGeometry ();
		result.request_mode = OS.CWWidth | OS.CWHeight;
		OS.XtQueryGeometry (handle, null, result);
		width += result.width;
		height += result.height;
	}

	/*
	* Feature in Motif. If a label's labelType is XmSTRING but 
	* the label string is empty, recomputing the size will
	* not take into account the height of the font, as we would
	* like it to. Take care of this case.
	* 
	* Note:  When the label string is empty a single space is set
	* into the widget. So the preferred height is computed properly.
	* Just make sure the preferred width is zero.
	*/
	if (labelType == OS.XmSTRING && text.length () == 0) {
		width = 0;
	}
	if (wHint != SWT.DEFAULT) {
		int [] argList3 = {
			OS.XmNmarginWidth, 0,  /* 1 */
			OS.XmNmarginLeft, 0,   /* 3 */
			OS.XmNmarginRight, 0,  /* 5 */
		};
		OS.XtGetValues (handle, argList3, argList3.length / 2);
		width = wHint + (border * 2) + (argList3 [1] * 2) + argList3 [3] + argList3 [5];
	}
	if (hHint != SWT.DEFAULT) {
		int [] argList4 = {
			OS.XmNmarginHeight, 0,  /* 1 */
			OS.XmNmarginTop, 0,     /* 3 */
			OS.XmNmarginBottom, 0,  /* 5 */
		};
		OS.XtGetValues (handle, argList4, argList4.length / 2);
		height = hHint + (border * 2) + (argList4 [1] * 2) + argList4 [3] + argList4 [5];
	}
	return new Point (width, height);
}
void createHandle (int index) {
	state |= THEME_BACKGROUND;
	int parentHandle = parent.handle;
	int borderWidth = (style & SWT.BORDER) != 0 ? 1 : 0;
	if ((style & SWT.SEPARATOR) != 0) {
		int separatorType = separatorType ();
		int orientation = (style & SWT.HORIZONTAL) != 0 ? OS.XmHORIZONTAL : OS.XmVERTICAL;
		int [] argList = {
			OS.XmNancestorSensitive, 1,
			OS.XmNborderWidth, borderWidth,
			OS.XmNorientation, orientation,
			OS.XmNseparatorType, separatorType,
		};
		handle = OS.XmCreateSeparator (parentHandle, null, argList, argList.length / 2);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		return;
	}
	int [] argList1 = {
		OS.XmNancestorSensitive, 1,
		OS.XmNmarginWidth, 0,
		OS.XmNmarginHeight, 0, 
		OS.XmNresizePolicy, OS.XmRESIZE_NONE,
		OS.XmNborderWidth, borderWidth,
	};
	formHandle = OS.XmCreateForm (parentHandle, null, argList1, argList1.length / 2);
	if (formHandle == 0) error (SWT.ERROR_NO_HANDLES);
	int alignment = OS.XmALIGNMENT_BEGINNING;
	if ((style & SWT.CENTER) != 0) alignment = OS.XmALIGNMENT_CENTER;
	if ((style & SWT.RIGHT) != 0) alignment = OS.XmALIGNMENT_END;
	int [] argList2 = {
		OS.XmNalignment, alignment,
		OS.XmNtopAttachment, OS.XmATTACH_FORM,
		OS.XmNleftAttachment, OS.XmATTACH_FORM,
		OS.XmNrightAttachment, OS.XmATTACH_FORM,
	};
	/*
	* Bug in Motif.  The widget will not receive mouse events, if the
	* label string is empty.  The fix is to initialize it to a space.
	*/
	byte [] buffer = {(byte) ' ', 0};
	handle = OS.XmCreateLabel (formHandle, buffer, argList2, argList2.length / 2);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	int [] argList3 = {OS.XmNbackground, 0};
	OS.XtGetValues (handle, argList3, argList3.length / 2);
	OS.XtSetValues (formHandle, argList3, argList3.length / 2);
}
int defaultBackground () {
	return display.labelBackground;
}
Font defaultFont () {
	return display.labelFont;
}
int defaultForeground () {
	return display.labelForeground;
}
void deregister () {
	super.deregister ();
	if (formHandle != 0) display.removeWidget (formHandle);
}
void enableWidget (boolean enabled) {
	super.enableWidget (enabled);
	if (formHandle != 0) enableHandle (enabled, formHandle);
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
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) return SWT.LEFT;
	int [] argList = {OS.XmNalignment, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int alignment = argList [1];
	if (alignment == OS.XmALIGNMENT_BEGINNING) return SWT.LEFT;
	if (alignment == OS.XmALIGNMENT_CENTER) return SWT.CENTER;
	if (alignment == OS.XmALIGNMENT_END)return SWT.RIGHT;
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
	checkWidget();
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
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) return "";
	return text;
}
void hookEvents () {
	super.hookEvents ();
	if (formHandle != 0) {
		int windowProc = display.windowProc;
		OS.XtAddEventHandler (formHandle, OS.ButtonPressMask, false, windowProc, BUTTON_PRESS);
		OS.XtAddEventHandler (formHandle, OS.ButtonReleaseMask, false, windowProc, BUTTON_RELEASE);
		OS.XtAddEventHandler (formHandle, OS.PointerMotionMask, false, windowProc, POINTER_MOTION);
		OS.XtAddEventHandler (formHandle, OS.EnterWindowMask, false, windowProc, ENTER_WINDOW);
		OS.XtAddEventHandler (formHandle, OS.LeaveWindowMask, false, windowProc, LEAVE_WINDOW);
		OS.XtInsertEventHandler (formHandle, OS.ExposureMask, false, windowProc, EXPOSURE, OS.XtListTail);
	}
}
void manageChildren () {
	if (formHandle != 0) {
		OS.XtSetMappedWhenManaged (formHandle, false);
		OS.XtManageChild (formHandle);
	}
	super.manageChildren ();
	if (formHandle != 0) {
		int [] argList = {OS.XmNborderWidth, 0};
		OS.XtGetValues (formHandle, argList, argList.length / 2);
		OS.XtResizeWidget (formHandle, 1, 1, argList [1]);
		OS.XtSetMappedWhenManaged (formHandle, true);
	}
}
boolean mnemonicHit (char key) {
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
			if (children [index].setFocus ()) return true;
		}
		control = control.parent;
	}
	return false;
}
boolean mnemonicMatch (char key) {
	char mnemonic = findMnemonic (getText ());
	if (mnemonic == '\0') return false;
	return Character.toUpperCase (key) == Character.toUpperCase (mnemonic);
}
void propagateWidget (boolean enabled) {
	super.propagateWidget (enabled);
	if (formHandle != 0) propagateHandle (enabled, formHandle, OS.None);
}
void realizeChildren () {
	super.realizeChildren ();
	setBitGravity ();
}
void redrawWidget (int x, int y, int width, int height, boolean redrawAll, boolean allChildren, boolean trim) {
	super.redrawWidget (x, y, width, height, redrawAll, allChildren, trim);
	if (formHandle != 0) { 
		short [] root_x = new short [1], root_y = new short [1];
		OS.XtTranslateCoords (handle, (short) x, (short) y, root_x, root_y);
		short [] label_x = new short [1], label_y = new short [1];
		OS.XtTranslateCoords (formHandle, (short) 0, (short) 0, label_x, label_y);
		redrawHandle (root_x [0] - label_x [0], root_y [0] - label_y [0], width, height, redrawAll, formHandle);
	}
}
void register () {
	super.register ();
	if (formHandle != 0) display.addWidget (formHandle, this);
}
void releaseHandle () {
	super.releaseHandle ();
	formHandle = 0;
}
void releaseWidget () {
	super.releaseWidget ();
	int [] argList = {
		OS.XmNlabelPixmap, OS.XmUNSPECIFIED_PIXMAP,
		OS.XmNlabelInsensitivePixmap, OS.XmUNSPECIFIED_PIXMAP,
	};
	OS.XtSetValues (handle, argList, argList.length / 2);
	if (bitmap != null) bitmap.dispose ();
	if (disabled != null) disabled.dispose ();
	image = bitmap = disabled = null; 
}
int separatorType () {
	if ((style & (SWT.SHADOW_IN)) != 0) return OS.XmSHADOW_ETCHED_IN;
	if ((style & (SWT.SHADOW_OUT)) != 0) return OS.XmSHADOW_ETCHED_OUT;
	return OS.XmSINGLE_LINE;
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
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) return;
	int [] argList = {OS.XmNalignment, OS.XmALIGNMENT_BEGINNING};
	if ((alignment & SWT.CENTER) != 0) argList [1] = OS.XmALIGNMENT_CENTER;
	if ((alignment & SWT.RIGHT) != 0) argList [1] = OS.XmALIGNMENT_END;
	OS.XtSetValues (handle, argList, argList.length / 2);
	setBitGravity ();
}
void setBackgroundPixel (int pixel) {
	super.setBackgroundPixel (pixel);
	if (formHandle != 0) {
		int [] argList1 = {OS.XmNforeground, 0, OS.XmNhighlightColor, 0};
		OS.XtGetValues (formHandle, argList1, argList1.length / 2);
		OS.XmChangeColor (formHandle, pixel);
		OS.XtSetValues (formHandle, argList1, argList1.length / 2);
	}
	int [] argList2 = {OS.XmNlabelType, 0};
	OS.XtGetValues (handle, argList2, argList2.length / 2);
	if (argList2 [1] == OS.XmPIXMAP) setBitmap (image);
}
void setBitGravity () {
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return;
	int xWindow = OS.XtWindow (handle);
	if (xWindow == 0) return;
	int flags = OS.CWBitGravity;
	XSetWindowAttributes attributes = new XSetWindowAttributes ();
	attributes.bit_gravity = (style & SWT.LEFT) != 0 ? OS.NorthWestGravity : OS.ForgetGravity;
	OS.XChangeWindowAttributes (xDisplay, xWindow, flags, attributes);
}
void setBitmap (Image image) {
	int labelPixmap = OS.XmUNSPECIFIED_PIXMAP;
	int labelInsensitivePixmap = OS.XmUNSPECIFIED_PIXMAP;
	if (bitmap != null) bitmap.dispose ();
	if (disabled != null) disabled.dispose ();
	bitmap = disabled = null;
	if (image != null) {
		if (image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		switch (image.type) {
			case SWT.BITMAP:
				ImageData data;
				if (image.mask == 0	&& (data = image.getImageData ()).alpha == -1 && data.alphaData == null && data.transparentPixel == -1) {
					labelPixmap = image.pixmap;
					disabled = new Image (display, image, SWT.IMAGE_DISABLE);
					labelInsensitivePixmap = disabled.pixmap;
					break;
				}
				//FALL THROUGH
			case SWT.ICON:
				Rectangle rect = image.getBounds ();
				bitmap = new Image (display, rect.width, rect.height);
				GC gc = new GC (bitmap);
				gc.setBackground (getBackground ());
				gc.fillRectangle (rect);
				gc.drawImage (image, 0, 0);
				gc.dispose ();
				labelPixmap = bitmap.pixmap;
				disabled = new Image (display, bitmap, SWT.IMAGE_DISABLE);
				labelInsensitivePixmap = disabled.pixmap;
				break;
			default:
				error (SWT.ERROR_NOT_IMPLEMENTED);
		}
	}
	int [] argList = {
		OS.XmNlabelType, image == null ? OS.XmSTRING : OS.XmPIXMAP,
		OS.XmNlabelPixmap, labelPixmap,
		OS.XmNlabelInsensitivePixmap, labelInsensitivePixmap,
	};
	OS.XtSetValues (handle, argList, argList.length / 2);
}
boolean setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	boolean changed = super.setBounds (x, y, width, height, move, resize);
	if (changed && resize && (style & SWT.WRAP) != 0) {
		int [] argList = {OS.XmNlabelType, 0,};
		OS.XtGetValues (handle, argList, argList.length / 2);
		if (argList [1] == OS.XmSTRING) _setText (text);
	} 
	return changed;
}
public void setFont (Font font) {
	checkWidget();
	
	/*
	* Bug in Motif. Setting the font in a label widget that does
	* not have a non-empty string causes GP on UTF-8 locale.
	* The fix is to set a non-empty string, change the font,
	* and restore the empty string at the end. 
	*/
	int [] argList1 = {OS.XmNlabelString, 0, OS.XmNlabelType, 0};
	OS.XtGetValues (handle, argList1, argList1.length / 2);
	boolean fixString = OS.IsDBLocale && OS.XmStringEmpty (argList1 [1]); 
	if (fixString) {
		byte[] buffer = Converter.wcsToMbcs (getCodePage (), "string", true);
		int xmString = OS.XmStringCreateLocalized (buffer);	
		int [] argList2 = { 
			OS.XmNlabelType, OS.XmSTRING,
			OS.XmNlabelString, xmString,
		};
		OS.XtSetValues (handle, argList2, argList2.length / 2);
		OS.XmStringFree (xmString);
	}
	super.setFont (font);
	if (fixString) OS.XtSetValues (handle, argList1, argList1.length / 2);	
	if ((style & SWT.WRAP) != 0) {
		int [] argList = {OS.XmNlabelType, 0,};
		OS.XtGetValues (handle, argList, argList.length / 2);
		if (argList [1] == OS.XmSTRING) _setText (text);
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
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) return;
	setBitmap (this.image = image);
}
void setParentBackground () {
	super.setParentBackground ();
	if (formHandle != 0) setParentBackground (formHandle);
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
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	/*
	* Feature in Motif.  Motif does not optimize the case
	* when the same text is set into a label causing
	* it to flash.  The fix is to test for equality and
	* do nothing.
	*/
	if (text.equals (string)) return;
	_setText (text = string);
}
int topHandle () {
	if (formHandle != 0) return formHandle;
	return handle;
}
}

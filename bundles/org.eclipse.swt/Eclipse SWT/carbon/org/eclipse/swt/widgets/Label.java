package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.carbon.*;

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
public /*final*/ class Label extends Control {
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
	if ((style & SWT.SEPARATOR) != 0) return style;
	return checkBits (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
}
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int border = getBorderWidth ();
	int width = 0, height = 0;
	
	if ((style & SWT.SEPARATOR) != 0) {
		if ((style & SWT.HORIZONTAL) != 0) {
			width += DEFAULT_WIDTH;
			height += 3;
		} else {
			width += 3;
			height += DEFAULT_HEIGHT;
		}
	} else {
		if (image != null) {
			Rectangle r = image.getBounds();
			width= r.width;
			height= r.height;
		} else {
			short[] bounds= new short[2];
			short[] baseLine= new short[1];
			boolean wrap= false;
			if ((style & SWT.WRAP) != 0 && wHint != SWT.DEFAULT) {
				wrap= true;
				bounds[1]= (short) wHint;	// If we are wrapping text, calculate the height based on wHint.
			}
			int sHandle= OS.CFStringCreateWithCharacters(removeMnemonics(text));
					
			GC gc= new GC(this);
			gc.installFont();
			OS.GetThemeTextDimensions(sHandle, OS.kThemeCurrentPortFont, OS.kThemeStateActive, wrap, bounds, baseLine);
			gc.dispose();
			
			OS.CFRelease(sHandle);
			width = bounds[1];
			height = bounds[0];
		}
		/*
		* Feature in Motif. If a label's labelType is XmSTRING but it
		* has no label set into it yet, recomputing the size will
		* not take into account the height of the font, as we would
		* like it to. Take care of this case.
		*/
		/* AW
		if (text.length () == 0) {
			height += getFontHeight ();
			width = 0;
		}
		*/
	}
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	return new Point (width + 2*border, height + 2*border);
}
void createHandle (int index) {
	state |= HANDLE;
	int parentHandle = parent.handle;
	int borderWidth = (style & SWT.BORDER) != 0 ? 1 : 0;
	if ((style & SWT.SEPARATOR) != 0) {
  		handle = MacUtil.createSeparator(parentHandle, style);
	} else {
		handle = MacUtil.createDrawingArea(parentHandle, 0, 0, borderWidth);
	}
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
}
int defaultBackground () {
	return getDisplay ().labelBackground;
}
Font defaultFont () {
	return getDisplay ().labelFont;
}
int defaultForeground () {
	return getDisplay ().labelForeground;
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
	if ((style & SWT.CENTER) != 0)
		return SWT.CENTER;
	if ((style & SWT.RIGHT) != 0)
		return SWT.RIGHT;
	return SWT.LEFT;
}
public int getBorderWidth () {
	checkWidget();
	return (style & SWT.BORDER) != 0 ? 1 : 0;
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
	Display display= getDisplay();		
	OS.SetControlData(handle, OS.kControlEntireControl, OS.kControlUserPaneDrawProcTag, display.fUserPaneDrawProc);
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
int processPaint (Object callData) {
	if ((style & SWT.SEPARATOR) != 0) return 0;
	
	int hndl= topHandle();
	Display display= getDisplay();
	
	MacRect bounds= new MacRect();
	OS.GetControlBounds(hndl, bounds.getData());

	int w= bounds.getWidth();
	int h= bounds.getHeight();
	int borderWidth = (style & SWT.BORDER) != 0 ? 1 : 0;

	GC gc= new GC(this);
	gc.carbon_focus();
	
	gc.fillRectangle(0, 0, w, h);
	
	if (image != null) {
		Rectangle imageBounds= image.getBounds();
		gc.drawImage(image, (w-imageBounds.width) / 2, (h-imageBounds.height) / 2);
	} else {
		int sHandle= OS.CFStringCreateWithCharacters(removeMnemonics(text));
		boolean wrap= (style & SWT.WRAP) != 0;
		short just= 0;
		if ((style & SWT.RIGHT) != 0)
			just= 2;
		else if ((style & SWT.CENTER) != 0)
			just= 1;
		if (OS.IsControlEnabled(handle))
			OS.RGBForeColor(0x000000);
		else
			OS.RGBForeColor(0x808080);
		gc.installFont();
		bounds.set(bounds.getX()+borderWidth, bounds.getY()+borderWidth, w-2*borderWidth, h-2*borderWidth);
		OS.DrawThemeTextBox(sHandle, OS.kThemeCurrentPortFont, OS.kThemeStateActive, wrap, bounds.getData(), just, 0);
		OS.CFRelease(sHandle);
	}
	
	if (borderWidth > 0) {
		gc.setForeground(display.getSystemColor(SWT.COLOR_GRAY));
		gc.drawRectangle(0, 0, w-1, h-1);
	}
	
	gc.carbon_unfocus();
	gc.dispose();
	
	return 0;
}
void propagateWidget (boolean enabled) {
	super.propagateWidget (enabled);
	/*
	* Labels never participate in focus traversal when
	* either enabled or disabled.
	*/
	/* AW
	if (enabled) {
		int [] argList = {OS.XmNtraversalOn, 0};
		OS.XtSetValues (handle, argList, argList.length / 2);
	}
	*/
}
void releaseWidget () {
	super.releaseWidget ();
    /* AW
	int [] argList = {
		OS.XmNlabelPixmap, OS.XmUNSPECIFIED_PIXMAP,
		OS.XmNlabelInsensitivePixmap, OS.XmUNSPECIFIED_PIXMAP,
	};
	OS.XtSetValues (handle, argList, argList.length / 2);
    */
	if (bitmap != null) bitmap.dispose ();
	if (disabled != null) disabled.dispose ();
	image = bitmap = disabled = null;
}
/* AW
int separatorType () {
	if ((style & (SWT.SHADOW_IN)) != 0) return OS.XmSHADOW_ETCHED_IN;
	if ((style & (SWT.SHADOW_OUT)) != 0) return OS.XmSHADOW_ETCHED_OUT;
	return OS.XmSHADOW_ETCHED_IN;
}
*/
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
	int mask= SWT.LEFT | SWT.CENTER | SWT.RIGHT;
	int newAlignment= alignment & mask;
	int currentAlignment= style & mask;
	if (currentAlignment != newAlignment) {
		style &= ~mask;
		style |= newAlignment;
		redrawWidget (0, 0, 0, 0, false);
	}
}
/* AW
void setBackgroundPixel (int pixel) {
	super.setBackgroundPixel (pixel);
	int [] argList = {OS.XmNlabelType, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (argList [1] == OS.XmPIXMAP) setBitmap (image);
}
*/
void setBitmap (Image image) {
	/* AW
	int labelPixmap = OS.XmUNSPECIFIED_PIXMAP;
	int labelInsensitivePixmap = OS.XmUNSPECIFIED_PIXMAP;
	*/
	if (bitmap != null) bitmap.dispose ();
	if (disabled != null) disabled.dispose ();
	bitmap = disabled = null;
	if (image != null) {
		if (image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		Display display = getDisplay ();
		switch (image.type) {
			case SWT.BITMAP:
				/* AW
				labelPixmap = image.pixmap;
				*/
				disabled = new Image (display, image, SWT.IMAGE_DISABLE);
				/* AW
				labelInsensitivePixmap = disabled.pixmap;
				*/
				break;
			case SWT.ICON:
				Rectangle rect = image.getBounds ();
				bitmap = new Image (display, rect.width, rect.height);
				GC gc = new GC (bitmap);
				gc.setBackground (getBackground ());
				gc.fillRectangle (rect);
				gc.drawImage (image, 0, 0);
				gc.dispose ();
				/* AW
				labelPixmap = bitmap.pixmap;
				*/
				disabled = new Image (display, bitmap, SWT.IMAGE_DISABLE);
				/* AW
				labelInsensitivePixmap = disabled.pixmap;
				*/
				break;
			default:
				error (SWT.ERROR_NOT_IMPLEMENTED);
		}
	}
	/* AW
	int [] argList = {
		OS.XmNlabelType, OS.XmPIXMAP,
		OS.XmNlabelPixmap, labelPixmap,
		OS.XmNlabelInsensitivePixmap, labelInsensitivePixmap,
	};
	OS.XtSetValues (handle, argList, argList.length / 2);
	*/
	redrawWidget (0, 0, 0, 0, false);
}
public void setBounds (int x, int y, int width, int height) {
	super.setBounds (x, y, width, height);
	if ((style & SWT.WRAP) != 0) setText (text);
}
public void setFont (Font font) {
	super.setFont (font);
	if ((style & SWT.WRAP) != 0) setText (text);
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
	setBitmap (this.image = image);
}
public void setSize (int width, int height) {
	super.setSize (width, height);
	if ((style & SWT.WRAP) != 0) setText (text);
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

	/* Strip out mnemonic marker symbols, and remember the mnemonic. */
	char [] unicode = new char [string.length ()];
	string.getChars (0, unicode.length, unicode, 0);
	int i=0, j=0, mnemonic=0;
	while (i < unicode.length) {
		if ((unicode [j++] = unicode [i++]) == Mnemonic) {
			if (i == unicode.length) {continue;}
			if (unicode [i] == Mnemonic) {i++; continue;}
			if (mnemonic == 0) mnemonic = unicode [i];
			j--;
		}
	}
	while (j < unicode.length) unicode [j++] = 0;

	redrawWidget (0, 0, 0, 0, false);
}
}

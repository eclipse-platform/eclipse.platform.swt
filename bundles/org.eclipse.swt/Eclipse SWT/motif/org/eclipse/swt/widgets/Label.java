package org.eclipse.swt.widgets;

/*
* Licensed Materials - Property of IBM,
* SWT - The Simple Widget Toolkit,
* (c) Copyright IBM Corp 1998, 1999.
*/

/* Imports */
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
*	A label is a non-selectable user interface object
* that represents displays a static value.
*
* <b>Styles</b><br>
* <dd>SEPARATOR, HORIZONTAL, VERTICAL, SHADOW_IN, SHADOW_OUT<br>
*/

/* Class Definition */
public /*final*/ class Label extends Control {
	Image image, bitmap, disabled;
/**
* Creates a new instance of the widget.
*/
public Label (Composite parent, int style) {
	super (parent, checkStyle (style));
}
static int checkStyle (int style) {
	if ((style & SWT.SEPARATOR) != 0) return style;
	return checkBits (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
}
/**
* Computes the preferred size.
*/
public Point computeSize (int wHint, int hHint, boolean changed) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int border = getBorderWidth ();
	int width = border * 2, height = border * 2;
	if ((style & SWT.SEPARATOR) != 0) {
		if ((style & SWT.HORIZONTAL) != 0) {
			width += DEFAULT_WIDTH;  height += 3;
		} else {
			width += 3; height += DEFAULT_HEIGHT;
		}
		if (wHint != SWT.DEFAULT) width = wHint + (border * 2);
		if (hHint != SWT.DEFAULT) height = hHint + (border * 2);
		return new Point (width, height);
	}
	XtWidgetGeometry result = new XtWidgetGeometry ();
	result.request_mode = OS.CWWidth | OS.CWHeight;
	int [] argList2 = {OS.XmNrecomputeSize, 1};
	OS.XtSetValues(handle, argList2, argList2.length / 2);
	OS.XtQueryGeometry (handle, null, result);
	int [] argList3 = {OS.XmNrecomputeSize, 0};
	OS.XtSetValues(handle, argList3, argList3.length / 2);
	width += result.width;
	height += result.height;
	/**
	 * Feature in Motif. If a button's labelType is XmSTRING but it
	 * has no label set into it yet, recomputing the size will
	 * not take into account the height of the font, as we would
	 * like it to. Take care of this case.
	 */
	int [] argList = {OS.XmNlabelType, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (argList [1] == OS.XmSTRING) {
		int [] argList1 = {OS.XmNlabelString, 0};
		OS.XtGetValues (handle, argList1, argList1.length / 2);
		int xmString = argList1 [1];
		if (OS.XmStringEmpty (xmString)) {
			height += getFontHeight ();
			width = 0;
		}
	}
	if (wHint != SWT.DEFAULT) width = wHint + (border * 2);
	if (hHint != SWT.DEFAULT) height = hHint + (border * 2);
	return new Point (width, height);
}
void createHandle (int index) {
	state |= HANDLE;
	int borderWidth = (style & SWT.BORDER) != 0 ? 1 : 0;
	if ((style & SWT.SEPARATOR) != 0) {
		int separatorType = separatorType ();
		int orientation = (style & SWT.HORIZONTAL) != 0 ? OS.XmHORIZONTAL : OS.XmVERTICAL;
		int [] argList = {
			OS.XmNborderWidth, borderWidth,
			OS.XmNorientation, orientation,
			OS.XmNseparatorType, separatorType,
		};
		handle = OS.XmCreateSeparator (parent.handle, null, argList, argList.length / 2);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		return;
	}
	int alignment = OS.XmALIGNMENT_BEGINNING;
	if ((style & SWT.CENTER) != 0) alignment = OS.XmALIGNMENT_CENTER;
	if ((style & SWT.RIGHT) != 0) alignment = OS.XmALIGNMENT_END;
	int [] argList = {
		OS.XmNrecomputeSize, 0,
		OS.XmNalignment, alignment,
		OS.XmNborderWidth, borderWidth,
	};
	handle = OS.XmCreateLabel (parent.handle, null, argList, argList.length / 2);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
}
int defaultBackground () {
	return getDisplay ().labelBackground;
}
int defaultFont () {
	return getDisplay ().labelFont;
}
int defaultForeground () {
	return getDisplay ().labelForeground;
}
/**
* Gets the alignment.
* <p>
* @return the alignment
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public int getAlignment () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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
* Gets the widget image.
* <p>
* @return the widget image
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public Image getImage () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return image;
}
String getNameText () {
	return getText ();
}
/**
* Gets the widget text.
* <p>
* @return the widget text
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public String getText () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & SWT.SEPARATOR) != 0) return "";
	int [] argList = {OS.XmNlabelString, 0, OS.XmNmnemonic, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int xmString = argList [1], mnemonic = argList [3];
	if (xmString == 0) error (SWT.ERROR_CANNOT_GET_TEXT);
	char [] result = null;
	int [] parseTable = getDisplay ().parseTable;
	int address = OS.XmStringUnparse (
		xmString,
		null,
		OS.XmCHARSET_TEXT,
		OS.XmCHARSET_TEXT,
		parseTable,
		parseTable.length,
		OS.XmOUTPUT_ALL);
	if (address != 0) {
		int length = OS.strlen (address);
		byte [] buffer = new byte [length];
		OS.memmove (buffer, address, length);
		OS.XtFree (address);
		result = Converter.mbcsToWcs (null, buffer);
	}
	if (xmString != 0) OS.XmStringFree (xmString);
	int count = 0;
	if (mnemonic != 0) count++;
	for (int i=0; i<result.length-1; i++)
		if (result [i] == Mnemonic) count++;
	char [] newResult = result;
	if ((count != 0) || (mnemonic != 0)) {
		newResult = new char [result.length + count];
		int i = 0, j = 0;
		while (i < result.length) {
			if ((mnemonic != 0) && (result [i] == mnemonic)) {
				if (j < newResult.length) newResult [j++] = Mnemonic;
				mnemonic = 0;
			}
			if ((newResult [j++] = result [i++]) == Mnemonic)
				if (j < newResult.length) newResult [j++] = Mnemonic;
		}
	}
	return new String (newResult);
}
boolean getWrap () {
	return false;
}
boolean mnemonicHit () {
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
void propagateHandle (boolean enabled, int widgetHandle) {
	super.propagateHandle (enabled, widgetHandle);
	/*
	* Labels never participate in focus traversal when
	* either enabled or disabled.
	*/
	if (enabled) {
		int [] argList = {OS.XmNtraversalOn, 0};
		OS.XtSetValues (widgetHandle, argList, argList.length / 2);
	}
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
	return OS.XmSHADOW_ETCHED_IN;
}
/**
* Sets the alignment.
* <p>
* @param aligment
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setAlignment (int alignment) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & SWT.SEPARATOR) != 0) return;
	int [] argList = {OS.XmNalignment, OS.XmALIGNMENT_BEGINNING};
	if ((alignment & SWT.CENTER) != 0) argList [1] = OS.XmALIGNMENT_CENTER;
	if ((alignment & SWT.RIGHT) != 0) argList [1] = OS.XmALIGNMENT_END;
	OS.XtSetValues (handle, argList, argList.length / 2);
}
void setBackgroundPixel (int pixel) {
	super.setBackgroundPixel (pixel);
	int [] argList = {OS.XmNlabelType, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (argList [1] == OS.XmPIXMAP) setBitmap (image);
}
void setBitmap (Image image) {
	int labelPixmap = OS.XmUNSPECIFIED_PIXMAP;
	int labelInsensitivePixmap = OS.XmUNSPECIFIED_PIXMAP;
	if (bitmap != null) bitmap.dispose ();
	if (disabled != null) disabled.dispose ();
	bitmap = disabled = null;
	if (image != null) {
		Display display = getDisplay ();
		switch (image.type) {
			case SWT.BITMAP:
				labelPixmap = image.pixmap;
				disabled = new Image (display, image, SWT.IMAGE_DISABLE);
				labelInsensitivePixmap = disabled.pixmap;
				break;
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
		OS.XmNlabelType, OS.XmPIXMAP,
		OS.XmNlabelPixmap, labelPixmap,
		OS.XmNlabelInsensitivePixmap, labelInsensitivePixmap,
	};
	OS.XtSetValues (handle, argList, argList.length / 2);
}
/**
* Sets the widget image.
* <p>
* @param image the widget image (or null)
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setImage (Image image) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	setBitmap (this.image = image);
}
/**
* Set the widget text.
*
* PARAMETERS
*
* 	string - the new label for the widget
*
* REMARKS
*
*	This method sets the widget label.  The label may include
* the mnemonic characters and line delimiters.
*
**/
/**
* Sets the widget text.
* <p>
* @param string the widget text
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when string is null
*/
public void setText (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	char [] text = new char [string.length ()];
	string.getChars (0, text.length, text, 0);
	int i=0, j=0, mnemonic=0;
	while (i < text.length) {
		if ((text [j++] = text [i++]) == Mnemonic) {
			if (i == text.length) {continue;}
			if (text [i] == Mnemonic) {i++; continue;}
			if (mnemonic == 0) mnemonic = text [i];
			j--;
		}
	}
	while (j < text.length) text [j++] = 0;
	byte [] buffer = Converter.wcsToMbcs (null, text, true);
	int [] parseTable = getDisplay ().parseTable;
	int xmString = OS.XmStringParseText (
		buffer,
		0,
		OS.XmFONTLIST_DEFAULT_TAG, 
		OS.XmCHARSET_TEXT, 
		parseTable,
		parseTable.length,
		0);
	if (xmString == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
	int [] argList = {
		OS.XmNlabelType, OS.XmSTRING,
		OS.XmNlabelString, xmString,
		OS.XmNmnemonic, mnemonic,
	};
	OS.XtSetValues (handle, argList, argList.length / 2);
	if (xmString != 0) OS.XmStringFree (xmString);
}
void setWrap (boolean wrap) {
	// NOT DONE
}
}

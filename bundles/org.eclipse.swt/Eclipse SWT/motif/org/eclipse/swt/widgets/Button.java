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
import org.eclipse.swt.events.*;

/**
*	The button class represents a selectable user interface object
* that issues notificiation when pressed and released. 
*
* <p>
* <b>Styles</b><br>
* <dd>ARROW, CHECK, PUSH, RADIO, TOGGLE<br>
* <br>
* <b>Events</b><br>
* <dd>Selection<br>
*/

/* Class Definition */
public /*final*/ class Button extends Control {
	Image image, bitmap, disabled;
	static final byte [] ARM_AND_ACTIVATE;
	static {
		String name = "ArmAndActivate";
		int length = name.length();
		char [] unicode = new char [length];
		name.getChars (0, length, unicode, 0);
		byte [] buffer = new byte [length + 1];
		for (int i = 0; i < length; i++) {
			buffer[i] = (byte) unicode[i];
		}
		ARM_AND_ACTIVATE = buffer;
	}
/**
* Creates a new instance of the widget.
*/
public Button (Composite parent, int style) {
	super (parent, checkStyle (style));
}
/**	 
* Adds the listener to receive events.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void addSelectionListener(SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
}
static int checkStyle (int style) {
	style = checkBits (style, SWT.PUSH, SWT.ARROW, SWT.CHECK, SWT.RADIO, SWT.TOGGLE, 0);
	if ((style & SWT.PUSH) != 0) {
		return checkBits (style, SWT.CENTER, SWT.LEFT, SWT.RIGHT, 0, 0, 0);
	}
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) != 0) {
		return checkBits (style, SWT.LEFT, SWT.RIGHT, SWT.CENTER, 0, 0, 0);
	}
	if ((style & SWT.ARROW) != 0) {
		return checkBits (style, SWT.UP, SWT.DOWN, SWT.LEFT, SWT.RIGHT, 0, 0);
	}
	return style;
}
void click () {
	OS.XtCallActionProc (handle, ARM_AND_ACTIVATE, new XAnyEvent (), null, 0);
}
/**
* Computes the preferred size.
*/
public Point computeSize (int wHint, int hHint, boolean changed) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int border = getBorderWidth ();
	int width = border * 2, height = border * 2;
	if ((style & SWT.ARROW) != 0) {
		Display display = getDisplay ();
		width += display.scrolledMarginX;
		height += display.scrolledMarginY;
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
		if (OS.XmStringEmpty (xmString)) height += getFontHeight ();
	}
	if (wHint != SWT.DEFAULT || hHint != SWT.DEFAULT) {	
		int [] argList4 = new int [] {OS.XmNmarginLeft, 0, OS.XmNmarginRight, 0, OS.XmNmarginTop, 0, OS.XmNmarginBottom, 0};
		OS.XtGetValues (handle, argList4, argList4.length / 2);
		if (wHint != SWT.DEFAULT) width = wHint + argList4 [1] + argList4 [3] + (border * 2);
		if (hHint != SWT.DEFAULT) height = hHint + argList4 [5] + argList4 [7] + (border * 2);
	}	
	return new Point (width, height);
}
void createHandle (int index) {
	state |= HANDLE;
	int borderWidth = (style & SWT.BORDER) != 0 ? 1 : 0;
	
	/* ARROW button */
	if ((style & SWT.ARROW) != 0) {
		int alignment = OS.XmARROW_UP;
		if ((style & SWT.UP) != 0) alignment = OS.XmARROW_UP;
		if ((style & SWT.DOWN) != 0) alignment = OS.XmARROW_DOWN;
		if ((style & SWT.LEFT) != 0) alignment = OS.XmARROW_LEFT;
		if ((style & SWT.RIGHT) != 0) alignment = OS.XmARROW_RIGHT;
		int [] argList = {
			OS.XmNtraversalOn, 0,
			OS.XmNarrowDirection, alignment,
			OS.XmNborderWidth, borderWidth,
		};
		handle = OS.XmCreateArrowButton (parent.handle, null, argList, argList.length / 2);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		if ((style & SWT.FLAT) != 0) {
			int [] argList1 = {OS.XmNshadowThickness, 1};
			OS.XtSetValues (handle, argList1, argList1.length / 2);
		}
		return;
	}

	/* Compute alignment */
	int alignment = OS.XmALIGNMENT_BEGINNING;
	if ((style & SWT.CENTER) != 0) alignment = OS.XmALIGNMENT_CENTER;
	if ((style & SWT.RIGHT) != 0) alignment = OS.XmALIGNMENT_END;
	
	/* TOGGLE button */
	if ((style & SWT.TOGGLE) != 0) {
		/*
		* Bug in Motif.  When XmNindicatorOn is set to false,
		* Motif doesn't reset the shadow thickness to give a
		* push button look.  The fix is to set the shadow
		* thickness when ever this resource is changed.
		*/
		int [] argList = {
			OS.XmNrecomputeSize, 0,
			OS.XmNindicatorOn, 0,
			OS.XmNshadowThickness, (style & SWT.FLAT) != 0 ? 1 : 2,
			OS.XmNalignment, alignment,
			OS.XmNborderWidth, borderWidth,
		};
		handle = OS.XmCreateToggleButton (parent.handle, null, argList, argList.length / 2);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		return;
	}
	
	/* CHECK or RADIO button */
	if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
		/*
		* Bug in Motif.  For some reason, a toggle button
		* with XmNindicatorType XmONE_OF_MANY must have this
		* value set at creation or the highlight color will
		* not be correct.  The fix is to set these values
		* on create.
		*/
		int indicatorType = OS.XmONE_OF_MANY;
		if ((style & SWT.CHECK) != 0) indicatorType = OS.XmN_OF_MANY;
		int [] argList = {
			OS.XmNrecomputeSize, 0,
			OS.XmNindicatorType, indicatorType,
			OS.XmNalignment, alignment,
			OS.XmNborderWidth, borderWidth,
		};
		handle = OS.XmCreateToggleButton (parent.handle, null, argList, argList.length / 2);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		return;
	}
	
	/* PUSH button */
	int [] argList = {
		OS.XmNrecomputeSize, 0,
		OS.XmNalignment, alignment,
		OS.XmNborderWidth, borderWidth,
	};
	handle = OS.XmCreatePushButton (parent.handle, null, argList, argList.length / 2);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	if ((style & SWT.FLAT) != 0) {
		int [] argList1 = {OS.XmNshadowThickness, 1};
		OS.XtSetValues (handle, argList1, argList1.length / 2);
	}
}
void createWidget (int index) {
	super.createWidget (index);
	if ((style & SWT.PUSH) == 0) return;
	if (getShell ().parent == null) return;
	int [] argList = new int [] {OS.XmNdefaultButtonShadowThickness, 1};
	OS.XtSetValues (handle, argList, argList.length / 2);
}
int defaultBackground () {
	return getDisplay ().buttonBackground;
}
int defaultFont () {
	return getDisplay ().buttonFont;
}
int defaultForeground () {
	return getDisplay ().buttonForeground;
}
/**
* Gets the alignment.
* <p>
*	This method returns the aligment of the button.
* If the button is an ARROW, the value indicates
* the direction of the arrow.
* <p>
* LEFT, RIGHT, UP, DOWN - when the button is an ARROW button.<br>
* LEFT, RIGHT, CENTER - when the button is any other style.
*
* @return the button alignment 
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public int getAlignment () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & SWT.ARROW) != 0) {
		int [] argList = {OS.XmNarrowDirection, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);
		int direction = argList [1];
		if (direction == OS.XmARROW_UP) return SWT.UP;
		if (direction == OS.XmARROW_DOWN) return SWT.DOWN;
		if (direction == OS.XmARROW_LEFT) return SWT.LEFT;
		if (direction == OS.XmARROW_RIGHT) return SWT.RIGHT;
		return SWT.UP;
	}
	int [] argList = {OS.XmNalignment, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int alignment = argList [1];
	if (alignment == OS.XmALIGNMENT_BEGINNING) return SWT.LEFT;
	if (alignment == OS.XmALIGNMENT_CENTER) return SWT.CENTER;
	if (alignment == OS.XmALIGNMENT_END)return SWT.RIGHT;
	return SWT.CENTER;
}
boolean getDefault () {
	if ((style & SWT.PUSH) == 0) return false;
	int [] argList = {OS.XmNshowAsDefault, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1] != 0;
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
* Gets the selection.
* <p>
*	This method gets the widget selection state for a
* widgets with the style CHECK, RADIO or TOGGLE.  It
* returns false for widgets that do not have one of
* these styles.
* true or false - for CHECK, RADIO or TOGGLE.
* false - for all other widget styles
*
* @return the selection state
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public boolean getSelection () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return false;
	int [] argList = {OS.XmNset, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1] != 0;
}
/**
* Gets the widget text.
* <p>
*	This method returns the button label.  The label may
* include the mnemonic character but must not contain line
* delimiters.
*
* @return the button text
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public String getText () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & SWT.ARROW) != 0) return "";
	int [] argList = {OS.XmNlabelString, 0, OS.XmNmnemonic, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int xmString = argList [1], mnemonic = argList [3];
	if (xmString == 0) error (SWT.ERROR_CANNOT_GET_TEXT);
	char [] result = null;	
	int address = OS.XmStringUnparse (
		xmString,
		null,
		OS.XmCHARSET_TEXT,
		OS.XmCHARSET_TEXT,
		null,
		0,
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
void hookEvents () {
	super.hookEvents ();
	int callback = OS.XmNactivateCallback;
	int windowProc = getDisplay ().windowProc;
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) != 0) callback = OS.XmNvalueChangedCallback;	
	OS.XtAddCallback (handle, callback, windowProc, SWT.Selection);
}
boolean mnemonicHit () {
	if (!setFocus ()) return false;
	click ();
	return true;
}
boolean mnemonicMatch (char key) {
	char mnemonic = findMnemonic (getText ());
	if (mnemonic == '\0') return false;
	return Character.toUpperCase (key) == Character.toUpperCase (mnemonic);
}
int processFocusIn () {
	super.processFocusIn ();
	if ((style & SWT.PUSH) == 0) return 0;
	getShell ().setDefaultButton (this, false);
	return 0;
}
int processFocusOut () {
	super.processFocusOut ();
	if ((style & SWT.PUSH) == 0) return 0;
	if (getDefault ()) {
		getShell ().setDefaultButton (null, false);
	}
	return 0;
}
int processSelection (int callData) {
	if ((style & SWT.RADIO) != 0) {
		if ((parent.getStyle () & SWT.NO_RADIO_GROUP) == 0) selectRadio ();
	}
	return super.processSelection (callData);
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
/**	 
* Removes the listener.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void removeSelectionListener(SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Selection, listener);
	eventTable.unhook(SWT.DefaultSelection,listener);	
}
void selectRadio () {
	Control [] children = parent._getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (this != child && child instanceof Button) {
			Button button = (Button) child;
			if ((button.getStyle () & SWT.RADIO) != 0) {
				if (button.getSelection ()) {
					button.setSelection (false);
					button.postEvent (SWT.Selection);
				}
			}
		}
	}
	setSelection (true);
}
/**
* Sets the alignment.
* <p>
*	This method sets the aligment of the button.
* If the button is an ARROW, the value indicated
* the direction of the arrow.
*
* @param alignment the button alignment or the direction 
*	of the arrow if the button is an ARROW
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setAlignment (int alignment) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & SWT.ARROW) != 0) {
		int [] argList = {OS.XmNarrowDirection, OS.XmARROW_UP};
		if ((alignment & SWT.UP) != 0) argList [1] = OS.XmARROW_UP;
		if ((alignment & SWT.DOWN) != 0) argList [1] = OS.XmARROW_DOWN;
		if ((alignment & SWT.LEFT) != 0) argList [1] = OS.XmARROW_LEFT;
		if ((alignment & SWT.RIGHT) != 0) argList [1] = OS.XmARROW_RIGHT;
		OS.XtSetValues (handle, argList, argList.length / 2);
		return;
	}
	if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) return;
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
void setDefault (boolean value) {
	if ((style & SWT.PUSH) == 0) return;
	if (getShell ().parent == null) return;
	int [] argList = {OS.XmNshowAsDefault, (value ? 1 : 0)};
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
* Sets the selection.
* <p>
*	This method Sets the widget selection state for a
* widgets with the style CHECK, RADIO or TOGGLE.  It
* returns false for widgets that do not have one of
* these styles.
*
* @param selected the new selection state
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setSelection (boolean selected) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return;
	int [] argList = {OS.XmNset, selected ? 1 : 0};
	OS.XtSetValues (handle, argList, argList.length / 2);
}
/**
* Sets the widget text.
* <p>
*	This method sets the button label.  The label may include
* the mnemonic character but must not contain line delimiters.
*
* @param string the desired label of the button
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
	if ((style & SWT.ARROW) != 0) return;
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
	int xmString = OS.XmStringParseText (
		buffer,
		0,
		OS.XmFONTLIST_DEFAULT_TAG, 
		OS.XmCHARSET_TEXT, 
		null,
		0,
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
}

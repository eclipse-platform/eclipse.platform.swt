package org.eclipse.swt.widgets;

/*
* Licensed Materials - Property of IBM,
* SWT - The Simple Widget Toolkit,
* (c) Copyright IBM Corp 1998, 1999.
*/

/* Imports */
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.*;

/**
*	The group class implements an etched border with that
* is typically used to group radio buttons.  A group can
* have an optional label.
* <p>
* <b>Styles</b><br>
* <dd>SHADOW_IN, SHADOW_OUT,<br>
* <dd>SHADOW_ETCHED_IN, SHADOW_ETCHED_OUT<br>
* <br>
*/

/* Class Definition */
public /*final*/ class Group extends Composite {
	int labelHandle;
/**
* Creates a new instance of the widget.
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
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}
public Rectangle computeTrim (int x, int y, int width, int height) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int trimX, trimY, trimWidth, trimHeight;	
	int [] argList = {
		OS.XmNwidth, 0, 
		OS.XmNheight, 0, 
		OS.XmNshadowThickness, 0, 
		OS.XmNmarginWidth, 0, 
		OS.XmNmarginHeight, 0
	};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int thickness = argList [5];
	int marginWidth = argList [7];
	int marginHeight = argList [9];
	trimX = x - marginWidth + thickness;
	trimY = y - marginHeight + thickness;
	trimWidth = width + ((marginWidth + thickness) * 2);
	trimHeight = height + ((marginHeight + thickness) * 2);
	if (OS.XtIsManaged (labelHandle)) {
		int [] argList2 = {OS.XmNy, 0, OS.XmNheight, 0};
		OS.XtGetValues (labelHandle, argList2, argList2.length / 2);
		int labelHeight = ((short) argList2 [1]) + argList2 [3];
		trimY = y - labelHeight;
		trimHeight = height + labelHeight + (marginHeight + thickness);
	}
	return new Rectangle (trimX, trimY, trimWidth, trimHeight);
}
void createHandle (int index) {
	state |= HANDLE;
	
	/*
	* Feature in Motif.  When a widget is managed or unmanaged,
	* it may request and be granted, a new size in the OS.  This
	* behavior is unwanted.  The fix is to create a parent for
	* the list that will disallow geometry requests.
	*/
	int border = (style & SWT.BORDER) != 0 ? 1 : 0;
	int [] argList1 = {OS.XmNborderWidth, border};
	formHandle = OS.XmCreateForm (parent.handle, null, argList1, argList1.length / 2);
	if (formHandle == 0) error (SWT.ERROR_NO_HANDLES);
	int [] argList2 = {
		OS.XmNshadowType, shadowType (),
		OS.XmNtopAttachment, OS.XmATTACH_FORM,
		OS.XmNbottomAttachment, OS.XmATTACH_FORM,
		OS.XmNleftAttachment, OS.XmATTACH_FORM,
		OS.XmNrightAttachment, OS.XmATTACH_FORM,
		OS.XmNresizable, 0,
	};
	handle = OS.XmCreateFrame (formHandle, null, argList2, argList2.length / 2);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	int [] argList3 = {OS.XmNchildType, OS.XmFRAME_TITLE_CHILD};
	labelHandle = OS.XmCreateLabel (handle, null, argList3, argList3.length / 2);
	if (labelHandle == 0) error (SWT.ERROR_NO_HANDLES);
}
void createWidget (int index) {
	super.createWidget (index);
	/*
	* Bug in Motif. For some reason, if a form has not been realized,
	* calling XtResizeWidget () on the form does not lay out properly.
	* The fix is to force the widget to be realized by forcing the shell
	* to be realized. 
	*/
	getShell ().realizeWidget ();
}
void enableWidget (boolean enabled) {
	super.enableWidget (enabled);
	enableHandle (enabled, labelHandle);
}
int fontHandle () {
	return labelHandle;
}
/**
* Gets the client area.
*/
public Rectangle getClientArea () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {
		OS.XmNwidth, 0, 
		OS.XmNheight, 0, 
		OS.XmNshadowThickness, 0, 
		OS.XmNmarginWidth, 0, 
		OS.XmNmarginHeight, 0
	};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int thickness = argList [5];
	int marginWidth = argList [7];
	int marginHeight = argList [9];
	int x = marginWidth + thickness;
	int y = marginHeight + thickness;
	int width = argList [1] - ((marginWidth + thickness) * 2) - 1;
	int height = argList [3] - ((marginHeight + thickness) * 2) - 1;
	if (OS.XtIsManaged (labelHandle)) {
		int [] argList2 = {OS.XmNy, 0, OS.XmNheight, 0};
		OS.XtGetValues (labelHandle, argList2, argList2.length / 2);
		y = ((short) argList2 [1]) + argList2 [3];
		height = argList [3] - y - (marginHeight + thickness) - 1;
	}
	return new Rectangle (x, y, width, height);
}
/**
* Gets the widget text.
* <p>
* @return the text
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public String getText () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNlabelString, 0};
	OS.XtGetValues (labelHandle, argList, 1);
	int xmString = argList [1];
	int address = OS.XmStringUnparse (
		xmString,
		null,
		OS.XmCHARSET_TEXT,
		OS.XmCHARSET_TEXT,
		null,
		0,
		OS.XmOUTPUT_ALL);
	if (address == 0) return "";
	int length = OS.strlen (address);
	byte [] buffer = new byte [length];
	OS.memmove (buffer, address, length);
	OS.XtFree (address);
	OS.XmStringFree (xmString);
	return new String (Converter.mbcsToWcs (null, buffer));
}
boolean mnemonicHit () {
	return setFocus ();
}
boolean mnemonicMatch (char key) {
	char mnemonic = findMnemonic (getText ());
	if (mnemonic == '\0') return false;
	return Character.toUpperCase (key) == Character.toUpperCase (mnemonic);
}
void propagateWidget (boolean enabled) {
	super.propagateWidget (enabled);
	propagateHandle (enabled, labelHandle);
}
void redrawWidget (int x, int y, int width, int height, boolean all) {
	super.redrawWidget (x, y, width, height, all);
	short [] root_x = new short [1], root_y = new short [1];
	OS.XtTranslateCoords (handle, (short) x, (short) y, root_x, root_y);
	short [] label_x = new short [1], label_y = new short [1];
	OS.XtTranslateCoords (labelHandle, (short) 0, (short) 0, label_x, label_y);
	redrawHandle (root_x [0] - label_x [0], root_y [0] - label_y [0], width, height, labelHandle);
}
void releaseHandle () {
	super.releaseHandle ();
	labelHandle = 0;
}
/**
* Sets the widget text.
* <p>
* @param string the string
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
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	int xmString = OS.XmStringParseText (
		buffer,
		0,
		OS.XmFONTLIST_DEFAULT_TAG, 
		OS.XmCHARSET_TEXT, 
		null,
		0,
		0);
	int [] argList = {OS.XmNlabelString, xmString};
	OS.XtSetValues (labelHandle, argList, argList.length / 2);
	OS.XmStringFree (xmString);
	if (string.length () == 0) {
		OS.XtUnmanageChild (labelHandle);
	} else {
		OS.XtManageChild (labelHandle);
	}
}
int shadowType () {
	if ((style & SWT.SHADOW_IN) != 0) return OS.XmSHADOW_IN;
	if ((style & SWT.SHADOW_OUT) != 0) return OS.XmSHADOW_OUT;
	if ((style & SWT.SHADOW_ETCHED_IN) != 0) return OS.XmSHADOW_ETCHED_IN;
	if ((style & SWT.SHADOW_ETCHED_OUT) != 0) return OS.XmSHADOW_ETCHED_OUT;
	return OS.XmSHADOW_ETCHED_IN;
}
}

package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.*;

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
	int labelHandle;

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
	style |= SWT.NO_FOCUS;
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
	checkWidget();
	int trimX, trimY, trimWidth, trimHeight;	
	int [] argList = {
		OS.XmNshadowThickness, 0, 
		OS.XmNmarginWidth, 0, 
		OS.XmNmarginHeight, 0
	};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int thickness = argList [1];
	int marginWidth = argList [3];
	int marginHeight = argList [5];
	int borderWidth = getBorderWidth ();
	trimX = x - marginWidth + thickness - borderWidth;
	trimY = y - marginHeight + thickness - borderWidth;
	trimWidth = width + ((marginWidth + thickness + borderWidth) * 2);
	trimHeight = height + ((marginHeight + thickness + borderWidth) * 2);
	if (OS.XtIsManaged (labelHandle)) {
		int [] argList2 = {OS.XmNy, 0, OS.XmNheight, 0, OS.XmNchildHorizontalSpacing, 0};
		OS.XtGetValues (labelHandle, argList2, argList2.length / 2);
		int titleHeight = ((short) argList2 [1]) + argList2 [3];
		trimY = y - titleHeight;
		trimHeight = height + titleHeight + (marginHeight + thickness + borderWidth);
		XtWidgetGeometry result = new XtWidgetGeometry ();
		OS.XtQueryGeometry (labelHandle, null, result);
		int titleWidth = result.width + 2 * (argList2 [5] + marginWidth + thickness + borderWidth);
		trimWidth = Math.max (trimWidth, titleWidth);
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
	int [] argList1 = {
		OS.XmNancestorSensitive, 1,
		OS.XmNborderWidth, border,
	};
	int parentHandle = parent.handle;
	formHandle = OS.XmCreateForm (parentHandle, null, argList1, argList1.length / 2);
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
	int [] argList3 = {OS.XmNframeChildType, OS.XmFRAME_TITLE_CHILD};
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
public Rectangle getClientArea () {
	checkWidget();
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
	return new String (Converter.mbcsToWcs (getCodePage (), buffer));
}
boolean mnemonicHit (char key) {
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
 * Sets the receiver's text, which is the string that will
 * be displayed as the receiver's <em>title</em>, to the argument,
 * which may not be null. 
 *
 * @param text the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
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
	byte [] buffer = Converter.wcsToMbcs (getCodePage (), text, true);
	int xmString = OS.XmStringParseText (
		buffer,
		0,
		OS.XmFONTLIST_DEFAULT_TAG, 
		OS.XmCHARSET_TEXT, 
		null,
		0,
		0);	
	if (xmString == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
	if (mnemonic == 0) mnemonic = OS.XK_VoidSymbol;
	int [] argList = {
		OS.XmNlabelType, OS.XmSTRING,
		OS.XmNlabelString, xmString,
		OS.XmNmnemonic, mnemonic,
	};
	OS.XtSetValues (labelHandle, argList, argList.length / 2);
	if (xmString != 0) OS.XmStringFree (xmString);
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

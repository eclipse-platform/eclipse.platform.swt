package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.carbon.*;
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
public /*final*/ class Group extends Composite {

	private static final int LABEL_HEIGHT= 20;
	private static final int MARGIN= 4;

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
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}
public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget();
    /* AW
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
	int borderWidth = getBorderWidth ();
	trimX = x - marginWidth + thickness - borderWidth;
	trimY = y - marginHeight + thickness - borderWidth;
	trimWidth = width + ((marginWidth + thickness + borderWidth) * 2);
	trimHeight = height + ((marginHeight + thickness + borderWidth) * 2);
	if (OS.XtIsManaged (labelHandle)) {
		int [] argList2 = {OS.XmNy, 0, OS.XmNheight, 0};
		OS.XtGetValues (labelHandle, argList2, argList2.length / 2);
		int labelHeight = ((short) argList2 [1]) + argList2 [3];
		trimY = y - labelHeight;
		trimHeight = height + labelHeight + (marginHeight + thickness);
	}
    */
	return new Rectangle (x-MARGIN, y-LABEL_HEIGHT, width+(2*MARGIN), height+LABEL_HEIGHT+MARGIN);
}
void createHandle (int index) {
	state |= HANDLE;
	int parentHandle = parent.handle;
    /*
	formHandle = OS.XmCreateForm (parentHandle, null, argList1, argList1.length / 2);
	if (formHandle == 0) error (SWT.ERROR_NO_HANDLES);
    */
    /* AW
	int [] argList2 = {
		OS.XmNshadowType, shadowType (),
		OS.XmNtopAttachment, OS.XmATTACH_FORM,
		OS.XmNbottomAttachment, OS.XmATTACH_FORM,
		OS.XmNleftAttachment, OS.XmATTACH_FORM,
		OS.XmNrightAttachment, OS.XmATTACH_FORM,
		OS.XmNresizable, 0,
	};
	handle = OS.XmCreateFrame (formHandle, null, argList2, argList2.length / 2);
    */
	handle= MacUtil.newControl(parentHandle, OS.kControlGroupBoxTextTitleProc);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	setFont(defaultFont());
}
Font defaultFont () {
	return getDisplay ().groupFont;
}
public Rectangle getClientArea () {
	checkWidget();
    /* AW
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
    */
	Point e= getSize();
    return new Rectangle(MARGIN, LABEL_HEIGHT, e.x-(2*MARGIN), e.y-(LABEL_HEIGHT+MARGIN));
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
	int[] sHandle= new int[1];
    OS.GetControlTitleAsCFString(handle, sHandle);
	return MacUtil.getStringAndRelease(sHandle[0]);
}
/* AW
boolean mnemonicHit (char key) {
	return setFocus ();
}
boolean mnemonicMatch (char key) {
	char mnemonic = findMnemonic (getText ());
	if (mnemonic == '\0') return false;
	return Character.toUpperCase (key) == Character.toUpperCase (mnemonic);
}
*/
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
	int sHandle= 0;
	try {
		sHandle= OS.CFStringCreateWithCharacters(MacUtil.removeMnemonics(string));
		OS.SetControlTitleWithCFString(handle, sHandle);
	} finally {
		if (sHandle != 0)
			OS.CFRelease(sHandle);
	}
}
}

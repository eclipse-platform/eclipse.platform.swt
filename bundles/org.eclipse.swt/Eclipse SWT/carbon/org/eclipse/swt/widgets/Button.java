package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.Rect;
import org.eclipse.swt.internal.carbon.ControlFontStyleRec;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

public  class Button extends Control {
	String text;

public Button (Composite parent, int style) {
	super (parent, checkStyle (style));
}

public void addSelectionListener(SelectionListener listener) {
	checkWidget();
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

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	if (false) return new Point (100, 50);
	if ((style & SWT.ARROW) != 0) {
		return new Point (25, 25);
	}
	Rect rect = new Rect();
	short [] base = new short [1];
	OS.GetBestControlRect (handle, rect, base);
	int width = rect.right - rect.left + 1;
	int height = rect.bottom - rect.top;
	if ((style & SWT.TOGGLE) == 0) height -= 12;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	return new Point (width, height);
}

void createHandle () {
	int [] outControl = new int [1];
	int window = OS.GetControlOwner (parent.handle);
				
	if ((style & SWT.ARROW) != 0) {
//		short orientation = OS.kControlPopupArrowOrientationEast;
//		if ((style & SWT.UP) != 0) orientation = (short) OS.kControlPopupArrowOrientationNorth;
//		if ((style & SWT.DOWN) != 0) orientation = (short) OS.kControlPopupArrowOrientationSouth;
//		if ((style & SWT.LEFT) != 0) orientation = (short) OS.kControlPopupArrowOrientationWest;
//		OS.CreatePopupArrowControl (window, null, orientation, (short)OS.kControlPopupArrowSizeNormal, outControl);
		OS.CreateBevelButtonControl(window, null, 0, (short)0, (short)0, 0, (short)0, (short)0, (short)0, outControl);
		if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
		handle = outControl [0];
		OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlBevelButtonKindTag, 2, new short [] {(short)OS.kThemeArrowButton});
	}
	
	if ((style & SWT.CHECK) != 0) {
		//OS.CreateCheckBoxControl (window, null, 0, 0 /*initially off*/, true, outControl);
		OS.CreateBevelButtonControl(window, null, 0, (short)0, (short)OS.kControlBehaviorToggles, 0, (short)0, (short)0, (short)0, outControl);
		if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
		handle = outControl [0];
		OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlBevelButtonKindTag, 2, new short [] {(short)OS.kThemeCheckBox});
	}
	
	if ((style & SWT.RADIO) != 0) {
		//OS.CreateRadioButtonControl(window, null, 0, 0 /*initially off*/, true, outControl);
		OS.CreateBevelButtonControl(window, null, 0, (short)0, (short)OS.kControlBehaviorToggles, 0, (short)0, (short)0, (short)0, outControl);
		if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
		handle = outControl [0];
		OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlBevelButtonKindTag, 2, new short [] {(short)OS.kThemeRadioButton});
	}
	
	if ((style & SWT.TOGGLE) != 0) {
		OS.CreateBevelButtonControl(window, null, 0, (short)OS.kControlBevelButtonNormalBevel, (short)OS.kControlBehaviorToggles, 0, (short)0, (short)0, (short)0, outControl);
		if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
		handle = outControl [0];
		OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlBevelButtonKindTag, 2, new short [] {(short)OS.kThemeRoundedBevelButton});
	}
	
	if (outControl [0] == 0) {
		//OS.CreatePushButtonControl (window, null, 0, outControl);
		OS.CreateBevelButtonControl(window, null, 0, (short)2, (short)OS.kControlBehaviorPushbutton, 0, (short)0, (short)0, (short)0, outControl);
		if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
		handle = outControl [0];
		OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlBevelButtonKindTag, 2, new short [] {(short)OS.kThemePushButton});
	}

	ControlFontStyleRec fontRec = new ControlFontStyleRec();
	fontRec.flags = (short)OS.kControlUseFontMask;
	fontRec.font = 0;
	OS.SetControlFontStyle (handle, fontRec);
	OS.HIViewAddSubview (parent.handle, handle);
	OS.HIViewSetZOrder (handle, OS.kHIViewZOrderBelow, 0);
}

public int getAlignment () {
	checkWidget();
    return 0;
}

public Image getImage () {
	checkWidget();
	return null;
}
String getNameText () {
	return getText ();
}

public boolean getSelection () {
	checkWidget();
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return false;
    return false;
}

public String getText () {
	checkWidget();
	return text;
}

public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Selection, listener);
	eventTable.unhook(SWT.DefaultSelection,listener);
}

public void setAlignment (int alignment) {
	checkWidget();
}

public void setImage (Image image) {
	checkWidget();
}

public void setSelection (boolean selected) {
	checkWidget();
}

public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.ARROW) != 0) return;
	text = string;
	char [] buffer = new char [text.length ()];
	int i=0, j=0;
	while (i < buffer.length) {
		if ((buffer [j++] = buffer [i++]) == Mnemonic) {
			if (i == buffer.length) {continue;}
			if (buffer [i] == Mnemonic) {i++; continue;}
			j--;
		}
	}
	text.getChars (0, buffer.length, buffer, 0);
	int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, j);
	if (ptr == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
	OS.SetControlTitleWithCFString (handle, ptr);
	OS.CFRelease (ptr);
}

}

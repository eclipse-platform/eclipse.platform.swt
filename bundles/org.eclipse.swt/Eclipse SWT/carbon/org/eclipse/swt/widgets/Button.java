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
import org.eclipse.swt.internal.carbon.CFRange;
import org.eclipse.swt.internal.carbon.ControlButtonContentInfo;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

public class Button extends Control {
	String text;
	Image image;
	
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
	// NEEDS WORK
	// - empty string/icon case is broken
	if ((style & SWT.ARROW) != 0) {
		return new Point (10, 10);
	}
	Rect rect = new Rect();
	short [] base = new short [1];
	OS.GetBestControlRect (handle, rect, base);
	int width = rect.right - rect.left;
	int height = rect.bottom - rect.top;
	if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
		height -= 17;
	}
	if ((style & SWT.PUSH) != 0 && (style & SWT.FLAT) == 0) {
		width += 1;
		height -= 11;
	}
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	return new Point (width, height);
}

void createHandle () {
	int [] outControl = new int [1];
	int window = OS.GetControlOwner (parent.handle);
				
	if ((style & SWT.ARROW) != 0) {
		short orientation = OS.kControlPopupArrowOrientationEast;
		if ((style & SWT.UP) != 0) orientation = (short) OS.kControlPopupArrowOrientationNorth;
		if ((style & SWT.DOWN) != 0) orientation = (short) OS.kControlPopupArrowOrientationSouth;
		if ((style & SWT.LEFT) != 0) orientation = (short) OS.kControlPopupArrowOrientationWest;
		OS.CreatePopupArrowControl (window, null, orientation, (short)OS.kControlPopupArrowSizeNormal, outControl);
		if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
		handle = outControl [0];
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
		if ((style & SWT.FLAT) == 0 ) {
			OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlBevelButtonKindTag, 2, new short [] {(short)OS.kThemeRoundedBevelButton});
		}
	}
	
	if (outControl [0] == 0) {
		//OS.CreatePushButtonControl (window, null, 0, outControl);
		OS.CreateBevelButtonControl(window, null, 0, (short)2, (short)OS.kControlBehaviorPushbutton, 0, (short)0, (short)0, (short)0, outControl);
		if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
		handle = outControl [0];
		if ((style & SWT.FLAT) == 0 ) {
			OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlBevelButtonKindTag, 2, new short [] {(short)OS.kThemePushButton});
		}
	}

	ControlFontStyleRec fontRec = new ControlFontStyleRec();
	fontRec.flags = (short)OS.kControlUseFontMask;
	fontRec.font = 0;
	OS.SetControlFontStyle (handle, fontRec);
	OS.HIViewAddSubview (parent.handle, handle);
	OS.HIViewSetZOrder (handle, OS.kHIViewZOrderBelow, 0);
	
	if ((style & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) != 0) {
		int textAlignment = 0;
		int graphicAlignment = 0;
		if ((style & SWT.LEFT) != 0) {
			textAlignment = OS.kControlBevelButtonAlignTextFlushLeft;
			graphicAlignment = OS.kControlBevelButtonAlignLeft;
		}
		if ((style & SWT.RIGHT) != 0) {
			textAlignment = OS.kControlBevelButtonAlignTextCenter;
			graphicAlignment = OS.kControlBevelButtonAlignCenter;
		}
		if ((style & SWT.CENTER) != 0) {
			textAlignment = OS.kControlBevelButtonAlignTextFlushRight;
			graphicAlignment = OS.kControlBevelButtonAlignRight;
		}
		OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlBevelButtonTextAlignTag, 2, new short [] {(short)textAlignment});
		OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlBevelButtonGraphicAlignTag, 2, new short [] {(short)graphicAlignment});
	}
}

public int getAlignment () {
	checkWidget ();
	if ((style & SWT.ARROW) != 0) {
		if ((style & SWT.UP) != 0) return SWT.UP;
		if ((style & SWT.DOWN) != 0) return SWT.DOWN;
		if ((style & SWT.LEFT) != 0) return SWT.LEFT;
		if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
		return SWT.UP;
	}
	if ((style & SWT.LEFT) != 0) return SWT.LEFT;
	if ((style & SWT.CENTER) != 0) return SWT.CENTER;
	if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
	return SWT.LEFT;
}

public Image getImage () {
	checkWidget();
	return image;
}
String getNameText () {
	return getText ();
}

public boolean getSelection () {
	checkWidget ();
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return false;
    return OS.GetControl32BitValue(handle) != 0;
}

public String getText () {
	checkWidget ();
	return text;
}

int kEventControlHit (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlHit (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	if ((style & SWT.RADIO) != 0) {
		if ((parent.getStyle () & SWT.NO_RADIO_GROUP) == 0) {
			selectRadio ();
		}
	}
	postEvent (SWT.Selection);
	return OS.eventNotHandledErr;
}

public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Selection, listener);
	eventTable.unhook(SWT.DefaultSelection,listener);
}

void selectRadio () {
	int index = 0;
	Control [] children = parent._getChildren ();
	while (index < children.length && children [index] != this) index++;
	int i = index - 1;
	while (i >= 0 && children [i].setRadioSelection (false)) --i;
	int j = index + 1;
	while (j < children.length && children [j].setRadioSelection (false)) j++;
	setSelection (true);
}

public void setAlignment (int alignment) {
	if ((style & SWT.ARROW) != 0) {
		if ((style & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT)) == 0) return; 
		style &= ~(SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT);
		style |= alignment & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT);
		//NEEDS WORK
		return;
	}
	if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) return;
	style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	style |= alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	int textAlignment = 0;
	int graphicAlignment = 0;
	if ((style & SWT.LEFT) != 0) {
		textAlignment = OS.kControlBevelButtonAlignTextFlushLeft;
		graphicAlignment = OS.kControlBevelButtonAlignLeft;
	}
	if ((style & SWT.RIGHT) != 0) {
		textAlignment = OS.kControlBevelButtonAlignTextCenter;
		graphicAlignment = OS.kControlBevelButtonAlignCenter;
	}
	if ((style & SWT.CENTER) != 0) {
		textAlignment = OS.kControlBevelButtonAlignTextFlushRight;
		graphicAlignment = OS.kControlBevelButtonAlignRight;
	}
	OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlBevelButtonTextAlignTag, 2, new short [] {(short)textAlignment});
	OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlBevelButtonGraphicAlignTag, 2, new short [] {(short)graphicAlignment});
	OS.HIViewSetNeedsDisplay (handle, true);
}

public boolean setFocus () {
	checkWidget ();
	if ((style & SWT.ARROW) != 0) return false;
	return super.setFocus ();
}

public void setImage (Image image) {
	checkWidget();
	if ((style & SWT.ARROW) != 0) return;
	this.image = image;
	if (text != null) {
		char [] buffer = new char [1];
		int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, 1);
		if (ptr == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
		OS.SetControlTitleWithCFString (handle, ptr);
		OS.CFRelease (ptr);
	}
	//int kControlContentIconRef = 132;
	//int kOnSystemDisk = -32768;
	//int kSystemIconsCreator = ('m'<<24) + ('a'<<16) + ('c'<<8) + 's';
	//int kColorSyncFolderIcon = ('p'<<24) + ('r'<<16) + ('o'<<8) + 'f';
	//int [] iconRef = new int [1];
	//OS.GetIconRef ((short)kOnSystemDisk, kSystemIconsCreator, kColorSyncFolderIcon, iconRef);
	//ControlButtonContentInfo inContent = new ControlButtonContentInfo ();
	//inContent.contentType = (short)kControlContentIconRef;
	//inContent.iconRef = iconRef [0];
	//OS.SetBevelButtonContentInfo (handle, inContent);
	//OS.HIViewSetNeedsDisplay (handle, true);
}

boolean setRadioSelection (boolean value){
	if ((style & SWT.RADIO) == 0) return false;
	if (getSelection () != value) {
		setSelection (value);
		postEvent (SWT.Selection);
	}
	return true;
}

public void setSelection (boolean selected) {
	checkWidget();
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return;
	OS.SetControl32BitValue (handle, selected ? 1 : 0);
}

public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.ARROW) != 0) return;
	text = string;
	if (image != null) {
		ControlButtonContentInfo inContent = new ControlButtonContentInfo();
		inContent.contentType = (short)OS.kControlContentTextOnly;
		OS.SetBevelButtonContentInfo(handle, inContent);
	}
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

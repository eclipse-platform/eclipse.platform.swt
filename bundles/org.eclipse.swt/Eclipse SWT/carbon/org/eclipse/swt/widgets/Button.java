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
import org.eclipse.swt.internal.carbon.ControlButtonContentInfo;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

public class Button extends Control {
	String text;
	Image image;
	int cIcon;
	boolean displayImage;
	int textExtentX;
	int textExtentY;
	
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
	// NEEDS WORK - empty string
	if ((style & SWT.ARROW) != 0) {
		int width = 20, height = 20;
		if (wHint != SWT.DEFAULT) width = wHint;
		if (hHint != SWT.DEFAULT) height = hHint;
		return new Point (width, height);
	}
	Rect rect = new Rect();
	short [] base = new short [1];
	OS.GetBestControlRect (handle, rect, base);
	int width = rect.right - rect.left;
	int height = rect.bottom - rect.top;

	if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
		if (displayImage) {
			Rectangle bounds = image.getBounds();
			width += bounds.width + 20;
			height += bounds.height;
		} else {
			width += 8;
		}
	} else {
		// If any theme was applied to a bevel button, the compute size 
		// needs to be fixed
		if ((style & SWT.FLAT) == 0 || (style & SWT.TOGGLE) != 0) {
			if (displayImage) {
				Rectangle bounds = image.getBounds();
				width = bounds.width;
				height = bounds.height;
				if ((style & SWT.PUSH) != 0) {
					width += 28;
					height += 10;
				}
				if ((style & SWT.TOGGLE) != 0) {
					width += 6;
					height += 6;
				}
			} else {
				if ((style & SWT.PUSH) != 0) {
					width = textExtentX + 16;
					height = textExtentY;
				}
				if ((style & SWT.TOGGLE) != 0) {
					width = textExtentX - 10;
					height = textExtentY - 2;
				}
			}
		}
	}
	width = Math.max(20, width);
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	return new Point (width, height);
}

void createHandle () {
	int [] outControl = new int [1];
	int window = OS.GetControlOwner (parent.handle);
				
	if ((style & SWT.ARROW) != 0) {
		int orientation = OS.kThemeDisclosureRight;
		if ((style & SWT.UP) != 0) orientation = OS.kThemeDisclosureRight; // NEEDS WORK
		if ((style & SWT.DOWN) != 0) orientation = OS.kThemeDisclosureDown;
		if ((style & SWT.LEFT) != 0) orientation = OS.kThemeDisclosureLeft;
		OS.CreateBevelButtonControl(window, null, 0, (short)0, (short)OS.kControlBehaviorPushbutton, 0, (short)0, (short)0, (short)0, outControl);
		if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
		handle = outControl [0];
		OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlBevelButtonKindTag, 2, new short [] {(short)(OS.kThemeDisclosureButton)});
		OS.SetControl32BitMaximum (handle, 2);
		OS.SetControl32BitValue (handle, orientation);
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
	
	if ((style & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) != 0) {
		int textAlignment = 0;
		int graphicAlignment = 0;
		if ((style & SWT.LEFT) != 0) {
			textAlignment = OS.kControlBevelButtonAlignTextFlushLeft;
			graphicAlignment = OS.kControlBevelButtonAlignLeft;
		}
		if ((style & SWT.CENTER) != 0) {
			textAlignment = OS.kControlBevelButtonAlignTextCenter;
			graphicAlignment = OS.kControlBevelButtonAlignCenter;
		}
		if ((style & SWT.RIGHT) != 0) {
			textAlignment = OS.kControlBevelButtonAlignTextFlushRight;
			graphicAlignment = OS.kControlBevelButtonAlignRight;
		}
		OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlBevelButtonTextAlignTag, 2, new short [] {(short)textAlignment});
		OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlBevelButtonGraphicAlignTag, 2, new short [] {(short)graphicAlignment});
	}
}

void destroyWidget () {
	if (cIcon != 0) {
		destroyCIcon(cIcon);
		cIcon = 0;
	}
	super.destroyWidget();
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
	/*
	* This code is intentionally commented.  When two groups
	* of radio buttons with the same parent are separated by
	* another control, the correct behavior should be that
	* the two groups act independently.  This is consistent
	* with radio tool and menu items.  The commented code
	* implements this behavior.
	*/
//	int index = 0;
//	Control [] children = parent._getChildren ();
//	while (index < children.length && children [index] != this) index++;
//	int i = index - 1;
//	while (i >= 0 && children [i].setRadioSelection (false)) --i;
//	int j = index + 1;
//	while (j < children.length && children [j].setRadioSelection (false)) j++;
//	setSelection (true);
	Control [] children = parent._getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (this != child) child.setRadioSelection (false);
	}
	setSelection (true);
}

public void setAlignment (int alignment) {
	if ((style & SWT.ARROW) != 0) {
		if ((style & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT)) == 0) return; 
		style &= ~(SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT);
		style |= alignment & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT);
		int orientation = OS.kThemeDisclosureRight;
		if ((style & SWT.UP) != 0) orientation = OS.kThemeDisclosureRight; // NEEDS WORK
		if ((style & SWT.DOWN) != 0) orientation = OS.kThemeDisclosureDown;
		if ((style & SWT.LEFT) != 0) orientation = OS.kThemeDisclosureLeft;
		OS.SetControl32BitValue (handle, orientation);
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
	if ((style & SWT.CENTER) != 0) {
		textAlignment = OS.kControlBevelButtonAlignTextCenter;
		graphicAlignment = OS.kControlBevelButtonAlignCenter;
	}
	if ((style & SWT.RIGHT) != 0) {
		textAlignment = OS.kControlBevelButtonAlignTextFlushRight;
		graphicAlignment = OS.kControlBevelButtonAlignRight;
	}
	OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlBevelButtonTextAlignTag, 2, new short [] {(short)textAlignment});
	OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlBevelButtonGraphicAlignTag, 2, new short [] {(short)graphicAlignment});
	OS.HIViewSetNeedsDisplay (handle, true);
}

public void setBounds (int x, int y, int width, int height) {
	checkWidget ();
	/* Bug in MacOS X. When setting the height of a bevel button 
	 * to a value less than 20, the button is drawn incorrectly.
	 * The fix is to force the height to be greater than or equal to 20.
	 */
	if ((style & SWT.ARROW) == 0) {
		height = Math.max (20, height);
	}
	super.setBounds (x, y, width, height);
}

public boolean setFocus () {
	checkWidget ();
	if ((style & SWT.ARROW) != 0) return false;
	return super.setFocus ();
}

public void setImage (Image image) {
	checkWidget();
	if ((style & SWT.ARROW) != 0) return;
	if (cIcon != 0) {
		destroyCIcon(cIcon);
		cIcon = 0;
	}
	this.image = image;
	if (image == null) {
		ControlButtonContentInfo inContent = new ControlButtonContentInfo();
		inContent.contentType = (short)OS.kControlContentTextOnly;
		OS.SetBevelButtonContentInfo (handle, inContent);
		displayImage = false;
		return;
	}
	if (text != null) {
		char [] buffer = new char [1];
		int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, 1);
		if (ptr == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
		OS.SetControlTitleWithCFString (handle, ptr);
		OS.CFRelease (ptr);
	}
	cIcon = createCIcon (image);
	ControlButtonContentInfo inContent = new ControlButtonContentInfo ();
	inContent.contentType = (short)OS.kControlContentCIconHandle;
	inContent.iconRef = cIcon;
	OS.SetBevelButtonContentInfo (handle, inContent);
	OS.HIViewSetNeedsDisplay (handle, true);
	displayImage = true;
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
	if (displayImage) {
		ControlButtonContentInfo inContent = new ControlButtonContentInfo();
		inContent.contentType = (short)OS.kControlContentTextOnly;
		OS.SetBevelButtonContentInfo(handle, inContent);
	}
	displayImage = false;
	char [] buffer = new char [text.length ()];
	text.getChars (0, buffer.length, buffer, 0);
	int i=0, j=0;
	while (i < buffer.length) {
		if ((buffer [j++] = buffer [i++]) == Mnemonic) {
			if (i == buffer.length) {continue;}
			if (buffer [i] == Mnemonic) {i++; continue;}
			j--;
		}
	}
	int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, j);
	if (ptr == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
	OS.SetControlTitleWithCFString (handle, ptr);
	OS.CFRelease (ptr);
	Rect rect = new Rect();
	short [] base = new short [1];
	OS.GetBestControlRect (handle, rect, base);
	textExtentX = rect.right - rect.left;
	textExtentY = rect.bottom - rect.top;
	
}

}

package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.ControlButtonContentInfo;
import org.eclipse.swt.internal.carbon.HMHelpContentRec;
import org.eclipse.swt.internal.carbon.Rect;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

public class ToolItem extends Item {
	int handle, iconHandle, labelHandle, arrowHandle;
	int cIcon, labelCIcon, arrowCIcon;
	ToolBar parent;
	Image hotImage, disabledImage;
	String toolTipText;
	Control control;
	boolean tracking;

	static final int DEFAULT_WIDTH = 24;
	static final int DEFAULT_HEIGHT = 22;
	static final int DEFAULT_SEPARATOR_WIDTH = 8;

public ToolItem (ToolBar parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	parent.createItem (this, parent.getItemCount ());
	parent.relayout ();
}

public ToolItem (ToolBar parent, int style, int index) {
	super (parent, checkStyle (style));
	this.parent = parent;
	parent.createItem (this, index);
	parent.relayout ();
}

public void addSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.PUSH, SWT.CHECK, SWT.RADIO, SWT.SEPARATOR, SWT.DROP_DOWN, 0);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

Point computeSize () {
	checkWidget();
	int width = 0, height = 0;
	if ((style & SWT.SEPARATOR) != 0) {
		if ((style & SWT.HORIZONTAL) != 0) {
			width = getWidth ();
			height = DEFAULT_HEIGHT;
		} else {
			width = DEFAULT_WIDTH;
			height = getWidth ();
		}
	} else {
		int space = 0;
		int stringWidth = 0, stringHeight = 0;
		if (text.length () != 0) {
			GC gc = new GC (parent);
			Point size = gc.stringExtent (text);
			stringWidth = size.x;
			stringHeight = size.y;
			gc.dispose ();
		}
		int imageWidth = 0, imageHeight = 0;
		if (image != null) {
			if (text.length () != 0) space = 2;
			Rectangle rect = image.getBounds ();
			imageWidth = rect.width;
			imageHeight = rect.height;
		}
		if ((parent.style & SWT.RIGHT) != 0) {
			width = stringWidth + imageWidth;
			height = Math.max (stringHeight, imageHeight);
		} else {
			width = Math.max (stringWidth, imageWidth);
			height = stringHeight + imageHeight;
		}
		if ((style & SWT.DROP_DOWN) != 0) {
			int arrowWidth = 6; //NOT DONE
			width += 3 + arrowWidth;
		}
		int inset = 3;
		width += space + inset * 2;
		height += space + inset * 2;
	}
	return new Point (width, height);
}

void createHandle () {
	int [] outControl = new int [1];
	int window = OS.GetControlOwner (parent.handle);
	int features = OS.kControlSupportsEmbedding | 1 << 4;
	OS.CreateUserPaneControl (window, null, features, outControl);
	if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
	handle = outControl [0];
	int width = DEFAULT_WIDTH, height = DEFAULT_HEIGHT;
	if ((style & SWT.SEPARATOR) == 0) {
		ControlButtonContentInfo inContent = new ControlButtonContentInfo ();
		if ((style & SWT.DROP_DOWN) != 0) {
			OS.CreateIconControl(window, null, inContent, false, outControl);
			if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
			arrowHandle = outControl [0];
			updateArrow ();
		}
		OS.CreateIconControl(window, null, inContent, false, outControl);
		if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
		iconHandle = outControl [0];
		OS.CreateIconControl(window, null, inContent, false, outControl);
		if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
		labelHandle = outControl [0];
	} else {
		if ((parent.style & SWT.HORIZONTAL) != 0) {
			width = DEFAULT_SEPARATOR_WIDTH;
		} else {
			height = DEFAULT_SEPARATOR_WIDTH;
		}
	}	
	setBounds (0, 0, width, height);
	parent.relayout ();
}

void createWidget () {
	super.createWidget ();
	setZOrder ();
	toolTipText = "";
}

int defaultThemeFont () {	
	return OS.kThemeToolbarFont;
}

void deregister () {
	super.deregister ();
	WidgetTable.remove (handle);
	if (iconHandle != 0) WidgetTable.remove (iconHandle);
	if (labelHandle != 0) WidgetTable.remove (labelHandle);
	if (arrowHandle != 0) WidgetTable.remove (arrowHandle);
}

void destroyWidget () {
	int theControl = handle;
	releaseHandle ();
	if (theControl != 0) {
		OS.DisposeControl (theControl);
	}
}

public void dispose () {
	if (isDisposed()) return;
	ToolBar parent = this.parent;
	super.dispose ();
	parent.relayout ();
}

void drawWidget (int control) {
	drawBackground (control, null);
	if ((style & SWT.SEPARATOR) != 0) {
		Rect rect = new Rect ();
		OS.GetControlBounds (handle, rect);
		rect.top += 2;
		rect.bottom -= 2;
		OS.DrawThemeSeparator (rect, 0);
	}
}

public Rectangle getBounds () {
	checkWidget();
	Rect rect = getControlBounds (handle);
	return new Rectangle (rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
}

public Control getControl () {
	checkWidget();
	return control;
}

public Image getDisabledImage () {
	checkWidget();
	return disabledImage;
}

public boolean getEnabled () {
	checkWidget();
	return OS.IsControlEnabled(handle);
}

public Display getDisplay () {
	Composite parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}

int getDrawCount () {
	return parent.getDrawCount ();
}

public Image getHotImage () {
	checkWidget();
	return hotImage;
}

public ToolBar getParent () {
	checkWidget();
	return parent;
}

public boolean getSelection () {
	checkWidget();
	if ((style & (SWT.CHECK | SWT.RADIO)) == 0) return false;
	short [] transform = new short [1];
 	OS.GetControlData (iconHandle, (short) OS.kControlEntireControl, OS.kControlIconTransformTag, 2, transform, null);
  	return (transform [0] & OS.kTransformSelected) != 0;
}

public String getToolTipText () {
	checkWidget();
	return toolTipText;
}

public int getWidth () {
	checkWidget();
	Rect rect = new Rect ();
	OS.GetControlBounds (handle, rect);
	return rect.right - rect.left;
}

int helpProc (int inControl, int inGlobalMouse, int inRequest, int outContentProvided, int ioHelpContent) {
	Display display = getDisplay ();
    switch (inRequest) {
		case OS.kHMSupplyContent: {
			int [] contentProvided = new int [] {OS.kHMContentNotProvided};
			if (toolTipText != null && toolTipText.length () != 0) {
				char [] buffer = new char [toolTipText.length ()];
				toolTipText.getChars (0, buffer.length, buffer, 0);
				int i=0, j=0;
				while (i < buffer.length) {
					if ((buffer [j++] = buffer [i++]) == Mnemonic) {
						if (i == buffer.length) {continue;}
						if (buffer [i] == Mnemonic) {i++; continue;}
						j--;
					}
				}
				if (display.helpString != 0) OS.CFRelease (display.helpString);
		    	display.helpString = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, j);
				HMHelpContentRec helpContent = new HMHelpContentRec ();
				OS.memcpy (helpContent, ioHelpContent, HMHelpContentRec.sizeof);
		        helpContent.version = OS.kMacHelpVersion;
		        helpContent.tagSide = OS.kHMDefaultSide;
				display.helpControl = null;
		        helpContent.absHotRect_left = (short) 0;
		     	helpContent.absHotRect_top = (short) 0;
		        helpContent.absHotRect_right = (short) 0;
		        helpContent.absHotRect_bottom = (short) 0;
		        helpContent.content0_contentType = OS.kHMCFStringContent;
		        helpContent.content0_tagCFString = display.helpString;
		        helpContent.content1_contentType = OS.kHMCFStringContent;
		        helpContent.content1_tagCFString = display.helpString;
				OS.memcpy (ioHelpContent, helpContent, HMHelpContentRec.sizeof);
				contentProvided [0] = OS.kHMContentProvided;
			}
			OS.memcpy (outContentProvided, contentProvided, 4);
			break;
		}
		case OS.kHMDisposeContent: {
			if (display.helpString != 0) OS.CFRelease (display.helpString);
			display.helpString = 0;
			break;
		}
	}
	return OS.noErr;
}

void hookEvents () {
	super.hookEvents ();
	Display display = getDisplay ();
	int controlProc = display.controlProc;
	int [] mask1 = new int [] {
		OS.kEventClassControl, OS.kEventControlDraw,
		OS.kEventClassControl, OS.kEventControlHit,
		OS.kEventClassControl, OS.kEventControlContextualMenuClick,
		OS.kEventClassControl, OS.kEventControlTrack,
	};
	int controlTarget = OS.GetControlEventTarget (handle);
	OS.InstallEventHandler (controlTarget, controlProc, mask1.length / 2, mask1, handle, null);
	int [] mask2 = new int [] {
		OS.kEventClassControl, OS.kEventControlDraw,
		OS.kEventClassControl, OS.kEventControlContextualMenuClick,
		OS.kEventClassControl, OS.kEventControlTrack,
	};
	if (iconHandle != 0) {
		controlTarget = OS.GetControlEventTarget (iconHandle);
		OS.InstallEventHandler (controlTarget, controlProc, mask2.length / 2, mask2, iconHandle, null);
	}
	if (labelHandle != 0) {
		controlTarget = OS.GetControlEventTarget (labelHandle);
		OS.InstallEventHandler (controlTarget, controlProc, mask2.length / 2, mask2, labelHandle, null);
	}
	if (arrowHandle != 0) {
		controlTarget = OS.GetControlEventTarget (arrowHandle);
		OS.InstallEventHandler (controlTarget, controlProc, mask2.length / 2, mask2, arrowHandle, null);
	}
	int helpProc = display.helpProc;
	OS.HMInstallControlContentCallback (handle, helpProc);
}

public boolean isEnabled () {
	checkWidget();
	return getEnabled () && parent.isEnabled ();
}

int kEventControlContextualMenuClick (int nextHandler, int theEvent, int userData) {
	return parent.kEventControlContextualMenuClick (nextHandler, theEvent, userData);
}

int kEventControlHit (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlHit (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	Event event = new Event ();
	if ((style & SWT.RADIO) != 0) {
		if ((parent.getStyle () & SWT.NO_RADIO_GROUP) == 0) {
			selectRadio ();
		}
	}
	if ((style & SWT.CHECK) != 0) setSelection (!getSelection ());
	if ((style & SWT.DROP_DOWN) != 0) {
		int [] theControl = new int [1];
		OS.GetEventParameter (theEvent, OS.kEventParamDirectObject, OS.typeControlRef, null, 4, null, theControl);
		if (theControl [0] == arrowHandle) event.detail = SWT.ARROW;
	}
	postEvent (SWT.Selection, event);
	return OS.eventNotHandledErr;
}

int kEventControlTrack (int nextHandler, int theEvent, int userData) {
	tracking = true;
	return OS.eventNotHandledErr;
}

int kEventMouseDown (int nextHandler, int theEvent, int userData) {
	int result = parent.kEventMouseDown (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	/*
	* Feature in the Macintosh.  When some controls get kEventControlClick
	* (which gets sent from kEventMouseDown), they calls TrackControl() or
	* HandleControlClick() to track the mouse.  Unfortunately, mouse move
	* events and the mouse up event is consumed.  The fix is to call the
	* default handler and send a fake mouse up when tracking is finished.
	* 
	* NOTE: No mouse move events are sent while tracking.  There is no
	* fix for this at this time.
	*/
	Display display = getDisplay ();
	display.grabControl = null;
	display.runDeferredEvents ();
	tracking = false;
	result = OS.CallNextEventHandler (nextHandler, theEvent);
	if (tracking) {
		org.eclipse.swt.internal.carbon.Point outPt = new org.eclipse.swt.internal.carbon.Point ();
		OS.GetGlobalMouse (outPt);
		Rect rect = new Rect ();
		int window = OS.GetControlOwner (handle);
		OS.GetWindowBounds (window, (short) OS.kWindowContentRgn, rect);
		int x = outPt.h - rect.left;
		int y = outPt.v - rect.top;
		int [] theControl = new int [1];
		OS.GetEventParameter (theEvent, OS.kEventParamDirectObject, OS.typeControlRef, null, 4, null, theControl);
		OS.GetControlBounds (theControl [0], rect);
		x -= rect.left;
		y -=  rect.top;
		short [] button = new short [1];
		OS.GetEventParameter (theEvent, OS.kEventParamMouseButton, OS.typeMouseButton, null, 2, null, button);
		int chord = OS.GetCurrentEventButtonState ();
		int modifiers = OS.GetCurrentEventKeyModifiers ();
		parent.sendMouseEvent (SWT.MouseUp, button [0], chord, (short)x, (short)y, modifiers);
	}
	tracking = false;
	return result;
}

int kEventMouseDragged (int nextHandler, int theEvent, int userData) {
	return parent.kEventMouseDragged (nextHandler, theEvent, userData);
}

int kEventMouseMoved (int nextHandler, int theEvent, int userData) {
	return parent.kEventMouseMoved (nextHandler, theEvent, userData);
}

int kEventMouseUp (int nextHandler, int theEvent, int userData) {
	return parent.kEventMouseUp (nextHandler, theEvent, userData);
}

void register () {
	super.register ();
	WidgetTable.put (handle, this);
	if (iconHandle != 0) WidgetTable.put (iconHandle, this);
	if (labelHandle != 0) WidgetTable.put (labelHandle, this);
	if (arrowHandle != 0) WidgetTable.put (arrowHandle, this);
}

void releaseChild () {
	super.releaseChild ();
	parent.destroyItem (this);
}

void releaseHandle () {
	super.releaseHandle ();
	handle = iconHandle = labelHandle = arrowHandle = 0;
}

void releaseWidget () {
	super.releaseWidget ();
	if (cIcon != 0) destroyCIcon (cIcon);
	if (labelCIcon != 0) destroyCIcon (labelCIcon);
	if (arrowCIcon != 0) destroyCIcon (arrowCIcon);
	cIcon = labelCIcon = arrowCIcon = 0;
	parent = null;
	control = null;
	toolTipText = null;
	image = disabledImage = hotImage = null; 
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
	ToolItem [] items = parent.getItems ();
	while (index < items.length && items [index] != this) index++;
	int i = index - 1;
	while (i >= 0 && items [i].setRadioSelection (false)) --i;
	int j = index + 1;
	while (j < items.length && items [j].setRadioSelection (false)) j++;
	setSelection (true);
}

void setBounds (int x, int y, int width, int height) {
	if (control != null) control.setBounds (x, y, width, height);
	setBounds (handle, x, y, width, height, true, true, false);
	if ((style & SWT.SEPARATOR) != 0) return;
	int space = 0;
	int inset = 3;
	int stringWidth = 0, stringHeight = 0;
	if (text.length () != 0) {
		GC gc = new GC (parent);
		Point size = gc.stringExtent (text);
		stringWidth = size.x;
		stringHeight = size.y;
		gc.dispose ();
	}
	int imageWidth = 0, imageHeight = 0;
	if (image != null) {
		if (text.length () != 0) space = 2;
		Rectangle rect = image.getBounds ();
		imageWidth = rect.width;
		imageHeight = rect.height;
	}
	int arrowWidth = 0, arrowHeight = 0;
	if ((style & SWT.DROP_DOWN) != 0) {
		arrowWidth = 6;
		arrowHeight = 4; //NOT DONE
	}
	if ((parent.style & SWT.RIGHT) != 0) {
		int imageX = inset;
		int imageY = inset + (height - (inset * 2) - imageHeight) / 2;
		setBounds (iconHandle, imageX, imageY, imageWidth, imageHeight, true, true, false);
		int labelX = imageX + imageWidth + space;
		int labelY = inset + (height - (inset * 2) - stringHeight) / 2;
		setBounds (labelHandle, labelX, labelY, stringWidth, stringHeight, true, true, false);
	} else {
		int imageX = inset + (width - (inset * 2) - (arrowWidth + 3) - imageWidth) / 2;
		int imageY = inset;
		setBounds (iconHandle, imageX, imageY, imageWidth, imageHeight, true, true, false);
		int labelX = inset + (width - (inset * 2) - (arrowWidth + 3) - stringWidth) / 2;
		int labelY = imageY + imageHeight + space;
		setBounds (labelHandle, labelX, labelY, stringWidth, stringHeight, true, true, false);
	}
	if ((style & SWT.DROP_DOWN) != 0) {
		int arrowX = width - inset - arrowWidth;
		int arrowY = inset + (height - (inset * 2) - arrowHeight) / 2;
		setBounds (arrowHandle, arrowX, arrowY, arrowWidth, arrowHeight, true, true, false);
	}
}

public void setControl (Control control) {
	checkWidget();
	if (control != null) {
		if (control.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (control.parent != parent) error (SWT.ERROR_INVALID_PARENT);
	}
	if ((style & SWT.SEPARATOR) == 0) return;
	this.control = control;
	if (control != null && !control.isDisposed ()) {
		control.setBounds (getBounds ());
	}
}

public void setEnabled (boolean enabled) {
	checkWidget();
	if (enabled) {
		OS.EnableControl (handle);
	} else {
		OS.DisableControl (handle);
	}
}

void setFontStyle (Font font) {
	/* This code is intentionaly commented. */
//	ControlFontStyleRec fontStyle = new ControlFontStyleRec ();
//	if (font != null) {
//		fontStyle.flags |= OS.kControlUseFontMask | OS.kControlUseSizeMask | OS.kControlUseFaceMask;
//		fontStyle.font = font.id;
//		fontStyle.style = font.style;
//		fontStyle.size = font.size;
//	} else {
//		fontStyle.flags |= OS.kControlUseThemeFontIDMask;
//		fontStyle.font = (short) defaultThemeFont ();
//	}
//	OS.SetControlFontStyle (labelHandle, fontStyle);
	updateText ();
}

public void setDisabledImage (Image image) {
	checkWidget();
	if (image != null && image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	disabledImage = image;
	updateImage ();
}

public void setHotImage (Image image) {
	checkWidget();
	if (image != null && image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	hotImage = image;
	updateImage ();
}

public void setImage (Image image) {
	checkWidget();
	if (image != null && image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	super.setImage (image);
	updateImage ();
}

boolean setRadioSelection (boolean value) {
	if ((style & SWT.RADIO) == 0) return false;
	if (getSelection () != value) {
		setSelection (value);
		postEvent (SWT.Selection);
	}
	return true;
}

public void setSelection (boolean selected) {
	checkWidget();
	if ((style & (SWT.CHECK | SWT.RADIO)) == 0) return;
	int transform = selected ? OS.kTransformSelected : 0;
	OS.SetControlData (iconHandle, OS.kControlEntireControl, OS.kControlIconTransformTag, 2, new short [] {(short)transform});
	OS.SetControlData (labelHandle, OS.kControlEntireControl, OS.kControlIconTransformTag, 2, new short [] {(short)transform});
	redrawWidget (handle);
}

void setSize (int width, int height, boolean layout) {
	Rect rect = new Rect();
	OS.GetControlBounds (handle, rect);
	if ((rect.right - rect.left) != width || (rect.bottom - rect.top) != height) {
		setBounds (handle, 0, 0, width, height, false, true, false);
		if (layout) parent.relayout ();
	}
}

public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	super.setText (string);
	updateText ();
}

public void setToolTipText (String string) {
	checkWidget();
	toolTipText = string;
}

public void setWidth (int width) {
	checkWidget();
	if ((style & SWT.SEPARATOR) == 0) return;
	if (width < 0) return;
	Rect rect = new Rect ();
	OS.GetControlBounds (handle, rect);
	setSize (width, rect.bottom - rect.top, true);
	if (control != null && !control.isDisposed ()) {
		control.setBounds (getBounds ());
	}
}

void setZOrder () {
	OS.HIViewAddSubview (parent.handle, handle);
	if (iconHandle != 0) OS.HIViewAddSubview (handle, iconHandle);
	if (labelHandle != 0) OS.HIViewAddSubview (handle, labelHandle);
	if (arrowHandle != 0) OS.HIViewAddSubview (handle, arrowHandle);
}

void updateImage () {
	if (cIcon != 0) destroyCIcon (cIcon);
	cIcon = 0;
	Image image = null;
	if (hotImage != null) {
		image = hotImage;
	} else {
		if (this.image != null) {
			image = this.image;
		} else {
			image = disabledImage;
		}
	}
	ControlButtonContentInfo inContent = new ControlButtonContentInfo ();
	if (image != null) {
		cIcon = createCIcon (image);
		inContent.contentType = (short) OS.kControlContentCIconHandle;
		inContent.iconRef = cIcon;
	}
	OS.SetBevelButtonContentInfo (iconHandle, inContent);
	redrawWidget (iconHandle);
	Point size = computeSize ();
	setSize (size.x, size.y, true);
}

void updateArrow () {
	if (arrowCIcon != 0) destroyCIcon (arrowCIcon);
	arrowCIcon = 0;
	Display display = getDisplay ();
	Image image = new Image (display, 7, 4);
	GC gc = new GC (image);
	int startX = 0, startY = 0;
	int [] arrow = {startX, startY, startX + 3, startY + 3, startX + 6, startY};
	gc.setBackground (parent.getForeground ());
	gc.fillPolygon (arrow);
	gc.drawPolygon (arrow);
	gc.dispose ();
	ImageData data = image.getImageData ();
	data.transparentPixel = 0xFFFFFFFF;
	image.dispose ();
	image = new Image (getDisplay (), data, data.getTransparencyMask());
	arrowCIcon = createCIcon (image);
	image.dispose ();
	ControlButtonContentInfo inContent = new ControlButtonContentInfo ();
	inContent.contentType = (short) OS.kControlContentCIconHandle;
	inContent.iconRef = arrowCIcon;
	OS.SetBevelButtonContentInfo (arrowHandle, inContent);	
}

void updateText () {
	if (labelCIcon != 0) destroyCIcon (labelCIcon);
	labelCIcon = 0;
	ControlButtonContentInfo inContent = new ControlButtonContentInfo ();
	if (text.length () > 0) {
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
		Font font = parent.getFont ();
		GC gc = new GC (parent);
		Point size = gc.stringExtent (text);
		gc.dispose ();
		Display display = getDisplay ();
		Image image = new Image (display, size.x, size.y);
		gc = new GC (image);
		gc.setFont (font);
		gc.drawString (text, 0, 0);
		gc.dispose ();
		ImageData data = image.getImageData ();
		data.transparentPixel = 0xFFFFFFFF;
		image.dispose ();
		image = new Image (display, data, data.getTransparencyMask());
		labelCIcon = createCIcon (image);
		image.dispose ();
		inContent.contentType = (short) OS.kControlContentCIconHandle;
		inContent.iconRef = labelCIcon;
	}
	OS.SetBevelButtonContentInfo (labelHandle, inContent);	
	redrawWidget (labelHandle);
	Point size = computeSize ();
	setSize (size.x, size.y, true);
}

}

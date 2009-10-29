/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.carbon.CFRange;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.ControlButtonContentInfo;
import org.eclipse.swt.internal.carbon.ControlFontStyleRec;
import org.eclipse.swt.internal.carbon.HMHelpContentRec;
import org.eclipse.swt.internal.carbon.CGRect;
import org.eclipse.swt.internal.carbon.CGPoint;
import org.eclipse.swt.internal.carbon.HIThemeSeparatorDrawInfo;
import org.eclipse.swt.internal.carbon.HIThemePopupArrowDrawInfo;
import org.eclipse.swt.internal.carbon.HIThemeTextInfo;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class represent a selectable user interface object
 * that represents a button in a tool bar.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>PUSH, CHECK, RADIO, SEPARATOR, DROP_DOWN</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles CHECK, PUSH, RADIO, SEPARATOR and DROP_DOWN 
 * may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#toolbar">ToolBar, ToolItem snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ToolItem extends Item {
	int handle, iconHandle, labelHandle;
	int cIcon;
	int visibleRgn, partCode;
	int width = DEFAULT_SEPARATOR_WIDTH;
	ToolBar parent;
	Image hotImage, disabledImage;
	String toolTipText;
	Control control;
	boolean selection;

	static final int DEFAULT_WIDTH = 24;
	static final int DEFAULT_HEIGHT = 22;
	static final int DEFAULT_SEPARATOR_WIDTH = 6;
	static final int ARROW_WIDTH = 9;
	static final int INSET = 3;
	static final String [] AX_ATTRIBUTES = {
		OS.kAXDescriptionAttribute,
		OS.kAXTitleAttribute,
		OS.kAXValueAttribute,
	};

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>ToolBar</code>) and a style value
 * describing its behavior and appearance. The item is added
 * to the end of the items maintained by its parent.
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
 * @see SWT#PUSH
 * @see SWT#CHECK
 * @see SWT#RADIO
 * @see SWT#SEPARATOR
 * @see SWT#DROP_DOWN
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ToolItem (ToolBar parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	parent.createItem (this, parent.getItemCount ());
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>ToolBar</code>), a style value
 * describing its behavior and appearance, and the index
 * at which to place it in the items maintained by its parent.
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
 * @param index the zero-relative index to store the receiver in its parent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the parent (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#PUSH
 * @see SWT#CHECK
 * @see SWT#RADIO
 * @see SWT#SEPARATOR
 * @see SWT#DROP_DOWN
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ToolItem (ToolBar parent, int style, int index) {
	super (parent, checkStyle (style));
	this.parent = parent;
	parent.createItem (this, index);
}

int actionProc (int theControl, int partCode) {
	int result = parent.actionProc (theControl, partCode);
	if (result == OS.noErr) return result;
	if (isDisposed ()) return OS.noErr;
	this.partCode = partCode;
	if (text.length () > 0 && theControl == labelHandle) {
		if (image != null && iconHandle != 0) {
			int transform = partCode != 0 ? OS.kTransformSelected : 0;
			OS.SetControlData (iconHandle, OS.kControlEntireControl, OS.kControlIconTransformTag, 2, new short [] {(short)transform});
			redrawWidget (iconHandle, false);
		}
		redrawWidget (labelHandle, false);		
	}
	if (image != null && theControl == iconHandle) {
		redrawWidget (labelHandle, false);
	}
	if (theControl == handle) {
		if (image != null && iconHandle != 0) {
			int transform = partCode != 0 ? OS.kTransformSelected : 0;
			OS.SetControlData (iconHandle, OS.kControlEntireControl, OS.kControlIconTransformTag, 2, new short [] {(short)transform});
			redrawWidget (iconHandle, false);
		}
		if (labelHandle != 0) redrawWidget (labelHandle, false);
	}
	return result;
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called when the mouse is over the arrow portion of a drop-down tool,
 * the event object detail field contains the value <code>SWT.ARROW</code>.
 * <code>widgetDefaultSelected</code> is not called.
 * </p>
 *
 * @param listener the listener which should be notified when the control is selected by the user,
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #removeSelectionListener
 * @see SelectionEvent
 */
public void addSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
}

int callPaintEventHandler (int control, int damageRgn, int visibleRgn, int theEvent, int nextHandler) {
	if (control == labelHandle && (partCode != 0 || ((state & DISABLED) != 0 && OS.VERSION >= 0x1040))) {
		HIThemeTextInfo info = new HIThemeTextInfo ();
		if (partCode != 0) {
			info.state = OS.kThemeStatePressed;
		} else {
			if (OS.IsControlActive (labelHandle)) {
				info.state = (state & DISABLED) == 0 ? OS.kThemeStateActive : OS.kThemeStateUnavailable;
			} else {
				info.state = (state & DISABLED) == 0 ? OS.kThemeStateInactive : OS.kThemeStateUnavailableInactive;
			}
		}
		Font font = parent.font;
		if (font != null) {
			short [] family = new short [1], style = new short [1];
			OS.FMGetFontFamilyInstanceFromFont (font.handle, family, style);
			OS.TextFont (family [0]);
			OS.TextFace ((short) (style [0] | font.style));
			OS.TextSize ((short) font.size);
			info.fontID = (short) OS.kThemeCurrentPortFont; 
		} else {
			info.fontID = (short) parent.defaultThemeFont ();
		}
		CGRect rect = new CGRect ();
		OS.HIViewGetBounds (labelHandle, rect);
		int [] context = new int [1];
		OS.GetEventParameter (theEvent, OS.kEventParamCGContextRef, OS.typeCGContextRef, null, 4, null, context);
		if ((state & DISABLED) != 0 && OS.VERSION >= 0x1040) {
			OS.HIThemeSetTextFill (OS.kThemeTextColorMenuItemDisabled, 0, context [0], OS.kHIThemeOrientationNormal);
		} else {
			int colorspace = OS.CGColorSpaceCreateDeviceRGB ();
			OS.CGContextSetFillColorSpace (context [0], colorspace);
			OS.CGColorSpaceRelease (colorspace);
			OS.CGContextSetFillColor (context [0], parent.getForegroundColor ().handle);
		}
		int [] ptr = new int [1];
		OS.GetControlData (labelHandle, (short) 0, OS.kControlStaticTextCFStringTag, 4, ptr, null);
		OS.HIThemeDrawTextBox (ptr [0], rect, info, context [0], OS.kHIThemeOrientationNormal);
		OS.CFRelease (ptr [0]);
		return OS.noErr;
	}
	if (control == iconHandle && OS.VERSION >= 0x1040) {
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
		if (image != null) {
			int imageHandle = image.handle;
			int alphaInfo = OS.CGImageGetAlphaInfo (imageHandle);
			if (alphaInfo == OS.kCGImageAlphaFirst) {
				int [] buffer = new int [1];
				OS.GetEventParameter (theEvent, OS.kEventParamCGContextRef, OS.typeCGContextRef, null, 4, null, buffer);
				int context = buffer [0];
				CGRect rect = new CGRect ();
				OS.HIViewGetBounds (iconHandle, rect);
				if (partCode != 0) {
					OS.HICreateTransformedCGImage (imageHandle, OS.kHITransformSelected, buffer);
					imageHandle = buffer [0];
				} else if ((state & DISABLED) != 0) {
					OS.HICreateTransformedCGImage (imageHandle, OS.kHITransformDisabled, buffer);
					imageHandle = buffer [0];
				}
				OS.HIViewDrawCGImage (context, rect, imageHandle);
				if (imageHandle != image.handle) OS.CGImageRelease (imageHandle);
				return OS.noErr;
			}
		}
	}
	return super.callPaintEventHandler (control, damageRgn, visibleRgn, theEvent, nextHandler);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.PUSH, SWT.CHECK, SWT.RADIO, SWT.SEPARATOR, SWT.DROP_DOWN, 0);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

int colorProc (int inControl, int inMessage, int inDrawDepth, int inDrawInColor) {
	switch (inMessage) {
		case OS.kControlMsgApplyTextColor: {
			if (parent.foreground != null) {
				OS.RGBForeColor (toRGBColor (parent.foreground));
			} else {
				OS.SetThemeTextColor ((short) OS.kThemeTextColorDialogActive, (short) inDrawDepth, inDrawInColor != 0);
			}
			return OS.noErr;
		}
		case OS.kControlMsgSetUpBackground: {
			float [] background = parent.background != null ? parent.background : parent.getParentBackground ();
			if (background != null) {
				OS.RGBBackColor (toRGBColor (background));
			} else {
				OS.SetThemeBackground ((short) OS.kThemeBrushDialogBackgroundActive, (short) inDrawDepth, inDrawInColor != 0);
			}
			return OS.noErr;
		}
	}
	return OS.eventNotHandledErr;
}

Point computeSize () {
//	checkWidget();
	int width = 0, height = 0;
	if ((style & SWT.SEPARATOR) != 0) {
		if ((parent.style & SWT.HORIZONTAL) != 0) {
			width = getWidth ();
			height = DEFAULT_HEIGHT;
		} else {
			width = DEFAULT_WIDTH;
			height = getWidth ();
		}
		if (control != null) {
			height = Math.max (height, control.getMininumHeight ());
		}
	} else {
		int space = 0;
		if (text.length () != 0 || image != null) {
			int stringWidth = 0, stringHeight = 0;
			if (text.length () != 0) {
				Point size = textExtent ();
				stringWidth = size.x;
				stringHeight = size.y;
			}
			int imageWidth = 0, imageHeight = 0;
			if (image != null) {
				if (text.length () != 0) space = 2;
				Rectangle rect = image.getBounds ();
				imageWidth = rect.width;
				imageHeight = rect.height;
			}
			if ((parent.style & SWT.RIGHT) != 0) {
				width = stringWidth + imageWidth + space;
				height = Math.max (stringHeight, imageHeight);
			} else {
				width = Math.max (stringWidth, imageWidth);
				height = stringHeight + imageHeight + space;
			}
		} else {
			width = DEFAULT_WIDTH;
			height = DEFAULT_HEIGHT;
		}
		if ((style & SWT.DROP_DOWN) != 0) {
			width += ARROW_WIDTH;
		}
		width += INSET * 2;
		height += INSET * 2;
	}
	return new Point (width, height);
}

boolean contains (String [] array, String element) {
	for (int i = 0; i < array.length; i++) {
		if (array [i].equals (element)) {
			return true;
		}
	}
	return false;
}

void createHandle () {
	int [] outControl = new int [1];
	int window = OS.GetControlOwner (parent.handle);
	int features = OS.kControlSupportsEmbedding | OS.kControlWantsActivate;
	OS.CreateUserPaneControl (window, null, features, outControl);
	if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
	handle = outControl [0];
	OS.HIObjectSetAccessibilityIgnored (handle, true);
	if ((style & SWT.SEPARATOR) == 0) {
		ControlButtonContentInfo inContent = new ControlButtonContentInfo ();
		OS.CreateIconControl(window, null, inContent, false, outControl);
		if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
		iconHandle = outControl [0];
		OS.HIObjectSetAccessibilityIgnored (iconHandle, true);
		ControlFontStyleRec fontStyle = new ControlFontStyleRec ();
		Font font = parent.font;
		if (font != null) {
			short [] family = new short [1], style = new short [1];
			OS.FMGetFontFamilyInstanceFromFont (font.handle, family, style);
			fontStyle.flags |= OS.kControlUseFontMask | OS.kControlUseSizeMask | OS.kControlUseFaceMask;
			fontStyle.font = family [0];
			fontStyle.style = (short) (style [0] | font.style);
			fontStyle.size = (short) font.size;
		} else {
			fontStyle.flags = (short) OS.kControlUseThemeFontIDMask;
			fontStyle.font = (short) parent.defaultThemeFont ();
		}
		OS.CreateStaticTextControl (window, null, 0, fontStyle, outControl);
		if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
		labelHandle = outControl [0];
		OS.HIObjectSetAccessibilityIgnored (labelHandle, true);
	}
}

void createWidget () {
	super.createWidget ();
	setZOrder ();
	toolTipText = "";
}

void deregister () {
	super.deregister ();
	display.removeWidget (handle);
	if (iconHandle != 0) display.removeWidget (iconHandle);
	if (labelHandle != 0) display.removeWidget (labelHandle);
}

void destroyWidget () {
	parent.destroyItem (this);
	int theControl = handle;
	releaseHandle ();
	if (theControl != 0) {
		OS.DisposeControl (theControl);
	}
}

void drawBackground (int control, int context) {
	if (control == handle && getSelection ()) {
		CGRect rect = new CGRect();
		OS.HIViewGetBounds (handle, rect);
		OS.CGContextSaveGState (context);
		OS.CGContextSetFillColor (context, new float[]{0.1f, 0.1f, 0.1f, 0.1f});
		OS.CGContextFillRect (context, rect);
		OS.CGContextSetStrokeColor (context, new float[]{0.2f, 0.2f, 0.2f, 0.2f});
		rect.x += 0.5f;
		rect.y += 0.5f;
		rect.width -= 1;
		rect.height -= 1;
		OS.CGContextStrokeRect (context, rect);
		OS.CGContextRestoreGState (context);
	}
}

void drawWidget (int control, int context, int damageRgn, int visibleRgn, int theEvent) {
	if (control == handle && (style & (SWT.DROP_DOWN | SWT.SEPARATOR)) != 0) {
		int state;
		if (OS.IsControlEnabled (control)) {
			state = OS.IsControlActive (control) ? OS.kThemeStateActive : OS.kThemeStateInactive;
		} else {
			state = OS.IsControlActive (control) ? OS.kThemeStateUnavailable : OS.kThemeStateUnavailableInactive;
		}
		CGRect rect = new CGRect ();
		OS.HIViewGetBounds (handle, rect);
		if ((style & SWT.SEPARATOR) != 0 && this.control == null) {
			rect.y += 2;
			rect.height -= 4;
			HIThemeSeparatorDrawInfo info = new HIThemeSeparatorDrawInfo ();
			info.state = state;
			OS.HIThemeDrawSeparator (rect, info, context, OS.kHIThemeOrientationNormal);
		}
		if ((style & SWT.DROP_DOWN) != 0) {
			rect.y = rect.height / 2 - 1;
			rect.x = rect.width - ARROW_WIDTH;
			HIThemePopupArrowDrawInfo info = new HIThemePopupArrowDrawInfo ();
			info.state = state;
			info.orientation = (short) OS.kThemeArrowDown;
			info.size = (short) OS.kThemeArrow5pt;
			OS.HIThemeDrawPopupArrow (rect, info, context, OS.kHIThemeOrientationNormal);
		}
	}
}

String [] getAxAttributes () {
	return AX_ATTRIBUTES;
}

/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent.
 *
 * @return the receiver's bounding rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle getBounds () {
	checkWidget();
	return getControlBounds (handle);
}

/**
 * Returns the control that is used to fill the bounds of
 * the item when the item is a <code>SEPARATOR</code>.
 *
 * @return the control
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Control getControl () {
	checkWidget();
	return control;
}

/**
 * Returns the receiver's disabled image if it has one, or null
 * if it does not.
 * <p>
 * The disabled image is displayed when the receiver is disabled.
 * </p>
 *
 * @return the receiver's disabled image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getDisabledImage () {
	checkWidget();
	return disabledImage;
}

/**
 * Returns <code>true</code> if the receiver is enabled, and
 * <code>false</code> otherwise. A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #isEnabled
 */
public boolean getEnabled () {
	checkWidget();
	return (state & DISABLED) == 0;
}

boolean getDrawing () {
	return parent.getDrawing ();
}

/**
 * Returns the receiver's hot image if it has one, or null
 * if it does not.
 * <p>
 * The hot image is displayed when the mouse enters the receiver.
 * </p>
 *
 * @return the receiver's hot image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getHotImage () {
	checkWidget();
	return hotImage;
}

/**
 * Returns the receiver's parent, which must be a <code>ToolBar</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public ToolBar getParent () {
	checkWidget();
	return parent;
}

/**
 * Returns <code>true</code> if the receiver is selected,
 * and false otherwise.
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked (which some platforms draw as a
 * pushed in button). If the receiver is of any other type, this method
 * returns false.
 * </p>
 *
 * @return the selection state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getSelection () {
	checkWidget();
	if ((style & (SWT.CHECK | SWT.RADIO)) == 0) return false;
	return selection;
}

/**
 * Returns the receiver's tool tip text, or null if it has not been set.
 *
 * @return the receiver's tool tip text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getToolTipText () {
	checkWidget();
	return toolTipText;
}

/**
 * Gets the width of the receiver.
 *
 * @return the width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getWidth () {
	checkWidget();
	return width;
}

int getVisibleRegion (int control, boolean clipChildren) {
	if (visibleRgn == 0) {
		visibleRgn = OS.NewRgn ();
		calculateVisibleRegion (control, visibleRgn, false);
	}
	int result = OS.NewRgn ();
	OS.CopyRgn (visibleRgn, result);
	return result;
}

int helpProc (int inControl, int inGlobalMouse, int inRequest, int outContentProvided, int ioHelpContent) {
	if (parent.toolTipText == null) {
	    switch (inRequest) {
			case OS.kHMSupplyContent: {
				short [] contentProvided = { OS.kHMContentNotProvided };
				if (toolTipText != null && toolTipText.length () != 0) {
					char [] buffer = new char [toolTipText.length ()];
					toolTipText.getChars (0, buffer.length, buffer, 0);
					int length = fixMnemonic (buffer);
					if (display.helpString != 0) OS.CFRelease (display.helpString);
					display.helpString = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, length);
					HMHelpContentRec helpContent = new HMHelpContentRec ();
					OS.memmove (helpContent, ioHelpContent, HMHelpContentRec.sizeof);
					helpContent.version = OS.kMacHelpVersion;
					helpContent.tagSide = (short) OS.kHMDefaultSide;
					display.helpWidget = null;
					helpContent.absHotRect_left = (short) 0;
					helpContent.absHotRect_top = (short) 0;
					helpContent.absHotRect_right = (short) 0;
					helpContent.absHotRect_bottom = (short) 0;
					helpContent.content0_contentType = OS.kHMCFStringContent;
					helpContent.content0_tagCFString = display.helpString;
					helpContent.content1_contentType = OS.kHMCFStringContent;
					helpContent.content1_tagCFString = display.helpString;
					OS.memmove (ioHelpContent, helpContent, HMHelpContentRec.sizeof);
					contentProvided [0] = OS.kHMContentProvided;
				}
				OS.memmove (outContentProvided, contentProvided, 2);
				break;
			}
			case OS.kHMDisposeContent: {
				if (display.helpString != 0) OS.CFRelease (display.helpString);
				display.helpWidget = null;
				display.helpString = 0;
				break;
			}
	    }
		return OS.noErr;
	}
	return parent.helpProc (inControl, inGlobalMouse, inRequest, outContentProvided, ioHelpContent);
}

void hookEvents () {
	super.hookEvents ();
	int controlProc = display.controlProc;
	int colorProc = display.colorProc;
	int [] mask1 = new int [] {
		OS.kEventClassControl, OS.kEventControlDraw,
		OS.kEventClassControl, OS.kEventControlHit,
		OS.kEventClassControl, OS.kEventControlHitTest,
		OS.kEventClassControl, OS.kEventControlTrack,
	};
	int controlTarget = OS.GetControlEventTarget (handle);
	OS.InstallEventHandler (controlTarget, controlProc, mask1.length / 2, mask1, handle, null);
	int [] mask2 = new int [] {
		OS.kEventClassControl, OS.kEventControlDraw,
		OS.kEventClassControl, OS.kEventControlHitTest,
		OS.kEventClassControl, OS.kEventControlTrack,
		OS.kEventClassControl, OS.kEventControlGetClickActivation,
	};
	int accessibilityProc = display.accessibilityProc;
	int [] mask3 = new int [] {
		OS.kEventClassAccessibility, OS.kEventAccessibleGetAllAttributeNames,
		OS.kEventClassAccessibility, OS.kEventAccessibleGetNamedAttribute,
	};
	if (iconHandle != 0) {
		controlTarget = OS.GetControlEventTarget (iconHandle);
		OS.InstallEventHandler (controlTarget, controlProc, mask2.length / 2, mask2, iconHandle, null);
		OS.SetControlColorProc (iconHandle, colorProc);
		OS.SetControlAction (iconHandle, display.actionProc);
		OS.InstallEventHandler (controlTarget, accessibilityProc, mask3.length / 2, mask3, iconHandle, null);
	}
	if (labelHandle != 0) {
		controlTarget = OS.GetControlEventTarget (labelHandle);
		OS.InstallEventHandler (controlTarget, controlProc, mask2.length / 2, mask2, labelHandle, null);
		OS.SetControlColorProc (labelHandle, colorProc);
		OS.SetControlAction (labelHandle, display.actionProc);
	}
	if ((style & SWT.SEPARATOR) == 0) {
		OS.SetControlAction (handle, display.actionProc);
	}
	int helpProc = display.helpProc;
	OS.HMInstallControlContentCallback (handle, helpProc);
	OS.SetControlColorProc (handle, colorProc);
}

void invalidateVisibleRegion (int control) {
	resetVisibleRegion (control);
	parent.resetVisibleRegion (control);
}

void invalWindowRgn (int window, int rgn) {
	parent.invalWindowRgn (window, rgn);
}

boolean isDrawing () {
	return getDrawing() && parent.isDrawing();
}

/**
 * Returns <code>true</code> if the receiver is enabled and all
 * of the receiver's ancestors are enabled, and <code>false</code>
 * otherwise. A disabled control is typically not selectable from the
 * user interface and draws with an inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #getEnabled
 */
public boolean isEnabled () {
	checkWidget();
	return getEnabled () && parent.isEnabled ();
}

int kEventAccessibleGetAllAttributeNames (int nextHandler, int theEvent, int userData) {
	int code = OS.eventNotHandledErr;
	String [] attributes = getAxAttributes ();
	if (attributes != null) {
		OS.CallNextEventHandler (nextHandler, theEvent);
		int [] arrayRef = new int [1];
		OS.GetEventParameter (theEvent, OS.kEventParamAccessibleAttributeNames, OS.typeCFMutableArrayRef, null, 4, null, arrayRef);
		int attributesArrayRef = arrayRef [0];
		int length = OS.CFArrayGetCount (attributesArrayRef);
		String[] osAttributes = new String [length];
		for (int i = 0; i < length; i++) {
			int stringRef = OS.CFArrayGetValueAtIndex (attributesArrayRef, i);
			int strLength = OS.CFStringGetLength (stringRef);
			char [] buffer = new char [strLength];
			CFRange range = new CFRange ();
			range.length = strLength;
			OS.CFStringGetCharacters (stringRef, range, buffer);
			osAttributes [i] = new String (buffer);
		}
		for (int i = 0; i < attributes.length; i++) {
			if (!contains (osAttributes, attributes [i])) {
				String string = attributes [i];
				char [] buffer = new char [string.length ()];
				string.getChars (0, buffer.length, buffer, 0);
				int stringRef = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
				OS.CFArrayAppendValue (attributesArrayRef, stringRef);
				OS.CFRelease (stringRef);
			}
		}
		code = OS.noErr;
	}
	if (parent.accessible != null) {
		code = parent.accessible.internal_kEventAccessibleGetAllAttributeNames (nextHandler, theEvent, code);
	}
	return code;
}

int kEventAccessibleGetNamedAttribute (int nextHandler, int theEvent, int userData) {
	int code = OS.eventNotHandledErr;
	int [] stringRef = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamAccessibleAttributeName, OS.typeCFStringRef, null, 4, null, stringRef);
	int length = 0;
	if (stringRef [0] != 0) length = OS.CFStringGetLength (stringRef [0]);
	char [] buffer = new char [length];
	CFRange range = new CFRange ();
	range.length = length;
	OS.CFStringGetCharacters (stringRef [0], range, buffer);
	String attributeName = new String(buffer);
	if (attributeName.equals (OS.kAXRoleAttribute) || attributeName.equals (OS.kAXRoleDescriptionAttribute)) {
		String roleText = ((style & SWT.PUSH) != 0) ? OS.kAXButtonRole
				: ((style & SWT.RADIO) != 0) ? OS.kAXRadioButtonRole
				: ((style & SWT.CHECK) != 0) ? OS.kAXCheckBoxRole
				: ((style & SWT.DROP_DOWN) != 0) ? OS.kAXMenuButtonRole
				: null; // SEPARATOR
		if (roleText != null) {
			buffer = new char [roleText.length ()];
			roleText.getChars (0, buffer.length, buffer, 0);
			stringRef [0] = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
			if (stringRef [0] != 0) {
				if (attributeName.equals (OS.kAXRoleAttribute)) {
					OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, stringRef);
				} else { // kAXRoleDescriptionAttribute
					int stringRef2 = OS.HICopyAccessibilityRoleDescription (stringRef [0], 0);
					OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, new int [] {stringRef2});
					OS.CFRelease(stringRef2);
				}
				OS.CFRelease(stringRef [0]);
				code = OS.noErr;
			}
		}
	} else if (attributeName.equals (OS.kAXTitleAttribute) || attributeName.equals (OS.kAXDescriptionAttribute)) {
		String accessibleText = toolTipText;
		if (accessibleText == null || accessibleText.equals("")) accessibleText = text;
		if (!(accessibleText == null || accessibleText.equals(""))) {
			buffer = new char [accessibleText.length ()];
			accessibleText.getChars (0, buffer.length, buffer, 0);
			int ref = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
			OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, new int [] {ref});
			OS.CFRelease(ref);
			code = OS.noErr;
		}
	} else if (attributeName.equals (OS.kAXValueAttribute) && (style & (SWT.CHECK | SWT.RADIO)) != 0) {
		OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeSInt32, 4, new int [] {selection ? 1 : 0});
		code = OS.noErr;
	}
	if (parent.accessible != null) {
		code = parent.accessible.internal_kEventAccessibleGetNamedAttribute (nextHandler, theEvent, code);
	}
	return code;
}

int kEventControlGetClickActivation (int nextHandler, int theEvent, int userData) {
	return parent.kEventControlGetClickActivation (nextHandler, theEvent, userData);
}

int kEventControlHit (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlHit (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	if ((style & SWT.RADIO) != 0) {
		if ((parent.getStyle () & SWT.NO_RADIO_GROUP) == 0) {
			selectRadio ();
		}
	}
	if ((style & SWT.CHECK) != 0) setSelection (!getSelection ());
	sendSelectionEvent (SWT.Selection);
	return OS.eventNotHandledErr;
}

int kEventControlHitTest (int nextHandler, int theEvent, int userData) {
	/*
	* Feature in the Macintosh.  When kWindowCompositingAttribute is
	* set in the window, controls within the window are selected when
	* any button is pressed, not just the left one.  When the control
	* has a menu, this causes both selection and a menu to be displayed.
	* The fix is to check for button two and avoid setting the part
	* code, which stops the selection from happening.
	*/		
	if (display.clickCountButton == 2) return OS.noErr;
	int [] theControl = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamDirectObject, OS.typeControlRef, null, 4, null, theControl);
	if (theControl [0] == labelHandle || (theControl [0] == handle && (style & SWT.SEPARATOR) == 0)) {
		CGRect rect = new CGRect ();
		OS.HIViewGetBounds (theControl [0], rect);
		CGPoint pt = new CGPoint ();
		OS.GetEventParameter (theEvent, OS.kEventParamMouseLocation, OS.typeHIPoint, null, CGPoint.sizeof, null, pt);
		if (OS.CGRectContainsPoint (rect, pt) != 0 && (theControl [0] != handle || (style & SWT.DROP_DOWN) == 0 || rect.width - pt.x >= ARROW_WIDTH + INSET)) {
			OS.SetEventParameter (theEvent, OS.kEventParamControlPart, OS.typeControlPartCode, 2, new short[]{(short)1});
			return OS.noErr;
		}
	}
	return OS.eventNotHandledErr;
}

int kEventControlTrack (int nextHandler, int theEvent, int userData) {
	int result = parent.kEventControlTrack (nextHandler, theEvent, userData);
	if (isDisposed ()) return OS.noErr;
	partCode = 0;
	if (text.length () > 0 && labelHandle != 0) {
		redrawWidget (labelHandle, false);
	}
	if (image != null && iconHandle != 0) {
		OS.SetControlData (iconHandle, OS.kControlEntireControl, OS.kControlIconTransformTag, 2, new short [] {(short) 0});
		redrawWidget (iconHandle, false);
	}
	return result;
}

int kEventMouseDown (int nextHandler, int theEvent, int userData) {
	int result = parent.kEventMouseDown (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	
	if ((style & SWT.DROP_DOWN) != 0) {
		CGPoint pt = new CGPoint ();
		OS.GetEventParameter (theEvent, OS.kEventParamWindowMouseLocation, OS.typeHIPoint, null, CGPoint.sizeof, null, pt);
		OS.HIViewConvertPoint (pt, 0, handle);
		CGRect rect = new CGRect ();
		OS.HIViewGetFrame (handle, rect);
		int x = (int) pt.x;
		int width = (int) rect.width;
		if (width - x < ARROW_WIDTH + INSET) {
			OS.HIViewConvertPoint (pt, handle, parent.handle);
			Event event = new Event ();
			event.detail = SWT.ARROW;
			event.x = (int) rect.x;
			event.y = (int) (rect.y + rect.height);
			sendSelectionEvent (SWT.Selection, event, false);				
		}
	}	
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
	display.addWidget (handle, this);
	if (iconHandle != 0) display.addWidget (iconHandle, this);
	if (labelHandle != 0) display.addWidget (labelHandle, this);
}


void releaseParent () {
	super.releaseParent ();
	setVisible (false);
}

void releaseHandle () {
	super.releaseHandle ();
	handle = iconHandle = labelHandle = 0;
	parent = null;
}

void releaseWidget () {
	super.releaseWidget ();
	if (cIcon != 0) destroyCIcon (cIcon);
	cIcon = 0;
	if (visibleRgn != 0) OS.DisposeRgn (visibleRgn);
	visibleRgn = 0;
	control = null;
	toolTipText = null;
	image = disabledImage = hotImage = null; 
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is selected by the user.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #addSelectionListener
 */
public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Selection, listener);
	eventTable.unhook(SWT.DefaultSelection,listener);	
}

void resetVisibleRegion (int control) {
	if (visibleRgn != 0) {
		OS.DisposeRgn (visibleRgn);
		visibleRgn = 0;
	}
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

void setBackground (float [] color) {
	parent.setBackground (handle, color);
	if (labelHandle != 0) {
		parent.setBackground (labelHandle, color);
	}
	if (iconHandle != 0) parent.setBackground (iconHandle, color);
}

void setBounds (int x, int y, int width, int height) {
	setBounds (handle, x, y, width, height, true, true, false);
	if ((style & SWT.SEPARATOR) != 0) return;
	int space = 0;
	int stringWidth = 0, stringHeight = 0;
	if (text.length () != 0) {
		Point size = textExtent ();
		stringWidth = size.x;
		stringHeight = size.y;
	}
	int imageWidth = 0, imageHeight = 0;
	if (image != null) {
		if (text.length () != 0) space = 2;
		Rectangle rect = image.getBounds ();
		imageWidth = rect.width;
		imageHeight = rect.height;
	}
	int arrowWidth = 0;
	if ((style & SWT.DROP_DOWN) != 0) {
		arrowWidth = ARROW_WIDTH;
	}
	if ((parent.style & SWT.RIGHT) != 0) {
		int imageX = INSET;
		int imageY = INSET + (height - (INSET * 2) - imageHeight) / 2;
		setBounds (iconHandle, imageX, imageY, imageWidth, imageHeight, true, true, false);
		int labelX = imageX + imageWidth + space;
		int labelY = INSET + (height - (INSET * 2) - stringHeight) / 2;
		setBounds (labelHandle, labelX, labelY, stringWidth, stringHeight, true, true, false);
	} else {
		int imageX = INSET + (width - (INSET * 2) - arrowWidth - imageWidth) / 2;
		int imageY = INSET + (height - imageHeight - stringHeight - INSET * 2) / 2;
		setBounds (iconHandle, imageX, imageY, imageWidth, imageHeight, true, true, false);
		int labelX = INSET + (width - (INSET * 2) - arrowWidth - stringWidth) / 2;
		int labelY = imageY + imageHeight + space;
		setBounds (labelHandle, labelX, labelY, stringWidth, stringHeight, true, true, false);
	}
}

/**
 * Sets the control that is used to fill the bounds of
 * the item when the item is a <code>SEPARATOR</code>.
 *
 * @param control the new control
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li> 
 *    <li>ERROR_INVALID_PARENT - if the control is not in the same widget tree</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setControl (Control control) {
	checkWidget();
	if (control != null) {
		if (control.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (control.parent != parent) error (SWT.ERROR_INVALID_PARENT);
	}
	if ((style & SWT.SEPARATOR) == 0) return;
	if (this.control == control) return;
	this.control = control;
	redrawWidget (handle, false);
	if (control != null && !control.isDisposed ()) {
		control.moveAbove (null);
	}
	parent.relayout ();
}

/**
 * Enables the receiver if the argument is <code>true</code>,
 * and disables it otherwise.
 * <p>
 * A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 * </p>
 *
 * @param enabled the new enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setEnabled (boolean enabled) {
	checkWidget();
	if ((state & DISABLED) == 0 && enabled) return;
	if (enabled) {
		state &= ~DISABLED;
		OS.EnableControl (handle);
	} else {
		state |= DISABLED;
		OS.DisableControl (handle);
	}
}

void setFontStyle (Font font) {
	parent.setFontStyle (labelHandle, font);
}

void setForeground (float [] color) {
	parent.setForeground (handle, color);
	if (labelHandle != 0) {
		parent.setForeground (labelHandle, color);
	}
	if (iconHandle != 0) parent.setForeground (iconHandle, color);
}

/**
 * Sets the receiver's disabled image to the argument, which may be
 * null indicating that no disabled image should be displayed.
 * <p>
 * The disabled image is displayed when the receiver is disabled.
 * </p>
 *
 * @param image the disabled image to display on the receiver (may be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setDisabledImage (Image image) {
	checkWidget();
	if (image != null && image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	disabledImage = image;
	updateImage (true);
}

/**
 * Sets the receiver's hot image to the argument, which may be
 * null indicating that no hot image should be displayed.
 * <p>
 * The hot image is displayed when the mouse enters the receiver.
 * </p>
 *
 * @param image the hot image to display on the receiver (may be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setHotImage (Image image) {
	checkWidget();
	if (image != null && image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	hotImage = image;
	updateImage (true);
}

public void setImage (Image image) {
	checkWidget();
	if (image != null && image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	super.setImage (image);
	updateImage (true);
	OS.HIObjectSetAccessibilityIgnored (iconHandle, image == null);
}

boolean setRadioSelection (boolean value) {
	if ((style & SWT.RADIO) == 0) return false;
	if (getSelection () != value) {
		setSelection (value);
		sendSelectionEvent (SWT.Selection);
	}
	return true;
}

/**
 * Sets the selection state of the receiver.
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked (which some platforms draw as a
 * pushed in button).
 * </p>
 *
 * @param selected the new selection state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (boolean selected) {
	checkWidget();
	if ((style & (SWT.CHECK | SWT.RADIO)) == 0) return;
	this.selection = selected;
	redrawWidget (handle, true);
}

/**
 * Sets the receiver's text. The string may include
 * the mnemonic character.
 * </p>
 * <p>
 * Mnemonics are indicated by an '&amp;' that causes the next
 * character to be the mnemonic.  When the user presses a
 * key sequence that matches the mnemonic, a selection
 * event occurs. On most platforms, the mnemonic appears
 * underlined but may be emphasised in a platform specific
 * manner.  The mnemonic indicator character '&amp;' can be
 * escaped by doubling it in the string, causing a single
 * '&amp;' to be displayed.
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
	super.setText (string);
	char [] buffer = new char [text.length ()];
	text.getChars (0, buffer.length, buffer, 0);
	int length = fixMnemonic (buffer);
	int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, length);
	if (ptr == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
	OS.SetControlData (labelHandle, 0 , OS.kControlStaticTextCFStringTag, 4, new int[]{ptr});
	OS.CFRelease (ptr);
	redrawWidget (labelHandle, false);
	parent.relayout ();
	OS.HIObjectSetAccessibilityIgnored (labelHandle, string.equals(""));
}

/**
 * Sets the receiver's tool tip text to the argument, which
 * may be null indicating that the default tool tip for the 
 * control will be shown. For a control that has a default
 * tool tip, such as the Tree control on Windows, setting
 * the tool tip text to an empty string replaces the default,
 * causing no tool tip text to be shown.
 * <p>
 * The mnemonic indicator (character '&amp;') is not displayed in a tool tip.
 * To display a single '&amp;' in the tool tip, the character '&amp;' can be 
 * escaped by doubling it in the string.
 * </p>
 * 
 * @param string the new tool tip text (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setToolTipText (String string) {
	checkWidget();
	toolTipText = string;
}

void setVisible (boolean visible) {
	if (visible) {
		if ((state & HIDDEN) == 0) return;
		state &= ~HIDDEN;
	} else {
		if ((state & HIDDEN) != 0) return;
		state |= HIDDEN;
	}
	setVisible (handle, visible);
}

/**
 * Sets the width of the receiver, for <code>SEPARATOR</code> ToolItems.
 *
 * @param width the new width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setWidth (int width) {
	checkWidget();
	if ((style & SWT.SEPARATOR) == 0) return;
	if (width < 0 || this.width == width) return;
	this.width = width;
	redrawWidget (handle, false);
	parent.relayout();
}

void setZOrder () {
	OS.HIViewAddSubview (parent.handle, handle);
	if (iconHandle != 0) OS.HIViewAddSubview (handle, iconHandle);
	if (labelHandle != 0) OS.HIViewAddSubview (handle, labelHandle);
}

void updateImage (boolean layout) {
	if ((style & SWT.SEPARATOR) != 0) return;
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
	if (layout) {
		redrawWidget (iconHandle, false);
		parent.relayout();
	}
}

Point textExtent () {
	int [] ptr = new int [1];
	OS.GetControlData (labelHandle, (short) 0, OS.kControlStaticTextCFStringTag, 4, ptr, null);
	Point result = parent.textExtent (ptr [0], 0);
	if (ptr [0] != 0) OS.CFRelease (ptr [0]);
	return result;
}

}

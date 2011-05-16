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


import org.eclipse.swt.internal.carbon.CGRect;
import org.eclipse.swt.internal.carbon.ControlFontStyleRec;
import org.eclipse.swt.internal.carbon.HILayoutInfo;
import org.eclipse.swt.internal.carbon.OS;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class represent a non-selectable
 * user interface object that displays a string or image.
 * When SEPARATOR is specified, displays a single
 * vertical or horizontal line.
 * <p>
 * Shadow styles are hints and may not be honored
 * by the platform.  To create a separator label
 * with the default shadow style for the platform,
 * do not specify a shadow style.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SEPARATOR, HORIZONTAL, VERTICAL</dd>
 * <dd>SHADOW_IN, SHADOW_OUT, SHADOW_NONE</dd>
 * <dd>CENTER, LEFT, RIGHT, WRAP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of SHADOW_IN, SHADOW_OUT and SHADOW_NONE may be specified.
 * SHADOW_NONE is a HINT. Only one of HORIZONTAL and VERTICAL may be specified.
 * Only one of CENTER, LEFT and RIGHT may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#label">Label snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Label extends Control {
	String text = "";
	Image image;
	boolean isImage;

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
 * @see SWT#SEPARATOR
 * @see SWT#HORIZONTAL
 * @see SWT#VERTICAL
 * @see SWT#SHADOW_IN
 * @see SWT#SHADOW_OUT
 * @see SWT#SHADOW_NONE
 * @see SWT#CENTER
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#WRAP
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Label (Composite parent, int style) {
	super (parent, checkStyle (style));
}

void addRelation (Control control) {
	if (!control.isDescribedByLabel ()) return;
	
	int labelElement = OS.AXUIElementCreateWithHIObjectAndIdentifier (handle, 0);
	String string = OS.kAXTitleUIElementAttribute;  // control LabeledBy this
	char [] buffer = new char [string.length ()];
	string.getChars (0, buffer.length, buffer, 0);
	int stringRef = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
	OS.HIObjectSetAuxiliaryAccessibilityAttribute(control.focusHandle (), 0, stringRef, labelElement);
	OS.CFRelease(labelElement);
	OS.CFRelease(stringRef);
	
	int relatedElement = OS.AXUIElementCreateWithHIObjectAndIdentifier (control.focusHandle (), 0);
	int array = OS.CFArrayCreateMutable(OS.kCFAllocatorDefault, 1, 0);
	OS.CFArrayAppendValue(array, relatedElement);
	string = OS.kAXServesAsTitleForUIElementsAttribute;  // this LabelFor control
	buffer = new char [string.length ()];
	string.getChars (0, buffer.length, buffer, 0);
	stringRef = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
	OS.HIObjectSetAuxiliaryAccessibilityAttribute(handle, 0, stringRef, array);
	OS.CFRelease(relatedElement);
	OS.CFRelease(stringRef);
	OS.CFRelease(array);
}

static int checkStyle (int style) {
	style |= SWT.NO_FOCUS;
	if ((style & SWT.SEPARATOR) != 0) {
		style = checkBits (style, SWT.VERTICAL, SWT.HORIZONTAL, 0, 0, 0, 0);
		return checkBits (style, SWT.SHADOW_OUT, SWT.SHADOW_IN, SWT.SHADOW_NONE, 0, 0, 0);
	} 
	return checkBits (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int width = 0, height = 0;
	if ((style & SWT.SEPARATOR) != 0) {
		if ((style & SWT.HORIZONTAL) != 0) {
			width = DEFAULT_WIDTH;
			height = 3;
		} else {
			width = 3;
			height = DEFAULT_HEIGHT;
		}
	} else {
		if (isImage && image != null) {
			Rectangle r = image.getBounds ();
			width = r.width;
			height = r.height;
		} else {			
			int [] ptr = new int [1];
			OS.GetControlData (handle, (short) 0 , OS.kControlStaticTextCFStringTag, 4, ptr, null);
			Point size = textExtent (ptr [0], (style & SWT.WRAP) != 0 && wHint != SWT.DEFAULT ? wHint : 0);
			if (ptr [0] != 0) OS.CFRelease (ptr [0]);
			width = size.x;
			height = size.y;			
		}
	}
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	return new Point (width, height);
}

void createHandle () {
	state |= GRAB | THEME_BACKGROUND;
	int [] outControl = new int [1];
	int window = OS.GetControlOwner (parent.handle);
	if ((style & SWT.SEPARATOR) != 0) {
		/*
		 * Feature in Carbon: Separator control decides how to orient itself
		 * based on the width and height. If height >= width it orients
		 * vertically, else it orients horizontally. 
		 * Fix is to have two native controls to implement the separator label.
		 * The top control (userPaneControl) honors the bounds set by the
		 * user and the inner one (separatorControl) creates the separator
		 * with the correct orientation.
		 */
		int features = OS.kControlSupportsEmbedding;
		OS.CreateUserPaneControl (window, null, features, outControl);
		if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
		handle = outControl [0];
		outControl[0] = 0;
		OS.CreateSeparatorControl (window, null, outControl);
		if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
		int separatorHandle = outControl [0];
		OS.HIViewAddSubview (handle, separatorHandle);
		CGRect r = new CGRect();
		r.width = DEFAULT_WIDTH;
		r.height = DEFAULT_HEIGHT;
		OS.HIViewSetFrame (handle, r);
		HILayoutInfo layout = new HILayoutInfo ();
		layout.version = 0;
		OS.HIViewGetLayoutInfo (separatorHandle, layout);
		if ((style & SWT.HORIZONTAL) != 0) {
			r.height = 3;
			layout.scale.x.ratio = 1.0f;
			layout.position.y.kind = OS.kHILayoutPositionCenter;
		} else {
			r.width = 3;
			layout.position.x.kind = OS.kHILayoutPositionCenter;
			layout.scale.y.ratio = 1.0f;
		}
		OS.HIViewSetFrame (separatorHandle, r);
		OS.HIViewSetLayoutInfo (separatorHandle, layout);
	} else {
		int just = OS.teFlushLeft;
		if ((style & SWT.CENTER) != 0) just = OS.teCenter;
		if ((style & SWT.RIGHT) != 0) just = OS.teFlushRight;
		ControlFontStyleRec fontStyle = new ControlFontStyleRec ();
		fontStyle.flags |= OS.kControlUseJustMask;
		fontStyle.just = (short) just;
		OS.CreateStaticTextControl (window, null, 0, fontStyle, outControl);
		if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
		handle = outControl [0];
		if ((style & SWT.WRAP) == 0) {
			OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlStaticTextIsMultilineTag, 1, new byte[] {0});
		}
	}
}

int defaultThemeFont () {
	if (display.smallFonts) return OS.kThemeSmallSystemFont;
	return OS.kThemePushButtonFont;
}

void drawBackground (int control, int context) {
	fillBackground (control, context, null);
}

void drawWidget (int control, int context, int damageRgn, int visibleRgn, int theEvent) {
	if (isImage && image != null) {
		GCData data = new GCData ();
		data.paintEvent = theEvent;
		data.visibleRgn = visibleRgn;
		GC gc = GC.carbon_new (this, data);
		int x = 0;
		Point size = getSize ();
		Rectangle bounds = image.getBounds ();
		if ((style & SWT.CENTER) != 0) x = (size.x - bounds.width) / 2;
		if ((style & SWT.RIGHT) != 0) x = size.x - bounds.width;
		gc.drawImage (image, x, 0);
		gc.dispose ();
	}
	super.drawWidget (control, context, damageRgn, visibleRgn, theEvent);
}

/**
 * Returns a value which describes the position of the
 * text or image in the receiver. The value will be one of
 * <code>LEFT</code>, <code>RIGHT</code> or <code>CENTER</code>
 * unless the receiver is a <code>SEPARATOR</code> label, in 
 * which case, <code>NONE</code> is returned.
 *
 * @return the alignment 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getAlignment () {
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) return SWT.LEFT;
	if ((style & SWT.CENTER) != 0) return SWT.CENTER;
	if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
	return SWT.LEFT;
}

/**
 * Returns the receiver's image if it has one, or null
 * if it does not.
 *
 * @return the receiver's image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getImage () {
	checkWidget();
	return image;
}

String getNameText () {
	return getText ();
}

/**
 * Returns the receiver's text, which will be an empty
 * string if it has never been set or if the receiver is
 * a <code>SEPARATOR</code> label.
 *
 * @return the receiver's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText () {
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) return "";
	return text;
}

boolean isDescribedByLabel () {
	return false;
}

/*
 * Remove "Label for" relations from the receiver.
 */
void removeRelation () {
	String string = OS.kAXServesAsTitleForUIElementsAttribute;
	char [] buffer = new char [string.length ()];
	string.getChars (0, buffer.length, buffer, 0);
	int stringRef = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
	OS.HIObjectSetAuxiliaryAccessibilityAttribute(handle, 0, stringRef, 0);
	OS.CFRelease(stringRef);
}

/**
 * Controls how text and images will be displayed in the receiver.
 * The argument should be one of <code>LEFT</code>, <code>RIGHT</code>
 * or <code>CENTER</code>.  If the receiver is a <code>SEPARATOR</code>
 * label, the argument is ignored and the alignment is not changed.
 *
 * @param alignment the new alignment 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setAlignment (int alignment) {
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) return;
	if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) return;
	style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	style |= alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	int just = OS.teFlushLeft;
	if ((alignment & SWT.CENTER) != 0) just = OS.teCenter;
	if ((alignment & SWT.RIGHT) != 0) just = OS.teFlushRight;
	ControlFontStyleRec fontStyle = new ControlFontStyleRec ();
	OS.GetControlData (handle, (short) OS.kControlEntireControl, OS.kControlFontStyleTag, ControlFontStyleRec.sizeof, fontStyle, null);
	fontStyle.flags |= OS.kControlUseJustMask;
	fontStyle.just = (short) just;
	OS.SetControlFontStyle (handle, fontStyle);
	redraw ();
}

/**
 * Sets the receiver's image to the argument, which may be
 * null indicating that no image should be displayed.
 *
 * @param image the image to display on the receiver (may be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setImage (Image image) {
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) return;
	if (image != null && image.isDisposed ()) {
		error (SWT.ERROR_INVALID_ARGUMENT);
	}
	this.image = image;
	isImage = true;
	if (image == null) {
		setText (text);
		return;
	}
	if (text.length () > 0) {
		int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, null, 0);
		if (ptr == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
		OS.SetControlData (handle, 0 , OS.kControlStaticTextCFStringTag, 4, new int[]{ptr});
		OS.CFRelease (ptr);
	}
	redraw ();
}

/**
 * Sets the receiver's text.
 * <p>
 * This method sets the widget label.  The label may include
 * the mnemonic character and line delimiters.
 * </p>
 * <p>
 * Mnemonics are indicated by an '&amp;' that causes the next
 * character to be the mnemonic.  When the user presses a
 * key sequence that matches the mnemonic, focus is assigned
 * to the control that follows the label. On most platforms,
 * the mnemonic appears underlined but may be emphasised in a
 * platform specific manner.  The mnemonic indicator character
 * '&amp;' can be escaped by doubling it in the string, causing
 * a single '&amp;' to be displayed.
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
	isImage = false;
	text = string;
	char [] buffer = new char [text.length ()];
	text.getChars (0, buffer.length, buffer, 0);
	int length = fixMnemonic (buffer);
	int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, length);
	if (ptr == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
	OS.SetControlData (handle, 0 , OS.kControlStaticTextCFStringTag, 4, new int[]{ptr});
	OS.CFRelease (ptr);
	redraw ();
}

}

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.CGRect;
import org.eclipse.swt.internal.carbon.Rect;

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
	String text = "";
	
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
	checkWidget ();
	if (OS.HIVIEW) {
		CGRect oldBounds = new CGRect (), bounds = oldBounds;
		OS.HIViewGetFrame (handle, oldBounds);
		int MIN_SIZE = 100;
		if (oldBounds.width < MIN_SIZE || oldBounds.height < MIN_SIZE) {
			OS.HIViewSetDrawingEnabled (handle, false);
			bounds = new CGRect ();
			bounds.width = bounds.height = 100;
			OS.HIViewSetFrame (handle, bounds);
		}
		int rgnHandle = OS.NewRgn ();
		OS.GetControlRegion (handle, (short)OS.kControlContentMetaPart, rgnHandle);
		Rect client = new Rect ();
		OS.GetRegionBounds (rgnHandle, client);
		OS.DisposeRgn (rgnHandle);
		width += (int) bounds.width - (client.right - client.left);
		height += (int) bounds.height - (client.bottom - client.top);
		if (oldBounds.width < MIN_SIZE || oldBounds.height < MIN_SIZE) {
			OS.HIViewSetFrame (handle, oldBounds);
			OS.HIViewSetDrawingEnabled (handle, drawCount == 0);
		}
		return new Rectangle (-client.left, -client.top, width, height);
	}
	Rect bounds, oldBounds = new Rect ();
	OS.GetControlBounds (handle, oldBounds);
	boolean fixBounds = (oldBounds.right - oldBounds.left) < 100 || (oldBounds.bottom - oldBounds.top) < 100;
	if (fixBounds) {
		bounds = new Rect ();
		bounds.right = bounds.bottom = 100;
		OS.SetControlBounds (handle, bounds);
	} else {
		bounds = oldBounds;
	}
	int rgnHandle = OS.NewRgn ();
	OS.GetControlRegion (handle, (short)OS.kControlContentMetaPart, rgnHandle);
	Rect client = new Rect ();
	OS.GetRegionBounds (rgnHandle, client);
	OS.DisposeRgn (rgnHandle);
	if (fixBounds) OS.SetControlBounds (handle, oldBounds);
	x -= client.left - bounds.left;
	y -= client.top - bounds.top;
	width += Math.max (8, (bounds.right - bounds.left) - (client.right - client.left));
	height += Math.max (text.length () == 0 ? 8 : 22, (bounds.bottom - bounds.top) - (client.bottom - client.top));
	return new Rectangle (x, y, width, height);
}

void createHandle () {
	state |= THEME_BACKGROUND;
	int [] outControl = new int [1];
	int window = OS.GetControlOwner (parent.handle);
	OS.CreateGroupBoxControl (window, null, 0, true, outControl);
	if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
	handle = outControl [0];
}

void drawBackground (int control, int context) {
	fillBackground (control, context, null);
}

public Rectangle getClientArea () {
	checkWidget();
	if (OS.HIVIEW) {
		int rgnHandle = OS.NewRgn ();
		OS.GetControlRegion (handle, (short)OS.kControlContentMetaPart, rgnHandle);
		Rect client = new Rect ();
		OS.GetRegionBounds (rgnHandle, client);
		OS.DisposeRgn (rgnHandle);
		return new Rectangle (client.left, client.top, client.right - client.left, client.bottom - client.top);
	}
	Rect bounds = new Rect ();
	OS.GetControlBounds (handle, bounds);
	int rgnHandle = OS.NewRgn ();
	OS.GetControlRegion (handle, (short)OS.kControlContentMetaPart, rgnHandle);
	Rect client = new Rect ();
	OS.GetRegionBounds (rgnHandle, client);
	OS.DisposeRgn (rgnHandle);
	int x = Math.max (0, client.left - bounds.left);
	int y = text.length () == 0 ? x : Math.max (0, client.top - bounds.top);
	int width = Math.max (0, client.right - client.left);
	int height = Math.max (0, text.length () == 0 ? bounds.bottom - bounds.top - 2*y : client.bottom - client.top);
	return new Rectangle (x, y, width, height);
}

String getNameText () {
	return getText ();
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
	checkWidget ();
	return text;
}

float getThemeAlpha () {
	return 0.25f * parent.getThemeAlpha ();
}

/**
 * Sets the receiver's text, which is the string that will
 * be displayed as the receiver's <em>title</em>, to the argument,
 * which may not be null. The string may include the mnemonic character.
 * </p>
 * Mnemonics are indicated by an '&amp;' that causes the next
 * character to be the mnemonic.  When the user presses a
 * key sequence that matches the mnemonic, focus is assigned
 * to the first child of the group. On most platforms, the
 * mnemonic appears underlined but may be emphasised in a
 * platform specific manner.  The mnemonic indicator character
 * '&amp;' can be escaped by doubling it in the string, causing
 * a single '&amp;' to be displayed.
 * </p>
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
	text = string;
	char [] buffer = new char [text.length ()];
	text.getChars (0, buffer.length, buffer, 0);
	int length = fixMnemonic (buffer);
	int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, length);
	if (ptr == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
	OS.SetControlTitleWithCFString (handle, ptr);
	OS.CFRelease (ptr);
}

}

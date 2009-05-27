/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.wpf.*;
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
 * 
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public class Group extends Composite {
	int parentingHandle;
	String text;

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

int backgroundProperty () {
	return OS.Control_BackgroundProperty ();
}

int clientHandle () {
	return parentingHandle;
}

void createHandle () {
	state |= THEME_BACKGROUND;
	handle = OS.gcnew_GroupBox ();
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	parentingHandle = OS.gcnew_Canvas ();
	if (parentingHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.ContentControl_Content (handle, parentingHandle);
}

public Rectangle computeTrim (int x, int y, int width, int height) {
	int topHandle = topHandle ();
	int clientHandle = clientHandle();
	if (clientHandle != topHandle) {
		double currentWidth = OS.FrameworkElement_Width (topHandle);
		double currentHeight = OS.FrameworkElement_Height (topHandle);
		OS.FrameworkElement_Width (topHandle, 100);
		OS.FrameworkElement_Height (topHandle, 100);
		updateLayout (topHandle);
		int w = (int) OS.FrameworkElement_ActualWidth (topHandle);
		int h = (int) OS.FrameworkElement_ActualHeight (topHandle);
		int clientWidth = (int) OS.FrameworkElement_ActualWidth (clientHandle);
		int clientHeight = (int) OS.FrameworkElement_ActualHeight (clientHandle);
		int point = OS.gcnew_Point (0, 0);
		int result = OS.UIElement_TranslatePoint (clientHandle, point, topHandle);
		x -= (int) OS.Point_X (result);
		y -= (int) OS.Point_Y (result);
		width += (w - clientWidth);
		height += (h - clientHeight);
		OS.GCHandle_Free (point);
		OS.GCHandle_Free (result);
		OS.FrameworkElement_Width (topHandle, currentWidth);
		OS.FrameworkElement_Height (topHandle, currentHeight);
	}
	return new Rectangle (x, y, width, height);
}

int defaultBackground () {
	return OS.SystemColors_ControlColor;
}

void enableWidget (boolean enabled) {
	OS.UIElement_IsHitTestVisible (handle, enabled);
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

boolean mnemonicHit (char key) {
	return false;
}

boolean mnemonicMatch (char key) {
	int accessText = OS.HeaderedContentControl_Header (handle);
	boolean result = super.mnemonicMatch (accessText, key);
	OS.GCHandle_Free (accessText);
	return result;
}

void releaseHandle () {
	super.releaseHandle ();
	OS.GCHandle_Free (parentingHandle);
	parentingHandle = 0;
}

void setForegroundBrush (int brush) {
	if (brush != 0) {
		OS.Control_Foreground (handle, brush);
	} else {
		int property = OS.Control_ForegroundProperty ();
		OS.DependencyObject_ClearValue (handle, property);
		OS.GCHandle_Free (property);
	}
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
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (string.equals (text)) return;
	text = string;
	int ptr = createDotNetString (text, true);
	int accessText = OS.gcnew_AccessText ();
	if (ptr != 0) OS.AccessText_Text (accessText, ptr);
	OS.HeaderedContentControl_Header (handle, accessText);
	if (ptr != 0) OS.GCHandle_Free (ptr);
	OS.GCHandle_Free (accessText);
}

int parentingHandle () {
	return parentingHandle;
}

}

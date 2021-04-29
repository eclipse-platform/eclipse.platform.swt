/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;

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
	long clientHandle, labelHandle;
	String text = "";
	// We use this to keep track of the foreground color
	GdkRGBA foreground;

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

@Override
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

@Override
long clientHandle () {
	return clientHandle;
}

@Override
Point computeSizeInPixels (int wHint, int hHint, boolean changed) {
	Point size = super.computeSizeInPixels(wHint, hHint, changed);
	int width = computeNativeSize (handle, SWT.DEFAULT, SWT.DEFAULT, false).x;
	size.x = Math.max (size.x, width);
	return size;
}
@Override
Rectangle computeTrimInPixels (int x, int y, int width, int height) {
	checkWidget();
	forceResize ();
	GtkAllocation allocation = new GtkAllocation();
	GTK.gtk_widget_get_allocation (clientHandle, allocation);
	int clientX = allocation.x;
	int clientY = allocation.y;
	x -= clientX;
	y -= clientY;
	width += clientX + clientX;
	height += clientX + clientY;
	return new Rectangle (x, y, width, height);
}

@Override
Rectangle getClientAreaInPixels () {
	Rectangle clientRectangle = super.getClientAreaInPixels ();
	/*
	* Bug 453827 Child position fix.
	* SWT's calls to gtk_widget_size_allocate and gtk_widget_set_allocation
	* causes GTK+ to move the clientHandle's SwtFixed down by the size of the label.
	* These calls can come up from 'shell' and group has no control over these calls.
	*
	* This is an undesired side-effect. Client handle's x & y positions should never
	* be incremented as this is an internal sub-container.
	*
	* Note: 0 by 0 was chosen as 1 by 1 shifts controls beyond their original pos.
	* The long term fix would be to not use widget_*_allocation from higher containers
	* like shell and to not use	gtkframe in non-group widgets (e.g used in label atm).
	*/
	clientRectangle.x = 0;
	clientRectangle.y = 0;
	return clientRectangle;
}

@Override
GdkRGBA getContextColorGdkRGBA () {
	if (foreground != null) {
		return foreground;
	} else {
		return display.COLOR_WIDGET_FOREGROUND_RGBA;
	}
}

@Override
GdkRGBA getContextBackgroundGdkRGBA () {
	return super.getContextBackgroundGdkRGBA();
}

@Override
void createHandle(int index) {
	state |= HANDLE | THEME_BACKGROUND;

	fixedHandle = OS.g_object_new (display.gtk_fixed_get_type (), 0);
	if (fixedHandle == 0) error (SWT.ERROR_NO_HANDLES);
	if (!GTK.GTK4) GTK3.gtk_widget_set_has_window(fixedHandle, true);

	handle = GTK.gtk_frame_new (null);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);

	labelHandle = GTK.gtk_label_new (null);
	if (labelHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.g_object_ref_sink (labelHandle);

	clientHandle = OS.g_object_new (display.gtk_fixed_get_type (), 0);
	if (clientHandle == 0) error (SWT.ERROR_NO_HANDLES);
	/*
	 * Bug 453827 - clientHandle now has it's own window so that
	 * it can listen to events (clicking/tooltip etc.) and so that
	 * background can be drawn on it.
	 */
	if (!GTK.GTK4) GTK3.gtk_widget_set_has_window(clientHandle, true);

	if (GTK.GTK4) {
		OS.swt_fixed_add(fixedHandle, handle);
		GTK4.gtk_frame_set_child(handle, clientHandle);
	} else {
		GTK3.gtk_container_add (fixedHandle, handle);
		GTK3.gtk_container_add (handle, clientHandle);

		if ((style & SWT.SHADOW_IN) != 0) {
			GTK3.gtk_frame_set_shadow_type (handle, GTK.GTK_SHADOW_IN);
		}
		if ((style & SWT.SHADOW_OUT) != 0) {
			GTK3.gtk_frame_set_shadow_type (handle, GTK.GTK_SHADOW_OUT);
		}
		if ((style & SWT.SHADOW_ETCHED_IN) != 0) {
			GTK3.gtk_frame_set_shadow_type (handle, GTK.GTK_SHADOW_ETCHED_IN);
		}
		if ((style & SWT.SHADOW_ETCHED_OUT) != 0) {
			GTK3.gtk_frame_set_shadow_type (handle, GTK.GTK_SHADOW_ETCHED_OUT);
		}
	}

	// In GTK 3 font description is inherited from parent widget which is not how SWT has always worked,
	// reset to default font to get the usual behavior
	setFontDescription (defaultFont ().handle);
}

@Override
int applyThemeBackground () {
	return 1;
}

@Override
void deregister () {
	super.deregister ();
	display.removeWidget (clientHandle);
	display.removeWidget (labelHandle);
}

@Override
void enableWidget (boolean enabled) {
	GTK.gtk_widget_set_sensitive (labelHandle, enabled);
}

@Override
long eventHandle () {
	/*
	 * Bug 453827 - Group's events should be handled via it's internal
	 * fixed container (clientHandle) and not via it's top level.
	 * This makes it behave more like composite.
	 */
	return clientHandle;
}

@Override
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
	checkWidget();
	return text;
}

@Override
void hookEvents () {
	super.hookEvents();
	if (labelHandle != 0) {
		OS.g_signal_connect_closure_by_id (labelHandle, display.signalIds [MNEMONIC_ACTIVATE], 0, display.getClosure (MNEMONIC_ACTIVATE), false);
	}
}

@Override
boolean mnemonicHit (char key) {
	if (labelHandle == 0) return false;
	boolean result = super.mnemonicHit (labelHandle, key);
	if (result) setFocus ();
	return result;
}

@Override
boolean mnemonicMatch (char key) {
	if (labelHandle == 0) return false;
	return mnemonicMatch (labelHandle, key);
}

@Override
long parentingHandle() {
	/*
	 * Bug 453827 - Children should be attached to the internal fixed
	 * subcontainer (clienthandle) and not the top-level fixedHandle.
	 */
	return clientHandle;
}

@Override
void register () {
	super.register ();
	display.addWidget (clientHandle, this);
	display.addWidget (labelHandle, this);
}

@Override
void releaseHandle () {
	super.releaseHandle ();
	clientHandle = labelHandle = 0;
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	if (labelHandle != 0) OS.g_object_unref (labelHandle);
	text = null;
}

@Override
void setFontDescription (long font) {
	super.setFontDescription (font);
	setFontDescription (labelHandle, font);
}

@Override
void setForegroundGdkRGBA (long handle, GdkRGBA rgba) {
	/*
	 * When using CSS, setting the foreground color on an empty label
	 * widget prevents the background from being set. If a user wants
	 * to specify a foreground color before the text is set, store the
	 * color and wait until text is specified to apply it.
	 */
	if (text != null && !text.isEmpty()) {
		super.setForegroundGdkRGBA (labelHandle, rgba);
	}
	foreground = rgba;
}

@Override
void setForegroundGdkRGBA (GdkRGBA rgba) {
	super.setForegroundGdkRGBA(rgba);
	setForegroundGdkRGBA (labelHandle, rgba);
}

@Override
void setOrientation (boolean create) {
	super.setOrientation (create);
	if ((style & SWT.RIGHT_TO_LEFT) != 0 || !create) {
		int dir = (style & SWT.RIGHT_TO_LEFT) != 0 ? GTK.GTK_TEXT_DIR_RTL : GTK.GTK_TEXT_DIR_LTR;
		GTK.gtk_widget_set_direction (labelHandle, dir);
	}
}

/**
 * Sets the receiver's text, which is the string that will
 * be displayed as the receiver's <em>title</em>, to the argument,
 * which may not be null. The string may include the mnemonic character.
 * <p>
 * Mnemonics are indicated by an '&amp;' that causes the next
 * character to be the mnemonic.  When the user presses a
 * key sequence that matches the mnemonic, focus is assigned
 * to the first child of the group. On most platforms, the
 * mnemonic appears underlined but may be emphasised in a
 * platform specific manner.  The mnemonic indicator character
 * '&amp;' can be escaped by doubling it in the string, causing
 * a single '&amp;' to be displayed.
 * </p><p>
 * Note: If control characters like '\n', '\t' etc. are used
 * in the string, then the behavior is platform dependent.
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
	char [] chars = fixMnemonic (string);
	byte [] buffer = Converter.wcsToMbcs (chars, true);
	GTK.gtk_label_set_text_with_mnemonic (labelHandle, buffer);
	if (string.length () != 0) {
		if (GTK.gtk_frame_get_label_widget (handle) == 0) {
			GTK.gtk_frame_set_label_widget (handle, labelHandle);
		}
	} else {
		GTK.gtk_frame_set_label_widget (handle, 0);
	}
	// Set the foreground now that the text has been set
	if (foreground != null) {
		setForegroundGdkRGBA (labelHandle, foreground);
	}
}

@Override
void showWidget () {
	super.showWidget ();
	if (clientHandle != 0) GTK.gtk_widget_show (clientHandle);
	if (labelHandle != 0) GTK.gtk_widget_show (labelHandle);
}

@Override
int setBounds(int x, int y, int width, int height, boolean move, boolean resize) {
	// Work around for bug 470129.
	// See also https://bugzilla.gnome.org/show_bug.cgi?id=754976 :
	// GtkFrame: Attempt to allocate size of width 1 (or a small number) fails
	//
	// GtkFrame does not handle well allocating less than its minimum size
	GtkRequisition requisition = new GtkRequisition();
	GTK.gtk_widget_get_preferred_size(handle, requisition, null);
	/*
	 * Feature in GTK3.20+: size calculations take into account GtkCSSNode
	 * elements which we cannot access. If the to-be-allocated size minus
	 * these elements is < 0, allocate the preferred size instead.
	 */
	width = (width - (requisition.width - width)) < 0 ? requisition.width : width;
	height = (height - (requisition.height - height)) < 0 ? requisition.height : height;
	return super.setBounds(x, y, width, height, move, resize);
}

@Override
long paintHandle() {
	if (GTK.GTK4) return clientHandle;
	long topHandle = topHandle ();
	/* we draw all our children on the clientHandle*/
	long paintHandle = clientHandle;
	while (paintHandle != topHandle) {
		if (GTK3.gtk_widget_get_has_window(paintHandle)) break;
		paintHandle = GTK.gtk_widget_get_parent (paintHandle);
	}
	return paintHandle;
}

@Override
long paintWindow () {
	long paintHandle = clientHandle;
	GTK.gtk_widget_realize (paintHandle);
	return gtk_widget_get_window (paintHandle);
}

@Override
long paintSurface () {
	long paintHandle = clientHandle;
	GTK.gtk_widget_realize (paintHandle);
	return gtk_widget_get_surface (paintHandle);
}

}
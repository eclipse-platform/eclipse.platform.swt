/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.cocoa.*;

/**
 * Instances of this class allow the user to select a color
 * from a predefined set of available colors.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample, Dialog tab</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ColorDialog extends Dialog {
	RGB rgb;
	RGB [] rgbs;
	boolean selected;

/**
 * Constructs a new instance of this class given only its parent.
 *
 * @param parent a composite control which will be the parent of the new instance
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ColorDialog(Shell parent) {
	this(parent, SWT.APPLICATION_MODAL);
}

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
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ColorDialog(Shell parent, int style) {
	super (parent, checkStyle (parent, style));
	checkSubclass ();
}

void changeColor(int /*long*/ id, int /*long*/ sel, int /*long*/ sender) {
	selected = true;
}

/**
 * Returns the currently selected color in the receiver.
 *
 * @return the RGB value for the selected color, may be null
 *
 * @see PaletteData#getRGBs
 */
public RGB getRGB() {
	return rgb;
}

/**
 * Returns an array of <code>RGB</code>s which are the list of
 * custom colors selected by the user in the receiver, or null
 * if no custom colors were selected.
 *
 * @return the array of RGBs, which may be null
 * 
 * @since 3.8
 */
public RGB[] getRGBs() {
	return rgbs;
}

/**
 * Makes the receiver visible and brings it to the front
 * of the display.
 *
 * @return the selected color, or null if the dialog was
 *         cancelled, no color was selected, or an error
 *         occurred
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public RGB open() {	
	NSColorPanel panel = NSColorPanel.sharedColorPanel();
	Display display = parent != null ? parent.getDisplay() : Display.getCurrent();
	if (rgb != null) {
		NSColor color = NSColor.colorWithDeviceRed(rgb.red / 255f, rgb.green / 255f, rgb.blue / 255f, 1);
		panel.setColor(color);
	}
	NSString appName = Display.getApplicationName();
	NSColorList colorList = NSColorList.colorListNamed(appName);
	if (colorList == null) {
		colorList = (NSColorList)new NSColorList().alloc();
		colorList.initWithName(appName);
		panel.attachColorList(colorList);
	} else {
		colorList.retain();
	}
	if (rgbs != null) {
		NSArray keys = colorList.allKeys();
		int length = (int)/*64*/keys.count();
		for (int i=length-1; i>=0; i--) {
			colorList.removeColorWithKey(new NSString(keys.objectAtIndex(i)));
		}
		for (int i=0; i<rgbs.length; i++) {
			RGB rgb = rgbs [i];
			if (rgb != null) {
				NSColor color = NSColor.colorWithDeviceRed(rgb.red / 255f, rgb.green / 255f, rgb.blue / 255f, 1);
				NSString key = appName;
				if (i > 0) {
					key = key.stringByAppendingString(NSString.stringWith(" "+i));
				}
				colorList.insertColor(color, key, i);
			}
		}
	}
	SWTPanelDelegate delegate = (SWTPanelDelegate)new SWTPanelDelegate().alloc().init();
	int /*long*/ jniRef = OS.NewGlobalRef(this);
	if (jniRef == 0) error(SWT.ERROR_NO_HANDLES);
	OS.object_setInstanceVariable(delegate.id, Display.SWT_OBJECT, jniRef);
	panel.setDelegate(delegate);
	rgb = null;
	selected = false;
	panel.orderFront(null);
	display.setModalDialog(this);
	NSApplication.sharedApplication().runModalForWindow(panel);
	display.setModalDialog(null);
	panel.setDelegate(null);
	delegate.release();
	OS.DeleteGlobalRef(jniRef);
	if (selected) {
		NSColor color = panel.color();
		if (color != null) {
			float /*double*/ [] handle = display.getNSColorRGB(color);
			rgb = new RGB((int)(handle[0] * 255), (int)(handle[1] * 255), (int)(handle[2] * 255));
		}
	}
	NSArray keys = colorList.allKeys();
	int length = (int)/*64*/keys.count();
	rgbs = new RGB[length];
	for (int i=0; i<length; i++) {
		NSString key = new NSString(keys.objectAtIndex(i));
		float /*double*/ [] handle = display.getNSColorRGB(colorList.colorWithKey(key));
		rgbs[i] = new RGB((int)(handle[0] * 255), (int)(handle[1] * 255), (int)(handle[2] * 255));
	}
	colorList.release();
	return rgb;
}

/**
 * Sets the receiver's selected color to be the argument.
 *
 * @param rgb the new RGB value for the selected color, may be
 *        null to let the platform select a default when
 *        open() is called
 * @see PaletteData#getRGBs
 */
public void setRGB(RGB rgb) {
	this.rgb = rgb;
}

/**
 * Sets the receiver's list of custom colors to be the given array
 * of <code>RGB</code>s, which may be null to let the platform select
 * a default when open() is called.
 *
 * @param rgbs the array of RGBs, which may be null
 *
 * @since 3.8
 */
public void setRGBs(RGB[] rgbs) {
	this.rgbs = rgbs;
}

void windowWillClose(int /*long*/ id, int /*long*/ sel, int /*long*/ sender) {
	NSApplication.sharedApplication().stop(null);
}
}

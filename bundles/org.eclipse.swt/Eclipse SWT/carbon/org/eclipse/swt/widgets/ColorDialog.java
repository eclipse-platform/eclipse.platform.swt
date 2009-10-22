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


import org.eclipse.swt.*;
import org.eclipse.swt.internal.carbon.*;
import org.eclipse.swt.graphics.RGB;

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
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample, Dialog tab</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ColorDialog extends Dialog {
	RGB rgb;
	
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
	ColorPickerInfo info = new ColorPickerInfo ();
	if (rgb != null) {
		info.red = (short)(rgb.red * 257);
		info.green = (short)(rgb.green * 257);
		info.blue = (short)(rgb.blue * 257);
	} else {
		info.red = (short)(255 * 257);
		info.green = (short)(255 * 257);
		info.blue = (short)(255 * 257);		
	}
	info.flags = OS.kColorPickerDialogIsMoveable | OS.kColorPickerDialogIsModal;
	// NEEDS WORK - shouldn't be at mouse location
	info.placeWhere = (short)OS.kAtSpecifiedOrigin;
	org.eclipse.swt.internal.carbon.Point mp = new org.eclipse.swt.internal.carbon.Point ();
	OS.GetGlobalMouse (mp);
	info.v = mp.v;
	info.h = mp.h;
	if (title != null) {
		// NEEDS WORK - no title displayed		
		info.prompt = new byte[256];
		int length = title.length();
		if (length > 255) length = 255;
		info.prompt [0] = (byte)length;
		for (int i=0; i<length; i++) {
			info.prompt [i+1] = (byte)title.charAt (i);
		}
	}
	Display display = parent != null ? parent.getDisplay() : Display.getCurrent();
	display.setModalDialog(this);
	rgb = null;
	if (OS.PickColor (info) == OS.noErr && info.newColorChosen) {
		int red = (info.red >> 8) & 0xFF;
		int green = (info.green >> 8) & 0xFF;
		int blue =	(info.blue >> 8) & 0xFF;
		rgb = new RGB(red, green, blue);
	}
	display.setModalDialog(null);
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
}

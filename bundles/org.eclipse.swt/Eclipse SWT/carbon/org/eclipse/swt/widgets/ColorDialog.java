package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
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
 */
public /*final*/ class ColorDialog extends Dialog {

	private RGB rgb;
	
/**
 * Constructs a new instance of this class given only its parent.
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
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
	this(parent, SWT.NULL);
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
	super(parent, style | SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);
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
	short[] c= new short[3];
	c[0]= (short) (rgb.red * 257);
	c[1]= (short) (rgb.green * 257);
	c[2]= (short) (rgb.blue * 257);
	
	MacPoint mp= new MacPoint();
	OS.GetGlobalMouse(mp.getData());
	
	boolean[] success= new boolean[1];
	if (OS.PickColor(c, mp.getData(), MacUtil.Str255(title), success) == OS.kNoErr) {
		if (success[0]) {
			rgb.red= c[0] / 257;
			rgb.green= c[1] / 257;
			rgb.blue= c[2] / 257;
		} else
			rgb= null;
	} else
		rgb= null;
	
	return rgb;
}
/**
 * Returns the receiver's selected color to be the argument.
 *
 * @param rgb the new RGB value for the selected color, may be
 *        null to let the platform to select a default when
 *        open() is called
 *
 * @see PaletteData#getRGBs
 */
public void setRGB(RGB rgb) {
	this.rgb = rgb;
}
}

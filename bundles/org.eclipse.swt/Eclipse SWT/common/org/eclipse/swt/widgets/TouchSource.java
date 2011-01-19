/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
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

/**
 * Instances of this class represent a source of touch input. It is used to identify which input source
 * generated a <code>Touch</code> object. It also provides information about the input source, which is important
 * when deciding how to interpret the information in the <code>Touch</code> object.
 * <p>
 * Instances of this class can be marked as direct or indirect:
 * <ul>
 * <li>When an instance is marked as <em>direct</em> the touch source is a touch-sensitive digitizer surface such
 * as a tablet or a touch screen. There is a one-to-one mapping between a touch point and a location in a window.
 * </li><li>
 * When an instance is marked as <em>indirect</em> (or, more precisely, not direct) the touch source is a track pad 
 * or other device that normally moves the cursor, but can also interpret multiple touches on its surface. In this 
 * case, there is not a one-to-one map between the location of the touch on the device and a location on the display
 * because the user can remove their finger or stylus and touch another part of the device and resume what they were
 * doing.
 * </li>
 * </ul>
 *
 * @see Touch
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * 
 * @since 3.7
 */

public final class TouchSource {
	int /*long*/ handle;
	boolean direct;	
	Rectangle bounds;

/**
 * Constructs a new touch source from the given inputs.
 * 
 * @param direct Is the touch source direct or indirect? 
 * @param height height of the source in pixels.
 * @param width width of the source in pixels.
 */
TouchSource (int /*long*/ handle, boolean direct, Rectangle bounds) {
	this.handle = handle;
	this.direct = direct;
	this.bounds = bounds;
}

/**
 * Returns the type of touch input this source generates; true for direct, false for indirect.
 * @return boolean If true, the input source is direct. If false, the input source is indirect.
 */
public boolean isDirect () {
	return direct;
}

/**
 * Returns the bounding rectangle of the device. For a direct source, this corresponds to the bounds of the display
 * device in pixels. For an indirect source, this contains the size of the device in pixels.
 * <p>
 * Note that the x and y values may not necessarily be zero if the TouchSource is a direct source.
 * @return Rectangle The bounding rectangle of the input source.
 */
public Rectangle getBounds () {
	return new Rectangle (bounds.x, bounds.y, bounds.width, bounds.height);
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the event
 */
public String toString () {
	return "{handle=" + handle
	+ " direct=" + direct
	+ " bounds=" + bounds;
}
}
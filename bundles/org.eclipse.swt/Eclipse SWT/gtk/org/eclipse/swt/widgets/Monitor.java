/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.*;

/**
 * Instances of this class are descriptions of monitors.
 *
 * @see Display
 * 
 * @since 2.2
 */
public final class Monitor {
	int handle;
	int x, y, width, height;
	int clientX, clientY, clientWidth, clientHeight;
	
/**
 * Prevents uninitialized instances from being created outside the package.
 */
Monitor () {	
}
	
public boolean equals (Object object) {
	if (object == this) return true;
	if (!(object instanceof Monitor)) return false;
	Monitor monitor = (Monitor) object;
	return handle == monitor.handle;
}

/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its device.
 *
 * @return the receiver's bounding rectangle
 */	
public Rectangle getBounds () {
	return new Rectangle (x, y, width, height);
}
	
/**
 * Returns a rectangle which describes the area of the
 * receiver which is capable of displaying data.
 * 
 * @return the client area
 */
public Rectangle getClientArea () {
	return new Rectangle (clientX, clientY, clientWidth, clientHeight);
}
	
public int hashCode () {
	return handle;
}

}

/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSGradient extends NSObject {

public NSGradient() {
	super();
}

public NSGradient(long /*int*/ id) {
	super(id);
}

public NSGradient(id id) {
	super(id);
}

public void drawFromPoint(NSPoint startingPoint, NSPoint endingPoint, long /*int*/ options) {
	OS.objc_msgSend(this.id, OS.sel_drawFromPoint_toPoint_options_, startingPoint, endingPoint, options);
}

public void drawInRect(NSRect rect, double /*float*/ angle) {
	OS.objc_msgSend(this.id, OS.sel_drawInRect_angle_, rect, angle);
}

public NSGradient initWithStartingColor(NSColor startingColor, NSColor endingColor) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_initWithStartingColor_endingColor_, startingColor != null ? startingColor.id : 0, endingColor != null ? endingColor.id : 0);
	return result == this.id ? this : (result != 0 ? new NSGradient(result) : null);
}

}

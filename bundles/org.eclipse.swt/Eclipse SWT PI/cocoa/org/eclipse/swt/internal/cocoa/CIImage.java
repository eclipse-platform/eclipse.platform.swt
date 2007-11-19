/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class CIImage extends NSObject {

public CIImage() {
	super();
}

public CIImage(int id) {
	super(id);
}

public void drawAtPoint(NSPoint point, NSRect fromRect, int op, float delta) {
	OS.objc_msgSend(this.id, OS.sel_drawAtPoint_1fromRect_1operation_1fraction_1, point, fromRect, op, delta);
}

public void drawInRect(NSRect rect, NSRect fromRect, int op, float delta) {
	OS.objc_msgSend(this.id, OS.sel_drawInRect_1fromRect_1operation_1fraction_1, rect, fromRect, op, delta);
}

public CIImage initWithBitmapImageRep(NSBitmapImageRep bitmapImageRep) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithBitmapImageRep_1, bitmapImageRep != null ? bitmapImageRep.id : 0);
	return result != 0 ? this : null;
}

}

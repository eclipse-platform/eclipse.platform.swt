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

public class NSRulerMarker extends NSObject {

public NSRulerMarker() {
	super();
}

public NSRulerMarker(int id) {
	super(id);
}

public void drawRect(NSRect rect) {
	OS.objc_msgSend(this.id, OS.sel_drawRect_1, rect);
}

public NSImage image() {
	int result = OS.objc_msgSend(this.id, OS.sel_image);
	return result != 0 ? new NSImage(result) : null;
}

public NSPoint imageOrigin() {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_imageOrigin);
	return result;
}

public NSRect imageRectInRuler() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_imageRectInRuler);
	return result;
}

public id initWithRulerView(NSRulerView ruler, float location, NSImage image, NSPoint imageOrigin) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithRulerView_1markerLocation_1image_1imageOrigin_1, ruler != null ? ruler.id : 0, location, image != null ? image.id : 0, imageOrigin);
	return result != 0 ? new id(result) : null;
}

public boolean isDragging() {
	return OS.objc_msgSend(this.id, OS.sel_isDragging) != 0;
}

public boolean isMovable() {
	return OS.objc_msgSend(this.id, OS.sel_isMovable) != 0;
}

public boolean isRemovable() {
	return OS.objc_msgSend(this.id, OS.sel_isRemovable) != 0;
}

public float markerLocation() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_markerLocation);
}

public id  representedObject() {
	int result = OS.objc_msgSend(this.id, OS.sel_representedObject);
	return result != 0 ? new id (result) : null;
}

public NSRulerView ruler() {
	int result = OS.objc_msgSend(this.id, OS.sel_ruler);
	return result != 0 ? new NSRulerView(result) : null;
}

public void setImage(NSImage image) {
	OS.objc_msgSend(this.id, OS.sel_setImage_1, image != null ? image.id : 0);
}

public void setImageOrigin(NSPoint imageOrigin) {
	OS.objc_msgSend(this.id, OS.sel_setImageOrigin_1, imageOrigin);
}

public void setMarkerLocation(float location) {
	OS.objc_msgSend(this.id, OS.sel_setMarkerLocation_1, location);
}

public void setMovable(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setMovable_1, flag);
}

public void setRemovable(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setRemovable_1, flag);
}

public void setRepresentedObject(id  representedObject) {
	OS.objc_msgSend(this.id, OS.sel_setRepresentedObject_1, representedObject != null ? representedObject.id : 0);
}

public float thicknessRequiredInRuler() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_thicknessRequiredInRuler);
}

public boolean trackMouse(NSEvent mouseDownEvent, boolean isAdding) {
	return OS.objc_msgSend(this.id, OS.sel_trackMouse_1adding_1, mouseDownEvent != null ? mouseDownEvent.id : 0, isAdding) != 0;
}

}

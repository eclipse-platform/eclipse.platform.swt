package org.eclipse.swt.internal.cocoa;

public class NSCursor extends NSObject {

public NSCursor() {
	super(0);
}

public NSCursor(int id) {
	super(id);
}

public int get_class() {
	return OS.class_NSCursor;
}

public NSCursor init(NSImage image, NSPoint hotspot) {
	int id = OS.objc_msgSend(this.id, OS.sel_initWithImage_1hotSpot_1, image != null ? image.id : 0, hotspot);
	return id != 0 ? this : null;
}

public static NSCursor arrowCursor() {
	int id = OS.objc_msgSend(OS.class_NSCursor, OS.sel_arrowCursor);
	return id != 0 ? new NSCursor(id) : null;
}

public static NSCursor crosshairCursor() {
	int id = OS.objc_msgSend(OS.class_NSCursor, OS.sel_crosshairCursor);
	return id != 0 ? new NSCursor(id) : null;
}

public static NSCursor IBeamCursor() {
	int id = OS.objc_msgSend(OS.class_NSCursor, OS.sel_IBeamCursor);
	return id != 0 ? new NSCursor(id) : null;
}

public static NSCursor pointingHandCursor() {
	int id = OS.objc_msgSend(OS.class_NSCursor, OS.sel_pointingHandCursor);
	return id != 0 ? new NSCursor(id) : null;
}

public static NSCursor resizeLeftRightCursor() {
	int id = OS.objc_msgSend(OS.class_NSCursor, OS.sel_resizeLeftRightCursor);
	return id != 0 ? new NSCursor(id) : null;
}

public static NSCursor resizeRightCursor() {
	int id = OS.objc_msgSend(OS.class_NSCursor, OS.sel_resizeRightCursor);
	return id != 0 ? new NSCursor(id) : null;
}

public static NSCursor resizeLeftCursor() {
	int id = OS.objc_msgSend(OS.class_NSCursor, OS.sel_resizeLeftCursor);
	return id != 0 ? new NSCursor(id) : null;
}

public static NSCursor resizeDownCursor() {
	int id = OS.objc_msgSend(OS.class_NSCursor, OS.sel_resizeDownCursor);
	return id != 0 ? new NSCursor(id) : null;
}

public static NSCursor resizeUpCursor() {
	int id = OS.objc_msgSend(OS.class_NSCursor, OS.sel_resizeUpCursor);
	return id != 0 ? new NSCursor(id) : null;
}

public static NSCursor resizeUpDownCursor() {
	int id = OS.objc_msgSend(OS.class_NSCursor, OS.sel_resizeUpDownCursor);
	return id != 0 ? new NSCursor(id) : null;
}

public void set() {
	OS.objc_msgSend(id, OS.sel_set);
}

public void setOnMouseEnter(boolean value) {
	OS.objc_msgSend(id, OS.sel_setOnMouseEntered_1, value ? 1 : 0);
}
}

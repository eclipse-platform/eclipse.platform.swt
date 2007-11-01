package org.eclipse.swt.internal.cocoa;

public class NSScreen extends NSObject {

public NSScreen(int id) {
	super(id);
}

public int depth() {
	return OS.objc_msgSend(id, OS.sel_depth);
}

public NSRect frame() {
	NSRect rect = new NSRect();
	OS.objc_msgSend_stret(rect, id, OS.sel_frame);
	return rect;
}

public static NSScreen mainScreen() {
	int id = OS.objc_msgSend(OS.class_NSScreen, OS.sel_mainScreen);
	return id != 0 ? new NSScreen(id) : null;
}

public static NSArray screens() {
	int id = OS.objc_msgSend(OS.class_NSScreen, OS.sel_screens);
	return id != 0 ? new NSArray(id) : null;
}

public NSRect visibleFrame() {
	NSRect rect = new NSRect();
	OS.objc_msgSend_stret(rect, id, OS.sel_visibleFrame);
	return rect;
}
}

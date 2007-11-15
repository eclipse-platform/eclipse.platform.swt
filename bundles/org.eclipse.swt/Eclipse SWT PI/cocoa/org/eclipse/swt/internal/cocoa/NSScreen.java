package org.eclipse.swt.internal.cocoa;

public class NSScreen extends NSObject {

public NSScreen() {
	super();
}

public NSScreen(int id) {
	super(id);
}

public static NSScreen deepestScreen() {
	int result = OS.objc_msgSend(OS.class_NSScreen, OS.sel_deepestScreen);
	return result != 0 ? new NSScreen(result) : null;
}

public int depth() {
	return OS.objc_msgSend(this.id, OS.sel_depth);
}

public NSDictionary deviceDescription() {
	int result = OS.objc_msgSend(this.id, OS.sel_deviceDescription);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSRect frame() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_frame);
	return result;
}

public static NSScreen mainScreen() {
	int result = OS.objc_msgSend(OS.class_NSScreen, OS.sel_mainScreen);
	return result != 0 ? new NSScreen(result) : null;
}

public static NSArray screens() {
	int result = OS.objc_msgSend(OS.class_NSScreen, OS.sel_screens);
	return result != 0 ? new NSArray(result) : null;
}

public int supportedWindowDepths() {
	return OS.objc_msgSend(this.id, OS.sel_supportedWindowDepths);
}

public float userSpaceScaleFactor() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_userSpaceScaleFactor);
}

public NSRect visibleFrame() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_visibleFrame);
	return result;
}

}

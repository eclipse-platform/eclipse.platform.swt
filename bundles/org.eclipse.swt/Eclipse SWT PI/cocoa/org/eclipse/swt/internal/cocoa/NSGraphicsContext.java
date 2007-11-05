package org.eclipse.swt.internal.cocoa;

public class NSGraphicsContext extends NSObject {

public NSGraphicsContext(int id) {
	super(id);
}

public static NSGraphicsContext graphicsContextWithBitmapImageRep(NSBitmapImageRep bitmapRep) {
	int id = OS.objc_msgSend(OS.class_NSGraphicsContext, OS.sel_graphicsContextWithBitmapImageRep_1, bitmapRep.id);
	return id != 0 ? new NSGraphicsContext(id) : null;
}

public static void setCurrentContext(NSGraphicsContext context) {
	OS.objc_msgSend(OS.class_NSGraphicsContext, OS.sel_setCurrentContext_1, context != null ? context.id : 0);
}

public static void saveGraphicsState() {
	OS.objc_msgSend(OS.class_NSGraphicsContext, OS.sel_saveGraphicsState);
}

public static void restoreGraphicsState() {
	OS.objc_msgSend(OS.class_NSGraphicsContext, OS.sel_restoreGraphicsState);
}

public int imageInterpolation() {
	return OS.objc_msgSend(id, OS.sel_imageInterpolation);
}

public void setImageInterpolation(int value) {
	OS.objc_msgSend(id, OS.sel_setImageInterpolation_1, value);
}

}

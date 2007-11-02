package org.eclipse.swt.internal.cocoa;

public class NSColor extends NSObject {

public NSColor(int id) {
	super(id);
}

public int get_class() {
	return OS.class_NSColor;
}

public static NSColor colorWithDeviceRGBA(float red, float green, float blue, float alpha) {
	int id = OS.objc_msgSend(OS.class_NSColor, OS.sel_colorWithDeviceRed_1green_1blue_1alpha_1, red, green, blue, alpha);
	return id != 0 ? new NSColor(id) : null;
}
}

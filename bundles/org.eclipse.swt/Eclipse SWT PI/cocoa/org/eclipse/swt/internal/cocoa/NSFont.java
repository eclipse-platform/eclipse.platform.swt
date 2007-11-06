package org.eclipse.swt.internal.cocoa;

public class NSFont extends NSObject {

public NSFont(int id) {
	super(id);
}

public int get_class() {
	return OS.class_NSFont;
}

public NSString familyName() {
	int id = OS.objc_msgSend(this.id, OS.sel_familyName);
	return id != 0 ? new NSString(id) : null;
}

public NSString fontName() {
	int id = OS.objc_msgSend(this.id, OS.sel_fontName);
	return id != 0 ? new NSString(id) : null;
}

public float ascender() {
	return (float)OS.objc_msgSend_fpret(id, OS.sel_ascender);
}

public float descender() {
	return (float)OS.objc_msgSend_fpret(id, OS.sel_descender);
}


public static NSFont fontWithName(NSString fontName, float size) {
	int id = OS.objc_msgSend(OS.class_NSFont, OS.sel_fontWithName_1size_1, fontName != null ? fontName.id : 0, size);
	return id != 0 ? new NSFont(id) : null;
}

public float pointSize() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_pointSize);
}
}

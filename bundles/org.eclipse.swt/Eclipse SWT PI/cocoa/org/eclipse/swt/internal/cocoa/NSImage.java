package org.eclipse.swt.internal.cocoa;

public class NSImage extends NSObject {

public NSImage() {
	super(0);
}

public NSImage(int id) {
	super(id);
}

public void addRepresentation(NSImageRep imageRep) {
	OS.objc_msgSend(id, OS.sel_addRepresentation_1, imageRep.id);
}

public int get_class() {
	return OS.class_NSImage;
}

public NSImage initWithSize(NSSize size) {
	int id = OS.objc_msgSend(this.id, OS.sel_initWithSize_1, size);
	return id != 0 ? this : null;
}

public NSSize size() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, id, OS.sel_size);
	return result;
}
}

package org.eclipse.swt.internal.cocoa;

public class NSPICTImageRep extends NSImageRep {

public NSPICTImageRep() {
	super();
}

public NSPICTImageRep(int id) {
	super(id);
}

public NSData PICTRepresentation() {
	int result = OS.objc_msgSend(this.id, OS.sel_PICTRepresentation);
	return result != 0 ? new NSData(result) : null;
}

public NSRect boundingBox() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_boundingBox);
	return result;
}

public static id imageRepWithData(NSData pictData) {
	int result = OS.objc_msgSend(OS.class_NSPICTImageRep, OS.sel_imageRepWithData_1, pictData != null ? pictData.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSPICTImageRep initWithData(NSData pictData) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithData_1, pictData != null ? pictData.id : 0);
	return result != 0 ? this : null;
}

}

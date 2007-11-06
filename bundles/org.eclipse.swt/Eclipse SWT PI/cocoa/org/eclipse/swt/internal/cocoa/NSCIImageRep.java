package org.eclipse.swt.internal.cocoa;

public class NSCIImageRep extends NSImageRep {

public NSCIImageRep() {
	super();
}

public NSCIImageRep(int id) {
	super(id);
}

public CIImage CIImage() {
	int result = OS.objc_msgSend(this.id, OS.sel_CIImage);
	return result != 0 ? new CIImage(result) : null;
}

public static id imageRepWithCIImage(CIImage image) {
	int result = OS.objc_msgSend(OS.class_NSCIImageRep, OS.sel_imageRepWithCIImage_1, image != null ? image.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithCIImage(CIImage image) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithCIImage_1, image != null ? image.id : 0);
	return result != 0 ? new id(result) : null;
}

}

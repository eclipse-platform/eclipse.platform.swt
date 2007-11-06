package org.eclipse.swt.internal.cocoa;

public class CIImage extends NSObject {

public CIImage() {
	super();
}

public CIImage(int id) {
	super(id);
}

public void drawAtPoint(NSPoint point, NSRect fromRect, int op, float delta) {
	OS.objc_msgSend(this.id, OS.sel_drawAtPoint_1fromRect_1operation_1fraction_1, point, fromRect, op, delta);
}

public void drawInRect(NSRect rect, NSRect fromRect, int op, float delta) {
	OS.objc_msgSend(this.id, OS.sel_drawInRect_1fromRect_1operation_1fraction_1, rect, fromRect, op, delta);
}

public id initWithBitmapImageRep(NSBitmapImageRep bitmapImageRep) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithBitmapImageRep_1, bitmapImageRep != null ? bitmapImageRep.id : 0);
	return result != 0 ? new id(result) : null;
}

}

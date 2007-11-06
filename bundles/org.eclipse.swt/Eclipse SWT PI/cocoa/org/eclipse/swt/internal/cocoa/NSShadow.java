package org.eclipse.swt.internal.cocoa;

public class NSShadow extends NSObject {

public NSShadow() {
	super();
}

public NSShadow(int id) {
	super(id);
}

public id init() {
	int result = OS.objc_msgSend(this.id, OS.sel_init);
	return result != 0 ? new id(result) : null;
}

public void set() {
	OS.objc_msgSend(this.id, OS.sel_set);
}

public void setShadowBlurRadius(float val) {
	OS.objc_msgSend(this.id, OS.sel_setShadowBlurRadius_1, val);
}

public void setShadowColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setShadowColor_1, color != null ? color.id : 0);
}

public void setShadowOffset(NSSize offset) {
	OS.objc_msgSend(this.id, OS.sel_setShadowOffset_1, offset);
}

public float shadowBlurRadius() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_shadowBlurRadius);
}

public NSColor shadowColor() {
	int result = OS.objc_msgSend(this.id, OS.sel_shadowColor);
	return result != 0 ? new NSColor(result) : null;
}

public NSSize shadowOffset() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_shadowOffset);
	return result;
}

}

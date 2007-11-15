package org.eclipse.swt.internal.cocoa;

public class CIColor extends NSObject {

public CIColor() {
	super();
}

public CIColor(int id) {
	super(id);
}

public CIColor initWithColor(NSColor color) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithColor_1, color != null ? color.id : 0);
	return result != 0 ? this : null;
}

}

package org.eclipse.swt.internal.cocoa;

public class NSGradient extends NSObject {

public NSGradient() {
	super(0);
}

public NSGradient(int id) {
	super(id);
}

public NSGradient init(NSColor startingColor, NSColor endingColor) {
	int id = OS.objc_msgSend(this.id, OS.sel_initWithStartingColor_1endingColor_1, startingColor.id, endingColor.id);
	return id != 0 ? this : null;
}

public void drawInRect(NSRect rect, float angle) {
	OS.objc_msgSend(id, OS.sel_drawInRect_1angle_1, rect, angle);
}

}

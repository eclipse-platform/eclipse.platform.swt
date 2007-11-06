package org.eclipse.swt.internal.cocoa;

public class NSAttributedString extends NSObject {

public NSAttributedString() {
	super(0);
}

public NSAttributedString(int id) {
	super(id);
}

public NSAttributedString init(NSString string, NSDictionary dictionary) {
	int id = OS.objc_msgSend(this.id, OS.sel_initWithString_1attributes_1, string.id, dictionary.id);
	return id != 0 ? this : null;
}

public void drawAtPoint(NSPoint pt) {
	OS.objc_msgSend(id, OS.sel_drawAtPoint_1, pt);
}

public NSSize size() {
	NSSize result = new NSSize();
	OS.objc_msgSend_size(id, OS.sel_size, result);
	return result;
}
}

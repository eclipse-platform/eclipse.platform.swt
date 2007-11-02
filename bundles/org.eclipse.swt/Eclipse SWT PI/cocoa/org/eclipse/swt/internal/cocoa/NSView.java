package org.eclipse.swt.internal.cocoa;

public class NSView extends NSObject {

public NSView(int id) {
	super(id);
}

//TODO - subclasses
//public int get_class() {
//	return OS.class_NSView;
//}

public NSView initWithFrame(NSRect rect) {
	int id = OS.objc_msgSend(this.id, OS.sel_initWithFrame_1, rect);
	return id != 0 ? this : null;
}

public void addSubview(NSView view) {
	OS.objc_msgSend(this.id, OS.sel_addSubview_1, view != null ? view.id : 0);
}

public int tag() {
	return OS.objc_msgSend(id, OS.sel_tag);
}
}

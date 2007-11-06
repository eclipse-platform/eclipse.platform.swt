package org.eclipse.swt.internal.cocoa;

public class NSView extends NSObject {

public NSView(int id) {
	super(id);
}

//TODO - subclasses
//public int get_class() {
//	return OS.class_NSView;
//}

public void addCursor(NSRect rect, NSCursor cursor) {
	OS.objc_msgSend(this.id, OS.sel_addCursorRect_1cursor_1, rect, cursor != null ? cursor.id : 0);
}

public NSRect frame() {
	NSRect rect = new NSRect();
	OS.objc_msgSend_stret(rect, id, OS.sel_frame);
	return rect;
}

public NSView initWithFrame(NSRect rect) {
	int id = OS.objc_msgSend(this.id, OS.sel_initWithFrame_1, rect);
	return id != 0 ? this : null;
}

public void addSubview(NSView view) {
	OS.objc_msgSend(this.id, OS.sel_addSubview_1, view != null ? view.id : 0);
}

public void setAutoresizesSubviews(boolean flags) {
	OS.objc_msgSend(this.id, OS.sel_setAutoresizesSubviews_1, flags ? 1: 0);
}

public void setFrame(NSRect rect) {
	OS.objc_msgSend(this.id, OS.sel_setFrame_1, rect);
}

public void setFrameOrigin(NSPoint origin) {
	OS.objc_msgSend(this.id, OS.sel_setFrameOrigin_1, origin);
}

public void setFrameSize(NSSize size) {
	OS.objc_msgSend(this.id, OS.sel_setFrameSize_1, size);
}

public void setHidden(boolean hidden) {
	OS.objc_msgSend(this.id, OS.sel_setHidden_1, hidden ? 1 : 0);
}

public int tag() {
	return OS.objc_msgSend(id, OS.sel_tag);
}
}

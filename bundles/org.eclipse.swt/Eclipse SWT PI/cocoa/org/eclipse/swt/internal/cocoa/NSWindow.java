package org.eclipse.swt.internal.cocoa;

public class NSWindow extends NSObject {

public NSWindow() {
	super(0);
}

public NSWindow(int id) {
	super(id);
}

public float alphaValue() {
	return (float)OS.objc_msgSend_fpret(id, OS.sel_alphaValue);
}

public void close() {
	OS.objc_msgSend(id, OS.sel_close);	
}

public NSView contentView() {
	int id = OS.objc_msgSend(this.id, OS.sel_contentView);
	if (id == 0) return null;
	return new NSView(id);
}

public int get_class() {
	return OS.class_NSWindow;
}

public NSWindow initWithContentRect(NSRect rect, int styleMask, int backing, boolean defer) {
	id = OS.objc_msgSend(id, OS.sel_initWithContentRect_1styleMask_1backing_1defer_1, rect, styleMask, backing, defer);
	return this;
}

public boolean isVisible() {
	return OS.objc_msgSend(id, OS.sel_isVisible) != 0;
}

public void makeKeyAndOrderFront(NSObject sender) {
	OS.objc_msgSend(id, OS.sel_makeKeyAndOrderFront_1, sender != null ? sender.id : 0);
}

public void orderOut(NSObject sender) {
	OS.objc_msgSend(id, OS.sel_orderOut_1, sender != null ? sender.id : 0);
}

public void orderFront(NSObject sender) {
	OS.objc_msgSend(id, OS.sel_orderFront_1, sender != null ? sender.id : 0);
}

public void setAlphaValue(float alpha) {
	OS.objc_msgSend(id, OS.sel_setAlphaValue_1, alpha);	
}

public void setDelegate(NSObject delegate) {
	OS.objc_msgSend(id, OS.sel_setDelegate_1, delegate != null ? delegate.id : 0);	
}

public void setTitle(NSString string) {
	OS.objc_msgSend(id, OS.sel_setTitle_1, string != null ? string.id : 0);	
}
}

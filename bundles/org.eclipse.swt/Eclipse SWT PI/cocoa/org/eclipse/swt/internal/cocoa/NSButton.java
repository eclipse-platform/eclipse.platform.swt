package org.eclipse.swt.internal.cocoa;

public class NSButton extends NSView {

public NSButton() {
	super(0);
}
	
public NSButton(int id) {
	super(id);
}

public int get_class() {
	return OS.class_NSButton;
}

public void setButtonType(int type) {
	OS.objc_msgSend(id, OS.sel_setButtonType_1, type);
}

public void setTitle(NSString title) {
	OS.objc_msgSend(id, OS.sel_setTitle_1, title != null ? title.id : 0);
}
}

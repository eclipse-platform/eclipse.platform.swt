package org.eclipse.swt.internal.cocoa;

public class NSButton extends NSControl {

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

public void setBezelStyle(int style) {
	OS.objc_msgSend(id, OS.sel_setBezelStyle_1, style);
}

public void setImage(NSImage image) {
	OS.objc_msgSend(id, OS.sel_setImage_1, image != null ? image.id : 0);
}

public void setTitle(NSString title) {
	OS.objc_msgSend(id, OS.sel_setTitle_1, title != null ? title.id : 0);
}
}

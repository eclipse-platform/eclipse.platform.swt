package org.eclipse.swt.internal.cocoa;

public class NSTextField extends NSControl {

public NSTextField() {
	super(0);
}
	
public NSTextField(int id) {
	super(id);
}

public int get_class() {
		return OS.class_NSTextField;
}

public void setTitle(NSString title) {
	OS.objc_msgSend(id, OS.sel_setTitle_1, title != null ? title.id : 0);
}
}

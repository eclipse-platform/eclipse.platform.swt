package org.eclipse.swt.internal.cocoa;

public class SWTMenu extends NSMenu {

public void setTag(int tag) {
	OS.objc_msgSend(id, OS.sel_setTag_1, tag);
}
}

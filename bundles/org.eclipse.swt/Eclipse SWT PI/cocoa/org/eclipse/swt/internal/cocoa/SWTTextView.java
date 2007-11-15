package org.eclipse.swt.internal.cocoa;

public class SWTTextView extends NSTextView {

public void setTag(int tag) {
	OS.objc_msgSend(id, OS.sel_setTag_1, tag);
}
}

package org.eclipse.swt.internal.cocoa;

public class SWTProgressIndicator extends NSProgressIndicator {

public void setTag(int tag) {
	OS.objc_msgSend(id, OS.sel_setTag_1, tag);
}
}

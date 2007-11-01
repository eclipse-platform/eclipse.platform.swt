package org.eclipse.swt.internal.cocoa;

public class SWTWindowDelegate extends NSObject {

public SWTWindowDelegate() {
	super(0);
}
	
public SWTWindowDelegate(int id) {
	super(id);
}

public int tag() {
	return OS.objc_msgSend(id, OS.sel_tag);
}

public void setTag(int tag) {
	OS.objc_msgSend(id, OS.sel_setTag_1, tag);
}

}

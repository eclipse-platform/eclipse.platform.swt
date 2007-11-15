package org.eclipse.swt.internal.cocoa;

public class SWTScrollView extends NSScrollView {

public SWTScrollView() {
	super(0);
}
	
public SWTScrollView(int id) {
	super(id);
}

public void setTag(int tag) {
	OS.objc_msgSend(id, OS.sel_setTag_1, tag);
}

}

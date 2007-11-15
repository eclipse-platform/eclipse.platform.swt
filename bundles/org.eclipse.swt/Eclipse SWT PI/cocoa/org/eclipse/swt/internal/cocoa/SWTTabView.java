package org.eclipse.swt.internal.cocoa;

public class SWTTabView extends NSTabView {

public SWTTabView() {
	super(0);
}
	
public SWTTabView(int id) {
	super(id);
}

public void setTag(int tag) {
	OS.objc_msgSend(id, OS.sel_setTag_1, tag);
}
}

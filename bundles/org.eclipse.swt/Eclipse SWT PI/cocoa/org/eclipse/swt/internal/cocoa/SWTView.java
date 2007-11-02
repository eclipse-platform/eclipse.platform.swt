package org.eclipse.swt.internal.cocoa;

public class SWTView extends NSScrollView {

public SWTView() {
	super(0);
}
	
public SWTView(int id) {
	super(id);
}

public void setTag(int tag) {
	OS.objc_msgSend(id, OS.sel_setTag_1, tag);
}

}

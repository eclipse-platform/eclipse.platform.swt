package org.eclipse.swt.internal.cocoa;

public class NSQuickDrawView extends NSView {

public NSQuickDrawView() {
	super();
}

public NSQuickDrawView(int id) {
	super(id);
}

public int qdPort() {
	return OS.objc_msgSend(this.id, OS.sel_qdPort);
}

}

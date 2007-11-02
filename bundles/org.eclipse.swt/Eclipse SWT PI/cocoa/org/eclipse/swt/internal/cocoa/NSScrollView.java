package org.eclipse.swt.internal.cocoa;

public class NSScrollView extends NSView {

public NSScrollView() {
	super(0);
}

public NSScrollView(int id) {
	super(id);
}

public void setBackgroundColor(NSColor color) {
	OS.objc_msgSend(id, OS.sel_setBackgroundColor_1, color != null ? color.id : 0);
}

}

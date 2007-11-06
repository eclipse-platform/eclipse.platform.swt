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

public void setHasHorizontalScroller(boolean flag) {
	OS.objc_msgSend(id, OS.sel_setHasHorizontalScroller_1, flag ? 1 : 0);
}

public void setHasVerticalScroller(boolean flag) {
	OS.objc_msgSend(id, OS.sel_setHasVerticalScroller_1, flag ? 1 : 0);
}

public void setDocumentView(NSView aView) {
	OS.objc_msgSend(id, OS.sel_setDocumentView_1, aView != null ? aView.id : 0);
}

}

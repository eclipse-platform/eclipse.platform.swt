package org.eclipse.swt.internal.cocoa;

public class NSTableHeaderCell extends NSTextFieldCell {

public NSTableHeaderCell() {
	super();
}

public NSTableHeaderCell(int id) {
	super(id);
}

public void drawSortIndicatorWithFrame(NSRect cellFrame, NSView controlView, boolean ascending, int priority) {
	OS.objc_msgSend(this.id, OS.sel_drawSortIndicatorWithFrame_1inView_1ascending_1priority_1, cellFrame, controlView != null ? controlView.id : 0, ascending, priority);
}

public NSRect sortIndicatorRectForBounds(NSRect theRect) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_sortIndicatorRectForBounds_1, theRect);
	return result;
}

}

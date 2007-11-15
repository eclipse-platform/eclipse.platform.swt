package org.eclipse.swt.internal.cocoa;

public class NSTableHeaderView extends NSView {

public NSTableHeaderView() {
	super();
}

public NSTableHeaderView(int id) {
	super(id);
}

public int columnAtPoint(NSPoint point) {
	return OS.objc_msgSend(this.id, OS.sel_columnAtPoint_1, point);
}

public int draggedColumn() {
	return OS.objc_msgSend(this.id, OS.sel_draggedColumn);
}

public float draggedDistance() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_draggedDistance);
}

public NSRect headerRectOfColumn(int column) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_headerRectOfColumn_1, column);
	return result;
}

public int resizedColumn() {
	return OS.objc_msgSend(this.id, OS.sel_resizedColumn);
}

public void setTableView(NSTableView tableView) {
	OS.objc_msgSend(this.id, OS.sel_setTableView_1, tableView != null ? tableView.id : 0);
}

public NSTableView tableView() {
	int result = OS.objc_msgSend(this.id, OS.sel_tableView);
	return result != 0 ? new NSTableView(result) : null;
}

}

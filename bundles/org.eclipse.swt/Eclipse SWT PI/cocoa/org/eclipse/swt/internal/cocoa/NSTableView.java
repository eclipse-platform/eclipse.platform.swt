package org.eclipse.swt.internal.cocoa;

public class NSTableView extends NSControl {

public NSTableView() {
	super(0);
}
	
public NSTableView(int id) {
	super(id);
}

//public int get_class() {
//		return OS.class_NSTableView;
//}

public void setDataSource(NSObject anObject) {
	OS.objc_msgSend(id, OS.sel_setDataSource_1, anObject != null ? anObject.id : 0);
}

public void addTableColumn(NSTableColumn aColumn) {
	OS.objc_msgSend(id, OS.sel_addTableColumn_1, aColumn != null ? aColumn.id : 0);
}

public void setGridStyleMask (int gridType) {
	OS.objc_msgSend(id, OS.sel_setGridStyleMask_1, gridType);
}

public void setHeaderView(NSTableHeaderView aHeaderView) {
	OS.objc_msgSend(id, OS.sel_setHeaderView_1, aHeaderView != null ? aHeaderView.id : 0);
}

public void setDelegate(NSObject delegate) {
	OS.objc_msgSend(id, OS.sel_setDelegate_1, delegate != null ? delegate.id : 0);	
}

public void reloadData() {
	OS.objc_msgSend(id, OS.sel_reloadData);
}

public void tile() {
	OS.objc_msgSend(id, OS.sel_tile);
}

public void setColumnAutoresizingStyle(int style) {
	OS.objc_msgSend(id, OS.sel_setColumnAutoresizingStyle_1, style);	
}

public void moveColumn(int columnIndex, int newIndex) {
	OS.objc_msgSend(id, OS.sel_moveColumn_1toColumn_1, columnIndex, newIndex);	
}

}

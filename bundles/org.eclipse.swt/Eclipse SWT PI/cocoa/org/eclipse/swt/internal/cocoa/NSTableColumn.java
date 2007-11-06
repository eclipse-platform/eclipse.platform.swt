package org.eclipse.swt.internal.cocoa;

public class NSTableColumn extends NSObject {

public NSTableColumn() {
	super(0);
}

public NSTableColumn(int id) {
	super(id);
}

public int get_class() {
	return OS.class_NSTableColumn;
}

public void initWithIdentifier(NSObject identifier) {
	OS.objc_msgSend(id, OS.sel_initWithIdentifier_1, identifier != null ? identifier.id : 0);
}

public void setWidth(float newWidth) {
	OS.objc_msgSend(id, OS.sel_setWidth_1, newWidth);
}

public void setResizingMask(int resizingMask) {
	OS.objc_msgSend(id, OS.sel_setResizingMask_1, resizingMask);
}

}

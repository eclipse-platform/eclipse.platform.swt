package org.eclipse.swt.internal.cocoa;

public class NSArray extends NSObject {

public NSArray(int id) {
	super(id);
}

public int count() {
	return OS.objc_msgSend(id, OS.sel_count);
}

public int objectAtIndex(int index) {
	return OS.objc_msgSend(id, OS.sel_objectAtIndex_1, index);	
}
}

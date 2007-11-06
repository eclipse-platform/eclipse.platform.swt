package org.eclipse.swt.internal.cocoa;

public class NSMutableDictionary extends NSDictionary {

public NSMutableDictionary() {
	super(0);
}

public NSMutableDictionary(int id) {
	super(id);
}

public static NSMutableDictionary dictionaryWithCapacity(int numItems) {
	int id = OS.objc_msgSend(OS.class_NSMutableDictionary, OS.sel_dictionaryWithCapacity_1, numItems);
	return id != 0 ? new NSMutableDictionary(id) : null;
}

public void setObject(NSObject object, int key) {
	OS.objc_msgSend(id, OS.sel_setObject_1forKey_1, object.id, key);
}
}

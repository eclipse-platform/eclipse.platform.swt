package org.eclipse.swt.internal.cocoa;

public class NSNull extends NSObject {

public NSNull() {
	super();
}

public NSNull(int id) {
	super(id);
}

public static NSNull null_() {
	int result = OS.objc_msgSend(OS.class_NSNull, OS.sel_null);
	return result != 0 ? new NSNull(result) : null;
}

}

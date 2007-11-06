package org.eclipse.swt.internal.cocoa;

public class NSEnumerator extends NSObject {

public NSEnumerator() {
	super();
}

public NSEnumerator(int id) {
	super(id);
}

public NSArray allObjects() {
	int result = OS.objc_msgSend(this.id, OS.sel_allObjects);
	return result != 0 ? new NSArray(result) : null;
}

public id nextObject() {
	int result = OS.objc_msgSend(this.id, OS.sel_nextObject);
	return result != 0 ? new id(result) : null;
}

}

package org.eclipse.swt.internal.cocoa;

public class NSCountedSet extends NSMutableSet {

public NSCountedSet() {
	super();
}

public NSCountedSet(int id) {
	super(id);
}

public void addObject(id object) {
	OS.objc_msgSend(this.id, OS.sel_addObject_1, object != null ? object.id : 0);
}

public int countForObject(id object) {
	return OS.objc_msgSend(this.id, OS.sel_countForObject_1, object != null ? object.id : 0);
}

public id initWithArray(NSArray array) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithArray_1, array != null ? array.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithCapacity(int numItems) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithCapacity_1, numItems);
	return result != 0 ? new id(result) : null;
}

public id initWithSet(NSSet set) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithSet_1, set != null ? set.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSEnumerator objectEnumerator() {
	int result = OS.objc_msgSend(this.id, OS.sel_objectEnumerator);
	return result != 0 ? new NSEnumerator(result) : null;
}

public void removeObject(id object) {
	OS.objc_msgSend(this.id, OS.sel_removeObject_1, object != null ? object.id : 0);
}

}

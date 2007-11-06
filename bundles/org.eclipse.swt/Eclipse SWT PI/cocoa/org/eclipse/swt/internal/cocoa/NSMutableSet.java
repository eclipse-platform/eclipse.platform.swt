package org.eclipse.swt.internal.cocoa;

public class NSMutableSet extends NSSet {

public NSMutableSet() {
	super();
}

public NSMutableSet(int id) {
	super(id);
}

public void addObject(id object) {
	OS.objc_msgSend(this.id, OS.sel_addObject_1, object != null ? object.id : 0);
}

public void addObjectsFromArray(NSArray array) {
	OS.objc_msgSend(this.id, OS.sel_addObjectsFromArray_1, array != null ? array.id : 0);
}

public void filterUsingPredicate(NSPredicate predicate) {
	OS.objc_msgSend(this.id, OS.sel_filterUsingPredicate_1, predicate != null ? predicate.id : 0);
}

public id initWithCapacity(int numItems) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithCapacity_1, numItems);
	return result != 0 ? new id(result) : null;
}

public void intersectSet(NSSet otherSet) {
	OS.objc_msgSend(this.id, OS.sel_intersectSet_1, otherSet != null ? otherSet.id : 0);
}

public void minusSet(NSSet otherSet) {
	OS.objc_msgSend(this.id, OS.sel_minusSet_1, otherSet != null ? otherSet.id : 0);
}

public void removeAllObjects() {
	OS.objc_msgSend(this.id, OS.sel_removeAllObjects);
}

public void removeObject(id object) {
	OS.objc_msgSend(this.id, OS.sel_removeObject_1, object != null ? object.id : 0);
}

public void setSet(NSSet otherSet) {
	OS.objc_msgSend(this.id, OS.sel_setSet_1, otherSet != null ? otherSet.id : 0);
}

public static id setWithCapacity(int numItems) {
	int result = OS.objc_msgSend(OS.class_NSMutableSet, OS.sel_setWithCapacity_1, numItems);
	return result != 0 ? new id(result) : null;
}

public void unionSet(NSSet otherSet) {
	OS.objc_msgSend(this.id, OS.sel_unionSet_1, otherSet != null ? otherSet.id : 0);
}

}

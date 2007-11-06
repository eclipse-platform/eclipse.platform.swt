package org.eclipse.swt.internal.cocoa;

public class NSObject extends id {

public int id;

public NSObject() {
}

public NSObject(int id) {
	this.id = id;
}

public NSObject alloc() {
	id = OS.objc_msgSend(get_class(), OS.sel_alloc);
	if (id == 0) return null;
	return this;
}

public int copy() {
	return OS.objc_msgSend(get_class(), OS.sel_copy);
}

public int get_class() {
	String name = getClass().getName();
	int index = name.lastIndexOf('.');
	if (index != -1) name = name.substring(index + 1);
	return OS.objc_getClass(name);
}

public NSObject init() {
	OS.objc_msgSend(id, OS.sel_init);
	return this;
}

public void release() {
	OS.objc_msgSend(id, OS.sel_release);
	id = 0;
}

public boolean respondsToSelector(int sel) {
	return OS.objc_msgSend(id, OS.sel_respondsToSelector_1, sel) != 0;
}
}

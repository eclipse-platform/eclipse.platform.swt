package org.eclipse.swt.internal.cocoa;

public class NSAutoreleasePool extends NSObject {

public NSAutoreleasePool() {
	super(0);
}
	
public NSAutoreleasePool(int id) {
	super(id);
}

public int get_class() {
	return OS.class_NSAutoreleasePool;
}

}

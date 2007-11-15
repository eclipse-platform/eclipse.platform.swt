package org.eclipse.swt.internal.cocoa;

public class NSMethodSignature extends NSObject {

public NSMethodSignature() {
	super();
}

public NSMethodSignature(int id) {
	super(id);
}

public int frameLength() {
	return OS.objc_msgSend(this.id, OS.sel_frameLength);
}

public int getArgumentTypeAtIndex(int idx) {
	return OS.objc_msgSend(this.id, OS.sel_getArgumentTypeAtIndex_1, idx);
}

public boolean isOneway() {
	return OS.objc_msgSend(this.id, OS.sel_isOneway) != 0;
}

public int methodReturnLength() {
	return OS.objc_msgSend(this.id, OS.sel_methodReturnLength);
}

public int methodReturnType() {
	return OS.objc_msgSend(this.id, OS.sel_methodReturnType);
}

public int numberOfArguments() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfArguments);
}

public static NSMethodSignature signatureWithObjCTypes(int types) {
	int result = OS.objc_msgSend(OS.class_NSMethodSignature, OS.sel_signatureWithObjCTypes_1, types);
	return result != 0 ? new NSMethodSignature(result) : null;
}

}

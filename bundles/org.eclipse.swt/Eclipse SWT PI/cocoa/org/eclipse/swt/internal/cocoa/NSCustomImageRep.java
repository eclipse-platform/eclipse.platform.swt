package org.eclipse.swt.internal.cocoa;

public class NSCustomImageRep extends NSImageRep {

public NSCustomImageRep() {
	super();
}

public NSCustomImageRep(int id) {
	super(id);
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public int drawSelector() {
	return OS.objc_msgSend(this.id, OS.sel_drawSelector);
}

public NSCustomImageRep initWithDrawSelector(int aMethod, id anObject) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithDrawSelector_1delegate_1, aMethod, anObject != null ? anObject.id : 0);
	return result != 0 ? this : null;
}

}

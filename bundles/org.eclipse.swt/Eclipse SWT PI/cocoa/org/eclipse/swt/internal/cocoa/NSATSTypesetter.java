package org.eclipse.swt.internal.cocoa;

public class NSATSTypesetter extends NSTypesetter {

public NSATSTypesetter() {
	super();
}

public NSATSTypesetter(int id) {
	super(id);
}

public NSRect lineFragmentRectForProposedRect(NSRect proposedRect, int remainingRect) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_lineFragmentRectForProposedRect_1remainingRect_1, proposedRect, remainingRect);
	return result;
}

public static id sharedTypesetter() {
	int result = OS.objc_msgSend(OS.class_NSATSTypesetter, OS.sel_sharedTypesetter);
	return result != 0 ? new id(result) : null;
}

}

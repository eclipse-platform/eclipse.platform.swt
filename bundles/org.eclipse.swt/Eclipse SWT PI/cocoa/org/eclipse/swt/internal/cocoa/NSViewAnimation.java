package org.eclipse.swt.internal.cocoa;

public class NSViewAnimation extends NSAnimation {

public NSViewAnimation() {
	super();
}

public NSViewAnimation(int id) {
	super(id);
}

public id initWithViewAnimations(NSArray viewAnimations) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithViewAnimations_1, viewAnimations != null ? viewAnimations.id : 0);
	return result != 0 ? new id(result) : null;
}

public void setViewAnimations(NSArray viewAnimations) {
	OS.objc_msgSend(this.id, OS.sel_setViewAnimations_1, viewAnimations != null ? viewAnimations.id : 0);
}

public NSArray viewAnimations() {
	int result = OS.objc_msgSend(this.id, OS.sel_viewAnimations);
	return result != 0 ? new NSArray(result) : null;
}

}

package org.eclipse.swt.internal.cocoa;

public class NSAnimationContext extends NSObject {

public NSAnimationContext() {
	super();
}

public NSAnimationContext(int id) {
	super(id);
}

public static void beginGrouping() {
	OS.objc_msgSend(OS.class_NSAnimationContext, OS.sel_beginGrouping);
}

public static NSAnimationContext currentContext() {
	int result = OS.objc_msgSend(OS.class_NSAnimationContext, OS.sel_currentContext);
	return result != 0 ? new NSAnimationContext(result) : null;
}

public double duration() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_duration);
}

public static void endGrouping() {
	OS.objc_msgSend(OS.class_NSAnimationContext, OS.sel_endGrouping);
}

public void setDuration(double duration) {
	OS.objc_msgSend(this.id, OS.sel_setDuration_1, duration);
}

}

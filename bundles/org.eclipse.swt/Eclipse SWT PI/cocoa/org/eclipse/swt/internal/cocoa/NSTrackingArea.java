package org.eclipse.swt.internal.cocoa;

public class NSTrackingArea extends NSObject {

public NSTrackingArea() {
	super();
}

public NSTrackingArea(int id) {
	super(id);
}

public NSTrackingArea initWithRect(NSRect rect, int options, id owner, NSDictionary userInfo) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithRect_1options_1owner_1userInfo_1, rect, options, owner != null ? owner.id : 0, userInfo != null ? userInfo.id : 0);
	return result == this.id ? this : (result != 0 ? new NSTrackingArea(result) : null);
}

public int options() {
	return OS.objc_msgSend(this.id, OS.sel_options);
}

public id owner() {
	int result = OS.objc_msgSend(this.id, OS.sel_owner);
	return result != 0 ? new id(result) : null;
}

public NSRect rect() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rect);
	return result;
}

public NSDictionary userInfo() {
	int result = OS.objc_msgSend(this.id, OS.sel_userInfo);
	return result != 0 ? new NSDictionary(result) : null;
}

}

package org.eclipse.swt.internal.cocoa;

public class DOMEvent extends NSObject {

public DOMEvent() {
	super();
}

public DOMEvent(int id) {
	super(id);
}

public boolean bubbles() {
	return OS.objc_msgSend(this.id, OS.sel_bubbles) != 0;
}

public boolean cancelable() {
	return OS.objc_msgSend(this.id, OS.sel_cancelable) != 0;
}

public id  currentTarget() {
	int result = OS.objc_msgSend(this.id, OS.sel_currentTarget);
	return result != 0 ? new id (result) : null;
}

public short eventPhase() {
	return (short)OS.objc_msgSend(this.id, OS.sel_eventPhase);
}

public void initEvent___(NSString initEvent, boolean canBubbleArg, boolean cancelableArg) {
	OS.objc_msgSend(this.id, OS.sel_initEvent_1_1_1, initEvent != null ? initEvent.id : 0, canBubbleArg, cancelableArg);
}

public void initEvent_canBubbleArg_cancelableArg_(NSString eventTypeArg, boolean canBubbleArg, boolean cancelableArg) {
	OS.objc_msgSend(this.id, OS.sel_initEvent_1canBubbleArg_1cancelableArg_1, eventTypeArg != null ? eventTypeArg.id : 0, canBubbleArg, cancelableArg);
}

public void preventDefault() {
	OS.objc_msgSend(this.id, OS.sel_preventDefault);
}

public void stopPropagation() {
	OS.objc_msgSend(this.id, OS.sel_stopPropagation);
}

public id  target() {
	int result = OS.objc_msgSend(this.id, OS.sel_target);
	return result != 0 ? new id (result) : null;
}

public long timeStamp() {
	return (long)OS.objc_msgSend(this.id, OS.sel_timeStamp);
}

public NSString type() {
	int result = OS.objc_msgSend(this.id, OS.sel_type);
	return result != 0 ? new NSString(result) : null;
}
}

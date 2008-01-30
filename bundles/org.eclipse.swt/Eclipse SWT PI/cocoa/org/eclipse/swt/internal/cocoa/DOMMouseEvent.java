package org.eclipse.swt.internal.cocoa;

public class DOMMouseEvent extends NSObject {

public DOMMouseEvent() {
	super();
}

public DOMMouseEvent(int id) {
	super(id);
}

public boolean altKey() {
	return OS.objc_msgSend(this.id, OS.sel_altKey) != 0;
}

public short button() {
	return (short)OS.objc_msgSend(this.id, OS.sel_button);
}

public int clientX() {
	return OS.objc_msgSend(this.id, OS.sel_clientX);
}

public int clientY() {
	return OS.objc_msgSend(this.id, OS.sel_clientY);
}

public boolean ctrlKey() {
	return OS.objc_msgSend(this.id, OS.sel_ctrlKey) != 0;
}

public int detail() {
	return OS.objc_msgSend(this.id, OS.sel_detail);
}

//public void initMouseEvent_______________(NSString initMouseEvent, boolean canBubble, boolean cancelable, DOMAbstractView view, int detail, int screenX, int screenY, int clientX, int clientY, boolean ctrlKey, boolean altKey, boolean shiftKey, boolean metaKey, short button, id relatedTarget) {
//	OS.objc_msgSend(this.id, OS.sel_initMouseEvent_1_1_1_1_1_1_1_1_1_1_1_1_1_1_1, initMouseEvent != null ? initMouseEvent.id : 0, canBubble, cancelable, view != null ? view.id : 0, detail, screenX, screenY, clientX, clientY, ctrlKey, altKey, shiftKey, metaKey, button, relatedTarget != null ? relatedTarget.id : 0);
//}

//public void initMouseEvent_canBubble_cancelable_view_detail_screenX_screenY_clientX_clientY_ctrlKey_altKey_shiftKey_metaKey_button_relatedTarget_(NSString type, boolean canBubble, boolean cancelable, DOMAbstractView view, int detail, int screenX, int screenY, int clientX, int clientY, boolean ctrlKey, boolean altKey, boolean shiftKey, boolean metaKey, short button, id  relatedTarget) {
//	OS.objc_msgSend(this.id, OS.sel_initMouseEvent_1canBubble_1cancelable_1view_1detail_1screenX_1screenY_1clientX_1clientY_1ctrlKey_1altKey_1shiftKey_1metaKey_1button_1relatedTarget_1, type != null ? type.id : 0, canBubble, cancelable, view != null ? view.id : 0, detail, screenX, screenY, clientX, clientY, ctrlKey, altKey, shiftKey, metaKey, button, relatedTarget != null ? relatedTarget.id : 0);
//}

public boolean metaKey() {
	return OS.objc_msgSend(this.id, OS.sel_metaKey) != 0;
}

public id  relatedTarget() {
	int result = OS.objc_msgSend(this.id, OS.sel_relatedTarget);
	return result != 0 ? new id (result) : null;
}

public int screenX() {
	return OS.objc_msgSend(this.id, OS.sel_screenX);
}

public int screenY() {
	return OS.objc_msgSend(this.id, OS.sel_screenY);
}

public boolean shiftKey() {
	return OS.objc_msgSend(this.id, OS.sel_shiftKey) != 0;
}

}

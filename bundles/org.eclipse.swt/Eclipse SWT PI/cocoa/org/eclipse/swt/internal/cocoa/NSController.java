package org.eclipse.swt.internal.cocoa;

public class NSController extends NSObject {

public NSController() {
	super();
}

public NSController(int id) {
	super(id);
}

public boolean commitEditing() {
	return OS.objc_msgSend(this.id, OS.sel_commitEditing) != 0;
}

public void commitEditingWithDelegate(id delegate, int didCommitSelector, int contextInfo) {
	OS.objc_msgSend(this.id, OS.sel_commitEditingWithDelegate_1didCommitSelector_1contextInfo_1, delegate != null ? delegate.id : 0, didCommitSelector, contextInfo);
}

public void discardEditing() {
	OS.objc_msgSend(this.id, OS.sel_discardEditing);
}

public boolean isEditing() {
	return OS.objc_msgSend(this.id, OS.sel_isEditing) != 0;
}

public void objectDidBeginEditing(id editor) {
	OS.objc_msgSend(this.id, OS.sel_objectDidBeginEditing_1, editor != null ? editor.id : 0);
}

public void objectDidEndEditing(id editor) {
	OS.objc_msgSend(this.id, OS.sel_objectDidEndEditing_1, editor != null ? editor.id : 0);
}

}

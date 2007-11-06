package org.eclipse.swt.internal.cocoa;

public class NSPredicateEditor extends NSRuleEditor {

public NSPredicateEditor() {
	super();
}

public NSPredicateEditor(int id) {
	super(id);
}

public NSArray rowTemplates() {
	int result = OS.objc_msgSend(this.id, OS.sel_rowTemplates);
	return result != 0 ? new NSArray(result) : null;
}

public void setRowTemplates(NSArray rowTemplates) {
	OS.objc_msgSend(this.id, OS.sel_setRowTemplates_1, rowTemplates != null ? rowTemplates.id : 0);
}

}

package org.eclipse.swt.internal.cocoa;

public class NSMutableAttributedString extends NSAttributedString {

public NSMutableAttributedString() {
	super();
}

public NSMutableAttributedString(int id) {
	super(id);
}

public void addAttribute(NSString name, id value, NSRange range) {
	OS.objc_msgSend(this.id, OS.sel_addAttribute_1value_1range_1, name != null ? name.id : 0, value != null ? value.id : 0, range);
}

public void addAttributes(NSDictionary attrs, NSRange range) {
	OS.objc_msgSend(this.id, OS.sel_addAttributes_1range_1, attrs != null ? attrs.id : 0, range);
}

public void appendAttributedString(NSAttributedString attrString) {
	OS.objc_msgSend(this.id, OS.sel_appendAttributedString_1, attrString != null ? attrString.id : 0);
}

public void beginEditing() {
	OS.objc_msgSend(this.id, OS.sel_beginEditing);
}

public void deleteCharactersInRange(NSRange range) {
	OS.objc_msgSend(this.id, OS.sel_deleteCharactersInRange_1, range);
}

public void endEditing() {
	OS.objc_msgSend(this.id, OS.sel_endEditing);
}

public void insertAttributedString(NSAttributedString attrString, int loc) {
	OS.objc_msgSend(this.id, OS.sel_insertAttributedString_1atIndex_1, attrString != null ? attrString.id : 0, loc);
}

public NSMutableString mutableString() {
	int result = OS.objc_msgSend(this.id, OS.sel_mutableString);
	return result != 0 ? new NSMutableString(result) : null;
}

public void removeAttribute(NSString name, NSRange range) {
	OS.objc_msgSend(this.id, OS.sel_removeAttribute_1range_1, name != null ? name.id : 0, range);
}

public void replaceCharactersInRange_withAttributedString_(NSRange range, NSAttributedString attrString) {
	OS.objc_msgSend(this.id, OS.sel_replaceCharactersInRange_1withAttributedString_1, range, attrString != null ? attrString.id : 0);
}

public void replaceCharactersInRange_withString_(NSRange range, NSString str) {
	OS.objc_msgSend(this.id, OS.sel_replaceCharactersInRange_1withString_1, range, str != null ? str.id : 0);
}

public void setAttributedString(NSAttributedString attrString) {
	OS.objc_msgSend(this.id, OS.sel_setAttributedString_1, attrString != null ? attrString.id : 0);
}

public void setAttributes(NSDictionary attrs, NSRange range) {
	OS.objc_msgSend(this.id, OS.sel_setAttributes_1range_1, attrs != null ? attrs.id : 0, range);
}

}

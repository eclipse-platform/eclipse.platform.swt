package org.eclipse.swt.internal.cocoa;

public class NSSpellServer extends NSObject {

public NSSpellServer() {
	super();
}

public NSSpellServer(int id) {
	super(id);
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public boolean isWordInUserDictionaries(NSString word, boolean flag) {
	return OS.objc_msgSend(this.id, OS.sel_isWordInUserDictionaries_1caseSensitive_1, word != null ? word.id : 0, flag) != 0;
}

public boolean registerLanguage(NSString language, NSString vendor) {
	return OS.objc_msgSend(this.id, OS.sel_registerLanguage_1byVendor_1, language != null ? language.id : 0, vendor != null ? vendor.id : 0) != 0;
}

public void run() {
	OS.objc_msgSend(this.id, OS.sel_run);
}

public void setDelegate(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, anObject != null ? anObject.id : 0);
}

}

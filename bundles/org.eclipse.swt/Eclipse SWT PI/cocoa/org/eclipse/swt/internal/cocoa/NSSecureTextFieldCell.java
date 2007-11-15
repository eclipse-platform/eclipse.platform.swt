package org.eclipse.swt.internal.cocoa;

public class NSSecureTextFieldCell extends NSTextFieldCell {

public NSSecureTextFieldCell() {
	super();
}

public NSSecureTextFieldCell(int id) {
	super(id);
}

public boolean echosBullets() {
	return OS.objc_msgSend(this.id, OS.sel_echosBullets) != 0;
}

public void setEchosBullets(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setEchosBullets_1, flag);
}

}

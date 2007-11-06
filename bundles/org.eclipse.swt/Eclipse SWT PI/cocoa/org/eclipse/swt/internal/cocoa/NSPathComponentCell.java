package org.eclipse.swt.internal.cocoa;

public class NSPathComponentCell extends NSTextFieldCell {

public NSPathComponentCell() {
	super();
}

public NSPathComponentCell(int id) {
	super(id);
}

public NSURL URL() {
	int result = OS.objc_msgSend(this.id, OS.sel_URL);
	return result != 0 ? new NSURL(result) : null;
}

public NSImage image() {
	int result = OS.objc_msgSend(this.id, OS.sel_image);
	return result != 0 ? new NSImage(result) : null;
}

public void setImage(NSImage value) {
	OS.objc_msgSend(this.id, OS.sel_setImage_1, value != null ? value.id : 0);
}

public void setURL(NSURL url) {
	OS.objc_msgSend(this.id, OS.sel_setURL_1, url != null ? url.id : 0);
}

}

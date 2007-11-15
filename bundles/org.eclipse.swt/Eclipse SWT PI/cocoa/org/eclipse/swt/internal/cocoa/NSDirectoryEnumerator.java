package org.eclipse.swt.internal.cocoa;

public class NSDirectoryEnumerator extends NSEnumerator {

public NSDirectoryEnumerator() {
	super();
}

public NSDirectoryEnumerator(int id) {
	super(id);
}

public NSDictionary directoryAttributes() {
	int result = OS.objc_msgSend(this.id, OS.sel_directoryAttributes);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSDictionary fileAttributes() {
	int result = OS.objc_msgSend(this.id, OS.sel_fileAttributes);
	return result != 0 ? new NSDictionary(result) : null;
}

public void skipDescendents() {
	OS.objc_msgSend(this.id, OS.sel_skipDescendents);
}

}

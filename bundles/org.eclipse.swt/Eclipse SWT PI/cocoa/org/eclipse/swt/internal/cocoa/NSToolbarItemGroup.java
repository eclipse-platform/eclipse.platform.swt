package org.eclipse.swt.internal.cocoa;

public class NSToolbarItemGroup extends NSToolbarItem {

public NSToolbarItemGroup() {
	super();
}

public NSToolbarItemGroup(int id) {
	super(id);
}

public void setSubitems(NSArray subitems) {
	OS.objc_msgSend(this.id, OS.sel_setSubitems_1, subitems != null ? subitems.id : 0);
}

public NSArray subitems() {
	int result = OS.objc_msgSend(this.id, OS.sel_subitems);
	return result != 0 ? new NSArray(result) : null;
}

}

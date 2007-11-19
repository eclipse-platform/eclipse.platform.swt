/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSCollectionViewItem extends NSObject {

public NSCollectionViewItem() {
	super();
}

public NSCollectionViewItem(int id) {
	super(id);
}

public NSCollectionView collectionView() {
	int result = OS.objc_msgSend(this.id, OS.sel_collectionView);
	return result != 0 ? new NSCollectionView(result) : null;
}

public boolean isSelected() {
	return OS.objc_msgSend(this.id, OS.sel_isSelected) != 0;
}

public id representedObject() {
	int result = OS.objc_msgSend(this.id, OS.sel_representedObject);
	return result != 0 ? new id(result) : null;
}

public void setRepresentedObject(id object) {
	OS.objc_msgSend(this.id, OS.sel_setRepresentedObject_1, object != null ? object.id : 0);
}

public void setSelected(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setSelected_1, flag);
}

public void setView(NSView view) {
	OS.objc_msgSend(this.id, OS.sel_setView_1, view != null ? view.id : 0);
}

public NSView view() {
	int result = OS.objc_msgSend(this.id, OS.sel_view);
	return result != 0 ? new NSView(result) : null;
}

}

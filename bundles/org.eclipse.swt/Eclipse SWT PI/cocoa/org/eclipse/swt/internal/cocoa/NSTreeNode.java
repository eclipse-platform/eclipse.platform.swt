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

public class NSTreeNode extends NSObject {

public NSTreeNode() {
	super();
}

public NSTreeNode(int id) {
	super(id);
}

public NSArray childNodes() {
	int result = OS.objc_msgSend(this.id, OS.sel_childNodes);
	return result != 0 ? new NSArray(result) : null;
}

public NSTreeNode descendantNodeAtIndexPath(NSIndexPath indexPath) {
	int result = OS.objc_msgSend(this.id, OS.sel_descendantNodeAtIndexPath_1, indexPath != null ? indexPath.id : 0);
	return result == this.id ? this : (result != 0 ? new NSTreeNode(result) : null);
}

public NSIndexPath indexPath() {
	int result = OS.objc_msgSend(this.id, OS.sel_indexPath);
	return result != 0 ? new NSIndexPath(result) : null;
}

public id initWithRepresentedObject(id modelObject) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithRepresentedObject_1, modelObject != null ? modelObject.id : 0);
	return result != 0 ? new id(result) : null;
}

public boolean isLeaf() {
	return OS.objc_msgSend(this.id, OS.sel_isLeaf) != 0;
}

public NSMutableArray mutableChildNodes() {
	int result = OS.objc_msgSend(this.id, OS.sel_mutableChildNodes);
	return result != 0 ? new NSMutableArray(result) : null;
}

public NSTreeNode parentNode() {
	int result = OS.objc_msgSend(this.id, OS.sel_parentNode);
	return result == this.id ? this : (result != 0 ? new NSTreeNode(result) : null);
}

public id representedObject() {
	int result = OS.objc_msgSend(this.id, OS.sel_representedObject);
	return result != 0 ? new id(result) : null;
}

public void sortWithSortDescriptors(NSArray sortDescriptors, boolean recursively) {
	OS.objc_msgSend(this.id, OS.sel_sortWithSortDescriptors_1recursively_1, sortDescriptors != null ? sortDescriptors.id : 0, recursively);
}

public static id treeNodeWithRepresentedObject(id modelObject) {
	int result = OS.objc_msgSend(OS.class_NSTreeNode, OS.sel_treeNodeWithRepresentedObject_1, modelObject != null ? modelObject.id : 0);
	return result != 0 ? new id(result) : null;
}

}

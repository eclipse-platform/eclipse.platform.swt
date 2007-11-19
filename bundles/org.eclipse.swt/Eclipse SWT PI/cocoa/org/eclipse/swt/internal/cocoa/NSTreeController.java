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

public class NSTreeController extends NSObjectController {

public NSTreeController() {
	super();
}

public NSTreeController(int id) {
	super(id);
}

public void add(id sender) {
	OS.objc_msgSend(this.id, OS.sel_add_1, sender != null ? sender.id : 0);
}

public void addChild(id sender) {
	OS.objc_msgSend(this.id, OS.sel_addChild_1, sender != null ? sender.id : 0);
}

public boolean addSelectionIndexPaths(NSArray indexPaths) {
	return OS.objc_msgSend(this.id, OS.sel_addSelectionIndexPaths_1, indexPaths != null ? indexPaths.id : 0) != 0;
}

public boolean alwaysUsesMultipleValuesMarker() {
	return OS.objc_msgSend(this.id, OS.sel_alwaysUsesMultipleValuesMarker) != 0;
}

public id arrangedObjects() {
	int result = OS.objc_msgSend(this.id, OS.sel_arrangedObjects);
	return result != 0 ? new id(result) : null;
}

public boolean avoidsEmptySelection() {
	return OS.objc_msgSend(this.id, OS.sel_avoidsEmptySelection) != 0;
}

public boolean canAddChild() {
	return OS.objc_msgSend(this.id, OS.sel_canAddChild) != 0;
}

public boolean canInsert() {
	return OS.objc_msgSend(this.id, OS.sel_canInsert) != 0;
}

public boolean canInsertChild() {
	return OS.objc_msgSend(this.id, OS.sel_canInsertChild) != 0;
}

public NSString childrenKeyPath() {
	int result = OS.objc_msgSend(this.id, OS.sel_childrenKeyPath);
	return result != 0 ? new NSString(result) : null;
}

public NSString childrenKeyPathForNode(NSTreeNode node) {
	int result = OS.objc_msgSend(this.id, OS.sel_childrenKeyPathForNode_1, node != null ? node.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public id content() {
	int result = OS.objc_msgSend(this.id, OS.sel_content);
	return result != 0 ? new id(result) : null;
}

public NSString countKeyPath() {
	int result = OS.objc_msgSend(this.id, OS.sel_countKeyPath);
	return result != 0 ? new NSString(result) : null;
}

public NSString countKeyPathForNode(NSTreeNode node) {
	int result = OS.objc_msgSend(this.id, OS.sel_countKeyPathForNode_1, node != null ? node.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public void insert(id sender) {
	OS.objc_msgSend(this.id, OS.sel_insert_1, sender != null ? sender.id : 0);
}

public void insertChild(id sender) {
	OS.objc_msgSend(this.id, OS.sel_insertChild_1, sender != null ? sender.id : 0);
}

public void insertObject(id object, NSIndexPath indexPath) {
	OS.objc_msgSend(this.id, OS.sel_insertObject_1atArrangedObjectIndexPath_1, object != null ? object.id : 0, indexPath != null ? indexPath.id : 0);
}

public void insertObjects(NSArray objects, NSArray indexPaths) {
	OS.objc_msgSend(this.id, OS.sel_insertObjects_1atArrangedObjectIndexPaths_1, objects != null ? objects.id : 0, indexPaths != null ? indexPaths.id : 0);
}

public NSString leafKeyPath() {
	int result = OS.objc_msgSend(this.id, OS.sel_leafKeyPath);
	return result != 0 ? new NSString(result) : null;
}

public NSString leafKeyPathForNode(NSTreeNode node) {
	int result = OS.objc_msgSend(this.id, OS.sel_leafKeyPathForNode_1, node != null ? node.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public void moveNode(NSTreeNode node, NSIndexPath indexPath) {
	OS.objc_msgSend(this.id, OS.sel_moveNode_1toIndexPath_1, node != null ? node.id : 0, indexPath != null ? indexPath.id : 0);
}

public void moveNodes(NSArray nodes, NSIndexPath startingIndexPath) {
	OS.objc_msgSend(this.id, OS.sel_moveNodes_1toIndexPath_1, nodes != null ? nodes.id : 0, startingIndexPath != null ? startingIndexPath.id : 0);
}

public boolean preservesSelection() {
	return OS.objc_msgSend(this.id, OS.sel_preservesSelection) != 0;
}

public void rearrangeObjects() {
	OS.objc_msgSend(this.id, OS.sel_rearrangeObjects);
}

public void remove(id sender) {
	OS.objc_msgSend(this.id, OS.sel_remove_1, sender != null ? sender.id : 0);
}

public void removeObjectAtArrangedObjectIndexPath(NSIndexPath indexPath) {
	OS.objc_msgSend(this.id, OS.sel_removeObjectAtArrangedObjectIndexPath_1, indexPath != null ? indexPath.id : 0);
}

public void removeObjectsAtArrangedObjectIndexPaths(NSArray indexPaths) {
	OS.objc_msgSend(this.id, OS.sel_removeObjectsAtArrangedObjectIndexPaths_1, indexPaths != null ? indexPaths.id : 0);
}

public boolean removeSelectionIndexPaths(NSArray indexPaths) {
	return OS.objc_msgSend(this.id, OS.sel_removeSelectionIndexPaths_1, indexPaths != null ? indexPaths.id : 0) != 0;
}

public NSArray selectedNodes() {
	int result = OS.objc_msgSend(this.id, OS.sel_selectedNodes);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray selectedObjects() {
	int result = OS.objc_msgSend(this.id, OS.sel_selectedObjects);
	return result != 0 ? new NSArray(result) : null;
}

public NSIndexPath selectionIndexPath() {
	int result = OS.objc_msgSend(this.id, OS.sel_selectionIndexPath);
	return result != 0 ? new NSIndexPath(result) : null;
}

public NSArray selectionIndexPaths() {
	int result = OS.objc_msgSend(this.id, OS.sel_selectionIndexPaths);
	return result != 0 ? new NSArray(result) : null;
}

public boolean selectsInsertedObjects() {
	return OS.objc_msgSend(this.id, OS.sel_selectsInsertedObjects) != 0;
}

public void setAlwaysUsesMultipleValuesMarker(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAlwaysUsesMultipleValuesMarker_1, flag);
}

public void setAvoidsEmptySelection(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAvoidsEmptySelection_1, flag);
}

public void setChildrenKeyPath(NSString keyPath) {
	OS.objc_msgSend(this.id, OS.sel_setChildrenKeyPath_1, keyPath != null ? keyPath.id : 0);
}

public void setContent(id content) {
	OS.objc_msgSend(this.id, OS.sel_setContent_1, content != null ? content.id : 0);
}

public void setCountKeyPath(NSString keyPath) {
	OS.objc_msgSend(this.id, OS.sel_setCountKeyPath_1, keyPath != null ? keyPath.id : 0);
}

public void setLeafKeyPath(NSString keyPath) {
	OS.objc_msgSend(this.id, OS.sel_setLeafKeyPath_1, keyPath != null ? keyPath.id : 0);
}

public void setPreservesSelection(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setPreservesSelection_1, flag);
}

public boolean setSelectionIndexPath(NSIndexPath indexPath) {
	return OS.objc_msgSend(this.id, OS.sel_setSelectionIndexPath_1, indexPath != null ? indexPath.id : 0) != 0;
}

public boolean setSelectionIndexPaths(NSArray indexPaths) {
	return OS.objc_msgSend(this.id, OS.sel_setSelectionIndexPaths_1, indexPaths != null ? indexPaths.id : 0) != 0;
}

public void setSelectsInsertedObjects(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setSelectsInsertedObjects_1, flag);
}

public void setSortDescriptors(NSArray sortDescriptors) {
	OS.objc_msgSend(this.id, OS.sel_setSortDescriptors_1, sortDescriptors != null ? sortDescriptors.id : 0);
}

public NSArray sortDescriptors() {
	int result = OS.objc_msgSend(this.id, OS.sel_sortDescriptors);
	return result != 0 ? new NSArray(result) : null;
}

}

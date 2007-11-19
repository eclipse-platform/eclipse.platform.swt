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

public class NSArrayController extends NSObjectController {

public NSArrayController() {
	super();
}

public NSArrayController(int id) {
	super(id);
}

public void add(id sender) {
	OS.objc_msgSend(this.id, OS.sel_add_1, sender != null ? sender.id : 0);
}

public void addObject(id object) {
	OS.objc_msgSend(this.id, OS.sel_addObject_1, object != null ? object.id : 0);
}

public void addObjects(NSArray objects) {
	OS.objc_msgSend(this.id, OS.sel_addObjects_1, objects != null ? objects.id : 0);
}

public boolean addSelectedObjects(NSArray objects) {
	return OS.objc_msgSend(this.id, OS.sel_addSelectedObjects_1, objects != null ? objects.id : 0) != 0;
}

public boolean addSelectionIndexes(NSIndexSet indexes) {
	return OS.objc_msgSend(this.id, OS.sel_addSelectionIndexes_1, indexes != null ? indexes.id : 0) != 0;
}

public boolean alwaysUsesMultipleValuesMarker() {
	return OS.objc_msgSend(this.id, OS.sel_alwaysUsesMultipleValuesMarker) != 0;
}

public NSArray arrangeObjects(NSArray objects) {
	int result = OS.objc_msgSend(this.id, OS.sel_arrangeObjects_1, objects != null ? objects.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public id arrangedObjects() {
	int result = OS.objc_msgSend(this.id, OS.sel_arrangedObjects);
	return result != 0 ? new id(result) : null;
}

public NSArray automaticRearrangementKeyPaths() {
	int result = OS.objc_msgSend(this.id, OS.sel_automaticRearrangementKeyPaths);
	return result != 0 ? new NSArray(result) : null;
}

public boolean automaticallyRearrangesObjects() {
	return OS.objc_msgSend(this.id, OS.sel_automaticallyRearrangesObjects) != 0;
}

public boolean avoidsEmptySelection() {
	return OS.objc_msgSend(this.id, OS.sel_avoidsEmptySelection) != 0;
}

public boolean canInsert() {
	return OS.objc_msgSend(this.id, OS.sel_canInsert) != 0;
}

public boolean canSelectNext() {
	return OS.objc_msgSend(this.id, OS.sel_canSelectNext) != 0;
}

public boolean canSelectPrevious() {
	return OS.objc_msgSend(this.id, OS.sel_canSelectPrevious) != 0;
}

public boolean clearsFilterPredicateOnInsertion() {
	return OS.objc_msgSend(this.id, OS.sel_clearsFilterPredicateOnInsertion) != 0;
}

public void didChangeArrangementCriteria() {
	OS.objc_msgSend(this.id, OS.sel_didChangeArrangementCriteria);
}

public NSPredicate filterPredicate() {
	int result = OS.objc_msgSend(this.id, OS.sel_filterPredicate);
	return result != 0 ? new NSPredicate(result) : null;
}

public void insert(id sender) {
	OS.objc_msgSend(this.id, OS.sel_insert_1, sender != null ? sender.id : 0);
}

public void insertObject(id object, int index) {
	OS.objc_msgSend(this.id, OS.sel_insertObject_1atArrangedObjectIndex_1, object != null ? object.id : 0, index);
}

public void insertObjects(NSArray objects, NSIndexSet indexes) {
	OS.objc_msgSend(this.id, OS.sel_insertObjects_1atArrangedObjectIndexes_1, objects != null ? objects.id : 0, indexes != null ? indexes.id : 0);
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

public void removeObject(id object) {
	OS.objc_msgSend(this.id, OS.sel_removeObject_1, object != null ? object.id : 0);
}

public void removeObjectAtArrangedObjectIndex(int index) {
	OS.objc_msgSend(this.id, OS.sel_removeObjectAtArrangedObjectIndex_1, index);
}

public void removeObjects(NSArray objects) {
	OS.objc_msgSend(this.id, OS.sel_removeObjects_1, objects != null ? objects.id : 0);
}

public void removeObjectsAtArrangedObjectIndexes(NSIndexSet indexes) {
	OS.objc_msgSend(this.id, OS.sel_removeObjectsAtArrangedObjectIndexes_1, indexes != null ? indexes.id : 0);
}

public boolean removeSelectedObjects(NSArray objects) {
	return OS.objc_msgSend(this.id, OS.sel_removeSelectedObjects_1, objects != null ? objects.id : 0) != 0;
}

public boolean removeSelectionIndexes(NSIndexSet indexes) {
	return OS.objc_msgSend(this.id, OS.sel_removeSelectionIndexes_1, indexes != null ? indexes.id : 0) != 0;
}

public void selectNext(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectNext_1, sender != null ? sender.id : 0);
}

public void selectPrevious(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectPrevious_1, sender != null ? sender.id : 0);
}

public NSArray selectedObjects() {
	int result = OS.objc_msgSend(this.id, OS.sel_selectedObjects);
	return result != 0 ? new NSArray(result) : null;
}

public int selectionIndex() {
	return OS.objc_msgSend(this.id, OS.sel_selectionIndex);
}

public NSIndexSet selectionIndexes() {
	int result = OS.objc_msgSend(this.id, OS.sel_selectionIndexes);
	return result != 0 ? new NSIndexSet(result) : null;
}

public boolean selectsInsertedObjects() {
	return OS.objc_msgSend(this.id, OS.sel_selectsInsertedObjects) != 0;
}

public void setAlwaysUsesMultipleValuesMarker(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAlwaysUsesMultipleValuesMarker_1, flag);
}

public void setAutomaticallyRearrangesObjects(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAutomaticallyRearrangesObjects_1, flag);
}

public void setAvoidsEmptySelection(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAvoidsEmptySelection_1, flag);
}

public void setClearsFilterPredicateOnInsertion(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setClearsFilterPredicateOnInsertion_1, flag);
}

public void setFilterPredicate(NSPredicate filterPredicate) {
	OS.objc_msgSend(this.id, OS.sel_setFilterPredicate_1, filterPredicate != null ? filterPredicate.id : 0);
}

public void setPreservesSelection(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setPreservesSelection_1, flag);
}

public boolean setSelectedObjects(NSArray objects) {
	return OS.objc_msgSend(this.id, OS.sel_setSelectedObjects_1, objects != null ? objects.id : 0) != 0;
}

public boolean setSelectionIndex(int index) {
	return OS.objc_msgSend(this.id, OS.sel_setSelectionIndex_1, index) != 0;
}

public boolean setSelectionIndexes(NSIndexSet indexes) {
	return OS.objc_msgSend(this.id, OS.sel_setSelectionIndexes_1, indexes != null ? indexes.id : 0) != 0;
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

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

public class NSObjectController extends NSController {

public NSObjectController() {
	super();
}

public NSObjectController(int id) {
	super(id);
}

public void add(id sender) {
	OS.objc_msgSend(this.id, OS.sel_add_1, sender != null ? sender.id : 0);
}

public void addObject(id object) {
	OS.objc_msgSend(this.id, OS.sel_addObject_1, object != null ? object.id : 0);
}

public boolean automaticallyPreparesContent() {
	return OS.objc_msgSend(this.id, OS.sel_automaticallyPreparesContent) != 0;
}

public boolean canAdd() {
	return OS.objc_msgSend(this.id, OS.sel_canAdd) != 0;
}

public boolean canRemove() {
	return OS.objc_msgSend(this.id, OS.sel_canRemove) != 0;
}

public id content() {
	int result = OS.objc_msgSend(this.id, OS.sel_content);
	return result != 0 ? new id(result) : null;
}

//public NSFetchRequest defaultFetchRequest() {
//	int result = OS.objc_msgSend(this.id, OS.sel_defaultFetchRequest);
//	return result != 0 ? new NSFetchRequest(result) : null;
//}

public NSString entityName() {
	int result = OS.objc_msgSend(this.id, OS.sel_entityName);
	return result != 0 ? new NSString(result) : null;
}

public void fetch(id sender) {
	OS.objc_msgSend(this.id, OS.sel_fetch_1, sender != null ? sender.id : 0);
}

public NSPredicate fetchPredicate() {
	int result = OS.objc_msgSend(this.id, OS.sel_fetchPredicate);
	return result != 0 ? new NSPredicate(result) : null;
}
//
//public boolean fetchWithRequest(NSFetchRequest fetchRequest, boolean merge, int error) {
//	return OS.objc_msgSend(this.id, OS.sel_fetchWithRequest_1merge_1error_1, fetchRequest != null ? fetchRequest.id : 0, merge, error) != 0;
//}

public NSObjectController initWithContent(id content) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContent_1, content != null ? content.id : 0);
	return result != 0 ? this : null;
}

public boolean isEditable() {
	return OS.objc_msgSend(this.id, OS.sel_isEditable) != 0;
}

//public NSManagedObjectContext managedObjectContext() {
//	int result = OS.objc_msgSend(this.id, OS.sel_managedObjectContext);
//	return result != 0 ? new NSManagedObjectContext(result) : null;
//}

public id newObject() {
	int result = OS.objc_msgSend(this.id, OS.sel_newObject);
	return result != 0 ? new id(result) : null;
}

public int objectClass() {
	return OS.objc_msgSend(this.id, OS.sel_objectClass);
}

public void prepareContent() {
	OS.objc_msgSend(this.id, OS.sel_prepareContent);
}

public void remove(id sender) {
	OS.objc_msgSend(this.id, OS.sel_remove_1, sender != null ? sender.id : 0);
}

public void removeObject(id object) {
	OS.objc_msgSend(this.id, OS.sel_removeObject_1, object != null ? object.id : 0);
}

public NSArray selectedObjects() {
	int result = OS.objc_msgSend(this.id, OS.sel_selectedObjects);
	return result != 0 ? new NSArray(result) : null;
}

public id selection() {
	int result = OS.objc_msgSend(this.id, OS.sel_selection);
	return result != 0 ? new id(result) : null;
}

public void setAutomaticallyPreparesContent(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAutomaticallyPreparesContent_1, flag);
}

public void setContent(id content) {
	OS.objc_msgSend(this.id, OS.sel_setContent_1, content != null ? content.id : 0);
}

public void setEditable(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setEditable_1, flag);
}

public void setEntityName(NSString entityName) {
	OS.objc_msgSend(this.id, OS.sel_setEntityName_1, entityName != null ? entityName.id : 0);
}

public void setFetchPredicate(NSPredicate predicate) {
	OS.objc_msgSend(this.id, OS.sel_setFetchPredicate_1, predicate != null ? predicate.id : 0);
}

//public void setManagedObjectContext(NSManagedObjectContext managedObjectContext) {
//	OS.objc_msgSend(this.id, OS.sel_setManagedObjectContext_1, managedObjectContext != null ? managedObjectContext.id : 0);
//}

public void setObjectClass(int objectClass) {
	OS.objc_msgSend(this.id, OS.sel_setObjectClass_1, objectClass);
}

public void setUsesLazyFetching(boolean enabled) {
	OS.objc_msgSend(this.id, OS.sel_setUsesLazyFetching_1, enabled);
}

public boolean usesLazyFetching() {
	return OS.objc_msgSend(this.id, OS.sel_usesLazyFetching) != 0;
}

public boolean validateUserInterfaceItem(id  item) {
	return OS.objc_msgSend(this.id, OS.sel_validateUserInterfaceItem_1, item != null ? item.id : 0) != 0;
}

}

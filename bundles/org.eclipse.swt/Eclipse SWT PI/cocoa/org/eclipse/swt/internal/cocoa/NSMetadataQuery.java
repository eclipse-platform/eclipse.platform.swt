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

public class NSMetadataQuery extends NSObject {

public NSMetadataQuery() {
	super();
}

public NSMetadataQuery(int id) {
	super(id);
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public void disableUpdates() {
	OS.objc_msgSend(this.id, OS.sel_disableUpdates);
}

public void enableUpdates() {
	OS.objc_msgSend(this.id, OS.sel_enableUpdates);
}

public NSArray groupedResults() {
	int result = OS.objc_msgSend(this.id, OS.sel_groupedResults);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray groupingAttributes() {
	int result = OS.objc_msgSend(this.id, OS.sel_groupingAttributes);
	return result != 0 ? new NSArray(result) : null;
}

public int indexOfResult(id result) {
	return OS.objc_msgSend(this.id, OS.sel_indexOfResult_1, result != null ? result.id : 0);
}

public boolean isGathering() {
	return OS.objc_msgSend(this.id, OS.sel_isGathering) != 0;
}

public boolean isStarted() {
	return OS.objc_msgSend(this.id, OS.sel_isStarted) != 0;
}

public boolean isStopped() {
	return OS.objc_msgSend(this.id, OS.sel_isStopped) != 0;
}

public double notificationBatchingInterval() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_notificationBatchingInterval);
}

public NSPredicate predicate() {
	int result = OS.objc_msgSend(this.id, OS.sel_predicate);
	return result != 0 ? new NSPredicate(result) : null;
}

public id resultAtIndex(int idx) {
	int result = OS.objc_msgSend(this.id, OS.sel_resultAtIndex_1, idx);
	return result != 0 ? new id(result) : null;
}

public int resultCount() {
	return OS.objc_msgSend(this.id, OS.sel_resultCount);
}

public NSArray results() {
	int result = OS.objc_msgSend(this.id, OS.sel_results);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray searchScopes() {
	int result = OS.objc_msgSend(this.id, OS.sel_searchScopes);
	return result != 0 ? new NSArray(result) : null;
}

public void setDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, delegate != null ? delegate.id : 0);
}

public void setGroupingAttributes(NSArray attrs) {
	OS.objc_msgSend(this.id, OS.sel_setGroupingAttributes_1, attrs != null ? attrs.id : 0);
}

public void setNotificationBatchingInterval(double ti) {
	OS.objc_msgSend(this.id, OS.sel_setNotificationBatchingInterval_1, ti);
}

public void setPredicate(NSPredicate predicate) {
	OS.objc_msgSend(this.id, OS.sel_setPredicate_1, predicate != null ? predicate.id : 0);
}

public void setSearchScopes(NSArray scopes) {
	OS.objc_msgSend(this.id, OS.sel_setSearchScopes_1, scopes != null ? scopes.id : 0);
}

public void setSortDescriptors(NSArray descriptors) {
	OS.objc_msgSend(this.id, OS.sel_setSortDescriptors_1, descriptors != null ? descriptors.id : 0);
}

public void setValueListAttributes(NSArray attrs) {
	OS.objc_msgSend(this.id, OS.sel_setValueListAttributes_1, attrs != null ? attrs.id : 0);
}

public NSArray sortDescriptors() {
	int result = OS.objc_msgSend(this.id, OS.sel_sortDescriptors);
	return result != 0 ? new NSArray(result) : null;
}

public boolean startQuery() {
	return OS.objc_msgSend(this.id, OS.sel_startQuery) != 0;
}

public void stopQuery() {
	OS.objc_msgSend(this.id, OS.sel_stopQuery);
}

public NSArray valueListAttributes() {
	int result = OS.objc_msgSend(this.id, OS.sel_valueListAttributes);
	return result != 0 ? new NSArray(result) : null;
}

public NSDictionary valueLists() {
	int result = OS.objc_msgSend(this.id, OS.sel_valueLists);
	return result != 0 ? new NSDictionary(result) : null;
}

public id valueOfAttribute(NSString attrName, int idx) {
	int result = OS.objc_msgSend(this.id, OS.sel_valueOfAttribute_1forResultAtIndex_1, attrName != null ? attrName.id : 0, idx);
	return result != 0 ? new id(result) : null;
}

}

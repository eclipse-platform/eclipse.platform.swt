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

public class NSPersistentDocument extends NSDocument {

public NSPersistentDocument() {
	super();
}

public NSPersistentDocument(int id) {
	super(id);
}

public boolean configurePersistentStoreCoordinatorForURL_ofType_error_(NSURL url, NSString fileType, int error) {
	return OS.objc_msgSend(this.id, OS.sel_configurePersistentStoreCoordinatorForURL_1ofType_1error_1, url != null ? url.id : 0, fileType != null ? fileType.id : 0, error) != 0;
}

public boolean configurePersistentStoreCoordinatorForURL_ofType_modelConfiguration_storeOptions_error_(NSURL url, NSString fileType, NSString configuration, NSDictionary storeOptions, int error) {
	return OS.objc_msgSend(this.id, OS.sel_configurePersistentStoreCoordinatorForURL_1ofType_1modelConfiguration_1storeOptions_1error_1, url != null ? url.id : 0, fileType != null ? fileType.id : 0, configuration != null ? configuration.id : 0, storeOptions != null ? storeOptions.id : 0, error) != 0;
}

//public NSManagedObjectContext managedObjectContext() {
//	int result = OS.objc_msgSend(this.id, OS.sel_managedObjectContext);
//	return result != 0 ? new NSManagedObjectContext(result) : null;
//}

public id managedObjectModel() {
	int result = OS.objc_msgSend(this.id, OS.sel_managedObjectModel);
	return result != 0 ? new id(result) : null;
}

public NSString persistentStoreTypeForFileType(NSString fileType) {
	int result = OS.objc_msgSend(this.id, OS.sel_persistentStoreTypeForFileType_1, fileType != null ? fileType.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public boolean readFromURL(NSURL absoluteURL, NSString typeName, int error) {
	return OS.objc_msgSend(this.id, OS.sel_readFromURL_1ofType_1error_1, absoluteURL != null ? absoluteURL.id : 0, typeName != null ? typeName.id : 0, error) != 0;
}

public boolean revertToContentsOfURL(NSURL inAbsoluteURL, NSString inTypeName, int outError) {
	return OS.objc_msgSend(this.id, OS.sel_revertToContentsOfURL_1ofType_1error_1, inAbsoluteURL != null ? inAbsoluteURL.id : 0, inTypeName != null ? inTypeName.id : 0, outError) != 0;
}

//public void setManagedObjectContext(NSManagedObjectContext managedObjectContext) {
//	OS.objc_msgSend(this.id, OS.sel_setManagedObjectContext_1, managedObjectContext != null ? managedObjectContext.id : 0);
//}

public boolean writeToURL(NSURL absoluteURL, NSString typeName, int saveOperation, NSURL absoluteOriginalContentsURL, int error) {
	return OS.objc_msgSend(this.id, OS.sel_writeToURL_1ofType_1forSaveOperation_1originalContentsURL_1error_1, absoluteURL != null ? absoluteURL.id : 0, typeName != null ? typeName.id : 0, saveOperation, absoluteOriginalContentsURL != null ? absoluteOriginalContentsURL.id : 0, error) != 0;
}

}

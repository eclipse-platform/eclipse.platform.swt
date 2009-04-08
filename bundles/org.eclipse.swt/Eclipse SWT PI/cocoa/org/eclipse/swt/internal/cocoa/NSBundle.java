/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSBundle extends NSObject {

public NSBundle() {
	super();
}

public NSBundle(int /*long*/ id) {
	super(id);
}

public NSBundle(id id) {
	super(id);
}

public static boolean loadNibFile(NSString fileName, NSDictionary context, int /*long*/ zone) {
	return OS.objc_msgSend_bool(OS.class_NSBundle, OS.sel_loadNibFile_externalNameTable_withZone_, fileName != null ? fileName.id : 0, context != null ? context.id : 0, zone);
}

public NSString bundleIdentifier() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_bundleIdentifier);
	return result != 0 ? new NSString(result) : null;
}

public NSString bundlePath() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_bundlePath);
	return result != 0 ? new NSString(result) : null;
}

public static NSBundle bundleWithIdentifier(NSString identifier) {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSBundle, OS.sel_bundleWithIdentifier_, identifier != null ? identifier.id : 0);
	return result != 0 ? new NSBundle(result) : null;
}

public static NSBundle bundleWithPath(NSString path) {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSBundle, OS.sel_bundleWithPath_, path != null ? path.id : 0);
	return result != 0 ? new NSBundle(result) : null;
}

public NSDictionary infoDictionary() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_infoDictionary);
	return result != 0 ? new NSDictionary(result) : null;
}

public static NSBundle mainBundle() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSBundle, OS.sel_mainBundle);
	return result != 0 ? new NSBundle(result) : null;
}

public id objectForInfoDictionaryKey(NSString key) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_objectForInfoDictionaryKey_, key != null ? key.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSString pathForResource(NSString name, NSString ext) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_pathForResource_ofType_, name != null ? name.id : 0, ext != null ? ext.id : 0);
	return result != 0 ? new NSString(result) : null;
}

}

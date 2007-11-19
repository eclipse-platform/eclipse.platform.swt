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

public class NSUserDefaultsController extends NSController {

public NSUserDefaultsController() {
	super();
}

public NSUserDefaultsController(int id) {
	super(id);
}

public boolean appliesImmediately() {
	return OS.objc_msgSend(this.id, OS.sel_appliesImmediately) != 0;
}

public NSUserDefaults defaults() {
	int result = OS.objc_msgSend(this.id, OS.sel_defaults);
	return result != 0 ? new NSUserDefaults(result) : null;
}

public boolean hasUnappliedChanges() {
	return OS.objc_msgSend(this.id, OS.sel_hasUnappliedChanges) != 0;
}

public id initWithDefaults(NSUserDefaults defaults, NSDictionary initialValues) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithDefaults_1initialValues_1, defaults != null ? defaults.id : 0, initialValues != null ? initialValues.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSDictionary initialValues() {
	int result = OS.objc_msgSend(this.id, OS.sel_initialValues);
	return result != 0 ? new NSDictionary(result) : null;
}

public void revert(id sender) {
	OS.objc_msgSend(this.id, OS.sel_revert_1, sender != null ? sender.id : 0);
}

public void revertToInitialValues(id sender) {
	OS.objc_msgSend(this.id, OS.sel_revertToInitialValues_1, sender != null ? sender.id : 0);
}

public void save(id sender) {
	OS.objc_msgSend(this.id, OS.sel_save_1, sender != null ? sender.id : 0);
}

public void setAppliesImmediately(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAppliesImmediately_1, flag);
}

public void setInitialValues(NSDictionary initialValues) {
	OS.objc_msgSend(this.id, OS.sel_setInitialValues_1, initialValues != null ? initialValues.id : 0);
}

public static id sharedUserDefaultsController() {
	int result = OS.objc_msgSend(OS.class_NSUserDefaultsController, OS.sel_sharedUserDefaultsController);
	return result != 0 ? new id(result) : null;
}

public id values() {
	int result = OS.objc_msgSend(this.id, OS.sel_values);
	return result != 0 ? new id(result) : null;
}

}

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

public class NSViewController extends NSResponder {

public NSViewController() {
	super();
}

public NSViewController(int id) {
	super(id);
}

public boolean commitEditing() {
	return OS.objc_msgSend(this.id, OS.sel_commitEditing) != 0;
}

public void commitEditingWithDelegate(id delegate, int didCommitSelector, int contextInfo) {
	OS.objc_msgSend(this.id, OS.sel_commitEditingWithDelegate_1didCommitSelector_1contextInfo_1, delegate != null ? delegate.id : 0, didCommitSelector, contextInfo);
}

public void discardEditing() {
	OS.objc_msgSend(this.id, OS.sel_discardEditing);
}

public id initWithNibName(NSString nibNameOrNil, NSBundle nibBundleOrNil) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithNibName_1bundle_1, nibNameOrNil != null ? nibNameOrNil.id : 0, nibBundleOrNil != null ? nibBundleOrNil.id : 0);
	return result != 0 ? new id(result) : null;
}

public void loadView() {
	OS.objc_msgSend(this.id, OS.sel_loadView);
}

public NSBundle nibBundle() {
	int result = OS.objc_msgSend(this.id, OS.sel_nibBundle);
	return result != 0 ? new NSBundle(result) : null;
}

public NSString nibName() {
	int result = OS.objc_msgSend(this.id, OS.sel_nibName);
	return result != 0 ? new NSString(result) : null;
}

public id representedObject() {
	int result = OS.objc_msgSend(this.id, OS.sel_representedObject);
	return result != 0 ? new id(result) : null;
}

public void setRepresentedObject(id representedObject) {
	OS.objc_msgSend(this.id, OS.sel_setRepresentedObject_1, representedObject != null ? representedObject.id : 0);
}

public void setTitle(NSString title) {
	OS.objc_msgSend(this.id, OS.sel_setTitle_1, title != null ? title.id : 0);
}

public void setView(NSView view) {
	OS.objc_msgSend(this.id, OS.sel_setView_1, view != null ? view.id : 0);
}

public NSString title() {
	int result = OS.objc_msgSend(this.id, OS.sel_title);
	return result != 0 ? new NSString(result) : null;
}

public NSView view() {
	int result = OS.objc_msgSend(this.id, OS.sel_view);
	return result != 0 ? new NSView(result) : null;
}

}

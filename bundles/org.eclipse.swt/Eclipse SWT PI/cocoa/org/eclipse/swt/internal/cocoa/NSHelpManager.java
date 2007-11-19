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

public class NSHelpManager extends NSObject {

public NSHelpManager() {
	super();
}

public NSHelpManager(int id) {
	super(id);
}

public NSAttributedString contextHelpForObject(id object) {
	int result = OS.objc_msgSend(this.id, OS.sel_contextHelpForObject_1, object != null ? object.id : 0);
	return result != 0 ? new NSAttributedString(result) : null;
}

public void findString(NSString query, NSString book) {
	OS.objc_msgSend(this.id, OS.sel_findString_1inBook_1, query != null ? query.id : 0, book != null ? book.id : 0);
}

public static boolean isContextHelpModeActive() {
	return OS.objc_msgSend(OS.class_NSHelpManager, OS.sel_isContextHelpModeActive) != 0;
}

public void openHelpAnchor(NSString anchor, NSString book) {
	OS.objc_msgSend(this.id, OS.sel_openHelpAnchor_1inBook_1, anchor != null ? anchor.id : 0, book != null ? book.id : 0);
}

public void removeContextHelpForObject(id object) {
	OS.objc_msgSend(this.id, OS.sel_removeContextHelpForObject_1, object != null ? object.id : 0);
}

public void setContextHelp(NSAttributedString attrString, id object) {
	OS.objc_msgSend(this.id, OS.sel_setContextHelp_1forObject_1, attrString != null ? attrString.id : 0, object != null ? object.id : 0);
}

public static void setContextHelpModeActive(boolean active) {
	OS.objc_msgSend(OS.class_NSHelpManager, OS.sel_setContextHelpModeActive_1, active);
}

public static NSHelpManager sharedHelpManager() {
	int result = OS.objc_msgSend(OS.class_NSHelpManager, OS.sel_sharedHelpManager);
	return result != 0 ? new NSHelpManager(result) : null;
}

public boolean showContextHelpForObject(id object, NSPoint pt) {
	return OS.objc_msgSend(this.id, OS.sel_showContextHelpForObject_1locationHint_1, object != null ? object.id : 0, pt) != 0;
}

}

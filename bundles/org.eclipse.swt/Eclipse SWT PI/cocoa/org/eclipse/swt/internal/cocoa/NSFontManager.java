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

public class NSFontManager extends NSObject {

public NSFontManager() {
	super();
}

public NSFontManager(int /*long*/ id) {
	super(id);
}

public NSFontManager(id id) {
	super(id);
}

public NSArray availableFonts() {
	int result = OS.objc_msgSend(this.id, OS.sel_availableFonts);
	return result != 0 ? new NSArray(result) : null;
}

public static NSFontManager sharedFontManager() {
	int result = OS.objc_msgSend(OS.class_NSFontManager, OS.sel_sharedFontManager);
	return result != 0 ? new NSFontManager(result) : null;
}

}

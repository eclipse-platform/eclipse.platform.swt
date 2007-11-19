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

public class NSMovie extends NSObject {

public NSMovie() {
	super();
}

public NSMovie(int id) {
	super(id);
}

public int QTMovie() {
	return OS.objc_msgSend(this.id, OS.sel_QTMovie);
}

public NSURL URL() {
	int result = OS.objc_msgSend(this.id, OS.sel_URL);
	return result != 0 ? new NSURL(result) : null;
}

public static boolean canInitWithPasteboard(NSPasteboard pasteboard) {
	return OS.objc_msgSend(OS.class_NSMovie, OS.sel_canInitWithPasteboard_1, pasteboard != null ? pasteboard.id : 0) != 0;
}

public NSMovie initWithMovie(int movie) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithMovie_1, movie);
	return result != 0 ? this : null;
}

public NSMovie initWithPasteboard(NSPasteboard pasteboard) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithPasteboard_1, pasteboard != null ? pasteboard.id : 0);
	return result != 0 ? this : null;
}

public NSMovie initWithURL(NSURL url, boolean byRef) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithURL_1byReference_1, url != null ? url.id : 0, byRef);
	return result != 0 ? this : null;
}

public static NSArray movieUnfilteredFileTypes() {
	int result = OS.objc_msgSend(OS.class_NSMovie, OS.sel_movieUnfilteredFileTypes);
	return result != 0 ? new NSArray(result) : null;
}

public static NSArray movieUnfilteredPasteboardTypes() {
	int result = OS.objc_msgSend(OS.class_NSMovie, OS.sel_movieUnfilteredPasteboardTypes);
	return result != 0 ? new NSArray(result) : null;
}

}

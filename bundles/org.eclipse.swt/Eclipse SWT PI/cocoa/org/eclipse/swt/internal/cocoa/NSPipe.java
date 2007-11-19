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

public class NSPipe extends NSObject {

public NSPipe() {
	super();
}

public NSPipe(int id) {
	super(id);
}

public NSFileHandle fileHandleForReading() {
	int result = OS.objc_msgSend(this.id, OS.sel_fileHandleForReading);
	return result != 0 ? new NSFileHandle(result) : null;
}

public NSFileHandle fileHandleForWriting() {
	int result = OS.objc_msgSend(this.id, OS.sel_fileHandleForWriting);
	return result != 0 ? new NSFileHandle(result) : null;
}

public static id pipe() {
	int result = OS.objc_msgSend(OS.class_NSPipe, OS.sel_pipe);
	return result != 0 ? new id(result) : null;
}

}

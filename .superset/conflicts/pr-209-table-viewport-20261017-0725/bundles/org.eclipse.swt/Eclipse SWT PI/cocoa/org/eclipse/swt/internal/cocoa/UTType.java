/*******************************************************************************
 * Copyright (c) 2026 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class UTType extends NSObject {

public UTType() {
	super();
}

public UTType(long id) {
	super(id);
}

public UTType(id id) {
	super(id);
}

public static UTType typeWithFilenameExtension(NSString filenameExtension) {
	long result = OS.objc_msgSend(OS.class_UTType, OS.sel_typeWithFilenameExtension_, filenameExtension != null ? filenameExtension.id : 0);
	return result != 0 ? new UTType(result) : null;
}

}

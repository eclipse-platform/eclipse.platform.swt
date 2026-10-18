/*******************************************************************************
 * Copyright (c) 2007, 2012 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class WebDocumentRepresentation extends NSObject {

public WebDocumentRepresentation() {
	super();
}

public WebDocumentRepresentation(long id) {
	super(id);
}

public WebDocumentRepresentation(id id) {
	super(id);
}

public NSString documentSource() {
	long result = OS.objc_msgSend(this.id, OS.sel_documentSource);
	return result != 0 ? new NSString(result) : null;
}
}

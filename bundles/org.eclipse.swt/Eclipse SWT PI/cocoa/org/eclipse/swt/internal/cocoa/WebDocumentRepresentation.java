/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class WebDocumentRepresentation extends NSObject {

public WebDocumentRepresentation() {
	super();
}

public WebDocumentRepresentation(long /*int*/ id) {
	super(id);
}

public WebDocumentRepresentation(id id) {
	super(id);
}

public NSString documentSource() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_documentSource);
	return result != 0 ? new NSString(result) : null;
}
}

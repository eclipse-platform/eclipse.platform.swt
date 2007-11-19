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

public class NSATSTypesetter extends NSTypesetter {

public NSATSTypesetter() {
	super();
}

public NSATSTypesetter(int id) {
	super(id);
}

public NSRect lineFragmentRectForProposedRect(NSRect proposedRect, int remainingRect) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_lineFragmentRectForProposedRect_1remainingRect_1, proposedRect, remainingRect);
	return result;
}

public static id sharedTypesetter() {
	int result = OS.objc_msgSend(OS.class_NSATSTypesetter, OS.sel_sharedTypesetter);
	return result != 0 ? new id(result) : null;
}

}

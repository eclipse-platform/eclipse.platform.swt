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

public class NSImageRep extends NSObject {

public NSImageRep() {
	super();
}

public NSImageRep(int /*long*/ id) {
	super(id);
}

public NSImageRep(id id) {
	super(id);
}

public int /*long*/ bitsPerSample() {
	return OS.objc_msgSend(this.id, OS.sel_bitsPerSample);
}

public boolean hasAlpha() {
	return OS.objc_msgSend(this.id, OS.sel_hasAlpha) != 0;
}

public int /*long*/ pixelsHigh() {
	return OS.objc_msgSend(this.id, OS.sel_pixelsHigh);
}

public int /*long*/ pixelsWide() {
	return OS.objc_msgSend(this.id, OS.sel_pixelsWide);
}

}

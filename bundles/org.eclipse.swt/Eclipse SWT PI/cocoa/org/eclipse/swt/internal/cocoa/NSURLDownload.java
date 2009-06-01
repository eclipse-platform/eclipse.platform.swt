/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSURLDownload extends NSObject {

public NSURLDownload() {
	super();
}

public NSURLDownload(int /*long*/ id) {
	super(id);
}

public NSURLDownload(id id) {
	super(id);
}

public void cancel() {
	OS.objc_msgSend(this.id, OS.sel_cancel);
}

public void setDestination(NSString path, boolean allowOverwrite) {
	OS.objc_msgSend(this.id, OS.sel_setDestination_allowOverwrite_, path != null ? path.id : 0, allowOverwrite);
}

}

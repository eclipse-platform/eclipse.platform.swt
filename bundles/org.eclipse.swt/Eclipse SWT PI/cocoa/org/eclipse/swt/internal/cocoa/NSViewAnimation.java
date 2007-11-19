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

public class NSViewAnimation extends NSAnimation {

public NSViewAnimation() {
	super();
}

public NSViewAnimation(int id) {
	super(id);
}

public id initWithViewAnimations(NSArray viewAnimations) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithViewAnimations_1, viewAnimations != null ? viewAnimations.id : 0);
	return result != 0 ? new id(result) : null;
}

public void setViewAnimations(NSArray viewAnimations) {
	OS.objc_msgSend(this.id, OS.sel_setViewAnimations_1, viewAnimations != null ? viewAnimations.id : 0);
}

public NSArray viewAnimations() {
	int result = OS.objc_msgSend(this.id, OS.sel_viewAnimations);
	return result != 0 ? new NSArray(result) : null;
}

}

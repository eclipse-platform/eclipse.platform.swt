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

public class NSTimer extends NSObject {

public NSTimer() {
	super();
}

public NSTimer(int /*long*/ id) {
	super(id);
}

public NSTimer(id id) {
	super(id);
}

public void invalidate() {
	OS.objc_msgSend(this.id, OS.sel_invalidate);
}

public static NSTimer scheduledTimerWithTimeInterval(double ti, id aTarget, int /*long*/ aSelector, id userInfo, boolean yesOrNo) {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSTimer, OS.sel_scheduledTimerWithTimeInterval_target_selector_userInfo_repeats_, ti, aTarget != null ? aTarget.id : 0, aSelector, userInfo != null ? userInfo.id : 0, yesOrNo);
	return result != 0 ? new NSTimer(result) : null;
}

public void setFireDate(NSDate date) {
	OS.objc_msgSend(this.id, OS.sel_setFireDate_, date != null ? date.id : 0);
}

public id userInfo() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_userInfo);
	return result != 0 ? new id(result) : null;
}

}

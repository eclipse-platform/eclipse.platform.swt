/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSTimer extends NSObject {

public NSTimer() {
	super();
}

public NSTimer(long id) {
	super(id);
}

public NSTimer(id id) {
	super(id);
}

public void invalidate() {
	OS.objc_msgSend(this.id, OS.sel_invalidate);
}

public static NSTimer scheduledTimerWithTimeInterval(double ti, id aTarget, long aSelector, id userInfo, boolean yesOrNo) {
	long result = OS.objc_msgSend(OS.class_NSTimer, OS.sel_scheduledTimerWithTimeInterval_target_selector_userInfo_repeats_, ti, aTarget != null ? aTarget.id : 0, aSelector, userInfo != null ? userInfo.id : 0, yesOrNo);
	return result != 0 ? new NSTimer(result) : null;
}

public void setFireDate(NSDate fireDate) {
	OS.objc_msgSend(this.id, OS.sel_setFireDate_, fireDate != null ? fireDate.id : 0);
}

public id userInfo() {
	long result = OS.objc_msgSend(this.id, OS.sel_userInfo);
	return result != 0 ? new id(result) : null;
}

}

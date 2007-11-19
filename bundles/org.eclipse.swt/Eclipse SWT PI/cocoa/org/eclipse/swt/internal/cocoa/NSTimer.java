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

public class NSTimer extends NSObject {

public NSTimer() {
	super();
}

public NSTimer(int id) {
	super(id);
}

public void fire() {
	OS.objc_msgSend(this.id, OS.sel_fire);
}

public NSDate fireDate() {
	int result = OS.objc_msgSend(this.id, OS.sel_fireDate);
	return result != 0 ? new NSDate(result) : null;
}

public id initWithFireDate(NSDate date, double ti, id t, int s, id ui, boolean rep) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithFireDate_1interval_1target_1selector_1userInfo_1repeats_1, date != null ? date.id : 0, ti, t != null ? t.id : 0, s, ui != null ? ui.id : 0, rep);
	return result != 0 ? new id(result) : null;
}

public void invalidate() {
	OS.objc_msgSend(this.id, OS.sel_invalidate);
}

public boolean isValid() {
	return OS.objc_msgSend(this.id, OS.sel_isValid) != 0;
}

public static NSTimer static_scheduledTimerWithTimeInterval_invocation_repeats_(double ti, NSInvocation invocation, boolean yesOrNo) {
	int result = OS.objc_msgSend(OS.class_NSTimer, OS.sel_scheduledTimerWithTimeInterval_1invocation_1repeats_1, ti, invocation != null ? invocation.id : 0, yesOrNo);
	return result != 0 ? new NSTimer(result) : null;
}

public static NSTimer static_scheduledTimerWithTimeInterval_target_selector_userInfo_repeats_(double ti, id aTarget, int aSelector, int userInfo, boolean yesOrNo) {
	int result = OS.objc_msgSend(OS.class_NSTimer, OS.sel_scheduledTimerWithTimeInterval_1target_1selector_1userInfo_1repeats_1, ti, aTarget != null ? aTarget.id : 0, aSelector, userInfo, yesOrNo);
	return result != 0 ? new NSTimer(result) : null;
}

public void setFireDate(NSDate date) {
	OS.objc_msgSend(this.id, OS.sel_setFireDate_1, date != null ? date.id : 0);
}

public double timeInterval() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_timeInterval);
}

public static NSTimer static_timerWithTimeInterval_invocation_repeats_(double ti, NSInvocation invocation, boolean yesOrNo) {
	int result = OS.objc_msgSend(OS.class_NSTimer, OS.sel_timerWithTimeInterval_1invocation_1repeats_1, ti, invocation != null ? invocation.id : 0, yesOrNo);
	return result != 0 ? new NSTimer(result) : null;
}

public static NSTimer static_timerWithTimeInterval_target_selector_userInfo_repeats_(double ti, id aTarget, int aSelector, id userInfo, boolean yesOrNo) {
	int result = OS.objc_msgSend(OS.class_NSTimer, OS.sel_timerWithTimeInterval_1target_1selector_1userInfo_1repeats_1, ti, aTarget != null ? aTarget.id : 0, aSelector, userInfo != null ? userInfo.id : 0, yesOrNo);
	return result != 0 ? new NSTimer(result) : null;
}

public int userInfo() {
	return OS.objc_msgSend(this.id, OS.sel_userInfo);
}

}

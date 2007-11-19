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

public class NSNotificationQueue extends NSObject {

public NSNotificationQueue() {
	super();
}

public NSNotificationQueue(int id) {
	super(id);
}

public static id defaultQueue() {
	int result = OS.objc_msgSend(OS.class_NSNotificationQueue, OS.sel_defaultQueue);
	return result != 0 ? new id(result) : null;
}

public void dequeueNotificationsMatching(NSNotification notification, int coalesceMask) {
	OS.objc_msgSend(this.id, OS.sel_dequeueNotificationsMatching_1coalesceMask_1, notification != null ? notification.id : 0, coalesceMask);
}

public void enqueueNotification_postingStyle_(NSNotification notification, int postingStyle) {
	OS.objc_msgSend(this.id, OS.sel_enqueueNotification_1postingStyle_1, notification != null ? notification.id : 0, postingStyle);
}

public void enqueueNotification_postingStyle_coalesceMask_forModes_(NSNotification notification, int postingStyle, int coalesceMask, NSArray modes) {
	OS.objc_msgSend(this.id, OS.sel_enqueueNotification_1postingStyle_1coalesceMask_1forModes_1, notification != null ? notification.id : 0, postingStyle, coalesceMask, modes != null ? modes.id : 0);
}

public id initWithNotificationCenter(NSNotificationCenter notificationCenter) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithNotificationCenter_1, notificationCenter != null ? notificationCenter.id : 0);
	return result != 0 ? new id(result) : null;
}

}

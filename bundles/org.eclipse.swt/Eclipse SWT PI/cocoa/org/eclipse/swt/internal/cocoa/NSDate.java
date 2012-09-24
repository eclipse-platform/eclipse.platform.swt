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

public class NSDate extends NSObject {

public NSDate() {
	super();
}

public NSDate(long /*int*/ id) {
	super(id);
}

public NSDate(id id) {
	super(id);
}

public NSCalendarDate dateWithCalendarFormat(NSString format, NSTimeZone aTimeZone) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_dateWithCalendarFormat_timeZone_, format != null ? format.id : 0, aTimeZone != null ? aTimeZone.id : 0);
	return result != 0 ? new NSCalendarDate(result) : null;
}

public static NSDate dateWithTimeIntervalSinceNow(double secs) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSDate, OS.sel_dateWithTimeIntervalSinceNow_, secs);
	return result != 0 ? new NSDate(result) : null;
}

public static NSDate distantFuture() {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSDate, OS.sel_distantFuture);
	return result != 0 ? new NSDate(result) : null;
}

}

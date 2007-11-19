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

public class NSAnimationContext extends NSObject {

public NSAnimationContext() {
	super();
}

public NSAnimationContext(int id) {
	super(id);
}

public static void beginGrouping() {
	OS.objc_msgSend(OS.class_NSAnimationContext, OS.sel_beginGrouping);
}

public static NSAnimationContext currentContext() {
	int result = OS.objc_msgSend(OS.class_NSAnimationContext, OS.sel_currentContext);
	return result != 0 ? new NSAnimationContext(result) : null;
}

public double duration() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_duration);
}

public static void endGrouping() {
	OS.objc_msgSend(OS.class_NSAnimationContext, OS.sel_endGrouping);
}

public void setDuration(double duration) {
	OS.objc_msgSend(this.id, OS.sel_setDuration_1, duration);
}

}

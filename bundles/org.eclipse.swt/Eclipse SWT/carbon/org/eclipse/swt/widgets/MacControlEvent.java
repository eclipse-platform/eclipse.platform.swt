/*
 * Copyright (c) 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
package org.eclipse.swt.widgets;

class MacControlEvent {

	private int fControlhandle;
	private int fPartCode;
	private boolean fMouseDown;
	private int fDamageRegion;
	private int fGCContext;

	public MacControlEvent(int handle, int partCode, boolean mouseDown) {
		fControlhandle= handle;
		fPartCode= partCode;
		fMouseDown= mouseDown;
	}
	
	public MacControlEvent(int handle, int damageRegion, int gccontext) {
		fControlhandle= handle;
		fDamageRegion= damageRegion;
		fGCContext= gccontext;
	}
	
	public int getControlHandle() {
		return fControlhandle;
	}
	
	public int getPartCode() {
		return fPartCode;
	}
	
	public int getDamageRegionHandle() {
		return fDamageRegion;
	}
	
	public int getGCContext() {
		return fGCContext;
	}
	
	public boolean isMouseDown() {
		return fMouseDown;
	}
}

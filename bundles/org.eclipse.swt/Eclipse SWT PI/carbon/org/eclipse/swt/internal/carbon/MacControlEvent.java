/*
 * (c) Copyright IBM Corp. 2002.
 * All Rights Reserved
 *
 * Andre Weinand, OTI - Initial version
 */
package org.eclipse.swt.internal.carbon;

public class MacControlEvent {

	private int fControlhandle;
	private int fPartCode;
	private boolean fMouseDown;
	private int fDamageRegion;

	public MacControlEvent(int handle, int partCode, boolean mouseDown) {
		fControlhandle= handle;
		fPartCode= partCode;
		fMouseDown= mouseDown;
	}
	
	public MacControlEvent(int handle, int damageRegion) {
		fControlhandle= handle;
		fDamageRegion= damageRegion;
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
	
	public boolean isMouseDown() {
		return fMouseDown;
	}
}

package org.eclipse.swt.accessibility;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

/**
 * This adapter class provides default implementations for the
 * methods described by the <code>AccessibleControlListener</code> interface.
 * <p>
 * Classes that wish to deal with <code>AccessibleControlEvent</code>s can
 * extend this class and override only the methods that they are
 * interested in.
 * </p>
 *
 * @see AccessibleControlListener
 * @see AccessibleControlEvent
 */
public abstract class AccessibleControlAdapter implements AccessibleControlListener {

	/**
	 * Sent when an accessibility client requests the identifier
	 * of the control child at the specified display coordinates.
	 * The default behavior is to do nothing.
	 *
	 * @param e an event object containing the following fields:
	 * x, y [IN] - the specified point in display coordinates
	 * childID [OUT] - the ID of the child at point, or CHILDID_SELF, or CHILDID_NONE
	 */
	public void accHitTest(AccessibleControlEvent e) {
	}
	
	public void accLocation(AccessibleControlEvent e) {
	}
	
	public void accNavigate(AccessibleControlEvent e) {
	}
	
	public void get_accChild(AccessibleControlEvent e) {
	}
	
	public void get_accChildCount(AccessibleControlEvent e) {
	}
	
	public void get_accDefaultAction(AccessibleControlEvent e) {
	}
	
	public void get_accFocus(AccessibleControlEvent e) {
	}
	
	public void get_accRole(AccessibleControlEvent e) {
	}
	
	public void get_accSelection(AccessibleControlEvent e) {
	}
	
	public void get_accState(AccessibleControlEvent e) {
	}
	
	public void get_accValue(AccessibleControlEvent e) {
	}
	
	// May not implement
	public void accDoDefaultAction(AccessibleControlEvent e) {
	}
	public void accSelect(AccessibleControlEvent e) {
	}
	public void get_accParent(AccessibleControlEvent e) {
	}
}

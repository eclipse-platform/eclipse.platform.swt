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
 * </p><p>
 * Accessibility clients use child identifiers to specify
 * whether they want information about a control or one of its children.
 * Child identifiers are positive integers > 0.
 * The identifier CHILDID_SELF (0) represents the control.
 * When returning a child identifier to a client, you may use CHILDID_NONE
 * to indicate that no child or control has the required information.
 * </p><p>
 * Note: This adapter is typically used by implementors of
 * a custom control to provide very detailed information about
 * the control instance to accessibility clients.
 * </p>
 *
 * @see AccessibleControlListener
 * @see AccessibleControlEvent
 * 
 * @since 2.0
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
	
	public void getChild(AccessibleControlEvent e) {
	}
	
	public void getChildCount(AccessibleControlEvent e) {
	}
	
	public void getDefaultAction(AccessibleControlEvent e) {
	}
	
	public void getFocus(AccessibleControlEvent e) {
	}
	
	public void getRole(AccessibleControlEvent e) {
	}
	
	public void getSelection(AccessibleControlEvent e) {
	}
	
	public void getState(AccessibleControlEvent e) {
	}
	
	public void getValue(AccessibleControlEvent e) {
	}
	
	// May need to implement for IEnumVARIANT
	public void getChildren(AccessibleControlEvent e) {
	}
}

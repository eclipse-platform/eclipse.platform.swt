package org.eclipse.swt.accessibility;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.SWTEventListener;

/**
 * Classes that implement this interface provide methods
 * that deal with the events that are generated when an
 * accessibility client sends a message to a control.
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to a control using the
 * <code>addAccessibleControlListener</code> method and removed
 * using the <code>removeAccessibleControlListener</code> method.
 * When a client requests information the appropriate method
 * will be invoked.
 * </p><p>
 * Accessibility clients use child identifiers to specify
 * whether they want information about a control or one of its children.
 * Child identifiers are positive integers > 0.
 * The identifier CHILDID_SELF (0) represents the control.
 * </p><p>
 * Note: This interface is typically used by implementors of
 * a custom control to provide very detailed information about
 * the control instance to accessibility clients.
 * </p>
 *
 * @see AccessibleControlAdapter
 * @see AccessibleControlEvent
 * 
 * @since 2.0
 */
public interface AccessibleControlListener extends SWTEventListener {

	public void accHitTest(AccessibleControlEvent e);
	public void accLocation(AccessibleControlEvent e);
	public void getChild(AccessibleControlEvent e);
	public void getChildCount(AccessibleControlEvent e);
	public void getDefaultAction(AccessibleControlEvent e);
	public void getFocus(AccessibleControlEvent e);
	public void getRole(AccessibleControlEvent e);
	public void getSelection(AccessibleControlEvent e);
	public void getState(AccessibleControlEvent e);
	public void getValue(AccessibleControlEvent e);
	
	// May need to implement for IEnumVARIANT
	public void getChildren(AccessibleControlEvent e);
	
	// May not implement - not sure what clients use these - likely testing clients, not accesibility (but not sure)
	public void accNavigate(AccessibleControlEvent e);
	//public void accDoDefaultAction(AccessibleControlEvent e);
	//public void accSelect(AccessibleControlEvent e);
	
	// Probably won't implement - the usual parent is probably good enough
	//public void getParent(AccessibleControlEvent e);
	
	// Will not implement
	//public void putName(AccessibleControlEvent e);
	//public void putValue(AccessibleControlEvent e);
}

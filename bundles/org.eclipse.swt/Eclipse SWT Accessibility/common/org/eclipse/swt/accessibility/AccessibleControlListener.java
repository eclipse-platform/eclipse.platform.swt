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
 * Note: This interface is typically used by implementors of
 * a custom control to provide very detailed information about
 * the control instance to accessibility clients.
 * </p>
 *
 * @see AccessibleControlAdapter
 * @see AccessibleControlEvent
 */
public interface AccessibleControlListener extends SWTEventListener {

	public void accHitTest(AccessibleControlEvent e);
	public void accLocation(AccessibleControlEvent e);
	public void accNavigate(AccessibleControlEvent e);
	public void get_accChild(AccessibleControlEvent e);
	public void get_accChildCount(AccessibleControlEvent e);
	public void get_accDefaultAction(AccessibleControlEvent e);
	public void get_accFocus(AccessibleControlEvent e);
	public void get_accParent(AccessibleControlEvent e);
	public void get_accRole(AccessibleControlEvent e);
	public void get_accSelection(AccessibleControlEvent e);
	public void get_accState(AccessibleControlEvent e);
	public void get_accValue(AccessibleControlEvent e);
	
	// May not implement
	public void accDoDefaultAction(AccessibleControlEvent e);
	public void accSelect(AccessibleControlEvent e);
	
	// Will not implement
	//public void put_accName(AccessibleControlEvent e);
	//public void put_accValue(AccessibleControlEvent e);
}

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
 * Note: Accessibility clients use child identifiers to specify
 * whether they want information about a control or one of its children.
 * Child identifiers are increasing integers beginning with 0.
 * The identifier CHILDID_SELF represents the control itself.
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

	/**
	 * Sent when an accessibility client requests the identifier
	 * of the control child at the specified display coordinates.
	 * The default behavior is to do nothing.
	 * <p>
	 * Return the identifier of the child at display point (x, y)
	 * in the <code>childID</code> field of the event object.
	 * Return CHILDID_SELF if point (x, y) is in the control itself
	 * and not in any child. Return CHILDID_NONE if point (x, y)
	 * is not contained in either the control or any of its children.
	 * </p>
	 *
	 * @param e an event object containing the following fields:<ul>
	 *    <li>x, y [IN] - the specified point in display coordinates</li>
	 *    <li>childID [OUT] - the ID of the child at point, or CHILDID_SELF, or CHILDID_NONE</li>
	 * </ul>
	 */
	public void hitTest(AccessibleControlEvent e);

	/**
	 * Sent when an accessibility client requests the location
	 * of the control, or the location of a child of the control.
	 * The default behavior is to do nothing.
	 * <p>
	 * Return a rectangle describing the location of the specified
	 * control or child in the <code>x, y, width, and height</code>
	 * fields of the event object.
	 * </p>
	 *
	 * @param e an event object containing the following fields:<ul>
	 *    <li>childID [IN] - an identifier specifying the control or one of its children</li>
	 *    <li>x, y, width, height [OUT] - the control or child location in display coordinates</li>
	 * </ul>
	 */
	public void getLocation(AccessibleControlEvent e);
	
	public void getChild(AccessibleControlEvent e);
	
	public void getChildCount(AccessibleControlEvent e);
	
	public void getDefaultAction(AccessibleControlEvent e);
	
	public void getFocus(AccessibleControlEvent e);
	
	public void getRole(AccessibleControlEvent e);
	
	public void getSelection(AccessibleControlEvent e);
	
	public void getState(AccessibleControlEvent e);
	
	public void getValue(AccessibleControlEvent e);
	
	public void getChildren(AccessibleControlEvent e);
}

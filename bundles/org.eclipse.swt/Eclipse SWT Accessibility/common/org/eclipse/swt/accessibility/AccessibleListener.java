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
 * <code>addAccessibleListener</code> method and removed
 * using the <code>removeAccessibleListener</code> method.
 * When a client requests information the appropriate method
 * will be invoked.
 * </p><p>
 * Note: Accessibility clients use child identifiers to specify
 * whether they want information about a control or one of its children.
 * Child identifiers are positive integers > 0.
 * The identifier CHILDID_SELF (0) represents the control.
 * </p>
 *
 * @see AccessibleAdapter
 * @see AccessibleEvent
 * 
 * @since 2.0
 */
public interface AccessibleListener extends SWTEventListener {

	/**
	 * Sent when an accessibility client requests the name
	 * of the control, or the name of a child of the control.
	 *
	 * @param e an event object containing the following fields:<ul>
	 *    <li>childID [IN] - an identifier specifying the control or one of its children</li>
	 *    <li>result [OUT] - the requested name string</li>
	 * </ul>
	 */
	public void getName(AccessibleEvent e);

	/**
	 * Sent when an accessibility client requests the help string
	 * of the control, or the help string of a child of the control.
	 *
	 * @param e an event object containing the following fields:<ul>
	 *    <li>childID [IN] - an identifier specifying the control or one of its children</li>
	 *    <li>result [OUT] - the requested help string</li>
	 * </ul>
	 */
	public void getHelp(AccessibleEvent e);

	/**
	 * Sent when an accessibility client requests the keyboard shortcut
	 * of the control, or the keyboard shortcut of a child of the control.
	 *
	 * @param e an event object containing the following fields:<ul>
	 *    <li>childID [IN] - an identifier specifying the control or one of its children</li>
	 *    <li>result [OUT] - the requested keyboard shortcut string (example: "CTRL+C")</li>
	 * </ul>
	 */
	public void getKeyboardShortcut(AccessibleEvent e);

	/**
	 * Sent when an accessibility client requests a description
	 * of the control, or a description of a child of the control.
	 *
	 * @param e an event object containing the following fields:<ul>
	 *    <li>childID [IN] - an identifier specifying the control or one of its children</li>
	 *    <li>result [OUT] - the requested description string</li>
	 * </ul>
	 */
	public void getDescription(AccessibleEvent e);
}

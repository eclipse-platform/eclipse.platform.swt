package org.eclipse.swt.accessibility;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

/**
 * This adapter class provides default implementations for the
 * methods described by the <code>AccessibleListener</code> interface.
 * <p>
 * Classes that wish to deal with <code>AccessibleEvent</code>s can
 * extend this class and override only the methods that they are
 * interested in.
 * </p><p>
 * Note: Accessibility clients use child identifiers to specify
 * whether they want information about a control or one of its children.
 * Child identifiers are positive integers > 0.
 * The identifier CHILDID_SELF (0) represents the control.
 * </p>
 *
 * @see AccessibleListener
 * @see AccessibleEvent
 */
public abstract class AccessibleAdapter implements AccessibleListener {

	/**
	 * Sent when an accessibility client requests the name
	 * of the control, or the name of a child of the control.
	 * The default behavior is to do nothing.
	 *
	 * @param e an event object containing the following fields:
	 * childID [IN] - an identifier specifying the control or one of its children
	 * result [OUT] - the requested name string
	 */
	public void getName(AccessibleEvent e) {
	}
		
	/**
	 * Sent when an accessibility client requests the help string
	 * of the control, or the help string of a child of the control.
	 * The default behavior is to do nothing.
	 *
	 * @param e an event object containing the following fields:
	 * childID [IN] - an identifier specifying the control or one of its children
	 * result [OUT] - the requested help string
	 */
	public void getHelp(AccessibleEvent e) {
	}
		
	/**
	 * Sent when an accessibility client requests the keyboard shortcut
	 * of the control, or the keyboard shortcut of a child of the control.
	 * The default behavior is to do nothing.
	 *
	 * @param e an event object containing the following fields:
	 * childID [IN] - an identifier specifying the control or one of its children
	 * result [OUT] - the requested keyboard shortcut string (example: "CTRL+C")
	 */
	public void getKeyboardShortcut(AccessibleEvent e) {
	}
		
	/**
	 * Sent when an accessibility client requests a description
	 * of the control, or a description of a child of the control.
	 * The default behavior is to do nothing.
	 *
	 * @param e an event object containing the following fields:
	 * childID [IN] - an identifier specifying the control or one of its children
	 * result [OUT] - the requested description string
	 */
	public void getDescription(AccessibleEvent e) {
	}
}

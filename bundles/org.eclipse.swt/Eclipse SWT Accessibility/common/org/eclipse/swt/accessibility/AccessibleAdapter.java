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
 * </p>
 *
 * @see AccessibleListener
 * @see AccessibleEvent
 */
public abstract class AccessibleAdapter implements AccessibleListener {

	/**
	 * Sent when an accessibility client requests the name
	 * of a control, or the specified child of a control.
	 * The default behavior is to do nothing.
	 *
	 * @param e an event object containing the following fields:
	 * childID [IN] - the specified child identifier, or CHILDID_SELF
	 * result [OUT] - the string representing the requested name
	 */
	public void get_accName(AccessibleEvent e) {
	}
		
	public void get_accHelp(AccessibleEvent e) {
	}
		
	public void get_accKeyboardShortcut(AccessibleEvent e) {
	}
		
	public void get_accDescription(AccessibleEvent e) {
	}
}

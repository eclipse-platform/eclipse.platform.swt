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
 * </p>
 *
 * @see AccessibleAdapter
 * @see AccessibleEvent
 */
public interface AccessibleListener extends SWTEventListener {

	public void get_accName(AccessibleEvent e);
	public void get_accHelp(AccessibleEvent e);
	public void get_accKeyboardShortcut(AccessibleEvent e);
	public void get_accDescription(AccessibleEvent e);

	// Will not implement (Windows-specific)
	//public void get_accHelpTopic(AccessibleEvent e);
}

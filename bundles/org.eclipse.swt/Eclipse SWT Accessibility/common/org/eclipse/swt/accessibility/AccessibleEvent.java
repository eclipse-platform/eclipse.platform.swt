package org.eclipse.swt.accessibility;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.SWTEventObject;

/**
 * Instances of this class are sent as a result of
 * accessibility clients sending messages to controls
 * asking for information about the control instance.
 * <p>
 * Note: The meaning of the result field depends
 * on the message that was sent.
 * </p>
 *
 * @see AccessibleListener
 * @see AccessibleAdapter
 * 
 * @since 2.0
 */
public class AccessibleEvent extends SWTEventObject {
	/**
	 * The value of this field is set by an accessibility client
	 * before the accessible listener method is called.
	 * ChildID can be CHILDID_SELF, representing the control itself,
	 * or a 0-based integer representing a specific child of the control.
	 */
	public int childID;
	
	/**
	 * The value of this field must be set in the accessible listener
	 * method before returning.
	 * What to set it to depends on the listener method called, and
	 * the childID specified by the client.
	 */
	public String result;
	
public AccessibleEvent(Object source) {
	super(source);
}

public String toString () {
	return "AccessibleEvent {childID=" + childID + " result=" + result + "}";
}	
}

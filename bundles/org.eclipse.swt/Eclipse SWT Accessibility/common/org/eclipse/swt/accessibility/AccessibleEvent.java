package org.eclipse.swt.accessibility;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
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
 * 
 * @since 2.0
 */
public class AccessibleEvent extends SWTEventObject {
	public int childID;	// IN - can be CHILDID_SELF or a positive integer
	public String result;	// OUT - result depends on the listener method called
	
public AccessibleEvent(Object source) {
	super(source);
}

public String toString () {
	return "AccessibleEvent {childID=" + childID + " result=" + result + "}";
}	
}

package org.eclipse.swt.accessibility;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.SWTEventObject;
import org.eclipse.swt.widgets.*;

/**
 * Instances of this class are sent as a result of
 * accessibility clients sending messages to controls
 * asking for detailed information about the implementation
 * of the control instance. Typically, only implementors
 * of custom controls need to listen for this event.
 * <p>
 * Note: The meaning of each field depends on the
 * message that was sent.
 * </p>
 *
 * @see AccessibleControlListener
 * @see AccessibleControlAdapter
 * 
 * @since 2.0
 */
public class AccessibleControlEvent extends SWTEventObject {
	public int childID;			// IN/OUT
	public Accessible accessible;	// OUT
	public int x, y;				// IN/OUT
	public int width, height;		// OUT
	public int detail;			// IN/OUT
	public String result;			// OUT
	public Object children[];		// [OUT]
	
public AccessibleControlEvent(Object source) {
	super(source);
}

public String toString () {
	return "AccessibleControlEvent {childID=" + childID + 
		" accessible=" + accessible + 
		" x=" + x + 
		" y=" + y + 
		" width=" + width + 
		" height=" + height + 
		" detail=" + detail + 
		" result=" + result + 
		"}";
}	
}

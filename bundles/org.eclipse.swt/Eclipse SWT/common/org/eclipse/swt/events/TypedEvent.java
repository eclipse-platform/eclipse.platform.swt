package org.eclipse.swt.events;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.SWTEventObject;

/**
 * This is the super class for all typed event classes provided
 * by SWT. Typed events contain particular information which is
 * applicable to the event occurance.
 *
 * @see org.eclipse.swt.widgets.Event
 */
public class TypedEvent extends SWTEventObject {
	
	/**
	 * the widget that issued the event
	 */
	public Widget widget;
	
	/**
	 * the time that the event occurred
	 */
	public int time;
	
	/**
	 * a field for application use
	 */
	public Object data;

/**
 * Constructs a new instance of this class.
 *
 * @param source the object that fired the event
 */
public TypedEvent(Object object) {
	super(object);
}

/**
 * Constructs a new instance of this class based on the
 * information in the argument.
 *
 * @param e the low level event to initialize the receiver with
 */
public TypedEvent(Event e) {
	super(e.widget);
	this.widget = e.widget;
	this.time = e.time;
}

}

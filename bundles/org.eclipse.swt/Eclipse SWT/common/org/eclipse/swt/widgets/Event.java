package org.eclipse.swt.widgets;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class provide a description of a particular
 * event which occurred within SWT. The SWT <em>untyped listener</em>
 * API uses these instances for all event dispatching.
 * <p>
 * Note: For a given event, only the fields which are appropriate
 * will be filled in. The contents of the fields which are not used
 * by the event are unspecified.
 * </p>
 * 
 * @see Listener
 * @see org.eclipse.swt.events.TypedEvent
 */

public class Event {
	
	/**
	 * the type of event, as defined by the event type constants
	 * in class <code>SWT</code>
	 *
	 * @see SWT
	 */
	public int type;
	
	/**
	 * the widget that the event occurred in
	 */
	public Widget widget;
	public Widget item;
	public GC gc;
	public int detail;
	public int x, y, width, height;
	public int time;
	public int count;
	public int button;
	public int keyCode;
	public int stateMask;
	public char character;
	public int start, end;
	public String text;
	public boolean doit = true;
	public Object data;
	
/**
* Gets the bounds.
* <p>
* @return a rectangle that is the bounds.
*/
public Rectangle getBounds () {
	return new Rectangle (x, y, width, height);
}
/**
* Sets the bounds.
* <p>
* @param x the new x position
* @param y the new y position
* @param width the new width
* @param height the new height
*/
public void setBounds (Rectangle rect) {
	this.x = rect.x;
	this.y = rect.y;
	this.width = rect.width;
	this.height = rect.height;
}
/**
* Returns a string representation of the object.
*
* @return a string representation of the object
*/
public String toString () {
	return "Event {type=" + type + ",widget=" + widget + ",x=" + x + ",y=" + y + ",width=" + width + ",height=" + height + "}";
}
}

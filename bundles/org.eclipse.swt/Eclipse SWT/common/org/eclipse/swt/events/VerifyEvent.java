package org.eclipse.swt.events;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.widgets.Event;

/**
 * Instances of this class are sent as a result of
 * widgets handling keyboard events
 *
 * @see VerifyListener
 */

public final class VerifyEvent extends KeyEvent {
	
	/**
	 * the range of text being modified
	 */
	public int start, end;
	
	/**
	 * the new text that will be inserted
	 */
	public String text;

	/**
	 * a flag indicating whether the operation should be allowed
	 */
	public boolean doit;
	
/**
 * Constructs a new instance of this class based on the
 * information in the given untyped event.
 *
 * @param e the untyped event containing the information
 */
public VerifyEvent(Event e) {
	super(e);
	this.character = e.character;
	this.keyCode = e.keyCode;
	this.stateMask = e.stateMask;
	this.start = e.start;
	this.end = e.end;
	this.text = e.text;
	this.doit = e.doit;
}

}

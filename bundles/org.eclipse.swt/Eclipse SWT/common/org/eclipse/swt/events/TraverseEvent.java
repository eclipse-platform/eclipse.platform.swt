package org.eclipse.swt.events;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.widgets.Event;

/**
 * Instances of this class are sent as a result of
 * widget traversal actions.
 *
 * @see TraverseListener
 */

public class TraverseEvent extends KeyEvent {
	
	/**
	 * the type of traversal
	 */
	public int detail;
	
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
public TraverseEvent(Event e) {
	super(e);
	this.doit = e.doit;
	this.detail = e.detail;
}

}

package org.eclipse.swt.events;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

import org.eclipse.swt.widgets.Event;

/**
 * Instances of this class are sent as a result of
 * operations being performed on shells.
 *
 * @see ShellListener
 */

public final class ShellEvent extends TypedEvent {

	/**
	 * a flag indicating whether the operation should be allowed
	 */
	public boolean doit;
	
public ShellEvent(Event e) {
	super(e);
	this.doit = e.doit;
}

}


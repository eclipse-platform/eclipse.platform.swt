package org.eclipse.swt.events;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

import org.eclipse.swt.widgets.Event;

/**
 * Instances of this class are sent as a result of
 * keys being pressed and released on the keyboard
 *
 * @see KeyListener
 */

public class KeyEvent extends TypedEvent {
	
	/**
	 * the character represented by the key that was typed
	 */
	public char character;
	
	/**
	 * the key code of the key that was typed
	 */
	public int keyCode;
	
	/**
	 * the state of the keyboard modifier keys at the time
	 * the event was generated
	 */
	public int stateMask;
	
/**
 * Constructs a new instance of this class based on the
 * information in the given untyped event.
 *
 * @param e the untyped event containing the information
 */
public KeyEvent(Event e) {
	super(e);
	this.character = e.character;
	this.keyCode = e.keyCode;
	this.stateMask = e.stateMask;
}

}

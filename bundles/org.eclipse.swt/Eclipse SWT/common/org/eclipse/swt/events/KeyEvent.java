package org.eclipse.swt.events;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
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

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.events;


import org.eclipse.swt.widgets.Event;

/**
 * Instances of this class are sent as a result of
 * keys being pressed and released on the keyboard
 *
 * @see KeyListener
 */

public class KeyEvent extends TypedEvent {
	
 	/**
 	 * the character represented by the key that was typed.  
	 * This is the final character that results after all modifiers have been
 	 * applied.  For example, when the user types Ctrl+A, the character value
 	 * is 0x01 (NUL).  It is important that applications do not attempt to modify
 	 * the character value based on a stateMask (such as SWT.CTRL) or the resulting
 	 * character will not be correct.
 	 */
	public char character;
	
	/**
	 * the key code of the key that was typed,
	 * as defined by the key code constants in class <code>SWT</code>.
	 * When the character field of the event is ambiguous, this field
	 * contains the unicode value of the original character.  For example,
	 * typing Ctrl+M or Return both result in the character '\r' but the
	 * keyCode field will also contain '\r' when Return was typed.
	 * 
	 * @see org.eclipse.swt.SWT
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

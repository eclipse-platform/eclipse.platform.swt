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
 * widgets handling keyboard events
 *
 * @see VerifyListener
 */

public final class VerifyEvent extends KeyEvent {
	
	/**
	 * the range of text being modified.
	 * Setting these fields has no effect.
	 */
	public int start, end;
	
	/**
	 * the new text that will be inserted.
	 * Setting this field will change the text that is about to
	 * be inserted or deleted.
	 */
	public String text;

	/**
	 * a flag indicating whether the operation should be allowed.
	 * Setting this field to false will cancel the operation.
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

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


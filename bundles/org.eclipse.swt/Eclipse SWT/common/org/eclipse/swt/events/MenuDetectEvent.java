/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.events;


import org.eclipse.swt.widgets.Event;

/**
 * Instances of this class are sent whenever the platform-
 * specific trigger for showing a context menu is detected.
 *
 * @see MenuDetectListener
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 *
 * @since 3.3
 */

public final class MenuDetectEvent extends TypedEvent {

	/**
	 * the display-relative x coordinate of the pointer
	 * at the time the context menu trigger occurred
	 */
	public int x;
	
	/**
	 * the display-relative y coordinate of the pointer
	 * at the time the context menu trigger occurred
	 */	
	public int y;
	
	/**
	 * A flag indicating whether the operation should be allowed.
	 * Setting this field to <code>false</code> will cancel the operation.
	 */
	public boolean doit;

	/**
	 * The event trigger type.
	 * <p><ul>
	 * <li>{@link org.eclipse.swt.SWT#CONTEXT_NONE}</li>
	 * <li>{@link org.eclipse.swt.SWT#CONTEXT_POINTER}</li>
	 * <li>{@link org.eclipse.swt.SWT#CONTEXT_FOCUS}</li>
	 * </ul></p>
	 * 
	 * A field indicating whether the event was triggered by a pointing device,
	 * such as a mouse, or by a focus-based device such as a keyboard.
	 * If the trigger was CONTEXT_FOCUS, then the application should provide
	 * new display-relative x and y coordinates based on the current
	 * selection or the current focus.
	 * 
	 * @since 3.8
	 */
	public int detail;
	
	
	private static final long serialVersionUID = -3061660596590828941L;

/**
 * Constructs a new instance of this class based on the
 * information in the given untyped event.
 *
 * @param e the untyped event containing the information
 */
public MenuDetectEvent(Event e) {
	super(e);
	this.x = e.x;
	this.y = e.y;
	this.doit = e.doit;
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the event
 */
public String toString() {
	String string = super.toString ();
	return string.substring (0, string.length() - 1) // remove trailing '}'
		+ " x=" + x
		+ " y=" + y
		+ " doit=" + doit
		+ "}";
}
}

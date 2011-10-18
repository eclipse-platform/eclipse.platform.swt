/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
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
 * Instances of this class are sent whenever mouse
 * related actions occur. This includes mouse buttons
 * being pressed and released, the mouse pointer being 
 * moved and the mouse pointer crossing widget boundaries.
 * <p>
 * Note: The <code>button</code> field is an integer that
 * represents the mouse button number.  This is not the same
 * as the <code>SWT</code> mask constants <code>BUTTONx</code>.
 * </p>
 *
 * @see MouseListener
 * @see MouseMoveListener
 * @see MouseTrackListener
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */

public class MouseEvent extends TypedEvent {
	
	/**
	 * the button that was pressed or released; 1 for the
	 * first button, 2 for the second button, and 3 for the
	 * third button, etc.
	 */
	public int button;
	
	/**
	 * the state of the keyboard modifier keys and mouse masks
	 * at the time the event was generated.
	 * 
	 * @see org.eclipse.swt.SWT#MODIFIER_MASK
	 * @see org.eclipse.swt.SWT#BUTTON_MASK
	 */
	public int stateMask;
	
	/**
	 * the widget-relative, x coordinate of the pointer
	 * at the time the mouse button was pressed or released
	 */
	public int x;
	
	/**
	 * the widget-relative, y coordinate of the pointer
	 * at the time the mouse button was pressed or released
	 */	
	public int y;
	
	/**
	 * the number times the mouse has been clicked, as defined
	 * by the operating system; 1 for the first click, 2 for the
	 * second click and so on.
	 * 
	 * @since 3.3
	 */
	public int count;

	static final long serialVersionUID = 3257288037011566898L;
	
/**
 * Constructs a new instance of this class based on the
 * information in the given untyped event.
 *
 * @param e the untyped event containing the information
 */
public MouseEvent(Event e) {
	super(e);
	this.x = e.x;
	this.y = e.y;
	this.button = e.button;
	this.stateMask = e.stateMask;
	this.count = e.count;
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
		+ " button=" + button
		+ " stateMask=0x" + Integer.toHexString(stateMask)
		+ " x=" + x
		+ " y=" + y
		+ " count=" + count
		+ "}";
}
}

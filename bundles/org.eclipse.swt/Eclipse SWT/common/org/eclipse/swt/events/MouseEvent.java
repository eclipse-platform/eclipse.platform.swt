package org.eclipse.swt.events;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

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
 */

public final class MouseEvent extends TypedEvent {
	
	/**
	 * the button that was pressed or released; 1 for the
	 * first button, 2 for the second button, and 3 for the
	 * third button, etc.
	 */
	public int button;
	
	/**
	 * the state of the keyboard modifier keys at the time
	 * the event was generated
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
}

}


/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.events.*;

/**
 * Instances of this class are created as a result of
 * a touch-based input device being touched. They are found
 * in the <code>touches</code> field of an Event or TouchEvent.
 * <p>
 * </p>
 *
 * @see TouchEvent
 * @see Event
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * 
 * @since 3.7
 */
public final class Touch {
	
	/**
	 * unique identity of the touch. Use this value to track changes to a touch during
	 * the touch's life. Two touches may have the same identity even though they came
	 * from different sources.
	 */
	public long id;
	
	/**
	 * object representing the input source that generated the touch
	 */
	public TouchSource source;
	
	/**
	 * the state of this touch at the time it was generated. If this field is 0
	 * the finger is still touching the device but has not otherwise moved
	 * since the last TouchEvent was generated.
	 * 
	 * @see org.eclipse.swt.SWT#TOUCHSTATE_DOWN
	 * @see org.eclipse.swt.SWT#TOUCHSTATE_MOVE
	 * @see org.eclipse.swt.SWT#TOUCHSTATE_UP
	 */
	public int state;
	
	/**
	 * a flag indicating that the touch is the first touch from a previous state
	 * of no touch points. Once designated as such, the touch remains the
	 * primary touch until all fingers are removed from the device. 
	 */
	public boolean primary;
	
	/** 
	 * the X location of the touch in TouchSource coordinates
	 */
	public int x;
	
	/**
	 * the Y location of the touch in TouchSource coordinates
	 */
	public int y;

/**
 * Constructs a new touch state from the given inputs.
 * 
 * @param identity Identity of the touch. 
 * @param source Object representing the device that generated the touch. 
 * @param state One of the state constants representing the state of this touch.
 * @param primary Is this touch the primary touch in a collection of touches?
 * @param x X location of the touch in screen coordinates
 * @param y Y location of the touch in screen coordinates
 */
Touch (long identity, TouchSource source, int state, boolean primary, int x, int y) {
	this.id = identity;
	this.source = source;
	this.state = state;
	this.primary = primary;
	this.x = x;
	this.y = y;
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the event
 */
public String toString() {
	return "Touch {id=" + id
	+ " source=" + source
	+ " state=" + state
	+ " primary=" + primary
	+ " x=" + x
	+ " y=" + y
	+ "}";
}

}

package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.events.*;

/**
 * Manages an interactive paint session.
 * Note that the coordinates received via the listener interfaces are virtualized to zero-origin
 * relative to the painting surface.
 */ 
public interface PaintSession extends MouseListener, MouseMoveListener {
	/**
	 * Returns the paint surface associated with this paint session
	 * 
	 * @return the associated PaintSurface
	 */
	public PaintSurface getPaintSurface();

	/**
	 * Activates the session.
	 * 
	 * Note: When overriding this method, call super.beginSession() at method start.
	 */
	public abstract void beginSession();
	
	/**
	 * Deactivates the session.
     *
	 * Note: When overriding this method, call super.endSession() at method exit.
     */
	public abstract void endSession();
	
	/**
	 * Resets the session.
	 * Aborts any operation in progress.
	 * 
	 * Note: When overriding this method, call super.resetSession() at method exit.
	 */
	public abstract void resetSession();
	
	
	/**
	 * Returns the name associated with this tool.
	 * 
	 * @return the localized name of this tool
	 */
	public String getDisplayName();
}

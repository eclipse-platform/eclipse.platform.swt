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
package org.eclipse.swt.custom;


import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * 
 */
public class CTabFolderEvent extends TypedEvent {
	/**
	 * The tab item for the operation.
	 */
 	public Widget item;

 	/**
	 * A flag indicating whether the operation should be allowed.
	 * Setting this field to <code>false</code> will cancel the operation.
	 */
 	public boolean doit;

	/**
	* DO NOT USE FIELD - UNDER CONSTRUCTION
	* @ since 3.0
	*/
 	public int x;
	public int y;
	public int width;
	public int height;
 	
 	/**
	 * the graphics context to use when painting
	 * that is configured to use the colors, font and
	 * damaged region of the control.  It is valid
	 * only during the paint and must not be disposed
	 * 
	 * DO NOT USE FIELD - UNDER CONSTRUCTION
	 * @ since 3.0
	 */
 	public GC gc;

/**
 * Constructs a new instance of this class.
 *
 * @param w the widget that fired the event
 */
CTabFolderEvent(Widget w) {
	super(w);
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
		+ " item=" + item
		+ " doit=" + doit
		+ " x=" + x
		+ " x=" + y
		+ " x=" + width
		+ " x=" + height
		+ " gc=" + gc
		+ "}";
}
}
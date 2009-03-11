/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;

/**
 * A <code>StatusTextEvent</code> is sent by a {@link Browser} to
 * {@link StatusTextListener}'s when the status text is changed.
 * The status text is typically displayed in the status bar of
 * a browser application.
 * 
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 *
 * @since 3.0
 */
public class StatusTextEvent extends TypedEvent {
	/** status text */
	public String text;
	
	static final long serialVersionUID = 3258407348371600439L;

/**
 * Constructs a new instance of this class.
 *
 * @param widget the widget that fired the event
 *
 * @since 3.5
 */
public StatusTextEvent(Widget widget) {
	super(widget);
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
		+ " text=" + text
		+ "}";
}
}

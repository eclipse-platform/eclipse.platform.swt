/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
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
 * @since 3.0
 */
public class StatusTextEvent extends TypedEvent {
	/** status text */
	public String text;
	
	static final long serialVersionUID = 3258407348371600439L;

StatusTextEvent(Widget w) {
	super(w);
}
}

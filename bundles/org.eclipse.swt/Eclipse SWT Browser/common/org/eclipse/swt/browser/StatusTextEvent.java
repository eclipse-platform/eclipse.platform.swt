/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;

/**
 * This event is sent to StatusTextListeners when the status text
 * is changed.  The status text is typically displayed in the status
 * bar of a browser.
 */
public class StatusTextEvent extends TypedEvent {
	/** status text */
	public String text;

	StatusTextEvent(Widget w) {
		super(w);
	}
}
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
 * This event is sent to LocationListeners when the location
 * is changed.
 * 
 * @since 3.0
 */
public class LocationEvent extends TypedEvent {
	/** current location */
	public String location;

	LocationEvent(Widget w) {
		super(w);
	}
}

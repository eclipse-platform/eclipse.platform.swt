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
 * This event is sent to OpenWindowListeners when a new Browser is
 * requested.
 * 
 * <p>
 * This event is typically sent when a script requesting a new
 * instance is executed. For example, the javascript command
 * <code>window.open("document.html", "Document")</code> will
 * trigger an OpenWindow event when it is run.
 * </p>
 * 
 * <p>
 * The default behaviour is to ignore new window requests and cancel
 * the navigation request. To handle new window requests,
 * the application needs to create an instance of Browser and set it
 * into the OpenWindowEvent.browser field.
 * </p>
 * 
 * <p>
 * NOTE: The API in the browser package is NOT finalized.
 * Use at your own risk, because it will most certainly change.
 * The only reason this API is being released at this time is so that 
 * other teams can try it out.
 * </p>
 * 
 * @since 3.0
 */
public class OpenWindowEvent extends TypedEvent {
	
	/** 
	 * Browser provided by the application. Default is null and null
	 * will cancel the associated navigation request.
	 */
	public Browser browser;

OpenWindowEvent(Widget w) {
	super(w);
}
}

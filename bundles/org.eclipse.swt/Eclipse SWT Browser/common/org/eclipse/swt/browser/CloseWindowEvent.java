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
 * This event is sent to CloseWindowListeners when an existing Browser
 * should be disposed.
 * 
 * <p>
 * This event is typically sent when a script closing an existing
 * window is executed. For example, the javascript command
 * <code>window.close()</code> will trigger a CloseWindow event
 * when it is run.
 * </p>
 * 
 * <p>
 * The default behaviour is to ignore close window requests. To handle
 * close window requests, the application would dispose the Browser
 * instance available in the CloseWindowEvent.widget field. The
 * application could also dispose the Shell hosting that Browser.
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
public class CloseWindowEvent extends TypedEvent {

CloseWindowEvent(Widget w) {
	super(w);
}
}

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

import org.eclipse.swt.internal.SWTEventListener;

/**
 * This listener interface may be implemented in order to receive
 * StatusTextEvents.
 * @see StatusTextEvent
 */
public interface StatusTextListener extends SWTEventListener {

/**
 * This method is called when the status text is changed. The
 * status text is typically showed in the status bar of a browser. 
 * <p>
 *
 * @param event.text the modified status text	
 *
 * @see StatusTextEvent
 */
public void changed(StatusTextEvent event);
}
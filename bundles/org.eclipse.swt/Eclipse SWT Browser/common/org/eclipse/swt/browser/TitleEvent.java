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
 * A <code>TitleEvent</code> is sent by a {@link Browser} to
 * {@link TitleListener}'s when the title of the current document
 * is available or when it is modified.
 * 
 * @since 3.0
 */
public class TitleEvent extends TypedEvent {
	/** the title of the current document */
	public String title;
	
	static final long serialVersionUID = 4121132532906340919L;

TitleEvent(Widget w) {
	super(w);
}
}

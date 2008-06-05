/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.events;


import org.eclipse.swt.widgets.Event;

/**
 * Instances of this class are sent as a result of
 * widgets gaining and losing focus.
 *
 * @see FocusListener
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */

public final class FocusEvent extends TypedEvent {

	static final long serialVersionUID = 3258134643684227381L;

/**
 * Constructs a new instance of this class based on the
 * information in the given untyped event.
 *
 * @param e the untyped event containing the information
 */
public FocusEvent(Event e) {
	super(e);
}

}


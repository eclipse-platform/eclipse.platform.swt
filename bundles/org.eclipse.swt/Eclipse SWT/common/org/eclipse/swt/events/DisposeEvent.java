/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.events;


import org.eclipse.swt.widgets.Event;

/**
 * Instances of this class are sent as a result of
 * widgets being disposed.
 *
 * @see DisposeListener
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */

public final class DisposeEvent extends TypedEvent {

	static final long serialVersionUID = 3257566187633521206L;

/**
 * Constructs a new instance of this class based on the
 * information in the given untyped event.
 *
 * @param e the untyped event containing the information
 */
public DisposeEvent(Event e) {
	super(e);
}

}


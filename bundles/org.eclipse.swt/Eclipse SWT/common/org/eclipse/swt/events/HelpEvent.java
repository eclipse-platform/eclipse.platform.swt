package org.eclipse.swt.events;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.widgets.Event;

/**
 * Instances of this class are sent as a result of
 * help being requested for a widget.
 *
 * @see HelpListener
 */

public final class HelpEvent extends TypedEvent {

/**
 * Constructs a new instance of this class based on the
 * information in the given untyped event.
 *
 * @param e the untyped event containing the information
 */
public HelpEvent(Event e) {
	super(e);
}

}


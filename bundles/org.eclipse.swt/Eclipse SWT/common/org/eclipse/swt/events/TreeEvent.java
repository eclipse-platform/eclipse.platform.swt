package org.eclipse.swt.events;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.widgets.Event;

/**
 * Instances of this class are sent as a result of
 * trees being expanded and collapsed.
 *
 * @see TreeListener
 */

public final class TreeEvent extends SelectionEvent {

/**
 * Constructs a new instance of this class based on the
 * information in the given untyped event.
 *
 * @param e the untyped event containing the information
 */
public TreeEvent(Event e) {
	super(e);
}

}


package org.eclipse.swt.custom;
/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp 2000, 2001
 */

/* Imports */
import org.eclipse.swt.events.*;

/**
 * This event is sent after a text change occurs.
 */
public final class ExtendedModifyEvent extends TypedEvent {
	public int start;			// start offset of the new text
	public int length;			// length of the new text
	public String replacedText;	// replaced text or empty string if no text was replaced
	
public ExtendedModifyEvent(StyledTextEvent e) {
	super(e);
	start = e.start;
	length = e.end - e.start;
	replacedText = e.text;
}
}
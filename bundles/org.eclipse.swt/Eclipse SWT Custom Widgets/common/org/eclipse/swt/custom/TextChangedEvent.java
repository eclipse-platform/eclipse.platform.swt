package org.eclipse.swt.custom;
/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp 2000, 2001
 */

/* Imports */
import org.eclipse.swt.events.*;

/**
 * This event is sent by the StyledTextContent implementor when a change to 
 * the text occurs.
 */
public class TextChangedEvent extends TypedEvent {
/**
 * Create the TextChangedEvent to be used by the StyledTextContent implementor.
 * <p>
 *
 * @param source the object that will be sending the TextChangedEvent, cannot be null	
 */
public TextChangedEvent(StyledTextContent source) {
	super(source);
}
}
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
	public int start;				// replace start offset
	public String replacedText;		// the replaced text or empty String of no text was replaced
	public int replacedCharCount; 	// length of text being replaced
	public int newCharCount; 		// length of new text
	public int replacedLineCount;	// number of lines replaced
	public int newLineCount; 		// number of new lines

/**
 * Create the TextChangedEvent to be used by the StyledTextContent implementor.
 * <p>
 *
 * @param source the object that will be sending the TextChangedEvent, cannot be null	
 */
public TextChangedEvent(StyledTextContent source) {
	super(source);
}
TextChangedEvent(StyledTextContent source, StyledTextEvent e) {
	super(source);
	start = e.start;
	replacedCharCount = e.replacedCharCount;
	newCharCount = e.newCharCount;
	replacedLineCount = e.replacedLineCount;
	newLineCount = e.newLineCount;
	replacedText = e.text;
}

}
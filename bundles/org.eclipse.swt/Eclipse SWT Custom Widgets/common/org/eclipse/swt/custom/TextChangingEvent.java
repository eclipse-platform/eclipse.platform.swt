package org.eclipse.swt.custom;
/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.events.*;

/**
 * This event is sent by the StyledTextContent implementor when a change to 
 * the text is about to occur.
 */
public class TextChangingEvent extends TypedEvent {
	public int start;				// replace start offset
	public String newText;			// text that is going to be inserted or empty
							// String if no text will be inserted
	public int replaceCharCount; 		// length of text that is going to be replaced
	public int newCharCount; 		// length of text that is going to be inserted
	public int replaceLineCount;		// number of lines that are going to be replaced
	public int newLineCount; 		// number of new lines that are going to be inserted

/**
 * Create the TextChangedEvent to be used by the StyledTextContent implementor.
 * <p>
 *
 * @param source the object that will be sending the TextChangedEvent, cannot be null	
 */
public TextChangingEvent(StyledTextContent source) {
	super(source);
}
TextChangingEvent(StyledTextContent source, StyledTextEvent e) {
	super(source);
	start = e.start;
	replaceCharCount = e.replaceCharCount;
	newCharCount = e.newCharCount;
	replaceLineCount = e.replaceLineCount;
	newLineCount = e.newLineCount;
	newText = e.text;
}

}

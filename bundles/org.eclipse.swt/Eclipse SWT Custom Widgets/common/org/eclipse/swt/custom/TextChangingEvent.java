package org.eclipse.swt.custom;
/*
 * (c) Copyright IBM Corp. 2000, 2002.
 * All Rights Reserved
 */

import org.eclipse.swt.events.*;

/**
 * This event is sent by the StyledTextContent implementor when a change
 * to the text is about to occur.
 */
public class TextChangingEvent extends TypedEvent {
	/**
	 * Start offset of the text that is going to be replaced
	 */
	public int start;
	/**
	 * Text that is going to be inserted or empty string
	 * if no text will be inserted
	 */
	public String newText;
	/**
	 * Length of text that is going to be replaced
	 */
	public int replaceCharCount;
	/**
	 * Length of text that is going to be inserted
	 */
	public int newCharCount;
	/**
	 * Number of lines that are going to be replaced
	 */
	public int replaceLineCount;
	/**
	 * Number of new lines that are going to be inserted
	 */
	public int newLineCount;

/**
 * Create the TextChangedEvent to be used by the StyledTextContent implementor.
 * <p>
 *
 * @param source the object that will be sending the new TextChangingEvent, 
 * 	cannot be null	
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

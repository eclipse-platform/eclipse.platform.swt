package org.eclipse.swt.custom;
/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp 2000, 2001
 */

/* Imports */
import org.eclipse.swt.events.*;

public interface StyledTextContent {

/**
 * Called by StyledText to add itself as an Observer to content changes.
 * Implementors should send a TextChangedEvent when changes to the content
 * occur. The widget only updates the screen when it receives a TextChangedEvent.
 * <p>
 *
 * @param listener the listener
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 */
public void addTextChangedListener(TextChangedListener listener);

/**
 * Return the number of characters in the content.
 * <p>
 *
 * @return the number of characters in the content.
 */
public int getCharCount();

/**
 * Return the line at the given character offset without delimiters.
 * <p>
 *
 * @param offset offset of the line to return. Does not include delimiters
 *	of preceeding lines. Offset 0 is the first character of the document.
 * @return the line text without delimiters
 */
public String getLine(int offset);

/**
 * Return the line index at the given character offset.
 * <p>
 *
 * @param offset offset of the line to return. The first character of the document
 *	is at offset 0.  An offset of getLength() is valid and should answer
 *	the number of lines. 
 * @return the line index. The first line is at index 0.  If the character at offset 
 *	is a delimiter character, answer the line index of the line that is delimited.  
 *	For example, if text = "\r\n\r\n", and delimiter = "\r\n", then:
 * <ul>
 * <li>getLineAtOffset(0) == 0
 * <li>getLineAtOffset(1) == 0
 * <li>getLineAtOffset(2) == 1
 * <li>getLineAtOffset(3) == 1
 * <li>getLineAtOffset(4) == 2
 * </ul>
 */
public int getLineAtOffset(int offset);

/**
 * Return the number of lines.  Should answer 1 when no text is specified.
 * The  StyledText widget relies on this behavior for drawing the cursor.
 * <p>
 *
 * @return the number of lines.  For example:
 * <ul>
 * <li>	text value ==> getLineCount		
 * <li>	null ==> 1		
 * <li>	"" ==> 1		
 * <li>	"a\n" ==> 2			
 * <li>	"\n\n" ==> 3			
 * </ul>
 */
public int getLineCount();

/**
 * Return the line delimiter that should be used by the StyledText 
 * widget when inserting new lines. New lines entered using key strokes 
 * and paste operations use this line delimiter.
 * Implementors may use System.getProperty("line.separator") to return
 * the platform line delimiter.
 * <p>
 *
 * @return the line delimiter that should be used by the StyledText widget
 *	when inserting new lines.
 */
public String getLineDelimiter();

/**
 * Return the character offset of the first character of the given line.
 * <p>
 * <b>NOTE:</b> When there is no text (i.e., no lines), getOffsetAtLine(0) 
 * is a valid call that should return 0.
 * </p>
 *
 * @param lineIndex	index of the line. The first line is at index 0.
 * @return offset offset of the first character of the line. The first character 
 *	of the document is at offset 0.  The return value should include
 *	line delimiters.  For example, if text = "\r\ntest\r\n" and delimiter = "\r\n", 
 *	then:
 * <ul>
 * <li>getOffsetAtLine(0) == 0
 * <li>getOffsetAtLine(1) == 2
 * <li>getOffsetAtLine(2) == 8
 * </ul>
 */
public int getOffsetAtLine(int lineIndex);

/**
 * Returns a string representing the content at the given range.
 * <p>
 *
 * @param start	the start offset of the text to return. Offset 0 is the first 
 *	character of the document.
 * @param length the length of the text to return
 * @return the text at the given range
 */
public String getTextRange(int start, int length);

/**
 * Remove the specified text changed listener.
 * <p>
 *
 * @param listener the listener
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 */
public void removeTextChangedListener(TextChangedListener listener);

/**
 * Replace the text with "newText" starting at position "start" 
 * for a length of "replaceLength".
 * <p>
 * Implementors have to notify TextChanged listeners after the content has 
 * been updated. The TextChangedEvent should be set as follows:
 * <ul>
 * <li>event.type = SWT.TextReplaced
 * <li>event.start = start of the replaced text
 * <li>event.numReplacedLines = number of replaced lines
 * <li>event.numNewLines = number of new lines
 * <li>event.replacedLength = length of the replaced text
 * <li>event.newLength = length of the new text
 * </ul>
 * <b>NOTE:</b> numNewLines is the number of inserted lines and numReplacedLines is 
 * the number of deleted lines based on the change that occurs visually.  For
 * example:
 * <ul>
 * <li>(replacedText, newText) ==> (numReplacedLines, numNewLines)
 * <li>("", "\n") ==> (0, 1)
 * <li>("\n\n", "a") ==> (2, 0)
 * <li>("a", "\n\n") ==> (0, 2)
 * <li>("\n", "") ==> (1, 0)
 * </ul>
 * </p>
 *
 * @param start	start offset of text to replace, none of the offsets include 
 *	delimiters of preceeding lines, offset 0 is the first character of the document 
 * @param replaceLength	start offset of text to replace
 * @param newText start offset of text to replace
 */
public void replaceTextRange(int start, int replaceLength, String text);

/**
 * Set text to "text".
 * Implementors have to notify TextChanged listeners after the content has 
 * been updated. The TextChangedEvent being sent must have the event type 
 * set to SWT.TextSet.
 * <p>
 *
 * @param text the new text
 */
public void setText (String text);
}
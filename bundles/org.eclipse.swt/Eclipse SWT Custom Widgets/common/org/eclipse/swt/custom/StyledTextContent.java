/*******************************************************************************
 * Copyright (c) 2000, 2020 IBM Corporation and others.
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
package org.eclipse.swt.custom;


/**
 * Clients may implement the StyledTextContent interface to provide a
 * custom store for the StyledText widget content. The StyledText widget
 * interacts with its StyledTextContent in order to access and update
 * the text that is being displayed and edited in the widget.
 * A custom content implementation can be set in the widget using the
 * StyledText.setContent API.
 */
public interface StyledTextContent {

/**
 * Called by StyledText to add itself as an Observer to content changes.
 * See TextChangeListener for a description of the listener methods that
 * are called when text changes occur.
 *
 * @param listener the listener
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 */
public void addTextChangeListener(TextChangeListener listener);

/**
 * Return the number of characters in the content.
 *
 * @return the number of characters in the content.
 */
public int getCharCount();

/**
 * Return the line at the given line index without delimiters.
 *
 * @param lineIndex index of the line to return. Does not include
 *	delimiters of preceding lines. Index 0 is the first line of the
 * 	content.
 * @return the line text without delimiters
 * 	For example, if text = "\r\n\r\n", and delimiter = "\r\n", then:
 * <ul>
 * <li>getLine(0) == ""
 * <li>getLine(1) == ""
 * <li>getLine(2) == ""
 * </ul>
 * 	or if text = "A\nBC\nD", and delimiter = "\n", then:
 * <ul>
 * <li>getLine(0) == "A"
 * <li>getLine(1) == "BC"
 * <li>getLine(2) == "D"
 * </ul>
 */
public String getLine(int lineIndex);

/**
 * Return the line index at the given character offset.
 *
 * @param offset offset of the line to return. The first character of the
 * 	document is at offset 0.  An offset of {@link #getCharCount()} is valid
 *	and should answer the line index of the last line.
 * @return the line index. The first line is at index 0.  If the character
 * 	at offset is a delimiter character, answer the line index of the line
 * 	that is delimited.
 * 	For example, if text = "\r\n\r\n", and delimiter = "\r\n", then:
 * <ul>
 * <li>getLineAtOffset(0) == 0
 * <li>getLineAtOffset(1) == 0
 * <li>getLineAtOffset(2) == 1
 * <li>getLineAtOffset(3) == 1
 * <li>getLineAtOffset(4) == 2
 * </ul>
 * 	or if text = "A\nBC\nD", and delimiter = "\n", then:
 * <ul>
 * <li>getLineAtOffset(0) == 0
 * <li>getLineAtOffset(1) == 0
 * <li>getLineAtOffset(2) == 1
 * <li>getLineAtOffset(3) == 1
 * <li>getLineAtOffset(4) == 1
 * <li>getLineAtOffset(5) == 2
 * <li>getLineAtOffset(6) == 2
 * </ul>
 */
public int getLineAtOffset(int offset);

/**
 * Return the number of lines.  Should answer 1 when no text is specified.
 * The  StyledText widget relies on this behavior for drawing the cursor.
 *
 * @return the number of lines.  For example:
 * <ul>
 * <li>	text value ==&gt; getLineCount
 * <li>	null ==&gt; 1
 * <li>	"" ==&gt; 1
 * <li>	"a\n" ==&gt; 2
 * <li>	"\n\n" ==&gt; 3
 * </ul>
 */
public int getLineCount();

/**
 * Return the line delimiter that should be used by the StyledText
 * widget when inserting new lines. New lines entered using key strokes
 * and paste operations use this line delimiter.
 * Implementors may use System.lineSeparator() to return
 * the platform line delimiter.
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
 * @param lineIndex index of the line. The first line is at index 0.
 * @return offset offset of the first character of the line. The first
 * 	character of the document is at offset 0.  The return value should
 * 	include line delimiters.
 * 	For example, if text = "\r\ntest\r\n" and delimiter = "\r\n", then:
 * <ul>
 * <li>getOffsetAtLine(0) == 0
 * <li>getOffsetAtLine(1) == 2
 * <li>getOffsetAtLine(2) == 8
 * </ul>
 */
public int getOffsetAtLine(int lineIndex);

/**
 * Returns a string representing the content at the given range.
 *
 * @param start the start offset of the text to return. Offset 0 is the
 * 	first character of the document.
 * @param length the length of the text to return
 * @return the text at the given range
 */
public String getTextRange(int start, int length);

/**
 * Remove the specified text changed listener.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 */
public void removeTextChangeListener(TextChangeListener listener);

/**
 * Replace the text with "newText" starting at position "start"
 * for a length of "replaceLength".
 * <p>
 * Implementors have to notify the TextChangeListeners that were added
 * using <code>addTextChangeListener</code> before and after the content
 * is changed. A <code>TextChangingEvent</code> has to be sent to the
 * textChanging method before the content is changed and a
 * <code>TextChangedEvent</code> has to be sent to the textChanged method
 * after the content has changed.
 * The text change that occurs after the <code>TextChangingEvent</code>
 * has been sent has to be consistent with the data provided in the
 * <code>TextChangingEvent</code>.
 * This data will be cached by the widget and will be used when the
 * <code>TextChangedEvent</code> is received.
 * <p>
 * The <code>TextChangingEvent</code> should be set as follows:
 * </p>
 * <ul>
 * <li>event.start = start of the replaced text
 * <li>event.newText = text that is going to be inserted or empty String
 *	if no text will be inserted
 * <li>event.replaceCharCount = length of text that is going to be replaced
 * <li>event.newCharCount = length of text that is going to be inserted
 * <li>event.replaceLineCount = number of lines that are going to be replaced
 * <li>event.newLineCount = number of new lines that are going to be inserted
 * </ul>
 * <b>NOTE:</b> newLineCount is the number of inserted lines and replaceLineCount
 * is the number of deleted lines based on the change that occurs visually.
 * For example:
 * <ul>
 * <li>(replaceText, newText) ==&gt; (replaceLineCount, newLineCount)
 * <li>("", "\n") ==&gt; (0, 1)
 * <li>("\n\n", "a") ==&gt; (2, 0)
 * <li>("a", "\n\n") ==&gt; (0, 2)
 * <li>("\n", "") ==&gt; (1, 0)
 * </ul>
 *
 * @param start start offset of text to replace, none of the offsets include
 *	delimiters of preceding lines, offset 0 is the first character of the
 * 	document
 * @param replaceLength length of text to replace
 * @param text text to replace
 * @see TextChangeListener
 */
public void replaceTextRange(int start, int replaceLength, String text);

/**
 * Set text to "text".
 * Implementors have to send a <code>TextChangedEvent</code> to the
 * textSet method of the TextChangeListeners that were added using
 * <code>addTextChangeListener</code>.
 *
 * @param text the new text
 * @see TextChangeListener
 */
public void setText(String text);
}

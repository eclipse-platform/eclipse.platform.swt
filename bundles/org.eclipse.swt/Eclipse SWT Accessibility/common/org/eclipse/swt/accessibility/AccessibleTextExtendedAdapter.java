/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.accessibility;

/**
 * WARNING: API UNDER CONSTRUCTION
 * 
 * This adapter class provides default implementations for the
 * methods in the <code>AccessibleTextExtendedListener</code> interface.
 * <p>
 * Classes that wish to deal with <code>AccessibleTextExtended</code> events can
 * extend this class and override only the methods that they are
 * interested in.
 * </p>
 *
 * @see AccessibleTextExtendedListener
 * @see AccessibleTextExtendedEvent
 *
 * @since 3.6
 */
public class AccessibleTextExtendedAdapter extends AccessibleTextAdapter implements AccessibleTextExtendedListener {
	/**
	 * Adds a text selection.
	 * 
	 * @param e an event object containing the following fields:<ul>
	 * <li>[in] start - the 0 based offset of the first character of the new selection
	 * <li>[in] end - the 0 based offset after the last character of the new selection
	 * </ul>
	 */
	public void addSelection(AccessibleTextExtendedEvent e) {}

	/**
	 * Returns the total number of characters in the text.
	 * 
	 * @param e an event object containing the following fields:<ul>
	 * <li>[out] count - the total number of characters
	 * </ul>
	 */
	public void getCharacterCount(AccessibleTextExtendedEvent e) {}

	/**
	 * Returns the number of links and link groups contained within this hypertext
	 * paragraph.
	 * 
	 * @param e an event object containing the following fields:<ul>
	 * <li>[out] count - the number of links and link groups within this hypertext paragraph,
	 * 		or 0 if there are none
	 * </ul>
	 */
	public void getHyperlinkCount(AccessibleTextExtendedEvent e) {}

	/**
	 * Returns the specified hyperlink.
	 * 
	 * @param e an event object containing the following fields:<ul>
	 * <li>[in] index - the 0 based index of the hyperlink to return
	 * <li>[out] accessible - the specified hyperlink object, or null if the index is invalid
	 * </ul>
	 */
	public void getHyperlink(AccessibleTextExtendedEvent e) {}

	/**
	 * Returns the index of the hyperlink that is associated with this character offset.
	 * <p>
	 * This is the case when a link spans the given character index.
	 * </p>
	 * @param e an event object containing the following fields:<ul>
	 * <li>[in] offset - the 0 based offset of the character for which to return the link index.
	 * <li>[out] index - the 0 based index of the hyperlink that is associated with this
	 * 		character offset, or -1 if the offset is not in a link
	 * </ul>
	 */
	public void getHyperlinkIndex(AccessibleTextExtendedEvent e) {}

	/**
	 * Returns the offset of the character under the specified point.
	 * <p>
	 * The same functionality could be achieved by using the bounding
	 * boxes for each character as returned by getCharacterBounds.
	 * The method getOffsetAtPoint, however, can be implemented more efficiently.
	 * </p>
	 * 
	 * @param e an event object containing the following fields:<ul>
	 * <li>[in] x - the x value in display coordinates for which to look up the offset of the character
	 * 		that is rendered on the display at that point
	 * <li>[in] y - the position's y value for which to look up the offset of the character
	 * 		that is rendered on the display at that point
	 * <li>[out] offset - the 0 based offset of the character under the given point,
	 * 		or -1 if the point is invalid or there is no character under the point
	 * </ul>
	 */
	public void getOffsetAtPoint(AccessibleTextExtendedEvent e) {}

	/**
	 * Returns the text range(s) contained within the given bounding box.
	 * <p>
	 * Partially visible characters are included in the returned ranges.
	 * </p>
	 * 
	 * @param e an event object containing the following fields:<ul>
	 * <li>[in] x - the X coordinate of the top left corner of the bounding box
	 * <li>[in] y - the Y coordinate of the top left corner of the bounding box
	 * <li>[in] width - the width of the bounding box
	 * <li>[in] height - the height of the bounding box
	 * <li>[typical out] start - the 0 based offset of the first character of the substring in the bounding box
	 * <li>[typical out] end - the 0 based offset after the last character of the substring in the bounding box
	 * <li>[optional out] ranges - an array of pairs specifying the start and end offsets of each range,
	 * 		if the text range is clipped
	 * </ul>
	 */
	public void getRanges(AccessibleTextExtendedEvent e) {}

	/**
	 * Returns the character offsets of the specified text selection.
	 * 
	 * @param e an event object containing the following fields:<ul>
	 * <li>[in] index - the 0 based index of the selection
	 * <li>[out] start - the 0 based offset of first selected character
	 * <li>[out] end - the 0 based offset after the last selected character
	 * </ul>
	 */
	public void getSelection(AccessibleTextExtendedEvent e) {}

	/**
	 * Returns the number of active non-contiguous selections.
	 * 
	 * @param e an event object containing the following fields:<ul>
	 * <li>[out] count - the number of active non-contiguous selections
	 * </ul>
	 */
	public void getSelectionCount(AccessibleTextExtendedEvent e) {}

	/**
	 * Returns a substring and its range for the given range, count and boundary type.
	 * <p>
	 * Returns the substring of the specified boundary type that is located count
	 * positions from the given character range. Also returns the start and end
	 * offsets of the returned substring.
	 * </p><p>
	 * For example, if the boundary type is TEXT_BOUNDARY_WORD, then the complete
	 * word that is located count words from the specified range is returned.
	 * If count is negative, then return the word that is count words before start.
	 * If count is positive, then return the word that is count words after end.
	 * If count is zero, start and end are the same, so return the word at start.
	 * </p><p>
	 * The whole text can be requested by passing start == 0 and end == getCharacterCount,
	 * TEXT_BOUNDARY_ALL, and 0 for count. Alternatively the whole text can be requested
	 * by calling AccessibleControlListener.getValue().
	 * </p><p>
	 * If start and end are valid, but no suitable word (or other boundary type) is found,
	 * the returned string is null and the returned range is degenerate (start == end).
	 * </p>
	 * 
	 * @param e an event object containing the following fields:<ul>
	 * <li>[in] type - the boundary type of the substring to return. One of:<ul>
	 * 		<li> TEXT_BOUNDARY_CHAR
	 * 		<li> TEXT_BOUNDARY_WORD
	 * 		<li> TEXT_BOUNDARY_SENTENCE
	 * 		<li> TEXT_BOUNDARY_PARAGRAPH
	 * 		<li> TEXT_BOUNDARY_LINE
	 * 		<li> TEXT_BOUNDARY_ALL
	 * 		</ul>
	 * <li>[in,out] start - the 0 based offset of first character of the substring
	 * <li>[in,out] end - the 0 based offset after the last character of the substring
	 * <li>[in,out] count - the number of boundary type units to move to find the substring to return.<ul>
	 * 		<li>in: look count units before start if count < 0, or after end if count > 0. Look at start if count == 0.
	 * 		<li>out: the actual number of boundary type units that were moved. This may be fewer than the input count.
	 * 		</ul>
	 * <li>[out] result - the requested substring.  This may be empty or null
	 * 		when no appropriate substring is found, or if the type is invalid.
	 * </ul>
	 */
	public void getText(AccessibleTextExtendedEvent e) {}

	/**
	 * Returns the bounding box(es) of the specified text range in display coordinates.
	 * <p>
	 * Note: The virtual character after the last character of the represented text,
	 * i.e. the one at offset getCharacterCount, is a special case. It represents the
	 * current input position and will therefore typically be queried by AT more
	 * often than other positions.  Because it does not represent an existing character
	 * its bounding box is defined in relation to preceding characters.  It should be
	 * roughly equivalent to the bounding box of some character when inserted at the
	 * end of the text; its height typically being the maximal height of all the
	 * characters in the text or the height of the preceding character, its width
	 * being at least one pixel so that the bounding box is not degenerate.
	 * </p>
	 * 
	 * @param e an event object containing the following fields:<ul>
	 * <li>[in] start - the 0 based offset of the first character of the substring for which to get the bounding box
	 * <li>[in] end - the 0 based offset after the last character of the substring for which to get the bounding box
	 * <li>[typical out] x - the X coordinate of the top left corner of the bounding box of the referenced substring
	 * <li>[typical out] y - the Y coordinate of the top left corner of the bounding box of the referenced substring
	 * <li>[typical out] width - the width of the bounding box of the referenced substring
	 * <li>[typical out] height - the height of the bounding box of the referenced substring
	 * <li>[optional out] rectangles - a set of disjoint bounding rectangles, if the substring is clipped
	 * </ul>
	 */
	public void getTextBounds(AccessibleTextExtendedEvent e) {}

	/**
	 * Returns the visible text range(s).
	 * <p>
	 * Partially visible characters are included in the returned ranges.
	 * </p>
	 * 
	 * @param e an event object containing the following fields:<ul>
	 * <li>[typical out] start - the 0 based offset of the first character of the visible substring
	 * <li>[typical out] end - the 0 based offset after the last character of the visible substring
	 * <li>[optional out] ranges - an array of pairs specifying the start and end offsets of each range,
	 * 		if the visible text range is clipped
	 * </ul>
	 */
	public void getVisibleRanges(AccessibleTextExtendedEvent e) {}

	/**
	 * Deselects a range of text.
	 * 
	 * @param e an event object containing the following fields:<ul>
	 * <li>[in] index - the 0 based index of selection to remove
	 * </ul>
	 */
	public void removeSelection(AccessibleTextExtendedEvent e) {}

	/**
	 * Scrolls a specific part of a substring according to the scroll type.
	 * 
	 * @param e an event object containing the following fields:<ul>
	 * <li>[in] start - the 0 based offset of the first character of the substring
	 * <li>[in] end - the 0 based offset after the last character of the substring
	 * <li>[in] type - a scroll type indicating where the substring should be placed
	 * 		on the screen. One of:<ul>
	 * 		<li>SCROLL_TYPE_TOP_LEFT
	 * 		<li>SCROLL_TYPE_BOTTOM_RIGHT
	 * 		<li>SCROLL_TYPE_TOP_EDGE
	 * 		<li>SCROLL_TYPE_BOTTOM_EDGE
	 * 		<li>SCROLL_TYPE_LEFT_EDGE
	 * 		<li>SCROLL_TYPE_RIGHT_EDGE
	 * 		<li>SCROLL_TYPE_ANYWHERE
	 * 		<li>SCROLL_TYPE_POINT
	 * 		</ul>
	 * </ul>
	 * <li>[optional in] x - for SCROLL_TYPE_POINT, the x coordinate of the destination point in display coordinates
	 * <li>[optional in] y - for SCROLL_TYPE_POINT, the y coordinate of the destination point in display coordinates
	 */
	public void scrollText(AccessibleTextExtendedEvent e) {}

	/**
	 * Sets the position of the caret.
	 * <p>
	 * The caret position is that of the character logically following it,
	 * e.g. to the right of it in a left to right language.
	 * The caret is actually placed to the leading side of the character with
	 * that offset. An offset of 0 places the caret so that the next insertion
	 * goes before the first character. An offset of getCharacterCount places
	 * the caret so that the next insertion goes after the last character.
	 * </p><p>
	 * Setting the caret position may or may not alter the current selection.  A
	 * change of the selection is notified to the accessibility event listeners with
	 * an EVENT_TEXT_SELECTION_CHANGED event.
	 * </p><p>
	 * When the new caret position differs from the old one, this is notified to the
	 * accessibility event listeners with an EVENT_TEXT_CARET_MOVED event.
	 * </p>
	 * 
	 * @param e an event object containing the following fields:<ul>
	 * <li>[in] offset - the new offset of the caret
	 * </ul>
	 */
	public void setCaretOffset(AccessibleTextExtendedEvent e) {}

	/**
	 * Changes the bounds of an existing selection.
	 * 
	 * @param e an event object containing the following fields:<ul>
	 * <li>[in] index - the 0 based index of the selection to change
	 * <li>[in] start - the new 0 based offset of the first character of the selection
	 * <li>[in] end - the new 0 based offset after the last character of the selection
	 * </ul>
	 */
	public void setSelection(AccessibleTextExtendedEvent e) {}
}

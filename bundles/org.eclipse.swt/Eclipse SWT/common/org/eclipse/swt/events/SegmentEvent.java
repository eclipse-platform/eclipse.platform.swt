/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.events;

import org.eclipse.swt.widgets.Event;

/**
 * This event is sent to SegmentListeners when a text content is to be modified.
 * The segments field can be used in conjunction with the segmentsChars field or
 * by itself. Setting only the segmentsChars field has no effect. When used by
 * itself, the segments field specify text ranges that should be treated as
 * separate segments.
 * <p>
 * The elements in the segments field specify the start offset of a segment
 * relative to the start of the text. They must follow the following rules:
 * <ul>
 * <li>first element must be 0
 * <li>elements must be in ascending order and must not have duplicates
 * <li>elements must not exceed the text length
 * </ul>
 * In addition, the last element may be set to the end of the text but this is
 * not required.
 *
 * The segments field may be left null if the entire text content doesn't
 * require segmentation.
 * </p>
 * A SegmentListener may be used when adjacent segments of right-to-left text
 * should not be reordered relative to each other. For example, within a Java
 * editor, you may wish multiple right-to-left string literals to be reordered
 * differently than the bidi algorithm specifies.  
 *
 * Example:
 * <pre>
 * 	stored text = "R1R2R3" + "R4R5R6"
 *		R1 to R6 are right-to-left characters. The quotation marks
 * 		are part of the text. The text is 13 characters long.
 * 
 * 	segments = null: 
 * 		entire text content will be reordered and thus the two R2L segments 
 * 		swapped (as per the bidi algorithm). 
 *		visual display (rendered on screen) = "R6R5R4" + "R3R2R1"
 * 
 * 	segments = [0, 5, 8]	
 * 		"R1R2R3" will be reordered, followed by [blank]+[blank] and 
 * 		"R4R5R6". 
 *		visual display = "R3R2R1" + "R6R5R4"
 * </pre>
 * 
 * <p>
 * The segments and segementsChars fields can be used together to obtain different
 * types of bidi reordering and text display. The application can use these two fields
 * to insert Unicode Control Characters in specific offsets in the text, the character
 * at segmentsChars[i] is inserted at the offset specified by segments[i]. When both fields 
 * are set, the rules for the segments field are less restrictive:
 * <ul>
 * <li>elements must be in ascending order, duplicates are allowed
 * <li>elements must not exceed the text length
 * </ul>
 * </p>
 *
 * @since 3.8
 */

public class SegmentEvent extends TypedEvent {
	/**
	 * Text with respect to which segments are calculated
	 */
	public String text;

	/**
	 * Text ranges that should be treated as separate segments (e.g. for bidi
	 * reordering)
	 */
	public int[] segments;

	/**
	 * Characters to be used in the segment boundaries (optional) 
	 */
	public char[] segmentsChars;

	private static final long serialVersionUID = -89306787890624323L;

	public SegmentEvent(Event e) {
		super(e);
		text = e.text;
	}
}

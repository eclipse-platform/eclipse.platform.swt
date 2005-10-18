/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.custom;

import org.eclipse.swt.events.*;

/**
 * This event is sent when a line is about to be drawn.
 */
public class LineStyleEvent extends TypedEvent {
	
	/**
	 * line start offset
	 */
	public int lineOffset;
	
	/**
	 * line text
	 */
	public String lineText;
	
	/**
	 * line styles
	 */
	public StyleRange[] styles;

	/* API UNDER CONSTRUCTION - DO NOT USE THIS FIELD
	 *  
	 * line alignment (output)
	 */
	public int alignment;

	/* API UNDER CONSTRUCTION - DO NOT USE THIS FIELD
	 *  
	 * line indent (output)
	 */
	public int indent;

	/* API UNDER CONSTRUCTION - DO NOT USE THIS FIELD
	 *  
	 * line justification (output)
	 */
	public boolean justify;
	
	static final long serialVersionUID = 3906081274027192884L;
	
public LineStyleEvent(StyledTextEvent e) {
	super(e);
	lineOffset = e.detail;
	lineText = e.text;
	styles = e.styles;
	alignment = -1;
	indent = -1;
}
}

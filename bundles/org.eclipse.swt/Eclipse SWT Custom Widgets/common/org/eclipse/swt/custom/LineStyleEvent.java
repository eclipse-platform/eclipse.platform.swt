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
	public int lineOffset;			// line start offset 
	public String lineText;			// line text
	public StyleRange[] styles;		// array of StyleRanges

	static final long serialVersionUID = 3906081274027192884L;
	
public LineStyleEvent(StyledTextEvent e) {
	super(e);
	lineOffset = e.detail;
	lineText = e.text;
	styles = e.styles;
}
}

package org.eclipse.swt.custom;
/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

/**
 * This event is sent when a line is about to be drawn.
 */
public class LineStyleEvent extends TypedEvent {
	public int lineOffset;			// line start offset 
	public String lineText;			// line text
	public StyleRange[] styles;		// array of StyleRanges

public LineStyleEvent(StyledTextEvent e) {
	super(e);
	lineOffset = e.detail;
	lineText = e.text;
	styles = e.styles;
}
}

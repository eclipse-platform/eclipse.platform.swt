package org.eclipse.swt.custom;
/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 *
 */ 
class StyledTextEvent extends Event {
	// used by LineStyleEvent
	StyleRange[] styles;		
	// used by LineBackgroundEvent
	Color lineBackground;
	// used by BidiSegmentEvent
	int[] segments;	
	// used by TextChangedEvent
	int replaceCharCount; 	
	int newCharCount; 
	int replaceLineCount;
	int newLineCount; 

StyledTextEvent (StyledTextContent content) {
	super();
	data = content;	
}
}



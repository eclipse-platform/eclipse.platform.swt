package org.eclipse.swt.custom;
/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
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



/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.custom;

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



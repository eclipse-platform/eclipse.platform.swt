/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.custom;

import org.eclipse.swt.internal.SWTEventListener;

/**
 * This listener interface may be implemented in order to receive
 * BidiSegmentEvents.
 * @see BidiSegmentEvent
 */ 
public interface BidiSegmentListener extends SWTEventListener {

/**
 * This method is called when a line needs to be reordered for 
 * measuring or rendering in a bidi locale. 
 * <p>
 *
 * @param event.lineOffset line start offset (input)	
 * @param event.lineText line text (input)
 * @param event.segments text segments that should be reordered 
 *	separately. (output)
 * @see BidiSegmentEvent
 */
public void lineGetSegments(BidiSegmentEvent event);

}


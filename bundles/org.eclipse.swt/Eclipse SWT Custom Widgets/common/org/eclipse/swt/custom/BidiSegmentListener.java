package org.eclipse.swt.custom;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
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


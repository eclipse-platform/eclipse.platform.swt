package org.eclipse.swt.custom;
/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import java.util.*;

/**
 * The StyledText widget implements this listener to receive
 * notifications when changes to the model occur.
 * It is not intended for use by users of the StyledText widget
 * or implementors of StyledTextContent. Users should listen to
 * the ModifyEvent or ExtendedModifyEvent that is sent by the StyledText 
 * widget to receive text change notifications.
 */
public interface TextChangeListener extends EventListener {

/**
 * @param event.start replace start offset (input)	
 * @param event.newText text that is going to be inserted or empty String
 *	if no text will be inserted (input)
 * @param event.replaceCharCount length of text that is going to be 
 *	replaced (input) 
 * @param event.newCharCount length of text that is going to be inserted
 *	(input)
 * @param event.replaceLineCount number of lines that are going to be 
 *	replaced (input)
 * @param event.newLineCount number of new lines that are going to be 
 *	inserted (input)
 */
public void textChanging(TextChangingEvent event);

/**
 */
public void textChanged(TextChangedEvent event);

/**
 */
public void textSet(TextChangedEvent event);
}



package org.eclipse.swt.custom;
/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp 2000, 2001
 */

/* Imports */
import java.util.*;

/**
 * The StyledText widget implements this listener to receive
 * notification when changes to the model occur.
 * It is not intended for use by users of the StyledText widget
 * or implementors of StyledTextContent. Users should listen to
 * the ModifyEvent or ExtendedModifyEvent that is sent by the StyledText 
 * widget to receive text change notifications.
 */

public interface TextChangedListener extends EventListener {
/**
 * @param event.start replace start offset (input)	
 * @param event.replacedText text being replaced (input)
 * @param event.replacedCharCount length of text being replaced (input)
 * @param event.newCharCount length of new text (input)
 * @param event.replacedLineCount number of lines replaced (input)
 * @param event.newLineCount number of new lines (input)
 */
public void textReplaced(TextChangedEvent event);

/**
 * @param event.start replace start offset (input)	
 * @param event.replacedText text being replaced (input)
 * @param event.replacedCharCount length of text being replaced (input)
 * @param event.newCharCount length of new text (input)
 * @param event.replacedLineCount number of lines replaced (input)
 * @param event.newLineCount number of new lines (input)
 */
public void textSet(TextChangedEvent event);
}



package org.eclipse.swt.custom;
/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import java.util.EventListener;

public interface ExtendedModifyListener extends EventListener {
/**
 * This method is called after a text change occurs.
 * <p>
 *
 * @param event.start the start offset of the new text (input)
 * @param event.length the length of the new text (input)
 * @param event.replacedText the replaced text (input)
 */
public void modifyText(ExtendedModifyEvent event);
}



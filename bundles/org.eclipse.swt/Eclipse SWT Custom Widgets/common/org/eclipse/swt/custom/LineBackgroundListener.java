package org.eclipse.swt.custom;
/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.SWTEventListener;

public interface LineBackgroundListener extends SWTEventListener {
	
/**
 * This method is called when a line is about to be drawn in order to get its
 * background color.
 * <p>
 *
 * @param event.lineOffset line start offset (input)	
 * @param event.lineText line text (input)
 * @param event.lineBackground line background color (output)
 */
public void lineGetBackground(LineBackgroundEvent event);
}

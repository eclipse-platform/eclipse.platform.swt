package org.eclipse.swt.custom;
/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp 2000, 2001
 */


/* Imports */
import java.util.*;


public interface LineBackgroundListener extends EventListener {
	
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
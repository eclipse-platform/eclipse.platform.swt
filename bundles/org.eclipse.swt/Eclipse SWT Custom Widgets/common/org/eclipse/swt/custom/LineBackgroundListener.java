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

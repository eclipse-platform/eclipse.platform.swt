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

import org.eclipse.swt.events.*;
import org.eclipse.swt.internal.SWTEventListener;

public interface VerifyKeyListener extends SWTEventListener {
/**
 * @param event.character the character that was typed (input)	
 * @param event.keyCode the key code that was typed (input)
 * @param event.stateMask the state of the keyboard (input)
 * @param event.doit processed or not (output)
 */
public void verifyKey (VerifyEvent event);
}

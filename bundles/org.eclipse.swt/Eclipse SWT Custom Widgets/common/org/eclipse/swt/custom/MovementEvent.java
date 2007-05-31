/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.custom;

import org.eclipse.swt.events.*;

/**
 * This event is sent when a new offset is required based on the current
 * offset and a movement type.
 * 
 * @since 3.3
 */
public class MovementEvent extends TypedEvent {
	
	/**
	 * line start offset (input)
	 */
	public int lineOffset;
	
	/**
	 * line text (input)
	 */
	public String lineText;
	
	/**
	 * the current offset (input)
	 */
	public int offset;
	
	/**
	 * the new offset  (input, output)
	 */
	public int newOffset;
	
	/**
	 * the movement type (input)
	 * 
	 * @see org.eclipse.swt.SWT#MOVEMENT_WORD
	 * @see org.eclipse.swt.SWT#MOVEMENT_WORD_END 
	 * @see org.eclipse.swt.SWT#MOVEMENT_WORD_START
	 * @see org.eclipse.swt.SWT#MOVEMENT_CHAR
	 * @see org.eclipse.swt.SWT#MOVEMENT_CLUSTER
	 */
	public int movement;
	
	static final long serialVersionUID = 3978765487853324342L;
	
public MovementEvent(StyledTextEvent e) {
	super(e);
	lineOffset = e.detail;
	lineText = e.text;
	movement = e.count;
	offset = e.start;
	newOffset = e.end;
}
}



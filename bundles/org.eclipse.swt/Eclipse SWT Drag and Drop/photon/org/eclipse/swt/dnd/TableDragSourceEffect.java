/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;

import org.eclipse.swt.widgets.*;

/**
 * This class provides default implementations to display a source image
 * when a drag is initiated from a <code>Table</code>.
 * 
 * <p>Classes that wish to provide their own source image for a <code>Table</code> can
 * extend the <code>TableDragSourceEffect</code> class, override the 
 * <code>TableDragSourceEffect.dragStart</code> method and set the field 
 * <code>DragSourceEvent.image</code> with their own image.</p>
 *
 * Subclasses that override any methods of this class must call the corresponding
 * <code>super</code> method to get the default drag source effect implementation.
 *
 * @see DragSourceEffect
 * @see DragSourceEvent
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * 
 * @since 3.3
 */
public class TableDragSourceEffect extends DragSourceEffect {
	/**
	 * Creates a new <code>TableDragSourceEffect</code> to handle drag effect 
	 * from the specified <code>Table</code>.
	 *
	 * @param table the <code>Table</code> that the user clicks on to initiate the drag
	 */
	public TableDragSourceEffect(Table table) {
		super(table);
	}
}
